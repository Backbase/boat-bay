package com.backbase.oss.boat.bay.service.source;

import com.backbase.oss.boat.bay.domain.Spec;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@Slf4j
class SourceResolverTest {

    @ParameterizedTest
    @CsvSource({
        "/accessgroup-integration-service/accessgroup-integration-inbound-api-v2.0.0.yaml, accessgroup-integration-inbound-api-v2.0.0.yaml",
        "/account-statement/account-statement-client-api-v2.0.0.yaml, account-statement-client-api-v2.0.0.yaml"
    })
    void testSpEL(String path, String name) {

        Spec spec = new Spec();
        spec.setName("test");
        spec.setSourcePath(path);
        spec.setSourceName(name);

        log.info("path: {}, name: {}", path, name);
        String[] expressions = {
            "sourceName.substring(0, sourceName.lastIndexOf('-'))",
            "sourcePath.substring(1, sourcePath.indexOf('/',1))",
            "sourceName.substring(0, sourceName.lastIndexOf('-')).replaceAll('-([a-z]*?)-api', '')",
            "sourceName.replaceAll('integration-api','').replaceAll('client-api','').replaceAll('integration-api','').replaceAll('service-api','').replaceAll('api', '')"
        };

        for (int i = 0; i < expressions.length; i++) {
            log.info("Output  {}", SpecSourceResolver.parseName(expressions[i], spec, null));
        }

    }

}
