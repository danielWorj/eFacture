package com.server.eFacture.Controller.Auth;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.eFacture.ConfigurationSec.JwtUtils;
import com.server.eFacture.DTO.AuthData;
import com.server.eFacture.Entity.Entreprise.Technicien;
import com.server.eFacture.Entity.Response.ServerResponse;
import com.server.eFacture.Repository.TechnicienRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/")
@Slf4j //Pour les log
@CrossOrigin("*")
public class AuthController {
    @Autowired
    private TechnicienRepository technicienRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;

    //Inscription
    @PostMapping(value="/register", consumes = "multipart/form-data")
    public ResponseEntity<ServerResponse> register(@RequestParam("technicien") String technicienString) throws JsonProcessingException {
        System.out.println(technicienString);
        ServerResponse serverResponse = new ServerResponse();
        Technicien technicien = new ObjectMapper().readValue(technicienString, Technicien.class);

        if (technicienRepository.findByNom(technicien.getNom())!= null ){
            serverResponse.setMessage("Ce nom d'utilisateur est déjà utilisé.");
            return ResponseEntity.badRequest().body(serverResponse);
        }

        technicien.setPassword(passwordEncoder.encode(technicien.getPassword()));
        technicienRepository.save(technicien);
        serverResponse.setMessage("Utilisateur enregistré avec succès");
        serverResponse.setStatus(true);
        return ResponseEntity.ok(serverResponse);
    }

    //@PostMapping(value="/login",  consumes = "multipart/form-data")

    @PostMapping(value="/login", consumes = "multipart/form-data")
    public ResponseEntity<?> login(@RequestParam("technicien") String technicienString) throws JsonProcessingException {
        System.out.println(technicienString);
        ServerResponse serverResponse = new ServerResponse();
        Technicien technicien = new ObjectMapper().readValue(technicienString, Technicien.class);

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(technicien.getNom(), technicien.getPassword()));
            if (authentication.isAuthenticated()){
                //Si l'authentification a fonctionné
                //On crée un token
                Map<String,Object> authData = new HashMap<>();
                authData.put("token", jwtUtils.generateToken(technicien.getNom()));
                authData.put("type", "Bearer");

                return ResponseEntity.ok(authData);
            }

            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Username or Password");
        }catch (AuthenticationException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
    @PostMapping(value ="/fetch",  consumes = "multipart/form-data")
    public ResponseEntity<Integer> fecthAuthId(@RequestParam("authdata") String authData) throws JsonProcessingException {
        AuthData authDataE = new  ObjectMapper().readValue(authData, AuthData.class);

        String username = this.jwtUtils.extractUsername(authDataE.getToken());

        Technicien technicien = this.technicienRepository.findByNom(username);

        return  ResponseEntity.ok(technicien.getId());
    }
}
