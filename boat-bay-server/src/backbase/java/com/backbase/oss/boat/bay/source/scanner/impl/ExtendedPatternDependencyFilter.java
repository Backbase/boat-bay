package com.backbase.oss.boat.bay.source.scanner.impl;

import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyFilter;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.version.InvalidVersionSpecificationException;
import org.eclipse.aether.version.Version;
import org.eclipse.aether.version.VersionRange;
import org.eclipse.aether.version.VersionScheme;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A extended simple filter to include artifacts from a list of patterns. The artifact pattern syntax is of the form:
 *
 * <pre>
 * [groupId]:[artifactId]:[extension]:[classifier]:[version]
 * </pre>
 * <p>
 * Where each pattern segment is optional and supports full and partial <code>*</code> wildcards. An empty pattern
 * segment is treated as an implicit wildcard. Version can be a range in case a {@link VersionScheme} is specified.
 * </p>
 * <p>
 * For example, <code>org.eclipse.*</code> would match all artifacts whose group id started with
 * <code>org.eclipse.</code> , and <code>:::*-SNAPSHOT</code> would match all snapshot artifacts.
 * </p>
 */
public class ExtendedPatternDependencyFilter implements DependencyFilter {

    private final Set<String> patterns = new HashSet<>();

    private final VersionScheme versionScheme;

    public ExtendedPatternDependencyFilter(List<String> patterns) {
        this.versionScheme = null;
        this.patterns.addAll(patterns);
    }


    public ExtendedPatternDependencyFilter(String... patterns) {
        this.versionScheme = null;
        this.patterns.addAll(Arrays.asList(patterns));
    }


    @Override
    public boolean accept(DependencyNode node, List<DependencyNode> parents) {
        final Dependency dependency = node.getDependency();
        if (dependency == null) {
            return true;
        }
        return accept(dependency.getArtifact());
    }

    protected boolean accept(final Artifact artifact) {
        for (final String pattern : patterns) {
            final boolean matched = accept(artifact, pattern);
            if (matched) {
                return true;
            }
        }
        return false;
    }


    private boolean accept(final Artifact artifact, final String pattern) {
        final String[] tokens = new String[]{
            artifact.getGroupId(),
            artifact.getArtifactId(),
            artifact.getExtension(),
            artifact.getClassifier(),
            artifact.getBaseVersion()};

        final String[] patternTokens = pattern.split(":");

        // fail immediately if pattern tokens outnumber tokens to match
        boolean matched = (patternTokens.length <= tokens.length);

        for (int i = 0; matched && i < patternTokens.length; i++) {
            matched = matches(tokens[i], patternTokens[i]);
        }

        return matched;
    }

    private boolean matches(final String token, final String pattern) {
        boolean matches;

        // support full wildcard and implied wildcard
        if ("*".equals(pattern) || pattern.length() == 0) {
            matches = true;
        }
        // support contains wildcard
        else if (pattern.startsWith("*") && pattern.endsWith("*")) {
            final String contains = pattern.substring(1, pattern.length() - 1);

            matches = (token.contains(contains));
        }
        // support leading wildcard
        else if (pattern.startsWith("*")) {
            final String suffix = pattern.substring(1, pattern.length());

            matches = token.endsWith(suffix);
        }
        // support trailing wildcard
        else if (pattern.endsWith("*")) {
            final String prefix = pattern.substring(0, pattern.length() - 1);

            matches = token.startsWith(prefix);
        }
        // support versions range
        else if (pattern.startsWith("[") || pattern.startsWith("(")) {
            matches = isVersionIncludedInRange(token, pattern);
        }
        // support exact match
        else {
            matches = token.equals(pattern);
        }

        return matches;
    }

    private boolean isVersionIncludedInRange(final String version, final String range) {
        if (versionScheme == null) {
            return false;
        } else {
            try {
                final Version parsedVersion = versionScheme.parseVersion(version);
                final VersionRange parsedRange = versionScheme.parseVersionRange(range);

                return parsedRange.containsVersion(parsedVersion);
            } catch (final InvalidVersionSpecificationException e) {
                return false;
            }
        }
    }

}
