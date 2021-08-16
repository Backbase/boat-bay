package com.backbase.oss.boat.bay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Configuration
public class BoatMvcConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.removeConvertible(String.class, String[].class);
        registry.removeConvertible(String.class, Collection.class);
        registry.addConverter(String.class, String[].class, noCommaSplitStringToArrayConverter());
        registry.addConverter(String.class, Collection.class, noCommaSplitStringToListConverter());
    }

    @Bean
    public Converter<String, String[]> noCommaSplitStringToArrayConverter() {
        return new Converter<String, String[]>() {
            @Override
            public String[] convert(String source) {
                String[] arrayWithOneElement = {source};
                return arrayWithOneElement;
            }
        };
    }

    @Bean
    public Converter<String, Collection> noCommaSplitStringToListConverter() {
        return new Converter<String, Collection>() {
            @Override
            public Collection convert(String source) {
                return Collections.singletonList(source);
            }
        };
    }
}
