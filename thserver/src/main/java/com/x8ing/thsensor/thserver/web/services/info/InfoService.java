package com.x8ing.thsensor.thserver.web.services.info;

import com.x8ing.thsensor.thserver.web.services.info.bean.InfoBean;
import com.x8ing.thsensor.thserver.web.services.info.bean.MemoryInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/info")
public class InfoService {

    private final InfoBean infoBean;

    public InfoService(InfoBean infoBean) {
        this.infoBean = infoBean;
    }

    @RequestMapping("/general")
    @ResponseBody
    public InfoBean getInfo() {
        return infoBean;
    }

    @RequestMapping("/memory")
    @ResponseBody
    public MemoryInfo getMemoryInfo() {
        return MemoryInfo.getCurrent();
    }

}
