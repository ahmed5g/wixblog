import {computed, inject, Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {jwtDecode} from 'jwt-decode';
import {signalStore, withState, withComputed, withMethods, patchState, withHooks} from '@ngrx/signals';
import {UserBasicInfo} from '../../shared/services/models/tokenUserBasicInfos';
import {JwtPayload} from '../../shared/services/models/jwtPayload';








const initialState: AuthState={
  token: localStorage.getItem('token') // Initialize from localStorage
};


export const AuthStore=signalStore(
  {providedIn: 'root'},


  withState(initialState),

  withComputed(({token}) => {
    const user=computed(() => {
      const currentToken=token();
      if (!currentToken) return null;

      try {
        const payload=jwtDecode<JwtPayload>(currentToken);

        if (Date.now() >= payload.exp * 1000) {
          return null;
        }

        return {
          id: payload.sub,
          email: payload.email,
          name: payload.name,
          role: payload.role,
          profilePicture: payload.picture || '',
          firstName: payload.first_name,
          lastName: payload.last_name,
          createdAt: new Date(payload.created_at * 1000)
        } as UserBasicInfo;
      } catch {
        return null;
      }
    });

    return {
      user,
      isAuthenticated: computed(() => !!user()),
      profilePicture: computed(() => user()?.profilePicture || null)
    };
  }),


  withMethods((store, router=inject(Router)) => {
    const clear=() => {
      localStorage.removeItem('token');
      patchState(store, {token: null});
    };

    const setToken=(newToken: string) => {
      try {
        const payload=jwtDecode<JwtPayload>(newToken);

        if (Date.now() >= payload.exp * 1000) {
          clear();
          return;
        }

        localStorage.setItem('token', newToken);
        patchState(store, {token: newToken});
      } catch {
        clear();
      }
    };

    const processUrlToken=() => {
      const url=new URL(window.location.href);
      const urlToken=url.searchParams.get('token');

      if (urlToken) {
        setToken(urlToken);


        url.searchParams.delete('token');
        const cleanUrl=url.pathname+url.search;
        window.history.replaceState({}, '', cleanUrl);

        setTimeout(() => {
          router.navigate(['/'], {replaceUrl: true});
        }, 0);

        return true;
      }
      return false;
    };

    return {
      setToken,
      logout: () => {
        clear();
        router.navigate(['/login']);
      },
      processUrlToken
    };
  }),

  withHooks({
    onInit(store) {
      const router=inject(Router);

      const wasUrlTokenProcessed=store.processUrlToken();

      if (!wasUrlTokenProcessed && store.token()) {
        try {
          const payload=jwtDecode<JwtPayload>(store.token()!);
          if (Date.now() >= payload.exp * 1000) {
            store.logout();
          }
        } catch {
          store.logout();
        }
      }
    }
  })
);
