import { Component, OnInit } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { AuthenticationService } from '../shared/authentication.service';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css'],
})
export class NavigationComponent implements OnInit {

  title = 'EVAL 360';
  version = '1.0.0';
  roles: String[];

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches)
    );

  constructor(private breakpointObserver: BreakpointObserver,
    private router: Router, private authenticationService: AuthenticationService) {}

  ngOnInit(): void {
    this.roles = this.authenticationService.getRoles();
  }

  logout() {
    this.authenticationService.logout();
    this.router.navigate(['login']);
  }

}
