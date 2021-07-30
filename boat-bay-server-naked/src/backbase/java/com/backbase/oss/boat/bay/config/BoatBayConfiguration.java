package com.backbase.oss.boat.bay.config;

import java.io.File;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
