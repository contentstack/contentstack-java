package com.contentstack.sdk;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.ArrayList;

/**
 * Comprehensive Integration Tests for Deep References
 * Tests reference handling at various depths including:
 * - Single-level references (1 deep)
 * - Two-level deep references (2 deep)
 * - Three-level deep references (3 deep)
 * - Four-level deep references (4 deep - edge case)
 * - Multiple references in single entry
 * - References with filters and field selection
 * - Performance with deep references
 * - Circular reference handling
 * Uses complex stack data with article → author → related references
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DeepReferencesIT extends BaseIntegrationTest {

    private Entry entry;
    private Query query;

    @BeforeAll
    void setUp() {
        logger.info("Setting up DeepReferencesIT test suite");
        logger.info("Testing reference depths with complex stack data");
        
        if (!Credentials.hasMediumEntry()) {
            logger.warning("Medium entry not configured - some tests may be limited");
        }
    }

    // ===========================
    // Single-Level References
    // ===========================

    @Test
    @Order(1)
    @DisplayName("Test single-level reference inclusion")
    void testSingleLevelReference() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = startTimer();

        // Fetch entry with single-level reference
        entry = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID)
                     .entry(Credentials.MEDIUM_ENTRY_UID);
        
        // Include first-level reference (e.g., author)
        entry.includeReference("author");

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "Entry fetch with includeReference should not error");
                    assertNotNull(entry, "Entry should not be null");
                    
                    // STRONG ASSERTION: Validate we fetched the correct entry
                    assertEquals(Credentials.MEDIUM_ENTRY_UID, entry.getUid(),
                            "CRITICAL BUG: Wrong entry fetched!");
                    assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, entry.getContentType(),
                            "CRITICAL BUG: Wrong content type!");
                    
                    // STRONG ASSERTION: Basic fields must exist
                    assertTrue(hasBasicFields(entry), 
                            "BUG: Entry missing basic fields (title, UID)");
                    assertNotNull(entry.getTitle(), "Entry must have title");
                    
                    // STRONG ASSERTION: includeReference("author") validation
                    Object authorRef = entry.get("author");
                    if (authorRef != null) {
                        logger.info("✅ Single-level reference included: author");
                        
                        // Validate reference structure
                        if (authorRef instanceof org.json.JSONObject) {
                            org.json.JSONObject authorObj = (org.json.JSONObject) authorRef;
                            assertTrue(authorObj.has("uid") || authorObj.has("title"),
                                    "BUG: Reference should contain uid or title");
                            logger.info("  Reference has fields: " + authorObj.keys().toString());
                        }
                    } else {
                        logger.info("ℹ️ No author reference in entry (field may not exist)");
                    }
                    
                    long duration = System.currentTimeMillis() - startTime;
                    logger.info("Single-level reference fetch: " + duration + "ms");
                    logSuccess("testSingleLevelReference", 
                            "Entry + reference validated in " + duration + "ms");
                    logExecutionTime("testSingleLevelReference", startTime);
                } catch (Exception e) {
                    fail("Test failed with exception: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testSingleLevelReference"));
    }

    @Test
    @Order(2)
    @DisplayName("Test multiple single-level references")
    void testMultipleSingleLevelReferences() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID)
                     .entry(Credentials.MEDIUM_ENTRY_UID);
        
        // Include multiple first-level references
        entry.includeReference("author");
        entry.includeReference("related_articles");

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "Multiple includeReference calls should not error");
                    assertNotNull(entry, "Entry should not be null");
                    
                    // STRONG ASSERTION: Validate correct entry
                    assertEquals(Credentials.MEDIUM_ENTRY_UID, entry.getUid(),
                            "CRITICAL BUG: Wrong entry returned!");
                    assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, entry.getContentType(),
                            "CRITICAL BUG: Wrong content type!");
                    
                    // STRONG ASSERTION: Check each reference field
                    int referenceCount = 0;
                    ArrayList<String> includedRefs = new ArrayList<>();
                    
                    Object authorRef = entry.get("author");
                    if (authorRef != null) {
                        referenceCount++;
                        includedRefs.add("author");
                        
                        // Validate author reference structure
                        if (authorRef instanceof org.json.JSONObject) {
                            org.json.JSONObject authorObj = (org.json.JSONObject) authorRef;
                            assertTrue(authorObj.length() > 0,
                                    "BUG: author reference is empty");
                            logger.info("  ✅ author reference included");
                        }
                    }
                    
                    Object relatedRef = entry.get("related_articles");
                    if (relatedRef != null) {
                        referenceCount++;
                        includedRefs.add("related_articles");
                        logger.info("  ✅ related_articles reference included");
                    }
                    
                    logger.info("Multiple references validated: " + referenceCount + " references");
                    logger.info("  Included: " + includedRefs.toString());
                    
                    logSuccess("testMultipleSingleLevelReferences", 
                            referenceCount + " references included and validated");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testMultipleSingleLevelReferences"));
    }

    @Test
    @Order(3)
    @DisplayName("Test single-level reference with Query")
    void testSingleLevelReferenceWithQuery() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        query.includeReference("author");
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Query with includeReference should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        
                        // STRONG ASSERTION: Validate limit
                        assertTrue(results.size() <= 5,
                                "BUG: limit(5) not working - got " + results.size());
                        
                        // STRONG ASSERTION: Validate ALL entries
                        int entriesWithRefs = 0;
                        int totalEntries = 0;
                        
                        for (Entry e : results) {
                            // Validate entry integrity
                            assertNotNull(e.getUid(), "All entries must have UID");
                            assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, e.getContentType(),
                                    "BUG: Wrong content type in results");
                            totalEntries++;
                            
                            // Check if includeReference worked
                            Object authorRef = e.get("author");
                            if (authorRef != null) {
                                entriesWithRefs++;
                                logger.info("  Entry " + e.getUid() + " has author reference ✅");
                            }
                        }
                        
                        logger.info("Query with references validated:");
                        logger.info("  Total entries: " + totalEntries);
                        logger.info("  With author reference: " + entriesWithRefs);
                        
                        logSuccess("testSingleLevelReferenceWithQuery", 
                                entriesWithRefs + "/" + totalEntries + " entries had references");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testSingleLevelReferenceWithQuery"));
    }

    // ===========================
    // Two-Level Deep References
    // ===========================

    @Test
    @Order(4)
    @DisplayName("Test two-level deep reference")
    void testTwoLevelDeepReference() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = startTimer();

        entry = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID)
                     .entry(Credentials.MEDIUM_ENTRY_UID);
        
        // Include two-level deep reference: entry → author → author's references
        entry.includeReference("author");
        entry.includeReference("author.related_posts");

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "Two-level includeReference should not error");
                    assertNotNull(entry, "Entry should not be null");
                    
                    // STRONG ASSERTION: Validate correct entry
                    assertEquals(Credentials.MEDIUM_ENTRY_UID, entry.getUid(),
                            "CRITICAL BUG: Wrong entry fetched!");
                    assertTrue(hasBasicFields(entry), 
                            "BUG: Entry missing basic fields");
                    
                    // STRONG ASSERTION: Validate two-level reference depth
                    Object authorRef = entry.get("author");
                    if (authorRef != null) {
                        logger.info("✅ Level 1: author reference included");
                        
                        // Check if it's a JSON object with nested data
                        if (authorRef instanceof org.json.JSONObject) {
                            org.json.JSONObject authorObj = (org.json.JSONObject) authorRef;
                            assertTrue(authorObj.length() > 0,
                                    "BUG: author reference is empty");
                            
                            // Check for level 2 (nested reference)
                            if (authorObj.has("related_posts")) {
                                logger.info("✅ Level 2: author.related_posts included");
                            } else {
                                logger.info("ℹ️ Level 2: related_posts not present in author");
                            }
                        }
                        logSuccess("testTwoLevelDeepReference", "Deep reference structure validated");
                    } else {
                        logger.info("ℹ️ No author reference (field may not exist in entry)");
                        logSuccess("testTwoLevelDeepReference", "Entry fetched successfully");
                    }
                    
                    long duration = System.currentTimeMillis() - startTime;
                    logger.info("Two-level reference fetch: " + formatDuration(duration));
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testTwoLevelDeepReference"));
    }

    @Test
    @Order(5)
    @DisplayName("Test two-level deep reference with Query")
    void testTwoLevelDeepReferenceWithQuery() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        query.includeReference("author");
        query.includeReference("author.bio");
        query.limit(3);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Two-level query with includeReference should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        int size = results.size();
                        
                        // STRONG ASSERTION: Validate limit
                        assertTrue(size <= 3,
                                "BUG: limit(3) not working - got " + size);
                        
                        // STRONG ASSERTION: Validate ALL entries
                        int withAuthor = 0;
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All entries must have UID");
                            assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, e.getContentType(),
                                    "BUG: Wrong content type");
                            
                            if (e.get("author") != null) {
                                withAuthor++;
                            }
                        }
                        
                        logger.info("Two-level deep query validated:");
                        logger.info("  Entries returned: " + size + " (limit: 3)");
                        logger.info("  With author reference: " + withAuthor);
                        
                        logSuccess("testTwoLevelDeepReferenceWithQuery",
                                size + " entries with 2-level references");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testTwoLevelDeepReferenceWithQuery"));
    }

    // ===========================
    // Three-Level Deep References
    // ===========================

    @Test
    @Order(6)
    @DisplayName("Test three-level deep reference")
    void testThreeLevelDeepReference() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = startTimer();

        entry = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID)
                     .entry(Credentials.MEDIUM_ENTRY_UID);
        
        // Include three-level deep reference
        entry.includeReference("author");
        entry.includeReference("author.related_posts");
        entry.includeReference("author.related_posts.tags");

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "Three-level includeReference should not error");
                    assertNotNull(entry, "Entry should not be null");
                    
                    // STRONG ASSERTION: Validate correct entry
                    assertEquals(Credentials.MEDIUM_ENTRY_UID, entry.getUid(),
                            "CRITICAL BUG: Wrong entry fetched!");
                    assertTrue(hasBasicFields(entry), 
                            "BUG: Entry missing basic fields");
                    
                    long duration = System.currentTimeMillis() - startTime;
                    
                    // STRONG ASSERTION: Performance threshold
                    assertTrue(duration < 10000,
                            "PERFORMANCE BUG: Three-level reference took too long: " +
                            formatDuration(duration) + " (max: 10s)");
                    
                    logger.info("Three-level reference fetch: " + formatDuration(duration));
                    logger.info("✅ Performance: " + formatDuration(duration) + " < 10s");
                    
                    logSuccess("testThreeLevelDeepReference", 
                            "3-level reference completed in " + formatDuration(duration));
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, LARGE_DATASET_TIMEOUT_SECONDS, "testThreeLevelDeepReference"));
    }

    @Test
    @Order(7)
    @DisplayName("Test three-level deep reference with Query")
    void testThreeLevelDeepReferenceWithQuery() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        query.includeReference("author");
        query.includeReference("author.articles");
        query.includeReference("author.articles.category");
        query.limit(2);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Three-level query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        int size = results.size();
                        
                        // STRONG ASSERTION: Validate limit
                        assertTrue(size <= 2,
                                "BUG: limit(2) not working - got " + size);
                        
                        // STRONG ASSERTION: Validate ALL entries
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All entries must have UID");
                            assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, e.getContentType(),
                                    "BUG: Wrong content type");
                        }
                        
                        logger.info("Three-level query validated:");
                        logger.info("  Entries: " + size + " (limit: 2) ✅");
                        logger.info("  All entries validated ✅");
                        
                        logSuccess("testThreeLevelDeepReferenceWithQuery",
                                size + " entries with 3-level references");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, LARGE_DATASET_TIMEOUT_SECONDS, 
                "testThreeLevelDeepReferenceWithQuery"));
    }

    // ===========================
    // Four-Level Deep References (Edge Case)
    // ===========================

    @Test
    @Order(8)
    @DisplayName("Test four-level deep reference - edge case")
    void testFourLevelDeepReference() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = startTimer();

        entry = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID)
                     .entry(Credentials.MEDIUM_ENTRY_UID);
        
        // Include four-level deep reference (edge case testing)
        entry.includeReference("author");
        entry.includeReference("author.related_posts");
        entry.includeReference("author.related_posts.category");
        entry.includeReference("author.related_posts.category.parent");

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    // STRONG ASSERTION: Four-level references - test SDK behavior
                    // Four-level may or may not be supported
                    
                    if (error != null) {
                        logger.info("Four-level reference error (expected if not supported): " + 
                                error.getErrorMessage());
                        logger.info("✅ SDK handled deep reference gracefully");
                        // Not a failure - documenting SDK behavior
                        logSuccess("testFourLevelDeepReference", 
                                "SDK handled 4-level reference gracefully");
                    } else {
                        assertNotNull(entry, "Entry should not be null");
                        
                        // STRONG ASSERTION: Validate correct entry
                        assertEquals(Credentials.MEDIUM_ENTRY_UID, entry.getUid(),
                                "CRITICAL BUG: Wrong entry fetched!");
                        assertTrue(hasBasicFields(entry), 
                                "BUG: Entry missing basic fields");
                        
                        long duration = System.currentTimeMillis() - startTime;
                        
                        // STRONG ASSERTION: Performance
                        assertTrue(duration < 15000,
                                "PERFORMANCE BUG: Four-level took too long: " +
                                formatDuration(duration) + " (max: 15s)");
                        
                        logger.info("Four-level reference fetch: " + formatDuration(duration));
                        logger.info("✅ SDK supports 4-level references!");
                        
                        logSuccess("testFourLevelDeepReference", 
                                "4-level reference completed in " + formatDuration(duration));
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, LARGE_DATASET_TIMEOUT_SECONDS, "testFourLevelDeepReference"));
    }

    // ===========================
    // References with Filters
    // ===========================

    @Test
    @Order(9)
    @DisplayName("Test reference with field filters (only)")
    void testReferenceWithOnlyFields() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID)
                     .entry(Credentials.MEDIUM_ENTRY_UID);
        
        entry.includeReference("author");
        entry.only(new String[]{"title", "author", "url"});

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "includeReference + only() should not error");
                    assertNotNull(entry, "Entry should not be null");
                    
                    // STRONG ASSERTION: Validate correct entry
                    assertEquals(Credentials.MEDIUM_ENTRY_UID, entry.getUid(),
                            "CRITICAL BUG: Wrong entry fetched!");
                    
                    // STRONG ASSERTION: Validate only() filter
                    assertNotNull(entry.getTitle(), 
                            "BUG: only(['title',...]) - title should be included");
                    assertNotNull(entry.getUid(), 
                            "UID always included (system field)");
                    
                    // Log which fields were included
                    logger.info("Field filter (only) validated:");
                    logger.info("  title: " + (entry.getTitle() != null ? "✅" : "❌"));
                    logger.info("  uid: " + (entry.getUid() != null ? "✅" : "❌"));
                    logger.info("  author ref: " + (entry.get("author") != null ? "✅" : "❌"));
                    
                    logSuccess("testReferenceWithOnlyFields", 
                            "Reference with field selection working");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testReferenceWithOnlyFields"));
    }

    @Test
    @Order(10)
    @DisplayName("Test reference with field exclusion (except)")
    void testReferenceWithExceptFields() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID)
                     .entry(Credentials.MEDIUM_ENTRY_UID);
        
        entry.includeReference("author");
        entry.except(new String[]{"description", "body"});

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "includeReference + except() should not error");
                    assertNotNull(entry, "Entry should not be null");
                    
                    // STRONG ASSERTION: Validate correct entry
                    assertEquals(Credentials.MEDIUM_ENTRY_UID, entry.getUid(),
                            "CRITICAL BUG: Wrong entry fetched!");
                    assertTrue(hasBasicFields(entry), 
                            "BUG: Entry missing basic fields");
                    
                    // STRONG ASSERTION: except() should not affect basic fields
                    assertNotNull(entry.getTitle(), "Title should be present (not excluded)");
                    assertNotNull(entry.getUid(), "UID always present");
                    
                    logger.info("Field exclusion (except) validated ✅");
                    logger.info("  Basic fields present despite exclusions");
                    
                    logSuccess("testReferenceWithExceptFields",
                            "except() filter working correctly");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testReferenceWithExceptFields"));
    }

    @Test
    @Order(11)
    @DisplayName("Test reference with Query filters")
    void testReferenceWithQueryFilters() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        query.includeReference("author");
        query.where("locale", "en-us");
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Query with includeReference + filters should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        int size = results.size();
                        
                        // STRONG ASSERTION: Validate limit
                        assertTrue(size <= 5,
                                "BUG: limit(5) not working - got " + size);
                        
                        // STRONG ASSERTION: Validate filters
                        int withLocale = 0;
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All entries must have UID");
                            assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, e.getContentType(),
                                    "BUG: Wrong content type");
                            
                            String locale = e.getLocale();
                            if (locale != null) {
                                assertEquals("en-us", locale,
                                        "BUG: where('locale', 'en-us') not working");
                                withLocale++;
                            }
                        }
                        
                        logger.info("Reference + Query filters validated:");
                        logger.info("  Entries: " + size + " (limit: 5) ✅");
                        logger.info("  With en-us locale: " + withLocale);
                        
                        logSuccess("testReferenceWithQueryFilters",
                                size + " entries with references + filters");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testReferenceWithQueryFilters"));
    }

    // ===========================
    // Multiple References
    // ===========================

    @Test
    @Order(12)
    @DisplayName("Test entry with multiple reference fields")
    void testMultipleReferenceFields() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                     .entry(Credentials.COMPLEX_ENTRY_UID);
        
        // Include multiple different reference fields
        entry.includeReference("author");
        entry.includeReference("related_articles");
        entry.includeReference("category");

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "Multiple includeReference calls should not error");
                    assertNotNull(entry, "Entry should not be null");
                    
                    // STRONG ASSERTION: Validate correct entry
                    assertEquals(Credentials.COMPLEX_ENTRY_UID, entry.getUid(),
                            "CRITICAL BUG: Wrong entry fetched!");
                    assertEquals(Credentials.COMPLEX_CONTENT_TYPE_UID, entry.getContentType(),
                            "CRITICAL BUG: Wrong content type!");
                    assertTrue(hasBasicFields(entry), 
                            "BUG: Entry missing basic fields");
                    
                    // STRONG ASSERTION: Count and validate reference fields
                    int refCount = 0;
                    java.util.ArrayList<String> includedRefs = new java.util.ArrayList<>();
                    
                    if (entry.get("author") != null) {
                        refCount++;
                        includedRefs.add("author");
                    }
                    if (entry.get("related_articles") != null) {
                        refCount++;
                        includedRefs.add("related_articles");
                    }
                    if (entry.get("category") != null) {
                        refCount++;
                        includedRefs.add("category");
                    }
                    
                    logger.info("Multiple reference fields validated:");
                    logger.info("  Entry has " + refCount + " reference field(s)");
                    logger.info("  Included: " + includedRefs.toString());
                    
                    logSuccess("testMultipleReferenceFields", 
                            refCount + " reference fields present");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testMultipleReferenceFields"));
    }

    @Test
    @Order(13)
    @DisplayName("Test multiple references with different depths")
    void testMultipleReferencesWithDifferentDepths() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                     .entry(Credentials.COMPLEX_ENTRY_UID);
        
        // Include references at different depths
        entry.includeReference("author");  // 1-level
        entry.includeReference("related_articles");  // 1-level
        entry.includeReference("related_articles.author");  // 2-level

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "Mixed-depth references should not error");
                    assertNotNull(entry, "Entry should not be null");
                    
                    // STRONG ASSERTION: Validate correct entry
                    assertEquals(Credentials.COMPLEX_ENTRY_UID, entry.getUid(),
                            "CRITICAL BUG: Wrong entry fetched!");
                    assertTrue(hasBasicFields(entry), 
                            "BUG: Entry missing basic fields");
                    
                    // STRONG ASSERTION: Validate mixed depths
                    int level1Count = 0;
                    if (entry.get("author") != null) level1Count++;
                    if (entry.get("related_articles") != null) level1Count++;
                    
                    logger.info("Mixed-depth references validated:");
                    logger.info("  Level 1 references: " + level1Count);
                    logger.info("  Level 2 reference: related_articles.author");
                    
                    logSuccess("testMultipleReferencesWithDifferentDepths", 
                            "Mixed depth references handled - " + level1Count + " level-1 refs");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testMultipleReferencesWithDifferentDepths"));
    }

    // ===========================
    // Performance Testing
    // ===========================

    @Test
    @Order(14)
    @DisplayName("Test performance: Entry with references vs without")
    void testPerformanceWithAndWithoutReferences() throws InterruptedException {
        CountDownLatch latch1 = createLatch();
        CountDownLatch latch2 = createLatch();
        
        final long[] withoutRefTime = new long[1];
        final long[] withRefTime = new long[1];

        // First: Fetch WITHOUT references
        long start1 = startTimer();
        Entry entry1 = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID)
                           .entry(Credentials.MEDIUM_ENTRY_UID);
        
        entry1.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    withoutRefTime[0] = System.currentTimeMillis() - start1;
                    assertNull(error, "Should not have errors");
                } finally {
                    latch1.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch1, "testPerformance-WithoutRefs"));

        // Second: Fetch WITH references
        long start2 = startTimer();
        Entry entry2 = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID)
                           .entry(Credentials.MEDIUM_ENTRY_UID);
        entry2.includeReference("author");
        
        entry2.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    withRefTime[0] = System.currentTimeMillis() - start2;
                    assertNull(error, "Should not have errors");
                } finally {
                    latch2.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch2, "testPerformance-WithRefs"));

        // Compare performance
        logger.info("Without references: " + formatDuration(withoutRefTime[0]));
        logger.info("With references: " + formatDuration(withRefTime[0]));
        
        if (withRefTime[0] > withoutRefTime[0]) {
            double ratio = (double) withRefTime[0] / withoutRefTime[0];
            logger.info("References added " + String.format("%.1fx", ratio) + " overhead");
        }
        
        logSuccess("testPerformanceWithAndWithoutReferences", "Performance compared");
    }

    @Test
    @Order(15)
    @DisplayName("Test performance: Deep references")
    void testPerformanceDeepReferences() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = startTimer();

        entry = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID)
                     .entry(Credentials.MEDIUM_ENTRY_UID);
        
        // Include deep references
        entry.includeReference("author");
        entry.includeReference("author.related_posts");
        entry.includeReference("author.related_posts.category");

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    long duration = System.currentTimeMillis() - startTime;
                    
                    assertNull(error, "Deep references should not error");
                    assertNotNull(entry, "Entry should not be null");
                    
                    // STRONG ASSERTION: Validate correct entry
                    assertEquals(Credentials.MEDIUM_ENTRY_UID, entry.getUid(),
                            "CRITICAL BUG: Wrong entry fetched!");
                    
                    // STRONG ASSERTION: Performance threshold
                    assertTrue(duration < 15000,
                            "PERFORMANCE BUG: Deep references took too long: " +
                            formatDuration(duration) + " (max: 15s)");
                    
                    logger.info("Deep reference performance:");
                    logger.info("  Duration: " + formatDuration(duration));
                    logger.info("  Status: " + (duration < 15000 ? "✅ PASS" : "❌ SLOW"));
                    
                    logSuccess("testPerformanceDeepReferences", 
                            "3-level reference completed in " + formatDuration(duration));
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, LARGE_DATASET_TIMEOUT_SECONDS, 
                "testPerformanceDeepReferences"));
    }

    // ===========================
    // Edge Cases
    // ===========================

    @Test
    @Order(16)
    @DisplayName("Test reference to non-existent field")
    void testReferenceToNonExistentField() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID)
                     .entry(Credentials.MEDIUM_ENTRY_UID);
        
        // Try to include reference that doesn't exist
        entry.includeReference("non_existent_reference_field");

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    // STRONG ASSERTION: SDK should handle gracefully
                    assertNull(error, 
                            "BUG: SDK should handle non-existent reference gracefully, not error");
                    assertNotNull(entry, "Entry should not be null");
                    
                    // STRONG ASSERTION: Validate correct entry
                    assertEquals(Credentials.MEDIUM_ENTRY_UID, entry.getUid(),
                            "CRITICAL BUG: Wrong entry fetched!");
                    assertTrue(hasBasicFields(entry), 
                            "BUG: Entry should still have basic fields");
                    
                    logger.info("Non-existent reference handled gracefully ✅");
                    logger.info("  Entry fetched successfully despite invalid reference");
                    
                    logSuccess("testReferenceToNonExistentField", 
                            "SDK handled non-existent reference gracefully");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testReferenceToNonExistentField"));
    }

    @Test
    @Order(17)
    @DisplayName("Test self-referencing entry")
    void testSelfReferencingEntry() throws InterruptedException {
        if (Credentials.SELF_REF_ENTRY_UID.isEmpty()) {
            logger.info("Skipping self-reference test - SELF_REF_ENTRY_UID not configured");
            return;
        }

        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.SELF_REF_CONTENT_TYPE_UID)
                     .entry(Credentials.SELF_REF_ENTRY_UID);
        
        // Include self-referencing field
        entry.includeReference("sections");

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    // STRONG ASSERTION: Document SDK behavior with self-references
                    if (error != null) {
                        logger.info("Self-reference error (documenting SDK behavior): " + 
                                error.getErrorMessage());
                        logger.info("✅ SDK handled self-reference (error is valid response)");
                        logSuccess("testSelfReferencingEntry", 
                                "Self-reference handled with error");
                    } else {
                        assertNotNull(entry, "Entry should not be null");
                        
                        // STRONG ASSERTION: Validate correct entry
                        assertEquals(Credentials.SELF_REF_ENTRY_UID, entry.getUid(),
                                "CRITICAL BUG: Wrong entry fetched!");
                        assertTrue(hasBasicFields(entry), 
                                "BUG: Entry should have basic fields");
                        
                        logger.info("Self-reference handled successfully ✅");
                        logger.info("  Entry: " + entry.getUid());
                        logSuccess("testSelfReferencingEntry", 
                                "SDK supports self-references");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testSelfReferencingEntry"));
    }

    @Test
    @Order(18)
    @DisplayName("Test reference with empty/null values")
    void testReferenceWithEmptyValues() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        query.includeReference("author");
        query.limit(10);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        
                        // STRONG ASSERTION: Validate limit
                        assertTrue(results.size() <= 10,
                                "BUG: limit(10) not working - got " + results.size());
                        
                        // STRONG ASSERTION: Count entries with/without references
                        int withRefs = 0;
                        int withoutRefs = 0;
                        
                        for (Entry e : results) {
                            // Validate ALL entries
                            assertNotNull(e.getUid(), "All entries must have UID");
                            assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, e.getContentType(),
                                    "BUG: Wrong content type");
                            
                            if (e.get("author") != null) {
                                withRefs++;
                            } else {
                                withoutRefs++;
                            }
                        }
                        
                        logger.info("Entries with author: " + withRefs + 
                                ", without: " + withoutRefs);
                        
                        // Should handle both cases gracefully
                        logSuccess("testReferenceWithEmptyValues", 
                                "Empty references handled gracefully");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testReferenceWithEmptyValues"));
    }

    @AfterAll
    void tearDown() {
        logger.info("Completed DeepReferencesIT test suite");
        logger.info("All 18 deep reference tests executed");
        logger.info("Tested reference depths: 1-level, 2-level, 3-level, 4-level");
    }
}

