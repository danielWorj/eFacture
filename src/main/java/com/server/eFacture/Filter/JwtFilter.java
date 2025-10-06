package com.server.eFacture.Filter;

import com.server.eFacture.ConfigurationSec.JwtUtils;
import com.server.eFacture.Service.CustomUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component //Il sera chargé comme beans dans spring
@ RequiredArgsConstructor //Permet de faire l'injection des deux dépendances privées

public class JwtFilter extends OncePerRequestFilter {
    //Ce filtre va permettre de filtrer la requête et vérifier que le token est correct

    private final CustomUserDetailService customUserDetailsService; //vérifier que l'utilisateur est vraiment qui il prétend être.
    @Autowired
    private final JwtUtils jwtUtils ;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Récuperer le token d'abord dans le header
        System.out.println("JWT Filter in action ...");
        final  String authHeader = request.getHeader("Authorization");

        System.out.println("Header : " + authHeader);
        //ystem.out.println("Header : " + request.ge);
        //Ensuite on va extraire le username du token

        String username = null;
        String jwt = null;

        //Ensuite on vérifie le si le authHeader est différent de nul
        // et s'il commence par Bearer
        //Quand on utilise un token du JWT, il faut toujours précedé le jwt par Bearer

        if (authHeader !=null && authHeader.startsWith("Bearer ")){
            //On verifie si le authHeader est different de null et s'il commence par Bearer

            jwt = authHeader.substring(7); // Le bearer c'est 7 caratères et tous ce qui vient après constitue notre jwt

            username = jwtUtils.extractUsername(jwt);

            System.out.println("Username extrait : " + username);
        }

        if (username !=null && SecurityContextHolder.getContext().getAuthentication() == null){
            //Ici on vérifie si le username n'est pas nul et si on a pas encore authentifié l'utilisateur
            //Si SecurityContextHolder.getContext().getAuthentication() est différent de nul , ça veut dire qu'on a été authentifié

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username); //Authentification de l'utilisateur dans la bd

            //On va valider le token
            if (jwtUtils.validateToken(jwt, userDetails)){
                //Si le user est valide , il faut notifié le contexte et signaler que l'utilisateur est bien authentifié.
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null , userDetails.getAuthorities());
                //Voir avec quel requete on a authentifie l'user
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);


            }
        };

        filterChain.doFilter(request,response);

    }
}
