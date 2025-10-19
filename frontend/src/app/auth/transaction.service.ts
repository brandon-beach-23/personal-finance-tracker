import {inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {BehaviorSubject, Observable, of, tap, throwError} from "rxjs";
import {TransactionResponse} from "./models/transaction-response.model";
import {TransactionRequest} from "./models/transaction-request.model";
import {catchError} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  private http = inject(HttpClient)
  private apiUrl = 'http://localhost:8080/api/transactions';
  private transactionsSubject =  new BehaviorSubject<TransactionResponse[]>([]);
  private transactions$: Observable<TransactionResponse[]> = this.transactionsSubject.asObservable();
  public selectedAccountIdSubject = new BehaviorSubject<number | null>(null);
  public selectedAccountId$ = this.selectedAccountIdSubject.asObservable();
  public selectedAccountNameSubject = new BehaviorSubject<string | null>(null);
  public selectedAccountName$ = this.selectedAccountNameSubject.asObservable();
  public selectedTransactionIdSubject = new BehaviorSubject<number | null>(null);
  public selectedTransactionId$ = this.selectedTransactionIdSubject.asObservable();
  public selectedTransactionNameSubject = new BehaviorSubject<string | null>(null);
  public selectedTransactionName$ = this.selectedTransactionNameSubject.asObservable();

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
    return this.http.put<TransactionResponse>(this.apiUrl, request).pipe(
      tap(() => {
        const currentId = this.selectedAccountIdSubject.getValue();
        if (currentId !== null){
          this.getTransactionsByAccountId(currentId).subscribe();
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
        }

        console.log('Service: Transaction deleted successfully refreshing list.');
      }),
      catchError(error => {
        console.error('Error deleting transaction.', error);
        return throwError(() => new Error('Transaction deletion failed.'))
      })
    );
  }
}
