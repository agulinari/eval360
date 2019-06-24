import { Component, NgZone } from "@angular/core";
import * as am4core from "@amcharts/amcharts4/core";
import * as am4charts from "@amcharts/amcharts4/charts";
import am4themes_animated from "@amcharts/amcharts4/themes/animated";
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material';
import { ReportService } from "../shared/report.service";
import { StatisticSp } from "../domain/statistics-status-project/statistics-sp";
import { StatisticSpEvaluee } from "../domain/statistics-status-project/statistics-sp-evaluee";
import { StatisticSpSection } from "../domain/statistics-status-project/statistics-sp-section";
import { StatisticSpItem } from "../domain/statistics-status-project/statistics-sp-item";
import { ErrorDialogComponent } from '../error-dialog/error-dialog.component';
import { StatisticSpPoint } from "../domain/statistics-status-project/statistics-sp-point";
import { LocalStorageService } from "../shared/local_storage.service";
import { StatisticSpRelation } from "../domain/statistics-status-project/statistics-sp-relation";

am4core.useTheme(am4themes_animated);

@Component({
  selector: "app-statistics-item-relation",
  templateUrl: "./statistics-item-relation.component.html",
  styleUrls: ["./statistics-item-relation.component.css"]
})

export class StatisticsItemRelationComponent {

  private chart: am4charts.XYChart3D;
  private statisticSpStatus: StatisticSp;

  private idCurrentUser : number;
  loading = false;

  constructor(private zone: NgZone,
    private route: ActivatedRoute,
    private router: Router,
    public dialog: MatDialog,
    private localStorageService: LocalStorageService,
    private reportService: ReportService) { }

  ngOnInit() {
    this.route.params.subscribe((params) => {
      this.statisticSpStatus=(this.localStorageService.getItem('statisticSpStatus') as StatisticSp);
      this.idCurrentUser = params['idUser'];
      //console.log('Entro a grafico XY_statisticSp: '+this.statisticSpStatus.statisticsSpEvaluees);
      this.loadGraphicXY();
    });
  }

  loadGraphicXY(): void {

 // this.zone.runOutsideAngular(() => {
      let chart = am4core.create("chartdivXY", am4charts.XYChart3D);
      this.statisticSpStatus.statisticsSpSections.forEach(ssps=>{
        let nameSection = ssps.name;
        let pointsJefes:number = 0;
        let pointsPares:number = 0;
        let pointsSubordinados:number = 0;
        let count = ssps.statisticSpItems.length;
        ssps.statisticSpItems.forEach(sspi=>{let point:StatisticSpPoint = sspi.points[this.idCurrentUser];
          pointsJefes+=point.pointManagers; 
          pointsPares+=point.pointPeers; 
          pointsSubordinados+=point.pointDirectReports; 
        });
        pointsJefes = (pointsJefes/count);
        pointsPares = (pointsPares/count);
        pointsSubordinados = (pointsSubordinados/count);
        let statisticSpRelation:StatisticSpRelation = new StatisticSpRelation();
        statisticSpRelation.seccion=nameSection;
        statisticSpRelation.jefes=pointsJefes;
        statisticSpRelation.pares=pointsPares;
        statisticSpRelation.subordinados=pointsSubordinados;
        chart.data.push(statisticSpRelation);

        console.log('Puntajes: '+pointsJefes+", "+pointsPares+", "+pointsSubordinados);
      });
            
      // Create axes
      let categoryAxis = chart.xAxes.push(new am4charts.CategoryAxis());
      categoryAxis.dataFields.category = "seccion";
      categoryAxis.renderer.grid.template.location = 0;
      categoryAxis.renderer.minGridDistance = 30;
      let label = categoryAxis.renderer.labels.template;
      label.truncate = true;
      label.maxWidth = 120;
      label.tooltipText = "{category}";

      let valueAxis = chart.yAxes.push(new am4charts.ValueAxis());
      valueAxis.title.text = "Puntaje promedio";
      valueAxis.renderer.labels.template.adapter.add("text", function (text) {
        return text + " pts";
      });

      // Create series
      let series = chart.series.push(new am4charts.ColumnSeries3D());
      series.dataFields.valueY = "jefes";
      series.dataFields.categoryX = "seccion";
      series.name = "Según sus jefes";
      series.clustered = false;
      series.columns.template.tooltipText = "Puntaje promedio en {category} (Jefes): [bold]{valueY}[/]";
      series.columns.template.fillOpacity = 0.9;

      let series2 = chart.series.push(new am4charts.ColumnSeries3D());
      series2.dataFields.valueY = "pares";
      series2.dataFields.categoryX = "seccion";
      series2.name = "Según sus pares";
      series2.clustered = false;
      series2.columns.template.tooltipText = "Puntaje promedio en {category} (Pares): [bold]{valueY}[/]";

      let series3 = chart.series.push(new am4charts.ColumnSeries3D());
      series3.dataFields.valueY = "pares";
      series3.dataFields.categoryX = "seccion";
      series3.name = "Según sus subordinados";
      series3.clustered = false;
      series3.columns.template.tooltipText = "Puntaje promedio en {category} (Subordinados): [bold]{valueY}[/]";

      this.chart = chart;
  // });
  }

  showError(error: string): void {
    this.dialog.open(ErrorDialogComponent, {
      data: { errorMsg: error }, width: '250px'
    });
  }

  ngOnDestroy() {
 // this.zone.runOutsideAngular(() => {
      if (this.chart) {
        this.chart.dispose();
      }
 //  });
  }
}