package com.server.eFacture;

import com.server.eFacture.Entity.Devis.Client;
import com.server.eFacture.Entity.Entreprise.Technicien;
import com.server.eFacture.Repository.ClientRepository;
import com.server.eFacture.Repository.TechnicienRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EFactureApplication {

	public static void main(String[] args) {

		SpringApplication.run(EFactureApplication.class, args);
	}

}
