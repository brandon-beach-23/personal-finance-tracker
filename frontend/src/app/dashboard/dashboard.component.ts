import { Component } from '@angular/core';
import {AuthService} from "../auth/auth.service";
import {Router, RouterLinkActive} from "@angular/router";
import {AccountListComponent} from "../features/account/account-list/account-list.component";
import {AccountModalComponent} from "../features/account/account-modal/account-modal.component";
import {TransactionListComponent} from "../features/transactions/transaction-list/transaction-list.component";
import {SavingsGoalListComponent} from "../features/savings-goal/savings-goal-list/savings-goal-list.component";

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    AccountListComponent,
    AccountModalComponent,
    TransactionListComponent,
    SavingsGoalListComponent,
    RouterLinkActive
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {

  constructor() { }


}
