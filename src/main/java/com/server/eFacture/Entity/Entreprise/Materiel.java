package com.server.eFacture.Entity.Entreprise;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Materiel {
    //Le different materiel disponible en bd
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String intitule ;
    private String unite ;
    private Integer prixUnitaire ;
    private String Description ;
}
