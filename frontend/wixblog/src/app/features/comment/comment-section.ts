import { ChangeDetectionStrategy, Component, inject, Input, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserAvatar } from '../../features/user/user-avatar';
import { AuthState } from '../../features/auth/auth-state';
import {comment} from 'postcss';

// Mock Comment Interface
export interface CommentDto {
  id: number;
  content: string;
  authorName: string;
  authorProfilePicture?: string;
  createdAt: Date;
  likeCount: number;
  isLiked: boolean;
  replies?: CommentDto[];
}

@Component({
  selector: 'app-comment-section',
  standalone: true,
  imports: [CommonModule, FormsModule, UserAvatar],
  template: `
    <!-- ======  Comment Section Wrapper  ========================= -->
    <section class="bg-base-200 rounded-box p-6 mt-10">

      <!--  Header  -->
      <h2 class="text-xl font-bold mb-4">{{ totalComments() }} Comments</h2>

      <!--  Add Comment  -->
      <div class="flex gap-3 mb-6">
        <app-user-avatar
          [src]="user()?.profilePicture"
          [alt]="user()?.name"
          size="sm"
          rounded>
        </app-user-avatar>
        <div class="flex-1">
          <textarea
            class="textarea textarea-bordered w-full"
            rows="3"
            placeholder="What are your thoughts?"
            [(ngModel)]="newComment"
            (keydown.enter)="onCommentKeydown($any($event))"
          ></textarea>
          <div class="flex justify-between items-center mt-2">
            <div class="flex gap-2">
              <!-- Emoji Picker Trigger -->
              <button
                class="btn btn-ghost btn-xs btn-circle"
                (click)="toggleEmojiPicker()"
                type="button"
              >
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M14.828 14.828a4 4 0 01-5.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/>
                </svg>
              </button>

              <!-- Image Upload -->
              <button class="btn btn-ghost btn-xs btn-circle" type="button">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z"/>
                </svg>
              </button>
            </div>
            <button
              class="btn btn-primary btn-sm"
              [disabled]="!newComment.trim()"
              (click)="submitComment()"
            >
              Post Comment
            </button>
          </div>

          <!-- Emoji Picker -->
          <div *ngIf="showEmojiPicker" class="mt-2 p-2 bg-base-100 rounded-lg border">
            <div class="flex flex-wrap gap-1">
              <button
                *ngFor="let emoji of popularEmojis"
                class="btn btn-xs btn-ghost"
                (click)="addEmoji(emoji)"
              >
                {{ emoji }}
              </button>
            </div>
          </div>
        </div>
      </div>

      <!--  Comments List  -->
      <div *ngIf="comments().length; else empty" class="space-y-6">
        <article
          *ngFor="let comment of comments(); trackBy: trackById"
          class="flex gap-3"
          [class.border-l-4]="comment.replies?.length"
          [class.border-primary]="comment.replies?.length"
          [class.pl-4]="comment.replies?.length"
        >
          <app-user-avatar
            [src]="comment.authorProfilePicture"
            [alt]="comment.authorName"
            size="xs"
            rounded>
          </app-user-avatar>

          <div class="flex-1">
            <!-- Comment Header -->
            <div class="flex items-center gap-2 mb-1">
              <span class="font-semibold text-sm">{{ comment.authorName }}</span>
              <span class="text-xs text-base-content/60">{{ comment.createdAt | date:'MMM d, y, h:mm a' }}</span>
              <span
                *ngIf="comment.authorName === 'You'"
                class="badge badge-primary badge-xs"
              >
                You
              </span>
            </div>

            <!-- Comment Content -->
            <p class="text-sm text-base-content mb-2 whitespace-pre-line">{{ comment.content }}</p>

            <!-- Comment Actions -->
            <div class="flex items-center gap-4">
              <button
                class="btn btn-ghost btn-xs gap-1"
                [class.text-red-500]="comment.isLiked"
                (click)="toggleLike(comment)"
              >
                <svg
                  class="w-4 h-4"
                  [class.fill-red-500]="comment.isLiked"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    [attr.stroke]="comment.isLiked ? 'none' : 'currentColor'"
                    d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"
                  />
                </svg>
                <span class="text-xs">{{ comment.likeCount }}</span>
              </button>

              <button
                class="btn btn-ghost btn-xs"
                (click)="startReply(comment)"
              >
                Reply
              </button>

              <div class="dropdown dropdown-end" *ngIf="comment.authorName === 'You'">
                <button tabindex="0" class="btn btn-ghost btn-xs btn-circle" #dropdownButton>
                  <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M5 12h.01M12 12h.01M19 12h.01M6 12a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0z"/>
                  </svg>
                </button>
                <ul tabindex="0" class="dropdown-content menu menu-compact bg-base-100 rounded-box w-32 shadow">
                  <li><a (click)="editComment(comment); dropdownButton.blur()">Edit</a></li>
                  <li><a (click)="deleteComment(comment.id); dropdownButton.blur()" class="text-error">Delete</a></li>
                </ul>
              </div>
            </div>

            <!-- Reply Input -->
            <div *ngIf="replyingTo === comment.id" class="mt-3 flex gap-2">
              <app-user-avatar
                [src]="user()?.profilePicture"
                [alt]="user()?.name"
                size="xs"
                rounded>
              </app-user-avatar>
              <div class="flex-1">
                <textarea
                  class="textarea textarea-bordered w-full textarea-sm"
                  rows="2"
                  placeholder="Write a reply..."
                  [(ngModel)]="replyText"
                  (keydown.enter)="onReplyKeydown($any($event), comment)"
                ></textarea>
                <div class="flex justify-end gap-2 mt-1">
                  <button class="btn btn-ghost btn-xs" (click)="cancelReply()">Cancel</button>
                  <button
                    class="btn btn-primary btn-xs"
                    [disabled]="!replyText.trim()"
                    (click)="submitReply(comment)"
                  >
                    Reply
                  </button>
                </div>
              </div>
            </div>

            <!-- Nested Replies -->
            <div *ngIf="comment.replies?.length" class="mt-4 space-y-4 ml-4 border-l-2 border-base-300 pl-4">
              <article
                *ngFor="let reply of comment.replies; trackBy: trackById"
                class="flex gap-3"
              >
                <app-user-avatar
                  [src]="reply.authorProfilePicture"
                  [alt]="reply.authorName"
                  size="xs"
                  rounded>
                </app-user-avatar>
                <div class="flex-1">
                  <div class="flex items-center gap-2 mb-1">
                    <span class="font-semibold text-sm">{{ reply.authorName }}</span>
                    <span class="text-xs text-base-content/60">{{ reply.createdAt | date:'MMM d, h:mm a' }}</span>
                  </div>
                  <p class="text-sm text-base-content">{{ reply.content }}</p>
                  <div class="flex items-center gap-4 mt-1">
                    <button
                      class="btn btn-ghost btn-xs gap-1"
                      [class.text-red-500]="reply.isLiked"
                      (click)="toggleLike(reply)"
                    >
                      <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"/>
                      </svg>
                      <span class="text-xs">{{ reply.likeCount }}</span>
                    </button>
                  </div>
                </div>
              </article>
            </div>
          </div>
        </article>
      </div>

      <!--  Empty state  -->
      <ng-template #empty>
        <div class="text-center text-base-content/60 py-8">
          <svg class="w-12 h-12 mx-auto mb-3 opacity-50" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1"
                  d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"/>
          </svg>
          <p class="text-lg font-medium mb-1">No comments yet</p>
          <p class="text-sm">Be the first to share your thoughts!</p>
        </div>
      </ng-template>
    </section>
  `,
  styles: [`
    :host {
      display: block;
    }
  `]
})
export class CommentSection {
  @Input() postId!: number;

