import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { UserAvatar } from './user-avatar';
import { AuthState } from '../auth/auth-state';

type Tab = 'stories' | 'lists' | 'about';

@Component({
  selector: 'app-user-account',
  standalone: true,
  imports: [CommonModule, FormsModule, UserAvatar],
  template: `
    <section class="bg-base-100 min-h-screen">

      <!-- ======  Cover  =========================================== -->
      <div class="relative h-64 md:h-80 bg-gradient-to-br from-base-300 to-base-200">
        <button class="btn btn-sm btn-circle btn-ghost absolute top-4 right-4" aria-label="Edit cover">
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z"/>
          </svg>
        </button>
      </div>

      <!-- ======  Header (avatar + name + more actions)  ============ -->
      <div class="max-w-7xl mx-auto px-4 -mt-24 relative z-10">
        <div class="flex flex-col md:flex-row items-start gap-6">

          <!--  avatar  -->
          <div class="avatar">
            <div class="w-40 mask mask-squircle ring ring-base-100 ring-offset-4">
              <app-user-avatar [src]="user()?.profilePicture" [alt]="user()?.name" size="xl"></app-user-avatar>
            </div>
            <button class="btn btn-circle btn-primary btn-sm absolute bottom-0 right-0" aria-label="Edit avatar">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                      d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z"/>
              </svg>
            </button>
          </div>

          <!--  name + handle + follow  -->
          <div class="flex-1">
            <h1 class="text-4xl font-bold text-base-content">{{ user()?.name ?? '—' }}</h1>
            <p class="text-sm text-base-content/70">@{{ user()?.email?.split('@')?.[0] ?? 'user' }}</p>

            <!--  Follow bar  -->
            <div class="flex items-center gap-4 mt-3">
              <button class="btn btn-primary btn-sm">Follow</button>
              <div class="text-sm text-base-content/70 flex gap-3">
                <span><strong>{{ stories.length }}</strong> Stories</span>
                <span><strong>1.2k</strong> Followers</span>
                <span><strong>42</strong> Following</span>
              </div>
            </div>

            <!--  Bio  -->
            <div class="mt-3 text-base text-base-content/80 max-w-2xl">
              <p *ngIf="!editingBio()" (click)="startEdit()" class="cursor-pointer hover:bg-base-200 rounded px-2 py-1">
                {{ bio() || 'Click to add a short bio…' }}
              </p>
              <textarea *ngIf="editingBio()"
                        class="textarea textarea-bordered w-full"
                        rows="2"
                        [(ngModel)]="bio"
                        (blur)="saveBio()"
                        #bioInput
                        autofocus>
          </textarea>
            </div>
          </div>

          <!--  More actions  -->
          <div class="dropdown dropdown-end">
            <button tabindex="0" class="btn btn-ghost btn-circle">
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                      d="M5 12h.01M12 12h.01M19 12h.01M6 12a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0z"/>
              </svg>
            </button>
            <ul tabindex="0" class="menu menu-compact dropdown-content bg-base-100 rounded-box w-52 shadow">
              <li><a (click)="editBio()">Edit bio</a></li>
              <li><a (click)="settings()">Settings</a></li>
              <li><a (click)="share()">Share profile</a></li>
            </ul>
          </div>
        </div>
      </div>

      <!-- ======  Two-column layout  ================================= -->
      <div class="max-w-7xl mx-auto px-4 mt-8">
        <div class="grid grid-cols-1 lg:grid-cols-12 gap-8">

          <!--  Left sidebar (sticky on desktop)  -->
          <aside class="lg:col-span-3 lg:sticky lg:top-24 self-start">
            <nav class="menu bg-base-100 rounded-box shadow-sm p-4">
              <li class="menu-title">
                <span>Quick links</span>
              </li>
              <li><a routerLink="/">Home</a></li>
              <li><a (click)="active.set('about')">About</a></li>
              <li><a (click)="active.set('lists')">Lists</a></li>
              <li><a (click)="active.set('stories')">Stories</a></li>
            </nav>

            <div class="mt-4 text-sm text-base-content/70">
              <p>Member since {{ user()?.createdAt | date:'longDate' }}</p>
            </div>
          </aside>

          <!--  Right content  -->
          <main class="lg:col-span-9">
            <!--  Tabs  -->
            <div class="border-b border-base-300">
              <div class="flex gap-8">
                <button
                  *ngFor="let t of tabs"
                  class="pb-3 text-sm font-medium border-b-2 transition-colors"
                  [class.text-base-content]="active() === t.id"
                  [class.border-primary]="active() === t.id"
                  [class.text-base-content60]="active() !== t.id"
                [class.border-transparent]="active() !== t.id"
                (click)="active.set(t.id)">
                {{ t.label }}
                </button>
              </div>
            </div>

            <!--  Tab Content  -->
            <div class="mt-8">
              <!--  Stories  -->
              <div *ngIf="active() === 'stories'" class="grid gap-8 md:grid-cols-2 lg:grid-cols-3">
                <article *ngFor="let s of stories" class="card card-compact bg-base-100 shadow-sm hover:shadow-md transition-shadow">
                  <figure>
                    <img [src]="s.image" [alt]="s.title" class="w-full h-48 object-cover"/>
                  </figure>
                  <div class="card-body">
                    <h2 class="card-title text-lg">{{ s.title }}</h2>
                    <p class="text-sm text-base-content/70">{{ s.excerpt }}</p>
                    <div class="card-actions justify-between items-center mt-2">
                      <span class="text-xs text-base-content/60">{{ s.date }}</span>
                      <button class="btn btn-primary btn-sm">Read</button>
                    </div>
                  </div>
                </article>
              </div>

              <!--  Lists  -->
              <div *ngIf="active() === 'lists'">
                <p class="text-sm text-base-content/70">No lists yet.</p>
              </div>

              <!--  About  -->
              <div *ngIf="active() === 'about'">
                <p class="text-sm text-base-content/70">You haven’t written an about page yet.</p>
              </div>
            </div>
          </main>
        </div>
      </div>
    </section>
  `,
  styles: [``]
})
export class UserAccount {

  editingBio = signal(false);

  startEdit(): void {
    this.editingBio.set(true);
  }

  saveBio(): void {
    this.editingBio.set(false);
    console.log('saved bio:', this.bio());
  }
  private readonly auth = inject(AuthState);
  readonly user = this.auth.user;

  bio = signal(this.user()?.bio ?? '');

  readonly tabs = [
    { id: 'stories', label: 'Stories' },
    { id: 'lists',   label: 'Lists' },
    { id: 'about',   label: 'About' }
  ] as const;
  readonly active = signal<'stories' | 'lists' | 'about'>('stories');

  readonly stories = [
    { title: 'My first story', excerpt: 'How it all began…', image: 'images/post/post-1.jpg', date: 'Mar 15' },
    { title: 'Angular signals', excerpt: 'Deep dive into reactivity', image: 'images/post/post-2.jpg', date: 'Mar 14' },
    { title: 'Design systems', excerpt: 'Building consistent UIs', image: 'images/post/post-3.jpg', date: 'Mar 13' }
  ];

  /* ----  placeholder actions  ---------------------------------- */
  editBio(): void { console.log('edit bio'); }
  settings(): void { console.log('settings'); }
  share(): void { console.log('share'); }
}
