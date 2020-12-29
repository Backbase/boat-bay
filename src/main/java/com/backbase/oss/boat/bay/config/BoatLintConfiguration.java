package com.backbase.oss.boat.bay.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.impl.ConfigImpl;
import com.typesafe.config.impl.Parseable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.zally.core.RulesManager;

@Configuration
public class BoatLintConfiguration {

    @Value("${boat.lint.ruleset.file:boat-bay-default.conf}")
    private String ruleSetFile;

    @Bean
    public Config config() {
        Config config = defaultReference(this.getClass().getClassLoader(), ruleSetFile)
            .withFallback(ConfigFactory.defaultReference());
        return config;
    }

    @Bean
    public RulesManager rulesManager(Config config) {
        return RulesManager.Companion.fromClassLoader(config);
    }


    public static Config defaultReference(final ClassLoader loader, String file) {
        return ConfigImpl.computeCachedConfig(loader, "boatReference", () -> {
            Config unresolvedResources = Parseable
                .newResources(file,
                    ConfigParseOptions.defaults().setClassLoader(loader))
                .parse().toConfig();
            return ConfigImpl.systemPropertiesAsConfig()
                .withFallback(unresolvedResources)
                .resolve();
        });
    }

}
