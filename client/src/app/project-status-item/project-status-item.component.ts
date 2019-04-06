import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription, Observable, forkJoin } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material';
import { UserService } from '../shared/user.service';
import { Project } from '../domain/project/project';
import { ProjectService } from '../shared/project.service';
import { EvalueeStatus } from '../domain/project-status/evaluee-status';
import { FeedbackProviderStatus } from '../domain/project-status/feedback-provider-status';
import { Evaluee } from '../domain/project/evaluee';
import { FeedbackProvider } from '../domain/project/feedback-provider';
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
import { ProjectStatus } from '../domain/project-status/project-status';
import { WaitingDialogComponent } from '../dialog/waiting-dialog.component';
import { NotificationService } from '../shared/notification.service';
import {timeout} from 'rxjs/operators';
import { fillProperties } from '@angular/core/src/util/property';
import { EvalueeDetailDialogComponent } from '../dialog/evaluee-detail-dialog.component';
import { FpDetailDialogComponent } from '../dialog/fp-detail-dialog.component';
import { ReviewerDetailDialogComponent } from '../dialog/reviewer-detail-dialog.component';

@Component({
  selector: 'app-project-status-item',
  templateUrl: './project-status-item.component.html',
  styleUrls: ['./project-status-item.component.css']
})
export class ProjectStatusItemComponent implements OnInit, OnDestroy {

  sub: Subscription;
  projectStatus: ProjectStatus = undefined;
  recordarFeedback = false;
  loading = false;

  constructor( private route: ActivatedRoute,
    private router: Router,
    public dialog: MatDialog,
    private userService: UserService,
    private authenticationService: AuthenticationService,
    private projectService: ProjectService,
    private notificationService: NotificationService) { }

  ngOnInit() {

    this.sub = this.route.params.subscribe(params => {
      const id = params['id'];
      if (id) {
        this.getProjectStatus(id);
        console.log('valor project status init var', this.projectStatus);
      }
    });
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

  getProjectStatus(id) {
    this.loading = true;
    this.projectService.getStatus(id).subscribe((projectStatus: ProjectStatus) => {
      console.log('valor project status init', projectStatus);
      if (projectStatus) {

        this.projectStatus = projectStatus;

        // Chequear que el usuario sea admin del proyecto
        const userId = this.authenticationService.getUserId();
        const isAdmin = (projectStatus.adminsStatus.find(admin => admin.idUser === +userId) !== undefined);
        if (!isAdmin) {
          console.log('El usuario no es admin del proyecto, volviendo a la lista');
          this.gotoList();
        } else {
          this.getStatusRecordatory();
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

  getStatusRecordatory() {

    if (this.projectStatus) {
      const isFeedbacksIncomplete =
      (this.projectStatus.feedbackProvidersStatus.find(item => item.status.toUpperCase() === 'PENDIENTE') !== undefined);
      this.recordarFeedback = isFeedbacksIncomplete;
      this.projectStatus.feedbackProvidersStatus.map(fp => fp.reminder = this.recordarFeedback);
    }

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

    this.projectStatus.adminsStatus.forEach(admin => {
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

    this.projectService.addAdmin(this.projectStatus.id, createAdmin)
    .pipe(finalize(() => {
        this.loading = false;
        this.getProjectStatus(this.projectStatus.id);
      })
    )
    .subscribe(
      res => {
        console.log('Agregando admin al proyecto', res);
      },
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

    this.projectStatus.evalueesStatus.forEach(evaluee => {
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


    this.projectService.addEvaluee(this.projectStatus.id, createEvaluee)
    .pipe(finalize(() => {
        this.loading = false;
        this.getProjectStatus(this.projectStatus.id);
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

  notificateFeedback(feedbackProviderStatus: FeedbackProviderStatus) {
    const dialogRef: MatDialogRef<WaitingDialogComponent> = this.dialog.open(WaitingDialogComponent,  {
      panelClass: 'transparent',
      disableClose: true
    });

    this.notificationService.notificateToProviders(feedbackProviderStatus, this.projectStatus).pipe(
      timeout(60000)
    ).subscribe(
      res => {
          console.log('Notificando feedback pendiente a provider', res);
          feedbackProviderStatus.reminder = false;
          dialogRef.close();
      },
      err => {
          console.log('Error notificando feedback pendiente', err);
          dialogRef.close();
          this.showError('Se produjo un error al notificar al provider el feedback pendiente');
      }
    );
  }

  exportProject() {
    this.projectService.exportProject(this.projectStatus.id).subscribe(res => {
      console.log(res);
      const newBlob = new Blob([res], { type: 'application/vnd.ms-excel' });

      if (window.navigator && window.navigator.msSaveOrOpenBlob) {
        window.navigator.msSaveOrOpenBlob(newBlob);
        return;
    }
     // For other browsers:
            // Create a link pointing to the ObjectURL containing the blob.
            const data = window.URL.createObjectURL(newBlob);

            const link = document.createElement('a');
            link.href = data;
            link.download = 'project.xlsx';
            // this is necessary as link.click() does not work on the latest firefox
            link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));

            setTimeout(function () {
                // For Firefox it is necessary to delay revoking the ObjectURL
                window.URL.revokeObjectURL(data);
            }, 100);

    }, error => {
      console.log(error);
    });
  }

  onEvalueeClick(evaluee: EvalueeStatus): void {
    this.dialog.open(EvalueeDetailDialogComponent, {
      data: evaluee, width: '500px'
    });
  }

  onFpClick(fp: FeedbackProviderStatus): void {
    this.dialog.open(FpDetailDialogComponent, {
      data: fp, width: '500px'
    });
  }

  onReviewerClick(reviewer: ReviewerStatus): void {
    this.dialog.open(ReviewerDetailDialogComponent, {
      data: reviewer, width: '500px'
    });
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
