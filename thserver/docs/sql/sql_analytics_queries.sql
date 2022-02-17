-- noinspection SqlResolveForFile


------------------------------------------------------------------------------------------------------------------------------------------------------------
-- Query validated on a few sample, seems to be OK
-- Query based on local Min and Max
------------------------------------------------------------------------------------------------------------------------------------------------------------
-- query to check error
select sum(boiler_temp_max_decrease_in_window) boiler_temp_max_decrease_in_window,
       sum(boiler_temp_max_increase_in_window) boiler_temp_max_increase_in_window
from (
         select min(measurement_date_t0)                as measurement_date_t0,
                max(measurement_date_t1)                as measurement_date_t1,
                day_of_week_starting_monday,
                day_of_week_text,
                min(boiler_temp_max_decrease_in_window) as boiler_temp_max_decrease_in_window,
                max(boiler_temp_max_increase_in_window) as boiler_temp_max_increase_in_window,
                max(num_of_statistic_records_1)         as num_of_statistic_records_1
         from (
                  select year1,
                         doy,
                         day_of_week_starting_monday,
                         day_of_week_text,
                         hour_of_day,
                         boiler_temp,
                         measurement_date,
                         measurement_date_t0,
                         measurement_date_t1,
                         case when boiler_temp_max_decrease_in_window > 0 then 0 else boiler_temp_max_decrease_in_window end     boiler_temp_max_decrease_in_window,
                         -- remove remaining noise for increase... It's very unlikely, that water heats up only for 0.1 °C in one hour. If the boiler jumps in, then more...
                         case when boiler_temp_max_increase_in_window <= 0.11 then 0 else boiler_temp_max_increase_in_window end boiler_temp_max_increase_in_window,
                         num_of_statistic_records_1
                  from (
                           select year1,
                                  doy,
                                  day_of_week_starting_monday,
                                  day_of_week_text,
                                  hour_of_day,
                                  boiler_temp,
                                  measurement_date,
                                  measurement_date_t0,
                                  measurement_date_t1,
                                  -1.0 * GREATEST(boiler_temp_window_tMax - boiler_temp_window_t1, boiler_temp_window_t0 - boiler_temp_window_tMin) boiler_temp_max_decrease_in_window,
                                  GREATEST(boiler_temp_window_tMax - boiler_temp_window_t0, boiler_temp_window_t1 - boiler_temp_window_tMin)        boiler_temp_max_increase_in_window,
                                  boiler_temp_window_t0,
                                  boiler_temp_window_t1,
                                  boiler_temp_window_tMin,
                                  boiler_temp_window_tMax,
                                  num_of_statistic_records_1
                           from (
                                    select measurement_date,
                                           year1,
                                           doy,
                                           case when day_of_week_starting_sunday <= 0 then day_of_week_starting_sunday + 7 else day_of_week_starting_sunday end as day_of_week_starting_monday,
                                           To_Char(measurement_date, 'DAY')                                                                                        day_of_week_text,
                                           hour_of_day,
                                           boiler_temp,
                                           minute_of_hour,
                                           first_value(measurement_date) over ( partition by year1, doy, hour_of_day order by minute_of_hour)                      measurement_date_t0,
                                           last_value(measurement_date) over ( partition by year1, doy, hour_of_day order by minute_of_hour)                       measurement_date_t1,
                                           first_value(boiler_temp) over ( partition by year1, doy, hour_of_day order by minute_of_hour)                           boiler_temp_window_t0,
                                           last_value(boiler_temp) over ( partition by year1, doy, hour_of_day order by minute_of_hour)                            boiler_temp_window_t1,
                                           min(boiler_temp) over ( partition by year1, doy, hour_of_day order by minute_of_hour)                                   boiler_temp_window_tMin,
                                           max(boiler_temp) over ( partition by year1, doy, hour_of_day order by minute_of_hour)                                   boiler_temp_window_tMax,
                                           num_of_statistic_records_1
                                    from (
                                             select measurement_date,
                                                    extract(year from measurement_date)   year1,
                                                    extract(doy from measurement_date)    doy, -- day of the year
                                                    extract(hour from measurement_date)   hour_of_day,
                                                    extract(minute from measurement_date) minute_of_hour,
                                                    extract(dow from measurement_date) as day_of_week_starting_sunday,
                                                    To_Char(measurement_date, 'DAY')      day_of_week_text,
                                                    count(1) over ()                      num_of_statistic_records_1,
                                                    h1.boiler_temp
                                             from heat_pump h1
                                             where 1 = 1
                                               and measurement_date >= :measurement_date_start -- TO_TIMESTAMP( '2017-03-31 9:30:20', 'YYYY-MM-DD HH24:MI:SS')
                                               and measurement_date <= :measurement_date_end   -- TO_TIMESTAMP( '2030-03-31 9:30:20', 'YYYY-MM-DD HH24:MI:SS')
                                             order by year1, doy, hour_of_day, minute_of_hour
                                         ) h2
                                ) h3
                       ) h4
              ) h5
         group by year1, doy, day_of_week_starting_monday, day_of_week_text, hour_of_day
         order by year1, doy, hour_of_day) h7;



