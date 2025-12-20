import {Component, inject, signal} from '@angular/core';
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {AuthProvider} from './auth-provider.enum';
import {GOOGLE_AUTH_URL, GITHUB_AUTH_URL} from '../../../../environment';
import {Router, RouterLink} from '@angular/router';
import {UserControllerService} from '../../shared/services/services/user-controller.service';
import {LoginControllerService} from '../../shared/services/services/login-controller.service';
import {AuthStore} from './authStore';
import {throwError} from 'rxjs';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterLink],
  template: `
    <div class="flex min-h-screen items-center justify-center bg-white p-6 font-sans antialiased">
      <div class="w-full max-w-[380px] flex flex-col items-center">

        <header class="text-center mb-10">
          <h1 class="text-[32px] font-serif tracking-tight leading-tight text-[#242424] font-medium">
            Welcome back.
          </h1>
        </header>

        <div class="flex items-center justify-center w-full gap-3 mb-10">
          <button (click)="loginWith(AuthProvider.google)"
                  class="flex items-center justify-center gap-2 px-4 py-1.5 rounded-full border border-[#e6e6e6] text-[12px] text-[#242424] transition-all hover:border-[#bdbdbd] hover:bg-[#fafafa] active:scale-[0.98]">
            <svg class="h-3.5 w-3.5 shrink-0" viewBox="0 0 24 24">
              <path fill="#4285F4"
                    d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/>
              <path fill="#34A853"
                    d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"/>
              <path fill="#FBBC05"
                    d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"/>
              <path fill="#EA4335"
                    d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"/>
            </svg>
            <span>Google</span>
          </button>

          <button (click)="loginWith(AuthProvider.github)"
                  class="flex items-center justify-center gap-2 px-4 py-1.5 rounded-full border border-[#e6e6e6] text-[12px] text-[#242424] transition-all hover:border-[#bdbdbd] hover:bg-[#fafafa] active:scale-[0.98]">
            <svg class="h-3.5 w-3.5 fill-[#242424] shrink-0" viewBox="0 0 24 24">
              <path
                d="M12 .297c-6.63 0-12 5.373-12 12 0 5.303 3.438 9.8 8.205 11.385.6.113.82-.258.82-.577 0-.285-.01-1.04-.015-2.04-3.338.724-4.042-1.61-4.042-1.61C4.422 18.07 3.633 17.7 3.633 17.7c-1.087-.744.084-.729.084-.729 1.205.084 1.838 1.236 1.838 1.236 1.07 1.835 2.809 1.305 3.495.998.108-.776.417-1.305.76-1.605-2.665-.3-5.466-1.332-5.466-5.93 0-1.31.465-2.38 1.235-3.22-.135-.303-.54-1.523.105-3.176 0 0 1.005-.322 3.3 1.23.96-.267 1.98-.399 3-.405 1.02.006 2.04.138 3 .405 2.28-1.552 3.285-1.23 3.285-1.23.645 1.653.24 2.873.12 3.176.765.84 1.23 1.91 1.23 3.22 0 4.61-2.805 5.625-5.475 5.92.42.36.81 1.096.81 2.22 0 1.606-.015 2.896-.015 3.286 0 .315.21.69.825.57C20.565 22.092 24 17.592 24 12.297c0-6.627-5.373-12-12-12"/>
            </svg>
            <span>GitHub</span>
          </button>
        </div>

        <div class="w-full flex items-center mb-8">
          <div class="flex-grow border-t border-[#f2f2f2]"></div>
          <span class="mx-3 text-[12px] text-slate-400 font-light italic">or</span>
          <div class="flex-grow border-t border-[#f2f2f2]"></div>
        </div>

        <form [formGroup]="loginForm" (ngSubmit)="onSubmit()" class="w-full space-y-7">
          <div class="relative group">
            <label class="block text-[12px] font-medium text-slate-500 mb-0.5 ml-0.5">Your email</label> <input
            id="email" formControlName="email" type="email"
            class="w-full py-1.5 border-b border-[#e6e6e6] outline-none transition-colors focus:border-[#242424] bg-transparent text-[#242424] text-[14px]"
            [class.border-red-500]="isInvalid('email')">
          </div>

          <div class="relative group">
            <div class="flex justify-between items-center mb-0.5 ml-0.5">
              <label class="block text-[12px] font-medium text-slate-500">Your password</label> <a href="#"
                                                                                                   class="text-[11px] text-slate-400 hover:text-[#242424] transition-colors underline underline-offset-2">Forgot?</a>
            </div>
            <input id="password" formControlName="password" type="password"
                   class="w-full py-1.5 border-b border-[#e6e6e6] outline-none transition-colors focus:border-[#242424] bg-transparent text-[#242424] text-[14px]"
                   [class.border-red-500]="isInvalid('password')">
          </div>

          @if (errorMessage()) {
            <p class="text-[12px] text-red-600 text-center italic">{{ errorMessage() }}</p>
          }
          @if (redirectionMessage()) {
            <div class="alert alert-info">
              {{ redirectionMessage() }}
              <div class="spinner-border spinner-border-sm" role="status"></div>
            </div>
          }

          <div class="flex flex-col items-center gap-6 pt-2">
            <button type="submit" [disabled]="loginForm.invalid || isLoading()"
                    class="w-[120px] py-2 rounded-full bg-[#191919] text-white text-[13.5px] font-medium hover:bg-black transition-all disabled:opacity-20 active:scale-95">
              {{ isLoading() ? '...' : 'Continue' }}
            </button>

            <p class="text-[13px] text-[#242424]">
              No account?
              <button type="button" class="text-[#1a8917] font-bold hover:text-[#156d12] transition-colors ml-1"
                      routerLink="/wrapper">Create one
              </button>
            </p>
          </div>
        </form>

        <footer class="mt-16 text-center pt-6">
          <p class="text-[11px] leading-relaxed text-slate-400">
            Forgot email or trouble signing in? <br> <a href="#"
                                                        class="underline hover:text-[#242424] transition-colors">Get
            help from support</a>.
          </p>
        </footer>
      </div>
    </div>
  `
})
export class Login {
  private fb=inject(FormBuilder);
  isLoading=signal(false);
  errorMessage=signal<string | null>(null);
  submitted=signal(false);
  private loginService=inject(LoginControllerService);
  protected readonly AuthProvider=AuthProvider;
  protected readonly navigator=navigator;
  private router=inject(Router);
  private authStore=inject(AuthStore);
  redirectionMessage = signal<string | null>(null);


  loginForm=this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]]
  });

  isInvalid(controlName: string): boolean {
    const control=this.loginForm.get(controlName);
    return !!(this.submitted() && control?.invalid);
  }

  loginWith(provider: AuthProvider) {
    const urls: Partial<Record<AuthProvider, string>> ={
      [AuthProvider.google]: GOOGLE_AUTH_URL,
      [AuthProvider.github]: GITHUB_AUTH_URL
    };
    const url=urls[provider];
    if (url) window.location.href=url;
  }


  onSubmit() {

    if (this.authStore.isAuthenticated()) {
      this.redirectionMessage.set(`You are already connected with ${this.authStore.user()!.email} Redirecting ...`);

      setTimeout(() => {
        this.router.navigate(['/']);
      }, 6000);

      return;
    }
    this.submitted.set(true);
    if (this.loginForm.valid) {
      this.isLoading.set(true);
      this.loginService.login({
        body: {
          email: this.loginForm.value.email!,
          password: this.loginForm.value.password!
        }
      })
        .subscribe({
          next: (res:any) => {
            this.authStore.setToken(res.accessToken);
            this.router.navigate(['/']);
          },
          error: (err) => console.error('Login failed', err)
        });
    }

  }
}



