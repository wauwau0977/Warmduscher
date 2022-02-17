import {HeatingDataService} from "./heating-data.service";

export class MeteoSwissEntity {

  constructor(public id: string,
              public stationId: string,
              public stationName: string,
              public temperature: number,
              public temperatureMin: number,
              public temperatureMax: number,
              public temperatureMeasureDate: Date,
              public temperatureMeasureDateMin: Date,
              public temperatureMeasureDateMax: Date,
              public windGustSpeed: number,
              public windGustSpeedMin: number,
              public windGustSpeedMax: number,
              public windMeasureDate: Date,
              public windMeasureDateMin: Date,
              public windMeasureDateMax: Date,
  ) {

  }

  static emptyInstance() {
    return new MeteoSwissEntity("", "", "", 0, 0, 0, new Date(), new Date(), new Date(), 0, 0, 0, new Date(), new Date(), new Date());
  }

  static ofWebService(data: any) {
    if (data == null) {
      return this.emptyInstance();
    } else {
      return new MeteoSwissEntity(
        data.id,
        data.stationId,
        data.stationName,
        data.temperature,
        data.temperatureMin,
        data.temperatureMax,
        HeatingDataService.convertDate(data.temperatureMeasureDate),
        HeatingDataService.convertDate(data.temperatureMeasureDateMin),
        HeatingDataService.convertDate(data.temperatureMeasureDateMax),
        data.windGustSpeed,
        data.windGustSpeedMin,
        data.windGustSpeedMax,
        HeatingDataService.convertDate(data.windMeasureDate),
        HeatingDataService.convertDate(data.windMeasureDateMin),
        HeatingDataService.convertDate(data.windMeasureDateMax),
      );
    }
  }
}
