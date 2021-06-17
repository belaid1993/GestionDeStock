package com.belaid.gestionDeStock.services.impl;

import com.belaid.gestionDeStock.dto.FournisseurDto;
import com.belaid.gestionDeStock.exception.EntityNotFoundException;
import com.belaid.gestionDeStock.exception.ErrorCodes;
import com.belaid.gestionDeStock.exception.InvalidEntityException;
import com.belaid.gestionDeStock.exception.InvalidOperationException;
import com.belaid.gestionDeStock.model.CommandeClient;
import com.belaid.gestionDeStock.model.Fournisseur;
import com.belaid.gestionDeStock.repository.CommandeFournisseurRepository;
import com.belaid.gestionDeStock.repository.FournisseurRepository;
import com.belaid.gestionDeStock.services.FournisseurService;
import com.belaid.gestionDeStock.validator.FournisseurValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FournisseurServiceImpl implements FournisseurService {

    private FournisseurRepository fournisseurRepository;
    private CommandeFournisseurRepository commandeFournisseurRepository;

    @Autowired
    public FournisseurServiceImpl(FournisseurRepository fournisseurRepository, CommandeFournisseurRepository commandeFournisseurRepository) {
        this.fournisseurRepository = fournisseurRepository;
        this.commandeFournisseurRepository = commandeFournisseurRepository;
    }

    @Override
    public FournisseurDto save(FournisseurDto dto) {
        List<String> errors = FournisseurValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Fournisseur n'est pas valid {}", dto);
            throw new InvalidEntityException("Fournisseur n'est pas valid", ErrorCodes.FOURNISSEUR_NOT_VALID, errors);
        }

        Fournisseur saveFournisseur = fournisseurRepository.save(FournisseurDto.toEntity(dto));

        return FournisseurDto.fromEntity(saveFournisseur);
    }

    @Override
    public FournisseurDto findById(Integer id) {
        if (id == null) {
            log.error("Fournisseur ID est null");
            return null;
        }

        Optional<Fournisseur> fournisseur = fournisseurRepository.findById(id);

        FournisseurDto dto = FournisseurDto.fromEntity(fournisseur.get());

        return Optional.of(dto).orElseThrow(() -> new EntityNotFoundException("Aucun Fournisseur avec ID " + id + " n'ete trouver dans BDD", ErrorCodes.FOURNISSEUR_NOT_FOUND));
    }

    @Override
    public List<FournisseurDto> findAll() {
        return fournisseurRepository.findAll().stream()
                .map(FournisseurDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Fournisseur ID is null");
            return;
        }
        List<CommandeClient> commandeFournisseur = commandeFournisseurRepository.findAllByFournisseurId(id);
        if (!commandeFournisseur.isEmpty()) {
            throw new InvalidOperationException("Impossible de supprimer un fournisseur qui a deja des commandes",
                    ErrorCodes.FOURNISSEUR_ALREADY_IN_USE);
        }
        fournisseurRepository.deleteById(id);
    }
}
