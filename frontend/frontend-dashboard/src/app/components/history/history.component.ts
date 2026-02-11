import { Component, OnInit } from '@angular/core';
import { TransactionService, Transaction } from '../../services/transaction.service';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { MatTable } from '@angular/material/table';

@Component({
    selector: 'app-history',
    templateUrl: './history.component.html',
    imports: [
        MatToolbarModule,
        MatButtonModule,
        CommonModule,
        MatTable
    ]
})
export class HistoryComponent implements OnInit {

    transactions: Transaction[] = [];
    displayedColumns = ['type', 'amount', 'status', 'timestamp'];

    constructor(private transactionService: TransactionService) { }

    ngOnInit(): void {
        // Make sure you subscribe to the transaction service
        this.transactionService.getTransactions().subscribe(data => {
            this.transactions = data.sort((a, b) =>
                new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime()
            );
        });
    }

    isCredit(type: string): boolean {
        return type === 'CREDIT';
    }
}
