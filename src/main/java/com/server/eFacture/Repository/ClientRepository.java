package com.server.eFacture.Repository;

import com.server.eFacture.Entity.Devis.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClientRepository extends JpaRepository<Client,Integer> {

    @Query(value = "SELECT c FROM Client c ORDER BY c.id DESC LIMIT 1")
    Client lastClientSave();
}
