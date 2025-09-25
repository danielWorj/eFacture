package com.server.eFacture.Entity.Devis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.server.eFacture.Entity.Entreprise.Materiel;
import com.server.eFacture.Entity.Entreprise.Tache;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;

@Entity
@Data
public class TacheMateriel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"HibernateLazyInitializer", "handler"})
    private Tache tache;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"HibernateLazyInitializer", "handler"})
    private Materiel materiel;

    private Integer quantite ;
}
