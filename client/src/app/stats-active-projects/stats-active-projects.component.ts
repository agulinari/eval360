import { Component, OnInit, AfterViewInit, OnDestroy, NgZone } from '@angular/core';
import * as am4core from '@amcharts/amcharts4/core';
import * as am4charts from '@amcharts/amcharts4/charts';
import am4themes_animated from '@amcharts/amcharts4/themes/animated';


am4core.useTheme(am4themes_animated);

@Component({
  selector: 'app-stats-active-projects',
  templateUrl: './stats-active-projects.component.html',
  styleUrls: ['./stats-active-projects.component.css']
})
export class StatsActiveProjectsComponent implements AfterViewInit, OnInit, OnDestroy {

  private chart: am4charts.PieChart;

  constructor(private zone: NgZone) { }

  ngOnInit() {
  }

  ngAfterViewInit() {

    this.zone.runOutsideAngular(() => {

      // Create chart instance
      const chart = am4core.create('chartdiv', am4charts.PieChart);
      // Set data
      let selected;
      const types = [{
        type: 'Fossil Energy',
        percent: 70,
        color: chart.colors.getIndex(0),
        subs: [{
          type: 'Oil',
          percent: 15
        }, {
          type: 'Coal',
          percent: 35
        }, {
          type: 'Nuclear',
          percent: 20
        }]
      }, {
        type: 'Green Energy',
        percent: 30,
        color: chart.colors.getIndex(1),
        subs: [{
          type: 'Hydro',
          percent: 15
        }, {
          type: 'Wind',
          percent: 10
        }, {
          type: 'Other',
          percent: 5
        }]
      }];

      // Add data
      chart.data = generateChartData();

      // Add and configure Series
      const pieSeries = chart.series.push(new am4charts.PieSeries());
      pieSeries.dataFields.value = 'percent';
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
                percent: types[i].subs[x].percent,
                color: types[i].color,
                pulled: true
              });
            }
          } else {
            chartData.push({
              type: types[i].type,
              percent: types[i].percent,
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

}
