package com.belaid.gestionDeStock.validator;

import com.belaid.gestionDeStock.dto.ClientDto;
import com.belaid.gestionDeStock.dto.FournisseurDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class FournisseurValidator {

    public static List<String> validate(FournisseurDto fournisseurDto) {
        List<String> errors = new ArrayList<>();

        if (fournisseurDto == null) {
            errors.add("Veuillez renseigner le nom de fournisseur");
            errors.add("Veuillez renseigner le prenom de fournisseur");
            errors.add("Veuillez renseigner l'email de fournisseur");
            errors.add("Veuillez renseigner le num de Tel de fournisseur");
            return errors;
        }

        if (!StringUtils.hasLength(fournisseurDto.getNom())) {
            errors.add("Veuillez renseigner le nom de fournisseur");
        }

        if (!StringUtils.hasLength(fournisseurDto.getPrenom())) {
            errors.add("Veuillez renseigner le prenom de fournisseur");
        }

        if (!StringUtils.hasLength(fournisseurDto.getMail())) {
            errors.add("Veuillez renseigner l'email de fournisseur");
        }

        if (!StringUtils.hasLength(fournisseurDto.getNumTel())) {
            errors.add("Veuillez renseigner le num de Tel de fournisseur");
        }

        return errors;
    }
}
