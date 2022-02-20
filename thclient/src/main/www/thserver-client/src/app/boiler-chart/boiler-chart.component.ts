import {Component, HostListener, Input, OnInit} from '@angular/core';
import {HeatingDataService} from "../heating-data.service";
import {HeatingEntity} from "../entities/heatingEntity";
import {forkJoin, interval} from "rxjs";
import * as moment from "moment";
import {Moment} from "moment";
import * as Highcharts from 'highcharts';
import {Chart} from 'highcharts';
import NoDataToDisplay from 'highcharts/modules/no-data-to-display';
import theme from 'highcharts/themes/dark-unica';
import {FormBuilder, Validators} from '@angular/forms';
import more from 'highcharts/highcharts-more';
import {Interval, UtilsServiceService} from "../utils-service.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MeteoSwissEntity} from "../entities/meteoSwissEntity";
import {BoilerStatsByHourEntity} from "../entities/boilerStatsByHourEntity";
import {BoilerStatsDayOfWeekEntity} from "../entities/boilerStatsDayOfWeekEntity";
import {Router} from "@angular/router";
import {SoleInOutDeltaInOperationStatEntity} from "../entities/soleInOutDeltaInOperationStatEntity";


more(Highcharts);

@Component({
  selector: 'app-boiler-chart',
  templateUrl: './boiler-chart.component.html',
  styleUrls: ['./boiler-chart.component.sass']
})
export class BoilerChartComponent implements OnInit {

  constructor(private heatingDataService: HeatingDataService,
              private formBuilder: FormBuilder,
              private utilsServiceService: UtilsServiceService,
              private snackBar: MatSnackBar,
              private router: Router
  ) {
  }

  @Input()
  overviewMode: boolean = false;


  lastUserActivationTime: Moment = moment().subtract(1, 'days');

  //@Output() receivedNewTHValue = new EventEmitter();
  chartUpdateFlag: boolean = false;
  chartUpdateFlagBoilerStatsByHour: boolean = false;
  chartUpdateFlagBoilerStatsByDayOfWeek: boolean = false;
  chartUpdateFlagSoleDeltaTempInOperation: boolean = false;

  boilerTempAverage: any = [];
  boilerTempMinMax: any = [];
  boilerTempDeltaTemp: any = [];

  boilerStatsByHour: any = [];
  boilerStatsByHourNumberOfStaticsRecords: number = 0;
  boilerStatsByDayOfWeek: any = [];
  boilerStatsByDayOfWeekNumberOfStaticsRecords: number = 0;

  soleInTempAverage: any = [];
  soleOutTempAverage: any = [];
  soleInTempMinMax: any = [];
  soleOutTempMinMax: any = [];

  soleTempDelta: any = [];
  soleTempDeltaInOperationAvg: any = [];
  soleTempDeltaInOperationMinMax: any = [];

  heatingInTempMinMax: any = [];
  heatingOutTempMinMax: any = [];

  heatingTempDelta: any = [];

  compressorHours: any = [];

  outdoorTempAverage: any = [];
  outdoorTempAverageMeteo1: any = []; // TODO: hack, properly support stations
  outdoorTempAverageMeteo2: any = []; // TODO: hack, properly support stations
  outdoorTempMinMax: any = [];

  windGustMeteoSwiss: any = [];

  // operationChartSeries: any = [];

  highcharts: typeof Highcharts = Highcharts;

  panelOpenState: boolean = true;
  loading: boolean = false;
  loadingBoilerByHour: boolean = false;
  loadingBoilerByDayOfWeek: boolean = false;
  loadingSoleDeltaTempInOperation: boolean = false;

  // based on the chart maxPoints automatically select an appropriate interval
  autoSelectedInterval: Interval = UtilsServiceService.getStandardIntervals()[0];

  myForm = this.formBuilder.group({
    "chartDataPoints": ["", Validators.required],
    "customFromDate": ["", Validators.required],
    "customFromDateTimePart": ["", Validators.required],
    "customToDate": ["", Validators.required],
    "customToDateTimePart": ["", Validators.required],
    "intervalAutoMatching": ["", Validators.required],
  });


  onMyFormSubmit(): void {
  }

  public calculateAutoInterval() {
    this.autoSelectedInterval = this.utilsServiceService.getIntervalInSecondsForMaxDataPoints(this.myForm.value.chartDataPoints, this.getFromDate().toDate(), this.getToDate().toDate());
  }

  ngOnInit(): void {

    NoDataToDisplay(this.highcharts); // "enable" that required function
    theme(Highcharts);

    // subscribe to any form changes
    // this.myForm.controls['chartDataPoints'].valueChanges.subscribe(value => {
    this.myForm.valueChanges.subscribe(value => {
      //console.debug(value);
      this.calculateAutoInterval();
    });

    this.myForm.patchValue({
      chartDataPoints: 350, // how many data points to load initially (150 in 24 hrs results in about 15 min slots)
      intervalAutoMatching: true,
    });

    this.adjustTimeAndReload();

  }

