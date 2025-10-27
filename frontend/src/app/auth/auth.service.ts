import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {RegistrationRequest} from '../models/registration-request.model';
import {BehaviorSubject, Observable, tap} from 'rxjs';
import {LoginRequest} from "../models/login-request.model";
import {LoginResponse} from "../models/login-response.model";
import {Router} from "@angular/router";
import {jwtDecode} from "jwt-decode";
import {environment} from "../../environments/environment";
import {AccountService} from "../services/account.service";
import {TransactionService} from "../services/transaction.service";


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/api/auth`;
  private TOKEN_KEY = 'jwt_token';
  private USERNAME_KEY = 'username';
  private loggedIn$ =  new BehaviorSubject<boolean>(false);

  constructor(private http: HttpClient, private router: Router, private accountService: AccountService, private transactionService: TransactionService) {
    this.updateLoginStatus();
  }

  register(data: RegistrationRequest): Observable<any> {
    const url = `${this.apiUrl}/register`;
    return this.http.post(url, data);
  }

  login(credentials: LoginRequest): Observable<any> {
    const url = `${this.apiUrl}/login`;

    return this.http.post<LoginResponse>(url, credentials).pipe(tap(response => {
      localStorage.removeItem('selectedAccountId')
      localStorage.removeItem('cachedAccounts');
      localStorage.removeItem('cachedTransactions');

      localStorage.setItem(this.TOKEN_KEY, response.token);
      localStorage.setItem(this.USERNAME_KEY, response.username);
      this.updateLoginStatus();
      })
    );
  }

  getToken(): string | null {
    try {
      return localStorage.getItem(this.TOKEN_KEY);
    } catch (e) {
      console.error('Error accessing localStorage:', e);
      return null;
    }
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USERNAME_KEY);
    this.accountService.clearState();
    this.transactionService.clearState();
    this.updateLoginStatus();
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    const token = this.getToken();
    return !!token && !this.isTokenExpired(token);
  }

  private isTokenExpired(token: string): boolean {
    if (!token) return true;
    try {
      const decoded: any = jwtDecode(token);
      const currentTime = Date.now() / 1000;
      return decoded.exp < currentTime;
    } catch (error) {
      return true;
    }
  }

  getCurrentUsername(): string | null {
    return localStorage.getItem(this.USERNAME_KEY);
  }

  get isLoggedIn$(): Observable<boolean> {
    return this.loggedIn$.asObservable();
  }

  updateLoginStatus(): void {
    this.loggedIn$.next(this.isLoggedIn());
  }
}
