package com.belaid.gestionDeStock.controller;

import com.belaid.gestionDeStock.controller.api.EntrepriseApi;
import com.belaid.gestionDeStock.dto.EntrepriseDto;
import com.belaid.gestionDeStock.services.EntrepriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EntrepriseController implements EntrepriseApi {

    private EntrepriseService entrepriseService;

    @Autowired
    public EntrepriseController(EntrepriseService entrepriseService) {
        this.entrepriseService = entrepriseService;
    }

    @Override
    public EntrepriseDto save(EntrepriseDto dto) {
        return entrepriseService.save(dto);
    }

    @Override
    public EntrepriseDto findById(Integer id) {
        return entrepriseService.findById(id);
    }

    @Override
    public List<EntrepriseDto> findAll() {
        return entrepriseService.findAll();
    }

    @Override
    public void delete(Integer id) {
        entrepriseService.delete(id);
    }
}
