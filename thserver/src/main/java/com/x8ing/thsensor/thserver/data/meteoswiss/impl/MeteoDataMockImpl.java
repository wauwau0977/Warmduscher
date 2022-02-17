package com.x8ing.thsensor.thserver.data.meteoswiss.impl;

import com.x8ing.thsensor.thserver.Profiles;
import com.x8ing.thsensor.thserver.data.meteoswiss.MeteoDataService;
import com.x8ing.thsensor.thserver.db.entity.meteoswiss.MeteoSwissEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Profile(Profiles.SENSOR_MOCK)
public class MeteoDataMockImpl implements MeteoDataService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final long t0 = System.currentTimeMillis() - 1;


    @Override
    public void init() {

    }

    @Override
    public List<MeteoSwissEntity> getData() {

        log.info("Generate mock data for MeteoSwiss");

        @SuppressWarnings("IntegerDivisionInFloatingPointContext")
        double dtS = (System.currentTimeMillis() - t0) / 1000;

        MeteoSwissEntity entity = new MeteoSwissEntity();
        entity.setCreateDate(new Date());

        entity.setStationName("Kloten");
        entity.setStationId("KLO");

        entity.setWindMeasureDate(new Date());
        entity.setWindGustSpeed(dtS / 30 + 60);

        entity.setTemperatureMeasureDate(new Date());
        entity.setTemperature(dtS / 10 - 10);

        return List.of(entity);
    }
}
