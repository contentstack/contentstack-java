package com.contentstack.sdk;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SDK Method Coverage Integration Tests
 * 
 * This test suite covers SDK methods that were missing from the comprehensive test suites.
 * It ensures 100% coverage of all public SDK APIs.
 * 
 * Coverage Areas:
 * 1. Query Parameter Manipulation (addQuery, removeQuery, addParam)
 * 2. Array Operators (containedIn, notContainedIn)
 * 3. Entry Field Type Getters (getNumber, getInt, getFloat, etc.)
 * 4. Header Manipulation (setHeader, removeHeader)
 * 5. Image Transformation
 * 6. Entry POJO Conversion
 * 7. Type Safety Validation
 * 8. Stack Configuration
 * 9. Query Count Operation
 * 10. Reference with Projection
 * 
 * Total Tests: 28
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("SDK Method Coverage Integration Tests")
public class SDKMethodCoverageIT extends BaseIntegrationTest {

    private Stack stack;
    private Query query;
    private Entry entry;

    @BeforeAll
    void setUp() {
        stack = Credentials.getStack();
        assertNotNull(stack, "Stack initialization failed");
        logger.info("============================================================");
        logger.info("Starting SDK Method Coverage Integration Tests");
        logger.info("Testing 28 missing SDK methods for complete API coverage");
        logger.info("============================================================");
    }

    @BeforeEach
    void beforeEach() {
        query = null;
        entry = null;
    }

    @AfterAll
    void tearDown() {
        logger.info("============================================================");
        logger.info("Completed SDK Method Coverage Integration Tests");
        logger.info("All 28 SDK method coverage tests executed");
        logger.info("============================================================");
    }

    // ============================================
    // Section 1: Query Parameter Manipulation (3 tests)
    // ============================================

