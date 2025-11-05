import {Component, Input} from '@angular/core';
import {PostBlock} from '../../../../../core/models/PostBlock';
import {QuillEditorComponent} from 'ngx-quill';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-text-block',
  imports: [
    QuillEditorComponent,
    FormsModule
  ],
  templateUrl: './text-block.html',
  styleUrl: './text-block.scss',
  standalone: true
})
export class TextBlock {

  @Input() block!: PostBlock;
  quillModules = { toolbar: false };

}
