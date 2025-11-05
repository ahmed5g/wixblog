import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-not-found-minimal',
  imports: [CommonModule, RouterLink],
  template: `
    <section class="min-vh-100 d-flex align-items-center justify-content-center bg-light">
      <div class="container">
        <div class="row justify-content-center">
          <div class="col-12 col-md-8 col-lg-6 text-center">
            <!-- 404 Illustration - Centered -->
            <div class="mb-4 d-flex justify-content-center">
              <img src="/images/undraw_taken_mshk.svg"
                   alt="404 Not Found"
                   class="img-fluid mx-auto"
                   style="max-height: 200px; display: block;">
            </div>

            <!-- 404 Content -->
            <div class="mb-4">
              <h1 class="display-1 text-muted mb-2">404</h1>
              <h2 class="h3 text-dark mb-3">Page Not Found</h2>
              <p class="lead text-muted mx-auto" style="max-width: 500px;">
                Sorry, we couldn't find the page you're looking for. The page might have been moved, deleted, or you entered the wrong URL.
              </p>
            </div>

            <!-- Action Buttons -->
            <div class="d-flex flex-column flex-sm-row justify-content-center align-items-center gap-3 mt-4">
              <a routerLink="/" class="btn btn-primary btn-lg px-4">
                <i class="ti-home mr-2"></i>Go Home
              </a>
              <button (click)="goBack()" class="btn btn-outline-primary btn-lg px-4">
                <i class="ti-arrow-left mr-2"></i>Go Back
              </button>
            </div>
          </div>
        </div>
      </div>
    </section>
  `,
  styles: [`
    .min-vh-100 {
      min-height: 100vh;
    }
    .display-1 {
      font-size: 5rem;
      font-weight: 300;
      line-height: 1;
    }

    @media (max-width: 768px) {
      .display-1 {
        font-size: 3.5rem;
      }

      .btn-lg {
        padding: 0.75rem 1.5rem;
        font-size: 1rem;
      }
    }

    .bg-light {
      background-color: #f8f9fa !important;
    }
  `],
  standalone: true
})
export class NotFound {
  goBack() {
    window.history.back();
  }
}
