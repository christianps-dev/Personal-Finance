import { CurrencyPipe } from '@angular/common';
import { DashboardUserDTO } from '../../models/auth-dto/dashboard-user-dto';
import { Component } from '@angular/core';
import { DashboardFinancesDTO } from '../../models/transactions-dto/dashboard-dto';
import { AsideComponent } from "../aside-component/aside-component/aside-component";

@Component({
  selector: 'app-dashboard-page',
  imports: [CurrencyPipe, AsideComponent],
  templateUrl: './dashboard-page.html',
  styleUrl: './dashboard-page.css',
})
export class DashboardPage {
  userDasboard: DashboardUserDTO | any;
  username = sessionStorage.getItem("username")
  totalMonthly?: DashboardFinancesDTO;
  months = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];


}
