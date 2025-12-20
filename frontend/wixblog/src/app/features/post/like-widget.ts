import { Component, Input, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthStore} from '../auth/authStore';


@Component({
  selector: 'app-like-widget',
  standalone: true,
  imports: [CommonModule],
  template: `
    <!--  Full-width bar under title  -->
    <div class="w-full px-4 py-3 border-top border-bottom border-b border-base-300">
      <div class="max-w-4xl mx-auto flex items-center justify-between">

        <!--  LEFT : Clap & Comments  -->
        <div class="flex items-center gap-2">
          <!-- Clap / Applause -->
          <button
            class="btn btn-ghost btn-sm gap-1"
            [class.btn-primary]="iLiked()"
            [disabled]="!authContext.isAuthenticated()"
            (click)="toggleClap()">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M7 11.5V14m0-2.5v-6a1.5 1.5 0 113 0m-3 6a1.5 1.5 0 00-3 0v2a7.5 7.5 0 0015 0v-5a1.5 1.5 0 00-3 0m-6-3V11m0-5.5v-1a1.5 1.5 0 013 0v1m0 0V11m0-5.5a1.5 1.5 0 013 0v3m0 0V11m0-5.5a1.5 1.5 0 013 0v1m-6.5 9.5a1.5 1.5 0 00-3 0v2a7.5 7.5 0 0015 0v-5a1.5 1.5 0 00-3 0"/>
            </svg>
            {{ count() }}
          </button>

          <!-- Comments -->
          <button class="btn btn-ghost btn-sm gap-1">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"/>
            </svg>
            {{ commentsCount }}
          </button>

          <!--  Avatars of first 3 likers  -->
          @if (likers().length > 0) {
            <div class="avatar-group -space-x-3">
              @for (l of likers() | slice:0:3; track l.id) {
                <div class="avatar" [title]="l.userName || ''">
                  <div class="w-8 h-8 rounded-full border-2 border-base-100">
                    @if (l.avatarUrl) {
                      <img [src]="l.avatarUrl" [alt]="l.userName || '?'" class="w-full h-full rounded-full" />
                    } @else {
                      <div class="w-full h-full rounded-full bg-base-300 flex items-center justify-center text-xs">
                        {{ (l.userName || '?')[0] }}
                      </div>
                    }
                  </div>
                </div>
              }
            </div>
          }
        </div>

        <!--  RIGHT : Save, Listen, Share, More  -->
        <div class="flex items-center gap-2">
          <!-- Save (bookmark) -->
          <button
            class="btn btn-ghost btn-sm"
            title="Save"

            [disabled]="!authContext.isAuthenticated()">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M5 5a2 2 0 012-2h10a2 2 0 012 2v16l-7-3.5L5 21V5z"/>
            </svg>
          </button>

          <!-- Listen (headphone) -->
          <button class="btn btn-ghost btn-sm" title="Listen" >
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M9 19V6l12-3v13M9 19c-1.105 0-2-.895-2-2s.895-2 2-2 2 .895 2 2-.895 2-2 2zm12-3c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2z"/>
            </svg>
          </button>

          <!-- Share -->
          <button class="btn btn-ghost btn-sm" title="Share" >
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M8.684 13.342C8.886 12.938 9 12.482 9 12s-.114-.938-.316-1.342m0 2.684a3 3 0 110-2.684m0 2.684l6.632 3.316m-6.632-6.316l6.632-3.316m0 0a3 3 0 105.367-2.684 3 3 0 00-5.367 2.684zm0 9.368a3 3 0 105.367 2.684 3 3 0 00-5.367-2.684z"/>
            </svg>
          </button>

          <!-- More (three dots) -->
          <div class="dropdown dropdown-end">
            <button tabindex="0" class="btn btn-ghost btn-sm btn-circle">
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                      d="M5 12h.01M12 12h.01M19 12h.01M6 12a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0z"/>
              </svg>
            </button>
            <ul tabindex="0" class="menu menu-compact dropdown-content bg-base-100 rounded-box w-52 shadow">
<!--              <li><a (click)="copyLink()">Copy link</a></li>-->
<!--              @if (authContext.isAuthenticated()) {-->
<!--                <li><a (click)="report()">Report</a></li>-->
<!--              }-->
            </ul>
          </div>
        </div>
      </div>
    </div>
  `,
})
export class LikeWidget implements OnInit {
  @Input() postId!: number;
  @Input() commentsCount: number = 0;

