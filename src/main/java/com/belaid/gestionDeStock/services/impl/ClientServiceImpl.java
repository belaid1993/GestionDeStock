package com.belaid.gestionDeStock.services.impl;

import com.belaid.gestionDeStock.dto.ClientDto;
import com.belaid.gestionDeStock.exception.EntityNotFoundException;
import com.belaid.gestionDeStock.exception.ErrorCodes;
import com.belaid.gestionDeStock.exception.InvalidEntityException;
import com.belaid.gestionDeStock.model.Client;
import com.belaid.gestionDeStock.repository.ClientRepository;
import com.belaid.gestionDeStock.services.ClientService;
import com.belaid.gestionDeStock.validator.ClientValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public ClientDto save(ClientDto dto) {
        List<String> errors = ClientValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Client n'est pas valid {}", dto);
            throw new InvalidEntityException("Client n'est pas valid", ErrorCodes.CLIENT_NOT_VALID, errors);
        }

        Client saveClient = ClientDto.toEntity(dto);

        return ClientDto.fromEntity(saveClient);
    }

    @Override
    public ClientDto findById(Integer id) {
        if (id == null) {
            log.error("Client ID est null");
            return null;
        }

        Optional<Client> client = clientRepository.findById(id);

        ClientDto dto = ClientDto.fromEntity(client.get());

        return Optional.of(dto).orElseThrow(() -> new EntityNotFoundException("Aucun Client avec ID " + id + " n'ete trouver dans BDD", ErrorCodes.CLIENT_NOT_FOUND));
    }

    @Override
    public List<ClientDto> findAll() {
        return clientRepository.findAll().stream()
                .map(ClientDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("client ID est null");
            return;
        }
        clientRepository.deleteById(id);
    }
}
