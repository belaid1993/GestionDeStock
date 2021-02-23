package com.belaid.gestionDeStock.repository;

import com.belaid.gestionDeStock.model.MvtStk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MvtStkRepository extends JpaRepository<MvtStk, Integer> {
}
