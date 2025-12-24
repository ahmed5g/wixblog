import {AfterViewInit, Component, ElementRef, HostListener, Input, ViewChild} from '@angular/core';
import {NgForOf, NgIf} from '@angular/common';

@Component({
  selector: 'app-topics-slider',
  imports: [
    NgForOf,
    NgIf
  ],
  template: `
    <div class="slider-container">

      <div *ngIf="canScrollLeft" class="edge-fade-left"></div>
      <button
        *ngIf="canScrollLeft"
        (click)="move('left')"
        class="nav-arrow arrow-left">
        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path d="M15 19l-7-7 7-7" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
      </button>

      <div #slider (scroll)="updateArrows()" class="scroll-area no-scrollbar">

        <button class="pill-explore">
          <svg class="w-5 h-5 fill-white" viewBox="0 0 24 24">
            <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8zm-5.5-2.5l7.5-3L17 7l-7.5 3L6.5 17.5zm5.5-7.42c.51 0 .92.41.92.92 0 .51-.41.92-.92.92-.51 0-.92-.41-.92-.92 0-.51.41-.92.92-.92z"/>
          </svg>
          Explore topics
        </button>

        <a *ngFor="let topic of topics" href="javascript:void(0)" class="pill">
          {{ topic }}
        </a>
      </div>

      <div *ngIf="canScrollRight" class="edge-fade-right"></div>
      <button
        *ngIf="canScrollRight"
        (click)="move('right')"
        class="nav-arrow arrow-right">
        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path d="M9 5l7 7-7 7" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
      </button>
    </div>
  `,
  styles: `
    @reference "tailwindcss";
    .slider-wrapper { @apply flex items-center relative w-full mb-16; }

    /* Fixed scrollbar issue by using standard CSS instead of custom utility */
    .slider-container {
      @apply flex gap-3 overflow-x-auto scroll-smooth py-2 flex-1;
      scrollbar-width: none;
    }
    .slider-container::-webkit-scrollbar { display: none; }
    .scroll-btn-left, .scroll-btn-right { @apply absolute z-10 bg-white border border-neutral-200 shadow-sm w-8 h-8 rounded-full flex items-center justify-center text-xl; }
    .scroll-btn-left { @apply left-[-15px]; }
    .scroll-btn-right { @apply right-[-15px]; }
    .topic-pill { @apply flex-none px-5 py-2 rounded-full bg-[#F2F2F2] text-sm hover:bg-[#E6E6E6] cursor-pointer transition-colors; }
    .main-topic-pill { @apply border border-black font-medium bg-white flex items-center gap-2; }



    /* Functional Styles */
    .no-scrollbar::-webkit-scrollbar { display: none; }
    .no-scrollbar { -ms-overflow-style: none; scrollbar-width: none; }

    /* Tailwind Components */
    .slider-container {
      @apply relative flex items-center w-full bg-white max-w-screen-xl mx-auto;
    }

    .scroll-area {
      @apply flex items-center gap-2 overflow-x-auto scroll-smooth  py-4 px-2;
    }

    /* Navigation Buttons */
    .nav-arrow {
      @apply absolute z-20 w-10 h-10 flex items-center justify-center bg-white
      rounded-full shadow-md text-gray-400 hover:text-black transition-all duration-200;
    }

    .arrow-left { @apply left-0 -translate-x-1/2; }
    .arrow-right { @apply right-0 translate-x-1/2; }

    /* Fading Overlays */
    .edge-fade-left {
      @apply absolute left-0 top-0 bottom-0 w-20 z-10 pointer-events-none
      bg-gradient-to-r from-white to-transparent;
    }

    .edge-fade-right {
      @apply absolute right-0 top-0 bottom-0 w-20 z-10 pointer-events-none
      bg-gradient-to-l from-white to-transparent;
    }

    /* Topic Pills */
    .pill {
      @apply px-4 py-2 bg-[#f2f2f2] rounded-full text-sm font-sans
      text-[#242424] whitespace-nowrap hover:bg-[#e6e6e6] transition-colors no-underline;
    }

    .pill-explore {
      @apply flex items-center gap-2 bg-[#242424] text-white px-5 py-2.5
      rounded-full text-sm font-medium shrink-0 hover:bg-black transition-all;
    }



  `,
})
export class TopicsSlider implements AfterViewInit {

  @Input() sliderScrolled: boolean | undefined;
  @Input() showLeftButton: boolean | undefined;
  // @Input() topTopics: string[] | undefined;

  @ViewChild('slider') slider!: ElementRef<HTMLElement>;

  canScrollLeft = false;
  canScrollRight = true;

  // Exact topics from your last requested set
  topics = [
    'Motherhood', 'Family', 'Parenting', 'Health', 'Relationships', 'Self',
    'Technology', 'Software Engineering', 'Programming', 'JavaScript',
    'Angular', 'Web Development', 'Design', 'UX', 'UI', 'Business',
    'Marketing', 'Startup', 'Money', 'Cryptocurrency', 'Blockchain',
    'Politics', 'Culture', 'Society', 'Science', 'Psychology',
    'Mental Health', 'Productivity', 'Writing', 'Art'
  ];

  ngAfterViewInit() {
    this.updateArrows();
  }

  move(direction: 'left' | 'right') {
    const el = this.slider.nativeElement;
    const moveAmount = 400;
    el.scrollLeft += (direction === 'left' ? -moveAmount : moveAmount);
    // Timeout to ensure arrow state updates after smooth scroll finishes
    setTimeout(() => this.updateArrows(), 400);
  }

  updateArrows() {
    const el = this.slider.nativeElement;
    // Show left arrow if we have scrolled away from the start
    this.canScrollLeft = el.scrollLeft > 20;

    // Show right arrow if there is more content to show
    const maxScroll = el.scrollWidth - el.clientWidth;
    this.canScrollRight = el.scrollLeft < maxScroll - 20;
  }
}
