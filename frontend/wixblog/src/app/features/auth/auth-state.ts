import {Injectable, Signal, signal} from '@angular/core';
import { AuthResponseDto } from '../../shared/services/models/auth-response-dto';
import { AuthenticationService } from '../../shared/services/services/authentication.service';

@Injectable({ providedIn: 'root' })
export class AuthState {
  private readonly _user = signal<AuthResponseDto | null>(null);
  readonly user: Signal<AuthResponseDto | null> = this._user.asReadonly();  // read-only view for components

  constructor(private api: AuthenticationService) {
    this.init();
  }

   init(): void {
    // subscribe to the OBSERVABLE, then write the value into the signal
    this.api.getCurrentUser().subscribe({
      next:  dto  => this._user.set(dto),
      error: ()   => this._user.set(null)
    });
  }

  logout(): void {
    this.api.logout().subscribe(() => this._user.set(null));
  }
}
