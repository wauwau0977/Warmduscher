package com.x8ing.thsensor.thserver.device.service.impl;

import com.x8ing.thsensor.thserver.Profiles;
import com.x8ing.thsensor.thserver.db.entity.HeatPumpEntity;
import com.x8ing.thsensor.thserver.device.service.HeatingDataReadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Profile(Profiles.SENSOR_MOCK)
public class HeatingDataReadServiceMock implements HeatingDataReadService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final long t0 = System.currentTimeMillis() - 1;


    @Override
    public void init() throws Exception {

    }

    @Override
    public HeatPumpEntity getData() throws Exception {
        @SuppressWarnings("IntegerDivisionInFloatingPointContext")
        double dtS = (System.currentTimeMillis() - t0) / 1000;

        HeatPumpEntity ret = new HeatPumpEntity();
        ret.setHeatingIn(dtS / 30 + 20);
        ret.setHeatingOut(dtS / 30 + 30);
        ret.setSoleIn(dtS / 30 + 10);
        ret.setSoleOut(dtS / 30 + 5);
        ret.setBoilerTemp(dtS / 30 + 30);
        ret.setCompressorHours((int) (dtS + 100));
        ret.setIreg300TempOutdoor(dtS / 10 - 12);
        log.info("Return " + ret);
        return ret;
    }

    @Override
    public List<String> scanAllRegisters(int maxRegister) {
        return List.of("Not implemented");
    }
}
