package com.x8ing.thsensor.thserver.db.dao;

import com.x8ing.thsensor.thserver.db.entity.SessionDevice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Cacheable;
import java.util.Optional;

@SuppressWarnings("SqlResolve")
@Repository
public interface SessionDeviceRepository extends CrudRepository<SessionDevice, String> {


}

