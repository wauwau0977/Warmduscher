package com.x8ing.thsensor.thserver.db.entity.analytics;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Date;

@JsonPropertyOrder({"measurementDateStart", "measurementDateEnd", "soleInOutDeltaInOperationAvg", "soleInOutDeltaInOperationMin", "soleInOutDeltaInOperationMax", "compressorState", "totalNumberOfProbesInSampleWindow"})
public interface SoleInOutDeltaInOperationStats {

    Date getMeasurementDateStart();

    Date getMeasurementDateEnd();

    Double getSoleInOutDeltaInOperationAvg();

    Double getSoleInOutDeltaInOperationMin();

    Double getSoleInOutDeltaInOperationMax();

    Boolean getCompressorState();

    Integer getTotalNumberOfProbesInSampleWindow();

}
