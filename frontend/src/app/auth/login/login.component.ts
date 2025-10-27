import { Component } from '@angular/core';
import {CommonModule} from "@angular/common";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {AuthService} from "../auth.service";
import {Router} from "@angular/router";
import {LoginRequest} from "../../models/login-request.model";
import {error} from "@angular/compiler-cli/src/transformers/util";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  loginForm: FormGroup;
  responseMessage: string = '';

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router ) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  onsubmit(): void {
    const loginRequest: LoginRequest = this. loginForm.value;
    this.authService.login(loginRequest).subscribe({
      next: (response) => {
      this.responseMessage = 'Login Successful';
      console.log('Login Successful', response);
      this.router.navigate(['/dashboard']);
      },
      error: (error) => {
        this.responseMessage = 'Incorrect user name or password';
        console.log("Incorrect user name or password");
        this.loginForm.controls['password'].reset();
      }
      });
    }

    goToRegister(): void {
    this.router.navigate(['/register']);
    }

}
