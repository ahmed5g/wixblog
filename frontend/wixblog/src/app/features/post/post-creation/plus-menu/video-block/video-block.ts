import {Component, Input} from '@angular/core';
import {PostBlock} from '../../../../../core/models/PostBlock';

@Component({
  selector: 'app-video-block',
  imports: [],
  templateUrl: './video-block.html',
  styleUrl: './video-block.scss',
  standalone: true
})
export class VideoBlock {
  @Input() block!: PostBlock;

}
