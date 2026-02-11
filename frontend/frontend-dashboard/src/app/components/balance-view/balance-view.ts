import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbar } from '@angular/material/toolbar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { AccountService } from '../../services/account.service';
import { AccountResponse } from '../../models/account.model';

@Component({
  selector: 'app-balance-view',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatToolbar,
    MatProgressSpinnerModule
  ],
  templateUrl: './balance-view.html',
  styleUrls: ['./balance-view.css']
})
export class BalanceViewComponent implements OnInit {
  balance: number = 0;
  accountData: AccountResponse | null = null;
  loading = true;
  error = '';
  accountId!: number;

  constructor(
    private accountService: AccountService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.accountId = +params['id'];
      this.loadBalance();
    });
  }

  loadBalance(): void {
    this.loading = true;
    this.error = '';

    // Load both balance and account info for display
    this.accountService.getBalance(this.accountId).subscribe({
      next: (balance) => {
        this.balance = balance;
        this.loadAccountInfo();
      },
      error: (err) => {
        this.error = 'Failed to load balance';
        this.loading = false;
        console.error('Error:', err);
      }
    });
  }

  loadAccountInfo(): void {
    this.accountService.getAccount(this.accountId).subscribe({
      next: (data) => {
        this.accountData = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        // Even if account details fail, we have the balance
        this.loading = false;
        this.cdr.detectChanges();
        console.error('Error loading account details:', err);
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/dashboard']);
  }

  viewFullDetails(): void {
    this.router.navigate(['/account-details', this.accountId]);
  }
}