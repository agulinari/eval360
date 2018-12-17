import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription, Observable, forkJoin } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog, MatDialogConfig } from '@angular/material';
import { UserService } from '../shared/user.service';
import { Project } from '../domain/project/project';
import { ProjectService } from '../shared/project.service';
import { EvalueeStatus } from '../domain/project-status/evaluee-status';
import { FeedbackProviderStatus } from '../domain/project-status/feedback-provider-status';
import { Evaluee } from '../domain/project/evaluee';
import { AddEvalueeDialogComponent } from '../dialog/add-evaluee-dialog.component';
import { ErrorDialogComponent } from '../error-dialog/error-dialog.component';
import { CreateEvaluee } from '../domain/create-project/create-evaluee';
import { CreateFeedbackProvider } from '../domain/create-project/create-feedback-provider';
import { finalize } from 'rxjs/operators';
import { AddAdminDialogComponent } from '../dialog/add-admin-dialog.component';
import { CreateAdmin } from '../domain/create-project/create-admin';
import { ProjectAdmin } from '../domain/project/project-admin';
import { AdminStatus } from '../domain/project-status/admin-status';
import { AuthenticationService } from '../shared/authentication.service';
import { ReviewerStatus } from '../domain/project-status/reviewer-status';
import { CreateReviewer } from '../domain/create-project/create-reviewer';

@Component({
  selector: 'app-project-status',
  templateUrl: './project-status.component.html',
  styleUrls: ['./project-status.component.css']
})
export class ProjectStatusComponent implements OnInit, OnDestroy {

  sub: Subscription;
  project: Project = undefined;
  evaluees: EvalueeStatus[] = [];
  admins: AdminStatus[] = [];
  feedbackProviders: FeedbackProviderStatus[] = [];
  reviewers: ReviewerStatus[] = [];
  loading = false;

  constructor( private route: ActivatedRoute,
    private router: Router,
    public dialog: MatDialog,
    private userService: UserService,
    private authenticationService: AuthenticationService,
    private projectService: ProjectService) { }