  private readonly auth = inject(AuthState);

  user = this.auth.user;
  comments = signal<CommentDto[]>([]);
  newComment = '';
  replyText = '';
  replyingTo: number | null = null;
  showEmojiPicker = false;
  editingComment: CommentDto | null = null;

  popularEmojis = ['😊', '😂', '❤️', '🔥', '👏', '👍', '🎉', '🙏', '😍', '🤔'];

  // Mock data initialization
  ngOnInit(): void {
    this.loadMockComments();
  }

  private loadMockComments(): void {
    const mockComments: CommentDto[] = [
      {
        id: 1,
        content: 'This is such an insightful post! I really enjoyed reading about these concepts. The examples provided were particularly helpful in understanding the practical applications.',
        authorName: 'Sarah Johnson',
        authorProfilePicture: 'https://images.unsplash.com/photo-1494790108755-2616b612b786?w=64&h=64&fit=crop&crop=face',
        createdAt: new Date(Date.now() - 2 * 60 * 60 * 1000), // 2 hours ago
        likeCount: 12,
        isLiked: false,
        replies: [
          {
            id: 4,
            content: 'I completely agree! The examples made everything click for me too.',
            authorName: 'Mike Chen',
            authorProfilePicture: 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=64&h=64&fit=crop&crop=face',
            createdAt: new Date(Date.now() - 1 * 60 * 60 * 1000), // 1 hour ago
            likeCount: 3,
            isLiked: false
          }
        ]
      },
      {
        id: 2,
        content: 'Great points! However, I have a different perspective on the third section. In my experience, the approach mentioned might not scale well for larger applications.',
        authorName: 'Alex Rodriguez',
        authorProfilePicture: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=64&h=64&fit=crop&crop=face',
        createdAt: new Date(Date.now() - 4 * 60 * 60 * 1000), // 4 hours ago
        likeCount: 8,
        isLiked: true,
        replies: []
      },
      {
        id: 3,
        content: 'Thanks for sharing this! Could you elaborate more on the implementation details? I\'d love to see some code examples.',
        authorName: 'You',
        authorProfilePicture: this.user()?.profilePicture,
        createdAt: new Date(Date.now() - 6 * 60 * 60 * 1000), // 6 hours ago
        likeCount: 5,
        isLiked: false,
        replies: []
      }
    ];

    this.comments.set(mockComments);
  }

