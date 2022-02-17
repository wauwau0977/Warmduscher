package com.x8ing.thsensor.thserver.web.services.info.bean;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InfoBean {

    private String deviceName = "";

    @Value("${thserver.buildTimestampServer}")
    private String buildTimestampServer = "";

    @Value("${thserver.buildVersionServer}.0")
    private String buildVersionServer = "";

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getBuildTimestampServer() {
        return buildTimestampServer;
    }

    public void setBuildTimestampServer(String buildTimestampServer) {
        this.buildTimestampServer = buildTimestampServer;
    }

    public String getBuildVersionServer() {
        return buildVersionServer;
    }

    public void setBuildVersionServer(String buildVersionServer) {
        this.buildVersionServer = buildVersionServer;
    }

    @Override
    public String toString() {
        return "InfoBean{" +
                "deviceName='" + deviceName + '\'' +
                ", buildTimestampServer='" + buildTimestampServer + '\'' +
                ", buildVersionServer='" + buildVersionServer + '\'' +
                '}';
    }
}
