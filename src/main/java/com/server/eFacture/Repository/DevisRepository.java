package com.server.eFacture.Repository;

import com.server.eFacture.Entity.Devis.Client;
import com.server.eFacture.Entity.Devis.Devis;
import com.server.eFacture.Entity.Entreprise.Technicien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DevisRepository extends JpaRepository<Devis,Integer> {
    List<Devis> findByTechnicien(Technicien technicien);
    List<Devis> findByClient(Client client);

    @Query(value = "SELECT d FROM Devis d ORDER BY d.id DESC LIMIT 1")
    Devis lastDevisSave();
}
