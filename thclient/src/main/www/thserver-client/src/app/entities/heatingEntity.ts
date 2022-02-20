import {HeatingDataService} from "../heating-data.service";

export class HeatingEntity {

  constructor(public id: string | null,
              public measurementDate: Date,
              public boilerTemp: number,
              public boilerTempMin: number,
              public boilerTempMax: number,
              public compressorHours: number,
              public heatingIn: number,
              public heatingInMin: number,
              public heatingInMax: number,
              public heatingOut: number,
              public heatingOutMin: number,
              public heatingOutMax: number,
              public soleIn: number,
              public soleInMin: number,
              public soleInMax: number,
              public soleOut: number,
              public soleOutMin: number,
              public soleOutMax: number,
              public ireg300TempOutdoor: number,
              public ireg300TempOutdoorMin: number,
              public ireg300TempOutdoorMax: number,
              public di1Error: number,
              public di10Compressor1: number,
              public di14PumpDirect: number,
              public di15PumpBoiler: number,
              public di17BoilerEl: number,
              public di21PumpPrimary: number,
              public di22pumpLoad: number,
              public di70PumpHk1: number,
              public di71Hkm1ixOpen: number,
              public di72Hkm1ixClose: number,
  ) {

  }

  static emptyInstance() {
    return new HeatingEntity(null, new Date(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    );
  }

  static ofWebService(data: any) {
    if (data == null) {
      return this.emptyInstance();
    } else {
      return new HeatingEntity(
        data.id,
        HeatingDataService.convertDate(data.measurementDate),
        data.boilerTemp,
        data.boilerTempMin,
        data.boilerTempMax,
        data.compressorHours,
        data.heatingIn,
        data.heatingInMin,
        data.heatingInMax,
        data.heatingOut,
        data.heatingOutMin,
        data.heatingOutMax,
        data.soleIn,
        data.soleInMin,
        data.soleInMax,
        data.soleOut,
        data.soleOutMin,
        data.soleOutMax,
        data.ireg300TempOutdoor,
        data.ireg300TempOutdoorMin,
        data.ireg300TempOutdoorMax,
        data.di1Error,
        data.di10Compressor1,
        data.di14PumpDirect,
        data.di15PumpBoiler,
        data.di17BoilerEl,
        data.di21PumpPrimary,
        data.di22pumpLoad,
        data.di70PumpHk1,
        data.di71Hkm1ixOpen,
        data.di72Hkm1ixClose,
      );
    }
  }
}
