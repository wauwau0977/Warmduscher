package com.x8ing.thsensor.thserver.db.entity.audit;

import com.x8ing.thsensor.thserver.utils.UUIDUtils;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "AUDIT_LOG_ENTITY")
@Table(indexes = {
        @Index(name = "AUDIT_LOG_IX_1", columnList = "createDate"),
})
public class AuditLogEntity {

    @Id
    private String id = UUIDUtils.generateShortTextUUID();

    private Date createDate = new Date();

    private String scope1;
    private String scope2;
    private String scope3;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String message;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String detail;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String exception;

    public AuditLogEntity() {
    }

    public AuditLogEntity(String scope1, String scope2, String scope3, String message, String detail, String exception) {
        this.scope1 = scope1;
        this.scope2 = scope2;
        this.scope3 = scope3;
        this.message = message;
        this.detail = detail;
        this.exception = exception;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuditLogEntity that = (AuditLogEntity) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getScope1() {
        return scope1;
    }

    public void setScope1(String scope1) {
        this.scope1 = scope1;
    }

    public String getScope2() {
        return scope2;
    }

    public void setScope2(String scope2) {
        this.scope2 = scope2;
    }

    public String getScope3() {
        return scope3;
    }

    public void setScope3(String scope3) {
        this.scope3 = scope3;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }
}
