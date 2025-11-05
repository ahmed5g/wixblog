import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

export interface Author {
  name: string;
  bio: string;
  followers: number;
  avatar: string;
  link: string;
}

@Component({
  selector: 'app-who-to-follow',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="widget who-to-follow">
      <h4 class="widget-title">Who to follow</h4>
      <ul class="list-unstyled">
        <li *ngFor="let a of authors" class="follow-item">
          <img [src]="a.avatar" class="avatar" alt="">
          <div class="info">
            <a [routerLink]="a.link" class="name">{{ a.name }}</a>
            <p class="bio">{{ a.bio }}</p>
            <small>{{ a.followers | number }} followers</small>
          </div>
          <button class="btn-follow">Follow</button>
        </li>
      </ul>
      <a routerLink="/recommended" class="see-all">See all recommendations →</a>
    </div>
  `,
  styles: [`
    .who-to-follow { padding: 1rem 0; border-bottom: 1px solid #f0f0f0; }
    .follow-item { display: flex; gap: .75rem; margin-bottom: 1rem; align-items: flex-start; }
    .avatar { width: 52px; height: 52px; border-radius: 50%; object-fit: cover; }
    .info { flex: 1; }
    .name { font-weight: 600; color: #000; text-decoration: none; }
    .bio { font-size: .875rem; color: #666; margin: .125rem 0; }
    .btn-follow { background: #1a8917; color: #fff; border: 0; padding: .35rem .9rem; border-radius: 4px; font-size: .875rem; cursor: pointer; }
    .see-all { font-size: .875rem; color: #1a8917; text-decoration: none; }
  `]
})
export class WhoToFollow {
  @Input({ required: true }) authors!: Author[];
}
