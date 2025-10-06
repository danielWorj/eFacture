package com.server.eFacture.Controller.Devis;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.server.eFacture.Entity.Devis.Devis;
import com.server.eFacture.Entity.Devis.Enregistrement;
import com.server.eFacture.Entity.Entreprise.Materiel;
import com.server.eFacture.Entity.Response.ServerResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("efacture/devis/")
@CrossOrigin("*")
public interface DevisControllerIN {

    @PostMapping("/creation")
    ResponseEntity<Devis> createDevis(@RequestParam("devis") String devis) throws JsonProcessingException;

    @PostMapping("/construction")
    ResponseEntity<ServerResponse> constructionDevis(@RequestParam("enregistrement") String enregistrement) throws  JsonProcessingException ;
    @GetMapping("/bydevis/{id}")
    ResponseEntity<List<Enregistrement>> findEnregistrementByDevis(@PathVariable Integer id);
    @GetMapping("/impression-enregistrement/{idDevis}/{idTache}")
    ResponseEntity<List<Enregistrement>> findEnregistrementByDevisAndTache(@PathVariable Integer idDevis, @PathVariable Integer idTache);
    @GetMapping("/impression-complete/{id}")
    ResponseEntity<ServerResponse> impressionCompleteDevis(@PathVariable Integer id) throws MalformedURLException, FileNotFoundException;

    @GetMapping("/impression-tache/{devisid}/{tacheid}")
    ResponseEntity<ServerResponse> impressionTacheByDevis(@PathVariable Integer devisid,@PathVariable Integer tacheid ) throws MalformedURLException, FileNotFoundException;

}
