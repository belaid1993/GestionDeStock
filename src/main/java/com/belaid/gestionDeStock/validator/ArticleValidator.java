package com.belaid.gestionDeStock.validator;

import com.belaid.gestionDeStock.dto.ArticleDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ArticleValidator {

    public static List<String> validate(ArticleDto articleDto) {
        List<String> errors = new ArrayList<>();

        if (articleDto == null) {
            errors.add("veuillez renseigner le code d'article");
            errors.add("veuillez renseigner la designation d'article");
            errors.add("veuillez renseigner le taux tva de l'article");
            errors.add("veuillez renseigner le prix unitaire ht de l'article");
            errors.add("veuillez renseigner le prix unitaire ttc de l'article");
            errors.add("veuillez selectionner une category");
            return errors;

        }

        if (!StringUtils.hasLength(articleDto.getCodeArticle())) {
            errors.add("veuillez renseigner le code d'article");
        }

        if (!StringUtils.hasLength(articleDto.getDesignation())) {
            errors.add("veuillez renseigner la designation d'article");
        }

        if (articleDto.getTauxTva() == null) {
            errors.add("veuillez renseigner le taux tva de l'article");
        }

        if (articleDto.getPrixUnitaireHt() == null) {
            errors.add("veuillez renseigner le prix unitaire ht de l'article");
        }

        if (articleDto.getPrixUnitaireTtc() == null) {
            errors.add("veuillez renseigner le prix unitaire ttc de l'article");
        }

        if (articleDto.getCategory() == null) {
            errors.add("veuillez selectionner une category");
        }

        return errors;
    }
}
