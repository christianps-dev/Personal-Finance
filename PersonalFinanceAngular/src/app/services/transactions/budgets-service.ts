import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Enviroment } from '../../../enviroment';
import { BudgetDTO } from '../../models/transactions-dto/budgets-dto';

@Injectable({
  providedIn: 'root',
})
export class BudgetsService {

  apiUrl = Enviroment.apiURL + "/finance"

  constructor(private http : HttpClient){}

  public getBudgets(month: number){
   const req = {
      params: new HttpParams().set('email', sessionStorage.getItem("email") ?? '')
    };

    return this.http.get<BudgetDTO[]>(this.apiUrl + "/budgets/" + month, req)
  }
}