  // Computed total comments including replies
  totalComments = () => {
    return this.comments().reduce((total, comment) => {
      return total + 1 + (comment.replies?.length || 0);
    }, 0);
  };

  submitComment(): void {
    if (!this.newComment.trim()) return;

    const newComment: CommentDto = {
      id: Date.now(),
      content: this.newComment.trim(),
      authorName: 'You',
      authorProfilePicture: this.user()?.profilePicture,
      createdAt: new Date(),
      likeCount: 0,
      isLiked: false,
      replies: []
    };

    this.comments.update(comments => [newComment, ...comments]);
    this.newComment = '';
    this.showEmojiPicker = false;
  }

  startReply(comment: CommentDto): void {
    this.replyingTo = comment.id;
    this.replyText = '';
  }

  cancelReply(): void {
    this.replyingTo = null;
    this.replyText = '';
  }

  submitReply(parentComment: CommentDto): void {
    if (!this.replyText.trim()) return;

    const newReply: CommentDto = {
      id: Date.now(),
      content: this.replyText.trim(),
      authorName: 'You',
      authorProfilePicture: this.user()?.profilePicture,
      createdAt: new Date(),
      likeCount: 0,
      isLiked: false
    };

    this.comments.update(comments =>
      comments.map(comment =>
        comment.id === parentComment.id
          ? {
            ...comment,
            replies: [...(comment.replies || []), newReply]
          }
          : comment
      )
    );

    this.cancelReply();
  }

  toggleLike(comment: CommentDto): void {
    this.comments.update(comments =>
      comments.map(c => {
        if (c.id === comment.id) {
          return {
            ...c,
            likeCount: c.isLiked ? c.likeCount - 1 : c.likeCount + 1,
            isLiked: !c.isLiked
          };
        }

        // Also check replies
        if (c.replies) {
          return {
            ...c,
            replies: c.replies.map(reply =>
              reply.id === comment.id
                ? {
                  ...reply,
                  likeCount: reply.isLiked ? reply.likeCount - 1 : reply.likeCount + 1,
                  isLiked: !reply.isLiked
                }
                : reply
            )
          };
        }

        return c;
      })
    );
  }

  editComment(comment: CommentDto): void {
    this.editingComment = comment;
    this.newComment = comment.content;
  }

  deleteComment(commentId: number): void {
    this.comments.update(comments =>
      comments.filter(comment => comment.id !== commentId)
    );
  }

  toggleEmojiPicker(): void {
    this.showEmojiPicker = !this.showEmojiPicker;
  }

  addEmoji(emoji: string): void {
    this.newComment += emoji;
    this.showEmojiPicker = false;
  }

  onCommentKeydown(event: KeyboardEvent): void {
    if (event.key === 'Enter' && event.ctrlKey) {
      event.preventDefault();
      this.submitComment();
    }
  }

  onReplyKeydown(event: KeyboardEvent, comment: CommentDto): void {
    if (event.key === 'Enter' && event.ctrlKey) {
      event.preventDefault();
      this.submitReply(comment);
    }
  }

  trackById = (_: number, item: CommentDto) => item.id;
  protected readonly comment = comment;
}
