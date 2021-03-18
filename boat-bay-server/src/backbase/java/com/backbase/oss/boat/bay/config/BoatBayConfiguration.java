package com.backbase.oss.boat.bay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.File;

@ConfigurationProperties(prefix = "boat.bay")
@Data
public class BoatBayConfiguration {

    private Bootstrap bootstrap;

    @Data
    public static class Bootstrap {

        private File file;
        private Boolean enabled;
    }
}
