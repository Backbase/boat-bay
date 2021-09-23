package com.backbase.oss.boat.bay.util;

import static org.openapitools.openapidiff.core.utils.Copy.copyMap;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.openapitools.openapidiff.core.compare.MapKeyDiff;
import org.openapitools.openapidiff.core.model.ChangedOpenApi;

public class TagsDiff {

    private TagsDiff() {}

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

        //Loop through all Old Urls
        oldPaths
            .keySet()
            .forEach(
                oldPath -> {
                    Optional<Map.Entry<String, PathItem>> result = findMatchingPathInNewSpecPaths(oldPath, newPaths);

                    if (result.isPresent()) {
                        // Extract all http operations from both urls and process common
                        // NOTE - Goal is to find only missing tags from old specs. Other difference are already calculated
                        // and in ChangedOpenApi object which will be rendered separately.
                        String newPath = result.get().getKey();
                        Map<PathItem.HttpMethod, Operation> oldOperationMap = oldPaths.get(oldPath).readOperationsMap();
                        Map<PathItem.HttpMethod, Operation> newOperationMap = newPaths.get(newPath).readOperationsMap();
                        List<PathItem.HttpMethod> sharedHttpMethods = MapKeyDiff.diff(oldOperationMap, newOperationMap).getSharedKey();

                        for (PathItem.HttpMethod method : sharedHttpMethods) {
                            Optional<List<String>> missingOldTags = compareOldAndNewTags(
                                oldOperationMap.get(method).getTags(),
                                newOperationMap.get(method).getTags()
                            );
                            if (missingOldTags.isPresent()) {
                                missingTags.put(oldPath, Map.of(method, missingOldTags.get()));
                            }
                        }
                    }
                }
            );
        return missingTags;
    }

    private static Optional<List<String>> compareOldAndNewTags(List<String> oldTags, List<String> newTags) {
        if (oldTags != null) {
            if (newTags != null) {
                //Remove all common tags
                oldTags.removeAll(newTags);
            }
            //Ideally this should be empty. If not, tags have been changed/removed from new specs
            if (!oldTags.isEmpty()) {
                return Optional.of(oldTags);
            }
        }
        return Optional.empty();
    }

    private static Optional<Map.Entry<String, PathItem>> findMatchingPathInNewSpecPaths(String oldPath, Map<String, PathItem> newPaths) {
        // Normalizing because if spec 1 has /bar/{mypath}/foo and spec 2 has /bar/{mynewpath}/foo, both are same.
        String normalizeUrl = normalizePath(oldPath);

        //Find the equivalent url in the newPaths
        return newPaths.entrySet().stream().filter(item -> normalizePath(item.getKey()).equals(normalizeUrl)).findAny();
    }
}
