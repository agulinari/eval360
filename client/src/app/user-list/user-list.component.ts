import { Component, OnInit, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { MatPaginator, MatSort, MatDialog } from '@angular/material';
import { UserListDataSource } from './user-list-datasource';
import { UserService } from '../shared/user.service';
import { fromEvent, merge } from 'rxjs';
import { debounceTime, distinctUntilChanged, tap } from 'rxjs/operators';
import { Router, ActivatedRoute } from '@angular/router';
import { ConfirmDialogComponent } from '../dialog/confirm-dialog.component';
import { ErrorDialogComponent } from '../error-dialog/error-dialog.component';
import { ProjectService } from '../shared/project.service';

@Component({
  selector: 'user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css'],
})
export class UserListComponent implements AfterViewInit, OnInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild('input') input: ElementRef;
  dataSource: UserListDataSource;
  usersCount = 3;


  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['username', 'actions'];

  constructor(private userService: UserService,
    private projectService: ProjectService,
    public dialog: MatDialog,
    private router: Router,
    private route: ActivatedRoute) { }

  editUser(row) {
    console.log('Row clicked: ', row.id);
    this.router.navigate([`../user-edit/${row.id}`], { relativeTo: this.route });
  }

  userProfile(row) {
    this.router.navigate([`../user-profile/${row.id}`], { relativeTo: this.route });
  }

  addUser() {
    this.router.navigate(['../user-new'], {relativeTo: this.route});
  }

  deleteUser(id) {

    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {message: '¿Está seguro que desea eliminar al usuario?'}, width: '250px'
    });

    dialogRef.afterClosed().subscribe(
      data => {
        if (data === 1) {

          this.projectService.findActiveProjectsByUser(id).subscribe(
            res => {
              console.log('Consultando proyectos activos de usuario', res);
              if (res.length === 0) {
                this.confirmDeleteUser(id);
              } else {
                this.showError('El usuario participa en proyectos activos y no se puede eliminar');
              }
            },
            err => {
              console.log('Error consultando proyectos activos de usuario', err);
              this.showError('Se produjo un error al eliminar el usuario');
            }
          );
        }
      }
    );

  }

  confirmDeleteUser(id) {
    this.userService.remove(id).subscribe(
      res => console.log('Eliminando usuario', res),
      err => {
        console.log('Error eliminando usuario', err);
        this.showError('Se produjo un error al eliminar el usuario');
      },
      () =>  this.loadUsersPage());
  }

  showError(error: string): void {
    this.dialog.open(ErrorDialogComponent, {
      data: {errorMsg: error}, width: '250px'
    });
  }

  ngOnInit() {
    this.dataSource = new UserListDataSource(this.paginator, this.sort, this.userService);
    this.dataSource.loadUsers();
  }

  ngAfterViewInit() {

    // server-side search
    fromEvent(this.input.nativeElement, 'keyup')
      .pipe(
        debounceTime(150),
        distinctUntilChanged(),
        tap(() => {
          this.paginator.pageIndex = 0;
          this.loadUsersPage();
        })
      )
      .subscribe();

    // reset the paginator after sorting
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        tap(() => this.loadUsersPage())
      )
      .subscribe();
  }

  loadUsersPage() {
    const sortOption = this.sort.active + ',' + this.sort.direction;
    this.dataSource.loadUsers(this.input.nativeElement.value, sortOption, this.paginator.pageIndex, this.paginator.pageSize);
  }
}
