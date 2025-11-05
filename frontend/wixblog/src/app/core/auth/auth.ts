import {Injectable} from '@angular/core';
import {BehaviorSubject, firstValueFrom} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {User} from "../models/User";
import {AuthResponse} from '../models/AuthResponse';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080';
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUserObservable$ = this.currentUserSubject.asObservable();
  private authCheckInProgress = false;
  constructor(private http: HttpClient) {
    this.checkAuthStatus();
  }

  private getStoredUser(): User | null {
    if (typeof window !== 'undefined' && window.sessionStorage) {
      const storedUser = sessionStorage.getItem('currentUser');
      return storedUser ? JSON.parse(storedUser) : null;
    }
    return null;
  }

  // Store user in sessionStorage
  private storeUser(user: User | null): void {
    if (typeof window !== 'undefined' && window.sessionStorage) {
      if (user) {
        sessionStorage.setItem('currentUser', JSON.stringify(user));
      } else {
        sessionStorage.removeItem('currentUser');
      }
    }
  }

  checkAuthStatus(): void {
    if (this.authCheckInProgress) return;

    this.authCheckInProgress = true;
    console.log('🔐 Checking server session...');

    this.http.get<AuthResponse>(`${this.apiUrl}/auth/user`, {
      withCredentials: true
    }).subscribe({
      next: (response) => {
        console.log('📡 Server session response:', response);
        if (response.authenticated) {
          const user: User = {
            id: response.id,
            email: response.email,
            name: response.name,
            profilePicture: response.profilePicture,
            role: response.role,
            createdAt: response.createdAt,
            authenticated: true
          };
          this.currentUserSubject.next(user);
          console.log('✅ Session active for:', user.name);
        } else {
          this.currentUserSubject.next(null);
          console.log('❌ No active session');
        }
        this.authCheckInProgress = false;
      },
      error: (error) => {
        console.error('🚨 Session check failed:', error);
        this.currentUserSubject.next(null);
        this.authCheckInProgress = false;
      }
    });
  }



  async isLoggedIn(): Promise<boolean> {
    // If we already have a user, return true immediately
    if (this.currentUserSubject.value?.authenticated) {
      return true;
    }

    // If no user in memory, check with server
    try {
      const response = await firstValueFrom(
        this.http.get<AuthResponse>(`${this.apiUrl}/auth/user`, {
          withCredentials: true
        })
      );

      if (response.authenticated) {
        const user: User = {
          id: response.id,
          email: response.email,
          name: response.name,
          profilePicture: response.profilePicture,
          role: response.role,
          createdAt: response.createdAt,
          authenticated: true
        };
        this.currentUserSubject.next(user);
        return true;
      }
      return false;
    } catch (error) {
      return false;
    }
  }
  // Check if user has specific role
  hasRole(role: string): boolean {
    const user = this.currentUserSubject.value;
    return user?.role === role;
  }

  // Check if user is admin
  isAdmin(): boolean {
    return this.hasRole('ROLE_ADMIN');
  }

  loadCurrentUser(): void {
    this.checkAuthStatus();
  }

  // Initiate Google login
  loginWithGoogle(): void {
    window.location.href = `${this.apiUrl}/oauth2/authorization/google`;
  }

  // Logout
  logout(): void {
    this.http.post(`${this.apiUrl}/logout`, {}, {
      withCredentials: true
    }).subscribe({
      next: () => {
        this.storeUser(null); // Clear sessionStorage
        this.currentUserSubject.next(null);
        window.location.href = "http://localhost:4200/login";
      },
      error: () => {
        this.storeUser(null); // Clear sessionStorage
        this.currentUserSubject.next(null);
        window.location.href = "http://localhost:4200/login";
      }
    });
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  // Refresh user data
  refreshUser(): void {
    this.checkAuthStatus();
  }
}
