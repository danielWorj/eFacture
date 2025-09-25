package com.server.eFacture.Repository;

import com.server.eFacture.Entity.Devis.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client,Integer> {
}
