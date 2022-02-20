package com.x8ing.thsensor.thserver.db.dao;

import com.x8ing.thsensor.thserver.db.entity.HeatPumpEntity;
import com.x8ing.thsensor.thserver.db.entity.HeatPumpStatisticsEntity;
import com.x8ing.thsensor.thserver.db.entity.analytics.BoilerStatsByDayOfWeek;
import com.x8ing.thsensor.thserver.db.entity.analytics.BoilerStatsByHour;
import com.x8ing.thsensor.thserver.db.entity.analytics.SoleInOutDeltaInOperationStats;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@SuppressWarnings("SqlResolve")
@Repository
public interface HeatPumpRepository extends CrudRepository<HeatPumpEntity, String> {

    @Query(value = "select * from heat_pump hp order by measurement_date desc limit 1", nativeQuery = true)
    HeatPumpEntity getLastEntry();

    @Query(value = "select * from heat_pump hp order by measurement_date desc limit :maxRows", nativeQuery = true)
    List<HeatPumpEntity> getLastEntries(int maxRows);

    @Query(value = "select\n" +
            "    max(id) id,\n" +
            "    -- measurement_date\n" +
            "    max(measurement_date) measurementDate,\n" +
            "    min(measurement_date) measurementDateMin,\n" +
            "    max(measurement_date) measurementDateMax,\n" +
            "    -- boilder_temp\n" +
            "    avg(boiler_temp) boilerTemp,\n" +
            "    min(boiler_temp) boilerTempMin,\n" +
            "    max(boiler_temp) boilerTempMax,\n" +
            "    -- compressor_hours\n" +
            "    avg(compressor_hours) compressorHours,\n" +
            "    min(compressor_hours) compressorHoursMin,\n" +
            "    max(compressor_hours) compressorHoursMax,\n" +
            "    -- heating_in\n" +
            "    avg(heating_in) heatingIn,\n" +
            "    min(heating_in) heatingInMin,\n" +
            "    max(heating_in) heatingInMax,\n" +
            "    -- heating_out        \n" +
            "    avg(heating_out) heatingOut,\n" +
            "    min(heating_out) heatingOutMin,\n" +
            "    max(heating_out) heatingOutMax,\n" +
            "    -- sole_in\n" +
            "    avg(sole_in) soleIn,\n" +
            "    min(sole_in) soleInMin,\n" +
            "    max(sole_in) soleInMax,\n" +
            "    -- sole_out\n" +
            "    avg(sole_out) soleOut,\n" +
            "    min(sole_out) soleOutMin,\n" +
            "    max(sole_out) soleOutMax,\n" +
            "\t   -- outdoorTemperature\n" +
            "       avg(ireg300temp_outdoor) ireg300TempOutdoor,\n" +
            "       min(ireg300temp_outdoor) ireg300TempOutdoorMin,\n" +
            "       max(ireg300temp_outdoor) ireg300TempOutdoorMAx,\n" +
            "       -- for the values below, calculate the on-percentage of the consolidated values in the time period\n" +
            "       -- 1 means it was always on, 0 means it was never on, anything between is the ratio. \n" +
            "       round(avg(cast ( di1error as integer)),5) di1Error,\n" +
            "       round(avg(cast ( di10Compressor1 as integer)),5) di10Compressor1,\n" +
            "       round(avg(cast ( di14pump_direct as integer)),5) di14PumpDirect,\n" +
            "       round(avg(cast ( di15pump_boiler as integer)),5) di15PumpBoiler,\n" +
            "       round(avg(cast ( di17boiler_el as integer)),5) di17BoilerEl,\n" +
            "       round(avg(cast ( di21pump_primary as integer)),5) di21PumpPrimary,\n" +
            "       round(avg(cast ( di22pump_load as integer)),5) di22pumpLoad,\n" +
            "       round(avg(cast ( di70pumphk1 as integer)),5) di70PumpHk1,\n" +
            "       round(avg(cast ( di71hkm1ix_open as integer)),5) di71Hkm1ixOpen,\n" +
            "       round(avg(cast ( di72hkm1ix_close as integer)),5) di72Hkm1ixClose     \n" +
            "from (select ntile(:maxRows) over ( order by measurement_date ) as grp, *\n" +
            "      from heat_pump t\n" +
            "      where t.measurement_date > :measurement_date_start  -- TO_TIMESTAMP( '2017-03-31 9:30:20', 'YYYY-MM-DD HH24:MI:SS')\n" +
            "      and t.measurement_date < :measurement_date_end) t\n" +
            "group by grp\n" +
            "order by measurementDate desc", nativeQuery = true)
    List<HeatPumpStatisticsEntity> findBetweenDatesLimitByRowsStats(
            Date measurement_date_start,
            Date measurement_date_end,
            int maxRows);


