import { Routes } from '@angular/router';
import {DashboardComponent} from "./dashboard/dashboard.component";
import {RegistrationComponent} from  './auth/components/registration/registration.component';
import {LoginComponent} from "./auth/components/login/login.component";
import {AuthGuard} from "./auth/auth.guard";

export const routes: Routes = [
  // Default path if logged in
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },

  // Public Routes
  { path: 'register', component: RegistrationComponent},
  { path: 'login', component: LoginComponent },

  // Protected Routes (Uses AuthGuard)
   { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] }



  // Route 5: Catch-all for 404
  // { path: '**', component: NotFoundComponent }

];
