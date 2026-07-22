import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Enviroment } from '../../../enviroment';
import { BudgetsDashboardDto } from '../../models/transactions-dto/budgets-dashboard-dto';
import { BudgetDTO } from '../../models/transactions-dto/budgets-dto';
import { UserInfoModel } from '../../models/objects/general-models';

@Injectable({
  providedIn: 'root',
})
export class BudgetsService {
  apiUrl = Enviroment.apiURL + '/finance';

  req = {
    params: new HttpParams().set('email', UserInfoModel.email ?? ''),
  };

  constructor(private http: HttpClient) {}

  public getBudgetsDashboard(month: number) {
    return this.http.get<BudgetsDashboardDto[]>(this.apiUrl + '/budgets/spends/' + month, this.req);
  }

  public getBudgets(month: number) {
    return this.http.get<BudgetDTO[]>(this.apiUrl + '/budgets/' + month, this.req);
  }

  public addBudgets(budget: BudgetDTO) {
    const params = new HttpParams().set('email', UserInfoModel.email ?? '');
    this.http.post<BudgetDTO[]>(this.apiUrl + '/budget', budget, { params }).subscribe({
      complete: () => console.log('Budget added'),
      error: (err) => console.log('Error' + err),
    });
  }
}
