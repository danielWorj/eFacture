package com.server.eFacture.Controller.Configuration;

import com.server.eFacture.Entity.Entreprise.Materiel;
import com.server.eFacture.Entity.Entreprise.Tache;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("efacture/api/v1/configuration/")
@CrossOrigin("*")
public interface ConfigurationContIn {

    @GetMapping("/materiel/all")
    ResponseEntity<List<Materiel>> findMateriel();

    @GetMapping("/tache/all")
    ResponseEntity<List<Tache>> findTache();
}
