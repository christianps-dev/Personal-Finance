import { UserRequestDTO } from '../../models/auth-dto/user-request';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserRegisterDTO } from '../../models/auth-dto/user-register';
import { Router } from '@angular/router';
import { Enviroment } from '../../../enviroment';

@Injectable({
  providedIn: 'root',
})
export class RegisterUser {
  registerUserURL = Enviroment.apiURL + "auth/register";

  constructor(private http: HttpClient, private router: Router) {}

  registerNewUser(user: UserRegisterDTO): any {

    this.http.post<UserRequestDTO>(this.registerUserURL, user).subscribe({
      next: () => {
        alert("Success user registration "),
        this.router.navigate(["/login"])
      },
      error: (err) => alert("Registration error "+ err)
    }
    );
    return ;

  }
}


