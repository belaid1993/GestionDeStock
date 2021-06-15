package com.belaid.gestionDeStock.services.strategy;

import com.belaid.gestionDeStock.dto.ClientDto;
import com.belaid.gestionDeStock.exception.ErrorCodes;
import com.belaid.gestionDeStock.exception.InvalidOperationException;
import com.belaid.gestionDeStock.model.Client;
import com.belaid.gestionDeStock.services.ClientService;
import com.belaid.gestionDeStock.services.FlickrService;
import com.flickr4java.flickr.FlickrException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;

@Service("clientStrategy")
@Slf4j
public class SaveClientPhoto implements Strategy<ClientDto> {

    private FlickrService flickrService;
    private ClientService clientService;

    @Autowired
    public SaveClientPhoto(FlickrService flickrService, ClientService clientService) {
        this.flickrService = flickrService;
        this.clientService = clientService;
    }

    @Override
    public ClientDto savePhoto(Integer id, InputStream photo, String title) throws FlickrException {

        ClientDto client = clientService.findById(id);
        String urlPhoto = flickrService.savePhoto(photo, title);

        if(!StringUtils.hasLength(urlPhoto)){
            throw new InvalidOperationException("Erreur lors de l'enregistrement de photo de client", ErrorCodes.UPDATE_PHOTO_EXCEPTION);
        }

        client.setPhoto(urlPhoto);

        return clientService.save(client);
    }
}
