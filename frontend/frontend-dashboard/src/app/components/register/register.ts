import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router,RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from '../../services/auth.service'; // Use the service!
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    RouterLink,
    FormsModule,
    MatCardModule,
    MatInputModule,
    MatButtonModule,
    CommonModule,
    MatIconModule,
    MatProgressBarModule
  ],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  username = '';
  password = '';
  message = '';
  errorMessage = '';

  constructor(
    private authService: AuthService, 
    private router: Router
  ) {}

  register(): void {
    if (!this.username.trim() || !this.password.trim()) {
      this.errorMessage = 'Username and password are required';
      return;
    }

    this.errorMessage = '';
    this.message = '';

    // Call the Service
    this.authService.register(this.username, this.password).subscribe({
      next: (response) => {
        // Backend returns JSON: { "message": "User registered successfully!" }
        this.message = response.message; 
        
        // Redirect after 2 seconds
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: (err) => {
        console.error('Registration failed:', err);
        if (err.error && err.error.message) {
          this.errorMessage = err.error.message; // "Username already exists..."
        } else {
          this.errorMessage = 'Registration failed. Try again.';
        }
      }
    });
  }
}