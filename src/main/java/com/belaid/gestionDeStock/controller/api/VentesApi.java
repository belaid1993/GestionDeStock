package com.belaid.gestionDeStock.controller.api;

import com.belaid.gestionDeStock.dto.VentesDto;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.belaid.gestionDeStock.utils.Constants.VENTES_ENDPOINT;

@Api("ventes")
public interface VentesApi {

    @PostMapping(VENTES_ENDPOINT + "/create")
    VentesDto save(@RequestBody VentesDto dto);

    @GetMapping(VENTES_ENDPOINT + "/{idVente}")
    VentesDto findById(@PathVariable("idVente") Integer id);

    @GetMapping(VENTES_ENDPOINT + "/{codeVente}")
    VentesDto findByCode(@PathVariable("codeVente") String code);

    @GetMapping(VENTES_ENDPOINT + "/all")
    List<VentesDto> findAll();

    @DeleteMapping(VENTES_ENDPOINT + "/delete/{idVente}")
    void delete(@PathVariable("idVente") Integer id);

}
