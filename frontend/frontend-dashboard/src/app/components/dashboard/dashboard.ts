import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { AuthService } from '../../services/auth.service';
import { AccountService } from '../../services/account.service';
import { AccountResponse } from '../../models/account.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatToolbarModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css']
})
export class DashboardComponent implements OnInit {
  accountData: AccountResponse | null = null;
  currentBalance: number = 0;
  loading = true;
  error = '';
  selectedAccountId = 1; // Default account ID

  constructor(
    private authService: AuthService,
    private accountService: AccountService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadAccountData();
  }

  loadAccountData(): void {
    this.loading = true;
    this.error = '';

    this.accountService.getAccount(this.selectedAccountId).subscribe({
      next: (data) => {
        console.log('Data received in component:', data); 
        this.accountData = data;
        this.currentBalance = data.balance;
        this.loading = false;
        this.cdr.detectChanges(); 
      },
      error: (err) => {
        this.error = 'Failed to load account data.';
        this.loading = false;
        this.cdr.detectChanges(); 
      }
    });
  }

  viewAccountDetails(accountId: number): void {
    this.router.navigate(['/account-details', accountId]);
  }

  viewBalance(accountId: number): void {
    this.router.navigate(['/balance', accountId]);
  }

  doTransfer(accountId: number): void {
    this.router.navigate(['/transfer', accountId]);
  }

  viewTransactions(accountId: number): void {
    this.router.navigate(['/transactions', accountId]);
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  refreshData(): void {
    this.loadAccountData();
  }
}