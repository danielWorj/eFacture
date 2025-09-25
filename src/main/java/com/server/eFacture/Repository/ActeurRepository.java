package com.server.eFacture.Repository;

import com.server.eFacture.Entity.Entreprise.Acteur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActeurRepository extends JpaRepository<Acteur,Integer> {
}
