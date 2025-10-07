package com.server.eFacture.Controller.Configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.eFacture.Entity.Devis.Client;
import com.server.eFacture.Entity.Entreprise.Materiel;
import com.server.eFacture.Entity.Entreprise.Tache;
import com.server.eFacture.Entity.Entreprise.Technicien;
import com.server.eFacture.Entity.Response.ServerResponse;
import com.server.eFacture.Repository.ClientRepository;
import com.server.eFacture.Repository.MaterielRepository;
import com.server.eFacture.Repository.TacheRepository;
import com.server.eFacture.Repository.TechnicienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class ConfigurationContIM implements ConfigurationContIn{
    @Autowired
    private MaterielRepository materielRepository;
    @Autowired
    private TacheRepository tacheRepository;
    @Autowired
    private ClientRepository clientRepository;


    @Override
    public ResponseEntity<String> login() {
        return ResponseEntity.ok("Login give");
    }

    @Override
    public ResponseEntity<List<Materiel>> findMateriel() {
        return new ResponseEntity<>(this.materielRepository.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ServerResponse> createMateriel(String materiel) throws JsonProcessingException {
        ServerResponse serverResponse = new ServerResponse();
        Materiel materielS = new ObjectMapper().readValue(materiel, Materiel.class);
        System.out.println(materielS);

        this.materielRepository.save(materielS);

        serverResponse.setStatus(true);
        serverResponse.setMessage("Materiel cree avec succes");
        return ResponseEntity.ok(serverResponse);
    }

    @Override
    public ResponseEntity<List<Client>> findAllClient() {
        System.out.println("Fecth client");
        return ResponseEntity.ok(this.clientRepository.findAll());
    }

    @Override
    public ResponseEntity<List<Tache>> findTache() {
        return ResponseEntity.ok(this.tacheRepository.findAll());
    }

    @Override
    public ResponseEntity<ServerResponse> createTache(String tache) throws JsonProcessingException {
        ServerResponse serverResponse = new ServerResponse();
        Tache tacheS = new  ObjectMapper().readValue(tache, Tache.class);
        System.out.println(tacheS);

        this.tacheRepository.save(tacheS);
        serverResponse.setStatus(true);
        serverResponse.setMessage("Tache cree avec success");
        return ResponseEntity.ok(serverResponse);
    }



    private final String fileDirectory = "D:\\Projet Devis Facture\\eFacture\\Libert";


    public ResponseEntity<Resource> downloadFile(String filename) throws FileNotFoundException {

        System.out.println("telechargement");

        File file = new File(fileDirectory + File.separator + filename);
        if (!file.exists()) {
            throw new FileNotFoundException("Fichier non trouvé : " + filename);
        }
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
