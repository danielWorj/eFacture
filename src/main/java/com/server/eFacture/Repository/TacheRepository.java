package com.server.eFacture.Repository;

import com.server.eFacture.Entity.Entreprise.Tache;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TacheRepository extends JpaRepository<Tache,Integer> {
}
