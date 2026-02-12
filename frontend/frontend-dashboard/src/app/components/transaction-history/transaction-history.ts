import { Component, OnInit, ChangeDetectorRef } from '@angular/core'; // 1. Added ChangeDetectorRef
import { ActivatedRoute } from '@angular/router';
import { Location, CommonModule } from '@angular/common';
import { TransactionService, Transaction } from '../../services/transaction.service';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-transaction-history',
  standalone: true,
  imports: [
    CommonModule,
    MatToolbarModule,
    MatButtonModule,
    MatTableModule,
    MatIconModule
  ],
  templateUrl: './transaction-history.html',
  styleUrl: './transaction-history.css',
})
export class TransactionHistory implements OnInit {
  transactions: Transaction[] = [];
  currentAccountId: number | null = null;
  displayedColumns = ['id', 'date', 'description', 'amount', 'status'];

  constructor(
    private transactionService: TransactionService,
    private route: ActivatedRoute,
    private location: Location,
    private cdr: ChangeDetectorRef // 2. Inject ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.currentAccountId = Number(idParam);
      this.loadTransactions(this.currentAccountId);
    }
  }

  loadTransactions(id: number): void {
    this.transactionService.getTransactions(id).subscribe({
      next: (data) => {
        if (data) {
          this.transactions = [...data].sort((a, b) => 
            new Date(b.createdOn).getTime() - new Date(a.createdOn).getTime()
          );
          
          // 3. Force UI Update
          // This is critical when the view doesn't react to async data updates
          this.cdr.detectChanges(); 
        }
      },
      error: (err) => {
        console.error('Error fetching transactions:', err);
        this.cdr.detectChanges();
      }
    });
  }

  isSent(t: Transaction): boolean {
    return Number(t.fromAccountId) === Number(this.currentAccountId);
  }

  goBack(): void {
    this.location.back();
  }
}