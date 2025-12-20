// role.guard.ts
import { Injectable, inject } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot } from '@angular/router';
import { AuthStore} from './authStore';


@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {
  private authContext = inject(AuthStore);

  canActivate(route: ActivatedRouteSnapshot): boolean {
    // const expectedRoles = route.data['roles'] as UserRole[];
    // const userRole = this.authContext.role();
    //
    // if (!userRole || !expectedRoles.includes(userRole)) {
    //   // Optionally redirect to unauthorized page
    //   return false;
    // }

    return true;
  }
}
