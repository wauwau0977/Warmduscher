select * from session_device sd order by session_create_date desc;

select * from session_request_v;
select * from session_request_v where agent_string not like '%SM-N986B%' and ip !='[139.178.15.160]' and ip!='[192.168.1.220]' and (client_id not in('2268903091311958932','4245618976225540740', '25024834181519266384') or client_id is null);
select * from session_request_v where agent_string not like '%SM-N986B%' and ip !='[139.178.15.160]';
select * from session_request_v where client_id = '21310822171722163564'; -- iphone 14.6
select * from session_request_v where agent_string like '%14_6%'; -- iphone 14.6
select * from session_request_v where agent_string like '%iPhone%'; -- iphone 14.6
select * from session_request_v where agent_string like '%FP3%'; -- iphone 14.6
select * from session_request_v where ex is not null;



select * from session_request sr order by request_date desc;
select * from session_request sr where ip = '[139.178.15.160]' order by request_date desc;
select * from session_request sr where client_id not in ('503067257529145551', '2268903091311958932','32209267332037223782') order by request_date desc;


select
    count(1),
    client_id,
    --session_id ,
    max(request_date) request_date ,
    max(client_version) client_version
from
    session_request sr
group by
    client_id
    --, session_id
order by
    3 desc
;

select * from session_request sr where client_id not in ('41623015051281333564234021998263059548') order by request_date desc;


select * from heat_pump hp order by measurement_date desc;
select measurement_date , ireg300temp_outdoor  from heat_pump hp order by measurement_date desc;


select min(measurement_date), max(measurement_date) from heat_pump hp ;
select * from heat_pump hp order by measurement_date desc;
select min(measurement_date) from heat_pump hp where ireg152boiler2 is not null;
select * from heat_pump hp where id ='SSkp4gT8ukwqSudDAqB1SV';


-- update heat_pump set ireg152boiler2 = ireg152boiler2 *10 where ireg152boiler2 < 35;
-- commit;

delete from heat_pump where boiler_temp =0;
commit;


drop view session_request_v;
create view session_request_v as
select
    sr.id,
    sr.session_id ,
    sr.request_date ,
    sr."path" ,
    sr.http_status st,
    sr.processing_time dt,
    sr.ip,
    sr."exception" ex,
    sr.client_id ,
    sr.client_version ,
    sd.agent_string ,
    sd.ip ip_sd
from
    session_request sr
        left join session_device sd on
            sr.session_id = sd.session_id
order by
    sr.request_date desc
;

select * from session_request_v ;

select distinct * from ( select
                             di10compressor1,
                             di11compressor2,
                             di12valve4,
                             di13,
                             di14pump_direct,
                             di150,
                             di151,
                             di152,
                             di153,
                             di154,
                             di15pump_boiler,
                             di16we,
                             di17boiler_el,
                             di18pool_pump,
                             di19heat_pump_on,
                             di1error,
                             di20error,
                             di21pump_primary,
                             di22pump_load,
                             di23pump_ground_water,
                             di30compressor1ready,
                             di31compressor2ready,
                             di70pumphk1,
                             di71hkm1ix_open,
                             di72hkm1ix_close,
                             di90pumphk2,
                             di91hkm2ix_open,
                             di92hkm2ix_close,
                             -- ireg152boiler2,
                             -- ireg170temp_psp,
                             -- ireg300temp_outdoor,
                             -- ireg50circ_temp,
                             ireg70temp_circ1,
                             ireg90temp_circ2
                         from
                             heat_pump hp ) x;



