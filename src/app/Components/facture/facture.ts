import { Component, signal } from '@angular/core';
import { DevisService } from '../../Core/Services/Devis/devis-service';
import { Devis } from '../../Core/Model/Devis/Devis';
import { ServerResponse } from '../../Core/Model/Server Response/ServerResponse';
import { Tache } from '../../Core/Model/Devis/Tache';

@Component({
  selector: 'app-facture',
  imports: [],
  templateUrl: './facture.html',
  styleUrl: './facture.css'
})
export class Facture {

  constructor(private devisService : DevisService){
    this.getAllDevis();
  }

  listDevis = signal<Devis[]>([]); 
  getAllDevis(){
    this.devisService.getAllDevis().subscribe({
      next:(data : Devis[])=>{

        this.listDevis.set(data); 

      }
    })
  }

   telechargementTotaleByDevis(idDevis :number){
  //1- Lance l'impression 

  //2- Lancement du telechargement 

  //2a - Liste des taches par devis
  //2b - Lancement du telechargement par devis et tache
  //let idDevis = 1; 
  let devisById : Devis ; 

  this.devisService.devisById(idDevis).subscribe({
    next :(data : Devis)=>{
      devisById = data; 
      console.log(devisById);
    }
  })

  this.devisService.impressionCompleteDevis(idDevis).subscribe({
    next :(data :ServerResponse)=>{
      console.log(data); 
      
      let listTache: Tache[] = []; 
        
      this.devisService.getAllTacheByDevis(idDevis).subscribe({
        next:(data :Tache[])=>{
          listTache = data; 
          console.log(listTache);
          //Lancement du telechargement par devis et tache

          for (let j = 0; j < listTache.length; j++) {
            this.downloadPdfDevisTache(idDevis, listTache[j].id, devisById.client.nom, listTache[j].intitule);  
          }
        }, 
        error : ()=>{
          console.error('List tache par Devis : failed'); 
        }
      })
    }, 
    error : ()=>{
      console.log('Impression complete : failed'); 
    }
  }); 


 }

 downloadPdfDevisTache(idDevis:number, idTache:number , nomClient:string , nomTache:string ){
  this.devisService.telechargementByDevisAndTache(idDevis,idTache).subscribe(blob => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = nomClient+" "+nomTache+".pdf"; // nom du fichier
      a.click();
      window.URL.revokeObjectURL(url);
      console.log('Telechargement reussie');
  })
 }

}