------------------------------------------------------------------------------------------------------------------------------------------------------------
-- simple query, does NOT work properly
------------------------------------------------------------------------------------------------------------------------------------------------------------
select sum(boiler_temp_incr), sum(boiler_temp_desc)
from (
         select measurement_date,
                case when boiler_temp_delta < 0 then boiler_temp_delta else 0 end as boiler_temp_incr,
                case when boiler_temp_delta > 0 then boiler_temp_delta else 0 end as boiler_temp_desc
         from (
                  select measurement_date,
                         boiler_temp,
                         (lag(boiler_temp) over (order by measurement_date) - boiler_temp) as boiler_temp_delta
                  from heat_pump hp
                  order by measurement_date asc) h1) h2;





------------------------------------------------------------------------------------------------------------------------------------------------------------
-- Query to get the time when the compressor was on or off and some stats around that
------------------------------------------------------------------------------------------------------------------------------------------------------------
with q1 as (
    select seq_id,
           di10compressor1,
           count(1)                                                              as probs_in_between,
           min(measurement_date)                                                 as measurement_date_start,
           max(measurement_date)                                                 as measurement_date_end,
           (max(measurement_date) - min(measurement_date))                       as dt,
           extract('epoch' from (max(measurement_date) - min(measurement_date))) as dt_in_seconds,
           count(1) over ()                                                      as total_number_of_toggles,
           groupid
    from (
             select id,
                    measurement_date,
                    di10compressor1,
                    -- gaps and island problem https://towardsdatascience.com/gaps-and-islands-with-mysql-b407040d133d
                    row_number() over ( order by measurement_date) - row_number() over (partition by di10compressor1 order by measurement_date) as seq_id,
                    -- have a generic grouping in seconds
                    round(extract(epoch from measurement_date) / :group_every_nth_second)                                                          groupid
             from heat_pump
             where 1 = 1
               and measurement_date >= :measurement_date_start -- TO_TIMESTAMP( '2017-03-31 9:30:20', 'YYYY-MM-DD HH24:MI:SS')
               and measurement_date <= :measurement_date_end   -- TO_TIMESTAMP( '2030-03-31 9:30:20', 'YYYY-MM-DD HH24:MI:SS')
             order by measurement_date desc) h1
    group by seq_id, di10compressor1, groupid
    order by measurement_date_start desc)
-- main query
select min(measurement_date_start)                   as measurement_date_start,
       max(measurement_date_end)                     as measurement_date_end,
       di10compressor1                               as di10compressor1_state,
       count(1)                                      as nr_of_toggles,
       round(cast(avg(dt_in_seconds) as numeric), 2) as average_time_in_seconds,
       min(dt_in_seconds)                            as min_time_in_seconds,
       max(dt_in_seconds)                            as max_time_in_seconds,
       avg(dt)                                       as average_interval,
       min(dt)                                       as min_interval,
       max(dt)                                       as max_interval,
       groupid                                       as groupid
from q1
group by di10compressor1, groupid
-- sort by groupId to keep groups together, as they don't start at the same moment
order by groupid asc, di10compressor1, di10compressor1
;

------------------------------------------------------------------------------------------------------------------------------------------------------------
-- histogram of the compressor length of a run
------------------------------------------------------------------------------------------------------------------------------------------------------------
-- Query to get the time when the compressor was on or off and some stats around that
with q1 as (
    select seq_id,
           di10compressor1,
           count(1)                                                              as probs_in_between,
           min(measurement_date)                                                 as measurement_date_start,
           max(measurement_date)                                                 as measurement_date_end,
           (max(measurement_date) - min(measurement_date))                       as dt,
           extract('epoch' from (max(measurement_date) - min(measurement_date))) as dt_in_seconds,
           count(1) over ()                                                      as total_number_of_toggles,
           groupid
    from (
             select id,
                    measurement_date,
                    di10compressor1,
                    -- gaps and island problem https://towardsdatascience.com/gaps-and-islands-with-mysql-b407040d133d
                    row_number() over ( order by measurement_date) - row_number() over (partition by di10compressor1 order by measurement_date) as seq_id,
                    -- completely generic grouping based on param, either on time or records, depending what is given
                    case
                        -- group by a given time in seconds
                        when :group_every_nth_second>0 then (round(extract(epoch from measurement_date) / :group_every_nth_second))
                        -- group by max number of rows
                        when :maxRows>0 then ( select ntile(:maxRows) over ( order by measurement_date ))
                        -- default grouping 1hr
                        else (round(extract(epoch from measurement_date) / 3600))
                        end as groupid
             from heat_pump
             where 1 = 1
               and measurement_date >= :measurement_date_start -- TO_TIMESTAMP( '2017-03-31 9:30:20', 'YYYY-MM-DD HH24:MI:SS')
               and measurement_date <= :measurement_date_end   -- TO_TIMESTAMP( '2030-03-31 9:30:20', 'YYYY-MM-DD HH24:MI:SS')
             order by measurement_date desc) h1
    group by seq_id, di10compressor1, groupid
    order by measurement_date_start desc)
