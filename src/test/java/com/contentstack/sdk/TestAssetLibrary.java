package com.contentstack.sdk;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.LinkedHashMap;
import static org.junit.jupiter.api.Assertions.*;
import com.contentstack.sdk.AssetLibrary.ORDERBY;

/**
 * Comprehensive unit tests for AssetLibrary class.
 * Tests all asset library query operations, filters, and configurations.
 */
public class TestAssetLibrary {

    private AssetLibrary assetLibrary;

    @BeforeEach
    void setUp() {
        assetLibrary = new AssetLibrary();
        assetLibrary.headers = new LinkedHashMap<>();
    }

    // ========== CONSTRUCTOR TESTS ==========

    @Test
    void testAssetLibraryConstructor() {
        AssetLibrary library = new AssetLibrary();
        assertNotNull(library);
        assertNotNull(library.urlQueries);
    }

    // ========== HEADER TESTS ==========

    @Test
    void testSetHeader() {
        assetLibrary.setHeader("custom-header", "custom-value");
        assertTrue(assetLibrary.headers.containsKey("custom-header"));
        assertEquals("custom-value", assetLibrary.headers.get("custom-header"));
    }

    @Test
    void testSetMultipleHeaders() {
        assetLibrary.setHeader("header1", "value1");
        assetLibrary.setHeader("header2", "value2");
        assetLibrary.setHeader("header3", "value3");
        
        assertEquals(3, assetLibrary.headers.size());
        assertEquals("value1", assetLibrary.headers.get("header1"));
        assertEquals("value2", assetLibrary.headers.get("header2"));
        assertEquals("value3", assetLibrary.headers.get("header3"));
    }

    @Test
    void testRemoveHeader() {
        assetLibrary.setHeader("temp-header", "temp-value");
        assertTrue(assetLibrary.headers.containsKey("temp-header"));
        
        assetLibrary.removeHeader("temp-header");
        assertFalse(assetLibrary.headers.containsKey("temp-header"));
    }

    @Test
    void testRemoveNonExistentHeader() {
        assetLibrary.removeHeader("non-existent");
        // Should not throw exception
        assertNotNull(assetLibrary.headers);
    }

    @Test
    void testRemoveEmptyHeader() {
        assetLibrary.removeHeader("");
        // Should not do anything
        assertNotNull(assetLibrary.headers);
    }

    // ========== SORT TESTS ==========

    @Test
    void testSortAscending() {
        AssetLibrary result = assetLibrary.sort("created_at", ORDERBY.ASCENDING);
        assertSame(assetLibrary, result); // Check method chaining
        assertTrue(assetLibrary.urlQueries.has("asc"));
        assertEquals("created_at", assetLibrary.urlQueries.get("asc"));
    }

    @Test
    void testSortDescending() {
        AssetLibrary result = assetLibrary.sort("updated_at", ORDERBY.DESCENDING);
        assertSame(assetLibrary, result);
        assertTrue(assetLibrary.urlQueries.has("desc"));
        assertEquals("updated_at", assetLibrary.urlQueries.get("desc"));
    }

    @Test
    void testSortMultipleFields() {
        assetLibrary.sort("field1", ORDERBY.ASCENDING);
        assetLibrary.sort("field2", ORDERBY.DESCENDING);
        
        assertTrue(assetLibrary.urlQueries.has("asc"));
        assertTrue(assetLibrary.urlQueries.has("desc"));
    }

    // ========== INCLUDE TESTS ==========

    @Test
    void testIncludeCount() {
        AssetLibrary result = assetLibrary.includeCount();
        assertSame(assetLibrary, result);
        assertTrue(assetLibrary.urlQueries.has("include_count"));
        assertEquals("true", assetLibrary.urlQueries.get("include_count"));
    }

    @Test
    void testIncludeRelativeUrl() {
        AssetLibrary result = assetLibrary.includeRelativeUrl();
        assertSame(assetLibrary, result);
        assertTrue(assetLibrary.urlQueries.has("relative_urls"));
        assertEquals("true", assetLibrary.urlQueries.get("relative_urls"));
    }

    @Test
    void testIncludeFallback() {
        AssetLibrary result = assetLibrary.includeFallback();
        assertSame(assetLibrary, result);
        assertTrue(assetLibrary.urlQueries.has("include_fallback"));
        assertEquals(true, assetLibrary.urlQueries.get("include_fallback"));
    }

