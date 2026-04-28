import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { AuthUser } from '../../services/auth-user/auth-user';
import { UserRegisterDTO} from '../../models/auth-dto/user-register';
import { Router } from "@angular/router";

@Component({
  selector: 'app-signup-page',
  imports: [ReactiveFormsModule],
  templateUrl: './signup-page.html',
  styleUrl: './signup-page.css',
})
export class SignupPage {

  constructor(private authUser : AuthUser, private router : Router) {}

  registerForm = new FormGroup({
    username: new FormControl('',Validators.required),
    email: new FormControl('', [Validators.email, Validators.required]),
    password: new FormControl('', [Validators.minLength(8), Validators.required]),
  });

  registerUserSignUp(){
    const userDTO = this.registerForm.value as UserRegisterDTO;
    console.log(userDTO)
    this.registerForm.reset({
      username: "",
      email: "",
      password: ""
    })
    return this.authUser.registerNewUser(userDTO);
  }

  sendLoginpage(){
    this.router.navigate(['/login'])
  }

}
