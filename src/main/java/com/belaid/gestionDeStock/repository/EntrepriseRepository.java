package com.belaid.gestionDeStock.repository;

import com.belaid.gestionDeStock.model.Entreprise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntrepriseRepository extends JpaRepository<Entreprise, Integer> {
}
