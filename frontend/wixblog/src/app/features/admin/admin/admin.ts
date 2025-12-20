// import {Component, OnInit} from '@angular/core';
//
//
//
// import {UserService} from '../user-service';
// import {User} from '../../../core/models/User';
// import {AuthService} from '../../../core/auth/auth';
// import {NgForOf, NgIf} from '@angular/common';
//
// @Component({
//   selector: 'app-admin',
//   imports: [
//     NgIf,
//     NgForOf
//   ],
//   templateUrl: './admin.html',
//   styleUrl: './admin.scss',
//   standalone: true
// })
// export class Admin implements OnInit {
//   users: User[] = [];
//   currentUserId: number | null = null;
//
//   constructor(
//     private userService: UserService,
//     private authService: AuthService
//   ) {}
//
//   ngOnInit(): void {
//     this.loadUsers();
//     this.currentUserId = this.authService.getCurrentUser()?.id || null;
//   }
//
//   loadUsers(): void {
//     this.userService.getAllUsers().subscribe({
//       next: (users) => this.users = users,
//       error: (error) => console.error('Error loading users', error)
//     });
//   }
//
//   promoteToAdmin(userId: number): void {
//     this.userService.promoteToAdmin(userId).subscribe({
//       next: () => this.loadUsers(),
//       error: (error) => console.error('Error promoting user', error)
//     });
//   }
//
//   demoteToUser(userId: number): void {
//     this.userService.demoteToUser(userId).subscribe({
//       next: () => this.loadUsers(),
//       error: (error) => console.error('Error demoting user', error)
//     });
//   }
// }
