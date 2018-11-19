import { DataSource } from '@angular/cdk/collections';
import { MatPaginator, MatSort } from '@angular/material';
import { map, catchError, finalize } from 'rxjs/operators';
import { Observable, of as observableOf, merge, BehaviorSubject, of } from 'rxjs';
import { Template } from '../domain/template';
import { TemplateService } from '../shared/template.service';
import { TemplateList } from '../domain/template-list';

/**
 * Data source for the TemplateList view. This class should
 * encapsulate all logic for fetching and manipulating the displayed data
 * (including sorting, pagination, and filtering).
 */
export class TemplateListDataSource extends DataSource<Template> {

  private templatesSubject = new BehaviorSubject<Template[]>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);
  public totalItems = 0;
  public loading$ = this.loadingSubject.asObservable();

  constructor(private paginator: MatPaginator, private sort: MatSort, private templateService: TemplateService) {
    super();
  }

  /**
   * Connect this data source to the table. The table will only update when
   * the returned stream emits new items.
   * @returns A stream of the items to be rendered.
   */
  connect(): Observable<Template[]> {
    return this.templatesSubject.asObservable();
  }

  /**
   *  Called when the table is being destroyed. Use this function, to clean up
   * any open connections or free any held resources that were set up during connect.
   */
  disconnect() {
    this.templatesSubject.complete();
    this.loadingSubject.complete();
  }


  loadTemplates(filter = '', sortOrder = 'id,asc', pageIndex = 0, pageSize = 10) {

    this.loadingSubject.next(true);

    this.templateService.find(filter, sortOrder, pageIndex, pageSize).pipe(
      catchError(() => of([])),
      finalize(() => this.loadingSubject.next(false))
    )
    .subscribe(templateList => {
      const tl = templateList as TemplateList;
      this.templatesSubject.next(tl.templates);
      this.totalItems = tl.total;
    });
  }

}
