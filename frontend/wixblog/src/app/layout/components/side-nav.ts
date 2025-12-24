import { Component, input, signal, effect } from '@angular/core';

@Component({
  selector: 'app-side-nav',
  standalone: true,
  template: `
    <nav class="side-nav"
         [class.closed]="!isOpen()"
         [class.no-transition]="shouldDisableTransition()">
      <div class="inner-container" [class.opacity-0]="!isOpen()">
        <div class="nav-group">
          <a class="nav-item active">
            <svg viewBox="0 0 24 24" fill="currentColor" class="w-6 h-6"><path d="M4 10V21h6v-6h4v6h6V10l-8-7-8 7z"/></svg>
            <span class="nav-text">Home</span>
          </a>
          <a class="nav-item">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" class="w-6 h-6"><path d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" stroke-width="1.5"/></svg>
            <span class="nav-text">Library</span>
          </a>
          <a class="nav-item">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" class="w-6 h-6"><path d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" stroke-width="1.5"/></svg>
            <span class="nav-text">Profile</span>
          </a>
          <a class="nav-item">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" class="w-6 h-6"><path d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" stroke-width="1.5"/></svg>
            <span class="nav-text">Stories</span>
          </a>
          <a class="nav-item">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" class="w-6 h-6"><path d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0h6m0 0v-4a2 2 0 114 0v4m-4 0h4" stroke-width="1.5"/></svg>
            <span class="nav-text">Stats</span>
          </a>
        </div>

        <div class="divider"></div>

        <div class="following-section">
          <h4 class="section-label">Following</h4>
          <button class="discover-btn">
            <svg class="w-5 h-5 shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path d="M12 4v16m8-8H4" stroke-width="2"/></svg>
            <span class="nav-text">Find writers and publications to follow.</span>
          </button>
          <a href="#" class="see-suggestions nav-text">See suggestions</a>
        </div>
      </div>
    </nav>
  `,
  styles: [`
    @reference "tailwindcss";

    .side-nav {
      @apply h-[calc(100vh-57px)] border-r border-gray-100 sticky top-[57px] bg-white
      transition-[width] duration-300 ease-in-out overflow-hidden;
      width: 260px;
    }

    .side-nav.closed {
      @apply w-0 border-r-0 p-0;
    }

    /* Kill transitions immediately when this class is present */
    .no-transition, .no-transition * {
      transition: none !important;
    }

    .inner-container {
      @apply p-4 w-64 transition-opacity duration-200;
    }

    .nav-group { @apply flex flex-col gap-2; }

    .nav-item {
      @apply flex items-center gap-4 px-3 py-2 text-gray-500 hover:text-black
      transition-colors cursor-pointer text-sm no-underline whitespace-nowrap;
    }

    .nav-item.active { @apply text-black font-medium; }
    .divider { @apply h-[1px] bg-gray-100 my-6; }
    .section-label { @apply text-[13px] font-semibold text-gray-900 mb-4 px-3 whitespace-nowrap; }

    .discover-btn {
      @apply flex items-start gap-3 px-3 text-left text-[13px] text-gray-500
      hover:text-black mb-2 bg-transparent border-none cursor-pointer;
    }

    .see-suggestions {
      @apply block px-3 text-[13px] text-green-700 hover:text-black mt-2 no-underline;
    }

    .nav-text { @apply whitespace-nowrap; }
  `]
})
export class SideNav {
  isOpen = input<boolean>(true);

  // Track initial load to skip the very first animation
  shouldDisableTransition = signal(true);

  constructor() {
    // We keep transitions disabled for a short window during initialization
    setTimeout(() => {
      this.shouldDisableTransition.set(false);
    }, 250);
  }
}
