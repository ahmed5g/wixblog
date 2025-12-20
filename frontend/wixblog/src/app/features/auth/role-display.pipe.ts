// role-display.pipe.ts
import { Pipe, PipeTransform } from '@angular/core';
import {UserRole} from '../../shared/services/models/userRole';



@Pipe({
  name: 'roleDisplay',
  standalone: true
})
export class RoleDisplayPipe implements PipeTransform {
  transform(role: UserRole | null | undefined): string {
    if (!role) return '';

    const roleMap: Record<UserRole, string> = {
      'ROLE_ADMIN': 'Administrator',
      'ROLE_USER': 'User',
      'ROLE_GUEST': 'Guest'
    };

    return roleMap[role] || role;
  }
}
