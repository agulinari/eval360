import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { UserListComponent } from './user-list/user-list.component';
import { StatisticsComponent } from './statistics/statistics.component';
import { RoleGuardService } from './role-guard.service';
import { UserEditComponent } from './user-edit/user-edit.component';
import { TemplateEditComponent } from './template-edit/template-edit.component';
import { TemplateListComponent } from './template-list/template-list.component';
import { MainComponent } from './main/main.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { ProjectListComponent } from './project-list/project-list.component';
import { ProjectCreateComponent } from './project-create/project-create.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ProjectStatusComponent } from './project-status/project-status.component';
import { EvaluationComponent } from './evaluation/evaluation.component';
import { EvaluationListComponent } from './evaluation-list/evaluation-list.component';
import { StatsActiveProjectsComponent } from './stats-active-projects/stats-active-projects.component';


const appRoutes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  {
    path: 'main',
    component: MainComponent,
    canActivate: [RoleGuardService],
    data: {
          expectedRoles: ['ROLE_ADMIN', 'ROLE_USER']
    },
    children: [
      { path: '', redirectTo: 'home', pathMatch: 'full'},
      { path: 'home', component: DashboardComponent },
      {
        path: 'user-list',
        component: UserListComponent,
        canActivate: [RoleGuardService],
        data: {
          expectedRoles: ['ROLE_ADMIN']
        }
      },
      {
        path: 'user-edit/:id',
        component: UserEditComponent,
        canActivate: [RoleGuardService],
        data: {
          expectedRoles: ['ROLE_ADMIN']
        }
      },
      {
        path: 'user-profile/:id',
        component: UserProfileComponent,
        canActivate: [RoleGuardService],
        data: {
          expectedRoles: ['ROLE_ADMIN']
        }
      },
      {
        path: 'user-new',
        component: UserEditComponent,
        canActivate: [RoleGuardService],
        data: {
          expectedRoles: ['ROLE_ADMIN']
        }
      },
      {
        path: 'template-edit/:id',
        component: TemplateEditComponent,
        canActivate: [RoleGuardService],
        data: {
          expectedRoles: ['ROLE_ADMIN']
        }
      },
      {
        path: 'template-list',
        component: TemplateListComponent,
        canActivate: [RoleGuardService],
        data: {
          expectedRoles: ['ROLE_ADMIN']
        }
      },
      {
        path: 'template-new',
        component: TemplateEditComponent,
        canActivate: [RoleGuardService],
        data: {
          expectedRoles: ['ROLE_ADMIN']
        }
      },
      {
        path: 'project-list',
        component: ProjectListComponent,
        canActivate: [RoleGuardService],
        data: {
          expectedRoles: ['ROLE_USER']
        }
      },
      {
        path: 'project-create',
        component: ProjectCreateComponent,
        canActivate: [RoleGuardService],
        data: {
          expectedRoles: ['ROLE_USER']
        }
      },
      {
        path: 'project-status/:id',
        component: ProjectStatusComponent,
        canActivate: [RoleGuardService],
        data: {
          expectedRoles: ['ROLE_USER']
        }
      },
      {
        path: 'project-tasks/:id',
        component: EvaluationListComponent,
        canActivate: [RoleGuardService],
        data: {
          expectedRoles: ['ROLE_USER']
        }
      },
      {
        path: 'project-tasks/:idProject/evaluation/:idEvaluee',
        component: EvaluationComponent,
        canActivate: [RoleGuardService],
        data: {
          expectedRoles: ['ROLE_USER']
        }
      },
      {
        path: 'statistics',
        component: StatsActiveProjectsComponent,
        canActivate: [RoleGuardService],
        data: {
          expectedRoles: ['ROLE_ADMIN']
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
