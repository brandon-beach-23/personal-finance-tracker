import {Component, OnInit} from '@angular/core';
import {CurrencyPipe, DatePipe} from "@angular/common";
import {FormBuilder, FormGroup} from "@angular/forms";
import {TransactionResponse} from "../../../models/transaction-response.model";
import {TransactionService} from "../../../services/transaction.service";
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';


@Component({
  selector: 'app-spending-report',
  standalone: true,
  imports: [
    CurrencyPipe,
    DatePipe,
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './spending-report.component.html',
  styleUrl: './spending-report.component.css'
})
export class SpendingReportComponent implements OnInit{
  reportForm!: FormGroup;
  report: TransactionResponse[] = [];

  months = [
    { value: 1, label: 'January' }, { value: 2, label: 'February' }, { value: 3, label: 'March' },
    { value: 4, label: 'April' }, { value: 5, label: 'May' }, { value: 6, label: 'June' },
    { value: 7, label: 'July' }, { value: 8, label: 'August' }, { value: 9, label: 'September' },
    { value: 10, label: 'October' }, { value: 11, label: 'November' }, { value: 12, label: 'December' }
  ]

  years: number[] = [];

  constructor(private fb: FormBuilder, private transactionService: TransactionService) {
    this.reportForm = this.fb.group({
      month: [new Date().getMonth() + 1],
      year: [new Date().getFullYear()]
    });
  }

  ngOnInit(): void {
    const currentYear = new Date().getFullYear();
    this.years = Array.from({ length: 5 }, (_, i) => currentYear - i);
  }


  generateReport(): void {
    const {month, year} = this.reportForm.value;
    this.transactionService.getTransactionsByMonthAndYear(month, year).subscribe(txns => {
      this.report = txns;
    });
  }

  get totalSpending(): number {
    return this.report.reduce((sum, tx) => sum + tx.transactionAmount, 0);
  }

}
