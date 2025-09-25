import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-auth',
  imports: [],
  templateUrl: './auth.html',
  styleUrl: './auth.css'
})
export class Auth {

  //Cette donnee va transiter de l`enfant vers le parent 
  @Output() connectionStatus = new EventEmitter<boolean>();
  
  loginBtn(){
    this.connectionStatus.emit(true);
  }
}
