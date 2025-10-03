import { Routes } from '@angular/router';
import {DashboardComponent} from "./dashboard/dashboard.component";
import {RegistrationComponent} from "./registration/registration.component";

export const routes: Routes = [
  // Route 1: Default path redirects to the dashboard
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },

  // Route 2: Dashboard
   { path: 'dashboard', component: DashboardComponent },

  //Route 3: User Registration
  { path: 'registration', component: RegistrationComponent}

  // Route 3: Transactions List
  // { path: 'transactions', component: TransactionListComponent },

  // Route 4: User Login
  // { path: 'login', component: LoginComponent },

  // Route 5: Catch-all for 404
  // { path: '**', component: NotFoundComponent }

];
