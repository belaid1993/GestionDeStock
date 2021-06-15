package com.belaid.gestionDeStock.services.strategy;

import com.belaid.gestionDeStock.dto.EntrepriseDto;
import com.belaid.gestionDeStock.exception.ErrorCodes;
import com.belaid.gestionDeStock.exception.InvalidEntityException;
import com.belaid.gestionDeStock.exception.InvalidOperationException;
import com.belaid.gestionDeStock.model.Entreprise;
import com.belaid.gestionDeStock.services.EntrepriseService;
import com.belaid.gestionDeStock.services.FlickrService;
import com.flickr4java.flickr.FlickrException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;

@Service("entrepriseStrategy")
@Slf4j
public class SaveEntreprisePhoto implements Strategy<EntrepriseDto> {

    private EntrepriseService entrepriseService;
    private FlickrService flickrService;

    @Autowired
    public SaveEntreprisePhoto(EntrepriseService entrepriseService, FlickrService flickrService) {
        this.entrepriseService = entrepriseService;
        this.flickrService = flickrService;
    }

    @Override
    public EntrepriseDto savePhoto(Integer id, InputStream photo, String title) throws FlickrException {

        EntrepriseDto entreprise = entrepriseService.findById(id);
        String urlPhoto = flickrService.savePhoto(photo, title);

        if(!StringUtils.hasLength(urlPhoto)){
            throw new InvalidOperationException("Erreur lors de l'enregistrement de photo de l'entreprise", ErrorCodes.UPDATE_PHOTO_EXCEPTION);
        }

        entreprise.setPhoto(urlPhoto);

        return entrepriseService.save(entreprise);
    }
}
