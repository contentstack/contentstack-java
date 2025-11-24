package com.contentstack.sdk.utils;

import java.util.logging.Logger;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Performance assertion utilities for integration tests.
 */
public class PerformanceAssertion {
    
    private static final Logger logger = Logger.getLogger(PerformanceAssertion.class.getName());
    
    // Performance thresholds (milliseconds)
    public static final long FAST_OPERATION_MS = 2000;      // < 2s (increased for API calls)
    public static final long NORMAL_OPERATION_MS = 3000;    // < 3s
    public static final long SLOW_OPERATION_MS = 5000;      // < 5s
    public static final long LARGE_DATASET_MS = 10000;      // < 10s
    
    /**
     * Assert that operation completed within specified time
     * 
     * @param actualMs Actual duration in milliseconds
     * @param maxMs Maximum allowed duration in milliseconds
     * @param operationName Name of operation (for error message)
     * @throws AssertionError if actualMs > maxMs
     */
    public static void assertResponseTime(long actualMs, long maxMs, String operationName) {
        assertTrue(actualMs <= maxMs, 
                String.format("%s took %dms, expected <= %dms (%.1fx slower)", 
                        operationName, actualMs, maxMs, (double)actualMs / maxMs));
    }
    
    /**
     * Assert that operation completed within specified time
     * Overload without operation name
     * 
     * @param actualMs Actual duration in milliseconds
     * @param maxMs Maximum allowed duration in milliseconds
     * @throws AssertionError if actualMs > maxMs
     */
    public static void assertResponseTime(long actualMs, long maxMs) {
        assertResponseTime(actualMs, maxMs, "Operation");
    }
    
    /**
     * Assert fast operation (< 1 second)
     * 
     * @param actualMs Actual duration in milliseconds
     * @param operationName Name of operation
     */
    public static void assertFastOperation(long actualMs, String operationName) {
        assertResponseTime(actualMs, FAST_OPERATION_MS, operationName);
    }
    
    /**
     * Assert normal operation (< 3 seconds)
     * 
     * @param actualMs Actual duration in milliseconds
     * @param operationName Name of operation
     */
    public static void assertNormalOperation(long actualMs, String operationName) {
        assertResponseTime(actualMs, NORMAL_OPERATION_MS, operationName);
    }
    
    /**
     * Assert slow operation (< 5 seconds)
     * 
     * @param actualMs Actual duration in milliseconds
     * @param operationName Name of operation
     */
    public static void assertSlowOperation(long actualMs, String operationName) {
        assertResponseTime(actualMs, SLOW_OPERATION_MS, operationName);
    }
    
    /**
     * Assert large dataset operation (< 10 seconds)
     * 
     * @param actualMs Actual duration in milliseconds
     * @param operationName Name of operation
     */
    public static void assertLargeDatasetOperation(long actualMs, String operationName) {
        assertResponseTime(actualMs, LARGE_DATASET_MS, operationName);
    }
    
    /**
     * Assert memory usage is below threshold
     * 
     * @param currentBytes Current memory usage in bytes
     * @param maxBytes Maximum allowed memory usage in bytes
     * @param operationName Name of operation
     * @throws AssertionError if currentBytes > maxBytes
     */
    public static void assertMemoryUsage(long currentBytes, long maxBytes, String operationName) {
        assertTrue(currentBytes <= maxBytes,
                String.format("%s used %s, expected <= %s", 
                        operationName, 
                        formatBytes(currentBytes), 
                        formatBytes(maxBytes)));
    }
    
    /**
     * Assert memory usage is below threshold
     * Overload without operation name
     * 
     * @param currentBytes Current memory usage in bytes
     * @param maxBytes Maximum allowed memory usage in bytes
     */
    public static void assertMemoryUsage(long currentBytes, long maxBytes) {
        assertMemoryUsage(currentBytes, maxBytes, "Operation");
    }
    
    /**
     * Get current memory usage
     * 
     * @return Current memory usage in bytes
     */
    public static long getCurrentMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
    
