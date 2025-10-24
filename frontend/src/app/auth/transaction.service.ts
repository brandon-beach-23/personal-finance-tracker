import {inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {BehaviorSubject, filter, Observable, of, switchMap, tap, throwError} from "rxjs";
import {TransactionResponse} from "./models/transaction-response.model";
import {TransactionRequest} from "./models/transaction-request.model";
import {catchError} from "rxjs/operators";
import {AccountResponse} from "./models/account-response.model";
import {AccountService} from "./account.service";

@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  private http = inject(HttpClient)
  private accountService = inject(AccountService);
  private apiUrl = 'http://localhost:8080/api/transactions';
  private transactionsSubject =  new BehaviorSubject<TransactionResponse[]>([]);
  public transactions$: Observable<TransactionResponse[]> = this.transactionsSubject.asObservable();
  public selectedAccountIdSubject = new BehaviorSubject<number | null>(null);
  public selectedAccountId$ = this.selectedAccountIdSubject.asObservable();
  public selectedAccountNameSubject = new BehaviorSubject<string | null>(null);
  public selectedAccountName$ = this.selectedAccountNameSubject.asObservable();
  public selectedTransactionIdSubject = new BehaviorSubject<number | null>(null);
  public selectedTransactionId$ = this.selectedTransactionIdSubject.asObservable();
  public selectedTransactionNameSubject = new BehaviorSubject<string | null>(null);
  public selectedTransactionName$ = this.selectedTransactionNameSubject.asObservable();
  private selectedAccountSubject = new BehaviorSubject<AccountResponse | null>(null);
  public selectedAccount$ = this.selectedAccountSubject.asObservable();


  constructor() { }

  //Setter method to update the account ID of the selected account
  public setSelectedAccountId(accountId: number): void {
    this.selectedAccountIdSubject.next(accountId);
  }

  public setSelectedAccountName(accountName: string): void {
    this.selectedAccountNameSubject.next(accountName);
  }

  public setSelectedTransactionId(transactionId: number): void {
    this.selectedTransactionIdSubject.next(transactionId);
  }

  public setSelectedTransactionName(transactionName: string): void {
    this.selectedTransactionNameSubject.next(transactionName);
  }


  //Create a new transaction
  createTransaction(request: TransactionRequest): Observable<TransactionResponse> {
    return this.http.post<TransactionResponse>(this.apiUrl, request).pipe(
      tap(() => {
        const currentId = this.selectedAccountIdSubject.getValue();
        if (currentId !== null){
          this.getTransactionsByAccountId(currentId).subscribe();
          this.getAccountById(currentId).subscribe();
          this.accountService.getAccounts().subscribe();
        }
        console.log('Service: New transaction created refreshing list.');
      }),
      catchError(error => {
        console.error('Error adding transaction.', error);
        return throwError(() => new Error('Transaction creation failed.'))
      })
    );
  }

  //Read transactions by account id
  getTransactionsByAccountId(accountId: number): Observable<TransactionResponse[]> {
    const fullUrl = `${this.apiUrl}/accountId?accountId=${accountId}`;
    return this.http.get<TransactionResponse[]>(fullUrl).pipe(
      tap(transactions => {
        this.transactionsSubject.next(transactions);
      })
    );
  }

  //Update transaction
  updateTransaction(transactionId: number, request :TransactionRequest): Observable<TransactionResponse> {
    const fullUrl = `${this.apiUrl}/${transactionId}`;
    return this.http.put<TransactionResponse>(fullUrl, request).pipe(
      tap(() => {
        const currentId = this.selectedAccountIdSubject.getValue();
        if (currentId !== null){
          this.getTransactionsByAccountId(currentId).subscribe();
          this.getAccountById(currentId).subscribe();
          this.accountService.getAccounts().subscribe();
        }

        console.log('Service: Transaction updated successfully refreshing list.');
      }),
      catchError(error => {
        console.error('Error updating transaction.', error);
        return throwError(() => new Error('Transaction update failed.'))
      })
    );
  }

  //Delete transaction
  deleteTransaction(transactionId: number): Observable<void> {
    const fullUrl: string  = `${this.apiUrl}/${transactionId}`;
    return this.http.delete<void>(fullUrl).pipe(
      tap(() => {
        const currentId = this.selectedAccountIdSubject.getValue();
        if (currentId !== null){
          this.getTransactionsByAccountId(currentId).subscribe();
          this.getAccountById(currentId).subscribe();
          this.accountService.getAccounts().subscribe();
        }

        console.log('Service: Transaction deleted successfully refreshing list.');
      }),
      catchError(error => {
        console.error('Error deleting transaction.', error);
        return throwError(() => new Error('Transaction deletion failed.'))
      })
    );
  }

  getTransactionById(transactionId: number): Observable<TransactionResponse> {
    const fullUrl = `${this.apiUrl}/${transactionId}`;
    return this.http.get<TransactionResponse>(fullUrl).pipe(
      catchError(error => {
        console.error('Error fetching single transaction.', error);
        return throwError(() => new Error('Transaction fetch failed.'));
      })
    );
  }

  getAccountById(accountId: number): Observable<AccountResponse> {
    const url = `http://localhost:8080/api/accounts/${accountId}`;
    return this.http.get<AccountResponse>(url).pipe(
      tap(account => {
        this.selectedAccountSubject.next(account);
        console.log('Service: Account refreshed with updated balance.');
      }),
      catchError(error => {
        console.error('Error fetching account.', error);
        return throwError(() => new Error('Account fetch failed.'));
      })
    );
  }

  getTransactionsByMonthAndYear(month: number, year: number): Observable<TransactionResponse[]> {
    const url = `${this.apiUrl}/report?month=${month}&year=${year}`;
    return this.http.get<TransactionResponse[]>(url).pipe(
      tap(report => {
        console.log(`Service: Fetched ${report.length} transactions for ${month}/${year}`);
      }),
      catchError(error => {
        console.error('Error fetching spending report:', error);
        return throwError(() => new Error('Spending report fetch failed'));
      })
    );
  }

}
