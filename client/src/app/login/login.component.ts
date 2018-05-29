import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { AuthenticationService } from '../shared/authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  loading = false;
  error = '';

  constructor(
      private fb: FormBuilder,
      private router: Router,
      private authenticationService: AuthenticationService) {
        this.createForm();
    }

    createForm() {
        this.loginForm = this.fb.group({
          username: ['',  Validators.compose([Validators.required, Validators.maxLength(30)])],
          password: ['', Validators.compose([Validators.required, Validators.maxLength(30)])]
      });
    }

  ngOnInit() {
      // reset login status
      this.authenticationService.logout();
  }

  login() {
      this.loading = true;
      const formModel = this.loginForm.value;
      this.authenticationService.login(formModel.username, formModel.password)
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
