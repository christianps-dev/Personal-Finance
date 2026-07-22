import { HttpClient, HttpParams } from '@angular/common/http';
import { Enviroment } from '../../../enviroment';
import { Injectable } from '@angular/core';
import { getLastTransactionDTO } from '../../models/transactions-dto/getTransactionDTO';
import { FullTransactionDTO } from '../../models/transactions-dto/full-transaction-dto';
import { GeneralServices } from '../general-service/general-services';
import { UserInfoModel } from '../../models/objects/general-models';

@Injectable({
  providedIn: 'root',
})
export class Finances {
  transactionURL = Enviroment.apiURL + '/finance';

  constructor(
    private http: HttpClient,
    private general: GeneralServices,
  ) {}

  addNewTransaction(transaction: TransactionDTO) {
    const params = new HttpParams().set('email', UserInfoModel.email);

    this.http
      .post<TransactionDTO>(this.transactionURL + '/transaction', transaction, { params })
      .subscribe({
        complete: () => console.log('Transaction successfully'),
        error: (err) => {
          console.log('Transaction failed', err);
          this.general.logoutUser();
        },
      });

    return null;
  }

  getLastTransactionsByQtd(qtd: number) {
    const req = {
      params: new HttpParams().set('email', UserInfoModel.email),
    };
    return this.http.get<getLastTransactionDTO[]>(
      this.transactionURL + '/transactions/last/' + qtd,
      req,
    );
  }

  getAllTransactionsByMonth(month: number) {
    const req = {
      params: new HttpParams().set('email', UserInfoModel.email),
    };
    return this.http.get<getLastTransactionDTO[]>(
      this.transactionURL + '/transactions/' + month,
      req,
    );
  }

  public getFullTransactionsByMonth( month: number) {
    const req = {
      params: new HttpParams().set('email', UserInfoModel.email),
    };
    return this.http.get<FullTransactionDTO[]>(
      this.transactionURL + '/transactions/full/' + month,
      req,
    );
  }
}
