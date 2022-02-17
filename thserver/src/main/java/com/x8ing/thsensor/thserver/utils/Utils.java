package com.x8ing.thsensor.thserver.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.ToDoubleFunction;

public class Utils {

    private static final Logger log = LoggerFactory.getLogger(Utils.class);


    private static final String[] IP_HEADERS = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"

            // you can add more matching headers here ...
    };

    public static String getRequestIP(HttpServletRequest request) {
        for (String header : IP_HEADERS) {
            String value = request.getHeader(header);
            if (value == null || value.isEmpty()) {
                continue;
            }
            return Arrays.toString(value.split("\\s*,\\s*"));

        }
        return Arrays.toString(new String[]{request.getRemoteAddr()});
    }

    public static <T> double getMedian(Collection<T> entries, ToDoubleFunction<T> valueSupplier, int limit) {

        assert entries != null : "Entries must not be null";
        assert valueSupplier != null : "Supplier must not be null";

        Median median = new Median();

        if (limit < 0) {
            limit = 0;
        }

        int samples = entries.size();
        if (limit > 0 && entries.size() >= limit) {
            samples = limit;
        }

        double[] values = new double[samples];

        Iterator<T> iterator = entries.iterator();
        int startPos = entries.size() - samples;
        int posRel = 0;
        for (int i = 0; i < entries.size(); i++) {
            double value = valueSupplier.applyAsDouble(iterator.next());


            if (i < startPos) {
                continue;
            }

            values[posRel] = value;
            posRel++;
        }

        return median.evaluate(values);
    }

    public static <T> T getLastElement(final Collection<T> c) {
        if (c == null || c.size() <= 0) {
            return null;
        }
        final Iterator<T> itr = c.iterator();
        T lastElement = itr.next();
        while (itr.hasNext()) {
            lastElement = itr.next();
        }
        return lastElement;
    }

    public static BigDecimal toBigDecimalWithRounding(double d) {
        return toBigDecimalWithRounding(d, 3);
    }

    public static BigDecimal toBigDecimalWithRounding(double d, int precision) {
        return new BigDecimal(d).setScale(precision, RoundingMode.HALF_UP);
    }

    public static LocalDateTime convertUTCToSwitzerlandTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("Europe/Zurich")).toLocalDateTime();
    }

    public static String formatLocalDateTimeToLocalSwitzerlandTime(LocalDateTime localDateTime) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd MM yyyy HH:mm:ss");
        return df.format(convertUTCToSwitzerlandTime(localDateTime));
    }

    public static RestTemplate getRestTemplate() {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setConnectTimeout(5000);
        simpleClientHttpRequestFactory.setReadTimeout(5000);
        return new RestTemplate(simpleClientHttpRequestFactory);
    }

    public static String toJSON(Object o) {
        if (o == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error("Error serializing object to JSON. object=" + o + " e=" + e.toString(), e);
            return "";
        }
    }
}
