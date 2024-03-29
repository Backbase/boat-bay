package com.backbase.oss.boat.bay.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

import static com.backbase.oss.boat.bay.config.BoatCacheManager.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.backbase.oss.boat.bay.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.backbase.oss.boat.bay.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.backbase.oss.boat.bay.domain.User.class.getName());
            createCache(cm, com.backbase.oss.boat.bay.domain.Authority.class.getName());
            createCache(cm, com.backbase.oss.boat.bay.domain.User.class.getName() + ".authorities");
            createCache(cm, com.backbase.oss.boat.bay.domain.Dashboard.class.getName());
            createCache(cm, com.backbase.oss.boat.bay.domain.Portal.class.getName());
            createCache(cm, com.backbase.oss.boat.bay.domain.Portal.class.getName() + ".products");
            createCache(cm, com.backbase.oss.boat.bay.domain.Portal.class.getName() + ".lintRules");
            createCache(cm, com.backbase.oss.boat.bay.domain.ProductRelease.class.getName());
            createCache(cm, com.backbase.oss.boat.bay.domain.ProductRelease.class.getName() + ".specs");
            createCache(cm, com.backbase.oss.boat.bay.domain.Product.class.getName());
            createCache(cm, com.backbase.oss.boat.bay.domain.Product.class.getName() + ".productReleases");
            createCache(cm, com.backbase.oss.boat.bay.domain.Product.class.getName() + ".capabilities");
            createCache(cm, com.backbase.oss.boat.bay.domain.Capability.class.getName());
            createCache(cm, com.backbase.oss.boat.bay.domain.Capability.class.getName() + ".serviceDefinitions");
            createCache(cm, com.backbase.oss.boat.bay.domain.ServiceDefinition.class.getName());
            createCache(cm, com.backbase.oss.boat.bay.domain.ServiceDefinition.class.getName() + ".specs");
            createCache(cm, com.backbase.oss.boat.bay.domain.Spec.class.getName());
            createCache(cm, com.backbase.oss.boat.bay.domain.Spec.class.getName() + ".tags");
            createCache(cm, com.backbase.oss.boat.bay.domain.Spec.class.getName() + ".productReleases");
            createCache(cm, com.backbase.oss.boat.bay.domain.Tag.class.getName());
            createCache(cm, com.backbase.oss.boat.bay.domain.Tag.class.getName() + ".specs");
            createCache(cm, com.backbase.oss.boat.bay.domain.SpecType.class.getName());
            createCache(cm, com.backbase.oss.boat.bay.domain.Source.class.getName());
            createCache(cm, com.backbase.oss.boat.bay.domain.Source.class.getName() + ".sourcePaths");
            createCache(cm, com.backbase.oss.boat.bay.domain.SourcePath.class.getName());
            createCache(cm, com.backbase.oss.boat.bay.domain.LintRule.class.getName());
            createCache(cm, com.backbase.oss.boat.bay.domain.LintReport.class.getName());
            createCache(cm, com.backbase.oss.boat.bay.domain.LintReport.class.getName() + ".violations");
            createCache(cm, com.backbase.oss.boat.bay.domain.LintRuleViolation.class.getName());
            createCache(cm, com.backbase.oss.boat.bay.domain.ZallyConfig.class.getName());
            // jhipster-needle-ehcache-add-entry

            createCache(cm, PORTAL);
            createCache(cm, PORTAL_PRODUCT);
            createCache(cm, PRODUCT_RELEASES);
            createCache(cm, PRODUCT_TAGS);
            createCache(cm, PRODUCT_SPECS);
            createCache(cm, PRODUCT_SERVICES);
            createCache(cm, PRODUCT_CAPABILITIES);
            createCache(cm, SPEC_LINT_REPORT);
            createCache(cm, STATISTICS);

        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
