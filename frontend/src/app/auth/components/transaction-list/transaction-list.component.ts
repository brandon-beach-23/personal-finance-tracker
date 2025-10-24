import
{assertInInjectionContext, Component, EventEmitter, inject, OnInit, Output, signal} from '@angular/core';
import {TransactionService} from "../../transaction.service";
import {TransactionResponse} from "../../models/transaction-response.model";
import {empty, filter, Observable, switchMap} from "rxjs";
import {CommonModule, CurrencyPipe, DatePipe} from "@angular/common";
import {AddTransactionModalComponent} from "../add-transaction-modal/add-transaction-modal.component";
import {
  EditDeleteTransactionModalComponent
} from "../edit-delete-transaction-modal/edit-delete-transaction-modal.component";
import {TransactionRequest} from "../../models/transaction-request.model";
import {FormControl, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {CategoryService} from "../../category.service";

@Component({
  selector: 'app-transaction-list',
  standalone: true,
  imports: [
    CurrencyPipe,
    DatePipe,
    CommonModule,
    AddTransactionModalComponent,
    EditDeleteTransactionModalComponent,
    ReactiveFormsModule
  ],
  templateUrl: './transaction-list.component.html',
  styleUrl: './transaction-list.component.css'
})
export class TransactionListComponent implements OnInit{
  public transactionService = inject(TransactionService);
  private categoryService = inject(CategoryService);

  public selectedAccountName$: Observable<string | null>;
  public selectedTransactionId$: Observable<number | null>;
  public selectedTransactionName$: Observable<string | null>;
  public transactions$ = this.transactionService.transactions$;
  filteredTransactions: TransactionResponse[] = [];
  categories: string[] = [];


  transactionFilterForm = new FormGroup({
    filterType: new FormControl('name'),
    searchTerm: new FormControl(''),
    category: new FormControl('')
  });



  constructor() {
    this.selectedAccountName$ = this.transactionService.selectedAccountName$;
    this.selectedTransactionId$ = this.transactionService.selectedTransactionId$
    this.selectedTransactionName$ = this.transactionService.selectedTransactionName$;
  }


  public transactions = signal<TransactionResponse[]>([]);

  @Output() transactionSelected = new EventEmitter<TransactionResponse>();

  isAddTransactionModalOpen = signal(false);
  isEditDeleteTransactionModalOpen = signal(false);

  openAddTransactionModal(): void {
    this.isAddTransactionModalOpen.set(true);
  }

  closeAddTransactionModal(): void {
    this.isAddTransactionModalOpen.set(false);
  }

  openEditDeleteTransactionModal(): void {
    this.isEditDeleteTransactionModalOpen.set(true);
  }

  closeEditDeleteTransactionModal(): void {
    this.isEditDeleteTransactionModalOpen.set(false);
  }

  ngOnInit(): void {

    this.transactionService.selectedAccountId$.pipe(
      filter((id): id is number => id !== null),
      switchMap(accountId => this.transactionService.getTransactionsByAccountId(accountId))).subscribe();

    this.categoryService.getAllCategories().subscribe(categoryResponses => {
      this.categories = categoryResponses.map(cat => cat.categoryName);
    });

    this.transactionService.selectedAccountId$.pipe(
      filter((id): id is number => id !== null),
      switchMap(accountId => this.transactionService.getTransactionsByAccountId(accountId))
    ).subscribe();

    this.transactionService.transactions$.subscribe(txns => {
      this.transactions.set(txns);
      this.filteredTransactions = txns;
    });

    this.transactionFilterForm.valueChanges.subscribe(filters => {
      this.applyFilters(filters);
    });

  }

  selectTransaction(transaction: TransactionResponse): void {
    console.log('TransactionName selected:', transaction.transactionName);
    console.log('TransactionId selected:', transaction.transactionId);
    this.transactionSelected.emit(transaction);
    this.transactionService.setSelectedTransactionId(transaction.transactionId);
    this.transactionService.setSelectedTransactionName(transaction.transactionName);
  }


  trackByTransactionId(index: number, transaction: TransactionResponse): number {
    return transaction.transactionId;
  }

  applyFilters(filters: any): void {
    const { searchTerm, category } = filters;
    const term = searchTerm.toLowerCase();

    this.filteredTransactions = this.transactions().filter(tx => {
      const matchesCategory = !category || tx.categoryName === category;
      const matchesName = tx.transactionName.toLowerCase().includes(term);

      return matchesCategory && matchesName;
    });
  }




}
