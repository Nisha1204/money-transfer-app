import { Component, OnInit, ChangeDetectorRef, ViewChild } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { BaseChartDirective, provideCharts, withDefaultRegisterables } from 'ng2-charts';
import { ChartConfiguration, ChartData, ChartType } from 'chart.js';
import { AnalyticsService } from '../../services/analytics.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  providers: [provideCharts(withDefaultRegisterables())],
  imports: [
    CommonModule, MatToolbarModule, MatButtonModule, MatCardModule, 
    MatIconModule, MatProgressSpinnerModule, MatTooltipModule, DecimalPipe, BaseChartDirective
  ],
  templateUrl: './admin-dashboard.html',
  styleUrl: './admin-dashboard.css',
})
export class AdminDashboard implements OnInit {
  stats: any;
  loading = true;
  error = '';

  // Chart 1: Line Chart for Trends
  public lineChartData: ChartData<'line'> = { datasets: [], labels: [] };
  public lineChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: { legend: { display: false } },
    scales: { y: { beginAtZero: true, grid: { display: false } }, x: { grid: { display: false } } }
  };

  constructor(
    private analyticsService: AnalyticsService,
    private authService: AuthService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void { this.fetchStats(); }

  fetchStats() {
    this.analyticsService.getStats().subscribe({
      next: (data) => {
        this.stats = data;
        this.setupCharts(data);
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.error = 'Snowflake Connection Failed';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  setupCharts(data: any) {
    this.lineChartData = {
      labels: data.trendLabels.reverse(),
      datasets: [{
        data: data.trendValues.reverse(),
        label: 'Transactions',
        borderColor: '#0ea5e9',
        backgroundColor: 'rgba(14, 165, 233, 0.1)',
        fill: true,
        tension: 0.4
      }]
    };
  }

  logout() { this.authService.logout(); }
}