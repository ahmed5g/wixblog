import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../../core/auth/auth';
import {DatePipe, NgForOf, NgIf} from '@angular/common';
import {User} from '../../../core/models/User';
import {Router, RouterLink} from '@angular/router';
import {UserDTO} from '../../../core/models/UserDTO';
import {UserService} from '../user-service';


@Component({
  selector: 'app-dashboard',
  imports: [
    NgIf,
    DatePipe,
    RouterLink,
    NgForOf
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
  standalone: true
})
export class DashboardComponent implements OnInit {
  user: User | null = null;
  isAdmin = false;

  allUsers: UserDTO[] = [];

  constructor(private authService: AuthService,
              private router :Router,
              private userService: UserService) {
  }

  isLoading = true;
  ngOnInit(): void {
    // Listen for user updates
    this.authService.currentUserObservable$.subscribe(user => {
      this.user = user;
      this.isAdmin = user?.role === 'ROLE_ADMIN';
      this.isLoading = false;

      // If user is admin, load all users
      if (this.isAdmin) {
        this.loadAllUsers();
      }
    });

    // Check session on dashboard load
    this.authService.checkAuthStatus();
  }

  // Load all users (admin only)
  loadAllUsers(): void {
    this.userService.getAllUsers().subscribe({
      next: (users) => {
        this.allUsers = users;
        console.log('Loaded users:', users);
      },
      error: (error) => {
        console.error('Error loading users:', error);
      }
    });
  }

  // ✅ ADD THESE COMPUTED PROPERTIES FOR TEMPLATE
  get adminCount(): number {
    return this.allUsers.filter(user => user.role === 'ROLE_ADMIN').length;
  }

  get userCount(): number {
    return this.allUsers.filter(user => user.role === 'ROLE_USER').length;
  }

  get recentUsers(): UserDTO[] {
    return this.allUsers.slice(0, 5);
  }

  // Promote user to admin
  promoteUser(userId: number): void {
    this.userService.promoteToAdmin(userId).subscribe({
      next: (updatedUser) => {
        console.log('User promoted:', updatedUser);
        this.loadAllUsers(); // Refresh the list
        // Update current user if it's the same user
        if (this.user?.id === userId) {
          this.authService.checkAuthStatus(); // Refresh current user data
        }
      },
      error: (error) => {
        console.error('Error promoting user:', error);
      }
    });
  }

  // Demote admin to user
  demoteUser(userId: number): void {
    this.userService.demoteToUser(userId).subscribe({
      next: (updatedUser) => {
        console.log('User demoted:', updatedUser);
        this.loadAllUsers(); // Refresh the list
        // Update current user if it's the same user
        if (this.user?.id === userId) {
          this.authService.checkAuthStatus(); // Refresh current user data
        }
      },
      error: (error) => {
        console.error('Error demoting user:', error);
      }
    });
  }



  logout(): void {
    this.authService.logout();
  }

}
