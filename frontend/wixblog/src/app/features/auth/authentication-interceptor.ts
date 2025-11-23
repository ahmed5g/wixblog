import {HttpErrorResponse, HttpHandlerFn, HttpInterceptorFn, HttpRequest} from "@angular/common/http";
import {inject} from "@angular/core";
import {ACCESS_TOKEN_HEADER_KEY} from '../../../../environment';

import {catchError, throwError} from "rxjs";
import {AuthService} from './auth-service';


export const authenticationInterceptor: HttpInterceptorFn = (req: HttpRequest<unknown>, next: HttpHandlerFn) => {
  const authService = inject(AuthService);


  const authToken = authService.getToken();

  if (authToken) {
    req = req.clone({
      setHeaders: {
        [ACCESS_TOKEN_HEADER_KEY] : `Bearer ${authToken}`,
      },
    });
  }

  return next(req)
    .pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 403) {
          authService.logout();
        }

        const errorMessage = JSON.stringify(error.error, null, '\t');


        return throwError(() => error);
      })
    )

}
