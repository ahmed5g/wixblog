import {inject, Injectable} from '@angular/core';
import {Api} from '../services/api';
import {BehaviorSubject, map, Observable} from 'rxjs';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Token} from '../models/token';

@Injectable({
  providedIn: 'root'
})
export class Auth {

  private api = inject(Api);

  token:string = "";

  constructor(private http: Api) {}

  get(url: string): any {
    return this.api.get("http://localhost:8080" + url);
  }

  getToken(code: string): Observable<boolean> {
    return this.api.get<Token>(
      `http://localhost:8080/auth/callback?code=${code}`,
      { observe: 'response' }
    ).pipe(
      map((response: HttpResponse<Token>) => {
        if (response.status === 200 && response.body) {
          this.token = response.body.token;
          return true;
        }
        return false;
      })
    );
  }

  private userSource = new BehaviorSubject<any>(null);
  user$ = this.userSource.asObservable();


  loadUser() {
    this.http.get('/api/user').subscribe({
      next: (user) => this.userSource.next(user),
      error: () => this.userSource.next(null)
    });
  }

  logout() {
    window.location.href = '/logout';
  }

}
