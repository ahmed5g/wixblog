import {Component, OnInit} from '@angular/core';
import {Auth} from '../../../core/auth/auth';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';



@Component({
  selector: 'app-login-form',
  imports: [],
  templateUrl: './login-form.html',
  styleUrl: './login-form.scss',
  standalone: true
})
export class LoginForm implements OnInit {


  constructor(private http: Auth, private router: Router) {}

  url: string = "";



  ngOnInit(): void {
    this.http.get("/auth/url").subscribe((data: any) => this.url = data.authURL);
  }
}
