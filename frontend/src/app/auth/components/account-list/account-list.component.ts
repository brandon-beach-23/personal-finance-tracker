import { Component, inject, OnInit, signal, WritableSignal, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import {map, Observable} from 'rxjs';

// Import the new modal component
import { AccountModalComponent } from '../account-modal/account-modal.component';

import { AccountService} from "../../account.service";
import { AccountResponse} from "../../models/account-response.model";
import {EditDeleteAccountModalComponent} from "../edit-delete-account-modal/edit-delete-account-modal.component";

@Component({
  selector: 'app-account-list',
  standalone: true,
  // Ensure CommonModule is here for *ngFor, and add the Modal Component
  imports: [CommonModule, AccountModalComponent, EditDeleteAccountModalComponent],
  templateUrl: './account-list.component.html',
  styleUrl: './account-list.component.css'
})
export class AccountListComponent implements OnInit {
  private accountService = inject(AccountService);

  // Expose the Observable stream directly to the template
  public checkingAccounts$!: Observable<AccountResponse[]>;

  // === State Management for UI/Modal ===
  // Signal to control the visibility of the Add/Edit form modal
// Assuming this is the parent component (e.g., AccountsComponent)
  isAddModalOpen = signal(false);
  isEditDeleteModalOpen = signal(false); // New dedicated signal

  openAddModal() {
    this.isAddModalOpen.set(true);
  }

  closeAddModal() {
    this.isAddModalOpen.set(false);
  }

  openEditDeleteMode() {
    this.isEditDeleteModalOpen.set(true); // Open the Edit/Delete modal
  }

  closeEditDeleteModal() {
    this.isEditDeleteModalOpen.set(false); // Close the Edit/Delete modal
    // You might also call a refresh function here: this.loadAccounts();
  }

  // Event emitter to send the selected account up to the parent dashboard
  @Output() accountSelected = new EventEmitter<AccountResponse>();

  ngOnInit(): void {
    // 1. Filter the account stream (or just assign the main stream)
    this.checkingAccounts$ = this.accountService.accounts$.pipe(
      map(allAccounts =>
        allAccounts.filter(account => account.accountType === 'CHECKING')
    )
    );

    // 2. Trigger the HTTP fetch operation.
    this.accountService.getAccounts().subscribe();
  }

  // Handles clicking an account in the list
  selectAccount(account: AccountResponse): void {
    console.log('Account selected:', account.accountName);
    this.accountSelected.emit(account); // Emit the event
  }

  // Add this method to your AccountListComponent class
  refreshAccounts(): void {
    // This calls the service, which performs the GET request,
    // and then updates the BehaviorSubject (accounts$) that your template is subscribed to.
    this.accountService.getAccounts().subscribe();
    console.log('Account list refresh triggered.');
  }
}
