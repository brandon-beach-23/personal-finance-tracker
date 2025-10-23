import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {AccountService} from "../../account.service";
import {SavingsGoalService} from "../../savings-goal.service";
import {SavingsGoalRequest} from "../../models/savings-goal-request.model";
import {SavingsAccountResponse} from "../../models/savings-account-response.model";
import {NgForOf, NgIf, TitleCasePipe} from "@angular/common";

@Component({
  selector: 'app-add-savings-goal-modal',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    ReactiveFormsModule,
    TitleCasePipe
  ],
  templateUrl: './add-savings-goal-modal.component.html',
  styleUrl: './add-savings-goal-modal.component.css'
})
export class AddSavingsGoalModalComponent implements OnInit{

  constructor(private fb: FormBuilder, private accountService: AccountService, private savingsGoalService: SavingsGoalService) {}

  addSavingsGoalForm!: FormGroup;
  responseMessage: string = '';

  @Input() selectedAccount: SavingsAccountResponse | null = null;

  @Output() closeModal = new EventEmitter<void>();

  ngOnInit() {
    this.addSavingsGoalForm = this.fb.group({
      goalName: ['', Validators.required],
      targetAmount: [0, [Validators.required, Validators.min(0)]]
    });
  }

  onSubmit(): void {
    if (this.addSavingsGoalForm.invalid) {
      this.addSavingsGoalForm.markAllAsTouched();
      return;
    }

    if (!this.selectedAccount) {
      this.responseMessage = 'No account selected.';
      return;
    }


    const savingsGoalRequest: SavingsGoalRequest = this.addSavingsGoalForm.value as SavingsGoalRequest;
    savingsGoalRequest.savingsAccountId = this.selectedAccount.accountId;

    this.savingsGoalService.createGoal(savingsGoalRequest).subscribe({
      next: (response): void => {
        this.responseMessage = `Savings Goal ${response.goalName} added successfully.`;
        console.log('Goal added successfully', response);

        setTimeout(() => this.closeModal.emit(), 1000);
      },
      error: (error): void => {
        this.responseMessage = 'Add savings goal failed. Please check the console for details.';
        console.error('Add goal failed', error);
      }
    });
  }

  close(): void {
    this.closeModal.emit();
  }
}
