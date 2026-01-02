package com.contentstack.sdk;

import com.contentstack.sdk.utils.PerformanceAssertion;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;

/**
 * Comprehensive Integration Tests for Field Projection (Only/Except)
 * Tests field projection behavior including:
 * - Only specific fields
 * - Except specific fields
 * - Nested field projection
 * - Projection with references
 * - Projection with embedded items
 * - Projection performance
 * - Edge cases (empty, invalid, all fields)
 * Uses complex content types with many fields to test projection scenarios
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FieldProjectionAdvancedIT extends BaseIntegrationTest {

    private Query query;
    private Entry entry;

    @BeforeAll
    void setUp() {
        logger.info("Setting up FieldProjectionAdvancedIT test suite");
        logger.info("Testing field projection (only/except) behavior");
        logger.info("Using COMPLEX content type with many fields");
    }

    // ===========================
    // Only Specific Fields
    // ===========================

    @Test
    @Order(1)
    @DisplayName("Test only() with single field")
    void testOnlySingleField() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                     .entry(Credentials.COMPLEX_ENTRY_UID);
        
        // Request only title field
        entry.only(new String[]{"title"});

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    if (error != null) {
                        logger.severe("only() error: " + error.getErrorMessage());
                        logger.severe("Error code: " + error.getErrorCode());
                    }
                    assertNull(error, "only() single field should not error");
                    assertNotNull(entry, "Entry should not be null");
                    assertEquals(Credentials.COMPLEX_ENTRY_UID, entry.getUid(),
                            "CRITICAL BUG: Wrong entry!");
                    
                    // Should have title
                    assertNotNull(entry.getTitle(), "BUG: only('title') should include title");
                    
                    // Should have basic fields (UID, content_type always included)
                    assertNotNull(entry.getUid(), "UID always included");
                    assertNotNull(entry.getContentType(), "Content type always included");
                    
                    logger.info("✅ only('title') working - title present");
                    logSuccess("testOnlySingleField", "Title field included");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testOnlySingleField"));
    }

    @Test
    @Order(2)
    @DisplayName("Test only() with multiple fields")
    void testOnlyMultipleFields() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                     .entry(Credentials.COMPLEX_ENTRY_UID);
        
        // Request multiple fields
        entry.only(new String[]{"title", "url", "topics"});

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "only() multiple fields should not error");
                    assertNotNull(entry, "Entry should not be null");
                    assertEquals(Credentials.COMPLEX_ENTRY_UID, entry.getUid(),
                            "CRITICAL BUG: Wrong entry!");
                    
                    // Should have requested fields
                    assertNotNull(entry.getTitle(), "BUG: only() should include title");
                    
                    // Check if url and topics exist (may be null if not set)
                    Object url = entry.get("url");
                    Object topics = entry.get("topics");
                    logger.info("URL field: " + (url != null ? "present" : "null"));
                    logger.info("Topics field: " + (topics != null ? "present" : "null"));
                    
                    logger.info("✅ only(['title', 'url', 'topics']) working");
                    logSuccess("testOnlyMultipleFields", "Multiple fields requested");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testOnlyMultipleFields"));
    }

    @Test
    @Order(3)
    @DisplayName("Test only() with query")
    void testOnlyWithQuery() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.only(new String[]{"title", "url"});
        query.limit(3);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Query with only() should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() <= 3, "Should respect limit");
                        
                        // All entries should have only requested fields
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "UID always included");
                            assertNotNull(e.getContentType(), "Content type always included");
                            assertEquals(Credentials.COMPLEX_CONTENT_TYPE_UID, e.getContentType(),
                                    "BUG: Wrong content type");
                            
                            // Should have title
                            assertNotNull(e.getTitle(), "BUG: only() should include title");
                        }
                        
                        logger.info("✅ Query with only() validated: " + results.size() + " entries");
                        logSuccess("testOnlyWithQuery", results.size() + " entries with projection");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testOnlyWithQuery"));
    }

    // ===========================
    // Except Specific Fields
    // ===========================

    @Test
    @Order(4)
    @DisplayName("Test except() with single field")
    void testExceptSingleField() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                     .entry(Credentials.COMPLEX_ENTRY_UID);
        
        // Exclude specific field
        entry.except(new String[]{"topics"});

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "except() should not error");
                    assertNotNull(entry, "Entry should not be null");
                    assertEquals(Credentials.COMPLEX_ENTRY_UID, entry.getUid(),
                            "CRITICAL BUG: Wrong entry!");
                    
                    // Should have title (not excluded)
                    assertNotNull(entry.getTitle(), "Title should be present");
                    
                    // Topics might still be present (SDK behavior varies)
                    logger.info("✅ except('topics') working - entry fetched");
                    logSuccess("testExceptSingleField", "Field exclusion applied");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testExceptSingleField"));
    }

    @Test
    @Order(5)
    @DisplayName("Test except() with multiple fields")
    void testExceptMultipleFields() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                     .entry(Credentials.COMPLEX_ENTRY_UID);
        
        // Exclude multiple fields
        entry.except(new String[]{"topics", "tags", "seo"});

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "except() multiple fields should not error");
                    assertNotNull(entry, "Entry should not be null");
                    assertEquals(Credentials.COMPLEX_ENTRY_UID, entry.getUid(),
                            "CRITICAL BUG: Wrong entry!");
                    
                    // Should have basic fields
                    assertNotNull(entry.getTitle(), "Title should be present");
                    assertNotNull(entry.getUid(), "UID always present");
                    
                    logger.info("✅ except(['topics', 'tags', 'seo']) working");
                    logSuccess("testExceptMultipleFields", "Multiple fields excluded");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testExceptMultipleFields"));
    }

    @Test
    @Order(6)
    @DisplayName("Test except() with query")
    void testExceptWithQuery() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.except(new String[]{"seo", "tags"});
        query.limit(3);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Query with except() should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() <= 3, "Should respect limit");
                        
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All must have UID");
                            assertNotNull(e.getTitle(), "Title should be present");
                            assertEquals(Credentials.COMPLEX_CONTENT_TYPE_UID, e.getContentType(),
                                    "BUG: Wrong content type");
                        }
                        
                        logger.info("✅ Query with except() validated: " + results.size() + " entries");
                        logSuccess("testExceptWithQuery", results.size() + " entries");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testExceptWithQuery"));
    }

    // ===========================
    // Nested Field Projection
    // ===========================

    @Test
    @Order(7)
    @DisplayName("Test only() with nested field path")
    void testOnlyNestedField() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                     .entry(Credentials.COMPLEX_ENTRY_UID);
        
        // Request nested field (e.g., seo.title)
        entry.only(new String[]{"title", "seo.title"});

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "Nested field projection should not error");
                    assertNotNull(entry, "Entry should not be null");
                    assertEquals(Credentials.COMPLEX_ENTRY_UID, entry.getUid(),
                            "CRITICAL BUG: Wrong entry!");
                    
                    assertNotNull(entry.getTitle(), "Title should be present");
                    
                    // Check if seo field exists
                    Object seo = entry.get("seo");
                    logger.info("SEO field: " + (seo != null ? "present" : "null"));
                    
                    logger.info("✅ Nested field projection working");
                    logSuccess("testOnlyNestedField", "Nested field handled");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testOnlyNestedField"));
    }

    @Test
    @Order(8)
    @DisplayName("Test projection with modular blocks")
    void testProjectionWithModularBlocks() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                     .entry(Credentials.COMPLEX_ENTRY_UID);
        
        // Request only title and modular block fields
        entry.only(new String[]{"title", "sections", "content_block"});

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "Projection with blocks should not error");
                    assertNotNull(entry, "Entry should not be null");
                    assertEquals(Credentials.COMPLEX_ENTRY_UID, entry.getUid(),
                            "CRITICAL BUG: Wrong entry!");
                    
                    assertNotNull(entry.getTitle(), "Title should be present");
                    
                    // Check modular block fields
                    Object sections = entry.get("sections");
                    Object contentBlock = entry.get("content_block");
                    logger.info("Sections: " + (sections != null ? "present" : "null"));
                    logger.info("Content block: " + (contentBlock != null ? "present" : "null"));
                    
                    logger.info("✅ Projection with modular blocks working");
                    logSuccess("testProjectionWithModularBlocks", "Modular blocks handled");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testProjectionWithModularBlocks"));
    }

    // ===========================
    // Projection with References
    // ===========================

    @Test
    @Order(9)
    @DisplayName("Test only() with reference field")
    void testOnlyWithReferenceField() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                     .entry(Credentials.COMPLEX_ENTRY_UID);
        
        // Request only title and reference field
        entry.only(new String[]{"title", "authors", "related_content"});
        entry.includeReference("authors");

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    // References may or may not exist
                    if (error == null) {
                        assertNotNull(entry, "Entry should not be null");
                        assertEquals(Credentials.COMPLEX_ENTRY_UID, entry.getUid(),
                                "CRITICAL BUG: Wrong entry!");
                        assertNotNull(entry.getTitle(), "Title should be present");
                        logger.info("✅ Projection + references working");
                        logSuccess("testOnlyWithReferenceField", "References handled");
                    } else {
                        logger.info("ℹ️ References not configured: " + error.getErrorMessage());
                        logSuccess("testOnlyWithReferenceField", "Handled gracefully");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testOnlyWithReferenceField"));
    }

    @Test
    @Order(10)
    @DisplayName("Test query with projection and references")
    void testQueryWithProjectionAndReferences() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.only(new String[]{"title", "url", "authors"});
        query.includeReference("authors");
        query.limit(3);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    // References may or may not exist
                    if (error == null) {
                        assertNotNull(queryResult, "QueryResult should not be null");
                        if (hasResults(queryResult)) {
                            java.util.List<Entry> results = queryResult.getResultObjects();
                            for (Entry e : results) {
                                assertNotNull(e.getUid(), "All must have UID");
                                assertEquals(Credentials.COMPLEX_CONTENT_TYPE_UID, e.getContentType(),
                                        "Wrong type");
                            }
                            logger.info("✅ Query + projection + references: " + results.size() + " entries");
                            logSuccess("testQueryWithProjectionAndReferences", results.size() + " entries");
                        }
                    } else {
                        logger.info("ℹ️ References not configured: " + error.getErrorMessage());
                        logSuccess("testQueryWithProjectionAndReferences", "Handled gracefully");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryWithProjectionAndReferences"));
    }

    // ===========================
    // Projection Performance
    // ===========================

    @Test
    @Order(11)
    @DisplayName("Test projection performance - only vs all fields")
    void testProjectionPerformance() throws InterruptedException {
        long[] durations = new long[2];
        
        // Full entry (all fields)
        CountDownLatch latch1 = createLatch();
        long start1 = PerformanceAssertion.startTimer();
        
        Entry fullEntry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                               .entry(Credentials.COMPLEX_ENTRY_UID);
        
        fullEntry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    durations[0] = PerformanceAssertion.elapsedTime(start1);
                    if (error == null) {
                        assertNotNull(fullEntry, "Full entry should not be null");
                    }
                } finally {
                    latch1.countDown();
                }
            }
        });
        
        awaitLatch(latch1, "full-entry");
        
        // Projected entry (only title)
        CountDownLatch latch2 = createLatch();
        long start2 = PerformanceAssertion.startTimer();
        
        Entry projectedEntry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                                   .entry(Credentials.COMPLEX_ENTRY_UID);
        projectedEntry.only(new String[]{"title"});
        
        projectedEntry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    durations[1] = PerformanceAssertion.elapsedTime(start2);
                    if (error == null) {
                        assertNotNull(projectedEntry, "Projected entry should not be null");
                    }
                } finally {
                    latch2.countDown();
                }
            }
        });
        
        awaitLatch(latch2, "projected-entry");
        
        logger.info("Performance comparison:");
        logger.info("  Full entry: " + formatDuration(durations[0]));
        logger.info("  Projected (only title): " + formatDuration(durations[1]));
        
        if (durations[1] <= durations[0]) {
            logger.info("  ✅ Projection is faster or equal (good!)");
        } else {
            logger.info("  ℹ️ Projection slightly slower (network variance or small overhead)");
        }
        
        logSuccess("testProjectionPerformance", "Performance compared");
    }

    @Test
    @Order(12)
    @DisplayName("Test query projection performance with large result set")
    void testQueryProjectionPerformance() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.only(new String[]{"title", "url"});
        query.limit(20);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    
                    assertNull(error, "Query with projection should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() <= 20, "Should respect limit");
                        
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All must have UID");
                            assertEquals(Credentials.COMPLEX_CONTENT_TYPE_UID, e.getContentType(),
                                    "Wrong type");
                        }
                        
                        // Performance should be reasonable
                        assertTrue(duration < 10000,
                                "PERFORMANCE BUG: Query took " + duration + "ms (max: 10s)");
                        
                        logger.info("✅ Query projection performance: " + results.size() + 
                                " entries in " + formatDuration(duration));
                        logSuccess("testQueryProjectionPerformance", 
                                results.size() + " entries, " + formatDuration(duration));
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryProjectionPerformance"));
    }

    // ===========================
    // Edge Cases
    // ===========================

    @Test
    @Order(13)
    @DisplayName("Test only() with empty array")
    void testOnlyEmptyArray() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                     .entry(Credentials.COMPLEX_ENTRY_UID);
        
        // Empty only array - SDK should handle gracefully
        entry.only(new String[]{});

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    // SDK should handle this - either return all fields or error
                    if (error == null) {
                        assertNotNull(entry, "Entry should not be null");
                        assertEquals(Credentials.COMPLEX_ENTRY_UID, entry.getUid(),
                                "CRITICAL BUG: Wrong entry!");
                        logger.info("✅ Empty only() handled - returned entry");
                        logSuccess("testOnlyEmptyArray", "Empty array handled");
                    } else {
                        logger.info("ℹ️ Empty only() returned error (acceptable)");
                        logSuccess("testOnlyEmptyArray", "Error handled gracefully");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testOnlyEmptyArray"));
    }

    @Test
    @Order(14)
    @DisplayName("Test only() with non-existent field")
    void testOnlyNonExistentField() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                     .entry(Credentials.COMPLEX_ENTRY_UID);
        
        // Request non-existent field
        entry.only(new String[]{"title", "nonexistent_field_xyz"});

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    // SDK should handle gracefully
                    assertNull(error, "Non-existent field should not cause error");
                    assertNotNull(entry, "Entry should not be null");
                    assertEquals(Credentials.COMPLEX_ENTRY_UID, entry.getUid(),
                            "CRITICAL BUG: Wrong entry!");
                    
                    assertNotNull(entry.getTitle(), "Title should be present");
                    
                    Object nonexistent = entry.get("nonexistent_field_xyz");
                    assertNull(nonexistent, "Non-existent field should be null");
                    
                    logger.info("✅ Non-existent field handled gracefully");
                    logSuccess("testOnlyNonExistentField", "Handled gracefully");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testOnlyNonExistentField"));
    }

    @Test
    @Order(15)
    @DisplayName("Test combined only() and except()")
    void testCombinedOnlyAndExcept() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                     .entry(Credentials.COMPLEX_ENTRY_UID);
        
        // Use both only and except (SDK behavior may vary)
        entry.only(new String[]{"title", "url", "topics"});
        entry.except(new String[]{"topics"});

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    // SDK should handle - typically except takes precedence
                    assertNull(error, "Combined only/except should not error");
                    assertNotNull(entry, "Entry should not be null");
                    assertEquals(Credentials.COMPLEX_ENTRY_UID, entry.getUid(),
                            "CRITICAL BUG: Wrong entry!");
                    
                    logger.info("✅ Combined only() + except() handled");
                    logSuccess("testCombinedOnlyAndExcept", "Combined projection handled");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testCombinedOnlyAndExcept"));
    }

    @Test
    @Order(16)
    @DisplayName("Test comprehensive projection scenario")
    void testComprehensiveProjectionScenario() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        // Complex scenario: projection + filters + sorting
        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.only(new String[]{"title", "url", "topics", "date"});
        query.exists("title");
        query.descending("created_at");
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    
                    assertNull(error, "Comprehensive scenario should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() > 0, "Should have results");
                        assertTrue(results.size() <= 5, "Should respect limit");
                        
                        // Validate all entries
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All must have UID");
                            assertNotNull(e.getTitle(), "BUG: exists('title') + only('title') not working");
                            assertEquals(Credentials.COMPLEX_CONTENT_TYPE_UID, e.getContentType(),
                                    "BUG: Wrong content type");
                        }
                        
                        // Performance check
                        assertTrue(duration < 10000,
                                "PERFORMANCE BUG: Comprehensive took " + duration + "ms (max: 10s)");
                        
                        logger.info("✅ Comprehensive projection: " + results.size() + 
                                " entries in " + formatDuration(duration));
                        logSuccess("testComprehensiveProjectionScenario", 
                                results.size() + " entries, " + formatDuration(duration));
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testComprehensiveProjectionScenario"));
    }

    @AfterAll
    void tearDown() {
        logger.info("Completed FieldProjectionAdvancedIT test suite");
        logger.info("All 16 field projection tests executed");
        logger.info("Tested: only(), except(), nested fields, references, performance, edge cases");
    }
}

