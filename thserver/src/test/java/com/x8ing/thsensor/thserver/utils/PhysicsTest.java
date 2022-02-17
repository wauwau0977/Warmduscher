package com.x8ing.thsensor.thserver.utils;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class PhysicsTest {

    @Test
    public void calculateAbsoluteHumidityApproximation() {

        {
            double ah = Physics.calculateAbsoluteHumidityApproximation(20, 80);
            System.out.println(ah);
            assertTrue(ah > 13.7 && ah < 13.9);
        }
        {
            double ah = Physics.calculateAbsoluteHumidityApproximation(20, 90);
            System.out.println(ah);
            assertTrue(ah > 15.5 && ah < 15.6);
        }

        {
            double ah = Physics.calculateAbsoluteHumidityApproximation(5, 50);
            System.out.println(ah);
            assertTrue(ah > 3.3 && ah < 3.5);
        }


    }
}