    @Query(value = "select\n" +
            "    max(id) id,\n" +
            "    -- measurement_date\n" +
            "    max(measurement_date) measurementDate,\n" +
            "    min(measurement_date) measurementDateMin,\n" +
            "    max(measurement_date) measurementDateMax,\n" +
            "    -- boilder_temp\n" +
            "    avg(boiler_temp) boilerTemp,\n" +
            "    min(boiler_temp) boilerTempMin,\n" +
            "    max(boiler_temp) boilerTempMax,\n" +
            "    -- compressor_hours\n" +
            "    avg(compressor_hours) compressorHours,\n" +
            "    min(compressor_hours) compressorHoursMin,\n" +
            "    max(compressor_hours) compressorHoursMax,\n" +
            "    -- heating_in\n" +
            "    avg(heating_in) heatingIn,\n" +
            "    min(heating_in) heatingInMin,\n" +
            "    max(heating_in) heatingInMax,\n" +
            "    -- heating_out        \n" +
            "    avg(heating_out) heatingOut,\n" +
            "    min(heating_out) heatingOutMin,\n" +
            "    max(heating_out) heatingOutMax,\n" +
            "    -- sole_in\n" +
            "    avg(sole_in) soleIn,\n" +
            "    min(sole_in) soleInMin,\n" +
            "    max(sole_in) soleInMax,\n" +
            "    -- sole_out\n" +
            "    avg(sole_out) soleOut,\n" +
            "    min(sole_out) soleOutMin,\n" +
            "    max(sole_out) soleOutMax,\n" +
            "\t   -- outdoorTemperature\n" +
            "       avg(ireg300temp_outdoor) ireg300TempOutdoor,\n" +
            "       min(ireg300temp_outdoor) ireg300TempOutdoorMin,\n" +
            "       max(ireg300temp_outdoor) ireg300TempOutdoorMAx,\n" +
            "       -- for the values below, calculate the on-percentage of the consolidated values in the time period\n" +
            "       -- 1 means it was always on, 0 means it was never on, anything between is the ratio. \n" +
            "       round(avg(cast ( di1error as integer)),5) di1Error,\n" +
            "       round(avg(cast ( di10Compressor1 as integer)),5) di10Compressor1,\n" +
            "       round(avg(cast ( di14pump_direct as integer)),5) di14PumpDirect,\n" +
            "       round(avg(cast ( di15pump_boiler as integer)),5) di15PumpBoiler,\n" +
            "       round(avg(cast ( di17boiler_el as integer)),5) di17BoilerEl,\n" +
            "       round(avg(cast ( di21pump_primary as integer)),5) di21PumpPrimary,\n" +
            "       round(avg(cast ( di22pump_load as integer)),5) di22pumpLoad,\n" +
            "       round(avg(cast ( di70pumphk1 as integer)),5) di70PumpHk1,\n" +
            "       round(avg(cast ( di71hkm1ix_open as integer)),5) di71Hkm1ixOpen,\n" +
            "       round(avg(cast ( di72hkm1ix_close as integer)),5) di72Hkm1ixClose     \n" +
            "from\n" +
            "    (\n" +
            "    select\n" +
            "        -- have a generic grouping in seconds\n" +
            "        round(extract(epoch from measurement_date) / :group_every_nth_second) groupid,\n" +
            "        h.*\n" +
            "    from\n" +
            "        heat_pump h\n" +
            "    where\n" +
            "        h.measurement_date > :measurement_date_start\n" +
            "        -- TO_TIMESTAMP( '2017-03-31 9:30:20', 'YYYY-MM-DD HH24:MI:SS')\n" +
            "        and h.measurement_date < :measurement_date_end)\n" +
            "        q1\n" +
            "group by\n" +
            "    groupid\n" +
            "order by\n" +
            "    measurementDate desc", nativeQuery = true)
    List<HeatPumpStatisticsEntity> findBetweenDatesLimitByFixedIntervalStats(
            Date measurement_date_start,
            Date measurement_date_end,
            @Param("group_every_nth_second") int groupEveryNthSecond);

