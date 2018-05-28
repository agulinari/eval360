import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { AuthenticationService } from '../shared/authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  username: string;
  password: string;
  loading = false;
  error = '';

  constructor(
      private router: Router,
      private authenticationService: AuthenticationService) { }

  ngOnInit() {
      // reset login status
      this.authenticationService.logout();
  }

  login() {
      this.loading = true;
      this.authenticationService.login(this.username, this.password)
          .subscribe(result => {
              if (result === true) {
                  // login successful
                  this.router.navigate(['home']);
              } else {
                  // login failed
                  this.error = 'Username or password is incorrect';
                  this.loading = false;
              }
          }, error => {
            this.loading = false;
            this.error = error;
          });
  }

}
