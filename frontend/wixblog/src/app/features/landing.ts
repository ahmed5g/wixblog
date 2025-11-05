import { ChangeDetectionStrategy, Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { firstValueFrom } from 'rxjs';
import { PostCard } from './post/post-card';
import { SearchWidget } from './post/search-widget';
import { WhoToFollow } from './post/who-to-follow';
import { StaffToPick } from './post/staff-to-pick';
import { TopicsFollowed } from './post/topics-followed';
import { RecommendedList } from './post/recommended-list.component';
import { PostControllerService } from '../shared/services/services';
import { PostDto } from '../shared/services/models/post-dto';

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
            <!--  REAL posts from API  -->
            <app-post-card
              *ngFor="let p of posts(); trackBy: trackById"
              [id]="p.id!"
              [title]="p.title!"
              [excerpt]="p.summary!"
              [author]="p.authorName!"
              [authorLink]="'/@' + (p.authorName || '')"
              [date]="p.publishedAt!"
              [category]="'General'"
              [categoryLink]="'#'"
              [tags]="mapTags(p)"
              [images]="[p.featuredImage || 'images/placeholder.jpg']"
              [link]="'/post/' + p.slug!"
              [slug]="p.slug!">
            </app-post-card>

            <!--  Skeleton while loading  -->
            <div *ngIf="isLoading()" class="flex justify-center items-center h-24">
              <span class="loading loading-spinner loading-lg text-primary"></span>
            </div>

            <!--  Empty state  -->
            <div *ngIf="!isLoading() && posts().length === 0" class="text-center py-12">
              <p class="text-base-content/60 text-lg">No posts for the moment.</p>
              <p class="text-sm text-base-content/40 mt-2">Check back later or publish your first story!</p>
            </div>
          </main>

          <aside class="col-lg-4">
            <app-search-widget (search)="onSearch($event)"></app-search-widget>
            <app-who-to-follow [authors]="authors"></app-who-to-follow>
            <app-staff-picks [posts]="staffPicks"></app-staff-picks>
            <app-topics-followed [topics]="topics"></app-topics-followed>
            <app-recommended-list [posts]="recommended"></app-recommended-list>
          </aside>
        </div>
      </div>
    </section>
  `,
})
export class Landing implements OnInit {
  private readonly postApi = inject(PostControllerService);

  /* ----  signals  ---------------------------------------------- */
  posts = signal<PostDto[]>([]);
  isLoading = signal<boolean>(true);

  /* ----  mock side-widgets (still static)  -------------------- */
  /* ----  mock side-widgets (still static)  -------------------- */
  authors = MOCK_AUTHORS;
  staffPicks = this.posts().slice(0, 3).map(p => ({
    title: p.title!,
    author: p.authorName!,
    link: '/post/' + p.slug!,
    images: [p.featuredImage || 'images/placeholder.jpg']
  }));
  recommended = this.posts().slice(2, 5).map(p => ({
    title: p.title!,
    link: '/post/' + p.slug!,
    excerpt: p.summary!,
    images: [p.featuredImage || 'images/placeholder.jpg']
  }));
  topics = MOCK_TOPICS;

  ngOnInit(): void {
    this.loadPublished();
  }

  /* ----  fetch published posts  -------------------------------- */
  private async loadPublished(): Promise<void> {
    this.isLoading.set(true);
    try {
      const page = await firstValueFrom(this.postApi.getPublishedPosts({ page: 0, size: 12 }));
      this.posts.set(page.content ?? []);
    } catch (e) {
      console.error('Could not load posts', e);
      this.posts.set([]);
    } finally {
      this.isLoading.set(false);
    }
  }

  /* ----  helpers  ---------------------------------------------- */
  trackById = (_: number, p: PostDto) => p.id;

  mapTags(p: PostDto) {
    /*  if your back-end returns tags as string[]  */
    return (p as any).tags?.map((t: string) => ({ label: t, link: `/tag/${t}` })) ?? [];
  }

  onSearch(term: string): void {
    console.log('search:', term);
  }
}

/* ------------------------------------------------------------------ */
/*  temporary mocks – delete when widgets are wired to real endpoints  */
/* ------------------------------------------------------------------ */
const MOCK_POSTS: PostDto[] = [
  {
    id: 1,
    title: "Getting Started with Angular 18",
    summary: "Learn the new features and improvements in Angular 18 and how to upgrade your applications.",
    authorName: "Sarah Johnson",
    authorProfilePicture: "https://images.unsplash.com/photo-1627398242454-45a1465c2479?ixlib=rb-4.0.3&w=600",
    publishedAt: "2024-03-15T10:00:00Z",
    featuredImage: "https://images.unsplash.com/photo-1627398242454-45a1465c2479?ixlib=rb-4.0.3&w=600",
    slug: "getting-started-with-angular-18",
    timeToRead: 8,
    likeCount: 42,
    commentCount: 12
  },
  {
    id: 2,
    title: "Mastering Spring Boot 3.2",
    summary: "Deep dive into the latest Spring Boot features including virtual threads and improved observability.",
    authorName: "Mike Chen",
    authorProfilePicture: "https://images.unsplash.com/photo-1555066931-4365d14bab8c?ixlib=rb-4.0.3&w=600",
    publishedAt: "2024-03-12T14:30:00Z",
    featuredImage: "https://images.unsplash.com/photo-1555066931-4365d14bab8c?ixlib=rb-4.0.3&w=600",
    slug: "mastering-spring-boot-3-2",
    timeToRead: 12,
    likeCount: 67,
    commentCount: 8
  },
  {
    id: 3,
    title: "The Future of AI in Web Development",
    summary: "Exploring how artificial intelligence is transforming the way we build and maintain web applications.",
    authorName: "Alex Rivera",
    authorProfilePicture: "https://images.unsplash.com/photo-1485827404703-89b55fcc595e?ixlib=rb-4.0.3&w=600",
    publishedAt: "2024-03-10T09:15:00Z",
    featuredImage: "https://images.unsplash.com/photo-1485827404703-89b55fcc595e?ixlib=rb-4.0.3&w=600",
    slug: "future-of-ai-web-development",
    timeToRead: 6,
    likeCount: 89,
    commentCount: 15
  },
  {
    id: 4,
    title: "Building Scalable Microservices with Docker",
    summary: "A comprehensive guide to designing and deploying microservices architecture using Docker and Kubernetes.",
    authorName: "Priya Patel",
    authorProfilePicture: "https://images.unsplash.com/photo-1626716630596-ca2f3c1354f4?ixlib=rb-4.0.3&w=600",
    publishedAt: "2024-03-08T16:45:00Z",
    featuredImage: "https://images.unsplash.com/photo-1626716630596-ca2f3c1354f4?ixlib=rb-4.0.3&w=600",
    slug: "scalable-microservices-docker",
    timeToRead: 15,
    likeCount: 54,
    commentCount: 9
  },
  {
    id: 5,
    title: "CSS Grid vs Flexbox: When to Use What",
    summary: "Understanding the differences between CSS Grid and Flexbox with practical examples and use cases.",
    authorName: "David Kim",
    authorProfilePicture: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?ixlib=rb-4.0.3&w=600",
    publishedAt: "2024-03-05T11:20:00Z",
    featuredImage: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?ixlib=rb-4.0.3&w=600",
    slug: "css-grid-vs-flexbox",
    timeToRead: 7,
    likeCount: 38,
    commentCount: 5
  },
  {
    id: 6,
    title: "TypeScript Best Practices for Large Applications",
    summary: "Essential TypeScript patterns and practices for maintaining large-scale enterprise applications.",
    authorName: "Emma Wilson",
    authorProfilePicture: "https://images.unsplash.com/photo-1516116216624-53a697fbe396?ixlib=rb-4.0.3&w=600",
    publishedAt: "2024-03-01T13:10:00Z",
    featuredImage: "https://images.unsplash.com/photo-1516116216624-53a697fbe396?ixlib=rb-4.0.3&w=600",
    slug: "typescript-best-practices",
    timeToRead: 10,
    likeCount: 73,
    commentCount: 11
  }
];

const MOCK_AUTHORS: any[] = [
  {
    id: 1,
    name: "Sarah Johnson",
    username: "@sarahjohnson",
    avatar: "https://images.unsplash.com/photo-1494790108755-2616b612b786?ixlib=rb-4.0.3&w=100&h=100&fit=crop&crop=face ",
    bio: "Senior Frontend Developer at TechCorp. Passionate about Angular and UX design.",
    followers: 1247,
    isFollowing: false
  },
  {
    id: 2,
    name: "Mike Chen",
    username: "@mikechen",
    avatar: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?ixlib=rb-4.0.3&w=100&h=100&fit=crop&crop=face ",
    bio: "Java Architect and Spring Framework enthusiast. Open source contributor.",
    followers: 892,
    isFollowing: true
  },
  {
    id: 3,
    name: "Alex Rivera",
    username: "@alexrivera",
    avatar: "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-4.0.3&w=100&h=100&fit=crop&crop=face ",
    bio: "AI Researcher and Full Stack Developer. Exploring the intersection of AI and web technologies.",
    followers: 2156,
    isFollowing: false
  },
  {
    id: 4,
    name: "Priya Patel",
    username: "@priyapatel",
    avatar: "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-4.0.3&w=100&h=100&fit=crop&crop=face ",
    bio: "DevOps Engineer and Cloud Architect. Kubernetes and Docker expert.",
    followers: 1678,
    isFollowing: true
  },
  {
    id: 5,
    name: "David Kim",
    username: "@davidkim",
    avatar: "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?ixlib=rb-4.0.3&w=100&h=100&fit=crop&crop=face ",
    bio: "UI/UX Designer and CSS wizard. Creating beautiful and accessible web experiences.",
    followers: 943,
    isFollowing: false
  }
];

const MOCK_TOPICS: any[] = [
  {
    name: "JavaScript",
    count: 245,
    link: "/tag/javascript",
    description: "The language of the web"
  },
  {
    name: "TypeScript",
    count: 189,
    link: "/tag/typescript",
    description: "JavaScript with syntax for types"
  },
  {
    name: "Angular",
    count: 156,
    link: "/tag/angular",
    description: "Platform for building mobile and desktop web applications"
  },
  {
    name: "React",
    count: 278,
    link: "/tag/react",
    description: "A JavaScript library for building user interfaces"
  },
  {
    name: "Vue.js",
    count: 134,
    link: "/tag/vuejs",
    description: "The Progressive JavaScript Framework"
  },
  {
    name: "Node.js",
    count: 201,
    link: "/tag/nodejs",
    description: "JavaScript runtime built on Chrome's V8 JavaScript engine"
  },
  {
    name: "Python",
    count: 267,
    link: "/tag/python",
    description: "A programming language that lets you work quickly"
  },
  {
    name: "Java",
    count: 198,
    link: "/tag/java",
    description: "A high-level, class-based, object-oriented programming language"
  },
  {
    name: "Docker",
    count: 145,
    link: "/tag/docker",
    description: "Empowering App Development for Developers"
  },
  {
    name: "Kubernetes",
    count: 112,
    link: "/tag/kubernetes",
    description: "Production-Grade Container Orchestration"
  }
];
