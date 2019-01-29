import { Component, OnInit, OnDestroy } from '@angular/core';
import { ErrorDialogComponent } from '../error-dialog/error-dialog.component';
import { Subscription } from 'rxjs';
import { Project } from '../domain/project/project';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material';
import { UserService } from '../shared/user.service';
import { AuthenticationService } from '../shared/authentication.service';
import { ProjectService } from '../shared/project.service';
import { EvalueeStatus } from '../domain/project-status/evaluee-status';
import { EvalueeItem } from '../domain/evaluation-list/Evaluee-Item';
import { EvalueeFeedbackProvider } from '../domain/project-status/evaluee-feedback-provider';

@Component({
  selector: 'app-evaluation-list',
  templateUrl: './evaluation-list.component.html',
  styleUrls: ['./evaluation-list.component.css']
})
export class EvaluationListComponent implements OnInit, OnDestroy {

  sub: Subscription;
  project: Project = undefined;
  pendingEvaluees: EvalueeItem[] = [];
  completedEvaluees: EvalueeItem[] = [];
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
    this.pendingEvaluees = [];
    this.completedEvaluees = [];
    this.projectService.get(id).subscribe((project: Project) => {

      if (project) {

        // Chequear que el usuario sea admin del proyecto
        const userId = this.authenticationService.getUserId();
        const isFeedbackProvider = (project.feedbackProviders.find(fp => fp.idUser === +userId) !== undefined);
        const isEvaluee = (project.evaluees.find(evaluee => evaluee.idUser === +userId) !== undefined);

        if ((!isFeedbackProvider) && (!isEvaluee)) {
          console.log('El usuario no es participante del proyecto, volviendo a la lista');
          this.gotoList();
        }

        project.evaluees.forEach(evaluee => {
          const evalFeedbackProvider = evaluee.feedbackProviders.find(efp => efp.feedbackProvider.idUser === +userId);
          if (evalFeedbackProvider !== undefined && evalFeedbackProvider.status === 'PENDIENTE') {
            const user = this.userService.get(evaluee.idUser.toString());
            const evalueeItem: EvalueeItem = {
              id: evalFeedbackProvider.id,
              idUser: evaluee.idUser,
              user: user,
              relationship: evalFeedbackProvider.relationship
            };
            this.pendingEvaluees.push(evalueeItem);
          }
        });

        this.project = project;

       } else {
        console.log(`Proyecto con id '${id}' no encontrado, volviendo a la lista`);
        this.gotoList();
      }
    },
    err => {
      console.log('Error obteniendo proyecto', err);
      this.showError('Se produjo un error al  obtener al proyecto');
      this.gotoList();
    },
    () => {
      this.loading = false;
    });
  }

  showError(error: string): void {
    this.dialog.open(ErrorDialogComponent, {
      data: {errorMsg: error}, width: '250px'
    });
  }

  goToEvaluationForm(evalueeId) {

    this.router.navigate([`/main/project-tasks/${this.project.id}/evaluation/${evalueeId}`]);
  }

  gotoList() {
    this.router.navigate(['/main/project-list']);
  }

}
