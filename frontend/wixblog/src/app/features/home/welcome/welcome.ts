import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

declare const google: any;

@Component({
  selector: 'app-welcome',
  standalone: true,
  templateUrl: './welcome.html',
  styleUrls: ['./welcome.scss']
})
export class Welcome implements OnInit {
  constructor(private router: Router) {}

  ngOnInit(): void {
    google.accounts.id.initialize({
      client_id: '992551284845-2l5v80jaf5om158g9aqauubg7l001h39.apps.googleusercontent.com',
      callback: (response: any) => this.handleLogin(response)
    });

    google.accounts.id.renderButton(
      document.getElementById("googleBtn"),
      { theme: "outline", size: "large" }
    );
  }

  handleLogin(response: any) {
    console.log("Login response:", response);
    const token = response.credential;

    fetch('http://localhost:8080/api/auth/google', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ token })
    })
      .then(res => res.json())
      .then(data => {
        console.log("Backend returned:", data);
        localStorage.setItem('user', JSON.stringify(data));
        this.router.navigate(['/home']);
      });
  }

}
