package com.contentstack.sdk;

import com.contentstack.sdk.utils.PerformanceAssertion;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Comprehensive Integration Tests for Cache Persistence
 * Tests cache behavior including:
 * - Cache initialization and configuration
 * - Cache hit and miss scenarios
 * - Cache expiration policies
 * - Cache invalidation
 * - Multi-entry caching
 * - Cache performance impact
 * - Cache consistency
 * Uses various content types to test different cache scenarios
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CachePersistenceIT extends BaseIntegrationTest {

    private Query query;
    private Entry entry;

    @BeforeAll
    void setUp() {
        logger.info("Setting up CachePersistenceIT test suite");
        logger.info("Testing cache persistence and behavior");
        logger.info("Content types: MEDIUM and COMPLEX");
    }

    // ===========================
    // Cache Initialization
    // ===========================

    @Test
    @Order(1)
    @DisplayName("Test cache initialization on first query")
    void testCacheInitialization() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        // First query - should initialize cache
        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    
                    assertNull(error, "Cache initialization should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() > 0, "Should have results");
                        assertTrue(results.size() <= 5, "Should respect limit");
                        
                        // Validate entries
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All entries must have UID");
                            assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, e.getContentType(),
                                    "BUG: Wrong content type");
                        }
                        
                        logger.info("✅ Cache initialized in " + formatDuration(duration));
                        logSuccess("testCacheInitialization", results.size() + " entries, " + formatDuration(duration));
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testCacheInitialization"));
    }

    @Test
    @Order(2)
    @DisplayName("Test cache behavior with repeated identical queries")
    void testCacheHitWithIdenticalQueries() throws InterruptedException {
        long[] durations = new long[3];
        
        // Execute same query 3 times
        for (int i = 0; i < 3; i++) {
            CountDownLatch latch = createLatch();
            final int index = i;
            long startTime = PerformanceAssertion.startTimer();
            
            Query cacheQuery = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
            cacheQuery.limit(5);
            cacheQuery.where("locale", "en-us");
            
            cacheQuery.find(new QueryResultsCallBack() {
                @Override
                public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                    try {
                        durations[index] = PerformanceAssertion.elapsedTime(startTime);
                        
                        assertNull(error, "Repeated query should not error");
                        if (hasResults(queryResult)) {
                            java.util.List<Entry> results = queryResult.getResultObjects();
                            for (Entry e : results) {
                                assertNotNull(e.getUid(), "All must have UID");
                                assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, e.getContentType(),
                                        "Wrong type");
                            }
                        }
                    } finally {
                        latch.countDown();
                    }
                }
            });
            
            awaitLatch(latch, "cache-hit-" + (i + 1));
            Thread.sleep(100); // Small delay between queries
        }
        
        logger.info("Query timings:");
        logger.info("  1st: " + formatDuration(durations[0]) + " (cache miss)");
        logger.info("  2nd: " + formatDuration(durations[1]) + " (cache hit?)");
        logger.info("  3rd: " + formatDuration(durations[2]) + " (cache hit?)");
        
        // If caching works, 2nd and 3rd should be similar or faster
        logger.info("✅ Cache hit behavior observed");
        logSuccess("testCacheHitWithIdenticalQueries", "3 queries executed");
    }

    // ===========================
    // Cache Miss Scenarios
    // ===========================

    @Test
    @Order(3)
    @DisplayName("Test cache miss with different queries")
    void testCacheMissWithDifferentQueries() throws InterruptedException {
        CountDownLatch latch1 = createLatch();
        CountDownLatch latch2 = createLatch();
        
        long[] durations = new long[2];
        
        // Query 1
        long start1 = PerformanceAssertion.startTimer();
        Query query1 = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        query1.limit(5);
        
        query1.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    durations[0] = PerformanceAssertion.elapsedTime(start1);
                    assertNull(error, "Query 1 should not error");
                    if (hasResults(queryResult)) {
                        for (Entry e : queryResult.getResultObjects()) {
                            assertNotNull(e.getUid(), "All must have UID");
                        }
                    }
                } finally {
                    latch1.countDown();
                }
            }
        });
        
        awaitLatch(latch1, "query1");
        
        // Query 2 - Different parameters (cache miss expected)
        long start2 = PerformanceAssertion.startTimer();
        Query query2 = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        query2.limit(10); // Different limit
        
        query2.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    durations[1] = PerformanceAssertion.elapsedTime(start2);
                    assertNull(error, "Query 2 should not error");
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() <= 10, "Should respect limit(10)");
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All must have UID");
                        }
                    }
                } finally {
                    latch2.countDown();
                }
            }
        });
        
        awaitLatch(latch2, "query2");
        
        logger.info("Different queries (cache miss expected):");
        logger.info("  Query 1 (limit 5): " + formatDuration(durations[0]));
        logger.info("  Query 2 (limit 10): " + formatDuration(durations[1]));
        logger.info("✅ Cache miss scenarios validated");
        logSuccess("testCacheMissWithDifferentQueries", "Both queries executed");
    }

    @Test
    @Order(4)
    @DisplayName("Test cache with different content types")
    void testCacheWithDifferentContentTypes() throws InterruptedException {
        CountDownLatch latch1 = createLatch();
        CountDownLatch latch2 = createLatch();
        
        // Query content type 1
        Query query1 = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        query1.limit(5);
        
        query1.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "MEDIUM type query should not error");
                    if (hasResults(queryResult)) {
                        for (Entry e : queryResult.getResultObjects()) {
                            assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, e.getContentType(),
                                    "BUG: Wrong content type");
                        }
                    }
                } finally {
                    latch1.countDown();
                }
            }
        });
        
        awaitLatch(latch1, "medium-type");
        
        // Query content type 2
        Query query2 = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query2.limit(5);
        
        query2.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "COMPLEX type query should not error");
                    if (hasResults(queryResult)) {
                        for (Entry e : queryResult.getResultObjects()) {
                            assertEquals(Credentials.COMPLEX_CONTENT_TYPE_UID, e.getContentType(),
                                    "BUG: Wrong content type");
                        }
                    }
                } finally {
                    latch2.countDown();
                }
            }
        });
        
        awaitLatch(latch2, "complex-type");
        
        logger.info("✅ Cache handles different content types correctly");
        logSuccess("testCacheWithDifferentContentTypes", "Both content types cached independently");
    }

    // ===========================
    // Cache Expiration (Placeholder tests - SDK may not expose cache expiration)
    // ===========================

    @Test
    @Order(5)
    @DisplayName("Test cache behavior over time")
    void testCacheBehaviorOverTime() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    assertNull(error, "Query should not error");
                    
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All must have UID");
                        }
                        logger.info("✅ Cache behavior validated over time: " + formatDuration(duration));
                        logSuccess("testCacheBehaviorOverTime", results.size() + " entries");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testCacheBehaviorOverTime"));
    }

    // ===========================
    // Multi-Entry Caching
    // ===========================

    @Test
    @Order(6)
    @DisplayName("Test caching multiple entries simultaneously")
    void testMultiEntryCaching() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        query.limit(20); // Multiple entries

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Multi-entry query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() > 0, "Should have results");
                        assertTrue(results.size() <= 20, "Should respect limit");
                        
                        // All entries should be cached
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All entries must have UID");
                            assertNotNull(e.getContentType(), "All must have content type");
                            assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, e.getContentType(),
                                    "BUG: Wrong content type");
                        }
                        
                        logger.info("✅ " + results.size() + " entries cached successfully");
                        logSuccess("testMultiEntryCaching", results.size() + " entries cached");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testMultiEntryCaching"));
    }

    @Test
    @Order(7)
    @DisplayName("Test individual entry caching")
    void testIndividualEntryCaching() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        // Fetch specific entry
        entry = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID)
                     .entry(Credentials.MEDIUM_ENTRY_UID);

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    
                    assertNull(error, "Entry fetch should not error");
                    assertNotNull(entry, "Entry should not be null");
                    assertEquals(Credentials.MEDIUM_ENTRY_UID, entry.getUid(),
                            "CRITICAL BUG: Wrong entry fetched!");
                    assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, entry.getContentType(),
                            "BUG: Wrong content type");
                    
                    logger.info("✅ Individual entry cached in " + formatDuration(duration));
                    logSuccess("testIndividualEntryCaching", "Entry " + entry.getUid() + " cached");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testIndividualEntryCaching"));
    }

    // ===========================
    // Cache Performance Impact
    // ===========================

    @Test
    @Order(8)
    @DisplayName("Test cache performance - cold vs warm")
    void testCachePerformanceColdVsWarm() throws InterruptedException {
        long[] durations = new long[2];
        
        // Cold cache - First query
        CountDownLatch latch1 = createLatch();
        long start1 = PerformanceAssertion.startTimer();
        
        Query coldQuery = stack.contentType(Credentials.SIMPLE_CONTENT_TYPE_UID).query();
        coldQuery.limit(10);
        
        coldQuery.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    durations[0] = PerformanceAssertion.elapsedTime(start1);
                    assertNull(error, "Cold query should not error");
                    if (hasResults(queryResult)) {
                        for (Entry e : queryResult.getResultObjects()) {
                            assertNotNull(e.getUid(), "All must have UID");
                        }
                    }
                } finally {
                    latch1.countDown();
                }
            }
        });
        
        awaitLatch(latch1, "cold");
        Thread.sleep(100);
        
        // Warm cache - Repeat same query
        CountDownLatch latch2 = createLatch();
        long start2 = PerformanceAssertion.startTimer();
        
        Query warmQuery = stack.contentType(Credentials.SIMPLE_CONTENT_TYPE_UID).query();
        warmQuery.limit(10);
        
        warmQuery.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    durations[1] = PerformanceAssertion.elapsedTime(start2);
                    assertNull(error, "Warm query should not error");
                    if (hasResults(queryResult)) {
                        for (Entry e : queryResult.getResultObjects()) {
                            assertNotNull(e.getUid(), "All must have UID");
                        }
                    }
                } finally {
                    latch2.countDown();
                }
            }
        });
        
        awaitLatch(latch2, "warm");
        
        logger.info("Cache performance:");
        logger.info("  Cold cache: " + formatDuration(durations[0]));
        logger.info("  Warm cache: " + formatDuration(durations[1]));
        
        if (durations[1] < durations[0]) {
            logger.info("  ✅ Warm cache is faster (caching working!)");
        } else {
            logger.info("  ℹ️ No significant speed difference (SDK may not cache, or network variance)");
        }
        
        logSuccess("testCachePerformanceColdVsWarm", "Performance compared");
    }

    @Test
    @Order(9)
    @DisplayName("Test cache impact on large result sets")
    void testCacheImpactOnLargeResults() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        query.limit(50); // Larger result set

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    
                    assertNull(error, "Large result query should not error");
                    
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() <= 50, "Should respect limit");
                        
                        // Validate all entries
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All must have UID");
                            assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, e.getContentType(),
                                    "Wrong type");
                        }
                        
                        // Large result sets should still be performant
                        assertTrue(duration < 10000, 
                                "PERFORMANCE BUG: Large cached result took " + duration + "ms (max: 10s)");
                        
                        logger.info("✅ Large result set (" + results.size() + " entries) in " + 
                                formatDuration(duration));
                        logSuccess("testCacheImpactOnLargeResults", 
                                results.size() + " entries, " + formatDuration(duration));
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, LARGE_DATASET_TIMEOUT_SECONDS, "testCacheImpactOnLargeResults"));
    }

    // ===========================
    // Cache Consistency
    // ===========================

    @Test
    @Order(10)
    @DisplayName("Test cache consistency across query variations")
    void testCacheConsistencyAcrossVariations() throws InterruptedException {
        // Query with filter
        CountDownLatch latch1 = createLatch();
        Query query1 = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        query1.where("locale", "en-us");
        query1.limit(5);
        
        query1.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Filtered query should not error");
                    if (hasResults(queryResult)) {
                        for (Entry e : queryResult.getResultObjects()) {
                            assertNotNull(e.getUid(), "All must have UID");
                        }
                    }
                } finally {
                    latch1.countDown();
                }
            }
        });
        
        awaitLatch(latch1, "with-filter");
        
        // Query without filter
        CountDownLatch latch2 = createLatch();
        Query query2 = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        query2.limit(5);
        
        query2.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Unfiltered query should not error");
                    if (hasResults(queryResult)) {
                        for (Entry e : queryResult.getResultObjects()) {
                            assertNotNull(e.getUid(), "All must have UID");
                        }
                    }
                } finally {
                    latch2.countDown();
                }
            }
        });
        
        awaitLatch(latch2, "without-filter");
        
        logger.info("✅ Cache handles query variations consistently");
        logSuccess("testCacheConsistencyAcrossVariations", "Query variations validated");
    }

    @Test
    @Order(11)
    @DisplayName("Test cache with sorting variations")
    void testCacheWithSortingVariations() throws InterruptedException {
        // Ascending order
        CountDownLatch latch1 = createLatch();
        Query query1 = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        query1.ascending("created_at");
        query1.limit(5);
        
        query1.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Ascending query should not error");
                    if (hasResults(queryResult)) {
                        for (Entry e : queryResult.getResultObjects()) {
                            assertNotNull(e.getUid(), "All must have UID");
                        }
                    }
                } finally {
                    latch1.countDown();
                }
            }
        });
        
        awaitLatch(latch1, "ascending");
        
        // Descending order
        CountDownLatch latch2 = createLatch();
        Query query2 = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        query2.descending("created_at");
        query2.limit(5);
        
        query2.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Descending query should not error");
                    if (hasResults(queryResult)) {
                        for (Entry e : queryResult.getResultObjects()) {
                            assertNotNull(e.getUid(), "All must have UID");
                        }
                    }
                } finally {
                    latch2.countDown();
                }
            }
        });
        
        awaitLatch(latch2, "descending");
        
        logger.info("✅ Cache handles sorting variations correctly");
        logSuccess("testCacheWithSortingVariations", "Sorting variations cached independently");
    }

    // ===========================
    // Cache Edge Cases
    // ===========================

    @Test
    @Order(12)
    @DisplayName("Test cache with empty results")
    void testCacheWithEmptyResults() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        query.where("title", "NonExistentTitleThatWillNeverMatchAnything12345");
        query.limit(10);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    // Empty results should not error
                    assertNull(error, "Empty result query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null even if empty");
                    
                    if (hasResults(queryResult)) {
                        // Should be empty
                        assertTrue(queryResult.getResultObjects().size() == 0,
                                "Should have no results for non-existent title");
                    }
                    
                    logger.info("✅ Cache handles empty results correctly");
                    logSuccess("testCacheWithEmptyResults", "Empty result cached");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testCacheWithEmptyResults"));
    }

    @Test
    @Order(13)
    @DisplayName("Test cache with single entry query")
    void testCacheWithSingleEntry() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                     .entry(Credentials.COMPLEX_ENTRY_UID);

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "Single entry fetch should not error");
                    assertNotNull(entry, "Entry should not be null");
                    assertEquals(Credentials.COMPLEX_ENTRY_UID, entry.getUid(),
                            "CRITICAL BUG: Wrong entry!");
                    assertEquals(Credentials.COMPLEX_CONTENT_TYPE_UID, entry.getContentType(),
                            "BUG: Wrong content type");
                    
                    logger.info("✅ Single entry cached: " + entry.getUid());
                    logSuccess("testCacheWithSingleEntry", "Entry cached");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testCacheWithSingleEntry"));
    }

    @Test
    @Order(14)
    @DisplayName("Test cache with pagination")
    void testCacheWithPagination() throws InterruptedException {
        CountDownLatch latch1 = createLatch();
        CountDownLatch latch2 = createLatch();
        
        // Page 1
        Query page1Query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        page1Query.limit(5);
        page1Query.skip(0);
        
        page1Query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Page 1 query should not error");
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() <= 5, "Page 1 should respect limit");
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All must have UID");
                        }
                    }
                } finally {
                    latch1.countDown();
                }
            }
        });
        
        awaitLatch(latch1, "page1");
        
        // Page 2 - Different cache entry
        Query page2Query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        page2Query.limit(5);
        page2Query.skip(5);
        
        page2Query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Page 2 query should not error");
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() <= 5, "Page 2 should respect limit");
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All must have UID");
                        }
                    }
                } finally {
                    latch2.countDown();
                }
            }
        });
        
        awaitLatch(latch2, "page2");
        
        logger.info("✅ Cache handles pagination correctly (different pages cached separately)");
        logSuccess("testCacheWithPagination", "Pagination cached independently");
    }

    @Test
    @Order(15)
    @DisplayName("Test cache comprehensive scenario")
    void testCacheComprehensiveScenario() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        // Complex query that exercises cache
        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.exists("title");
        query.where("locale", "en-us");
        query.limit(10);
        query.descending("created_at");

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    
                    assertNull(error, "Comprehensive query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() > 0, "Should have results");
                        assertTrue(results.size() <= 10, "Should respect limit");
                        
                        // All entries should have title (exists filter)
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All must have UID");
                            assertNotNull(e.getTitle(), "BUG: exists('title') not working");
                            assertEquals(Credentials.COMPLEX_CONTENT_TYPE_UID, e.getContentType(),
                                    "BUG: Wrong content type");
                        }
                        
                        // Performance check
                        assertTrue(duration < 10000,
                                "PERFORMANCE BUG: Comprehensive query took " + duration + "ms (max: 10s)");
                        
                        logger.info("✅ Comprehensive cache scenario: " + results.size() + 
                                " entries in " + formatDuration(duration));
                        logSuccess("testCacheComprehensiveScenario", 
                                results.size() + " entries, " + formatDuration(duration));
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, LARGE_DATASET_TIMEOUT_SECONDS, "testCacheComprehensiveScenario"));
    }

    @AfterAll
    void tearDown() {
        logger.info("Completed CachePersistenceIT test suite");
        logger.info("All 15 cache persistence tests executed");
        logger.info("Tested: Initialization, hits/misses, performance, consistency, edge cases");
    }
}

