import { BudgetDTO } from '../../models/transactions-dto/budgets-dto';
import { ChangeDetectorRef, Component } from '@angular/core';
import { AsideComponent } from "../aside-component/aside-component";
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators, ɵInternalFormsSharedModule } from '@angular/forms';
import { CurrencyPipe } from '@angular/common';
import { BudgetsService } from '../../services/transactions/budgets-service';

@Component({
  selector: 'app-budget-page',
  imports: [
    AsideComponent,
    ReactiveFormsModule,
    CurrencyPipe,
    ɵInternalFormsSharedModule,
    FormsModule,
  ],
  templateUrl: './budget-page.html',
  styleUrl: './budget-page.css',
})
export class BudgetPage {
  categories = ['Food', 'Transport', 'Leisure', 'Health', 'Housing', 'Others'];

  months = [
    'January',
    'February',
    'March',
    'April',
    'May',
    'June',
    'July',
    'August',
    'September',
    'October',
    'November',
    'December',
  ];

  monthData = '';

  pageCounter: number = 0;
  maxPages: number = 1;
  maxItemsPerPage: number = 3

  constructor(
    private budgets: BudgetsService,
    private cdr: ChangeDetectorRef
  ) {}

  budgetForm = new FormGroup({
    value: new FormControl(0, [Validators.required, Validators.nullValidator, Validators.min(1)]),
    category: new FormControl('', [Validators.required, Validators.nullValidator]),
    month: new FormControl('', [Validators.required, Validators.nullValidator]),
  });

  budgetList?: BudgetDTO[];

  public addBudget() {
    return;
  }

  public updateMonth(month: number) {
    this.budgets.getBudgets(month).subscribe({
      next: (nxt) => {
        this.budgetList = nxt;
        this.maxPages = Math.ceil(nxt.length / 3);
        console.log('Getting budgets successfully');
        this.cdr.detectChanges();

      },
      error: (err) => console.log('Error getting budgets' + err),
    });
  }

  public updateBudgetsPanel() {
    this.updateMonth(this.months.indexOf(this.monthData) + 1);
  }

  public nextPageCounter() {
    if (this.maxPages && this.pageCounter < this.maxPages - 1) this.pageCounter++;
  }

  public previousPageCounter() {
    if (this.pageCounter > 0) this.pageCounter--;
  }

  protected readonly Math = Math;
}
