import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../../core/auth/auth';
import {Router} from '@angular/router';
import {retry} from 'rxjs';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-login-success',
  imports: [
    NgIf
  ],
  templateUrl: './login-success.html',
  styleUrl: './login-success.scss',
  standalone: true
})
export class LoginSuccessComponent implements OnInit {
  isLoading = true;
  statusMessage = 'Checking authentication status...';
  success = false;
  constructor(
    private authService: AuthService,
    private router: Router
  ) {
  }

  async ngOnInit(): Promise<void> {
    await this.checkAuthAndRedirect();
  }

  async checkAuthAndRedirect(): Promise<void> {
    this.isLoading = true;
    this.statusMessage = 'Checking authentication status...';

    try {
      // ✅ FIX: Use await with the async isLoggedIn() method
      const isAuthenticated = await this.authService.isLoggedIn();

      if (isAuthenticated) {
        this.statusMessage = 'Redirecting to dashboard...';
        this.success = true;
        this.router.navigate(['/dashboard']);
      } else {
        this.statusMessage = 'Authentication failed. Please try again.';
        this.isLoading = false;
      }
    } catch (error) {
      this.statusMessage = 'Error checking authentication. Please try again.';
      this.isLoading = false;
    }
  }

  retry(): void {
    this.checkAuthAndRedirect();
  }
}
