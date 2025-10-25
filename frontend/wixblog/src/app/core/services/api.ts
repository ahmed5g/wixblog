import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class Api {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080';

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    let headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    return headers;
  }

  private formatUrl(endpoint: string): string {
    return endpoint.startsWith('http')
      ? endpoint
      : `${this.baseUrl}/${endpoint.replace(/^\/+/, '')}`;
  }

  // -------------------------
  // GET method overloads
  // -------------------------

  // 1️⃣ Observe body (default)
  get<T>(endpoint: string, params?: HttpParams): Observable<T>;

  // 2️⃣ Observe full response
  get<T>(endpoint: string, options: { params?: HttpParams; observe: 'response' }): Observable<HttpResponse<T>>;

  // 3️⃣ Implementation
  get<T>(endpoint: string, options?: any): Observable<T | HttpResponse<T>> {
    if (options?.observe === 'response') {
      return this.http.get<T>(this.formatUrl(endpoint), {
        headers: this.getHeaders(),
        params: options.params,
        observe: 'response'
      });
    } else {
      return this.http.get<T>(this.formatUrl(endpoint), {
        headers: this.getHeaders(),
        params: options
      });
    }
  }

  post<T>(endpoint: string, body: any): Observable<T> {
    return this.http.post<T>(this.formatUrl(endpoint), body, { headers: this.getHeaders() });
  }

  put<T>(endpoint: string, body: any): Observable<T> {
    return this.http.put<T>(this.formatUrl(endpoint), body, { headers: this.getHeaders() });
  }

  patch<T>(endpoint: string, body: any): Observable<T> {
    return this.http.patch<T>(this.formatUrl(endpoint), body, { headers: this.getHeaders() });
  }

  delete<T>(endpoint: string): Observable<T> {
    return this.http.delete<T>(this.formatUrl(endpoint), { headers: this.getHeaders() });
  }
}