    @Query(value = "select hour_of_day                             as hourOfTheDay,\n" +
            "       sum(boiler_temp_max_decrease_in_window) as sumBoilerDiffDecrease,\n" +
            "       sum(boiler_temp_max_increase_in_window) as sumBoilerDiffIncrease,\n" +
            "       max(num_of_statistic_records_1)         as numOfStatisticRecords1\n" +
            "from (\n" +
            "         select min(measurement_date_t0)                as measurement_date_t0,\n" +
            "                max(measurement_date_t1)                as measurement_date_t1,\n" +
            "                day_of_week_starting_monday,\n" +
            "                day_of_week_text,\n" +
            "                hour_of_day,\n" +
            "                min(boiler_temp_max_decrease_in_window) as boiler_temp_max_decrease_in_window,\n" +
            "                max(boiler_temp_max_increase_in_window) as boiler_temp_max_increase_in_window,\n" +
            "                max(num_of_statistic_records_1)         as num_of_statistic_records_1\n" +
            "         from (\n" +
            "                  select year1,\n" +
            "                         doy,\n" +
            "                         day_of_week_starting_monday,\n" +
            "                         day_of_week_text,\n" +
            "                         hour_of_day,\n" +
            "                         boiler_temp,\n" +
            "                         measurement_date,\n" +
            "                         measurement_date_t0,\n" +
            "                         measurement_date_t1,\n" +
            "                         case when boiler_temp_max_decrease_in_window > 0 then 0 else boiler_temp_max_decrease_in_window end     boiler_temp_max_decrease_in_window,\n" +
            "                         case when boiler_temp_max_increase_in_window <= 0.11 then 0 else boiler_temp_max_increase_in_window end boiler_temp_max_increase_in_window,\n" +
            "                         num_of_statistic_records_1\n" +
            "                  from (\n" +
            "                           select year1,\n" +
            "                                  doy,\n" +
            "                                  day_of_week_starting_monday,\n" +
            "                                  day_of_week_text,\n" +
            "                                  hour_of_day,\n" +
            "                                  boiler_temp,\n" +
            "                                  measurement_date,\n" +
            "                                  measurement_date_t0,\n" +
            "                                  measurement_date_t1,\n" +
            "                                  -1.0 * GREATEST(boiler_temp_window_tMax - boiler_temp_window_t1, boiler_temp_window_t0 - boiler_temp_window_tMin) boiler_temp_max_decrease_in_window,\n" +
            "                                  GREATEST(boiler_temp_window_tMax - boiler_temp_window_t0, boiler_temp_window_t1 - boiler_temp_window_tMin)        boiler_temp_max_increase_in_window,\n" +
            "                                  boiler_temp_window_t0,\n" +
            "                                  boiler_temp_window_t1,\n" +
            "                                  boiler_temp_window_tMin,\n" +
            "                                  boiler_temp_window_tMax,\n" +
            "                                  num_of_statistic_records_1\n" +
            "                           from (\n" +
            "                                    select measurement_date,\n" +
            "                                           year1,\n" +
            "                                           doy,\n" +
            "                                           case when day_of_week_starting_sunday <= 0 then day_of_week_starting_sunday + 7 else day_of_week_starting_sunday end as day_of_week_starting_monday,\n" +
            "                                           To_Char(measurement_date, 'DAY')                                                                                        day_of_week_text,\n" +
            "                                           hour_of_day,\n" +
            "                                           boiler_temp,\n" +
            "                                           minute_of_hour,\n" +
            "                                           first_value(measurement_date) over ( partition by year1, doy, hour_of_day order by minute_of_hour)                      measurement_date_t0,\n" +
            "                                           last_value(measurement_date) over ( partition by year1, doy, hour_of_day order by minute_of_hour)                       measurement_date_t1,\n" +
            "                                           first_value(boiler_temp) over ( partition by year1, doy, hour_of_day order by minute_of_hour)                           boiler_temp_window_t0,\n" +
            "                                           last_value(boiler_temp) over ( partition by year1, doy, hour_of_day order by minute_of_hour)                            boiler_temp_window_t1,\n" +
            "                                           min(boiler_temp) over ( partition by year1, doy, hour_of_day order by minute_of_hour)                                   boiler_temp_window_tMin,\n" +
            "                                           max(boiler_temp) over ( partition by year1, doy, hour_of_day order by minute_of_hour)                                   boiler_temp_window_tMax,\n" +
            "                                           num_of_statistic_records_1\n" +
            "                                    from (\n" +
            "                                             select measurement_date,\n" +
            "                                                    extract(year from measurement_date)   year1,\n" +
            "                                                    extract(doy from measurement_date)    doy, -- day of the year\n" +
            "                                                    extract(hour from measurement_date)   hour_of_day,\n" +
            "                                                    extract(minute from measurement_date) minute_of_hour,\n" +
            "                                                    extract(dow from measurement_date) as day_of_week_starting_sunday,\n" +
            "                                                    To_Char(measurement_date, 'DAY')      day_of_week_text,\n" +
            "                                                    count(1) over ()                      num_of_statistic_records_1,\n" +
            "                                                    h1.boiler_temp\n" +
            "                                             from heat_pump h1\n" +
            "                                             where 1 = 1\n" +
            "                                               and measurement_date >= :measurement_date_start " +
            "                                               and measurement_date <= :measurement_date_end   " +
            "                                             order by year1, doy, hour_of_day, minute_of_hour\n" +
            "                                         ) h2\n" +
            "                                ) h3\n" +
            "                       ) h4\n" +
            "              ) h5\n" +
            "         group by year1, doy, day_of_week_starting_monday, day_of_week_text, hour_of_day\n" +
            "         order by year1, doy, hour_of_day) h7\n" +
            "group by hour_of_day\n" +
            "order by hour_of_day", nativeQuery = true)
    List<BoilerStatsByHour> getBoilerStatsByHour(Date measurement_date_start, Date measurement_date_end);


