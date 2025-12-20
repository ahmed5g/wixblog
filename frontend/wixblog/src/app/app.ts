import {Component, HostListener, inject, OnInit} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {CommonModule} from '@angular/common';
import { AuthStore} from './features/auth/authStore';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule],
  templateUrl: './app.html',
  styleUrls: ['./app.scss'],
})
export class App implements OnInit {
  private authContext = inject(AuthStore);
  menuVisible = true;
  isMobile = false;
  sidebarVisible: boolean = false;

  constructor() {
    this.onResize();
  }


  @HostListener('window:resize', ['$event'])
  onResize(event?: any) {
    this.isMobile = globalThis.window.innerWidth < 1024;
    if (this.isMobile) {
      this.menuVisible = false;
    } else {
      this.menuVisible = true;
    }
  }

  toggleMenu() {
    this.menuVisible = !this.menuVisible;
  }

  onMainScroll(event: any) {
    const scrollTop = event.target.scrollTop;
    this.sidebarVisible = scrollTop > 100; // Show sidebar after scrolling 100px
  }


  ngOnInit() {

  }
}
