import { Finances } from '../../services/transactions/finances';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { getLastTransactionDTO } from '../../models/transactions-dto/getTransactionDTO';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { AsideComponent } from "../aside-component/aside-component";
import { GeneralServices } from '../../services/general-service/general-services';
import { ExpenseCategoriesModel, IncomeCategoriesModel } from '../../models/objects/general-models';

@Component({
  selector: 'app-finance-form',
  imports: [ReactiveFormsModule, CurrencyPipe, AsideComponent, DatePipe],
  templateUrl: './finance-form.html',
  styleUrls: ['./finance-form.css'],
})
export class FinanceForm implements OnInit {
  constructor(
    private transaction: Finances,
    private cdr: ChangeDetectorRef,
    private general: GeneralServices,
  ) {}

  expensesCategories = ExpenseCategoriesModel;
  incomeCategories = IncomeCategoriesModel;
  qtdTransactionsMenu = 3;
  transactionsDisplayed?: getLastTransactionDTO[];

  financeForm = new FormGroup({
    description: new FormControl(''),
    value: new FormControl(0, [Validators.required, Validators.nullValidator, Validators.min(1)]),
    category: new FormControl('', [Validators.required, Validators.nullValidator]),
    type: new FormControl('expense', Validators.required),
    date: new FormControl(''),
  });

  ngOnInit() {
    this.getLastTransactions(this.qtdTransactionsMenu);
  }

  public addTransaction() {
    const financeAdd = this.financeForm.value as TransactionDTO;
    financeAdd.value = financeAdd.value * 100;

    if (this.financeForm.value.type === 'expense') financeAdd.value *= -1;

    this.financeForm.reset();

    this.financeForm.setValue({
      description: '',
      value: 0,
      category: '',
      type: 'expense',
      date: '',
    });

    this.transaction.addNewTransaction(financeAdd);

    this.getLastTransactions(this.qtdTransactionsMenu);
  }

  public setTransactionType(type: 'income' | 'expense') {
    this.financeForm.patchValue({ type: type });
  }

  public getLastTransactions(qtd: number) {
    this.transaction.getLastTransactionsByQtd(qtd).subscribe({
      next: (nxt) => {
        this.transactionsDisplayed = nxt;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.log('Error while getting transactions', err);
        this.general.logoutUser();
      },
    });
  }
}
