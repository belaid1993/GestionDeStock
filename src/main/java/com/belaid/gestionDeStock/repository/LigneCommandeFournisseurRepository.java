package com.belaid.gestionDeStock.repository;

import com.belaid.gestionDeStock.model.LigneCommandeFournisseur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LigneCommandeFournisseurRepository extends JpaRepository<LigneCommandeFournisseur, Integer> {

    List<LigneCommandeFournisseur> findAllByCommandeFournisseurId(Integer id);

    List<LigneCommandeFournisseur> findAllByArticleId(Integer idArticle);
}


