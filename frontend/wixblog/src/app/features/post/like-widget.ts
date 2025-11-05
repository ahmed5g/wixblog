import { ChangeDetectionStrategy, Component, Input, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { firstValueFrom } from 'rxjs';
import { UserAvatar } from '../user/user-avatar';
import { AuthState } from '../auth/auth-state';
import { LikeControllerService } from '../../shared/services/services/like-controller.service';
import { LikeDto } from '../../shared/services/models/like-dto';
import { UserDto } from '../../shared/services/models/user-dto';

@Component({
  selector: 'app-like-widget',
  standalone: true,
  imports: [CommonModule, UserAvatar],
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
            (click)="toggleClap()"
            [disabled]="!user()">
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
          <div class="avatar-group -space-x-3">
            <div *ngFor="let l of likers() | slice:0:3" class="avatar" [title]="l.userName || ''">
              <app-user-avatar
                [src]="'images/placeholder-avatar.jpg'"
                [alt]="l.userName || '?'"
                size="xs">
              </app-user-avatar>
            </div>
          </div>
        </div>

        <!--  RIGHT : Save, Listen, Share, More  -->
        <div class="flex items-center gap-2">
          <!-- Save (bookmark) -->
          <button class="btn btn-ghost btn-sm" title="Save" (click)="savePost()">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M5 5a2 2 0 012-2h10a2 2 0 012 2v16l-7-3.5L5 21V5z"/>
            </svg>
          </button>

          <!-- Listen (headphone) -->
          <button class="btn btn-ghost btn-sm" title="Listen" (click)="listenPost()">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M9 19V6l12-3v13M9 19c-1.105 0-2-.895-2-2s.895-2 2-2 2 .895 2 2-.895 2-2 2zm12-3c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2z"/>
            </svg>
          </button>

          <!-- Share -->
          <button class="btn btn-ghost btn-sm" title="Share" (click)="sharePost()">
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
              <li><a (click)="copyLink()">Copy link</a></li>
              <li><a (click)="report()">Report</a></li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [``]
})
export class LikeWidget implements OnInit {
  @Input() postId!: number;
  @Input() commentsCount: number = 0;

  private readonly auth = inject(AuthState);
  private readonly likeApi = inject(LikeControllerService);

  user = this.auth.user;
  count = signal<number>(0);
  iLiked = signal<boolean>(false);
  likers = signal<LikeDto[]>([]);

  ngOnInit(): void {
    this.loadRealLikes();
  }

  /* ----------  real data  ------------------------------------- */
  private async loadRealLikes(): Promise<void> {
    try {
      const [cnt, has, users] = await Promise.all([
        firstValueFrom(this.likeApi.getLikeCount({ postId: this.postId })),
        firstValueFrom(this.likeApi.hasLikedPost({ postId: this.postId })),
        firstValueFrom(this.likeApi.getUsersWhoLikedPost({ postId: this.postId }))
      ]);
      this.count.set(cnt);
      this.iLiked.set(has);
      this.likers.set(this.usersToLikes(users, this.postId));
    } catch (e) {
      console.error('Could not load likes', e);
      this.count.set(0);
      this.iLiked.set(false);
      this.likers.set([]);
    }
  }

  /* ----------  toggle clap  ----------------------------------- */
  async toggleClap(): Promise<void> {
    if (!this.user()) return;

    try {
      if (this.iLiked()) {
        await firstValueFrom(this.likeApi.unlikePost({ postId: this.postId }));
        this.count.update(n => n - 1);
        this.iLiked.set(false);
      } else {
        await firstValueFrom(this.likeApi.likePost({ postId: this.postId }));
        this.count.update(n => n + 1);
        this.iLiked.set(true);
      }
      // reload avatars after toggle
      const users = await firstValueFrom(this.likeApi.getUsersWhoLikedPost({ postId: this.postId }));
      this.likers.set(this.usersToLikes(users, this.postId));
    } catch (e) {
      console.error('Clap toggle failed', e);
    }
  }


  //TODO CHECK THIS LOGIC USERDTO AND LIKEDTO
  /* ----------  mapper (delete when backend returns LikeDto[])  */
  private usersToLikes(users: UserDto[], postId: number): LikeDto[] {
    return users.map(u => ({
      id: u.id,
      userId: u.id,
      userName: u.name || '',
      avatarUrl: u.profilePicture,
      postId,
      createdAt: u.createdAt || ''
    }));
  }

  /* ----------  placeholders  ---------------------------------- */
  savePost(): void  { console.log('save post'); }
  listenPost(): void { console.log('listen post'); }
  sharePost(): void { console.log('share post'); }
  copyLink(): void  { navigator.clipboard.writeText(location.href); }
  report(): void    { console.log('report post'); }
}
