package com.belaid.gestionDeStock.repository;

import com.belaid.gestionDeStock.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
}
