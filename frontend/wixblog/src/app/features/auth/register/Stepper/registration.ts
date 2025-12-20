// step-one.component.ts
import {Component, inject, output} from '@angular/core';
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';
import {RegistrationStore} from '../registrationStore';


@Component({
  selector: 'app-registration',
  standalone: true,
  imports: [ReactiveFormsModule],
  template: `
    <div class="flex flex-col items-center animate-step">
      <h2 class="text-[32px] font-serif font-bold text-[#292929] mb-12 tracking-tight">
        Join WixBlog.
      </h2>

      <form [formGroup]="form" (ngSubmit)="onSubmit()" class="w-full max-w-[320px]">
        <div class="mb-8">
          <label class="text-xs text-gray-500 font-medium uppercase tracking-widest mb-1 block">Your Name</label> <input
          type="text" formControlName="name" placeholder="Name"
          class="w-full border-b border-gray-200 py-2 text-lg outline-none focus:border-black transition-colors placeholder:text-gray-200">
        </div>

        <div class="mb-8">
          <label class="text-xs text-gray-500 font-medium uppercase tracking-widest mb-1 block">Your Email</label>
          <input type="email" formControlName="email" placeholder="Email"
                 class="w-full border-b border-gray-200 py-2 text-lg outline-none focus:border-black transition-colors placeholder:text-gray-200">
        </div>

        <div class="mb-10">
          <label class="text-xs text-gray-500 font-medium uppercase tracking-widest mb-1 block">Your Password</label>
          <input type="password" formControlName="password" placeholder="Password"
                 class="w-full border-b border-gray-200 py-2 text-lg outline-none focus:border-black transition-colors placeholder:text-gray-200">
        </div>

        <button
          type="submit"
          [disabled]="form.invalid"
          class="btn-black"
        >
          Next
        </button>

      </form>
    </div>
  `
})
export class Registration {
  readonly store=inject(RegistrationStore);
  private fb=inject(FormBuilder);
  next=output<void>();

  form=this.fb.group({
    name: [this.store.name(), Validators.required],
    email: [this.store.email(), [Validators.required, Validators.email]],
    password: [this.store.password(), [Validators.required, Validators.minLength(6)]]
  });

  onSubmit() {
    console.log('Form Status:', this.form.status); // Check if it says 'VALID'

    if (this.form.valid) {
      const rawValues = this.form.getRawValue();
      this.store.updateStep(rawValues as any);

      console.log('Emitting next...');
      this.next.emit(); // This triggers the (next) in the wrapper
    } else {
      // Mark all as touched to show errors if it's invalid
      this.form.markAllAsTouched();
    }
  }
}
