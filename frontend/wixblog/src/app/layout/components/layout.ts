import { Component } from '@angular/core';
import {TopBar} from './top-bar';

import {RouterOutlet} from '@angular/router';
import {Footer} from './footer';

@Component({
  selector: 'app-layout',
  imports: [
    TopBar,
    Footer,
    RouterOutlet
  ],
  template: `
    <app-top-bar></app-top-bar>
    <router-outlet></router-outlet>
    <app-footer></app-footer>
  `,
  styles: ``,
  standalone: true
})
export class Layout {

}
