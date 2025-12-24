import { Component } from '@angular/core';

@Component({
  selector: 'app-footer',
  imports: [],
  template: `
    <footer class="section-sm pb-0 border-top border-default">

      <div class="flex flex-wrap justify-center gap-x-6 gap-y-2">
        <a href="#" class="footer-link">Help</a>
        <a href="#" class="footer-link">Status</a>
        <a href="#" class="footer-link">About</a>
        <a href="#" class="footer-link">Careers</a>
        <a href="#" class="footer-link">Press</a>
        <a href="#" class="footer-link">Blog</a>
        <a href="#" class="footer-link">Privacy</a>
        <a href="#" class="footer-link">Rules</a>
        <a href="#" class="footer-link">Terms</a>
        <a href="#" class="footer-link">Text to speech</a>
      </div>
    </footer>

  `,
  styles: ``,
  standalone: true
})
export class Footer {

}
