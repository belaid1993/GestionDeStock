package com.belaid.gestionDeStock.repository;

import com.belaid.gestionDeStock.model.Ventes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentesRepository extends JpaRepository<Ventes, Integer> {
}
