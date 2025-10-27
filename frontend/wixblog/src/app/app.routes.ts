import { Routes } from '@angular/router';
import {LoginForm} from './features/auth/login-form/login-form';
import {DashboardComponent} from './features/admin/dashboard/dashboard';
import {authGuard} from './core/guards/auth-guard';
import {LoginSuccessComponent} from './features/auth/login-success/login-success';
import {Admin} from './features/admin/admin/admin';
import {adminGuard} from './core/guards/admin-guard';

export const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'login', component: LoginForm },
  { path: 'login-success', component: LoginSuccessComponent },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [authGuard]
  },
  {
    path: 'admin',
    component: Admin,
    canActivate: [authGuard, adminGuard]
  },
  { path: '**', redirectTo: '/dashboard' }
];
