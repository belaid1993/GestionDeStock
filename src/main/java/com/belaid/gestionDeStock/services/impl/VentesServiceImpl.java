package com.belaid.gestionDeStock.services.impl;

import com.belaid.gestionDeStock.dto.ArticleDto;
import com.belaid.gestionDeStock.dto.LigneVenteDto;
import com.belaid.gestionDeStock.dto.MvtStkDto;
import com.belaid.gestionDeStock.dto.VentesDto;
import com.belaid.gestionDeStock.exception.EntityNotFoundException;
import com.belaid.gestionDeStock.exception.ErrorCodes;
import com.belaid.gestionDeStock.exception.InvalidEntityException;
import com.belaid.gestionDeStock.model.*;
import com.belaid.gestionDeStock.repository.ArticleRepository;
import com.belaid.gestionDeStock.repository.LigneVenteRepository;
import com.belaid.gestionDeStock.repository.VentesRepository;
import com.belaid.gestionDeStock.services.MvtStkService;
import com.belaid.gestionDeStock.services.VentesService;
import com.belaid.gestionDeStock.validator.VentesValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VentesServiceImpl implements VentesService {

    private ArticleRepository articleRepository;
    private VentesRepository ventesRepository;
    private LigneVenteRepository ligneVenteRepository;
    private MvtStkService mvtStkService;


    @Autowired
    public VentesServiceImpl(ArticleRepository articleRepository, VentesRepository ventesRepository, LigneVenteRepository ligneVenteRepository, MvtStkService mvtStkService) {
        this.articleRepository = articleRepository;
        this.ventesRepository = ventesRepository;
        this.ligneVenteRepository = ligneVenteRepository;
        this.mvtStkService = mvtStkService;
    }

    @Override
    public VentesDto save(VentesDto dto) {
        List<String> errors = VentesValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Ventes n'est pas Valid");
            throw new InvalidEntityException("L'objet vente n'est pas valid", ErrorCodes.VENTE_NOT_VALID, errors);
        }

        List<String> articleErrors = new ArrayList<>();

        dto.getLigneVentes().forEach(ligneVenteDto -> {
            Optional<Article> article = articleRepository.findById(ligneVenteDto.getArticle().getId());
            if (article.isEmpty()) {
                articleErrors.add("Aucun Article avec l'ID " + ligneVenteDto.getArticle().getId() + " n'a ete touver dans BDD");
            }
        });

        if (!articleErrors.isEmpty()) {
            log.error("One or more articles were not found in BDD {} ", articleErrors);
            throw new InvalidEntityException("Un ou plusieurs articles n'ont pas trouver dans BDD", ErrorCodes.VENTE_NOT_VALID, articleErrors);
        }

        Ventes ventes = ventesRepository.save(VentesDto.toEntity(dto));

        dto.getLigneVentes().forEach(ligneVenteDto -> {
            LigneVente ligneVente = LigneVenteDto.toEntity(ligneVenteDto);
            ligneVente.setVente(ventes);
            ligneVenteRepository.save(ligneVente);
            updateMvtStk(ligneVente);
        });

        return VentesDto.fromEntity(ventes);
    }

    @Override
    public VentesDto findById(Integer id) {
        if (id == null) {
            log.error("Ventes ID is NULL");
            return null;
        }

        return ventesRepository.findById(id)
                .map(VentesDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("Aucun ventes n'a ete trouver avec l'ID " + id, ErrorCodes.VENTE_NOT_FOUND));
    }

    @Override
    public VentesDto findByCode(String code) {
        if (!StringUtils.hasLength(code)) {
            log.error("Ventes CODE is NULL");
            return null;
        }
        return ventesRepository.findVentesByCode(code)
                .map(VentesDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("Aucun ventes n'a ete trouver avec l'CODE " + code, ErrorCodes.VENTE_NOT_FOUND));
    }

    @Override
    public List<VentesDto> findAll() {
        return ventesRepository.findAll().stream()
                .map(VentesDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Ventes ID is NULL");
            return;
        }
        ventesRepository.deleteById(id);
    }

    private void updateMvtStk(LigneVente lig) {
        MvtStkDto mvtStkDto = MvtStkDto.builder()
                .article(ArticleDto.fromEntity(lig.getArticle()))
                .dateMvt(Instant.now())
                .typeMvt(TypeMvt.SORTIE)
                .sourceMvt(SourceMvtStk.VENTE)
                .quantite(lig.getQuantite())
                .idEntreprise(lig.getIdEntreprise())
                .build();
        mvtStkService.sortieStock(mvtStkDto);
    }
}
