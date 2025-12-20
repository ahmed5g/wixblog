import {CommonModule} from '@angular/common';
import {ChangeDetectionStrategy, Component, Input} from '@angular/core';
import {RouterLink} from '@angular/router';
import {Tag} from './tag';


@Component({
  selector: 'app-tag-widget',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="widget">
      <h5 class="widget-title"><span>Tags</span></h5>
      <ul class="list-inline widget-list-inline">
        <li *ngFor="let t of tags" class="list-inline-item">
          <a [routerLink]="t.link">{{ t.label }}</a>
        </li>
      </ul>
    </div>
  `,
})
export class TagWidget {
  @Input({ required: true }) tags!: Tag[];
}
