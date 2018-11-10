import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { MatPaginator, MatSort } from '@angular/material';
import { TemplateListDataSource } from './template-list-datasource';
import { TemplateService } from '../shared/template.service';
import { tap } from 'rxjs/operators';
import { merge } from 'rxjs';

@Component({
  selector: 'template-list',
  templateUrl: './template-list.component.html',
  styleUrls: ['./template-list.component.css'],
})
export class TemplateListComponent implements AfterViewInit, OnInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  dataSource: TemplateListDataSource;
  templatesCount = 12;

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['id', 'title'];

  constructor(private templateService: TemplateService) { }

  onRowClicked(row) {
    console.log('Row clicked: ', row);
  }

  ngOnInit() {
    this.dataSource = new TemplateListDataSource(this.paginator, this.sort, this.templateService);
    this.dataSource.loadTemplates();
  }

  ngAfterViewInit() {

    // reset the paginator after sorting
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        tap(() => this.loadTemplatesPage())
      )
      .subscribe();
  }

  loadTemplatesPage() {
    this.dataSource.loadTemplates('', this.sort.active + ',' + this.sort.direction, this.paginator.pageIndex, this.paginator.pageSize);
  }
}
