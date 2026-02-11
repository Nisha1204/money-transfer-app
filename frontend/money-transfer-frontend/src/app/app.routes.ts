import { Routes } from '@angular/router';
import { Login } from './login/login';
import { AuthGuard } from './auth/auth.guard';
import { Register } from './register/register';

export const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  {
    path: 'dashboard',
    canActivate: [AuthGuard],
    loadComponent: () =>
      import('./dashboard/dashboard').then(m => m.Dashboard)
  },
  { path: '', redirectTo: 'login', pathMatch: 'full' }
];
