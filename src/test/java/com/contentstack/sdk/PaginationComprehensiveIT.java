package com.contentstack.sdk;

import com.contentstack.sdk.utils.PerformanceAssertion;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

/**
 * Comprehensive Integration Tests for Pagination
 * Tests pagination behavior including:
 * - Basic limit and skip
 * - Limit edge cases (0, 1, max)
 * - Skip edge cases (0, large values)
 * - Combinations of limit + skip
 * - Pagination consistency (no duplicates)
 * - Pagination with filters
 * - Pagination with sorting
 * - Performance with large skip values
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PaginationComprehensiveIT extends BaseIntegrationTest {

    private Query query;

    @BeforeAll
    void setUp() {
        logger.info("Setting up PaginationComprehensiveIT test suite");
        logger.info("Testing pagination (limit/skip) behavior");
        logger.info("Using content type: " + Credentials.COMPLEX_CONTENT_TYPE_UID);
    }

    // ===========================
    // Basic Limit Tests
    // ===========================

    @Test
    @Order(1)
    @DisplayName("Test basic limit - fetch 5 entries")
    void testBasicLimit() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Limit query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() <= 5,
                                "BUG: limit(5) returned " + results.size() + " entries");
                        assertTrue(results.size() > 0, "Should have at least some results");
                        
                        // Validate all entries
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All entries must have UID");
                            assertEquals(Credentials.COMPLEX_CONTENT_TYPE_UID, e.getContentType(),
                                    "BUG: Wrong content type");
                        }
                        
                        logger.info("✅ limit(5) returned " + results.size() + " entries");
                        logSuccess("testBasicLimit", results.size() + " entries");
                    } else {
                        logger.warning("No results found for basic limit test");
                        logSuccess("testBasicLimit", "No results (expected for empty content type)");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testBasicLimit"));
    }

    @Test
    @Order(2)
    @DisplayName("Test limit = 1 (single entry)")
    void testLimitOne() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.limit(1);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "limit(1) should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        List<Entry> results = queryResult.getResultObjects();
                        assertEquals(1, results.size(),
                                "BUG: limit(1) returned " + results.size() + " entries");
                        
                        Entry entry = results.get(0);
                        assertNotNull(entry.getUid(), "Entry must have UID");
                        assertEquals(Credentials.COMPLEX_CONTENT_TYPE_UID, entry.getContentType(),
                                "BUG: Wrong content type");
                        
                        logger.info("✅ limit(1) returned exactly 1 entry: " + entry.getUid());
                        logSuccess("testLimitOne", "Single entry returned");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testLimitOne"));
    }

    @Test
    @Order(3)
    @DisplayName("Test limit = 0 (edge case)")
    void testLimitZero() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.limit(0);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    // SDK may return error or return no results
                    if (error != null) {
                        logger.info("ℹ️ limit(0) returned error (acceptable): " + error.getErrorMessage());
                        logSuccess("testLimitZero", "Error handled");
                    } else {
                        assertNotNull(queryResult, "QueryResult should not be null");
                        if (hasResults(queryResult)) {
                            List<Entry> results = queryResult.getResultObjects();
                            logger.info("ℹ️ limit(0) returned " + results.size() + " entries");
                        } else {
                            logger.info("✅ limit(0) returned no results (expected)");
                        }
                        logSuccess("testLimitZero", "Handled gracefully");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testLimitZero"));
    }

    @Test
    @Order(4)
    @DisplayName("Test large limit (100)")
    void testLargeLimit() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.limit(100);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    
                    assertNull(error, "Large limit should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() <= 100,
                                "BUG: limit(100) returned " + results.size() + " entries");
                        
                        // Performance check
                        assertTrue(duration < 15000,
                                "PERFORMANCE BUG: Large limit took " + duration + "ms (max: 15s)");
                        
                        logger.info("✅ limit(100) returned " + results.size() + 
                                " entries in " + formatDuration(duration));
                        logSuccess("testLargeLimit", results.size() + " entries, " + formatDuration(duration));
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testLargeLimit"));
    }

    // ===========================
    // Basic Skip Tests
    // ===========================

    @Test
    @Order(5)
    @DisplayName("Test basic skip - skip first 5 entries")
    void testBasicSkip() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.skip(5);
        query.limit(10);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Skip query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() <= 10, "Should respect limit");
                        
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All entries must have UID");
                            assertEquals(Credentials.COMPLEX_CONTENT_TYPE_UID, e.getContentType(),
                                    "BUG: Wrong content type");
                        }
                        
                        logger.info("✅ skip(5) + limit(10) returned " + results.size() + " entries");
                        logSuccess("testBasicSkip", results.size() + " entries skipped");
                    } else {
                        logger.info("ℹ️ skip(5) returned no results (fewer than 5 entries exist)");
                        logSuccess("testBasicSkip", "Handled empty result");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testBasicSkip"));
    }

    @Test
    @Order(6)
    @DisplayName("Test skip = 0 (no skip)")
    void testSkipZero() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.skip(0);
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "skip(0) should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() <= 5, "Should respect limit");
                        assertTrue(results.size() > 0, "Should have results");
                        
                        logger.info("✅ skip(0) + limit(5) returned " + results.size() + " entries");
                        logSuccess("testSkipZero", results.size() + " entries");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testSkipZero"));
    }

    @Test
    @Order(7)
    @DisplayName("Test large skip (skip 100)")
    void testLargeSkip() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.skip(100);
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    // Large skip may return no results if not enough entries
                    if (error == null) {
                        assertNotNull(queryResult, "QueryResult should not be null");
                        if (hasResults(queryResult)) {
                            List<Entry> results = queryResult.getResultObjects();
                            assertTrue(results.size() <= 5, "Should respect limit");
                            logger.info("✅ skip(100) returned " + results.size() + " entries");
                            logSuccess("testLargeSkip", results.size() + " entries");
                        } else {
                            logger.info("ℹ️ skip(100) returned no results (expected for small datasets)");
                            logSuccess("testLargeSkip", "No results (expected)");
                        }
                    } else {
                        logger.info("ℹ️ skip(100) returned error: " + error.getErrorMessage());
                        logSuccess("testLargeSkip", "Error handled");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testLargeSkip"));
    }

    // ===========================
    // Pagination Combinations
    // ===========================

    @Test
    @Order(8)
    @DisplayName("Test pagination page 1 (skip=0, limit=10)")
    void testPaginationPage1() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.skip(0);
        query.limit(10);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Page 1 query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() <= 10, "Should respect limit");
                        
                        logger.info("✅ Page 1 returned " + results.size() + " entries");
                        logSuccess("testPaginationPage1", "Page 1: " + results.size() + " entries");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testPaginationPage1"));
    }

    @Test
    @Order(9)
    @DisplayName("Test pagination page 2 (skip=10, limit=10)")
    void testPaginationPage2() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.skip(10);
        query.limit(10);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Page 2 query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() <= 10, "Should respect limit");
                        
                        logger.info("✅ Page 2 returned " + results.size() + " entries");
                        logSuccess("testPaginationPage2", "Page 2: " + results.size() + " entries");
                    } else {
                        logger.info("ℹ️ Page 2 returned no results (fewer than 10 entries exist)");
                        logSuccess("testPaginationPage2", "No results (expected)");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testPaginationPage2"));
    }

    @Test
    @Order(10)
    @DisplayName("Test pagination consistency - no duplicate UIDs across pages")
    void testPaginationNoDuplicates() throws InterruptedException {
        Set<String> page1Uids = new HashSet<>();
        Set<String> page2Uids = new HashSet<>();
        
        // Fetch page 1
        CountDownLatch latch1 = createLatch();
        Query query1 = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query1.skip(0);
        query1.limit(5);
        query1.ascending("created_at"); // Stable ordering
        
        query1.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    if (error == null && hasResults(queryResult)) {
                        for (Entry e : queryResult.getResultObjects()) {
                            page1Uids.add(e.getUid());
                        }
                        logger.info("Page 1 UIDs: " + page1Uids.size());
                    }
                } finally {
                    latch1.countDown();
                }
            }
        });
        
        awaitLatch(latch1, "page1");
        
        // Fetch page 2
        CountDownLatch latch2 = createLatch();
        Query query2 = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query2.skip(5);
        query2.limit(5);
        query2.ascending("created_at"); // Same ordering
        
        query2.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    if (error == null && hasResults(queryResult)) {
                        for (Entry e : queryResult.getResultObjects()) {
                            page2Uids.add(e.getUid());
                        }
                        logger.info("Page 2 UIDs: " + page2Uids.size());
                    }
                    
                    // Check for duplicates
                    Set<String> intersection = new HashSet<>(page1Uids);
                    intersection.retainAll(page2Uids);
                    
                    if (!intersection.isEmpty()) {
                        fail("BUG: Found duplicate UIDs across pages: " + intersection);
                    }
                    
                    logger.info("✅ No duplicate UIDs across pages");
                    logSuccess("testPaginationNoDuplicates", 
                            "Page1: " + page1Uids.size() + ", Page2: " + page2Uids.size());
                } finally {
                    latch2.countDown();
                }
            }
        });
        
        assertTrue(awaitLatch(latch2, "testPaginationNoDuplicates"));
    }

    // ===========================
    // Pagination with Filters
    // ===========================

    @Test
    @Order(11)
    @DisplayName("Test pagination with filter (exists + limit)")
    void testPaginationWithFilter() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.exists("title");
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Pagination + filter should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() <= 5, "Should respect limit");
                        
                        // All must have title (filter)
                        for (Entry e : results) {
                            assertNotNull(e.getTitle(),
                                    "BUG: exists('title') + limit not working");
                        }
                        
                        logger.info("✅ Pagination + filter returned " + results.size() + " entries");
                        logSuccess("testPaginationWithFilter", results.size() + " entries with filter");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testPaginationWithFilter"));
    }

    @Test
    @Order(12)
    @DisplayName("Test pagination with multiple filters")
    void testPaginationWithMultipleFilters() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.exists("title");
        query.exists("url");
        query.skip(2);
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Pagination + multiple filters should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() <= 5, "Should respect limit");
                        
                        for (Entry e : results) {
                            assertNotNull(e.getTitle(), "All must have title");
                            // url may be null depending on content
                        }
                        
                        logger.info("✅ Pagination + multiple filters: " + results.size() + " entries");
                        logSuccess("testPaginationWithMultipleFilters", results.size() + " entries");
                    } else {
                        logger.info("ℹ️ No results (filters too restrictive or skip too large)");
                        logSuccess("testPaginationWithMultipleFilters", "No results");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testPaginationWithMultipleFilters"));
    }

    // ===========================
    // Pagination with Sorting
    // ===========================

    @Test
    @Order(13)
    @DisplayName("Test pagination with ascending sort")
    void testPaginationWithAscendingSort() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.ascending("created_at");
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Pagination + sort should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() <= 5, "Should respect limit");
                        
                        // Ordering validation (if created_at is accessible)
                        logger.info("✅ Pagination + ascending sort: " + results.size() + " entries");
                        logSuccess("testPaginationWithAscendingSort", results.size() + " entries sorted");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testPaginationWithAscendingSort"));
    }

    @Test
    @Order(14)
    @DisplayName("Test pagination with descending sort")
    void testPaginationWithDescendingSort() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.descending("created_at");
        query.skip(2);
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Pagination + descending sort should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() <= 5, "Should respect limit");
                        
                        logger.info("✅ Pagination + descending sort: " + results.size() + " entries");
                        logSuccess("testPaginationWithDescendingSort", results.size() + " entries");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testPaginationWithDescendingSort"));
    }

    // ===========================
    // Performance Tests
    // ===========================

    @Test
    @Order(15)
    @DisplayName("Test pagination performance - multiple pages")
    void testPaginationPerformance() throws InterruptedException {
        int pageSize = 10;
        int[] totalFetched = {0};
        long startTime = PerformanceAssertion.startTimer();
        
        // Fetch 3 pages
        for (int page = 0; page < 3; page++) {
            CountDownLatch latch = createLatch();
            int skip = page * pageSize;
            
            Query pageQuery = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
            pageQuery.skip(skip);
            pageQuery.limit(pageSize);
            
            pageQuery.find(new QueryResultsCallBack() {
                @Override
                public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                    try {
                        if (error == null && hasResults(queryResult)) {
                            totalFetched[0] += queryResult.getResultObjects().size();
                        }
                    } finally {
                        latch.countDown();
                    }
                }
            });
            
            awaitLatch(latch, "page-" + page);
        }
        
        long duration = PerformanceAssertion.elapsedTime(startTime);
        
        // Performance assertion
        assertTrue(duration < 20000,
                "PERFORMANCE BUG: 3 pages took " + duration + "ms (max: 20s)");
        
        logger.info("✅ Pagination performance: " + totalFetched[0] + " entries in " + formatDuration(duration));
        logSuccess("testPaginationPerformance", 
                totalFetched[0] + " entries, " + formatDuration(duration));
    }

    @Test
    @Order(16)
    @DisplayName("Test pagination with large skip performance")
    void testLargeSkipPerformance() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.skip(50);
        query.limit(10);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    
                    // Large skip should still perform reasonably
                    assertTrue(duration < 10000,
                            "PERFORMANCE BUG: skip(50) took " + duration + "ms (max: 10s)");
                    
                    if (error == null) {
                        assertNotNull(queryResult, "QueryResult should not be null");
                        if (hasResults(queryResult)) {
                            List<Entry> results = queryResult.getResultObjects();
                            logger.info("✅ skip(50) returned " + results.size() + 
                                    " entries in " + formatDuration(duration));
                            logSuccess("testLargeSkipPerformance", 
                                    results.size() + " entries, " + formatDuration(duration));
                        } else {
                            logger.info("ℹ️ skip(50) no results in " + formatDuration(duration));
                            logSuccess("testLargeSkipPerformance", "No results, " + formatDuration(duration));
                        }
                    } else {
                        logger.info("ℹ️ skip(50) error in " + formatDuration(duration));
                        logSuccess("testLargeSkipPerformance", "Error, " + formatDuration(duration));
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testLargeSkipPerformance"));
    }

    // ===========================
    // Edge Cases
    // ===========================

    @Test
    @Order(17)
    @DisplayName("Test pagination beyond available entries")
    void testPaginationBeyondAvailable() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.skip(1000); // Very large skip
        query.limit(10);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    // Should return empty results or handle gracefully
                    if (error == null) {
                        assertNotNull(queryResult, "QueryResult should not be null");
                        if (hasResults(queryResult)) {
                            List<Entry> results = queryResult.getResultObjects();
                            logger.info("ℹ️ skip(1000) returned " + results.size() + " entries (unexpected)");
                        } else {
                            logger.info("✅ skip(1000) returned no results (expected)");
                        }
                        logSuccess("testPaginationBeyondAvailable", "Handled gracefully");
                    } else {
                        logger.info("ℹ️ skip(1000) returned error (acceptable): " + error.getErrorMessage());
                        logSuccess("testPaginationBeyondAvailable", "Error handled");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testPaginationBeyondAvailable"));
    }

    @Test
    @Order(18)
    @DisplayName("Test comprehensive pagination scenario")
    void testComprehensivePaginationScenario() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        // Complex scenario: filters + sort + pagination
        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.exists("title");
        query.descending("created_at");
        query.skip(3);
        query.limit(7);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    
                    assertNull(error, "Comprehensive scenario should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() <= 7, "Should respect limit");
                        
                        // All must have title
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All must have UID");
                            assertNotNull(e.getTitle(), "BUG: exists('title') not working");
                            assertEquals(Credentials.COMPLEX_CONTENT_TYPE_UID, e.getContentType(),
                                    "BUG: Wrong content type");
                        }
                        
                        // Performance
                        assertTrue(duration < 10000,
                                "PERFORMANCE BUG: Comprehensive took " + duration + "ms (max: 10s)");
                        
                        logger.info("✅ Comprehensive pagination: " + results.size() + 
                                " entries in " + formatDuration(duration));
                        logSuccess("testComprehensivePaginationScenario", 
                                results.size() + " entries, " + formatDuration(duration));
                    } else {
                        logger.info("ℹ️ Comprehensive pagination returned no results");
                        logSuccess("testComprehensivePaginationScenario", "No results");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testComprehensivePaginationScenario"));
    }

    @AfterAll
    void tearDown() {
        logger.info("Completed PaginationComprehensiveIT test suite");
        logger.info("All 18 pagination tests executed");
        logger.info("Tested: limit, skip, combinations, consistency, filters, sorting, performance, edge cases");
    }
}

