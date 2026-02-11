import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { DashboardComponent } from './components/dashboard/dashboard';
import { AccountDetailsComponent } from './components/account-details/account-details';
import { BalanceViewComponent } from './components/balance-view/balance-view';
import { authGuard } from './guards/auth.guard';
import { TransferComponent } from './components/transfer/transfer';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { 
    path: 'dashboard', 
    component: DashboardComponent,
    canActivate: [authGuard]
  },
  { 
    path: 'account-details/:id', 
    component: AccountDetailsComponent,
    canActivate: [authGuard]
  },
  { 
    path: 'balance/:id', 
    component: BalanceViewComponent,
    canActivate: [authGuard]
  },
  { 
    path: 'transfer/:id', 
    component: TransferComponent,
    canActivate: [authGuard]
  },
  { path: '**', redirectTo: '/login' }
];