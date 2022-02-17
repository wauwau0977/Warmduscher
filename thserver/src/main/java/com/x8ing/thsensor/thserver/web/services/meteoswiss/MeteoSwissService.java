package com.x8ing.thsensor.thserver.web.services.meteoswiss;

import com.x8ing.thsensor.thserver.db.dao.meteoswiss.MeteoSwissRepository;
import com.x8ing.thsensor.thserver.db.dao.meteoswiss.MeteoSwissStatsRepository;
import com.x8ing.thsensor.thserver.db.entity.meteoswiss.MeteoSwissEntity;
import com.x8ing.thsensor.thserver.db.entity.meteoswiss.MeteoSwissStatisticsEntity;
import com.x8ing.thsensor.thserver.utils.ThException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/meteo-swiss")
public class MeteoSwissService {

    private final MeteoSwissRepository meteoSwissRepository;
    private final MeteoSwissStatsRepository meteoSwissStatsRepository;

    public MeteoSwissService(MeteoSwissRepository meteoSwissRepository, MeteoSwissStatsRepository meteoSwissStatsRepository) {
        this.meteoSwissRepository = meteoSwissRepository;
        this.meteoSwissStatsRepository = meteoSwissStatsRepository;
    }

    @RequestMapping("/current")
    @ResponseBody
    public MeteoSwissEntity getCurrent(
            @RequestParam(name = "stationId", required = true) String stationId
    ) throws Exception {
        // done in interceptor
        // log.info("Got request for current. ip=" + Utils.getRequestIP(request));
        return meteoSwissRepository.getLastEntries(stationId, 1).stream().findFirst().orElse(null);
    }

    @RequestMapping("/lastValues")
    @ResponseBody
    public List<MeteoSwissEntity> lastValues(
            @RequestParam(name = "maxRows", required = false, defaultValue = "1500") int maxRows,
            @RequestParam(name = "stationId", required = true) String stationId
    ) throws Exception {
        return meteoSwissRepository.getLastEntries(stationId, maxRows);
    }


    /**
     * format to use params as ISO:
     * {{BASE_URL}}/heatpump-data/getBetweenDates?maxRows=100&start=2021-12-24T09:42:59.437995&end=2031-12-25T09:42:59.437995
     * <p>
     * Date ISO format defined in application.yml file.
     */
    @RequestMapping("/getBetweenDates")
    @ResponseBody
    public List<MeteoSwissStatisticsEntity> getBetweenDates(
            @RequestParam(name = "start") Date start,
            @RequestParam(name = "end") Date end,
            @RequestParam(name = "maxRows", required = false, defaultValue = "-1") int maxRows,
            @RequestParam(name = "groupEveryNthSecond", required = false, defaultValue = "-1") int groupEveryNthSecond,
            @RequestParam(name = "stationIdList", required = false) Set<String> stationIdList
    ) throws Exception {

        if (groupEveryNthSecond > 0 && maxRows > 0) {
            throw new ThException("Either supply 'groupEveryNthSecond' or 'maxRows' as a param. Both is not possible");
        }

        if (groupEveryNthSecond < 0 && maxRows < 0) {
            throw new ThException("At least supply one limiting criteria, either 'groupEveryNthSecond' or 'maxRows' as a param.");
        }

        List<MeteoSwissStatisticsEntity> result = null;
        if (groupEveryNthSecond > 0) {
            result = meteoSwissStatsRepository.findBetweenDatesLimitByFixedIntervalStats(start, end, groupEveryNthSecond);
        } else if (maxRows > 0) {
            result = meteoSwissStatsRepository.findBetweenDatesLimitByRowsStats(start, end, maxRows);
        } else {
            throw new ThException("invalid state");
        }

        // filter on java level (less efficient), as DB side filtering seems to be tricky with a full optinal param which is part of predicate
        // and station_id  = coalesce(:station_id, station_id)  (does not work..)
        if (result != null && CollectionUtils.isNotEmpty(stationIdList)) {
            result = result.stream().
                    filter(meteoSwissStatisticsEntity -> stationIdList.contains(meteoSwissStatisticsEntity.getStationId()))
                    .collect(Collectors.toList());
        }
        return result;
    }
}
