package com.server.eFacture.ConfigurationSec;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component //Pour qu'il puisse être injecté
public class JwtUtils {
    //Classe de construction de notre token ;
    //Le token a 3 parties : l'entête , ... et la signature.

    //On va mettre ici toute les méthodes dont on aura besoin pour manipuler le token dans l'application

    //On commence par récuperer les valeurs qu'on a stocké dans le applications.properties
    @Value("${app.secret-key}") //permet de récupérer des valeurs stockées dans le app.properties
    private String secretKey;

    @Value("${app.expiration-time}")
    private long expirationTime;

    //Permet de généer le token
    public String generateToken(String username){
        //Ici on va utiliser le username pour générer le token.
        Map<String, Object> claims = new HashMap<>(); //Les claims sont une classe avec une structure type/valeur
        return createToken(claims,username);
    }


    private String createToken(Map<String,Object> claims, String subject){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();

        //Le builder permet de construire le jwt

    }

    private Key getSignKey(){
        //Permet d'avoir une clé signé par rapport à la clé que j'ai déjà definie

        byte[] keyBytes = secretKey.getBytes(); //Clé ayant déjà été finie
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    public Boolean validateToken(String token , UserDetails userDetails){
        //Méthode pour valider le token avec en paramètre le token reçu et les détails de notre utilisateur.

        String username = extractUsername(token); //On extrait le username qui est dans le token et ensuite on va le comparé avec
        // le username du userDetails et vérifié s'il n'est pas obselète.
        //Ensuite on retourne le booléen
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        //Vérifie si le token a expiré
        return extractExpirationDate(token).before(new Date()); //Vérifier que la date d'exp est inférieure à la date actuel
    }

    public String extractUsername(String token){
        //Pour extraire le username du token qu'on a cédé plus haut dans le subject de construction du token
        //Ici on veut le username dans le subject
        return extractClaim(token, Claims::getSubject);
    }


    public Date extractExpirationDate(String token){

        return extractClaim(token, Claims::getExpiration);
    }
    private  <T> T extractClaim(String token , Function<Claims,T> claimsResolver){
        //On met un type générique
        //Comme on va utiliser la méthode à plusieurs endroits on va la définir en méthode générique.

        final Claims claims = extractAllClaims(token); //Récupérer tous les claims dans le token
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        //Cette méthode d'avoir tous les claims qu'on a défini dans la construction du token
        return Jwts.parser()//
                .setSigningKey(getSignKey())
                .parseClaimsJws(token)
                .getBody();
    }


}
