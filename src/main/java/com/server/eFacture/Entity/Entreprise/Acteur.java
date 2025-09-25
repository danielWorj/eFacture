package com.server.eFacture.Entity.Entreprise;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "qualite") //Colonne de la differenciation
public class Acteur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nom ;
    private Integer telephone ;
    private String localisation ;
}
