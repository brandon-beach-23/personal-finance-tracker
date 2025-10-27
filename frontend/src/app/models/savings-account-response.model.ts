import {AccountResponse} from "./account-response.model";
import {SavingsGoalResponse} from "./savings-goal-response.model";

export interface SavingsAccountResponse extends AccountResponse{
  savingsGoalResponse?: SavingsGoalResponse;
}
