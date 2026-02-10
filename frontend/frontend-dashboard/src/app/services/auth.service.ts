import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly AUTH_KEY = 'auth_credentials';

  constructor() {}

  login(username: string, password: string): boolean {
    // Store credentials in base64 format for Basic Auth
    const credentials = btoa(`${username}:${password}`);
    localStorage.setItem(this.AUTH_KEY, credentials);
    return true;
  }

  logout(): void {
    localStorage.removeItem(this.AUTH_KEY);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  getToken(): string | null {
    return localStorage.getItem(this.AUTH_KEY);
  }

  getAuthHeader(): string {
    const token = this.getToken();
    return token ? `Basic ${token}` : '';
  }
}