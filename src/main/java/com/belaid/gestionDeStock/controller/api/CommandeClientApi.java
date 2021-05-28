package com.belaid.gestionDeStock.controller.api;

import com.belaid.gestionDeStock.dto.CommandeClientDto;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.belaid.gestionDeStock.utils.Constants.APP_ROOT;

@Api("commandesclients")
public interface CommandeClientApi {

    @PostMapping(APP_ROOT + "/commandesclients/create")
    ResponseEntity<CommandeClientDto> save(@RequestBody CommandeClientDto dto);

    @GetMapping(APP_ROOT + "/commandesclients/{idCommandeClient}")
    ResponseEntity<CommandeClientDto> findById(@PathVariable("idCommandeClient") Integer id);

    @GetMapping(APP_ROOT + "/commandesclients/filter/{codeCommandeClient}")
    ResponseEntity<CommandeClientDto> findByCode(@PathVariable("codeCommandeClient") String code);

    @GetMapping(APP_ROOT + "/commandesclients/all")
    ResponseEntity<List<CommandeClientDto>> findAll();

    @DeleteMapping(APP_ROOT + "/commandesclients/delete/{idCommandeClient}")
    ResponseEntity delete(@PathVariable("idCommandeClient") Integer id);
}
