import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from './shared/authentication.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'EVAL 360';
  version = '1.0.0';

  constructor(private router: Router, private authenticationService: AuthenticationService) {
  }

  async ngOnInit() {

  }

  logout() {
    this.authenticationService.logout();
    this.router.navigate(['login']);
  }
}
