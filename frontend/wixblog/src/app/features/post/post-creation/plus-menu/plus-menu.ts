import { Component, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-plus-menu',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="flex flex-col gap-2 bg-white shadow rounded-lg p-2">
      <button class="btn btn-sm" (click)="addText.emit()">Text</button>
      <button class="btn btn-sm" (click)="addImage.emit()">Image</button>
      <button class="btn btn-sm" (click)="addVideo.emit()">Video</button>
      <button class="btn btn-sm" (click)="addDivider.emit()">Divider</button>
    </div>
  `
})
export class PlusMenu {
  @Output() addText = new EventEmitter<void>();
  @Output() addImage = new EventEmitter<void>();
  @Output() addVideo = new EventEmitter<void>();
  @Output() addDivider = new EventEmitter<void>();
}
