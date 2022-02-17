package com.x8ing.thsensor.thserver.device.service;

import com.x8ing.thsensor.thserver.db.dao.HeatPumpRepository;
import com.x8ing.thsensor.thserver.db.entity.HeatPumpEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HeatingDataPoller {

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    private final HeatingDataReadService heatingDataReadService;

    private final HeatPumpRepository heatPumpRepository;

    public HeatingDataPoller(HeatingDataReadService heatingDataReadService, HeatPumpRepository heatPumpRepository) {
        this.heatingDataReadService = heatingDataReadService;
        this.heatPumpRepository = heatPumpRepository;
    }

    @Scheduled(fixedDelayString = "${thserver.pollingInterval:PT60s}", initialDelay = 0)
    public void pollData() {

        long t0 = System.currentTimeMillis();

        HeatPumpEntity heatPumpEntity;
        try {
            heatPumpEntity = heatingDataReadService.getData();
        } catch (Throwable e) {
            String msg = "Exception while reading data from ModBus or MockService.";
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }

        heatPumpRepository.save(heatPumpEntity);

        log.info("Did poll data and persisted it successfully. dt=" + (System.currentTimeMillis() - t0));

    }
}
