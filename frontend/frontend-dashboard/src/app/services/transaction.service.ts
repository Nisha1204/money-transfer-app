import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface Transaction {
  id: string; // Changed from number to string (UUID)
  fromAccountId: number; // Matches API number
  toAccountId: number;   // Matches API number
  amount: number;
  status: TransactionStatus;
  failureReason: string | null;
  idempotencyKey: string;
  createdOn: string;
  new: boolean;
}

export enum TransactionStatus {
  SUCCESS = 'SUCCESS',
  FAILED = 'FAILED'
}

@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  private readonly API_BASE = 'http://localhost:8080/api/v1';

  constructor(private http: HttpClient) { }

  getTransactions(accountId: number): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(`${this.API_BASE}/accounts/${accountId}/transactions`);
  }
}