import { Component, OnInit, ViewChild, AfterViewInit, ElementRef } from '@angular/core';
import { MatPaginator, MatSort, MatDialog } from '@angular/material';
import { ProjectListDataSource } from './project-list-datasource';
import { fromEvent, merge } from 'rxjs';
import { debounceTime, distinctUntilChanged, tap } from 'rxjs/operators';
import { Router, ActivatedRoute } from '@angular/router';
import { ProjectService } from '../shared/project.service';
import { AuthenticationService } from '../shared/authentication.service';
import { Project } from '../domain/project/project';
import { HttpResponse, HttpEventType } from '@angular/common/http';
import { CreateProjectDialogComponent } from '../dialog/create-project-dialog.component';
import { ErrorDialogComponent } from '../error-dialog/error-dialog.component';

@Component({
  selector: 'app-project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.css'],
})
export class ProjectListComponent implements OnInit, AfterViewInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild('input') input: ElementRef;

  dataSource: ProjectListDataSource;
  userId: string;

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['name', 'startDate', 'actions'];

  constructor(private projectService: ProjectService,
    private authenticationService: AuthenticationService,
    public dialog: MatDialog,
    private router: Router,
    private route: ActivatedRoute) { }

  goToProjectStatus(id: number, idEvTemp: number) {
    this.router.navigate([`../project/${id}/template/${idEvTemp}`], {relativeTo: this.route});
  }

  goToProjectTasks(id: number) {
    this.router.navigate([`../project-tasks/${id}`], {relativeTo: this.route});
  }

  newProject() {
    this.router.navigate(['../project-new'], {relativeTo: this.route});
  }

  ngOnInit() {
    this.userId = this.authenticationService.getUserId();
    this.dataSource = new ProjectListDataSource(this.paginator, this.sort, this.authenticationService, this.projectService);
    this.dataSource.loadProjects();
  }

  ngAfterViewInit() {

    // server-side search
    fromEvent(this.input.nativeElement, 'keyup')
      .pipe(
        debounceTime(150),
        distinctUntilChanged(),
        tap(() => {
          this.paginator.pageIndex = 0;
          this.loadProjects();
        })
      )
      .subscribe();

    // reset the paginator after sorting
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        tap(() => this.loadProjects())
      )
      .subscribe();
  }

  userIsAdmin(project: Project): boolean {
    const userId = this.authenticationService.getUserId();
    const isAdmin = (project.projectAdmins.find(admin => admin.idUser === +userId) !== undefined);
    return isAdmin;
  }

  createProject() {
    const dialogRef = this.dialog.open(CreateProjectDialogComponent, {
      data: {}, minWidth: '250px'
    });

    dialogRef.afterClosed().subscribe(
      data => {
        if (data['code'] === 1 ) { // Import
          this.loadProjects();
        }
        if (data['code'] === 2) { // Manual
          this.router.navigate(['../project-create'], {relativeTo: this.route});
        }
        if (data['code'] === 3) { // Error importing
          this.showError(data['message']);
        }
      }
    );
  }

  loadProjects() {
    const sortOption = this.sort.active + ',' + this.sort.direction;
    this.dataSource.loadProjects(this.input.nativeElement.value, sortOption, this.paginator.pageIndex, this.paginator.pageSize);
  }

  showError(error: string): void {
    this.dialog.open(ErrorDialogComponent, {
      data: {errorMsg: error}, width: '250px'
    });
  }

  showBadge(project: Project): number {

    let pending = 0;
    project.evaluees.forEach(evaluee => {
        evaluee.feedbackProviders.forEach(efp => {
            if (efp.feedbackProvider.idUser === +this.userId && efp.status === 'PENDIENTE') {
                pending++;
            }
        });
    });
    return pending;
  }

}
