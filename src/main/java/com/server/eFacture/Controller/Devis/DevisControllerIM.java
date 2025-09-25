package com.server.eFacture.Controller.Devis;

import com.server.eFacture.Entity.Devis.Client;
import com.server.eFacture.Entity.Devis.Devis;
import com.server.eFacture.Entity.Devis.Enregistrement;
import com.server.eFacture.Entity.Entreprise.Materiel;
import com.server.eFacture.Entity.Entreprise.Technicien;
import com.server.eFacture.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

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

    @Override
    public ResponseEntity<String> postData() {

        /*Client client1 = new Client(1 ,"daniel", 656889897, "Douala");
        Client client2 = new Client(2 ,"Libert", 656889897, "Nanga");
        Client client3 = new Client(3 ,"Epoh", 656889897, "Bamenda");
        Client client4 = new Client(4 ,"Land", 656889897, "Douala");


        clientRepository.save(client1);
        clientRepository.save(client2);
        clientRepository.save(client3);
        clientRepository.save(client4);

        Technicien technicien = new Technicien(1, "Joel", 690987665 , "Edea");

        technicienRepository.save(technicien);*/
        return ResponseEntity.ok("Post data : success");
    }

    @Override
    public ResponseEntity<List<Enregistrement>> findEnregistrementByDevis(Integer id) {

        return ResponseEntity.ok(this.enregistrementRepository.findByDevisGroupByTache(
                this.devisRepository.findById(id).orElse(null)
        ));
    }

    @Override
    public ResponseEntity<List<Materiel>> findMateriel() {
        return new ResponseEntity<>(this.materielRepository.findAll(), HttpStatus.OK);
    }


}
