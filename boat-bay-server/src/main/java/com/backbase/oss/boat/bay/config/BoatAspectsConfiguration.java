package com.backbase.oss.boat.bay.config;

import com.backbase.oss.boat.bay.aop.spec.SpecRepositoryAspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class BoatAspectsConfiguration {


    @Bean
    public SpecRepositoryAspect specRepositoryAspect(ApplicationEventPublisher eventPublisher) {
        return new SpecRepositoryAspect(eventPublisher);
    }


}
