import {HttpInterceptorFn, HttpErrorResponse} from '@angular/common/http';
import {inject} from '@angular/core';
import {Router} from '@angular/router';
import {catchError, throwError} from 'rxjs';
import {AuthStore} from './authStore';

export const authInterceptor: HttpInterceptorFn=(req, next) => {
  const authContext=inject(AuthStore);
  const router=inject(Router);

  const token=authContext.token();

  // should NOT include the Authorization header
  const excludedUrls=['/auth/login', '/user/register', '/user/login'];
  const isExcluded=excludedUrls.some(url => req.url.includes(url));

  let authRequest=req;

  if (token && !isExcluded) {
    authRequest=req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  return next(authRequest).pipe(
    catchError((error: HttpErrorResponse) => {

      if (error.status===401) {
        // logout user if 401 occurs
        authContext.logout();
        router.navigate(['/login'], {
          queryParams: {returnUrl: router.url}
        });
      }

      // Handle 403 (Forbidden) - User is logged in but doesn't have permission
      // Or they are trying to access login/register while already authenticated
      if (error.status === 403 && authContext.isAuthenticated()) {


        return throwError(() => new Error('Already authenticated. Redirecting...'));
      }

      return throwError(() => error);
    })
  );
};
