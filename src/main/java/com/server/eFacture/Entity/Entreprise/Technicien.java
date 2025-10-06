package com.server.eFacture.Entity.Entreprise;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
@DiscriminatorValue("technicien")
public class Technicien extends Acteur{
    //Classe qui permet la connexion du technicien à la plateforme

    private String password;
    private String role ;

}
