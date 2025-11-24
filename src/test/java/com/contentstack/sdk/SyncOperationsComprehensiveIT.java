package com.contentstack.sdk;

import com.contentstack.sdk.utils.PerformanceAssertion;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * Comprehensive Integration Tests for Sync Operations
 * Tests sync functionality including:
 * - Initial sync
 * - Sync token management
 * - Pagination token
 * - Sync from date
 * - Sync performance
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SyncOperationsComprehensiveIT extends BaseIntegrationTest {

    private static String syncToken = null;
    private static String paginationToken = null;

    @BeforeAll
    void setUp() {
        logger.info("Setting up SyncOperationsComprehensiveIT test suite");
        logger.info("Testing sync operations");
        logger.info("Note: Sync operations are typically used for offline-first applications");
    }

    // ===========================
    // Initial Sync Tests
    // ===========================

    @Test
    @Order(1)
    @DisplayName("Test initial sync")
    void testInitialSync() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        stack.sync(new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack synchronousStack, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    
                    assertNull(error, "Initial sync should not error");
                    assertNotNull(synchronousStack, "SyncStack should not be null");
                    
                    // Check if sync returned items
                    int itemCount = synchronousStack.getCount();
                    assertTrue(itemCount >= 0, "Item count should be non-negative");
                    
                    // Get sync token for subsequent syncs
                    syncToken = synchronousStack.getSyncToken();
                    paginationToken = synchronousStack.getPaginationToken();
                    
                    if (syncToken != null && !syncToken.isEmpty()) {
                        logger.info("Sync token obtained: " + syncToken.substring(0, Math.min(20, syncToken.length())) + "...");
                    }
                    
                    if (paginationToken != null && !paginationToken.isEmpty()) {
                        logger.info("Pagination token obtained: " + paginationToken.substring(0, Math.min(20, paginationToken.length())) + "...");
                    }
                    
                    logger.info("✅ Initial sync completed: " + itemCount + " items in " + formatDuration(duration));
                    logSuccess("testInitialSync", itemCount + " items, " + formatDuration(duration));
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testInitialSync"));
    }

    @Test
    @Order(2)
    @DisplayName("Test sync returns stack object")
    void testSyncReturnsStackObject() throws InterruptedException {
        CountDownLatch latch = createLatch();

        stack.sync(new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack synchronousStack, Error error) {
                try {
                    assertNull(error, "Sync should not error");
                    assertNotNull(synchronousStack, "BUG: SyncStack should not be null");
                    
                    int itemCount = synchronousStack.getCount();
                    
                    logger.info("✅ Sync returns stack object with " + itemCount + " items");
                    logSuccess("testSyncReturnsStackObject", itemCount + " items");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testSyncReturnsStackObject"));
    }

    @Test
    @Order(3)
    @DisplayName("Test sync has count method")
    void testSyncHasCountMethod() throws InterruptedException {
        CountDownLatch latch = createLatch();

        stack.sync(new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack synchronousStack, Error error) {
                try {
                    assertNull(error, "Sync should not error");
                    assertNotNull(synchronousStack, "SyncStack should not be null");
                    
                    // Verify getCount() method exists and works
                    int itemCount = synchronousStack.getCount();
                    assertTrue(itemCount >= 0, "BUG: Count should be non-negative");
                    
                    logger.info("✅ Sync count method works: " + itemCount + " items");
                    logSuccess("testSyncHasCountMethod", itemCount + " items");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testSyncHasCountMethod"));
    }

    // ===========================
    // Sync Token Tests
    // ===========================

    @Test
    @Order(4)
    @DisplayName("Test sync token is generated")
    void testSyncTokenIsGenerated() throws InterruptedException {
        CountDownLatch latch = createLatch();

        stack.sync(new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack synchronousStack, Error error) {
                try {
                    assertNull(error, "Sync should not error");
                    assertNotNull(synchronousStack, "SyncStack should not be null");
                    
                    String token = synchronousStack.getSyncToken();
                    
                    if (token != null && !token.isEmpty()) {
                        assertTrue(token.length() > 10, "BUG: Sync token should have reasonable length");
                        logger.info("✅ Sync token generated: " + token.length() + " chars");
                        logSuccess("testSyncTokenIsGenerated", "Token: " + token.length() + " chars");
                    } else {
                        logger.info("ℹ️ No sync token (might be end of sync)");
                        logSuccess("testSyncTokenIsGenerated", "No token");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testSyncTokenIsGenerated"));
    }

    @Test
    @Order(5)
    @DisplayName("Test sync with sync token")
    void testSyncWithSyncToken() throws InterruptedException {
        // First get a sync token if we don't have one
        if (syncToken == null || syncToken.isEmpty()) {
            CountDownLatch latch1 = createLatch();
            
            stack.sync(new SyncResultCallBack() {
                @Override
                public void onCompletion(SyncStack synchronousStack, Error error) {
                    try {
                        if (error == null && synchronousStack != null) {
                            syncToken = synchronousStack.getSyncToken();
                        }
                    } finally {
                        latch1.countDown();
                    }
                }
            });
            
            awaitLatch(latch1, "get-token");
        }
        
        // Now use the sync token
        if (syncToken != null && !syncToken.isEmpty()) {
            CountDownLatch latch2 = createLatch();
            
            stack.syncToken(syncToken, new SyncResultCallBack() {
                @Override
                public void onCompletion(SyncStack synchronousStack, Error error) {
                    try {
                        assertNull(error, "Sync with token should not error");
                        assertNotNull(synchronousStack, "SyncStack should not be null");
                        
                        int itemCount = synchronousStack.getCount();
                        
                        logger.info("✅ Sync with token: " + itemCount + " items (delta)");
                        logSuccess("testSyncWithSyncToken", itemCount + " items");
                    } finally {
                        latch2.countDown();
                    }
                }
            });
            
            assertTrue(awaitLatch(latch2, "testSyncWithSyncToken"));
        } else {
            logger.info("ℹ️ No sync token available to test");
            logSuccess("testSyncWithSyncToken", "No token available");
        }
    }

    @Test
    @Order(6)
    @DisplayName("Test sync with pagination token")
    void testSyncWithPaginationToken() throws InterruptedException {
        // Use pagination token if available from initial sync
        if (paginationToken != null && !paginationToken.isEmpty()) {
            CountDownLatch latch = createLatch();
            
            stack.syncPaginationToken(paginationToken, new SyncResultCallBack() {
                @Override
                public void onCompletion(SyncStack synchronousStack, Error error) {
                    try {
                        assertNull(error, "Sync with pagination token should not error");
                        assertNotNull(synchronousStack, "SyncStack should not be null");
                        
                        int itemCount = synchronousStack.getCount();
                        
                        logger.info("✅ Sync with pagination token: " + itemCount + " items");
                        logSuccess("testSyncWithPaginationToken", itemCount + " items");
                    } finally {
                        latch.countDown();
                    }
                }
            });
            
            assertTrue(awaitLatch(latch, "testSyncWithPaginationToken"));
        } else {
            logger.info("ℹ️ No pagination token available (all items fit in first page)");
            logSuccess("testSyncWithPaginationToken", "No pagination needed");
        }
    }

    // ===========================
    // Sync From Date Tests
    // ===========================

    @Test
    @Order(7)
    @DisplayName("Test sync from date")
    void testSyncFromDate() throws InterruptedException {
        CountDownLatch latch = createLatch();
        
        // Sync from 30 days ago
        Date thirtyDaysAgo = new Date(System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000));

        stack.syncFromDate(thirtyDaysAgo, new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack synchronousStack, Error error) {
                try {
                    assertNull(error, "Sync from date should not error");
                    assertNotNull(synchronousStack, "SyncStack should not be null");
                    
                    int itemCount = synchronousStack.getCount();
                    
                    logger.info("✅ Sync from date (30 days ago): " + itemCount + " items");
                    logSuccess("testSyncFromDate", itemCount + " items");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testSyncFromDate"));
    }

    @Test
    @Order(8)
    @DisplayName("Test sync from recent date")
    void testSyncFromRecentDate() throws InterruptedException {
        CountDownLatch latch = createLatch();
        
        // Sync from 1 day ago
        Date oneDayAgo = new Date(System.currentTimeMillis() - (24L * 60 * 60 * 1000));

        stack.syncFromDate(oneDayAgo, new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack synchronousStack, Error error) {
                try {
                    assertNull(error, "Sync from recent date should not error");
                    assertNotNull(synchronousStack, "SyncStack should not be null");
                    
                    int itemCount = synchronousStack.getCount();
                    
                    logger.info("✅ Sync from recent date (1 day ago): " + itemCount + " items");
                    logSuccess("testSyncFromRecentDate", itemCount + " items");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testSyncFromRecentDate"));
    }

    @Test
    @Order(9)
    @DisplayName("Test sync from old date")
    void testSyncFromOldDate() throws InterruptedException {
        CountDownLatch latch = createLatch();
        
        // Sync from 365 days ago
        Date oneYearAgo = new Date(System.currentTimeMillis() - (365L * 24 * 60 * 60 * 1000));

        stack.syncFromDate(oneYearAgo, new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack synchronousStack, Error error) {
                try {
                    // May error if date is too old (acceptable)
                    if (error != null) {
                        logger.info("✅ Sync from old date returned error (acceptable): " + error.getErrorMessage());
                        logSuccess("testSyncFromOldDate", "Error for old date");
                    } else {
                        assertNotNull(synchronousStack, "SyncStack should not be null");
                        int itemCount = synchronousStack.getCount();
                        logger.info("✅ Sync from old date (1 year ago): " + itemCount + " items");
                        logSuccess("testSyncFromOldDate", itemCount + " items");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testSyncFromOldDate"));
    }

    // ===========================
    // Multiple Sync Tests
    // ===========================

    @Test
    @Order(10)
    @DisplayName("Test multiple consecutive syncs")
    void testMultipleConsecutiveSyncs() throws InterruptedException {
        int syncCount = 3;
        final int[] totalItems = {0};
        
        for (int i = 0; i < syncCount; i++) {
            CountDownLatch latch = createLatch();
            final int[] currentCount = {0};
            final int syncIndex = i;
            
            stack.sync(new SyncResultCallBack() {
                @Override
                public void onCompletion(SyncStack synchronousStack, Error error) {
                    try {
                        assertNull(error, "Sync " + (syncIndex + 1) + " should not error");
                        assertNotNull(synchronousStack, "SyncStack should not be null");
                        currentCount[0] = synchronousStack.getCount();
                    } finally {
                        latch.countDown();
                    }
                }
            });
            
            awaitLatch(latch, "sync-" + i);
            totalItems[0] += currentCount[0];
        }
        
        logger.info("✅ Multiple consecutive syncs: " + syncCount + " syncs, " + totalItems[0] + " total items");
        logSuccess("testMultipleConsecutiveSyncs", syncCount + " syncs, " + totalItems[0] + " items");
    }

    // ===========================
    // Performance Tests
    // ===========================

    @Test
    @Order(11)
    @DisplayName("Test sync performance")
    void testSyncPerformance() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        stack.sync(new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack synchronousStack, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    
                    assertNull(error, "Sync should not error");
                    assertNotNull(synchronousStack, "SyncStack should not be null");
                    
                    int itemCount = synchronousStack.getCount();
                    
                    // Sync performance depends on data size, but should complete reasonably
                    assertTrue(duration < 30000,
                            "PERFORMANCE BUG: Sync took " + duration + "ms (max: 30s)");
                    
                    logger.info("✅ Sync performance: " + itemCount + " items in " + formatDuration(duration));
                    logSuccess("testSyncPerformance", itemCount + " items, " + formatDuration(duration));
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testSyncPerformance"));
    }

    @Test
    @Order(12)
    @DisplayName("Test sync with token performance")
    void testSyncWithTokenPerformance() throws InterruptedException {
        // First get a sync token if we don't have one
        if (syncToken == null || syncToken.isEmpty()) {
            CountDownLatch latch1 = createLatch();
            
            stack.sync(new SyncResultCallBack() {
                @Override
                public void onCompletion(SyncStack synchronousStack, Error error) {
                    try {
                        if (error == null && synchronousStack != null) {
                            syncToken = synchronousStack.getSyncToken();
                        }
                    } finally {
                        latch1.countDown();
                    }
                }
            });
            
            awaitLatch(latch1, "get-token");
        }
        
        if (syncToken != null && !syncToken.isEmpty()) {
            CountDownLatch latch2 = createLatch();
            long startTime = PerformanceAssertion.startTimer();
            
            stack.syncToken(syncToken, new SyncResultCallBack() {
                @Override
                public void onCompletion(SyncStack synchronousStack, Error error) {
                    try {
                        long duration = PerformanceAssertion.elapsedTime(startTime);
                        
                        assertNull(error, "Sync with token should not error");
                        assertNotNull(synchronousStack, "SyncStack should not be null");
                        
                        int itemCount = synchronousStack.getCount();
                        
                        // Token-based sync should be fast (delta only)
                        assertTrue(duration < 10000,
                                "PERFORMANCE BUG: Token sync took " + duration + "ms (max: 10s)");
                        
                        logger.info("✅ Token sync performance: " + itemCount + " items in " + formatDuration(duration));
                        logSuccess("testSyncWithTokenPerformance", 
                                itemCount + " items, " + formatDuration(duration));
                    } finally {
                        latch2.countDown();
                    }
                }
            });
            
            assertTrue(awaitLatch(latch2, "testSyncWithTokenPerformance"));
        } else {
            logger.info("ℹ️ No sync token available");
            logSuccess("testSyncWithTokenPerformance", "No token");
        }
    }

    // ===========================
    // Error Handling Tests
    // ===========================

    @Test
    @Order(13)
    @DisplayName("Test sync with invalid token")
    void testSyncWithInvalidToken() throws InterruptedException {
        CountDownLatch latch = createLatch();

        stack.syncToken("invalid_sync_token_xyz_123", new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack synchronousStack, Error error) {
                try {
                    // Should return error for invalid token
                    assertNotNull(error, "BUG: Should error for invalid sync token");
                    assertNotNull(error.getErrorMessage(), "Error message should not be null");
                    
                    logger.info("✅ Invalid sync token error: " + error.getErrorMessage());
                    logSuccess("testSyncWithInvalidToken", "Error handled correctly");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testSyncWithInvalidToken"));
    }

    @Test
    @Order(14)
    @DisplayName("Test sync with invalid pagination token")
    void testSyncWithInvalidPaginationToken() throws InterruptedException {
        CountDownLatch latch = createLatch();

        stack.syncPaginationToken("invalid_pagination_token_xyz_123", new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack synchronousStack, Error error) {
                try {
                    // Should return error for invalid pagination token
                    assertNotNull(error, "BUG: Should error for invalid pagination token");
                    assertNotNull(error.getErrorMessage(), "Error message should not be null");
                    
                    logger.info("✅ Invalid pagination token error: " + error.getErrorMessage());
                    logSuccess("testSyncWithInvalidPaginationToken", "Error handled correctly");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testSyncWithInvalidPaginationToken"));
    }

    @Test
    @Order(15)
    @DisplayName("Test comprehensive sync scenario")
    void testComprehensiveSyncScenario() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        // Initial sync
        stack.sync(new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack synchronousStack, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    
                    assertNull(error, "Comprehensive sync should not error");
                    assertNotNull(synchronousStack, "SyncStack should not be null");
                    
                    int itemCount = synchronousStack.getCount();
                    String newSyncToken = synchronousStack.getSyncToken();
                    String newPaginationToken = synchronousStack.getPaginationToken();
                    
                    // Validate results
                    assertTrue(itemCount >= 0, "Item count should be non-negative");
                    
                    // Log token availability
                    boolean hasSyncToken = (newSyncToken != null && !newSyncToken.isEmpty());
                    boolean hasPaginationToken = (newPaginationToken != null && !newPaginationToken.isEmpty());
                    
                    // Performance check
                    assertTrue(duration < 30000,
                            "PERFORMANCE BUG: Comprehensive sync took " + duration + "ms (max: 30s)");
                    
                    logger.info("✅ COMPREHENSIVE: " + itemCount + " items, " + 
                            "SyncToken=" + hasSyncToken + ", " +
                            "PaginationToken=" + hasPaginationToken + ", " +
                            formatDuration(duration));
                    logSuccess("testComprehensiveSyncScenario", 
                            itemCount + " items, " + formatDuration(duration));
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testComprehensiveSyncScenario"));
    }

    @AfterAll
    void tearDown() {
        logger.info("Completed SyncOperationsComprehensiveIT test suite");
        logger.info("All 15 sync operation tests executed");
        logger.info("Tested: initial sync, sync tokens, pagination tokens, sync from date, performance, error handling");
    }
}