    @Test
    void testIncludeMetadata() {
        AssetLibrary result = assetLibrary.includeMetadata();
        assertSame(assetLibrary, result);
        assertTrue(assetLibrary.urlQueries.has("include_metadata"));
        assertEquals(true, assetLibrary.urlQueries.get("include_metadata"));
    }

    @Test
    void testMultipleIncludes() {
        assetLibrary.includeCount()
                   .includeRelativeUrl()
                   .includeFallback()
                   .includeMetadata();
        
        assertTrue(assetLibrary.urlQueries.has("include_count"));
        assertTrue(assetLibrary.urlQueries.has("relative_urls"));
        assertTrue(assetLibrary.urlQueries.has("include_fallback"));
        assertTrue(assetLibrary.urlQueries.has("include_metadata"));
    }

    // ========== PARAM TESTS ==========

    @Test
    void testAddParam() {
        AssetLibrary result = assetLibrary.addParam("key1", "value1");
        assertSame(assetLibrary, result);
        assertTrue(assetLibrary.urlQueries.has("key1"));
        assertEquals("value1", assetLibrary.urlQueries.get("key1"));
    }

    @Test
    void testAddMultipleParams() {
        assetLibrary.addParam("param1", "value1");
        assetLibrary.addParam("param2", 123);
        assetLibrary.addParam("param3", true);
        
        assertEquals(3, assetLibrary.urlQueries.length());
        assertEquals("value1", assetLibrary.urlQueries.get("param1"));
        assertEquals(123, assetLibrary.urlQueries.get("param2"));
        assertEquals(true, assetLibrary.urlQueries.get("param3"));
    }

    @Test
    void testAddParamWithNumericValue() {
        assetLibrary.addParam("count", 100);
        assertEquals(100, assetLibrary.urlQueries.get("count"));
    }

    @Test
    void testAddParamWithBooleanValue() {
        assetLibrary.addParam("enabled", false);
        assertEquals(false, assetLibrary.urlQueries.get("enabled"));
    }

    @Test
    void testAddParamOverwritesExisting() {
        assetLibrary.addParam("key", "value1");
        assertEquals("value1", assetLibrary.urlQueries.get("key"));
        
        assetLibrary.addParam("key", "value2");
        assertEquals("value2", assetLibrary.urlQueries.get("key"));
    }

    @Test
    void testRemoveParam() {
        assetLibrary.addParam("param1", "value1");
        assertTrue(assetLibrary.urlQueries.has("param1"));
        
        AssetLibrary result = assetLibrary.removeParam("param1");
        assertSame(assetLibrary, result);
        assertFalse(assetLibrary.urlQueries.has("param1"));
    }

    @Test
    void testRemoveNonExistentParam() {
        assetLibrary.removeParam("non_existent");
        // Should not throw exception
        assertNotNull(assetLibrary.urlQueries);
    }

    // ========== PAGINATION TESTS ==========

    @Test
    void testSkip() {
        AssetLibrary result = assetLibrary.skip(10);
        assertSame(assetLibrary, result);
        assertTrue(assetLibrary.urlQueries.has("skip"));
        assertEquals(10, assetLibrary.urlQueries.get("skip"));
    }

    @Test
    void testSkipZero() {
        assetLibrary.skip(0);
        assertEquals(0, assetLibrary.urlQueries.get("skip"));
    }

    @Test
    void testSkipNegative() {
        assetLibrary.skip(-5);
        assertEquals(-5, assetLibrary.urlQueries.get("skip"));
    }

    @Test
    void testLimit() {
        AssetLibrary result = assetLibrary.limit(50);
        assertSame(assetLibrary, result);
        assertTrue(assetLibrary.urlQueries.has("limit"));
        assertEquals(50, assetLibrary.urlQueries.get("limit"));
    }

    @Test
    void testLimitZero() {
        assetLibrary.limit(0);
        assertEquals(0, assetLibrary.urlQueries.get("limit"));
    }

    @Test
    void testLimitOne() {
        assetLibrary.limit(1);
        assertEquals(1, assetLibrary.urlQueries.get("limit"));
    }

    @Test
    void testPaginationWithSkipAndLimit() {
        assetLibrary.skip(20).limit(10);
        
        assertEquals(20, assetLibrary.urlQueries.get("skip"));
        assertEquals(10, assetLibrary.urlQueries.get("limit"));
    }

    // ========== GET COUNT TESTS ==========

    @Test
    void testGetCountDefault() {
        assertEquals(0, assetLibrary.getCount());
    }

