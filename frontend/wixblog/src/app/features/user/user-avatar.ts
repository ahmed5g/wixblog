import {
  ChangeDetectionStrategy,
  Component,
  Input,
  Output,
  EventEmitter,
  HostBinding,
  booleanAttribute
} from '@angular/core';
import { CommonModule } from '@angular/common';

type Size = 'xs' | 'sm' | 'md' | 'lg' | 'xl';

// Bootstrap size mapping
const SIZE_MAP: Record<Size, string> = {
  xs: 'avatar-xs',  // 24px
  sm: 'avatar-sm',  // 32px
  md: 'avatar-md',  // 40px
  lg: 'avatar-lg',  // 48px
  xl: 'avatar-xl'   // 64px
};

@Component({
  selector: 'app-user-avatar',
  standalone: true,
  imports: [CommonModule],
  template: `
    <figure
      class="avatar-wrapper rounded-circle overflow-hidden position-relative"
      [class.border]="ring"
      [class.border-white]="ring"
      [class.border-2]="ring"
      [class.cursor-pointer]="hover"
      [ngClass]="sizeClass"
    >
      <!-- Skeleton shimmer while loading -->
      <div
        *ngIf="!isLoaded"
        class="avatar-shimmer rounded-circle"
        [ngClass]="sizeClass"
      ></div>

      <img
        [src]="src"
        [alt]="alt"
        (load)="onLoad()"
        (error)="onError()"
        [hidden]="!isLoaded"
        class="avatar-img rounded-circle object-fit-cover w-100 h-100"
        [ngClass]="sizeClass"
      />

      <!-- Fallback initials -->
      <span
        *ngIf="showFallback"
        class="avatar-fallback rounded-circle bg-secondary text-white fw-semibold user-select-none d-flex align-items-center justify-content-center w-100 h-100"
        [ngClass]="sizeClass"
      >
        {{ fallbackText }}
      </span>
    </figure>
  `,
  styles: [`
    .avatar-wrapper {
      display: inline-flex;
      flex-shrink: 0;
    }

    .avatar-xs {
      width: 24px;
      height: 24px;
      font-size: 0.625rem;
    }

    .avatar-sm {
      width: 32px;
      height: 32px;
      font-size: 0.75rem;
    }

    .avatar-md {
      width: 40px;
      height: 40px;
      font-size: 0.875rem;
    }

    .avatar-lg {
      width: 48px;
      height: 48px;
      font-size: 1rem;
    }

    .avatar-xl {
      width: 64px;
      height: 64px;
      font-size: 1.25rem;
    }

    .avatar-shimmer {
      background: linear-gradient(90deg, #f8f9fa 25%, #e9ecef 50%, #f8f9fa 75%);
      background-size: 200% 100%;
      animation: shimmer 1.2s infinite;
    }

    .avatar-img {
      transition: opacity 0.2s ease;
    }

    .avatar-wrapper.cursor-pointer:hover .avatar-img {
      opacity: 0.8;
    }

    @keyframes shimmer {
      0% { background-position: 200% 0; }
      100% { background-position: -200% 0; }
    }
  `],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UserAvatar {
  @Input() src?: string | null;
  @Input() alt?: string = 'User avatar';
  @Input() size: Size = 'md';
  @Input({transform: booleanAttribute}) ring = false;
  @Input({transform: booleanAttribute}) hover = false;

  @Output() loaded = new EventEmitter<boolean>();

  @HostBinding('class') get hostClass() {
    return 'd-inline-flex align-items-center justify-content-center';
  }

  protected isLoaded = false;
  protected showFallback = false;

  get sizeClass(): string {
    return SIZE_MAP[this.size];
  }

  get fallbackText(): string {
    if (!this.alt || this.alt === 'User avatar') return '?';
    const parts = this.alt.trim().split(' ');
    if (parts.length > 1) {
      return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
    }
    return parts[0][0].toUpperCase();
  }

  onLoad(): void {
    this.isLoaded = true;
    this.showFallback = false;
    this.loaded.emit(true);
  }

  onError(): void {
    this.showFallback = true;
    this.isLoaded = true;
    this.loaded.emit(false);
  }
}
