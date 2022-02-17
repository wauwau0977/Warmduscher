package com.x8ing.thsensor.thserver.db.dao.meteoswiss;

import com.x8ing.thsensor.thserver.db.entity.meteoswiss.MeteoSwissEntity;
import com.x8ing.thsensor.thserver.db.entity.meteoswiss.MeteoSwissStatisticsEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@SuppressWarnings("SqlResolve")
@Repository
public interface MeteoSwissStatsRepository extends CrudRepository<MeteoSwissStatisticsEntity, String> {

    @Query(value = "select\n" +
            "\t   max(id) id,\n" +
            "\t   station_id,\n" +
            "\t   max(station_name) station_name,\n" +
            "\t   -- temperature\n" +
            "\t   max(temperature_measure_date)  \ttemperature_measure_date, \n" +
            "       min(temperature_measure_date)  \ttemperature_measure_date_min,\n" +
            "       max(temperature_measure_date)    temperature_measure_date_max,\n" +
            "       avg(temperature)\t\t\t\t\ttemperature, \n" +
            "       min(temperature)  \t\t\t\ttemperature_min,\n" +
            "       max(temperature)    \t\t\t\ttemperature_max,\n" +
            "       -- wind_gust\n" +
            "       max(wind_measure_date)  \t\t\twind_measure_date, \n" +
            "       min(wind_measure_date)  \t\t\twind_measure_date_min,\n" +
            "       max(wind_measure_date)    \t\twind_measure_date_max,\n" +
            "       avg(wind_gust_speed) \t\t\twind_gust_speed, \n" +
            "       min(wind_gust_speed)  \t\t\twind_gust_speed_min,\n" +
            "       max(wind_gust_speed)    \t\t\twind_gust_speed_max\n" +
            "from (select ntile(:maxRows) over ( order by temperature_measure_date ) as grp, *\n" +
            "      from meteo_swiss t\n" +
            "      where t.temperature_measure_date > :measurement_date_start  -- TO_TIMESTAMP( '2017-03-31 9:30:20', 'YYYY-MM-DD HH24:MI:SS')\n" +
            "      and t.temperature_measure_date < :measurement_date_end) t\n" +
            "group by grp, station_id\n" +
            "order by temperature_measure_date desc", nativeQuery = true)
    List<MeteoSwissStatisticsEntity> findBetweenDatesLimitByRowsStats(
            Date measurement_date_start,
            Date measurement_date_end,
            int maxRows);


    @Query(value = "select\n" +
            "\t   max(id) id,\n" +
            "\t   station_id,\n" +
            "\t   max(station_name) station_name,\n" +
            "\t   -- temperature\n" +
            "\t   max(temperature_measure_date)  \ttemperature_measure_date, \n" +
            "       min(temperature_measure_date)  \ttemperature_measure_date_min,\n" +
            "       max(temperature_measure_date)    temperature_measure_date_max,\n" +
            "       avg(temperature)\t\t\t\t\ttemperature, \n" +
            "       min(temperature)  \t\t\t\ttemperature_min,\n" +
            "       max(temperature)    \t\t\t\ttemperature_max,\n" +
            "       -- wind_gust\n" +
            "       max(wind_measure_date)  \t\t\twind_measure_date, \n" +
            "       min(wind_measure_date)  \t\t\twind_measure_date_min,\n" +
            "       max(wind_measure_date)    \t\twind_measure_date_max,\n" +
            "       avg(wind_gust_speed) \t\t\twind_gust_speed, \n" +
            "       min(wind_gust_speed)  \t\t\twind_gust_speed_min,\n" +
            "       max(wind_gust_speed)    \t\t\twind_gust_speed_max\n" +
            "from\n" +
            "    (\n" +
            "    select\n" +
            "        -- have a generic grouping in seconds\n" +
            "        round(extract(epoch from temperature_measure_date) / :group_every_nth_second) groupid,\n" +
            "        t.*\n" +
            "    from\n" +
            "        meteo_swiss t\n" +
            "    where\n" +
            "        t.temperature_measure_date > :measurement_date_start\n" +
            "        -- TO_TIMESTAMP( '2017-03-31 9:30:20', 'YYYY-MM-DD HH24:MI:SS')\n" +
            "        and t.temperature_measure_date < :measurement_date_end)\n" +
            "        q1\n" +
            "group by\n" +
            "    groupid, station_id \n" +
            "order by\n" +
            "    temperature_measure_date desc", nativeQuery = true)
    List<MeteoSwissStatisticsEntity> findBetweenDatesLimitByFixedIntervalStats(
            Date measurement_date_start,
            Date measurement_date_end,
            @Param("group_every_nth_second") int groupEveryNthSecond);

}

