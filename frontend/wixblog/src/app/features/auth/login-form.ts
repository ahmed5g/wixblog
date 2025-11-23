import {Component, OnInit} from '@angular/core';

import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {GOOGLE_AUTH_URL, GITHUB_AUTH_URL} from '../../../../environment';


import {CommonModule} from '@angular/common';
import {AuthService} from './auth-service';
import {ApiService} from './ApiService';
import { AuthProvider } from "./auth-provider.enum";



@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CommonModule
  ],
  template: `

    <div class="flex min-h-screen items-center justify-center bg-gray-50 px-4 py-12 sm:px-6 lg:px-8">
      <div class="w-full max-w-md space-y-8">
        <!-- Card Container -->
        <div class="bg-white shadow-2xl rounded-lg border border-gray-200 p-8">

          <!-- Header -->
          <div class="text-center mb-8">
            <h2 class="text-3xl font-bold tracking-tight text-gray-900">
              Log in to your account
            </h2>
            <p class="mt-2 text-sm text-gray-600">
              Welcome back! Please sign in to continue.
            </p>
          </div>

          <!-- Social Login Buttons -->
          <div class="mb-6">
            <div class="grid grid-cols-2 gap-3">
              <!-- Google Login -->
              <button
                (click)="loginWithProvider(AuthProvider.google)"
                type="button"
                class="flex w-full items-center justify-center gap-3 rounded-md bg-white px-3 py-2 text-sm font-semibold text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 hover:bg-gray-50 focus-visible:ring-transparent transition-colors duration-200">
                <svg class="h-5 w-5" viewBox="0 0 24 24">
                  <path fill="#4285F4"
                        d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/>
                  <path fill="#34A853"
                        d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"/>
                  <path fill="#FBBC05"
                        d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"/>
                  <path fill="#EA4335"
                        d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"/>
                </svg>
                Google
              </button>

              <!-- GitHub Login -->
              <button
                (click)="loginWithProvider(AuthProvider.github)"
                type="button"
                class="flex w-full items-center justify-center gap-3 rounded-md bg-gray-900 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-gray-800 focus-visible:ring-transparent transition-colors duration-200">
                <svg class="h-5 w-5" fill="currentColor" viewBox="0 0 20 20">
                  <path fill-rule="evenodd"
                        d="M10 0C4.477 0 0 4.484 0 10.017c0 4.425 2.865 8.18 6.839 9.504.5.092.682-.217.682-.483 0-.237-.008-.868-.013-1.703-2.782.605-3.369-1.343-3.369-1.343-.454-1.158-1.11-1.466-1.11-1.466-.908-.62.069-.608.069-.608 1.003.07 1.531 1.032 1.531 1.032.892 1.53 2.341 1.088 2.91.832.092-.647.35-1.088.636-1.338-2.22-.253-4.555-1.113-4.555-4.951 0-1.093.39-1.988 1.029-2.688-.103-.253-.446-1.272.098-2.65 0 0 .84-.27 2.75 1.026A9.564 9.564 0 0110 4.844c.85.004 1.705.115 2.504.337 1.909-1.296 2.747-1.027 2.747-1.027.546 1.379.203 2.398.1 2.651.64.7 1.028 1.595 1.028 2.688 0 3.848-2.339 4.695-4.566 4.942.359.31.678.921.678 1.856 0 1.338-.012 2.419-.012 2.747 0 .268.18.58.688.482A10.019 10.019 0 0020 10.017C20 4.484 15.522 0 10 0z"
                        clip-rule="evenodd"/>
                </svg>
                GitHub
              </button>
            </div>
          </div>

          <!-- Divider -->
          <div class="relative mb-6">
            <div class="absolute inset-0 flex items-center">
              <div class="w-full border-t border-gray-300"></div>
            </div>
            <div class="relative flex justify-center text-sm">
              <span class="bg-white px-2 text-gray-500">Or continue with email</span>
            </div>
          </div>

          <!-- Login Form -->
          <form [formGroup]="loginForm" class="space-y-6">
            <!-- Email Field -->
            <div>
              <label for="email" class="block text-sm font-medium leading-6 text-gray-900"> Email address </label>
              <div class="mt-2">
                <input
                  id="email"
                  formControlName="email"
                  name="email"
                  type="email"
                  autocomplete="email"
                  class="block w-full rounded-md border-0 py-3 px-4 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-blue-600 sm:text-sm sm:leading-6 transition-colors duration-200"
                  placeholder="Enter your email">
                <div *ngIf="submitted && loginForm.get('email')?.errors" class="mt-1 text-sm text-red-600">
                  <span *ngIf="loginForm.get('email')?.errors?.['required']">Email is required</span> <span
                  *ngIf="loginForm.get('email')?.errors?.['email']">Please enter a valid email</span>
                </div>
              </div>
            </div>

            <!-- Password Field -->
            <div>
              <label for="password" class="block text-sm font-medium leading-6 text-gray-900"> Password </label>
              <div class="mt-2">
                <input
                  id="password"
                  formControlName="password"
                  name="password"
                  type="password"
                  autocomplete="current-password"
                  class="block w-full rounded-md border-0 py-3 px-4 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-blue-600 sm:text-sm sm:leading-6 transition-colors duration-200"
                  placeholder="Enter your password">
                <div *ngIf="submitted && loginForm.get('password')?.errors" class="mt-1 text-sm text-red-600">
                  <span *ngIf="loginForm.get('password')?.errors?.['required']">Password is required</span>
                </div>
              </div>
            </div>

            <!-- Error Message -->
            <div *ngIf="errorResponse" class="rounded-md bg-red-50 p-4">
              <div class="flex">
                <div class="flex-shrink-0">
                  <svg class="h-5 w-5 text-red-400" viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd"
                          d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.28 7.22a.75.75 0 00-1.06 1.06L8.94 10l-1.72 1.72a.75.75 0 101.06 1.06L10 11.06l1.72 1.72a.75.75 0 101.06-1.06L11.06 10l1.72-1.72a.75.75 0 00-1.06-1.06L10 8.94 8.28 7.22z"
                          clip-rule="evenodd"/>
                  </svg>
                </div>
                <div class="ml-3">
                  <h3 class="text-sm font-medium text-red-800">
                    Authentication Error
                  </h3>
                  <div class="mt-2 text-sm text-red-700">
                    <p>{{ errorMessage }}</p>
                  </div>
                </div>
              </div>
            </div>

            <!-- Submit Button -->
            <div>
              <button
                type="submit"
                (click)="onSubmit()"
                [disabled]="loginForm.invalid || loading"
                class="flex w-full justify-center rounded-md bg-blue-600 px-3 py-3 text-sm font-semibold text-white shadow-sm hover:bg-blue-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-blue-600 disabled:bg-blue-300 disabled:cursor-not-allowed transition-colors duration-200">
                <span *ngIf="!loading">Log in</span> <span *ngIf="loading" class="flex items-center">
              <svg class="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none"
                   viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor"
                      d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              Signing in...
            </span>
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>

  `,
  styles: `
    :host {
      display: block;
      background-color: #f9fafb;
    }

    /* Custom animations */
    .animate-spin {
      animation: spin 1s linear infinite;
    }

    @keyframes spin {
      from {
        transform: rotate(0deg);
      }
      to {
        transform: rotate(360deg);
      }
    }

    /* Focus states */
    input:focus {
      outline: 2px solid transparent;
      outline-offset: 2px;
    }

    /* Smooth transitions */
    * {
      transition-property: color, background-color, border-color, text-decoration-color, fill, stroke;
      transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
      transition-duration: 150ms;
    }
  `
})
export class Login implements OnInit {
  authProvider?: AuthProvider;
  loginForm!: FormGroup;
  loading = false;
  submitted = false;

  errorResponse: boolean = false;
  errorMessage: string = '';

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private authService: AuthService,
    private apiService: ApiService
  ) {
  }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    this.errorResponse = false;
    this.submitted = true;

    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;
    this.apiService.login(this.loginForm.value)
      .subscribe({
        next: data => {
          const token = JSON.parse(JSON.stringify(data)).accessToken;
          if (token) {
            this.authProvider = AuthProvider.local;
            this.authService.setAuthentication(token);
            this.router.navigate(['/pages', 'profile', this.authProvider], {state: {from: this.router.routerState.snapshot.url}});
          } else {
            this.errorResponse = true;
            this.errorMessage = "Authentication failed.";
          }
        },
        error: error => {
          this.errorResponse = true;
          this.errorMessage = error.error.message;
          this.loading = false;
        }
      });
  }

  loginWithProvider(provider: AuthProvider) {
    switch (provider) {
      case AuthProvider.google:
        window.location.href = GOOGLE_AUTH_URL;
        break;
      case AuthProvider.github:
        window.location.href = GITHUB_AUTH_URL;
        break;
      default:
        console.error('Unknown provider');
    }
  }

  protected readonly AuthProvider = AuthProvider;
}
