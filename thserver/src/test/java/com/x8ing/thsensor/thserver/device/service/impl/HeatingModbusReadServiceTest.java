package com.x8ing.thsensor.thserver.device.service.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeatingModbusReadServiceTest {

    @Test
    void getSignedNumber() {

        // positive numbers
        assertEquals(HeatingModbusReadService.getSignedNumber(0), 0);
        assertEquals(HeatingModbusReadService.getSignedNumber(1), 1);
        assertEquals(HeatingModbusReadService.getSignedNumber(2), 2);
        assertEquals(HeatingModbusReadService.getSignedNumber(10), 10);
        assertEquals(HeatingModbusReadService.getSignedNumber(11), 11);
        assertEquals(HeatingModbusReadService.getSignedNumber(1001), 1001);
        assertEquals(HeatingModbusReadService.getSignedNumber(32767), 32767);


        // negative numbers
        assertEquals(HeatingModbusReadService.getSignedNumber(65535), -1);
        assertEquals(HeatingModbusReadService.getSignedNumber(65534), -2);
        assertEquals(HeatingModbusReadService.getSignedNumber(65533), -3);
        assertEquals(HeatingModbusReadService.getSignedNumber(65523), -13);
        assertEquals(HeatingModbusReadService.getSignedNumber(32768), -32768);


    }
}