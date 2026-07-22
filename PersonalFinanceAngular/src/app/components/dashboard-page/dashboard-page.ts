import { Finances } from '../../services/transactions/finances';
import { CurrencyPipe, NgClass } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { DashboardFinancesDTO } from '../../models/transactions-dto/dashboard-dto';
import { AsideComponent } from "../aside-component/aside-component";
import { getLastTransactionDTO } from '../../models/transactions-dto/getTransactionDTO';
import { Chart, registerables} from 'chart.js';
import { FormsModule, ɵInternalFormsSharedModule } from '@angular/forms';
import { TabelaServices } from '../../services/general-service/chart-services';
import { BudgetsDashboardDto } from '../../models/transactions-dto/budgets-dashboard-dto';
import { BudgetsService } from '../../services/transactions/budgets-service';
import { GeneralServices } from '../../services/general-service/general-services';
import {
  UserInfoModel,
  MonthsModel,
  ExpenseCategoriesModel,
} from '../../models/objects/general-models';

Chart.register(...registerables)

@Component({
  selector: 'app-dashboard-page',
  imports: [CurrencyPipe, AsideComponent, ɵInternalFormsSharedModule, FormsModule, NgClass],
  templateUrl: './dashboard-page.html',
  styleUrl: './dashboard-page.css',
})
export class DashboardPage implements OnInit {
  monthData = '';

  totalMonthly: DashboardFinancesDTO = { incomes: 0, expenses: 0, balance: 0 };

  months = MonthsModel;

  budgetsDashboard?: BudgetsDashboardDto[];

  typeChart = 'bar';

  userCredentials = UserInfoModel;

  chartCategories = ExpenseCategoriesModel;

  everyExpenses: { [key: string]: number } = {
    FOOD: 0,
    TRANSPORT: 0,
    LEISURE: 0,
    HEALTH: 0,
    HOUSING: 0,
    OTHERS: 0,
  };

  everyIncomes: { [key: string]: number } = {
    JOB: 0,
    FREELANCE: 0,
    GIFT: 0,
    OTHERS: 0,
  };

  financeChart: any;

  constructor(
    private finance: Finances,
    private cdr: ChangeDetectorRef,
    private graficos: TabelaServices,
    private budgets: BudgetsService,
    private general: GeneralServices,
  ) {}

  public ngOnInit(): void {
    this.financeChart = this.graficos.createChart(this.typeChart, this.chartCategories);
  }

  public getAllByMonth(month: number): void {
    this.finance.getAllTransactionsByMonth(month).subscribe({
      next: (transactions: getLastTransactionDTO[]) => {
        this.chartCategories.forEach((cat) => (this.everyExpenses[cat] = 0));
        this.resetFinancesData();

        for (let t of transactions) {
          if (t.value < 0) {
            this.totalMonthly.expenses += t.value;

            if (this.everyExpenses[t.category] !== undefined) {
              this.everyExpenses[t.category] += Math.abs(t.value) / 100;
            }
          } else {
            this.totalMonthly.incomes += t.value;

            if (this.everyIncomes[t.category] !== undefined) {
              this.everyIncomes[t.category] += Math.abs(t.value) / 100;
            }
          }
        }

        this.totalMonthly.incomes /= 100;
        this.totalMonthly.expenses /= 100;

        this.getBudgetsLimits();

        this.updateChart();

        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error getting the transactions', err);
      },
    });
  }

  private clearBudgets() {
    while (this.budgetsDashboard?.length != null && this.budgetsDashboard?.length > 0)
      this.budgetsDashboard?.pop();
  }

  public setChartType(type: string) {
    this.typeChart = type;
    this.graficos.destroyChart(this.financeChart);
    this.financeChart = this.graficos.createChart(this.typeChart, this.chartCategories);
    this.updateMonth();
  }

  public updateMonth() {
    this.getAllByMonth(this.months.indexOf(this.monthData) + 1);
  }

  public updateChart(): void {
    if (this.financeChart) {
      if (this.typeChart === 'bar') {
        const activeCategories = this.chartCategories.filter((cat) => {
          const hasExpense = this.everyExpenses[cat] > 0;
          const hasIncome = this.everyIncomes[cat] > 0;
          return hasExpense || hasIncome;
        });


        const expensesData = activeCategories.map((cat) => this.everyExpenses[cat] || 0);
        const incomesData = activeCategories.map((cat) => this.everyIncomes[cat] || 0);

        this.financeChart.data.labels = activeCategories;
        this.financeChart.data.datasets[0].data = expensesData;
        this.financeChart.data.datasets[1].data = incomesData;


        this.financeChart.options = {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              display: true,
              labels: { color: '#ffffff' },
            },
            tooltip: {
              callbacks: {
                label: (context: any) => {
                  const value = context.parsed.y;
                  return `${context.dataset.label}: ${new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value)}`;
                },
              },
            },
          },
          scales: {
            x: {
              stacked: false,
              grid: { display: false },
              ticks: { color: '#ffffff' },
            },
            y: {
              stacked: false,
              beginAtZero: true,
              grid: { color: 'rgba(255, 255, 255, 0.1)' },
              ticks: { color: '#ffffff' },
            },
          },
        };

      }

      else {
        const activeExpenses = Object.keys(this.everyExpenses).filter(
          (cat) => this.everyExpenses[cat] > 0,
        );
        const activeIncomes = Object.keys(this.everyIncomes).filter(
          (cat) => this.everyIncomes[cat] > 0,
        );

        const allLabels = [...activeExpenses, ...activeIncomes];

        const dataValues = [
          ...activeExpenses.map((cat) => this.everyExpenses[cat]),
          ...activeIncomes.map((cat) => this.everyIncomes[cat]),
        ];

        const colors = [
          ...activeExpenses.map(() => '#d32823'),
          ...activeIncomes.map(() => '#6ab70b'),
        ];

        this.financeChart.data.labels = allLabels;

        this.financeChart.data.datasets[0].data = dataValues;
        this.financeChart.data.datasets[0].backgroundColor = colors;

        this.financeChart.options.plugins!.tooltip!.callbacks!.label = (context: any) => {
          const label = context.label;
          const value = context.parsed;
          const isIncome = activeIncomes.includes(label);
          const type = isIncome ? 'Income' : 'Expenses';

          const formatted = new Intl.NumberFormat('pt-BR', {
            style: 'currency',
            currency: 'BRL',
          }).format(value);

          return `${type} - ${label}: ${formatted}`;
        };

      }

      this.financeChart.update();

    }
  }

  private resetFinancesData() {
    this.everyExpenses = {
      FOOD: 0,
      TRANSPORT: 0,
      LEISURE: 0,
      HEALTH: 0,
      HOUSING: 0,
      OTHERS: 0,
    };

    this.everyIncomes = {
      JOB: 0,
      FREELANCE: 0,
      GIFT: 0,
      OTHERS: 0,
    };

    this.totalMonthly = { incomes: 0, expenses: 0, balance: 0 };
  }

  private getBudgetsLimits() {
    this.clearBudgets();

    this.budgets.getBudgetsDashboard(this.months.indexOf(this.monthData) + 1).subscribe({
      next: (nxt) => (this.budgetsDashboard = nxt),
      error: (err) => {
        console.log('Error getting budgets' + err);
        this.general.logoutUser();
      },
    });
    return;
  }
}
