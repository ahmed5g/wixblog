import {HttpEvent, HttpHandler, HttpInterceptor, HttpInterceptorFn, HttpRequest} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Only add credentials for our API calls, not external URLs
    if (req.url.startsWith('http://localhost:8080')) {
      const clonedReq = req.clone({
        withCredentials: true // Important for Spring Session to work
      });
      return next.handle(clonedReq);
    }

    return next.handle(req);
  }
}
