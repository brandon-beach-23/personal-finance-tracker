import {inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {SavingsGoalRequest} from "./models/savings-goal-request.model";
import {Observable} from "rxjs";
import {SavingsAccountResponse} from "./models/savings-account-response.model";
import {SavingsGoalResponse} from "./models/savings-goal-response.model";

@Injectable({
  providedIn: 'root'
})
export class SavingsGoalService {

  private apiUrl = '/api/savings-goals';
  private http = inject(HttpClient);

  constructor() { }

  createGoal(request: SavingsGoalRequest): Observable<SavingsGoalResponse> {
    return this.http.post<SavingsGoalResponse>(this.apiUrl, request);
  }

  updateGoal(id: number, request: SavingsGoalRequest): Observable<SavingsGoalResponse> {
    return this.http.put<SavingsGoalResponse>(`${this.apiUrl}/${id}`, request);
  }

  deleteGoal(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getGoalById(id: number): Observable<SavingsGoalResponse> {
    return this.http.get<SavingsGoalResponse>(`${this.apiUrl}/${id}`);
  }

  getGoalBySavingsAccountId(accountId: number): Observable<SavingsGoalResponse> {
    return this.http.get<SavingsGoalResponse>(`${this.apiUrl}?savingsAccountId=${accountId}`);
  }

}
