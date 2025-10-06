import { Component, EventEmitter, Input, Output, signal } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, ɵInternalFormsSharedModule } from '@angular/forms';
import { AuthService } from '../../Core/Services/Auth/auth-service';
import { ServerResponse } from '../../Core/Model/Server Response/ServerResponse';
import { AuthData } from '../../Core/Model/Auth/AuthData';


@Component({
  selector: 'app-auth',
  imports: [ɵInternalFormsSharedModule , ReactiveFormsModule],
  templateUrl: './auth.html',
  styleUrl: './auth.css'
})
export class Auth {
  technicienForm!:FormGroup; 
  authForm!:FormGroup;
  //Cette donnee va transiter de l`enfant vers le parent 
  @Output() connectionStatus = new EventEmitter<boolean>();
  
  isRegistration = signal(false); 

  constructor(private fb:FormBuilder,  private authService :AuthService){
    this.authForm = this.fb.group({
      type :new FormControl(), 
      token : new FormControl()
    }); 
    this.technicienForm = this.fb.group({
      id :new FormControl(),
      nom :new FormControl(),
      telephone :new FormControl(),
      role :new FormControl(),
      password :new FormControl(),
      localisation :new FormControl(),
    })
  }

  sendToRegistration(){
    this.isRegistration.set(true); 
  }

  sendToLogin(){
    this.isRegistration.set(false);
  }


  
  authentification(){
    if (this.isRegistration()) {
      //Register
        const formData :FormData = new FormData(); 

        this.technicienForm.controls['role'].setValue("technicien");

        formData.append("technicien", JSON.stringify(this.technicienForm.value));

        this.authService.registration(formData).subscribe({
          next:(data:ServerResponse)=>{
            console.log(data);
            this.isRegistration.set(true); 
            // if (data.status) {
            //   this.isRegistration.set(true); 
            // }
          }, 
          error :()=>{
            console.error('Registration failed ...')
          }
        })
    }
    if (!this.isRegistration()) {
      //Login
        const formData :FormData = new FormData(); 

        formData.append("technicien", JSON.stringify(this.technicienForm.value));
        
        this.authService.login(formData).subscribe({
          next:(data:AuthData)=>{
            localStorage.setItem('token', data.token); 

            this.destroyToken(900000); // Lance le timer pour supprimer le token 

            this.connectionStatus.emit(true); 
            //this.getAllProduct();

            this.fetchTechnicien(data);

        
          }, 
          error :()=>{
            console.error('Registration failed ...')
          }
        })
        }
  }

  
  destroyToken(timeExpirationToken :number) {
    setTimeout(() => {
      localStorage.removeItem('token');
      console.log('Token supprimé du localStorage');
      this.connectionStatus.emit(false);
    }, timeExpirationToken); // 900000 ms = 15 minutes
  }

  fetchTechnicien(authdata : AuthData){
    const formData : FormData = new FormData(); 

    this.authForm.controls['type'].setValue(authdata.type);
    this.authForm.controls['token'].setValue(authdata.token);

    formData.append("authdata", JSON.stringify(this.authForm.value)); 

    this.authService.fetch(formData).subscribe({
      next:(data :number)=>{
        console.log(`Id du technicien : ${data}`);
        
        localStorage.setItem('technicien', data.toString());

      }, 
      error :()=>{
        console.log('Fetch technicien : failed'); 
      }
    })


  }
}
