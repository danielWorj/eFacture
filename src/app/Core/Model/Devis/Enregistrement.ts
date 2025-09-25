import { Materiel } from "../Entreprise/Materiel";
import { Devis } from "./Devis";
import { Tache } from "./Tache";

export interface Enregistrement{
    id:number; 
    devis :Devis; 
    tache :Tache ; 
    materiel : Materiel ; 
    quantite : number ; 
}