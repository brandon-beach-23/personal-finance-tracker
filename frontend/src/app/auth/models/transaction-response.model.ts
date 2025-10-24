export interface TransactionResponse {
  transactionId: number;
  transactionName: string;
  transactionAmount: number;
  categoryName: string;
  transactionType: 'DEBIT' | 'CREDIT';
  accountId: number;
  transactionDate: string;
  accountName: string;
}
