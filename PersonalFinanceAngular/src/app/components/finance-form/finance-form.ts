import { Finances } from './../../services/finances';
import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule, FormControlName } from '@angular/forms';

@Component({
  selector: 'app-finance-form',
  imports: [ReactiveFormsModule],
  templateUrl: './finance-form.html',
  styleUrls: ['./finance-form.css']
})

export class FinanceForm {

  constructor(private transaction : Finances){}



  categories = ['Food', 'Transport', 'Leisure', 'Health', 'Housing', 'Others'];


  financeForm = new FormGroup ({
    description: new FormControl(''),
    value: new FormControl( 0, [Validators.required, Validators.nullValidator]),
    category: new FormControl('', [Validators.required, Validators.nullValidator])/*,
    date: new FormControl('', [Validators.required, Validators.nullValidator])*/
    }
  )

  adicionarGasto() {

    const financeAdd = this.financeForm.value as TransactionDTO;
    financeAdd.email = sessionStorage.getItem('email') ?? '';
    financeAdd.value = financeAdd.value * 100;

    if(financeAdd.value > 0){
      return this.transaction.creditTransaction(financeAdd);
    }
    else if(financeAdd.value < 0) {
      return this.transaction.debitTransaction(financeAdd);
    }
    else{
      return null;
    }


      this.financeForm.setValue({
         description: '',
         value: 0,
         category: ''/*,
         date: ''*/
      });
  }

}
