import { Component } from '@angular/core';
import {AuthService} from "../auth/auth.service";
import {Router} from "@angular/router";
import {AccountListComponent} from "../auth/components/account-list/account-list.component";
import {AccountModalComponent} from "../auth/components/account-modal/account-modal.component";

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    AccountListComponent,
    AccountModalComponent
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {

  constructor(private authService: AuthService, private router: Router) { }

  logout(): void {
    this.authService.logout();
  }

  getCurrentUserName(): string | null {
    return this.authService.getCurrentUsername();
  }
}
