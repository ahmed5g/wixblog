import {
  ChangeDetectionStrategy,
  Component,
  inject,
  signal,
  OnInit,
  DestroyRef
} from '@angular/core';
import {CommonModule} from '@angular/common';
import {takeUntilDestroyed} from '@angular/core/rxjs-interop';
import {PostCard} from './post/post-card';
import {SearchWidget} from './post/search-widget';
import {WhoToFollow} from './post/who-to-follow';
import {StaffToPick} from './post/staff-to-pick';
import {TopicsFollowed} from './post/topics-followed';
import {RecommendedList} from './post/recommended-list.component';
import {PostService} from '../shared/services/services/post.service';

import {PostResponse} from '../shared/services/models/post-response';
import {AuthorDto} from '../shared/services/models/author-dto';
import { AuthStore} from './auth/authStore';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [
    CommonModule,
    PostCard,
    SearchWidget,
    WhoToFollow,
    StaffToPick,
    TopicsFollowed,
    RecommendedList
  ],
  template: `
    <section class="section">
      <div class="container">
        <div class="row">
          <main class="col-lg-8 mb-5 mb-lg-0">


            <!-- REAL posts from API -->
            @for (p of posts(); track trackById($index, p)) {
              <app-post-card
                [id]="p.id!"
                [title]="p.title!"
                [excerpt]="p.summary!"
                [author]="p.author?.name || 'Unknown Author'"
                [authorLink]="'/author/' + (p.author?.id || '')"
                [date]="p.createdAt!"
                [category]="'General'"
                [categoryLink]="'#'"
                [tags]="mapTags(p)"
                [images]="[p.featuredImage || 'assets/images/placeholder.jpg']"
                [link]="'/post/' + p.slug!"
                [slug]="p.slug!"
              />
            }

            <!-- Skeleton while loading -->
            @if (isLoading()) {
              <div class="flex justify-center items-center h-24">
                <span class="loading loading-spinner loading-lg text-primary"></span>
              </div>
            }

            <!-- Empty state -->
            @if (!isLoading() && posts().length === 0) {
              <div class="text-center py-12">
                <p class="text-base-content/60 text-lg">No posts for the moment.</p>
                <p class="text-sm text-base-content/40 mt-2">Check back later or publish your first story!</p>
              </div>
            }
          </main>

          <aside class="col-lg-4">
            <!-- Load search widget immediately -->
            <app-search-widget></app-search-widget>

            <!-- Load other widgets with proper @defer blocks -->
            @defer (on viewport) {
              <app-who-to-follow [authors]="authors()"></app-who-to-follow>
            } @placeholder {
              <div class="card mb-3">
                <div class="card-body">
                  <div class="placeholder-glow">
                    <h6 class="card-title placeholder col-8 mb-2"></h6>
                    <div class="placeholder col-10" style="height: 60px;"></div>
                    <div class="placeholder col-10" style="height: 60px;"></div>
                  </div>
                </div>
              </div>
            }

            @defer (on viewport) {
              <app-staff-picks [posts]="staffPicks()"></app-staff-picks>
            } @placeholder {
              <div class="card mb-3">
                <div class="card-body">
                  <div class="placeholder-glow">
                    <h6 class="card-title placeholder col-6 mb-2"></h6>
                    <div class="placeholder col-12 mb-2" style="height: 80px;"></div>
                  </div>
                </div>
              </div>
            }

            @defer (on viewport) {
              <app-topics-followed [topics]="topics"></app-topics-followed>
            } @placeholder {
              <div class="card mb-3">
                <div class="card-body">
                  <div class="placeholder-glow">
                    <h6 class="card-title placeholder col-7 mb-2"></h6>
                    <div class="placeholder col-12 mb-1" style="height: 30px;"></div>
                    <div class="placeholder col-12 mb-1" style="height: 30px;"></div>
                  </div>
                </div>
              </div>
            }

            @defer (on viewport) {
              <app-recommended-list [posts]="recommended()"></app-recommended-list>
            } @placeholder {
              <div class="card">
                <div class="card-body">
                  <div class="placeholder-glow">
                    <h6 class="card-title placeholder col-9 mb-2"></h6>
                    <div class="placeholder col-12 mb-2" style="height: 70px;"></div>
                  </div>
                </div>
              </div>
            }
          </aside>
        </div>
      </div>
    </section>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class Landing implements OnInit {
  private readonly postApi = inject(PostService);
  private readonly destroyRef = inject(DestroyRef);
  protected readonly auth = inject(AuthStore);

  /* ---- signals ---------------------------------------------- */
  posts = signal<PostResponse[]>([]);
  isLoading = signal<boolean>(true);
  authors = signal<AuthorDto[]>([]);
  staffPicks = signal<any[]>([]);
  recommended = signal<any[]>([]);

  /* ---- static topics (for now) ------------------------------ */
  topics = MOCK_TOPICS;

  ngOnInit(): void {
    this.loadPublished();
  }

  /* ---- fetch published posts -------------------------------- */
  private loadPublished(): void {
    this.isLoading.set(true);

    this.postApi.getPublishedPosts({page: 0, size: 12})
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (page) => {
          this.posts.set(page.content ?? []);

          // Extract unique authors from posts
          const uniqueAuthors = this.extractUniqueAuthors(page.content ?? []);
          this.authors.set(uniqueAuthors);

          // Initialize side widgets
          this.initializeSideWidgets();

          this.isLoading.set(false);
        },
        error: () => {
          this.posts.set([]);
          this.authors.set([]);
          this.isLoading.set(false);
        }
      });
  }

  /* ---- initialize side widgets based on loaded posts -------- */
  private initializeSideWidgets(): void {
    const posts = this.posts();

    // Staff picks: first 3 posts
    this.staffPicks.set(posts.slice(0, 3).map(p => ({
      id: p.id,
      title: p.title!,
      author: p.author?.name || 'Unknown Author',
      authorId: p.author?.id,
      link: '/post/' + p.slug!,
      excerpt: p.summary!,
      images: [p.featuredImage || 'assets/images/placeholder.jpg'],
      readTime: p.readTime,
      likeCount: p.likeCount,
      commentCount: p.commentCount
    })));

    // Recommended: posts 2-5 (or all if less)
    this.recommended.set(posts.slice(2, 5).map(p => ({
      id: p.id,
      title: p.title!,
      link: '/post/' + p.slug!,
      excerpt: p.summary!,
      images: [p.featuredImage || 'assets/images/placeholder.jpg'],
      author: p.author?.name || 'Unknown Author',
      readTime: p.readTime,
      date: p.createdAt
    })));
  }

  /* ---- extract unique authors from posts -------------------- */
  private extractUniqueAuthors(posts: PostResponse[]): AuthorDto[] {
    const authorMap = new Map<number, AuthorDto>();

    posts.forEach(post => {
      if (post.author && post.author.id) {
        if (!authorMap.has(post.author.id)) {
          authorMap.set(post.author.id, post.author);
        }
      }
    });

    return Array.from(authorMap.values()).map(author => ({
      ...author,
      followers: Math.floor(Math.random() * 1000) + 100,
      isFollowing: false
    }));
  }

  /* ---- helpers ---------------------------------------------- */
  trackById = (_: number, p: PostResponse) => p.id;

  mapTags(p: PostResponse) {
    const mockTags = ['Tech', 'Development', 'Programming'];
    return mockTags.map(t => ({label: t, link: `/tag/${t.toLowerCase()}`}));
  }

  onSearch(term: string): void {
    // Implement search functionality here
  }
}

/* ------------------------------------------------------------------ */
/*  static mocks for topics (would come from API in real app)         */
/* ------------------------------------------------------------------ */
const MOCK_TOPICS: any[] = [
  {
    id: 1,
    name: "JavaScript",
    count: 245,
    link: "/tag/javascript",
    description: "The language of the web"
  },
  {
    id: 2,
    name: "TypeScript",
    count: 189,
    link: "/tag/typescript",
    description: "JavaScript with syntax for types"
  },
  {
    id: 3,
    name: "Angular",
    count: 156,
    link: "/tag/angular",
    description: "Platform for building mobile and desktop web applications"
  },
  {
    id: 4,
    name: "React",
    count: 278,
    link: "/tag/react",
    description: "A JavaScript library for building user interfaces"
  },
  {
    id: 5,
    name: "Vue.js",
    count: 134,
    link: "/tag/vuejs",
    description: "The Progressive JavaScript Framework"
  },
  {
    id: 6,
    name: "Node.js",
    count: 201,
    link: "/tag/nodejs",
    description: "JavaScript runtime built on Chrome's V8 JavaScript engine"
  },
  {
    id: 7,
    name: "Python",
    count: 267,
    link: "/tag/python",
    description: "A programming language that lets you work quickly"
  },
  {
    id: 8,
    name: "Java",
    count: 198,
    link: "/tag/java",
    description: "A high-level, class-based, object-oriented programming language"
  },
  {
    id: 9,
    name: "Docker",
    count: 145,
    link: "/tag/docker",
    description: "Empowering App Development for Developers"
  },
  {
    id: 10,
    name: "Kubernetes",
    count: 112,
    link: "/tag/kubernetes",
    description: "Production-Grade Container Orchestration"
  }
];
