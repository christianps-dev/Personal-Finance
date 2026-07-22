import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class GeneralServices {

  constructor(private router: Router){}

  public logoutUser(){
    alert("Need to be logged to user program, returning to login page");
    sessionStorage.clear();
    this.router.navigate(['login']);
    return;
  }

}
