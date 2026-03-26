package com.contentstack.sdk;

import java.util.Objects;

/**
 * Configuration options for HTTP request retry mechanism.
 * 
 * <p>This class allows customization of retry behavior for failed HTTP requests.
 * By default, retries are enabled with exponential backoff strategy.
 * 
 * <p><b>Default Configuration:</b>
 * <ul>
 *   <li>Retry limit: 3 attempts</li>
 *   <li>Retry delay: 1000ms (1 second)</li>
 *   <li>Backoff strategy: EXPONENTIAL</li>
 *   <li>Retryable status codes: 408, 429, 502, 503, 504</li>
 *   <li>Retry enabled: true</li>
 * </ul>
 * 
 * <p><b>Example Usage:</b>
 * <pre>{@code
 * // Custom retry configuration
 * RetryOptions options = new RetryOptions()
 *     .setRetryLimit(5)
 *     .setRetryDelay(2000)
 *     .setBackoffStrategy(BackoffStrategy.LINEAR)
 *     .setRetryableStatusCodes(429, 502, 503);
 * 
 * Config config = new Config();
 * config.setRetryOptions(options);
 * 
 * // Disable retries
 * RetryOptions noRetry = new RetryOptions().setRetryEnabled(false);
 * }</pre>
 * 
 * @since 2.0.0
 */
public class RetryOptions {

    /** Default number of retry attempts */
    private static final int DEFAULT_RETRY_LIMIT = 3;
    
    /** Default base delay in milliseconds */
    private static final long DEFAULT_RETRY_DELAY_MS = 1000;
    
    /** Maximum allowed retry attempts to prevent infinite retries */
    private static final int MAX_RETRY_LIMIT = 10;
    
    /** Default retryable HTTP status codes (transient errors) */
    private static final int[] DEFAULT_RETRYABLE_STATUS_CODES = {
        408,  // Request Timeout
        429,  // Too Many Requests (rate limiting)
        502,  // Bad Gateway
        503,  // Service Unavailable
        504   // Gateway Timeout
    };

    /**
     * Maximum number of retry attempts.
     * 0 means no retries (but retry can still be enabled for other reasons).
     */
    private int retryLimit = DEFAULT_RETRY_LIMIT;
    
    /**
     * Base delay in milliseconds between retry attempts.
     * Actual delay depends on backoff strategy.
     */
    private long retryDelayMs = DEFAULT_RETRY_DELAY_MS;
    
    /**
     * Strategy for calculating delay between retries.
     */
    private BackoffStrategy backoffStrategy = BackoffStrategy.EXPONENTIAL;
    
    /**
     * HTTP status codes that should trigger a retry.
     * Only these codes will be retried; others fail immediately.
     */
    private int[] retryableStatusCodes = DEFAULT_RETRYABLE_STATUS_CODES.clone();
    
    /**
     * Master switch to enable/disable retry mechanism entirely.
     * When false, no retries occur regardless of other settings.
     */
    private boolean retryEnabled = true;

    private CustomBackoffStrategy customBackoffStrategy = null;

    /**
     * Defines how delay between retries is calculated.
     */
    public enum BackoffStrategy {
        /**
         * Fixed delay - same wait time for each retry.
         * <p>Example with 1000ms base delay:
         * <ul>
         *   <li>Attempt 1: 1000ms</li>
         *   <li>Attempt 2: 1000ms</li>
         *   <li>Attempt 3: 1000ms</li>
         * </ul>
         */
        FIXED,
        
        /**
         * Linear backoff - delay increases linearly with attempt number.
         * <p>Example with 1000ms base delay:
         * <ul>
         *   <li>Attempt 1: 1000ms (1 × 1000)</li>
         *   <li>Attempt 2: 2000ms (2 × 1000)</li>
         *   <li>Attempt 3: 3000ms (3 × 1000)</li>
         * </ul>
         */
        LINEAR,
        
        /**
         * Exponential backoff - delay doubles with each attempt.
         * <p>Example with 1000ms base delay:
         * <ul>
         *   <li>Attempt 1: 1000ms (2⁰ × 1000)</li>
         *   <li>Attempt 2: 2000ms (2¹ × 1000)</li>
         *   <li>Attempt 3: 4000ms (2² × 1000)</li>
         * </ul>
         * <p><b>Recommended for most use cases</b> as it quickly backs off
         * to prevent overwhelming the server while allowing fast recovery.
         */
        EXPONENTIAL,

        /**
         * Custom backoff strategy - delay is calculated by the user.
         * <p>Example with 1000ms base delay:
         * <ul>
         *   <li>Attempt 1: 1000ms</li>
         *   <li>Attempt 2: 2000ms</li>
         *   <li>Attempt 3: 3000ms</li>
         * </ul>
         */
        CUSTOM,
    }

    /**
     * Creates RetryOptions with default configuration.
     * <p>Defaults: 3 retries, 1000ms delay, exponential backoff,
     * retries on [408, 429, 502, 503, 504], enabled.
     */
    public RetryOptions() { 
    }

