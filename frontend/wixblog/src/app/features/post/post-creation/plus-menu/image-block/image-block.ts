import {Component, Input} from '@angular/core';
import {PostBlock} from '../../../../../core/models/PostBlock';

@Component({
  selector: 'app-image-block',
  imports: [],
  templateUrl: './image-block.html',
  styleUrl: './image-block.scss',
  standalone: true
})
export class ImageBlock {
  @Input() block!: PostBlock;

}
