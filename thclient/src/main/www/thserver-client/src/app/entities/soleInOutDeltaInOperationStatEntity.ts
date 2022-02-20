import {HeatingDataService} from "../heating-data.service";

export class SoleInOutDeltaInOperationStatEntity {

  constructor(
    public measurementDateStart: Date,
    public measurementDateEnd: Date,
    public soleInOutDeltaInOperationAvg: number,
    public soleInOutDeltaInOperationMin: number,
    public soleInOutDeltaInOperationMax: number,
    public compressorState: boolean,
    public totalNumberOfProbesInSampleWindow: number,
  ) {
  }

  static emptyInstance() {
    return new SoleInOutDeltaInOperationStatEntity(new Date(), new Date(), 0, 0, 0, false, 0);
  }

  static ofWebService(data: any): SoleInOutDeltaInOperationStatEntity {
    if (data == null) {
      return this.emptyInstance();
    } else {
      return new SoleInOutDeltaInOperationStatEntity(
        HeatingDataService.convertDate(data.measurementDateStart),
        HeatingDataService.convertDate(data.measurementDateEnd),
        data.soleInOutDeltaInOperationAvg,
        data.soleInOutDeltaInOperationMin,
        data.soleInOutDeltaInOperationMax,
        data.compressorState,
        data.totalNumberOfProbesInSampleWindow
      );
    }
  }
}
