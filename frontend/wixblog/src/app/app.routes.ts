import {Routes} from '@angular/router';

import {NotFound} from './features/util/not-found';
import {Landing} from './features/landing';
import {GetStarted} from './features/home/get-started';
import {Login} from './features/auth/login-form';
import {authGuard} from './features/auth/auth-guard';
import {postUpload} from './features/post/post-upload';
import {Registration} from './features/auth/register/Stepper/registration';
import {UserInfo} from './features/auth/register/Stepper/user-info';
import {RegisterStepper} from './features/auth/register/register-wrapper';
import {guestGuard} from './features/auth/guest.guard';
import {Layout} from './layout/layout';


export const routes: Routes=[
  {
    path: '',
    component: Layout,
    children: [
      {path: '', component: Landing},
      {path: 'welcome', component: GetStarted},
      {
        path: 'pages',
        loadChildren: () => import('./features/pages.routes').then(m => m.Pages)
      },
      {
        path: 'user',
        loadChildren: () => import('./features/user/user.routes').then(m => m.UserRoutes),
        canActivate: [authGuard]
      }
    ]
  },
  {path: 'auth', loadChildren: () => import('./features/auth/auth.routes').then(m => m.AUTH_ROUTES) },
  {path: 'notfound', component: NotFound},
  {path: "write", component: postUpload},
  {path: "wrapper", component: RegisterStepper},



  {path: '**', redirectTo: '/notfound'}
];
