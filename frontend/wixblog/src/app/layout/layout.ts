import {Component, inject, signal} from '@angular/core';
import {TopBar} from './components/top-bar';
import {SideNav} from './components/side-nav';
import {RouterOutlet} from '@angular/router';
import {UIStore} from './UIStore';


@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [TopBar, SideNav, RouterOutlet],
  template: `
    <div class="layout-container">
      <app-top-bar (toggleSidebar)="uiStore.toggle()"></app-top-bar>

      <div class="main-body">
      <app-side-nav [isOpen]="uiStore.isOpen()"></app-side-nav>

      <main class="content-area">

            <router-outlet></router-outlet>

        </main>
      </div>
    </div>
  `,
  styles: [`
    @reference "tailwindcss";
    .layout-container { @apply min-h-screen bg-white; }
    .main-body { @apply flex; }
    .content-area { @apply flex-1 min-w-0 h-[calc(100vh-57px)] overflow-y-auto p-8; }
  `]
})
export class Layout {


  readonly uiStore = inject(UIStore);
}
