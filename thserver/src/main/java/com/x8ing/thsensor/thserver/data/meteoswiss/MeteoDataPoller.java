package com.x8ing.thsensor.thserver.data.meteoswiss;

import com.x8ing.thsensor.thserver.db.dao.meteoswiss.MeteoSwissRepository;
import com.x8ing.thsensor.thserver.db.entity.meteoswiss.MeteoSwissEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MeteoDataPoller {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final MeteoDataService meteoDataService;

    private final MeteoSwissRepository meteoSwissRepository;

    public MeteoDataPoller(MeteoDataService meteoDataService, MeteoSwissRepository meteSwissRepository) {
        this.meteoDataService = meteoDataService;
        this.meteoSwissRepository = meteSwissRepository;

        meteoDataService.init(); // init
        log.info("Did init MeteoDataService " + meteoDataService.getClass().getSimpleName());
    }


    @Scheduled(fixedDelayString = "${thserver.meteoSwiss.pollingInterval:PT900s}", initialDelay = 0)
    public void pollData() {

        long t0 = System.currentTimeMillis();

        try {
            List<MeteoSwissEntity> meteoSwissEntity = meteoDataService.getData();
            meteoSwissRepository.saveAll(meteoSwissEntity);
        } catch (Throwable e) {
            String msg = "Exception while reading data from MeteoSwiss";
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }

        log.info("Did poll live data from MeteoSwiss completed and persisted successfully. dt=" + (System.currentTimeMillis() - t0));

    }


}
