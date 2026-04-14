import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { UserLoginDTO } from '../../models/auth-dto/user-login-dto'
import { LoginUser } from '../../services/auth-user/login-user';
import { RouterLink } from "@angular/router";

@Component({
  selector: 'app-login-page',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './login-page.html',
  styleUrl: './login-page.css',
})
export class LoginPage {

    LoginForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required, Validators.minLength(8)])
    })

    constructor(private loginRequest: LoginUser){}

    loginUser(){
      const userLogin = this.LoginForm.value as UserLoginDTO;

      this.LoginForm.reset({
        email: "",
        password: ""
      })

      console.log(userLogin.password, userLogin.email);
      return this.loginRequest.siginUser(userLogin);
    }
}
