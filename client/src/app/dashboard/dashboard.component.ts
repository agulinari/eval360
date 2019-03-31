import { Component, OnInit } from '@angular/core';
import { map } from 'rxjs/operators';
import { Breakpoints, BreakpointObserver } from '@angular/cdk/layout';
import { AuthenticationService } from '../shared/authentication.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent implements OnInit{

  private roles: String[];

  /** Based on the screen size, switch from standard to one column per row */
  cards = this.breakpointObserver.observe(Breakpoints.Handset).pipe(
    map(({ matches }) => {
      if (matches) {
        return [
          { title: 'Proyectos',
            subtitle: 'Administración de proyectos',
            collapse: false,
            roles: 'ROLE_USER',
            url: '/main/project-list',
            content: 'Para gestionar sus proyectos acceda aquí.',
            image: '/assets/images/report.jpg', cols: 2, rows: 1 },
          { title: 'Usuarios',
            subtitle: 'Administración de usuarios',
            collapse: false,
            roles: 'ROLE_ADMIN',
            url: '/main/user-list',
            content: 'Administre los usuarios del sistema aquí.',
            image: '/assets/images/organization.jpg', cols: 2, rows: 1 },
          { title: 'Templates',
            subtitle: 'Administración de templates',
            collapse: false,
            url: '/main/template-list',
            roles: 'ROLE_ADMIN',
            content: 'Para gestionar sus templates ingrese aquí.',
            image: '/assets/images/evaluation.jpg', cols: 2, rows: 1 },
          { title: 'Estadisticas',
            subtitle: 'Administración de estadisticas',
            collapse: false,
            url: '/main/statistics',
            roles: 'ROLE_ADMIN',
            content: 'Para gestionar sus estadisticas ingrese aquí.',
            image: '/assets/images/statistics.jpg', cols: 2, rows: 1 }
        ];
      }

      return [
        { title: 'Proyectos',
          subtitle: 'Administración de proyectos',
          collapse: false,
          url: '/main/project-list',
          roles: 'ROLE_USER',
          content: 'Para gestionar sus proyectos acceda aquí.',
          image: '/assets/images/report.jpg', cols: 2, rows: 1 },
       { title: 'Usuarios',
          subtitle: 'Administración de usuarios',
          content: 'Administre los usuarios del sistema aquí.',
          collapse: false,
          url: '/main/user-list',
          roles: 'ROLE_ADMIN',
          image: '/assets/images/organization.jpg', cols: 1, rows: 1 },
       { title: 'Templates',
         subtitle: 'Administración de templates',
         collapse: false,
         url: '/main/template-list',
         roles: 'ROLE_ADMIN',
         content: 'Para gestionar sus templates ingrese aquí.',
         image: '/assets/images/evaluation.jpg', cols: 1, rows: 1 },
       { title: 'Estadisticas',
         subtitle: 'Administración de estadisticas',
         collapse: false,
         url: '/main/statistics-list',
         roles: 'ROLE_ADMIN',
         content: 'Para gestionar sus estadisticas ingrese aquí.',
         image: '/assets/images/statistics.jpg', cols: 2, rows: 1 }
      ];
    })
  );

  collapse(card) {
    card.collapse = !card.collapse;
  }

  goTo(link) {
    this.router.navigate([link]);
  }

  constructor(private breakpointObserver: BreakpointObserver,
    private router: Router,
    private authenticationService: AuthenticationService) {}

  ngOnInit() {
    this.roles = this.authenticationService.getRoles();
  }
}