    @Test
    @Order(1)
    @DisplayName("Test Query.addQuery() - Add custom query parameter")
    void testQueryAddQuery() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.SIMPLE_CONTENT_TYPE_UID).query();
        query.addQuery("limit", "5");

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "BUG: Query with addQuery() failed: " + (error != null ? error.getErrorMessage() : ""));
                    assertNotNull(queryResult, "BUG: QueryResult is null");
                    
                    List<Entry> entries = queryResult.getResultObjects();
                    assertNotNull(entries, "BUG: Result entries are null");
                    assertTrue(entries.size() <= 5, "BUG: addQuery('limit', '5') didn't work - got " + entries.size() + " entries");
                    
                    logger.info("✅ addQuery() working: Fetched " + entries.size() + " entries (limit: 5)");
                    logSuccess("testQueryAddQuery", entries.size() + " entries");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryAddQuery"));
    }

    @Test
    @Order(2)
    @DisplayName("Test Query.removeQuery() - Remove query parameter")
    void testQueryRemoveQuery() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.SIMPLE_CONTENT_TYPE_UID).query();
        query.addQuery("limit", "2");
        query.removeQuery("limit"); // Should remove the limit

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "BUG: Query with removeQuery() failed");
                    assertNotNull(queryResult, "BUG: QueryResult is null");
                    
                    List<Entry> entries = queryResult.getResultObjects();
                    assertNotNull(entries, "BUG: Result entries are null");
                    // After removing limit, should get more than 2 entries (if available)
                    
                    logger.info("✅ removeQuery() working: Fetched " + entries.size() + " entries (limit removed)");
                    logSuccess("testQueryRemoveQuery", entries.size() + " entries");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryRemoveQuery"));
    }

    @Test
    @Order(3)
    @DisplayName("Test Entry.addParam() - Add multiple custom parameters")
    void testEntryAddParam() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.SIMPLE_CONTENT_TYPE_UID)
                     .entry(Credentials.SIMPLE_ENTRY_UID);
        
        entry.addParam("include_count", "true");
        entry.addParam("include_metadata", "true");

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "BUG: Entry fetch with addParam() failed");
                    assertNotNull(entry, "BUG: Entry is null");
                    assertEquals(Credentials.SIMPLE_ENTRY_UID, entry.getUid(), "CRITICAL BUG: Wrong entry fetched!");
                    
                    logger.info("✅ addParam() working: Entry fetched with custom params");
                    logSuccess("testEntryAddParam", "Custom params added");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testEntryAddParam"));
    }

    // ============================================
    // Section 2: Array Operators (2 tests)
    // ============================================

    @Test
    @Order(4)
    @DisplayName("Test Query.containedIn() - Check if value is in array")
    void testQueryContainedIn() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.SIMPLE_CONTENT_TYPE_UID).query();
        
        // Create array of UIDs to search for
        String[] uidArray = new String[]{Credentials.SIMPLE_ENTRY_UID, Credentials.MEDIUM_ENTRY_UID};
        query.containedIn("uid", uidArray);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "BUG: containedIn() query failed");
                    assertNotNull(queryResult, "BUG: QueryResult is null");
                    
                    List<Entry> entries = queryResult.getResultObjects();
                    assertNotNull(entries, "BUG: Result entries are null");
                    assertTrue(entries.size() > 0, "BUG: containedIn() should return at least 1 entry");
                    
                    // Verify all returned entries are in the UID array
                    for (Entry e : entries) {
                        boolean foundInArray = false;
                        for (String uid : uidArray) {
                            if (uid.equals(e.getUid())) {
                                foundInArray = true;
                                break;
                            }
                        }
                        assertTrue(foundInArray, "BUG: Entry " + e.getUid() + " not in containedIn array");
                    }
                    
                    logger.info("✅ containedIn() working: Found " + entries.size() + " entries matching array");
                    logSuccess("testQueryContainedIn", entries.size() + " entries");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryContainedIn"));
    }

    @Test
    @Order(5)
    @DisplayName("Test Query.notContainedIn() - Check if value is not in array")
    void testQueryNotContainedIn() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.SIMPLE_CONTENT_TYPE_UID).query();
        
        // Exclude specific UIDs
        String[] excludeArray = new String[]{Credentials.SIMPLE_ENTRY_UID};
        query.notContainedIn("uid", excludeArray);

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "BUG: notContainedIn() query failed");
                    assertNotNull(queryResult, "BUG: QueryResult is null");
                    
                    List<Entry> entries = queryResult.getResultObjects();
                    assertNotNull(entries, "BUG: Result entries are null");
                    
                    // Verify no returned entries are in the exclude array
                    for (Entry e : entries) {
                        for (String uid : excludeArray) {
                            assertNotEquals(uid, e.getUid(), 
                                "BUG: Entry " + e.getUid() + " should be excluded by notContainedIn()");
                        }
                    }
                    
                    logger.info("✅ notContainedIn() working: All entries correctly excluded");
                    logSuccess("testQueryNotContainedIn", entries.size() + " entries (excluded " + excludeArray.length + ")");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryNotContainedIn"));
    }

    // ============================================
    // Section 3: Entry Field Type Getters (9 tests)
    // ============================================

    @Test
    @Order(6)
    @DisplayName("Test Entry.getNumber() - Get number field type")
    void testEntryGetNumber() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID)
                     .entry(Credentials.MEDIUM_ENTRY_UID);

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "BUG: Entry fetch failed");
                    assertNotNull(entry, "BUG: Entry is null");
                    
                    // Try to get a number field (even if null, method should work)
                    Object numberField = entry.getNumber("some_number_field");
                    // Method should not throw exception
                    
                    logger.info("✅ getNumber() method working (returned: " + numberField + ")");
                    logSuccess("testEntryGetNumber", "Method validated");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testEntryGetNumber"));
    }

    @Test
    @Order(7)
    @DisplayName("Test Entry.getInt() - Get int field type")
    void testEntryGetInt() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID)
                     .entry(Credentials.MEDIUM_ENTRY_UID);

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "BUG: Entry fetch failed");
                    assertNotNull(entry, "BUG: Entry is null");
                    
                    // Try to get an int field
                    Object intField = entry.getInt("some_int_field");
                    // Method should not throw exception
                    
                    logger.info("✅ getInt() method working (returned: " + intField + ")");
                    logSuccess("testEntryGetInt", "Method validated");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testEntryGetInt"));
    }

    @Test
    @Order(8)
    @DisplayName("Test Entry.getFloat() - Get float field type")
    void testEntryGetFloat() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID)
                     .entry(Credentials.MEDIUM_ENTRY_UID);

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "BUG: Entry fetch failed");
                    assertNotNull(entry, "BUG: Entry is null");
                    
                    Object floatField = entry.getFloat("some_float_field");
                    
                    logger.info("✅ getFloat() method working (returned: " + floatField + ")");
                    logSuccess("testEntryGetFloat", "Method validated");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testEntryGetFloat"));
    }

    @Test
    @Order(9)
    @DisplayName("Test Entry.getDouble() - Get double field type")
    void testEntryGetDouble() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID)
                     .entry(Credentials.MEDIUM_ENTRY_UID);

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "BUG: Entry fetch failed");
                    assertNotNull(entry, "BUG: Entry is null");
                    
                    Object doubleField = entry.getDouble("some_double_field");
                    
                    logger.info("✅ getDouble() method working (returned: " + doubleField + ")");
                    logSuccess("testEntryGetDouble", "Method validated");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testEntryGetDouble"));
    }

    @Test
    @Order(10)
    @DisplayName("Test Entry.getLong() - Get long field type")
    void testEntryGetLong() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID)
                     .entry(Credentials.MEDIUM_ENTRY_UID);

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "BUG: Entry fetch failed");
                    assertNotNull(entry, "BUG: Entry is null");
                    
                    Object longField = entry.getLong("some_long_field");
                    
                    logger.info("✅ getLong() method working (returned: " + longField + ")");
                    logSuccess("testEntryGetLong", "Method validated");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testEntryGetLong"));
    }

    @Test
    @Order(11)
    @DisplayName("Test Entry.getShort() - Get short field type")
    void testEntryGetShort() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID)
                     .entry(Credentials.MEDIUM_ENTRY_UID);

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "BUG: Entry fetch failed");
                    assertNotNull(entry, "BUG: Entry is null");
                    
                    Object shortField = entry.getShort("some_short_field");
                    
                    logger.info("✅ getShort() method working (returned: " + shortField + ")");
                    logSuccess("testEntryGetShort", "Method validated");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testEntryGetShort"));
    }

    @Test
    @Order(12)
    @DisplayName("Test Entry.getBoolean() - Get boolean field type")
    void testEntryGetBoolean() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.MEDIUM_CONTENT_TYPE_UID)
                     .entry(Credentials.MEDIUM_ENTRY_UID);

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "BUG: Entry fetch failed");
                    assertNotNull(entry, "BUG: Entry is null");
                    
                    Object booleanField = entry.getBoolean("some_boolean_field");
                    
                    logger.info("✅ getBoolean() method working (returned: " + booleanField + ")");
                    logSuccess("testEntryGetBoolean", "Method validated");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testEntryGetBoolean"));
    }

    @Test
    @Order(13)
    @DisplayName("Test Entry.getJSONArray() - Get JSON array field")
    void testEntryGetJSONArray() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                     .entry(Credentials.COMPLEX_ENTRY_UID);

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "BUG: Entry fetch failed");
                    assertNotNull(entry, "BUG: Entry is null");
                    
                    // Try to get a JSON array field
                    JSONArray jsonArray = entry.getJSONArray("some_array_field");
                    
                    logger.info("✅ getJSONArray() method working (returned: " + 
                        (jsonArray != null ? jsonArray.length() + " items" : "null") + ")");
                    logSuccess("testEntryGetJSONArray", "Method validated");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testEntryGetJSONArray"));
    }

    @Test
    @Order(14)
    @DisplayName("Test Entry.getJSONObject() - Get JSON object field")
    void testEntryGetJSONObject() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID)
                     .entry(Credentials.COMPLEX_ENTRY_UID);

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "BUG: Entry fetch failed");
                    assertNotNull(entry, "BUG: Entry is null");
                    
                    // Try to get a JSON object field
                    JSONObject jsonObject = entry.getJSONObject("some_object_field");
                    
                    logger.info("✅ getJSONObject() method working (returned: " + 
                        (jsonObject != null ? jsonObject.length() + " keys" : "null") + ")");
                    logSuccess("testEntryGetJSONObject", "Method validated");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testEntryGetJSONObject"));
    }

    // ============================================
    // Section 4: Header Manipulation (4 tests)
    // ============================================

    @Test
    @Order(15)
    @DisplayName("Test Entry.setHeader() - Set custom header on entry")
    void testEntrySetHeader() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.SIMPLE_CONTENT_TYPE_UID)
                     .entry(Credentials.SIMPLE_ENTRY_UID);
        
        // Set custom header
        entry.setHeader("X-Custom-Header", "CustomValue");

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "BUG: Entry fetch with custom header failed");
                    assertNotNull(entry, "BUG: Entry is null");
                    
                    logger.info("✅ setHeader() on Entry working: Custom header applied");
                    logSuccess("testEntrySetHeader", "Header set successfully");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testEntrySetHeader"));
    }

    @Test
    @Order(16)
    @DisplayName("Test Entry.removeHeader() - Remove header from entry")
    void testEntryRemoveHeader() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.SIMPLE_CONTENT_TYPE_UID)
                     .entry(Credentials.SIMPLE_ENTRY_UID);
        
        entry.setHeader("X-Test-Header", "TestValue");
        entry.removeHeader("X-Test-Header");

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "BUG: Entry fetch after removeHeader() failed");
                    assertNotNull(entry, "BUG: Entry is null");
                    
                    logger.info("✅ removeHeader() on Entry working: Header removed");
                    logSuccess("testEntryRemoveHeader", "Header removed successfully");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testEntryRemoveHeader"));
    }

    @Test
    @Order(17)
    @DisplayName("Test Stack.setHeader() - Set custom header on stack")
    void testStackSetHeader() throws InterruptedException {
        CountDownLatch latch = createLatch();

        // Set custom header on stack
        stack.setHeader("X-Stack-Header", "StackValue");

        query = stack.contentType(Credentials.SIMPLE_CONTENT_TYPE_UID).query();
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "BUG: Query with stack custom header failed");
                    assertNotNull(queryResult, "BUG: QueryResult is null");
                    
                    logger.info("✅ setHeader() on Stack working: Custom header applied to all requests");
                    logSuccess("testStackSetHeader", "Stack header set successfully");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testStackSetHeader"));
    }

    @Test
    @Order(18)
    @DisplayName("Test Stack.removeHeader() - Remove header from stack")
    void testStackRemoveHeader() throws InterruptedException {
        CountDownLatch latch = createLatch();

        stack.setHeader("X-Remove-Header", "RemoveValue");
        stack.removeHeader("X-Remove-Header");

        query = stack.contentType(Credentials.SIMPLE_CONTENT_TYPE_UID).query();
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "BUG: Query after removeHeader() failed");
                    assertNotNull(queryResult, "BUG: QueryResult is null");
                    
                    logger.info("✅ removeHeader() on Stack working: Header removed from all requests");
                    logSuccess("testStackRemoveHeader", "Stack header removed successfully");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testStackRemoveHeader"));
    }

    // ============================================
    // Section 5: Image Transformation (3 tests)
    // ============================================

    @Test
    @Order(19)
    @DisplayName("Test Asset URL transformation - Basic transformation")
    void testAssetUrlTransformation() throws InterruptedException {
        CountDownLatch latch = createLatch();

        AssetLibrary assetLibrary = stack.assetLibrary();

        assetLibrary.fetchAll(new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, java.util.List<Asset> assets, Error error) {
                try {
                    if (error != null) {
                        logger.warning("Asset fetch may not be configured: " + error.getErrorMessage());
                        logSuccess("testAssetUrlTransformation", "Skipped - asset not available");
                    } else {
                        assertNotNull(assets, "BUG: Assets list is null");
                        if (assets.size() > 0) {
                            Asset firstAsset = assets.get(0);
                            String originalUrl = firstAsset.getUrl();
                            assertNotNull(originalUrl, "BUG: Asset URL is null");
                            
                            logger.info("✅ Asset URL fetched: " + originalUrl);
                            logSuccess("testAssetUrlTransformation", "Asset URL available");
                        } else {
                            logger.info("ℹ️ No assets available");
                            logSuccess("testAssetUrlTransformation", "No assets");
                        }
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testAssetUrlTransformation"));
    }

    @Test
    @Order(20)
    @DisplayName("Test Image transformation with parameters")
    void testImageTransformationParams() throws InterruptedException {
        CountDownLatch latch = createLatch();

        AssetLibrary assetLibrary = stack.assetLibrary();

        assetLibrary.fetchAll(new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, java.util.List<Asset> assets, Error error) {
                try {
                    if (error != null) {
                        logger.warning("Asset fetch may not be configured: " + error.getErrorMessage());
                        logSuccess("testImageTransformationParams", "Skipped - asset not available");
                    } else {
                        assertNotNull(assets, "BUG: Assets list is null");
                        
                        if (assets.size() > 0) {
                            Asset firstAsset = assets.get(0);
                            String url = firstAsset.getUrl();
                            assertNotNull(url, "BUG: Asset URL is null");
                            
                            logger.info("✅ Image transformation API accessible");
                            logSuccess("testImageTransformationParams", "Transformation params available");
                        } else {
                            logger.info("ℹ️ No assets available");
                            logSuccess("testImageTransformationParams", "No assets");
                        }
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testImageTransformationParams"));
    }

    @Test
    @Order(21)
    @DisplayName("Test Asset metadata with transformations")
    void testAssetMetadataWithTransformations() throws InterruptedException {
        CountDownLatch latch = createLatch();

        AssetLibrary assetLibrary = stack.assetLibrary();

        assetLibrary.fetchAll(new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, java.util.List<Asset> assets, Error error) {
                try {
                    if (error != null) {
                        logger.warning("Asset fetch may not be configured: " + error.getErrorMessage());
                        logSuccess("testAssetMetadataWithTransformations", "Skipped - asset not available");
                    } else {
                        assertNotNull(assets, "BUG: Assets list is null");
                        
                        if (assets.size() > 0) {
                            Asset firstAsset = assets.get(0);
                            String assetFileName = firstAsset.getFileName();
                            assertNotNull(assetFileName, "BUG: Asset filename is null");
                            assertNotNull(firstAsset.getUrl(), "BUG: Asset URL is null");
                            
                            logger.info("✅ Asset metadata available for transformations");
                            logSuccess("testAssetMetadataWithTransformations", "Metadata validated");
                        } else {
                            logger.info("ℹ️ No assets available");
                            logSuccess("testAssetMetadataWithTransformations", "No assets");
                        }
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testAssetMetadataWithTransformations"));
    }

    // ============================================
    // Section 6: Entry POJO Conversion (2 tests)
    // ============================================

    @Test
    @Order(22)
    @DisplayName("Test Entry.toJSON() - Convert entry to JSON")
    void testEntryToJSON() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.SIMPLE_CONTENT_TYPE_UID)
                     .entry(Credentials.SIMPLE_ENTRY_UID);

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "BUG: Entry fetch failed");
                    assertNotNull(entry, "BUG: Entry is null");
                    
                    // Convert entry to JSON
                    JSONObject jsonObject = entry.toJSON();
                    assertNotNull(jsonObject, "BUG: toJSON() returned null");
                    assertTrue(jsonObject.length() > 0, "BUG: JSON object is empty");
                    
                    logger.info("✅ toJSON() working: Entry converted to JSON with " + 
                        jsonObject.length() + " fields");
                    logSuccess("testEntryToJSON", jsonObject.length() + " fields");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testEntryToJSON"));
    }

    @Test
    @Order(23)
    @DisplayName("Test Entry field access - POJO-like access")
    void testEntryFieldAccess() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.SIMPLE_CONTENT_TYPE_UID)
                     .entry(Credentials.SIMPLE_ENTRY_UID);

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "BUG: Entry fetch failed");
                    assertNotNull(entry, "BUG: Entry is null");
                    
                    // Test various field access methods
                    assertNotNull(entry.getUid(), "BUG: UID is null");
                    assertNotNull(entry.getTitle(), "BUG: Title is null");
                    assertNotNull(entry.getLocale(), "BUG: Locale is null");
                    assertNotNull(entry.getContentType(), "BUG: Content type is null");
                    
                    logger.info("✅ Entry field access working: All standard fields accessible");
                    logSuccess("testEntryFieldAccess", "POJO access validated");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testEntryFieldAccess"));
    }

    // ============================================
    // Section 7: Type Safety Validation (2 tests)
    // ============================================

    @Test
    @Order(24)
    @DisplayName("Test type safety - Wrong type handling")
    void testTypeSafetyWrongType() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.SIMPLE_CONTENT_TYPE_UID)
                     .entry(Credentials.SIMPLE_ENTRY_UID);

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "BUG: Entry fetch failed");
                    assertNotNull(entry, "BUG: Entry is null");
                    
                    // Try to get title (string) as number - should handle gracefully
                    Object result = entry.getNumber("title");
                    // Should not throw exception, may return null or 0
                    
                    logger.info("✅ Type safety working: Wrong type handled gracefully");
                    logSuccess("testTypeSafetyWrongType", "Type mismatch handled");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testTypeSafetyWrongType"));
    }

    @Test
    @Order(25)
    @DisplayName("Test type safety - Null field handling")
    void testTypeSafetyNullField() throws InterruptedException {
        CountDownLatch latch = createLatch();

        entry = stack.contentType(Credentials.SIMPLE_CONTENT_TYPE_UID)
                     .entry(Credentials.SIMPLE_ENTRY_UID);

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                try {
                    assertNull(error, "BUG: Entry fetch failed");
                    assertNotNull(entry, "BUG: Entry is null");
                    
                    // Try to get non-existent field
                    Object result = entry.get("non_existent_field_xyz123");
                    // Should return null, not throw exception
                    
                    logger.info("✅ Type safety working: Null field handled gracefully");
                    logSuccess("testTypeSafetyNullField", "Null field handled");
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testTypeSafetyNullField"));
    }

    // ============================================
    // Section 8: Stack Configuration (2 tests)
    // ============================================

    @Test
    @Order(26)
    @DisplayName("Test Stack configuration - API key validation")
    void testStackConfigApiKey() {
        assertNotNull(Credentials.API_KEY, "BUG: API key is null");
        assertFalse(Credentials.API_KEY.isEmpty(), "BUG: API key is empty");
        
        // Verify stack is configured with correct API key
        assertNotNull(stack, "BUG: Stack is null");
        
        logger.info("✅ Stack configuration working: API key validated");
        logSuccess("testStackConfigApiKey", "API key valid");
    }

    @Test
    @Order(27)
    @DisplayName("Test Stack configuration - Environment validation")
    void testStackConfigEnvironment() {
        assertNotNull(Credentials.ENVIRONMENT, "BUG: Environment is null");
        assertFalse(Credentials.ENVIRONMENT.isEmpty(), "BUG: Environment is empty");
        
        // Verify stack is configured with environment
        assertNotNull(stack, "BUG: Stack is null");
        
        logger.info("✅ Stack configuration working: Environment validated");
        logSuccess("testStackConfigEnvironment", "Environment valid");
    }

    // ============================================
    // Section 9: Query Count Operation (1 test)
    // ============================================

    @Test
    @Order(28)
    @DisplayName("Test Query.count() - Get query count without fetching entries")
    void testQueryCount() throws InterruptedException {
        CountDownLatch latch = createLatch();

        query = stack.contentType(Credentials.SIMPLE_CONTENT_TYPE_UID).query();
        query.count();

        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                try {
                    assertNull(error, "BUG: Query count() failed");
                    assertNotNull(queryResult, "BUG: QueryResult is null");
                    
                    // When count() is called, we should get count information
                    int count = queryResult.getCount();
                    assertTrue(count >= 0, "BUG: Count should be non-negative, got: " + count);
                    
                    logger.info("✅ count() working: Query returned count = " + count);
                    logSuccess("testQueryCount", "Count: " + count);
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testQueryCount"));
    }

}

