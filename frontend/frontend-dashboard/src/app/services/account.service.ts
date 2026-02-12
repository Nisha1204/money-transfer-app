import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AccountResponse } from '../models/account.model';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private readonly API_BASE = 'http://localhost:8080/api/v1';

  constructor(private http: HttpClient) {}

  getAccount(accountId: number): Observable<AccountResponse> {
    return this.http.get<AccountResponse>(`${this.API_BASE}/accounts/${accountId}`);
  }

  getBalance(accountId: number): Observable<number> {
    return this.http.get<number>(`${this.API_BASE}/accounts/${accountId}/balance`);
  }

  getMyAccounts(): Observable<AccountResponse[]> {
    return this.http.get<AccountResponse[]>(`${this.API_BASE}/accounts/my-accounts`);
  }
}