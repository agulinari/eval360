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
  idTemplate: number;

  constructor(private router: Router,
              private route: ActivatedRoute) {
  
  }

  ngOnInit() {
   
    this.sub = this.route.params.subscribe(params => {
      const id = params['id'];
      const idTemp = params['idEvTemp'];
      console.log('idTemplate: '+idTemp);
      if (id) {
        this.idProject = id;
        this.idTemplate = idTemp;
        this.inicializarNavLinks();
      }
    });
  
  }

  inicializarNavLinks(){
    
    this.navLinks = [
      {
        label: 'Estado',
        link: '/main/project/'+ this.idProject+'/template/'+this.idTemplate +
              '/project-status-item/'+this.idProject,
        index: 0
      }, {
        label: 'Estadisticas',
        link: '/main/project/'+ this.idProject +'/template/'+this.idTemplate + '/statistics-list-item/'+this.idProject+
              '/template/'+ this.idTemplate,
        index: 1
      }
    ];
  
    this.router.events.subscribe((res) => {
       this.activeLinkIndex = this.navLinks.indexOf(this.navLinks.find(tab => tab.link === this.router.url));
    });

    this.gotoProjectStatusDefault();
  }

  gotoProjectStatusDefault() {
    this.router.navigate(['/main/project/'+ this.idProject+'/template/'+this.idTemplate +
                          '/project-status-item/'+this.idProject]);
  }

  ngOnDestroy() {
      this.sub.unsubscribe();
  }
}