import { DashboardUserDTO } from './../../models/dashboard-user-dto';
import { Component } from '@angular/core';

@Component({
  selector: 'app-dashboard-page',
  imports: [],
  templateUrl: './dashboard-page.html',
  styleUrl: './dashboard-page.css',
})
export class DashboardPage {
  userDasboard: DashboardUserDTO | any;
  username = sessionStorage.getItem("username")




}
