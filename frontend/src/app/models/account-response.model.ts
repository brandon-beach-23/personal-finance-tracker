export interface AccountResponse {
  accountId: number;
  accountName: string;
  balance: number;
  accountType: 'CHECKING' | 'SAVINGS';
}
