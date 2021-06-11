package com.belaid.gestionDeStock.services;

import com.belaid.gestionDeStock.dto.CommandeClientDto;
import com.belaid.gestionDeStock.model.EtatCommande;

import java.util.List;

public interface CommandeClientService {

    CommandeClientDto save(CommandeClientDto dto);

    CommandeClientDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande);

    CommandeClientDto findById(Integer id);

    CommandeClientDto findByCode(String code);

    List<CommandeClientDto> findAll();

    void delete(Integer id);
}
