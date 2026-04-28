import { Injectable } from '@angular/core';
import { Enviroment } from '../../../enviroment';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { UserRegisterDTO } from '../../models/auth-dto/user-register';
import { UserRequestDTO } from '../../models/auth-dto/user-request';
import { UserLoginDTO } from '../../models/auth-dto/user-login-dto';
import { ChangePasswordDTO } from '../../models/auth-dto/change-password-dto';

@Injectable({
  providedIn: 'root',
})
export class AuthUser {

  authUserUrl = Enviroment.apiURL + "auth";

  constructor(private http: HttpClient, private router: Router) {}

  public registerNewUser(user: UserRegisterDTO): any {

    this.http.post<UserRequestDTO>(this.authUserUrl + "/register", user).subscribe({
      next: () => {
        alert("Success user registration "),
        this.router.navigate(["/login"])
      },
      error: (err) => alert("Registration error "+ err)
    }
    );
    return ;
  }

  public siginUser(user: UserLoginDTO){

      this.http.post<UserRequestDTO>(this.authUserUrl + "/login", user).subscribe({
        next: (next) => {
          sessionStorage.setItem("email", next.email)
          sessionStorage.setItem("token", next.token)
          sessionStorage.setItem("username", next.username)
          this.router.navigate(["/dashboard"])
        },
        error: (err) => {alert("Login unsuccesfull "+ err)}
      });
    }

    public changePassword(newPassword: ChangePasswordDTO){
      this.http.post<ChangePasswordDTO>(this.authUserUrl + "/changepassword", newPassword).subscribe({
        complete: () => alert("Password change successfully"),
        error: (err) => alert("Password change unsuccesfull "+ err)
      })

    }
}
