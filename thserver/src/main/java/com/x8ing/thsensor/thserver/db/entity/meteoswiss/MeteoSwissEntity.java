package com.x8ing.thsensor.thserver.db.entity.meteoswiss;

import com.x8ing.thsensor.thserver.utils.UUIDUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Date;

@Entity(name = "METEO_SWISS")
@Table(indexes = {
        @Index(name = "METEO_SWISS_IX_1", columnList = "createDate"),
        @Index(name = "METEO_SWISS_IX_2", columnList = "sunshineMeasureDate"),
        @Index(name = "METEO_SWISS_IX_3", columnList = "temperatureMeasureDate"),
})
public class MeteoSwissEntity {

    @Id
    private String id = UUIDUtils.generateShortTextUUID();

    private Date createDate = new Date();

    private String stationId;
    private String stationName;

    /**
     * percentage of the sunshine in the time period.
     * Normally 10 min with Meteo
     */
    private double sunshine;
    private Date sunshineMeasureDate;

    private double temperature;
    private Date temperatureMeasureDate;

    private Double windGustSpeed;
    private Double windDirection;
    private Date  windMeasureDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MeteoSwissEntity that = (MeteoSwissEntity) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "MeteoSwissEntity{" +
                "id='" + id + '\'' +
                ", createDate=" + createDate +
                ", stationId='" + stationId + '\'' +
                ", sunshine=" + sunshine +
                ", sunshineMeasureDate=" + sunshineMeasureDate +
                ", temperature=" + temperature +
                ", temperatureMeasureDate=" + temperatureMeasureDate +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public double getSunshine() {
        return sunshine;
    }

    public void setSunshine(double sunshine) {
        this.sunshine = sunshine;
    }

    public Date getSunshineMeasureDate() {
        return sunshineMeasureDate;
    }

    public void setSunshineMeasureDate(Date sunshineMeasureDate) {
        this.sunshineMeasureDate = sunshineMeasureDate;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public Date getTemperatureMeasureDate() {
        return temperatureMeasureDate;
    }

    public void setTemperatureMeasureDate(Date temperatureMeasureDate) {
        this.temperatureMeasureDate = temperatureMeasureDate;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Double getWindGustSpeed() {
        return windGustSpeed;
    }

    public void setWindGustSpeed(Double windGustSpeed) {
        this.windGustSpeed = windGustSpeed;
    }

    public Double getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(Double windDirection) {
        this.windDirection = windDirection;
    }

    public Date getWindMeasureDate() {
        return windMeasureDate;
    }

    public void setWindMeasureDate(Date windMeasureDate) {
        this.windMeasureDate = windMeasureDate;
    }
}
