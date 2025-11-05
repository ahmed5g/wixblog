import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterLink} from '@angular/router';
import {Post} from './post';

@Component({
  selector: 'app-latest-widget',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="widget">
      <h5 class="widget-title"><span>Latest Article</span></h5>

      <ul class="list-unstyled widget-list">
        <li *ngFor="let p of posts" class="media widget-post align-items-center">
          <a [routerLink]="p.link">
            <img [src]="p.images[0]"
                 class="mr-3"
                 loading="lazy"
                 alt="">
          </a>
          <div class="media-body">
            <h5 class="h6 mb-0">
              <a [routerLink]="p.link">{{ p.title }}</a>
            </h5>
            <small>{{ p.date | date:'mediumDate' }}</small>
          </div>
        </li>
      </ul>
    </div>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class LatestWidget {
  @Input({ required: true }) posts!: Pick<Post, 'title' | 'link' | 'images' | 'date'>[];
}
