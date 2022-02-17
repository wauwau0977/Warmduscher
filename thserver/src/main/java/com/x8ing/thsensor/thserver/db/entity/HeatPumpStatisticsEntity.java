package com.x8ing.thsensor.thserver.db.entity;

import com.x8ing.thsensor.thserver.utils.UUIDUtils;

import java.util.Date;

public interface HeatPumpStatisticsEntity {

    default String getId() {
        return UUIDUtils.generateShortTextUUID();
    }

    Double getBoilerTemp();

    Integer getCompressorHours();

    Double getHeatingIn();

    Double getHeatingOut();

    Double getSoleIn();

    Double getSoleOut();

    Date getMeasurementDate();

    Double getBoilerTempMin();

    Double getBoilerTempMax();

    Integer getCompressorHoursMin();

    Integer getCompressorHoursMax();

    Double getHeatingInMin();

    Double getHeatingInMax();

    Double getHeatingOutMin();

    Double getHeatingOutMax();

    Double getSoleInMin();

    Double getSoleInMax();

    Double getSoleOutMin();

    Double getSoleOutMax();

    Date getMeasurementDateMin();

    Date getMeasurementDateMax();

    Double getIreg300TempOutdoor();

    Double getIreg300TempOutdoorMin();

    Double getIreg300TempOutdoorMax();

    Double getDi1Error();

    Double getDi10Compressor1();

    Double getDi14PumpDirect();

    Double getDi15PumpBoiler();

    Double getDi17BoilerEl();

    Double getDi21PumpPrimary();

    Double getDi22pumpLoad();

    Double getDi70PumpHk1();

    Double getDi71Hkm1ixOpen();

    Double getDi72Hkm1ixClose();
}
