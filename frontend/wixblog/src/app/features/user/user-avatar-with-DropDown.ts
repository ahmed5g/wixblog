import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  Output,
  inject,
  booleanAttribute
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap'; // ← Import here
import { UserAvatar } from './user-avatar';
import { AuthState } from '../auth/auth-state';

export type MenuAction = 'profile' | 'library' | 'stories' | 'stats' | 'settings' | 'signout';

@Component({
  selector: 'app-user-avatar-with-dropdown',
  standalone: true,
  imports: [CommonModule, UserAvatar, NgbDropdownModule], // ← Add to imports
  template: `
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
          [src]="auth.user()?.profilePicture"
          [alt]="auth.user()?.name ?? 'User avatar'"
          [size]="size"
          [hover]="true">
        </app-user-avatar>
      </button>

      <div ngbDropdownMenu class="shadow border-0" style="min-width: 240px;">
        <!-- Header -->
        <div class="dropdown-header d-flex align-items-center gap-3 px-3 py-2 border-bottom bg-light">
          <app-user-avatar
            [src]="auth.user()?.profilePicture"
            [alt]="auth.user()?.name ?? 'User avatar'"
            size="md">
          </app-user-avatar>
          <div class="lh-sm">
            <p class="fw-semibold text-dark mb-0">{{ auth.user()?.name ?? '—' }}</p>
            <p class="text-muted small mb-0">&#64;{{ handle() }}</p>
          </div>
        </div>

        <!-- Menu Items -->
        <button
          *ngFor="let item of menu"
          ngbDropdownItem
          class="dropdown-item"
          (click)="onMenuItemClick(item.action)">
          {{ item.label }}
        </button>

        <!-- Divider -->
        <div class="dropdown-divider"></div>

        <!-- Sign Out -->
        <button
          ngbDropdownItem
          class="dropdown-item text-danger"
          (click)="onMenuItemClick('signout')">
          Sign out
        </button>
      </div>
    </div>
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
  `],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UserAvatarWithDropDown {
  @Input() size: 'xs' | 'sm' | 'md' | 'lg' | 'xl' = 'md';
  @Input({transform: booleanAttribute}) ring = false;
  @Output() action = new EventEmitter<MenuAction>();

  public readonly auth = inject(AuthState);
  private readonly router = inject(Router);

  protected readonly menu: { label: string; action: MenuAction }[] = [
    { label: 'Profile', action: 'profile' },
    { label: 'Library', action: 'library' },
    { label: 'Stories', action: 'stories' },
    { label: 'Stats', action: 'stats' },
    { label: 'Settings', action: 'settings' },
  ];

  protected handle(): string {
    const email = this.auth.user()?.email;
    return email ? email.split('@')[0] : '—';
  }

  protected onMenuItemClick(action: MenuAction): void {
    this.action.emit(action);

    switch (action) {
      case 'profile':
        this.router.navigate(['/user']);
        break;
      case 'signout':
        this.auth.logout();
        this.router.navigate(['/login']);
        break;
    }
  }
}
