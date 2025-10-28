import { Routes } from '@angular/router';
import {LoginForm} from './features/auth/login-form/login-form';
import { UserProfile} from './features/user/user-profile/user-profile';
import {authGuard} from './core/guards/auth-guard';
import {LoginSuccessComponent} from './features/auth/login-success/login-success';
import {Admin} from './features/admin/admin/admin';
import {adminGuard} from './core/guards/admin-guard';
import {Main} from './features/home/main/main';

export const routes: Routes = [
  { path: 'login', component: LoginForm },
  { path: 'login-success', component: LoginSuccessComponent },

  {
    path: '',
    component: Main,
    canActivate: [authGuard]
  },
  {
    path: 'profile',
    component: UserProfile,
    canActivate: [authGuard]
  },
  {
    path: 'admin',
    component: Admin,
    canActivate: [authGuard, adminGuard]
  },
  { path: '**', redirectTo: '/main', pathMatch: 'full' }
];
