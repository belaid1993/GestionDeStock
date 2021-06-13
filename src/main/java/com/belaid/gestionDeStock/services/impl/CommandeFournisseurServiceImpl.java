package com.belaid.gestionDeStock.services.impl;

import com.belaid.gestionDeStock.dto.ArticleDto;
import com.belaid.gestionDeStock.dto.CommandeFournisseurDto;
import com.belaid.gestionDeStock.dto.FournisseurDto;
import com.belaid.gestionDeStock.dto.LigneCommandeFournisseurDto;
import com.belaid.gestionDeStock.exception.EntityNotFoundException;
import com.belaid.gestionDeStock.exception.ErrorCodes;
import com.belaid.gestionDeStock.exception.InvalidEntityException;
import com.belaid.gestionDeStock.exception.InvalidOperationException;
import com.belaid.gestionDeStock.model.*;
import com.belaid.gestionDeStock.repository.ArticleRepository;
import com.belaid.gestionDeStock.repository.CommandeFournisseurRepository;
import com.belaid.gestionDeStock.repository.FournisseurRepository;
import com.belaid.gestionDeStock.repository.LigneCommandeFournisseurRepository;
import com.belaid.gestionDeStock.services.CommandeFournisseurService;
import com.belaid.gestionDeStock.validator.ArticleValidator;
import com.belaid.gestionDeStock.validator.CommandeFournisseurValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
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
    public CommandeFournisseurDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande) {
        checkIdCommand(idCommande);

        if (!StringUtils.hasLength(String.valueOf(etatCommande))) {
            log.error("L'etat commande fournisseur is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de commande avec un etat NULL", ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }

        CommandeFournisseurDto commandeFournisseur = checkEtatCommande(idCommande);

        commandeFournisseur.setEtatCommande(etatCommande);

        return CommandeFournisseurDto.fromEntity(commandeFournisseurRepository.save(CommandeFournisseurDto.toEntity(commandeFournisseur)));

    }

    @Override
    public CommandeFournisseurDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite) {
        checkIdCommand(idCommande);

        checkIdLigneCommande(idLigneCommande);

        if (quantite == null || quantite.compareTo(BigDecimal.ZERO) == 0) {
            log.error("L'ID Ligne commande fournisseur is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de commande avec une quantité NULL ou Zero", ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }

        CommandeFournisseurDto commandeFournisseur = checkEtatCommande(idCommande);

        Optional<LigneCommandeFournisseur> ligneCommandeFournisseurOptional = findLigneCommandeFournisseur(idLigneCommande);

        LigneCommandeFournisseur ligneCommandeFournisseur = ligneCommandeFournisseurOptional.get();

        ligneCommandeFournisseur.setQuantite(quantite);

        ligneCommandeFournisseurRepository.save(ligneCommandeFournisseur);

        return commandeFournisseur;
    }

    @Override
    public CommandeFournisseurDto updateFournisseur(Integer idCommande, Integer idFournisseur) {
        checkIdCommand(idCommande);

        if (idFournisseur == null) {
            log.error("L'ID Ligne commande fournisseur is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de commande avec un ID de fournisseur NULL", ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }

        CommandeFournisseurDto commandeFournisseur = checkEtatCommande(idCommande);

        Optional<Fournisseur> fournisseurOptional = fournisseurRepository.findById(idFournisseur);

        if (fournisseurOptional.isEmpty()) {
            throw new EntityNotFoundException("Aucun fournisseur n'a ete trouver avec l'ID " + idFournisseur, ErrorCodes.FOURNISSEUR_NOT_FOUND);

        }

        commandeFournisseur.setFournisseur(FournisseurDto.fromEntity(fournisseurOptional.get()));

        return CommandeFournisseurDto.fromEntity(
                commandeFournisseurRepository.save(CommandeFournisseurDto.toEntity(commandeFournisseur))
        );
    }

    @Override
    public CommandeFournisseurDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer idArticle) {
        checkIdCommand(idCommande);
        checkIdLigneCommande(idLigneCommande);
        checkIdArticle(idArticle, "nouvel");
        CommandeFournisseurDto commandeFournisseur = checkEtatCommande(idCommande);
        Optional<LigneCommandeFournisseur> ligneCommandeFournisseurOptional = findLigneCommandeFournisseur(idLigneCommande);

        Optional<Article> articleOptional = articleRepository.findById(idArticle);
        if (articleOptional.isEmpty()) {
            throw new EntityNotFoundException("Aucun article n'a ete trouver avec l'ID " + idArticle, ErrorCodes.ARTICLE_NOT_FOUND);
        }

        List<String> errors = ArticleValidator.validate(ArticleDto.fromEntity(articleOptional.get()));
        if (!errors.isEmpty()) {
            throw new InvalidEntityException("Article invalid", ErrorCodes.ARTICLE_NOT_VALID, errors);
        }

        LigneCommandeFournisseur ligneCommandeFournisseurToSave = ligneCommandeFournisseurOptional.get();
        ligneCommandeFournisseurToSave.setArticle(articleOptional.get());
        ligneCommandeFournisseurRepository.save(ligneCommandeFournisseurToSave);

        return commandeFournisseur;
    }

    @Override
    public CommandeFournisseurDto deleteArticle(Integer idCommande, Integer idLigneCommande) {
        checkIdCommand(idCommande);
        checkIdLigneCommande(idLigneCommande);

        CommandeFournisseurDto commandeFournisseur = checkEtatCommande(idCommande);
        Optional<LigneCommandeFournisseur> ligneCommandeFournisseurOptional = findLigneCommandeFournisseur(idLigneCommande);

        ligneCommandeFournisseurRepository.deleteById(idLigneCommande);

        return commandeFournisseur;
    }

    @Override
    public List<LigneCommandeFournisseurDto> findAllLignesCommandesFournisseurByCommandeFournisseurId(Integer idCommande) {
        return ligneCommandeFournisseurRepository.findAllByCommandeFournisseurId(idCommande).stream()
                .map(LigneCommandeFournisseurDto::fromEntity)
                .collect(Collectors.toList());
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

    private void checkIdLigneCommande(Integer idLigneCommande) {
        if (idLigneCommande == null) {
            log.error("L'ID Ligne commande fournisseur is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de commande avec un ID de Ligne de commande NULL", ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }
    }

    private void checkIdCommand(Integer idCommande) {
        if (idCommande == null) {
            log.error("commande fournisseur ID est NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de commande avec un ID NULL", ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }
    }

    private void checkIdArticle(Integer idArticle, String msg) {
        if (idArticle == null) {
            log.error("L'ID " + msg + " Article is NULL");
            throw new InvalidOperationException("Impossible de modifier un " + msg + " article avec un ID NULL", ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }
    }

    private CommandeFournisseurDto checkEtatCommande(Integer idCommande) {
        CommandeFournisseurDto commandeFournisseur = findById(idCommande);

        if (commandeFournisseur.isCommandeLivree()) {
            throw new InvalidOperationException("Impossible de modifier la commande lorsqu'elle est livree", ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }
        return commandeFournisseur;
    }

    private Optional<LigneCommandeFournisseur> findLigneCommandeFournisseur(Integer idLigneCommande) {
        Optional<LigneCommandeFournisseur> ligneCommandeFournisseurOptional = ligneCommandeFournisseurRepository.findById(idLigneCommande);

        if (ligneCommandeFournisseurOptional.isEmpty()) {
            throw new EntityNotFoundException("Aucun ligne commande fournisseur n'a ete trouver avec l'ID " + idLigneCommande, ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND);
        }
        return ligneCommandeFournisseurOptional;
    }
}
