package com.belaid.gestionDeStock.services.impl;

import com.belaid.gestionDeStock.dto.EntrepriseDto;
import com.belaid.gestionDeStock.dto.RolesDto;
import com.belaid.gestionDeStock.dto.UtilisateurDto;
import com.belaid.gestionDeStock.exception.EntityNotFoundException;
import com.belaid.gestionDeStock.exception.ErrorCodes;
import com.belaid.gestionDeStock.exception.InvalidEntityException;
import com.belaid.gestionDeStock.repository.EntrepriseRepository;
import com.belaid.gestionDeStock.repository.RolesRepository;
import com.belaid.gestionDeStock.services.EntrepriseService;
import com.belaid.gestionDeStock.services.UtilisateurService;
import com.belaid.gestionDeStock.validator.EntrepriseValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EntrepriseServiceImpl implements EntrepriseService {

    private EntrepriseRepository entrepriseRepository;
    private UtilisateurService utilisateurService;
    private RolesRepository rolesRepository;

    @Autowired
    public EntrepriseServiceImpl(EntrepriseRepository entrepriseRepository, UtilisateurService utilisateurService,
                                 RolesRepository rolesRepository) {
        this.entrepriseRepository = entrepriseRepository;
        this.utilisateurService = utilisateurService;
        this.rolesRepository = rolesRepository;
    }

    @Override
    public EntrepriseDto save(EntrepriseDto dto) {
        List<String> errors = EntrepriseValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Entreprise is not valid {}", dto);
            throw new InvalidEntityException("L'entreprise n'est pas valide", ErrorCodes.ENTREPRISE_NOT_VALID, errors);
        }
        EntrepriseDto savedEntreprise = EntrepriseDto.fromEntity(
                entrepriseRepository.save(EntrepriseDto.toEntity(dto))
        );

        UtilisateurDto utilisateur = fromEntreprise(savedEntreprise);

        UtilisateurDto savedUser = utilisateurService.save(utilisateur);

        RolesDto rolesDto = RolesDto.builder()
                .roleName("ADMIN")
                .utilisateur(savedUser)
                .build();

        rolesRepository.save(RolesDto.toEntity(rolesDto));

        return  savedEntreprise;
    }

    private UtilisateurDto fromEntreprise(EntrepriseDto dto) {
        return UtilisateurDto.builder()
                .adresse(dto.getAdresse())
                .nom(dto.getNom())
                .prenom(dto.getCodeFiscal())
                .email(dto.getEmail())
                .moteDePasse(generateRandomPassword())
                .entreprise(dto)
                .dateDeNaissance(Instant.now())
                .photo(dto.getPhoto())
                .build();
    }

    private String generateRandomPassword() {
        return "som3R@nd0mP@$$word";
    }

    @Override
    public EntrepriseDto findById(Integer id) {
        if (id == null) {
            log.error("Entreprise ID is null");
            return null;
        }
        return entrepriseRepository.findById(id)
                .map(EntrepriseDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune entreprise avec l'ID = " + id + " n' ete trouve dans la BDD",
                        ErrorCodes.ENTREPRISE_NOT_FOUND)
                );
    }

    @Override
    public List<EntrepriseDto> findAll() {
        return entrepriseRepository.findAll().stream()
                .map(EntrepriseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Entreprise ID is null");
            return;
        }
        entrepriseRepository.deleteById(id);
    }
}
