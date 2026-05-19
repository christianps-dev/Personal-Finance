import { Finances } from './../../services/finances';
import { CurrencyPipe } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { DashboardFinancesDTO } from '../../models/transactions-dto/dashboard-dto';
import { AsideComponent } from "../aside-component/aside-component/aside-component";
import { getLastTransactionDTO } from '../../models/transactions-dto/getTransactionDTO';
import { Chart, registerables} from 'chart.js';
import { UserGenericDTO } from '../../models/user-generic-dto';
import { FormsModule, ɵInternalFormsSharedModule } from '@angular/forms';
import { TabelaServices } from '../../services/general-service/chart-services';
Chart.register(...registerables)

@Component({
  selector: 'app-dashboard-page',
  imports: [CurrencyPipe, AsideComponent, ɵInternalFormsSharedModule, FormsModule],
  templateUrl: './dashboard-page.html',
  styleUrl: './dashboard-page.css',
})


export class DashboardPage implements OnInit {

  monthData = "";

  totalMonthly: DashboardFinancesDTO = { incomes: 0, expenses: 0, balance: 0 };

  months = ['January', 'February', 'March', 'April', 'May', 'June',
     'July', 'August', 'September', 'October', 'November', 'December'
  ];

  typeChart = 'bar';

  userCredentials: UserGenericDTO = {
    email: sessionStorage.getItem("email") ?? '',
    username: sessionStorage.getItem("username") ?? ''
  };

  chartCategories = ['FOOD', 'TRANSPORT', 'LEISURE', 'HEALTH', 'HOUSING',
     'OTHERS', 'JOB', 'FREELANCE', 'GIFT',
  ];

  everyExpenses: { [key: string]: number } = {
    'FOOD': 0, 'TRANSPORT': 0, 'LEISURE': 0, 'HEALTH': 0, 'HOUSING': 0, 'OTHERS': 0 };

  everyIncomes: { [key: string]: number } = {
    'JOB' : 0, 'FREELANCE' : 0, 'GIFT' : 0, 'OTHERS': 0 };

  financeChart: any;

  constructor(private finance: Finances, private cdr: ChangeDetectorRef,private graficos: TabelaServices) {}

  public ngOnInit(): void {
    this.financeChart = this.graficos.createChart(this.typeChart, this.chartCategories);
  }

  public getAllByMonth(month : number): void {
    this.finance.getAllTransactionsByMonth(this.userCredentials.email, month).subscribe({
      next: (transactions: getLastTransactionDTO[]) => {
        this.chartCategories.forEach(cat => this.everyExpenses[cat] = 0);
        this.resetFinacesData();

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

        this.updateChart();

        this.cdr.detectChanges();
      },
      error: (err) => console.error("Error getting the transactions", err)
    });
  }

  public setChartType(type: string) {
    this.typeChart = type;
    this.graficos.destroyChart(this.financeChart);
    this.financeChart = this.graficos.createChart(this.typeChart, this.chartCategories);
    this.updateMonth();
  }

  public updateMonth(){
    this.getAllByMonth(this.months.indexOf(this.monthData) + 1);
  }

  public updateChart(): void{

    if (this.financeChart) {

      if(this.typeChart === "bar"){
        const activeCategories = this.chartCategories.filter(cat => {
          const hasExpense = this.everyExpenses[cat] > 0;
          const hasIncome = this.everyIncomes[cat] > 0;
          return hasExpense || hasIncome;
        });

        const expensesData = activeCategories.map(cat => this.everyExpenses[cat] || 0);
        const incomesData = activeCategories.map(cat => this.everyIncomes[cat] || 0);

        this.financeChart.data.labels = activeCategories;
        this.financeChart.data.datasets[0].data = expensesData;
        this.financeChart.data.datasets[1].data = incomesData;

        this.financeChart.options = {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              display: true,
              labels: { color: '#ffffff' }
            },
            tooltip: {
              callbacks: {
                label: (context: any) => {
                  const value = context.parsed.y;
                  return `${context.dataset.label}: ${new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value)}`;
                }
              }
            }
          },
          scales: {
            x: {
              stacked: false,
              grid: { display: false },
              ticks: { color: '#ffffff' }
            },
            y: {
              stacked: false,
              beginAtZero: true,
              grid: { color: 'rgba(255, 255, 255, 0.1)' },
              ticks: { color: '#ffffff' }
            }
          }
        };

        this.financeChart.update();
      }

      if(this.typeChart === "doughnut"){
        const activeExpenses = Object.keys(this.everyExpenses).filter(cat => this.everyExpenses[cat] > 0);
        const activeIncomes = Object.keys(this.everyIncomes).filter(cat => this.everyIncomes[cat] > 0);

        const allLabels = [...activeExpenses, ...activeIncomes];

        const dataValues = [
          ...activeExpenses.map(cat => this.everyExpenses[cat]),
          ...activeIncomes.map(cat => this.everyIncomes[cat])
        ];

        const colors = [
          ...activeExpenses.map(() => '#d32823'),
          ...activeIncomes.map(() => '#6ab70b')
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
            currency: 'BRL'
          }).format(value);

          return `${type} - ${label}: ${formatted}`;
        };

        this.financeChart.update();
      }

      if(this.typeChart === "pie"){
      const activeExpenses = Object.keys(this.everyExpenses).filter(cat => this.everyExpenses[cat] > 0);
      const activeIncomes = Object.keys(this.everyIncomes).filter(cat => this.everyIncomes[cat] > 0);

      const allLabels = [...activeExpenses, ...activeIncomes];

      const dataValues = [
        ...activeExpenses.map(cat => this.everyExpenses[cat]),
        ...activeIncomes.map(cat => this.everyIncomes[cat])
      ];

      const colors = [
        ...activeExpenses.map(() => '#d32823'),
        ...activeIncomes.map(() => '#6ab70b')
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
          currency: 'BRL'
        }).format(value);

        return `${type} - ${label}: ${formatted}`;
      };

      this.financeChart.update();
      }
    }
  }

  private resetFinacesData(){
    this.everyExpenses = {
    'FOOD': 0, 'TRANSPORT': 0, 'LEISURE': 0, 'HEALTH': 0, 'HOUSING': 0, 'OTHERS': 0 };

    this.everyIncomes = {
    'JOB' : 0, 'FREELANCE' : 0, 'GIFT' : 0, 'OTHERS': 0 };

    this.totalMonthly = { incomes: 0, expenses: 0, balance: 0 };
  }

  /*
  private updateAccBalance(): void{
    this.finance.getAccountBalance(this.userCredentials.email).subscribe({
      next: (nxt) => {this.totalMonthly.balance = nxt.balance / 100,
        this.cdr.detectChanges()
      },
      error: (err) => console.log("Account not found" + err)
    })
  }
    */
}
