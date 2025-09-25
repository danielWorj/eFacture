package com.server.eFacture.Repository;

import com.server.eFacture.Entity.Entreprise.Technicien;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechnicienRepository extends JpaRepository<Technicien,Integer> {
    Technicien findByNom(String nom);
}
