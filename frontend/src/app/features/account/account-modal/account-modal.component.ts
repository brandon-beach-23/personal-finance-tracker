import { Component, Output, EventEmitter, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { CommonModule } from "@angular/common";
import { AccountService } from "../../../services/account.service"; // Ensure this path is correct based on your file structure
import { AccountRequest} from "../../../models/account-request.model"; // Using the correct account model path

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
  private accountService = inject(AccountService);

  addAccountForm!: FormGroup; // Use definite assignment assertion with initialization in ngOnInit
  responseMessage: string = '';

  // Output event to notify the parent component (Account List) to close the modal
  @Output() closeModal = new EventEmitter<void>();

  // Available account types for the select dropdown
  accountTypes: ('CHECKING' | 'SAVINGS')[] = ['CHECKING', 'SAVINGS'];

  ngOnInit(): void {
    // Initialize the form with required controls and validators
    this.addAccountForm = this.fb.group({
      accountName: ['', Validators.required],
      // Validators.required will enforce a value, but you might want custom number validation too
      accountBalance: [0, [Validators.required, Validators.min(0)]],
      accountType: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.addAccountForm.invalid) {
      // Optional: Mark all fields as touched to show errors immediately
      this.addAccountForm.markAllAsTouched();
      return;
    }

    // Cast value to your request model
    const accountRequest: AccountRequest = this.addAccountForm.value as AccountRequest;

    this.accountService.createAccount(accountRequest).subscribe({
      next: (response): void => {
        this.responseMessage = `Account '${response.accountName}' added successfully!`;
        console.log('Account added successfully', response);

        // Success: Close the modal after a short delay
        setTimeout(() => this.closeModal.emit(), 1000);
      },
      error: (error): void => {
        this.responseMessage = 'Add account failed. Please check the console for details.';
        console.error('Add account failed', error);
      }
    });
  }

  close(): void {
    this.closeModal.emit();
  }
}