------------------------------------------------------------------------------------------------------------------------
-- METEO_SWISS: connect heat-pump and meteo swiss data with LATERAL query
------------------------------------------------------------------------------------------------------------------------
create or replace view heat_pump_meteo_swiss_v as
select
    hp.id, hp.boiler_temp, hp.compressor_hours, hp.heating_in, hp.heating_out, hp.measurement_date, hp.sole_in, hp.sole_out, hp.di10compressor1, hp.di11compressor2, hp.di12valve4, hp.di13, hp.di14pump_direct, hp.di150, hp.di151, hp.di152, hp.di153, hp.di154, hp.di15pump_boiler, hp.di16we, hp.di17boiler_el, hp.di18pool_pump, hp.di19heat_pump_on, hp.di1error, hp.di20error, hp.di21pump_primary, hp.di22pump_load, hp.di23pump_ground_water, hp.di30compressor1ready, hp.di31compressor2ready, hp.di70pumphk1, hp.di71hkm1ix_open, hp.di72hkm1ix_close, hp.di90pumphk2, hp.di91hkm2ix_open, hp.di92hkm2ix_close, hp.ireg152boiler2, hp.ireg170temp_psp, hp.ireg300temp_outdoor, hp.ireg50circ_temp, hp.ireg70temp_circ1, hp.ireg90temp_circ2 ,
    ms.id as ms_id, ms.create_date ms_create_date, ms.station_id, ms.sunshine, ms.sunshine_measure_date, ms.temperature, ms.temperature_measure_date, ms.station_name, ms.wind_direction, ms.wind_gust_speed, ms.wind_measure_date,
    ((EXTRACT(EPOCH FROM (ms.temperature_measure_date - hp.measurement_date))) ) measurement_delta_in_seconds
from
    heat_pump hp
        ,
    lateral (
        select
            ms.*
        from
            meteo_swiss ms
        where ms.station_id = 'KLO'
          and abs(EXTRACT(EPOCH FROM (ms.temperature_measure_date - hp.measurement_date))) < 90*60  -- limit time, just consider measurements withing 90 minutes to match
        order by
            -- abs(cast(ms.temperature_measure_date as DATE) - cast(hp.measurement_date as DATE) )
            abs(EXTRACT(EPOCH FROM (ms.temperature_measure_date - hp.measurement_date)))
        limit 1
        ) ms
order by hp.measurement_date desc
;

-- see temperature diff between local and Meteo Swiss, ordered by extreme values
select * from (select id, measurement_date , temperature_measure_date, ireg300temp_outdoor , temperature , ireg300temp_outdoor-temperature temp_diff from heat_pump_meteo_swiss_v where temperature is not null order by measurement_date desc) d order by abs(temp_diff) desc;


------------------------------------------------------------------------------------------------------------------------
-- correct outdoor temperature for negative values
------------------------------------------------------------------------------------------------------------------------
update heat_pump set ireg300temp_outdoor =  ((ireg300temp_outdoor*10 - 65535 - 1)/10)
where ireg300temp_outdoor*10 > 32768;


update heat_pump set ireg300temp_outdoor = ireg300temp_outdoor / 10
where measurement_date <  '2022-01-24 09:26:57.566' and ireg300temp_outdoor <0
;

select measurement_date, ireg300temp_outdoor  from heat_pump hp
where measurement_date <  '2022-01-24 09:26:57.566' and ireg300temp_outdoor <0
order by measurement_date desc;

select measurement_date, ireg300temp_outdoor  from heat_pump hp  order by measurement_date desc;

select * from session_request_12;
select * from session_device_12;




select id, "path", request_date, session_id, client_id, client_version, "exception", http_status, ip, processing_time  from session_request sr ;

insert into session_request_12 (id, "path", request_date, session_id, client_id, client_version, "exception", http_status, ip, processing_time ) (
    select * from session_request  where id in (
        select  id  as ip from session_request_v where
                agent_string not like '%SM-N986B%' and ip !='[139.178.15.160]' and ip!='[192.168.1.220]'
                                                   and (client_id not in('2268903091311958932','4245618976225540740', '25024834181519266384') or client_id is null)
        except
        select id  from session_request_12  ));

insert into se
select * from session_device sd  where id in (
    select distinct session_id  from session_request  where id in (
        select  id  as ip from session_request_v where
                agent_string not like '%SM-N986B%' and ip !='[139.178.15.160]' and ip!='[192.168.1.220]'
                                                   and (client_id not in('2268903091311958932','4245618976225540740', '25024834181519266384') or client_id is null)
        except
        select id  from session_request_12  ));

commit;