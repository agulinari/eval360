import { Component, OnInit } from '@angular/core';
import { Subscription, Observable, forkJoin } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material';
import { UserService } from '../shared/user.service';
import { Project } from '../domain/project';
import { ProjectService } from '../shared/project.service';
import { User } from '../domain/user';
import { EvalueeStatus } from '../domain/project-status/evaluee-status';
import { FeedbackProviderStatus } from '../domain/project-status/feedback-provider-status';

@Component({
  selector: 'app-project-status',
  templateUrl: './project-status.component.html',
  styleUrls: ['./project-status.component.css']
})
export class ProjectStatusComponent implements OnInit {

  sub: Subscription;
  project: Project = undefined;
  evaluees: EvalueeStatus[] = [];
  feedbackProviders: FeedbackProviderStatus[] = [];

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

  getProject(id) {
    this.projectService.get(id).subscribe((project: Project) => {

      if (project) {
        project.evaluees.forEach(evaluee => {
          const user = this.userService.get(evaluee.idUser.toString());
          const evalueeStatus: EvalueeStatus = {
              id: evaluee.id,
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
    });
  }


  gotoList() {
    this.router.navigate(['/main/project-list']);
  }

}
