import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import {Post} from './post';


@Component({
  selector: 'app-staff-picks',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="widget staff-picks">
      <h4 class="widget-title">Staff picks</h4>
      <ul class="list-unstyled">
        <li *ngFor="let p of posts" class="pick-item">
          <a [routerLink]="p.link" class="pick-link">{{ p.title }}</a>
          <span class="pick-author">by {{ p.author }}</span>
        </li>
      </ul>
    </div>
  `,
  styles: [`
    .staff-picks { padding: 1rem 0; border-bottom: 1px solid #f0f0f0; }
    .pick-item { margin-bottom: .75rem; }
    .pick-link { display: block; font-weight: 600; color: #000; text-decoration: none; }
    .pick-author { font-size: .8125rem; color: #757575; }
  `]
})
export class StaffToPick {
  @Input({ required: true }) posts!: Pick<Post, 'title' | 'author' | 'link'>[];
}
