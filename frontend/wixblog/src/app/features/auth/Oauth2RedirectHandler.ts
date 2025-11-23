import {Component, OnInit} from '@angular/core';

import {ActivatedRoute, Router} from "@angular/router";
import {AuthProvider} from './auth-provider.enum';
import {AuthService} from './auth-service';


@Component({
  selector: 'app-oauth2-redirect-handler',
  standalone: true,
  imports: [],
  template: ``,
  styles: ``
})
export class Oauth2RedirectHandler implements OnInit {

  token!: string;
  error!: string;
  authProvider: AuthProvider = AuthProvider.provider;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,

  ) {
  }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.authProvider = params.get('provider') as AuthProvider;
    })

    this.route.queryParams.subscribe(params => {
      this.token = params['token'];
      this.error = params['error'];

      if (this.token) {
        this.authService.setAuthentication(this.token);
        this.router.navigate(
          ['/pages', 'profile', this.authProvider],
          {state: {from: this.router.routerState.snapshot.url}}
        )
      } else {

        this.router.navigate(
          ['/login'],
          {state: {from: this.router.routerState.snapshot.url, error: this.error}});
      }
    })
  }

}
