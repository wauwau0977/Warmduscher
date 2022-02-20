package com.x8ing.thsensor.thserver.db.entity.analytics;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"hourOfTheDay", "sumBoilerDiffDecrease", "sumBoilerDiffIncrease", "numOfStatisticRecords1"})
public interface BoilerStatsByHour {

    Integer getHourOfTheDay();

    Double getSumBoilerDiffDecrease();

    Double getSumBoilerDiffIncrease();

//    Double getMaxBoilerTemp();
//
//    Double getMinBoilerTemp();
//
//    Double getAvgBoilerTemp();

    Long getNumOfStatisticRecords1();
}
