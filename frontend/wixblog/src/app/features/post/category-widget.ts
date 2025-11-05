import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterLink} from '@angular/router';

export interface CategoryCount {
  name : string;
  link : string;
  count: number;
}

@Component({
  selector: 'app-category-widget',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="widget">
      <h5 class="widget-title"><span>Categories</span></h5>
      <ul class="list-unstyled widget-list">
        <li *ngFor="let c of categories">
          <a [routerLink]="c.link" class="d-flex">
            {{ c.name }} <small class="ml-auto">({{ c.count }})</small>
          </a>
        </li>
      </ul>
    </div>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CategoryWidgetComponent {
  @Input({ required: true }) categories!: CategoryCount[];
}
