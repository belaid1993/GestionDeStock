package com.belaid.gestionDeStock.controller.api;

import com.belaid.gestionDeStock.dto.CommandeFournisseurDto;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.belaid.gestionDeStock.utils.Constants.*;

@Api("commandesfournisseurs")
public interface CommandesFournisseursApi {

    @PostMapping(CREATE_COMMANDE_FOURNISSEUR_ENDPOINT)
    CommandeFournisseurDto save(@RequestBody CommandeFournisseurDto dto);

    @GetMapping(FIND_COMMANDE_FOURNISSEUR_BY_ID_ENDPOINT)
    CommandeFournisseurDto findById(@PathVariable("idCommandeFournisseur") Integer id);

    @GetMapping(FIND_COMMANDE_FOURNISSEUR_BY_CODE_ENDPOINT)
    CommandeFournisseurDto findByCode(@PathVariable("codeCommandeFournisseur") String code);

    @GetMapping(FIND_ALL_COMMANDE_FOURNISSEUR_ENDPOINT)
    List<CommandeFournisseurDto> findAll();

    @DeleteMapping(DELETE_COMMANDE_FOURNISSEUR_ENDPOINT)
    void delete(@PathVariable("idCommandeFournisseur") Integer id);
}
