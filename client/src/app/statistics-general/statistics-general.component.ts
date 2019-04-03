import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-statistics-general',
  templateUrl: './statistics-general.component.html',
  styleUrls: ['./statistics-general.component.css']
})
export class StatisticsGeneralComponent implements OnInit {

  navLinks: any[];
  activeLinkIndex = -1;

  constructor(private router: Router) {
  }

  ngOnInit() {
    this.inicializarNavLinks();
  }

  inicializarNavLinks() {
    this.navLinks = [
      {
        label: 'Historial de Usuarios',
        link: '/main/statistics/user-history',
        index: 0
      }, {
        label: 'Proyectos Activos',
        link: '/main/statistics/active-projects',
        index: 1
      }
    ];

    this.router.events.subscribe((res) => {
       this.activeLinkIndex = this.navLinks.indexOf(this.navLinks.find(tab => tab.link === this.router.url));
    });

   // this.gotoStatisticsDefault();
  }

  gotoStatisticsDefault() {
    this.router.navigate(['/main/statistics/user-history']);
  }

}
