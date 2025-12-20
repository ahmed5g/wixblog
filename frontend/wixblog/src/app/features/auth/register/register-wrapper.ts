import {Component, inject, signal} from '@angular/core';

import {RegistrationStore} from './registrationStore';
import {UserInfo} from './Stepper/user-info';
import {Registration} from './Stepper/registration';
import {getState} from '@ngrx/signals';
import {LoginControllerService} from '../../../shared/services/services/login-controller.service';
import {Router} from '@angular/router';
import {AuthStore} from '../authStore';
import {delay, switchMap} from 'rxjs/operators';
import {UserControllerService} from '../../../shared/services/services/user-controller.service';

@Component({
  selector: 'app-register-stepper',
  standalone: true,
  imports: [
    Registration,
    UserInfo
  ],
  template: `
    <div class="stepper-wrapper">
      @if (currentStep() === 1) {
        <app-registration (next)="currentStep.set(2)"/>
      }

      @if (currentStep() === 2) {
        <app-user-info (back)="currentStep.set(1)" (finish)="handleRegister()"/>
      }
    </div>
  `,
  styles: [`


  `]
})
export class RegisterStepper {
  currentStep = signal(1);
  isLoading = signal(false);

  readonly store = inject(RegistrationStore);
  private authStore = inject(AuthStore);
  private loginService = inject(LoginControllerService);
  private userService = inject(UserControllerService);
  private router = inject(Router);


  //todo we need the profile picture size to be minimal so that the token size does not increase too much?
  handleRegister() {
    this.isLoading.set(true);
    const payload = getState(this.store);



    this.userService.createUser({ body: payload }).pipe(
      delay(500),
      switchMap(() => {
        return this.loginService.login({
          body: {
            email: payload.email!,
            password: payload.password!
          }
        });
      })
    ).subscribe({
      next: (res: any) => {
        if (res.accessToken) {
          this.authStore.setToken(res.accessToken);
          this.store.reset();
          this.isLoading.set(false);
          this.router.navigate(['/']);
        } else {
          console.error('No accessToken found in response', res);
          this.isLoading.set(false);
        }
      },
      error: (err) => {
        this.isLoading.set(false);
        console.error('Registration or Login failed', err);
      }
    });
  }

}


