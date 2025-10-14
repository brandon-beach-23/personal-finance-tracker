import {inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {BehaviorSubject, Observable, tap, throwError} from "rxjs";
import {AccountResponse} from "./models/account-response.model";
import {catchError} from "rxjs/operators";
import {AccountRequest} from "./models/account-request.model";

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/accounts';
  private accountsSubject = new BehaviorSubject<AccountResponse[]>([]);
  public accounts$: Observable<AccountResponse[]> = this.accountsSubject.asObservable();

  constructor() { }

  // --- CREATE ---
  createAccount(request: AccountRequest): Observable<AccountResponse> { // <-- Return type specified here
    // ⭐️ FIX: Explicitly specify the return type of the POST request as <AccountResponse>
    return this.http.post<AccountResponse>(this.apiUrl, request).pipe(
      tap(() => {
        // After successful creation, refresh the entire list by re-calling getAccounts()
        // This makes sure the new account shows up immediately.
        this.getAccounts().subscribe();
        console.log('Service: New account created, refreshing list.');
      }),
      catchError(error => {
        console.error('Error adding account:', error);
        // Re-throw the error so the component can handle it (e.g., show an alert)
        return throwError(() => new Error('Account creation failed.'));
      })
    );
  }

  // --- READ ALL ---
  getAccounts(): Observable<AccountResponse[]> {
    return this.http.get<AccountResponse[]>(this.apiUrl).pipe(
      // Use the tap operator to inspect the data and push it into the Subject
      tap(accounts => {
        // This is the line that pushes the data to everyone subscribing to accounts$
        this.accountsSubject.next(accounts);
        console.log('Service: Accounts loaded and Subject updated:', accounts);
      })
    );
  }

  // --- READ ONE (New) ---
  getAccountById(accountId: number): Observable<AccountResponse> {
    return this.http.get<AccountResponse>(`${this.apiUrl}/${accountId}`);
  }

  // --- UPDATE (New) ---
  updateAccount(accountId: number, request: AccountRequest): Observable<AccountResponse> {
    // Note: The backend only uses name and type from this request DTO for update
    return this.http.put<AccountResponse>(`${this.apiUrl}/${accountId}`, request);
  }

  // --- DELETE (New) ---
  deleteAccount(accountId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${accountId}`);
  }
}
