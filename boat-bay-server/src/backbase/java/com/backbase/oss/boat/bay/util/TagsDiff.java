package com.backbase.oss.boat.bay.util;

import static org.openapitools.openapidiff.core.utils.Copy.copyMap;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import java.util.*;
import org.openapitools.openapidiff.core.compare.MapKeyDiff;
import org.openapitools.openapidiff.core.model.ChangedOpenApi;

public class TagsDiff {

    private static final String REGEX_PATH = "\\{([^/]+)\\}";

    private static String normalizePath(String path) {
        return path.replaceAll(REGEX_PATH, "{}");
    }

    public static Paths valOrEmpty(Paths path) {
        if (path == null) {
            path = new Paths();
        }
        return path;
    }

    public static Map<String, Map<PathItem.HttpMethod, List<String>>> findMissingTags(ChangedOpenApi changedOpenApi) {
        //Copy elements to maintain immutability
        Map<String, PathItem> oldPaths = copyMap(valOrEmpty(changedOpenApi.getOldSpecOpenApi().getPaths()));
        Map<String, PathItem> newPaths = copyMap(valOrEmpty(changedOpenApi.getNewSpecOpenApi().getPaths()));

        Map<String, Map<PathItem.HttpMethod, List<String>>> missingTags = new HashMap<>();

        //Loop through all Urls
        oldPaths
            .keySet()
            .forEach(
                (String url) -> {
                    PathItem oldPathWithAllOperations = oldPaths.get(url);

                    // Normalizing because if spec 1 is /bar/{mypath}/foo and spec 2 is /bar/{mynewpath}/foo, both are same.
                    String normalizeUrl = normalizePath(url);

                    //Find the equivalent url in the newPaths
                    Optional<Map.Entry<String, PathItem>> result = newPaths
                        .entrySet()
                        .stream()
                        .filter(item -> normalizePath(item.getKey()).equals(normalizeUrl))
                        .findAny();

                    if (result.isPresent()) {
                        String rightUrl = result.get().getKey();
                        PathItem newPathWithAllOperations = newPaths.get(rightUrl);

                        // Extract all operations from both urls and process common
                        // NOTE - Goal is to find only missing tags from old specs. Other difference are already calculated
                        Map<PathItem.HttpMethod, Operation> oldOperationMap = oldPathWithAllOperations.readOperationsMap();
                        Map<PathItem.HttpMethod, Operation> newOperationMap = newPathWithAllOperations.readOperationsMap();
                        MapKeyDiff<PathItem.HttpMethod, Operation> operationsDiff = MapKeyDiff.diff(oldOperationMap, newOperationMap);

                        List<PathItem.HttpMethod> sharedMethods = operationsDiff.getSharedKey();

                        for (PathItem.HttpMethod method : sharedMethods) {
                            List<String> oldTags = oldOperationMap.get(method).getTags();
                            List<String> newTags = newOperationMap.get(method).getTags();
                            if (oldTags != null && newTags != null) {
                                //Remove all common tags
                                oldTags.removeAll(newTags);

                                //Tags changed in new Spec
                                if (!oldTags.isEmpty()) {
                                    missingTags.put(url, Map.of(method, oldTags));
                                }
                            }
                        }
                    }
                }
            );
        return missingTags;
    }
}
