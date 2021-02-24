package com.belaid.gestionDeStock.repository;

import com.belaid.gestionDeStock.model.CommandeClient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandeClientRepository extends JpaRepository<CommandeClient, Integer> {
}
