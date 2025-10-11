import { Component, signal } from '@angular/core';
import { DevisService } from '../../Core/Services/Devis/devis-service';
import { Devis } from '../../Core/Model/Devis/Devis';
import { ServerResponse } from '../../Core/Model/Server Response/ServerResponse';
import { Tache } from '../../Core/Model/Devis/Tache';
import { Enregistrement } from '../../Core/Model/Devis/Enregistrement';

@Component({
  selector: 'app-facture',
  imports: [],
  templateUrl: './facture.html',
  styleUrl: './facture.css'
})
export class Facture {

  constructor(private devisService : DevisService){
    this.getAllDevis();
    this.countDevis();
  }

 listDevis = signal<Devis[]>([]); 
 getAllDevis(){
    this.devisService.getAllDevis().subscribe({
      next:(data : Devis[])=>{

        this.listDevis.set(data); 

      }
    })
 }

 nombreOfDevis = signal<number>(0);

 countDevis(){
  
  let idTechnicien = parseInt(sessionStorage.getItem('technicien')!); 
  
  console.log(`${idTechnicien}`);

  this.devisService.countDevis(idTechnicien).subscribe({
    next : (data:number)=>{
      this.nombreOfDevis.set(data);
    }
  })
 }
 devisClicked = signal<Devis>({
   id: 0,
   client: { id: 0, nom: '', telephone: '', localisation: '' },
   technicien: { id: 0, nom: '', telephone: '' , localisation: '' , password:'' },
   // Add other required Devis properties here if needed
 }); 
 getFactureInfo(id:number){
  this.devisService.devisById(id).subscribe({
    next:(data : Devis)=>{
      this.devisClicked.set(data); 
      
      this.calculmontantTotal(id); 
    }, 
    error:()=>{
      console.log('Find devis by id : failed');
    }
  })
 }

 montantTotal = signal<number>(0);
 calculmontantTotal(id:number){
  this.montantTotal.set(0);
  //On recupere d'abord 
  let listEnregistrement : Enregistrement[] = [];
  this.devisService.fetcHAllEnrigrementByDevis(id).subscribe({
    next:(data:Enregistrement[])=>{
      listEnregistrement = data;
      for (let j = 0; j < data.length; j++) {
        console.log(`le montant total a ${j} est ${this.montantTotal}`); 

        this.montantTotal.set(this.montantTotal() + (data[j].quantite!*data[j].materiel.prixUnitaire!)); 

  
      }

        console.log(`le montant total est ${this.montantTotal}`); 

    }
  })

 }

telechargementTotaleByDevis(idDevis :number){
  //1- Lance l'impression 

  //2- Lancement du telechargement 

  //2a - Liste des taches par devis
  //2b - Lancement du telechargement par devis et tache
  //let idDevis = 1; 
    if (idDevis!=0) {
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

    this.devisService.impressionRecapitulatif(idDevis).subscribe({
      next:(data :ServerResponse)=>{
        if (data) {
          console.log('Le recapitulatif a ete imprime'); 

          //On procede au telechargement 
          this.downloadPdfRecapitulatif(idDevis,  devisById.client.nom);
        }
      }
    })

    }else{
      alert("Terminer l'elaboration du devis avant de le telecharger")
    }

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

downloadPdfRecapitulatif(idDevis:number,  nomClient:string ){
  this.devisService.telechargementRecapitulatifByDevis(idDevis).subscribe(blob => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = nomClient+" Recapitulatif.pdf";  // nom du fichier
      a.click();
      window.URL.revokeObjectURL(url);
      console.log('Telechargement reussie');
  })
 }
}
