import { HttpClient, HttpParams } from '@angular/common/http';
import { Enviroment } from './../../enviroment';
import { Injectable } from '@angular/core';
import { getLastTransactionDTO } from '../models/transactions-dto/getTransactionDTO';
import { UserGenericDTO } from '../models/user-generic-dto';

@Injectable({
  providedIn: 'root',
})
export class Finances {

  transactionURL = Enviroment.apiURL + "/finance";

  constructor(private http : HttpClient){}

  addNewTransaction(transaction: TransactionDTO){
    return this.http.post<TransactionDTO>(this.transactionURL + "/transaction", transaction).subscribe({
      complete: () => console.log("Transaction successfuly"),
      error: (err) => console.log("Transaction failed",err)
    })
  }

  getLastTransactionsByQtd(qtd: number, user: UserGenericDTO){
    const req = {
      params: new HttpParams().set('email', user.email)
    };
    return this.http.get<getLastTransactionDTO[]>(this.transactionURL + "/transactions/last/" + qtd, req);
  }

  getAllTransactions(user: UserGenericDTO){
    const req = {
      params: new HttpParams().set('email', user.email)
    };
    return this.http.get<getLastTransactionDTO[]>(this.transactionURL + "/transactions", req)
  }
}