  private adjustTimeAndReload() {

    let now = moment();
    let lastActiveSinceSeconds = now.diff(this.lastUserActivationTime, 'seconds');
    let updateDatesRequired = false;
    if (lastActiveSinceSeconds > 180) {
      updateDatesRequired = true;
    }

    console.log('adjustTimeAndReload. ' +
      ' lastActiveSinceSeconds: ' + lastActiveSinceSeconds
      + " updateDatesRequired:" + updateDatesRequired
      + " lastUserActivationTime:" + this.lastUserActivationTime.format());

    if (updateDatesRequired) {
      this.myForm.patchValue({
          customFromDate: moment().subtract(24, "hours").toDate(),
          customFromDateTimePart: moment().format('HH'),
          customToDate: moment().toDate(),
          customToDateTimePart: moment().add(1, "hours").format('HH'),
        }
      );

      this.lastUserActivationTime = now;

      if (this.router.url.indexOf("insights") > 0) {
        // this.snackBar.open("Datum für Graph aktualisiert", '', {
        //   duration: 2000
        // });
      }

      this.calculateAutoInterval();

    }

    this.myReload();
  }

  /**
   * Listener to catch if app gets active again
   */
  @HostListener('document:visibilitychange', ['$event'])
  visibilitychange() {
    console.log("document:visibilitychange called for boiler-chart");
    if (!document.hidden) {
      this.adjustTimeAndReload();
    }
  }

  getFromDate(): Moment {
    return moment(this.myForm.value.customFromDate)
      .startOf('day')
      .add(this.myForm.value.customFromDateTimePart, 'hours');
  }

  getToDate(): Moment {
    return moment(this.myForm.value.customToDate)
      .startOf('day')
      .add(this.myForm.value.customToDateTimePart, 'hours');
  }

  isIntervalAutoMatching(): boolean {
    return this.myForm.value.intervalAutoMatching;
  }

