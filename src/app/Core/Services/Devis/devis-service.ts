import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Devis } from '../../Model/Devis/Devis';
import { eFactureEndPoints } from '../../Constants/EndPoints';
import { ServerResponse } from '../../Model/Server Response/ServerResponse';
import { Enregistrement } from '../../Model/Devis/Enregistrement';
import { Tache } from '../../Model/Devis/Tache';


@Injectable({
  providedIn: 'root'
})
export class DevisService {
  constructor(private http :HttpClient){}

  getAllDevis():Observable<Devis[]>{
    return this.http.get<Devis[]>(eFactureEndPoints.Devis.all); 
  }
  creationDevis(request :any){
    return this.http.post<Devis>(eFactureEndPoints.Devis.creation, request); 
  }

  countDevis(idTechnicien :number):Observable<number>{
    return this.http.get<number>(eFactureEndPoints.Devis.count+"/"+idTechnicien);
  }
  devisById(id:number):Observable<Devis>{
    return this.http.get<Devis>(eFactureEndPoints.Devis.id+"/"+id);
  }

  devisConstructor(request:any){
    return this.http.post<ServerResponse>(eFactureEndPoints.Devis.construction,request); 
  }

  fetchAllEnregistrementByDevisAndTache(idDevis :number, idTache:number){
    return this.http.get<Enregistrement[]>(`${eFactureEndPoints.Enregistrement.allByDevisAndTache}/${idDevis}/${idTache}`);
  }

  fetcHAllEnrigrementByDevis(idDevis :number):Observable<Enregistrement[]>{
    return this.http.get<Enregistrement[]>(`${eFactureEndPoints.Enregistrement.byDevis}/${idDevis}`);
  }

  impressionCompleteDevis(devis :number):Observable<ServerResponse>{
    return this.http.get<ServerResponse>(eFactureEndPoints.Enregistrement.impressionComplete+'/'+devis);
  }

  impressionPartielDevisAndTache(devis:number, tache :number):Observable<ServerResponse>{
    return this.http.get<ServerResponse>(eFactureEndPoints.Enregistrement.impressionTache+'/'+devis+'/'+tache); 
  }

  download(filename:string):Observable<any>{
       // return this.http.get<any>(eFactureEndPoints.Enregistrement.download+'/'+filename);
    return this.http.get(eFactureEndPoints.Enregistrement.download+'/'+filename, { responseType: 'blob' }); 
  }

  getAllTacheByDevis(idDevis :number):Observable<Tache[]> {
    return this.http.get<Tache[]>(eFactureEndPoints.Tache.allTacheByDevis+"/"+idDevis); 
  }

  telechargementByDevisAndTache(idDevis :number , idTache:number):Observable<any>{
    return this.http.get(eFactureEndPoints.Enregistrement.telechargementByDevisAndTache+"/"+idDevis+"/"+idTache, { responseType: 'blob' });
  }

  impressionRecapitulatif(idDevis :number):Observable<ServerResponse>{
    return this.http.get<ServerResponse>(eFactureEndPoints.Enregistrement.impressionRecapitulatif+"/"+idDevis); 
  }

  telechargementRecapitulatifByDevis(idDevis :number):Observable<any>{
    return this.http.get(eFactureEndPoints.Enregistrement.telechargementRecapitulatif+"/"+idDevis, { responseType: 'blob' });
  }


}
