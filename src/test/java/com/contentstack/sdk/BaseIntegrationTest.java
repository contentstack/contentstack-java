package com.contentstack.sdk;

import com.contentstack.sdk.utils.TestHelpers;
import org.junit.jupiter.api.*;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

/**
 * Base class for all integration tests.
 * Provides common setup, utilities, and patterns for integration testing.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseIntegrationTest {

    protected final Logger logger = Logger.getLogger(this.getClass().getName());
    protected static Stack stack;
    
    /**
     * Default timeout for async operations (seconds)
     */
    protected static final int DEFAULT_TIMEOUT_SECONDS = 10;
    
    /**
     * Timeout for performance-sensitive operations (seconds)
     */
    protected static final int PERFORMANCE_TIMEOUT_SECONDS = 5;
    
    /**
     * Timeout for large dataset operations (seconds)
     */
    protected static final int LARGE_DATASET_TIMEOUT_SECONDS = 30;
    
    /**
     * Initialize shared stack instance before all tests
     */
    @BeforeAll
    public static void setUpBase() {
        stack = Credentials.getStack();
        if (stack == null) {
            throw new IllegalStateException("Stack initialization failed. Check your .env configuration.");
        }
    }
    
    /**
     * Log test suite start
     */
    @BeforeAll
    public void logTestSuiteStart() {
        logger.info(repeatString("=", 60));
        logger.info("Starting Test Suite: " + this.getClass().getSimpleName());
        logger.info(repeatString("=", 60));
        
        // Log available test data
        if (TestHelpers.isComplexTestDataAvailable()) {
            logger.info("✅ Complex test data available");
        }
        if (TestHelpers.isTaxonomyTestingAvailable()) {
            logger.info("✅ Taxonomy testing available");
        }
        if (TestHelpers.isVariantTestingAvailable()) {
            logger.info("✅ Variant testing available");
        }
    }
    
    /**
     * Log test suite completion
     */
    @AfterAll
    public void logTestSuiteEnd(TestInfo testInfo) {
        logger.info(repeatString("=", 60));
        logger.info("Completed Test Suite: " + this.getClass().getSimpleName());
        logger.info(repeatString("=", 60));
    }
    
    /**
     * Log individual test start
     */
    @BeforeEach
    public void logTestStart(TestInfo testInfo) {
        logger.info(repeatString("-", 60));
        logger.info("Starting Test: " + testInfo.getDisplayName());
    }
    
    /**
     * Log individual test end
     */
    @AfterEach
    public void logTestEnd(TestInfo testInfo) {
        logger.info("Completed Test: " + testInfo.getDisplayName());
        logger.info(repeatString("-", 60));
    }
    
    /**
     * Repeat a string n times (Java 8 compatible)
     */
    private String repeatString(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
    
    /**
     * Create a new CountDownLatch with count of 1
     * 
     * @return CountDownLatch initialized to 1
     */
    protected CountDownLatch createLatch() {
        return new CountDownLatch(1);
    }
    
    /**
     * Create a new CountDownLatch with specified count
     * 
     * @param count Initial count
     * @return CountDownLatch initialized to count
     */
    protected CountDownLatch createLatch(int count) {
        return new CountDownLatch(count);
    }
    
    /**
     * Wait for latch with default timeout
     * 
     * @param latch CountDownLatch to wait for
     * @param testName Name of test (for logging)
     * @return true if latch counted down before timeout
     * @throws InterruptedException if interrupted
     */
    protected boolean awaitLatch(CountDownLatch latch, String testName) throws InterruptedException {
        return TestHelpers.awaitLatch(latch, DEFAULT_TIMEOUT_SECONDS, testName);
    }
    
    /**
     * Wait for latch with custom timeout
     * 
     * @param latch CountDownLatch to wait for
     * @param timeoutSeconds Timeout in seconds
     * @param testName Name of test (for logging)
     * @return true if latch counted down before timeout
     * @throws InterruptedException if interrupted
     */
    protected boolean awaitLatch(CountDownLatch latch, int timeoutSeconds, String testName) 
            throws InterruptedException {
        return TestHelpers.awaitLatch(latch, timeoutSeconds, testName);
    }
    
    /**
     * Log test success
     * 
     * @param testName Name of test
     */
    protected void logSuccess(String testName) {
        TestHelpers.logSuccess(testName);
    }
    
    /**
     * Log test success with message
     * 
     * @param testName Name of test
     * @param message Additional message
     */
    protected void logSuccess(String testName, String message) {
        TestHelpers.logSuccess(testName, message);
    }
    
    /**
     * Log test failure
     * 
     * @param testName Name of test
     * @param error The error
     */
    protected void logFailure(String testName, com.contentstack.sdk.Error error) {
        TestHelpers.logFailure(testName, error);
    }
    
    /**
     * Log test warning
     * 
     * @param testName Name of test
     * @param message Warning message
     */
    protected void logWarning(String testName, String message) {
        TestHelpers.logWarning(testName, message);
    }
    
    /**
     * Measure test execution time
     * 
     * @return Current timestamp in milliseconds
     */
    protected long startTimer() {
        return System.currentTimeMillis();
    }
    
    /**
     * Log execution time since start
     * 
     * @param testName Name of test
     * @param startTime Start timestamp from startTimer()
     */
    protected void logExecutionTime(String testName, long startTime) {
        TestHelpers.logExecutionTime(testName, startTime);
    }
    
    /**
     * Get formatted duration
     * 
     * @param durationMs Duration in milliseconds
     * @return Formatted string (e.g., "1.23s" or "456ms")
     */
    protected String formatDuration(long durationMs) {
        return TestHelpers.formatDuration(durationMs);
    }
    
    /**
     * Validate entry has basic required fields
     * 
     * @param entry Entry to validate
     * @return true if entry has uid, title, locale
     */
    protected boolean hasBasicFields(Entry entry) {
        return TestHelpers.hasBasicFields(entry);
    }
    
    /**
     * Validate query result has entries
     * 
     * @param result QueryResult to validate
     * @return true if result has entries
     */
    protected boolean hasResults(QueryResult result) {
        return TestHelpers.hasResults(result);
    }
    
    /**
     * Safely get header value as String
     * 
     * @param entry Entry to get header from
     * @param headerName Name of header
     * @return Header value as String, or null
     */
    protected String getHeaderAsString(Entry entry, String headerName) {
        return TestHelpers.getHeaderAsString(entry, headerName);
    }
}