  myReload() {

    console.log("execute myReload");
    this.loading = true;
    this.loadingBoilerByHour = true;
    this.loadingBoilerByDayOfWeek = true;

    // check if we ask the date for an interval or for points
    let maxRows = 0;
    let groupEveryNthSecond = 0;
    if (this.isIntervalAutoMatching()) {
      groupEveryNthSecond = this.autoSelectedInterval.intervalInSeconds;
    } else {
      maxRows = this.myForm.value.chartDataPoints;
    }

    // TODO: make hard coded Station configurable (favorite station)
    let stationIds = new Set<string>();
    stationIds.add('KLO');
    stationIds.add('SHA');
    let serviceMeteoHistorical = this.heatingDataService.getMeteoSwissHistorical(true, this.getFromDate(), this.getToDate(), maxRows, groupEveryNthSecond, stationIds);
    let serviceHeatingDataHistorical = this.heatingDataService.getHistorical(true, this.getFromDate(), this.getToDate(), maxRows, groupEveryNthSecond);
    let serviceBoilerStatsByHour = this.heatingDataService.getBoilerStatsByHour(true, this.getFromDate(), this.getToDate());
    let serviceBoilerStatsDayOfWeek = this.heatingDataService.getBoilerStatsByDayOfWeek(true, this.getFromDate(), this.getToDate());
    let serviceSoleDeltaInOperationStats = this.heatingDataService.getSoleDeltaInOperationStats(true, this.getFromDate(), this.getToDate(), maxRows, groupEveryNthSecond);


    forkJoin([serviceHeatingDataHistorical, serviceMeteoHistorical]).subscribe({
      next: (results: any) => {

        let dataHeating = results[0];
        let dataMeteo = results[1];

        // reset the array (attention, creating a new one looses UI proxy object!!!)
        this.boilerTempMinMax.length = 0;
        this.boilerTempAverage.length = 0;
        this.boilerTempDeltaTemp.length = 0;
        this.boilerStatsByDayOfWeek.length = 0;
        this.boilerStatsByHour.length = 0;
        this.soleInTempMinMax.length = 0;
        this.soleOutTempMinMax.length = 0;
        this.soleTempDeltaInOperationAvg.length = 0;
        this.soleTempDeltaInOperationMinMax.length = 0;
        this.soleTempDelta.length = 0;
        this.heatingInTempMinMax.length = 0;
        this.heatingOutTempMinMax.length = 0;
        this.heatingTempDelta.length = 0;
        this.compressorHours.length = 0;
        this.outdoorTempAverage.length = 0;
        this.outdoorTempAverageMeteo1.length = 0;
        this.outdoorTempAverageMeteo2.length = 0;
        this.outdoorTempMinMax.length = 0;
        this.windGustMeteoSwiss.length = 0;
        //this.operationChartSeries.length = 0;

        let tempMin: number = 1E10;
        let prevTemp: number = 0;
        let prevtempFirst: boolean = true;

        let heatingEntites: HeatingEntity[] = [];
        dataHeating.map(e => heatingEntites.push(HeatingEntity.ofWebService(e)));
        heatingEntites.reverse(); // sort them for highcharts

        let meteoEntites: MeteoSwissEntity[] = [];
        dataMeteo.map(e => meteoEntites.push(MeteoSwissEntity.ofWebService(e)));
        meteoEntites.reverse(); // sort them for highcharts


        heatingEntites.forEach(heatingEntity => {

          // boiler
          this.boilerTempAverage.push([heatingEntity.measurementDate.getTime(), heatingEntity.boilerTemp]);
          this.boilerTempMinMax.push([heatingEntity.measurementDate.getTime(), heatingEntity.boilerTempMin, heatingEntity.boilerTempMax]);

          if (!prevtempFirst) {
            this.boilerTempDeltaTemp.push({
              x: heatingEntity.measurementDate.getTime(),
              y: heatingEntity.boilerTemp - prevTemp
            });
          }
          prevTemp = heatingEntity.boilerTemp;
          prevtempFirst = false;

          if (tempMin > heatingEntity.boilerTempMin) {
            tempMin = heatingEntity.boilerTempMin;
          }

          // sole
          this.soleInTempAverage.push([heatingEntity.measurementDate.getTime(), heatingEntity.soleIn]);
          this.soleInTempMinMax.push([heatingEntity.measurementDate.getTime(), heatingEntity.soleInMin, heatingEntity.soleInMax]);
          this.soleOutTempMinMax.push([heatingEntity.measurementDate.getTime(), heatingEntity.soleOutMin, heatingEntity.soleOutMax]);

          // sole delta between in and out
          this.soleTempDelta.push([heatingEntity.measurementDate.getTime(), heatingEntity.soleInMin - heatingEntity.soleOutMin, heatingEntity.soleInMax - heatingEntity.soleOutMax]);

          // heating
          this.heatingInTempMinMax.push([heatingEntity.measurementDate.getTime(), heatingEntity.heatingInMin, heatingEntity.heatingInMax]);
          this.heatingOutTempMinMax.push([heatingEntity.measurementDate.getTime(), heatingEntity.heatingOutMin, heatingEntity.heatingOutMax]);

          // heating delta between in and out
          this.heatingTempDelta.push([heatingEntity.measurementDate.getTime(), heatingEntity.heatingOutMin - heatingEntity.heatingInMin, heatingEntity.heatingOutMax - heatingEntity.heatingInMax]);

          // outdoor temperature
          this.outdoorTempAverage.push([heatingEntity.measurementDate.getTime(), heatingEntity.ireg300TempOutdoor]);
          this.outdoorTempMinMax.push([heatingEntity.measurementDate.getTime(), heatingEntity.ireg300TempOutdoorMin, heatingEntity.ireg300TempOutdoorMax]);

          // compressor hours
          this.compressorHours.push([heatingEntity.measurementDate.getTime(), heatingEntity.compressorHours]);

        });

        {
          // populate operation Chart data
          let operationEntries = new Map<string, string>();
          operationEntries.set("di10Compressor1", "Haupt Kompr");
          operationEntries.set("di14PumpDirect", "Pumpe direkt");
          operationEntries.set("di15PumpBoiler", "Pumpe Boiler");
          operationEntries.set("di17BoilerEl", "Boiler Elektro");
          operationEntries.set("di21PumpPrimary", "Primär Pumpe");
          operationEntries.set("di22pumpLoad", "Lade Pumpe");
          operationEntries.set("di70PumpHk1", "HK1 Pumpe");
          operationEntries.set("di71Hkm1ixOpen", "Hkm Auf");
          operationEntries.set("di72Hkm1ixClose", "Hkm Zu");
          operationEntries.set("di1Error", "Störung");

          if (this.operationsChartRef != null && this.operationsChartRef?.yAxis.length <= 1) {
            console.log("Operations-Chart need to add yAxis");

            let yNr = -1;
            operationEntries.forEach((key, value) => {
              yNr++;
              let yAxis = {
                top: 42 * yNr,
                height: 30,
                offset: 0,
                min: 0,
                max: 100,
                title: {
                  text: key,
                  rotation: 0,
                },
                gridLineWidth: 0,
                labels: {
                  enabled: false,
                },
                minorTickLength: 0,
                tickLength: 0,
              };

              if (this.operationsChartRef) {
                this.operationsChartRef.addAxis(yAxis, false, false, false);
              }

            });
          }

          let operationChartSeries: any = [];
          let yAxis: number = -1;
          operationEntries.forEach((seriesName, seriesProperty) => {
            // collect data
            let operationData: any = [];
            yAxis++;
            let isFirst: boolean = false;
            heatingEntites.forEach((heatingEntity) => {
              if (isFirst) {
                isFirst = false;
                operationData.push([heatingEntity.measurementDate.getTime() - 1, 0]); // to make the area cover the sub-line
              }
              operationData.push([heatingEntity.measurementDate.getTime(), heatingEntity[seriesProperty] * 100]);
            });

            // create a series entry
            // xxxxx
            let seriesEntry = {
              name: seriesName,
              data: operationData,
              type: 'area',
              // type: 'line',
              yAxis: yAxis,
              animation: false,
            };
            operationChartSeries.push(seriesEntry);
          });

          // https://stackblitz.com/edit/highcharts-angular-basic-line-swh9fw?file=src%2Fapp%2Fapp.component.ts
          // https://stackblitz.com/edit/highcharts-angular-basic-line-ehcvup?file=src/app/app.component.ts
          //console.log(JSON.stringify(this.operationChartSeries));
          // remove all present series first
          // var seriesLength = this.operationsChartRef.series.length;
          // for(var i = seriesLength-1; i>-1;i--){
          //   this.operationsChartRef.series[i].remove();
          // }
          if (this.operationsChartRef) {
            while (this.operationsChartRef.series.length > 0) {
              this.operationsChartRef.series[0].remove(true);
            }

            operationChartSeries.forEach(s => {
              if (this.operationsChartRef) {
                this.operationsChartRef.addSeries(s, false);
              }
            })

            //debugger;
          }
        }

        // populate Meteo-Swiss data
        meteoEntites.forEach(meteoEntity => {
          // TODO: hack, support multiple stations properly
          if (meteoEntity.stationId === 'KLO') {
            this.outdoorTempAverageMeteo1.push([meteoEntity.temperatureMeasureDate.getTime(), meteoEntity.temperature]);
            this.windGustMeteoSwiss.push([meteoEntity.windMeasureDate.getTime(), meteoEntity.windGustSpeedMax]);
          } else if (meteoEntity.stationId === 'SHA') {
            this.outdoorTempAverageMeteo2.push([meteoEntity.temperatureMeasureDate.getTime(), meteoEntity.temperature]);
          }
        });


        // ugly: need to fix min, as it's not taken automatically for area charts
        if (this.chartOptionsBoilerAverageTemp.yAxis && "min" in this.chartOptionsBoilerAverageTemp.yAxis) {
          this.chartOptionsBoilerAverageTemp.yAxis.min = tempMin;
        }
        this.chartUpdateFlag = true;
        console.log("General Charts reloaded with data. data-points: " + this.boilerTempAverage.length);
        // this.receivedNewTHValue.emit(this.chartUpdateFlag);
      },
      error: (e) => {
        console.log("error while loading data for chart", e);
        this.loading = false;
      },
      complete: () => {
        this.loading = false;
      }
    });


    serviceBoilerStatsByHour.subscribe({
      next: (boilerByHour: any) => {

        // populate Boiler Stats By Hour chart
        let boilerByHourStat = new Map<number, BoilerStatsByHourEntity>();
        boilerByHour.map(e => {
          let entity = BoilerStatsByHourEntity.ofWebService(e);
          boilerByHourStat.set(entity.hourOfTheDay, entity);
          this.boilerStatsByHourNumberOfStaticsRecords = entity.numOfStatisticRecords1; // same for all
        });

        // make sure we have a graph entry for all categories, even if not present in service result
        for (let i: number = 0; i <= 23; i++) {
          let entity = boilerByHourStat.get(i);
          if (entity == null) {
            this.boilerStatsByHour.push(0);
          } else {
            this.boilerStatsByHour.push(entity.sumBoilerDiffDecrease * -1);
          }
        }

        console.log("BoilerStatByHour Chart reloaded with data. data-points: " + boilerByHourStat.size);
        this.chartUpdateFlagBoilerStatsByHour = true;

      },
      error: (e) => {
        console.log("error while loading data for chart", e);
        this.loadingBoilerByHour = false;
      },
      complete: () => {
        this.loadingBoilerByHour = false;
      }
    });

    serviceBoilerStatsDayOfWeek.subscribe({
      next: (boilerByDayOfWeek: any) => {

        // populate Boiler Stats By Day of the Week
        let boilerByDayOfTheWeekStat = new Map<number, BoilerStatsDayOfWeekEntity>();
        boilerByDayOfWeek.map(e => {
          let entity = BoilerStatsDayOfWeekEntity.ofWebService(e);
          boilerByDayOfTheWeekStat.set(entity.dayOfWeekStartingMonday, entity);
          this.boilerStatsByDayOfWeekNumberOfStaticsRecords = entity.numOfStatisticRecords1; // same for all
        });

        // make sure we have a graph entry for all categories, even if not present in service result
        for (let i: number = 1; i <= 7; i++) {
          let entity = boilerByDayOfTheWeekStat.get(i);
          if (entity == null) {
            this.boilerStatsByDayOfWeek.push(0);
          } else {
            this.boilerStatsByDayOfWeek.push(entity.sumBoilerDiffDecrease * -1);
          }
        }

        console.log("BoilerStatByDayOfWeek Chart reloaded with data. data-points: " + boilerByDayOfWeek.length);
        this.chartUpdateFlagBoilerStatsByDayOfWeek = true;
      },
      error: (e) => {
        console.log("error while loading data for chart", e);
        this.loadingBoilerByDayOfWeek = false;
      },
      complete: () => {
        this.loadingBoilerByDayOfWeek = false;
      }
    });

    serviceSoleDeltaInOperationStats.subscribe({
      next: (soleDeltaInOperationStatsResults: any) => {

        soleDeltaInOperationStatsResults.forEach(soleDeltaInOperationStatsResult => {
          let soleDeltaInOpsEntity = SoleInOutDeltaInOperationStatEntity.ofWebService(soleDeltaInOperationStatsResult);
          this.soleTempDeltaInOperationMinMax.push([soleDeltaInOpsEntity.measurementDateStart.getTime(), soleDeltaInOpsEntity.soleInOutDeltaInOperationMin, soleDeltaInOpsEntity.soleInOutDeltaInOperationMax]);
          this.soleTempDeltaInOperationAvg.push([soleDeltaInOpsEntity.measurementDateStart.getTime(), soleDeltaInOpsEntity.soleInOutDeltaInOperationAvg]);
        });

        console.log("soleDeltaInOperationStats Chart reloaded with data. data-points: " + soleDeltaInOperationStatsResults.length);
        this.chartUpdateFlagSoleDeltaTempInOperation = true;
      },
      error: (e) => {
        console.log("error while loading data for chart", e);
        this.loadingSoleDeltaTempInOperation = false;
      },
      complete: () => {
        this.loadingSoleDeltaTempInOperation = false;
      }
    });
  }


