// user-avatar.component.ts
import { Component, Input, booleanAttribute, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import {AuthStore} from '../../../features/auth/authStore';


@Component({
  selector: 'app-user-avatar',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="avatar" [class]="sizeClass">
      @if (imageSrc && !imageError) {
        <img
          [src]="imageSrc"
          [alt]="altText"
          class="avatar-img"
          (load)="onImageLoad()"
          (error)="onImageError()"
        />
      } @else {
        <div class="avatar-fallback">
          {{ initials }}
        </div>
      }
    </div>
  `,
  styles: [`
    .avatar {
      border-radius: 50%;
      overflow: hidden;
      display: inline-flex;
      align-items: center;
      justify-content: center;
      background-color: #f3f4f6;
    }

    .avatar-xs { width: 24px; height: 24px; min-width: 24px; min-height: 24px; }
    .avatar-sm { width: 32px; height: 32px; min-width: 32px; min-height: 32px; }
    .avatar-md { width: 40px; height: 40px; min-width: 40px; min-height: 40px; }
    .avatar-lg { width: 48px; height: 48px; min-width: 48px; min-height: 48px; }
    .avatar-xl { width: 64px; height: 64px; min-width: 64px; min-height: 64px; }

    .avatar-img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }

    .avatar-fallback {
      width: 100%;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      font-weight: 600;
    }

    .avatar-xs .avatar-fallback { font-size: 10px; }
    .avatar-sm .avatar-fallback { font-size: 12px; }
    .avatar-md .avatar-fallback { font-size: 14px; }
    .avatar-lg .avatar-fallback { font-size: 16px; }
    .avatar-xl .avatar-fallback { font-size: 18px; }
  `]
})
export class UserAvatar {
  private auth = inject(AuthStore);

  @Input() size: 'xs' | 'sm' | 'md' | 'lg' | 'xl' = 'md';
  @Input({ transform: booleanAttribute }) ring = false;
  @Input({ transform: booleanAttribute }) hover = false;
  @Input() customSrc?: string;
  @Input() customAlt?: string;

  imageError = false;

  get sizeClass(): string {
    return `avatar-${this.size}`;
  }

  get imageSrc(): string | null {
    // Use custom src if provided
    if (this.customSrc) return this.customSrc;

    // Otherwise use the user's profile picture from auth context
    const user = this.auth.user();
    return user?.profilePicture || null;
  }

  get altText(): string {
    if (this.customAlt) return this.customAlt;

    const user = this.auth.user();
    return user?.name || 'User avatar';
  }

  get initials(): string {
    const name = this.altText;
    if (!name || name === 'User avatar') return 'U';

    const parts = name.split(' ').filter(p => p.length > 0);
    if (parts.length >= 2) {
      return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
    } else if (parts.length === 1) {
      return parts[0][0].toUpperCase();
    }

    return 'U';
  }

  onImageLoad(): void {
    this.imageError = false;
  }

  onImageError(): void {
    this.imageError = true;
  }
}
