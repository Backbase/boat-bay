package com.backbase.oss.boat.bay.util;

import static org.assertj.core.api.Assertions.assertThat;

import io.swagger.v3.oas.models.PathItem;
import java.io.File;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.openapitools.openapidiff.core.OpenApiCompare;
import org.openapitools.openapidiff.core.model.ChangedOpenApi;

@Slf4j
class TagsDiffTest {

    @Test
    public void test_when_no_changes_to_tag_were_made() {
        TagsDiff tagsDiff = new TagsDiff();

        ChangedOpenApi diff = OpenApiCompare.fromLocations(
            getFile("/compare-tags/no-change/pet-store-client-api-v1.yaml"),
            getFile("/compare-tags/no-change/pet-store-client-api-v1_1.yaml")
        );

        Map<String, Map<PathItem.HttpMethod, List<String>>> result = tagsDiff.findMissingTags(diff);
        log.info("test_when_no_changes_to_tag_were_made result {}", result.toString());
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void test_when_one_tag_was_removed() {
        TagsDiff tagsDiff = new TagsDiff();

        ChangedOpenApi diff = OpenApiCompare.fromLocations(
            getFile("/compare-tags/one-removed/pet-store-client-api-v1.yaml"),
            getFile("/compare-tags/one-removed/pet-store-client-api-v1_1.yaml")
        );

        Map<String, Map<PathItem.HttpMethod, List<String>>> result = tagsDiff.findMissingTags(diff);
        log.info("test_when_one_tag_was_removed result {}", result.toString());

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get("/pet/{petId}")).isNotEmpty();
        assertThat(result.get("/pet/{petId}").get(PathItem.HttpMethod.GET).get(0)).isEqualTo("tag-three");
    }

    @Test
    public void test_when_one_tag_was_removed_and_one_changed() {
        TagsDiff tagsDiff = new TagsDiff();

        ChangedOpenApi diff = OpenApiCompare.fromLocations(
            getFile("/compare-tags/one-removed-one-changed/pet-store-client-api-v1.yaml"),
            getFile("/compare-tags/one-removed-one-changed/pet-store-client-api-v1_1.yaml")
        );

        Map<String, Map<PathItem.HttpMethod, List<String>>> result = tagsDiff.findMissingTags(diff);
        log.info("test_when_one_tag_was_removed_and_one_changed result {}", result.toString());

        assertThat(result.size()).isEqualTo(2);

        assertThat(result.get("/pet")).isNotEmpty();
        assertThat(result.get("/pet").get(PathItem.HttpMethod.POST).get(0)).isEqualTo("tag-two");

        assertThat(result.get("/pet/{petId}")).isNotEmpty();
        assertThat(result.get("/pet/{petId}").get(PathItem.HttpMethod.GET).get(0)).isEqualTo("tag-three");
    }

    private String getFile(String glob) {
        return (new File("src/test/resources").getAbsolutePath() + glob);
    }
}
