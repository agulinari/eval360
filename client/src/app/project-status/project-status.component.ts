import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription, Observable, forkJoin } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material';

@Component({
  selector: 'app-project-status',
  templateUrl: './project-status.component.html',
  styleUrls: ['./project-status.component.css']
})
export class ProjectStatusComponent implements OnInit, OnDestroy {
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
      console.log('Entro a project status con id'+id);
      if (id) {
        this.idProject = id;
        this.inicializarNavLinks();
      }
    });
  
  }

  inicializarNavLinks(){
    this.navLinks = [
      {
        label: 'Estado',
        link: '/main/project-status/'+ this.idProject +'/project-status-item/'+ this.idProject,
        index: 0
      }, {
        label: 'Estadisticas',
        link: '/main/project-status/'+this.idProject+'/statistics-list-item/'+ this.idProject,
        index: 1
      }
    ];
    
    this.router.events.subscribe((res) => {
       this.activeLinkIndex = this.navLinks.indexOf(this.navLinks.find(tab => tab.link === this.router.url));
    });

    this.gotoProjectStatusDefault();
  }

  gotoProjectStatusDefault() {
    this.router.navigate(['/main/project-status/'+ this.idProject +'/project-status-item/'+ this.idProject]);
  }

  ngOnDestroy() {
      this.sub.unsubscribe();
  }
}