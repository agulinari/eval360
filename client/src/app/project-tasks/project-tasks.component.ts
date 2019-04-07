import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-project-tasks',
  templateUrl: './project-tasks.component.html',
  styleUrls: ['./project-tasks.component.css']
})
export class ProjectTasksComponent implements OnInit, OnDestroy {

  sub: Subscription;
  navLinks: any[];
  activeLinkIndex = -1;
  idProject: number;

  constructor(private router: Router,
              private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.sub = this.route.params.subscribe(params => {
      const id = params['id'];
      if (id) {
        this.idProject = id;
        this.inicializarNavLinks();
      }
    });
  }

  inicializarNavLinks() {
    this.navLinks = [
      {
        label: 'Mis Evaluaciones',
        link: '/main/project-tasks/' + this.idProject + '/my-evaluations',
        index: 0
      }, {
        label: 'Mis Reportes',
        link: '/main/project-tasks/' + this.idProject + '/my-reports',
        index: 1
      }
    ];
    this.router.events.subscribe((res) => {
       this.activeLinkIndex = this.navLinks.indexOf(this.navLinks.find(tab => tab.link === this.router.url));
    });

    this.goToProjectTasksDefault();
  }

  goToProjectTasksDefault() {
    this.router.navigate(['/main/project-tasks/' + this.idProject + '/my-evaluations']);
  }

  ngOnDestroy() {
      this.sub.unsubscribe();
  }
}
