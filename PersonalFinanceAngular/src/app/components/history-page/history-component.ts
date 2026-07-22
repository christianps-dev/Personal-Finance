import { CurrencyPipe, DatePipe, NgClass } from '@angular/common';
import { ChangeDetectorRef, Component } from '@angular/core';
import { AsideComponent } from "../aside-component/aside-component";
import { FormsModule } from "@angular/forms";
import { FullTransactionDTO } from '../../models/transactions-dto/full-transaction-dto';
import { Finances } from '../../services/transactions/finances';
import { GeneralServices } from '../../services/general-service/general-services';
import { MonthsModel } from '../../models/objects/general-models';


@Component({
  selector: 'app-history-component',
  imports: [AsideComponent, FormsModule, CurrencyPipe, NgClass, DatePipe],
  templateUrl: './history-component.html',
  styleUrl: './history-component.css',
})
export class HistoryPage {
  constructor(
    private finances: Finances,
    private cdr: ChangeDetectorRef,
    private general: GeneralServices,
  ) {}

  monthData = '';

  months = MonthsModel;

  pageCounter: number = 0;
  maxPages?: number = 0;

  transactionsDisplayed?: FullTransactionDTO[];

  public updateMonth() {
    this.finances
      .getFullTransactionsByMonth(
        this.months.indexOf(this.monthData) + 1,
      )
      .subscribe({
        next: (nxt) => {
          this.transactionsDisplayed = nxt;
          this.maxPages = nxt.length / 7;
          console.log(this.maxPages);
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.log('Error while getting transactions ' + err);
          this.general.logoutUser();
        },
      });
  }

  public nextPageCounter() {
    if (this.maxPages && this.pageCounter < this.maxPages - 1) this.pageCounter++;
  }

  public previousPageCounter() {
    if (this.pageCounter > 0) this.pageCounter--;
  }
}
