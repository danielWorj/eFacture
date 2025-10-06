import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Devis } from '../../Model/Devis/Devis';
import { eFactureEndPoints } from '../../Constants/EndPoints';
import { ServerResponse } from '../../Model/Server Response/ServerResponse';
import { Enregistrement } from '../../Model/Devis/Enregistrement';


@Injectable({
  providedIn: 'root'
})
export class DevisService {
  constructor(private http :HttpClient){}

  creationDevis(request :any){
    return this.http.post<Devis>(eFactureEndPoints.Devis.creation, request); 
  }

  devisConstructor(request:any){
    return this.http.post<ServerResponse>(eFactureEndPoints.Devis.construction,request); 
  }

  fetchAllEnregistrementByDevisAndTache(idDevis :number, idTache:number){
    return this.http.get<Enregistrement[]>(`${eFactureEndPoints.Enregistrement.allByDevisAndTache}/${idDevis}/${idTache}`);
  }

  impressionCompleteDevis(devis :number):Observable<ServerResponse>{
    return this.http.get<ServerResponse>(eFactureEndPoints.Enregistrement.impressionComplete+'/'+devis);
  }

  impressionPartielDevisAndTache(devis:number, tache :number):Observable<ServerResponse>{
    return this.http.get<ServerResponse>(eFactureEndPoints.Enregistrement.impressionTache+'/'+devis+'/'+tache); 
  }
}
