import {
  ActivatedRouteSnapshot,
  CanActivate,
  Router,
  RouterStateSnapshot, UrlTree
} from '@angular/router';
import {Injectable} from "@angular/core";
import {AuthService} from "./auth.service";
import {Observable} from "rxjs";


@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | UrlTree | Promise<boolean | UrlTree> | boolean | UrlTree{

    if(this.authService.isLoggedIn()) {
      return true;
    } else {
      console.warn('Route access denied. Redirecting to login.');
      return this.router.createUrlTree(['/login']);
    }
  }
}
