package com.server.eFacture.Controller.Devis;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.server.eFacture.DTO.JSON.TacheMontantDTO;
import com.server.eFacture.Entity.Devis.Devis;
import com.server.eFacture.Entity.Devis.Enregistrement;
import com.server.eFacture.Entity.Entreprise.Materiel;
import com.server.eFacture.Entity.Entreprise.Tache;
import com.server.eFacture.Entity.Response.ServerResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("efacture/devis/")
@CrossOrigin("*")
public interface DevisControllerIN {

    @PostMapping("/creation")
    ResponseEntity<Devis> createDevis(@RequestParam("devis") String devis) throws JsonProcessingException;
    @GetMapping("/all")
    ResponseEntity<List<Devis>> getAllDevis();
    @GetMapping("/count/{idTechnicien}")
    ResponseEntity<Integer> sumTotalDevis(@PathVariable Integer idTechnicien);
    @GetMapping("/byid/{id}")
    ResponseEntity<Devis> findDevisById(@PathVariable Integer id);
    @PostMapping("/construction")
    ResponseEntity<ServerResponse> constructionDevis(@RequestParam("enregistrement") String enregistrement) throws  JsonProcessingException ;
    @GetMapping("/bydevis/{id}")
    ResponseEntity<List<Enregistrement>> findEnregistrementByDevis(@PathVariable Integer id);
    @GetMapping("allTache/{id}")//Toute les tache du devis id
    ResponseEntity<List<Tache>> findAllTacheByDevis(@PathVariable Integer id);
    @GetMapping("/impression-enregistrement/{idDevis}/{idTache}")
    ResponseEntity<List<Enregistrement>> findEnregistrementByDevisAndTache(@PathVariable Integer idDevis, @PathVariable Integer idTache);
    @GetMapping("/impression-complete/{id}")
    ResponseEntity<ServerResponse> impressionCompleteDevis(@PathVariable Integer id) throws MalformedURLException, FileNotFoundException;

    @GetMapping("/impression-tache/{devisid}/{tacheid}")
    ResponseEntity<ServerResponse> impressionTacheByDevis(@PathVariable Integer devisid,@PathVariable Integer tacheid ) throws MalformedURLException, FileNotFoundException;

    @GetMapping("/telechargement-devis-tache/{idDevis}/{idTache}")//id ici c'est l'id du devis
    ResponseEntity<Resource> telechargementComplet(@PathVariable Integer idDevis , @PathVariable Integer idTache ) throws FileNotFoundException;

    @GetMapping("/impression-recapitulatif/{iddevis}")
    ResponseEntity<ServerResponse> impressionRecapitulatif(@PathVariable Integer iddevis) throws MalformedURLException, FileNotFoundException;

    @GetMapping("/telechargement-devis-recapitulatif/{id}")//id ici c'est l'id du devis
    ResponseEntity<Resource> telechargementRecapitulatifByDevis(@PathVariable Integer id) throws FileNotFoundException;

}
