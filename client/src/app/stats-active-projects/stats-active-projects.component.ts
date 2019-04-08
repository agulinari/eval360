import { Component, OnInit, AfterViewInit, OnDestroy, NgZone } from '@angular/core';
import * as am4core from '@amcharts/amcharts4/core';
import * as am4charts from '@amcharts/amcharts4/charts';
import am4themes_animated from '@amcharts/amcharts4/themes/animated';
import { ProjectService } from '../shared/project.service';
import { ActiveProjectStats } from '../domain/statistics/active-project-stats';
import { ErrorDialogComponent } from '../error-dialog/error-dialog.component';
import { MatDialog } from '@angular/material';


am4core.useTheme(am4themes_animated);

@Component({
  selector: 'app-stats-active-projects',
  templateUrl: './stats-active-projects.component.html',
  styleUrls: ['./stats-active-projects.component.css']
})
export class StatsActiveProjectsComponent implements AfterViewInit, OnDestroy {

  private chart: am4charts.PieChart;

  constructor(private zone: NgZone,
    private dialog: MatDialog,
    private projectService: ProjectService) { }


  ngAfterViewInit() {
    this.projectService.getActiveProjectsStats().subscribe(
      res => {
        console.log('Obteniendo historia de usuario', res);
        this.createChart(res);
      },
      err => {
        console.log('Error obteniendo historia de usuario', err);
        this.showError('Se produjo un error al obtener historial del usuario');
      }
    );
  }

  createChart(stats: ActiveProjectStats[]) {

    this.zone.runOutsideAngular(() => {

      // Create chart instance
      const chart = am4core.create('chartdiv', am4charts.PieChart);
      // Set data
      let selected;
      const types = [];

      let totalEvaluations = 0;
      stats.forEach(stat => {
        totalEvaluations += stat.evaluationsByManagers + stat.evaluationsByPeers + stat.evaluationsBySubordiantes;
      });

      stats.forEach(stat => {
        const totalEvals = stat.evaluationsByManagers + stat.evaluationsByPeers + stat.evaluationsBySubordiantes;

        const type = {
          type: stat.projectName,
          evaluations: totalEvals,
          color: chart.colors.next(),
          subs: [
            {
              type: 'Jefe',
              evaluations: stat.evaluationsByManagers
            }, {
              type: 'Par',
              evaluations: stat.evaluationsByPeers
            }, {
              type: 'Dependiente',
              evaluations: stat.evaluationsBySubordiantes
            }
          ]
        };
        types.push(type);
      });

      // Add data
      chart.data = generateChartData();

      // Add and configure Series
      const pieSeries = chart.series.push(new am4charts.PieSeries());
      pieSeries.dataFields.value = 'evaluations';
      pieSeries.dataFields.category = 'type';
      pieSeries.slices.template.propertyFields.fill = 'color';
      pieSeries.slices.template.propertyFields['isActive'] = 'pulled';
      pieSeries.slices.template.strokeWidth = 0;

      function generateChartData() {
        const chartData = [];
        for (let i = 0; i < types.length; i++) {
          if (i === selected) {
            for (let x = 0; x < types[i].subs.length; x++) {
              chartData.push({
                type: types[i].subs[x].type,
                evaluations: types[i].subs[x].evaluations,
                color: types[i].color,
                pulled: true
              });
            }
          } else {
            chartData.push({
              type: types[i].type,
              evaluations: types[i].evaluations,
              color: types[i].color,
              id: i
            });
          }
        }
        return chartData;
      }

      pieSeries.slices.template.events.on('hit', function(event) {
        if (event.target.dataItem.dataContext['id'] !== undefined) {
          selected = event.target.dataItem.dataContext['id'];
        } else {
          selected = undefined;
        }
        chart.data = generateChartData();
      });

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

  showError(error: string): void {
    this.dialog.open(ErrorDialogComponent, {
      data: {errorMsg: error}, width: '250px'
    });
  }

}
