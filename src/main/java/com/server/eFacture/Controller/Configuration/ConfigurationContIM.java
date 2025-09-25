package com.server.eFacture.Controller.Configuration;

import com.server.eFacture.Entity.Entreprise.Materiel;
import com.server.eFacture.Entity.Entreprise.Tache;
import com.server.eFacture.Repository.MaterielRepository;
import com.server.eFacture.Repository.TacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ConfigurationContIM implements ConfigurationContIn{
    @Autowired
    private MaterielRepository materielRepository;
    @Autowired
    private TacheRepository tacheRepository;

    @Override
    public ResponseEntity<List<Materiel>> findMateriel() {
        return new ResponseEntity<>(this.materielRepository.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Tache>> findTache() {
        return ResponseEntity.ok(this.tacheRepository.findAll());
    }
}
