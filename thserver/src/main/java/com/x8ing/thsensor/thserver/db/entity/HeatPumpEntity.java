package com.x8ing.thsensor.thserver.db.entity;

import com.x8ing.thsensor.thserver.utils.UUIDUtils;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "HEAT_PUMP")
@Table(indexes = {
        @Index(name = "HEAT_PUMP_IX_1", columnList = "measurementDate"),
})
public class HeatPumpEntity {

    @Id
    private String id = UUIDUtils.generateShortTextUUID();

    private Date measurementDate = new Date();

    private double boilerTemp;
    private int compressorHours;
    private double heatingIn;
    private double heatingOut;
    private double soleIn;
    private double soleOut;

    // additional input register, not yet certain what they are, but seem to be set
    @Column(nullable = true)
    private Double ireg50CircTemp;

    @Column(nullable = true)
    private Double ireg70TempCirc1;

    @Column(nullable = true)
    private Double ireg90TempCirc2;

    /**
     * Boiler Elektro-Einsatz Stunden
     */
    @Column(nullable = true)
    private Double ireg152Boiler2;

    @Column(nullable = true)
    private Double ireg170TempPsp;

    @Column(nullable = true)
    private Double ireg300TempOutdoor; // outdoor temp, but seems wrong?

    // additional discrete input, not yet certain what they are, but seem to be set
    private Boolean di1Error;
    private Boolean di10Compressor1;
    private Boolean di11Compressor2;
    private Boolean di12Valve4;
    private Boolean di13;
    private Boolean di14PumpDirect;
    private Boolean di15PumpBoiler;
    private Boolean di16We;
    private Boolean di17BoilerEl;
    private Boolean di18PoolPump;
    private Boolean di19HeatPumpOn;
    private Boolean di20Error;
    private Boolean di21PumpPrimary;
    private Boolean di22PumpLoad;
    private Boolean di23PumpGroundWater;
    private Boolean di30Compressor1Ready;
    private Boolean di31Compressor2Ready;
    private Boolean di70PumpHK1;
    private Boolean di71HKM1ixOpen;
    private Boolean di72HKM1ixClose;
    private Boolean di90PumpHK2;
    private Boolean di91HKM2ixOpen;
    private Boolean di92HKM2ixClose;
    private Boolean di150;
    private Boolean di151;
    private Boolean di152;
    private Boolean di153;
    private Boolean di154;


    public HeatPumpEntity() {
    }

