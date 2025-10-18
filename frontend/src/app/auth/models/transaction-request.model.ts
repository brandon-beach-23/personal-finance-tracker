export interface TransactionRequest {
  transactionName: string;
  transactionAmount: number;
  categoryId: number | null;
  transactionType: 'DEBIT' | 'CREDIT';
  accountId: number | null;
  transactionDate: string;
}
