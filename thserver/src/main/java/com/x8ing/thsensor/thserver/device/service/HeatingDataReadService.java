package com.x8ing.thsensor.thserver.device.service;

import com.x8ing.thsensor.thserver.db.entity.HeatPumpEntity;

import java.util.List;

public interface HeatingDataReadService {


    void init() throws Exception;

    HeatPumpEntity getData() throws Throwable;

    List<String> scanAllRegisters(int maxRegister);
}
