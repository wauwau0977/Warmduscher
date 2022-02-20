package com.x8ing.thsensor.thserver.db.entity.analytics;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"dayOfWeekStartingMonday", "dayOfWeekText", "sumBoilerDiffDecrease", "sumBoilerDiffIncrease", "numOfStatisticRecords1"})
public interface BoilerStatsByDayOfWeek {

    Integer getDayOfWeekStartingMonday();

    String getDayOfWeekText();

    Double getSumBoilerDiffDecrease();

    Double getSumBoilerDiffIncrease();

    Long getNumOfStatisticRecords1();
}
