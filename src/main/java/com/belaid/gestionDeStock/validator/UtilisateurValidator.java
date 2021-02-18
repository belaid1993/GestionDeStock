package com.belaid.gestionDeStock.validator;

import com.belaid.gestionDeStock.dto.UtilisateurDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class UtilisateurValidator {

    public static List<String> validate(UtilisateurDto utilisateurDto) {
        List<String> errors = new ArrayList<>();

        if (utilisateurDto == null) {
            errors.add("veuillez renseigner le nom de l'utilisateur");
            errors.add("veuillez renseigner le prenom de l'utilisateur");
            errors.add("veuillez renseigner le mot de passe de l'utilisateur");
            errors.add("veuillez renseigner l'email de l'utilisateur");
            errors.add("veuillez renseigner l'adresse de l'utilisateur");
            return errors;
        }

        if (!StringUtils.hasLength(utilisateurDto.getNom())) {
            errors.add("veuillez renseigner le nom de l'utilisateur");
        }

        if (!StringUtils.hasLength(utilisateurDto.getPrenom())) {
            errors.add("veuillez renseigner le prenom de l'utilisateur");
        }

        if (!StringUtils.hasLength(utilisateurDto.getMotDePasse())) {
            errors.add("veuillez renseigner le mot de passe de l'utilisateur");
        }

        if (!StringUtils.hasLength(utilisateurDto.getEmail())) {
            errors.add("veuillez renseigner l'email de l'utilisateur");
        }

        if (utilisateurDto.getDateDeNaissance() == null) {
            errors.add("veuillez renseigner la date de naissance de l'utilisateur");
        }

        if (utilisateurDto.getAdresse() == null) {
            errors.add("veuillez renseigner l'adresse de l'utilisateur");
        } else {
            if (!StringUtils.hasLength(utilisateurDto.getAdresse().getAdresse1())) {
                errors.add("le champs 'adresse1' est obligatoire");
            }
            if (!StringUtils.hasLength(utilisateurDto.getAdresse().getVille())) {
                errors.add("le champs 'ville' est obligatoire");
            }
            if (!StringUtils.hasLength(utilisateurDto.getAdresse().getCodePostale())) {
                errors.add("le champs 'code postale' est obligatoire");
            }
            if (!StringUtils.hasLength(utilisateurDto.getAdresse().getPays())) {
                errors.add("le champs 'pays' est obligatoire");
            }
        }

        return errors;
    }
}
