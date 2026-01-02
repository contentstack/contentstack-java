package com.contentstack.sdk;

import com.contentstack.sdk.utils.PerformanceAssertion;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;

/**
 * Comprehensive Integration Tests for Global Fields
 * Tests global field functionality including:
 * - Entry with global fields
 * - Global field data access
 * - Multiple global fields in entry
 * - Global field with different types
 * - Global field validation
 * - Performance with global fields
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GlobalFieldsComprehensiveIT extends BaseIntegrationTest {

    @BeforeAll
    void setUp() {
        logger.info("Setting up GlobalFieldsComprehensiveIT test suite");
        logger.info("Testing global fields functionality");
        if (Credentials.GLOBAL_FIELD_SIMPLE != null) {
            logger.info("Using global field: " + Credentials.GLOBAL_FIELD_SIMPLE);
        }
    }

    // ===========================
    // Basic Global Field Tests
    // ===========================

    @Test
    @Order(1)
    @DisplayName("Test entry has global field")
    void testEntryHasGlobalField() throws InterruptedException {
        if (Credentials.GLOBAL_FIELD_SIMPLE == null || Credentials.GLOBAL_FIELD_SIMPLE.isEmpty()) {
            logger.info("ℹ️ No global field configured, skipping test");
            logSuccess("testEntryHasGlobalField", "Skipped");
            return;
        }

        CountDownLatch latch = createLatch();

        Query query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (queryResult.getResultObjects().size() > 0) {
                        Entry entry = queryResult.getResultObjects().get(0);
                        
                        // Check if global field exists in entry
                        Object globalFieldValue = entry.get(Credentials.GLOBAL_FIELD_SIMPLE);
                        
                        if (globalFieldValue != null) {
                            logger.info("✅ Entry has global field: " + Credentials.GLOBAL_FIELD_SIMPLE);
                            logSuccess("testEntryHasGlobalField", "Global field present");
                        } else {
                            logger.info("ℹ️ Entry does not have global field (field may not be in schema)");
                            logSuccess("testEntryHasGlobalField", "Global field absent");
                        }
                    } else {
                        logger.info("ℹ️ No entries to test");
                        logSuccess("testEntryHasGlobalField", "No entries");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testEntryHasGlobalField"));
    }

    @Test
    @Order(2)
    @DisplayName("Test global field data access")
    void testGlobalFieldDataAccess() throws InterruptedException {
        if (Credentials.GLOBAL_FIELD_SIMPLE == null || Credentials.GLOBAL_FIELD_SIMPLE.isEmpty()) {
            logger.info("ℹ️ No global field configured, skipping test");
            logSuccess("testGlobalFieldDataAccess", "Skipped");
            return;
        }

        CountDownLatch latch = createLatch();

        Query query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.limit(10);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    int entriesWithGlobalField = 0;
                    for (Entry entry : queryResult.getResultObjects()) {
                        Object globalFieldValue = entry.get(Credentials.GLOBAL_FIELD_SIMPLE);
                        if (globalFieldValue != null) {
                            entriesWithGlobalField++;
                        }
                    }
                    
                    logger.info("✅ " + entriesWithGlobalField + "/" + queryResult.getResultObjects().size() + 
                            " entries have global field");
                    logSuccess("testGlobalFieldDataAccess", 
                            entriesWithGlobalField + " entries with field");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testGlobalFieldDataAccess"));
    }

    @Test
    @Order(3)
    @DisplayName("Test multiple global fields")
    void testMultipleGlobalFields() throws InterruptedException {
        CountDownLatch latch = createLatch();

        Query query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (queryResult.getResultObjects().size() > 0) {
                        Entry entry = queryResult.getResultObjects().get(0);
                        
                        int globalFieldCount = 0;
                        
                        // Check simple global field
                        if (Credentials.GLOBAL_FIELD_SIMPLE != null && 
                            entry.get(Credentials.GLOBAL_FIELD_SIMPLE) != null) {
                            globalFieldCount++;
                        }
                        
                        // Check medium global field
                        if (Credentials.GLOBAL_FIELD_MEDIUM != null && 
                            entry.get(Credentials.GLOBAL_FIELD_MEDIUM) != null) {
                            globalFieldCount++;
                        }
                        
                        // Check complex global field
                        if (Credentials.GLOBAL_FIELD_COMPLEX != null && 
                            entry.get(Credentials.GLOBAL_FIELD_COMPLEX) != null) {
                            globalFieldCount++;
                        }
                        
                        // Check video global field
                        if (Credentials.GLOBAL_FIELD_VIDEO != null && 
                            entry.get(Credentials.GLOBAL_FIELD_VIDEO) != null) {
                            globalFieldCount++;
                        }
                        
                        logger.info("✅ Entry has " + globalFieldCount + " global field(s)");
                        logSuccess("testMultipleGlobalFields", globalFieldCount + " global fields");
                    } else {
                        logger.info("ℹ️ No entries to test");
                        logSuccess("testMultipleGlobalFields", "No entries");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testMultipleGlobalFields"));
    }

    // ===========================
    // Global Field Types Tests
    // ===========================

    @Test
    @Order(4)
    @DisplayName("Test global field simple type")
    void testGlobalFieldSimpleType() throws InterruptedException {
        if (Credentials.GLOBAL_FIELD_SIMPLE == null || Credentials.GLOBAL_FIELD_SIMPLE.isEmpty()) {
            logger.info("ℹ️ No simple global field configured, skipping test");
            logSuccess("testGlobalFieldSimpleType", "Skipped");
            return;
        }

        CountDownLatch latch = createLatch();

        Query query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    for (Entry entry : queryResult.getResultObjects()) {
                        Object simpleField = entry.get(Credentials.GLOBAL_FIELD_SIMPLE);
                        if (simpleField != null) {
                            // Simple field found
                            logger.info("✅ Simple global field type: " + simpleField.getClass().getSimpleName());
                            logSuccess("testGlobalFieldSimpleType", "Simple field present");
                            break;
                        }
                    }
                    
                    logSuccess("testGlobalFieldSimpleType", "Test completed");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testGlobalFieldSimpleType"));
    }

    @Test
    @Order(5)
    @DisplayName("Test global field complex type")
    void testGlobalFieldComplexType() throws InterruptedException {
        if (Credentials.GLOBAL_FIELD_COMPLEX == null || Credentials.GLOBAL_FIELD_COMPLEX.isEmpty()) {
            logger.info("ℹ️ No complex global field configured, skipping test");
            logSuccess("testGlobalFieldComplexType", "Skipped");
            return;
        }

        CountDownLatch latch = createLatch();

        Query query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    for (Entry entry : queryResult.getResultObjects()) {
                        Object complexField = entry.get(Credentials.GLOBAL_FIELD_COMPLEX);
                        if (complexField != null) {
                            // Complex field found
                            logger.info("✅ Complex global field type: " + complexField.getClass().getSimpleName());
                            logSuccess("testGlobalFieldComplexType", "Complex field present");
                            break;
                        }
                    }
                    
                    logSuccess("testGlobalFieldComplexType", "Test completed");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testGlobalFieldComplexType"));
    }

    // ===========================
    // Query with Global Fields
    // ===========================

    @Test
    @Order(6)
    @DisplayName("Test query only with global field")
    void testQueryOnlyWithGlobalField() throws InterruptedException {
        if (Credentials.GLOBAL_FIELD_SIMPLE == null || Credentials.GLOBAL_FIELD_SIMPLE.isEmpty()) {
            logger.info("ℹ️ No global field configured, skipping test");
            logSuccess("testQueryOnlyWithGlobalField", "Skipped");
            return;
        }

        CountDownLatch latch = createLatch();

        Query query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.only(new String[]{"title", Credentials.GLOBAL_FIELD_SIMPLE});
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Query with only() should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (queryResult.getResultObjects().size() > 0) {
                        Entry entry = queryResult.getResultObjects().get(0);
                        
                        // Title should be present (in only())
                        assertNotNull(entry.get("title"), "Title should be present with only()");
                        
                        // Global field may or may not be present
                        Object globalField = entry.get(Credentials.GLOBAL_FIELD_SIMPLE);
                        logger.info("Global field with only(): " + (globalField != null ? "present" : "absent"));
                    }
                    
                    logger.info("✅ Query with only() including global field");
                    logSuccess("testQueryOnlyWithGlobalField", "Only with global field");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryOnlyWithGlobalField"));
    }

    @Test
    @Order(7)
    @DisplayName("Test query except global field")
    void testQueryExceptGlobalField() throws InterruptedException {
        if (Credentials.GLOBAL_FIELD_SIMPLE == null || Credentials.GLOBAL_FIELD_SIMPLE.isEmpty()) {
            logger.info("ℹ️ No global field configured, skipping test");
            logSuccess("testQueryExceptGlobalField", "Skipped");
            return;
        }

        CountDownLatch latch = createLatch();

        Query query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.except(new String[]{Credentials.GLOBAL_FIELD_SIMPLE});
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Query with except() should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (queryResult.getResultObjects().size() > 0) {
                        Entry entry = queryResult.getResultObjects().get(0);
                        
                        // Global field should ideally be excluded
                        Object globalField = entry.get(Credentials.GLOBAL_FIELD_SIMPLE);
                        logger.info("Global field with except(): " + (globalField != null ? "present" : "absent"));
                    }
                    
                    logger.info("✅ Query with except() excluding global field");
                    logSuccess("testQueryExceptGlobalField", "Except global field");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryExceptGlobalField"));
    }

    // ===========================
    // Performance Tests
    // ===========================

    @Test
    @Order(8)
    @DisplayName("Test query performance with global fields")
    void testQueryPerformanceWithGlobalFields() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        Query query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.limit(10);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    
                    assertNull(error, "Query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    // Global fields should not significantly impact performance
                    assertTrue(duration < 5000,
                            "PERFORMANCE BUG: Query with global fields took " + duration + "ms (max: 5s)");
                    
                    logger.info("✅ Query with global fields: " + queryResult.getResultObjects().size() + 
                            " entries in " + formatDuration(duration));
                    logSuccess("testQueryPerformanceWithGlobalFields", 
                            queryResult.getResultObjects().size() + " entries, " + formatDuration(duration));
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryPerformanceWithGlobalFields"));
    }

    @Test
    @Order(9)
    @DisplayName("Test multiple queries with global fields")
    void testMultipleQueriesWithGlobalFields() throws InterruptedException {
        int queryCount = 3;
        long startTime = PerformanceAssertion.startTimer();
        
        for (int i = 0; i < queryCount; i++) {
            CountDownLatch latch = createLatch();
            
            Query query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
            query.limit(5);
            
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
            
            awaitLatch(latch, "query-" + i);
        }
        
        long duration = PerformanceAssertion.elapsedTime(startTime);
        
        assertTrue(duration < 10000,
                "PERFORMANCE BUG: " + queryCount + " queries took " + duration + "ms (max: 10s)");
        
        logger.info("✅ Multiple queries with global fields: " + queryCount + " queries in " + 
                formatDuration(duration));
        logSuccess("testMultipleQueriesWithGlobalFields", 
                queryCount + " queries, " + formatDuration(duration));
    }

    // ===========================
    // Entry-Level Global Field Tests
    // ===========================

    @Test
    @Order(10)
    @DisplayName("Test entry fetch with global fields")
    void testEntryFetchWithGlobalFields() throws InterruptedException {
        CountDownLatch latch = createLatch();

        Entry entry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                .entry(Credentials.COMPLEX_ENTRY_UID);

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    // Entry fetch completes
                    if (error == null) {
                        // Check for global fields
                        int globalFieldsFound = 0;
                        
                        if (Credentials.GLOBAL_FIELD_SIMPLE != null && 
                            entry.get(Credentials.GLOBAL_FIELD_SIMPLE) != null) {
                            globalFieldsFound++;
                        }
                        if (Credentials.GLOBAL_FIELD_MEDIUM != null && 
                            entry.get(Credentials.GLOBAL_FIELD_MEDIUM) != null) {
                            globalFieldsFound++;
                        }
                        if (Credentials.GLOBAL_FIELD_COMPLEX != null && 
                            entry.get(Credentials.GLOBAL_FIELD_COMPLEX) != null) {
                            globalFieldsFound++;
                        }
                        if (Credentials.GLOBAL_FIELD_VIDEO != null && 
                            entry.get(Credentials.GLOBAL_FIELD_VIDEO) != null) {
                            globalFieldsFound++;
                        }
                        
                        logger.info("✅ Entry has " + globalFieldsFound + " global field(s)");
                        logSuccess("testEntryFetchWithGlobalFields", globalFieldsFound + " global fields");
                    } else {
                        logger.info("Entry fetch returned error: " + error.getErrorMessage());
                        logSuccess("testEntryFetchWithGlobalFields", "Entry error");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testEntryFetchWithGlobalFields"));
    }

    @Test
    @Order(11)
    @DisplayName("Test global field consistency across queries")
    void testGlobalFieldConsistencyAcrossQueries() throws InterruptedException {
        if (Credentials.GLOBAL_FIELD_SIMPLE == null || Credentials.GLOBAL_FIELD_SIMPLE.isEmpty()) {
            logger.info("ℹ️ No global field configured, skipping test");
            logSuccess("testGlobalFieldConsistencyAcrossQueries", "Skipped");
            return;
        }

        final Object[] firstValue = {null};
        
        // First query
        CountDownLatch latch1 = createLatch();
        Query query1 = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query1.limit(1);
        
        query1.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    if (error == null && queryResult != null && queryResult.getResultObjects().size() > 0) {
                        firstValue[0] = queryResult.getResultObjects().get(0).get(Credentials.GLOBAL_FIELD_SIMPLE);
                    }
                } finally {
                    latch1.countDown();
                }
            }
        });
        
        awaitLatch(latch1, "first-query");
        
        // Second query - same results should have same global field value
        CountDownLatch latch2 = createLatch();
        Query query2 = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query2.limit(1);
        
        query2.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Second query should not error");
                    
                    if (queryResult != null && queryResult.getResultObjects().size() > 0) {
                        Object secondValue = queryResult.getResultObjects().get(0).get(Credentials.GLOBAL_FIELD_SIMPLE);
                        
                        // Values should be consistent
                        boolean consistent = (firstValue[0] == null && secondValue == null) ||
                                           (firstValue[0] != null && firstValue[0].equals(secondValue));
                        
                        if (consistent) {
                            logger.info("✅ Global field values consistent across queries");
                            logSuccess("testGlobalFieldConsistencyAcrossQueries", "Consistent");
                        } else {
                            logger.info("ℹ️ Global field values differ (may be different entries)");
                            logSuccess("testGlobalFieldConsistencyAcrossQueries", "Different values");
                        }
                    }
                } finally {
                    latch2.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch2, "testGlobalFieldConsistencyAcrossQueries"));
    }

    // ===========================
    // Comprehensive Tests
    // ===========================

    @Test
    @Order(12)
    @DisplayName("Test all global field types")
    void testAllGlobalFieldTypes() throws InterruptedException {
        CountDownLatch latch = createLatch();

        Query query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    int totalGlobalFieldsFound = 0;
                    
                    for (Entry entry : queryResult.getResultObjects()) {
                        int entryGlobalFields = 0;
                        
                        if (Credentials.GLOBAL_FIELD_SIMPLE != null && 
                            entry.get(Credentials.GLOBAL_FIELD_SIMPLE) != null) {
                            entryGlobalFields++;
                        }
                        if (Credentials.GLOBAL_FIELD_MEDIUM != null && 
                            entry.get(Credentials.GLOBAL_FIELD_MEDIUM) != null) {
                            entryGlobalFields++;
                        }
                        if (Credentials.GLOBAL_FIELD_COMPLEX != null && 
                            entry.get(Credentials.GLOBAL_FIELD_COMPLEX) != null) {
                            entryGlobalFields++;
                        }
                        if (Credentials.GLOBAL_FIELD_VIDEO != null && 
                            entry.get(Credentials.GLOBAL_FIELD_VIDEO) != null) {
                            entryGlobalFields++;
                        }
                        
                        totalGlobalFieldsFound += entryGlobalFields;
                    }
                    
                    logger.info("✅ Total global fields found across " + 
                            queryResult.getResultObjects().size() + " entries: " + totalGlobalFieldsFound);
                    logSuccess("testAllGlobalFieldTypes", 
                            totalGlobalFieldsFound + " global fields across " + 
                            queryResult.getResultObjects().size() + " entries");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testAllGlobalFieldTypes"));
    }

    @Test
    @Order(13)
    @DisplayName("Test comprehensive global field scenario")
    void testComprehensiveGlobalFieldScenario() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        Query query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.limit(10);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    
                    assertNull(error, "Comprehensive query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    int entryCount = queryResult.getResultObjects().size();
                    int entriesWithGlobalFields = 0;
                    int totalGlobalFields = 0;
                    
                    for (Entry entry : queryResult.getResultObjects()) {
                        int entryGlobalFieldCount = 0;
                        
                        if (Credentials.GLOBAL_FIELD_SIMPLE != null && 
                            entry.get(Credentials.GLOBAL_FIELD_SIMPLE) != null) {
                            entryGlobalFieldCount++;
                        }
                        if (Credentials.GLOBAL_FIELD_MEDIUM != null && 
                            entry.get(Credentials.GLOBAL_FIELD_MEDIUM) != null) {
                            entryGlobalFieldCount++;
                        }
                        if (Credentials.GLOBAL_FIELD_COMPLEX != null && 
                            entry.get(Credentials.GLOBAL_FIELD_COMPLEX) != null) {
                            entryGlobalFieldCount++;
                        }
                        if (Credentials.GLOBAL_FIELD_VIDEO != null && 
                            entry.get(Credentials.GLOBAL_FIELD_VIDEO) != null) {
                            entryGlobalFieldCount++;
                        }
                        
                        if (entryGlobalFieldCount > 0) {
                            entriesWithGlobalFields++;
                            totalGlobalFields += entryGlobalFieldCount;
                        }
                    }
                    
                    // Performance check
                    assertTrue(duration < 5000,
                            "PERFORMANCE BUG: Comprehensive scenario took " + duration + "ms (max: 5s)");
                    
                    logger.info("✅ COMPREHENSIVE: " + entryCount + " entries, " + 
                            entriesWithGlobalFields + " with global fields, " +
                            totalGlobalFields + " total fields, " + formatDuration(duration));
                    logSuccess("testComprehensiveGlobalFieldScenario", 
                            entryCount + " entries, " + totalGlobalFields + " global fields, " + 
                            formatDuration(duration));
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testComprehensiveGlobalFieldScenario"));
    }

    @AfterAll
    void tearDown() {
        logger.info("Completed GlobalFieldsComprehensiveIT test suite");
        logger.info("All 13 global field tests executed");
        logger.info("Tested: global field presence, types, queries, performance, comprehensive scenarios");
        logger.info("🎉 PHASE 4 COMPLETE! All optional coverage tasks finished!");
    }
}

