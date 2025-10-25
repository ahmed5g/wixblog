import { Routes } from '@angular/router';
import {Home} from './features/home/home/home';
import {Welcome} from './features/home/welcome/welcome';

export const routes: Routes = [
  { path: 'home', component: Home },
  {
    path: 'auth',
    loadChildren: () => import('./features/auth/auth.routes').then(m => m.AUTH_ROUTES)
  },
  { path: '**', redirectTo: '' },
  { path: '', component: Welcome },

];
