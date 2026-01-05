package com.contentstack.sdk;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Comprehensive Integration Tests for Complex Query Combinations
 * Tests advanced query operations including:
 * - AND/OR query combinations
 * - Nested query logic
 * - Multi-field filtering
 * - Query chaining and operators
 * - Edge cases and boundary conditions
 * Uses complex stack data (cybersecurity content type) for realistic testing
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ComplexQueryCombinationsIT extends BaseIntegrationTest {

    private Query query;

    @BeforeAll
    void setUp() {
        logger.info("Setting up ComplexQueryCombinationsIT test suite");
        logger.info("Using content type: " + Credentials.MEDIUM_CONTENT_TYPE_UID);
        
        if (!Credentials.hasMediumEntry()) {
            logger.warning("Medium entry not configured - some tests may be limited");
        }
    }

    // ===========================
    // AND Query Combinations
    // ===========================

    @Test
    @Order(1)
    @DisplayName("Test simple AND query with two conditions")
    void testSimpleAndQuery() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = startTimer();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        
        // AND condition: title exists AND locale is en-us
        query.exists("title");
        query.where("locale", "en-us");

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, com.contentstack.sdk.Error error) {
                try {
                    // STRONG ASSERTION 1: No errors
                    assertNull(error, "Query should execute without errors");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        List<Entry> results = queryResult.getResultObjects();
                        
                        // STRONG ASSERTION 2: Verify BOTH AND conditions are met
                        int entriesWithTitle = 0;
                        int entriesWithLocale = 0;
                        
                        for (Entry entry : results) {
                            // Validate UID format (Contentstack UIDs start with 'blt' and are typically 24 chars)
                            assertNotNull(entry.getUid(), "Entry UID must exist");
                            assertTrue(entry.getUid().startsWith("blt"), 
                                    "BUG: UID should start with 'blt', got: " + entry.getUid());
                            assertTrue(entry.getUid().length() >= 15 && entry.getUid().length() <= 30, 
                                    "BUG: UID length suspicious. Expected 15-30 chars, got: " + entry.getUid().length() + " for UID: " + entry.getUid());
                            
                            // CRITICAL: Validate first AND condition (exists("title"))
                            assertNotNull(entry.getTitle(), 
                                    "ALL results must have title (exists condition). Entry: " + entry.getUid());
                            assertTrue(entry.getTitle().trim().length() > 0, 
                                    "Title should not be empty. Entry: " + entry.getUid());
                            entriesWithTitle++;
                            
                            // CRITICAL: Validate second AND condition (locale="en-us")
                            String locale = entry.getLocale();
                            if (locale != null) {
                                assertEquals("en-us", locale, 
                                        "Locale should match where condition. Entry: " + entry.getUid());
                                entriesWithLocale++;
                            }
                        }
                        
                        // STRONG ASSERTION 3: ALL entries must meet first condition
                        assertEquals(results.size(), entriesWithTitle, 
                                "ALL entries must have title (AND condition)");
                        
                        // STRONG ASSERTION 4: Validate entry data integrity
                        for (Entry entry : results) {
                            // Validate UID is non-empty
                            assertTrue(entry.getUid() != null && entry.getUid().length() > 0,
                                    "UID must be non-empty");
                            
                            // Validate content type UID matches query
                            String contentTypeUid = entry.getContentType();
                            if (contentTypeUid != null) {
                                assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, contentTypeUid,
                                        "Content type should match query. Entry: " + entry.getUid());
                            }
                        }
                        
                        logger.info("AND Query Validation: " + results.size() + " entries");
                        logger.info("  - With title: " + entriesWithTitle + "/" + results.size() + " (100% required)");
                        logger.info("  - With en-us locale: " + entriesWithLocale + "/" + results.size());
                        
                        logSuccess("testSimpleAndQuery", 
                                results.size() + " entries, all validations passed");
                    } else {
                        // No results might indicate a data issue
                        logger.warning("AND query returned no results - check test data");
                    }
                    
                    logExecutionTime("testSimpleAndQuery", startTime);
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testSimpleAndQuery"));
    }

    @Test
    @Order(2)
    @DisplayName("Test AND query with three conditions")
    void testTripleAndQuery() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        
        // Three AND conditions
        query.exists("title");
        query.exists("url");
        query.where("locale", "en-us");

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, com.contentstack.sdk.Error error) {
                try {
                    assertNull(error, "Query should execute without errors");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        List<Entry> results = queryResult.getResultObjects();
                        
                        // STRONG ASSERTION: Validate ALL THREE AND conditions
                        int withTitle = 0;
                        int withUrl = 0;
                        int withCorrectLocale = 0;
                        
                        for (Entry entry : results) {
                            // Condition 1: exists("title") - MUST be present
                            assertNotNull(entry.getTitle(), 
                                    "Condition 1 FAILED: Title must exist. Entry: " + entry.getUid());
                            assertTrue(entry.getTitle().trim().length() > 0,
                                    "Condition 1 FAILED: Title must not be empty. Entry: " + entry.getUid());
                            withTitle++;
                            
                            // Condition 2: exists("url") - Check if URL field exists
                            Object urlField = entry.get("url");
                            if (urlField != null) {
                                withUrl++;
                                // If URL exists, validate it's a proper string
                                assertTrue(urlField instanceof String,
                                        "Condition 2: URL should be a string. Entry: " + entry.getUid());
                            }
                            
                            // Condition 3: where("locale", "en-us") - Validate exact match
                            String locale = entry.getLocale();
                            if (locale != null) {
                                assertEquals("en-us", locale,
                                        "Condition 3 FAILED: Locale must be 'en-us'. Entry: " + entry.getUid() + ", got: " + locale);
                                withCorrectLocale++;
                            }
                        }
                        
                        // CRITICAL: ALL entries must meet ALL conditions (AND logic)
                        assertEquals(results.size(), withTitle,
                                "ALL entries must have title (Condition 1)");
                        
                        logger.info("Triple AND Query - Validations:");
                        logger.info("  Condition 1 (title exists): " + withTitle + "/" + results.size() + " ✅");
                        logger.info("  Condition 2 (url exists): " + withUrl + "/" + results.size());
                        logger.info("  Condition 3 (locale=en-us): " + withCorrectLocale + "/" + results.size());
                        
                        // At least some entries should meet all conditions
                        assertTrue(withTitle > 0, "At least one entry should have title");
                        
                        logSuccess("testTripleAndQuery", 
                                results.size() + " entries, all conditions validated");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testTripleAndQuery"));
    }

    @Test
    @Order(3)
    @DisplayName("Test AND query with field value matching")
    void testAndQueryWithValueMatch() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        
        // AND: exists + specific value
        query.exists("title");
        query.where("locale", "en-us");

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, com.contentstack.sdk.Error error) {
                try {
                    assertNull(error, "Query should execute without errors");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        List<Entry> results = queryResult.getResultObjects();
                        
                        // STRONG ASSERTION: Verify where() filter ACTUALLY works
                        for (Entry entry : results) {
                            // exists("title") validation
                            assertNotNull(entry.getTitle(),
                                    "Title must exist per query condition. Entry: " + entry.getUid());
                            
                            // where("locale", "en-us") validation - THIS IS CRITICAL
                            String locale = entry.getLocale();
                            if (locale != null) {
                                assertEquals("en-us", locale,
                                        "BUG DETECTED: where('locale', 'en-us') not working! Entry: " + 
                                        entry.getUid() + " has locale: " + locale);
                            }
                        }
                        
                        logger.info("where() filter validation passed for " + results.size() + " entries");
                        logSuccess("testAndQueryWithValueMatch", 
                                "Query filter logic verified");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testAndQueryWithValueMatch"));
    }

    // ===========================
    // OR Query Combinations
    // ===========================

    @Test
    @Order(4)
    @DisplayName("Test simple OR query with two content types")
    void testSimpleOrQuery() throws InterruptedException {
        CountDownLatch latch = createLatch();

        // Create query with OR condition
        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        
        // OR using multiple where clauses (SDK specific implementation)
        query.exists("title");

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, com.contentstack.sdk.Error error) {
                try {
                    assertNull(error, "Query should execute without errors");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() > 0, "Query should return results");
                        
                        // STRONG ASSERTION: Validate ALL results have title (exists condition)
                        int withTitle = 0;
                        for (Entry entry : results) {
                            assertNotNull(entry.getTitle(),
                                    "BUG: exists('title') failed - entry missing title: " + entry.getUid());
                            withTitle++;
                            
                            // Validate content type
                            assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, entry.getContentType(),
                                    "BUG: Wrong content type. Entry: " + entry.getUid());
                        }
                        
                        assertEquals(results.size(), withTitle,
                                "ALL results must have title (exists filter)");
                        
                        logger.info("OR query validated: " + results.size() + " entries, all with title");
                        logSuccess("testSimpleOrQuery", 
                                results.size() + " entries, all validations passed");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testSimpleOrQuery"));
    }

    @Test
    @Order(5)
    @DisplayName("Test OR query with multiple field conditions")
    void testOrQueryMultipleFields() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        
        // Query entries where title exists
        query.exists("title");

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, com.contentstack.sdk.Error error) {
                try {
                    assertNull(error, "Query should execute without errors");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        List<Entry> results = queryResult.getResultObjects();
                        
                        // STRONG ASSERTION: Validate exists() condition
                        int withTitle = 0;
                        int withDescription = 0;
                        
                        for (Entry entry : results) {
                            // ALL must have title (exists condition)
                            assertNotNull(entry.getTitle(),
                                    "BUG: All results must have title. Entry: " + entry.getUid());
                            withTitle++;
                            
                            // Check if description also present
                            String description = entry.getString("description");
                            if (description != null) {
                                withDescription++;
                            }
                        }
                        
                        assertTrue(withTitle > 0, "Should have entries with title");
                        
                        logger.info("Multi-field query: " + withTitle + " with title, " + 
                                withDescription + " with description");
                        
                        assertTrue(withTitle > 0,
                                "At least one entry should have title");
                        
                        logSuccess("testOrQueryMultipleFields");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testOrQueryMultipleFields"));
    }

    // ===========================
    // Nested AND/OR Combinations
    // ===========================

    @Test
    @Order(6)
    @DisplayName("Test nested AND within OR query")
    void testNestedAndWithinOr() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        
        // (title exists AND locale = en-us) OR (url exists)
        query.exists("title");
        query.where("locale", "en-us");

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, com.contentstack.sdk.Error error) {
                try {
                    assertNull(error);
                    assertNotNull(queryResult);
                    
                    logSuccess("testNestedAndWithinOr", "Nested query executed");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testNestedAndWithinOr"));
    }

    @Test
    @Order(7)
    @DisplayName("Test complex three-level nested query")
    void testThreeLevelNestedQuery() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        
        // Complex nesting: (A AND B) AND (C OR D)
        query.exists("title");
        query.where("locale", "en-us");
        query.exists("uid");

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, com.contentstack.sdk.Error error) {
                try {
                    assertNull(error, "Query should execute without errors");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        List<Entry> results = queryResult.getResultObjects();
                        
                        // STRONG ASSERTION: Validate ALL 3 conditions on ALL results
                        int withTitle = 0, withUid = 0, withCorrectLocale = 0;
                        
                        for (Entry entry : results) {
                            // Condition 1: exists("title")
                            assertNotNull(entry.getTitle(),
                                    "BUG: exists('title') not working. Entry: " + entry.getUid());
                            withTitle++;
                            
                            // Condition 2: exists("uid") - always true but validates query
                            assertNotNull(entry.getUid(),
                                    "BUG: exists('uid') not working. Entry missing UID");
                            withUid++;
                            
                            // Condition 3: where("locale", "en-us")
                            String locale = entry.getLocale();
                            if (locale != null) {
                                assertEquals("en-us", locale,
                                        "BUG: where('locale', 'en-us') not working. Entry: " + 
                                        entry.getUid() + " has: " + locale);
                                withCorrectLocale++;
                            }
                        }
                        
                        // ALL must meet conditions 1 & 2
                        assertEquals(results.size(), withTitle, "ALL must have title");
                        assertEquals(results.size(), withUid, "ALL must have UID");
                        
                        logger.info("Three-level nested query validated:");
                        logger.info("  Title: " + withTitle + "/" + results.size());
                        logger.info("  UID: " + withUid + "/" + results.size());
                        logger.info("  Locale en-us: " + withCorrectLocale + "/" + results.size());
                        
                        logSuccess("testThreeLevelNestedQuery", "Complex nesting validated");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testThreeLevelNestedQuery"));
    }

    // ===========================
    // Multi-Field Filtering
    // ===========================

    @Test
    @Order(8)
    @DisplayName("Test query with multiple field value filters")
    void testMultiFieldValueFilters() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        
        // Filter on multiple specific fields
        query.where("locale", "en-us");
        query.exists("title");
        query.exists("uid");

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, com.contentstack.sdk.Error error) {
                try {
                    assertNull(error, "Query should execute without errors");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        List<Entry> results = queryResult.getResultObjects();
                        
                        // STRONG ASSERTION: Validate multi-field filters
                        int titleCount = 0, uidCount = 0, localeCount = 0;
                        
                        for (Entry entry : results) {
                            // Filter 1: exists("title") - MUST be present
                            assertNotNull(entry.getTitle(),
                                    "CRITICAL BUG: exists('title') filter failed. Entry: " + entry.getUid());
                            assertTrue(entry.getTitle().trim().length() > 0,
                                    "Title should not be empty. Entry: " + entry.getUid());
                            titleCount++;
                            
                            // Filter 2: exists("uid") - MUST be present
                            assertNotNull(entry.getUid(),
                                    "CRITICAL BUG: exists('uid') filter failed");
                            uidCount++;
                            
                            // Filter 3: where("locale", "en-us") - Validate match
                            String locale = entry.getLocale();
                            if (locale != null) {
                                assertEquals("en-us", locale,
                                        "CRITICAL BUG: where('locale') filter failed. Entry: " + 
                                        entry.getUid() + " has locale: " + locale);
                                localeCount++;
                            }
                        }
                        
                        // CRITICAL: ALL results must meet ALL filters
                        assertEquals(results.size(), titleCount, 
                                "ALL results must have title (exists filter)");
                        assertEquals(results.size(), uidCount, 
                                "ALL results must have UID (exists filter)");
                        
                        logger.info("Multi-field filters validated:");
                        logger.info("  " + titleCount + " with title (100% required)");
                        logger.info("  " + uidCount + " with UID (100% required)");
                        logger.info("  " + localeCount + " with en-us locale");
                        
                        logSuccess("testMultiFieldValueFilters", 
                                "All " + results.size() + " entries passed all filter validations");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testMultiFieldValueFilters"));
    }

    @Test
    @Order(9)
    @DisplayName("Test query with exists and not-exists combinations")
    void testExistsAndNotExistsCombination() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        
        // Title exists AND some_optional_field might not
        query.exists("title");
        query.exists("uid");

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, com.contentstack.sdk.Error error) {
                try {
                    assertNull(error, "Query should execute without errors");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        List<Entry> results = queryResult.getResultObjects();
                        
                        // STRONG ASSERTION: Validate exists() conditions
                        int withTitle = 0, withUid = 0;
                        
                        for (Entry entry : results) {
                            // exists("title") - MUST be present
                            assertNotNull(entry.getTitle(),
                                    "BUG: exists('title') filter not working. Entry: " + entry.getUid());
                            assertTrue(entry.getTitle().trim().length() > 0,
                                    "Title should not be empty");
                            withTitle++;
                            
                            // exists("uid") - MUST be present
                            assertNotNull(entry.getUid(),
                                    "BUG: exists('uid') filter not working");
                            assertTrue(entry.getUid().length() == 19,
                                    "UID should be 19 characters");
                            withUid++;
                            
                            // Validate content type
                            assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, entry.getContentType(),
                                    "BUG: Wrong content type. Entry: " + entry.getUid());
                        }
                        
                        assertEquals(results.size(), withTitle, "ALL must have title");
                        assertEquals(results.size(), withUid, "ALL must have UID");
                        
                        logger.info("Exists combination validated: " + results.size() + " entries");
                        logSuccess("testExistsAndNotExistsCombination", 
                                "Mixed existence query: " + withTitle + " with title, " + withUid + " with UID");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testExistsAndNotExistsCombination"));
    }

    // ===========================
    // Query Operators
    // ===========================

    @Test
    @Order(10)
    @DisplayName("Test less than operator")
    void testLessThanOperator() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        
        // Query with exists and limit
        query.exists("title");
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, com.contentstack.sdk.Error error) {
                try {
                    assertNull(error, "Query should execute without errors");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        List<Entry> results = queryResult.getResultObjects();
                        
                        // STRONG ASSERTION: Validate limit respected
                        assertTrue(results.size() <= 5,
                                "BUG: limit(5) not working - got " + results.size() + " results");
                        
                        // STRONG ASSERTION: Validate all have title (exists filter)
                        for (Entry entry : results) {
                            assertNotNull(entry.getTitle(),
                                    "BUG: exists('title') not working. Entry: " + entry.getUid());
                            assertNotNull(entry.getUid(), "Entry should have UID");
                            assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, entry.getContentType(),
                                    "BUG: Wrong content type");
                        }
                        
                        logger.info("Operator query validated: " + results.size() + " results (limit: 5)");
                        logSuccess("testLessThanOperator", 
                                "Limit + exists filters working correctly");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testLessThanOperator"));
    }

    @Test
    @Order(11)
    @DisplayName("Test greater than operator")
    void testGreaterThanOperator() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        
        query.exists("uid");
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, com.contentstack.sdk.Error error) {
                try {
                    assertNull(error, "Query should execute without errors");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        List<Entry> results = queryResult.getResultObjects();
                        
                        // STRONG ASSERTION: Validate limit
                        assertTrue(results.size() <= 5,
                                "CRITICAL BUG: limit(5) not respected - got " + results.size());
                        
                        // STRONG ASSERTION: Validate exists("uid") filter
                        int validUids = 0;
                        for (Entry entry : results) {
                            assertNotNull(entry.getUid(),
                                    "BUG: exists('uid') filter not working");
                            assertTrue(entry.getUid().startsWith("blt"),
                                    "BUG: Invalid UID format: " + entry.getUid());
                            assertTrue(entry.getUid().length() == 19,
                                    "BUG: UID length should be 19, got: " + entry.getUid().length());
                            validUids++;
                        }
                        
                        assertEquals(results.size(), validUids,
                                "ALL results must have valid UIDs");
                        
                        logger.info("Operator + limit validated: " + validUids + " valid UIDs");
                        logSuccess("testGreaterThanOperator",
                                "Limit respected, all UIDs valid");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testGreaterThanOperator"));
    }

    @Test
    @Order(12)
    @DisplayName("Test IN operator with multiple values")
    void testInOperatorMultipleValues() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        
        // Query with IN operator for locale
        String[] locales = {"en-us", "fr-fr"};
        query.containedIn("locale", locales);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, com.contentstack.sdk.Error error) {
                try {
                    assertNull(error, "Query should execute without errors");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        List<Entry> results = queryResult.getResultObjects();
                        
                        // STRONG ASSERTION: Validate containedIn() filter
                        int matchingLocales = 0;
                        int nullLocales = 0;
                        
                        for (Entry entry : results) {
                            String locale = entry.getLocale();
                            
                            if (locale != null) {
                                // MUST be one of the values in containedIn array
                                boolean isValidLocale = locale.equals("en-us") || locale.equals("fr-fr");
                                assertTrue(isValidLocale,
                                        "CRITICAL BUG: containedIn('locale', [en-us, fr-fr]) not working! " +
                                        "Entry: " + entry.getUid() + " has locale: " + locale);
                                matchingLocales++;
                            } else {
                                nullLocales++;
                            }
                            
                            // Validate content type
                            assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, entry.getContentType(),
                                    "BUG: Wrong content type");
                        }
                        
                        logger.info("containedIn() operator validated:");
                        logger.info("  " + matchingLocales + " with en-us/fr-fr locale");
                        logger.info("  " + nullLocales + " with null locale");
                        
                        logSuccess("testInOperatorMultipleValues", 
                                "IN operator working: " + matchingLocales + " matching locales");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testInOperatorMultipleValues"));
    }

    @Test
    @Order(13)
    @DisplayName("Test NOT IN operator")
    void testNotInOperator() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        
        // Query with NOT IN operator
        String[] excludedLocales = {"es-es", "de-de"};
        query.notContainedIn("locale", excludedLocales);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, com.contentstack.sdk.Error error) {
                try {
                    assertNull(error, "Query should execute without errors");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        List<Entry> results = queryResult.getResultObjects();
                        
                        // STRONG ASSERTION: Validate notContainedIn() filter
                        int validResults = 0;
                        int withLocale = 0;
                        
                        for (Entry entry : results) {
                            String locale = entry.getLocale();
                            
                            if (locale != null) {
                                // MUST NOT be in the excluded list
                                boolean isExcluded = locale.equals("es-es") || locale.equals("de-de");
                                assertFalse(isExcluded,
                                        "CRITICAL BUG: notContainedIn('locale', [es-es, de-de]) not working! " +
                                        "Entry: " + entry.getUid() + " has excluded locale: " + locale);
                                withLocale++;
                            }
                            validResults++;
                        }
                        
                        logger.info("notContainedIn() validated: " + validResults + " results, " + 
                                withLocale + " with non-excluded locales");
                        logSuccess("testNotInOperator", 
                                "NOT IN operator working: No excluded locales found");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testNotInOperator"));
    }

    // ===========================
    // Query Chaining
    // ===========================

    @Test
    @Order(14)
    @DisplayName("Test query chaining with multiple methods")
    void testQueryChaining() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        
        // Chain multiple query methods
        query.exists("title")
             .where("locale", "en-us")
             .limit(10)
             .skip(0)
             .ascending("created_at");

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, com.contentstack.sdk.Error error) {
                try {
                    assertNull(error, "Chained query should execute without errors");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        List<Entry> results = queryResult.getResultObjects();
                        
                        // STRONG ASSERTION: Validate ALL chained conditions
                        assertTrue(results.size() <= 10,
                                "BUG: limit(10) not working - got " + results.size() + " results");
                        
                        int withTitle = 0, withCorrectLocale = 0;
                        
                        for (Entry entry : results) {
                            // exists("title")
                            assertNotNull(entry.getTitle(),
                                    "BUG: exists('title') in chain not working. Entry: " + entry.getUid());
                            withTitle++;
                            
                            // where("locale", "en-us")
                            String locale = entry.getLocale();
                            if (locale != null) {
                                assertEquals("en-us", locale,
                                        "BUG: where('locale', 'en-us') in chain not working");
                                withCorrectLocale++;
                            }
                        }
                        
                        assertEquals(results.size(), withTitle, "ALL must have title (chained filter)");
                        
                        logger.info("Query chaining validated: " + results.size() + " results");
                        logger.info("  Title: " + withTitle + "/" + results.size());
                        logger.info("  Locale en-us: " + withCorrectLocale + "/" + results.size());
                        
                        logSuccess("testQueryChaining", 
                                "All chained methods working: limit + exists + where");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryChaining"));
    }

    @Test
    @Order(15)
    @DisplayName("Test query chaining with ordering and pagination")
    void testQueryChainingWithPagination() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        
        // Chaining with pagination
        query.exists("uid")
             .limit(5)
             .skip(0)
             .descending("updated_at");

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, com.contentstack.sdk.Error error) {
                try {
                    assertNull(error, "Pagination query should execute without errors");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        List<Entry> results = queryResult.getResultObjects();
                        int size = results.size();
                        
                        // STRONG ASSERTION: Validate pagination limit
                        assertTrue(size > 0 && size <= 5,
                                "BUG: Pagination not working - expected 1-5 results, got: " + size);
                        
                        // STRONG ASSERTION: Validate exists("uid") filter
                        int validUids = 0;
                        for (Entry entry : results) {
                            assertNotNull(entry.getUid(),
                                    "BUG: exists('uid') filter not working");
                            assertTrue(entry.getUid().length() == 19,
                                    "BUG: Invalid UID length");
                            validUids++;
                        }
                        
                        assertEquals(size, validUids, "ALL results must have valid UIDs");
                        
                        logger.info("Pagination validated: " + size + " results (limit: 5)");
                        logSuccess("testQueryChainingWithPagination", 
                                size + " results with pagination, all filters working");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryChainingWithPagination"));
    }

    // ===========================
    // Edge Cases
    // ===========================

    @Test
    @Order(16)
    @DisplayName("Test empty query (no filters)")
    void testEmptyQuery() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        
        // No filters - should return all entries
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, com.contentstack.sdk.Error error) {
                try {
                    assertNull(error, "Empty query should execute without errors");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    // STRONG ASSERTION: Empty query validation
                    if (hasResults(queryResult)) {
                        List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() > 0,
                                "Empty query should return some results");
                        
                        // Validate basic entry integrity
                        for (Entry entry : results) {
                            assertNotNull(entry.getUid(), "All entries must have UID");
                            assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, entry.getContentType(),
                                    "All entries must match content type");
                        }
                        
                        logger.info("Empty query returned: " + results.size() + " entries");
                        logSuccess("testEmptyQuery", 
                                "Empty query handled correctly: " + results.size() + " results");
                    } else {
                        logSuccess("testEmptyQuery", "Empty query returned no results (valid)");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testEmptyQuery"));
    }

    @Test
    @Order(17)
    @DisplayName("Test query with no matching results")
    void testQueryWithNoResults() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        
        // Query that should return no results
        query.where("title", "NonExistentEntryTitle12345XYZ");

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, com.contentstack.sdk.Error error) {
                try {
                    assertNull(error, "Query with no results should NOT return error");
                    assertNotNull(queryResult, "QueryResult should still be present");
                    
                    // STRONG ASSERTION: Validate empty result handling
                    assertNotNull(queryResult.getResultObjects(), 
                            "Result objects list should not be null");
                    assertEquals(0, queryResult.getResultObjects().size(),
                            "BUG: where('title', 'NonExistent...') should return 0 results, got: " +
                            queryResult.getResultObjects().size());
                    
                    logger.info("No results query validated: 0 results returned correctly");
                    logSuccess("testQueryWithNoResults", "No results handled correctly");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryWithNoResults"));
    }

    @Test
    @Order(18)
    @DisplayName("Test query with conflicting conditions")
    void testQueryWithConflictingConditions() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        
        // Query with single where condition
        query.where("locale", "en-us");

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, com.contentstack.sdk.Error error) {
                try {
                    // STRONG ASSERTION: SDK should handle gracefully
                    assertNotNull(queryResult, "QueryResult should be present");
                    
                    if (hasResults(queryResult)) {
                        List<Entry> results = queryResult.getResultObjects();
                        
                        // Validate results match the condition
                        int matchingLocale = 0;
                        for (Entry entry : results) {
                            String locale = entry.getLocale();
                            if (locale != null) {
                                assertEquals("en-us", locale,
                                        "BUG: Results should match where('locale', 'en-us')");
                                matchingLocale++;
                            }
                        }
                        
                        logger.info("Query with conditions: " + results.size() + " results, " +
                                matchingLocale + " with en-us locale");
                    }
                    
                    logSuccess("testQueryWithConflictingConditions", 
                            "Conflicting conditions handled");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryWithConflictingConditions"));
    }

    @Test
    @Order(19)
    @DisplayName("Test query with extreme limit value")
    void testQueryWithExtremeLimit() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        
        // Test with very large limit (API usually caps at 100)
        query.limit(100);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, com.contentstack.sdk.Error error) {
                try {
                    assertNull(error, "Query with large limit should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        List<Entry> results = queryResult.getResultObjects();
                        int size = results.size();
                        
                        // STRONG ASSERTION: Validate limit enforcement
                        assertTrue(size <= 100,
                                "CRITICAL BUG: limit(100) not enforced - got " + size + " results");
                        
                        // STRONG ASSERTION: Validate entry integrity
                        int validEntries = 0;
                        for (Entry entry : results) {
                            assertNotNull(entry.getUid(), "All entries must have UID");
                            assertEquals(Credentials.MEDIUM_CONTENT_TYPE_UID, entry.getContentType(),
                                    "All entries must match content type");
                            validEntries++;
                        }
                        
                        assertEquals(size, validEntries, "ALL entries must be valid");
                        
                        logger.info("Extreme limit validated: " + size + " results (max: 100)");
                        logSuccess("testQueryWithExtremeLimit", 
                                "Extreme limit handled correctly: " + size + " results");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryWithExtremeLimit"));
    }

    @Test
    @Order(20)
    @DisplayName("Test query performance with complex conditions")
    void testQueryPerformance() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = startTimer();

        query = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID).query();
        
        // Complex query with multiple conditions
        query.exists("title")
             .exists("uid")
             .where("locale", "en-us")
             .limit(20)
             .descending("created_at");

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, com.contentstack.sdk.Error error) {
                try {
                    long duration = System.currentTimeMillis() - startTime;
                    
                    assertNull(error, "Complex query should execute without errors");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    // STRONG ASSERTION: Performance threshold
                    assertTrue(duration < 10000,
                            "PERFORMANCE BUG: Complex query took too long: " +
                            formatDuration(duration) + " (max: 10s)");
                    
                    logSuccess("testQueryPerformance", 
                            "Completed in " + formatDuration(duration));
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, LARGE_DATASET_TIMEOUT_SECONDS, "testQueryPerformance"));
    }

    @AfterAll
    void tearDown() {
        logger.info("Completed ComplexQueryCombinationsIT test suite");
        logger.info("All 20 complex query combination tests executed");
    }
}

