package com.belaid.gestionDeStock.services;

import com.belaid.gestionDeStock.dto.ChangerMotDePasseUtilisateurDto;
import com.belaid.gestionDeStock.dto.UtilisateurDto;

import java.util.List;

public interface UtilisateurService {

    UtilisateurDto save(UtilisateurDto dto);

    UtilisateurDto findById(Integer id);

    List<UtilisateurDto> findAll();

    void delete(Integer id);

    UtilisateurDto findByEmail(String email);

    UtilisateurDto chenagerMotDePasse(ChangerMotDePasseUtilisateurDto dto);
}
