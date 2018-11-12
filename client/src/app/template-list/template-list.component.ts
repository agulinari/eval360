import { Component, OnInit, ViewChild, AfterViewInit, ElementRef } from '@angular/core';
import { MatPaginator, MatSort } from '@angular/material';
import { TemplateListDataSource } from './template-list-datasource';
import { TemplateService } from '../shared/template.service';
import { tap, distinctUntilChanged, debounceTime } from 'rxjs/operators';
import { merge, fromEvent } from 'rxjs';
import { Router, ActivatedRoute } from '@angular/router';

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
  templatesCount = 12;

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['id', 'title', 'actions'];

  constructor(private templateService: TemplateService, private router: Router, private route: ActivatedRoute) { }

  onRowClicked(row) {
    console.log('Row clicked: ', row.id);
    this.router.navigate([`../template-edit/${row.id}`], { relativeTo: this.route });
  }

  deleteTemplate(id: number) {

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
