package com.contentstack.sdk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RetryOptions class.
 * Tests configuration, validation, and default values.
 */
class TestRetryOptions {

    private RetryOptions retryOptions;

    @BeforeEach
    void setUp() {
        retryOptions = new RetryOptions();
    }

    // ===========================
    // Default Values Tests
    // ===========================

    @Test
    @DisplayName("Test default retry limit is 3")
    void testDefaultRetryLimit() {
        assertEquals(3, retryOptions.getRetryLimit(),
                "Default retry limit should be 3");
    }

    @Test
    @DisplayName("Test default retry delay is 1000ms")
    void testDefaultRetryDelay() {
        assertEquals(1000L, retryOptions.getRetryDelay(),
                "Default retry delay should be 1000ms");
    }

    @Test
    @DisplayName("Test default backoff strategy is EXPONENTIAL")
    void testDefaultBackoffStrategy() {
        assertEquals(RetryOptions.BackoffStrategy.EXPONENTIAL,
                retryOptions.getBackoffStrategy(),
                "Default backoff strategy should be EXPONENTIAL");
    }

    @Test
    @DisplayName("Test default retryable status codes")
    void testDefaultRetryableStatusCodes() {
        int[] expected = {408, 429, 502, 503, 504};
        assertArrayEquals(expected, retryOptions.getRetryableStatusCodes(),
                "Default retryable status codes should be 408, 429, 502, 503, 504");
    }

    @Test
    @DisplayName("Test retry is enabled by default")
    void testRetryEnabledByDefault() {
        assertTrue(retryOptions.isRetryEnabled(),
                "Retry should be enabled by default");
    }

    // ===========================
    // Setter Tests
    // ===========================

    @Test
    @DisplayName("Test setting valid retry limit")
    void testSetValidRetryLimit() {
        RetryOptions result = retryOptions.setRetryLimit(5);
        
        assertEquals(5, retryOptions.getRetryLimit(),
                "Retry limit should be set to 5");
        assertSame(retryOptions, result,
                "Setter should return same instance for chaining");
    }

    @Test
    @DisplayName("Test setting retry limit to 0 (disabled retries)")
    void testSetRetryLimitToZero() {
        retryOptions.setRetryLimit(0);
        assertEquals(0, retryOptions.getRetryLimit(),
                "Retry limit can be set to 0");
    }

    @Test
    @DisplayName("Test setting retry limit to maximum (10)")
    void testSetRetryLimitToMaximum() {
        retryOptions.setRetryLimit(10);
        assertEquals(10, retryOptions.getRetryLimit(),
                "Retry limit should accept maximum value of 10");
    }

