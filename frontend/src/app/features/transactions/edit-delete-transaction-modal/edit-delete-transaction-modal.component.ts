import {Component, EventEmitter, inject, Input, OnInit, Output} from '@angular/core';
import {TransactionService} from "../../../services/transaction.service";
import {TransactionResponse} from "../../../models/transaction-response.model";
import {CategoryService} from "../../../services/category.service";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {CategoryResponse} from "../../../models/category-response.model";
import {Observable} from "rxjs";
import {TransactionRequest} from "../../../models/transaction-request.model";
import {NgForOf} from "@angular/common";

@Component({
  selector: 'app-edit-delete-transaction-modal',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgForOf
  ],
  templateUrl: './edit-delete-transaction-modal.component.html',
  styleUrl: './edit-delete-transaction-modal.component.css'
})
export class EditDeleteTransactionModalComponent implements OnInit{

  private transactionService = inject(TransactionService);
  private categoryService = inject(CategoryService);

  private fb = inject(FormBuilder);
  editTransactionForm!: FormGroup;

  categories: CategoryResponse[] = [];

  @Input() transactionId: number | null = null;
  @Output() close = new EventEmitter<void>();

  public selectedTransaction: TransactionResponse | null = null;

  ngOnInit() {
    if (this.transactionId) {
      this.categoryService.getAllCategories().subscribe({
        next: (data) => {
          this.categories = data;
          this.loadTransactionDetails(this.transactionId!);
        },
        error: (err) => {
          console.error("Failed to load categories:", err);
        }
      });
    }
  }


  private loadTransactionDetails(id: number) {
    this.transactionService.getTransactionById(id).subscribe({
      next: (transaction) => {
        this.selectedTransaction = transaction;
        this.initializeForm(transaction);
        console.log('Loaded transaction for editing:', transaction);
      },
      error: (err) => {
        console.error("Failed to load transaction details:", err);
      }
    });
  }

  closeModal(): void {
    this.close.emit();
  }

  private initializeForm(transaction: TransactionResponse) {
    this.editTransactionForm = this.fb.group({
      transactionName: [transaction.transactionName, Validators.required],
      transactionAmount: [transaction.transactionAmount, Validators.required],
      transactionType: [transaction.transactionType, Validators.required],
      categoryName: [transaction.categoryName, Validators.required],
      transactionDate: [transaction.transactionDate, Validators.required],
    })

  }

  onUpdateSubmit(): void {

    if (this.editTransactionForm.valid && this.transactionId) {
      const formValue = this.editTransactionForm.value;
      const selectedCategory = this.categories.find(c => c.categoryName === formValue.categoryName);
      const request: TransactionRequest = {
        ...formValue,
        accountId: this.selectedTransaction?.accountId,
        categoryId: selectedCategory?.categoryId

      };
      console.log('Updating transaction ID:', this.transactionId);

      console.log('Request payload:', request);


      this.transactionService.updateTransaction(this.transactionId, request).subscribe({
        next:() => {
          this.closeModal();
        },
        error: (err) => {
          console.error('Update failed:', err);
        }
      });
    }
  }

  onDeleteConfirm(): void {
    if (confirm(`Are you sure you want to delete the transaction "${this.selectedTransaction?.transactionName}"?`)) {
      if (this.transactionId) {
        this.transactionService.deleteTransaction(this.transactionId).subscribe({
          next: () => {
            this.closeModal();
          },
          error: (err) => {
            console.error('Delete failed:', err);
          }
        });
      }
    }
  }
}