  // get access to the real chart object
  operationsChartCallback: Highcharts.ChartCallbackFunction = chart => {
    console.log("Did call chart-callback");
    this.operationsChartRef = chart;
  };

  operationsChartRef?: Chart;

  subscribe = interval(1000 * 180).subscribe(
    val => {
      this.myReload();
    }
  );

  /************************************************************************************************
   * CHART: Boiler Average Temp (chartOptions)
   ************************************************************************************************/
  chartOptionsBoilerAverageTemp: Highcharts.Options = {
    series: [{
      name: 'Durchschnitt Temp',
      data: this.boilerTempAverage,
      zIndex: 1,
      type: 'line',
      lineWidth: 0,
      color: '#2596be',
      marker: {
        enabled: false
      }
    }, {
      name: 'Bereich',
      data: this.boilerTempMinMax,
      type: 'arearange',
      lineWidth: 2,
      linkedTo: ':previous',
      color: '#2596be',
      fillOpacity: 0.5,
      zIndex: 0,
      marker: {
        enabled: false
      }
    }],
    lang: {
      noData: '',
      loading: ''
    },
    time: {
      // super important setting! otherwise it's all UTC
      timezoneOffset: new Date().getTimezoneOffset()
    },
    credits: {
      enabled: false
    },
    xAxis: {
      type: 'datetime',
    },
    yAxis: {
      title: {
        text: ''
      },
      min: 0, // auto: seems not to work on area charts, calc it manually
      max: null,
      //tickInterval: 5,
      plotBands: [{
        zIndex: 200,
        from: 0,
        to: 42,
        color: 'rgba(38,132,255,0.15)',
        label: {
          text: 'Aufheiz-Zone (kühl)',
          style: {
            color: '#b7b7b7'
          }
        }
      }, {
        zIndex: 200,
        from: 42,
        to: 46,
        color: 'rgba(161,73,255,0.15)',
        label: {
          text: 'Aufheiz-Zone (lauwarm)',
          style: {
            color: '#b7b7b7'
          }
        }
      }, {
        zIndex: 200,
        from: 46,
        to: 51,
        color: 'rgba(255,10,10,0.15)',
        label: {
          text: 'Soll-Zone (warm)',
          style: {
            color: '#b7b7b7'
          }
        }
      }, {
        zIndex: 200,
        from: 51,
        to: 100,
        color: 'rgba(255,129,2,0.15)',
        label: {
          text: 'Legionellen Schaltung (sehr heiss)',
          style: {
            color: '#b7b7b7'
          }
        }
      }
      ]
    },
    tooltip: {
      //crosshairs: true,
      shared: true,
      valueDecimals: 2,
      valueSuffix: '°C',
      xDateFormat: '%A, %d.%m.%Y %H:%M',
      outside: true, // make sure the tooltip comes on top of labels
    },
    title: {
      text: ''
    },
    legend: {
      enabled: false
    },
    chart: {
      // spacingLeft: 5,
      // spacingRight: 2,
      backgroundColor: '#424242',
      animation: false,
      style: {
        fontFamily: 'Roboto'
      }
    }
  }


