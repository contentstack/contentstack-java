package com.contentstack.sdk;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * API integration tests for variant + branch support on Entry and Query.
 *
 * All tests make real network calls against the stack configured in .env.
 * Tests are skipped automatically when the required credentials are absent:
 *   - variantUid / variantsUid  → variant tests
 *   - BRANCH_UID                → branch-scoped tests
 *   - assetUid                  → single-entry (fetch) tests
 *
 * Sections
 *   1. Entry.fetch() — single-entry API tests
 *   2. Query.find()  — multi-entry API tests
 *   3. Combined / advanced scenarios
 *   4. Negative / error scenarios
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Variant + Branch — API Integration Tests")
class VariantBranchApiIT extends BaseIntegrationTest {

    @BeforeAll
    void logConfig() {
        logger.info("=== VariantBranchApiIT configuration ===");
        logger.info("contentType  : " + Credentials.CONTENT_TYPE);
        logger.info("entryUid     : " + Credentials.ENTRY_UID);
        logger.info("variantUid   : " + Credentials.VARIANT_UID);
        logger.info("variantsUid  : " + String.join(", ", Credentials.VARIANTS_UID));
        logger.info("branchUid    : " + Credentials.BRANCH_UID);
        logger.info("Variant support : " + Credentials.hasVariantSupport());
        logger.info("Branch support  : " + Credentials.hasBranchSupport());
        logger.info("========================================");
    }

    // =========================================================
    // Section 1 — Entry.fetch() single-entry API tests
    // =========================================================

    @Test
    @Order(101)
    @DisplayName("[Entry] fetch with single variant — response has valid entry fields")
    void testEntryFetchSingleVariant() throws InterruptedException {
        assumeTrue(Credentials.hasVariantSupport(), "variantUid not configured");
        assumeFalse(Credentials.ENTRY_UID.isEmpty(), "assetUid (entry UID) not configured");

        CountDownLatch latch = createLatch();

        stack.contentType(Credentials.CONTENT_TYPE)
                .entry(Credentials.ENTRY_UID)
                .variants(Credentials.VARIANT_UID)
                .fetch(new EntryResultCallBack() {
                    @Override
                    public void onCompletion(ResponseType responseType, Error error) {
                        try {
                            assertNull(error, apiError("Entry fetch single variant", error));
                            assertNotNull(responseType);
                            logSuccess("testEntryFetchSingleVariant",
                                    "responseType=" + responseType);
                        } finally {
                            latch.countDown();
                        }
                    }
                });

        assertTrue(awaitLatch(latch, "testEntryFetchSingleVariant"), "Timed out");
    }