    @Test
    @DisplayName("Test negative retry limit throws exception")
    void testNegativeRetryLimitThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> retryOptions.setRetryLimit(-1),
                "Negative retry limit should throw exception"
        );
        assertTrue(exception.getMessage().contains("cannot be negative"),
                "Exception message should mention 'cannot be negative'");
    }

    @Test
    @DisplayName("Test retry limit above 10 throws exception")
    void testRetryLimitAbove10ThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> retryOptions.setRetryLimit(11),
                "Retry limit above 10 should throw exception"
        );
        assertTrue(exception.getMessage().contains("cannot exceed 10"),
                "Exception message should mention 'cannot exceed 10'");
    }

    @Test
    @DisplayName("Test setting valid retry delay")
    void testSetValidRetryDelay() {
        RetryOptions result = retryOptions.setRetryDelay(2000L);
        
        assertEquals(2000L, retryOptions.getRetryDelay(),
                "Retry delay should be set to 2000ms");
        assertSame(retryOptions, result,
                "Setter should return same instance for chaining");
    }

    @Test
    @DisplayName("Test setting retry delay to 0")
    void testSetRetryDelayToZero() {
        retryOptions.setRetryDelay(0L);
        assertEquals(0L, retryOptions.getRetryDelay(),
                "Retry delay can be set to 0");
    }

    @Test
    @DisplayName("Test negative retry delay throws exception")
    void testNegativeRetryDelayThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> retryOptions.setRetryDelay(-100L),
                "Negative retry delay should throw exception"
        );
        assertTrue(exception.getMessage().contains("cannot be negative"),
                "Exception message should mention 'cannot be negative'");
    }

    @Test
    @DisplayName("Test setting backoff strategy to FIXED")
    void testSetBackoffStrategyFixed() {
        retryOptions.setBackoffStrategy(RetryOptions.BackoffStrategy.FIXED);
        assertEquals(RetryOptions.BackoffStrategy.FIXED,
                retryOptions.getBackoffStrategy(),
                "Backoff strategy should be set to FIXED");
    }

    @Test
    @DisplayName("Test setting backoff strategy to LINEAR")
    void testSetBackoffStrategyLinear() {
        retryOptions.setBackoffStrategy(RetryOptions.BackoffStrategy.LINEAR);
        assertEquals(RetryOptions.BackoffStrategy.LINEAR,
                retryOptions.getBackoffStrategy(),
                "Backoff strategy should be set to LINEAR");
    }

    @Test
    @DisplayName("Test null backoff strategy throws exception")
    void testNullBackoffStrategyThrowsException() {
        assertThrows(
                NullPointerException.class,
                () -> retryOptions.setBackoffStrategy(null),
                "Null backoff strategy should throw exception"
        );
    }

    @Test
    @DisplayName("Test setting custom retryable status codes")
    void testSetCustomRetryableStatusCodes() {
        int[] customCodes = {500, 502, 503};
        RetryOptions result = retryOptions.setRetryableStatusCodes(customCodes);
        
        assertArrayEquals(customCodes, retryOptions.getRetryableStatusCodes(),
                "Custom retryable status codes should be set");
        assertSame(retryOptions, result,
                "Setter should return same instance for chaining");
    }

    @Test
    @DisplayName("Test setting empty retryable status codes")
    void testSetEmptyRetryableStatusCodes() {
        retryOptions.setRetryableStatusCodes(new int[]{});
        assertArrayEquals(new int[]{}, retryOptions.getRetryableStatusCodes(),
                "Empty status codes array should be accepted");
    }

    @Test
    @DisplayName("Test setting null retryable status codes")
    void testSetNullRetryableStatusCodes() {
        retryOptions.setRetryableStatusCodes(null);
        assertArrayEquals(new int[]{}, retryOptions.getRetryableStatusCodes(),
                "Null status codes should result in empty array");
    }

    @Test
    @DisplayName("Test getRetryableStatusCodes returns defensive copy")
    void testGetRetryableStatusCodesReturnsDefensiveCopy() {
        int[] codes = retryOptions.getRetryableStatusCodes();
        int originalFirst = codes[0];
        
        // Modify returned array
        codes[0] = 999;
        
        // Original should be unchanged
        int[] codesAgain = retryOptions.getRetryableStatusCodes();
        assertEquals(originalFirst, codesAgain[0],
                "Modifying returned array should not affect internal state");
    }

    @Test
    @DisplayName("Test enabling retry")
    void testEnableRetry() {
        retryOptions.setRetryEnabled(true);
        assertTrue(retryOptions.isRetryEnabled(),
                "Retry should be enabled");
    }

    @Test
    @DisplayName("Test disabling retry")
    void testDisableRetry() {
        retryOptions.setRetryEnabled(false);
        assertFalse(retryOptions.isRetryEnabled(),
                "Retry should be disabled");
    }

    // ===========================
    // Fluent API / Method Chaining Tests
    // ===========================

    @Test
    @DisplayName("Test fluent API method chaining")
    void testFluentMethodChaining() {
        RetryOptions result = retryOptions
                .setRetryLimit(5)
                .setRetryDelay(2000L)
                .setBackoffStrategy(RetryOptions.BackoffStrategy.LINEAR)
                .setRetryableStatusCodes(429, 503)
                .setRetryEnabled(true);

        assertSame(retryOptions, result,
                "All setters should return same instance");
        assertEquals(5, retryOptions.getRetryLimit());
        assertEquals(2000L, retryOptions.getRetryDelay());
        assertEquals(RetryOptions.BackoffStrategy.LINEAR, retryOptions.getBackoffStrategy());
        assertArrayEquals(new int[]{429, 503}, retryOptions.getRetryableStatusCodes());
        assertTrue(retryOptions.isRetryEnabled());
    }

    // ===========================
    // toString() Tests
    // ===========================

    @Test
    @DisplayName("Test setRetryableStatusCodes with code below 100 throws exception")
    void testSetRetryableStatusCodesWithCodeBelow100() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> retryOptions.setRetryableStatusCodes(99),
                "Should throw exception for status code < 100"
        );
        assertTrue(exception.getMessage().contains("Invalid HTTP status code: 99"),
                "Exception message should mention invalid code 99");
        assertTrue(exception.getMessage().contains("Must be between 100 and 599"),
                "Exception message should mention valid range");
    }

    @Test
    @DisplayName("Test setRetryableStatusCodes with code above 599 throws exception")
    void testSetRetryableStatusCodesWithCodeAbove599() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> retryOptions.setRetryableStatusCodes(600),
                "Should throw exception for status code > 599"
        );
        assertTrue(exception.getMessage().contains("Invalid HTTP status code: 600"),
                "Exception message should mention invalid code 600");
        assertTrue(exception.getMessage().contains("Must be between 100 and 599"),
                "Exception message should mention valid range");
    }

    @Test
    @DisplayName("Test setRetryableStatusCodes with mixed valid and invalid codes")
    void testSetRetryableStatusCodesWithMixedCodes() {
        // Should throw on first invalid code encountered
        assertThrows(
                IllegalArgumentException.class,
                () -> retryOptions.setRetryableStatusCodes(200, 50, 503),
                "Should throw exception when encountering invalid code in array"
        );
    }

    @Test
    @DisplayName("Test toString contains all configuration")
    void testToStringContainsConfiguration() {
        String result = retryOptions.toString();
        
        assertNotNull(result, "toString should not return null");
        assertTrue(result.contains("enabled="), "toString should contain enabled status");
        assertTrue(result.contains("limit="), "toString should contain limit");
        assertTrue(result.contains("delay="), "toString should contain delay");
        assertTrue(result.contains("strategy="), "toString should contain strategy");
        assertTrue(result.contains("retryableCodes="), "toString should contain retryable codes");
    }

    @Test
    @DisplayName("Test toString with disabled retry")
    void testToStringWithDisabledRetry() {
        retryOptions.setRetryEnabled(false);
        String result = retryOptions.toString();
        
        assertTrue(result.contains("enabled=false"),
                "toString should show retry as disabled");
    }

    // ===========================
    // Backoff Strategy Enum Tests
    // ===========================

    @Test
    @DisplayName("Test BackoffStrategy enum values")
    void testBackoffStrategyEnumValues() {
        RetryOptions.BackoffStrategy[] strategies = RetryOptions.BackoffStrategy.values();
        
        assertEquals(4, strategies.length,
                "Should have 4 backoff strategies (FIXED, LINEAR, EXPONENTIAL, CUSTOM)");
        assertTrue(Arrays.asList(strategies).contains(RetryOptions.BackoffStrategy.FIXED));
        assertTrue(Arrays.asList(strategies).contains(RetryOptions.BackoffStrategy.LINEAR));
        assertTrue(Arrays.asList(strategies).contains(RetryOptions.BackoffStrategy.EXPONENTIAL));
        assertTrue(Arrays.asList(strategies).contains(RetryOptions.BackoffStrategy.CUSTOM));
    }

    @Test
    @DisplayName("Test BackoffStrategy valueOf")
    void testBackoffStrategyValueOf() {
        assertEquals(RetryOptions.BackoffStrategy.FIXED,
                RetryOptions.BackoffStrategy.valueOf("FIXED"));
        assertEquals(RetryOptions.BackoffStrategy.LINEAR,
                RetryOptions.BackoffStrategy.valueOf("LINEAR"));
        assertEquals(RetryOptions.BackoffStrategy.EXPONENTIAL,
                RetryOptions.BackoffStrategy.valueOf("EXPONENTIAL"));
    }

    // ===========================
    // Edge Cases
    // ===========================

    @Test
    @DisplayName("Test setting very large delay value")
    void testVeryLargeDelayValue() {
        long largeDelay = Long.MAX_VALUE;
        retryOptions.setRetryDelay(largeDelay);
        assertEquals(largeDelay, retryOptions.getRetryDelay(),
                "Should accept very large delay values");
    }

    @Test
    @DisplayName("Test setting single retryable status code")
    void testSingleRetryableStatusCode() {
        retryOptions.setRetryableStatusCodes(429);
        assertArrayEquals(new int[]{429}, retryOptions.getRetryableStatusCodes(),
                "Should accept single status code");
    }

    @Test
    @DisplayName("Test multiple instances are independent")
    void testMultipleInstancesAreIndependent() {
        RetryOptions options1 = new RetryOptions();
        RetryOptions options2 = new RetryOptions();
        
        options1.setRetryLimit(5);
        options2.setRetryLimit(7);
        
        assertEquals(5, options1.getRetryLimit(),
                "First instance should have its own limit");
        assertEquals(7, options2.getRetryLimit(),
                "Second instance should have its own limit");
    }

    // ===========================
    // Custom Backoff Strategy Tests
    // ===========================

    @Test
    @DisplayName("Test setting custom backoff strategy")
    void testSetCustomBackoffStrategy() {
        CustomBackoffStrategy customStrategy = (attempt, statusCode, exception) -> 
            1000L * (attempt + 1);
        
        RetryOptions result = retryOptions.setCustomBackoffStrategy(customStrategy);
        
        assertEquals(customStrategy, retryOptions.getCustomBackoffStrategy(),
                "Custom backoff strategy should be set");
        assertSame(retryOptions, result,
                "Setter should return same instance for chaining");
    }

    @Test
    @DisplayName("Test setting custom backoff changes strategy to CUSTOM")
    void testCustomBackoffSetsStrategyToCustom() {
        retryOptions.setBackoffStrategy(RetryOptions.BackoffStrategy.EXPONENTIAL);
        
        retryOptions.setCustomBackoffStrategy((a, s, e) -> 1000L);
        
        assertEquals(RetryOptions.BackoffStrategy.CUSTOM,
                retryOptions.getBackoffStrategy(),
                "Backoff strategy should be set to CUSTOM");
    }

    @Test
    @DisplayName("Test hasCustomBackoff returns false by default")
    void testHasCustomBackoffDefaultFalse() {
        assertFalse(retryOptions.hasCustomBackoff(),
                "Should not have custom backoff by default");
    }

    @Test
    @DisplayName("Test hasCustomBackoff returns true after setting")
    void testHasCustomBackoffAfterSetting() {
        retryOptions.setCustomBackoffStrategy((a, s, e) -> 1000L);
        
        assertTrue(retryOptions.hasCustomBackoff(),
                "Should have custom backoff after setting");
    }

    @Test
    @DisplayName("Test null custom backoff strategy throws exception")
    void testNullCustomBackoffStrategyThrowsException() {
        assertThrows(
                NullPointerException.class,
                () -> retryOptions.setCustomBackoffStrategy(null),
                "Null custom backoff strategy should throw exception"
        );
    }

    @Test
    @DisplayName("Test getCustomBackoffStrategy returns null by default")
    void testGetCustomBackoffStrategyDefaultNull() {
        assertNull(retryOptions.getCustomBackoffStrategy(),
                "Custom backoff strategy should be null by default");
    }

    @Test
    @DisplayName("Test custom backoff strategy in fluent chain")
    void testCustomBackoffStrategyInFluentChain() {
        CustomBackoffStrategy strategy = (attempt, statusCode, exception) -> 2000L;
        
        RetryOptions result = retryOptions
                .setRetryLimit(5)
                .setRetryDelay(1000L)
                .setCustomBackoffStrategy(strategy)
                .setRetryEnabled(true);
        
        assertSame(retryOptions, result,
                "All setters should return same instance");
        assertEquals(strategy, retryOptions.getCustomBackoffStrategy(),
                "Custom strategy should be set");
        assertTrue(retryOptions.hasCustomBackoff(),
                "Should have custom backoff");
    }

    @Test
    @DisplayName("Test CUSTOM enum value exists in BackoffStrategy")
    void testCustomEnumValueExists() {
        RetryOptions.BackoffStrategy[] strategies = RetryOptions.BackoffStrategy.values();
        
        boolean hasCustom = false;
        for (RetryOptions.BackoffStrategy strategy : strategies) {
            if (strategy == RetryOptions.BackoffStrategy.CUSTOM) {
                hasCustom = true;
                break;
            }
        }
        
        assertTrue(hasCustom, "BackoffStrategy enum should have CUSTOM value");
    }

    @Test
    @DisplayName("Test toString includes custom backoff status")
    void testToStringWithCustomBackoff() {
        retryOptions.setCustomBackoffStrategy((a, s, e) -> 1000L);
        
        String result = retryOptions.toString();
        
        assertNotNull(result, "toString should not return null");
        assertTrue(result.contains("strategy=CUSTOM"),
                "toString should indicate CUSTOM strategy when custom backoff is set");
    }

    @Test
    @DisplayName("Test custom backoff strategy can be lambda")
    void testCustomBackoffStrategyAsLambda() {
        retryOptions.setCustomBackoffStrategy((attempt, statusCode, exception) -> {
            // Custom logic
            return 1000L * (long)Math.pow(2, attempt);
        });
        
        assertTrue(retryOptions.hasCustomBackoff(),
                "Should accept lambda as custom backoff");
        assertNotNull(retryOptions.getCustomBackoffStrategy(),
                "Custom backoff should not be null");
    }

    @Test
    @DisplayName("Test custom backoff strategy can be method reference")
    void testCustomBackoffStrategyAsMethodReference() {
        retryOptions.setCustomBackoffStrategy(this::customBackoffMethod);
        
        assertTrue(retryOptions.hasCustomBackoff(),
                "Should accept method reference as custom backoff");
    }
    
    // Helper method for method reference test
    private long customBackoffMethod(int attempt, int statusCode, java.io.IOException exception) {
        return 1000L * attempt;
    }
}