  ngOnInit() {

    this.sub = this.route.params.subscribe(params => {
      const id = params['id'];
      if (id) {
        this.getProject(id);
      }
    });
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

  getProject(id) {
    this.loading = true;
    this.evaluees = [];
    this.feedbackProviders = [];
    this.reviewers = [];
    this.admins = [];
    this.projectService.get(id).subscribe((project: Project) => {

      if (project) {
        project.evaluees.forEach(evaluee => {
          const user = this.userService.get(evaluee.idUser.toString());
          const evalueeStatus: EvalueeStatus = {
              id: evaluee.id,
              idUser: evaluee.idUser,
              user: user,
              status: 'Pendiente',
              feedbackRequests: evaluee.feedbackProviders.length,
              feedbackResponses: 0,
              progress: 0
          };

          let responses = 0;
          evaluee.feedbackProviders.forEach(fp => {
            if (fp.status !== 'PENDIENTE') {
              responses++;
            }
          });
          evalueeStatus.feedbackResponses = responses;
          evalueeStatus.progress = responses * 100 / evalueeStatus.feedbackRequests;
          this.evaluees.push(evalueeStatus);
        });
        project.feedbackProviders.forEach(fp => {
          const user = this.userService.get(fp.idUser.toString());
          const fpStatus: FeedbackProviderStatus = {
            id: fp.id,
            idUser: fp.idUser,
            user: user,
            status: 'Pendiente',
            feedbacksPending: 0,
            feedbacksCompleted: 0,
            progress: 0
          };
          let pending = 0;
          let completed = 0;
          project.evaluees.forEach(evaluee => {
            evaluee.feedbackProviders.forEach(efp => {
              if (efp.feedbackProvider.id === fp.id) {
                (efp.status === 'PENDIENTE') ? pending++ : completed++;
              }
            });
          });
          fpStatus.feedbacksPending = pending;
          fpStatus.feedbacksCompleted = completed;
          fpStatus.progress = (completed * 100) / (pending + completed);
          this.feedbackProviders.push(fpStatus);
        });

        project.reviewers.forEach(reviewer => {
          const user = this.userService.get(reviewer.idUser.toString());
          const reviewerStatus: ReviewerStatus = {
            id: reviewer.id,
            idUser: reviewer.idUser,
            user: user,
            feedbacksAvailable: 0
          };

          this.reviewers.push(reviewerStatus);
        });

        project.projectAdmins.forEach(admin => {
          const user = this.userService.get(admin.idUser.toString());
          const adminStatus: AdminStatus = {
            id: admin.id,
            idUser: admin.idUser,
            user: user,
            creator: admin.creator
          };
          this.admins.push(adminStatus);
        });
        this.project = project;

        // Chequear que el usuario sea admin del proyecto
        const userId = this.authenticationService.getUserId();
        const isAdmin = (project.projectAdmins.find(admin => admin.idUser === +userId) !== undefined);
        if (!isAdmin) {
          console.log('El usuario no es admin del proyecto, volviendo a la lista');
          this.gotoList();
        }
      } else {
        console.log(`Proyecto con id '${id}' no encontrado, volviendo a la lista`);
        this.gotoList();
      }
    },
    err => {
      console.log('Error obteniendo proyecto', err);
      this.showError('Se produjo un error al agregar obtener al proyecto');
      this.gotoList();
    },
    () => {
      this.loading = false;
    });
  }


  openAddAdminDialog() {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.minWidth = '400px';
    dialogConfig.data = this.getAdmins();
    const dialogRef = this.dialog.open(AddAdminDialogComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      data => {
        if (data) {
          this.addAdmin(data);
        }
      }
    );
  }

  getAdmins(): number[] {
    const users: number[] = [];

    this.admins.forEach(admin => {
      users.push(admin.idUser);
    });
    return users;
  }

  addAdmin(admin: ProjectAdmin): void {
    this.loading = true;
    const createAdmin: CreateAdmin = {
      id: null,
      idUser: admin.user.id as number,
      creator: false
    };

    this.projectService.addAdmin(this.project.id, createAdmin)
    .pipe(finalize(() => {
        this.loading = false;
        this.getProject(this.project.id);
      })
    )
    .subscribe(
      res => console.log('Agregando admin al proyecto', res),
      err => {
        console.log('Error agregando admin al proyecto', err);
        this.showError('Se produjo un error al agregar admin al proyecto');
      }
    );
  }


  openAddEvalueeDialog() {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.minWidth = '400px';
    dialogConfig.data = this.getEvaluees();
    const dialogRef = this.dialog.open(AddEvalueeDialogComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      data => {
        if (data) {
          this.addEvaluee(data);
        }
      }
    );
  }

  getEvaluees(): number[] {
    const users: number[] = [];

    this.evaluees.forEach(evaluee => {
      users.push(evaluee.idUser);
    });
    return users;
  }

  addEvaluee(evaluee: Evaluee): void {
    this.loading = true;
    const createEvaluee: CreateEvaluee = {
      id: null,
      idUser: evaluee.user.id as number,
      feedbackProviders: [],
      reviewers: []
    };

    evaluee.feedbackProviders.forEach( fp => {

      const createFeedbackProvider: CreateFeedbackProvider = {
        id: null,
        idUser: fp.user.id as number,
        relationship: fp.relationship as string
      };
      createEvaluee.feedbackProviders.push(createFeedbackProvider);
    });

    evaluee.reviewers.forEach( reviewer => {

      const createReviewer: CreateReviewer = {
        id: null,
        idUser: reviewer.user.id as number
      };
      createEvaluee.reviewers.push(createReviewer);
    });


    this.projectService.addEvaluee(this.project.id, createEvaluee)
    .pipe(finalize(() => {
        this.loading = false;
        this.getProject(this.project.id);
      })
    )
    .subscribe(
      res => console.log('Agregando evaluado al proyecto', res),
      err => {
        console.log('Error agregando evaluado al proyecto', err);
        this.showError('Se produjo un error al agregar evaluado al proyecto');
      }
    );
  }

  showError(error: string): void {
    this.dialog.open(ErrorDialogComponent, {
      data: {errorMsg: error}, width: '250px'
    });
  }

  gotoList() {
    this.router.navigate(['/main/project-list']);
  }

}