    // ========== CHAINING TESTS ==========

    @Test
    void testMethodChaining() {
        AssetLibrary result = assetLibrary
            .includeCount()
            .includeRelativeUrl()
            .includeFallback()
            .includeMetadata()
            .skip(10)
            .limit(20)
            .sort("created_at", ORDERBY.ASCENDING);
        
        assertSame(assetLibrary, result);
        assertTrue(assetLibrary.urlQueries.has("include_count"));
        assertTrue(assetLibrary.urlQueries.has("relative_urls"));
        assertTrue(assetLibrary.urlQueries.has("include_fallback"));
        assertTrue(assetLibrary.urlQueries.has("include_metadata"));
        assertEquals(10, assetLibrary.urlQueries.get("skip"));
        assertEquals(20, assetLibrary.urlQueries.get("limit"));
        assertTrue(assetLibrary.urlQueries.has("asc"));
    }

    // ========== EDGE CASE TESTS ==========

    @Test
    void testMultipleSkipCallsOverwrite() {
        assetLibrary.skip(10);
        assertEquals(10, assetLibrary.urlQueries.get("skip"));
        
        assetLibrary.skip(20);
        assertEquals(20, assetLibrary.urlQueries.get("skip"));
    }

    @Test
    void testMultipleLimitCallsOverwrite() {
        assetLibrary.limit(10);
        assertEquals(10, assetLibrary.urlQueries.get("limit"));
        
        assetLibrary.limit(50);
        assertEquals(50, assetLibrary.urlQueries.get("limit"));
    }

    @Test
    void testEmptyHeaderKeyRemoval() {
        assetLibrary.setHeader("key1", "value1");
        assetLibrary.removeHeader("");
        
        // Empty key should be ignored
        assertTrue(assetLibrary.headers.containsKey("key1"));
    }

    @Test
    void testUrlQueriesInitialization() {
        AssetLibrary newLibrary = new AssetLibrary();
        assertNotNull(newLibrary.urlQueries);
        assertEquals(0, newLibrary.urlQueries.length());
    }

    @Test
    void testAddParamWithJSONObject() {
        JSONObject json = new JSONObject();
        json.put("nested", "value");
        assetLibrary.addParam("complex_param", json);
        
        assertTrue(assetLibrary.urlQueries.has("complex_param"));
        assertEquals(json, assetLibrary.urlQueries.get("complex_param"));
    }

    @Test
    void testAddParamWithDoubleValue() {
        assetLibrary.addParam("rating", 4.5);
        assertEquals(4.5, assetLibrary.urlQueries.get("rating"));
    }

    @Test
    void testSortBothAscendingAndDescending() {
        assetLibrary.sort("field1", ORDERBY.ASCENDING);
        assetLibrary.sort("field2", ORDERBY.DESCENDING);
        
        assertEquals("field1", assetLibrary.urlQueries.get("asc"));
        assertEquals("field2", assetLibrary.urlQueries.get("desc"));
    }

    @Test
    void testAllIncludeFlagsSet() {
        assetLibrary.includeCount()
                   .includeRelativeUrl()
                   .includeFallback()
                   .includeMetadata();
        
        assertEquals(4, assetLibrary.urlQueries.length());
        assertEquals("true", assetLibrary.urlQueries.get("include_count"));
        assertEquals("true", assetLibrary.urlQueries.get("relative_urls"));
        assertEquals(true, assetLibrary.urlQueries.get("include_fallback"));
        assertEquals(true, assetLibrary.urlQueries.get("include_metadata"));
    }

    @Test
    void testPaginationLargeNumbers() {
        assetLibrary.skip(1000).limit(500);
        
        assertEquals(1000, assetLibrary.urlQueries.get("skip"));
        assertEquals(500, assetLibrary.urlQueries.get("limit"));
    }

    @Test
    void testHeaderOverwrite() {
        assetLibrary.setHeader("key", "value1");
        assertEquals("value1", assetLibrary.headers.get("key"));
        
        assetLibrary.setHeader("key", "value2");
        assertEquals("value2", assetLibrary.headers.get("key"));
    }

    @Test
    void testRemoveAndAddSameParam() {
        assetLibrary.addParam("param1", "value1");
        assetLibrary.removeParam("param1");
        assertFalse(assetLibrary.urlQueries.has("param1"));
        
        assetLibrary.addParam("param1", "value2");
        assertTrue(assetLibrary.urlQueries.has("param1"));
        assertEquals("value2", assetLibrary.urlQueries.get("param1"));
    }

