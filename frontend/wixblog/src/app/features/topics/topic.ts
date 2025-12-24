import {Component} from '@angular/core';
import {TopicsSlider} from './topics-slider';
import {NgForOf} from '@angular/common';


interface FollowCard {
  name: string;
  bio: string;
  avatarUrl: string;
  isFollowing: boolean;
}
@Component({
  selector: 'app-topic',
  imports: [
    TopicsSlider,
    NgForOf
  ],
  template: `

    <div class="page-wrapper">
      <app-topics-slider></app-topics-slider>

      <div class="min-h-screen bg-white text-[#242424]">

        <header class="page-header">
          <section class="hero-container">
            <h1 class="page-title">Life</h1>
            <div class="flex items-center justify-center gap-1.5 text-[16px] text-[#6B6B6B] mb-6">
              <span>Topic</span> <span class="font-bold">·</span> <span>8M followers</span> <span
              class="font-bold">·</span> <span>1M stories</span>
            </div>

            <div class="flex justify-center w-full">
              <button class="btn-black">Follow</button>
            </div>

          </section>
        </header>

        <main class="max-w-[1040px] mx-auto px-6 py-10">

          <section class="mb-16">
            <h2 class="section-label  ">Recommended stories</h2>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-12 mb-16">
              <article *ngFor="let post of [1, 2]" class="post-container">

                <img src="https://picsum.photos/seed/mother/600/400" class="post-image-full" alt="Post thumbnail">

                <div class="post-content">
                  <div class="post-author-row">
                    <img src="https://i.pravatar.cc/100?u=sarah" class="author-avatar" alt="avatar"> <span
                    class="author-name">Sarah Jenkins</span>
                  </div>

                  <h3 class="post-title">
                    Growth Quits: Lessons from entering Motherhood
                  </h3>
                  <p class="post-excerpt">
                    Entering the fourth trimester felt like learning a language where the rules change every hour. Here
                    is what I learned about identity.
                  </p>

                  <div class="post-meta-row">
                    <div class="meta-details">
                      <span>Oct 5</span> <span>·</span> <span>8 min read</span> <span
                      class="category-pill">Motherhood</span>
                    </div>

                    <div class="flex items-center gap-3">
                      <svg class="icon-action" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                              d="M5 5a2 2 0 012-2h10a2 2 0 012 2v16l-7-3.5L5 21V5z"/>
                      </svg>
                      <svg class="icon-action" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M5 12h.01M12 12h.01M19 12h.01M6 12a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0z"/>
                      </svg>
                    </div>
                  </div>
                </div>
              </article>
            </div>

            <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8">
              <article *ngFor="let post of gridPosts " class="grid-card">

                <img src="https://picsum.photos/seed/mother/600/400" class="post-image-full" alt="Post thumbnail">

                <div class="post-content">
                  <div class="post-author-row">
                    <img src="https://i.pravatar.cc/100?u=sarah" class="author-avatar" alt="avatar"> <span
                    class="author-name">Sarah Jenkins</span>
                  </div>

                  <h3 class="post-title">
                    Growth Quits: Lessons from entering Motherhood
                  </h3>
                  <p class="post-excerpt">
                    Entering the fourth trimester felt like learning a language where the rules change every hour. Here
                    is what I learned about identity.
                  </p>

                  <div class="post-meta-row">
                    <div class="meta-details">
                      <span>Oct 5</span> <span>·</span> <span>8 min read</span> <span
                      class="category-pill">Motherhood</span>
                    </div>

                    <div class="flex items-center gap-3">
                      <svg class="icon-action" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                              d="M5 5a2 2 0 012-2h10a2 2 0 012 2v16l-7-3.5L5 21V5z"/>
                      </svg>
                      <svg class="icon-action" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M5 12h.01M12 12h.01M19 12h.01M6 12a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0z"/>
                      </svg>
                    </div>
                  </div>
                </div>
              </article>

            </div>

          </section>

          <section class="follow-section">
            <h2 class="section-title">Who to follow</h2>

            <div class="cards-container">
              @for (user of users; track user.name; let i = $index) {
                <div class="follow-card">
                  <div class="card-top-content">
                    <div class="avatar-wrapper">
                      <img [src]="user.avatarUrl"
                           [alt]="user.name"
                           class="avatar-img"
                           [class.rounded-full]="i % 2 === 0"
                           [class.rounded-2xl]="i % 2 !== 0" />
                    </div>

                    <h3 class="user-name">{{ user.name }}</h3>
                    <p class="pub-info">Publication &middot; 148K followers</p>
                    <p class="user-bio">{{ user.bio }}</p>
                  </div>

                  <div class="button-container">
                    <button class="follow-btn">Follow</button>
                  </div>
                </div>
              }
            </div>
          </section>

          <section class="latest-stories-section">
            <div class="label-column">
              <h2 class=" section-label">Latest stories</h2>
            </div>

            <div class="stories-column">

              <article class="card-list-item" *ngFor="let post of listPosts">
                <div class="post-content">
                  <div class="author-row">
                    <img src="https://i.pravatar.cc/100?u=sarah" class="author-avatar" alt="author"> <span
                    class="author-name">Sarah Jenkins</span>
                  </div>

                  <h3 class="post-title">
                    Growth Quits: Lessons from entering Motherhood
                  </h3>
                  <p class="post-excerpt">
                    Entering the fourth trimester felt like learning a language where the rules change every hour. Here
                    is what I learned about identity.
                  </p>

                  <div class="post-footer">
                    <div class="meta-data">
                      <span>Oct 5</span> <span>·</span> <span>8 min read</span> <span
                      class="px-2 py-0.5 bg-gray-100 rounded-full text-[10px]">Motherhood</span>
                    </div>

                    <div class="action-icons">
                      <svg class="icon-btn" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                              d="M5 5a2 2 0 012-2h10a2 2 0 012 2v16l-7-3.5L5 21V5z"/>
                      </svg>
                      <svg class="icon-btn" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M5 12h.01M12 12h.01M19 12h.01M6 12a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0z"/>
                      </svg>
                    </div>
                  </div>
                </div>

                <div class="image-wrapper">
                  <img src="https://picsum.photos/seed/mother/400/300" class="card-thumbnail" alt="Post thumbnail">

                </div>
              </article>

            </div>
          </section>
        </main>
      </div>

    </div>

  `,
  styles: `

    @theme {
      --font-label: "Instrument Sans", "Inter", sans-serif;
      --color-label: #242424;
      --color-excerpt: #6B6B6B;
    }

    @reference "tailwindcss";
    /* Defined Tailwind Components */
    .nav-slider {
      @apply sticky top-0 z-50 bg-white border-b border-gray-100;
    }

    .nav-container {
      @apply max-w-[1040px] mx-auto px-6 overflow-x-auto flex items-center space-x-8 py-3 text-sm font-sans text-gray-500 whitespace-nowrap;
    }

    .page-header {
      @apply border-b border-gray-100 text-center;
    }

    .btn-primary {
      @apply bg-[#242424] text-white px-6 py-1.5 rounded-full font-sans text-sm hover:bg-black transition-all;
    }

    .btn-outline {
      @apply border border-gray-300 px-4 py-1.5 rounded-full font-sans text-sm hover:border-black transition-all;
    }

    .section-label {
      /* 1. Apply base Tailwind styles first */
      @apply text-label border-b border-gray-100 pb-2 mb-8 font-label  font-semibold leading-[-0.016em] font-[24px] tracking-[-0.016em];


      /* 4. Desktop specific overrides to match your 1080px specs */
      @media (min-width: 1080px) {
        font-size: 24px;
        line-height: 30px;

      }
    }

    /* Post Card Components */
    .card-hero {
      @apply cursor-pointer flex flex-col;
    }

    .card-hero-img {
      @apply w-full h-72 object-cover mb-4 rounded-sm transition-opacity group-hover:opacity-95;
    }

    .card-hero-title {
      @apply font-serif text-2xl font-bold mb-2 leading-snug group-hover:underline decoration-1 underline-offset-4;
    }

    .card-grid {
      @apply cursor-pointer;
    }

    .card-grid-img {
      @apply w-full h-44 object-cover mb-4 rounded-sm;
    }

    .card-grid-title {
      @apply font-serif text-lg font-bold leading-tight mb-2 group-hover:text-gray-600;
    }

    .card-list {
      @apply flex flex-col-reverse md:flex-row md:items-start md:justify-between gap-8 pb-10 border-b border-gray-50;
    }

    .card-list-img {
      @apply w-full md:w-40 md:h-28 object-cover rounded-sm;
    }

    .meta-text {
      @apply font-sans text-xs text-gray-400;
    }

    .author-badge {
      @apply flex items-center gap-2 font-sans text-xs mb-3 font-semibold;
    }


    /* 1. Changed to flex-col to force image to the top */
    .post-container {
      @apply flex flex-col gap-4 py-8 border-b border-gray-100 cursor-pointer ;
    }

    .post-content {
      @apply flex flex-col gap-2;
    }

    /* 2. Adjusted image to full width for the top position with a small margin bottom */
    .post-image {
      @apply w-full h-[220px] object-cover rounded-sm mb-2
      transition-opacity group-hover:opacity-95;
    }

    .post-author-row {
      @apply flex items-center gap-2 mb-1;
    }

    .author-avatar {
      @apply w-5 h-5 rounded-full bg-gray-200 object-cover;
    }

    .author-name {
      @apply font-sans text-xs font-semibold text-[#242424] hover:underline;
    }

    .post-title {
      @apply font-serif text-2xl font-bold leading-tight text-[#242424]
      group-hover:text-gray-700 transition-colors;
    }

    .post-excerpt {
      @apply font-serif text-gray-500 line-clamp-2 text-base leading-relaxed;
    }

    .post-meta-row {
      @apply mt-4 flex items-center justify-between font-sans text-xs text-gray-400;
    }

    .meta-details {
      @apply flex items-center gap-2;
    }

    .category-pill {
      @apply px-2 py-0.5 bg-gray-100 rounded-full text-[10px] text-gray-600;
    }

    .icon-action {
      @apply w-5 h-5 text-gray-400 hover:text-black transition-colors;
    }


    /* Container using Flex layout */
    .latest-stories-section {
      @apply flex flex-col md:flex-row gap-8 md:gap-16 border-t border-gray-100 pt-12;
    }

    /* Left Column - Takes up 25% on desktop */
    .label-column {
      @apply md:w-1/4;
    }

    /* Right Column - Takes up 75% on desktop */
    .stories-column {
      @apply md:w-3/4 flex flex-col gap-12;
    }

    /* Adjusting the label to remove the default bottom border if you prefer the vertical separation */
    .section-label-fixed {
      @apply font-sans text-sm  tracking-wider text-[#242424] sticky top-24;
    }

    /* Refined Card List for the new structure */
    .card-list {
      @apply flex items-start justify-between gap-6 pb-12 border-b border-gray-50 last:border-0;
    }

    .post-image {
      @apply w-24 h-24 md:w-32 md:h-32 object-cover rounded-sm shrink-0;
    }

    /* Ensure the article stacks vertically */
    .post-container {
      @apply flex flex-col w-full cursor-pointer;
    }

    /* Make the image span the full container width */
    .post-image-full {
      @apply w-full aspect-[16/9] object-cover rounded-sm mb-4;
      /* aspect-video or a fixed height like h-[240px] also works well */
    }

    /* Ensure content has its own spacing but stays aligned */
    .post-content {
      @apply flex flex-col gap-2 w-full;
    }


    /*latest stories card list item*/

    /* Container for the List Item style (Image on Right) */
    .card-list-item {
      @apply flex items-center justify-between gap-6 py-8 border-b border-gray-100 cursor-pointer ;
    }

    .post-content {
      @apply flex flex-col flex-1 gap-2;
    }

    .author-row {
      @apply flex items-center gap-2 mb-1;
    }

    .author-avatar {
      @apply w-5 h-5 rounded-full object-cover;
    }

    .author-name {
      @apply font-label text-xs font-medium text-label hover:underline;
    }

    .post-title {
      @apply font-label text-xl font-bold leading-tight text-label
      group-hover:text-gray-600 transition-colors;
    }

    .post-excerpt {
      @apply font-label text-[15px] leading-relaxed text-excerpt line-clamp-2;
    }

    .post-footer {
      @apply mt-4 flex items-center justify-between;
    }

    .meta-data {
      @apply flex items-center gap-2 font-label text-xs text-excerpt;
    }

    .action-icons {
      @apply flex items-center gap-4 text-excerpt;
    }

    .icon-btn {
      @apply w-5 h-5 hover:text-label transition-colors;
    }


    /* New: Wrapper to force centering */
    .image-wrapper {
      @apply flex items-center justify-center bg-gray-50 overflow-hidden shrink-0;
      /* Fixed frame dimensions */
      @apply w-28 h-20 md:w-40 md:h-28;
    }

    /* Updated: From object-cover to object-contain/center */
    .card-thumbnail {
      @apply m-auto object-center object-contain transition-transform duration-300;
    }

    /*who to follow*/
    .follow-section {
      @apply py-8 px-4 max-w-6xl mx-auto;
    }
    .shape-circle { @apply rounded-full; }
    .shape-square { @apply rounded-xl; }

    .section-title {
      @apply text-sm font-bold text-gray-900 mb-6;
    }


    .avatar-wrapper {
      @apply w-16 h-16 mb-6; /* Fixed size is required for rounding to work */
    }

    /* The actual shape logic */
    .shape-circle { @apply rounded-full; }
    .shape-square { @apply rounded-xl; }

    .avatar-img {
      @apply w-full h-full object-cover block;
    }
    .cards-container {
      @apply grid grid-cols-1 sm:grid-cols-3 lg:grid-cols-5 gap-4 mb-6;
    }

    .follow-card {
      /* Card with gray border */
      @apply flex flex-col justify-between p-5 border border-gray-200 rounded-sm bg-white;
    }

    .card-top-content {
      @apply flex flex-col items-start;
    }



    .avatar-img {
      @apply w-full h-full rounded-full object-cover grayscale;
    }

    .user-name {
      @apply text-[15px] font-bold text-gray-900 leading-tight mb-1;
    }

    .user-bio {
      @apply text-xs text-gray-500 line-clamp-2 mb-4 leading-snug;
    }

    .button-container {
      /* Centers the button horizontally */
      @apply flex justify-center w-full mt-auto;
    }

    .follow-btn {
      /* White background, black border, light black text */
      @apply px-6 py-1 border border-black bg-white rounded-full
      text-sm font-light text-black transition-all duration-200
      hover:bg-gray-50;
    }

    .see-more-btn {
      @apply text-xs text-green-700 font-medium hover:text-black mt-4;
    }
  `,
})
export class Topic {

