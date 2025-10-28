import { Component, OnInit } from '@angular/core';
import { User } from '../../../core/models/User';
import { AuthService } from '../../../core/auth/auth';
import { UserService } from '../../admin/user-service';
import { Router } from '@angular/router';
import {DatePipe, NgIf} from '@angular/common';

@Component({
  selector: 'app-user-profile',
  imports: [DatePipe, NgIf],
  templateUrl: './user-profile.html',
  styleUrls: ['./user-profile.scss'],
  standalone: true
})
export class UserProfile implements OnInit {
  user: User | null = null;       // Current logged-in user
  allUsers: User[] = [];          // Admin-only
  isAdmin = false;                // Current user is admin
  isLoading = true;               // Loading state
  activeTab: 'about' | 'posts' | 'activity' = 'about';

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // 1️⃣ Try to get current user immediately
    const current = this.authService.getCurrentUser();
    if (current) {
      this.setUser(current);
    }

    // 2️⃣ Subscribe to future updates
    this.authService.currentUserObservable$.subscribe(user => {
      if (user) this.setUser(user);
    });

    // 3️⃣ Trigger server fetch if user is not yet loaded
    if (!current) {
      this.authService.checkAuthStatus();
    }
  }

  private setUser(user: User) {
    this.user = user;
    this.isAdmin = user.role === 'ROLE_ADMIN';
    this.isLoading = false;

    if (this.isAdmin) {
      this.loadAllUsers();
    }
  }

  // Admin: load all users
  loadAllUsers(): void {
    this.userService.getAllUsers().subscribe({
      next: (users: User[]) => this.allUsers = users,
      error: (err) => console.error('Error loading users:', err)
    });
  }

  // Promote a user to admin
  promoteUser(userId: number): void {
    this.userService.promoteToAdmin(userId).subscribe({
      next: () => this.loadAllUsers(),
      error: (err) => console.error('Error promoting user:', err)
    });
  }

  // Demote admin to user
  demoteUser(userId: number): void {
    this.userService.demoteToUser(userId).subscribe({
      next: () => this.loadAllUsers(),
      error: (err) => console.error('Error demoting user:', err)
    });
  }

  // Logout
  logout(): void {
    this.authService.logout();
  }

  // Computed getters for template
  get adminCount(): number {
    return this.allUsers.filter(u => u.role === 'ROLE_ADMIN').length;
  }

  get userCount(): number {
    return this.allUsers.filter(u => u.role === 'ROLE_USER').length;
  }

  get recentUsers(): User[] {
    return this.allUsers.slice(0, 5);
  }

  // Switch tabs
  switchTab(tab: 'about' | 'posts' | 'activity'): void {
    this.activeTab = tab;
  }
}
