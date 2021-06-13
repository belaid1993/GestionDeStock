package com.belaid.gestionDeStock.services;

import com.belaid.gestionDeStock.dto.CommandeFournisseurDto;
import com.belaid.gestionDeStock.dto.LigneCommandeFournisseurDto;
import com.belaid.gestionDeStock.model.EtatCommande;

import java.math.BigDecimal;
import java.util.List;

public interface CommandeFournisseurService {

    CommandeFournisseurDto save(CommandeFournisseurDto dto);

    CommandeFournisseurDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande);

    CommandeFournisseurDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite);

    CommandeFournisseurDto updateFournisseur(Integer idCommande, Integer idFournisseur);

    CommandeFournisseurDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer newIdArticle);

    CommandeFournisseurDto deleteArticle(Integer idCommande, Integer idLigneCommande);

    List<LigneCommandeFournisseurDto> findAllLignesCommandesFournisseurByCommandeFournisseurId(Integer idCommande);


    CommandeFournisseurDto findById(Integer id);

    CommandeFournisseurDto findByCode(String code);

    List<CommandeFournisseurDto> findAll();

    void delete(Integer id);
}
