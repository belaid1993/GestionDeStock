package com.belaid.gestionDeStock.services.auth;

import com.belaid.gestionDeStock.dto.UtilisateurDto;
import com.belaid.gestionDeStock.model.auth.ExtendedUser;
import com.belaid.gestionDeStock.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {

    @Autowired
    private UtilisateurService utilisateurService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UtilisateurDto utilisateur = utilisateurService.findByEmail(email);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        utilisateur.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRoleName())));

        return new ExtendedUser(utilisateur.getEmail(), utilisateur.getMoteDePasse(),
                utilisateur.getEntreprise().getId(), authorities);
    }
}
