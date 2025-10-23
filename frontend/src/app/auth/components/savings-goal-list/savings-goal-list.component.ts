import {Component, inject, OnInit, signal} from '@angular/core';
import {map, Observable} from "rxjs";
import {SavingsAccountResponse} from "../../models/savings-account-response.model";
import {SavingsGoalService} from "../../savings-goal.service";
import {AccountService} from "../../account.service";
import {AsyncPipe, DecimalPipe, NgForOf, NgIf} from "@angular/common";
import {AddSavingsGoalModalComponent} from "../add-savings-goal-modal/add-savings-goal-modal.component";

@Component({
  selector: 'app-savings-goal-list',
  standalone: true,
  imports: [
    DecimalPipe,
    AsyncPipe,
    NgIf,
    NgForOf,
    AddSavingsGoalModalComponent
  ],
  templateUrl: './savings-goal-list.component.html',
  styleUrl: './savings-goal-list.component.css'
})
export class SavingsGoalListComponent implements OnInit{

  public savingsAccounts$!: Observable<SavingsAccountResponse[]>;

  isAddSavingsGoalModalOpen = signal(false);
  isEditDeleteSavingsGoalModalOpen = signal(false);

  selectedAccount = signal<SavingsAccountResponse | null>(null);

  constructor(private accountService: AccountService, private savingsGoalService: SavingsGoalService) {  }

  ngOnInit() {
    this.savingsAccounts$ = this.accountService.accounts$.pipe(
      map(accounts => accounts.filter(account => account.accountType === 'SAVINGS'))
    );
  }

  openAddSavingsGoalModal(account: SavingsAccountResponse) {
    this.selectedAccount.set(account)
    this.isAddSavingsGoalModalOpen.set(true);
  }

  closeAddSavingsGoalModal() {
    this.isAddSavingsGoalModalOpen.set(false);
    this.accountService.getAccounts().subscribe();
  }

  openEditDeleteSavingsGoalModal(account: SavingsAccountResponse) {
    this.selectedAccount.set(account);
    this.isEditDeleteSavingsGoalModalOpen.set(true);
  }

  closeEditDeleteSavingsGoalModal() {
    this.isEditDeleteSavingsGoalModalOpen.set(false);
    this.accountService.getAccounts().subscribe();
  }

  refreshAccounts() {
    this.accountService.getAccounts();
  }
}
