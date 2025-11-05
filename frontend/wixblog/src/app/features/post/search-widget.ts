import { ChangeDetectionStrategy, Component, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-search-widget',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="widget">
      <h4 class="widget-title"><span>Search</span></h4>
      <form (ngSubmit)="search.emit(term)" class="widget-search">
        <input type="search"
               name="s"
               [(ngModel)]="term"
               placeholder="Type & Hit Enter...">
        <button type="submit"><i class="ti-search"></i></button>
      </form>
    </div>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SearchWidget {
  term = '';
  @Output() search = new EventEmitter<string>();
}
