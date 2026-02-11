import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { v4 as uuidv4 } from 'uuid';

@Injectable({
  providedIn: 'root'
})
export class TransferService {
  // Ensure this matches your @RequestMapping("/api/v1") + @PostMapping("/transfers")
  private apiUrl = 'http://localhost:8080/api/v1/transfers';

  constructor(private http: HttpClient) {}

  executeTransfer(fromAccountId: number, toAccountId: number, amount: number): Observable<any> {
    const payload = {
      fromAccountId,
      toAccountId,
      amount,
      idempotencyKey: uuidv4()
    };
    
    // Authorization header is automatically added by the interceptor
    return this.http.post(this.apiUrl, payload, { responseType: 'json' });
  }
}