-- main query
select WIDTH_BUCKET(dt_in_seconds, 0, 3600, 20)                                  as bucket_nr,
       to_char(min(dt), 'HH24:MI:SS') || ' - ' || to_char(max(dt), 'HH24:MI:SS') as bucket_description,
       round(min(dt_in_seconds))                                                 as dt_in_seconds_min,
       round(max(dt_in_seconds))                                                 as dt_in_seconds_max,
       count(1)                                                                     number_of_comressor_starts
from q1
where di10compressor1 = true
group by bucket_nr
order by bucket_nr
;


------------------------------------------------------------------------------------------------------------------------------------------------------------
-- SOLE IN/OUT difference while in operation
------------------------------------------------------------------------------------------------------------------------------------------------------------
-- a query to get the temperature difference between SOLE_IN and SOLE_OUT when the compressor is running for at least a few minutes.
-- this avoids the issue, that the readings tend to go to the environment temperature when no water is circulating
select min(measurement_date)                                   measurement_date_start,
       max(measurement_date)                                   measurement_date_end,
       count(1)                                                number_of_probes,
       di10compressor1,
       round(cast(avg(sole_in) - avg(sole_out) as numeric), 1) sole_in_out_delta_in_operation_avg, -- most interesting column
       round(cast(avg(sole_in) as numeric), 1)                 sole_in_avg,
       min(sole_in)                                            sole_in_min,
       max(sole_in)                                            sole_in_max,
       round(cast(avg(sole_out) as numeric), 1)                sole_out_avg,
       min(sole_out)                                           sole_out_min,
       max(sole_out)                                           sole_in_max
from (
         select h1.*,
                first_value(measurement_date) over (partition by seq_id order by measurement_date)                                                                                                                      compressor_start,
                first_value(measurement_date) over (partition by seq_id order by measurement_date desc)                                                                                                                 compressor_end,
                extract('epoch' from (first_value(measurement_date) over (partition by seq_id order by measurement_date desc) - first_value(measurement_date) over (partition by seq_id order by measurement_date))) as compressor_runtime_in_seconds,
                extract('epoch' from (measurement_date - first_value(measurement_date) over (partition by seq_id order by measurement_date)))                                                                        as seconds_since_toggle_on,
                extract('epoch' from (first_value(measurement_date) over (partition by seq_id order by measurement_date desc) - measurement_date))                                                                   as seconds_before_toggle_off
         from (
                  select id,
                         measurement_date,
                         di10compressor1,
                         sole_in,
                         sole_out,
                         -- gaps and island problem https://towardsdatascience.com/gaps-and-islands-with-mysql-b407040d133d
                         row_number() over ( order by measurement_date) - row_number() over (partition by di10compressor1 order by measurement_date) as seq_id,
                         -- completely generic grouping based on param, either on time or records, depending what is given
                         case
                             -- group by a given time in seconds
                             when :group_every_nth_second > 0 then (round(extract(epoch from measurement_date) / :group_every_nth_second))
                             -- group by max number of rows
                             when :maxRows > 0 then (select ntile(:maxRows) over ( order by measurement_date ))
                             -- default grouping 1hr
                             else (round(extract(epoch from measurement_date) / 3600))
                             end                                                                                                                     as groupid
                  from heat_pump
                  where 1 = 1
                    and measurement_date >= :measurement_date_start -- TO_TIMESTAMP( '2017-03-31 9:30:20', 'YYYY-MM-DD HH24:MI:SS')
                    and measurement_date <= :measurement_date_end   -- TO_TIMESTAMP( '2030-03-31 9:30:20', 'YYYY-MM-DD HH24:MI:SS')
                  order by measurement_date desc
              ) h1
         order by measurement_date desc
     ) h2
where 1 = 1
  -- only take readings after the compressor did run for a while, also, ignore the ones shortly before turning off
  and di10compressor1 = true
  and seconds_since_toggle_on > 180
  and seconds_before_toggle_off > 60
  and seconds_since_toggle_on < 3600 * 23   -- runs almost 24h must be an error
  and seconds_before_toggle_off < 3600 * 23 -- runs almost 24h must be an error
group by groupid, di10compressor1
order by measurement_date_start asc
;
