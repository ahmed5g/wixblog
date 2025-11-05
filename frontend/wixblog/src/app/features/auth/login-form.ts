import { ChangeDetectionStrategy, Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { AuthenticationService } from '../../shared/services/services/authentication.service';
import { AuthState } from '../../features/auth/auth-state';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule],
  template: `
    <section class="grid h-screen place-items-center bg-base-100">
      <div class="card w-96 bg-base-100 shadow-xl p-6 text-center">
        <h2 class="text-2xl font-bold mb-2">Welcome</h2>
        <p class="text-base-content/70 mb-6">Sign in to continue</p>

        <!--  Already logged in  -->
        <div *ngIf="user()" class="mb-4">
          <p>You are already logged in.</p>
          <button class="btn btn-primary btn-sm mt-2" routerLink="/">Go home</button>
        </div>

        <!--  Login button  -->
        <button
          *ngIf="!user()"
          class="btn btn-outline btn-wide"
          (click)="loginWithGoogle()">
          <svg class="w-5 h-5 mr-2" viewBox="0 0 24 24">
            <path fill="currentColor" d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/>
            <path fill="currentColor" d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"/>
            <path fill="currentColor" d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"/>
            <path fill="currentColor" d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.56 14.87 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"/>
          </svg>
          Continue with Google
        </button>
      </div>
    </section>
  `,
})
export class LoginForm implements OnInit {
  private readonly auth = inject(AuthenticationService);
  private readonly authState = inject(AuthState);
  private readonly router = inject(Router);

  user = this.authState.user;

  ngOnInit(): void {
    // if already logged in, go home
    if (this.user()) this.router.navigate(['/']);
  }

  loginWithGoogle(): void {
    this.auth.loginWithGoogle(); // redirects to Spring OAuth2
  }
}
