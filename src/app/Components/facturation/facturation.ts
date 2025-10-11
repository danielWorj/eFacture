import { Component, OnInit, signal } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ServerResponse } from '../../Core/Model/Server Response/ServerResponse';
import { Enregistrement } from '../../Core/Model/Devis/Enregistrement';
import { Devis } from '../../Core/Model/Devis/Devis';
import { Materiel } from '../../Core/Model/Entreprise/Materiel';
import { DevisService } from '../../Core/Services/Devis/devis-service';
import { ConfigurationService } from '../../Core/Services/Configuration/configuration-service';
import { DatePipe, DecimalPipe } from '@angular/common';
import { Tache } from '../../Core/Model/Devis/Tache';
import { Client } from '../../Core/Model/Entreprise/Client';
// Ensure that Client is exported as a class or interface in its module.


@Component({
  selector: 'app-facturation',
  imports: [ReactiveFormsModule , DatePipe],
  templateUrl: './facturation.html',
  styleUrl: './facturation.css'
})
export class Facturation implements OnInit{
 

  devisFb! : FormGroup; 
  enregistrementFb!:FormGroup ; 
  

  constructor(private fb :FormBuilder , private devisService :DevisService , private configService :ConfigurationService){
    this.devisFb = this.fb.group({
      id : new FormControl(),
      client : new FormControl(),
      technicien : new FormControl(),
      telephone : new FormControl()
    });

    this.enregistrementFb = this.fb.group({
      id : new FormControl(),
      devis : new FormControl(),
      tache : new FormControl(),
      materiel : new FormControl(),
      quantite : new FormControl(),
    });
  }


 listMateriel = signal<Materiel[]>([]); 

 currentDate = new Date() ; 
 ngOnInit(): void {
    this.getAllTache(); 
    this.getAllMateriel();
 }

 client :string = ''; 
 setClient(e:any){
  this.client = e?.target.value; 
 }



 materiel:number =0; 
 setMateriel(e:any){
  this.materiel = parseInt(e.target.value);
 }

 devis = signal<number>(0);
 devisId = 0 ; 
 clientIsCreated = signal(false) ; 
 clientInTreatment = signal<Client>({ id: 0, nom: '', telephone: '', localisation: '' }); 

 declarationDevis(){
  

  const formData :FormData = new FormData; 


  const idTechnicien = localStorage.getItem('technicien') || '';
  
  //console.log(`Id : ${idTechnicien}`); 

  this.devisFb.controls['technicien'].setValue(idTechnicien);

  console.log(this.devisFb.value);

  formData.append("devis", JSON.stringify(this.devisFb.value)); 
  
  this.devisService.creationDevis(formData).subscribe({
      next : (data:Devis)=>{
        this.clientInTreatment.set(data.client);

        this.devis.set(data.id);

        this.devisId = data.id;

        this.clientIsCreated.set(true); 

        this.getAllTache(); 

        this.getAllMateriel( ) ;

        alert('Le client a bien ete creee');
        //alert('Client Cree');


      } ,
      error : ()=>{
        console.log('Erreur de creation de devis');
      }
  })
 }

 validation(){
   this.getAllMateriel();

 }
 constructionDevis(){
  //alert(this.tache);
  if (this.tache!=0 ) {
      const formData : FormData = new FormData; 

      console.log(this.enregistrementFb.value); 

      
      this.enregistrementFb.controls['devis'].setValue(this.devisId);
      this.enregistrementFb.controls['tache'].setValue(this.tache);


      formData.append("enregistrement", JSON.stringify(this.enregistrementFb.value)); 

      console.log(this.enregistrementFb.value);

      this.devisService.devisConstructor(formData).subscribe({
        next : (data:ServerResponse)=>{
                this.getAllEnregistrementByDevisAndTache(this.devisId,this.tache);

              if (data.status) {

                console.log(data.message);
                
                // this.getAllEnregistrementByDevisAndTache(this.devis,this.tache);

              }
          } ,
          error : ()=>{
            console.log('Erreur de construction du devis');
          }
      })
  }else{
    alert('Veillez choisir une tache a realiser')
  }
 }

tache:number = 0; 
setTache(e:any){
  this.tache = parseInt(e.target.value);

   this.activeDevisOptions(this.devisId,this.tache);

  // this.getAllMateriel();
 }
activeDevisOptions(idDevis : number,idTache :number , ){
  this.getAllMateriel();
  this.getAllEnregistrementByDevisAndTache(idDevis,idTache); 
 }
listEnregistrement = signal<Enregistrement[]>([]); 
 getAllEnregistrementByDevisAndTache(idDevis :any, idTache :any){
 //alert('Aleeeerrtttttt');
  this.devisService.fetchAllEnregistrementByDevisAndTache(idDevis,idTache).subscribe({
    next : (data :Enregistrement[])=>{
      this.listEnregistrement.set(data); 
      console.log(this.listEnregistrement);

      this.calculSomme()//Methode qui calcule la somme totale
    }
  });
 }


 getAllMateriel(){
  this.configService.getListMateriel().subscribe({
      next:(data:Materiel[])=>{
        this.listMateriel.set(data); 
        console.log(this.listMateriel);
      }, 
      error(err) {
        console.log('Liste materiel : failed'); 
      },
    }
  )
 }

 listTache= signal<Tache[]>([]); 
 getAllTache(){
  this.configService.getListTache().subscribe({
    next :(data:Tache[])=>{
        this.listTache.set(data); 
    }, 
    error(err) {
      console.log('Liste tache : failed'); 
    },
  })
 }

 isImprimer :boolean = false ; 

 impressinPartielParTache(){
  this.devisService.impressionPartielDevisAndTache(this.devisId,this.tache).subscribe({
    next :(data:ServerResponse)=>{
      if (data.status) {
        console.log('Impression de la tache : reussi')
      }
    }, 
    error : ()=>{
      console.log('Erreur d impression : failed'); 
    }
  });
 }

 impressionTotale(){
  this.devisService.impressionCompleteDevis(this.devis()).subscribe({
    next :(data :ServerResponse)=>{
      if (data.status) {
        console.log('Impression Complete : reussi');
      }
    }, 
    error : ()=>{
      console.log('Impression complete : failed'); 
    }
  })
 }

 sommeEnregistrement = signal<number>(0); 
 listEnregistre : Enregistrement[] = []; 

 calculSomme(){
  let somme = 0;
  const enregistrements = this.listEnregistrement();
  for (let i = 0; i < enregistrements.length; i++) {
    const enregistrement = enregistrements[i];

    somme = somme + (enregistrement.materiel.prixUnitaire! * enregistrement.quantite);

    console.log(somme);
  }
  this.sommeEnregistrement.set(somme);
  console.log(this.sommeEnregistrement());
 }

 telechargement(){
  const filename="doku.jpeg"; 

  // this.devisService.download(filename).subscribe({
  //   next :(data:any)=>{
  //     console.log('telechargement du fichier en cours');
  //   }
  // })

  this.devisService.download(filename).subscribe(blob => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = filename; // nom du fichier
      a.click();
      window.URL.revokeObjectURL(url);
  });
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
