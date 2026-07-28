import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { UserInfoModel } from '../../models/objects/general-models';

@Injectable({
  providedIn: 'root',
})
export class GeneralServices {
  constructor(private router: Router) {}

  public logoutUser() {
    alert('Need to be logged to user program, returning to login page');
    sessionStorage.clear();
    this.router.navigate(['login']);
  }

  public getUserInfo( ): UserInfoModel {
    return {
      email: sessionStorage.getItem('email') || 'null',
      token: sessionStorage.getItem('token') || 'null',
      username: sessionStorage.getItem('username') || 'null',
    };
  }
}
