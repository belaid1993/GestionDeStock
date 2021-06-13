package com.belaid.gestionDeStock.services.impl;

import com.belaid.gestionDeStock.dto.ArticleDto;
import com.belaid.gestionDeStock.dto.LigneCommandeClientDto;
import com.belaid.gestionDeStock.dto.LigneCommandeFournisseurDto;
import com.belaid.gestionDeStock.dto.LigneVenteDto;
import com.belaid.gestionDeStock.exception.EntityNotFoundException;
import com.belaid.gestionDeStock.exception.ErrorCodes;
import com.belaid.gestionDeStock.exception.InvalidEntityException;
import com.belaid.gestionDeStock.model.Article;
import com.belaid.gestionDeStock.repository.ArticleRepository;
import com.belaid.gestionDeStock.repository.LigneCommandeClientRepository;
import com.belaid.gestionDeStock.repository.LigneCommandeFournisseurRepository;
import com.belaid.gestionDeStock.repository.LigneVenteRepository;
import com.belaid.gestionDeStock.services.ArticleService;
import com.belaid.gestionDeStock.validator.ArticleValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    private ArticleRepository articleRepository;
    private LigneVenteRepository venteRepository;
    private LigneCommandeFournisseurRepository commandeFournisseurRepository;
    private LigneCommandeClientRepository commandeClientRepository;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository, LigneVenteRepository venteRepository, LigneCommandeFournisseurRepository commandeFournisseurRepository, LigneCommandeClientRepository commandeClientRepository) {
        this.articleRepository = articleRepository;
        this.venteRepository = venteRepository;
        this.commandeFournisseurRepository = commandeFournisseurRepository;
        this.commandeClientRepository = commandeClientRepository;
    }

    @Override
    public ArticleDto save(ArticleDto dto) {

        List<String> errors = ArticleValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Article n'est pas valide {}", dto);
            throw new InvalidEntityException("L'article n'est pas valide", ErrorCodes.ARTICLE_NOT_VALID, errors);
        }
        Article savedArticle = articleRepository.save(ArticleDto.toEntity(dto));
        return ArticleDto.fromEntity(savedArticle);
    }

    @Override
    public ArticleDto findById(Integer id) {
        if (id == null) {
            log.error("Article ID est null");
            return null;
        }

        Optional<Article> article = articleRepository.findById(id);

        ArticleDto dto = ArticleDto.fromEntity(article.get());

        return Optional.of(dto).orElseThrow(() -> new EntityNotFoundException("Aucun article avec ID " + id + " n'ete trouver dans BDD",
                ErrorCodes.ARTICLE_NOT_FOUND));
    }

    @Override
    public ArticleDto findByCodeArticle(String codeArticle) {
        if (!StringUtils.hasLength(codeArticle)) {
            log.error("Article code est null");
            return null;
        }

        Optional<Article> article = articleRepository.findArticleByCodeArticle(codeArticle);

        ArticleDto dto = ArticleDto.fromEntity(article.get());

        return Optional.of(dto).orElseThrow(() -> new EntityNotFoundException("Aucun article avec code " + codeArticle + " n'ete trouver dans BDD"
                , ErrorCodes.ARTICLE_NOT_FOUND));
    }

    @Override
    public List<ArticleDto> findAll() {
        return articleRepository.findAll().stream()
                .map(ArticleDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<LigneVenteDto> findHistoriqueVentes(Integer idArticle) {
        return venteRepository.findAllByArticleId(idArticle).stream()
                .map(LigneVenteDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<LigneCommandeClientDto> findHistoriqueCommandeClient(Integer idArticle) {
        return commandeClientRepository.findAllByCommandeClientId(idArticle).stream()
                .map(LigneCommandeClientDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<LigneCommandeFournisseurDto> findHistoriqueCommandeFournisseur(Integer idArticle) {
        return commandeFournisseurRepository.findAllByCommandeFournisseurId(idArticle).stream()
                .map(LigneCommandeFournisseurDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleDto> findAllArticleByIdCategory(Integer idCategory) {
        return articleRepository.findAllByCategoryId(idCategory).stream()
                .map(ArticleDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Article ID est null");
            return;
        }
        articleRepository.deleteById(id);

    }
}
