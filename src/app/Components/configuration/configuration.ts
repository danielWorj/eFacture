import { Component, signal } from '@angular/core';
import { FormBuilder, FormGroup, FormControl, ReactiveFormsModule } from '@angular/forms';
import { Materiel } from '../../Core/Model/Entreprise/Materiel';
import { RouterLink } from '@angular/router';
import { Tache } from '../../Core/Model/Devis/Tache';
import { ConfigurationService } from '../../Core/Services/Configuration/configuration-service';
import { ServerResponse } from '../../Core/Model/Server Response/ServerResponse';

@Component({
  selector: 'app-configuration',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './configuration.html',
  styleUrl: './configuration.css'
})
export class Configuration {
  
  

  listMateriel = signal<Materiel[]>([]);
  listTache = signal<Tache[]>([]);
  materielFb: FormGroup;
  tacheFb: FormGroup;

  constructor(private fb: FormBuilder , private configurationService : ConfigurationService) {
    this.materielFb = this.fb.group({
      id: new FormControl(''),
      intitule: new FormControl(''),
      description: new FormControl(0),
      unite: new FormControl(''),
      prixUnitaire: new FormControl(0)
    });

    this.tacheFb = this.fb.group({
      id: new FormControl(''),
      intitule: new FormControl(''),
      description: new FormControl(0),
      
    });

    this.getAllMateriel(); 

    this.getAllTache(); 
  }

  getAllMateriel(){
    this.configurationService.getListMateriel().subscribe({
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

  
  getAllTache(){
    this.configurationService.getListTache().subscribe({
      next :(data:Tache[])=>{
          this.listTache.set(data); 
      }, 
      error(err) {
        console.log('Liste tache : failed'); 
      },
    })
  }

  materielIsCreated=signal<Boolean>(false); 

  createMatiere(){
    const formData :FormData = new FormData(); 

    formData.append("materiel", JSON.stringify(this.materielFb.value)); 

    console.log(this.materielFb.value);

    this.configurationService.createMateriel(formData).subscribe({
      next:(data:ServerResponse)=>{
        this.materielIsCreated.set(true); 

        this.getAllMateriel(); 

        this.destroyMessageCreatedMateriel(60000);
      }, 
      error:()=>{
        console.error("Erreur de creation de materiel"); 
      }
    })
  }

  isTacheCreated = signal<boolean>(false); 
  createTache(){
    const formData :FormData = new FormData(); 

    formData.append("tache", JSON.stringify(this.tacheFb.value)); 
    this.configurationService.createTache(formData).subscribe({
      next:(data:ServerResponse)=>{
        this.isTacheCreated.set(true);

        this.getAllTache();

        this.destroyMessageCreatedTache(60000);
      }, 
      error:()=>{
        console.error("Erreur de creation de tache"); 
      }
    })
  }

  destroyMessageCreatedMateriel(timeExpirationToken :number) {
    setTimeout(() => {
      console.log('Suppression du message');
      this.materielIsCreated.set(false);
    }, timeExpirationToken); // 900000 ms = 15 minutes
  }

  
  destroyMessageCreatedTache(timeExpirationToken :number) {
    setTimeout(() => {
      console.log('Suppression du message');
      this.isTacheCreated.set(false);
    }, timeExpirationToken); // 900000 ms = 15 minutes
  }
}
