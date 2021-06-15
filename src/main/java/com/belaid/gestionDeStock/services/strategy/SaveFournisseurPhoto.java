package com.belaid.gestionDeStock.services.strategy;

import com.belaid.gestionDeStock.dto.FournisseurDto;
import com.belaid.gestionDeStock.exception.ErrorCodes;
import com.belaid.gestionDeStock.exception.InvalidOperationException;
import com.belaid.gestionDeStock.model.Fournisseur;
import com.belaid.gestionDeStock.services.FlickrService;
import com.belaid.gestionDeStock.services.FournisseurService;
import com.flickr4java.flickr.FlickrException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;

@Service("fournisseurStrategy")
@Slf4j
public class SaveFournisseurPhoto implements Strategy<FournisseurDto> {

    private FlickrService flickrService;
    private FournisseurService fournisseurService;

    @Autowired
    public SaveFournisseurPhoto(FlickrService flickrService, FournisseurService fournisseurService) {
        this.flickrService = flickrService;
        this.fournisseurService = fournisseurService;
    }

    @Override
    public FournisseurDto savePhoto(Integer id, InputStream photo, String title) throws FlickrException {

        FournisseurDto fournisseur = fournisseurService.findById(id);
        String urlPhoto = flickrService.savePhoto(photo, title);

        if(!StringUtils.hasLength(urlPhoto)){
            throw new InvalidOperationException("Erreur lors de l'enregistrement de photo de fournisseur", ErrorCodes.UPDATE_PHOTO_EXCEPTION);
        }
        fournisseur.setPhoto(urlPhoto);

        return fournisseurService.save(fournisseur);
    }
}
