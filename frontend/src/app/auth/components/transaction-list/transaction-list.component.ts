import
{Component, EventEmitter, inject, OnInit, Output, signal} from '@angular/core';
import {TransactionService} from "../../transaction.service";
import {TransactionResponse} from "../../models/transaction-response.model";
import {filter, Observable, switchMap} from "rxjs";
import {CommonModule, CurrencyPipe, DatePipe} from "@angular/common";
import {AddTransactionModalComponent} from "../add-transaction-modal/add-transaction-modal.component";

@Component({
  selector: 'app-transaction-list',
  standalone: true,
  imports: [
    CurrencyPipe,
    DatePipe,
    CommonModule,
    AddTransactionModalComponent
  ],
  templateUrl: './transaction-list.component.html',
  styleUrl: './transaction-list.component.css'
})
export class TransactionListComponent implements OnInit{
  public transactionService = inject(TransactionService);

  public selectedAccountName$: Observable<string | null>;
  public selectedTransactionId$: Observable<number | null>;
  public selectedTransactionName$: Observable<string | null>;


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
      switchMap(accountId => {
        return this.transactionService.getTransactionsByAccountId(accountId);
      })
    )
      .subscribe(transactions => {
        this.transactions.set(transactions);
      });
  }

  selectTransaction(transaction: TransactionResponse): void {
    console.log('Transaction selected:', transaction.transactionName);
    this.transactionSelected.emit(transaction);
    this.transactionService.setSelectedTransactionId(transaction.transactionId);
    this.transactionService.setSelectedTransactionName(transaction.transactionName);
  }


}
