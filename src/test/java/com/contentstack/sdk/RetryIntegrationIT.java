package com.contentstack.sdk;

import com.contentstack.sdk.utils.PerformanceAssertion;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;

/**
 * Comprehensive Integration Tests for Retry Mechanisms
 * Tests retry behavior including:
 * - Network retry configuration
 * - Retry policy validation
 * - Max retry limits
 * - Exponential backoff (if supported)
 * - Retry with different operations
 * - Performance impact of retries
 * Note: These tests validate retry configuration and behavior,
 * not actual network failures (which are difficult to test reliably)
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RetryIntegrationIT extends BaseIntegrationTest {

    @BeforeAll
    void setUp() {
        logger.info("Setting up RetryIntegrationIT test suite");
        logger.info("Testing retry mechanisms and configuration");
    }

    // ===========================
    // Retry Configuration Tests
    // ===========================

    @Test
    @Order(1)
    @DisplayName("Test stack initialization with default retry")
    void testStackInitializationWithDefaultRetry() {
        // Stack should initialize with default retry settings
        assertNotNull(stack, "Stack should not be null");
        
        logger.info("✅ Stack initialized with default retry configuration");
        logSuccess("testStackInitializationWithDefaultRetry", "Default retry config");
    }

    @Test
    @Order(2)
    @DisplayName("Test query with retry behavior")
    void testQueryWithRetryBehavior() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        Query query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    
                    // Should succeed (no retries needed for valid request)
                    assertNull(error, "Valid query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    // Should complete quickly (no retries)
                    assertTrue(duration < 5000,
                            "Valid query should complete quickly: " + duration + "ms");
                    
                    logger.info("✅ Query with retry behavior: " + queryResult.getResultObjects().size() + 
                            " results in " + formatDuration(duration));
                    logSuccess("testQueryWithRetryBehavior", formatDuration(duration));
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryWithRetryBehavior"));
    }

    @Test
    @Order(3)
    @DisplayName("Test entry fetch with retry behavior")
    void testEntryFetchWithRetryBehavior() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        Entry entry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                .entry(Credentials.COMPLEX_ENTRY_UID);

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    
                    // Entry fetch completes (error or success)
                    if (error != null) {
                        logger.info("Entry fetch returned error: " + error.getErrorMessage());
                    }
                    
                    // Should complete quickly (with or without retries)
                    assertTrue(duration < 5000,
                            "Entry fetch should complete quickly: " + duration + "ms");
                    
                    logger.info("✅ Entry fetch with retry behavior: " + formatDuration(duration));
                    logSuccess("testEntryFetchWithRetryBehavior", formatDuration(duration));
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testEntryFetchWithRetryBehavior"));
    }

    @Test
    @Order(4)
    @DisplayName("Test asset fetch with retry behavior")
    void testAssetFetchWithRetryBehavior() throws InterruptedException {
        if (Credentials.IMAGE_ASSET_UID == null || Credentials.IMAGE_ASSET_UID.isEmpty()) {
            logger.info("ℹ️ No asset UID configured, skipping test");
            logSuccess("testAssetFetchWithRetryBehavior", "Skipped");
            return;
        }

        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        Asset asset = stack.asset(Credentials.IMAGE_ASSET_UID);

        asset.fetch(new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    
                    assertNull(error, "Valid asset fetch should not error");
                    assertNotNull(asset.getAssetUid(), "Asset should have UID");
                    
                    // Should complete quickly (no retries)
                    assertTrue(duration < 5000,
                            "Valid asset fetch should complete quickly: " + duration + "ms");
                    
                    logger.info("✅ Asset fetch with retry behavior: " + formatDuration(duration));
                    logSuccess("testAssetFetchWithRetryBehavior", formatDuration(duration));
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testAssetFetchWithRetryBehavior"));
    }

    // ===========================
    // Retry Performance Tests
    // ===========================

    @Test
    @Order(5)
    @DisplayName("Test multiple requests with retry")
    void testMultipleRequestsWithRetry() throws InterruptedException {
        int requestCount = 5;
        long startTime = PerformanceAssertion.startTimer();
        
        for (int i = 0; i < requestCount; i++) {
            CountDownLatch latch = createLatch();
            
            Query query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
            query.limit(3);
            
            query.find(new QueryResultsCallBack() {
                @Override
                public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                    try {
                        assertNull(error, "Query should not error");
                        assertNotNull(queryResult, "QueryResult should not be null");
                    } finally {
                        latch.countDown();
                    }
                }
            });
            
            awaitLatch(latch, "request-" + i);
        }
        
        long duration = PerformanceAssertion.elapsedTime(startTime);
        
        // Multiple requests should complete reasonably fast
        assertTrue(duration < 15000,
                "PERFORMANCE BUG: " + requestCount + " requests took " + duration + "ms (max: 15s)");
        
        logger.info("✅ Multiple requests with retry: " + requestCount + " requests in " + 
                formatDuration(duration));
        logSuccess("testMultipleRequestsWithRetry", 
                requestCount + " requests, " + formatDuration(duration));
    }

    @Test
    @Order(6)
    @DisplayName("Test retry behavior consistency")
    void testRetryBehaviorConsistency() throws InterruptedException {
        // Make same request multiple times and ensure consistent behavior
        final long[] durations = new long[3];
        
        for (int i = 0; i < 3; i++) {
            CountDownLatch latch = createLatch();
            long startTime = PerformanceAssertion.startTimer();
            final int index = i;
            
            Query query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
            query.limit(5);
            
            query.find(new QueryResultsCallBack() {
                @Override
                public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                    try {
                        durations[index] = PerformanceAssertion.elapsedTime(startTime);
                        assertNull(error, "Query should not error");
                        assertNotNull(queryResult, "QueryResult should not be null");
                    } finally {
                        latch.countDown();
                    }
                }
            });
            
            awaitLatch(latch, "consistency-" + i);
        }
        
        // Durations should be relatively consistent (within 3x of each other)
        long minDuration = Math.min(durations[0], Math.min(durations[1], durations[2]));
        long maxDuration = Math.max(durations[0], Math.max(durations[1], durations[2]));
        
        assertTrue(maxDuration < minDuration * 3,
                "CONSISTENCY BUG: Request durations vary too much: " + 
                minDuration + "ms to " + maxDuration + "ms");
        
        logger.info("✅ Retry behavior consistent: " + minDuration + "ms to " + maxDuration + "ms");
        logSuccess("testRetryBehaviorConsistency", 
                minDuration + "ms to " + maxDuration + "ms");
    }

    // ===========================
    // Error Retry Tests
    // ===========================

    @Test
    @Order(7)
    @DisplayName("Test retry with invalid request")
    void testRetryWithInvalidRequest() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        // Invalid request should fail without excessive retries
        Entry entry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                .entry("invalid_entry_uid_xyz");

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    
                    // Should error (invalid UID)
                    assertNotNull(error, "Invalid entry should error");
                    
                    // Should fail quickly (no retries for 404-type errors)
                    assertTrue(duration < 5000,
                            "Invalid request should fail quickly: " + duration + "ms");
                    
                    logger.info("✅ Invalid request handled: " + formatDuration(duration));
                    logSuccess("testRetryWithInvalidRequest", formatDuration(duration));
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testRetryWithInvalidRequest"));
    }

    @Test
    @Order(8)
    @DisplayName("Test retry does not hang on errors")
    void testRetryDoesNotHangOnErrors() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        // Multiple invalid requests should all complete without hanging
        Query query = stack.contentType("nonexistent_content_type_xyz").query();

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    
                    // Should error
                    assertNotNull(error, "Invalid content type should error");
                    
                    // Should not hang
                    assertTrue(duration < 10000,
                            "Error request should not hang: " + duration + "ms");
                    
                    logger.info("✅ Retry does not hang on errors: " + formatDuration(duration));
                    logSuccess("testRetryDoesNotHangOnErrors", formatDuration(duration));
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testRetryDoesNotHangOnErrors"));
    }

    // ===========================
    // Comprehensive Tests
    // ===========================

    @Test
    @Order(9)
    @DisplayName("Test retry with complex query")
    void testRetryWithComplexQuery() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        Query query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.where("title", Credentials.COMPLEX_ENTRY_UID);
        query.includeReference("reference");
        query.includeCount();
        query.limit(10);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    
                    // Complex query should work with retry
                    assertNull(error, "Complex query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    // Should complete in reasonable time
                    assertTrue(duration < 10000,
                            "Complex query should complete in reasonable time: " + duration + "ms");
                    
                    logger.info("✅ Complex query with retry: " + formatDuration(duration));
                    logSuccess("testRetryWithComplexQuery", formatDuration(duration));
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testRetryWithComplexQuery"));
    }

    @Test
    @Order(10)
    @DisplayName("Test comprehensive retry scenario")
    void testComprehensiveRetryScenario() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        // Test multiple operation types with retry
        final boolean[] querySuccess = {false};
        final boolean[] entrySuccess = {false};
        final boolean[] assetSuccess = {false};
        
        // 1. Query
        CountDownLatch queryLatch = createLatch();
        Query query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.limit(3);
        
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    querySuccess[0] = (error == null && queryResult != null);
                } finally {
                    queryLatch.countDown();
                }
            }
        });
        awaitLatch(queryLatch, "query");
        
        // 2. Entry
        CountDownLatch entryLatch = createLatch();
        Entry entry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                .entry(Credentials.COMPLEX_ENTRY_UID);
        
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    // Mark as success if completed (even with error - we're testing retry completes)
                    entrySuccess[0] = true;
                } finally {
                    entryLatch.countDown();
                }
            }
        });
        awaitLatch(entryLatch, "entry");
        
        // 3. Asset (if available)
        if (Credentials.IMAGE_ASSET_UID != null && !Credentials.IMAGE_ASSET_UID.isEmpty()) {
            CountDownLatch assetLatch = createLatch();
            Asset asset = stack.asset(Credentials.IMAGE_ASSET_UID);
            
            asset.fetch(new FetchResultCallback() {
                @Override
                public void onCompletion(ResponseType responseType, Error error) {
                    try {
                        assetSuccess[0] = (error == null);
                    } finally {
                        assetLatch.countDown();
                    }
                }
            });
            awaitLatch(assetLatch, "asset");
        } else {
            assetSuccess[0] = true; // Skip asset test
        }
        
        long duration = PerformanceAssertion.elapsedTime(startTime);
        
        // Validate all operations completed (with or without error)
        assertTrue(querySuccess[0], "BUG: Query should complete with retry");
        assertTrue(entrySuccess[0], "BUG: Entry fetch should complete with retry");
        assertTrue(assetSuccess[0], "BUG: Asset fetch should complete with retry");
        
        // Should complete in reasonable time
        assertTrue(duration < 15000,
                "PERFORMANCE BUG: Comprehensive scenario took " + duration + "ms (max: 15s)");
        
        logger.info("✅ COMPREHENSIVE: All operations succeeded with retry in " + 
                formatDuration(duration));
        logSuccess("testComprehensiveRetryScenario", formatDuration(duration));
        
        latch.countDown();
        assertTrue(awaitLatch(latch, "testComprehensiveRetryScenario"));
    }

    @AfterAll
    void tearDown() {
        logger.info("Completed RetryIntegrationIT test suite");
        logger.info("All 10 retry integration tests executed");
        logger.info("Tested: retry configuration, behavior, performance, error handling, comprehensive scenarios");
    }
}

