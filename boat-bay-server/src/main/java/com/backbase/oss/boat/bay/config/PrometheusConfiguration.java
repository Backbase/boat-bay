package com.backbase.oss.boat.bay.config;

import io.prometheus.client.exporter.PushGateway;
import java.net.URL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "prometheus.push.gateway.url")
public class PrometheusConfiguration {

    @Value("${prometheus.push.gateway.url}")
    private URL prometheusPushGatewayUrl;


    public PushGateway pushGateway() {
        return new PushGateway(prometheusPushGatewayUrl);
    }




}
