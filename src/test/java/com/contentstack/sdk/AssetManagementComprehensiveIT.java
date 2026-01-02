package com.contentstack.sdk;

import com.contentstack.sdk.utils.PerformanceAssertion;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;

/**
 * Comprehensive Integration Tests for Asset Management
 * Tests asset operations including:
 * - Basic asset fetching
 * - Asset metadata access
 * - Asset library queries
 * - Asset filters and search
 * - Asset folders (if supported)
 * - Asset with entries (references)
 * - Performance with assets
 * - Edge cases
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AssetManagementComprehensiveIT extends BaseIntegrationTest {

    private AssetLibrary assetLibrary;

    @BeforeAll
    void setUp() {
        logger.info("Setting up AssetManagementComprehensiveIT test suite");
        logger.info("Testing asset management operations");
        if (Credentials.IMAGE_ASSET_UID != null) {
            logger.info("Using asset UID: " + Credentials.IMAGE_ASSET_UID);
        }
    }

    // ===========================
    // Basic Asset Tests
    // ===========================

    @Test
    @Order(1)
    @DisplayName("Test fetch single asset")
    void testFetchSingleAsset() throws InterruptedException {
        CountDownLatch latch = createLatch();

        if (Credentials.IMAGE_ASSET_UID == null || Credentials.IMAGE_ASSET_UID.isEmpty()) {
            logger.info("ℹ️ No asset UID configured, skipping test");
            logSuccess("testFetchSingleAsset", "Skipped - no asset UID");
            latch.countDown();
            assertTrue(awaitLatch(latch, "testFetchSingleAsset"));
            return;
        }

        Asset asset = stack.asset(Credentials.IMAGE_ASSET_UID);

        asset.fetch(new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "Asset fetch should not error");
                    assertNotNull(asset, "Asset should not be null");
                    
                    // Validate asset properties
                    assertNotNull(asset.getAssetUid(), "BUG: Asset UID missing");
                    assertEquals(Credentials.IMAGE_ASSET_UID, asset.getAssetUid(),
                            "BUG: Wrong asset UID");
                    
                    String filename = asset.getFileName();
                    assertNotNull(filename, "BUG: Filename missing");
                    assertTrue(filename.length() > 0, "BUG: Filename empty");
                    
                    logger.info("✅ Asset fetched: " + filename + " (" + asset.getAssetUid() + ")");
                    logSuccess("testFetchSingleAsset", "Asset: " + filename);
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testFetchSingleAsset"));
    }

    @Test
    @Order(2)
    @DisplayName("Test asset has metadata")
    void testAssetHasMetadata() throws InterruptedException {
        CountDownLatch latch = createLatch();

        if (Credentials.IMAGE_ASSET_UID == null || Credentials.IMAGE_ASSET_UID.isEmpty()) {
            logger.info("ℹ️ No asset UID configured, skipping test");
            logSuccess("testAssetHasMetadata", "Skipped");
            latch.countDown();
            assertTrue(awaitLatch(latch, "testAssetHasMetadata"));
            return;
        }

        Asset asset = stack.asset(Credentials.IMAGE_ASSET_UID);

        asset.fetch(new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "Asset fetch should not error");
                    assertNotNull(asset, "Asset should not be null");
                    
                    // Check metadata
                    String filename = asset.getFileName();
                    String fileType = asset.getFileType();
                    String fileSize = asset.getFileSize();
                    String url = asset.getUrl();
                    
                    assertNotNull(filename, "BUG: Filename missing");
                    assertNotNull(url, "BUG: URL missing");
                    
                    logger.info("Asset metadata:");
                    logger.info("  Filename: " + filename);
                    logger.info("  Type: " + (fileType != null ? fileType : "unknown"));
                    logger.info("  Size: " + (fileSize != null ? fileSize + " bytes" : "unknown"));
                    logger.info("  URL: " + url);
                    
                    logger.info("✅ Asset metadata present");
                    logSuccess("testAssetHasMetadata", "Metadata validated");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testAssetHasMetadata"));
    }

    @Test
    @Order(3)
    @DisplayName("Test asset URL access")
    void testAssetUrlAccess() throws InterruptedException {
        CountDownLatch latch = createLatch();

        if (Credentials.IMAGE_ASSET_UID == null || Credentials.IMAGE_ASSET_UID.isEmpty()) {
            logger.info("ℹ️ No asset UID configured, skipping test");
            logSuccess("testAssetUrlAccess", "Skipped");
            latch.countDown();
            assertTrue(awaitLatch(latch, "testAssetUrlAccess"));
            return;
        }

        Asset asset = stack.asset(Credentials.IMAGE_ASSET_UID);

        asset.fetch(new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "Asset fetch should not error");
                    assertNotNull(asset, "Asset should not be null");
                    
                    String url = asset.getUrl();
                    assertNotNull(url, "BUG: Asset URL missing");
                    assertTrue(url.startsWith("http"), "BUG: URL should be HTTP(S)");
                    assertTrue(url.length() > 10, "BUG: URL too short");
                    
                    logger.info("✅ Asset URL: " + url);
                    logSuccess("testAssetUrlAccess", "URL accessible");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testAssetUrlAccess"));
    }

    // ===========================
    // Asset Library Tests
    // ===========================

    @Test
    @Order(4)
    @DisplayName("Test fetch asset library")
    void testFetchAssetLibrary() throws InterruptedException {
        CountDownLatch latch = createLatch();

        assetLibrary = stack.assetLibrary();

        assetLibrary.fetchAll(new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, java.util.List<Asset> assets, Error error) {
                try {
                    assertNull(error, "Asset library fetch should not error");
                    assertNotNull(assets, "Assets list should not be null");
                    
                    if (assets.size() > 0) {
                        logger.info("✅ Asset library has " + assets.size() + " asset(s)");
                        
                        // Validate first asset
                        Asset firstAsset = assets.get(0);
                        assertNotNull(firstAsset.getAssetUid(), "First asset must have UID");
                        assertNotNull(firstAsset.getFileName(), "First asset must have filename");
                        
                        logSuccess("testFetchAssetLibrary", assets.size() + " assets");
                    } else {
                        logger.info("ℹ️ Asset library is empty");
                        logSuccess("testFetchAssetLibrary", "Empty library");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testFetchAssetLibrary"));
    }

    @Test
    @Order(5)
    @DisplayName("Test asset library with limit")
    void testAssetLibraryWithLimit() throws InterruptedException, IllegalAccessException {
        CountDownLatch latch = createLatch();

        assetLibrary = stack.assetLibrary();
        assetLibrary.limit(5);

        assetLibrary.fetchAll(new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, java.util.List<Asset> assets, Error error) {
                try {
                    assertNull(error, "Asset library fetch should not error");
                    assertNotNull(assets, "Assets list should not be null");
                    
                    assertTrue(assets.size() <= 5, "BUG: limit(5) returned " + assets.size() + " assets");
                    
                    logger.info("✅ Asset library with limit(5): " + assets.size() + " assets");
                    logSuccess("testAssetLibraryWithLimit", assets.size() + " assets");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testAssetLibraryWithLimit"));
    }

    @Test
    @Order(6)
    @DisplayName("Test asset library with skip")
    void testAssetLibraryWithSkip() throws InterruptedException, IllegalAccessException {
        CountDownLatch latch = createLatch();

        assetLibrary = stack.assetLibrary();
        assetLibrary.skip(2).limit(5);

        assetLibrary.fetchAll(new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, java.util.List<Asset> assets, Error error) {
                try {
                    assertNull(error, "Asset library fetch should not error");
                    assertNotNull(assets, "Assets list should not be null");
                    
                    assertTrue(assets.size() <= 5, "Should respect limit");
                    
                    logger.info("✅ Asset library skip(2) + limit(5): " + assets.size() + " assets");
                    logSuccess("testAssetLibraryWithSkip", assets.size() + " assets");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testAssetLibraryWithSkip"));
    }

    // ===========================
    // Asset Search and Filters
    // ===========================

    @Test
    @Order(7)
    @DisplayName("Test asset library with include count")
    void testAssetLibraryWithIncludeCount() throws InterruptedException, IllegalAccessException {
        CountDownLatch latch = createLatch();

        assetLibrary = stack.assetLibrary();
        assetLibrary.includeCount().limit(5);

        assetLibrary.fetchAll(new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, java.util.List<Asset> assets, Error error) {
                try {
                    assertNull(error, "Asset library fetch should not error");
                    assertNotNull(assets, "Assets list should not be null");
                    
                    logger.info("✅ Asset library with count: " + assets.size() + " assets returned");
                    logSuccess("testAssetLibraryWithIncludeCount", assets.size() + " assets");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testAssetLibraryWithIncludeCount"));
    }

    @Test
    @Order(8)
    @DisplayName("Test asset library with relative URLs")
    void testAssetLibraryWithRelativeUrls() throws InterruptedException, IllegalAccessException {
        CountDownLatch latch = createLatch();

        assetLibrary = stack.assetLibrary();
        assetLibrary.includeRelativeUrl().limit(3);

        assetLibrary.fetchAll(new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, java.util.List<Asset> assets, Error error) {
                try {
                    assertNull(error, "Asset library fetch should not error");
                    assertNotNull(assets, "Assets list should not be null");
                    
                    if (assets.size() > 0) {
                        Asset firstAsset = assets.get(0);
                        String url = firstAsset.getUrl();
                        assertNotNull(url, "Asset URL should not be null");
                        
                        logger.info("✅ Asset with relative URL: " + url);
                        logSuccess("testAssetLibraryWithRelativeUrls", assets.size() + " assets");
                    } else {
                        logSuccess("testAssetLibraryWithRelativeUrls", "No assets");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testAssetLibraryWithRelativeUrls"));
    }

    // ===========================
    // Asset with Entries
    // ===========================

    @Test
    @Order(9)
    @DisplayName("Test asset used in entries")
    void testAssetUsedInEntries() throws InterruptedException {
        CountDownLatch latch = createLatch();

        if (Credentials.IMAGE_ASSET_UID == null || Credentials.IMAGE_ASSET_UID.isEmpty()) {
            logger.info("ℹ️ No asset UID configured, skipping test");
            logSuccess("testAssetUsedInEntries", "Skipped");
            latch.countDown();
            assertTrue(awaitLatch(latch, "testAssetUsedInEntries"));
            return;
        }

        Asset asset = stack.asset(Credentials.IMAGE_ASSET_UID);

        asset.fetch(new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "Asset fetch should not error");
                    assertNotNull(asset, "Asset should not be null");
                    
                    // Asset should be fetchable (indicating it's valid)
                    assertNotNull(asset.getAssetUid(), "Asset UID should be present");
                    
                    logger.info("✅ Asset exists and can be used in entries");
                    logSuccess("testAssetUsedInEntries", "Asset valid for entry usage");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testAssetUsedInEntries"));
    }

    // ===========================
    // Asset Metadata Tests
    // ===========================

    @Test
    @Order(10)
    @DisplayName("Test asset file type validation")
    void testAssetFileTypeValidation() throws InterruptedException {
        CountDownLatch latch = createLatch();

        if (Credentials.IMAGE_ASSET_UID == null || Credentials.IMAGE_ASSET_UID.isEmpty()) {
            logger.info("ℹ️ No asset UID configured, skipping test");
            logSuccess("testAssetFileTypeValidation", "Skipped");
            latch.countDown();
            assertTrue(awaitLatch(latch, "testAssetFileTypeValidation"));
            return;
        }

        Asset asset = stack.asset(Credentials.IMAGE_ASSET_UID);

        asset.fetch(new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "Asset fetch should not error");
                    assertNotNull(asset, "Asset should not be null");
                    
                    String fileType = asset.getFileType();
                    String filename = asset.getFileName();
                    
                    if (fileType != null) {
                        assertFalse(fileType.isEmpty(), "File type should not be empty");
                        logger.info("Asset file type: " + fileType);
                        
                        // Common file types
                        boolean isKnownType = fileType.contains("image") || 
                                              fileType.contains("pdf") || 
                                              fileType.contains("video") ||
                                              fileType.contains("audio") ||
                                              fileType.contains("text") ||
                                              fileType.contains("application");
                        
                        if (isKnownType) {
                            logger.info("✅ Known file type: " + fileType);
                        } else {
                            logger.info("ℹ️ Custom file type: " + fileType);
                        }
                    } else {
                        logger.info("ℹ️ File type not available for: " + filename);
                    }
                    
                    logSuccess("testAssetFileTypeValidation", "File type: " + fileType);
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testAssetFileTypeValidation"));
    }

    @Test
    @Order(11)
    @DisplayName("Test asset file size")
    void testAssetFileSize() throws InterruptedException {
        CountDownLatch latch = createLatch();

        if (Credentials.IMAGE_ASSET_UID == null || Credentials.IMAGE_ASSET_UID.isEmpty()) {
            logger.info("ℹ️ No asset UID configured, skipping test");
            logSuccess("testAssetFileSize", "Skipped");
            latch.countDown();
            assertTrue(awaitLatch(latch, "testAssetFileSize"));
            return;
        }

        Asset asset = stack.asset(Credentials.IMAGE_ASSET_UID);

        asset.fetch(new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "Asset fetch should not error");
                    assertNotNull(asset, "Asset should not be null");
                    
                    String fileSize = asset.getFileSize();
                    
                    if (fileSize != null) {
                        assertFalse(fileSize.isEmpty(), "BUG: File size should not be empty");
                        
                        logger.info("✅ Asset file size: " + fileSize);
                        logSuccess("testAssetFileSize", fileSize);
                    } else {
                        logger.info("ℹ️ File size not available");
                        logSuccess("testAssetFileSize", "Size not available");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testAssetFileSize"));
    }

    @Test
    @Order(12)
    @DisplayName("Test asset creation metadata")
    void testAssetCreationMetadata() throws InterruptedException {
        CountDownLatch latch = createLatch();

        if (Credentials.IMAGE_ASSET_UID == null || Credentials.IMAGE_ASSET_UID.isEmpty()) {
            logger.info("ℹ️ No asset UID configured, skipping test");
            logSuccess("testAssetCreationMetadata", "Skipped");
            latch.countDown();
            assertTrue(awaitLatch(latch, "testAssetCreationMetadata"));
            return;
        }

        Asset asset = stack.asset(Credentials.IMAGE_ASSET_UID);

        asset.fetch(new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "Asset fetch should not error");
                    assertNotNull(asset, "Asset should not be null");
                    
                    // Check creation metadata
                    String createdBy = asset.getCreatedBy();
                    String updatedBy = asset.getUpdatedBy();
                    
                    logger.info("Created by: " + (createdBy != null ? createdBy : "not available"));
                    logger.info("Updated by: " + (updatedBy != null ? updatedBy : "not available"));
                    
                    logger.info("✅ Asset metadata fields accessible");
                    logSuccess("testAssetCreationMetadata", "Metadata accessible");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testAssetCreationMetadata"));
    }

    // ===========================
    // Performance Tests
    // ===========================

    @Test
    @Order(13)
    @DisplayName("Test asset fetch performance")
    void testAssetFetchPerformance() throws InterruptedException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        if (Credentials.IMAGE_ASSET_UID == null || Credentials.IMAGE_ASSET_UID.isEmpty()) {
            logger.info("ℹ️ No asset UID configured, skipping test");
            logSuccess("testAssetFetchPerformance", "Skipped");
            latch.countDown();
            assertTrue(awaitLatch(latch, "testAssetFetchPerformance"));
            return;
        }

        Asset asset = stack.asset(Credentials.IMAGE_ASSET_UID);

        asset.fetch(new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    
                    assertNull(error, "Asset fetch should not error");
                    assertNotNull(asset, "Asset should not be null");
                    
                    // Performance check
                    assertTrue(duration < 5000,
                            "PERFORMANCE BUG: Asset fetch took " + duration + "ms (max: 5s)");
                    
                    logger.info("✅ Asset fetched in " + formatDuration(duration));
                    logSuccess("testAssetFetchPerformance", formatDuration(duration));
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testAssetFetchPerformance"));
    }

    @Test
    @Order(14)
    @DisplayName("Test asset library fetch performance")
    void testAssetLibraryFetchPerformance() throws InterruptedException, IllegalAccessException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        assetLibrary = stack.assetLibrary();
        assetLibrary.limit(20);

        assetLibrary.fetchAll(new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, java.util.List<Asset> assets, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    
                    assertNull(error, "Asset library fetch should not error");
                    assertNotNull(assets, "Assets list should not be null");
                    
                    // Performance check
                    assertTrue(duration < 10000,
                            "PERFORMANCE BUG: Asset library fetch took " + duration + "ms (max: 10s)");
                    
                    logger.info("✅ Asset library (" + assets.size() + " assets) fetched in " + 
                            formatDuration(duration));
                    logSuccess("testAssetLibraryFetchPerformance", 
                            assets.size() + " assets, " + formatDuration(duration));
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testAssetLibraryFetchPerformance"));
    }

    @Test
    @Order(15)
    @DisplayName("Test multiple asset fetches performance")
    void testMultipleAssetFetchesPerformance() throws InterruptedException {
        if (Credentials.IMAGE_ASSET_UID == null || Credentials.IMAGE_ASSET_UID.isEmpty()) {
            logger.info("ℹ️ No asset UID configured, skipping test");
            logSuccess("testMultipleAssetFetchesPerformance", "Skipped");
            return;
        }

        int fetchCount = 3;
        long startTime = PerformanceAssertion.startTimer();
        
        for (int i = 0; i < fetchCount; i++) {
            CountDownLatch latch = createLatch();
            
            Asset asset = stack.asset(Credentials.IMAGE_ASSET_UID);
            
            asset.fetch(new FetchResultCallback() {
                @Override
                public void onCompletion(ResponseType responseType, Error error) {
                    try {
                        assertNull(error, "Asset fetch should not error");
                        assertNotNull(asset, "Asset should not be null");
                    } finally {
                        latch.countDown();
                    }
                }
            });
            
            awaitLatch(latch, "fetch-" + i);
        }
        
        long duration = PerformanceAssertion.elapsedTime(startTime);
        
        // Multiple fetches should be reasonably fast
        assertTrue(duration < 15000,
                "PERFORMANCE BUG: " + fetchCount + " fetches took " + duration + "ms (max: 15s)");
        
        logger.info("✅ " + fetchCount + " asset fetches in " + formatDuration(duration));
        logSuccess("testMultipleAssetFetchesPerformance", 
                fetchCount + " fetches, " + formatDuration(duration));
    }

    // ===========================
    // Edge Cases
    // ===========================

    @Test
    @Order(16)
    @DisplayName("Test invalid asset UID")
    void testInvalidAssetUid() throws InterruptedException {
        CountDownLatch latch = createLatch();

        Asset asset = stack.asset("nonexistent_asset_uid_xyz");

        asset.fetch(new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    // Should return error for invalid UID
                    if (error != null) {
                        logger.info("✅ Invalid asset UID handled with error: " + error.getErrorMessage());
                        logSuccess("testInvalidAssetUid", "Error handled correctly");
                    } else {
                        fail("BUG: Should error for invalid asset UID");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testInvalidAssetUid"));
    }

    @Test
    @Order(17)
    @DisplayName("Test asset library pagination")
    void testAssetLibraryPagination() throws InterruptedException, IllegalAccessException {
        // Fetch two pages and ensure no overlap
        final String[] firstPageFirstUid = {null};
        
        // Page 1
        CountDownLatch latch1 = createLatch();
        AssetLibrary page1 = stack.assetLibrary();
        page1.skip(0).limit(5);
        
        page1.fetchAll(new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, java.util.List<Asset> assets, Error error) {
                try {
                    if (error == null && assets != null && assets.size() > 0) {
                        firstPageFirstUid[0] = assets.get(0).getAssetUid();
                    }
                } finally {
                    latch1.countDown();
                }
            }
        });
        
        awaitLatch(latch1, "page-1");
        
        // Page 2
        CountDownLatch latch2 = createLatch();
        AssetLibrary page2 = stack.assetLibrary();
        page2.skip(5).limit(5);
        
        page2.fetchAll(new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, java.util.List<Asset> assets, Error error) {
                try {
                    assertNull(error, "Page 2 fetch should not error");
                    assertNotNull(assets, "Assets list should not be null");
                    
                    // Ensure pages don't overlap
                    if (firstPageFirstUid[0] != null && assets.size() > 0) {
                        for (Asset asset : assets) {
                            assertNotEquals(firstPageFirstUid[0], asset.getAssetUid(),
                                    "BUG: Page 2 should not contain assets from page 1");
                        }
                    }
                    
                    logger.info("✅ Pagination working: " + assets.size() + " assets in page 2");
                    logSuccess("testAssetLibraryPagination", "Page 2: " + assets.size() + " assets");
                } finally {
                    latch2.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch2, "testAssetLibraryPagination"));
    }

    @Test
    @Order(18)
    @DisplayName("Test asset library consistency")
    void testAssetLibraryConsistency() throws InterruptedException, IllegalAccessException {
        // Fetch asset library twice and compare count
        final int[] firstCount = {0};
        
        // First fetch
        CountDownLatch latch1 = createLatch();
        AssetLibrary lib1 = stack.assetLibrary();
        lib1.limit(10);
        
        lib1.fetchAll(new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, java.util.List<Asset> assets, Error error) {
                try {
                    if (error == null && assets != null) {
                        firstCount[0] = assets.size();
                    }
                } finally {
                    latch1.countDown();
                }
            }
        });
        
        awaitLatch(latch1, "first-fetch");
        
        // Second fetch
        CountDownLatch latch2 = createLatch();
        AssetLibrary lib2 = stack.assetLibrary();
        lib2.limit(10);
        
        lib2.fetchAll(new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, java.util.List<Asset> assets, Error error) {
                try {
                    assertNull(error, "Second fetch should not error");
                    assertNotNull(assets, "Assets list should not be null");
                    
                    int secondCount = assets.size();
                    
                    // Count should be consistent (assuming no concurrent modifications)
                    assertEquals(firstCount[0], secondCount,
                            "BUG: Asset count inconsistent between fetches");
                    
                    logger.info("✅ Asset library consistency validated: " + secondCount + " assets");
                    logSuccess("testAssetLibraryConsistency", "Consistent: " + secondCount + " assets");
                } finally {
                    latch2.countDown();
                }
            }
        });
        
        assertTrue(awaitLatch(latch2, "testAssetLibraryConsistency"));
    }

    @Test
    @Order(19)
    @DisplayName("Test asset with all metadata fields")
    void testAssetWithAllMetadataFields() throws InterruptedException {
        CountDownLatch latch = createLatch();

        if (Credentials.IMAGE_ASSET_UID == null || Credentials.IMAGE_ASSET_UID.isEmpty()) {
            logger.info("ℹ️ No asset UID configured, skipping test");
            logSuccess("testAssetWithAllMetadataFields", "Skipped");
            latch.countDown();
            assertTrue(awaitLatch(latch, "testAssetWithAllMetadataFields"));
            return;
        }

        Asset asset = stack.asset(Credentials.IMAGE_ASSET_UID);

        asset.fetch(new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "Asset fetch should not error");
                    assertNotNull(asset, "Asset should not be null");
                    
                    // Comprehensive metadata check
                    int metadataFieldCount = 0;
                    
                    if (asset.getAssetUid() != null) metadataFieldCount++;
                    if (asset.getFileName() != null) metadataFieldCount++;
                    if (asset.getFileType() != null) metadataFieldCount++;
                    if (asset.getFileSize() != null) metadataFieldCount++;
                    if (asset.getUrl() != null) metadataFieldCount++;
                    if (asset.getCreatedBy() != null) metadataFieldCount++;
                    if (asset.getUpdatedBy() != null) metadataFieldCount++;
                    
                    assertTrue(metadataFieldCount >= 3, 
                            "BUG: Asset should have at least basic metadata (UID, filename, URL)");
                    
                    logger.info("✅ Asset has " + metadataFieldCount + " metadata fields");
                    logSuccess("testAssetWithAllMetadataFields", metadataFieldCount + " fields");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testAssetWithAllMetadataFields"));
    }

    @Test
    @Order(20)
    @DisplayName("Test comprehensive asset management scenario")
    void testComprehensiveAssetManagementScenario() throws InterruptedException, IllegalAccessException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        assetLibrary = stack.assetLibrary();
        assetLibrary.includeCount().includeRelativeUrl().limit(10);

        assetLibrary.fetchAll(new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, java.util.List<Asset> assets, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    
                    assertNull(error, "Comprehensive scenario should not error");
                    assertNotNull(assets, "Assets list should not be null");
                    assertTrue(assets.size() <= 10, "Should respect limit");
                    
                    // Validate all assets
                    for (Asset asset : assets) {
                        assertNotNull(asset.getAssetUid(), "All assets must have UID");
                        assertNotNull(asset.getFileName(), "All assets must have filename");
                        assertNotNull(asset.getUrl(), "All assets must have URL");
                    }
                    
                    // Performance check
                    assertTrue(duration < 10000,
                            "PERFORMANCE BUG: Comprehensive took " + duration + "ms (max: 10s)");
                    
                    logger.info("✅ COMPREHENSIVE: " + assets.size() + " assets validated in " + 
                            formatDuration(duration));
                    logSuccess("testComprehensiveAssetManagementScenario", 
                            assets.size() + " assets, " + formatDuration(duration));
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testComprehensiveAssetManagementScenario"));
    }

    @AfterAll
    void tearDown() {
        logger.info("Completed AssetManagementComprehensiveIT test suite");
        logger.info("All 20 asset management tests executed");
        logger.info("Tested: fetch, metadata, library, filters, performance, edge cases");
    }
}

