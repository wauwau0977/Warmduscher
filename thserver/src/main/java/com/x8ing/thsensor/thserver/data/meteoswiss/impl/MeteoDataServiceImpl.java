package com.x8ing.thsensor.thserver.data.meteoswiss.impl;

import com.jayway.jsonpath.*;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import com.x8ing.thsensor.thserver.Profiles;
import com.x8ing.thsensor.thserver.data.meteoswiss.MeteoDataService;
import com.x8ing.thsensor.thserver.db.entity.meteoswiss.MeteoSwissEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;

@Component
@Profile("!" + Profiles.SENSOR_MOCK)
public class MeteoDataServiceImpl implements MeteoDataService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${thserver.meteoSwiss.urlSunshine}")
    private String urlSunshine;

    @Value("${thserver.meteoSwiss.urlTemperature}")
    private String urlTemperature;

    @Value("${thserver.meteoSwiss.urlWindGust}")
    private String urlWindGust;

    @Value("${thserver.meteoSwiss.stationIds}")
    private List<String> stationIds;

    public void init() {
        log.info("Init");

        Configuration.setDefaults(new Configuration.Defaults() {

            private final JsonProvider jsonProvider = new JacksonJsonProvider();
            private final MappingProvider mappingProvider = new JacksonMappingProvider();

            @Override
            public JsonProvider jsonProvider() {
                return jsonProvider;
            }

            @Override
            public MappingProvider mappingProvider() {
                return mappingProvider;
            }

            @Override
            public Set<Option> options() {
                return EnumSet.noneOf(Option.class);
            }
        });

    }


    @Override
    public List<MeteoSwissEntity> getData() {

        long t0 = System.currentTimeMillis();
        log.info("About to query the MeteoSwiss service for live temperature, sunshine and windgust info.");

        String sunshineJSON = callService(urlSunshine);
        String temperatureJSON = callService(urlTemperature);
        String windGustJSON = callService(urlWindGust);

        long dtServiceMeteoSwiss = System.currentTimeMillis() - t0;

        List<MeteoSwissEntity> entities = new ArrayList<>();
        for (String stationId : stationIds) {

            MeteoSwissEntity entity = new MeteoSwissEntity();
            entity.setStationId(stationId);

            ResDateValue sunshine = extractFromJSON(sunshineJSON, stationId, null);
            entity.setSunshine(sunshine.getValue1());
            entity.setSunshineMeasureDate(sunshine.getMeasurementDate());

            ResDateValue temperature = extractFromJSON(temperatureJSON, stationId, null);
            entity.setTemperature(temperature.getValue1());
            entity.setTemperatureMeasureDate(temperature.getMeasurementDate());

            ResDateValue windGust = extractFromJSON(windGustJSON, stationId, "wind_direction");
            entity.setWindGustSpeed(windGust.getValue1());
            entity.setWindDirection(windGust.getValue2());
            entity.setWindMeasureDate(windGust.getMeasurementDate());

            entity.setStationName(temperature.getStationName());

            entities.add(entity);
        }

        log.info("MeteoSwiss data polling completed. "
                + "dt[ms]=" + (System.currentTimeMillis() - t0)
                + " dtServiceMeteoSwiss=" + dtServiceMeteoSwiss
                + " numberOfStations=" + stationIds.size()
                + " stationIds:" + stationIds);

        return entities;
    }

    private ResDateValue extractFromJSON(String json, String stationId, String value2Property) {

        DocumentContext parsed = JsonPath.parse(json);

        //Double temperature = ((ArrayList)parsed.read(basePath + ".value")).get(0);
        TypeRef<List<String>> typeRefString = new TypeRef<>() {
        };
        TypeRef<List<Double>> typeRefDouble = new TypeRef<>() {
        };

        String basePath = "$[*][?(@.id=='" + stationId + "')].properties";
        String timeStamp = parsed.read(basePath + ".reference_ts", typeRefString).get(0); // e.g. 2022-01-30T17:50:00Z

        TemporalAccessor ta = DateTimeFormatter.ISO_INSTANT.parse(timeStamp);
        Instant i = Instant.from(ta);
        Date measureTimeStamp = Date.from(i);

        Double value1 = parsed.read(basePath + ".value", typeRefDouble).get(0);

        Double value2 = null;
        if (value2Property != null) {
            value2 = parsed.read(basePath + "." + value2Property, typeRefDouble).get(0);
        }
        String stationName = parsed.read(basePath + ".station_name", typeRefString).get(0);

        return new ResDateValue(measureTimeStamp, value1, value2, stationName);
    }

    private String callService(String url) {
        RestTemplate restTemplate = new RestTemplate();
        // important: set UTF8, otherwise RestTemplate will do ISO
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate.getForObject(url, String.class);
    }

    @Data
    @AllArgsConstructor
    private static class ResDateValue {
        private Date measurementDate;

        private Double value1;

        private Double value2;

        private String stationName;

    }
}
