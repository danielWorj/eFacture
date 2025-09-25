import { Component, OnInit, signal } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ServerResponse } from '../../Core/Model/Server Response/ServerResponse';
import { Enregistrement } from '../../Core/Model/Devis/Enregistrement';
import { Devis } from '../../Core/Model/Devis/Devis';
import { Materiel } from '../../Core/Model/Entreprise/Materiel';
import { DevisService } from '../../Core/Services/Devis/devis-service';
import { ConfigurationService } from '../../Core/Services/Configuration/configuration-service';
import { DatePipe } from '@angular/common';
import { Tache } from '../../Core/Model/Devis/Tache';


@Component({
  selector: 'app-facturation',
  imports: [ReactiveFormsModule],
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
    });

    this.enregistrementFb = this.fb.group({
      id : new FormControl(),
      devis : new FormControl(),
      tache : new FormControl(),
      materiel : new FormControl(),
      quantite : new FormControl(),
    });
  }


 listMateriel:Materiel[] = []; 

 currentDate = new Date() ; 
 ngOnInit(): void {

 }

 client :string = ''; 
 setClient(e:any){
  this.client = e?.target.value; 
 }

 tache:number = 0; 
 setTache(e:any){
  this.tache = e.target.value;

  this.getAllEnregistrementByDevisAndTache(this.devis,this.tache);

  this.getAllMateriel();
 }

 materiel:number =0; 
 setMateriel(e:any){
  this.materiel = e.target.value;
  alert(this.materiel);
 }

 devis :number = 0 ; 
 declarationDevis(){
  const formData :FormData = new FormData; 

  console.log(this.devisFb.value);

  formData.append("devis", JSON.stringify(this.devisFb.value)); 

  this.devisService.creationDevis(formData).subscribe({
      next : (data:Devis)=>{
        this.devis = 1;
  
        if (this.devis!=0) {
          alert('ouverture');
        }
        console.log(this.devis);

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
  // const formData : FormData = new FormData; 

  // console.log(this.enregistrementFb.value); 

  // this.enregistrementFb.controls['devis'].setValue(this.devis);
  // this.enregistrementFb.controls['tache'].setValue(this.tache);


  // formData.append("enregistrement", JSON.stringify(this.enregistrementFb.value)); 

  // this.devisService.devisConstructor(formData).subscribe({
  //    next : (data:ServerResponse)=>{
  //         if (data.status) {

  //           console.log(data.message);


  //         }
  //     } ,
  //     error : ()=>{
  //       console.log('Erreur de construction du devis');
  //     }
  // })
 }

 activeDevisOptions(idDevis : number,idTache :number , ){
  this.getAllMateriel();
  this.getAllEnregistrementByDevisAndTache(idDevis,idTache); 
 }
listEnregistrement :Enregistrement[] = []; 
 getAllEnregistrementByDevisAndTache(idDevis :any, idTache :any){
 

  this.devisService.fetchAllEnregistrementByDevisAndTache(idDevis,idTache).subscribe({
    next : (data :Enregistrement[])=>{
      this.listEnregistrement = data; 
      console.log(this.listEnregistrement);
    }
  });
 }


 getAllMateriel(){
  this.configService.getListMateriel().subscribe({
      next:(data:Materiel[])=>{
        this.listMateriel = data; 
        console.log(this.listMateriel);
      }, 
      error(err) {
        console.log('Liste materiel : failed'); 
      },
    }
  )
 }

 listTache : Tache[] = []; 
 getAllTache(){
  this.configService.getListTache().subscribe({
    next :(data:Tache[])=>{
        this.listTache = data; 
        console.log(this.listMateriel);
    }, 
    error(err) {
      console.log('Liste materiel : failed'); 
    },
  })
 }

  
}