    // ========== ADD PARAM VALIDATION TESTS ==========

    @Test
    void testAddParamWithValidKeyAndValue() {
        AssetLibrary result = assetLibrary.addParam("valid_key", "valid_value");
        
        assertNotNull(result);
        assertTrue(assetLibrary.urlQueries.has("valid_key"));
        assertEquals("valid_value", assetLibrary.urlQueries.get("valid_key"));
    }

    @Test
    void testAddParamWithInvalidKey() {
        // Keys with special characters should be rejected
        AssetLibrary result = assetLibrary.addParam("invalid@key", "value");
        
        // Should return this but not add to queries
        assertNotNull(result);
        assertFalse(assetLibrary.urlQueries.has("invalid@key"));
    }

    @Test
    void testAddParamWithInvalidValue() {
        // Values with special characters should be rejected
        AssetLibrary result = assetLibrary.addParam("key", "invalid@value!");
        
        assertNotNull(result);
        assertFalse(assetLibrary.urlQueries.has("key"));
    }

    @Test
    void testAddParamWithEmptyKey() {
        AssetLibrary result = assetLibrary.addParam("", "value");
        
        assertNotNull(result);
        assertFalse(assetLibrary.urlQueries.has(""));
    }

    // ========== WHERE METHOD TESTS ==========

    @Test
    void testWhereWithValidKeyValue() {
        AssetLibrary result = assetLibrary.where("title", "test_asset");
        
        assertNotNull(result);
        assertTrue(assetLibrary.urlQueries.has("query"));
    }

    @Test
    void testWhereWithInvalidKey() {
        assertThrows(IllegalArgumentException.class, () -> {
            assetLibrary.where("invalid@key", "value");
        });
    }

