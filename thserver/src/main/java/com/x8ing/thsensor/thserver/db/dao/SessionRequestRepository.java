package com.x8ing.thsensor.thserver.db.dao;

import com.x8ing.thsensor.thserver.db.entity.SessionRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@SuppressWarnings("SqlResolve")
@Repository
public interface SessionRequestRepository extends CrudRepository<SessionRequest, String> {


}

