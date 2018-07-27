import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from '../shared/authentication.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  private roles: String[];

  constructor(private authenticationService: AuthenticationService) { }

  ngOnInit() {
    this.roles = this.authenticationService.getRoles();
  }

}
