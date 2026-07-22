import { BudgetDTO } from '../../models/transactions-dto/budgets-dto';
import { ChangeDetectorRef, Component } from '@angular/core';
import { AsideComponent } from "../aside-component/aside-component";
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators, ɵInternalFormsSharedModule } from '@angular/forms';
import { CurrencyPipe } from '@angular/common';
import { BudgetsService } from '../../services/transactions/budgets-service';
import { GeneralServices } from '../../services/general-service/general-services';
import { MonthsModel, ExpenseCategoriesModel } from '../../models/objects/general-models';


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
  categories = ExpenseCategoriesModel;

  months = MonthsModel;

  monthData = '';

  pageCounter: number = 0;
  maxPages: number = 1;
  maxItemsPerPage: number = 3;

  constructor(
    private budgets: BudgetsService,
    private cdr: ChangeDetectorRef,
    private general: GeneralServices,
  ) {}

  budgetForm = new FormGroup({
    category: new FormControl('', [Validators.required, Validators.nullValidator]),
    month: new FormControl('', [Validators.required, Validators.nullValidator]),
    budgetLimit: new FormControl(0, [
      Validators.required,
      Validators.nullValidator,
      Validators.min(1),
    ]),
  });

  budgetList?: BudgetDTO[];

  public addBudget() {
    const newBudget = this.budgetForm.value as BudgetDTO;
    newBudget.month = this.months.indexOf(newBudget.month) + 1;
    newBudget.budgetLimit *= 100;
    this.budgets.addBudgets(newBudget);
    this.clearForm();
  }

  public updateMonth(month: number) {
    this.budgets.getBudgets(month).subscribe({
      next: (nxt) => {
        this.budgetList = nxt;
        this.maxPages = Math.ceil(nxt.length / 3);
        console.log('Getting budgets successfully');
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.log('Error getting budgets' + err);
        this.general.logoutUser();
      },
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

  private clearForm() {
    this.budgetForm.setValue({
      category: '',
      month: '',
      budgetLimit: 0,
    });
  }
}
