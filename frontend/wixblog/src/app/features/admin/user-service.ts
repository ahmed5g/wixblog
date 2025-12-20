// import { Injectable } from '@angular/core';
// import {HttpClient} from '@angular/common/http';
// import {Observable} from 'rxjs';
// import {User} from '../../core/models/User';
//
//
// @Injectable({
//   providedIn: 'root'
// })
// export class UserService {
//   private apiUrl = 'http://localhost:8080';
//
//   constructor(private http: HttpClient) { }
//
//   // Get all users (admin only)
//   getAllUsers(): Observable<User[]> {
//     return this.http.get<User[]>(`${this.apiUrl}/api/admin/users`);
//   }
//
//   // Promote user to admin
//   promoteToAdmin(userId: number): Observable<User> {
//     return this.http.put<User>(`${this.apiUrl}/api/admin/users/${userId}/promote-to-admin`, {});
//   }
//
//   // Demote admin to user
//   demoteToUser(userId: number): Observable<User> {
//     return this.http.put<User>(`${this.apiUrl}/api/admin/users/${userId}/demote-to-user`, {});
//   }
//
//   // Get user profile
//   getUserProfile(): Observable<User> {
//     return this.http.get<User>(`${this.apiUrl}/api/user/profile`);
//   }
//
//
// }
