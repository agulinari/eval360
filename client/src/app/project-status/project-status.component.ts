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

@Component({
  selector: 'app-project-status',
  templateUrl: './project-status.component.html',
  styleUrls: ['./project-status.component.css']
})
export class ProjectStatusComponent implements OnInit, OnDestroy {

  sub: Subscription;
  project: Project = undefined;
  evaluees: EvalueeStatus[] = [];
  feedbackProviders: FeedbackProviderStatus[] = [];
  loading = false;

  constructor( private route: ActivatedRoute,
    private router: Router,
    public dialog: MatDialog,
    private userService: UserService,
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
        this.project = project;
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
      feedbackProviders: []
    };

    evaluee.feedbackProviders.forEach( fp => {

      const createFeedbackProvider: CreateFeedbackProvider = {
        id: null,
        idUser: fp.user.id as number,
        relationship: fp.relationship as string
      };
      createEvaluee.feedbackProviders.push(createFeedbackProvider);
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
