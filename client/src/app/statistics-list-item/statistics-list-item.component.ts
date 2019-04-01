import { Component, NgZone } from "@angular/core";
import * as am4core from "@amcharts/amcharts4/core";
import * as am4charts from "@amcharts/amcharts4/charts";
import am4themes_animated from "@amcharts/amcharts4/themes/animated";
import {StatisticService} from "../shared/statistic.service"
import {StatisticSp} from "../domain/statistics-status-project/statistics-sp"
import {StatisticSpItem} from "../domain/statistics-status-project/statistics-sp-item"

am4core.useTheme(am4themes_animated);

@Component({
  selector: "app-root",
  templateUrl: "./statistics-list-item.component.html",
  styleUrls: ["./statistics-list-item.component.css"]
})

export class StatisticsListItemComponent {
  
private chart: am4charts.RadarChart;
private statisticSpStatus: StatisticSp;
   
 // private series:am4charts.RadarColumnSeries;
 // private yearLabel:am4core.Label;
 // private categoryAxis: am4charts.CategoryAxis;
 // private evaluados:Array<string>;
 // private result_evaluaciones:any;
  //private colorSet: am4core.ColorSet;
  //private startIdxEval: number;
  //private endIdxEval: number;
  //private currentIdxEval: number;

  constructor(private zone: NgZone, 
              private statisticService: StatisticService) {}

