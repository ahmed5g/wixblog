import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import {Post} from './post';


@Component({
  selector: 'app-recommended-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="widget recommended-list">
      <h4 class="widget-title">Recommended for you</h4>
      <ul class="list-unstyled">
        <li *ngFor="let p of posts" class="rec-item">
          <img [src]="p.images[0]" class="rec-thumb" alt="">
          <div class="rec-body">
            <a [routerLink]="p.link" class="rec-title">{{ p.title }}</a>
            <p class="rec-excerpt">{{ p.excerpt | slice:0:70 }}…</p>
          </div>
        </li>
      </ul>
    </div>
  `,
  styles: [`
    .recommended-list { padding: 1rem 0; }
    .rec-item { display: flex; gap: .75rem; margin-bottom: 1rem; }
    .rec-thumb { width: 72px; height: 72px; object-fit: cover; border-radius: 4px; }
    .rec-body { flex: 1; }
    .rec-title { font-weight: 600; color: #000; text-decoration: none; }
    .rec-excerpt { font-size: .8125rem; color: #666; margin: .25rem 0 0; }
  `]
})
export class RecommendedList {
  @Input({ required: true }) posts!: Pick<Post, 'title' | 'excerpt' | 'images' | 'link'>[];
}
