import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterLink} from '@angular/router';
import {Tag} from './tag';

@Component({
  selector: 'app-post-card',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <article class="row mb-5">
      <div class="col-md-4 mb-4 mb-md-0">
        <div class="post-slider slider-sm">
          <img *ngFor="let img of images; trackBy: trackImg"
               loading="lazy"
               [src]="img"
               class="img-fluid"
               alt="post-thumb"
               style="height:200px; object-fit: cover;">
        </div>
      </div>

      <div class="col-md-8">
        <h3 class="h5">
          <a class="post-title" [routerLink]="['/pages/post', slug]">{{ title }}</a>
        </h3>

        <ul class="list-inline post-meta mb-2">
          <li class="list-inline-item">
            <i class="ti-user mr-2"></i>
            <a [routerLink]="authorLink">{{ author }}</a>
          </li>
          <li class="list-inline-item">Date : {{ date | date:'mediumDate' }}</li>
          <li class="list-inline-item">
            Categories : <a [routerLink]="categoryLink" class="ml-1">{{ category }}</a>
          </li>
          <li class="list-inline-item">
            Tags :
            <ng-container *ngFor="let t of tags; trackBy: trackTag; last as isLast">
              <a [routerLink]="t.link">{{ t.label }}</a><span *ngIf="!isLast">,</span>
            </ng-container>
          </li>
        </ul>

        <p>{{ excerpt }}</p>
        <a [routerLink]="['/pages/post', slug]" class="btn btn-outline-primary">Continue Reading</a>
      </div>
    </article>
  `,

})
export class PostCard {
  @Input({ required: true }) id       !: number;
  @Input({ required: true }) title    !: string;
  @Input({ required: true }) excerpt  !: string;
  @Input({ required: true }) author   !: string;
  @Input({ required: true }) authorLink!: string;
  @Input({ required: true }) date     !: string;
  @Input({ required: true }) category !: string;
  @Input({ required: true }) categoryLink!: string;
  @Input({ required: true }) tags     !: Tag[];
  @Input({ required: true }) images   !: string[];
  @Input({ required: true }) slug    !: string;
  @Input({ required: true }) link     !: string;

  trackImg = (_: number, url: string) => url;
  trackTag = (_: number, t: Tag) => t.label;
}
