package com.x8ing.thsensor.thserver.db.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Date;

@Entity(name = "SESSION_DEVICE")
@Table(indexes = {
        @Index(name = "SESSION_DEVICE_IX_1", columnList = "sessionCreateDate"),
        @Index(name = "SESSION_DEVICE_IX_2", columnList = "sessionId"),
        @Index(name = "SESSION_DEVICE_IX_3", columnList = "clientId"),
})
public class SessionDevice {

    @Id
    private String sessionId;

    private String clientId;

    private Date sessionCreateDate = new Date();

    private String agentString;

    private String ip;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Date getSessionCreateDate() {
        return sessionCreateDate;
    }

    public void setSessionCreateDate(Date sessionCreateDate) {
        this.sessionCreateDate = sessionCreateDate;
    }

    public String getAgentString() {
        return agentString;
    }

    public void setAgentString(String agentString) {
        this.agentString = agentString;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "SessionDevice{" +
                "sessionId='" + sessionId + '\'' +
                ", agentString='" + agentString + '\'' +
                ", clientId='" + clientId + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SessionDevice that = (SessionDevice) o;

        return sessionId != null ? sessionId.equals(that.sessionId) : that.sessionId == null;
    }

    @Override
    public int hashCode() {
        return sessionId != null ? sessionId.hashCode() : 0;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientSesssionId) {
        this.clientId = clientSesssionId;
    }
}
