import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {SavingsGoalService} from "../../../services/savings-goal.service";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {SavingsGoalResponse} from "../../../models/savings-goal-response.model";
import {SavingsGoalRequest} from "../../../models/savings-goal-request.model";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-edit-savings-goal-modal',
  standalone: true,
  imports: [
    NgIf,
    ReactiveFormsModule
  ],
  templateUrl: './edit-savings-goal-modal.component.html',
  styleUrl: './edit-savings-goal-modal.component.css'
})
export class EditSavingsGoalModalComponent implements OnInit{

  constructor(private savingsGoalService: SavingsGoalService, private fb: FormBuilder) {  }

  public selectedSavingsGoal: SavingsGoalResponse | null = null;
  public editSavingsGoalForm!: FormGroup;
  responseMessage: string = '';

  @Input() savingsAccountId: number | null | undefined = null;
  @Output() close = new EventEmitter<void>();
  @Output() goalUpdated = new EventEmitter<void>();


  ngOnInit() {
    if (this.savingsAccountId){
      this.loadSavingsGoalDetails(this.savingsAccountId);
    }
  }

  private loadSavingsGoalDetails(id: number): void {
    this.savingsGoalService.getGoalBySavingsAccountId(id).subscribe({
      next: (savingsGoal) => {
        this.selectedSavingsGoal = savingsGoal;
        this.initializeForm(savingsGoal);
        console.log('Loaded savings goal for editing:', savingsGoal);
      },
      error:(err)=> {
        console.error('Failed to load savings goal details:', err);
    }
    });
  }

  private initializeForm(savingsGoal: SavingsGoalResponse) {
    this.editSavingsGoalForm = this.fb.group({
      goalName: [savingsGoal.goalName, Validators.required],
      targetAmount: [savingsGoal.targetAmount, Validators.required]
    })
  }

  onUpdateSubmit(): void {
    if (this.editSavingsGoalForm.valid && this.selectedSavingsGoal?.id) {

      const formValue = this.editSavingsGoalForm.value;
      console.log('Form value:', formValue);

      const request: SavingsGoalRequest = {
        goalName: formValue.goalName,
        targetAmount: formValue.targetAmount,
        savingsAccountId: this.selectedSavingsGoal.savingsAccountId
      };
      console.log('Updating savings goal id', this.selectedSavingsGoal.id);
      console.log('Request payload:', request);

      this.savingsGoalService.updateGoal(this.selectedSavingsGoal.id, request).subscribe({
        next: (response) => {
          this.responseMessage = `Savings Goal ${response.goalName} updated successfully.`;
          this.goalUpdated.emit();
          setTimeout(() => this.closeModal(), 1000);
        },
        error: (err) => {
          this.responseMessage = 'Add savings goal failed. Please check the console for details.';
          console.error('Update failed', err);
        }
      });
    }
  }

  closeModal(): void {
    this.close.emit();
  }
}
