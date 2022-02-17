package com.x8ing.thsensor.thserver.web;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Configuration
@SpringBootConfiguration
public class DateTimeConfig {

    // private static final String dateFormat = "yyyyMMdd";
    // private static final String dateTimeFormat = "yyyyMMdd-HHmmss";

    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ISO_DATE;
    private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ISO_DATE_TIME;


    @Bean
    public FormattingConversionService conversionService() {
        DefaultFormattingConversionService conversionService =
                new DefaultFormattingConversionService(false);

        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setDateFormatter(dateFormat);
        registrar.setDateTimeFormatter(dateTimeFormat);
        // registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        registrar.registerFormatters(conversionService);

        // other desired formatters

        return conversionService;
    }


    // @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            builder.simpleDateFormat(dateTimeFormat.toString());
            builder.serializers(new LocalDateSerializer(dateFormat));
            builder.serializers(new LocalDateTimeSerializer(dateTimeFormat));
        };
    }


    @Bean
    public Formatter<LocalDate> localDateFormatter() {
        return new Formatter<LocalDate>() {
            @Override
            public LocalDate parse(String text, Locale locale) throws ParseException {
                return LocalDate.parse(text, dateFormat);
            }

            @Override
            public String print(LocalDate object, Locale locale) {
                return dateFormat.format(object);
            }
        };
    }

    @Bean
    public Formatter<LocalDateTime> localDateTimeFormatter() {
        return new Formatter<LocalDateTime>() {
            @Override
            public LocalDateTime parse(String text, Locale locale) throws ParseException {
                return LocalDateTime.parse(text, dateTimeFormat);
            }

            @Override
            public String print(LocalDateTime object, Locale locale) {
                return dateTimeFormat.format(object);
            }
        };
    }
}
