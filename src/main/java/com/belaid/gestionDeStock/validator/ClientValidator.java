package com.belaid.gestionDeStock.validator;

import com.belaid.gestionDeStock.dto.ClientDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ClientValidator {

    public static List<String> validate(ClientDto clientDto) {
        List<String> errors = new ArrayList<>();

        if (clientDto == null) {
            errors.add("Veuillez renseigner le nom de client");
            errors.add("Veuillez renseigner le prenom de client");
            errors.add("Veuillez renseigner l'email de client");
            errors.add("Veuillez renseigner le num de Tel de client");
            return errors;
        }

        if (!StringUtils.hasLength(clientDto.getNom())) {
            errors.add("Veuillez renseigner le nom de client");
        }

        if (!StringUtils.hasLength(clientDto.getPrenom())) {
            errors.add("Veuillez renseigner le prenom de client");
        }

        if (!StringUtils.hasLength(clientDto.getMail())) {
            errors.add("Veuillez renseigner l'email de client");
        }

        if (!StringUtils.hasLength(clientDto.getNumTel())) {
            errors.add("Veuillez renseigner le num de Tel de client");
        }

        return errors;
    }
}
