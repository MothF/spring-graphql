package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.graphql.data.method.annotation.support.AnnotatedControllerConfigurer;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

//@SuppressWarnings("Convert2Lambda")
//@Configuration
public class ScalarsConversionConfig {
    /*private static final ZoneId SYSTEM_TIMEZONE = ZoneId.systemDefault();

    //not working
    public ScalarsConversionConfig(AnnotatedControllerConfigurer annotatedControllerConfigurer) {
        annotatedControllerConfigurer.setConversionService(new ScalarsConversionService());
    }

    // not working
    @Bean
    public AnnotatedControllerConfigurer annotatedControllerConfigurer() {
        AnnotatedControllerConfigurer annotatedControllerConfigurer = new AnnotatedControllerConfigurer();
        annotatedControllerConfigurer.setConversionService(new ScalarsConversionService());
        return annotatedControllerConfigurer;
    }

    static void addInstantConverters(ConverterRegistry converterRegistry) {
        converterRegistry.addConverter(new Converter<Instant, OffsetDateTime>() {
            @Override
            public OffsetDateTime convert(Instant source) {
                return OffsetDateTime.ofInstant(source, SYSTEM_TIMEZONE);
            }
        });
        converterRegistry.addConverter(new Converter<Instant, ZonedDateTime>() {
            @Override
            public ZonedDateTime convert(Instant source) {
                return ZonedDateTime.ofInstant(source, SYSTEM_TIMEZONE);
            }
        });
        converterRegistry.addConverter(new Converter<Instant, LocalDateTime>() {
            @Override
            public LocalDateTime convert(Instant source) {
                return LocalDateTime.ofInstant(source, SYSTEM_TIMEZONE);
            }
        });
    }

    static class ScalarsConversionService extends DefaultFormattingConversionService {

        public ScalarsConversionService() {
            addInstantConverters(this);
        }
    }*/
}
