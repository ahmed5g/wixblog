import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import {AuthService} from '../auth/auth';


export const authGuard: CanActivateFn = async (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // Use the async isLoggedIn method
  const isAuthenticated = await authService.isLoggedIn();

  if (isAuthenticated) {
    return true;
  } else {
    router.navigate(['/login']);
    return false;
  }
};
