package com.server.eFacture.Controller.Devis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.eFacture.DTO.DevisDTO;
import com.server.eFacture.DTO.EnregistrementDTO;
import com.server.eFacture.Entity.Devis.Client;
import com.server.eFacture.Entity.Devis.Devis;
import com.server.eFacture.Entity.Devis.Enregistrement;
import com.server.eFacture.Entity.Entreprise.Materiel;
import com.server.eFacture.Entity.Entreprise.Tache;
import com.server.eFacture.Entity.Entreprise.Technicien;
import com.server.eFacture.Entity.Response.ServerResponse;
import com.server.eFacture.Repository.*;
import com.server.eFacture.Utils.GeneratePDF;
import lombok.RequiredArgsConstructor;
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
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;

@Controller
public class DevisControllerIM implements DevisControllerIN{
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private DevisRepository devisRepository;
    @Autowired
    private TechnicienRepository technicienRepository;
    @Autowired
    private  EnregistrementRepository enregistrementRepository;
    @Autowired
    private MaterielRepository materielRepository;
    @Autowired
    private TacheRepository tacheRepository;


    @Override
    public ResponseEntity<Devis> createDevis(String devis) throws JsonProcessingException {
        System.out.println(devis);
        DevisDTO devisDTO = new ObjectMapper().readValue(devis, DevisDTO.class);

        Client client = new Client();

        client.setNom(devisDTO.getClient());
        client.setTelephone(devisDTO.getTelephone());
        client.setLocalisation("Non precise");

        this.clientRepository.save(client);

        Client clientLastSave = this.clientRepository.lastClientSave();

        Devis devisToSave = new Devis();

        devisToSave.setClient(
                clientLastSave
        );

        devisToSave.setTechnicien(
                this.technicienRepository.findById(devisDTO.getTechnicien()).orElse(null)
        );

        devisToSave.setDate(new Date());
        devisToSave.setStatus(true);

        this.devisRepository.save(devisToSave);

        Devis lastDevisSave = this.devisRepository.lastDevisSave();


        return ResponseEntity.ok(lastDevisSave);
    }

    @Override
    public ResponseEntity<List<Devis>> getAllDevis() {
        return ResponseEntity.ok(
                this.devisRepository.findAll()
        );
    }

    @Override
    public ResponseEntity<Devis> findDevisById(Integer id) {
        return ResponseEntity.ok(
                this.devisRepository.findById(
                        id
                ).orElse(null)
        );
    }

    @Override
    public ResponseEntity<ServerResponse> constructionDevis(String enregistrement) throws JsonProcessingException {
        EnregistrementDTO enregistrementDTO = new ObjectMapper().readValue(enregistrement, EnregistrementDTO.class);
        ServerResponse serverResponse = new ServerResponse();
        Enregistrement enregistrementDB = new Enregistrement();

        enregistrementDB.setQuantite(enregistrementDTO.getQuantite());
        enregistrementDB.setMateriel(
                this.materielRepository.findById(
                        enregistrementDTO.getMateriel()
                ).orElse(null)
        );

        enregistrementDB.setTache(
                this.tacheRepository.findById(
                        enregistrementDTO.getTache()
                ).orElse(null)
        );

        enregistrementDB.setDevis(
                this.devisRepository.findById(
                        enregistrementDTO.getDevis()
                ).orElse(null)
        );

        this.enregistrementRepository.save(enregistrementDB);

        serverResponse.setMessage("Enregistrement dans la facture cree");
        serverResponse.setStatus(true);

        return ResponseEntity.ok(serverResponse);
    }

    @Override
    public ResponseEntity<List<Enregistrement>> findEnregistrementByDevis(Integer id) {

        return ResponseEntity.ok(this.enregistrementRepository.findByDevis(
                this.devisRepository.findById(id).orElse(null)
        ));
    }

    @Override
    public ResponseEntity<List<Tache>> findAllTacheByDevis(Integer id) {
        //Recupere toutes les taches d,un devis
        return ResponseEntity.ok(
                this.enregistrementRepository.findAllTacheByDevis(
                        id
                )
        );
    }

    @Override
    public ResponseEntity<List<Enregistrement>> findEnregistrementByDevisAndTache(Integer idDevis, Integer idTache) {
        return ResponseEntity.ok(
                this.enregistrementRepository.findByDevisAndTache(
                        this.devisRepository.findById(idDevis).orElse(null),
                        this.tacheRepository.findById(idTache).orElse(null)
                )
        );
    }

    @Override
    public ResponseEntity<ServerResponse> impressionCompleteDevis(Integer id) throws MalformedURLException, FileNotFoundException {
        Devis devis =  this.devisRepository.findById(id).orElse(null);
        List<Enregistrement> enregistrementList = this.enregistrementRepository.findByDevis(devis);
        GeneratePDF generatePDF  = new GeneratePDF();

        for (int i = 0; i < enregistrementList.size(); i++) {
            generatePDF.generate( enregistrementList.get(i).getDevis() ,enregistrementList.get(i).getTache(), enregistrementList );
            System.out.println("Impression Pour la tache "+enregistrementList.get(i).getTache().getIntitule());
        }

        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setStatus(true);
        serverResponse.setMessage("Impression complete du Devis "+ devis.getId() + " du client "+ devis.getClient().getNom() );

        return ResponseEntity.ok(serverResponse);
    }

    @Override
    public ResponseEntity<ServerResponse> impressionTacheByDevis(Integer devisid, Integer tacheid) throws MalformedURLException, FileNotFoundException {
        Devis devis = this.devisRepository.findById(devisid).orElse(null) ;
        Tache tache = this.tacheRepository.findById(tacheid).orElse(null);
        List<Enregistrement> enregistrementList = this.enregistrementRepository.findByDevisAndTache(devis,tache);
        GeneratePDF generatePDF  = new GeneratePDF();
        generatePDF.generate(
                devis,
                tache,
                enregistrementList
        );

        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setMessage(
                "Devis de la tache : " + tache.getIntitule() + "du client " + devis.getClient().getNom() + " a ete realiser avec succes..."
        );
        serverResponse.setStatus(true);

        return ResponseEntity.ok(serverResponse);
    }

    @Override
    public ResponseEntity<Resource> telechargementComplet(Integer idDevis , Integer idTache) throws FileNotFoundException {
        //Recuperation du du client
        Devis devis = this.devisRepository.findById(
                idDevis
        ).orElse(null);

        Client client = devis.getClient();

        //Recuperation de la tache
        Tache tache = this.tacheRepository.findById(
                idTache
        ).orElse(null);

        //File directory with client name ;

        String fileDirectory = "D:\\Projet Devis Facture\\eFacture\\"+client.getNom();


        //File name with Tache

        String filename = devis.getClient().getNom()+" "+tache.getIntitule()+".pdf";
        File file = new File(fileDirectory + File.separator + filename);

        //Lancement proprement dit du telechargement

        if (!file.exists()) {
            //Si le fichier n'existe pas le dossier indexe
            throw new FileNotFoundException("Fichier "+filename+" non trouve dans le dossier : " + fileDirectory);
        }

        //Sinon on renvoie la resource

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);


    }


}
