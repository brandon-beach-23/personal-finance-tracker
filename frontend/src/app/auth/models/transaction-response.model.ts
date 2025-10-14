export interface TransactionResponse {
  transactionId: number;
  transactionName: string;
  transactionAmount: number;
  transactionCategory: string;
  transactionType: 'DEBIT' | 'CREDIT';
  accountId: number;
  transactionDate: string;
}
