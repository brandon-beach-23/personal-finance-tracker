import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {RegistrationRequest} from './models/registration-request.model';
import {Observable, tap} from 'rxjs';
import {LoginRequest} from "./models/login-request.model";
import {LoginResponse} from "./models/login-response.model";


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {    }

  register(data: RegistrationRequest): Observable<any> {
    const url = `${this.apiUrl}/register`;
    return this.http.post(url, data);
  }

  login(credentials: LoginRequest): Observable<any> {
    const url = `${this.apiUrl}/login`;
    return this.http.post<LoginResponse>(url, credentials).pipe(tap(response => {
      localStorage.setItem('jwt_token', response.token);
      localStorage.setItem('username', response.username);
    }));
  }
}
