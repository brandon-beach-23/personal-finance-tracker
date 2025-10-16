import { Component, Output, EventEmitter, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { CommonModule } from "@angular/common";
// Ensure this path is correct based on your file structure
import {TransactionService} from "../../transaction.service";
import {TransactionRequest} from "../../models/transaction-request.model"; // Using the correct account model path

@Component({
  selector: 'app-account-modal',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './account-modal.component.html',
  styleUrl: './account-modal.component.css'
})
export class AccountModalComponent implements OnInit {
  private fb = inject(FormBuilder);
  private transactionService = inject(TransactionService);


  addTransactionForm!: FormGroup; // Use definite assignment assertion with initialization in ngOnInit
  responseMessage: string = '';

  // Output event to notify the parent component (Account List) to close the modal
  @Output() closeModal = new EventEmitter<void>();

  // Available account types for the select dropdown
  transactionTypes: ('DEBIT' | 'CREDIT')[] = ['DEBIT', 'CREDIT'];

  ngOnInit(): void {
    // Initialize the form with required controls and validators
    this.addTransactionForm = this.fb.group({
      transactionName: ['', Validators.required],
      // Validators.required will enforce a value, but you might want custom number validation too
      transactionAmount: [0, [Validators.required, Validators.min(0)]],
      transactionType: ['', Validators.required],
      transactionCategory: ['', Validators.required],
      transactionDate: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.addTransactionForm.invalid) {
      // Optional: Mark all fields as touched to show errors immediately
      this.addTransactionForm.markAllAsTouched();
      return;
    }

    const accountId = this.transactionService.selectedAccountIdSubject.getValue();

    if (!accountId) {
      this.responseMessage = 'Error: No account selected for this transaction.';
      return;
    }

    // Cast value to your request model
    const transactionRequest: TransactionRequest = this.addTransactionForm.value as TransactionRequest;
    transactionRequest.accountId = accountId;


    this.transactionService.createTransaction(transactionRequest).subscribe({
      next: (response): void => {
        this.responseMessage = `Transaction '${response.transactionName}' added successfully!`;
        console.log('Transaction added successfully', response);

        // Success: Close the modal after a short delay
        setTimeout(() => this.closeModal.emit(), 1000);
      },
      error: (error): void => {
        this.responseMessage = 'Add transaction failed. Please check the console for details.';
        console.error('Add transaction failed', error);
      }
    });
  }

  close(): void {
    this.closeModal.emit();
  }
}
