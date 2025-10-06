import { Component, OnInit, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Sidebar } from "./Layouts/sidebar/sidebar";
import { Navbar } from "./Layouts/navbar/navbar";
import { Auth } from "./Components/auth/auth";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Sidebar, Navbar, Auth],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  
  isAuth : boolean = false; 
  protected readonly title = signal('webview');

  ngOnInit(): void {
      this.immediatlyDestroyToken()
      this.checkIfTokenExists();
  }
  
  immediatlyDestroyToken(){
    localStorage.removeItem('token');
  }
  checkIfTokenExists(){
    let tokenChar :string = localStorage.getItem('token')!;
    console.log(tokenChar);
    if(localStorage.getItem('token')==null){

      console.log('Il n y a pas token');
    }else{
      //Dans le cas ou il y'a le token 
      this.isAuth = true;


    }
  }
  //Ici on va utiliser une donnne provenant de l enfant Auth pour modifier l etat de isAuth qui une variable du parent 

  changeAuthStatus(status : boolean){
    this.isAuth = status;

    console.log(this.isAuth);
  }
}
