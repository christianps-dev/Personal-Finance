import { HttpClient } from '@angular/common/http';
import { Enviroment } from './../../enviroment';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class Finances {

  transactionURL = Enviroment.apiURL + "user";

  constructor(private http : HttpClient){}

  creditTransaction(transaction: TransactionDTO){
    return this.http.post<TransactionDTO>(this.transactionURL + "/credit", transaction).subscribe({
      complete: () => alert("Value " + transaction.value + " Credited succesfully"),
      error: (err) => alert("Credit in value " + transaction.value + " failed" + err)
    })
  }
  debitTransaction(transaction: TransactionDTO){
    return this.http.post<TransactionDTO>(this.transactionURL + "/debit", transaction).subscribe({
      complete: () => alert("Value " + transaction.value + " Debited succesfully"),
      error: (err) => alert("Debit in value " + transaction.value + " failed" +  err),

    })
  }
}
