import { Client } from "../Entreprise/Client";
import { Technicien } from "../Entreprise/Technicien";

export interface Devis{
    id :number ; 
    client : Client; 
    technicien : Technicien ; 
}