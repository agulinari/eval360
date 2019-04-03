import { Component, NgZone, AfterViewInit, OnDestroy, OnInit } from '@angular/core';
import * as am4core from '@amcharts/amcharts4/core';
import * as am4charts from '@amcharts/amcharts4/charts';
import am4themes_animated from '@amcharts/amcharts4/themes/animated';
import { ProjectService } from '../shared/project.service';
import { UserService } from '../shared/user.service';
import { debounceTime, tap, switchMap, finalize } from 'rxjs/operators';
import { User } from '../domain/user/user';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material';
import { UserHistory } from '../domain/statistics/user-history';
import { ErrorDialogComponent } from '../error-dialog/error-dialog.component';

am4core.useTheme(am4themes_animated);

@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.css']
})

export class StatisticsComponent implements AfterViewInit, OnInit, OnDestroy {
  private chart: am4charts.XYChart;
  private colorSet = new am4core.ColorSet();

  filteredUsers: User[] = [];
  usersForm: FormGroup;
  isLoading = false;
  selectedUser: User;

  constructor(private zone: NgZone,
    private dialog: MatDialog,
    private fb: FormBuilder,
    private projectService: ProjectService,
    private userService: UserService) {}

  ngOnInit() {
    this.selectedUser = null;

    this.usersForm = this.fb.group({
        userInput: ['']
    });

    this.usersForm.get('userInput')
    .valueChanges
    .pipe(
        debounceTime(300),
        tap(() => this.isLoading = true),
        switchMap(value => this.userService.find(value, 'username,asc', 0, 10).pipe(
            finalize(() => this.isLoading = false)
        ))
    ).subscribe(userList => this.filteredUsers = userList.users);
 }


  ngAfterViewInit() {

    this.colorSet.saturation = 0.4;

    this.zone.runOutsideAngular(() => {

    const chart = am4core.create('chartdiv', am4charts.XYChart);
    chart.hiddenState.properties.opacity = 0; // this creates initial fade-in

    chart.paddingRight = 30;
    chart.dateFormatter.inputDateFormat = 'yyyy-MM-dd HH:mm';

    chart.data = [ ];

    chart.dateFormatter.dateFormat = 'yyyy-MM-dd';
    chart.dateFormatter.inputDateFormat = 'yyyy-MM-dd';

    const categoryAxis = chart.yAxes.push(new am4charts.CategoryAxis());
    categoryAxis.dataFields.category = 'category';
    categoryAxis.renderer.grid.template.location = 0;
    categoryAxis.renderer.inversed = true;

    const dateAxis = chart.xAxes.push(new am4charts.DateAxis());
    dateAxis.renderer.minGridDistance = 70;
    dateAxis.baseInterval = { count: 1, timeUnit: 'day' };
    // dateAxis.max = new Date(2018, 0, 1, 24, 0, 0, 0).getTime();
    // dateAxis.strictMinMax = true;
    dateAxis.renderer.tooltipLocation = 0;

    const series1 = chart.series.push(new am4charts.ColumnSeries());
    series1.columns.template.width = am4core.percent(80);
    series1.columns.template.tooltipText = '{task}: [bold]{openDateX}[/] - [bold]{dateX}[/]';

    series1.dataFields.openDateX = 'start';
    series1.dataFields.dateX = 'end';
    series1.dataFields.categoryY = 'category';
    series1.columns.template.propertyFields.fill = 'color'; // get color from data
    series1.columns.template.propertyFields.stroke = 'color';
    series1.columns.template.strokeOpacity = 1;

    chart.scrollbarX = new am4core.Scrollbar();

    this.chart = chart;
});
  }

  ngOnDestroy() {
    this.zone.runOutsideAngular(() => {
      if (this.chart) {
        this.chart.dispose();
      }
    });
  }

  addUser() {

    this.projectService.getUserHistory(this.selectedUser.id).subscribe(
      res => {
        console.log('Obteniendo historia de usuario', res);
        this.updateChart(this.selectedUser.username, res);
      },
      err => {
        console.log('Error obteniendo historia de usuario', err);
        this.showError('Se produjo un error al obtener historial del usuario');
      }
    );

  }

  updateChart(username: string, userHistory: UserHistory) {

    this.zone.runOutsideAngular(() =>  {

      let index = 0;
      userHistory.evaluationInstances.forEach(instance => {

        const item = {
          'category': username,
          'start': instance.startDate,
          'end': instance.endDate,
          'color': this.colorSet.next().brighten(index),
          'task': instance.projectName
        };

        index = index + 0.2;
        this.chart.data.push(item);
      });
      this.chart.validateData();

    });
  }

  displayFn(user: User) {
    if (user) {
        return user.username;
    }
  }

  userClick(event: any) {
    this.selectedUser = event.option.value;
  }

  checkUser() {
      if (!this.selectedUser || this.selectedUser !== this.usersForm.controls['userInput'].value) {
        this.usersForm.controls['userInput'].setValue(null);
        this.selectedUser = null;
      }
  }

  showError(error: string): void {
    this.dialog.open(ErrorDialogComponent, {
      data: {errorMsg: error}, width: '250px'
    });
  }
}
