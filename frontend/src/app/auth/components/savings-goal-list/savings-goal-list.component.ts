import {Component, inject, OnInit, signal} from '@angular/core';
import {map, Observable} from "rxjs";
import {SavingsAccountResponse} from "../../models/savings-account-response.model";
import {SavingsGoalService} from "../../savings-goal.service";
import {AccountService} from "../../account.service";
import {AsyncPipe, DecimalPipe, NgForOf, NgIf} from "@angular/common";
import {AddSavingsGoalModalComponent} from "../add-savings-goal-modal/add-savings-goal-modal.component";
import {EditSavingsGoalModalComponent} from "../edit-savings-goal-modal/edit-savings-goal-modal.component";

@Component({
  selector: 'app-savings-goal-list',
  standalone: true,
  imports: [
    DecimalPipe,
    AsyncPipe,
    NgIf,
    NgForOf,
    AddSavingsGoalModalComponent,
    EditSavingsGoalModalComponent
  ],
  templateUrl: './savings-goal-list.component.html',
  styleUrl: './savings-goal-list.component.css'
})
export class SavingsGoalListComponent implements OnInit{

  public savingsAccounts$!: Observable<SavingsAccountResponse[]>;
  public goalId: number | null | undefined = null;


  isAddSavingsGoalModalOpen = signal(false);
  isEditSavingsGoalModalOpen = signal(false);

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

  openEditSavingsGoalModal(account: SavingsAccountResponse) {
    this.goalId = account.savingsGoalResponse?.savingsAccountId;
    this.selectedAccount.set(account);
    this.isEditSavingsGoalModalOpen.set(true);
  }

  closeEditSavingsGoalModal() {
    this.isEditSavingsGoalModalOpen.set(false);
    this.accountService.getAccounts().subscribe();
  }

  refreshAccounts() {
    this.accountService.getAccounts();
  }


}
