package com.contentstack.sdk;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

import java.util.concurrent.CountDownLatch;

/**
 * Integration tests for variant + branch header combinations.
 *
 * Network tests are skipped automatically when the required credentials
 * (variantUid, BRANCH_UID) are absent from the .env file.
 * Header-verification tests (Order 9-11) always run — no network needed.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Variant + Branch Integration Tests")
class VariantBranchIT extends BaseIntegrationTest {

    @BeforeAll
    void setUp() {
        logger.info("Variant support : " + Credentials.hasVariantSupport());
        logger.info("Branch support  : " + Credentials.hasBranchSupport());
        logger.info("Content type    : " + Credentials.CONTENT_TYPE);
        logger.info("Entry UID       : " + Credentials.ENTRY_UID);
    }

    // =========================================================
    // Entry.fetch() — single-entry with variant(s)
    // =========================================================

    @Test
    @Order(1)
    @DisplayName("fetch entry — single variant")
    void testFetchEntryWithSingleVariant() throws InterruptedException {
        assumeTrue(Credentials.hasVariantSupport(), "variantUid not configured");
        assumeFalse(Credentials.ENTRY_UID.isEmpty(), "assetUid not configured");

        CountDownLatch latch = createLatch();

        stack.contentType(Credentials.CONTENT_TYPE)
                .entry(Credentials.ENTRY_UID)
                .variants(Credentials.VARIANT_UID)
                .fetch(new EntryResultCallBack() {
                    @Override
                    public void onCompletion(ResponseType responseType, Error error) {
                        try {
                            assertNull(error, errorMessage("single variant", error));
                            logSuccess("testFetchEntryWithSingleVariant");
                        } finally {
                            latch.countDown();
                        }
                    }
                });

        assertTrue(awaitLatch(latch, "testFetchEntryWithSingleVariant"));
    }

    @Test
    @Order(2)
    @DisplayName("fetch entry — multiple variants")
    void testFetchEntryWithMultipleVariants() throws InterruptedException {
        assumeTrue(Credentials.hasVariantSupport() && Credentials.VARIANTS_UID.length > 1,
                "multiple variantsUid not configured");
        assumeFalse(Credentials.ENTRY_UID.isEmpty(), "assetUid not configured");

        CountDownLatch latch = createLatch();

        stack.contentType(Credentials.CONTENT_TYPE)
                .entry(Credentials.ENTRY_UID)
                .variants(Credentials.VARIANTS_UID)
                .fetch(new EntryResultCallBack() {
                    @Override
                    public void onCompletion(ResponseType responseType, Error error) {
                        try {
                            assertNull(error, errorMessage("multiple variants", error));
                            logSuccess("testFetchEntryWithMultipleVariants");
                        } finally {
                            latch.countDown();
                        }
                    }
                });

        assertTrue(awaitLatch(latch, "testFetchEntryWithMultipleVariants"));
    }

    @Test
    @Order(3)
    @DisplayName("fetch entry — single variant + branch")
    void testFetchEntryWithSingleVariantAndBranch() throws InterruptedException {
        assumeTrue(Credentials.hasVariantSupport(), "variantUid not configured");
        assumeTrue(Credentials.hasBranchSupport(), "BRANCH_UID not configured");
        assumeFalse(Credentials.ENTRY_UID.isEmpty(), "assetUid not configured");

        CountDownLatch latch = createLatch();

        stack.contentType(Credentials.CONTENT_TYPE)
                .entry(Credentials.ENTRY_UID)
                .variants(Credentials.VARIANT_UID, Credentials.BRANCH_UID)
                .fetch(new EntryResultCallBack() {
                    @Override
                    public void onCompletion(ResponseType responseType, Error error) {
                        try {
                            assertNull(error, errorMessage("single variant + branch", error));
                            logSuccess("testFetchEntryWithSingleVariantAndBranch");
                        } finally {
                            latch.countDown();
                        }
                    }
                });

        assertTrue(awaitLatch(latch, "testFetchEntryWithSingleVariantAndBranch"));
    }

    @Test
    @Order(4)
    @DisplayName("fetch entry — multiple variants + branch")
    void testFetchEntryWithMultipleVariantsAndBranch() throws InterruptedException {
        assumeTrue(Credentials.hasVariantSupport() && Credentials.VARIANTS_UID.length > 1,
                "multiple variantsUid not configured");
        assumeTrue(Credentials.hasBranchSupport(), "BRANCH_UID not configured");
        assumeFalse(Credentials.ENTRY_UID.isEmpty(), "assetUid not configured");

        CountDownLatch latch = createLatch();

        stack.contentType(Credentials.CONTENT_TYPE)
                .entry(Credentials.ENTRY_UID)
                .variants(Credentials.VARIANTS_UID, Credentials.BRANCH_UID)
                .fetch(new EntryResultCallBack() {
                    @Override
                    public void onCompletion(ResponseType responseType, Error error) {
                        try {
                            assertNull(error, errorMessage("multiple variants + branch", error));
                            logSuccess("testFetchEntryWithMultipleVariantsAndBranch");
                        } finally {
                            latch.countDown();
                        }
                    }
                });

        assertTrue(awaitLatch(latch, "testFetchEntryWithMultipleVariantsAndBranch"));
    }

    // =========================================================
    // Query.find() — all entries with variant(s)
    // =========================================================

    @Test
    @Order(5)
    @DisplayName("find entries — single variant")
    void testFindEntriesWithSingleVariant() throws InterruptedException {
        assumeTrue(Credentials.hasVariantSupport(), "variantUid not configured");

        CountDownLatch latch = createLatch();

        stack.contentType(Credentials.CONTENT_TYPE)
                .query()
                .variants(Credentials.VARIANT_UID)
                .find(new QueryResultsCallBack() {
                    @Override
                    public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                        try {
                            assertNull(error, errorMessage("find single variant", error));
                            assertNotNull(queryResult);
                            logSuccess("testFindEntriesWithSingleVariant",
                                    queryResult.getResultObjects().size() + " entries");
                        } finally {
                            latch.countDown();
                        }
                    }
                });

        assertTrue(awaitLatch(latch, "testFindEntriesWithSingleVariant"));
    }

    @Test
    @Order(6)
    @DisplayName("find entries — multiple variants")
    void testFindEntriesWithMultipleVariants() throws InterruptedException {
        assumeTrue(Credentials.hasVariantSupport() && Credentials.VARIANTS_UID.length > 1,
                "multiple variantsUid not configured");

        CountDownLatch latch = createLatch();

        stack.contentType(Credentials.CONTENT_TYPE)
                .query()
                .variants(Credentials.VARIANTS_UID)
                .find(new QueryResultsCallBack() {
                    @Override
                    public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                        try {
                            assertNull(error, errorMessage("find multiple variants", error));
                            assertNotNull(queryResult);
                            logSuccess("testFindEntriesWithMultipleVariants",
                                    queryResult.getResultObjects().size() + " entries");
                        } finally {
                            latch.countDown();
                        }
                    }
                });

        assertTrue(awaitLatch(latch, "testFindEntriesWithMultipleVariants"));
    }

    @Test
    @Order(7)
    @DisplayName("find entries — single variant + branch")
    void testFindEntriesWithSingleVariantAndBranch() throws InterruptedException {
        assumeTrue(Credentials.hasVariantSupport(), "variantUid not configured");
        assumeTrue(Credentials.hasBranchSupport(), "BRANCH_UID not configured");

        CountDownLatch latch = createLatch();

        stack.contentType(Credentials.CONTENT_TYPE)
                .query()
                .variants(Credentials.VARIANT_UID, Credentials.BRANCH_UID)
                .find(new QueryResultsCallBack() {
                    @Override
                    public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                        try {
                            assertNull(error, errorMessage("find single variant + branch", error));
                            assertNotNull(queryResult);
                            logSuccess("testFindEntriesWithSingleVariantAndBranch",
                                    queryResult.getResultObjects().size() + " entries");
                        } finally {
                            latch.countDown();
                        }
                    }
                });

        assertTrue(awaitLatch(latch, "testFindEntriesWithSingleVariantAndBranch"));
    }

    @Test
    @Order(8)
    @DisplayName("find entries — multiple variants + branch")
    void testFindEntriesWithMultipleVariantsAndBranch() throws InterruptedException {
        assumeTrue(Credentials.hasVariantSupport() && Credentials.VARIANTS_UID.length > 1,
                "multiple variantsUid not configured");
        assumeTrue(Credentials.hasBranchSupport(), "BRANCH_UID not configured");

        CountDownLatch latch = createLatch();

        stack.contentType(Credentials.CONTENT_TYPE)
                .query()
                .variants(Credentials.VARIANTS_UID, Credentials.BRANCH_UID)
                .find(new QueryResultsCallBack() {
                    @Override
                    public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                        try {
                            assertNull(error, errorMessage("find multiple variants + branch", error));
                            assertNotNull(queryResult);
                            logSuccess("testFindEntriesWithMultipleVariantsAndBranch",
                                    queryResult.getResultObjects().size() + " entries");
                        } finally {
                            latch.countDown();
                        }
                    }
                });

        assertTrue(awaitLatch(latch, "testFindEntriesWithMultipleVariantsAndBranch"));
    }

    // =========================================================
    // Header verification — no network, always run
    // =========================================================

    @Test
    @Order(9)
    @DisplayName("Entry: variant + branch headers are set correctly")
    void testEntryVariantBranchHeadersSet() throws IllegalAccessException {
        Entry entry = stack.contentType(Credentials.CONTENT_TYPE)
                .entry("dummy_uid")
                .variants("v1", "main");

        assertEquals("v1", entry.getHeaders().get("x-cs-variant-uid"));
        assertEquals("main", entry.getHeaders().get("branch"));
        logSuccess("testEntryVariantBranchHeadersSet");
    }

    @Test
    @Order(10)
    @DisplayName("Entry: multiple variants joined with comma, branch set")
    void testEntryMultipleVariantsJoinedWithComma() throws IllegalAccessException {
        Entry entry = stack.contentType(Credentials.CONTENT_TYPE)
                .entry("dummy_uid")
                .variants(new String[]{"v1", "v2", "v3"}, "staging");

        String header = (String) entry.getHeaders().get("x-cs-variant-uid");
        assertTrue(header.contains("v1") && header.contains("v2") && header.contains("v3"));
        assertEquals("staging", entry.getHeaders().get("branch"));
        logSuccess("testEntryMultipleVariantsJoinedWithComma");
    }

    @Test
    @Order(11)
    @DisplayName("Query: variant + branch headers are set correctly")
    void testQueryVariantBranchHeadersSet() {
        Query query = stack.contentType(Credentials.CONTENT_TYPE)
                .query()
                .variants("v1", "main");

        assertEquals("v1", query.headers.get("x-cs-variant-uid"));
        assertEquals("main", query.headers.get("branch"));
        logSuccess("testQueryVariantBranchHeadersSet");
    }

    @Test
    @Order(12)
    @DisplayName("Query: multiple variants joined with comma, branch set")
    void testQueryMultipleVariantsJoinedWithComma() {
        Query query = stack.contentType(Credentials.CONTENT_TYPE)
                .query()
                .variants(new String[]{"v1", "v2", "v3"}, "staging");

        String header = (String) query.headers.get("x-cs-variant-uid");
        assertTrue(header.contains("v1") && header.contains("v2") && header.contains("v3"));
        assertEquals("staging", query.headers.get("branch"));
        logSuccess("testQueryMultipleVariantsJoinedWithComma");
    }

    // =========================================================
    // Edge cases — header-only, always run
    // =========================================================

    @Test
    @Order(13)
    @DisplayName("Edge: whitespace-only variant is not set on entry")
    void testEntryWhitespaceVariantNotSet() throws IllegalAccessException {
        Entry entry = stack.contentType(Credentials.CONTENT_TYPE)
                .entry("dummy_uid")
                .variants("   ");

        assertFalse(entry.getHeaders().containsKey("x-cs-variant-uid"),
                "whitespace-only variant should not set header");
        logSuccess("testEntryWhitespaceVariantNotSet");
    }

    @Test
    @Order(14)
    @DisplayName("Edge: whitespace-only variant is not set on query")
    void testQueryWhitespaceVariantNotSet() {
        Query query = stack.contentType(Credentials.CONTENT_TYPE)
                .query()
                .variants("   ");

        assertFalse(query.headers.containsKey("x-cs-variant-uid"),
                "whitespace-only variant should not set header");
        logSuccess("testQueryWhitespaceVariantNotSet");
    }

    @Test
    @Order(15)
    @DisplayName("Edge: array of all-blank entries produces no variant header")
    void testAllBlankArrayProducesNoHeader() throws IllegalAccessException {
        Entry entry = stack.contentType(Credentials.CONTENT_TYPE)
                .entry("dummy_uid")
                .variants(new String[]{null, "", "  ", null}, "main");

        assertFalse(entry.getHeaders().containsKey("x-cs-variant-uid"),
                "all-blank array should not set variant header");
        assertEquals("main", entry.getHeaders().get("branch"),
                "branch should still be set even when variant array is all-blank");
        logSuccess("testAllBlankArrayProducesNoHeader");
    }

    @Test
    @Order(16)
    @DisplayName("Edge: single-element array produces same header as string overload")
    void testSingleElementArrayMatchesStringOverload() throws IllegalAccessException {
        Entry fromString = stack.contentType(Credentials.CONTENT_TYPE)
                .entry("dummy_uid")
                .variants("v1");

        Entry fromArray = stack.contentType(Credentials.CONTENT_TYPE)
                .entry("dummy_uid")
                .variants(new String[]{"v1"});

        assertEquals(
                fromString.getHeaders().get("x-cs-variant-uid"),
                fromArray.getHeaders().get("x-cs-variant-uid"),
                "single-element array should produce the same header as the string overload");
        logSuccess("testSingleElementArrayMatchesStringOverload");
    }

    @Test
    @Order(17)
    @DisplayName("Edge: second variants() call overwrites first")
    void testSecondVariantsCallOverwritesFirst() throws IllegalAccessException {
        Entry entry = stack.contentType(Credentials.CONTENT_TYPE)
                .entry("dummy_uid")
                .variants("v1", "branch-a")
                .variants("v2", "branch-b");

        assertEquals("v2", entry.getHeaders().get("x-cs-variant-uid"),
                "second call should overwrite variant");
        assertEquals("branch-b", entry.getHeaders().get("branch"),
                "second call should overwrite branch");
        logSuccess("testSecondVariantsCallOverwritesFirst");
    }

    @Test
    @Order(18)
    @DisplayName("Edge: variants() branch overrides stack-level branch")
    void testVariantsBranchOverridesStackLevelBranch() throws IllegalAccessException {
        Config config = new Config();
        config.setBranch("stack-branch");
        Stack localStack = Contentstack.stack(
                Credentials.API_KEY, Credentials.DELIVERY_TOKEN, Credentials.ENVIRONMENT, config);

        Entry entry = localStack.contentType(Credentials.CONTENT_TYPE)
                .entry("dummy_uid")
                .variants("v1", "request-branch");

        assertEquals("request-branch", entry.getHeaders().get("branch"),
                "request-level branch should override stack-level branch");
        logSuccess("testVariantsBranchOverridesStackLevelBranch");
    }

    @Test
    @Order(19)
    @DisplayName("Edge: variants() and includeBranch() can be chained together")
    void testVariantsAndIncludeBranchChaining() throws IllegalAccessException {
        Entry entry = stack.contentType(Credentials.CONTENT_TYPE)
                .entry("dummy_uid")
                .variants("v1", "main")
                .includeBranch();

        assertEquals("v1", entry.getHeaders().get("x-cs-variant-uid"));
        assertEquals("main", entry.getHeaders().get("branch"));
        assertTrue(entry.params.has("include_branch"),
                "includeBranch() query param should be set alongside variant headers");
        logSuccess("testVariantsAndIncludeBranchChaining");
    }

    @Test
    @Order(20)
    @DisplayName("Edge: whitespace-only branch is not added to entry headers")
    void testWhitespaceOnlyBranchNotSet() throws IllegalAccessException {
        Entry entry = stack.contentType(Credentials.CONTENT_TYPE)
                .entry("dummy_uid")
                .variants("v1", "   ");

        assertEquals("v1", entry.getHeaders().get("x-cs-variant-uid"));
        assertFalse(entry.getHeaders().containsKey("branch"),
                "whitespace-only branch should not set header");
        logSuccess("testWhitespaceOnlyBranchNotSet");
    }

    // =========================================================
    // Negative cases — network, skipped if credentials absent
    // =========================================================

    @Test
    @Order(21)
    @DisplayName("Negative: fetch with invalid branch returns error")
    void testFetchWithInvalidBranchReturnsError() throws InterruptedException {
        assumeTrue(Credentials.hasVariantSupport(), "variantUid not configured");
        assumeFalse(Credentials.ENTRY_UID.isEmpty(), "assetUid not configured");

        CountDownLatch latch = createLatch();
        boolean[] errorReceived = {false};

        stack.contentType(Credentials.CONTENT_TYPE)
                .entry(Credentials.ENTRY_UID)
                .variants(Credentials.VARIANT_UID, "non_existent_branch_xyz_404")
                .fetch(new EntryResultCallBack() {
                    @Override
                    public void onCompletion(ResponseType responseType, Error error) {
                        try {
                            errorReceived[0] = (error != null);
                            assertTrue(error != null,
                                    "fetching with a non-existent branch should return an error");
                            logger.info("Expected error received: [" + error.getErrorCode()
                                    + "] " + error.getErrorMessage());
                            logSuccess("testFetchWithInvalidBranchReturnsError",
                                    "error code: " + error.getErrorCode());
                        } finally {
                            latch.countDown();
                        }
                    }
                });

        assertTrue(awaitLatch(latch, "testFetchWithInvalidBranchReturnsError"));
        assertTrue(errorReceived[0], "callback must have been invoked with an error");
    }

    @Test
    @Order(22)
    @DisplayName("Negative: find with invalid branch returns error")
    void testFindWithInvalidBranchReturnsError() throws InterruptedException {
        assumeTrue(Credentials.hasVariantSupport(), "variantUid not configured");

        CountDownLatch latch = createLatch();
        boolean[] errorReceived = {false};

        stack.contentType(Credentials.CONTENT_TYPE)
                .query()
                .variants(Credentials.VARIANT_UID, "non_existent_branch_xyz_404")
                .find(new QueryResultsCallBack() {
                    @Override
                    public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                        try {
                            errorReceived[0] = (error != null);
                            assertTrue(error != null,
                                    "querying with a non-existent branch should return an error");
                            logger.info("Expected error received: [" + error.getErrorCode()
                                    + "] " + error.getErrorMessage());
                            logSuccess("testFindWithInvalidBranchReturnsError",
                                    "error code: " + error.getErrorCode());
                        } finally {
                            latch.countDown();
                        }
                    }
                });

        assertTrue(awaitLatch(latch, "testFindWithInvalidBranchReturnsError"));
        assertTrue(errorReceived[0], "callback must have been invoked with an error");
    }

    @Test
    @Order(23)
    @DisplayName("Negative: fetch non-existent entry UID with variant returns error")
    void testFetchNonExistentEntryWithVariantReturnsError() throws InterruptedException {
        assumeTrue(Credentials.hasVariantSupport(), "variantUid not configured");

        CountDownLatch latch = createLatch();
        boolean[] errorReceived = {false};

        stack.contentType(Credentials.CONTENT_TYPE)
                .entry("non_existent_entry_uid_xyz_404")
                .variants(Credentials.VARIANT_UID)
                .fetch(new EntryResultCallBack() {
                    @Override
                    public void onCompletion(ResponseType responseType, Error error) {
                        try {
                            errorReceived[0] = (error != null);
                            assertTrue(error != null,
                                    "fetching a non-existent entry should return an error");
                            logger.info("Expected error received: [" + error.getErrorCode()
                                    + "] " + error.getErrorMessage());
                            logSuccess("testFetchNonExistentEntryWithVariantReturnsError",
                                    "error code: " + error.getErrorCode());
                        } finally {
                            latch.countDown();
                        }
                    }
                });

        assertTrue(awaitLatch(latch, "testFetchNonExistentEntryWithVariantReturnsError"));
        assertTrue(errorReceived[0], "callback must have been invoked with an error");
    }

    @Test
    @Order(24)
    @DisplayName("Negative: find with invalid branch + multiple variants returns error")
    void testFindWithInvalidBranchAndMultipleVariantsReturnsError() throws InterruptedException {
        assumeTrue(Credentials.hasVariantSupport() && Credentials.VARIANTS_UID.length > 1,
                "multiple variantsUid not configured");

        CountDownLatch latch = createLatch();
        boolean[] errorReceived = {false};

        stack.contentType(Credentials.CONTENT_TYPE)
                .query()
                .variants(Credentials.VARIANTS_UID, "non_existent_branch_xyz_404")
                .find(new QueryResultsCallBack() {
                    @Override
                    public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                        try {
                            errorReceived[0] = (error != null);
                            assertTrue(error != null,
                                    "querying with invalid branch + multiple variants should return an error");
                            logger.info("Expected error received: [" + error.getErrorCode()
                                    + "] " + error.getErrorMessage());
                            logSuccess("testFindWithInvalidBranchAndMultipleVariantsReturnsError",
                                    "error code: " + error.getErrorCode());
                        } finally {
                            latch.countDown();
                        }
                    }
                });

        assertTrue(awaitLatch(latch, "testFindWithInvalidBranchAndMultipleVariantsReturnsError"));
        assertTrue(errorReceived[0], "callback must have been invoked with an error");
    }

    // =========================================================
    // Helpers
    // =========================================================

    private String errorMessage(String context, Error error) {
        return context + " should not error" +
                (error != null ? ": [" + error.getErrorCode() + "] " + error.getErrorMessage() : "");
    }
}
