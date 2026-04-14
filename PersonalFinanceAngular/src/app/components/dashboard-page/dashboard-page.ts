import { DashboardUserDTO } from '../../models/auth-dto/dashboard-user-dto';
import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard-page',
  imports: [],
  templateUrl: './dashboard-page.html',
  styleUrl: './dashboard-page.css',
})
export class DashboardPage {
  userDasboard: DashboardUserDTO | any;
  username = sessionStorage.getItem("username")

  constructor(private router : Router) {}

  navigateToUpdates(){
    return this.router.navigate(['finances'])
  }




}
