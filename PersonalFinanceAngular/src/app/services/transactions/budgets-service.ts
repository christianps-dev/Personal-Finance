import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Enviroment } from '../../../enviroment';
import { BudgetsDashboardDto } from '../../models/transactions-dto/budgets-dashboard-dto';
import { BudgetDTO } from '../../models/transactions-dto/budgets-dto';

@Injectable({
  providedIn: 'root',
})
export class BudgetsService {
  apiUrl = Enviroment.apiURL + '/finance';

  constructor(private http: HttpClient) {}

  public getBudgetsDashboard(month: number) {
    const req = {
      params: new HttpParams().set('email', sessionStorage.getItem('email') ?? ''),
    };

    return this.http.get<BudgetsDashboardDto[]>(this.apiUrl + '/budgets/spends/' + month, req);
  }

  public getBudgets(month: number) {
    const req = {
      params: new HttpParams().set('email', sessionStorage.getItem('email') ?? ''),
    };

    return this.http.get<BudgetDTO[]>(this.apiUrl + '/budgets/' + month, req);
  }

  public addBudgets(budget: BudgetDTO) {

    const email = sessionStorage.getItem('email') || '';

    const params = new HttpParams().set('email', email);

    this.http.post<BudgetDTO[]>(this.apiUrl + "/budget", budget, { params }).subscribe({
      complete: () => console.log('Budget added'),
      error: (err) => console.log('Error' + err),
    });

    return null;

  }
}
