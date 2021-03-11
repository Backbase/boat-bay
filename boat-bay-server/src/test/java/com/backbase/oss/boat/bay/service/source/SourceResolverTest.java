package com.backbase.oss.boat.bay.service.source;

import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.service.model.UploadRequestBody;
import com.backbase.oss.boat.bay.util.SpringExpressionUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@Slf4j
class SourceResolverTest {
// test expressions

    @ParameterizedTest
    @CsvSource({"com.backbase.oss.boat.example, lint-petstore-breaking ",
    "com.backbase.oss.boat.example,validate-petstore "})
    void testMavenSourceTypeSpel(String groupId, String artifactId){

        UploadRequestBody request = new UploadRequestBody().artifactId(artifactId).projectId(groupId);
        String g = groupId.substring(groupId.indexOf('.')+1);

        String[] expressions = {
                "artifactId",
                "artifactId",
                "projectId.substring(projectId.indexOf('.')+1).replace('.','-') ",
                "projectId.substring(projectId.indexOf('.')+1).replace('.','-') "
        };

        for (int i = 0; i < expressions.length; i++) {
            log.info("Output  {}", SpringExpressionUtils.parseName(expressions[i], request, null));
        }

    }

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
            "sourceName.substring(0, sourceName.lastIndexOf('-')).replaceAll('-([a-z]*?)-api', '')",
            "sourceName.substring(0, sourceName.lastIndexOf('-'))",
            "sourcePath.substring(1, sourcePath.indexOf('/',1))",
            "sourceName.substring(0, sourceName.lastIndexOf('-')).replaceAll('-([a-z]*?)-api', '')",
            "sourceName.replaceAll('integration-api','').replaceAll('client-api','').replaceAll('integration-api','').replaceAll('service-api','').replaceAll('api', '')",
            "sourceName.substring(0, sourceName.lastIndexOf('-')).replaceAll('-([a-z]*?)-api', '')"
        };

        for (int i = 0; i < expressions.length; i++) {
            log.info("Output  {}", SpringExpressionUtils.parseName(expressions[i], spec, null));
        }

    }

}