    @Override
    public String toString() {
        return "TemperatureHumidityEntity{" +
                "id='" + id + '\'' +
                ", boilerTemp=" + boilerTemp +
                ", compressorHours=" + compressorHours +
                ", heatingIn=" + heatingIn +
                ", heatingOut=" + heatingOut +
                ", soleIn=" + soleIn +
                ", soleOut=" + soleOut +
                ", measurementDate=" + measurementDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HeatPumpEntity that = (HeatPumpEntity) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getBoilerTemp() {
        return boilerTemp;
    }

    public void setBoilerTemp(double boilerTemp) {
        this.boilerTemp = boilerTemp;
    }

    public int getCompressorHours() {
        return compressorHours;
    }

    public void setCompressorHours(int compressorHours) {
        this.compressorHours = compressorHours;
    }

    public double getHeatingIn() {
        return heatingIn;
    }

    public void setHeatingIn(double heatingIn) {
        this.heatingIn = heatingIn;
    }

    public double getHeatingOut() {
        return heatingOut;
    }

    public void setHeatingOut(double heatingOut) {
        this.heatingOut = heatingOut;
    }

    public double getSoleIn() {
        return soleIn;
    }

    public void setSoleIn(double soleIn) {
        this.soleIn = soleIn;
    }

    public double getSoleOut() {
        return soleOut;
    }

    public void setSoleOut(double soleOut) {
        this.soleOut = soleOut;
    }

    public Date getMeasurementDate() {
        return measurementDate;
    }

    public void setMeasurementDate(Date date) {
        this.measurementDate = date;
    }

    public Double getIreg50CircTemp() {
        return ireg50CircTemp;
    }

    public void setIreg50CircTemp(Double ireg50CircTemp) {
        this.ireg50CircTemp = ireg50CircTemp;
    }

    public Double getIreg70TempCirc1() {
        return ireg70TempCirc1;
    }

    public void setIreg70TempCirc1(Double ireg70TempCirc1) {
        this.ireg70TempCirc1 = ireg70TempCirc1;
    }

    public Double getIreg90TempCirc2() {
        return ireg90TempCirc2;
    }

    public void setIreg90TempCirc2(Double ireg90TempCirc2) {
        this.ireg90TempCirc2 = ireg90TempCirc2;
    }

    public Double getIreg152Boiler2() {
        return ireg152Boiler2;
    }

    public void setIreg152Boiler2(Double ireg152Boiler2) {
        this.ireg152Boiler2 = ireg152Boiler2;
    }

    public Double getIreg170TempPsp() {
        return ireg170TempPsp;
    }

    public void setIreg170TempPsp(Double ireg170TempPsp) {
        this.ireg170TempPsp = ireg170TempPsp;
    }

    public Double getIreg300TempOutdoor() {
        return ireg300TempOutdoor;
    }

    public void setIreg300TempOutdoor(Double ireg300TempOutdoor) {
        this.ireg300TempOutdoor = ireg300TempOutdoor;
    }

    public Boolean isDi1Error() {
        return di1Error;
    }

    public void setDi1Error(Boolean di1Error) {
        this.di1Error = di1Error;
    }

    public Boolean isDi10Compressor1() {
        return di10Compressor1;
    }

    public void setDi10Compressor1(Boolean di10Compressor1) {
        this.di10Compressor1 = di10Compressor1;
    }

    public Boolean isDi11Compressor2() {
        return di11Compressor2;
    }

    public void setDi11Compressor2(Boolean di11Compressor2) {
        this.di11Compressor2 = di11Compressor2;
    }

    public Boolean isDi12Valve4() {
        return di12Valve4;
    }

    public void setDi12Valve4(Boolean di12Valve4) {
        this.di12Valve4 = di12Valve4;
    }

    public Boolean isDi13() {
        return di13;
    }

    public void setDi13(Boolean di13) {
        this.di13 = di13;
    }

    public Boolean isDi14PumpDirect() {
        return di14PumpDirect;
    }

    public void setDi14PumpDirect(Boolean di14PumpDirect) {
        this.di14PumpDirect = di14PumpDirect;
    }

    public Boolean isDi15PumpBoiler() {
        return di15PumpBoiler;
    }

    public void setDi15PumpBoiler(Boolean di15PumpBoiler) {
        this.di15PumpBoiler = di15PumpBoiler;
    }

    public Boolean isDi16We() {
        return di16We;
    }

    public void setDi16We(Boolean di16We) {
        this.di16We = di16We;
    }

    public Boolean isDi17BoilerEl() {
        return di17BoilerEl;
    }

    public void setDi17BoilerEl(Boolean di17BoilerEl) {
        this.di17BoilerEl = di17BoilerEl;
    }

    public Boolean isDi18PoolPump() {
        return di18PoolPump;
    }

    public void setDi18PoolPump(Boolean di18PoolPump) {
        this.di18PoolPump = di18PoolPump;
    }

    public Boolean isDi19HeatPumpOn() {
        return di19HeatPumpOn;
    }

    public void setDi19HeatPumpOn(Boolean di19HeatPumpOn) {
        this.di19HeatPumpOn = di19HeatPumpOn;
    }

    public Boolean isDi20Error() {
        return di20Error;
    }

    public void setDi20Error(Boolean di20Error) {
        this.di20Error = di20Error;
    }

    public Boolean isDi21PumpPrimary() {
        return di21PumpPrimary;
    }

    public void setDi21PumpPrimary(Boolean di21PumpPrimary) {
        this.di21PumpPrimary = di21PumpPrimary;
    }

    public Boolean isDi22PumpLoad() {
        return di22PumpLoad;
    }

    public void setDi22PumpLoad(Boolean di22PumpLoad) {
        this.di22PumpLoad = di22PumpLoad;
    }

    public Boolean isDi23PumpGroundWater() {
        return di23PumpGroundWater;
    }

    public void setDi23PumpGroundWater(Boolean di23PumpGroundWater) {
        this.di23PumpGroundWater = di23PumpGroundWater;
    }

    public Boolean isDi30Compressor1Ready() {
        return di30Compressor1Ready;
    }

    public void setDi30Compressor1Ready(Boolean di30Compressor1Ready) {
        this.di30Compressor1Ready = di30Compressor1Ready;
    }

    public Boolean isDi31Compressor2Ready() {
        return di31Compressor2Ready;
    }

    public void setDi31Compressor2Ready(Boolean di31Compressor2Ready) {
        this.di31Compressor2Ready = di31Compressor2Ready;
    }

    public Boolean isDi70PumpHK1() {
        return di70PumpHK1;
    }

    public void setDi70PumpHK1(Boolean di70PumpHK1) {
        this.di70PumpHK1 = di70PumpHK1;
    }

    public Boolean isDi71HKM1ixOpen() {
        return di71HKM1ixOpen;
    }

    public void setDi71HKM1ixOpen(Boolean di71HKM1ixOpen) {
        this.di71HKM1ixOpen = di71HKM1ixOpen;
    }

    public Boolean isDi72HKM1ixClose() {
        return di72HKM1ixClose;
    }

    public void setDi72HKM1ixClose(Boolean di72HKM1ixClose) {
        this.di72HKM1ixClose = di72HKM1ixClose;
    }

    public Boolean isDi90PumpHK2() {
        return di90PumpHK2;
    }

    public void setDi90PumpHK2(Boolean di90PumpHK2) {
        this.di90PumpHK2 = di90PumpHK2;
    }

    public Boolean isDi91HKM2ixOpen() {
        return di91HKM2ixOpen;
    }

    public void setDi91HKM2ixOpen(Boolean di91HKM2ixOpen) {
        this.di91HKM2ixOpen = di91HKM2ixOpen;
    }

    public Boolean isDi92HKM2ixClose() {
        return di92HKM2ixClose;
    }

    public void setDi92HKM2ixClose(Boolean di92HKM2ixClose) {
        this.di92HKM2ixClose = di92HKM2ixClose;
    }

    public Boolean isDi150() {
        return di150;
    }

    public void setDi150(Boolean di150) {
        this.di150 = di150;
    }

    public Boolean isDi151() {
        return di151;
    }

    public void setDi151(Boolean di151) {
        this.di151 = di151;
    }

    public Boolean isDi152() {
        return di152;
    }

    public void setDi152(Boolean di152) {
        this.di152 = di152;
    }

    public Boolean isDi153() {
        return di153;
    }

    public void setDi153(Boolean di153) {
        this.di153 = di153;
    }

    public Boolean isDi154() {
        return di154;
    }

    public void setDi154(Boolean di154) {
        this.di154 = di154;
    }
}
