import { Component } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, MatToolbarModule, MatButtonModule],
  templateUrl: './navbar.html',
  styles: ['.spacer { flex: 1 1 auto; }']
})
export class NavbarComponent {

  constructor(public authService: AuthService, private router: Router) {}

  onLogout(): void {
    this.authService.logout(); // Clears token
    this.router.navigate(['/login']); // Redirects user
  }
}