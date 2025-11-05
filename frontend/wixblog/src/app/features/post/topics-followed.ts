import { ChangeDetectionStrategy, Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import {Topic} from './Topic';


@Component({
  selector: 'app-topics-followed',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="widget topics-followed">
      <h4 class="widget-title">Topics you follow</h4>
      <div class="topic-chips">
        <a *ngFor="let t of topics" [routerLink]="t.link"
           class="topic-chip"
           [class.followed]="t.followed">
          {{ t.label }}
        </a>
      </div>
      <a routerLink="/topics" class="see-all">More topics →</a>
    </div>
  `,
  styles: [`
    .topics-followed { padding: 1rem 0; border-bottom: 1px solid #f0f0f0; }
    .topic-chips { display: flex; flex-wrap: wrap; gap: .5rem; margin-bottom: .75rem; }
    .topic-chip { padding: .25rem .65rem; border: 1px solid #ccc; border-radius: 999px; font-size: .8125rem; text-decoration: none; color: #000; }
    .followed { background: #000; color: #fff; border-color: #000; }
    .see-all { font-size: .875rem; color: #1a8917; text-decoration: none; }
  `]
})
export class TopicsFollowed {
  @Input({ required: true }) topics!: Topic[];
}
