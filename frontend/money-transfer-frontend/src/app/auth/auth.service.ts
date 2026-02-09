import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly TOKEN_KEY = 'auth_token';
  private baseUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  // ✅ login()
  login(username: string, password: string): Observable<any> {
    const token = btoa(`${username}:${password}`);
    const headers = new HttpHeaders({
      Authorization: `Basic ${token}`
    });

    // hit any protected endpoint to validate credentials
    return this.http.get(`${this.baseUrl}/api/v1/accounts/1`, { headers }).pipe(
      tap(() => {
        this.setToken(token);
      })
    );
  }

  // ✅ logout()
  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
  }

  // ✅ isAuthenticated()
  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  // ✅ getToken()  (SPEC-MATCHING NAME)
  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  // internal helper
  private setToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }
}
