// import {Component, OnInit} from '@angular/core';
// import {User} from '../../../core/models/User';
// import {AuthService} from '../../../core/auth/auth';
// import {UserService} from '../../admin/user-service';
// import {Router} from '@angular/router';
// import {DatePipe, NgIf} from '@angular/common';
//
// @Component({
//   selector: 'app-user-profile',
//   imports: [DatePipe, NgIf],
//   template: `
//     <div class="min-h-screen bg-base-100 py-10 px-4 flex justify-center">
//       <div class="max-w-5xl w-full space-y-6" *ngIf="!isLoading && user">
//
//         <!-- PROFILE HEADER -->
//         <div class="card bg-base-200 shadow-xl">
//           <div class="card-body flex flex-col md:flex-row items-center gap-6">
//
//             <!-- Avatar -->
//             <div class="avatar">
//               <div class="w-32 rounded-full ring ring-primary ring-offset-base-100 ring-offset-2">
//                 <img [src]="user.profilePicture || 'assets/img/default-avatar.png'" alt="User avatar">
//                 <!-- Changed user?.profilePicture to user.profilePicture -->
//               </div>
//             </div>
//
//             <!-- Info -->
//             <div class="flex-1 text-center md:text-left">
//               <h2 class="text-3xl font-bold text-primary">{{ user.name }}</h2>
//               <!-- Changed user?.name to user.name -->
//               <p class="text-base-content/70 mt-1">{{ 'No bio yet.' }}</p>
//
//               <div class="flex flex-wrap justify-center md:justify-start gap-2 mt-3">
//                 <div class="badge badge-primary badge-outline">Angular</div>
//                 <div class="badge badge-secondary badge-outline">Spring Boot</div>
//                 <div class="badge badge-accent badge-outline">UI/UX</div>
//               </div>
//
//               <div class="flex flex-wrap justify-center md:justify-start gap-3 mt-4 text-sm text-base-content/70">
//             <span class="flex items-center gap-1">
//               <i class="fa fa-envelope"></i> {{ user.email }}
//               <!-- Changed user?.email to user.email -->
//             </span>
//
//                 <span class="flex items-center gap-1">
//               <i class="fa fa-calendar"></i> Joined {{ user.createdAt | date }}
//                   <!-- Changed user?.createdAt to user.createdAt -->
//             </span>
//               </div>
//             </div>
//
//             <!-- Actions -->
//             <div class="flex gap-2 mt-4 md:mt-0">
//               <button class="btn btn-primary btn-sm">
//                 <i class="fa fa-edit mr-1"></i> Edit
//               </button>
//               <button class="btn btn-outline btn-secondary btn-sm" (click)="logout()">
//                 <i class="fa fa-sign-out-alt mr-1"></i> Logout
//               </button>
//             </div>
//           </div>
//         </div>
//
//   `,
//   styles: ``,
//   standalone: true
// })
// export class UserProfile implements OnInit {
//   user: User | null=null;       // Current logged-in user
//   allUsers: User[]=[];          // Admin-only
//   isAdmin=false;                // Current user is admin
//   isLoading=true;               // Loading state
//   activeTab: 'about' | 'posts' | 'activity'='about';
//
//   constructor(
//     private authService: AuthService,
//     private userService: UserService,
//     private router: Router
//   ) {
//   }
//
//   ngOnInit(): void {
//     // 1️⃣ Try to get current user immediately
//     const current=this.authService.getCurrentUser();
//     if (current) {
//       this.setUser(current);
//     }
//
//     // 2️⃣ Subscribe to future updates
//     this.authService.currentUserObservable$.subscribe(user => {
//       if (user) this.setUser(user);
//     });
//
//     // 3️⃣ Trigger server fetch if user is not yet loaded
//     if (!current) {
//       this.authService.checkAuthStatus();
//     }
//   }
//
//   private setUser(user: User) {
//     this.user=user;
//     this.isAdmin=user.role==='ROLE_ADMIN';
//     this.isLoading=false;
//
//     if (this.isAdmin) {
//       this.loadAllUsers();
//     }
//   }
//
//   // Admin: load all users
//   loadAllUsers(): void {
//     this.userService.getAllUsers().subscribe({
//       next: (users: User[]) => this.allUsers=users,
//       error: (err) => console.error('Error loading users:', err)
//     });
//   }
//
//   // Promote a user to admin
//   promoteUser(userId: number): void {
//     this.userService.promoteToAdmin(userId).subscribe({
//       next: () => this.loadAllUsers(),
//       error: (err) => console.error('Error promoting user:', err)
//     });
//   }
//
//   // Demote admin to user
//   demoteUser(userId: number): void {
//     this.userService.demoteToUser(userId).subscribe({
//       next: () => this.loadAllUsers(),
//       error: (err) => console.error('Error demoting user:', err)
//     });
//   }
//
//   // Logout
//   logout(): void {
//     this.authService.logout();
//   }
//
//   // Computed getters for template
//   get adminCount(): number {
//     return this.allUsers.filter(u => u.role==='ROLE_ADMIN').length;
//   }
//
//   get userCount(): number {
//     return this.allUsers.filter(u => u.role==='ROLE_USER').length;
//   }
//
//   get recentUsers(): User[] {
//     return this.allUsers.slice(0, 5);
//   }
//
//   // Switch tabs
//   switchTab(tab: 'about' | 'posts' | 'activity'): void {
//     this.activeTab=tab;
//   }
// }
