import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Materiel } from '../../Model/Entreprise/Materiel';
import { eFactureEndPoints } from '../../Constants/EndPoints';
import { Tache } from '../../Model/Devis/Tache';
import { ServerResponse } from '../../Model/Server Response/ServerResponse';

@Injectable({
  providedIn: 'root'
})
export class ConfigurationService {
  constructor(private http : HttpClient){}

  getListMateriel():Observable<Materiel[]>{
    return this.http.get<Materiel[]>(eFactureEndPoints.Materiel.all);
  }

  createMateriel(request:any):Observable<ServerResponse>{
    return this.http.post<ServerResponse>(eFactureEndPoints.Materiel.create, request);
  }

  getListTache():Observable<Tache[]>{
    return this.http.get<Tache[]>(eFactureEndPoints.Tache.all);
  }

  createTache(request:any):Observable<ServerResponse>{
    return this.http.post<ServerResponse>(eFactureEndPoints.Tache.create, request);
  }
}
