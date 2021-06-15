package com.belaid.gestionDeStock.services.strategy;

import com.belaid.gestionDeStock.dto.UtilisateurDto;
import com.belaid.gestionDeStock.exception.ErrorCodes;
import com.belaid.gestionDeStock.exception.InvalidOperationException;
import com.belaid.gestionDeStock.model.Utilisateur;
import com.belaid.gestionDeStock.services.FlickrService;
import com.belaid.gestionDeStock.services.UtilisateurService;
import com.flickr4java.flickr.FlickrException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;

@Service("utilisateurStrategy")
@Slf4j
public class SaveUtilisateurPhoto implements Strategy<UtilisateurDto> {

    private FlickrService flickrService;
    private UtilisateurService utilisateurService;

    @Autowired
    public SaveUtilisateurPhoto(FlickrService flickrService, UtilisateurService utilisateurService) {
        this.flickrService = flickrService;
        this.utilisateurService = utilisateurService;
    }

    @Override
    public UtilisateurDto savePhoto(Integer id, InputStream photo, String title) throws FlickrException {

        UtilisateurDto utilisateur = utilisateurService.findById(id);
        String urlPhoto = flickrService.savePhoto(photo, title);

        if(!StringUtils.hasLength(urlPhoto)){
            throw new InvalidOperationException("Erreur lors de l'enregistrement de photo de l'utilisateur", ErrorCodes.UPDATE_PHOTO_EXCEPTION);
        }

        utilisateur.setPhoto(urlPhoto);

        return utilisateurService.save(utilisateur);
    }
}
