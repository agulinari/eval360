import { Injectable } from '@angular/core';
import { AuthenticationService } from './shared/authentication.service';
import { Router, ActivatedRouteSnapshot, CanActivate } from '@angular/router';

@Injectable()
export class RoleGuardService implements CanActivate {

  constructor(public authenticationService: AuthenticationService, public router: Router) { }

  canActivate(route: ActivatedRouteSnapshot): boolean {
    // this will be passed from the route config
    // on the data property
    const expectedRoles = route.data.expectedRoles;

    const roles = this.authenticationService.getRoles();
    const token = localStorage.getItem('token');

    if (
      !this.authenticationService.isAuthenticated() ||
      roles.filter(role => expectedRoles.includes(role)).length === 0
    ) {
      this.router.navigate(['/login']);
      return false;
    }
    return true;
  }

}
