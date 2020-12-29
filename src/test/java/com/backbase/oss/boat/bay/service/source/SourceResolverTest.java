package com.backbase.oss.boat.bay.service.source;

import com.backbase.oss.boat.bay.domain.Spec;
import org.junit.jupiter.api.Test;

public class SourceResolverTest {

    @Test
    void testSpEL() {

        Spec spec = new Spec();
        spec.setName("test");
        spec.setSourcePath("/accessgroup-integration-service/accessgroup-integration-inbound-api-v2.0.0.yaml");
        spec.setSourceName("accessgroup-integration-inbound-api-v2.0.0.yaml");

        System.out.println(SpecSourceResolver.parseName("sourceName.substring(0, sourceName.lastIndexOf('-'))", spec, null));
        System.out.println(SpecSourceResolver.parseName("sourcePath.substring(1, sourcePath.indexOf('/',1))", spec,null));
    }

}
