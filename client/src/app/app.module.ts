import 'hammerjs';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatButtonModule, MatCardModule, MatInputModule, MatListModule, MatToolbarModule,
  MatTabsModule, MatDialogModule, MatStepperModule } from '@angular/material';
import { MatProgressSpinnerModule, MatIconModule, MatMenuModule, MatGridListModule, MatSelectModule } from '@angular/material';
import { MatAutocompleteModule, MatChipsModule, MatSlideToggleModule, MatProgressBarModule, MatTooltipModule,
  MatSidenavModule, MatTableModule, MatPaginatorModule, MatExpansionModule, MatSortModule, MatRadioModule } from '@angular/material';
import { FlexModule } from '@angular/flex-layout';

import { HttpClientModule } from '@angular/common/http';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './shared/auth.interceptor';
import { UnauthorizedInterceptor } from './shared/unauth.interceptor';

import { AuthenticationService } from './shared/authentication.service';
import { UserService } from './shared/user.service';
import { RoleGuardService } from './role-guard.service';
import { TemplateService } from './shared/template.service';

import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { UserListComponent } from './user-list/user-list.component';
import { UserEditComponent } from './user-edit/user-edit.component';
import { TemplateEditComponent } from './template-edit/template-edit.component';
import { NavigationComponent } from './navigation/navigation.component';
import { LayoutModule } from '@angular/cdk/layout';
import { AppRoutingModule } from './app-routing.module';
import { MainComponent } from './main/main.component';
import { TemplateListComponent } from './template-list/template-list.component';
import { ErrorDialogComponent } from './error-dialog/error-dialog.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { ProjectListComponent } from './project-list/project-list.component';
import { ProjectService } from './shared/project.service';
import { ConfirmDialogComponent } from './dialog/confirm-dialog.component';
import { ProjectCreateComponent } from './project-create/project-create.component';
import { AddEvalueeDialogComponent } from './dialog/add-evaluee-dialog.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { AddAdminDialogComponent } from './dialog/add-admin-dialog.component';
import { ProjectStatusComponent } from './project-status/project-status.component';
import { EvaluationComponent } from './evaluation/evaluation.component';
import { EvaluationService } from './shared/evaluation.service';
import { EvaluationListComponent } from './evaluation-list/evaluation-list.component';
import { EvaluationPreviewComponent } from './evaluation-preview/evaluation-preview.component';
import { ReportService } from './shared/report.service';


@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    UserListComponent,
    UserEditComponent,
    TemplateEditComponent,
    NavigationComponent,
    MainComponent,
    ErrorDialogComponent,
    ConfirmDialogComponent,
    AddEvalueeDialogComponent,
    AddAdminDialogComponent,
    EvaluationPreviewComponent,
    TemplateListComponent,
    UserProfileComponent,
    ProjectListComponent,
    ProjectCreateComponent,
    DashboardComponent,
    ProjectStatusComponent,
    EvaluationComponent,
    EvaluationListComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatChipsModule,
    MatCardModule,
    MatIconModule,
    MatInputModule,
    MatSelectModule,
    MatGridListModule,
    MatAutocompleteModule,
    MatListModule,
    MatMenuModule,
    MatToolbarModule,
    MatSlideToggleModule,
    MatProgressSpinnerModule,
    LayoutModule,
    MatSidenavModule,
    AppRoutingModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatRadioModule,
    MatDialogModule,
    MatStepperModule,
    MatProgressBarModule,
    MatTabsModule,
    MatExpansionModule,
    MatTooltipModule,
    FlexModule
  ],
  providers: [
    UserService,
    AuthenticationService,
    RoleGuardService,
    TemplateService,
    ProjectService,
    EvaluationService,
    ReportService,
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: UnauthorizedInterceptor, multi: true}
  ],
  bootstrap: [AppComponent],
  entryComponents: [ErrorDialogComponent, ConfirmDialogComponent, AddEvalueeDialogComponent,
     AddAdminDialogComponent, EvaluationPreviewComponent]
})
export class AppModule { }
