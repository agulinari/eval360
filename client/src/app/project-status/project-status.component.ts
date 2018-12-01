import { Component, OnInit } from '@angular/core';
import { Subscription, Observable, forkJoin } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material';
import { UserService } from '../shared/user.service';
import { Project } from '../domain/project';
import { ProjectService } from '../shared/project.service';
import { User } from '../domain/user';

@Component({
  selector: 'app-project-status',
  templateUrl: './project-status.component.html',
  styleUrls: ['./project-status.component.css']
})
export class ProjectStatusComponent implements OnInit {

  sub: Subscription;
  project: Project = undefined;
  users: Observable<User>[] = [];

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
          evaluee.user = this.userService.get(evaluee.idUser.toString());
        });
        project.feedbackProviders.forEach(fp => {
          fp.user = this.userService.get(fp.idUser.toString());
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