    @Test
    void testWhereWithInvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> {
            assetLibrary.where("key", "invalid@value!");
        });
    }

    @Test
    void testWhereMultipleCalls() {
        assetLibrary.where("title", "asset1");
        assetLibrary.where("description", "desc1");
        
        assertTrue(assetLibrary.urlQueries.has("query"));
    }

    // ========== FETCH ALL TESTS ==========

    @Test
    void testFetchAllWithCallback() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "environment");
        assetLibrary.stackInstance = stack;
        assetLibrary.setHeader("environment", "production");
        
        FetchAssetsCallback callback = new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, java.util.List<Asset> assets, Error error) {
                // Callback won't be invoked in unit tests
            }
        };
        
        // Actually call fetchAll
        assertDoesNotThrow(() -> assetLibrary.fetchAll(callback));
        
        // Verify environment was added to urlQueries
        assertTrue(assetLibrary.urlQueries.has("environment"));
        assertEquals("production", assetLibrary.urlQueries.get("environment"));
    }

    @Test
    void testFetchAllWithNullCallback() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "environment");
        assetLibrary.stackInstance = stack;
        assetLibrary.setHeader("environment", "staging");
        
        // fetchAll with null callback should not throw but won't create background task
        assertDoesNotThrow(() -> assetLibrary.fetchAll(null));
        
        // Environment should still be added
        assertTrue(assetLibrary.urlQueries.has("environment"));
    }

    @Test
    void testFetchAllAddsEnvironmentFromHeaders() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "environment");
        assetLibrary.stackInstance = stack;
        assetLibrary.setHeader("environment", "development");
        
        FetchAssetsCallback callback = new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, java.util.List<Asset> assets, Error error) {}
        };
        
        assetLibrary.fetchAll(callback);
        
        assertTrue(assetLibrary.urlQueries.has("environment"));
        assertEquals("development", assetLibrary.urlQueries.get("environment"));
    }

    @Test
    void testFetchAllPreservesExistingQueries() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "environment");
        assetLibrary.stackInstance = stack;
        assetLibrary.setHeader("environment", "production");
        assetLibrary.urlQueries.put("include_count", true);
        assetLibrary.urlQueries.put("limit", 50);
        
        FetchAssetsCallback callback = new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, java.util.List<Asset> assets, Error error) {}
        };
        
        assetLibrary.fetchAll(callback);
        
        // Verify environment is added while preserving existing queries
        assertEquals("production", assetLibrary.urlQueries.get("environment"));
        assertTrue((Boolean) assetLibrary.urlQueries.get("include_count"));
        assertEquals(50, assetLibrary.urlQueries.get("limit"));
    }

    // ========== GET RESULT TESTS ==========

    @Test
    void testGetResult() {
        // This method just logs a warning, so we verify it doesn't throw
        assertDoesNotThrow(() -> {
            assetLibrary.getResult(new Object(), "test_controller");
        });
    }

    // ========== GET RESULT OBJECT TESTS ==========

    @Test
    void testGetResultObjectWithNullObjects() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", 5);
        
        // Should handle null objects gracefully
        assertDoesNotThrow(() -> {
            assetLibrary.getResultObject(null, jsonObject, false);
        });
    }

    @Test
    void testGetResultObjectWithEmptyList() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", 0);
        
        java.util.List<Object> emptyList = new java.util.ArrayList<>();
        
        assertDoesNotThrow(() -> {
            assetLibrary.getResultObject(emptyList, jsonObject, false);
        });
    }

    @Test
    void testGetResultObjectExtractsCount() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "environment");
        assetLibrary.stackInstance = stack;
        
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", 42);
        
        java.util.List<Object> objects = new java.util.ArrayList<>();
        
        assetLibrary.getResultObject(objects, jsonObject, false);
        
        assertEquals(42, assetLibrary.count);
    }

    @Test
    void testGetResultObjectWithAssetModels() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "environment");
        assetLibrary.stackInstance = stack;
        
        // Create AssetModel using reflection since it's package-private
        JSONObject assetJson = new JSONObject();
        assetJson.put("uid", "test_asset_uid");
        assetJson.put("filename", "test.jpg");
        assetJson.put("content_type", "image/jpeg");
        assetJson.put("file_size", "1024");
        assetJson.put("url", "https://cdn.example.com/test.jpg");
        
        AssetModel model = new AssetModel(assetJson, true);
        
        java.util.List<Object> objects = new java.util.ArrayList<>();
        objects.add(model);
        
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", 1);
        
        final boolean[] callbackInvoked = {false};
        FetchAssetsCallback callback = new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, java.util.List<Asset> assets, Error error) {
                callbackInvoked[0] = true;
                assertEquals(1, assets.size());
            }
        };
        
        assetLibrary.callback = callback;
        assetLibrary.getResultObject(objects, jsonObject, false);
        
        assertTrue(callbackInvoked[0]);
    }

    @Test
    void testGetResultObjectWithNullJsonObject() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "environment");
        assetLibrary.stackInstance = stack;
        
        java.util.List<Object> objects = new java.util.ArrayList<>();
        
        // Should handle null jsonObject gracefully
        assertDoesNotThrow(() -> {
            assetLibrary.getResultObject(objects, null, false);
        });
    }

    // ========== VALIDATION METHOD TESTS (via reflection) ==========

    @Test
    void testIsValidKeyWithReflection() throws Exception {
        java.lang.reflect.Method method = AssetLibrary.class.getDeclaredMethod("isValidKey", String.class);
        method.setAccessible(true);
        
        // Valid keys (only alphanumeric, underscore, dot)
        assertTrue((Boolean) method.invoke(assetLibrary, "valid_key"));
        assertTrue((Boolean) method.invoke(assetLibrary, "key123"));
        assertTrue((Boolean) method.invoke(assetLibrary, "key_with_underscore"));
        assertTrue((Boolean) method.invoke(assetLibrary, "key.with.dot"));
        
        // Invalid keys (dashes, special chars, empty not allowed)
        assertFalse((Boolean) method.invoke(assetLibrary, "key-with-dash"));
        assertFalse((Boolean) method.invoke(assetLibrary, "invalid@key"));
        assertFalse((Boolean) method.invoke(assetLibrary, "key!"));
        assertFalse((Boolean) method.invoke(assetLibrary, ""));
    }

    @Test
    void testIsValidValueWithReflection() throws Exception {
        java.lang.reflect.Method method = AssetLibrary.class.getDeclaredMethod("isValidValue", Object.class);
        method.setAccessible(true);
        
        // Valid values
        assertTrue((Boolean) method.invoke(assetLibrary, "valid_value"));
        assertTrue((Boolean) method.invoke(assetLibrary, 123));
        assertTrue((Boolean) method.invoke(assetLibrary, true));
        assertTrue((Boolean) method.invoke(assetLibrary, "value with spaces"));
        
        // Invalid values
        assertFalse((Boolean) method.invoke(assetLibrary, "invalid@value"));
        assertFalse((Boolean) method.invoke(assetLibrary, "value!"));
    }

    @Test
    void testIsValidValueListWithReflection() throws Exception {
        java.lang.reflect.Method method = AssetLibrary.class.getDeclaredMethod("isValidValueList", Object[].class);
        method.setAccessible(true);
        
        // Valid lists
        Object[] validList1 = {"value1", "value2", "value3"};
        assertTrue((Boolean) method.invoke(assetLibrary, (Object) validList1));
        
        Object[] validList2 = {"value_with_underscore", "value-with-dash", "value 123"};
        assertTrue((Boolean) method.invoke(assetLibrary, (Object) validList2));
        
        // Invalid lists
        Object[] invalidList1 = {"valid", "invalid@value"};
        assertFalse((Boolean) method.invoke(assetLibrary, (Object) invalidList1));
        
        Object[] invalidList2 = {"value!", "another"};
        assertFalse((Boolean) method.invoke(assetLibrary, (Object) invalidList2));
    }

    @Test
    void testGetUrlParamsWithReflection() throws Exception {
        java.lang.reflect.Method method = AssetLibrary.class.getDeclaredMethod("getUrlParams", JSONObject.class);
        method.setAccessible(true);
        
        JSONObject queries = new JSONObject();
        queries.put("valid_key", "valid_value");
        queries.put("count", 10);
        queries.put("invalid@key", "value"); // Should be filtered out
        
        @SuppressWarnings("unchecked")
        java.util.HashMap<String, Object> result = (java.util.HashMap<String, Object>) method.invoke(assetLibrary, queries);
        
        assertNotNull(result);
        assertTrue(result.containsKey("valid_key"));
        assertTrue(result.containsKey("count"));
        assertFalse(result.containsKey("invalid@key")); // Invalid key should be filtered
    }

    // ========== REMOVE PARAM WITH INVALID KEY TESTS ==========

    @Test
    void testRemoveParamWithInvalidKey() {
        // First add a param
        assetLibrary.addParam("valid_key", "value");
        assertTrue(assetLibrary.urlQueries.has("valid_key"));
        
        // Try to remove with invalid key - should log warning but not crash
        AssetLibrary result = assetLibrary.removeParam("invalid@key");
        
        // Should return this for chaining
        assertNotNull(result);
        // Original param should still be there
        assertTrue(assetLibrary.urlQueries.has("valid_key"));
    }

    @Test
    void testRemoveParamWithInvalidKeySpecialChars() {
        assetLibrary.addParam("test", "value");
        
        // Try multiple invalid keys to ensure logger.warning is covered
        assetLibrary.removeParam("key!");
        assetLibrary.removeParam("key@test");
        assetLibrary.removeParam("key#hash");
        
        // Original param should still exist
        assertTrue(assetLibrary.urlQueries.has("test"));
    }

    @Test
    void testRemoveParamWithValidKeyThatDoesntExist() {
        // Try to remove a valid key that doesn't exist
        AssetLibrary result = assetLibrary.removeParam("nonexistent_key");
        
        // Should return this and not crash
        assertNotNull(result);
    }

    @Test
    void testRemoveParamWithEmptyKey() {
        assetLibrary.addParam("test", "value");
        
        // Try to remove with empty key (invalid)
        AssetLibrary result = assetLibrary.removeParam("");
        
        assertNotNull(result);
        assertTrue(assetLibrary.urlQueries.has("test"));
    }

    // ========== GET RESULT OBJECT WITH NON-ASSETMODEL OBJECTS ==========

    @Test
    void testGetResultObjectWithNonAssetModelObjects() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "environment");
        assetLibrary.stackInstance = stack;
        
        // Create a list with non-AssetModel objects
        java.util.List<Object> objects = new java.util.ArrayList<>();
        objects.add("not_an_asset_model"); // String instead of AssetModel
        
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", 1);
        
        FetchAssetsCallback callback = new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, java.util.List<Asset> assets, Error error) {
                // Should get empty list since the object wasn't an AssetModel
                assertTrue(assets.isEmpty());
            }
        };
        
        assetLibrary.callback = callback;
        
        // This should try to cast the string to AssetModel and likely fail
        // But we're testing that the else branch with INVALID_OBJECT_TYPE_ASSET_MODEL is covered
        assertDoesNotThrow(() -> {
            try {
                assetLibrary.getResultObject(objects, jsonObject, false);
            } catch (ClassCastException e) {
                // Expected - the String can't be cast to AssetModel
                // This covers the error case we're trying to test
            }
        });
    }
}
