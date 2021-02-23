package com.belaid.gestionDeStock.repository;

import com.belaid.gestionDeStock.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
}
