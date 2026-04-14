import { UserLoginDTO } from '../../models/auth-dto/user-login-dto';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Enviroment } from '../../../enviroment';
import { Router } from '@angular/router';
import { UserRequestDTO } from '../../models/auth-dto/user-request';

@Injectable({
  providedIn: 'root',
})
export class LoginUser {

  loginURL = Enviroment.apiURL + "auth/login";

  constructor(private http: HttpClient, private router: Router){}

  siginUser(user: UserLoginDTO){

    this.http.post<UserRequestDTO>(this.loginURL, user).subscribe({
      next: (next) => {
        sessionStorage.setItem("email", next.email)
        sessionStorage.setItem("token", next.token)
        sessionStorage.setItem("username", next.username)
        this.router.navigate(["/dashboard"])
      },
      error: (err) => {alert("Login unsuccesfull"+ err)}
    });
  }


}
