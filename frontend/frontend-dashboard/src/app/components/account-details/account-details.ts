import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTableModule } from '@angular/material/table';
import { AccountService } from '../../services/account.service';
import { AccountResponse } from '../../models/account.model';

@Component({
  selector: 'app-account-details',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatToolbarModule,
    MatProgressSpinnerModule,
    MatTableModule
  ],
  templateUrl: './account-details.html',
  styleUrls: ['./account-details.css']
})
export class AccountDetailsComponent implements OnInit {
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
      this.loadAccountDetails();
    });
  }

  loadAccountDetails(): void {
    this.loading = true;
    this.error = '';

    this.accountService.getAccount(this.accountId).subscribe({
      next: (data) => {
        this.accountData = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.error = 'Failed to load account details';
        this.loading = false;
        this.cdr.detectChanges();
        console.error('Error:', err);
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/dashboard']);
  }

  getStatusColor(status: string): string {
    switch (status) {
      case 'ACTIVE':
        return 'primary';
      case 'LOCKED':
        return 'warn';
      case 'CLOSED':
        return 'accent';
      default:
        return '';
    }
  }
}