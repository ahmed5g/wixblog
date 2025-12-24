import { Component, inject, output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';

import { MenuAction, UserAvatarWithDropDown } from '../../features/user/user-avatar-with-DropDown';
import { AuthStore } from '../../features/auth/authStore';

@Component({
  selector: 'app-top-bar',
  standalone: true,
  imports: [CommonModule, UserAvatarWithDropDown, RouterLink],
  template: `
    <header class="top-bar">
      <div class="flex items-center flex-1 min-w-0 h-full">
        <button (click)="toggleSidebar.emit()" class="menu-btn shrink-0" type="button">
          <svg class="icon-md" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path d="M4 6h16M4 12h16M4 18h16" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </button>

        <img routerLink="/" src="https://upload.wikimedia.org/wikipedia/commons/0/0d/Medium_logo_v2.svg"
             alt="Medium" class="logo" />

        <div class="search-container">
          <div
            class="search-input-wrapper"
            [ngClass]="isSearchFocused() ? 'search-active' : 'search-inactive'"
          >
            <svg class="search-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor">
              <path d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" stroke-width="2" stroke-linecap="round"/>
            </svg>
            <input
              type="text"
              placeholder="Search"
              class="search-input"
              (focus)="isSearchFocused.set(true)"
              (blur)="handleBlur()"
            />
          </div>

          @if (isSearchFocused()) {
            <div class="search-popup">
              <div class="search-popup-arrow"></div>

              <button
                (mousedown)="onNavigateToTopics($event)"
                class="search-popup-item"
                type="button">
                <div class="search-item-icon-circle">
                  <svg class="w-4 h-4 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <circle cx="12" cy="12" r="9" stroke-width="1.5"/>
                    <path d="M15 9l-2.25 4.5L9 15l2.25-4.5L15 9z" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </div>

                <span class="search-item-text">Explore topics</span>

                <svg class="w-4 h-4 text-black ml-auto" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 7l-10 10M17 7H7M17 7v10" />
                </svg>
              </button>
            </div>
          }
        </div>
      </div>

      <div class="nav-actions">
        @if (authStore.isAuthenticated() && authStore.user()) {
          <button routerLink="write" class="write-btn" type="button">
            <svg class="icon-md" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" stroke-width="1.5"/>
            </svg>
            <span class="hidden sm:inline">Write</span>
          </button>

          <app-user-avatar-with-dropdown
            class="cursor-pointer"
            size="sm"
            [ring]="true"
            (action)="onMenuAction($event)"/>
        } @else {
          <button routerLink="/auth/login" class="signin-btn">Sign In</button>
          <button routerLink="/register" class="get-started-btn">Get Started</button>
        }
      </div>
    </header>
  `,
  styles: `
    @reference "tailwindcss";

    .top-bar { @apply fixed top-0 left-0 w-full flex items-center justify-between px-6 bg-white border-b border-gray-100 z-[100] h-[57px]; }
    .nav-actions { @apply flex items-center gap-4 shrink-0 ml-4 h-full; }
    .icon-md { @apply w-6 h-6; }
    .logo { @apply h-5 cursor-pointer shrink-0 ml-4; }
    .menu-btn { @apply p-1 hover:bg-gray-100 rounded-full transition-colors border-none bg-transparent cursor-pointer text-gray-600; }
    .write-btn { @apply bg-transparent border-none flex items-center gap-2 text-gray-500 text-sm hover:text-black transition-colors cursor-pointer; }
    .signin-btn { @apply text-sm text-gray-500 hover:text-black transition-colors bg-transparent border-none cursor-pointer; }
    .get-started-btn { @apply bg-black text-white px-4 py-2 rounded-full text-sm hover:bg-neutral-800 transition-colors whitespace-nowrap; }

    .search-container { @apply relative flex items-center ml-4 w-full max-w-[240px] min-w-[180px]; }
    .search-input-wrapper { @apply flex items-center w-full rounded-full px-4 py-2 transition-all duration-200 border; }
    .search-active { @apply bg-white border-gray-200 shadow-none; }
    .search-inactive { @apply bg-[#F9F9F9] border-transparent; }
    .search-input { @apply bg-transparent border-none text-sm outline-none w-full placeholder-gray-500; }
    .search-icon { @apply w-4 h-4 text-gray-500 mr-2 shrink-0; }

    .search-popup { @apply absolute top-[calc(100%+14px)] left-0 w-[350px] bg-white border border-gray-200 rounded-lg py-2 z-[110] shadow-none; }
    .search-popup-arrow { @apply absolute -top-[6px] left-6 w-3 h-3 bg-white border-t border-l border-gray-200 rotate-45; }
    .search-popup-item { @apply w-full flex items-center gap-4 px-4 py-3 hover:bg-gray-50 transition-colors border-none bg-transparent cursor-pointer text-left; }
    .search-item-icon-circle { @apply w-8 h-8 rounded-full border border-gray-200 flex items-center justify-center shrink-0 bg-white; }
    .search-item-text { @apply font-normal text-gray-900 text-[15px] antialiased; }
  `
})
export class TopBar {
  private readonly router = inject(Router);
  public readonly authStore = inject(AuthStore);

  isSearchFocused = signal(false);
  toggleSidebar = output<void>();

  handleBlur(): void {
    // Keep timeout to prevent popup from flickering during normal interaction
    setTimeout(() => this.isSearchFocused.set(false), 200);
  }

  onNavigateToTopics(event: MouseEvent): void {
    // 1. Prevent input blur from winning the race
    event.preventDefault();
    // 2. Close the state immediately
    this.isSearchFocused.set(false);
    // 3. Navigate
    this.router.navigate(['/pages/topics']);
  }

  onMenuAction(action: MenuAction): void {
    if (action === 'profile') this.router.navigate(['/user']);
    if (action === 'signout') this.router.navigate(['/auth/login']);
  }
}
