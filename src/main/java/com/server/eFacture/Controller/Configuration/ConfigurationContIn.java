package com.server.eFacture.Controller.Configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.server.eFacture.Entity.Devis.Client;
import com.server.eFacture.Entity.Entreprise.Materiel;
import com.server.eFacture.Entity.Entreprise.Tache;
import com.server.eFacture.Entity.Entreprise.Technicien;
import com.server.eFacture.Entity.Response.ServerResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RequestMapping("efacture/configuration/")
@CrossOrigin("*")
public interface ConfigurationContIn {

    @GetMapping("/login")
    ResponseEntity<String> login();

    @GetMapping("/materiel/all")
    ResponseEntity<List<Materiel>> findMateriel();

    @PostMapping("/materiel/create")
    ResponseEntity<ServerResponse> createMateriel(@RequestParam("materiel") String materiel) throws JsonProcessingException;



    @GetMapping("/client/all")
    ResponseEntity<List<Client>> findAllClient();

    @GetMapping("/tache/all")
    ResponseEntity<List<Tache>> findTache();

    @PostMapping("/tache/create")
    ResponseEntity<ServerResponse> createTache(@RequestParam("tache") String tache) throws JsonProcessingException;

    @GetMapping("/load/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws FileNotFoundException;


}
