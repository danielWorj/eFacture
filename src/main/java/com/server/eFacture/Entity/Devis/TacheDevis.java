package com.server.eFacture.Entity.Devis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.server.eFacture.Entity.Entreprise.Materiel;
import com.server.eFacture.Entity.Entreprise.Tache;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TacheDevis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"HibernateLazyInitializer", "handler"})
    private Tache tache;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"HibernateLazyInitializer", "handler"})
    private Devis devis;


}
