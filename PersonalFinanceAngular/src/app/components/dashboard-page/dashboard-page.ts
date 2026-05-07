import { Finances } from './../../services/finances';
import { CurrencyPipe } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { DashboardFinancesDTO } from '../../models/transactions-dto/dashboard-dto';
import { AsideComponent } from "../aside-component/aside-component/aside-component";
import { getLastTransactionDTO } from '../../models/transactions-dto/getTransactionDTO';
import { Chart, registerables} from 'chart.js';
import { UserGenericDTO } from '../../models/user-generic-dto';
Chart.register(...registerables)

@Component({
  selector: 'app-dashboard-page',
  imports: [CurrencyPipe, AsideComponent],
  templateUrl: './dashboard-page.html',
  styleUrl: './dashboard-page.css',
})


export class DashboardPage implements OnInit {

  totalMonthly: DashboardFinancesDTO = { incomes: 0, expenses: 0 };
  months = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
  typeChart = 'bar';

  userCredentials: UserGenericDTO = {
    email: sessionStorage.getItem("email") ?? '',
    username: sessionStorage.getItem("username") ?? ''
  };

  chartCategories = ['Food', 'Transport', 'Leisure', 'Health', 'Housing', 'Others', 'Job', 'Freelance', 'Gift', 'Other'];

  everyExpenses: { [key: string]: number } = {
    'Food': 0, 'Transport': 0, 'Leisure': 0, 'Health': 0, 'Housing': 0, 'Others': 0 };

  everyIncomes: { [key: string]: number } = {
    'Job' : 0, 'Freelance' : 0, 'Gift' : 0, 'Other': 0 };

  financeChart: any;

  constructor(private finance: Finances, private cdr: ChangeDetectorRef) {}

  public ngOnInit(): void {
    this.createChart();
    this.getAllTransactionsDashboard();
  }

  public getAllTransactionsDashboard(): void {
    this.finance.getAllTransactions(this.userCredentials).subscribe({
      next: (transactions: getLastTransactionDTO[]) => {
        this.totalMonthly = { incomes: 0, expenses: 0 };
        this.chartCategories.forEach(cat => this.everyExpenses[cat] = 0);

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
    this.destroyChart(this.financeChart);
    this.createChart();
    this.getAllTransactionsDashboard()

  }

  public destroyChart(chart: Chart){
    chart.destroy()
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
              stacked: true,
              grid: { display: false },
              ticks: { color: '#ffffff' }
            },
            y: {
              stacked: true,
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

        this.financeChart.options.plugins.tooltip.callbacks.label = (context: any) => {
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

  public createChart(): void {

    if(this.typeChart === "bar") {

      this.financeChart = new Chart("Finance-chart", {
      type: 'bar',
      data: {
        labels: this.chartCategories,
        datasets: [
          {
            label: 'Expenses',
            data: [],
            backgroundColor: '#d32823',
            borderRadius: 6,
            borderSkipped: false,
          },
          {
            label: 'Incomes',
            data: [],
            backgroundColor: '#6ab70b',
            borderRadius: 6,
            borderSkipped: false,
          }
        ],
      },
      options: { aspectRatio: 2 }
      });
    }

    if(this.typeChart === "doughnut") {
      this.financeChart = new Chart("Finance-chart", {
      type: 'doughnut',
      data: {
        labels: [],
        datasets: [{
          label: 'Despesas por Categoria',
          data: [],
          backgroundColor: [
            '#d32823', '#f39c12', '#3498db', '#9b59b6', '#1abc9c', '#34495e', '#e67e22'
          ],
          borderWidth: 1,
          borderColor: '#ffffff',
          hoverOffset: 20
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'right',
            labels: { color: '#ffffff', padding: 20 }
          },
          tooltip: {
            callbacks: {
              label: (context: any) => {
                const value = context.parsed;
                const total = context.dataset.data.reduce((a: number, b: number) => a + b, 0);
                const percentage = ((value / total) * 100).toFixed(1);
                const formattedValue = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value);
                return `${context.label}: ${formattedValue} (${percentage}%)`;
              }
            }
          }
        },
        cutout: '70%'
      }
    });
    }

  }
}
