package com.server.eFacture.Entity.Response;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class ServerResponse {
    private String message ;
    private Boolean status ;
}
