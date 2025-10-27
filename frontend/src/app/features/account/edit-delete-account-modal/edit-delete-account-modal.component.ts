import {Component, computed, effect, EventEmitter, inject, OnInit, Output, signal} from '@angular/core';
import {of, tap, throwError} from "rxjs";
import {catchError} from "rxjs/operators";
import {AccountRequest} from "../../../models/account-request.model";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {AccountResponse} from "../../../models/account-response.model";
import {AccountService} from "../../../services/account.service";
import {CommonModule} from "@angular/common";

@Component({
  selector: 'app-edit-delete-account-modal',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './edit-delete-account-modal.component.html',
  styleUrl: './edit-delete-account-modal.component.css'
})
export class EditDeleteAccountModalComponent implements OnInit {

  private fb = inject(FormBuilder);
  private accountService = inject(AccountService);

  // State signals
  accounts = signal<AccountResponse[]>([]);
  selectedAccountId = signal<number | null>(null);
  selectedAccount = computed(() => {
    const id = this.selectedAccountId();
    if (id === null) {
      return null;
    }
    return this.accounts().find(a => a.accountId === id) || null;
  });

  // New state for confirmation step (replaces window.confirm())
  isConfirmingDelete = signal(false);

  // Submission state
  isSubmitting = signal(false);
  action = signal<'update' | 'delete' | null>(null);
  statusMessage = signal<string | null>(null);

  // Form Group
  editForm: FormGroup;

  // Output event to notify the parent component (e.g., AccountListComponent) to close the modal
  @Output() close = new EventEmitter<void>();
  // Output event to notify the parent component that a change requires refreshing the account list
  @Output() accountChanged = new EventEmitter<void>();

  constructor() {
    this.editForm = this.fb.group({
      accountName: [{value: '', disabled: true}, [Validators.required, Validators.maxLength(50)]],
      accountType: [{value: '', disabled: true}, Validators.required],
      balance: [{ value: 0, disabled: true }] // Balance is read-only
    });

    // Effect to automatically populate the form when a new account is selected
    effect(() => {
      const account = this.selectedAccount();
      if (account) {
        // Enable controls once an account is selected
        this.editForm.get('accountName')?.enable({emitEvent: false});
        this.editForm.get('accountType')?.enable({emitEvent: false});

        this.editForm.patchValue({
          accountName: account.accountName,
          accountType: account.accountType,
          // Fixed: Balance check is robust.
          balance: account.balance != null ? account.balance.toFixed(2) : '0.00'// Format for display
        }, { emitEvent: false });
        this.editForm.markAsPristine(); // Reset dirty status after loading
      } else {
        // Disable controls when no account is selected
        this.editForm.get('accountName')?.disable({emitEvent: false});
        this.editForm.get('accountType')?.disable({emitEvent: false});

        // ⭐️ FIX: Replace this.editForm.reset() with patchValue(null) to avoid signal-related rendering crashes
        this.editForm.patchValue({
          accountName: null,
          accountType: null,
          balance: null
        }, { emitEvent: false });
      }
    });
  }

  ngOnInit(): void {
    this.loadAccounts();
  }

  loadAccounts(): void {
    this.accountService.getAccounts().pipe(
      catchError(error => {
        this.statusMessage.set('Error loading accounts: ' + (error.error?.message || 'Server error'));
        return of([]);
      })
    ).subscribe(accounts => {
      this.accounts.set(accounts);
    });
  }

  onAccountSelected(event: Event): void {
    const target = event.target as HTMLSelectElement;
    const rawValue = target.value; // Now this should be a number string like "5"

    // Attempt to parse the raw value into an integer
    const parsedId = Number(rawValue);

    // If parsedId is NaN, it means the rawValue was not a number string (e.g., 'null', '')
    const accountId = (Number.isFinite(parsedId) && rawValue !== 'null') ? parsedId : null;

    // 1. Clear status and reset confirmation
    this.statusMessage.set(null);
    this.isConfirmingDelete.set(false);

    // 2. Clear form if no valid ID selected (The effect handles the form update/clear via the signal)

    // 3. Set the signal
    this.selectedAccountId.set(accountId);
  }

  onCancel(): void {
    if (this.isConfirmingDelete()) {
      // If confirming, go back to edit view
      this.isConfirmingDelete.set(false);
      this.statusMessage.set(null);
    } else {
      // Otherwise, close the modal entirely
      this.close.emit();
    }
  }

  onConfirmDeleteInit(): void {
    if (!this.selectedAccountId()) return;
    this.isConfirmingDelete.set(true);
  }

  onUpdate(): void {
    if (this.editForm.invalid || !this.selectedAccountId()) return;

    this.isSubmitting.set(true);
    this.action.set('update');
    this.statusMessage.set(null);

    // Construct the request DTO (only need name and type)
    const request: AccountRequest = {
      accountName: this.editForm.get('accountName')?.value,
      accountType: this.editForm.get('accountType')?.value,
      // Pass a dummy balance, as the backend DTO requires it but the service ignores it for update
      accountBalance: 0
    };

    this.accountService.updateAccount(this.selectedAccountId()!, request).pipe(
      tap((updatedAccount) => {
        // Update the account list in place to reflect the name/type change instantly
        this.accounts.update(accounts =>
          accounts.map(a => a.accountId === updatedAccount.accountId ? updatedAccount : a)
        );

        this.statusMessage.set('Account updated successfully!');
        this.accountChanged.emit(); // Tell parent to refresh list (if necessary)
        this.editForm.markAsPristine(); // Disable update button after success
      }),
      catchError(error => {
        this.statusMessage.set('Error updating account: ' + (error.error?.message || 'Failed to update.'));
        // Return a failing observable to prevent subscription complete from running
        return throwError(() => error);
      })
    ).subscribe({
      complete: () => {
        this.isSubmitting.set(false);
      }
    });
  }

  onDelete(): void {
    if (!this.selectedAccountId() || !this.isConfirmingDelete()) return;

    this.isSubmitting.set(true);
    this.action.set('delete');
    this.statusMessage.set(null);

    this.accountService.deleteAccount(this.selectedAccountId()!).pipe(
      tap(() => {
        // Remove the deleted account from the local list
        this.accounts.update(accounts => accounts.filter(a => a.accountId !== this.selectedAccountId()));
        this.selectedAccountId.set(null); // Clear selection
        this.isConfirmingDelete.set(false); // Reset confirmation state

        this.statusMessage.set('Account deleted successfully! Closing in 3 seconds...');
        this.accountChanged.emit(); // Tell parent to refresh list

        // Timeout to allow the user to see the success message before closing
        setTimeout(() => this.close.emit(), 3000);

      }),
      catchError(error => {
        this.statusMessage.set('Error deleting account: ' + (error.error?.message || 'Failed to delete.'));
        this.isConfirmingDelete.set(false); // Allow re-attempt
        return throwError(() => error);
      })
    ).subscribe({
      complete: () => {
        this.isSubmitting.set(false);
      }
    });
  }
}
