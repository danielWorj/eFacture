package com.server.eFacture.Repository;

import com.server.eFacture.Entity.Devis.Client;
import com.server.eFacture.Entity.Devis.Devis;
import com.server.eFacture.Entity.Entreprise.Technicien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DevisRepository extends JpaRepository<Devis,Integer> {
    List<Devis> findByTechnicien(Technicien technicien);

    List<Devis> findByClient(Client client);

    @Query(value = "SELECT d FROM Devis d ORDER BY d.id DESC LIMIT 1")
    Devis lastDevisSave();

    @Query(value = "SELECT COUNT(d) FROM Devis d")
    Integer numberOfDevis();
    @Query(value = "SELECT COUNT(d) FROM Devis d WHERE d.status=:status ")
    Integer numberOfDevisByStatus(Boolean status);

    @Query(value = "SELECT SUM(d) FROM Devis d JOIN d.technicien t WHERE t.id=:id")
    Integer numberTotalDevis(@Param("id") Integer id);

//    @Query(value = "SELECT t , e.quantite*m.prixUnitaire FROM Enregistrement e " +
//            "JOIN e.tache t JOIN e.devis d JOIN e.materiel m "+
//            "WHERE d.id=:id GROUP BY t ")
//    List<?> listMontantByTache(@Param("id") Integer id);


}
