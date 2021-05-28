package com.belaid.gestionDeStock.controller.api;

import com.belaid.gestionDeStock.dto.FournisseurDto;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.belaid.gestionDeStock.utils.Constants.FOURNISSEUR_ENDPOINT;

@Api("fournisseurs")
public interface FournisseurApi {

    @PostMapping(value = FOURNISSEUR_ENDPOINT + "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FournisseurDto save(@RequestBody FournisseurDto dto);

    @GetMapping(value = FOURNISSEUR_ENDPOINT + "/{idFournisseur}", produces = MediaType.APPLICATION_JSON_VALUE)
    FournisseurDto findById(@PathVariable("idFournisseur") Integer id);

    @GetMapping(value = FOURNISSEUR_ENDPOINT + "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    List<FournisseurDto> findAll();

    @DeleteMapping(value = FOURNISSEUR_ENDPOINT + "/delete/{idFournisseur}")
    void delete(@PathVariable("idFournisseur") Integer id);
}
