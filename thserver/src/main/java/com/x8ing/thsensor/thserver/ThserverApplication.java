package com.x8ing.thsensor.thserver;

import com.x8ing.thsensor.thserver.db.dao.audit.AuditLogRepository;
import com.x8ing.thsensor.thserver.db.entity.audit.AuditLogEntity;
import com.x8ing.thsensor.thserver.device.service.HeatingDataReadService;
import com.x8ing.thsensor.thserver.utils.Utils;
import com.x8ing.thsensor.thserver.utils.spring.StartupData;
import com.x8ing.thsensor.thserver.web.services.info.bean.InfoBean;
import com.x8ing.thsensor.thserver.web.services.info.bean.MemoryInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

@SpringBootApplication
@EnableScheduling
public class ThserverApplication {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final AuditLogRepository auditLogRepository;

    private final InfoBean infoBean;

    private final StartupData startupData;

    public ThserverApplication(HeatingDataReadService heatingDataReadService, AuditLogRepository auditLogRepository, InfoBean infoBean, StartupData startupData) {
        this.auditLogRepository = auditLogRepository;
        this.infoBean = infoBean;
        this.startupData = startupData;
        log.info("Created and initialized with heatingDataReadService=" + heatingDataReadService.getClass().getSimpleName());
        log.info("Started " + infoBean);
    }

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Zurich"));
        //TimeZone.setDefault(TimeZone.getTimeZone("UTC"));// better, have a standard????
        SpringApplication.run(ThserverApplication.class, args);
    }

    /**
     * Actually execute only once... Yet, completely after full init to have the bean available
     */
    @Scheduled(initialDelay = 50, fixedDelay = Long.MAX_VALUE)
    public void logStartup() {

        Map<String, Object> detailInfo = new TreeMap<>();
        detailInfo.put("startupTimes", startupData);
        detailInfo.put("memoryInfo", MemoryInfo.getCurrent());
        detailInfo.put("serverInfo", infoBean);

        this.auditLogRepository.save(new AuditLogEntity("SERVER", "START", "STARTUP", Utils.toJSON(detailInfo), null, null));
    }

}
