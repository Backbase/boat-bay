package com.backbase.oss.boat.bay.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.backbase.oss.boat.bay.web.views.dashboard.diff.DiffReportRenderer;
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
    void test_when_no_changes_to_tag_were_made() {
        ChangedOpenApi diff = OpenApiCompare.fromLocations(
            getFile("/compare-tags/no-change/pet-store-client-api-v1.yaml"),
            getFile("/compare-tags/no-change/pet-store-client-api-v1_1.yaml")
        );

        Map<String, Map<PathItem.HttpMethod, List<String>>> result = TagsDiff.findMissingTags(diff);
        log.info("test_when_no_changes_to_tag_were_made result {}", result.toString());
        assertThat(result).isEmpty();
    }

    @Test
    void test_when_one_tag_was_removed() {
        ChangedOpenApi diff = OpenApiCompare.fromLocations(
            getFile("/compare-tags/one-removed/pet-store-client-api-v1.yaml"),
            getFile("/compare-tags/one-removed/pet-store-client-api-v1_1.yaml")
        );

        Map<String, Map<PathItem.HttpMethod, List<String>>> result = TagsDiff.findMissingTags(diff);
        log.info("test_when_one_tag_was_removed result {}", result.toString());

        assertThat(result).hasSize(1);
        assertThat(result.get("/pet/{petId}")).isNotEmpty();
        assertThat(result.get("/pet/{petId}").get(PathItem.HttpMethod.GET).get(0)).isEqualTo("tag-three");
    }

    @Test
    void test_when_one_tag_was_removed_and_one_changed() {
        ChangedOpenApi diff = OpenApiCompare.fromLocations(
            getFile("/compare-tags/one-removed-one-changed/pet-store-client-api-v1.yaml"),
            getFile("/compare-tags/one-removed-one-changed/pet-store-client-api-v1_1.yaml")
        );

        Map<String, Map<PathItem.HttpMethod, List<String>>> result = TagsDiff.findMissingTags(diff);
        log.info("test_when_one_tag_was_removed_and_one_changed result {}", result.toString());

        assertThat(result).hasSize(2);

        assertThat(result.get("/pet")).isNotEmpty();
        assertThat(result.get("/pet").get(PathItem.HttpMethod.POST).get(0)).isEqualTo("tag-two");

        assertThat(result.get("/pet/{petId}")).isNotEmpty();
        assertThat(result.get("/pet/{petId}").get(PathItem.HttpMethod.GET).get(0)).isEqualTo("tag-three");
    }

    @Test
    void test_when_all_tags_were_removed() {
        ChangedOpenApi diff = OpenApiCompare.fromLocations(
            getFile("/compare-tags/all-tags-removed/pet-store-client-api-v1.yaml"),
            getFile("/compare-tags/all-tags-removed/pet-store-client-api-v1_1.yaml")
        );

        Map<String, Map<PathItem.HttpMethod, List<String>>> result = TagsDiff.findMissingTags(diff);
        log.info("test_when_all_tags_were_removed result {}", result.toString());

        assertThat(result).hasSize(1);

        assertThat(result.get("/pet/{petId}")).isNotEmpty();
        assertThat(result.get("/pet/{petId}").get(PathItem.HttpMethod.GET).get(0)).isEqualTo("tag-two");
        assertThat(result.get("/pet/{petId}").get(PathItem.HttpMethod.GET).get(1)).isEqualTo("tag-three");
    }

    @Test
    void renderHtmlTest() {
        DiffReportRenderer render = new DiffReportRenderer();

        ChangedOpenApi diff = OpenApiCompare.fromLocations(
            getFile("/compare-tags/one-removed-one-changed/pet-store-client-api-v1.yaml"),
            getFile("/compare-tags/one-removed-one-changed/pet-store-client-api-v1_1.yaml")
        );

        Map<String, Map<PathItem.HttpMethod, List<String>>> changedTags = TagsDiff.findMissingTags(diff);

        assertThat(render.render(diff, changedTags)).isNotBlank();
    }

    private String getFile(String glob) {
        return (new File("src/test/resources").getAbsolutePath() + glob);
    }
}
