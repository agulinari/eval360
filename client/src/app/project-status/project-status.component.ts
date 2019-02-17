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

@Component({
  selector: 'app-project-status',
  templateUrl: './project-status.component.html',
  styleUrls: ['./project-status.component.css']
})
export class ProjectStatusComponent implements OnInit, OnDestroy {

  sub: Subscription;
  projectStatus: ProjectStatus = undefined;
  recordarEvaluacion: boolean = false;
  recordarFeedback: boolean = false;
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
        console.log('valor project status init var',this.projectStatus);
        
      }
    });
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

  getProjectStatus(id) {
    this.loading = true;
    this.projectService.getStatus(id).subscribe((projectStatus: ProjectStatus) => {
      console.log('valor project status init',projectStatus);
      if (projectStatus) {
        
        this.projectStatus = projectStatus;

        // Chequear que el usuario sea admin del proyecto
        const userId = this.authenticationService.getUserId();
        const isAdmin = (projectStatus.adminsStatus.find(admin => admin.idUser === +userId) !== undefined);
        if (!isAdmin) {
          console.log('El usuario no es admin del proyecto, volviendo a la lista');
          this.gotoList();
        }else{
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

  getStatusRecordatory(){
    
    if(this.projectStatus){
      const isEvaluationIncomplete = (this.projectStatus.evalueesStatus.find(item => item.status.toUpperCase() === 'PENDIENTE') !== undefined);
      const isFeedbacksIncomplete = (this.projectStatus.feedbackProvidersStatus.find(item => item.status.toUpperCase() === 'PENDIENTE') !== undefined);
    
      this.recordarEvaluacion = isEvaluationIncomplete;
      this.recordarFeedback = isFeedbacksIncomplete;
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

  notificateFeedback(feedbackProviderStatus:FeedbackProviderStatus){
    const dialogRef: MatDialogRef<WaitingDialogComponent> = this.dialog.open(WaitingDialogComponent,  {
      panelClass: 'transparent',
      disableClose: true
    });

    this.notificationService.notificateToProviders(feedbackProviderStatus,this.projectStatus).subscribe(
      res => {
          console.log('Notificando feedback pendiente a provider',res);
          dialogRef.close();
      },
      err => {
          console.log('Error notificando feedback pendiente', err);
          dialogRef.close();
          this.showError('Se produjo un error al notificar al provider el feedback pendiente');
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
