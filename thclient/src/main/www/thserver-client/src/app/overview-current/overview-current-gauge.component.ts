import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {HeatingDataService} from "../heating-data.service";
import {interval} from 'rxjs';
import {HeatingEntity} from "../entities/heatingEntity";
import * as Highcharts from "highcharts";

import highchartsMore from "highcharts/highcharts-more.js"
import solidGauge from "highcharts/modules/solid-gauge.js";
import theme from 'highcharts/themes/dark-unica';


@Component({
  selector: 'app-overview-current-gauge',
  templateUrl: './overview-current-gauge.component.html',
  styleUrls: ['./overview-current-gauge.component.sass']
})
export class OverviewCurrentGaugeComponent implements OnInit {

  constructor(private heatingDataService: HeatingDataService) {
  }

  // HighchartsMore(Highcharts);

  ngOnInit(): void {
    this.myReload();
    //timer(2000,2000).subscribe()
    //new Observable().subscribe();

    highchartsMore(Highcharts);
    solidGauge(Highcharts);
    theme(Highcharts);

  }

  @Output() receivedNewTHValue = new EventEmitter();
  heatingEntity: HeatingEntity = HeatingEntity.emptyInstance();
  highcharts: typeof Highcharts = Highcharts;


  myReload() {
    return this.heatingDataService.getCurrent(false)
      .subscribe((data: any) => {
        this.heatingEntity = HeatingEntity.ofWebService(data);
        this.receivedNewTHValue.emit(data);
      });
  }

  subscribe = interval(10000).subscribe(
    val => {
      this.myReload();
    }
  );

  gaugeChartOptions: Highcharts.Options = {

    chart: {
      type: 'gauge',
      plotBorderWidth: 0,
      plotShadow: false
    },

    title: {
      text: 'Speedometer'
    },

    pane: {
      startAngle: -150,
      endAngle: 150,

    },

    // the value axis
    yAxis: [{
      min: 20,
      max: 65,

      minorTickInterval: 'auto',
      minorTickWidth: 1,
      minorTickLength: 10,
      minorTickPosition: 'inside',
      minorTickColor: '#666',

      tickPixelInterval: 30,
      tickWidth: 2,
      tickPosition: 'inside',
      tickLength: 10,
      tickColor: '#666',
      labels: {
        step: 2,
        //rotation: 'auto'
      },
      title: {
        text: 'km/h'
      },
      plotBands: [{
        from: 20,
        to: 30,
        color: '#55BF3B' // green
      }, {
        from: 30,
        to: 50,
        color: '#DDDF0D' // yellow
      }, {
        from: 50,
        to: 65,
        color: '#DF5353' // red
      }]
    }],

    series: [{
      name: 'Speed',
      type: 'gauge',
      data: [80],
      tooltip: {
        valueSuffix: ' km/h'
      }
    }]


  }

}
