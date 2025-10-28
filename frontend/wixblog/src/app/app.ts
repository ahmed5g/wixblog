import {Component, OnInit, signal} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {AuthService} from './core/auth/auth';
import {Header} from './core/layout/header/header';
import {Footer} from './core/layout/footer/footer';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet,
    Header,
    Footer

  ],
  templateUrl: './app.html',
  standalone: true,
  styleUrl: './app.scss'
})
export class App implements OnInit{
  protected readonly title = signal('wixblog');
  constructor(private authService: AuthService) {}

  ngOnInit() {
    this.authService.checkAuthStatus();
  }
}
