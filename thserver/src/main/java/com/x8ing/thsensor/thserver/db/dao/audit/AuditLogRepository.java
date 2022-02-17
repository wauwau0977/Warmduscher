package com.x8ing.thsensor.thserver.db.dao.audit;

import com.x8ing.thsensor.thserver.db.entity.audit.AuditLogEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@SuppressWarnings("SqlResolve")
@Repository
public interface AuditLogRepository extends CrudRepository<AuditLogEntity, String> {


}