  /************************************************************************************************
   * CHART: Boiler Delta Temp (chartOptionsDeltaTemp)
   ************************************************************************************************/
    // https://jsfiddle.net/BlackLabel/52wfpdve/
  chartOptionsBoilerDeltaTemp: Highcharts.Options = {
    chart: {
      type: 'column',
      backgroundColor: '#424242',
      animation: false,
      style: {
        fontFamily: 'Roboto'
      }
    },
    series: [{
      type: 'column',
      data: this.boilerTempDeltaTemp
    }],
    plotOptions: {
      column: {
        pointPadding: 0.2,
        borderWidth: 0
      },
      series: {
        zones: [{
          value: 0,
          color: '#2596be'
        }, {
          color: '#be3c25'
        }]
      }
    },
    lang: {
      noData: '',
      loading: ''
    },
    time: {
      // super important setting! otherwise it's all UTC
      timezoneOffset: new Date().getTimezoneOffset()
    },
    credits: {
      enabled: false
    },
    xAxis: {
      type: 'datetime'
    },
    yAxis: {
      title: {
        text: ''
      },
      min: null, // auto: seems not to work on area charts, calc it manually
      max: null,
    },
    tooltip: {
      //crosshairs: true,
      shared: true,
      valueDecimals: 1,
      valueSuffix: '°C',
      xDateFormat: '%A, %d.%m.%Y %H:%M',
    },
    title: {
      text: ''
    },
    legend: {
      enabled: false
    }
  }

  /************************************************************************************************
   * CHART: BoilerStatsByHour
   ************************************************************************************************/
  chartOptionsBoilerStatsByHour: Highcharts.Options = {
    chart: {
      type: 'column',
      animation: false,
      backgroundColor: '#424242',
      style: {
        fontFamily: 'Roboto'
      }
    },
    series: [{
      name: 'Boiler Gebrauch nach Stunden',
      type: 'column',
      data: this.boilerStatsByHour,
      color: '#2596be',
    }],
    plotOptions: {
      column: {
        pointPadding: 0.2,
        borderWidth: 0
      }
    },
    lang: {
      noData: '',
      loading: '',
      thousandsSep: ''
    },
    time: {
      // super important setting! otherwise it's all UTC
      timezoneOffset: new Date().getTimezoneOffset()
    },
    credits: {
      enabled: false
    },
    xAxis: {
      title: {
        text: ''
      },
      min: null, // auto: seems not to work on area charts, calc it manually
      max: null,
      categories: [
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12',
        '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23',],
    },
    yAxis: {
      title: {
        text: ''
      }
    },
    tooltip: {
      //crosshairs: true,
      shared: true,
      valueDecimals: 1,
      valueSuffix: '°C Total per Stunde',
      xDateFormat: '%A, %d.%m.%Y %H:%M',
    },
    title: {
      text: ''
    },
    legend: {
      enabled: true,
      backgroundColor: '#424242'
    },
  }

