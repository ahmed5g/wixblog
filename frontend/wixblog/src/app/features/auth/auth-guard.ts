// auth.guard.ts
import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { AuthStore} from './authStore';

export const authGuard: CanActivateFn = (route, state) => {
  const authContext = inject(AuthStore);
  const router = inject(Router);

  if (authContext.isAuthenticated()) {
    return true;
  }


  router.navigate(['/login'], {
    queryParams: { returnUrl: state.url }
  });

  return false;
};
