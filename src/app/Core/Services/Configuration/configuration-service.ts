import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Materiel } from '../../Model/Entreprise/Materiel';
import { eFactureEndPoints } from '../../Constants/EndPoints';
import { Tache } from '../../Model/Devis/Tache';

@Injectable({
  providedIn: 'root'
})
export class ConfigurationService {
  constructor(private http : HttpClient){}

  getListMateriel():Observable<Materiel[]>{
    return this.http.get<Materiel[]>(eFactureEndPoints.Materiel.all);
  }

  getListTache():Observable<Tache[]>{
    return this.http.get<Tache[]>(eFactureEndPoints.Tache.all);
  }
}