  /************************************************************************************************
   * CHART: BoilerStatsBy Day Of Week
   ************************************************************************************************/
  chartOptionsBoilerStatsByDayOfWeek: Highcharts.Options = {
    chart: {
      type: 'column',
      animation: false,
      backgroundColor: '#424242',
      style: {
        fontFamily: 'Roboto'
      }
    },
    series: [{
      name: 'Boiler Gebrauch nach Wochentag',
      type: 'column',
      data: this.boilerStatsByDayOfWeek,
      color: '#2596be',
    }],
    plotOptions: {
      column: {
        pointPadding: 0.2,
        borderWidth: 0
      }
    },
    lang: {
      noData: '',
      loading: '',
      thousandsSep: ''
    },
    time: {
      // super important setting! otherwise it's all UTC
      timezoneOffset: new Date().getTimezoneOffset()
    },
    credits: {
      enabled: false
    },
    xAxis: {
      title: {
        text: ''
      },
      min: null, // auto: seems not to work on area charts, calc it manually
      max: null,
      categories: ['Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa', 'So'],
    },
    yAxis: {
      title: {
        text: ''
      }
    },
    tooltip: {
      //crosshairs: true,
      shared: true,
      valueDecimals: 1,
      valueSuffix: '°C Total pro Wochentag',
      xDateFormat: '%A, %d.%m.%Y %H:%M',
    },
    title: {
      text: ''
    },
    legend: {
      enabled: true,
      backgroundColor: '#424242'
    },
  }


  /************************************************************************************************
   * CHART: SoleTemp Area
   ************************************************************************************************/
  chartOptionsSoleTemp: Highcharts.Options = {
    series: [{
      name: 'Sole Eintritt',
      data: this.soleInTempMinMax,
      type: 'arearange',
      lineWidth: 2,
      color: '#2596be',
      fillOpacity: 0.5,
      zIndex: 0,
      marker: {
        enabled: false
      }
    }, {
      name: 'Sole Austritt',
      data: this.soleOutTempMinMax,
      type: 'arearange',
      lineWidth: 2,
      color: '#be3c25',
      fillOpacity: 0.5,
      zIndex: 1,
      marker: {
        enabled: false
      }
    }

    ],
    lang: {
      noData: '',
      loading: ''
    },
    time: {
      // super important setting! otherwise it's all UTC
      timezoneOffset: new Date().getTimezoneOffset()
    },
    credits: {
      enabled: false
    },
    xAxis: {
      type: 'datetime'
    },
    yAxis: {
      title: {
        text: ''
      },
      min: 0, // auto: seems not to work on area charts, calc it manually
      max: null,
      //tickInterval: 5,
    },
    tooltip: {
      //crosshairs: true,
      shared: true,
      valueDecimals: 1,
      valueSuffix: '°C',
      xDateFormat: '%A, %d.%m.%Y %H:%M',
    },
    title: {
      text: ''
    },
    legend: {
      enabled: true,
      backgroundColor: '#424242'
    },
    chart: {
      backgroundColor: '#424242',
      style: {
        fontFamily: 'Roboto'
      }
    }
  }

  /************************************************************************************************
   * CHART: SoleTemp-Delta between MinMaxIn and MinMaxOut Area
   ************************************************************************************************/
  chartOptionsSoleDeltaTemp: Highcharts.Options = {
    series: [{
      name: 'Sole Temperatur Unterschied',
      data: this.soleTempDelta,
      type: 'arearange',
      lineWidth: 2,
      color: '#2596be',
      fillOpacity: 0.5,
      zIndex: 0,
      marker: {
        enabled: false
      },
      zones: [{
        value: 0,
        color: '#2596be'
      }, {
        color: '#be3c25'
      }]
    }],
    lang: {
      noData: '',
      loading: ''
    },
    time: {
      // super important setting! otherwise, it's all UTC
      timezoneOffset: new Date().getTimezoneOffset()
    },
    credits: {
      enabled: false
    },
    xAxis: {
      type: 'datetime'
    },
    yAxis: {
      title: {
        text: ''
      },
      min: null, // auto: seems not to work on area charts, calc it manually
      max: null,
      //tickInterval: 5,
    },
    tooltip: {
      //crosshairs: true,
      shared: true,
      valueDecimals: 1,
      valueSuffix: '°C',
      xDateFormat: '%A, %d.%m.%Y %H:%M',
    },
    title: {
      text: ''
    },
    legend: {
      enabled: true,
      backgroundColor: '#424242'
    },
    chart: {
      backgroundColor: '#424242',
      animation: false,
      style: {
        fontFamily: 'Roboto'
      }
    }
  }

