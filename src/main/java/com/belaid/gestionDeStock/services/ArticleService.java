package com.belaid.gestionDeStock.services;

import com.belaid.gestionDeStock.dto.ArticleDto;
import com.belaid.gestionDeStock.dto.LigneCommandeClientDto;
import com.belaid.gestionDeStock.dto.LigneCommandeFournisseurDto;
import com.belaid.gestionDeStock.dto.LigneVenteDto;

import java.util.List;


public interface ArticleService {

    ArticleDto save(ArticleDto dto);

    ArticleDto findById(Integer id);

    ArticleDto findByCodeArticle(String codeArticle);

    List<ArticleDto> findAll();

    List<LigneVenteDto> findHistoriqueVentes(Integer idArticle);

    List<LigneCommandeClientDto> findHistoriqueCommandeClient(Integer idArticle);

    List<LigneCommandeFournisseurDto> findHistoriqueCommandeFournisseur(Integer idArticle);

    List<ArticleDto> findAllArticleByIdCategory(Integer idCategory);

    void delete(Integer id);
}
