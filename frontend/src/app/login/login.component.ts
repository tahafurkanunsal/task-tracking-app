import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../service/auth/auth.service';
import { StorageService } from '../service/storage/storage.service';



@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  loginForm!: FormGroup;
  hidePassword = true;

  constructor(
    private formBuilder : FormBuilder,
    private authService : AuthService,
    private snackBar : MatSnackBar,
    private router : Router){
  }

  ngOnInit(): void{
    this.loginForm = this.formBuilder.group({
      email : [null , [Validators.required]],
      password : [null , [Validators.required]],
    })
  }

  togglePasswordVisibility(){
    this.hidePassword = !this.hidePassword;
  }

  onSubmit(): void{
    const email = this.loginForm.get('email')!.value;
    const password = this.loginForm.get('password')!.value;

    this.authService.login(email , password).subscribe(
      (response) =>{ 
        console.log(response);
        if(StorageService.isAdminLoggedIn()){
          this.router.navigateByUrl('admin/dashboard');
        }else if(StorageService.isUserLoggedIn()){
          this.router.navigateByUrl('user/dashboard');
        }else if(StorageService.isCompanyAdminLoggedIn()){
          this.router.navigateByUrl('company-admin/dashboard');
        }
      },
      (error) => {
        this.snackBar.open('Bad credentials' , 'ERROR', {duration: 5000});
      }
    )

  } 

}