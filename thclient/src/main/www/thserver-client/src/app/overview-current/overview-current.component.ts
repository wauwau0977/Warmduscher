import {Component, EventEmitter, HostListener, OnInit, Output} from '@angular/core';
import {HeatingDataService} from "../heating-data.service";
import {interval} from 'rxjs';
import {HeatingEntity} from "../entities/heatingEntity";
import {MeteoSwissEntity} from "../entities/meteoSwissEntity";
import * as moment from "moment";

// import * as Highcharts from "highcharts";
// import highchartsMore from "highcharts/highcharts-more.js"
// import solidGauge from "highcharts/modules/solid-gauge.js";
// import theme from 'highcharts/themes/dark-unica';


@Component({
  selector: 'app-overview-current',
  templateUrl: './overview-current.component.html',
  styleUrls: ['./overview-current.component.sass']
})
export class OverviewCurrentComponent implements OnInit {

  constructor(private heatingDataService: HeatingDataService) {
  }

  // HighchartsMore(Highcharts);
  lastServiceRefresh: Date = new Date(2000, 1, 1);

  ngOnInit(): void {
    this.myReload();
  }

  //@Output() receivedNewTHValue = new EventEmitter();
  heatingEntity: HeatingEntity = HeatingEntity.emptyInstance();

  //@Output() receivedNewMeteoValue = new EventEmitter();
  meteoSwissEntity: MeteoSwissEntity = MeteoSwissEntity.emptyInstance();

  myReload() {

    this.heatingDataService.getMeteoSwissCurrent(true, "KLO").subscribe(data => {
      this.meteoSwissEntity = MeteoSwissEntity.ofWebService(data);
      //this.receivedNewMeteoValue.emit(data);
    });

    let dateFrom = moment().subtract(24, "hours");
    let dateTo = moment();

    this.heatingDataService.getMeteoSwissHistorical(true, dateFrom, dateTo, 1, 0, new Set<string>().add("KLO")).subscribe(data => {
      console.log("Completed service call historic meteo:", data);
      // TODO: Implementation not yet done
    });


    return this.heatingDataService.getCurrent(true)
      .subscribe((data: any) => {
        this.heatingEntity = HeatingEntity.ofWebService(data);
        //this.receivedNewTHValue.emit(data);
      });
  }

  subscribe = interval(1000).subscribe(
    val => {
      // do more often intervals, to have better control in app case if it wakes up after a long sleep
      let now = new Date();
      let refreshBackendInterval = 30000;
      if ((now.getTime() - this.lastServiceRefresh.getTime() > refreshBackendInterval)) {
        console.log("Service refresh required. last one was " + this.lastServiceRefresh);
        this.lastServiceRefresh = now;
        this.myReload();
      }
    }
  );

  /**
   * Listener to catch if app gets active again
   */
  @HostListener('document:visibilitychange', ['$event'])
  visibilitychange() {
    console.log("document:visibilitychange called for overview-current");
    if (!document.hidden) {
      console.log("Detected reactivation of browser window. About to refresh.", new Date());
      this.myReload();
    }
  }

  getShowerRecommendation(): String {

    let boilerTemp = this.heatingEntity.boilerTemp;
    if (boilerTemp > 60) {
      return "Super heiss: Die Legionellenschaltung hat alles gegeben.";
    } else if (boilerTemp > 57) {
      return "Super heiss: Wahrscheinlich wegen Legionellen-Schaltung.";
    } else if (boilerTemp > 55) {
      return "Sehr heiss: Da könnte man ganze Badewannen füllen.";
    } else if (boilerTemp > 52) {
      return "Doch eher heiss: Dein Duschtraum wird wahr.";
    } else if (boilerTemp > 51) {
      return "Doch eher heiss: Für Profi Heiss-Duscher.";
    } else if (boilerTemp > 50) {
      return "Heiss: Duschspass garantiert für Heiss-Duscher.";
    } else if (boilerTemp > 45) {
      return "Heiss: Duschspass garantiert für Warm-Duscher.";
    } else if (boilerTemp > 47) {
      return "Ziemlich heiss. Komfortable Warm-Dusche möglich.";
    } else if (boilerTemp > 42) {
      return "Warm: Ja, ganz gut...";
    } else if (boilerTemp > 40) {
      return "Warm: Is noch okey...";
    } else if (boilerTemp > 39) {
      return "Warm: Immernoch genug warm...";
    } else if (boilerTemp > 38) {
      return "Lauwarm: Kurze Dusche ok...";
    } else if (boilerTemp > 37) {
      return "Lauwarm: Könnte erfrischend werden, oder kurz warten.";
    } else if (boilerTemp > 36) {
      return "Kühl: Kleine Mutprobe?";
    } else if (boilerTemp > 35) {
      return "Sehr kühl: Nur für Eisbären geeignet...";
    } else if (boilerTemp > 34) {
      return "Polar Kalt: Selbst Eisbären überlegen sich das...";
    } else if (boilerTemp > 33) {
      return "Kurz vor dem Gefrierpunkt ;-)";
    } else if (boilerTemp > 32) {
      return "Wusste nicht, dass es so kalt werden kann. ";
    }

    return "Stromausfall oder wird es wirklich jemals soo kalt?";
  }

}
