import { DataSource } from '@angular/cdk/collections';
import { MatPaginator, MatSort } from '@angular/material';
import { map, catchError, finalize } from 'rxjs/operators';
import { Observable, of as observableOf, merge, BehaviorSubject, of } from 'rxjs';
import { User } from '../domain/user/user';
import { UserService } from '../shared/user.service';
import { UserList } from '../domain/user/user-list';


/**
 * Data source for the UserList view. This class should
 * encapsulate all logic for fetching and manipulating the displayed data
 * (including sorting, pagination, and filtering).
 */
export class UserListDataSource extends DataSource<User> {

  private usersSubject = new BehaviorSubject<User[]>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);
  public totalItems = 0;
  public loading$ = this.loadingSubject.asObservable();

  constructor(private paginator: MatPaginator, private sort: MatSort, private userService: UserService) {
    super();
  }

  /**
   * Connect this data source to the table. The table will only update when
   * the returned stream emits new items.
   * @returns A stream of the items to be rendered.
   */
  connect(): Observable<User[]> {
    return this.usersSubject.asObservable();
  }

  /**
   *  Called when the table is being destroyed. Use this function, to clean up
   * any open connections or free any held resources that were set up during connect.
   */
  disconnect() {
    this.usersSubject.complete();
    this.loadingSubject.complete();
  }


  loadUsers(filter = '', sortOrder = 'id,asc', pageIndex = 0, pageSize = 10) {

    this.loadingSubject.next(true);

    this.userService.find(filter, sortOrder, pageIndex, pageSize).pipe(
      catchError(() => of([])),
      finalize(() => this.loadingSubject.next(false))
    )
    .subscribe(userList => {
      const ul = userList as UserList;
      this.usersSubject.next(ul.users);
      this.totalItems = ul.total;
    });
  }

}