  heroPosts=[
    {
      author: 'Zora',
      title: 'Whose wars are we allowed to forget?',
      excerpt: 'The hidden labor of mothers in war zones is often the only thing that keeps humanity alive.',
      meta: 'Oct 15 · 6 min read',
      img: 'https://picsum.photos/seed/p1/800/600',
      avatar: 'https://i.pravatar.cc/100?u=11'
    },
    {
      author: 'The Bold Italic',
      title: 'I finally watched the show my daughter loved',
      excerpt: 'Finding a connection through the screen long after the living room went quiet.',
      meta: 'Oct 12 · 10 min read',
      img: 'https://picsum.photos/seed/p2/800/600',
      avatar: 'https://i.pravatar.cc/100?u=12'
    }
  ];

  gridPosts=[
    {
      author: 'Human Parts',
      title: 'How I stopped fighting for motherhood',
      meta: 'Oct 10 · 5 min read',
      img: 'https://picsum.photos/seed/p3/400/300',
      avatar: 'https://i.pravatar.cc/100?u=13'
    },
    {
      author: 'The Startup',
      title: 'The silent shock of vanishing after birth',
      meta: 'Oct 8 · 7 min read',
      img: 'https://picsum.photos/seed/p4/400/300',
      avatar: 'https://i.pravatar.cc/100?u=14'
    },
    {
      author: 'Gen',
      title: 'Parenting while working is harder now',
      meta: 'Oct 7 · 4 min read',
      img: 'https://picsum.photos/seed/p5/400/300',
      avatar: 'https://i.pravatar.cc/100?u=15'
    }
  ];

