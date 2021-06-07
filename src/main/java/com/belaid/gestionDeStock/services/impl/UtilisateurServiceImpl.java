package com.belaid.gestionDeStock.services.impl;

import com.belaid.gestionDeStock.dto.UtilisateurDto;
import com.belaid.gestionDeStock.exception.EntityNotFoundException;
import com.belaid.gestionDeStock.exception.ErrorCodes;
import com.belaid.gestionDeStock.exception.InvalidEntityException;
import com.belaid.gestionDeStock.model.Utilisateur;
import com.belaid.gestionDeStock.repository.UtilisateurRepository;
import com.belaid.gestionDeStock.services.UtilisateurService;
import com.belaid.gestionDeStock.validator.UtilisateurValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UtilisateurServiceImpl implements UtilisateurService {

    private UtilisateurRepository utilisateurRepository;

    @Autowired
    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public UtilisateurDto save(UtilisateurDto dto) {
        List<String> errors = UtilisateurValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Utilisateur n'est pas valid {}");
            throw new InvalidEntityException("Utilisateur n'est pas valid", ErrorCodes.UTILISATEUR_NOT_VALID, errors);
        }

        Utilisateur saveUtilisateur = utilisateurRepository.save(UtilisateurDto.toEntity(dto));

        return UtilisateurDto.fromEntity(saveUtilisateur);
    }

    @Override
    public UtilisateurDto findById(Integer id) {
        if (id == null) {
            log.error("Utilisateur ID est null");
            return null;
        }

        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(id);

        UtilisateurDto dto = UtilisateurDto.fromEntity(utilisateur.get());

        return Optional.of(dto).orElseThrow(() -> new EntityNotFoundException("Aucun Utilisateur avec ID " + id + " n'ete trouver dans BDD", ErrorCodes.UTILISATEUR_NOT_FOUND));
    }

    @Override
    public List<UtilisateurDto> findAll() {
        return utilisateurRepository.findAll().stream()
                .map(UtilisateurDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Utilisateur ID est null");
            return;
        }
        utilisateurRepository.deleteById(id);
    }

    @Override
    public UtilisateurDto findByEmail(String email) {
        return utilisateurRepository.findUtilisateurByEmail(email)
                .map(UtilisateurDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun utilisateur avec l'email = " + email + " n' ete trouve dans la BDD",
                        ErrorCodes.UTILISATEUR_NOT_FOUND)
                );
    }
}
