import {Component, ElementRef, ViewChild, AfterViewInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {TopicsSlider} from './topics-slider';

interface Tag {
  name: string;
  link: string;
}

interface SubCategory {
  id: string;
  name: string;
  tags: Tag[];
}

interface MainCategory {
  title: string;
  subCategories: SubCategory[];
}

@Component({
  selector: 'app-medium-explore-topics',
  standalone: true,
  imports: [CommonModule, TopicsSlider],
  template: `
    <div class="page-wrapper">

      <app-topics-slider></app-topics-slider>

      <section class="hero-container">
        <h1 class="page-title">Explore Topics</h1>
        <div class="search-box-wrapper">
          <input type="text" class="search-input-field" placeholder="Search topics…"/>
        </div>
        <div class="recommended-row">
          <span class="recommended-label">Recommended:</span> <a href="#" class="recommended-badge"
                                                                 *ngFor="let item of recommended">{{ item }}</a>
        </div>
      </section>

      <div class="main-grid">
        <div *ngFor="let mainCat of categories" class="main-category-block">
          <h2 class="main-cat-title">{{ mainCat.title }}</h2>

          <div class="sub-categories-container">
            <div *ngFor="let sub of mainCat.subCategories" class="sub-cat-group">
              <h3 class="sub-cat-name">{{ sub.name }}</h3>

              <ul class="tag-list">
                <li *ngFor="let tag of getDisplayTags(sub)" class="tag-item">
                  <a *ngIf="tag.name" [href]="tag.link" class="tag-link">{{ tag.name }}</a> <span *ngIf="!tag.name"
                                                                                                  class="tag-placeholder">&nbsp;</span>
                </li>

                <div class="more-container">
                  <button *ngIf="sub.tags.length > 6"
                          (click)="toggleExpand(sub.id)"
                          class="more-btn">
                    {{ isExpanded(sub.id) ? 'Less' : 'More' }}
                  </button>
                  <div *ngIf="sub.tags.length <= 6" class="more-placeholder"></div>
                </div>
              </ul>
            </div>
          </div>

          <button class="show-all-btn">
            Show all in "{{ mainCat.title }}"
          </button>
        </div>
      </div>
    </div>
  `,
  styles: [`
    @reference "tailwindcss";


    .search-input-field {
      @apply w-full max-w-[700px] h-[56px] px-6 text-lg rounded-xl bg-[#F2F2F2] border-none outline-none focus:bg-white focus:ring-1 focus:ring-black;
    }

    .recommended-row {
      @apply flex flex-wrap justify-center items-center gap-3 text-[14px] mt-6;
    }

    .recommended-label {
      @apply text-[#6B6B6B];
    }

    .recommended-badge {
      @apply no-underline text-[#242424] bg-[#F7F7F7] px-3 py-1 rounded-full font-medium hover:underline;
    }

    .main-grid {
      @apply grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-x-12 gap-y-16 mt-12;
    }

    /* Update these specific classes in your styles */

    .main-category-block {
      /* Added border-b and padding-bottom for the separation line */
      @apply flex flex-col pt-6 pb-10 border-b border-gray-100;
    }

    .show-all-btn {
      /* Adjusted margin to sit nicely above the new line */
      @apply mt-6 text-[14px] text-[#6B6B6B] hover:text-[#242424]
      text-left bg-transparent border-none p-0 cursor-pointer transition-colors;
    }

    /* Optional: If you want to remove the line from the very last rows on large screens */
    @media (min-width: 1024px) {
      .main-grid > .main-category-block:nth-last-child(-n+3) {
        @apply border-b-0;
      }
    }

    .main-cat-title {
      @apply text-[22px] font-bold mb-8 text-[#242424] tracking-tight;
    }

    .sub-cat-name {
      @apply text-[13px] font-bold mb-4 text-[#242424] uppercase tracking-wide;
    }

    .tag-list {
      @apply flex flex-col gap-y-2;
    }

    .tag-item {
      @apply h-[28px] flex items-center;
    }

    .tag-link {
      @apply text-[15px] text-[#6B6B6B] no-underline hover:text-[#242424];
    }

    .more-container {
      @apply mt-2;
    }

    .more-btn {
      @apply text-[14px]  bg-transparent border-none p-0 cursor-pointer hover:underline;
    }

    .more-placeholder {
      @apply h-[21px];
    }

    .show-all-btn {
      @apply mt-6 text-[14px] text-[#1A8917]  hover:text-[#242424] text-left bg-transparent border-none p-0 cursor-pointer;
    }


    /* Expanded Search Box Wrapper */
    .search-box-wrapper {
      /* mx-auto is the safety net for centering block elements */
      @apply w-full max-w-[100%] mb-8 relative mx-auto;
    }

    .search-input-field {
      @apply w-full h-[72px] pl-14 pr-8 text-xl rounded-2xl bg-[#F2F2F2] border-none
      outline-none transition-all duration-200 text-left
      focus:bg-white focus:ring-1 focus:ring-black focus:shadow-sm;

      /* The icon logic from before */
      background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='none' viewBox='0 0 24 24' stroke='%236B6B6B' stroke-width='2'%3E%3Cpath stroke-linecap='round' stroke-linejoin='round' d='M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z' /%3E%3C/svg%3E");
      background-repeat: no-repeat;
      background-position: 20px center;
      background-size: 24px;
    }

    /* Recommended Section */
    .recommended-row {
      @apply flex flex-wrap justify-center items-center gap-4 text-[15px] mt-2 w-full mx-auto;
    }
  `]
})
export class TopicsExplorer {

  expandedSubCategories=new Set<string>();


  recommended=['Visual Design', 'UX Design', 'Product Management'];

  categories: MainCategory[]=[
    {
      title: 'Life',
      subCategories: [
        {
          id: 'l1',
          name: 'Family',
          tags: [{name: 'Adoption', link: '#'}, {name: 'Children', link: '#'}, {
            name: 'Elder Care',
            link: '#'
          }, {name: 'Fatherhood', link: '#'}, {name: 'Motherhood', link: '#'}, {
            name: 'Parenting',
            link: '#'
          }, {name: 'Education', link: '#'}]
        },
        {
          id: 'l2',
          name: 'Health',
          tags: [{name: 'Aging', link: '#'}, {name: 'Fitness', link: '#'}, {
            name: 'Nutrition',
            link: '#'
          }, {name: 'Sleep', link: '#'}]
        }
      ]
    },
    {
      title: 'Technology',
      subCategories: [
        {
          id: 't1',
          name: 'Artificial Intelligence',
          tags: [{name: 'ChatGPT', link: '#'}, {name: 'Deep Learning', link: '#'}, {
            name: 'Machine Learning',
            link: '#'
          }, {name: 'NLP', link: '#'}, {name: 'Robotics', link: '#'}, {
            name: 'Computer Vision',
            link: '#'
          }, {name: 'Generative AI', link: '#'}]
        },
        {
          id: 't2',
          name: 'Software Development',
          tags: [{name: 'Coding', link: '#'}, {name: 'DevOps', link: '#'}, {name: 'Architecture', link: '#'}]
        }
      ]
    },
    {
      title: 'Work',
      subCategories: [
        {
          id: 'w1',
          name: 'Business',
          tags: [{name: 'Startups', link: '#'}, {name: 'Venture Capital', link: '#'}, {
            name: 'Management',
            link: '#'
          }, {name: 'Entrepreneurship', link: '#'}]
        },
        {
          id: 'w2',
          name: 'Freelancing',
          tags: [{name: 'Gig Economy', link: '#'}, {name: 'Remote Work', link: '#'}, {name: 'Upwork', link: '#'}]
        }
      ]
    },
    {
      title: 'Culture',
      subCategories: [
        {
          id: 'c1',
          name: 'Philosophy',
          tags: [{name: 'Ethics', link: '#'}, {name: 'Logic', link: '#'}, {
            name: 'Stoicism',
            link: '#'
          }, {name: 'Existentialism', link: '#'}, {name: 'Metaphysics', link: '#'}, {
            name: 'Epistemology',
            link: '#'
          }, {name: 'Aesthetics', link: '#'}]
        },
        {
          id: 'c2',
          name: 'Art',
          tags: [{name: 'Painting', link: '#'}, {name: 'Sculpture', link: '#'}, {name: 'Digital Art', link: '#'}]
        }
      ]
    },
    {
      title: 'Self Improvement',
      subCategories: [
        {
          id: 's1',
          name: 'Productivity',
          tags: [{name: 'Focus', link: '#'}, {name: 'Time Management', link: '#'}, {
            name: 'Habits',
            link: '#'
          }, {name: 'Flow State', link: '#'}, {name: 'Journaling', link: '#'}, {
            name: 'Deep Work',
            link: '#'
          }, {name: 'Goals', link: '#'}]
        }
      ]
    },
    {
      title: 'Science',
      subCategories: [
        {
          id: 'sc1',
          name: 'Space',
          tags: [{name: 'NASA', link: '#'}, {name: 'SpaceX', link: '#'}, {
            name: 'Astronomy',
            link: '#'
          }, {name: 'Planets', link: '#'}, {name: 'Galaxy', link: '#'}, {
            name: 'Black Holes',
            link: '#'
          }, {name: 'Physics', link: '#'}]
        }
      ]
    },
    {
      title: 'Programming',
      subCategories: [
        {
          id: 'p1',
          name: 'Languages',
          tags: [{name: 'TypeScript', link: '#'}, {name: 'Rust', link: '#'}, {name: 'Python', link: '#'}, {
            name: 'Go',
            link: '#'
          }, {name: 'JavaScript', link: '#'}, {name: 'Java', link: '#'}, {name: 'C++', link: '#'}]
        }
      ]
    },
    {
      title: 'Marketing',
      subCategories: [
        {
          id: 'm1',
          name: 'Strategy',
          tags: [{name: 'SEO', link: '#'}, {name: 'Branding', link: '#'}, {
            name: 'Social Media',
            link: '#'
          }, {name: 'Copywriting', link: '#'}, {name: 'Advertising', link: '#'}, {
            name: 'Funnel',
            link: '#'
          }, {name: 'Growth', link: '#'}]
        }
      ]
    },
    {
      title: 'Society',
      subCategories: [
        {
          id: 'so1',
          name: 'Politics',
          tags: [{name: 'Justice', link: '#'}, {name: 'Law', link: '#'}, {
            name: 'Policy',
            link: '#'
          }, {name: 'Democracy', link: '#'}, {name: 'Equality', link: '#'}, {
            name: 'Economics',
            link: '#'
          }, {name: 'History', link: '#'}]
        }
      ]
    },
    {
      title: 'Design',
      subCategories: [
        {
          id: 'd1',
          name: 'Visual',
          tags: [{name: 'Typography', link: '#'}, {name: 'Color Theory', link: '#'}, {
            name: 'Layout',
            link: '#'
          }, {name: 'Figma', link: '#'}, {name: 'Logo', link: '#'}, {
            name: 'UI Design',
            link: '#'
          }, {name: 'UX Research', link: '#'}]
        }
      ]
    }
  ];


  toggleExpand(id: string) {
    if (this.expandedSubCategories.has(id)) {
      this.expandedSubCategories.delete(id);
    } else {
      this.expandedSubCategories.add(id);
    }
  }

  isExpanded(id: string): boolean {
    return this.expandedSubCategories.has(id);
  }

  getDisplayTags(sub: SubCategory): Tag[] {
    if (this.isExpanded(sub.id)) return sub.tags;
    const limit=6;
    const displayList=[...sub.tags].slice(0, limit);
    while (displayList.length < limit) {
      displayList.push({name: '', link: ''});
    }
    return displayList;
  }


}
