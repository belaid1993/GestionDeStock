package com.belaid.gestionDeStock.repository;

import com.belaid.gestionDeStock.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Integer> {
}