  ngAfterViewInit() {

  this.zone.runOutsideAngular(() => {
    
this.statisticSpStatus = this.statisticService.get("1");

let evaluados = this.statisticSpStatus.statisticsSpEvaluees;

let result_evaluaciones= this.statisticSpStatus.statisticsSpSections;

let startIdxEval = 0;
let endIdxEval = evaluados.length - 1;
let currentIdxEval = 2;
let colorSet = new am4core.ColorSet();

let chart = am4core.create("chartdiv", am4charts.RadarChart);
chart.numberFormatter.numberFormat = "#.##";
chart.hiddenState.properties.opacity = 0;

chart.startAngle = 270 - 180;
chart.endAngle = 270 + 180;

chart.radius = am4core.percent(60);
chart.innerRadius = am4core.percent(40);

// year label goes in the middle
let yearLabel = chart.radarContainer.createChild(am4core.Label);
yearLabel.horizontalCenter = "middle";
yearLabel.verticalCenter = "middle";
yearLabel.fill = am4core.color("#673AB7");
yearLabel.fontSize = 12;
yearLabel.text = evaluados[currentIdxEval].name;

// zoomout button
let zoomOutButton = chart.zoomOutButton;
zoomOutButton.dx = 0;
zoomOutButton.dy = 0;
zoomOutButton.marginBottom = 15;
zoomOutButton.parent = chart.rightAxesContainer;

// scrollbar
chart.scrollbarX = new am4core.Scrollbar();
chart.scrollbarX.parent = chart.rightAxesContainer;
chart.scrollbarX.orientation = "vertical";
chart.scrollbarX.align = "center";

// vertical orientation for zoom out button and scrollbar to be positioned properly
chart.rightAxesContainer.layout = "vertical";
chart.rightAxesContainer.padding(120, 20, 120, 20);

// category axis
let categoryAxis = chart.xAxes.push(new am4charts.CategoryAxis());
categoryAxis.renderer.grid.template.location = 0;
categoryAxis.dataFields.category = "area";

let categoryAxisRenderer = categoryAxis.renderer;
let categoryAxisLabel = categoryAxisRenderer.labels.template;
categoryAxisLabel.location = 0.5;
categoryAxisLabel.radius = 38;
categoryAxisLabel.relativeRotation = 90;

categoryAxisRenderer.minGridDistance = 13;
categoryAxisRenderer.grid.template.radius = -35;
categoryAxisRenderer.grid.template.strokeOpacity = 0.05;
categoryAxisRenderer.grid.template.interactionsEnabled = false;

categoryAxisRenderer.ticks.template.disabled = true;
categoryAxisRenderer.axisFills.template.disabled = true;
categoryAxisRenderer.line.disabled = true;

categoryAxisRenderer.tooltipLocation = 0.5;
categoryAxis.tooltip.defaultState.properties.opacity = 0;

// value axis
let valueAxis = chart.yAxes.push(new am4charts.ValueAxis());
valueAxis.min = -8;
valueAxis.max = 8;
valueAxis.strictMinMax = true;
valueAxis.tooltip.defaultState.properties.opacity = 0;
valueAxis.tooltip.animationDuration = 2;
valueAxis.cursorTooltipEnabled = true;
valueAxis.zIndex = 10;

let valueAxisRenderer = valueAxis.renderer;
valueAxisRenderer.axisFills.template.disabled = true;
valueAxisRenderer.ticks.template.disabled = true;
valueAxisRenderer.minGridDistance = 30;
valueAxisRenderer.grid.template.strokeOpacity = 0.05;


// series
let series = chart.series.push(new am4charts.RadarColumnSeries());
series.columns.template.width = am4core.percent(90);
series.columns.template.strokeOpacity = 0;
series.dataFields.valueY = "value" + currentIdxEval;
series.dataFields.categoryX = "area";
series.tooltipText = "{categoryX}:{valueY.value}";

// this makes columns to be of a different color, depending on value
series.heatRules.push({ target: series.columns.template, property: "fill", minValue: -8, maxValue: 8, min: am4core.color("#673AB7"), max: am4core.color("#F44336"), dataField: "valueY" });

// cursor
let cursor = new am4charts.RadarCursor();
chart.cursor = cursor;
cursor.behavior = "zoomX";

cursor.xAxis = categoryAxis;
cursor.innerRadius = am4core.percent(40);
cursor.lineY.disabled = true;

cursor.lineX.fillOpacity = 0.2;
cursor.lineX.fill = am4core.color("#000000");
cursor.lineX.strokeOpacity = 0;
cursor.fullWidthLineX = true;

// year slider
let yearSliderContainer = chart.createChild(am4core.Container);
yearSliderContainer.layout = "vertical";
yearSliderContainer.padding(0, 38, 0, 38);
yearSliderContainer.width = am4core.percent(100);

let yearSlider = yearSliderContainer.createChild(am4core.Slider);
yearSlider.events.on("rangechanged", function () {
    updateRadarData(startIdxEval + Math.round(yearSlider.start * (endIdxEval - startIdxEval)));
})
yearSlider.orientation = "horizontal";
yearSlider.start = 0.5;

chart.data = generateRadarData();

function generateRadarData() {
    let data = [];
    let i = 0;
    for (var seccion in result_evaluaciones) {
        let nombreSeccion = result_evaluaciones[seccion].name;
        let seccionData = result_evaluaciones[seccion].statisticSpItems;

        seccionData.forEach(function (area) {
            let rawDataItem = { "area": area.description }

            for (var y = 0;  y < area.points.length; y++) {
                rawDataItem["value" + (startIdxEval + y)] = area.points[y].point;
            }

            data.push(rawDataItem);
        });

        createRange(nombreSeccion, seccionData, i);
        i++;

    }
    return data;
}


function updateRadarData(idxEval:number) {
    if (currentIdxEval != idxEval) {
        currentIdxEval = idxEval;
        yearLabel.text = evaluados[currentIdxEval].name;
        series.dataFields.valueY = "value" + currentIdxEval;
        chart.invalidateRawData();
    }
}

function createRange(name:string, seccionData:Array<StatisticSpItem>, index:number) {

    let axisRange = categoryAxis.axisRanges.create();
    axisRange.axisFill.interactionsEnabled = true;
    axisRange.text = name;
    // first area
    axisRange.category = seccionData[0].title;
    // last area
    axisRange.endCategory = seccionData[seccionData.length - 1].title;
    // every 3rd color for a bigger contrast
    axisRange.axisFill.fill = colorSet.getIndex(index * 3);
    axisRange.grid.disabled = true;
    axisRange.label.interactionsEnabled = false;

    let axisFill = axisRange.axisFill;
    axisFill.innerRadius = -0.001; // almost the same as 100%, we set it in pixels as later we animate this property to some pixel value
    axisFill.radius = -20; // negative radius means it is calculated from max radius
    axisFill.disabled = false; // as regular fills are disabled, we need to enable this one
    axisFill.fillOpacity = 1;
    axisFill.togglable = true;

    axisFill.showSystemTooltip = true;
    axisFill.readerTitle = "click to zoom";
    axisFill.cursorOverStyle = am4core.MouseCursorStyle.pointer;

    axisFill.events.on("hit", function (event) {
        let dataItem = event.target.dataItem;
        if (!event.target.isActive) {
            categoryAxis.zoom({ start: 0, end: 1 });
        }
        else {
            categoryAxis.zoomToCategories(dataItem.category, dataItem.endCategory);
        }
    })

    // hover state
    let hoverState = axisFill.states.create("hover");
    hoverState.properties.innerRadius = -20;
    hoverState.properties.radius = -35;

    let axisLabel = axisRange.label;
    axisLabel.location = 0.5;
    axisLabel.fill = am4core.color("#ffffff");
    axisLabel.radius = 0;
    axisLabel.relativeRotation = 0;
}

let slider = yearSliderContainer.createChild(am4core.Slider);
slider.start = 1;
slider.events.on("rangechanged", function () {
    let start = slider.start;

    chart.startAngle = 270 - start * 179 - 1;
    chart.endAngle = 270 + start * 179 + 1;

    valueAxis.renderer.axisAngle = chart.startAngle;
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