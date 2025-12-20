// step-two.component.ts
import {Component, inject, output, signal} from '@angular/core';

import { FormsModule } from '@angular/forms';
import {RegistrationStore} from '../registrationStore';

@Component({
  selector: 'app-user-info',
  standalone: true,
  imports: [FormsModule],
  template: `
    <div class="flex flex-col items-center animate-step">
      <h2 class="text-[32px] font-serif font-bold text-[#292929] mb-4 tracking-tight">
        Create your profile.
      </h2>
      <p class="text-gray-500 text-sm mb-12 text-center">This is how readers will see you.</p>

      <div class="w-full max-w-[320px]">
        <input type="file" #fileInput (change)="onFileSelected($event)" accept="image/*" class="hidden">

        <div class="flex flex-col items-center mb-10">
          <div (click)="fileInput.click()"
               class="relative w-24 h-24 rounded-full bg-gray-50 border border-dashed border-gray-300 flex items-center justify-center cursor-pointer overflow-hidden hover:bg-gray-100 transition-colors group">

            @if (imagePreview()) {
              <img [src]="imagePreview()" class="w-full h-full object-cover">
              <div class="absolute inset-0 bg-black/20 opacity-0 group-hover:opacity-100 flex items-center justify-center transition-opacity">
                <span class="text-[10px] text-white font-bold uppercase tracking-tighter">Change</span>
              </div>
            } @else {
              <span class="text-xs text-gray-400 group-hover:text-gray-600 text-center px-2">Upload Photo</span>
            }
          </div>
        </div>

        <div class="mb-12">
          <label class="text-xs text-gray-400 font-medium uppercase tracking-widest mb-1 block text-left">Short Bio</label>
          <textarea [(ngModel)]="bio" placeholder="Writer, reader, and enthusiast..."
                    class="w-full border-b border-gray-200 py-2 text-lg outline-none focus:border-black transition-colors placeholder:text-gray-200 resize-none h-24"></textarea>
        </div>

        <div class="flex items-center justify-between gap-4">
          <button (click)="back.emit()" class="text-gray-400 hover:text-black text-sm font-medium transition-colors">
            Go back
          </button>

          <button (click)="submit()" class="btn-green">
            Create Account
          </button>
        </div>
      </div>
    </div>
  `
})
export class UserInfo {
  readonly store = inject(RegistrationStore);
  back = output<void>();
  finish = output<void>();

  bio = this.store.bio();

  imagePreview = signal<string | null>(this.store.profilePicture() || null);

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      const reader = new FileReader();
      reader.onload = () => {
        const base64String = reader.result as string;
        this.imagePreview.set(base64String);
        this.store.updateStep({ profilePicture: base64String });
      };
      reader.readAsDataURL(input.files[0]);
    }
  }

  submit() {
    this.store.updateStep({ bio: this.bio });
    this.finish.emit();
  }
}
