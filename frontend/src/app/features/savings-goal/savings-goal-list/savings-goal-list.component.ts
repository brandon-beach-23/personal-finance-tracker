import {Component, inject, OnInit, signal} from '@angular/core';
import {map, Observable} from "rxjs";
import {SavingsAccountResponse} from "../../../models/savings-account-response.model";
import {SavingsGoalService} from "../../../services/savings-goal.service";
import {AccountService} from "../../../services/account.service";
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
  responseMessage: string = '';

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
    console.log('Opening modal with goal ID:', account.savingsGoalResponse?.id);

    this.goalId = account.savingsGoalResponse?.id;
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

  confirmDeleteGoal(account: SavingsAccountResponse): void {
    const goal = account.savingsGoalResponse;
    if (!goal?.id) return;

    const confirmed = window.confirm(`Are you sure you want to delete the goal "${goal.goalName}"?`);
    if (!confirmed) return;

    this.savingsGoalService.deleteGoal(goal.id).subscribe({
      next: () => {
        this.responseMessage = 'Savings goal deleted successfully.';
        this.accountService.getAccounts().subscribe(); // Refresh list or close modal
        this.selectedAccount.set(null);
      },
      error: (err) => {
        this.responseMessage = 'Delete failed. Please check the console for details.';
        console.error('Delete failed', err);
      }
    });
  }


}
