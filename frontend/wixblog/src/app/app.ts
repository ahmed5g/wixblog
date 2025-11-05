import {Component, HostListener, OnInit, signal} from '@angular/core';
import { RouterOutlet, RouterLink } from '@angular/router';
import { SideBarMenu } from './shared/components/side-bar-menu/side-bar-menu';
import { AuthenticationService } from './shared/services/services/authentication.service';
import { AuthResponseDto } from './shared/services/models/auth-response-dto';
import { CommonModule, NgIf } from '@angular/common';
import {window} from 'rxjs';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule],
  templateUrl: './app.html',
  styleUrls: ['./app.scss'],
})
export class App implements OnInit {
  menuVisible = true;
  isMobile = false;
  sidebarVisible: boolean= false;

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
  ngOnInit(): void {
  }
}
