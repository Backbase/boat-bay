package com.backbase.oss.boat.bay.config;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class BoatCacheManager {

    private final CacheManager cacheManager;

    public static final String PORTAL = "portal";
    public static final String PORTAL_PRODUCT = "portal.product";
    public static final String PRODUCT_RELEASES = "product.releases";
    public static final String PRODUCT_TAGS = "product.tags";
    public static final String PRODUCT_SPECS = "product.specs";
    public static final String PRODUCT_SERVICES = "product.services";
    public static final String PRODUCT_CAPABILITIES = "product.capabilities";
    public static final String SPEC_LINT_REPORT = "spec.lintreport";
    public static final String STATISTICS = "statistics";

    public void clearCache() {
        for (String s : Arrays.asList(PORTAL, PORTAL_PRODUCT, PRODUCT_RELEASES, PRODUCT_SPECS, PRODUCT_SERVICES, PRODUCT_CAPABILITIES, STATISTICS)) {
            Cache cache = cacheManager.getCache(s);
            if (cache != null) {
                cache.clear();
            }
        }
    }
}
