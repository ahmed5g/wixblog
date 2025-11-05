import { inject } from '@angular/core';
import { Router, type CanActivateFn } from '@angular/router';
import { AuthState } from './auth-state';

export const authGuard: CanActivateFn = () => {
  const auth = inject(AuthState);
  const router = inject(Router);

  /* signal read – completely synchronous */
  if (auth.user()) {
    return true;
  }
  return router.createUrlTree(['/login']);
};