  // Inject AuthContext
  protected readonly authContext = inject(AuthStore);

  // Signals
  count = signal<number>(0);
  iLiked = signal<boolean>(false);
  likers = signal<LikeDto[]>([]);

  // Mock data interface
  private mockLikes: LikeDto[] = [
    { id: 1, userId: 101, userName: 'Sarah Johnson', avatarUrl: 'https://images.unsplash.com/photo-1494790108755-2616b612b786?w=64&h=64&fit=crop&crop=face', postId: 0, createdAt: '2024-01-15' },
    { id: 2, userId: 102, userName: 'Mike Chen', avatarUrl: 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=64&h=64&fit=crop&crop=face', postId: 0, createdAt: '2024-01-14' },
    { id: 3, userId: 103, userName: 'Alex Rivera', avatarUrl: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=64&h=64&fit=crop&crop=face', postId: 0, createdAt: '2024-01-13' },
    { id: 4, userId: 104, userName: 'Priya Patel', avatarUrl: 'https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=64&h=64&fit=crop&crop=face', postId: 0, createdAt: '2024-01-12' },
  ];

  ngOnInit(): void {
    // Initialize with mock data for now
    this.initializeMockData();
  }

  private initializeMockData(): void {
    // Set initial count (would come from API)
    this.count.set(42);

    // Check if current user has liked this post (mock logic)
    const currentUserId = this.authContext.user()?.id;
    // const hasLiked = this.mockLikes.some(like => like.userId === currentUserId);
    // this.iLiked.set(hasLiked);

    // Set likers
    this.likers.set(this.mockLikes);
  }

  /* ----------  toggle clap  ----------------------------------- */
  async toggleClap(): Promise<void> {
    if (!this.authContext.isAuthenticated()) {
      // Optionally redirect to login or show login prompt
      console.log('Please log in to like posts');
      return;
    }

    try {
      if (this.iLiked()) {
        // Unlike the post
        // await firstValueFrom(this.likeApi.unlikePost({ postId: this.postId }));
        this.count.update(n => Math.max(0, n - 1));
        this.iLiked.set(false);

        // Remove current user from likers
        const currentUserId = this.authContext.user()?.id;
        // this.likers.update(likers =>
        //   likers.filter(like => like.userId !== currentUserId)
        // );
      } else {
        // Like the post
        // await firstValueFrom(this.likeApi.likePost({ postId: this.postId }));
        this.count.update(n => n + 1);
        this.iLiked.set(true);

        // Add current user to likers
        const currentUser = this.authContext.user();
        // if (currentUser) {
        //   const newLike: LikeDto = {
        //     id: Date.now(),
        //     userId: currentUser.id!,
        //     userName: currentUser.name || 'You',
        //     avatarUrl: currentUser.profilePicture,
        //     postId: this.postId,
        //     createdAt: new Date().toISOString()
        //   };
        //   this.likers.update(likers => [newLike, ...likers]);
        // }
      }
    } catch (e) {
      console.error('Clap toggle failed', e);
    }
  }

  // /* ----------  placeholders  ---------------------------------- */
  // savePost(): void  {
  //   if (!this.authContext.isAuthenticated()) {
  //     console.log('Please log in to save posts');
  //     return;
  //   }
  //   console.log('save post');
  // }
  //
  // listenPost(): void {
  //   console.log('listen post');
  // }
  //
  // sharePost(): void {
  //   console.log('share post');
  // }
  //
  // copyLink(): void  {
  //   navigator.clipboard.writeText(location.href);
  //   console.log('Link copied to clipboard');
  // }
  //
  // report(): void {
  //   if (!this.authContext.isAuthenticated()) {
  //     console.log('Please log in to report posts');
  //     return;
  //   }
  //   console.log('report post');
  // }
}

// Interface for Like Data Transfer Object
interface LikeDto {
  id: number;
  userId: number;
  userName: string;
  avatarUrl?: string;
  postId: number;
  createdAt: string;
}
