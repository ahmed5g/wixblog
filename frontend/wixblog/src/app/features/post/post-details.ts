import {ChangeDetectionStrategy, Component, ElementRef, inject, OnInit, ViewChild} from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { PostControllerService } from '../../shared/services/services/post-controller.service';
import { PostDto } from '../../shared/services/models/post-dto';
import { FormsModule } from '@angular/forms';
import { CommentSection } from '../comment/comment-section';
import { LikeWidget } from './like-widget';

@Component({
  selector: 'app-post-details',
  standalone: true,
  imports: [CommonModule, FormsModule, CommentSection, LikeWidget],
  template: `
    <section class="section">
      <div class="container">
        <article class="row mb-4">
          <div class="col-lg-10 mx-auto mb-4">
            <!-- Loading State -->
            <div *ngIf="isLoading" class="text-center py-4">
              <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Loading...</span>
              </div>
              <p class="mt-2 text-muted">Loading post...</p>
            </div>

            <!-- Error State -->
            <div *ngIf="error && !isLoading" class="alert alert-danger text-center">
              <h4>Post Not Found</h4>
              <p>The post you're looking for doesn't exist or may have been removed.</p>
            </div>

            <!-- Post Content -->
            <div *ngIf="post && !isLoading">
              <h1 class="h2 mb-3">{{ post.title || 'Untitled Post' }}</h1>
              <ul class="list-inline post-meta mb-3">
                <li class="list-inline-item">
                  <i class="ti-user mr-2"></i>
                  {{ post.authorName || 'Unknown Author' }}
                </li>
                <li class="list-inline-item">
                  Date : {{ formatDate(post.createdAt) }}
                </li>
                <li class="list-inline-item" *ngIf="post.timeToRead">
                  Reading Time : {{ post.timeToRead }} min
                </li>
                <li class="list-inline-item" *ngIf="post.viewCount">
                  Views : {{ post.viewCount }}
                </li>
              </ul>

              <!-- Post Status -->
              <div *ngIf="post.status" class="mb-3">
                <span [class]="getStatusBadgeClass(post.status)" class="badge">
                  {{ post.status }}
                </span>
              </div>
            </div>
          </div>

          <!-- Featured Image -->
          <div *ngIf="post && post.featuredImage" class="col-12 mb-3">
            <div class="post-slider">
              <img [src]="post.featuredImage" [alt]="post.title || 'Post image'" class="img-fluid">
            </div>
          </div>

          <!-- Post Summary -->
          <div *ngIf="post && post.summary" class="col-lg-10 mx-auto mb-4">
            <div class="alert alert-info">
              <strong>Summary:</strong> {{ post.summary }}
            </div>
          </div>

          <!-- Main Content -->
          <div class="col-lg-10 mx-auto" *ngIf="post && !isLoading">
            <div class="content" [innerHTML]="safeContent"></div>

            <!-- Post Stats -->
            <div class="row mt-4 pt-4 border-top">
              <div class="col-md-6">
                <div class="d-flex gap-4 text-muted">
                  <div class="text-center">
                    <div class="h5 mb-1 text-primary">{{ post.likeCount || 0 }}</div>
                    <small>Likes</small>
                  </div>
                  <div class="text-center">
                    <div class="h5 mb-1 text-primary">{{ post.commentCount || 0 }}</div>
                    <small>Comments</small>
                  </div>
                  <div class="text-center">
                    <div class="h5 mb-1 text-primary">{{ post.viewCount || 0 }}</div>
                    <small>Views</small>
                  </div>
                </div>
              </div>
            </div>

            <!-- Like Widget -->
            <app-like-widget
              *ngIf="post.id"
              [postId]="post.id"
              [commentsCount]="post.commentCount || 0"
              class="mt-4">
            </app-like-widget>

            <!-- Comments Section -->
            <app-comment-section
              *ngIf="post.id && post.allowComments !== false"
              [postId]="post.id"
              class="mt-5">
            </app-comment-section>

            <!-- Comments Disabled Message -->
            <div *ngIf="post.allowComments === false" class="alert alert-warning mt-4">
              <i class="ti-info-alt me-2"></i>
              Comments are disabled for this post.
            </div>
          </div>
        </article>
      </div>
    </section>
  `,
  styles: [`
    .badge-published {
      background-color: #28a745;
      color: white;
    }

    .badge-draft {
      background-color: #6c757d;
      color: white;
    }

    .badge-archived {
      background-color: #ffc107;
      color: black;
    }

    .badge-deleted {
      background-color: #dc3545;
      color: white;
    }
  `],
})
export class PostDetails implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly postApi = inject(PostControllerService);
  private readonly sanitizer = inject(DomSanitizer);
  @ViewChild('dropdown') dropdown!: ElementRef;

  post: PostDto | null = null;
  isLoading = true;
  error = false;
  safeContent: SafeHtml = '';

  ngOnInit(): void {
    this.loadPost();
  }

  private loadPost(): void {
    const slug = this.route.snapshot.paramMap.get('slug');
    if (!slug) {
      this.isLoading = false;
      this.error = true;
      return;
    }

    this.isLoading = true;
    this.error = false;

    this.postApi.getPostBySlug({ slug })
      .subscribe({
        next: post => {
          this.post = post;
          this.safeContent = this.sanitizer.bypassSecurityTrustHtml(post.content ?? '');
          this.isLoading = false;
        },
        error: err => {
          this.post = null;
          this.isLoading = false;
          this.error = true;
          console.error('Error loading post:', err);
        }
      });
  }

  /* ----  helpers  ---------------------------------------------- */
  formatDate(dateString?: string): string {
    if (!dateString) return 'Unknown date';
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  }

  getStatusBadgeClass(status?: string): string {
    switch (status) {
      case 'PUBLISHED':
        return 'badge-published';
      case 'DRAFT':
        return 'badge-draft';
      case 'ARCHIVED':
        return 'badge-archived';
      case 'DELETED':
        return 'badge-deleted';
      default:
        return 'badge-secondary';
    }
  }

  mapTags(p: PostDto) {
    return (p as any).tags?.map((t: string) => ({ label: t, link: `/tag/${t}` })) ?? [];
  }

  copyLink(): void {
    navigator.clipboard.writeText(location.href);
  }

  report(): void {
    console.log('report post');
  }

  message = '';

  // Auto-resize textarea
  // onTextareaInput(event: Event) {
  //   const textarea = event.target as HTMLTextareaElement;
  //   textarea.style.height = 'auto';
  //   textarea.style.height = textarea.scrollHeight + 'px';
  // }

  // Handle enter key (send on enter, new line on shift+enter)
  onEnterKey(event: KeyboardEvent) {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      this.onSendClick();
    }
  }

  onImageClick() {
    console.log('Image button clicked');
  }

  onEmojiClick() {
    console.log('Emoji button clicked');
  }

  onSendClick() {
    if (this.message.trim()) {
      console.log('Sending message:', this.message);
      this.message = '';

      const textarea = document.querySelector('textarea');
      if (textarea) {
        textarea.style.height = 'auto';
      }
    }
  }
}
