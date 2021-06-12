package com.belaid.gestionDeStock.services;

import com.belaid.gestionDeStock.dto.CommandeClientDto;
import com.belaid.gestionDeStock.dto.LigneCommandeClientDto;
import com.belaid.gestionDeStock.model.EtatCommande;
import com.belaid.gestionDeStock.model.LigneCommandeClient;

import java.math.BigDecimal;
import java.util.List;

public interface CommandeClientService {

    CommandeClientDto save(CommandeClientDto dto);

    CommandeClientDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande);

    CommandeClientDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite);

    CommandeClientDto updateClient(Integer idCommande, Integer idClient);

    CommandeClientDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer newIdArticle);

    CommandeClientDto deleteArticle(Integer idCommande, Integer idLigneCommande);

    List<LigneCommandeClientDto> findAllLignesCommandesClientByCommandeClientId(Integer idCommande);

    CommandeClientDto findById(Integer id);

    CommandeClientDto findByCode(String code);

    List<CommandeClientDto> findAll();

    void delete(Integer id);
}
