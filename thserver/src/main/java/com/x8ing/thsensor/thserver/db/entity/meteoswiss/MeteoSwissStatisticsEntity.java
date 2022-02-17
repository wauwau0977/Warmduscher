package com.x8ing.thsensor.thserver.db.entity.meteoswiss;

import com.x8ing.thsensor.thserver.utils.UUIDUtils;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Immutable
public class MeteoSwissStatisticsEntity {

    @Id
    private String id = UUIDUtils.generateShortTextUUID();

    private String stationId;
    private String stationName;

    private Double temperature;
    private Double temperatureMin;
    private Double temperatureMax;

    private Date temperatureMeasureDate;
    private Date temperatureMeasureDateMin;
    private Date temperatureMeasureDateMax;

    private Double windGustSpeed;
    private Double windGustSpeedMin;
    private Double windGustSpeedMax;

    private Date  windMeasureDate;
    private Date  windMeasureDateMin;
    private Date  windMeasureDateMax;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(Double temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public Double getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(Double temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public Date getTemperatureMeasureDate() {
        return temperatureMeasureDate;
    }

    public void setTemperatureMeasureDate(Date temperatureMeasureDate) {
        this.temperatureMeasureDate = temperatureMeasureDate;
    }

    public Date getTemperatureMeasureDateMin() {
        return temperatureMeasureDateMin;
    }

    public void setTemperatureMeasureDateMin(Date temperatureMeasureDateMin) {
        this.temperatureMeasureDateMin = temperatureMeasureDateMin;
    }

    public Date getTemperatureMeasureDateMax() {
        return temperatureMeasureDateMax;
    }

    public void setTemperatureMeasureDateMax(Date temperatureMeasureDateMax) {
        this.temperatureMeasureDateMax = temperatureMeasureDateMax;
    }

    public Double getWindGustSpeed() {
        return windGustSpeed;
    }

    public void setWindGustSpeed(Double windGustSpeed) {
        this.windGustSpeed = windGustSpeed;
    }

    public Double getWindGustSpeedMin() {
        return windGustSpeedMin;
    }

    public void setWindGustSpeedMin(Double windGustSpeedMin) {
        this.windGustSpeedMin = windGustSpeedMin;
    }

    public Double getWindGustSpeedMax() {
        return windGustSpeedMax;
    }

    public void setWindGustSpeedMax(Double windGustSpeedMax) {
        this.windGustSpeedMax = windGustSpeedMax;
    }

    public Date getWindMeasureDate() {
        return windMeasureDate;
    }

    public void setWindMeasureDate(Date windMeasureDate) {
        this.windMeasureDate = windMeasureDate;
    }

    public Date getWindMeasureDateMin() {
        return windMeasureDateMin;
    }

    public void setWindMeasureDateMin(Date windMeasureDateMin) {
        this.windMeasureDateMin = windMeasureDateMin;
    }

    public Date getWindMeasureDateMax() {
        return windMeasureDateMax;
    }

    public void setWindMeasureDateMax(Date windMeasureDateMax) {
        this.windMeasureDateMax = windMeasureDateMax;
    }
}
