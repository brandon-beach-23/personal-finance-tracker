import { Routes } from '@angular/router';
import {DashboardComponent} from "./dashboard/dashboard.component";
import {RegistrationComponent} from  './auth/components/registration/registration.component';
import {LoginComponent} from "./auth/components/login/login.component";
import {AuthGuard} from "./auth/auth.guard";
import {SpendingReportComponent} from "./auth/components/spending-report/spending-report.component";

export const routes: Routes = [
  // Default path if logged in
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },

  // Public Routes
  { path: 'register', component: RegistrationComponent},
  { path: 'login', component: LoginComponent },

  // Protected Routes (Uses AuthGuard)
   { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },
   { path: 'report', component: SpendingReportComponent, canActivate: [AuthGuard] }

];
