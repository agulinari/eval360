import { Injectable } from '@angular/core';
import { AuthenticationService } from './shared/authentication.service';
import { Router, ActivatedRouteSnapshot, CanActivate } from '@angular/router';

@Injectable()
export class RoleGuardService implements CanActivate {

  constructor(public authenticationService: AuthenticationService, public router: Router) { }

  canActivate(route: ActivatedRouteSnapshot): boolean {
    // this will be passed from the route config
    // on the data property
    const expectedRole = route.data.expectedRole;

    const roles = this.authenticationService.getRoles();
    const token = localStorage.getItem('token');

    if (
      !this.authenticationService.isAuthenticated() ||
      !roles.includes(expectedRole)
    ) {
      this.router.navigate(['login']);
      return false;
    }
    return true;
  }

}
