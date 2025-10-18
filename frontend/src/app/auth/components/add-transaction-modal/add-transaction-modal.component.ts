import { Component, Output, EventEmitter, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { CommonModule } from "@angular/common";
// Ensure this path is correct based on your file structure
import {TransactionService} from "../../transaction.service";
import {TransactionRequest} from "../../models/transaction-request.model";
import {CategoryService} from "../../category.service";
import {CategoryResponse} from "../../models/category-response.model";
import {AddCategoryModalComponent} from "../add-category-modal/add-category-modal.component"; // Using the correct account model path

@Component({
  selector: 'app-add-transaction-modal',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    AddCategoryModalComponent
  ],
  templateUrl: './add-transaction-modal.component.html',
  styleUrl: './add-transaction-modal.component.css'
})
export class AddTransactionModalComponent implements OnInit {
  private fb = inject(FormBuilder);
  private transactionService = inject(TransactionService);
  private categoryService = inject(CategoryService);


  addTransactionForm!: FormGroup; // Use definite assignment assertion with initialization in ngOnInit
  responseMessage: string = '';

  isAddCategoryModalOpen: boolean = false;

  // Output event to notify the parent component (Account List) to close the modal
  @Output() closeModal = new EventEmitter<void>();

  // Available transaction types for the select dropdown
  transactionTypes: ('DEBIT' | 'CREDIT')[] = ['DEBIT', 'CREDIT'];
  public transactionCategories: CategoryResponse[] = [];

  ngOnInit(): void {
    // Initialize the form with required controls and validators
    this.addTransactionForm = this.fb.group({
      transactionName: ['', Validators.required],
      // Validators.required will enforce a value, but you might want custom number validation too
      transactionAmount: [0, [Validators.required, Validators.min(0.01)]],
      transactionType: ['', Validators.required],
      categoryId: [null, Validators.required],
      transactionDate: [this.getDefaultDate(), Validators.required]
    });

    this.loadCategories();
  }

  private getDefaultDate(): string{
    return new Date().toISOString().substring(0, 10);
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

    const rawFormValue = this.addTransactionForm.value;

    console.log('Amount being sent:', rawFormValue.transactionAmount, typeof rawFormValue.transactionAmount);

    const transactionRequest: TransactionRequest = {
      accountId: accountId,
      categoryId: rawFormValue.categoryId,
      transactionName: rawFormValue.transactionName,
      transactionAmount: rawFormValue.transactionAmount,
      transactionType: rawFormValue.transactionType,
      transactionDate: rawFormValue.transactionDate



    };

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

  closeAddCategoryModal(): void {
    this.isAddCategoryModalOpen = false;
    this.loadCategories();
    this.addTransactionForm.get('categoryId')?.setValue(null);
  }

  loadCategories(): void {
    this.categoryService.getAllCategories().subscribe(categories => {
      this.transactionCategories = categories;
    });
  }

  onCategoryAction(event: Event): void {
    const target = event.target as HTMLSelectElement;

    if (target.value === 'add-new') {
      this.isAddCategoryModalOpen = true;
      // When opening the modal, set to null to ensure the user must re-select upon return
      this.addTransactionForm.get('categoryId')?.setValue(null);
    }

    // CRITICAL: Force the control to mark itself as touched and update validity
    // This resolves the visual and internal state mismatch.
    this.addTransactionForm.get('categoryId')?.markAsTouched();
    this.addTransactionForm.get('categoryId')?.updateValueAndValidity();
  }

  // onCategorySelected(event: Event): void {
  //   const target = event.target as HTMLSelectElement;
  //   const rawValue = target.value;
  //
  //   if (rawValue === 'add-new') {
  //     this.isAddCategoryModalOpen = true;
  //     // Keep the control value as null/old ID while modal is open, or set to null
  //     // to force re-selection upon returning.
  //     this.addTransactionForm.get('categoryId')?.setValue(null);
  //     return;
  //   }
  //
  //   // const parsedId = Number(rawValue);
  //   // const categoryId = (Number.isFinite(parsedId) && rawValue !== 'null') ? parsedId : null;
  //   //
  //   // this.addTransactionForm.get('categoryId')?.setValue(categoryId);
  //   this.addTransactionForm.get('categoryId')?.markAsTouched();
  //   this.addTransactionForm.get('categoryId')?.updateValueAndValidity();
  //
  //   console.log('Final Category ID Value:', this.addTransactionForm.get('categoryId')?.value);
  //   console.log('Control Validity:', this.addTransactionForm.get('categoryId')?.valid);
  // }
}
