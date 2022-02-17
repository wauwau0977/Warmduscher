package com.x8ing.thsensor.thserver.utils;

public class Physics {

    /**
     * https://carnotcycle.wordpress.com/2012/08/04/how-to-convert-relative-humidity-to-absolute-humidity/
     * <p>
     * This formula is accurate to within 0.1% over the temperature range –30°C to +35°C
     *
     * @return Absolute Humidity (grams/m3)
     */
    public static final double calculateAbsoluteHumidityApproximation(double temperature, double relativeHumidity) {
        return (6.112 * Math.exp((17.67 * temperature) / (temperature + 243.5)) * relativeHumidity * 2.1674) / ((273.15 + temperature));
    }

}

