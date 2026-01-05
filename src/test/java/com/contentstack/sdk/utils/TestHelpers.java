package com.contentstack.sdk.utils;

import com.contentstack.sdk.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Common test helper utilities for integration tests.
 */
public class TestHelpers {
    
    private static final Logger logger = Logger.getLogger(TestHelpers.class.getName());
    private static final int DEFAULT_TIMEOUT_SECONDS = 10;
    
    /**
     * Wait for a CountDownLatch with default timeout
     * 
     * @param latch The CountDownLatch to wait for
     * @param testName Name of the test (for logging)
     * @return true if latch counted down before timeout
     * @throws InterruptedException if interrupted while waiting
     */
    public static boolean awaitLatch(CountDownLatch latch, String testName) throws InterruptedException {
        boolean completed = latch.await(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        if (!completed) {
            logger.warning(testName + " timed out after " + DEFAULT_TIMEOUT_SECONDS + " seconds");
        }
        return completed;
    }
    
    /**
     * Wait for a CountDownLatch with custom timeout
     * 
     * @param latch The CountDownLatch to wait for
     * @param timeoutSeconds Timeout in seconds
     * @param testName Name of the test (for logging)
     * @return true if latch counted down before timeout
     * @throws InterruptedException if interrupted while waiting
     */
    public static boolean awaitLatch(CountDownLatch latch, int timeoutSeconds, String testName) 
            throws InterruptedException {
        boolean completed = latch.await(timeoutSeconds, TimeUnit.SECONDS);
        if (!completed) {
            logger.warning(testName + " timed out after " + timeoutSeconds + " seconds");
        }
        return completed;
    }
    
    /**
     * Log successful test result
     * 
     * @param testName Name of the test
     */
    public static void logSuccess(String testName) {
        logger.info("✅ " + testName + " - PASSED");
    }
    
    /**
     * Log successful test result with additional info
     * 
     * @param testName Name of the test
     * @param message Additional message
     */
    public static void logSuccess(String testName, String message) {
        logger.info("✅ " + testName + " - PASSED: " + message);
    }
    
    /**
     * Log test failure
     * 
     * @param testName Name of the test
     * @param error The error that occurred
     */
    public static void logFailure(String testName, com.contentstack.sdk.Error error) {
        if (error != null) {
            logger.severe("❌ " + testName + " - FAILED: " + error.getErrorMessage());
        } else {
            logger.severe("❌ " + testName + " - FAILED: Unknown error");
        }
    }
    
    /**
     * Log test warning
     * 
     * @param testName Name of the test
     * @param message Warning message
     */
    public static void logWarning(String testName, String message) {
        logger.warning("⚠️  " + testName + " - WARNING: " + message);
    }
    
    /**
     * Validate that entry has required basic fields
     * 
     * @param entry Entry to validate
     * @return true if entry has uid, title, and locale
     */
    public static boolean hasBasicFields(Entry entry) {
        return entry != null 
                && entry.getUid() != null 
                && !entry.getUid().isEmpty()
                && entry.getLocale() != null 
                && !entry.getLocale().isEmpty();
    }
    
    /**
     * Validate that query result is not empty
     * 
     * @param result QueryResult to validate
     * @return true if result has entries
     */
    public static boolean hasResults(QueryResult result) {
        return result != null 
                && result.getResultObjects() != null 
                && !result.getResultObjects().isEmpty();
    }
    
    /**
     * Safely get header value as String
     * 
     * @param entry Entry to get header from
     * @param headerName Name of the header
     * @return Header value as String, or null if not present
     */
    public static String getHeaderAsString(Entry entry, String headerName) {
        if (entry == null || entry.getHeaders() == null) {
            return null;
        }
        Object headerValue = entry.getHeaders().get(headerName);
        return headerValue != null ? String.valueOf(headerValue) : null;
    }
    
    /**
     * Check if test data is configured for complex testing
     * 
     * @return true if complex entry configuration is available
     */
    public static boolean isComplexTestDataAvailable() {
        return Credentials.hasComplexEntry() 
                && Credentials.COMPLEX_CONTENT_TYPE_UID != null
                && !Credentials.COMPLEX_CONTENT_TYPE_UID.isEmpty();
    }
    
    /**
     * Check if taxonomy testing is possible
     * 
     * @return true if taxonomy terms are configured
     */
    public static boolean isTaxonomyTestingAvailable() {
        return Credentials.hasTaxonomySupport();
    }
    
    /**
     * Check if variant testing is possible
     * 
     * @return true if variant UID is configured
     */
    public static boolean isVariantTestingAvailable() {
        return Credentials.hasVariantSupport();
    }
    
    /**
     * Get a user-friendly summary of available test data
     * 
     * @return Summary string
     */
    public static String getTestDataSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("\n=== Test Data Summary ===\n");
        summary.append("Complex Entry: ").append(isComplexTestDataAvailable() ? "✅" : "❌").append("\n");
        summary.append("Taxonomy: ").append(isTaxonomyTestingAvailable() ? "✅" : "❌").append("\n");
        summary.append("Variant: ").append(isVariantTestingAvailable() ? "✅" : "❌").append("\n");
        summary.append("Global Fields: ").append(Credentials.hasGlobalFieldsConfigured() ? "✅" : "❌").append("\n");
        summary.append("Locale Fallback: ").append(Credentials.hasLocaleFallback() ? "✅" : "❌").append("\n");
        summary.append("========================\n");
        return summary.toString();
    }
    
    /**
     * Format duration in milliseconds to human-readable string
     * 
     * @param durationMs Duration in milliseconds
     * @return Formatted string (e.g., "1.23s" or "456ms")
     */
    public static String formatDuration(long durationMs) {
        if (durationMs >= 1000) {
            return String.format("%.2fs", durationMs / 1000.0);
        } else {
            return durationMs + "ms";
        }
    }
    
    /**
     * Measure and log execution time
     * 
     * @param testName Name of the test
     * @param startTime Start time in milliseconds (from System.currentTimeMillis())
     */
    public static void logExecutionTime(String testName, long startTime) {
        long duration = System.currentTimeMillis() - startTime;
        logger.info(testName + " completed in " + formatDuration(duration));
    }
}

