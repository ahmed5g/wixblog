import {Component} from '@angular/core';
import {UserAvatar} from '../../../shared/components/user-avatar/user-avatar';

@Component({
  selector: 'app-header',
  imports: [
    UserAvatar
  ],
  templateUrl: './header.html',
  styleUrl: './header.scss',
  standalone: true
})
export class Header {

}
