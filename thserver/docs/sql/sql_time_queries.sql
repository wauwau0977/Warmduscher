-- look back a parameterized number of minutes / time
select
    min(temperature) tempMin,
    max(temperature) tempMax,
    min(temperature_measure_date),
    max(temperature_measure_date)
from
    meteo_swiss m2
where
        m2.station_id = :station_id
  and temperature_measure_date > current_timestamp  - (interval '1' minute) * :minute_lookback;
;
