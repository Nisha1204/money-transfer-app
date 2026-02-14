import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { v4 as uuidv4 } from 'uuid';
import { catchError, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TransferService {
  // Ensure this matches your @RequestMapping("/api/v1") + @PostMapping("/transfers")
  private apiUrl = 'http://localhost:8080/api/v1/transfers';

  constructor(private http: HttpClient) {}

  /*
  getAccountData(id: number): Observable<any> {
    return this.http.get<any>(`http://localhost:8080/api/v1/accounts/${id}`);
  }
    */

// Change this method to use the "my-accounts" endpoint
getMyAccounts(): Observable<any[]> {
  return this.http.get<any[]>(`http://localhost:8080/api/v1/accounts/my-accounts`);
}


  executeTransfer(fromAccountId: number, toAccountId: number, amount: number): Observable<any> {
  const payload = { fromAccountId, toAccountId, amount, idempotencyKey: uuidv4() };
  
  return this.http.post(this.apiUrl, payload).pipe(
    catchError((error) => {
      // Log for debugging
      console.error('Service log:', error);
      // Re-throw the entire error object so the component can read error.error.message
      return throwError(() => error); 
    })
  );
}

}