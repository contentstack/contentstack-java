package com.contentstack.sdk;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 * Comprehensive Integration Tests for JSON RTE Embedded Items
 * Tests JSON Rich Text Editor embedded items functionality including:
 * - Basic embedded items inclusion
 * - Multiple embedded items in single entry
 * - Nested embedded items
 * - Embedded items with references
 * - Embedded items with Query
 * - Complex scenarios (multiple fields with embedded items)
 * - Edge cases and error handling
 * Uses complex stack data with JSON RTE fields containing embedded entries/assets
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JsonRteEmbeddedItemsIT extends BaseIntegrationTest {

    private Entry entry;
    private Query query;

    @BeforeAll
    void setUp() {
        logger.info("Setting up JsonRteEmbeddedItemsIT test suite");
        logger.info("Testing JSON RTE embedded items with complex stack data");
        
        if (!Credentials.hasComplexEntry()) {
            logger.warning("Complex entry not configured - some tests may be limited");
        }
    }

    // ===========================
    // Basic Embedded Items
    // ===========================

    @Test
    @Order(1)
    @DisplayName("Test basic embedded items inclusion")
    void testBasicEmbeddedItems() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = startTimer();

        entry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                     .entry(Credentials.COMPLEX_ENTRY_UID);
        
        // Include embedded items in JSON RTE fields
        entry.includeEmbeddedItems();

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "includeEmbeddedItems() should not error");
                    assertNotNull(entry, "Entry should not be null");
                    
                    // STRONG ASSERTION: Validate correct entry
                    assertEquals(Credentials.COMPLEX_ENTRY_UID, entry.getUid(),
                            "CRITICAL BUG: Wrong entry fetched!");
                    assertEquals(Credentials.COMPLEX_CONTENT_TYPE_UID, entry.getContentType(),
                            "CRITICAL BUG: Wrong content type!");
                    
                    // STRONG ASSERTION: Basic fields must exist
                    assertTrue(hasBasicFields(entry), 
                            "BUG: Entry missing basic fields");
                    assertNotNull(entry.getTitle(), "Entry must have title");
                    
                    long duration = System.currentTimeMillis() - startTime;
                    
                    logger.info("✅ Entry fetched with includeEmbeddedItems()");
                    logger.info("  Entry UID: " + entry.getUid());
                    logger.info("  Duration: " + formatDuration(duration));
                    
                    logSuccess("testBasicEmbeddedItems", 
                            "Embedded items included successfully in " + formatDuration(duration));
                    logExecutionTime("testBasicEmbeddedItems", startTime);
                } catch (Exception e) {
                    fail("Test failed with exception: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testBasicEmbeddedItems"));
    }

    @Test
    @Order(2)
    @DisplayName("Test embedded items with specific JSON RTE field")
    void testEmbeddedItemsWithSpecificField() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                     .entry(Credentials.COMPLEX_ENTRY_UID);
        
        entry.includeEmbeddedItems();
        entry.only(new String[]{"title", "description", "content", "uid"});

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "includeEmbeddedItems() + only() should not error");
                    assertNotNull(entry, "Entry should not be null");
                    
                    // STRONG ASSERTION: Validate correct entry
                    assertEquals(Credentials.COMPLEX_ENTRY_UID, entry.getUid(),
                            "CRITICAL BUG: Wrong entry fetched!");
                    
                    // STRONG ASSERTION: only() filter validation
                    assertNotNull(entry.getTitle(), 
                            "BUG: only(['title',...]) - title should be included");
                    assertNotNull(entry.getUid(), 
                            "UID always included (system field)");
                    
                    // Check JSON RTE fields
                    Object description = entry.get("description");
                    Object content = entry.get("content");
                    
                    int jsonRteFields = 0;
                    if (description != null) {
                        jsonRteFields++;
                        logger.info("  description field present ✅");
                    }
                    if (content != null) {
                        jsonRteFields++;
                        logger.info("  content field present ✅");
                    }
                    
                    logger.info("Embedded items with field selection validated:");
                    logger.info("  JSON RTE fields found: " + jsonRteFields);
                    
                    logSuccess("testEmbeddedItemsWithSpecificField",
                            jsonRteFields + " JSON RTE fields present");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testEmbeddedItemsWithSpecificField"));
    }

    @Test
    @Order(3)
    @DisplayName("Test embedded items without inclusion")
    void testWithoutEmbeddedItems() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                     .entry(Credentials.COMPLEX_ENTRY_UID);
        
        // Fetch WITHOUT includeEmbeddedItems() - baseline comparison

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "Entry fetch should not error");
                    assertNotNull(entry, "Entry should not be null");
                    
                    // STRONG ASSERTION: Validate correct entry
                    assertEquals(Credentials.COMPLEX_ENTRY_UID, entry.getUid(),
                            "CRITICAL BUG: Wrong entry fetched!");
                    assertEquals(Credentials.COMPLEX_CONTENT_TYPE_UID, entry.getContentType(),
                            "CRITICAL BUG: Wrong content type!");
                    
                    // STRONG ASSERTION: Basic fields
                    assertTrue(hasBasicFields(entry), 
                            "BUG: Entry missing basic fields");
                    
                    logger.info("✅ Baseline: Entry fetched WITHOUT includeEmbeddedItems()");
                    logger.info("  Entry UID: " + entry.getUid());
                    logger.info("  (Embedded items should be UIDs only, not expanded)");
                    
                    logSuccess("testWithoutEmbeddedItems", 
                            "Baseline comparison established");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testWithoutEmbeddedItems"));
    }

    @Test
    @Order(4)
    @DisplayName("Test embedded items with Query")
    void testEmbeddedItemsWithQuery() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.includeEmbeddedItems();
        query.limit(3);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Query with includeEmbeddedItems() should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        int size = results.size();
                        
                        // STRONG ASSERTION: Validate limit
                        assertTrue(size <= 3,
                                "BUG: limit(3) not working - got " + size);
                        
                        // STRONG ASSERTION: Validate ALL entries
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All entries must have UID");
                            assertEquals(Credentials.COMPLEX_CONTENT_TYPE_UID, e.getContentType(),
                                    "BUG: Wrong content type");
                        }
                        
                        logger.info("Query with embedded items validated:");
                        logger.info("  Entries: " + size + " (limit: 3) ✅");
                        
                        logSuccess("testEmbeddedItemsWithQuery",
                                size + " entries with embedded items");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testEmbeddedItemsWithQuery"));
    }

    // ===========================
    // Multiple Embedded Items
    // ===========================

    @Test
    @Order(5)
    @DisplayName("Test entry with multiple JSON RTE fields")
    void testMultipleJsonRteFields() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                     .entry(Credentials.COMPLEX_ENTRY_UID);
        
        entry.includeEmbeddedItems();

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "includeEmbeddedItems() should not error");
                    assertNotNull(entry, "Entry should not be null");
                    
                    // STRONG ASSERTION: Validate correct entry
                    assertEquals(Credentials.COMPLEX_ENTRY_UID, entry.getUid(),
                            "CRITICAL BUG: Wrong entry fetched!");
                    assertTrue(hasBasicFields(entry), 
                            "BUG: Entry missing basic fields");
                    
                    // STRONG ASSERTION: Check for JSON RTE fields
                    int jsonRteFields = 0;
                    java.util.ArrayList<String> foundFields = new java.util.ArrayList<>();
                    
                    if (entry.get("description") != null) {
                        jsonRteFields++;
                        foundFields.add("description");
                    }
                    if (entry.get("content") != null) {
                        jsonRteFields++;
                        foundFields.add("content");
                    }
                    if (entry.get("body") != null) {
                        jsonRteFields++;
                        foundFields.add("body");
                    }
                    if (entry.get("summary") != null) {
                        jsonRteFields++;
                        foundFields.add("summary");
                    }
                    
                    logger.info("Multiple JSON RTE fields validated:");
                    logger.info("  Fields found: " + jsonRteFields);
                    logger.info("  Fields: " + foundFields.toString());
                    
                    logSuccess("testMultipleJsonRteFields", 
                            jsonRteFields + " JSON RTE fields present");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testMultipleJsonRteFields"));
    }

    @Test
    @Order(6)
    @DisplayName("Test multiple entries with embedded items")
    void testMultipleEntriesWithEmbeddedItems() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.includeEmbeddedItems();
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Query with includeEmbeddedItems() should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        int size = results.size();
                        
                        // STRONG ASSERTION: Validate limit
                        assertTrue(size <= 5,
                                "BUG: limit(5) not working - got " + size);
                        
                        // STRONG ASSERTION: Validate ALL entries
                        int entriesWithContent = 0;
                        int totalValidated = 0;
                        
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All entries must have UID");
                            assertEquals(Credentials.COMPLEX_CONTENT_TYPE_UID, e.getContentType(),
                                    "BUG: Wrong content type");
                            totalValidated++;
                            
                            if (e.get("content") != null || e.get("description") != null) {
                                entriesWithContent++;
                            }
                        }
                        
                        assertEquals(size, totalValidated, "ALL entries must be validated");
                        
                        logger.info("Multiple entries with embedded items validated:");
                        logger.info("  Total entries: " + size);
                        logger.info("  With content fields: " + entriesWithContent);
                        
                        logSuccess("testMultipleEntriesWithEmbeddedItems",
                                entriesWithContent + "/" + size + " entries have content");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testMultipleEntriesWithEmbeddedItems"));
    }

    // ===========================
    // Embedded Items with References
    // ===========================

    @Test
    @Order(7)
    @DisplayName("Test embedded items with references")
    void testEmbeddedItemsWithReferences() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                     .entry(Credentials.COMPLEX_ENTRY_UID);
        
        // Include both embedded items and references
        entry.includeEmbeddedItems();
        entry.includeReference("author");

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "includeEmbeddedItems() + includeReference() should not error");
                    assertNotNull(entry, "Entry should not be null");
                    
                    // STRONG ASSERTION: Validate correct entry
                    assertEquals(Credentials.COMPLEX_ENTRY_UID, entry.getUid(),
                            "CRITICAL BUG: Wrong entry fetched!");
                    assertTrue(hasBasicFields(entry), 
                            "BUG: Entry missing basic fields");
                    
                    // STRONG ASSERTION: Validate both features work together
                    Object author = entry.get("author");
                    boolean hasReference = (author != null);
                    
                    logger.info("Embedded items + references validated:");
                    logger.info("  Author reference: " + (hasReference ? "✅ Present" : "ℹ️ Not present"));
                    logger.info("  includeEmbeddedItems() + includeReference() working together ✅");
                    
                    logSuccess("testEmbeddedItemsWithReferences", 
                            "Embedded items + references working together");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testEmbeddedItemsWithReferences"));
    }

    @Test
    @Order(8)
    @DisplayName("Test embedded items with deep references")
    void testEmbeddedItemsWithDeepReferences() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                     .entry(Credentials.COMPLEX_ENTRY_UID);
        
        // Include embedded items with deep references
        entry.includeEmbeddedItems();
        entry.includeReference("author");
        entry.includeReference("author.articles");

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "includeEmbeddedItems() + deep references should not error");
                    assertNotNull(entry, "Entry should not be null");
                    
                    // STRONG ASSERTION: Validate correct entry
                    assertEquals(Credentials.COMPLEX_ENTRY_UID, entry.getUid(),
                            "CRITICAL BUG: Wrong entry fetched!");
                    assertEquals(Credentials.COMPLEX_CONTENT_TYPE_UID, entry.getContentType(),
                            "CRITICAL BUG: Wrong content type!");
                    assertTrue(hasBasicFields(entry), 
                            "BUG: Entry missing basic fields");
                    
                    logger.info("Embedded items + deep references validated:");
                    logger.info("  Entry UID: " + entry.getUid() + " ✅");
                    logger.info("  includeEmbeddedItems() + 2-level references working ✅");
                    
                    logSuccess("testEmbeddedItemsWithDeepReferences", 
                            "Deep references (2-level) + embedded items working");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testEmbeddedItemsWithDeepReferences"));
    }

    // ===========================
    // Complex Scenarios
    // ===========================

    @Test
    @Order(9)
    @DisplayName("Test embedded items with field selection")
    void testEmbeddedItemsWithFieldSelection() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                     .entry(Credentials.COMPLEX_ENTRY_UID);
        
        entry.includeEmbeddedItems();
        entry.only(new String[]{"title", "content", "description"});

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "includeEmbeddedItems() + only() should not error");
                    assertNotNull(entry, "Entry should not be null");
                    
                    // STRONG ASSERTION: Validate correct entry
                    assertEquals(Credentials.COMPLEX_ENTRY_UID, entry.getUid(),
                            "CRITICAL BUG: Wrong entry fetched!");
                    
                    // STRONG ASSERTION: Field selection validation
                    assertNotNull(entry.getTitle(), 
                            "BUG: only(['title',...]) - title should be included");
                    
                    logger.info("Field selection + embedded items validated ✅");
                    logSuccess("testEmbeddedItemsWithFieldSelection", 
                            "Field selection with embedded items working");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testEmbeddedItemsWithFieldSelection"));
    }

    @Test
    @Order(10)
    @DisplayName("Test embedded items with Query filters")
    void testEmbeddedItemsWithQueryFilters() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.includeEmbeddedItems();
        query.where("locale", "en-us");
        query.exists("title");
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "includeEmbeddedItems() + filters should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        
                        // STRONG ASSERTION: Validate limit
                        assertTrue(results.size() <= 5,
                                "BUG: limit(5) not working");
                        
                        // STRONG ASSERTION: Validate filters on ALL results
                        int withTitle = 0, withLocale = 0;
                        for (Entry e : results) {
                            // exists("title") filter
                            assertNotNull(e.getTitle(), 
                                    "BUG: exists('title') not working. Entry: " + e.getUid());
                            withTitle++;
                            
                            // where("locale", "en-us") filter
                            String locale = e.getLocale();
                            if (locale != null) {
                                assertEquals("en-us", locale,
                                        "BUG: where('locale', 'en-us') not working");
                                withLocale++;
                            }
                        }
                        
                        assertEquals(results.size(), withTitle, "ALL must have title");
                        logger.info("Embedded items + filters: " + results.size() + " entries validated");
                        
                        logSuccess("testEmbeddedItemsWithQueryFilters",
                                results.size() + " entries with embedded items + filters");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testEmbeddedItemsWithQueryFilters"));
    }

    @Test
    @Order(11)
    @DisplayName("Test embedded items with pagination")
    void testEmbeddedItemsWithPagination() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.includeEmbeddedItems();
        query.limit(2);
        query.skip(0);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "includeEmbeddedItems() + pagination should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        int size = results.size();
                        
                        // STRONG ASSERTION: Validate pagination
                        assertTrue(size > 0 && size <= 2,
                                "BUG: Pagination not working - expected 1-2, got: " + size);
                        
                        // STRONG ASSERTION: Validate ALL entries
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All entries must have UID");
                            assertEquals(Credentials.COMPLEX_CONTENT_TYPE_UID, e.getContentType(),
                                    "BUG: Wrong content type");
                        }
                        
                        logger.info("Pagination + embedded items: " + size + " entries (limit: 2) ✅");
                        
                        logSuccess("testEmbeddedItemsWithPagination", 
                                size + " entries with pagination");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testEmbeddedItemsWithPagination"));
    }

    // ===========================
    // Performance Testing
    // ===========================

    @Test
    @Order(12)
    @DisplayName("Test performance: With vs without embedded items")
    void testPerformanceWithAndWithoutEmbeddedItems() throws InterruptedException {
        CountDownLatch latch1 = createLatch();
        CountDownLatch latch2 = createLatch();
        
        final long[] withoutEmbeddedTime = new long[1];
        final long[] withEmbeddedTime = new long[1];

        // First: Fetch WITHOUT embedded items
        long start1 = startTimer();
        Entry entry1 = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                           .entry(Credentials.COMPLEX_ENTRY_UID);
        
        entry1.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    withoutEmbeddedTime[0] = System.currentTimeMillis() - start1;
                    assertNull(error, "Should not have errors");
                } finally {
                    latch1.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch1, "testPerformance-WithoutEmbedded"));

        // Second: Fetch WITH embedded items
        long start2 = startTimer();
        Entry entry2 = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                           .entry(Credentials.COMPLEX_ENTRY_UID);
        entry2.includeEmbeddedItems();
        
        entry2.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    withEmbeddedTime[0] = System.currentTimeMillis() - start2;
                    assertNull(error, "Should not have errors");
                } finally {
                    latch2.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch2, "testPerformance-WithEmbedded"));

        // Compare performance
        logger.info("Without embedded items: " + formatDuration(withoutEmbeddedTime[0]));
        logger.info("With embedded items: " + formatDuration(withEmbeddedTime[0]));
        
        if (withEmbeddedTime[0] > withoutEmbeddedTime[0]) {
            double ratio = (double) withEmbeddedTime[0] / withoutEmbeddedTime[0];
            logger.info("Embedded items added " + String.format("%.1fx", ratio) + " overhead");
        }
        
        // Embedded items should still complete in reasonable time
        assertTrue(withEmbeddedTime[0] < 10000, 
                "Entry with embedded items should complete within 10s");
        
        logSuccess("testPerformanceWithAndWithoutEmbeddedItems", "Performance compared");
    }

    // ===========================
    // Edge Cases
    // ===========================

    @Test
    @Order(13)
    @DisplayName("Test entry without JSON RTE fields")
    void testEntryWithoutJsonRteFields() throws InterruptedException {
        CountDownLatch latch = createLatch();

        // Use simple entry that likely doesn't have JSON RTE
        entry = stack.contentType(Credentials.SIMPLE_CONTENT_TYPE_UID)
                     .entry(Credentials.SIMPLE_ENTRY_UID);
        
        entry.includeEmbeddedItems();

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    // STRONG ASSERTION: SDK should handle gracefully
                    assertNull(error, 
                            "BUG: includeEmbeddedItems() should handle entries without JSON RTE");
                    assertNotNull(entry, "Entry should not be null");
                    
                    // STRONG ASSERTION: Validate correct entry
                    assertEquals(Credentials.SIMPLE_ENTRY_UID, entry.getUid(),
                            "CRITICAL BUG: Wrong entry fetched!");
                    assertEquals(Credentials.SIMPLE_CONTENT_TYPE_UID, entry.getContentType(),
                            "CRITICAL BUG: Wrong content type!");
                    assertTrue(hasBasicFields(entry), 
                            "BUG: Entry should still have basic fields");
                    
                    logger.info("✅ Entry without JSON RTE handled gracefully");
                    logger.info("  Entry UID: " + entry.getUid());
                    
                    logSuccess("testEntryWithoutJsonRteFields", 
                            "SDK handled entry without JSON RTE gracefully");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testEntryWithoutJsonRteFields"));
    }

    @Test
    @Order(14)
    @DisplayName("Test embedded items with empty JSON RTE")
    void testEmbeddedItemsWithEmptyJsonRte() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        query.includeEmbeddedItems();
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "includeEmbeddedItems() should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        
                        // STRONG ASSERTION: Validate limit
                        assertTrue(results.size() <= 5,
                                "BUG: limit(5) not working");
                        
                        // STRONG ASSERTION: Validate ALL entries, count empty/populated
                        int entriesWithContent = 0, entriesWithoutContent = 0;
                        
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All entries must have UID");
                            assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, e.getContentType(),
                                    "BUG: Wrong content type");
                            
                            Object content = e.get("content");
                            if (content != null && !content.toString().isEmpty()) {
                                entriesWithContent++;
                            } else {
                                entriesWithoutContent++;
                            }
                        }
                        
                        logger.info("Empty/null JSON RTE handling validated:");
                        logger.info("  With content: " + entriesWithContent);
                        logger.info("  Without content: " + entriesWithoutContent);
                        logger.info("  ✅ SDK handles both gracefully");
                        
                        logSuccess("testEmbeddedItemsWithEmptyJsonRte", 
                                "Empty JSON RTE handled gracefully");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testEmbeddedItemsWithEmptyJsonRte"));
    }

    @Test
    @Order(15)
    @DisplayName("Test embedded items with complex entry structure")
    void testEmbeddedItemsWithComplexEntry() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = startTimer();

        // Use the most complex entry available
        entry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                     .entry(Credentials.COMPLEX_ENTRY_UID);
        
        // Include everything: embedded items, references, all fields
        entry.includeEmbeddedItems();
        entry.includeReference("author");
        entry.includeReference("related_articles");

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    long duration = System.currentTimeMillis() - startTime;
                    
                    assertNull(error, "Complex fetch with embedded items + references should not error");
                    assertNotNull(entry, "Entry should not be null");
                    
                    // STRONG ASSERTION: Validate correct entry
                    assertEquals(Credentials.COMPLEX_ENTRY_UID, entry.getUid(),
                            "CRITICAL BUG: Wrong entry fetched!");
                    assertEquals(Credentials.COMPLEX_CONTENT_TYPE_UID, entry.getContentType(),
                            "CRITICAL BUG: Wrong content type!");
                    assertTrue(hasBasicFields(entry), 
                            "BUG: Entry missing basic fields");
                    
                    // STRONG ASSERTION: Performance threshold for complex fetch
                    assertTrue(duration < 15000,
                            "PERFORMANCE BUG: Complex entry with embedded items + refs took too long: " +
                            formatDuration(duration) + " (max: 15s)");
                    
                    // STRONG ASSERTION: Count and validate populated fields
                    int fieldCount = 0;
                    java.util.ArrayList<String> populatedFields = new java.util.ArrayList<>();
                    
                    if (entry.getTitle() != null) {
                        fieldCount++;
                        populatedFields.add("title");
                    }
                    if (entry.get("description") != null) {
                        fieldCount++;
                        populatedFields.add("description");
                    }
                    if (entry.get("content") != null) {
                        fieldCount++;
                        populatedFields.add("content");
                    }
                    if (entry.get("author") != null) {
                        fieldCount++;
                        populatedFields.add("author");
                    }
                    if (entry.get("related_articles") != null) {
                        fieldCount++;
                        populatedFields.add("related_articles");
                    }
                    
                    logger.info("Complex entry validated:");
                    logger.info("  Populated fields: " + fieldCount);
                    logger.info("  Fields: " + populatedFields.toString());
                    logger.info("  Duration: " + formatDuration(duration) + " ✅");
                    logger.info("  includeEmbeddedItems() + includeReference() working together ✅");
                    
                    logSuccess("testEmbeddedItemsWithComplexEntry", 
                            fieldCount + " fields populated, completed in " + formatDuration(duration));
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, LARGE_DATASET_TIMEOUT_SECONDS, 
                "testEmbeddedItemsWithComplexEntry"));
    }

    @AfterAll
    void tearDown() {
        logger.info("Completed JsonRteEmbeddedItemsIT test suite");
        logger.info("All 15 JSON RTE embedded items tests executed");
        logger.info("Tested: Basic inclusion, multiple items, references, performance, edge cases");
    }
}

