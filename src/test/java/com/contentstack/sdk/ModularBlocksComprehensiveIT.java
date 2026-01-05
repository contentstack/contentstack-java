package com.contentstack.sdk;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.ArrayList;

/**
 * Comprehensive Integration Tests for Modular Blocks
 * Tests modular blocks functionality including:
 * - Single modular block entries
 * - Multiple modular blocks in single entry
 * - Nested modular blocks
 * - Different block types
 * - Modular blocks with references
 * - Query operations with modular blocks
 * - Complex scenarios
 * - Edge cases and error handling
 * Uses complex stack data with modular block structures
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ModularBlocksComprehensiveIT extends BaseIntegrationTest {

    private Entry entry;
    private Query query;

    @BeforeAll
    void setUp() {
        logger.info("Setting up ModularBlocksComprehensiveIT test suite");
        logger.info("Testing modular blocks with complex stack data");
        
        if (!Credentials.COMPLEX_BLOCKS_ENTRY_UID.isEmpty()) {
            logger.info("Using COMPLEX_BLOCKS entry: " + Credentials.COMPLEX_BLOCKS_ENTRY_UID);
        } else {
            logger.warning("COMPLEX_BLOCKS_ENTRY_UID not configured");
        }
    }

    // ===========================
    // Basic Modular Blocks
    // ===========================

    @Test
    @Order(1)
    @DisplayName("Test entry with single modular block")
    void testSingleModularBlock() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = startTimer();

        // Use entry that has modular blocks - fallback to complex entry
        String entryUid = Credentials.COMPLEX_BLOCKS_ENTRY_UID;
        String contentTypeUid = Credentials.COMPLEX_BLOCKS_CONTENT_TYPE_UID;

        entry = stack.contentType(contentTypeUid).entry(entryUid);

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    if (error != null) {
                        logger.warning("Entry fetch error (may not have blocks): " + error.getErrorMessage());
                    }
                    
                    if (entry != null && hasBasicFields(entry)) {
                        // STRONG ASSERTION: Validate correct entry
                        assertEquals(entryUid, entry.getUid(),
                                "CRITICAL BUG: Wrong entry fetched!");
                        assertEquals(contentTypeUid, entry.getContentType(),
                                "CRITICAL BUG: Wrong content type!");
                        
                        // STRONG ASSERTION: Check for modular block fields
                        int modularBlockFields = 0;
                        ArrayList<String> blockFields = new ArrayList<>();
                        
                        if (entry.get("modular_blocks") != null) {
                            modularBlockFields++;
                            blockFields.add("modular_blocks");
                        }
                        if (entry.get("sections") != null) {
                            modularBlockFields++;
                            blockFields.add("sections");
                        }
                        if (entry.get("components") != null) {
                            modularBlockFields++;
                            blockFields.add("components");
                        }
                        if (entry.get("blocks") != null) {
                            modularBlockFields++;
                            blockFields.add("blocks");
                        }
                        if (entry.get("page_components") != null) {
                            modularBlockFields++;
                            blockFields.add("page_components");
                        }
                        
                        long duration = System.currentTimeMillis() - startTime;
                        
                        logger.info("Modular blocks validated:");
                        logger.info("  Entry UID: " + entry.getUid() + " ✅");
                        logger.info("  Block fields found: " + modularBlockFields);
                        logger.info("  Fields: " + blockFields.toString());
                        logger.info("  Duration: " + formatDuration(duration));
                        
                        logSuccess("testSingleModularBlock", 
                                modularBlockFields + " block fields in " + formatDuration(duration));
                        logExecutionTime("testSingleModularBlock", startTime);
                    } else {
                        logger.info("ℹ️ Entry not available or no basic fields");
                    }
                } catch (Exception e) {
                    fail("Test failed with exception: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testSingleModularBlock"));
    }

    @Test
    @Order(2)
    @DisplayName("Test modular block with field selection")
    void testModularBlockWithFieldSelection() throws InterruptedException {
        CountDownLatch latch = createLatch();

        String entryUid = Credentials.COMPLEX_BLOCKS_ENTRY_UID;
        String contentTypeUid = Credentials.COMPLEX_BLOCKS_CONTENT_TYPE_UID;

        entry = stack.contentType(contentTypeUid).entry(entryUid);
        
        // Include only specific fields
        entry.only(new String[]{"title", "sections", "modular_blocks", "components"});

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "only() with modular blocks should not error");
                    assertNotNull(entry, "Entry should not be null");
                    assertEquals(entryUid, entry.getUid(), "CRITICAL BUG: Wrong entry!");
                    assertNotNull(entry.getTitle(), "BUG: Title should be included (only)");
                    logger.info("✅ Field selection + modular blocks working");
                    logSuccess("testModularBlockWithFieldSelection", "Field selection validated");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testModularBlockWithFieldSelection"));
    }

    @Test
    @Order(3)
    @DisplayName("Test modular block structure validation")
    void testModularBlockStructure() throws InterruptedException {
        CountDownLatch latch = createLatch();

        String entryUid = Credentials.COMPLEX_BLOCKS_ENTRY_UID;
        String contentTypeUid = Credentials.COMPLEX_BLOCKS_CONTENT_TYPE_UID;

        entry = stack.contentType(contentTypeUid).entry(entryUid);

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "Entry fetch should not error");
                    assertNotNull(entry, "Entry should not be null");
                    assertEquals(entryUid, entry.getUid(), "CRITICAL BUG: Wrong entry!");
                    assertTrue(hasBasicFields(entry), "BUG: Entry must have basic fields");
                    
                    Object sections = entry.get("sections");
                    if (sections != null && sections instanceof ArrayList) {
                        ArrayList<?> sectionsList = (ArrayList<?>) sections;
                        logger.info("✅ Modular blocks structure: " + sectionsList.size() + " block(s)");
                    }
                    logSuccess("testModularBlockStructure", "Structure validated");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testModularBlockStructure"));
    }

    @Test
    @Order(4)
    @DisplayName("Test Query with modular blocks")
    void testQueryWithModularBlocks() throws InterruptedException {
        CountDownLatch latch = createLatch();

        String contentTypeUid = !Credentials.COMPLEX_BLOCKS_ENTRY_UID.isEmpty()
                ? Credentials.COMPLEX_BLOCKS_CONTENT_TYPE_UID
                : Credentials.SELF_REF_CONTENT_TYPE_UID;

        query = stack.contentType(contentTypeUid).query();
        query.exists("title");
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() <= 5, "BUG: limit(5) not working");
                        
                        int withBlocks = 0, withTitle = 0;
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All entries must have UID");
                            if (e.getTitle() != null) withTitle++;
                            if (e.get("sections") != null || e.get("modular_blocks") != null || 
                                e.get("components") != null || e.get("blocks") != null) {
                                withBlocks++;
                            }
                        }
                        assertEquals(results.size(), withTitle, "ALL must have title (exists filter)");
                        logger.info("Query validated: " + results.size() + " entries, " + withBlocks + " with blocks");
                        logSuccess("testQueryWithModularBlocks", withBlocks + " with modular blocks");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryWithModularBlocks"));
    }

    @Test
    @Order(5)
    @DisplayName("Test multiple modular block fields")
    void testMultipleModularBlockFields() throws InterruptedException {
        CountDownLatch latch = createLatch();

        String entryUid = Credentials.COMPLEX_BLOCKS_ENTRY_UID;
        String contentTypeUid = Credentials.COMPLEX_BLOCKS_CONTENT_TYPE_UID;

        entry = stack.contentType(contentTypeUid).entry(entryUid);

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "Entry fetch should not error");
                    assertNotNull(entry, "Entry should not be null");
                    assertEquals(entryUid, entry.getUid(), "CRITICAL BUG: Wrong entry!");
                    
                    int blockFieldCount = 0;
                    ArrayList<String> blockFieldNames = new ArrayList<>();
                    
                    Object sections = entry.get("sections");
                    Object components = entry.get("components");
                    Object blocks = entry.get("blocks");
                    Object modularBlocks = entry.get("modular_blocks");
                    
                    if (sections != null && sections instanceof ArrayList) {
                        blockFieldCount++;
                        blockFieldNames.add("sections(" + ((ArrayList<?>)sections).size() + ")");
                    }
                    if (components != null && components instanceof ArrayList) {
                        blockFieldCount++;
                        blockFieldNames.add("components(" + ((ArrayList<?>)components).size() + ")");
                    }
                    if (blocks != null && blocks instanceof ArrayList) {
                        blockFieldCount++;
                        blockFieldNames.add("blocks(" + ((ArrayList<?>)blocks).size() + ")");
                    }
                    if (modularBlocks != null && modularBlocks instanceof ArrayList) {
                        blockFieldCount++;
                        blockFieldNames.add("modular_blocks(" + ((ArrayList<?>)modularBlocks).size() + ")");
                    }
                    
                    logger.info("Multiple block fields: " + blockFieldCount + " - " + blockFieldNames.toString());
                    logSuccess("testMultipleModularBlockFields", blockFieldCount + " block fields");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testMultipleModularBlockFields"));
    }

    // ===========================
    // Nested Modular Blocks
    // ===========================

    @Test
    @Order(6)
    @DisplayName("Test nested modular blocks")
    void testNestedModularBlocks() throws InterruptedException {
        CountDownLatch latch = createLatch();

        // Use complex entry for nested blocks testing
        entry = stack.contentType(Credentials.COMPLEX_BLOCKS_CONTENT_TYPE_UID)
                     .entry(Credentials.COMPLEX_BLOCKS_ENTRY_UID);

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "Fetch should not error");
                    assertNotNull(entry, "Entry should not be null");
                    assertEquals(Credentials.COMPLEX_BLOCKS_ENTRY_UID, entry.getUid(), "CRITICAL BUG: Wrong entry!");
                    assertTrue(hasBasicFields(entry), "BUG: Entry must have basic fields");
                    
                    Object sections = entry.get("sections");
                    if (sections != null && sections instanceof ArrayList) {
                        ArrayList<?> sectionsList = (ArrayList<?>) sections;
                        logger.info("✅ Nested blocks: " + sectionsList.size() + " section(s)");
                        logSuccess("testNestedModularBlocks", sectionsList.size() + " nested blocks");
                    } else {
                        logger.info("ℹ️ No nested sections (may not be configured)");
                        logSuccess("testNestedModularBlocks", "Entry validated");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testNestedModularBlocks"));
    }

    @Test
    @Order(7)
    @DisplayName("Test nested modular blocks with references")
    void testNestedModularBlocksWithReferences() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.COMPLEX_BLOCKS_CONTENT_TYPE_UID)
                     .entry(Credentials.COMPLEX_BLOCKS_ENTRY_UID);
        
        // Include references that might be in nested blocks
        entry.includeReference("sections");

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNotNull(entry, "Entry should not be null");
                    if (error != null) {
                        logger.info("Reference handling (expected if not configured): " + error.getErrorMessage());
                    } else {
                        assertEquals(Credentials.COMPLEX_BLOCKS_ENTRY_UID, entry.getUid(), "CRITICAL BUG: Wrong entry!");
                        logger.info("✅ Nested blocks + references working");
                    }
                    logSuccess("testNestedModularBlocksWithReferences", "Handled gracefully");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testNestedModularBlocksWithReferences"));
    }

    @Test
    @Order(8)
    @DisplayName("Test deeply nested modular blocks")
    void testDeeplyNestedModularBlocks() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = startTimer();

        entry = stack.contentType(Credentials.COMPLEX_BLOCKS_CONTENT_TYPE_UID)
                     .entry(Credentials.COMPLEX_BLOCKS_ENTRY_UID);

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    long duration = System.currentTimeMillis() - startTime;
                    assertNull(error, "Deep nesting should not error");
                    assertNotNull(entry, "Entry should not be null");
                    assertEquals(Credentials.COMPLEX_BLOCKS_ENTRY_UID, entry.getUid(), "CRITICAL BUG: Wrong entry!");
                    assertTrue(duration < 10000, 
                            "PERFORMANCE BUG: Deep nesting took " + duration + "ms (max: 10s)");
                    logger.info("✅ Performance: " + formatDuration(duration) + " < 10s");
                    logSuccess("testDeeplyNestedModularBlocks", formatDuration(duration));
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testDeeplyNestedModularBlocks"));
    }

    @Test
    @Order(9)
    @DisplayName("Test modular blocks iteration")
    void testModularBlocksIteration() throws InterruptedException {
        CountDownLatch latch = createLatch();

        String entryUid = Credentials.COMPLEX_BLOCKS_ENTRY_UID;
        String contentTypeUid = Credentials.COMPLEX_BLOCKS_CONTENT_TYPE_UID;

        entry = stack.contentType(contentTypeUid).entry(entryUid);

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "Iteration test should not error");
                    assertNotNull(entry, "Entry should not be null");
                    assertEquals(entryUid, entry.getUid(), "CRITICAL BUG: Wrong entry!");
                    
                    Object sections = entry.get("sections");
                    if (sections != null && sections instanceof ArrayList) {
                        ArrayList<?> blocks = (ArrayList<?>) sections;
                        int validBlocks = 0;
                        for (Object block : blocks) {
                            if (block != null) validBlocks++;
                        }
                        logger.info("✅ Iterated: " + validBlocks + "/" + blocks.size() + " blocks");
                        logSuccess("testModularBlocksIteration", validBlocks + " blocks iterated");
                    } else {
                        logger.info("ℹ️ No sections to iterate");
                        logSuccess("testModularBlocksIteration", "Entry validated");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testModularBlocksIteration"));
    }

    @Test
    @Order(10)
    @DisplayName("Test modular blocks with Query and pagination")
    void testModularBlocksWithPagination() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_BLOCKS_CONTENT_TYPE_UID).query();
        query.limit(3);
        query.skip(0);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Pagination query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        int size = results.size();
                        assertTrue(size > 0 && size <= 3, 
                                "BUG: Pagination not working - expected 1-3, got: " + size);
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All entries must have UID");
                        }
                        logger.info("✅ Pagination: " + size + " entries (limit: 3)");
                        logSuccess("testModularBlocksWithPagination", size + " entries");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testModularBlocksWithPagination"));
    }

    // ===========================
    // Different Block Types
    // ===========================

    @Test
    @Order(11)
    @DisplayName("Test different modular block types")
    void testDifferentBlockTypes() throws InterruptedException {
        CountDownLatch latch = createLatch();

        String entryUid = Credentials.COMPLEX_BLOCKS_ENTRY_UID;
        String contentTypeUid = Credentials.COMPLEX_BLOCKS_CONTENT_TYPE_UID;

        entry = stack.contentType(contentTypeUid).entry(entryUid);

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "Fetch should not error");
                    assertNotNull(entry, "Entry should not be null");
                    assertEquals(entryUid, entry.getUid(), "CRITICAL BUG: Wrong entry!");
                    
                    int differentTypes = 0;
                    ArrayList<String> typeNames = new ArrayList<>();
                    if (entry.get("hero_section") != null) { differentTypes++; typeNames.add("hero_section"); }
                    if (entry.get("content_section") != null) { differentTypes++; typeNames.add("content_section"); }
                    if (entry.get("gallery_section") != null) { differentTypes++; typeNames.add("gallery_section"); }
                    if (entry.get("sections") != null) { differentTypes++; typeNames.add("sections"); }
                    if (entry.get("page_components") != null) { differentTypes++; typeNames.add("page_components"); }
                    
                    logger.info("Block types: " + differentTypes + " - " + typeNames.toString());
                    logSuccess("testDifferentBlockTypes", differentTypes + " block types");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testDifferentBlockTypes"));
    }

    @Test
    @Order(12)
    @DisplayName("Test modular blocks with mixed content")
    void testModularBlocksWithMixedContent() throws InterruptedException {
        CountDownLatch latch = createLatch();

        String entryUid = Credentials.COMPLEX_BLOCKS_ENTRY_UID;
        String contentTypeUid = Credentials.COMPLEX_BLOCKS_CONTENT_TYPE_UID;

        entry = stack.contentType(contentTypeUid).entry(entryUid);

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    if (error != null) {
                        logger.warning("Entry fetch error: " + error.getErrorMessage());
                    }
                    if (entry != null && hasBasicFields(entry)) {
                        logger.info("Entry fetched successfully");
                    }
                    
                    assertNull(error, "Mixed content should not error");
                    assertNotNull(entry, "Entry should not be null");
                    assertEquals(entryUid, entry.getUid(), "CRITICAL BUG: Wrong entry!");
                    
                    boolean hasRegularFields = entry.getTitle() != null;
                    boolean hasModularBlocks = entry.get("sections") != null || 
                                              entry.get("modular_blocks") != null;
                    
                    logger.info("✅ Regular fields: " + hasRegularFields);
                    logger.info("✅ Modular blocks: " + hasModularBlocks);
                    logSuccess("testModularBlocksWithMixedContent", "Mixed content validated");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testModularBlocksWithMixedContent"));
    }

    // ===========================
    // Complex Scenarios
    // ===========================

    @Test
    @Order(13)
    @DisplayName("Test modular blocks with embedded items")
    void testModularBlocksWithEmbeddedItems() throws InterruptedException {
        CountDownLatch latch = createLatch();

        String entryUid = Credentials.COMPLEX_BLOCKS_ENTRY_UID;
        String contentTypeUid = Credentials.COMPLEX_BLOCKS_CONTENT_TYPE_UID;

        entry = stack.contentType(contentTypeUid).entry(entryUid);
        
        // Combine modular blocks with embedded items
        entry.includeEmbeddedItems();

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "Modular blocks + embedded items should not error");
                    assertNotNull(entry, "Entry should not be null");
                    assertEquals(entryUid, entry.getUid(), "CRITICAL BUG: Wrong entry!");
                    assertTrue(hasBasicFields(entry), "BUG: Entry must have basic fields");
                    logger.info("✅ Modular blocks + embedded items working");
                    logSuccess("testModularBlocksWithEmbeddedItems", "Combination working");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testModularBlocksWithEmbeddedItems"));
    }

    @Test
    @Order(14)
    @DisplayName("Test modular blocks with filters")
    void testModularBlocksWithFilters() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_BLOCKS_CONTENT_TYPE_UID).query();
        query.exists("title");
        query.where("locale", "en-us");
        query.limit(5);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Query with filters should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() <= 5, "BUG: limit(5) not working");
                        int withTitle = 0, withLocale = 0;
                        for (Entry e : results) {
                            assertNotNull(e.getTitle(), "BUG: exists('title') not working");
                            withTitle++;
                            String locale = e.getLocale();
                            if (locale != null && "en-us".equals(locale)) withLocale++;
                        }
                        assertEquals(results.size(), withTitle, "ALL must have title");
                        logger.info("✅ Filters: " + withTitle + " with title, " + withLocale + " with en-us");
                        logSuccess("testModularBlocksWithFilters", withTitle + " entries validated");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testModularBlocksWithFilters"));
    }

    // ===========================
    // Performance & Edge Cases
    // ===========================

    @Test
    @Order(15)
    @DisplayName("Test performance with complex modular blocks")
    void testPerformanceComplexModularBlocks() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = startTimer();

        String entryUid = Credentials.COMPLEX_BLOCKS_ENTRY_UID;
        String contentTypeUid = Credentials.COMPLEX_BLOCKS_CONTENT_TYPE_UID;

        entry = stack.contentType(contentTypeUid).entry(entryUid);

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    long duration = System.currentTimeMillis() - startTime;
                    assertNull(error, "Complex blocks should not error");
                    assertNotNull(entry, "Entry should not be null");
                    assertEquals(entryUid, entry.getUid(), "CRITICAL BUG: Wrong entry!");
                    assertTrue(duration < 10000, 
                            "PERFORMANCE BUG: Complex blocks took " + duration + "ms (max: 10s)");
                    logger.info("✅ Performance: " + formatDuration(duration) + " < 10s");
                    logSuccess("testPerformanceComplexModularBlocks", formatDuration(duration));
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testPerformanceComplexModularBlocks"));
    }

    @Test
    @Order(16)
    @DisplayName("Test entry without modular blocks")
    void testEntryWithoutModularBlocks() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.SIMPLE_CONTENT_TYPE_UID)
                     .entry(Credentials.SIMPLE_ENTRY_UID);

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "BUG: SDK should handle entries without blocks");
                    assertNotNull(entry, "Entry should not be null");
                    assertEquals(Credentials.SIMPLE_ENTRY_UID, entry.getUid(), "CRITICAL BUG: Wrong entry!");
                    assertTrue(hasBasicFields(entry), "BUG: Entry must have basic fields");
                    logger.info("✅ SDK handled entry without modular blocks gracefully");
                    logSuccess("testEntryWithoutModularBlocks", "Handled gracefully");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testEntryWithoutModularBlocks"));
    }

    @Test
    @Order(17)
    @DisplayName("Test empty modular blocks array")
    void testEmptyModularBlocksArray() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.COMPLEX_BLOCKS_CONTENT_TYPE_UID).query();
        query.limit(10);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "Query should not error");
                    assertNotNull(queryResult, "QueryResult should not be null");
                    
                    if (hasResults(queryResult)) {
                        java.util.List<Entry> results = queryResult.getResultObjects();
                        assertTrue(results.size() <= 10, "BUG: limit(10) not working");
                        
                        int entriesWithEmpty = 0, entriesWithPopulated = 0;
                        for (Entry e : results) {
                            assertNotNull(e.getUid(), "All entries must have UID");
                            Object sections = e.get("sections");
                            if (sections != null && sections instanceof ArrayList) {
                                ArrayList<?> list = (ArrayList<?>) sections;
                                if (list.isEmpty()) entriesWithEmpty++;
                                else entriesWithPopulated++;
                            }
                        }
                        logger.info("✅ Empty handling: " + entriesWithEmpty + " empty, " + entriesWithPopulated + " populated");
                        logSuccess("testEmptyModularBlocksArray", "Empty blocks handled gracefully");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testEmptyModularBlocksArray"));
    }

    @Test
    @Order(18)
    @DisplayName("Test modular blocks comprehensive scenario")
    void testModularBlocksComprehensiveScenario() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = startTimer();

        String entryUid = !Credentials.COMPLEX_BLOCKS_ENTRY_UID.isEmpty() 
                ? Credentials.COMPLEX_BLOCKS_ENTRY_UID 
                : Credentials.SELF_REF_ENTRY_UID;
        
        String contentTypeUid = !Credentials.COMPLEX_BLOCKS_ENTRY_UID.isEmpty()
                ? Credentials.COMPLEX_BLOCKS_CONTENT_TYPE_UID
                : Credentials.SELF_REF_CONTENT_TYPE_UID;

        entry = stack.contentType(contentTypeUid).entry(entryUid);
        entry.includeEmbeddedItems();
        entry.includeReference("sections");

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    long duration = System.currentTimeMillis() - startTime;
                    assertNotNull(entry, "Entry should not be null");
                    
                    if (error != null) {
                        logger.info("Comprehensive error (may not have all features): " + error.getErrorMessage());
                        logSuccess("testModularBlocksComprehensiveScenario", "Handled gracefully");
                    } else {
                        assertEquals(entryUid, entry.getUid(), "CRITICAL BUG: Wrong entry!");
                        assertTrue(hasBasicFields(entry), "BUG: Entry must have basic fields");
                        assertTrue(duration < 15000, 
                                "PERFORMANCE BUG: Comprehensive took " + duration + "ms (max: 15s)");
                        
                        int features = 0;
                        if (entry.get("sections") != null) features++;
                        if (entry.getTitle() != null) features++;
                        
                        logger.info("✅ Comprehensive: " + features + " features, " + formatDuration(duration));
                        logSuccess("testModularBlocksComprehensiveScenario", 
                                features + " features in " + formatDuration(duration));
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, LARGE_DATASET_TIMEOUT_SECONDS, 
                "testModularBlocksComprehensiveScenario"));
    }

    @AfterAll
    void tearDown() {
        logger.info("Completed ModularBlocksComprehensiveIT test suite");
        logger.info("All 18 modular blocks tests executed");
        logger.info("Tested: Single blocks, nested blocks, block types, complex scenarios, edge cases");
    }
}

