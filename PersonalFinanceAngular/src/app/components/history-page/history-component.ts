import { CurrencyPipe, DatePipe, NgClass } from '@angular/common';
import { UserGenericDTO } from './../../models/user-generic-dto';
import { ChangeDetectorRef, Component } from '@angular/core';
import { AsideComponent } from "../aside-component/aside-component/aside-component";
import { FormsModule } from "@angular/forms";
import { FullTransactionDTO } from '../../models/transactions-dto/full-transaction-dto';
import { Finances } from '../../services/finances';
import { max } from 'rxjs';

@Component({
  selector: 'app-history-component',
  imports: [AsideComponent, FormsModule, CurrencyPipe, NgClass, DatePipe],
  templateUrl: './history-component.html',
  styleUrl: './history-component.css',
})
export class HistoryPage {

  constructor(private finances: Finances, private cdr: ChangeDetectorRef){}

  userCredentials: UserGenericDTO = {
    username: sessionStorage.getItem('username') || '',
    email: sessionStorage.getItem('email') || ''
  }

  monthData = "";

  months = ['January', 'February', 'March', 'April', 'May', 'June',
     'July', 'August', 'September', 'October', 'November', 'December'
  ];

  pageCounter: number = 0;
  maxPages?: number = 0;

  transactionsDisplayed?: FullTransactionDTO[];

  public updateMonth(){
    this.finances.getFullTransactionsByMonth(this.userCredentials.email, (this.months.indexOf(this.monthData)) + 1).subscribe({
      next: (nxt) => (
        this.transactionsDisplayed = nxt,
        this.maxPages = nxt.length / 7,
        console.log(this.maxPages),
        this.cdr.detectChanges()
      ),
      error: (err) => console.log("Error while getting transacitons " + err)
    })

  }

  public nextPageCounter(){
    if(this.maxPages && this.pageCounter < this.maxPages - 1)
      this.pageCounter++;
  }

  public previousPageCounter(){
    if(this.pageCounter > 0)
      this.pageCounter--;
  }

}
