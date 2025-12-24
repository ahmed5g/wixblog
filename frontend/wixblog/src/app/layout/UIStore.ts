import { patchState, signalStore, withMethods, withState, withHooks } from '@ngrx/signals';
import { effect } from '@angular/core';

const SIDEBAR_STATE_KEY = 'sidebar_is_open';

export const UIStore = signalStore(
  { providedIn: 'root' },
  // 1. Define Initial State
  withState({
    isOpen: JSON.parse(localStorage.getItem(SIDEBAR_STATE_KEY) ?? 'true') as boolean
  }),
  // 2. Define Methods to update state
  withMethods((store) => ({
    toggle(): void {
      patchState(store, (state) => ({ isOpen: !state.isOpen }));
    },
    setOpen(value: boolean): void {
      patchState(store, { isOpen: value });
    }
  })),
  // 3. Sync state to localStorage whenever it changes
  withHooks({
    onInit(store) {
      effect(() => {
        const state = store.isOpen();
        localStorage.setItem(SIDEBAR_STATE_KEY, JSON.stringify(state));
      });
    }
  })
);