    @Query(value = "select \n" +
            "\tday_of_week_starting_monday as dayOfWeekStartingMonday,\n" +
            "\tday_of_week_text dayOfWeekText,\n" +
            "\tsum(boiler_temp_max_decrease_in_window) as sumBoilerDiffDecrease,\n" +
            "\tsum(boiler_temp_max_increase_in_window) as sumBoilerDiffIncrease,\n" +
            "\tmax(num_of_statistic_records_1) as numOfStatisticRecords1\n" +
            "from (\n" +
            "select min(measurement_date_t0)                as measurement_date_t0,\n" +
            "       max(measurement_date_t1)                as measurement_date_t1,\n" +
            "       day_of_week_starting_monday,\n" +
            "       day_of_week_text,\n" +
            "       hour_of_day,\n" +
            "       min(boiler_temp_max_decrease_in_window) as boiler_temp_max_decrease_in_window,\n" +
            "       max(boiler_temp_max_increase_in_window) as boiler_temp_max_increase_in_window,\n" +
            "       max(num_of_statistic_records_1)         as num_of_statistic_records_1\n" +
            "from (\n" +
            "         select year1,\n" +
            "                doy,\n" +
            "                day_of_week_starting_monday,\n" +
            "                day_of_week_text,\n" +
            "                hour_of_day,\n" +
            "                boiler_temp,\n" +
            "                measurement_date,\n" +
            "                measurement_date_t0,\n" +
            "                measurement_date_t1,\n" +
            "                case when boiler_temp_max_decrease_in_window > 0 then 0 else boiler_temp_max_decrease_in_window end     boiler_temp_max_decrease_in_window,\n" +
            "                case when boiler_temp_max_increase_in_window <= 0.11 then 0 else boiler_temp_max_increase_in_window end boiler_temp_max_increase_in_window,\n" +
            "                num_of_statistic_records_1\n" +
            "         from (\n" +
            "                  select year1,\n" +
            "                         doy,\n" +
            "                         day_of_week_starting_monday,\n" +
            "                         day_of_week_text,\n" +
            "                         hour_of_day,\n" +
            "                         boiler_temp,\n" +
            "                         measurement_date,\n" +
            "                         measurement_date_t0,\n" +
            "                         measurement_date_t1,\n" +
            "                         -1.0 * GREATEST(boiler_temp_window_tMax - boiler_temp_window_t1, boiler_temp_window_t0 - boiler_temp_window_tMin) boiler_temp_max_decrease_in_window,\n" +
            "                         GREATEST(boiler_temp_window_tMax - boiler_temp_window_t0, boiler_temp_window_t1 - boiler_temp_window_tMin)        boiler_temp_max_increase_in_window,\n" +
            "                         boiler_temp_window_t0,\n" +
            "                         boiler_temp_window_t1,\n" +
            "                         boiler_temp_window_tMin,\n" +
            "                         boiler_temp_window_tMax,\n" +
            "                         num_of_statistic_records_1\n" +
            "                  from (\n" +
            "                           select measurement_date,\n" +
            "                                  year1,\n" +
            "                                  doy,\n" +
            "                                  case when day_of_week_starting_sunday <= 0 then day_of_week_starting_sunday + 7 else day_of_week_starting_sunday end as day_of_week_starting_monday,\n" +
            "                                  To_Char(measurement_date, 'DAY')                                                                                        day_of_week_text,\n" +
            "                                  hour_of_day,\n" +
            "                                  boiler_temp,\n" +
            "                                  minute_of_hour,\n" +
            "                                  first_value(measurement_date) over ( partition by year1, doy, hour_of_day order by minute_of_hour)                      measurement_date_t0,\n" +
            "                                  last_value(measurement_date) over ( partition by year1, doy, hour_of_day order by minute_of_hour)                       measurement_date_t1,\n" +
            "                                  first_value(boiler_temp) over ( partition by year1, doy, hour_of_day order by minute_of_hour)                           boiler_temp_window_t0,\n" +
            "                                  last_value(boiler_temp) over ( partition by year1, doy, hour_of_day order by minute_of_hour)                            boiler_temp_window_t1,\n" +
            "                                  min(boiler_temp) over ( partition by year1, doy, hour_of_day order by minute_of_hour)                                   boiler_temp_window_tMin,\n" +
            "                                  max(boiler_temp) over ( partition by year1, doy, hour_of_day order by minute_of_hour)                                   boiler_temp_window_tMax,\n" +
            "                                  num_of_statistic_records_1\n" +
            "                           from (\n" +
            "                                    select measurement_date,\n" +
            "                                           extract(year from measurement_date)   year1,\n" +
            "                                           extract(doy from measurement_date)    doy, " +
            "                                           extract(hour from measurement_date)   hour_of_day,\n" +
            "                                           extract(minute from measurement_date) minute_of_hour,\n" +
            "                                           extract(dow from measurement_date) as day_of_week_starting_sunday,\n" +
            "                     To_Char(measurement_date , 'DAY') day_of_week_text,\n" +
            "                                           count(1) over ()                      num_of_statistic_records_1,\n" +
            "                                           h1.boiler_temp\n" +
            "                                    from heat_pump h1\n" +
            "                                    where 1 = 1\n" +
            "                                      and measurement_date >= :measurement_date_start " +
            "                                      and measurement_date <= :measurement_date_end   " +
            "                                    order by year1, doy, hour_of_day, minute_of_hour\n" +
            "                                ) h2\n" +
            "                       ) h3\n" +
            "              ) h4\n" +
            "     ) h5\n" +
            "group by year1, doy, day_of_week_starting_monday, day_of_week_text, hour_of_day\n" +
            "order by year1, doy, hour_of_day) h7\n" +
            "group by day_of_week_starting_monday, day_of_week_text \n" +
            "order by day_of_week_starting_monday", nativeQuery = true)
    List<BoilerStatsByDayOfWeek> getBoilerStatsByDayOfWeek(Date measurement_date_start, Date measurement_date_end);


