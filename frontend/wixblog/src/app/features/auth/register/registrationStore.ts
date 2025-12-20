import { signalStore, withState, withMethods, patchState, getState } from '@ngrx/signals';
import { effect } from '@angular/core';
import {RegisterRequest} from '../../../shared/services/models/register-request'; // <--- Import effect


const initialState: Required<RegisterRequest> = {
  email: '',
  name: '',
  password: '',
  profilePicture: '',
  bio: ''
};

export const RegistrationStore = signalStore(
  { providedIn: 'root' },
  withState<Required<RegisterRequest>>(() => {
    const saved = sessionStorage.getItem('registration_draft');
    return saved ? JSON.parse(saved) : initialState;
  }),
  withMethods((store) => {

    effect(() => {
      const state = getState(store);
      sessionStorage.setItem('registration_draft', JSON.stringify(state));
    });

    return {
      updateStep(fields: Partial<RegisterRequest>) {
        patchState(store, fields);
      },
      setProfilePicture(picture: string): void {
        patchState(store, { profilePicture: picture });
      },
      reset() {
        patchState(store, initialState);
        sessionStorage.removeItem('registration_draft');
      }
    };
  })
);
