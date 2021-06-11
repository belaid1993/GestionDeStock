package com.belaid.gestionDeStock.services.impl;

import com.belaid.gestionDeStock.dto.CommandeClientDto;
import com.belaid.gestionDeStock.exception.EntityNotFoundException;
import com.belaid.gestionDeStock.exception.ErrorCodes;
import com.belaid.gestionDeStock.exception.InvalidEntityException;
import com.belaid.gestionDeStock.exception.InvalidOperationException;
import com.belaid.gestionDeStock.model.*;
import com.belaid.gestionDeStock.repository.ArticleRepository;
import com.belaid.gestionDeStock.repository.ClientRepository;
import com.belaid.gestionDeStock.repository.CommandeClientRepository;
import com.belaid.gestionDeStock.repository.LigneCommandeClientRepository;
import com.belaid.gestionDeStock.services.CommandeClientService;
import com.belaid.gestionDeStock.validator.CommandeClientValidator;
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
public class CommandeClientServiceImpl implements CommandeClientService {

    private CommandeClientRepository commandeClientRepository;
    private LigneCommandeClientRepository ligneCommandeClientRepository;
    private ClientRepository clientRepository;
    private ArticleRepository articleRepository;

    @Autowired
    public CommandeClientServiceImpl(CommandeClientRepository commandeClientRepository, LigneCommandeClientRepository ligneCommandeClientRepository, ClientRepository clientRepository, ArticleRepository articleRepository) {
        this.commandeClientRepository = commandeClientRepository;
        this.ligneCommandeClientRepository = ligneCommandeClientRepository;
        this.clientRepository = clientRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public CommandeClientDto save(CommandeClientDto dto) {
        List<String> errors = CommandeClientValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Commande client n'est pas valid");
            throw new InvalidEntityException("la Commande client n'est pas valid", ErrorCodes.COMMANDE_CLIENT_NOT_VALID, errors);
        }

        if (dto.getId() != null && dto.isCommandeLivree()) {
            throw new InvalidOperationException("Impossible de modifier la commande lorsqu'elle est livree", ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }

        Optional<Client> client = clientRepository.findById(dto.getClient().getId());

        if (client.isEmpty()) {
            log.warn("clinet ID {} n'est pas existe dand BDD", dto.getClient().getId());
            throw new EntityNotFoundException("Aucun clinet avec ID " + dto.getClient().getId() + " n'a ete trouver !!", ErrorCodes.CLIENT_NOT_FOUND);
        }

        List<String> articleErrors = new ArrayList<>();

        if (dto.getLigneCommandeClients() != null) {
            dto.getLigneCommandeClients().forEach(ligCmdClt -> {
                if (ligCmdClt.getArticle() != null) {
                    Optional<Article> article = articleRepository.findById(ligCmdClt.getArticle().getId());
                    if (article.isEmpty()) {
                        articleErrors.add("l'article avec l'ID " + ligCmdClt.getArticle().getId() + " n'existe pas");
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

        CommandeClient savedCmdClt = commandeClientRepository.save(CommandeClientDto.toEntity(dto));

        if (dto.getLigneCommandeClients() != null) {
            dto.getLigneCommandeClients().forEach(ligCmdClt -> {
                LigneCommandeClient ligneCommandeClient = ligCmdClt.toEntity(ligCmdClt);
                ligneCommandeClient.setCommandeClient(savedCmdClt);
                ligneCommandeClientRepository.save(ligneCommandeClient);
            });
        }

        return CommandeClientDto.fromEntity(savedCmdClt);
    }

    @Override
    public CommandeClientDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande) {
        if (idCommande == null) {
            log.error("commande client ID est NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de commande avec un ID NULL", ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }

        if (!StringUtils.hasLength(String.valueOf(etatCommande))) {
            log.error("L'etat commande client is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de commande avec un etat NULL", ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }

        CommandeClientDto commandeClient = findById(idCommande);

        if (commandeClient.isCommandeLivree()) {
            throw new InvalidOperationException("Impossible de modifier la commande lorsqu'elle est livree", ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }

        commandeClient.setEtatCommande(etatCommande);

        return CommandeClientDto.fromEntity(commandeClientRepository.save(CommandeClientDto.toEntity(commandeClient)));
    }

    @Override
    public CommandeClientDto findById(Integer id) {
        if (id == null) {
            log.error("commande client ID est NULL");
            return null;
        }
        return commandeClientRepository.findById(id)
                .map(CommandeClientDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("Aucune commande client n'a ete trouver avec l'ID " + id, ErrorCodes.COMMANDE_CLIENT_NOT_FOUND));
    }

    @Override
    public CommandeClientDto findByCode(String code) {
        if (!StringUtils.hasLength(code)) {
            log.error("commande client CODE est NULL");
            return null;
        }
        return commandeClientRepository.findCommandeClientByCode(code)
                .map(CommandeClientDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("Aucune commande client n'a ete trouver avec le CODE " + code, ErrorCodes.COMMANDE_CLIENT_NOT_FOUND));
    }

    @Override
    public List<CommandeClientDto> findAll() {
        return commandeClientRepository.findAll()
                .stream().map(CommandeClientDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("commande client ID est NULL");
            return;
        }
        commandeClientRepository.deleteById(id);

    }
}
