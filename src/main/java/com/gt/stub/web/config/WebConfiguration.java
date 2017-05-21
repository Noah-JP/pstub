package com.gt.stub.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * Created by noah on 2017. 5. 21..
 */
@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {


    @Bean
    public ObjectMapper upperCaseObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(new PropertyNamingStrategy.PropertyNamingStrategyBase(){
            @Override
            public String translate(String propertyName) {
                return propertyName.toUpperCase();
            }
        });
        return objectMapper;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new FormHttpMessageConverter());
        super.configureMessageConverters(converters);
    }
}
