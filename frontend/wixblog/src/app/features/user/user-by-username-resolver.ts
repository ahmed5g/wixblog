import { inject } from '@angular/core';
import { ResolveFn } from '@angular/router';
import { AuthResponseDto } from '../../shared/services/models/auth-response-dto';
import {AuthState} from '../auth/auth-state';


export const userByUsernameResolver: ResolveFn<AuthResponseDto | null> = async (route) => {
  const segment = route.paramMap.get('username')!; // "johndoe"  or  "@johndoe"
  const username = segment.startsWith('@') ? segment.slice(1) : segment;

  const auth = inject(AuthState);
  const dto  = auth.user();

  return dto?.email?.startsWith(username + '@') ? dto : null;
};
