package com.server.eFacture.Repository;

import com.server.eFacture.Entity.Entreprise.Materiel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterielRepository extends JpaRepository<Materiel,Integer> {
}
