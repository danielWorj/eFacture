package com.server.eFacture.Repository;

import com.server.eFacture.Entity.Devis.Devis;
import com.server.eFacture.Entity.Devis.Enregistrement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EnregistrementRepository extends JpaRepository<Enregistrement,Integer> {

    @Query(value = "SELECT e FROM Enregistrement e JOIN e.devis d JOIN e.tache t WHERE d=:devis ")
    List<Enregistrement> findByDevisGroupByTache(Devis devis);
}
