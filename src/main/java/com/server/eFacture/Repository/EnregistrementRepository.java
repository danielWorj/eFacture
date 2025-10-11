package com.server.eFacture.Repository;

import com.server.eFacture.Entity.Devis.Devis;
import com.server.eFacture.Entity.Devis.Enregistrement;
import com.server.eFacture.Entity.Entreprise.Tache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnregistrementRepository extends JpaRepository<Enregistrement,Integer> {

    @Query(value = "SELECT e FROM Enregistrement e JOIN e.devis d JOIN e.tache t WHERE d=:devis ")
    List<Enregistrement> findByDevis(Devis devis);

    List<Enregistrement> findByDevisAndTache(Devis devis, Tache tache);

    @Query(value = "SELECT t FROM Enregistrement e JOIN e.devis d JOIN e.tache t WHERE d.id=:id ")
    List<Tache> findAllTacheByDevis(@Param("id") Integer id);

    @Query(value = "SELECT (e.quantite * m.prixUnitaire) FROM Enregistrement e " +
            "JOIN e.tache t JOIN e.devis d JOIN e.materiel m "+
            "WHERE d=:devis AND t=:tache  ")
    Long listMontantByTache(@Param("devis") Devis devis , @Param("tache") Tache tache);

}
