package com.contentstack.sdk;

import java.io.IOException;

@FunctionalInterface
public interface CustomBackoffStrategy {
    /**
     * Calculates delay before next retry.
     *
     * @param attempt current attempt number (0-based)
     * @param statusCode HTTP status code (or -1 for network errors)
     * @param exception the exception that occurred (may be null)
     * @return delay in milliseconds before next retry
     */
    long calculateDelay(int attempt, int statusCode, IOException exception);
}
