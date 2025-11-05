import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';

import { QuillModule } from 'ngx-quill';
import { FormsModule } from '@angular/forms';

import {PlusMenu} from './plus-menu/plus-menu';
import {TextBlock} from './plus-menu/text-block/text-block';
import {ImageBlock} from './plus-menu/image-block/image-block';
import {DividerBlock} from './plus-menu/divider-block/divider-block';
import {VideoBlock} from './plus-menu/video-block/video-block';
import {PostBlock} from '../../../core/models/PostBlock';
import { v4 as uuidv4 } from 'uuid';


@Component({
  selector: 'app-post-create',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    QuillModule,
    PlusMenu,
    TextBlock,
    ImageBlock,
    DividerBlock,
    VideoBlock
  ],
  templateUrl: './post-creation.html',
  styleUrls: ['./post-creation.scss'],
})
export class PostCreation {
  form: FormGroup;
  blocks: PostBlock[] = [];

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      title: ['']
    });
    // Start with one empty text block
    this.addTextBlock();
  }

  addTextBlock(index?: number) {
    const block: PostBlock = { id: uuidv4(), type: 'text', content: '' };
    if (index != null) this.blocks.splice(index, 0, block);
    else this.blocks.push(block);
  }

  addImageBlock(index?: number) {
    const block: PostBlock = { id: uuidv4(), type: 'image', content: '' };
    if (index != null) this.blocks.splice(index, 0, block);
    else this.blocks.push(block);
  }

  addVideoBlock(index?: number) {
    const block: PostBlock = { id: uuidv4(), type: 'video', content: '' };
    if (index != null) this.blocks.splice(index, 0, block);
    else this.blocks.push(block);
  }

  addDividerBlock(index?: number) {
    const block: PostBlock = { id: uuidv4(), type: 'divider' };
    if (index != null) this.blocks.splice(index, 0, block);
    else this.blocks.push(block);
  }

  removeBlock(index: number) {
    this.blocks.splice(index, 1);
  }

  onPublish() {
    console.log('Post data:', {
      title: this.form.value.title,
      blocks: this.blocks
    });
  }
}
