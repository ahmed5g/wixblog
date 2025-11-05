import {Routes} from '@angular/router';
import {UserAccount} from './user-account';
import { userByUsernameResolver} from './user-by-username-resolver';

export const UserRoutes: Routes = [
  { path: '', component: UserAccount},
]
