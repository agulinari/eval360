import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { UserListComponent } from './user-list/user-list.component';
import { StatisticsComponent } from './statistics/statistics.component';
import { StatisticsListItemComponent } from './statistics-list-item/statistics-list-item.component';
import { RoleGuardService } from './role-guard.service';
import { UserEditComponent } from './user-edit/user-edit.component';
import { TemplateEditComponent } from './template-edit/template-edit.component';
import { TemplateListComponent } from './template-list/template-list.component';
import { MainComponent } from './main/main.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { ProjectListComponent } from './project-list/project-list.component';
import { ProjectStatusComponent } from './project-status/project-status.component';
import { ProjectCreateComponent } from './project-create/project-create.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ProjectStatusItemComponent } from './project-status-item/project-status-item.component';
import { EvaluationComponent } from './evaluation/evaluation.component';
import { EvaluationListComponent } from './evaluation-list/evaluation-list.component';
import { StatsActiveProjectsComponent } from './stats-active-projects/stats-active-projects.component';
import { StatisticsGeneralComponent } from './statistics-general/statistics-general.component';
import { ReportListComponent } from './report-list/report-list.component';
import { ProjectTasksComponent } from './project-tasks/project-tasks.component';


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
        path: 'project-tasks/:idProject/evaluation/:idEvaluee',
        component: EvaluationComponent,
        canActivate: [RoleGuardService],
        data: {
          expectedRoles: ['ROLE_USER']
        }
      },
       {
        path: 'project/:id/template/:idEvTemp',
        component: ProjectStatusComponent,
        canActivate: [RoleGuardService],
        data: {
          expectedRoles: ['ROLE_USER']
        },
        children: [
          {
            path: 'project-status-item/:id',
            component: ProjectStatusItemComponent,
            canActivate: [RoleGuardService],
            data: {
              expectedRoles: ['ROLE_USER']
            }
          },
          {
            path: 'statistics-list-item/:idProject/template/:idTemplate',
            component: StatisticsListItemComponent,
            canActivate: [RoleGuardService],
            data: {
              expectedRoles: ['ROLE_USER']
            }
          }
        ]
      },
      {
        path: 'project-tasks/:id',
        component: ProjectTasksComponent,
        canActivate: [RoleGuardService],
        data: {
          expectedRoles: ['ROLE_USER']
        },
        children: [
          {
            path: 'my-evaluations',
            component: EvaluationListComponent,
            canActivate: [RoleGuardService],
            data: {
              expectedRoles: ['ROLE_USER']
            }
          },
          {
            path: 'my-reports',
            component: ReportListComponent,
            canActivate: [RoleGuardService],
            data: {
              expectedRoles: ['ROLE_USER']
            }
          }
        ]
      },
      {
        path: 'statistics',
        component: StatisticsGeneralComponent,
        canActivate: [RoleGuardService],
        data: {
          expectedRoles: ['ROLE_ADMIN']
        },
        children: [
          {
            path: '',
            pathMatch: 'full',
            redirectTo: 'user-history'
          },
          {
            path: 'user-history',
            component: StatisticsComponent,
            canActivate: [RoleGuardService],
            data: {
              expectedRoles: ['ROLE_ADMIN']
            }
          },
          {
            path: 'active-projects',
            component: StatsActiveProjectsComponent,
            canActivate: [RoleGuardService],
            data: {
              expectedRoles: ['ROLE_ADMIN']
            }
          }
        ]
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