  listPosts=[
    {
      author: 'Sarah Jenkins',
      title: 'Growth Quits: Lessons from entering Motherhood',
      excerpt: 'Becoming a mother is as much about losing yourself as it is about finding a child.',
      meta: 'Oct 5 · 8 min read',
      img: 'https://picsum.photos/seed/p6/300/300',
      avatar: 'https://i.pravatar.cc/100?u=16'
    },
    {
      author: 'The Post-Grad Guide',
      title: 'Mama, let’s talk about the 77th Trimester',
      excerpt: 'The recovery phase lasts a lifetime. Here is why we need to stop rushing.',
      meta: 'Oct 3 · 12 min read',
      img: 'https://picsum.photos/seed/p7/300/300',
      avatar: 'https://i.pravatar.cc/100?u=17'
    }
  ];


  users: FollowCard[] = [
    {
      name: 'Erin Kelly',
      bio: 'Author of several books, including "Motherhood: A Memoir"',
      avatarUrl: 'https://api.dicebear.com/7.x/avataaars/svg?seed=Erin',
      isFollowing: false
    },
    {
      name: 'James Finn',
      bio: 'Writer, LGBTQ+ activist, and father. Examining life through...',
      avatarUrl: 'https://api.dicebear.com/7.x/avataaars/svg?seed=James',
      isFollowing: false
    },
    {
      name: 'Catherine Connors',
      bio: 'PhD. Writer, researcher, and mom. Exploring the power of...',
      avatarUrl: 'https://api.dicebear.com/7.x/avataaars/svg?seed=Catherine',
      isFollowing: false
    },
    {
      name: 'Pew Research',
      bio: 'Nonpartisan fact tank informing the public about issues...',
      avatarUrl: 'https://api.dicebear.com/7.x/initials/svg?seed=PR',
      isFollowing: false
    },
    {
      name: 'Gilda Tonge',
      bio: 'Mental health advocate and storyteller sharing parenting...',
      avatarUrl: 'https://api.dicebear.com/7.x/avataaars/svg?seed=Gilda',
      isFollowing: false
    }
  ];
}
