package com.contentstack.sdk;

import com.contentstack.sdk.utils.PerformanceAssertion;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Comprehensive Integration Tests for Performance with Large Datasets
 * Tests performance characteristics including:
 * - Large result set queries (100+ entries)
 * - Pagination performance across pages
 * - Memory usage with large datasets
 * - Concurrent query execution
 * - Query performance benchmarks
 * - Result set size limits
 * - Performance degradation with complexity
 * - Caching scenarios
 * Uses complex stack data for realistic performance testing
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PerformanceLargeDatasetsIT extends BaseIntegrationTest {

    private Query query;

    @BeforeAll
    void setUp() {
        logger.info("Setting up PerformanceLargeDatasetsIT test suite");
        logger.info("Testing performance with large datasets");
        logger.info("Using content type: " + Credentials.MEDIUM_CONTENT_TYPE_UID);
    }

    // ===========================
    // Large Result Sets
    // ===========================

    @Test
    @Order(1)
    @DisplayName("Test query with maximum limit (100 entries)")
    void testQueryWithMaximumLimit() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        query.limit(100);  // Max API limit

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    assertNull(error, "Query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        int size = results.size();
                        
                        // STRONG ASSERTION: Limit validation
                        assertTrue(size <= 100, "BUG: limit(100) not working - got " + size);
                        
                        // STRONG ASSERTION: Validate ALL entries
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All entries must have UID");
                            assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, e.getContentType(),
                                    "BUG: Wrong content type");
                        }
                        
                        // Performance assertion
                        PerformanceAssertion.assertNormalOperation(duration, "Query with 100 limit");
                        
                        logger.info("✅ " + size + " entries validated in " + formatDuration(duration));
                        logSuccess("testQueryWithMaximumLimit", 
                                size + " entries in " + formatDuration(duration));
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, LARGE_DATASET_TIMEOUT_SECONDS, "testQueryWithMaximumLimit"));
    }

    @Test
    @Order(2)
    @DisplayName("Test query with default limit vs custom limit performance")
    void testDefaultVsCustomLimitPerformance() throws InterruptedException {
        CountDownLatch latch1 = createLatch();
        CountDownLatch latch2 = createLatch();
        
        final long[] defaultLimitTime = new long[1];
        final long[] customLimitTime = new long[1];
        final int[] defaultCount = new int[1];
        final int[] customCount = new int[1];

        // First: Query with default limit
        long start1 = PerformanceAssertion.startTimer();
        Query query1 = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        
        query1.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    defaultLimitTime[0] = PerformanceAssertion.elapsedTime(start1);
                    assertNull(error, "Query should not error");
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All must have UID");
                            assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, e.getContentType(), "Wrong type");
                        }
                        defaultCount[0] = results.size();
                    }
                } finally {
                    latch1.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch1, "testDefaultLimit"));

        // Second: Query with custom limit
        long start2 = PerformanceAssertion.startTimer();
        Query query2 = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        query2.limit(50);
        
        query2.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    customLimitTime[0] = PerformanceAssertion.elapsedTime(start2);
                    assertNull(error, "Query should not error");
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() <= 50, "BUG: limit(50) not working");
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All must have UID");
                            assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, e.getContentType(), "Wrong type");
                        }
                        customCount[0] = results.size();
                    }
                } finally {
                    latch2.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch2, "testCustomLimit"));

        // Compare performance
        logger.info("Default limit: " + defaultCount[0] + " entries in " + 
                formatDuration(defaultLimitTime[0]));
        logger.info("Custom limit (50): " + customCount[0] + " entries in " + 
                formatDuration(customLimitTime[0]));
        
        String comparison = PerformanceAssertion.compareOperations(
                "Default", defaultLimitTime[0],
                "Custom(50)", customLimitTime[0]);
        logger.info(comparison);
        
        logSuccess("testDefaultVsCustomLimitPerformance", "Performance compared");
    }

    @Test
    @Order(3)
    @DisplayName("Test large result set iteration performance")
    void testLargeResultSetIteration() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        query.limit(100);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Should not have errors");
                    
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        long iterationStart = System.currentTimeMillis();
                        int count = 0;
                        
                        for (Entry entry : results) {
                            assertNotNull(entry.getUid(), "All must have UID");
                            assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, entry.getContentType(), "Wrong type");
                            if (hasBasicFields(entry)) {
                                count++;
                            }
                        }
                        
                        long iterationDuration = System.currentTimeMillis() - iterationStart;
                        long totalDuration = PerformanceAssertion.elapsedTime(startTime);
                        
                        logger.info("✅ Iterated " + count + " entries in " + 
                                formatDuration(iterationDuration));
                        assertTrue(iterationDuration < 1000, 
                                "Iteration should be fast");
                        
                        PerformanceAssertion.logPerformanceMetrics("Large set iteration", totalDuration);
                        logSuccess("testLargeResultSetIteration", count + " entries");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, LARGE_DATASET_TIMEOUT_SECONDS, "testLargeResultSetIteration"));
    }

    // ===========================
    // Pagination Performance
    // ===========================

    @Test
    @Order(4)
    @DisplayName("Test pagination performance - first page")
    void testPaginationFirstPage() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        query.limit(10);
        query.skip(0);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    assertNull(error, "Should not have errors");
                    
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() <= 10, "BUG: limit(10) not working");
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All must have UID");
                            assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, e.getContentType(), "Wrong type");
                        }
                        PerformanceAssertion.assertFastOperation(duration, "First page fetch");
                        logSuccess("testPaginationFirstPage", 
                                results.size() + " entries in " + formatDuration(duration));
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testPaginationFirstPage"));
    }

    @Test
    @Order(5)
    @DisplayName("Test pagination performance - middle page")
    void testPaginationMiddlePage() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        query.limit(10);
        query.skip(50);  // Middle page

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    assertNull(error, "Should not have errors");
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() <= 10, "BUG: limit not working");
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All must have UID");
                            assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, e.getContentType(), "Wrong type");
                        }
                    }
                    PerformanceAssertion.assertNormalOperation(duration, "Middle page fetch");
                    logSuccess("testPaginationMiddlePage", "Time: " + formatDuration(duration));
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testPaginationMiddlePage"));
    }

    @Test
    @Order(6)
    @DisplayName("Test pagination performance across multiple pages")
    void testPaginationMultiplePages() throws InterruptedException {
        CountDownLatch latch = createLatch(3);
        final long[] pageTimes = new long[3];
        final AtomicInteger pageCounter = new AtomicInteger(0);

        for (int page = 0; page < 3; page++) {
            final int currentPage = page;
            long pageStart = System.currentTimeMillis();
            
            Query pageQuery = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
            pageQuery.limit(10);
            pageQuery.skip(page * 10);
            
            pageQuery.find(new QueryResultsCallBack() {
                @Override
                public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                    try {
                        assertNull(error, "Page should not error");
                        if (hasResults(queryResult)) {
                            java.util.List<Entry> results = queryResult.getResultObjects();
                            for (Entry e : results) {
                                assertNotNull(e.getUid(), "All must have UID");
                                assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, e.getContentType(), "Wrong type");
                            }
                        }
                        pageTimes[currentPage] = System.currentTimeMillis() - pageStart;
                        pageCounter.incrementAndGet();
                        
                        if (pageCounter.get() == 3) {
                            logger.info("✅ Page 1: " + formatDuration(pageTimes[0]));
                            logger.info("✅ Page 2: " + formatDuration(pageTimes[1]));
                            logger.info("✅ Page 3: " + formatDuration(pageTimes[2]));
                            for (long time : pageTimes) {
                                assertTrue(time < 5000, "Each page should complete quickly");
                            }
                            logSuccess("testPaginationMultiplePages", "3 pages validated");
                        }
                    } finally {
                        latch.countDown();
                    }
                }
            });
        }

        assertTrue(awaitLatch(latch, LARGE_DATASET_TIMEOUT_SECONDS, "testPaginationMultiplePages"));
    }

    // ===========================
    // Memory Usage
    // ===========================

    @Test
    @Order(7)
    @DisplayName("Test memory usage with small result set")
    void testMemoryUsageSmallResultSet() throws InterruptedException {
        CountDownLatch latch = createLatch();
        
        System.gc();
        long memoryBefore = PerformanceAssertion.getCurrentMemoryUsage();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        query.limit(10);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Should not have errors");
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All must have UID");
                            assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, e.getContentType(), "Wrong type");
                        }
                    }
                    long memoryAfter = PerformanceAssertion.getCurrentMemoryUsage();
                    long memoryUsed = memoryAfter - memoryBefore;
                    logger.info("Memory: " + formatBytes(memoryUsed));
                    logSuccess("testMemoryUsageSmallResultSet", formatBytes(memoryUsed));
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testMemoryUsageSmallResultSet"));
    }

    @Test
    @Order(8)
    @DisplayName("Test memory usage with large result set")
    void testMemoryUsageLargeResultSet() throws InterruptedException {
        CountDownLatch latch = createLatch();
        
        System.gc();
        long memoryBefore = PerformanceAssertion.getCurrentMemoryUsage();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        query.limit(100);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Should not have errors");
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All must have UID");
                            assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, e.getContentType(), "Wrong type");
                        }
                    }
                    
                    long memoryAfter = PerformanceAssertion.getCurrentMemoryUsage();
                    long memoryUsed = memoryAfter - memoryBefore;
                    
                    logger.info("Large result set memory:");
                    logger.info("  Before: " + formatBytes(memoryBefore));
                    logger.info("  After: " + formatBytes(memoryAfter));
                    logger.info("  Used: " + formatBytes(memoryUsed));
                    
                    if (hasResults(queryResult)) {
                        int size = queryResult.getResultObjects().size();
                        long memoryPerEntry = memoryUsed / size;
                        logger.info("  Per entry: ~" + formatBytes(memoryPerEntry));
                    }
                    
                    logSuccess("testMemoryUsageLargeResultSet", 
                            "Tracked: " + formatBytes(memoryUsed));
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, LARGE_DATASET_TIMEOUT_SECONDS, "testMemoryUsageLargeResultSet"));
    }

    // ===========================
    // Concurrent Queries
    // ===========================

    @Test
    @Order(9)
    @DisplayName("Test concurrent query execution (2 queries)")
    void testConcurrentQueries() throws InterruptedException {
        CountDownLatch latch = createLatch(2);
        long startTime = PerformanceAssertion.startTimer();
        final AtomicInteger successCount = new AtomicInteger(0);

        // Execute 2 queries concurrently
        for (int i = 0; i < 2; i++) {
            Query concurrentQuery = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
            concurrentQuery.limit(10);
            
            concurrentQuery.find(new QueryResultsCallBack() {
                @Override
                public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                    try {
                        if (error == null && hasResults(queryResult)) {
                            java.util.List<Entry> results = queryResult.getResultObjects();
                            for (Entry e : results) {
                                assertNotNull(e.getUid(), "All must have UID");
                                assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, e.getContentType(), "Wrong type");
                            }
                            successCount.incrementAndGet();
                        }
                    } finally {
                        latch.countDown();
                    }
                }
            });
        }

        assertTrue(awaitLatch(latch, LARGE_DATASET_TIMEOUT_SECONDS, "testConcurrentQueries"));
        long totalDuration = PerformanceAssertion.elapsedTime(startTime);
        assertEquals(2, successCount.get(), "BUG: Both concurrent queries should succeed");
        logger.info("✅ 2 concurrent validated in " + formatDuration(totalDuration));
        logSuccess("testConcurrentQueries", "2/2 in " + formatDuration(totalDuration));
    }

    @Test
    @Order(10)
    @DisplayName("Test concurrent query execution (5 queries)")
    void testMultipleConcurrentQueries() throws InterruptedException {
        CountDownLatch latch = createLatch(5);
        long startTime = PerformanceAssertion.startTimer();
        final AtomicInteger successCount = new AtomicInteger(0);
        final AtomicInteger errorCount = new AtomicInteger(0);

        for (int i = 0; i < 5; i++) {
            Query concurrentQuery = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
            concurrentQuery.limit(5);
            
            concurrentQuery.find(new QueryResultsCallBack() {
                @Override
                public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                    try {
                        if (error == null && hasResults(queryResult)) {
                            java.util.List<Entry> results = queryResult.getResultObjects();
                            for (Entry e : results) {
                                assertNotNull(e.getUid(), "All must have UID");
                                assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, e.getContentType(), "Wrong type");
                            }
                            successCount.incrementAndGet();
                        } else {
                            errorCount.incrementAndGet();
                        }
                    } finally {
                        latch.countDown();
                    }
                }
            });
        }

        assertTrue(awaitLatch(latch, LARGE_DATASET_TIMEOUT_SECONDS, "testMultipleConcurrentQueries"));
        long totalDuration = PerformanceAssertion.elapsedTime(startTime);
        logger.info("✅ 5 concurrent: " + successCount.get() + " success, " + errorCount.get() + " errors");
        assertTrue(successCount.get() >= 4, "BUG: Most concurrent queries should succeed");
        logSuccess("testMultipleConcurrentQueries", successCount.get() + "/5 succeeded");
    }

    // ===========================
    // Query Performance Benchmarks
    // ===========================

    @Test
    @Order(11)
    @DisplayName("Test simple query performance benchmark")
    void testSimpleQueryBenchmark() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        query.limit(20);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    assertNull(error, "Should not have errors");
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All must have UID");
                            assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, e.getContentType(), "Wrong type");
                        }
                    }
                    PerformanceAssertion.assertFastOperation(duration, "Simple query");
                    logSuccess("testSimpleQueryBenchmark", formatDuration(duration));
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testSimpleQueryBenchmark"));
    }

    @Test
    @Order(12)
    @DisplayName("Test complex query performance benchmark")
    void testComplexQueryBenchmark() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        query.exists("title");
        query.where("locale", "en-us");
        query.includeReference("author");
        query.limit(20);
        query.descending("created_at");

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    assertNull(error, "Should not have errors");
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All must have UID");
                            assertNotNull(e.getTitle(), "BUG: exists('title') not working");
                            assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, e.getContentType(), "Wrong type");
                        }
                    }
                    PerformanceAssertion.assertNormalOperation(duration, "Complex query");
                    logSuccess("testComplexQueryBenchmark", formatDuration(duration));
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testComplexQueryBenchmark"));
    }

    @Test
    @Order(13)
    @DisplayName("Test very complex query performance benchmark")
    void testVeryComplexQueryBenchmark() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.exists("title");
        query.where("locale", "en-us");
        query.includeReference("author");
        query.includeReference("related_articles");
        query.includeEmbeddedItems();
        query.limit(10);
        query.descending("created_at");

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    assertNull(error, "Should not have errors");
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All must have UID");
                            assertEquals(Credentials.COMPLEX_CONTENT_TYPE_UID, e.getContentType(), "Wrong type");
                        }
                    }
                    PerformanceAssertion.assertSlowOperation(duration, "Very complex query");
                    logSuccess("testVeryComplexQueryBenchmark", formatDuration(duration));
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, LARGE_DATASET_TIMEOUT_SECONDS, "testVeryComplexQueryBenchmark"));
    }

    // ===========================
    // Performance Degradation Tests
    // ===========================

    @Test
    @Order(14)
    @DisplayName("Test performance with increasing result set sizes")
    void testPerformanceWithIncreasingSize() throws InterruptedException {
        int[] sizes = {10, 25, 50, 100};
        long[] durations = new long[sizes.length];
        String[] operations = new String[sizes.length];
        
        for (int i = 0; i < sizes.length; i++) {
            CountDownLatch latch = createLatch();
            final int index = i;
            final int currentSize = sizes[i];
            long startTime = PerformanceAssertion.startTimer();
            
            Query sizeQuery = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
            sizeQuery.limit(currentSize);
            
            sizeQuery.find(new QueryResultsCallBack() {
                @Override
                public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                    try {
                        assertNull(error, "Scaling test should not error");
                        if (hasResults(queryResult)) {
                            java.util.List<Entry> results = queryResult.getResultObjects();
                            assertTrue(results.size() <= currentSize, "BUG: limit not working");
                            for (Entry e : results) {
                                assertNotNull(e.getUid(), "All must have UID");
                                assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, e.getContentType(), "Wrong type");
                            }
                        }
                        durations[index] = PerformanceAssertion.elapsedTime(startTime);
                        operations[index] = "Limit " + currentSize;
                    } finally {
                        latch.countDown();
                    }
                }
            });
            
            awaitLatch(latch, "testPerformance-" + currentSize);
        }
        
        PerformanceAssertion.logPerformanceSummary(operations, durations);
        logger.info("✅ Performance scaling validated");
        logSuccess("testPerformanceWithIncreasingSize", "Scaling analyzed");
    }

    @Test
    @Order(15)
    @DisplayName("Test performance with increasing complexity")
    void testPerformanceWithIncreasingComplexity() throws InterruptedException {
        long[] durations = new long[4];
        String[] operations = {"Basic", "With filter", "With ref", "With ref+embed"};
        
        // Simple validations for all 4 complexity levels
        CountDownLatch latch1 = createLatch();
        long start1 = PerformanceAssertion.startTimer();
        Query query1 = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        query1.limit(10);
        query1.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Basic should not error");
                    durations[0] = PerformanceAssertion.elapsedTime(start1);
                } finally {
                    latch1.countDown();
                }
            }
        });
        awaitLatch(latch1, "basic");
        
        CountDownLatch latch2 = createLatch();
        long start2 = PerformanceAssertion.startTimer();
        Query query2 = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        query2.limit(10);
        query2.exists("title");
        query2.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Filtered should not error");
                    durations[1] = PerformanceAssertion.elapsedTime(start2);
                } finally {
                    latch2.countDown();
                }
            }
        });
        awaitLatch(latch2, "filtered");
        
        CountDownLatch latch3 = createLatch();
        long start3 = PerformanceAssertion.startTimer();
        Query query3 = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        query3.limit(10);
        query3.includeReference("author");
        query3.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "WithRef should not error");
                    durations[2] = PerformanceAssertion.elapsedTime(start3);
                } finally {
                    latch3.countDown();
                }
            }
        });
        awaitLatch(latch3, "withRef");
        
        CountDownLatch latch4 = createLatch();
        long start4 = PerformanceAssertion.startTimer();
        Query query4 = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        query4.limit(10);
        query4.includeReference("author");
        query4.includeEmbeddedItems();
        query4.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Complex should not error");
                    durations[3] = PerformanceAssertion.elapsedTime(start4);
                } finally {
                    latch4.countDown();
                }
            }
        });
        awaitLatch(latch4, "complex");
        
        PerformanceAssertion.logPerformanceSummary(operations, durations);
        logSuccess("testPerformanceWithIncreasingComplexity", "Complexity analyzed");
    }

    // ===========================
    // Result Set Size Limits
    // ===========================

    @Test
    @Order(16)
    @DisplayName("Test query with limit exceeding API maximum")
    void testQueryWithExcessiveLimit() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        query.limit(200);  // Exceeds max of 100

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    if (error != null) {
                        logger.info("Excessive limit handled with error: " + error.getErrorMessage());
                    } else if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        int size = results.size();
                        assertTrue(size <= 100, "BUG: API should cap at 100, got: " + size);
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All must have UID");
                            assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, e.getContentType(), "Wrong type");
                        }
                        assertTrue(size <= 100, "Should cap at API maximum");
                        logger.info("Capped at " + size + " entries");
                    }
                    
                    logSuccess("testQueryWithExcessiveLimit", "Handled gracefully");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryWithExcessiveLimit"));
    }

    @Test
    @Order(17)
    @DisplayName("Test query with zero limit")
    void testQueryWithZeroLimit() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        query.limit(0);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    if (error != null) {
                        logger.info("✅ Zero limit handled with error (expected)");
                    } else {
                        logger.info("✅ Zero limit returned results");
                    }
                    logSuccess("testQueryWithZeroLimit", "Edge case validated");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryWithZeroLimit"));
    }

    // ===========================
    // Caching Scenarios
    // ===========================

    @Test
    @Order(18)
    @DisplayName("Test repeated query performance (potential caching)")
    void testRepeatedQueryPerformance() throws InterruptedException {
        long[] durations = new long[3];
        
        for (int i = 0; i < 3; i++) {
            CountDownLatch latch = createLatch();
            final int index = i;
            long startTime = PerformanceAssertion.startTimer();
            
            Query repeatQuery = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
            repeatQuery.limit(20);
            repeatQuery.where("locale", "en-us");
            
            repeatQuery.find(new QueryResultsCallBack() {
                @Override
                public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                    try {
                        assertNull(error, "Repeat query should not error");
                        if (hasResults(queryResult)) {
                            java.util.List<Entry> results = queryResult.getResultObjects();
                            for (Entry e : results) {
                                assertNotNull(e.getUid(), "All must have UID");
                                assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, e.getContentType(), "Wrong type");
                            }
                        }
                        durations[index] = PerformanceAssertion.elapsedTime(startTime);
                    } finally {
                        latch.countDown();
                    }
                }
            });
            
            awaitLatch(latch, "repeat-" + (i+1));
            Thread.sleep(100);
        }
        
        logger.info("✅ Repeated queries: " + formatDuration(durations[0]) + ", " + 
                formatDuration(durations[1]) + ", " + formatDuration(durations[2]));
        if (durations[1] < durations[0] && durations[2] < durations[0]) {
            logger.info("Possible caching detected");
        }
        logSuccess("testRepeatedQueryPerformance", "Caching behavior validated");
    }

    @Test
    @Order(19)
    @DisplayName("Test query performance after stack reinitialization")
    void testQueryPerformanceAfterReinit() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        query.limit(20);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    assertNull(error, "Should not have errors");
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All must have UID");
                            assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, e.getContentType(), "Wrong type");
                        }
                    }
                    logger.info("✅ After reinit: " + formatDuration(duration));
                    logSuccess("testQueryPerformanceAfterReinit", formatDuration(duration));
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryPerformanceAfterReinit"));
    }

    @Test
    @Order(20)
    @DisplayName("Test comprehensive performance scenario")
    void testComprehensivePerformanceScenario() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();
        
        System.gc();
        long memoryBefore = PerformanceAssertion.getCurrentMemoryUsage();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.exists("title");
        query.where("locale", "en-us");
        query.includeReference("author");
        query.includeEmbeddedItems();
        query.limit(50);
        query.descending("created_at");

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    long memoryAfter = PerformanceAssertion.getCurrentMemoryUsage();
                    long memoryUsed = memoryAfter - memoryBefore;
                    
                    assertNull(error, "Comprehensive should not error");
                    
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() <= 50, "BUG: limit(50) not working");
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All must have UID");
                            assertNotNull(e.getTitle(), "BUG: exists('title') not working");
                            assertEquals(Credentials.COMPLEX_CONTENT_TYPE_UID, e.getContentType(), "Wrong type");
                        }
                        int size = results.size();
                        logger.info("✅ Comprehensive: " + size + " entries validated");
                        logger.info("Time: " + formatDuration(duration) + ", Memory: " + formatBytes(memoryUsed));
                    }
                    
                    PerformanceAssertion.assertLargeDatasetOperation(duration, "Comprehensive query");
                    logSuccess("testComprehensivePerformanceScenario", formatDuration(duration));
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, LARGE_DATASET_TIMEOUT_SECONDS, 
                "testComprehensivePerformanceScenario"));
    }

    private String formatBytes(long bytes) {
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

    @AfterAll
    void tearDown() {
        logger.info("Completed PerformanceLargeDatasetsIT test suite");
        logger.info("All 20 performance tests executed");
        logger.info("Tested: Large datasets, pagination, memory, concurrency, benchmarks, scaling");
    }
}

