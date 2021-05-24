package com.belaid.gestionDeStock.services.impl;

import com.belaid.gestionDeStock.dto.CommandeFournisseurDto;
import com.belaid.gestionDeStock.exception.EntityNotFoundException;
import com.belaid.gestionDeStock.exception.ErrorCodes;
import com.belaid.gestionDeStock.exception.InvalidEntityException;
import com.belaid.gestionDeStock.model.Article;
import com.belaid.gestionDeStock.model.CommandeFournisseur;
import com.belaid.gestionDeStock.model.Fournisseur;
import com.belaid.gestionDeStock.model.LigneCommandeFournisseur;
import com.belaid.gestionDeStock.repository.ArticleRepository;
import com.belaid.gestionDeStock.repository.CommandeFournisseurRepository;
import com.belaid.gestionDeStock.repository.FournisseurRepository;
import com.belaid.gestionDeStock.repository.LigneCommandeFournisseurRepository;
import com.belaid.gestionDeStock.services.CommandeFournisseurService;
import com.belaid.gestionDeStock.validator.CommandeFournisseurValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommandeFournisseurServiceImpl implements CommandeFournisseurService {

    private CommandeFournisseurRepository commandeFournisseurRepository;
    private LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository;
    private FournisseurRepository fournisseurRepository;
    private ArticleRepository articleRepository;

    @Autowired
    public CommandeFournisseurServiceImpl(CommandeFournisseurRepository commandeFournisseurRepository, LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository, FournisseurRepository fournisseurRepository, ArticleRepository articleRepository) {
        this.commandeFournisseurRepository = commandeFournisseurRepository;
        this.ligneCommandeFournisseurRepository = ligneCommandeFournisseurRepository;
        this.fournisseurRepository = fournisseurRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public CommandeFournisseurDto save(CommandeFournisseurDto dto) {
        List<String> errors = CommandeFournisseurValidator.validate(dto);

        if (!errors.isEmpty()) {
            log.error("Commande fournisseur n'est pas valid");
            throw new InvalidEntityException("la Commande fournisseur n'est pas valid", ErrorCodes.COMMANDE_FOURNISSEUR_NOT_VALID, errors);
        }

        Optional<Fournisseur> fournisseur = fournisseurRepository.findById(dto.getFournisseur().getId());

        if (fournisseur.isEmpty()) {
            log.warn("fournisseur ID {} n'est pas existe dand BDD", dto.getFournisseur().getId());
            throw new EntityNotFoundException("Aucun fournisseur avec ID " + dto.getFournisseur().getId() + " n'a ete trouver !!", ErrorCodes.FOURNISSEUR_NOT_FOUND);
        }

        List<String> articleErrors = new ArrayList<>();

        if (dto.getLigneCommandeFournisseurs() != null) {
            dto.getLigneCommandeFournisseurs().stream().forEach(ligCmdFrs -> {
                if (ligCmdFrs.getArticle() != null) {
                    Optional<Article> article = articleRepository.findById(ligCmdFrs.getArticle().getId());
                    if (article.isEmpty()) {
                        articleErrors.add("l'article avec l'ID " + ligCmdFrs.getArticle().getId() + " n'existe pas");
                    }
                } else {
                    articleErrors.add("impossible d'enregistrer une commande avec un article NULL");
                }
            });
        }

        if (!articleErrors.isEmpty()) {
            log.warn("");
            throw new InvalidEntityException("Article n'existe pas dans la BDD", ErrorCodes.ARTICLE_NOT_FOUND, articleErrors);
        }

        CommandeFournisseur savedCmdFrs = commandeFournisseurRepository.save(CommandeFournisseurDto.toEntity(dto));

        if (dto.getLigneCommandeFournisseurs() != null) {
            dto.getLigneCommandeFournisseurs().stream().forEach(ligCmdFrs -> {
                LigneCommandeFournisseur ligneCommandeFournisseur = ligCmdFrs.toEntity(ligCmdFrs);
                ligneCommandeFournisseur.setCommandeFournisseur(savedCmdFrs);
                ligneCommandeFournisseurRepository.save(ligneCommandeFournisseur);
            });
        }

        return CommandeFournisseurDto.fromEntity(savedCmdFrs);
    }

    @Override
    public CommandeFournisseurDto findById(Integer id) {
        if (id == null) {
            log.error("commande fournisseur ID est NULL");
            return null;
        }
        return commandeFournisseurRepository.findById(id)
                .map(CommandeFournisseurDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("Aucune commande fournisseur n'a ete trouver avec l'ID " + id, ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND));
    }

    @Override
    public CommandeFournisseurDto findByCode(String code) {
        if (!StringUtils.hasLength(code)) {
            log.error("commande fournisseur CODE est NULL");
            return null;
        }
        return commandeFournisseurRepository.findCommandeFournisseurByCode(code)
                .map(CommandeFournisseurDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("Aucune commande fournisseur n'a ete trouver avec le CODE " + code, ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND));
    }

    @Override
    public List<CommandeFournisseurDto> findAll() {
        return commandeFournisseurRepository.findAll().stream()
                .map(CommandeFournisseurDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("commande fournisseur ID est NULL");
            return;
        }
        commandeFournisseurRepository.deleteById(id);
    }
}
