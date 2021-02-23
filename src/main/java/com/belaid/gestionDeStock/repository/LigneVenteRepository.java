package com.belaid.gestionDeStock.repository;

import com.belaid.gestionDeStock.model.LigneVente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LigneVenteRepository extends JpaRepository<LigneVente, Integer> {
}