  /************************************************************************************************
   * CHART: SoleTemp-Delta-In-Operation between MinMaxIn and MinMaxOut Area
   ************************************************************************************************/
  chartOptionsSoleDeltaTempInOperation: Highcharts.Options = {
    series: [{
      name: 'Mittlerer Temp-Unterschied nach 3 min',
      data: this.soleTempDeltaInOperationAvg,
      zIndex: 1, // on top of area
      type: 'line',
      lineWidth: 3,
      color: '#be3c25',
      marker: {
        enabled: false
      }
    }, {
      name: 'Bereich (Min/Max)',
      data: this.soleTempDeltaInOperationMinMax,
      type: 'arearange',
      lineWidth: 0,
      linkedTo: ':previous',
      color: '#c7c7c7',
      fillOpacity: 0.25,
      zIndex: 0,
      marker: {
        enabled: false
      }
    }],
    lang: {
      noData: '',
      loading: ''
    },
    time: {
      // super important setting! otherwise it's all UTC
      timezoneOffset: new Date().getTimezoneOffset()
    },
    credits: {
      enabled: false
    },
    xAxis: {
      type: 'datetime',
    },
    yAxis: {
      title: {
        text: ''
      },
      min: null, // auto: seems not to work on area charts, calc it manually
      max: null,
      //tickInterval: 5,
    },
    tooltip: {
      //crosshairs: true,
      shared: true,
      valueDecimals: 2,
      valueSuffix: '°C',
      xDateFormat: '%A, %d.%m.%Y %H:%M',
      outside: true, // make sure the tooltip comes on top of labels
    },
    title: {
      text: ''
    },
    legend: {
      enabled: false
    },
    chart: {
      // spacingLeft: 5,
      // spacingRight: 2,
      backgroundColor: '#424242',
      animation: false,
      style: {
        fontFamily: 'Roboto'
      }
    }
  }

  /************************************************************************************************
   * CHART: Heating Temp Area
   ************************************************************************************************/
  chartOptionsHeatingTemp: Highcharts.Options = {
    series: [{
      name: 'Heizung Rücklauf',
      data: this.heatingInTempMinMax,
      type: 'arearange',
      lineWidth: 2,
      color: '#2596be',
      fillOpacity: 0.5,
      zIndex: 0,
      marker: {
        enabled: false
      }
    }, {
      name: 'Heizung Vorlauf',
      data: this.heatingOutTempMinMax,
      type: 'arearange',
      lineWidth: 2,
      color: '#be3c25',
      fillOpacity: 0.5,
      zIndex: 1,
      marker: {
        enabled: false
      }
    }],
    lang: {
      noData: '',
      loading: ''
    },
    time: {
      // super important setting! otherwise, it's all UTC
      timezoneOffset: new Date().getTimezoneOffset()
    },
    credits: {
      enabled: false
    },
    xAxis: {
      type: 'datetime'
    },
    yAxis: {
      title: {
        text: ''
      },
      min: 0, // auto: seems not to work on area charts, calc it manually
      max: null,
      //tickInterval: 5,
    },
    tooltip: {
      //crosshairs: true,
      shared: true,
      valueDecimals: 1,
      valueSuffix: '°C',
      xDateFormat: '%A, %d.%m.%Y %H:%M',
    },
    title: {
      text: ''
    },
    legend: {
      enabled: true,
      backgroundColor: '#424242'
    },
    chart: {
      backgroundColor: '#424242',
      animation: false,
      style: {
        fontFamily: 'Roboto'
      }
    }
  }

  /************************************************************************************************
   * CHART: Heating area between MinMaxIn and MinMaxOut Area
   ************************************************************************************************/
  chartOptionsHeatingDeltaTemp: Highcharts.Options = {
    series: [{
      name: 'Heizung Temperatur Unterschied',
      data: this.heatingTempDelta,
      type: 'arearange',
      lineWidth: 2,
      color: '#2596be',
      fillOpacity: 0.5,
      zIndex: 0,
      marker: {
        enabled: false
      },
      zones: [{
        value: 0,
        color: '#2596be'
      }, {
        color: '#be3c25'
      }]
    }],
    lang: {
      noData: '',
      loading: ''
    },
    time: {
      // super important setting! otherwise, it's all UTC
      timezoneOffset: new Date().getTimezoneOffset()
    },
    credits: {
      enabled: false
    },
    xAxis: {
      type: 'datetime'
    },
    yAxis: {
      title: {
        text: ''
      },
      min: null, // auto: seems not to work on area charts, calc it manually
      max: null,
      //tickInterval: 5,
    },
    tooltip: {
      //crosshairs: true,
      shared: true,
      valueDecimals: 1,
      valueSuffix: '°C',
      xDateFormat: '%A, %d.%m.%Y %H:%M',
    },
    title: {
      text: ''
    },
    legend: {
      enabled: true,
      backgroundColor: '#424242'
    },
    chart: {
      backgroundColor: '#424242',
      animation: false,
      style: {
        fontFamily: 'Roboto'
      }
    }
  }


