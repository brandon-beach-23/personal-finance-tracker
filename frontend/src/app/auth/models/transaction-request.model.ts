export interface TransactionRequest {
  transactionName: string;
  transactionAmount: number;
  transactionCategory: string;
  transactionType: 'DEBIT' | 'CREDIT';
  accountId: number | null;
  transactionDate: string;
}