    /**
     * Sets the maximum number of retry attempts.
     * 
     * @param limit maximum retry attempts (0-10)
     * @return this RetryOptions instance for method chaining
     * @throws IllegalArgumentException if limit is negative or exceeds maximum
     */
    public RetryOptions setRetryLimit(int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException(
                "Retry limit cannot be negative. Provided: " + limit);
        }
        if (limit > MAX_RETRY_LIMIT) {
            throw new IllegalArgumentException(
                "Retry limit cannot exceed " + MAX_RETRY_LIMIT + ". Provided: " + limit);
        }
        this.retryLimit = limit;
        return this;  
    }
    
    /**
     * Sets the base delay between retry attempts in milliseconds.
     * 
     * @param delayMs base delay in milliseconds (must be non-negative)
     * @return this RetryOptions instance for method chaining
     * @throws IllegalArgumentException if delay is negative
     */
    public RetryOptions setRetryDelay(long delayMs) {
        if (delayMs < 0) {
            throw new IllegalArgumentException(
                "Retry delay cannot be negative. Provided: " + delayMs);
        }
        this.retryDelayMs = delayMs;
            return this;  
    }
    
    /**
     * Sets the backoff strategy for calculating retry delays.
     * 
     * @param strategy backoff strategy (FIXED, LINEAR, or EXPONENTIAL)
     * @return this RetryOptions instance for method chaining
     * @throws NullPointerException if strategy is null
     */
    public RetryOptions setBackoffStrategy(BackoffStrategy strategy) {
        this.backoffStrategy = Objects.requireNonNull(strategy, "Backoff strategy cannot be null");
        return this;  
    }
    
    /**
     * Sets the HTTP status codes that should trigger a retry.
     * <p>Only requests that fail with these status codes will be retried.
     * Other status codes will fail immediately.
     * <p>If null or empty array is provided, no status code-based retries will occur.
     * 
     * @param codes HTTP status codes to retry (e.g., 429, 502, 503)
     * @return this RetryOptions instance for method chaining
     * @throws IllegalArgumentException if any status code is outside valid HTTP range (100-599)
     */
    public RetryOptions setRetryableStatusCodes(int... codes) {
        if (codes == null || codes.length == 0) {
            this.retryableStatusCodes = new int[0];
            return this;  
        }
        // Validate each status code is in valid HTTP range (100-599)
        for (int code : codes) {
            if (code < 100 || code > 599) {
                throw new IllegalArgumentException(
                    "Invalid HTTP status code: " + code + ". Must be between 100 and 599.");
            }
        }
        this.retryableStatusCodes = codes.clone();  
        return this;  
    }
    
    /**
     * Enables or disables the retry mechanism.
     * <p>When disabled, no retries will occur regardless of other settings.
     * 
     * @param enabled true to enable retries, false to disable
     * @return this RetryOptions instance for method chaining
     */
    public RetryOptions setRetryEnabled(boolean enabled) {
        this.retryEnabled = enabled;
        return this; 
    }
    /**
     * Sets the custom backoff strategy.
     */
    public RetryOptions setCustomBackoffStrategy(CustomBackoffStrategy customStrategy) {
        this.customBackoffStrategy = Objects.requireNonNull(customStrategy, "Custom backoff strategy cannot be null");
        this.backoffStrategy = BackoffStrategy.CUSTOM;
        return this;
    }

    /**
     * Returns the custom backoff strategy.
     */
    public CustomBackoffStrategy getCustomBackoffStrategy() {
        return customBackoffStrategy;
    }

     /**
     * Checks if custom backoff is configured.
     */
     public boolean hasCustomBackoff() {
        return customBackoffStrategy != null;
    }
    /**
     * Returns the maximum number of retry attempts.
     * 
     * @return retry limit (0-10)
     */
    public int getRetryLimit() {
        return retryLimit;
    }
    
    /**
     * Returns the base delay in milliseconds between retry attempts.
     * 
     * @return retry delay in milliseconds
     */
    public long getRetryDelay() {
        return retryDelayMs;
    }
    
    /**
     * Returns the backoff strategy used for calculating retry delays.
     * 
     * @return backoff strategy (FIXED, LINEAR, or EXPONENTIAL)
     */
    public BackoffStrategy getBackoffStrategy() {
        return backoffStrategy;
    }
    
    /**
     * Returns the HTTP status codes that trigger retries.
     * <p>Returns a copy to prevent external modification.
     * 
     * @return array of retryable HTTP status codes
     */
    public int[] getRetryableStatusCodes() {
        return retryableStatusCodes.clone();  
    }
    
    /**
     * Returns whether the retry mechanism is enabled.
     * 
     * @return true if retry is enabled, false otherwise
     */
    public boolean isRetryEnabled() {
        return retryEnabled;
    }

    /**
     * Returns a string representation of the retry configuration.
     * Useful for debugging and logging.
     * 
     * @return string representation of this configuration
     */
    @Override
    public String toString() {
        return "RetryOptions{" +
                "enabled=" + retryEnabled +
                ", limit=" + retryLimit +
                ", delay=" + retryDelayMs + "ms" +
                ", strategy=" + backoffStrategy +
                ", retryableCodes=" + java.util.Arrays.toString(retryableStatusCodes) +
                '}';
    }
}