  /************************************************************************************************
   * CHART: compressor hours
   ************************************************************************************************/
  chartOptionsCompressorHours: Highcharts.Options = {
    chart: {
      type: 'line',
      animation: false,
      backgroundColor: '#424242',
      style: {
        fontFamily: 'Roboto'
      }
    },
    series: [{
      type: 'line',
      data: this.compressorHours,
      color: '#2596be'
    }],
    plotOptions: {
      column: {
        pointPadding: 0.2,
        borderWidth: 0
      },
      series: {
        lineWidth: 3,
        marker: {
          enabled: false
        }
      }
    },
    lang: {
      noData: '',
      loading: '',
      thousandsSep: ''
    },
    time: {
      // super important setting! otherwise it's all UTC
      timezoneOffset: new Date().getTimezoneOffset()
    },
    credits: {
      enabled: false
    },
    xAxis: {
      type: 'datetime'
    },
    yAxis: {
      title: {
        text: ''
      },
      min: null, // auto: seems not to work on area charts, calc it manually
      max: null
    },
    tooltip: {
      //crosshairs: true,
      shared: true,
      valueDecimals: 1,
      valueSuffix: ' h',
      xDateFormat: '%A, %d.%m.%Y %H:%M',
      // pointFormat: '{point.y} h'
    },
    title: {
      text: ''
    },
    legend: {
      enabled: false
    }
  }

  /************************************************************************************************
   * CHART: Operation chart xxx
   ************************************************************************************************/
  chartOptionsOperationsChart: Highcharts.Options = {
    chart: {
      animation: false,
      backgroundColor: '#424242',
      styledMode: false,
      style: {
        fontFamily: 'Roboto'
      },
    },
    plotOptions: {
      series: {
        color: '#2596be',
        marker: {
          enabled: false,
          symbol: 'circle',
        }
      }
    },
    time: {
      // super important setting! otherwise it's all UTC
      timezoneOffset: new Date().getTimezoneOffset()
    },
    xAxis: {
      type: 'datetime',
      gridLineWidth: 0,
      lineWidth: 0,
    },
    yAxis: [], // dynamically added, as well as series
    title: {
      text: ''
    },
    tooltip: {
      //crosshairs: true,
      shared: true,
      valueDecimals: 2,
      valueSuffix: '%',
      xDateFormat: '%A, %d.%m.%Y %H:%M',
      //pointFormat: '{point.y } h'
    },
    legend: {
      enabled: false
    },
    credits: {
      enabled: false
    }
  }

  /************************************************************************************************
   * CHART: outdoor temperature
   ************************************************************************************************/
  chartOptionsOutdoorTemperature
    :
    Highcharts
      .Options = {
    chart: {
      type: 'line',
      animation: false,
      backgroundColor: '#424242',
      style: {
        fontFamily: 'Roboto'
      }
    },
    series: [{
      name: 'Büelwisen Sensor',
      type: 'line',
      data: this.outdoorTempAverage,
      color: '#2596be',
      lineWidth: 3,
    }, {
      name: 'Meteo-Schweiz (Kloten)',
      type: 'line',
      data: this.outdoorTempAverageMeteo1,
      color: '#518663',
      //dashStyle: 'ShortDot',
      lineWidth: 2,
    },
      {
        name: 'Meteo-Schweiz (Schaffhausen)',
        type: 'line',
        data: this.outdoorTempAverageMeteo2,
        color: '#8c4522',
        //dashStyle: 'ShortDot',
        lineWidth: 2,
      }],
    plotOptions: {
      column: {
        pointPadding: 0.2,
        borderWidth: 0
      },
      series: {
        marker: {
          enabled: false
        }
      }
    },
    lang: {
      noData: '',
      loading: '',
      thousandsSep: ''
    },
    time: {
      // super important setting! otherwise it's all UTC
      timezoneOffset: new Date().getTimezoneOffset()
    },
    credits: {
      enabled: false
    },
    xAxis: {
      type: 'datetime'
    },
    yAxis: {
      title: {
        text: ''
      },
      min: null,
      max: null,
      minRange: 20,
    },
    tooltip: {
      //crosshairs: true,
      shared: true,
      valueDecimals: 1,
      valueSuffix: '°C',
      xDateFormat: '%A, %d.%m.%Y %H:%M',
      // pointFormat: '{point.y} h'
    },
    title: {
      text: ''
    },
    legend: {
      enabled: true,
      backgroundColor: '#424242'
    },
  }

  /************************************************************************************************
   * CHART: Wind
   ************************************************************************************************/
  chartOptionsWindGustMeteo: Highcharts.Options = {
    chart: {
      type: 'line',
      animation: false,
      backgroundColor: '#424242',
      style: {
        fontFamily: 'Roboto'
      }
    },
    series: [{
      name: 'Wind Spitze (Meteo Schweiz)',
      type: 'line',
      data: this.windGustMeteoSwiss,
      color: '#2596be',
    }],
    plotOptions: {
      column: {
        pointPadding: 0.2,
        borderWidth: 0
      },
      series: {
        lineWidth: 3,
        marker: {
          enabled: false
        }
      }
    },
    lang: {
      noData: '',
      loading: '',
      thousandsSep: ''
    },
    time: {
      // super important setting! otherwise it's all UTC
      timezoneOffset: new Date().getTimezoneOffset()
    },
    credits: {
      enabled: false
    },
    xAxis: {
      type: 'datetime'
    },
    yAxis: {
      title: {
        text: ''
      },
      min: null, // auto: seems not to work on area charts, calc it manually
      max: null
    },
    tooltip: {
      //crosshairs: true,
      shared: true,
      valueDecimals: 1,
      valueSuffix: 'km/h',
      xDateFormat: '%A, %d.%m.%Y %H:%M',
    },
    title: {
      text: ''
    },
    legend: {
      enabled: true,
      backgroundColor: '#424242'
    },
  }


}
