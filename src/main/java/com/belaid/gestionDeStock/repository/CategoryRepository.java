package com.belaid.gestionDeStock.repository;

import com.belaid.gestionDeStock.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
