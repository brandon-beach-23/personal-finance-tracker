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

    console.log('AuthGuard checking route:', state.url);
    if(this.authService.isLoggedIn()) {
      console.log('AuthGuard: access granted');
      return true;
    } else {
      console.warn('Route access denied. Redirecting to login.');
      return this.router.createUrlTree(['/login']);
    }
  }
}
