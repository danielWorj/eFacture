package com.server.eFacture.Repository;

import com.server.eFacture.Entity.Devis.Client;
import com.server.eFacture.Entity.Devis.Devis;
import com.server.eFacture.Entity.Entreprise.Technicien;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DevisRepository extends JpaRepository<Devis,Integer> {
    List<Devis> findByTechnicien(Technicien technicien);
    List<Devis> findByClient(Client client);
}