    /**
     * ------------------------------------------------------------------------------------------------------------------------------------------------------------
     * -- SOLE IN/OUT difference while in operation (with a completely generic group-by)
     * ------------------------------------------------------------------------------------------------------------------------------------------------------------
     * a query to get the temperature difference between SOLE_IN and SOLE_OUT when the compressor is running for at least a few minutes.
     * this avoids the issue, that the readings tend to go to the environment temperature when no water is circulating in the beginning.
     * that query is to consolidate the stats if we run a long term analysis
     *
     * @param groupEveryNthSecond optional: either maxRows or groupEveryNthSecond. Ignored if <0
     * @param maxRows             optional: either maxRows or groupEveryNthSecond. Ignored if <0
     */
    @Query(value = "select min(measurement_date_start)                                        as measurementDateStart,\n" +
            "       max(measurement_date_end)                                          as measurementDateEnd,\n" +
            "       round(cast(avg(sole_in_out_delta_in_operation_avg) as numeric), 3) as soleInOutDeltaInOperationAvg,\n" +
            "       round(cast(min(sole_in_out_delta_in_operation_avg) as numeric), 3) as soleInOutDeltaInOperationMin,\n" +
            "       round(cast(max(sole_in_out_delta_in_operation_avg) as numeric), 3) as soleInOutDeltaInOperationMax,\n" +
            "       di10compressor1                                                    as compressorState,\n" +
            "       sum(number_of_probes)                                              as totalNumberOfProbesInSampleWindow\n" +
            "from (\n" +
            "         -- query to have generic grouping\n" +
            "         select h3.*,\n" +
            "                -- completely generic grouping based on param, either on time or records, depending what is given\n" +
            "                case\n" +
            "                    -- group by a given time in seconds\n" +
            "                    when :group_every_nth_second > 0 then (round(extract(epoch from measurement_date_start) / :group_every_nth_second))\n" +
            "                    -- group by max number of rows\n" +
            "                    when :maxRows > 0 then (ntile(:maxRows) over ( order by measurement_date_start )) -- avoid sub-query here, which destroys the over() window\n" +
            "                -- default grouping 1hr\n" +
            "                    else (round(extract(epoch from measurement_date_start) / 3600))\n" +
            "                    end as group_id\n" +
            "         from (\n" +
            "                  -- That select provides us the statistical values per run of the heatpump excluding the inital startup phase\n" +
            "                  select min(measurement_date)                                   measurement_date_start,\n" +
            "                         max(measurement_date)                                   measurement_date_end,\n" +
            "                         count(1)                                                number_of_probes,\n" +
            "                         di10compressor1,\n" +
            "                         round(cast(avg(sole_in) - avg(sole_out) as numeric), 1) sole_in_out_delta_in_operation_avg, -- most interesting column!\n" +
            "                         round(cast(avg(sole_in) as numeric), 1)                 sole_in_avg,\n" +
            "                         min(sole_in)                                            sole_in_min,\n" +
            "                         max(sole_in)                                            sole_in_max,\n" +
            "                         round(cast(avg(sole_out) as numeric), 1)                sole_out_avg,\n" +
            "                         min(sole_out)                                           sole_out_min,\n" +
            "                         max(sole_out)                                           sole_in_max\n" +
            "                  from (\n" +
            "                           select h1.*,\n" +
            "                                  first_value(measurement_date) over (partition by seq_id order by measurement_date)                                                    compressor_start,\n" +
            "                                  first_value(measurement_date) over (partition by seq_id order by measurement_date desc)                                               compressor_end,\n" +
            "                                  extract('epoch' from (first_value(measurement_date) over (partition by seq_id order by measurement_date desc) -\n" +
            "                                                        first_value(measurement_date) over (partition by seq_id order by measurement_date)))                         as compressor_runtime_in_seconds,\n" +
            "                                  extract('epoch' from (measurement_date - first_value(measurement_date) over (partition by seq_id order by measurement_date)))      as seconds_since_toggle_on,\n" +
            "                                  extract('epoch' from (first_value(measurement_date) over (partition by seq_id order by measurement_date desc) - measurement_date)) as seconds_before_toggle_off\n" +
            "                           from (\n" +
            "                                    select id,\n" +
            "                                           measurement_date,\n" +
            "                                           di10compressor1,\n" +
            "                                           sole_in,\n" +
            "                                           sole_out,\n" +
            "                                           -- gaps and island problem https://towardsdatascience.com/gaps-and-islands-with-mysql-b407040d133d\n" +
            "                                           row_number() over ( order by measurement_date) - row_number() over (partition by di10compressor1 order by measurement_date) as seq_id\n" +
            "                                    from heat_pump\n" +
            "                                    where 1 = 1\n" +
            "                                      and measurement_date >= :measurement_date_start -- TO_TIMESTAMP( '2017-03-31 9:30:20', 'YYYY-MM-DD HH24:MI:SS')\n" +
            "                                      and measurement_date <= :measurement_date_end   -- TO_TIMESTAMP( '2030-03-31 9:30:20', 'YYYY-MM-DD HH24:MI:SS')\n" +
            "                                    order by measurement_date desc\n" +
            "                                ) h1\n" +
            "                           order by measurement_date desc\n" +
            "                       ) h2\n" +
            "                  where 1 = 1\n" +
            "                    -- only take readings after the compressor did run for a while, also, ignore the ones shortly before turning off\n" +
            "                    and di10compressor1 = true\n" +
            "                    and seconds_since_toggle_on > 180         -- exclude the startup phase while temperature still adjusts, only consider measurements after 3 min of compressor runtime\n" +
            "                    and seconds_before_toggle_off > 60\n" +
            "                    and seconds_since_toggle_on < 3600 * 23   -- runs almost 24h must be an error\n" +
            "                    and seconds_before_toggle_off < 3600 * 23 -- runs almost 24h must be an error\n" +
            "                  group by seq_id, di10compressor1\n" +
            "                  order by measurement_date_start asc\n" +
            "              ) h3\n" +
            "     ) h4\n" +
            "group by group_id, di10compressor1\n" +
            "order by measurementDateStart\n", nativeQuery = true)
    List<SoleInOutDeltaInOperationStats> getSoleDeltaInOperationStats(
            @Param(value = "measurement_date_start") Date measurementDateStart,
            @Param(value = "measurement_date_end") Date measurementDateEnd,
            @Param(value = "maxRows") int maxRows,
            @Param(value = "group_every_nth_second") int groupEveryNthSecond
    );
}

