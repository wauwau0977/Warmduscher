package com.x8ing.thsensor.thserver.db.dao.meteoswiss;

import com.x8ing.thsensor.thserver.db.entity.HeatPumpEntity;
import com.x8ing.thsensor.thserver.db.entity.meteoswiss.MeteoSwissEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("SqlResolve")
@Repository
public interface MeteoSwissRepository extends CrudRepository<MeteoSwissEntity,String> {


    @Query(value = "select * from meteo_swiss ms where station_id=:stationId order by temperature_measure_date desc limit :maxRows", nativeQuery = true)
    List<MeteoSwissEntity> getLastEntries(String stationId, int maxRows);

}

