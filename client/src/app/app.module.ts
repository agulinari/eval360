import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { EmployeeService } from './shared/employee.service';
import { GiphyService} from './shared/giphy.service';
import { HttpClientModule } from '@angular/common/http';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './shared/auth.interceptor';

import { AppComponent } from './app.component';
import { EmployeeListComponent } from './employee-list/employee-list.component';
import { MatButtonModule, MatCardModule, MatInputModule, MatListModule, MatToolbarModule } from '@angular/material';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { OktaCallbackComponent, OktaAuthModule } from '@okta/okta-angular';
import { RouterModule, Routes } from '@angular/router';
import { EmployeeEditComponent } from './employee-edit/employee-edit.component';

const config = {
  issuer: 'https://dev-432925.oktapreview.com/oauth2/default',
  redirectUri: 'http://localhost:4200/implicit/callback',
  clientId: '0oaf3f70pf9GJLgNC0h7'
};

const appRoutes: Routes = [
  { path: '', redirectTo: '/employee-list', pathMatch: 'full' },
  { path: 'employee-list', component: EmployeeListComponent },
  { path: 'employee-add', component: EmployeeEditComponent },
  { path: 'employee-edit/:id', component: EmployeeEditComponent },
  {
    path: 'implicit/callback',
    component: OktaCallbackComponent
  }
];

@NgModule({
  declarations: [
    AppComponent,
    EmployeeListComponent,
    EmployeeEditComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatCardModule,
    MatInputModule,
    MatListModule,
    MatToolbarModule,
    RouterModule.forRoot(
      appRoutes,
      { enableTracing: true }
    ),
    OktaAuthModule.initAuth(config)
  ],
  providers: [EmployeeService, GiphyService, {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true}],
  bootstrap: [AppComponent]
})
export class AppModule { }
