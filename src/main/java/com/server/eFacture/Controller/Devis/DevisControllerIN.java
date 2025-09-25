package com.server.eFacture.Controller.Devis;

import java.util.List;
import com.server.eFacture.Entity.Devis.Devis;
import com.server.eFacture.Entity.Devis.Enregistrement;
import com.server.eFacture.Entity.Entreprise.Materiel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("efacture/api/v1/devis/")
@CrossOrigin("*")
public interface DevisControllerIN {

    @GetMapping("/postdata")
    ResponseEntity<String> postData();

    @GetMapping("/bydevis/{id}")
    ResponseEntity<List<Enregistrement>> findEnregistrementByDevis(@PathVariable Integer id);

    @GetMapping("/materiel")
    ResponseEntity<List<Materiel>> findMateriel();
}
