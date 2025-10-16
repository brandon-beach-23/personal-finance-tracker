import {Component, inject, OnInit, signal} from '@angular/core';
import {TransactionService} from "../../transaction.service";
import {TransactionResponse} from "../../models/transaction-response.model";
import {filter, switchMap} from "rxjs";
import {CommonModule, CurrencyPipe, DatePipe} from "@angular/common";

@Component({
  selector: 'app-transaction-list',
  standalone: true,
  imports: [
    CurrencyPipe,
    DatePipe,
    CommonModule
  ],
  templateUrl: './transaction-list.component.html',
  styleUrl: './transaction-list.component.css'
})
export class TransactionListComponent implements OnInit{
  public transactionService = inject(TransactionService);

  public transactions = signal<TransactionResponse[]>([]);

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


}
