package com.x8ing.thsensor.thserver.data.meteoswiss;

import com.x8ing.thsensor.thserver.db.entity.meteoswiss.MeteoSwissEntity;

import java.util.List;

public interface MeteoDataService {


    void init();

    List<MeteoSwissEntity> getData();

}
