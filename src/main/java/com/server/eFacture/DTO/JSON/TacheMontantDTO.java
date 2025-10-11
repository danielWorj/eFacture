package com.server.eFacture.DTO.JSON;

import com.server.eFacture.Entity.Entreprise.Tache;
import lombok.Data;

@Data
public class TacheMontantDTO {
    private Tache tache;
    private Integer montant ;
}
