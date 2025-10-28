import {Component} from '@angular/core';
import { AuthService} from '../../../core/auth/auth';

import {Router} from '@angular/router';
import {NgIf} from '@angular/common';




@Component({
  selector: 'app-login-form',
  imports: [

  ],
  templateUrl: './login-form.html',
  styleUrl: './login-form.scss',
  standalone: true
})
export class LoginForm {
  constructor(private authService: AuthService, private router: Router) {}

  loginWithGoogle(): void {
    this.authService.loginWithGoogle();
  }
}