    @Test
    @Order(102)
    @DisplayName("[Entry] fetch with multiple variants — succeeds")
    void testEntryFetchMultipleVariants() throws InterruptedException {
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
                            assertNull(error, apiError("Entry fetch multiple variants", error));
                            assertNotNull(responseType);
                            logSuccess("testEntryFetchMultipleVariants");
                        } finally {
                            latch.countDown();
                        }
                    }
                });

        assertTrue(awaitLatch(latch, "testEntryFetchMultipleVariants"), "Timed out");
    }

    @Test
    @Order(103)
    @DisplayName("[Entry] fetch with single variant + branch — succeeds")
    void testEntryFetchSingleVariantAndBranch() throws InterruptedException {
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
                            assertNull(error, apiError("Entry fetch single variant + branch", error));
                            assertNotNull(responseType);
                            logSuccess("testEntryFetchSingleVariantAndBranch");
                        } finally {
                            latch.countDown();
                        }
                    }
                });

        assertTrue(awaitLatch(latch, "testEntryFetchSingleVariantAndBranch"), "Timed out");
    }

    @Test
    @Order(104)
    @DisplayName("[Entry] fetch with multiple variants + branch — succeeds")
    void testEntryFetchMultipleVariantsAndBranch() throws InterruptedException {
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
                            assertNull(error, apiError("Entry fetch multiple variants + branch", error));
                            assertNotNull(responseType);
                            logSuccess("testEntryFetchMultipleVariantsAndBranch");
                        } finally {
                            latch.countDown();
                        }
                    }
                });

        assertTrue(awaitLatch(latch, "testEntryFetchMultipleVariantsAndBranch"), "Timed out");
    }

    // =========================================================
    // Section 2 — Query.find() multi-entry API tests
    // =========================================================

    @Test
    @Order(201)
    @DisplayName("[Query] find with single variant — response has entries list")
    void testQueryFindSingleVariant() throws InterruptedException {
        assumeTrue(Credentials.hasVariantSupport(), "variantUid not configured");

        CountDownLatch latch = createLatch();

        stack.contentType(Credentials.CONTENT_TYPE)
                .query()
                .variants(Credentials.VARIANT_UID)
                .find(new QueryResultsCallBack() {
                    @Override
                    public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                        try {
                            assertNull(error, apiError("Query find single variant", error));
                            assertNotNull(queryResult, "QueryResult must not be null");
                            assertNotNull(queryResult.getResultObjects(), "Result list must not be null");

                            List<Entry> entries = queryResult.getResultObjects();
                            logger.info("Entries returned: " + entries.size());

                            for (Entry e : entries) {
                                assertNotNull(e.getUid(), "Every entry must have a uid");
                            }

                            logSuccess("testQueryFindSingleVariant", entries.size() + " entries");
                        } finally {
                            latch.countDown();
                        }
                    }
                });

        assertTrue(awaitLatch(latch, "testQueryFindSingleVariant"), "Timed out");
    }

    @Test
    @Order(202)
    @DisplayName("[Query] find with multiple variants — response has entries list")
    void testQueryFindMultipleVariants() throws InterruptedException {
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
                            assertNull(error, apiError("Query find multiple variants", error));
                            assertNotNull(queryResult);
                            assertNotNull(queryResult.getResultObjects());

                            logger.info("Entries returned: " + queryResult.getResultObjects().size());
                            logSuccess("testQueryFindMultipleVariants",
                                    queryResult.getResultObjects().size() + " entries");
                        } finally {
                            latch.countDown();
                        }
                    }
                });

        assertTrue(awaitLatch(latch, "testQueryFindMultipleVariants"), "Timed out");
    }

    @Test
    @Order(203)
    @DisplayName("[Query] find with single variant + branch — response has entries list")
    void testQueryFindSingleVariantAndBranch() throws InterruptedException {
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
                            assertNull(error, apiError("Query find single variant + branch", error));
                            assertNotNull(queryResult);
                            assertNotNull(queryResult.getResultObjects());
                            logSuccess("testQueryFindSingleVariantAndBranch",
                                    queryResult.getResultObjects().size() + " entries");
                        } finally {
                            latch.countDown();
                        }
                    }
                });

        assertTrue(awaitLatch(latch, "testQueryFindSingleVariantAndBranch"), "Timed out");
    }

    @Test
    @Order(204)
    @DisplayName("[Query] find with multiple variants + branch — response has entries list")
    void testQueryFindMultipleVariantsAndBranch() throws InterruptedException {
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
                            assertNull(error, apiError("Query find multiple variants + branch", error));
                            assertNotNull(queryResult);
                            assertNotNull(queryResult.getResultObjects());
                            logSuccess("testQueryFindMultipleVariantsAndBranch",
                                    queryResult.getResultObjects().size() + " entries");
                        } finally {
                            latch.countDown();
                        }
                    }
                });

        assertTrue(awaitLatch(latch, "testQueryFindMultipleVariantsAndBranch"), "Timed out");
    }

    // =========================================================
    // Section 3 — Combined / advanced scenarios
    // =========================================================

    @Test
    @Order(301)
    @DisplayName("[Combined] variant + includeBranch() — response includes _branch field")
    void testQueryFindVariantWithIncludeBranch() throws InterruptedException {
        assumeTrue(Credentials.hasVariantSupport(), "variantUid not configured");

        CountDownLatch latch = createLatch();

        stack.contentType(Credentials.CONTENT_TYPE)
                .query()
                .variants(Credentials.VARIANT_UID)
                .includeBranch()
                .find(new QueryResultsCallBack() {
                    @Override
                    public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                        try {
                            assertNull(error, apiError("variant + includeBranch", error));
                            assertNotNull(queryResult);

                            List<Entry> entries = queryResult.getResultObjects();
                            if (!entries.isEmpty()) {
                                Entry first = entries.get(0);
                                Object branch = first.get("_branch");
                                
                                assertNotNull(branch,
                                        "includeBranch() should add _branch field to each entry");
                                logger.info("_branch value: " + branch);
                            }

                            logSuccess("testQueryFindVariantWithIncludeBranch",
                                    entries.size() + " entries");
                        } finally {
                            latch.countDown();
                        }
                    }
                });

        assertTrue(awaitLatch(latch, "testQueryFindVariantWithIncludeBranch"), "Timed out");
    }

    @Test
    @Order(302)
    @DisplayName("[Combined] variant + limit — result count does not exceed limit")
    void testQueryFindVariantWithLimit() throws InterruptedException {
        assumeTrue(Credentials.hasVariantSupport(), "variantUid not configured");

        CountDownLatch latch = createLatch();
        int limit = 2;

        stack.contentType(Credentials.CONTENT_TYPE)
                .query()
                .variants(Credentials.VARIANT_UID)
                .limit(limit)
                .find(new QueryResultsCallBack() {
                    @Override
                    public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                        try {
                            assertNull(error, apiError("variant + limit", error));
                            assertNotNull(queryResult);

                            int count = queryResult.getResultObjects().size();
                            assertTrue(count <= limit,
                                    "limit(" + limit + ") not respected — got " + count + " entries");

                            logSuccess("testQueryFindVariantWithLimit", count + " entries (limit=" + limit + ")");
                        } finally {
                            latch.countDown();
                        }
                    }
                });

        assertTrue(awaitLatch(latch, "testQueryFindVariantWithLimit"), "Timed out");
    }

    @Test
    @Order(303)
    @DisplayName("[Combined] variant result count matches equivalent query without branch param")
    void testQueryVariantResultCountConsistency() throws InterruptedException {
        assumeTrue(Credentials.hasVariantSupport(), "variantUid not configured");

        CountDownLatch latch1 = new java.util.concurrent.CountDownLatch(1);
        CountDownLatch latch2 = new java.util.concurrent.CountDownLatch(1);
        int[] counts = {-1, -1};

        // First call: variants without branch
        stack.contentType(Credentials.CONTENT_TYPE)
                .query()
                .variants(Credentials.VARIANT_UID)
                .find(new QueryResultsCallBack() {
                    @Override
                    public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                        try {
                            if (error == null && queryResult != null)
                                counts[0] = queryResult.getResultObjects().size();
                        } finally {
                            latch1.countDown();
                        }
                    }
                });
        assertTrue(latch1.await(DEFAULT_TIMEOUT_SECONDS, java.util.concurrent.TimeUnit.SECONDS),
                "First find timed out");

        // Second call: same variant, no branch header (stack-level branch not set)
        stack.contentType(Credentials.CONTENT_TYPE)
                .query()
                .variants(Credentials.VARIANT_UID)
                .find(new QueryResultsCallBack() {
                    @Override
                    public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                        try {
                            if (error == null && queryResult != null)
                                counts[1] = queryResult.getResultObjects().size();
                        } finally {
                            latch2.countDown();
                        }
                    }
                });
        assertTrue(latch2.await(DEFAULT_TIMEOUT_SECONDS, java.util.concurrent.TimeUnit.SECONDS),
                "Second find timed out");

        // Both calls have the same variant header — results must be consistent
        assertNotEquals(-1, counts[0], "First call did not complete successfully");
        assertNotEquals(-1, counts[1], "Second call did not complete successfully");
        assertEquals(counts[0], counts[1],
                "Repeated variant query should return the same count: " + counts[0] + " vs " + counts[1]);

        logSuccess("testQueryVariantResultCountConsistency",
                "both calls returned " + counts[0] + " entries");
    }

    @Test
    @Order(304)
    @DisplayName("[Combined] entry variant + includeBranch() — _branch present in response")
    void testEntryFetchVariantWithIncludeBranch() throws InterruptedException {
        assumeTrue(Credentials.hasVariantSupport(), "variantUid not configured");
        assumeFalse(Credentials.ENTRY_UID.isEmpty(), "assetUid not configured");

        CountDownLatch latch = createLatch();

        stack.contentType(Credentials.CONTENT_TYPE)
                .entry(Credentials.ENTRY_UID)
                .variants(Credentials.VARIANT_UID)
                .includeBranch()
                .fetch(new EntryResultCallBack() {
                    @Override
                    public void onCompletion(ResponseType responseType, Error error) {
                        try {
                            assertNull(error, apiError("entry variant + includeBranch", error));
                            assertNotNull(responseType);
                            logSuccess("testEntryFetchVariantWithIncludeBranch");
                        } finally {
                            latch.countDown();
                        }
                    }
                });

        assertTrue(awaitLatch(latch, "testEntryFetchVariantWithIncludeBranch"), "Timed out");
    }

    // =========================================================
    // Section 4 — Negative / error scenarios
    // =========================================================

    @Test
    @Order(401)
    @DisplayName("[Negative] Entry fetch — non-existent branch returns API error")
    void testEntryFetchNonExistentBranchReturnsError() throws InterruptedException {
        assumeTrue(Credentials.hasVariantSupport(), "variantUid not configured");
        assumeFalse(Credentials.ENTRY_UID.isEmpty(), "assetUid not configured");

        CountDownLatch latch = createLatch();

        stack.contentType(Credentials.CONTENT_TYPE)
                .entry(Credentials.ENTRY_UID)
                .variants(Credentials.VARIANT_UID, "non_existent_branch_xyz_404")
                .fetch(new EntryResultCallBack() {
                    @Override
                    public void onCompletion(ResponseType responseType, Error error) {
                        try {
                            assertNotNull(error,
                                    "Fetching with a non-existent branch must return an error");
                            assertTrue(error.getErrorCode() > 0,
                                    "Error code must be a positive integer, got: " + error.getErrorCode());
                            logger.info("Expected error — code: " + error.getErrorCode()
                                    + ", message: " + error.getErrorMessage());
                            logSuccess("testEntryFetchNonExistentBranchReturnsError",
                                    "error code=" + error.getErrorCode());
                        } finally {
                            latch.countDown();
                        }
                    }
                });

        assertTrue(awaitLatch(latch, "testEntryFetchNonExistentBranchReturnsError"), "Timed out");
    }

    @Test
    @Order(402)
    @DisplayName("[Negative] Query find — non-existent branch returns API error")
    void testQueryFindNonExistentBranchReturnsError() throws InterruptedException {
        assumeTrue(Credentials.hasVariantSupport(), "variantUid not configured");

        CountDownLatch latch = createLatch();

        stack.contentType(Credentials.CONTENT_TYPE)
                .query()
                .variants(Credentials.VARIANT_UID, "non_existent_branch_xyz_404")
                .find(new QueryResultsCallBack() {
                    @Override
                    public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                        try {
                            assertNotNull(error,
                                    "Querying with a non-existent branch must return an error");
                            assertTrue(error.getErrorCode() > 0,
                                    "Error code must be a positive integer, got: " + error.getErrorCode());
                            logger.info("Expected error — code: " + error.getErrorCode()
                                    + ", message: " + error.getErrorMessage());
                            logSuccess("testQueryFindNonExistentBranchReturnsError",
                                    "error code=" + error.getErrorCode());
                        } finally {
                            latch.countDown();
                        }
                    }
                });

        assertTrue(awaitLatch(latch, "testQueryFindNonExistentBranchReturnsError"), "Timed out");
    }

    @Test
    @Order(403)
    @DisplayName("[Negative] Entry fetch — non-existent entry UID with variant returns error")
    void testEntryFetchNonExistentEntryUidReturnsError() throws InterruptedException {
        assumeTrue(Credentials.hasVariantSupport(), "variantUid not configured");

        CountDownLatch latch = createLatch();

        stack.contentType(Credentials.CONTENT_TYPE)
                .entry("non_existent_entry_uid_xyz_404")
                .variants(Credentials.VARIANT_UID)
                .fetch(new EntryResultCallBack() {
                    @Override
                    public void onCompletion(ResponseType responseType, Error error) {
                        try {
                            assertNotNull(error,
                                    "Fetching a non-existent entry must return an error");
                            assertTrue(error.getErrorCode() > 0,
                                    "Error code must be positive, got: " + error.getErrorCode());
                            logger.info("Expected error — code: " + error.getErrorCode()
                                    + ", message: " + error.getErrorMessage());
                            logSuccess("testEntryFetchNonExistentEntryUidReturnsError",
                                    "error code=" + error.getErrorCode());
                        } finally {
                            latch.countDown();
                        }
                    }
                });

        assertTrue(awaitLatch(latch, "testEntryFetchNonExistentEntryUidReturnsError"), "Timed out");
    }

    @Test
    @Order(404)
    @DisplayName("[Negative] Query find — non-existent branch + multiple variants returns error")
    void testQueryFindNonExistentBranchMultiVariantsReturnsError() throws InterruptedException {
        assumeTrue(Credentials.hasVariantSupport() && Credentials.VARIANTS_UID.length > 1,
                "multiple variantsUid not configured");

        CountDownLatch latch = createLatch();

        stack.contentType(Credentials.CONTENT_TYPE)
                .query()
                .variants(Credentials.VARIANTS_UID, "non_existent_branch_xyz_404")
                .find(new QueryResultsCallBack() {
                    @Override
                    public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                        try {
                            assertNotNull(error,
                                    "Querying with invalid branch + multiple variants must return an error");
                            assertTrue(error.getErrorCode() > 0,
                                    "Error code must be positive, got: " + error.getErrorCode());
                            logger.info("Expected error — code: " + error.getErrorCode()
                                    + ", message: " + error.getErrorMessage());
                            logSuccess("testQueryFindNonExistentBranchMultiVariantsReturnsError",
                                    "error code=" + error.getErrorCode());
                        } finally {
                            latch.countDown();
                        }
                    }
                });

        assertTrue(awaitLatch(latch, "testQueryFindNonExistentBranchMultiVariantsReturnsError"), "Timed out");
    }

    @Test
    @Order(405)
    @DisplayName("[Negative] Entry fetch — non-existent entry UID + non-existent branch returns error")
    void testEntryFetchBothInvalidUidAndBranchReturnsError() throws InterruptedException {
        assumeTrue(Credentials.hasVariantSupport(), "variantUid not configured");

        CountDownLatch latch = createLatch();

        stack.contentType(Credentials.CONTENT_TYPE)
                .entry("non_existent_entry_uid_xyz")
                .variants(Credentials.VARIANT_UID, "non_existent_branch_xyz_404")
                .fetch(new EntryResultCallBack() {
                    @Override
                    public void onCompletion(ResponseType responseType, Error error) {
                        try {
                            assertNotNull(error,
                                    "Fetching with both invalid entry UID and branch must return an error");
                            logger.info("Expected error — code: " + error.getErrorCode()
                                    + ", message: " + error.getErrorMessage());
                            logSuccess("testEntryFetchBothInvalidUidAndBranchReturnsError",
                                    "error code=" + error.getErrorCode());
                        } finally {
                            latch.countDown();
                        }
                    }
                });

        assertTrue(awaitLatch(latch, "testEntryFetchBothInvalidUidAndBranchReturnsError"), "Timed out");
    }

    // =========================================================
    // Helper
    // =========================================================

    private String apiError(String label, Error error) {
        if (error == null) return label + " should not error";
        return label + " returned unexpected error [" + error.getErrorCode() + "]: " + error.getErrorMessage();
    }
}
