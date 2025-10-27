import { Component } from '@angular/core';
import {CommonModule} from "@angular/common";
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  ValidationErrors,
  Validators
} from "@angular/forms";
import {AuthService} from "../auth.service";
import {RegistrationRequest} from "../../models/registration-request.model";
import {Router} from "@angular/router";

@Component({
  selector: 'app-registration',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './registration.component.html',
  styleUrl: './registration.component.css'
})
export class RegistrationComponent {

  registerForm: FormGroup;
  responseMessage: string =  '';
  passwordPattern = '^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^A-Za-z0-9]).{8,}$'

  passwordRequirements = {
    isMinLength: false,
    hasLowercase: false,
    hasUpperCase: false,
    hasDigit: false,
    hasSpecialChar: false,
    passwordsMatch: false,
  };

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    this.registerForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.pattern(this.passwordPattern)]],
      confirmPassword: ['', Validators.required]
    }, { validators: this.passwordsMatchValidator.bind(this)});
  }

  onSubmit(): void {
    const registrationRequest: RegistrationRequest = this.registerForm.value;
    this.authService.register(registrationRequest).subscribe({
      next: (response) => {this.responseMessage = 'Registration Sucessful!';
      console.log('Registration Successful!', response);
      this.router.navigate(['/login']);
      },
      error: (error) => {
        console.log('Registration Failed', error);
        this.responseMessage = 'Registration Failed';
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/login']);
  }

  passwordsMatchValidator(form: AbstractControl): ValidationErrors | null {
    const password = form.get('password')?.value;
    const confirmPassword = form.get('confirmPassword')?.value;

    const bothFilled = !!password && !! confirmPassword;
    const mismatch = password !== confirmPassword

    if (this.passwordRequirements) {
      this.passwordRequirements.passwordsMatch = bothFilled && !mismatch;
    }
    return mismatch ? { passwordsMismatch: true } : null;
  }

  onPasswordInput(): void {
    const pwd = this.registerForm.get('password')?.value || '';
    this.passwordRequirements = {
      isMinLength: pwd.length >= 8,
      hasLowercase: /[a-z]/.test(pwd),
      hasUpperCase: /[A-Z]/.test(pwd),
      hasDigit: /\d/.test(pwd),
      hasSpecialChar: /[\W_]/.test(pwd),
      passwordsMatch: this.passwordRequirements.passwordsMatch
    };
    this.registerForm.get('confirmPassword')?.updateValueAndValidity();
  }
}
