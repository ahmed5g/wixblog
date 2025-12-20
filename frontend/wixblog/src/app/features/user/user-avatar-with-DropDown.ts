// user-avatar-with-dropdown.component.ts
import {Component, EventEmitter, Input, Output, inject, booleanAttribute} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Router} from '@angular/router';
import {NgbDropdownModule} from '@ng-bootstrap/ng-bootstrap';
import {UserAvatar} from './user-avatar';
import { AuthStore} from '../auth/authStore';

export type MenuAction='profile' | 'library' | 'stories' | 'stats' | 'settings' | 'signout';

@Component({
  selector: 'app-user-avatar-with-dropdown',
  standalone: true,
  imports: [CommonModule, UserAvatar, NgbDropdownModule],
  template: `
    @if (auth.isAuthenticated()) {
      <div ngbDropdown class="d-inline-block" placement="bottom-end">
        <button
          class="btn p-0 border-0 bg-transparent"
          type="button"
          ngbDropdownToggle
          [class.border]="ring"
          [class.border-primary]="ring"
          [class.border-2]="ring"
          aria-expanded="false">
          <app-user-avatar
            [size]="size"
            [hover]="true"
            [src]="auth.profilePicture()"
            [alt]="auth.user()?.name || 'User'">
          </app-user-avatar>
        </button>

        <div ngbDropdownMenu class="shadow border-0" style="min-width: 240px;">
          <!-- Header -->
          <div class="dropdown-header d-flex align-items-center gap-3 px-3 py-2 border-bottom bg-light">
            <app-user-avatar
              size="md"
              [src]="auth.profilePicture()"
              [alt]="auth.user()?.name || 'User'">
            </app-user-avatar>
            <div class="lh-sm">
              <p class="fw-semibold text-dark mb-0">{{ auth.user()?.name || 'User' }}</p>
              <p class="text-muted small mb-0">&#64;{{ auth.user()?.email }}</p>
            </div>
          </div>

          <!-- Menu Items -->
          @for (item of menu; track item.action) {
            <button
              ngbDropdownItem
              class="dropdown-item"
              (click)="onMenuItemClick(item.action)">
              {{ item.label }}
            </button>
          }

          <div class="dropdown-divider"></div>

          <button
            ngbDropdownItem
            class="dropdown-item text-danger"
            (click)="onMenuItemClick('signout')">
            Sign out
          </button>
        </div>
      </div>
    }
  `,

  styles: [`
    .dropdown-toggle::after {
      display: none;
    }

    .dropdown-item {
      font-size: 0.875rem;
      cursor: pointer;
    }

    .dropdown-header {
      padding: 0.75rem 1rem;
    }
  `]
})
export class UserAvatarWithDropDown {
  @Input() size: 'xs' | 'sm' | 'md' | 'lg' | 'xl'='md';
  @Input({transform: booleanAttribute}) ring=false;
  @Output() action=new EventEmitter<MenuAction>();

  protected readonly auth=inject(AuthStore);
  private readonly router=inject(Router);

  protected readonly menu=[
    {label: 'Profile', action: 'profile' as MenuAction},
    {label: 'Library', action: 'library' as MenuAction},
    {label: 'Stories', action: 'stories' as MenuAction},
    {label: 'Stats', action: 'stats' as MenuAction},
    {label: 'Settings', action: 'settings' as MenuAction},
  ];


  protected onMenuItemClick(action: MenuAction): void {
    this.action.emit(action);

    switch(action) {
      case 'profile':
        this.router.navigate(['/user']);
        break;
      case 'signout':
        this.auth.logout();
        this.router.navigate(['/']);
        break;
    }
  }
}
