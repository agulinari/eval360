import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { UserListComponent } from './user-list/user-list.component';
import { RoleGuardService } from './role-guard.service';
import { UserEditComponent } from './user-edit/user-edit.component';
import { TemplateEditComponent } from './template-edit/template-edit.component';
import { TemplateListComponent } from './template-list/template-list.component';
import { MainComponent } from './main/main.component';


const appRoutes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  {
    path: 'main',
    component: MainComponent,
    canActivate: [RoleGuardService],
    data: {
          expectedRole: 'ROLE_ADMIN'
    },
    children: [
      { path: '', redirectTo: 'home', pathMatch: 'full'},
      { path: 'home', component: HomeComponent },
      {
        path: 'user-list',
        component: UserListComponent,
        canActivate: [RoleGuardService],
        data: {
          expectedRole: 'ROLE_ADMIN'
        }
      },
      {
        path: 'user-add',
        component: UserEditComponent,
        canActivate: [RoleGuardService],
        data: {
          expectedRole: 'ROLE_ADMIN'
        }
      },
      {
        path: 'user-edit/:id',
        component: UserEditComponent,
        canActivate: [RoleGuardService],
        data: {
          expectedRole: 'ROLE_ADMIN'
        }
      },
      {
        path: 'template-edit/:id',
        component: TemplateEditComponent,
        canActivate: [RoleGuardService],
        data: {
          expectedRole: 'ROLE_ADMIN'
        }
      },
      {
        path: 'template-list',
        component: TemplateListComponent,
        canActivate: [RoleGuardService],
        data: {
          expectedRole: 'ROLE_ADMIN'
        }
      }
    ]
  },
];

@NgModule({
  declarations: [],
  exports: [ RouterModule ],
  imports: [
    CommonModule,
    RouterModule.forRoot(
      appRoutes,
      { enableTracing: true }
    )
  ]
})
export class AppRoutingModule { }
