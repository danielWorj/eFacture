package com.server.eFacture.Service;

import com.server.eFacture.Entity.Entreprise.Technicien;
import com.server.eFacture.Repository.TechnicienRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    //Classe qui implemente une classe de Spring Security et qui permet de récupérer l'utilsateur par son userName

    private  final TechnicienRepository technicienRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      //Recherche du technicien par son username ;
        Technicien technicien = this.technicienRepository.findByNom(username);

        if (Objects.isNull(technicien)){
            //Dans le cas ou on a n'a pas le user on envoie une message d'exception
            throw new UsernameNotFoundException("Utilisateur non trouve");
        }

        //On retourne un user de spring security
        //La collection liste c'est pour les rôle
        return new org.springframework.security.core.userdetails.User(technicien.getNom(), technicien.getPassword() , Collections.singletonList(
                new SimpleGrantedAuthority(technicien.getRole())
        ));
    }
}
