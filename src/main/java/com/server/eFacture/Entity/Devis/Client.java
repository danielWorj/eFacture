package com.server.eFacture.Entity.Devis;

import com.server.eFacture.Entity.Entreprise.Acteur;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@DiscriminatorValue("client")
public class Client extends Acteur {

}
