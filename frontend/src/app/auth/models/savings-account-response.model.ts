import {AccountResponse} from "./account-response.model";

export interface SavingsAccountResponse extends AccountResponse{
  savingsGoalResponse?: SavingsAccountResponse;
}
