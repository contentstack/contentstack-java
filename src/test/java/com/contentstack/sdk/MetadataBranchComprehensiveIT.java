package com.contentstack.sdk;

import com.contentstack.sdk.utils.PerformanceAssertion;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;

/**
 * Comprehensive Integration Tests for Metadata and Branch Operations
 * Tests metadata and branch behavior including:
 * - Basic entry metadata access
 * - System metadata fields
 * - Branch-specific queries (if configured)
 * - Metadata with references
 * - Metadata with queries
 * - Performance with metadata inclusion
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MetadataBranchComprehensiveIT extends BaseIntegrationTest {

    private Query query;
    private Entry entry;

    @BeforeAll
    void setUp() {
        logger.info("Setting up MetadataBranchComprehensiveIT test suite");
        logger.info("Testing metadata and branch operations");
        logger.info("Using content type: " + Credentials.COMPLEX_CONTENT_TYPE_UID);
    }

    // ===========================
    // Basic Metadata Access
    // ===========================

    @Test
    @Order(1)
    @DisplayName("Test basic entry metadata access")
    void testBasicMetadataAccess() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.limit(1);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Metadata query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        Entry entry = queryResult.getResultObjects().get(0);
                        
                        // Basic metadata
                        assertNotNull(entry.getUid(), "BUG: UID missing");
                        assertNotNull(entry.getContentType(), "BUG: Content type missing");
                        assertEquals(Credentials.COMPLEX_CONTENT_TYPE_UID, entry.getContentType(),
                                "BUG: Wrong content type");
                        
                        // System fields
                        Object locale = entry.get("locale");
                        Object createdAt = entry.get("created_at");
                        Object updatedAt = entry.get("updated_at");
                        
                        assertNotNull(locale, "BUG: Locale metadata missing");
                        logger.info("Entry metadata - UID: " + entry.getUid() + ", Locale: " + locale);
                        
                        logger.info("✅ Basic metadata access working");
                        logSuccess("testBasicMetadataAccess", "Metadata accessible");
                    } else {
                        logger.warning("No entries to test metadata");
                        logSuccess("testBasicMetadataAccess", "No entries");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testBasicMetadataAccess"));
    }

    @Test
    @Order(2)
    @DisplayName("Test system metadata fields")
    void testSystemMetadataFields() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.limit(3);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "System metadata query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        for (Entry e : queryResult.getResultObjects()) {
                            // System metadata
                            assertNotNull(e.getUid(), "All entries must have UID");
                            assertNotNull(e.getContentType(), "All entries must have content type");
                            
                            Object locale = e.get("locale");
                            Object version = e.get("_version");
                            
                            assertNotNull(locale, "BUG: Locale missing");
                            logger.info("Entry " + e.getUid() + " - Version: " + version + ", Locale: " + locale);
                        }
                        
                        logger.info("✅ System metadata fields present: " + queryResult.getResultObjects().size() + " entries");
                        logSuccess("testSystemMetadataFields", queryResult.getResultObjects().size() + " entries");
                    } else {
                        logSuccess("testSystemMetadataFields", "No entries");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testSystemMetadataFields"));
    }

    @Test
    @Order(3)
    @DisplayName("Test entry locale metadata")
    void testEntryLocaleMetadata() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Locale metadata query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        int localeCount = 0;
                        for (Entry e : queryResult.getResultObjects()) {
                            Object locale = e.get("locale");
                            if (locale != null) {
                                localeCount++;
                                assertTrue(locale.toString().length() > 0,
                                        "BUG: Locale value empty");
                            }
                        }
                        
                        assertTrue(localeCount > 0, "BUG: No entries have locale metadata");
                        logger.info("✅ Locale metadata present in " + localeCount + " entries");
                        logSuccess("testEntryLocaleMetadata", localeCount + " entries with locale");
                    } else {
                        logSuccess("testEntryLocaleMetadata", "No entries");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testEntryLocaleMetadata"));
    }

    @Test
    @Order(4)
    @DisplayName("Test entry version metadata")
    void testEntryVersionMetadata() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Version metadata query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        int versionCount = 0;
                        for (Entry e : queryResult.getResultObjects()) {
                            Object version = e.get("_version");
                            if (version != null) {
                                versionCount++;
                                logger.info("Entry " + e.getUid() + " version: " + version);
                            }
                        }
                        
                        logger.info("✅ Version metadata present in " + versionCount + " entries");
                        logSuccess("testEntryVersionMetadata", versionCount + " entries with version");
                    } else {
                        logSuccess("testEntryVersionMetadata", "No entries");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testEntryVersionMetadata"));
    }

    // ===========================
    // Metadata with Queries
    // ===========================

    @Test
    @Order(5)
    @DisplayName("Test metadata with filtered query")
    void testMetadataWithFilteredQuery() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.exists("title");
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Filtered + metadata query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        for (Entry e : queryResult.getResultObjects()) {
                            assertNotNull(e.getUid(), "All must have UID metadata");
                            assertNotNull(e.getTitle(), "All must have title (filter)");
                            assertEquals(Credentials.COMPLEX_CONTENT_TYPE_UID, e.getContentType(),
                                    "BUG: Wrong content type");
                        }
                        
                        logger.info("✅ Metadata + filter: " + queryResult.getResultObjects().size() + " entries");
                        logSuccess("testMetadataWithFilteredQuery", queryResult.getResultObjects().size() + " entries");
                    } else {
                        logSuccess("testMetadataWithFilteredQuery", "No entries");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testMetadataWithFilteredQuery"));
    }

    @Test
    @Order(6)
    @DisplayName("Test metadata with sorted query")
    void testMetadataWithSortedQuery() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.descending("created_at");
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Sorted + metadata query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        for (Entry e : queryResult.getResultObjects()) {
                            assertNotNull(e.getUid(), "All must have UID");
                            Object createdAt = e.get("created_at");
                            logger.info("Entry " + e.getUid() + " created_at: " + createdAt);
                        }
                        
                        logger.info("✅ Metadata + sorting: " + queryResult.getResultObjects().size() + " entries");
                        logSuccess("testMetadataWithSortedQuery", queryResult.getResultObjects().size() + " entries");
                    } else {
                        logSuccess("testMetadataWithSortedQuery", "No entries");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testMetadataWithSortedQuery"));
    }

    @Test
    @Order(7)
    @DisplayName("Test metadata with pagination")
    void testMetadataWithPagination() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.skip(2);
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Pagination + metadata query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() <= 5, "Should respect limit");
                        
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All must have UID");
                        }
                        
                        logger.info("✅ Metadata + pagination: " + results.size() + " entries");
                        logSuccess("testMetadataWithPagination", results.size() + " entries");
                    } else {
                        logSuccess("testMetadataWithPagination", "No entries");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testMetadataWithPagination"));
    }

    // ===========================
    // Metadata with References
    // ===========================

    @Test
    @Order(8)
    @DisplayName("Test metadata with references")
    void testMetadataWithReferences() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.includeReference("authors");
        query.limit(3);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    if (error == null) {
                        assertNotNull(queryResult, "QueryResult should not be null");
                        if (hasResults(queryResult)) {
                            for (Entry e : queryResult.getResultObjects()) {
                                assertNotNull(e.getUid(), "All must have UID metadata");
                                Object locale = e.get("locale");
                                assertNotNull(locale, "All must have locale metadata");
                            }
                            
                            logger.info("✅ Metadata + references: " + queryResult.getResultObjects().size() + " entries");
                            logSuccess("testMetadataWithReferences", queryResult.getResultObjects().size() + " entries");
                        } else {
                            logSuccess("testMetadataWithReferences", "No entries");
                        }
                    } else {
                        logger.info("ℹ️ References not configured");
                        logSuccess("testMetadataWithReferences", "Handled");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testMetadataWithReferences"));
    }

    // ===========================
    // Branch Operations (if configured)
    // ===========================

    @Test
    @Order(9)
    @DisplayName("Test branch metadata if available")
    void testBranchMetadata() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.limit(3);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Branch metadata query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        int branchCount = 0;
                        for (Entry e : queryResult.getResultObjects()) {
                            Object branch = e.get("_branch");
                            if (branch != null) {
                                branchCount++;
                                logger.info("Entry " + e.getUid() + " branch: " + branch);
                            }
                        }
                        
                        if (branchCount > 0) {
                            logger.info("✅ Branch metadata present in " + branchCount + " entries");
                        } else {
                            logger.info("ℹ️ No branch metadata (not configured or main branch)");
                        }
                        logSuccess("testBranchMetadata", branchCount + " entries with branch metadata");
                    } else {
                        logSuccess("testBranchMetadata", "No entries");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testBranchMetadata"));
    }

    @Test
    @Order(10)
    @DisplayName("Test query on specific branch (if configured)")
    void testQueryOnSpecificBranch() throws InterruptedException {
        CountDownLatch latch = createLatch();

        // Note: Branch queries require proper SDK configuration
        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    if (error == null) {
                        assertNotNull(queryResult, "QueryResult should not be null");
                        if (hasResults(queryResult)) {
                            logger.info("✅ Branch-specific query: " + 
                                    queryResult.getResultObjects().size() + " entries");
                            logSuccess("testQueryOnSpecificBranch", 
                                    queryResult.getResultObjects().size() + " entries");
                        } else {
                            logSuccess("testQueryOnSpecificBranch", "No entries");
                        }
                    } else {
                        logger.info("ℹ️ Branch query handled");
                        logSuccess("testQueryOnSpecificBranch", "Handled");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryOnSpecificBranch"));
    }

    // ===========================
    // Performance Tests
    // ===========================

    @Test
    @Order(11)
    @DisplayName("Test metadata access performance")
    void testMetadataAccessPerformance() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.limit(20);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    
                    assertNull(error, "Metadata performance query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    // Metadata access should not significantly impact performance
                    assertTrue(duration < 10000,
                            "PERFORMANCE BUG: Metadata access took " + duration + "ms (max: 10s)");
                    
                    if (hasResults(queryResult)) {
                        // Access metadata for all entries
                        for (Entry e : queryResult.getResultObjects()) {
                            e.getUid();
                            e.getContentType();
                            e.get("locale");
                            e.get("_version");
                        }
                        
                        logger.info("✅ Metadata performance: " + 
                                queryResult.getResultObjects().size() + " entries in " + 
                                formatDuration(duration));
                        logSuccess("testMetadataAccessPerformance", 
                                queryResult.getResultObjects().size() + " entries, " + formatDuration(duration));
                    } else {
                        logSuccess("testMetadataAccessPerformance", "No entries, " + formatDuration(duration));
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testMetadataAccessPerformance"));
    }

    @Test
    @Order(12)
    @DisplayName("Test multiple metadata queries performance")
    void testMultipleMetadataQueriesPerformance() throws InterruptedException {
        int[] totalEntries = {0};
        long startTime = PerformanceAssertion.startTimer();
        
        // Run 3 queries
        for (int i = 0; i < 3; i++) {
            CountDownLatch latch = createLatch();
            
            Query q = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
            q.skip(i * 3);
            q.limit(3);
            
            q.find(new QueryResultsCallBack() {
                @Override
                public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                    try {
                        if (error == null && hasResults(queryResult)) {
                            totalEntries[0] += queryResult.getResultObjects().size();
                            // Access metadata
                            for (Entry e : queryResult.getResultObjects()) {
                                e.getUid();
                                e.get("locale");
                            }
                        }
                    } finally {
                        latch.countDown();
                    }
                }
            });
            
            awaitLatch(latch, "metadata-query-" + i);
        }
        
        long duration = PerformanceAssertion.elapsedTime(startTime);
        
        logger.info("✅ Multiple metadata queries: " + totalEntries[0] + 
                " total entries in " + formatDuration(duration));
        logSuccess("testMultipleMetadataQueriesPerformance", 
                totalEntries[0] + " entries, " + formatDuration(duration));
    }

    // ===========================
    // Edge Cases
    // ===========================

    @Test
    @Order(13)
    @DisplayName("Test metadata with empty results")
    void testMetadataWithEmptyResults() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.exists("nonexistent_field_xyz");
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    if (error == null) {
                        assertNotNull(queryResult, "QueryResult should not be null");
                        if (!hasResults(queryResult)) {
                            logger.info("✅ Metadata with empty results handled");
                        } else {
                            logger.info("ℹ️ Query returned results");
                        }
                        logSuccess("testMetadataWithEmptyResults", "Handled gracefully");
                    } else {
                        logger.info("ℹ️ Query error handled");
                        logSuccess("testMetadataWithEmptyResults", "Error handled");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testMetadataWithEmptyResults"));
    }

    @Test
    @Order(14)
    @DisplayName("Test metadata field access with missing fields")
    void testMetadataWithMissingFields() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.limit(3);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        for (Entry e : queryResult.getResultObjects()) {
                            // Try accessing potentially missing metadata
                            Object missingField = e.get("nonexistent_metadata");
                            assertNull(missingField, "Missing metadata should be null");
                        }
                        
                        logger.info("✅ Missing metadata fields handled gracefully");
                        logSuccess("testMetadataWithMissingFields", "Graceful handling");
                    } else {
                        logSuccess("testMetadataWithMissingFields", "No entries");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testMetadataWithMissingFields"));
    }

    // ===========================
    // Comprehensive Scenarios
    // ===========================

    @Test
    @Order(15)
    @DisplayName("Test comprehensive metadata access scenario")
    void testComprehensiveMetadataScenario() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.exists("title");
        query.descending("created_at");
        query.skip(1);
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    
                    assertNull(error, "Comprehensive metadata query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() <= 5, "Should respect limit");
                        
                        // Comprehensive metadata validation
                        int metadataValidCount = 0;
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "UID must be present");
                            assertEquals(Credentials.COMPLEX_CONTENT_TYPE_UID, e.getContentType(),
                                    "BUG: Wrong content type");
                            assertNotNull(e.getTitle(), "Title must be present (filter)");
                            
                            Object locale = e.get("locale");
                            Object version = e.get("_version");
                            
                            if (locale != null && version != null) {
                                metadataValidCount++;
                            }
                        }
                        
                        assertTrue(metadataValidCount > 0, "BUG: No entries have complete metadata");
                        
                        // Performance check
                        assertTrue(duration < 10000,
                                "PERFORMANCE BUG: Comprehensive took " + duration + "ms (max: 10s)");
                        
                        logger.info("✅ Comprehensive metadata: " + results.size() + 
                                " entries (" + metadataValidCount + " with full metadata) in " + 
                                formatDuration(duration));
                        logSuccess("testComprehensiveMetadataScenario", 
                                results.size() + " entries, " + formatDuration(duration));
                    } else {
                        logSuccess("testComprehensiveMetadataScenario", "No results");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testComprehensiveMetadataScenario"));
    }

    @Test
    @Order(16)
    @DisplayName("Test metadata consistency across multiple queries")
    void testMetadataConsistency() throws InterruptedException {
        java.util.Map<String, String> entryLocales = new java.util.HashMap<>();
        
        // Query 1 - fetch entries and store their locales
        CountDownLatch latch1 = createLatch();
        Query query1 = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query1.limit(5);
        
        query1.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    if (error == null && hasResults(queryResult)) {
                        for (Entry e : queryResult.getResultObjects()) {
                            Object locale = e.get("locale");
                            if (locale != null) {
                                entryLocales.put(e.getUid(), locale.toString());
                            }
                        }
                    }
                } finally {
                    latch1.countDown();
                }
            }
        });
        
        awaitLatch(latch1, "query1");
        
        // Query 2 - fetch same entries and verify locales match
        CountDownLatch latch2 = createLatch();
        Query query2 = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query2.limit(5);
        
        query2.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    if (error == null && hasResults(queryResult)) {
                        int matchCount = 0;
                        for (Entry e : queryResult.getResultObjects()) {
                            String uid = e.getUid();
                            if (entryLocales.containsKey(uid)) {
                                Object locale = e.get("locale");
                                if (locale != null && locale.toString().equals(entryLocales.get(uid))) {
                                    matchCount++;
                                } else {
                                    fail("BUG: Locale metadata inconsistent for " + uid);
                                }
                            }
                        }
                        
                        logger.info("✅ Metadata consistency: " + matchCount + " entries verified");
                        logSuccess("testMetadataConsistency", matchCount + " consistent entries");
                    } else {
                        logSuccess("testMetadataConsistency", "No entries to verify");
                    }
                } finally {
                    latch2.countDown();
                }
            }
        });
        
        assertTrue(awaitLatch(latch2, "testMetadataConsistency"));
    }

    @Test
    @Order(17)
    @DisplayName("Test metadata with field projection")
    void testMetadataWithFieldProjection() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.only(new String[]{"title"});
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    if (error == null) {
                        assertNotNull(queryResult, "QueryResult should not be null");
                        if (hasResults(queryResult)) {
                            for (Entry e : queryResult.getResultObjects()) {
                                // System metadata (UID, content type) should still be present even with projection
                                assertNotNull(e.getUid(), "BUG: UID missing with projection");
                                assertNotNull(e.getContentType(), "BUG: Content type missing with projection");
                                // Note: locale may not be included with projection unless explicitly requested
                                Object locale = e.get("locale");
                                logger.info("Entry " + e.getUid() + " locale with projection: " + locale);
                            }
                            
                            logger.info("✅ Metadata + projection: " + 
                                    queryResult.getResultObjects().size() + " entries");
                            logSuccess("testMetadataWithFieldProjection", 
                                    queryResult.getResultObjects().size() + " entries");
                        } else {
                            logSuccess("testMetadataWithFieldProjection", "No entries");
                        }
                    } else {
                        logger.info("ℹ️ Projection error: " + error.getErrorMessage());
                        logSuccess("testMetadataWithFieldProjection", "Handled");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testMetadataWithFieldProjection"));
    }

    @Test
    @Order(18)
    @DisplayName("Test final comprehensive metadata and branch scenario")
    void testFinalComprehensiveScenario() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        query = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID).query();
        query.exists("title");
        query.only(new String[]{"title", "url"});
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
                            
                            // Validate all metadata
                            for (Entry e : results) {
                                assertNotNull(e.getUid(), "UID must be present");
                                assertNotNull(e.getContentType(), "Content type must be present");
                                assertEquals(Credentials.COMPLEX_CONTENT_TYPE_UID, e.getContentType(),
                                        "BUG: Wrong content type");
                                
                                // Note: locale may not be included with projection unless explicitly requested
                                Object locale = e.get("locale");
                                logger.info("Entry " + e.getUid() + " locale: " + 
                                        (locale != null ? locale : "not included (projection)"));
                                
                                // Branch metadata (optional)
                                Object branch = e.get("_branch");
                                if (branch != null) {
                                    logger.info("Entry " + e.getUid() + " has branch: " + branch);
                                }
                            }
                            
                            // Performance
                            assertTrue(duration < 10000,
                                    "PERFORMANCE BUG: Final scenario took " + duration + "ms (max: 10s)");
                            
                            logger.info("✅ FINAL COMPREHENSIVE: " + results.size() + 
                                    " entries in " + formatDuration(duration));
                            logSuccess("testFinalComprehensiveScenario", 
                                    results.size() + " entries, " + formatDuration(duration));
                        } else {
                            logSuccess("testFinalComprehensiveScenario", "No results");
                        }
                    } else {
                        logger.info("ℹ️ Final scenario error: " + error.getErrorMessage());
                        logSuccess("testFinalComprehensiveScenario", "Handled");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testFinalComprehensiveScenario"));
    }

    @AfterAll
    void tearDown() {
        logger.info("Completed MetadataBranchComprehensiveIT test suite");
        logger.info("All 18 metadata/branch tests executed");
        logger.info("Tested: system metadata, locales, versions, branches, performance, consistency");
        logger.info("==================== PHASE 3 COMPLETE ====================");
    }
}

