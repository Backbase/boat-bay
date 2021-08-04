package com.backbase.oss.boat.bay.config;

import java.io.File;
import java.util.List;

import com.backbase.oss.boat.bay.domain.Dashboard;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.SpecType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "boat.bay")
@Data
public class BoatBayConfigurationProperties {

    private File mavenSettingsFile = new File("~/.m2/settings.xml");

    private Bootstrap bootstrap;

    @Data
    public static class Bootstrap {

        private Dashboard dashboard;
        private List<SpecType> specTypes;

        @Data
        public static class Dashboard {
            private String navTitle;
            private String name;
            private String title;
            private String subTitle;
            private String content;
            private Portal defaultPortal;

            @Data
            public static class Portal {

                private String key;
                private String name;
                private String subTitle;
                private String logoUrl;
                private String logoLink;
                private Product defaultProduct;

                @Data
                public static class Product {
                    private String key;
                    private String name;
                }
            }
        }

        @Data
        public static class SpecType {

            private String name;
            private String matchSpEL;
            private String icon;
        }
    }

  }
