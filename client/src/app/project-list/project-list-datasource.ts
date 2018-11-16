import { DataSource } from '@angular/cdk/collections';
import { MatPaginator, MatSort } from '@angular/material';
import { map, catchError, finalize } from 'rxjs/operators';
import { Observable, of as observableOf, merge, BehaviorSubject, of } from 'rxjs';
import { Project } from '../domain/project';
import { ProjectService } from '../shared/project.service';


/**
 * Data source for the ProjectList view. This class should
 * encapsulate all logic for fetching and manipulating the displayed data
 * (including sorting, pagination, and filtering).
 */
export class ProjectListDataSource extends DataSource<Project> {

  private projectsSubject = new BehaviorSubject<Project[]>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);

  public loading$ = this.loadingSubject.asObservable();

  constructor(private paginator: MatPaginator, private sort: MatSort, private projectService: ProjectService) {
    super();
  }

  /**
   * Connect this data source to the table. The table will only update when
   * the returned stream emits new items.
   * @returns A stream of the items to be rendered.
   */
  connect(): Observable<Project[]> {
    return this.projectsSubject.asObservable();
  }

  /**
   *  Called when the table is being destroyed. Use this function, to clean up
   * any open connections or free any held resources that were set up during connect.
   */
  disconnect() {
    this.projectsSubject.complete();
    this.loadingSubject.complete();
  }


  loadProjects(filter = '', sortOrder = 'id,asc', pageIndex = 0, pageSize = 10) {

    this.loadingSubject.next(true);

    /*this.projectService.find(filter, sortOrder, pageIndex, pageSize).pipe(
      catchError(() => of([])),
      finalize(() => this.loadingSubject.next(false))
    )*/

    this.projectService.getAll().pipe(
      catchError(() => of([])),
      finalize(() => this.loadingSubject.next(false))
    )
    .subscribe(templates => this.projectsSubject.next(templates));
  }
}
