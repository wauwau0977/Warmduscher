import {Injectable} from '@angular/core';

import {environment} from "../environments/environment";
import * as moment from "moment";
import {Moment} from "moment";
import {CacheService} from "./cache/cache.service";
import {HttpClient, HttpParams} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class HeatingDataService {

  constructor(private http: HttpClient, private cacheService: CacheService) {
  }

  serviceBaseURL = environment.serviceBaseURL;

  private static readonly CACHE_KEY_HISTORICAL: string = "CACHE_KEY_HISTORICAL";
  private static readonly CACHE_KEY_CURRENT: string = "CACHE_KEY_CURRENT";

  private static readonly CACHE_KEY_METEO_HISTORICAL: string = "CACHE_KEY_METEO_HISTORICAL";
  private static readonly CACHE_KEY_METEO_CURRENT: string = "CACHE_KEY_METEO_CURRENT";

  private static readonly CACHE_KEY_BOILER_STATS_BY_DAY_OF_THE_WEEK: string = "CACHE_KEY_BOILER_STATS_BY_DAY_OF_THE_WEEK";
  private static readonly CACHE_KEY_BOILER_STATS_BY_HOUR: string = "CACHE_KEY_BOILER_STATS_BY_HOUR";

  static convertDate(utcDateText: string) {
    let x1 = moment.utc(utcDateText);
    return x1.toDate();
  }

  static convertDateToTime(utcDateText: string) {
    let x1 = moment.utc(utcDateText);
    return x1.toDate().getTime();
  }

  getCurrent(evictCache: boolean) {
    return this.cacheService.get(
      HeatingDataService.CACHE_KEY_CURRENT,
      () => this.http.get(this.serviceBaseURL + '/heatpump-data/current'),
      evictCache);
  }

  getHistorical(evictCache: boolean, from: Moment, to: Moment, maxRows: number, groupEveryNthSecond: number) {
    let p = new HttpParams()
      .set('start', from.toJSON())
      .set('end', to.toJSON())
      .set('maxRows', maxRows)
      .set('groupEveryNthSecond', groupEveryNthSecond)
    ;

    return this.cacheService.get(
      HeatingDataService.CACHE_KEY_HISTORICAL,
      () => this.http.get(this.serviceBaseURL + '/heatpump-data/getBetweenDates', {params: p}),
      evictCache);
  }

  getServerInfo() {
    return this.http.get(this.serviceBaseURL + '/info/general');
  }

  getMeteoSwissCurrent(evictCache: boolean, stationId: string) {
    let p = new HttpParams()
      .set('stationId', stationId)
    ;

    return this.cacheService.get(
      HeatingDataService.CACHE_KEY_METEO_CURRENT,
      () => this.http.get(this.serviceBaseURL + '/meteo-swiss/current', {params: p}),
      evictCache);
  }

  getMeteoSwissHistorical(evictCache: boolean, from: Moment, to: Moment, maxRows: number, groupEveryNthSecond: number, stationIds?: Set<string>, doNotCache?: boolean) {

    let p = new HttpParams()
      .set('start', from.toJSON())
      .set('end', to.toJSON())
      .set('maxRows', maxRows)
      .set('groupEveryNthSecond', groupEveryNthSecond)
    ;

    if (stationIds != null) {
      let stationIdList: string = '';
      stationIds.forEach(s => stationIdList = stationIdList + s + ",");
      p = p.set('stationId', stationIdList);
    }

    return this.cacheService.get(
      HeatingDataService.CACHE_KEY_METEO_HISTORICAL,
      () => this.http.get(this.serviceBaseURL + '/meteo-swiss/getBetweenDates', {params: p}),
      evictCache, doNotCache);
  }

  getBoilerStatsByDayOfWeek(evictCache: boolean, from: Moment, to: Moment) {
    let p = new HttpParams()
      .set('start', from.toJSON())
      .set('end', to.toJSON());

    return this.cacheService.get(
      HeatingDataService.CACHE_KEY_BOILER_STATS_BY_DAY_OF_THE_WEEK,
      () => this.http.get(this.serviceBaseURL + '/heatpump-data/getBoilerStatsByDayOfWeek', {params: p}),
      evictCache);
  }

  getBoilerStatsByHour(evictCache: boolean, from: Moment, to: Moment) {
    let p = new HttpParams()
      .set('start', from.toJSON())
      .set('end', to.toJSON());

    return this.cacheService.get(
      HeatingDataService.CACHE_KEY_BOILER_STATS_BY_HOUR,
      () => this.http.get(this.serviceBaseURL + '/heatpump-data/getBoilerStatsByHour', {params: p}),
      evictCache);
  }

}
