import {ApplicationConfig, importProvidersFrom, provideZoneChangeDetection} from '@angular/core';
import { provideRouter } from '@angular/router';
import { QuillModule } from 'ngx-quill';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { routes } from './app.routes';

import { HttpInterceptorFn } from '@angular/common/http';
import {provideClientHydration} from '@angular/platform-browser';
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';
import {authenticationInterceptor} from './features/auth/authentication-interceptor';



export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    importProvidersFrom(QuillModule.forRoot()),
    provideClientHydration(),

    provideAnimationsAsync(),


    provideHttpClient(
      withFetch(),

      withInterceptors([authenticationInterceptor])
    )
  ]
};
