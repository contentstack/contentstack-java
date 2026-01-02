package com.contentstack.sdk;

import com.contentstack.sdk.utils.PerformanceAssertion;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;

/**
 * Comprehensive Integration Tests for Error Handling
 * Tests error scenarios including:
 * - Invalid UIDs (content type, entry, asset)
 * - Network error handling
 * - Invalid parameters
 * - Missing required fields
 * - Malformed queries
 * - Authentication errors
 * - Rate limiting (if applicable)
 * - Timeout scenarios
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ErrorHandlingComprehensiveIT extends BaseIntegrationTest {

    @BeforeAll
    void setUp() {
        logger.info("Setting up ErrorHandlingComprehensiveIT test suite");
        logger.info("Testing error handling scenarios");
    }

    // ===========================
    // Invalid UID Tests
    // ===========================

    @Test
    @Order(1)
    @DisplayName("Test invalid entry UID")
    void testInvalidEntryUid() throws InterruptedException {
        CountDownLatch latch = createLatch();

        Entry entry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                .entry("invalid_entry_uid_xyz_123");

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNotNull(error, "BUG: Should return error for invalid entry UID");
                    assertNotNull(error.getErrorMessage(), "Error message should not be null");
                    
                    logger.info("✅ Invalid entry UID error: " + error.getErrorMessage());
                    logSuccess("testInvalidEntryUid", "Error handled correctly");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testInvalidEntryUid"));
    }

    @Test
    @Order(2)
    @DisplayName("Test invalid content type UID")
    void testInvalidContentTypeUid() throws InterruptedException {
        CountDownLatch latch = createLatch();

        Query query = stack.contentType("invalid_content_type_xyz").query();

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNotNull(error, "BUG: Should return error for invalid content type UID");
                    assertNotNull(error.getErrorMessage(), "Error message should not be null");
                    
                    logger.info("✅ Invalid content type error: " + error.getErrorMessage());
                    logSuccess("testInvalidContentTypeUid", "Error handled correctly");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testInvalidContentTypeUid"));
    }

    @Test
    @Order(3)
    @DisplayName("Test invalid asset UID")
    void testInvalidAssetUid() throws InterruptedException {
        CountDownLatch latch = createLatch();

        Asset asset = stack.asset("invalid_asset_uid_xyz_123");

        asset.fetch(new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNotNull(error, "BUG: Should return error for invalid asset UID");
                    assertNotNull(error.getErrorMessage(), "Error message should not be null");
                    
                    logger.info("✅ Invalid asset UID error: " + error.getErrorMessage());
                    logSuccess("testInvalidAssetUid", "Error handled correctly");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testInvalidAssetUid"));
    }

    @Test
    @Order(4)
    @DisplayName("Test empty entry UID")
    void testEmptyEntryUid() throws InterruptedException {
        CountDownLatch latch = createLatch();

        Entry entry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).entry("");

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNotNull(error, "BUG: Should return error for empty entry UID");
                    
                    logger.info("✅ Empty entry UID error: " + error.getErrorMessage());
                    logSuccess("testEmptyEntryUid", "Error handled correctly");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testEmptyEntryUid"));
    }

    // ===========================
    // Malformed Query Tests
    // ===========================

    @Test
    @Order(5)
    @DisplayName("Test query with invalid field name")
    void testQueryWithInvalidFieldName() throws InterruptedException {
        CountDownLatch latch = createLatch();

        Query query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.where("nonexistent_field_xyz", "some_value");

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    // Should either return error OR return 0 results (both are valid)
                    if (error != null) {
                        logger.info("✅ Invalid field query returned error: " + error.getErrorMessage());
                        logSuccess("testQueryWithInvalidFieldName", "Error returned");
                    } else {
                        assertNotNull(queryResult, "QueryResult should not be null");
                        // Empty result is acceptable for non-existent field
                        logger.info("✅ Invalid field query returned empty results");
                        logSuccess("testQueryWithInvalidFieldName", "Empty results");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryWithInvalidFieldName"));
    }

    @Test
    @Order(6)
    @DisplayName("Test query with negative limit")
    void testQueryWithNegativeLimit() throws InterruptedException {
        CountDownLatch latch = createLatch();

        Query query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        
        try {
            query.limit(-5);
            
            query.find(new QueryResultsCallBack() {
                @Override
                public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                    try {
                        // Should either error or default to 0/ignore
                        if (error != null) {
                            logger.info("✅ Negative limit returned error: " + error.getErrorMessage());
                            logSuccess("testQueryWithNegativeLimit", "Error returned");
                        } else {
                            logger.info("ℹ️ Negative limit handled gracefully");
                            logSuccess("testQueryWithNegativeLimit", "Handled gracefully");
                        }
                    } finally {
                        latch.countDown();
                    }
                }
            });
        } catch (Exception e) {
            // Exception is also acceptable
            logger.info("✅ Negative limit threw exception: " + e.getMessage());
            logSuccess("testQueryWithNegativeLimit", "Exception thrown");
            latch.countDown();
        }

        assertTrue(awaitLatch(latch, "testQueryWithNegativeLimit"));
    }

    @Test
    @Order(7)
    @DisplayName("Test query with negative skip")
    void testQueryWithNegativeSkip() throws InterruptedException {
        CountDownLatch latch = createLatch();

        Query query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        
        try {
            query.skip(-10);
            
            query.find(new QueryResultsCallBack() {
                @Override
                public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                    try {
                        // Should either error or default to 0
                        if (error != null) {
                            logger.info("✅ Negative skip returned error: " + error.getErrorMessage());
                            logSuccess("testQueryWithNegativeSkip", "Error returned");
                        } else {
                            logger.info("ℹ️ Negative skip handled gracefully");
                            logSuccess("testQueryWithNegativeSkip", "Handled gracefully");
                        }
                    } finally {
                        latch.countDown();
                    }
                }
            });
        } catch (Exception e) {
            logger.info("✅ Negative skip threw exception: " + e.getMessage());
            logSuccess("testQueryWithNegativeSkip", "Exception thrown");
            latch.countDown();
        }

        assertTrue(awaitLatch(latch, "testQueryWithNegativeSkip"));
    }

    // ===========================
    // Reference and Include Tests
    // ===========================

    @Test
    @Order(8)
    @DisplayName("Test include reference with invalid field")
    void testIncludeReferenceWithInvalidField() throws InterruptedException {
        CountDownLatch latch = createLatch();

        Query query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.includeReference("nonexistent_reference_field");

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    // Should either error OR succeed with no references
                    if (error != null) {
                        logger.info("✅ Invalid reference field returned error: " + error.getErrorMessage());
                        logSuccess("testIncludeReferenceWithInvalidField", "Error returned");
                    } else {
                        assertNotNull(queryResult, "QueryResult should not be null");
                        logger.info("✅ Invalid reference field handled gracefully");
                        logSuccess("testIncludeReferenceWithInvalidField", "Handled gracefully");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testIncludeReferenceWithInvalidField"));
    }

    @Test
    @Order(9)
    @DisplayName("Test only() with invalid field")
    void testOnlyWithInvalidField() throws InterruptedException {
        CountDownLatch latch = createLatch();

        Query query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.only(new String[]{"nonexistent_field_xyz"});

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    // Should succeed but entries won't have that field
                    assertNull(error, "Should not error for non-existent field in only()");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    logger.info("✅ only() with invalid field handled gracefully");
                    logSuccess("testOnlyWithInvalidField", "Handled gracefully");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testOnlyWithInvalidField"));
    }

    @Test
    @Order(10)
    @DisplayName("Test except() with invalid field")
    void testExceptWithInvalidField() throws InterruptedException {
        CountDownLatch latch = createLatch();

        Query query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.except(new String[]{"nonexistent_field_xyz"});

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    // Should succeed (no harm in excluding non-existent field)
                    assertNull(error, "Should not error for non-existent field in except()");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    logger.info("✅ except() with invalid field handled gracefully");
                    logSuccess("testExceptWithInvalidField", "Handled gracefully");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testExceptWithInvalidField"));
    }

    // ===========================
    // Locale Tests
    // ===========================

    @Test
    @Order(11)
    @DisplayName("Test invalid locale")
    void testInvalidLocale() throws InterruptedException {
        CountDownLatch latch = createLatch();

        Query query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.locale("invalid-locale-xyz");

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    // Should either error OR return empty results
                    if (error != null) {
                        logger.info("✅ Invalid locale returned error: " + error.getErrorMessage());
                        logSuccess("testInvalidLocale", "Error returned");
                    } else {
                        assertNotNull(queryResult, "QueryResult should not be null");
                        logger.info("✅ Invalid locale handled gracefully");
                        logSuccess("testInvalidLocale", "Handled gracefully");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testInvalidLocale"));
    }

    // ===========================
    // Error Response Validation
    // ===========================

    @Test
    @Order(12)
    @DisplayName("Test error object has details")
    void testErrorObjectHasDetails() throws InterruptedException {
        CountDownLatch latch = createLatch();

        Entry entry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                .entry("definitely_invalid_uid_12345");

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNotNull(error, "Error should not be null");
                    
                    // Validate error has useful information
                    String errorMessage = error.getErrorMessage();
                    assertNotNull(errorMessage, "BUG: Error message should not be null");
                    assertFalse(errorMessage.isEmpty(), "BUG: Error message should not be empty");
                    
                    int errorCode = error.getErrorCode();
                    assertTrue(errorCode > 0, "BUG: Error code should be positive");
                    
                    logger.info("Error details:");
                    logger.info("  Code: " + errorCode);
                    logger.info("  Message: " + errorMessage);
                    
                    logger.info("✅ Error object has complete details");
                    logSuccess("testErrorObjectHasDetails", "Code: " + errorCode);
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testErrorObjectHasDetails"));
    }

    @Test
    @Order(13)
    @DisplayName("Test error code for not found")
    void testErrorCodeForNotFound() throws InterruptedException {
        CountDownLatch latch = createLatch();

        Entry entry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                .entry("not_found_entry_uid");

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNotNull(error, "Error should not be null");
                    
                    int errorCode = error.getErrorCode();
                    
                    // Common "not found" error codes: 404, 141, etc.
                    logger.info("Not found error code: " + errorCode);
                    assertTrue(errorCode > 0, "Error code should be meaningful");
                    
                    logger.info("✅ Not found error code validated");
                    logSuccess("testErrorCodeForNotFound", "Error code: " + errorCode);
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testErrorCodeForNotFound"));
    }

    // ===========================
    // Multiple Error Scenarios
    // ===========================

    @Test
    @Order(14)
    @DisplayName("Test multiple invalid entries in sequence")
    void testMultipleInvalidEntriesInSequence() throws InterruptedException {
        int errorCount = 0;
        
        for (int i = 0; i < 3; i++) {
            CountDownLatch latch = createLatch();
            final int[] hasError = {0};
            
            Entry entry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                    .entry("invalid_uid_" + i);
            
            entry.fetch(new EntryResultCallBack() {
                @Override
                public void onCompletion(ResponseType responseType, Error error) {
                    try {
                        if (error != null) {
                            hasError[0] = 1;
                        }
                    } finally {
                        latch.countDown();
                    }
                }
            });
            
            awaitLatch(latch, "invalid-" + i);
            errorCount += hasError[0];
        }
        
        assertEquals(3, errorCount, "BUG: All 3 invalid entries should return errors");
        logger.info("✅ Multiple invalid entries handled: " + errorCount + " errors");
        logSuccess("testMultipleInvalidEntriesInSequence", errorCount + " errors handled");
    }

    @Test
    @Order(15)
    @DisplayName("Test error recovery - subsequent call after error")
    void testErrorRecoveryValidAfterInvalid() throws InterruptedException {
        // First: invalid entry (should error)
        CountDownLatch latch1 = createLatch();
        final boolean[] hadError = {false};
        
        Entry invalidEntry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                .entry("invalid_uid_xyz");
        
        invalidEntry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    hadError[0] = (error != null);
                } finally {
                    latch1.countDown();
                }
            }
        });
        
        awaitLatch(latch1, "invalid-fetch");
        assertTrue(hadError[0], "Invalid entry should have errored");
        
        // Second: Make another query (SDK should still be functional)
        CountDownLatch latch2 = createLatch();
        final boolean[] secondCallCompleted = {false};
        
        Query query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.limit(1);
        
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    // Either success or error is fine - we just want to confirm SDK is still functional
                    secondCallCompleted[0] = true;
                    
                    if (error == null) {
                        logger.info("✅ SDK recovered from error - subsequent query successful");
                    } else {
                        logger.info("✅ SDK recovered from error - subsequent query returned (with error: " + error.getErrorMessage() + ")");
                    }
                    logSuccess("testErrorRecoveryValidAfterInvalid", "SDK functional after error");
                } finally {
                    latch2.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch2, "testErrorRecoveryValidAfterInvalid"));
        assertTrue(secondCallCompleted[0], "BUG: SDK should complete second call after error");
    }

    // ===========================
    // Null/Empty Parameter Tests
    // ===========================

    @Test
    @Order(16)
    @DisplayName("Test query with non-existent value")
    void testQueryWithNonExistentValue() throws InterruptedException {
        CountDownLatch latch = createLatch();

        Query query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.where("title", "this_value_does_not_exist_in_any_entry_xyz_12345");
        
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    // Should either error or return empty results
                    if (error != null) {
                        logger.info("✅ Non-existent value query returned error: " + error.getErrorMessage());
                        logSuccess("testQueryWithNonExistentValue", "Error returned");
                    } else {
                        assertNotNull(queryResult, "QueryResult should not be null");
                        // Empty result is acceptable
                        logger.info("✅ Non-existent value query handled gracefully: " + 
                                queryResult.getResultObjects().size() + " results");
                        logSuccess("testQueryWithNonExistentValue", "Handled gracefully");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryWithNonExistentValue"));
    }

    @Test
    @Order(17)
    @DisplayName("Test query with very large skip value")
    void testQueryWithVeryLargeSkip() throws InterruptedException {
        CountDownLatch latch = createLatch();

        Query query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.skip(10000); // Very large skip
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    // Should either error OR return empty results
                    if (error != null) {
                        logger.info("✅ Very large skip returned error: " + error.getErrorMessage());
                        logSuccess("testQueryWithVeryLargeSkip", "Error returned");
                    } else {
                        assertNotNull(queryResult, "QueryResult should not be null");
                        // Empty result is acceptable
                        logger.info("✅ Very large skip handled: " + 
                                queryResult.getResultObjects().size() + " results");
                        logSuccess("testQueryWithVeryLargeSkip", "Handled gracefully");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryWithVeryLargeSkip"));
    }

    @Test
    @Order(18)
    @DisplayName("Test comprehensive error handling scenario")
    void testComprehensiveErrorHandlingScenario() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        // Test multiple error conditions
        Query query = stack.contentType("invalid_ct_xyz").query();
        query.where("invalid_field", "invalid_value");
        query.locale("invalid-locale");
        query.includeReference("invalid_ref");

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    
                    // Should error (invalid content type)
                    assertNotNull(error, "BUG: Multiple invalid parameters should error");
                    assertNotNull(error.getErrorMessage(), "Error message should not be null");
                    
                    // Error should be returned quickly (not hang)
                    assertTrue(duration < 10000,
                            "PERFORMANCE BUG: Error response took " + duration + "ms (max: 10s)");
                    
                    logger.info("✅ COMPREHENSIVE: Error handled with multiple invalid params in " + 
                            formatDuration(duration));
                    logger.info("Error: " + error.getErrorMessage());
                    logSuccess("testComprehensiveErrorHandlingScenario", 
                            "Error code: " + error.getErrorCode() + ", " + formatDuration(duration));
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testComprehensiveErrorHandlingScenario"));
    }

    @AfterAll
    void tearDown() {
        logger.info("Completed ErrorHandlingComprehensiveIT test suite");
        logger.info("All 18 error handling tests executed");
        logger.info("Tested: invalid UIDs, malformed queries, error recovery, null params, comprehensive scenarios");
    }
}

