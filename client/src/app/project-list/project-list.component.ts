import { Component, OnInit, ViewChild, AfterViewInit, ElementRef } from '@angular/core';
import { MatPaginator, MatSort } from '@angular/material';
import { ProjectListDataSource } from './project-list-datasource';
import { fromEvent, merge } from 'rxjs';
import { debounceTime, distinctUntilChanged, tap } from 'rxjs/operators';
import { Router, ActivatedRoute } from '@angular/router';
import { ProjectService } from '../shared/project.service';

@Component({
  selector: 'project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.css'],
})
export class ProjectListComponent implements OnInit, AfterViewInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild('input') input: ElementRef;

  dataSource: ProjectListDataSource;
  projectsCount = 12;

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['id', 'name', 'actions'];

  constructor(private projectService: ProjectService, private router: Router, private route: ActivatedRoute) { }

  deleteProject(id: number) {

  }

  newProject() {
    this.router.navigate(['../project-new'], {relativeTo: this.route});
  }

  ngOnInit() {
    this.dataSource = new ProjectListDataSource(this.paginator, this.sort, this.projectService);
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

  loadProjects() {
    const sortOption = this.sort.active + ',' + this.sort.direction;
    this.dataSource.loadProjects(this.input.nativeElement.value, sortOption, this.paginator.pageIndex, this.paginator.pageSize);
  }
}
