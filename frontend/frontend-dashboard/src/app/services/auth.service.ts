import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly TOKEN_KEY = 'auth_token';
  // Points to your Spring Boot Backend
  private baseUrl = environment.apiBaseUrl; 

  constructor(private http: HttpClient, private router: Router) {}

  // 1. LOGIN
  // Matches @PostMapping("/api/auth/login")
  login(username: string, password: string): Observable<any> {
    const loginUrl = `${this.baseUrl}/api/auth/login`;
    
    // Send JSON body to Backend
    return this.http.post<any>(loginUrl, { username, password }).pipe(
      tap(response => {
        // If the backend returns 200 OK, we assume success.
        // We create a "Basic Auth" token to store in the browser.
        // (Even if your backend doesn't force security yet, this is good practice)
        const token = btoa(`${username}:${password}`);
        this.setToken(token);
      })
    );
  }

  // 2. REGISTER
  // Matches @PostMapping("/api/auth/register")
  register(username: string, password: string): Observable<any> {
    const registerUrl = `${this.baseUrl}/api/auth/register`;
    
    return this.http.post<any>(registerUrl, { 
      username, 
      password,
      role: 'USER' // Optional: depending on your DTO
    });
  }

  // 3. LOGOUT
  // auth.service.ts

logout(): void {
  // 1. Remove the "Key" so future requests fail
  localStorage.removeItem(this.TOKEN_KEY); 
  
  // 2. Redirect the user to the Login page visually
  this.router.navigate(['/login']); 
}
  // 4. CHECK AUTH STATUS
  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  private setToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }
}