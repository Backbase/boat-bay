package com.backbase.oss.boat.bay.util;

import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.service.model.UploadRequestBody;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.jfrog.artifactory.client.ItemHandle;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

@Slf4j
public class SpringExpressionUtils {

    private static final ExpressionParser parser = new SpelExpressionParser();
    private static final Map<String, Expression> expressionCache = new HashMap<>();

    private static Expression getExpression(String spEL) {
        if (!expressionCache.containsKey(spEL)) {
            log.debug("Creating expression from: {}", spEL);
            Expression value = parser.parseExpression(spEL);
            expressionCache.put(spEL, value);
            return value;
        }
        return expressionCache.get(spEL);
    }

    public static boolean match(String spEL, Spec spec) {
        if (spEL == null) {
            return false;
        }
        Boolean match;
        try {
            match = getExpression(spEL).getValue(spec, Boolean.class);
            return Objects.requireNonNullElse(match, false);
        } catch (EvaluationException | StringIndexOutOfBoundsException e) {
            log.warn("Expression: {} failed on: {}", spEL, spec);
            return false;
        }
    }

    public static String parseName(String spEL, Spec spec, String fallback) {
        if (spEL == null) {
            return fallback;
        }

        String name;
        try {
            name = getExpression(spEL).getValue(spec, String.class);
        } catch (EvaluationException | StringIndexOutOfBoundsException e) {
            log.warn("Expression: {} failed on: {}. Reason: {}", spEL, spec.getName(), e.getMessage());
            return fallback;
        }
        log.debug("Resolved: {}", name);
        return name;
    }

    public static String parseName(String spEL, ItemHandle itemHandle, String fallback) {
        if (spEL == null) {
            return fallback;
        }

        String name;
        try {
            name = getExpression(spEL).getValue(itemHandle, String.class);
        } catch (EvaluationException | StringIndexOutOfBoundsException e) {
            log.warn("Expression: {} failed on: {}. Reason: {}", spEL, itemHandle.info().getName(), e.getMessage());
            return fallback;
        }
        log.debug("Resolved: {}", name);
        return name;
    }

    public static String parseName(String spEL, UploadRequestBody uploadRequestBody, String fallback) {
        if (spEL == null) {
            return fallback;
        }

        String name;
        try {
            name = getExpression(spEL).getValue(uploadRequestBody, String.class);
        } catch (EvaluationException | StringIndexOutOfBoundsException e) {
            log.warn("Expression: {} failed on: {}. Reason: {}", spEL, uploadRequestBody.getArtifactId(), e.getMessage());
            return fallback;
        }
        log.debug("Resolved: {}", name);
        return name;
    }
    public static String parseName(String spEL, UploadRequestBody uploadRequestBody, String fallback) {
        if (spEL == null) {
            return fallback;
        }

        String name;
        try {
            name = getExpression(spEL).getValue(uploadRequestBody, String.class);
        } catch (EvaluationException | StringIndexOutOfBoundsException e) {
            log.warn("Expression: {} failed on: {}. Reason: {}", spEL, uploadRequestBody.getArtifactId(),e.getMessage());
            return fallback;
        }
        log.debug("Resolved: {}", name);
        return name;
    }
}
