package com.belaid.gestionDeStock.controller.api;

import com.belaid.gestionDeStock.dto.CommandeFournisseurDto;
import com.belaid.gestionDeStock.dto.LigneCommandeFournisseurDto;
import com.belaid.gestionDeStock.model.EtatCommande;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static com.belaid.gestionDeStock.utils.Constants.*;

@Api("commandesfournisseurs")
public interface CommandesFournisseursApi {

    @PostMapping(CREATE_COMMANDE_FOURNISSEUR_ENDPOINT)
    CommandeFournisseurDto save(@RequestBody CommandeFournisseurDto dto);

    @PatchMapping(APP_ROOT + "/commandesfournisseurs/update/etat/{idCommande}/{etatCommande}")
    ResponseEntity<CommandeFournisseurDto> updateEtatCommande(@PathVariable("idCommande") Integer idCommande, @PathVariable("etatCommande") EtatCommande etatCommande);

    @PatchMapping(APP_ROOT + "/commandesfournisseurs/update/quantite/{idCommande}/{idLigneCommande}/{quantite}")
    ResponseEntity<CommandeFournisseurDto> updateQuantiteCommande(@PathVariable("idCommande") Integer idCommande, @PathVariable("idLigneCommande") Integer idLigneCommande, @PathVariable("quantite") BigDecimal quantite);

    @PatchMapping(APP_ROOT + "/commandesfournisseurs/update/fournisseur/{idCommande}/{idFournisseur}")
    ResponseEntity<CommandeFournisseurDto> updateFournisseur(@PathVariable("idCommande") Integer idCommande, @PathVariable("idFournisseur") Integer idFournisseur);

    @PatchMapping(APP_ROOT + "/commandesfournisseurs/update/article/{idCommande}/{idLigneCommande}/{idArticle}")
    ResponseEntity<CommandeFournisseurDto> updateArticle(@PathVariable("idCommande") Integer idCommande, @PathVariable("idLigneCommande") Integer idLigneCommande, @PathVariable("idArticle") Integer idArticle);

    @DeleteMapping(APP_ROOT + "/commandesfournisseurs/delete/article/{idCommande}/{idLigneCommande}")
    ResponseEntity<CommandeFournisseurDto> deleteArticle(@PathVariable("idCommande") Integer idCommande, @PathVariable("idLigneCommande") Integer idLigneCommande);

    @GetMapping(APP_ROOT + "/commandesfournisseurs/ligneCommande/{idCommande}")
    ResponseEntity<List<LigneCommandeFournisseurDto>> findAllLignesCommandesFournisseurByCommandeFournisseurId(@PathVariable("idCommande") Integer idCommande);

    @GetMapping(FIND_COMMANDE_FOURNISSEUR_BY_ID_ENDPOINT)
    CommandeFournisseurDto findById(@PathVariable("idCommandeFournisseur") Integer id);

    @GetMapping(FIND_COMMANDE_FOURNISSEUR_BY_CODE_ENDPOINT)
    CommandeFournisseurDto findByCode(@PathVariable("codeCommandeFournisseur") String code);

    @GetMapping(FIND_ALL_COMMANDE_FOURNISSEUR_ENDPOINT)
    List<CommandeFournisseurDto> findAll();

    @DeleteMapping(DELETE_COMMANDE_FOURNISSEUR_ENDPOINT)
    void delete(@PathVariable("idCommandeFournisseur") Integer id);
}
