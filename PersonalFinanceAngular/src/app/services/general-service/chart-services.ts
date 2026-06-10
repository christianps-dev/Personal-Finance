import { Injectable, ChangeDetectorRef } from '@angular/core';
import { Finances } from '../transactions/finances';
import { Chart, registerables } from 'chart.js';
Chart.register(...registerables)

@Injectable({
  providedIn: 'root',
})
export class TabelaServices {

  public createChart(type: string, categories: string[]): Chart {

    let newChart: Chart;

    if(type === "bar") {

      newChart = new Chart("Finance-chart", {
      type: 'bar',
      data: {
        labels: categories,
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

      return newChart;
    }

    if(type === "doughnut") {
      newChart = new Chart("Finance-chart", {
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
      return newChart;

    }

    else {
      newChart = new Chart("Finance-chart", {
        type: 'pie',
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
      }

      })

      return newChart;
    }

  }

  public destroyChart(chart: Chart){
    chart.destroy()
  }


}
