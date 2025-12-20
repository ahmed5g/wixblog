import {Component, OnInit, OnDestroy, ElementRef, ViewChild} from '@angular/core';
import EditorJS from '@editorjs/editorjs';

import {Subject, timer} from 'rxjs';
import {debounceTime, switchMap, tap} from 'rxjs/operators';
import {FormsModule} from '@angular/forms';
import Header from '@editorjs/header';
import List from '@editorjs/list';
import ImageTool from '@editorjs/image';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-write-post',
  template: `
    <nav class="editor-nav">
      <div class="nav-content">
        <div class="left">
          <img src="assets/logo.png" alt="Medium" class="logo"> <span class="status-text" *ngIf="saveStatus !== 'idle'">
        {{ saveStatus === 'saving' ? 'Saving...' : 'Saved' }}
      </span>
        </div>
        <div class="right">
          <button class="btn-publish">Publish</button>
          <button class="btn-menu">...</button>
        </div>
      </div>
    </nav>

    <main class="editor-container">
      <div class="title-wrapper">
    <textarea
      class="title-input"
      placeholder="Title"
      [(ngModel)]="title"
      (input)="saveSubject.next()"
      rows="1"></textarea>
      </div>

      <div #editorElement id="editorjs"></div>
    </main>

  `
  ,
  imports: [
    FormsModule,
    NgIf
  ],
  styles: `.editor-container {
    max-width: 740px; // Medium's standard width
    margin: 0 auto;
    padding: 50px 20px;
    font-family: 'Charter', 'Georgia', serif; // Professional serif font
  }

  .title-input {
    width: 100%;
    border: none;
    outline: none;
    font-size: 42px;
    font-weight: 700;
    margin-bottom: 20px;
    color: #292929;
    resize: none;

    &::placeholder {
      color: #b3b3b1;
    }
  }

  .editor-nav {
    height: 65px;
    display: flex;
    align-items: center;
    position: sticky;
    top: 0;
    background: white;
    z-index: 10;

    .nav-content {
      width: 100%;
      max-width: 1032px;
      margin: 0 auto;
      display: flex;
      justify-content: space-between;
      padding: 0 20px;
    }
  }

  .btn-publish {
    background: #1a8917;
    color: white;
    border: none;
    padding: 5px 12px;
    border-radius: 99px;
    font-size: 13px;
    cursor: pointer;

    &:hover {
      background: #156d12;
    }
  }

  /* Overriding Editor.js styles to look like Medium */
  ::ng-deep .ce-block__content {
    max-width: 100%; // Ensures content fills the 740px container
  }

  ::ng-deep .ce-toolbar__content {
    max-width: 100%;
  }`
})
export class postUpload implements OnInit, OnDestroy {
  @ViewChild('editorElement', {static: true}) editorElement!: ElementRef;

  editor!: EditorJS;
  title: string='';
  saveStatus: 'saved' | 'saving' | 'idle'='idle';

  // Observable to trigger autosave
  protected saveSubject=new Subject<void>();

  ngOnInit() {
    this.initializeEditor();
    this.setupAutosave();
  }

  initializeEditor() {
    this.editor=new EditorJS({
      holder: this.editorElement.nativeElement,
      placeholder: 'Tell your story...',
      tools: {
        header: {
          class: Header as any,
          shortcut: 'CMD+SHIFT+H',
          inlineToolbar: true
        },
        list: {class: List, inlineToolbar: true},
        image: {
          class: ImageTool,
          config: {
            endpoints: {byFile: 'YOUR_API_UPLOAD_URL'}
          }
        }
      },
      onChange: () => this.saveSubject.next()
    });
  }

  setupAutosave() {
    this.saveSubject.pipe(
      tap(() => this.saveStatus='saving'),
      debounceTime(3000), // Wait for 2.5s of silence
      switchMap(async() => {
        const outputData=await this.editor.save();
        return this.saveToBackend(outputData);
      })
    ).subscribe(() => {
      this.saveStatus='saved';
      setTimeout(() => this.saveStatus='idle', 2000);
    });
  }

  async saveToBackend(blocks: any) {
    // Simulate API Call
    console.log('Saving Post:', {title: this.title, blocks});
    return new Promise(resolve => setTimeout(resolve, 500));
  }

  ngOnDestroy() {
    this.editor?.isReady.then(() => this.editor.destroy());
  }
}
