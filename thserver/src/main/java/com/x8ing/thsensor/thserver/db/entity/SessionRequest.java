package com.x8ing.thsensor.thserver.db.entity;

import com.x8ing.thsensor.thserver.utils.UUIDUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Date;

@Entity(name = "SESSION_REQUEST")
@Table(indexes = {
        @Index(name = "SESSION_REQUEST_IX_1", columnList = "requestDate"),
        @Index(name = "SESSION_REQUEST_IX_2", columnList = "clientId"),
        @Index(name = "SESSION_REQUEST_IX_3", columnList = "sessionId"),
})
public class SessionRequest {

    @Id
    private String id = UUIDUtils.generateShortTextUUID();

    private String sessionId;

    private String clientId;

    private String clientVersion;

    private Date requestDate = new Date();

    private String path;

    private String httpStatus;

    private String exception;

    private Long processingTime;

    private String ip;

    @Override
    public String toString() {
        return "SessionRequest { " +
                "processingTime=" + processingTime +
                ", id=" + id +
                ", sessionId='" + sessionId + '\'' +
                ", clientId='" + clientId + '\'' +
                ", httpStatus='" + httpStatus + '\'' +
                ", ip='" + ip + '\'' +
                ", path='" + path + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SessionRequest that = (SessionRequest) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientSessionId) {
        this.clientId = clientSessionId;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public String getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(String httpStatus) {
        this.httpStatus = httpStatus;
    }

    public Long getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(Long processingTime) {
        this.processingTime = processingTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }
}
