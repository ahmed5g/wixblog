import {Component, OnInit} from '@angular/core';
import {NgIf} from '@angular/common';
import {DomSanitizer, SafeUrl} from '@angular/platform-browser';

@Component({
  selector: 'app-home',
  imports: [
    NgIf,
  ],
  templateUrl: './home.html',
  styleUrl: './home.scss',
  standalone: true
})
export class Home implements OnInit{

  user: any;
  userPicture: SafeUrl | null = null;
  userPictureBlocked = false;

  constructor(private sanitizer: DomSanitizer) {}
  ngOnInit(): void {
    this.user = JSON.parse(localStorage.getItem('user')!);

    if (this.user?.picture && !this.userPictureBlocked) {
      this.userPicture = this.sanitizer.bypassSecurityTrustUrl(this.user.picture);
    } else if (this.user?.sub) {
      // generate a consistent avatar from user ID
      this.userPicture = `https://avatars.dicebear.com/api/identicon/${this.user.sub}.svg`;
    } else {
      this.userPicture = 'assets/default-avatar.png';
    }

  }
}
