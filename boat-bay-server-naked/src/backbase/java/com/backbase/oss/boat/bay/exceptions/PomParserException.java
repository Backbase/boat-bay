package com.backbase.oss.boat.bay.exceptions;

/**
 * The class {@code PomParserException} is used to wrap all exceptions related with pom parsing issues.
 */
public class PomParserException extends RuntimeException {

    public PomParserException(String message) {
        super(message);
    }
}