    /**
     * Get available memory
     * 
     * @return Available memory in bytes
     */
    public static long getAvailableMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.maxMemory() - (runtime.totalMemory() - runtime.freeMemory());
    }
    
    /**
     * Log performance metrics for an operation
     * 
     * @param operationName Name of operation
     * @param durationMs Duration in milliseconds
     */
    public static void logPerformanceMetrics(String operationName, long durationMs) {
        String performanceLevel = getPerformanceLevel(durationMs);
        logger.info(String.format("⏱️  %s: %s [%s]", 
                operationName, 
                formatDuration(durationMs), 
                performanceLevel));
    }
    
    /**
     * Log detailed performance metrics including memory
     * 
     * @param operationName Name of operation
     * @param durationMs Duration in milliseconds
     * @param memoryBytes Memory used in bytes
     */
    public static void logPerformanceMetrics(String operationName, long durationMs, long memoryBytes) {
        String performanceLevel = getPerformanceLevel(durationMs);
        logger.info(String.format("⏱️  %s: %s, Memory: %s [%s]", 
                operationName, 
                formatDuration(durationMs),
                formatBytes(memoryBytes),
                performanceLevel));
    }
    
    /**
     * Log performance summary for multiple operations
     * 
     * @param operations Array of operation names
     * @param durations Array of durations in milliseconds
     */
    public static void logPerformanceSummary(String[] operations, long[] durations) {
        if (operations.length != durations.length) {
            throw new IllegalArgumentException("Operations and durations arrays must be same length");
        }
        
        logger.info("=== Performance Summary ===");
        long totalDuration = 0;
        for (int i = 0; i < operations.length; i++) {
            logPerformanceMetrics(operations[i], durations[i]);
            totalDuration += durations[i];
        }
        logger.info(String.format("Total: %s", formatDuration(totalDuration)));
        logger.info("=========================");
    }
    
    /**
     * Compare two operation durations
     * 
     * @param operation1Name First operation name
     * @param duration1Ms First operation duration
     * @param operation2Name Second operation name
     * @param duration2Ms Second operation duration
     * @return Comparison summary string
     */
    public static String compareOperations(String operation1Name, long duration1Ms, 
                                          String operation2Name, long duration2Ms) {
        double ratio = (double) duration1Ms / duration2Ms;
        String faster = duration1Ms < duration2Ms ? operation1Name : operation2Name;
        String slower = duration1Ms < duration2Ms ? operation2Name : operation1Name;
        double improvement = Math.abs(ratio - 1.0) * 100;
        
        return String.format("%s is %.1f%% faster than %s", faster, improvement, slower);
    }
    
    /**
     * Format duration in milliseconds to human-readable string
     * 
     * @param durationMs Duration in milliseconds
     * @return Formatted string (e.g., "1.23s" or "456ms")
     */
    private static String formatDuration(long durationMs) {
        if (durationMs >= 1000) {
            return String.format("%.2fs", durationMs / 1000.0);
        } else {
            return durationMs + "ms";
        }
    }
    
    /**
     * Format bytes to human-readable string
     * 
     * @param bytes Number of bytes
     * @return Formatted string (e.g., "1.5 MB", "512 KB")
     */
    private static String formatBytes(long bytes) {
        if (bytes >= 1024 * 1024 * 1024) {
            return String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0));
        } else if (bytes >= 1024 * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
        } else if (bytes >= 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else {
            return bytes + " bytes";
        }
    }
    
    /**
     * Get performance level based on duration
     * 
     * @param durationMs Duration in milliseconds
     * @return Performance level string
     */
    private static String getPerformanceLevel(long durationMs) {
        if (durationMs < FAST_OPERATION_MS) {
            return "⚡ FAST";
        } else if (durationMs < NORMAL_OPERATION_MS) {
            return "✅ NORMAL";
        } else if (durationMs < SLOW_OPERATION_MS) {
            return "⚠️  SLOW";
        } else if (durationMs < LARGE_DATASET_MS) {
            return "🐢 VERY SLOW";
        } else {
            return "❌ TOO SLOW";
        }
    }
    
    /**
     * Start a performance timer
     * 
     * @return Current timestamp in milliseconds
     */
    public static long startTimer() {
        return System.currentTimeMillis();
    }
    
    /**
     * Calculate elapsed time since timer start
     * 
     * @param startTime Start timestamp from startTimer()
     * @return Elapsed time in milliseconds
     */
    public static long elapsedTime(long startTime) {
        return System.currentTimeMillis() - startTime;
    }
    
    /**
     * Measure and log operation performance
     * Helper method that combines timing and logging
     * 
     * @param operationName Name of operation
     * @param startTime Start timestamp
     * @return Duration in milliseconds
     */
    public static long measureAndLog(String operationName, long startTime) {
        long duration = elapsedTime(startTime);
        logPerformanceMetrics(operationName, duration);
        return duration;
    }
}

