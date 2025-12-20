// import { inject } from '@angular/core';
// import { ResolveFn } from '@angular/router';
//
// import {AuthState} from '../auth/auth-state';
// import {UserResponse} from '../../shared/services/models/user-response';
//
//
// export const userByUsernameResolver: ResolveFn<UserResponse | null> = async (route) => {
//   const segment = route.paramMap.get('username')!; // "johndoe"  or  "@johndoe"
//   const username = segment.startsWith('@') ? segment.slice(1) : segment;
//
//   const auth = inject(AuthState);
//   const dto  = auth.user();
//
//   return dto?.email?.startsWith(username + '@') ? dto : null;
// };
