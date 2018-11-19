import { Component, OnInit, ViewChild, AfterViewInit, ElementRef } from '@angular/core';
import { MatPaginator, MatSort, MatDialog } from '@angular/material';
import { TemplateListDataSource } from './template-list-datasource';
import { TemplateService } from '../shared/template.service';
import { tap, distinctUntilChanged, debounceTime } from 'rxjs/operators';
import { merge, fromEvent } from 'rxjs';
import { Router, ActivatedRoute } from '@angular/router';
import { ProjectService } from '../shared/project.service';
import { ConfirmDialogComponent } from '../dialog/confirm-dialog.component';
import { ErrorDialogComponent } from '../error-dialog/error-dialog.component';

@Component({
  selector: 'template-list',
  templateUrl: './template-list.component.html',
  styleUrls: ['./template-list.component.css'],
})
export class TemplateListComponent implements AfterViewInit, OnInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild('input') input: ElementRef;

  dataSource: TemplateListDataSource;

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['id', 'title', 'actions'];

  constructor(private templateService: TemplateService,
    private projectService: ProjectService,
    public dialog: MatDialog,
    private router: Router,
    private route: ActivatedRoute) { }

  cloneTemplate(id: number) {
    this.router.navigate(['../template-new'], { queryParams: { clone: id }, relativeTo: this.route});
  }

  editTemplate(id: number) {
    this.router.navigate([`../template-edit/${id}`], { relativeTo: this.route });
  }

  deleteTemplate(id) {

    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {message: '¿Está seguro que desea eliminar el template?'}, width: '250px'
    });

    dialogRef.afterClosed().subscribe(
      data => {
        if (data === 1) {

          this.projectService.findActiveProjectsByTemplate(id).subscribe(
            res => {
              console.log('Consultando proyectos activos del template', res);
              if (res.length === 0) {
                this.confirmDeleteUser(id);
              } else {
                this.showError('El template está siendo utilizando en proyectos activos y no se puede eliminar');
              }
            },
            err => {
              console.log('Error consultando proyectos activos del template', err);
              this.showError('Se produjo un error al eliminar el template');
            }
          );
        }
      }
    );

  }

  confirmDeleteUser(id) {
    this.templateService.remove(id).subscribe(
      res => console.log('Eliminando template', res),
      err => {
        console.log('Error eliminando template', err);
        this.showError('Se produjo un error al eliminar el template');
      },
      () =>  this.loadTemplatesPage());
  }

  showError(error: string): void {
    this.dialog.open(ErrorDialogComponent, {
      data: {errorMsg: error}, width: '250px'
    });
  }

  addTemplate() {
    this.router.navigate(['../template-new'], {relativeTo: this.route});
  }

  ngOnInit() {
    this.dataSource = new TemplateListDataSource(this.paginator, this.sort, this.templateService);
    this.dataSource.loadTemplates();
  }

  ngAfterViewInit() {

    // server-side search
    fromEvent(this.input.nativeElement, 'keyup')
      .pipe(
        debounceTime(150),
        distinctUntilChanged(),
        tap(() => {
          this.paginator.pageIndex = 0;
          this.loadTemplatesPage();
        })
      )
      .subscribe();

    // reset the paginator after sorting
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        tap(() => this.loadTemplatesPage())
      )
      .subscribe();
  }

  loadTemplatesPage() {
    const sortOption = this.sort.active + ',' + this.sort.direction;
    this.dataSource.loadTemplates(this.input.nativeElement.value, sortOption, this.paginator.pageIndex, this.paginator.pageSize);
  }
}
