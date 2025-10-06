import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthData } from '../../Model/Auth/AuthData';

import { eFactureEndPoints } from '../../Constants/EndPoints';
import { ServerResponse } from '../../Model/Server Response/ServerResponse';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(private http :HttpClient){}

   registration(request:any):Observable<ServerResponse>{
    console.log('Registration ...');
    return this.http.post<ServerResponse>(eFactureEndPoints.Auth.register, request); 
  }


  login(request:any):Observable<AuthData>{
    console.log('Login ...');
    return this.http.post<AuthData>(eFactureEndPoints.Auth.login, request); 
  }

  fetch(request:any):Observable<number>{
    return this.http.post<number>(eFactureEndPoints.Auth.fetch, request);
  }
  
}
