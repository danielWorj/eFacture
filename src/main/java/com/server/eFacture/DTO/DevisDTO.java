package com.server.eFacture.DTO;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class DevisDTO {
    private Integer id ;
    private String client ;
    private Integer telephone ;
    private Integer technicien ;
}
