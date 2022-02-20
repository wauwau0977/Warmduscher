package com.x8ing.thsensor.thserver.web.services.heating;

import com.x8ing.thsensor.thserver.db.dao.HeatPumpRepository;
import com.x8ing.thsensor.thserver.db.entity.HeatPumpEntity;
import com.x8ing.thsensor.thserver.db.entity.HeatPumpStatisticsEntity;
import com.x8ing.thsensor.thserver.db.entity.analytics.BoilerStatsByDayOfWeek;
import com.x8ing.thsensor.thserver.db.entity.analytics.BoilerStatsByHour;
import com.x8ing.thsensor.thserver.db.entity.analytics.SoleInOutDeltaInOperationStats;
import com.x8ing.thsensor.thserver.device.service.HeatingDataReadService;
import com.x8ing.thsensor.thserver.utils.ThException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/heatpump-data")
public class HeatPumpDataService {

    private final HeatPumpRepository heatPumpRepository;
    private final HeatingDataReadService heatingDataReadService;

    public HeatPumpDataService(HeatPumpRepository heatPumpRepository, HeatingDataReadService heatingDataReadService) {
        this.heatPumpRepository = heatPumpRepository;
        this.heatingDataReadService = heatingDataReadService;
    }

    @RequestMapping("/current")
    @ResponseBody
    public HeatPumpEntity getCurrent() throws Exception {
        // done in interceptor
        // log.info("Got request for current. ip=" + Utils.getRequestIP(request));
        return heatPumpRepository.getLastEntries(1).stream().findFirst().orElse(null);
    }

    @RequestMapping("/lastValues")
    @ResponseBody
    public List<HeatPumpEntity> lastValues(
            @RequestParam(name = "maxRows", required = false, defaultValue = "1500") int maxRows
    ) throws Exception {
        return heatPumpRepository.getLastEntries(maxRows);
    }


    /**
     * format to use params as ISO:
     * {{BASE_URL}}/heatpump-data/getBetweenDates?maxRows=100&start=2021-12-24T09:42:59.437995&end=2031-12-25T09:42:59.437995
     * <p>
     * Date ISO format defined in application.yml file.
     */
    @RequestMapping("/getBetweenDates")
    @ResponseBody
    public List<HeatPumpStatisticsEntity> getBetweenDates(
            @RequestParam(name = "start") Date start,
            @RequestParam(name = "end") Date end,
            @RequestParam(name = "maxRows", required = false, defaultValue = "-1") int maxRows,
            @RequestParam(name = "groupEveryNthSecond", required = false, defaultValue = "-1") int groupEveryNthSecond
    ) throws Exception {

        if (groupEveryNthSecond > 0 && maxRows > 0) {
            throw new ThException("Either supply 'groupEveryNthSecond' or 'maxRows' as a param. Both is not possible");
        }

        if (groupEveryNthSecond < 0 && maxRows < 0) {
            throw new ThException("At least supply one limiting criteria, either 'groupEveryNthSecond' or 'maxRows' as a param.");
        }

        if (groupEveryNthSecond > 0) {
            return heatPumpRepository.findBetweenDatesLimitByFixedIntervalStats(start, end, groupEveryNthSecond);
        } else if (maxRows > 0) {
            return heatPumpRepository.findBetweenDatesLimitByRowsStats(start, end, maxRows);
        }
        throw new ThException("invalid state");
    }

    @RequestMapping("/getBoilerStatsByHour")
    @ResponseBody
    public List<BoilerStatsByHour> getBoilerStatsByHour(
            @RequestParam(name = "start") Date start,
            @RequestParam(name = "end") Date end
    ) throws Exception {
        return heatPumpRepository.getBoilerStatsByHour(start, end);
    }

    @RequestMapping("/getBoilerStatsByDayOfWeek")
    @ResponseBody
    public List<BoilerStatsByDayOfWeek> getBoilerStatsByDayOfWeek(
            @RequestParam(name = "start") Date start,
            @RequestParam(name = "end") Date end
    ) throws Exception {
        return heatPumpRepository.getBoilerStatsByDayOfWeek(start, end);
    }

    @RequestMapping("/getSoleDeltaInOperationStats")
    @ResponseBody
    public List<SoleInOutDeltaInOperationStats> getSoleDeltaInOperationStats(
            @RequestParam(name = "start") Date start,
            @RequestParam(name = "end") Date end,
            @RequestParam(name = "maxRows", required = false, defaultValue = "-1") int maxRows,
            @RequestParam(name = "groupEveryNthSecond", required = false, defaultValue = "-1") int groupEveryNthSecond
    ) throws Exception {
        return heatPumpRepository.getSoleDeltaInOperationStats(start, end, maxRows, groupEveryNthSecond);
    }

    @RequestMapping("/scanRegisters")
    @ResponseBody
    public List<String> scanRegisters(
            @RequestParam(name = "maxRegister", defaultValue = "510", required = false) int maxRegister) {
        return heatingDataReadService.scanAllRegisters(maxRegister);
    }


}
