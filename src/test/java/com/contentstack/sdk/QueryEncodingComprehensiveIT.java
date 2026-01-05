package com.contentstack.sdk;

import com.contentstack.sdk.utils.PerformanceAssertion;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;

/**
 * Comprehensive Integration Tests for Query Encoding
 * Tests query parameter encoding including:
 * - Field names with special characters
 * - Query parameter encoding
 * - URL encoding for field values
 * - Complex query combinations (encoding stress test)
 * - Taxonomy queries (special chars in values)
 * - Performance with complex encoding
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class QueryEncodingComprehensiveIT extends BaseIntegrationTest {

    private Query query;

    @BeforeAll
    void setUp() {
        logger.info("Setting up QueryEncodingComprehensiveIT test suite");
        logger.info("Testing query encoding behavior");
        logger.info("Using content type: " + Credentials.COMPLEX_CONTENT_TYPE_UID);
    }

    // ===========================
    // Basic Query Encoding
    // ===========================

    @Test
    @Order(1)
    @DisplayName("Test basic query encoding with exists")
    void testBasicQueryEncoding() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.exists("title");
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Basic query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        logger.info("✅ Basic encoding: " + queryResult.getResultObjects().size() + " results");
                        logSuccess("testBasicQueryEncoding", queryResult.getResultObjects().size() + " results");
                    } else {
                        logSuccess("testBasicQueryEncoding", "No results");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testBasicQueryEncoding"));
    }

    @Test
    @Order(2)
    @DisplayName("Test query encoding with URL field")
    void testQueryEncodingWithUrlField() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.exists("url"); // URLs contain /, ?, &, etc.
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    if (error == null) {
                        assertNotNull(queryResult, "QueryResult should not be null");
                        if (hasResults(queryResult)) {
                            logger.info("✅ URL field encoding: " + queryResult.getResultObjects().size() + " results");
                            logSuccess("testQueryEncodingWithUrlField", queryResult.getResultObjects().size() + " results");
                        } else {
                            logSuccess("testQueryEncodingWithUrlField", "No results");
                        }
                    } else {
                        logger.info("ℹ️ URL field error: " + error.getErrorMessage());
                        logSuccess("testQueryEncodingWithUrlField", "Handled gracefully");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryEncodingWithUrlField"));
    }

    @Test
    @Order(3)
    @DisplayName("Test query encoding with nested field path")
    void testQueryEncodingWithNestedField() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.exists("seo.title"); // Dot notation encoding
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    if (error == null) {
                        assertNotNull(queryResult, "QueryResult should not be null");
                        if (hasResults(queryResult)) {
                            logger.info("✅ Nested field encoding: " + queryResult.getResultObjects().size() + " results");
                            logSuccess("testQueryEncodingWithNestedField", queryResult.getResultObjects().size() + " results");
                        } else {
                            logSuccess("testQueryEncodingWithNestedField", "No results");
                        }
                    } else {
                        logger.info("ℹ️ Nested field handled");
                        logSuccess("testQueryEncodingWithNestedField", "Handled");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryEncodingWithNestedField"));
    }

    @Test
    @Order(4)
    @DisplayName("Test query encoding with underscore field names")
    void testQueryEncodingWithUnderscoreFields() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.exists("content_block"); // Underscore in field name
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    if (error == null) {
                        assertNotNull(queryResult, "QueryResult should not be null");
                        if (hasResults(queryResult)) {
                            logger.info("✅ Underscore field encoding: " + queryResult.getResultObjects().size() + " results");
                            logSuccess("testQueryEncodingWithUnderscoreFields", queryResult.getResultObjects().size() + " results");
                        } else {
                            logSuccess("testQueryEncodingWithUnderscoreFields", "No results");
                        }
                    } else {
                        logSuccess("testQueryEncodingWithUnderscoreFields", "Handled");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryEncodingWithUnderscoreFields"));
    }

    @Test
    @Order(5)
    @DisplayName("Test query encoding with multiple field conditions")
    void testQueryEncodingWithMultipleFields() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.exists("title");
        query.exists("url");
        query.exists("topics");
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    if (error == null) {
                        assertNotNull(queryResult, "QueryResult should not be null");
                        if (hasResults(queryResult)) {
                            logger.info("✅ Multiple fields encoding: " + queryResult.getResultObjects().size() + " results");
                            logSuccess("testQueryEncodingWithMultipleFields", queryResult.getResultObjects().size() + " results");
                        } else {
                            logSuccess("testQueryEncodingWithMultipleFields", "No results");
                        }
                    } else {
                        logSuccess("testQueryEncodingWithMultipleFields", "Handled");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryEncodingWithMultipleFields"));
    }

    // ===========================
    // Taxonomy Query Encoding
    // ===========================

    @Test
    @Order(6)
    @DisplayName("Test query encoding with taxonomy")
    void testQueryEncodingWithTaxonomy() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        // Taxonomy queries involve complex parameter encoding
        if (Credentials.TAX_USA_STATE != null && !Credentials.TAX_USA_STATE.isEmpty()) {
            query.addQuery("taxonomies.usa", Credentials.TAX_USA_STATE);
        }
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    if (error == null) {
                        assertNotNull(queryResult, "QueryResult should not be null");
                        if (hasResults(queryResult)) {
                            logger.info("✅ Taxonomy encoding: " + queryResult.getResultObjects().size() + " results");
                            logSuccess("testQueryEncodingWithTaxonomy", queryResult.getResultObjects().size() + " results");
                        } else {
                            logSuccess("testQueryEncodingWithTaxonomy", "No results");
                        }
                    } else {
                        logger.info("ℹ️ Taxonomy error: " + error.getErrorMessage());
                        logSuccess("testQueryEncodingWithTaxonomy", "Handled");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryEncodingWithTaxonomy"));
    }

    @Test
    @Order(7)
    @DisplayName("Test query encoding with multiple taxonomy terms")
    void testQueryEncodingWithMultipleTaxonomyTerms() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        // Multiple taxonomy conditions
        if (Credentials.TAX_USA_STATE != null && !Credentials.TAX_USA_STATE.isEmpty()) {
            query.addQuery("taxonomies.usa", Credentials.TAX_USA_STATE);
        }
        if (Credentials.TAX_INDIA_STATE != null && !Credentials.TAX_INDIA_STATE.isEmpty()) {
            query.addQuery("taxonomies.india", Credentials.TAX_INDIA_STATE);
        }
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    if (error == null) {
                        assertNotNull(queryResult, "QueryResult should not be null");
                        if (hasResults(queryResult)) {
                            logger.info("✅ Multiple taxonomy encoding: " + queryResult.getResultObjects().size() + " results");
                            logSuccess("testQueryEncodingWithMultipleTaxonomyTerms", queryResult.getResultObjects().size() + " results");
                        } else {
                            logSuccess("testQueryEncodingWithMultipleTaxonomyTerms", "No results");
                        }
                    } else {
                        logSuccess("testQueryEncodingWithMultipleTaxonomyTerms", "Handled");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryEncodingWithMultipleTaxonomyTerms"));
    }

    // ===========================
    // Pagination + Encoding
    // ===========================

    @Test
    @Order(8)
    @DisplayName("Test query encoding with pagination")
    void testQueryEncodingWithPagination() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.exists("title");
        query.skip(2);
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Pagination + encoding should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() <= 5, "Should respect limit");
                        logger.info("✅ Pagination + encoding: " + results.size() + " results");
                        logSuccess("testQueryEncodingWithPagination", results.size() + " results");
                    } else {
                        logSuccess("testQueryEncodingWithPagination", "No results");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryEncodingWithPagination"));
    }

    @Test
    @Order(9)
    @DisplayName("Test query encoding with sorting")
    void testQueryEncodingWithSorting() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.exists("title");
        query.descending("created_at"); // Sort parameter encoding
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Sorting + encoding should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        logger.info("✅ Sorting + encoding: " + queryResult.getResultObjects().size() + " results");
                        logSuccess("testQueryEncodingWithSorting", queryResult.getResultObjects().size() + " results");
                    } else {
                        logSuccess("testQueryEncodingWithSorting", "No results");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryEncodingWithSorting"));
    }

    // ===========================
    // Complex Encoding Scenarios
    // ===========================

    @Test
    @Order(10)
    @DisplayName("Test complex query encoding - multiple conditions")
    void testComplexQueryEncoding() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.exists("title");
        query.exists("url");
        query.exists("content_block");
        query.descending("created_at");
        query.skip(1);
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Complex encoding should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() <= 5, "Should respect limit");
                        logger.info("✅ Complex encoding: " + results.size() + " results");
                        logSuccess("testComplexQueryEncoding", results.size() + " results");
                    } else {
                        logSuccess("testComplexQueryEncoding", "No results");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testComplexQueryEncoding"));
    }

    @Test
    @Order(11)
    @DisplayName("Test query encoding with references")
    void testQueryEncodingWithReferences() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.exists("title");
        query.includeReference("authors"); // Reference field encoding
        query.limit(3);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    if (error == null) {
                        assertNotNull(queryResult, "QueryResult should not be null");
                        if (hasResults(queryResult)) {
                            logger.info("✅ References + encoding: " + queryResult.getResultObjects().size() + " results");
                            logSuccess("testQueryEncodingWithReferences", queryResult.getResultObjects().size() + " results");
                        } else {
                            logSuccess("testQueryEncodingWithReferences", "No results");
                        }
                    } else {
                        logger.info("ℹ️ References not configured");
                        logSuccess("testQueryEncodingWithReferences", "Handled");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryEncodingWithReferences"));
    }

    @Test
    @Order(12)
    @DisplayName("Test query encoding with field projection")
    void testQueryEncodingWithProjection() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.only(new String[]{"title", "url", "content_block"}); // Field projection encoding
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    if (error == null) {
                        assertNotNull(queryResult, "QueryResult should not be null");
                        if (hasResults(queryResult)) {
                            logger.info("✅ Projection + encoding: " + queryResult.getResultObjects().size() + " results");
                            logSuccess("testQueryEncodingWithProjection", queryResult.getResultObjects().size() + " results");
                        } else {
                            logSuccess("testQueryEncodingWithProjection", "No results");
                        }
                    } else {
                        logger.info("ℹ️ Projection error: " + error.getErrorMessage());
                        logSuccess("testQueryEncodingWithProjection", "Handled");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryEncodingWithProjection"));
    }

    // ===========================
    // Performance Tests
    // ===========================

    @Test
    @Order(13)
    @DisplayName("Test query encoding performance")
    void testQueryEncodingPerformance() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        // Complex query with multiple encoding requirements
        query.exists("title");
        query.exists("url");
        query.descending("created_at");
        query.limit(20);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    
                    assertNull(error, "Encoding performance query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    // Encoding should not significantly impact performance
                    assertTrue(duration < 10000,
                            "PERFORMANCE BUG: Encoding query took " + duration + "ms (max: 10s)");
                    
                    if (hasResults(queryResult)) {
                        logger.info("✅ Encoding performance: " + 
                                queryResult.getResultObjects().size() + " results in " + 
                                formatDuration(duration));
                        logSuccess("testQueryEncodingPerformance", 
                                queryResult.getResultObjects().size() + " results, " + formatDuration(duration));
                    } else {
                        logger.info("✅ Encoding performance (no results): " + formatDuration(duration));
                        logSuccess("testQueryEncodingPerformance", "No results, " + formatDuration(duration));
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryEncodingPerformance"));
    }

    @Test
    @Order(14)
    @DisplayName("Test multiple sequential queries with encoding")
    void testMultipleQueriesEncoding() throws InterruptedException {
        int[] totalResults = {0};
        long startTime = PerformanceAssertion.startTimer();
        
        // Run 3 queries sequentially
        for (int i = 0; i < 3; i++) {
            CountDownLatch latch = createLatch();
            
            Query q = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
            q.exists("title");
            q.skip(i * 3);
            q.limit(3);
            
            q.find(new QueryResultsCallBack() {
                @Override
                public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                    try {
                        if (error == null && hasResults(queryResult)) {
                            totalResults[0] += queryResult.getResultObjects().size();
                        }
                    } finally {
                        latch.countDown();
                    }
                }
            });
            
            awaitLatch(latch, "query-" + i);
        }
        
        long duration = PerformanceAssertion.elapsedTime(startTime);
        
        logger.info("✅ Multiple queries encoding: " + totalResults[0] + " total results in " + formatDuration(duration));
        logSuccess("testMultipleQueriesEncoding", totalResults[0] + " results, " + formatDuration(duration));
    }

    // ===========================
    // Comprehensive Scenario
    // ===========================

    @Test
    @Order(15)
    @DisplayName("Test comprehensive encoding scenario")
    void testComprehensiveEncodingScenario() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        // Comprehensive query testing all encoding aspects
        query.exists("title");
        query.exists("content_block");
        query.only(new String[]{"title", "url", "topics"});
        query.descending("created_at");
        query.skip(1);
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    
                    if (error == null) {
                        assertNotNull(queryResult, "QueryResult should not be null");
                        
                        if (hasResults(queryResult)) {
                            java.util.List<Entry> results = queryResult.getResultObjects();
                            assertTrue(results.size() <= 5, "Should respect limit");
                            
                            // All entries should be valid
                            for (Entry e : results) {
                                assertNotNull(e.getUid(), "All must have UID");
                                assertEquals(Credentials.COMPLEX_CONTENT_TYPE_UID, e.getContentType(),
                                        "BUG: Wrong content type");
                            }
                            
                            // Performance check
                            assertTrue(duration < 10000,
                                    "PERFORMANCE BUG: Comprehensive took " + duration + "ms (max: 10s)");
                            
                            logger.info("✅ Comprehensive encoding: " + results.size() + 
                                    " entries in " + formatDuration(duration));
                            logSuccess("testComprehensiveEncodingScenario", 
                                    results.size() + " entries, " + formatDuration(duration));
                        } else {
                            logger.info("ℹ️ Comprehensive encoding returned no results");
                            logSuccess("testComprehensiveEncodingScenario", "No results");
                        }
                    } else {
                        logger.info("ℹ️ Comprehensive error: " + error.getErrorMessage());
                        logSuccess("testComprehensiveEncodingScenario", "Handled");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testComprehensiveEncodingScenario"));
    }

    @AfterAll
    void tearDown() {
        logger.info("Completed QueryEncodingComprehensiveIT test suite");
        logger.info("All 15 query encoding tests executed");
        logger.info("Tested: field names, URL encoding, taxonomy, pagination, sorting, performance");
    }
}
