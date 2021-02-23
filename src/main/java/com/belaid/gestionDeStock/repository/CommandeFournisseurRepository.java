package com.belaid.gestionDeStock.repository;

import com.belaid.gestionDeStock.model.CommandeFournisseur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandeFournisseurRepository extends JpaRepository<CommandeFournisseur, Integer> {
}
