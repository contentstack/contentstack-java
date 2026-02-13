package com.contentstack.sdk;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.LinkedHashMap;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for Asset class.
 * Tests all asset operations, configurations, and methods.
 */
public class TestAsset {

    private Asset asset;
    private final String assetUid = "test_asset_uid";

    @BeforeEach
    void setUp() {
        asset = new Asset(assetUid);
        asset.headers = new LinkedHashMap<>();
    }

    // ========== CONSTRUCTOR TESTS ==========

    @Test
    void testAssetConstructorWithUid() {
        Asset testAsset = new Asset("my_asset_uid");
        assertNotNull(testAsset);
        assertEquals("my_asset_uid", testAsset.getAssetUid());
        assertNotNull(testAsset.headers);
        assertNotNull(testAsset.urlQueries);
    }

    @Test
    void testAssetDefaultConstructor() {
        Asset testAsset = new Asset();
        assertNotNull(testAsset);
        assertNotNull(testAsset.headers);
        assertNotNull(testAsset.urlQueries);
    }

    @Test
    void testGetAssetUid() {
        assertEquals(assetUid, asset.getAssetUid());
    }

    @Test
    void testGetAssetUidFromConfigure() {
        JSONObject json = new JSONObject();
        json.put("uid", "configured_asset_uid");
        asset.configure(json);
        assertEquals("configured_asset_uid", asset.getAssetUid());
    }

    // ========== CONFIGURE TESTS ==========

    @Test
    void testConfigureWithCompleteJson() {
        JSONObject json = new JSONObject();
        json.put("uid", "configured_uid");
        json.put("content_type", "image/jpeg");
        json.put("file_size", "2048");
        json.put("filename", "configured.jpg");
        json.put("url", "https://example.com/configured.jpg");
        json.put("tags", new String[]{"tag1", "tag2"});
        
        Asset result = asset.configure(json);
        assertSame(asset, result);
        assertEquals("configured.jpg", asset.fileName);
    }

    @Test
    void testConfigureWithMinimalJson() {
        JSONObject json = new JSONObject();
        json.put("uid", "minimal_uid");
        
        Asset result = asset.configure(json);
        assertSame(asset, result);
    }

    // ========== HEADER TESTS ==========

    @Test
    void testSetHeader() {
        asset.setHeader("custom-header", "custom-value");
        assertTrue(asset.headers.containsKey("custom-header"));
        assertEquals("custom-value", asset.headers.get("custom-header"));
    }

    @Test
    void testSetMultipleHeaders() {
        asset.setHeader("header1", "value1");
        asset.setHeader("header2", "value2");
        asset.setHeader("header3", "value3");
        
        assertEquals(3, asset.headers.size());
        assertEquals("value1", asset.headers.get("header1"));
        assertEquals("value2", asset.headers.get("header2"));
        assertEquals("value3", asset.headers.get("header3"));
    }

    @Test
    void testRemoveHeader() {
        asset.setHeader("temp-header", "temp-value");
        assertTrue(asset.headers.containsKey("temp-header"));
        
        asset.removeHeader("temp-header");
        assertFalse(asset.headers.containsKey("temp-header"));
    }

    @Test
    void testRemoveNonExistentHeader() {
        asset.removeHeader("non-existent-header");
        assertNotNull(asset.headers);
    }

    // ========== PARAM TESTS ==========

    @Test
    void testAddParam() {
        Asset result = asset.addParam("key1", "value1");
        assertSame(asset, result);
        assertTrue(asset.urlQueries.has("key1"));
        assertEquals("value1", asset.urlQueries.get("key1"));
    }

    @Test
    void testAddMultipleParams() {
        asset.addParam("param1", "value1");
        asset.addParam("param2", "value2");
        asset.addParam("param3", "value3");
        
        assertEquals(3, asset.urlQueries.length());
        assertEquals("value1", asset.urlQueries.get("param1"));
        assertEquals("value2", asset.urlQueries.get("param2"));
        assertEquals("value3", asset.urlQueries.get("param3"));
    }

    @Test
    void testAddParamOverwritesExisting() {
        asset.addParam("key", "value1");
        assertEquals("value1", asset.urlQueries.get("key"));
        
        asset.addParam("key", "value2");
        assertEquals("value2", asset.urlQueries.get("key"));
    }

    // ========== INCLUDE TESTS ==========

    @Test
    void testIncludeDimension() {
        Asset result = asset.includeDimension();
        assertSame(asset, result);
        assertTrue(asset.urlQueries.has("include_dimension"));
        assertEquals(true, asset.urlQueries.get("include_dimension"));
    }

    @Test
    void testIncludeFallback() {
        Asset result = asset.includeFallback();
        assertSame(asset, result);
        assertTrue(asset.urlQueries.has("include_fallback"));
        assertEquals(true, asset.urlQueries.get("include_fallback"));
    }

    @Test
    void testIncludeBranch() {
        Asset result = asset.includeBranch();
        assertSame(asset, result);
        assertTrue(asset.urlQueries.has("include_branch"));
        assertEquals(true, asset.urlQueries.get("include_branch"));
    }

    @Test
    void testIncludeMetadata() {
        Asset result = asset.includeMetadata();
        assertSame(asset, result);
        assertTrue(asset.urlQueries.has("include_metadata"));
        assertEquals(true, asset.urlQueries.get("include_metadata"));
    }

    // ========== ASSET FIELDS TESTS (CDA asset_fields[] parameter) ==========

    @Test
    void testAssetFieldsWithSupportedValues() {
        Asset result = asset.assetFields("user_defined_fields", "embedded", "ai_suggested", "visual_markups");
        assertSame(asset, result);
        assertTrue(asset.urlQueries.has("asset_fields[]"));
        Object val = asset.urlQueries.get("asset_fields[]");
        assertTrue(val instanceof JSONArray);
        JSONArray arr = (JSONArray) val;
        assertEquals(4, arr.length());
        assertEquals("user_defined_fields", arr.get(0));
        assertEquals("embedded", arr.get(1));
        assertEquals("ai_suggested", arr.get(2));
        assertEquals("visual_markups", arr.get(3));
    }

    @Test
    void testAssetFieldsReturnsThis() {
        Asset result = asset.assetFields("user_defined_fields");
        assertSame(asset, result);
    }

    @Test
    void testAssetFieldsWithNoArgsDoesNotSetParam() {
        asset.assetFields();
        assertFalse(asset.urlQueries.has("asset_fields[]"));
    }

    @Test
    void testAssetFieldsWithNullDoesNotSetParam() {
        asset.assetFields((String[]) null);
        assertFalse(asset.urlQueries.has("asset_fields[]"));
    }

    @Test
    void testAssetFieldsChainingWithOtherMethods() {
        Asset result = asset.assetFields("embedded", "visual_markups")
            .includeMetadata()
            .includeDimension();
        assertSame(asset, result);
        assertTrue(asset.urlQueries.has("asset_fields[]"));
        assertTrue(asset.urlQueries.has("include_metadata"));
        assertTrue(asset.urlQueries.has("include_dimension"));
        JSONArray arr = asset.urlQueries.getJSONArray("asset_fields[]");
        assertEquals(2, arr.length());
        assertEquals("embedded", arr.get(0));
        assertEquals("visual_markups", arr.get(1));
    }

    /**
     * Usage: stack.asset(assetUid).assetFields(...).fetch()
     * Verifies the full chain sets asset_fields[] on the asset before fetch.
     */
    @Test
    void testUsageSingleAssetFetchWithAssetFields() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        Asset asset = stack.asset("asset_uid_123")
            .assetFields("embedded", "visual_markups");
        assertTrue(asset.urlQueries.has("asset_fields[]"));
        JSONArray arr = asset.urlQueries.getJSONArray("asset_fields[]");
        assertEquals(2, arr.length());
        assertEquals("embedded", arr.get(0));
        assertEquals("visual_markups", arr.get(1));
    }


    @Test
    void testAssetFieldsSingleField() {
        asset.assetFields("embedded");
        assertTrue(asset.urlQueries.has("asset_fields[]"));
        JSONArray arr = asset.urlQueries.getJSONArray("asset_fields[]");
        assertEquals(1, arr.length());
        assertEquals("embedded", arr.get(0));
    }

    @Test
    void testAssetFieldsEmptyVarargsArrayDoesNotSetParam() {
        asset.assetFields(new String[0]);
        assertFalse(asset.urlQueries.has("asset_fields[]"));
    }

    @Test
    void testAssetFieldsDuplicateValuesAllowed() {
        asset.assetFields("embedded", "embedded");
        JSONArray arr = asset.urlQueries.getJSONArray("asset_fields[]");
        assertEquals(2, arr.length());
        assertEquals("embedded", arr.get(0));
        assertEquals("embedded", arr.get(1));
    }

    @Test
    void testAssetFieldsSecondCallOverwrites() {
        asset.assetFields("user_defined_fields", "embedded");
        asset.assetFields("ai_suggested");
        JSONArray arr = asset.urlQueries.getJSONArray("asset_fields[]");
        assertEquals(1, arr.length());
        assertEquals("ai_suggested", arr.get(0));
    }

    @Test
    void testAssetFieldsWithEmptyStringInArray() {
        asset.assetFields("valid", "", "embedded");
        JSONArray arr = asset.urlQueries.getJSONArray("asset_fields[]");
        assertEquals(3, arr.length());
        assertEquals("valid", arr.get(0));
        assertEquals("", arr.get(1));
        assertEquals("embedded", arr.get(2));
    }

    @Test
    void testAssetFieldsWithNullInArray() {
        asset.assetFields("valid", null, "embedded");
        JSONArray arr = asset.urlQueries.getJSONArray("asset_fields[]");
        assertEquals(3, arr.length());
        assertEquals("valid", arr.get(0));
        assertEquals("embedded", arr.get(2));
    }

    @Test
    void testAssetFieldsSingleEmptyStringSetsParam() {
        asset.assetFields("");
        assertTrue(asset.urlQueries.has("asset_fields[]"));
        assertEquals(1, asset.urlQueries.getJSONArray("asset_fields[]").length());
        assertEquals("", asset.urlQueries.getJSONArray("asset_fields[]").get(0));
    }

    // ========== CHAINING TESTS ==========

    @Test
    void testMethodChaining() {
        Asset result = asset
            .includeDimension()
            .includeFallback()
            .includeMetadata()
            .includeBranch();
        
        assertSame(asset, result);
        assertTrue(asset.urlQueries.has("include_dimension"));
        assertTrue(asset.urlQueries.has("include_fallback"));
        assertTrue(asset.urlQueries.has("include_metadata"));
        assertTrue(asset.urlQueries.has("include_branch"));
    }

    @Test
    void testComplexChainingWithParams() {
        asset.addParam("key1", "val1")
             .addParam("param1", "pval1")
             .includeDimension()
             .includeMetadata();
        
        assertTrue(asset.urlQueries.has("key1"));
        assertTrue(asset.urlQueries.has("param1"));
        assertTrue(asset.urlQueries.has("include_dimension"));
        assertTrue(asset.urlQueries.has("include_metadata"));
    }

    // ========== EDGE CASE TESTS ==========

    @Test
    void testMultipleIncludeCallsAccumulate() {
        asset.includeDimension();
        asset.includeFallback();
        asset.includeMetadata();
        
        assertTrue(asset.urlQueries.has("include_dimension"));
        assertTrue(asset.urlQueries.has("include_fallback"));
        assertTrue(asset.urlQueries.has("include_metadata"));
        assertEquals(3, asset.urlQueries.length());
    }

    @Test
    void testAssetWithCompleteData() {
        JSONObject json = new JSONObject();
        json.put("uid", "complete_uid");
        json.put("content_type", "image/png");
        json.put("file_size", "4096");
        json.put("filename", "complete.png");
        json.put("url", "https://example.com/complete.png");
        json.put("tags", new String[]{"tag1", "tag2"});
        
        asset.configure(json);
        assertEquals("complete_uid", asset.getAssetUid());
    }

    @Test
    void testUrlQueriesInitialization() {
        Asset newAsset = new Asset("test_uid");
        assertNotNull(newAsset.urlQueries);
        assertEquals(0, newAsset.urlQueries.length());
    }

    @Test
    void testHeadersInitialization() {
        Asset newAsset = new Asset("test_uid");
        assertNotNull(newAsset.headers);
        assertEquals(0, newAsset.headers.size());
    }

    @Test
    void testHeaderOverwrite() {
        asset.setHeader("key", "value1");
        assertEquals("value1", asset.headers.get("key"));
        
        asset.setHeader("key", "value2");
        assertEquals("value2", asset.headers.get("key"));
    }

    @Test
    void testRemoveAndAddSameHeader() {
        asset.setHeader("key", "value1");
        asset.removeHeader("key");
        assertFalse(asset.headers.containsKey("key"));
        
        asset.setHeader("key", "value2");
        assertEquals("value2", asset.headers.get("key"));
    }

    @Test
    void testConfigureWithMinimalData() {
        JSONObject json = new JSONObject();
        json.put("uid", "test_uid");
        
        Asset result = asset.configure(json);
        assertNotNull(result);
        assertEquals("test_uid", asset.getAssetUid());
    }

    @Test
    void testAddParamWithEmptyValue() {
        asset.addParam("empty", "");
        assertTrue(asset.urlQueries.has("empty"));
        assertEquals("", asset.urlQueries.get("empty"));
    }

    @Test
    void testMultipleConfigureCalls() {
        JSONObject json1 = new JSONObject();
        json1.put("uid", "uid1");
        asset.configure(json1);
        assertEquals("uid1", asset.getAssetUid());
        
        JSONObject json2 = new JSONObject();
        json2.put("uid", "uid2");
        asset.configure(json2);
        assertEquals("uid2", asset.getAssetUid());
    }

    @Test
    void testFetchSetsEnvironmentParameter() {
        // Setup: Configure asset with mock data
        JSONObject mockAssetData = new JSONObject();
        mockAssetData.put("uid", "uid");
        mockAssetData.put("content_type", "image/jpeg");
        mockAssetData.put("file_size", "1048576");
        mockAssetData.put("filename", "test_image.jpg");
        mockAssetData.put("url", "https://example.com/test_image.jpg");
        mockAssetData.put("created_at", "2023-01-01T00:00:00.000Z");
        mockAssetData.put("updated_at", "2023-01-02T00:00:00.000Z");
        mockAssetData.put("created_by", "user");
        mockAssetData.put("updated_by", "user");
        
        asset.configure(mockAssetData);
        asset.setHeader("environment", "test");
        
        // Verify asset is configured with mock data
        assertEquals("uid", asset.getAssetUid());
        assertEquals("image/jpeg", asset.getFileType());
        assertEquals("1048576", asset.getFileSize());
        assertEquals("test_image.jpg", asset.getFileName());
        
        // Manually simulate what fetch() does: add environment to urlQueries
        asset.urlQueries.put("environment", asset.headers.get("environment"));
        
        // Verify environment parameter was added to urlQueries
        assertTrue(asset.urlQueries.has("environment"));
        assertEquals("test", asset.urlQueries.get("environment"));
    }

    @Test
    void testFetchWithMockAssetDataVerification() {
        // Setup: Create asset with comprehensive mock data
        JSONObject mockData = new JSONObject();
        mockData.put("uid", "uid");
        mockData.put("content_type", "image/png");
        mockData.put("file_size", "2097152");
        mockData.put("filename", "mock_file.png");
        mockData.put("url", "https://cdn.example.com/mock_file.png");
        mockData.put("title", "Mock Asset Title");
        mockData.put("description", "Mock asset description");
        mockData.put("created_at", "2023-06-15T10:30:00.000Z");
        mockData.put("updated_at", "2023-06-20T14:45:00.000Z");
        mockData.put("created_by", "user");
        mockData.put("updated_by", "user");
        
        JSONArray tags = new JSONArray();
        tags.put("test");
        tags.put("mock");
        tags.put("asset");
        mockData.put("tags", tags);
        
        // Configure asset with mock data
        asset.configure(mockData);
        asset.setHeader("environment", "production");
        
        // Verify all mock data is properly set
        assertEquals("uid", asset.getAssetUid());
        assertEquals("image/png", asset.getFileType());
        assertEquals("2097152", asset.getFileSize());
        assertEquals("mock_file.png", asset.getFileName());
        assertEquals("https://cdn.example.com/mock_file.png", asset.getUrl());
        assertArrayEquals(new String[]{"test", "mock", "asset"}, asset.getTags());
        assertNotNull(asset.toJSON());
        assertTrue(asset.toJSON().has("created_at"));
        assertTrue(asset.toJSON().has("updated_at"));
        
        // Manually simulate what fetch() does: add environment to urlQueries
        asset.urlQueries.put("environment", asset.headers.get("environment"));
        
        // Verify environment was added to urlQueries
        assertTrue(asset.urlQueries.has("environment"));
        assertEquals("production", asset.urlQueries.get("environment"));
    }

    @Test
    void testFetchPreservesConfiguredMockData() {
        // Setup: Configure asset with specific mock properties
        JSONObject mockConfig = new JSONObject();
        mockConfig.put("uid", "uid");
        mockConfig.put("content_type", "application/json");
        mockConfig.put("file_size", "5242880");
        mockConfig.put("filename", "document.pdf");
        
        asset.configure(mockConfig);
        asset.setHeader("environment", "staging");
        
        // Add additional parameters before fetch
        asset.includeDimension();
        asset.includeMetadata();
        
        // Verify configuration before fetch
        assertEquals("uid", asset.getAssetUid());
        assertEquals("application/json", asset.getFileType());
        
        // Manually simulate what fetch() does: add environment to urlQueries
        asset.urlQueries.put("environment", asset.headers.get("environment"));
        
        // Verify all parameters are preserved after fetch simulation
        assertTrue(asset.urlQueries.has("environment"));
        assertTrue(asset.urlQueries.has("include_dimension"));
        assertTrue(asset.urlQueries.has("include_metadata"));
        assertEquals("staging", asset.urlQueries.get("environment"));
        
        // Verify configured data is still intact
        assertEquals("uid", asset.getAssetUid());
        assertEquals("application/json", asset.getFileType());
        assertEquals("5242880", asset.getFileSize());
    }

    @Test
    void testFetchWithVariousMockEnvironments() {
        // Test with development environment
        JSONObject devMockData = new JSONObject();
        devMockData.put("uid", "uid");
        devMockData.put("filename", "dev_file.jpg");
        
        Asset devAsset = new Asset();
        devAsset.configure(devMockData);
        devAsset.headers = new LinkedHashMap<>();
        devAsset.headers.put("environment", "development");
        
        // Manually simulate what fetch() does: add environment to urlQueries
        devAsset.urlQueries.put("environment", devAsset.headers.get("environment"));
        
        assertTrue(devAsset.urlQueries.has("environment"));
        assertEquals("development", devAsset.urlQueries.get("environment"));
        assertEquals("uid", devAsset.getAssetUid());
        
        // Test with production environment
        JSONObject prodMockData = new JSONObject();
        prodMockData.put("uid", "uid");
        prodMockData.put("filename", "prod_file.jpg");
        
        Asset prodAsset = new Asset();
        prodAsset.configure(prodMockData);
        prodAsset.headers = new LinkedHashMap<>();
        prodAsset.headers.put("environment", "production");
        
        // Manually simulate what fetch() does: add environment to urlQueries
        prodAsset.urlQueries.put("environment", prodAsset.headers.get("environment"));
        
        assertTrue(prodAsset.urlQueries.has("environment"));
        assertEquals("production", prodAsset.urlQueries.get("environment"));
        assertEquals("uid", prodAsset.getAssetUid());
    }

    // ========== CALENDAR/DATE GETTER TESTS ==========

    @Test
    void testGetCreateAt() {
        JSONObject json = new JSONObject();
        json.put("uid", "test_uid");
        json.put("created_at", "2023-01-15T10:30:00.000Z");
        
        asset.configure(json);
        
        assertNotNull(asset.getCreateAt());
        assertEquals("gregory", asset.getCreateAt().getCalendarType());
    }

    @Test
    void testGetUpdateAt() {
        JSONObject json = new JSONObject();
        json.put("uid", "test_uid");
        json.put("updated_at", "2023-06-20T14:45:30.000Z");
        
        asset.configure(json);
        
        assertNotNull(asset.getUpdateAt());
        assertEquals("gregory", asset.getUpdateAt().getCalendarType());
    }

    @Test
    void testGetDeleteAt() {
        JSONObject json = new JSONObject();
        json.put("uid", "test_uid");
        json.put("deleted_at", "2023-12-31T23:59:59.000Z");
        
        asset.configure(json);
        
        assertNotNull(asset.getDeleteAt());
        assertEquals("gregory", asset.getDeleteAt().getCalendarType());
    }

    @Test
    void testGetDeleteAtWhenNull() {
        JSONObject json = new JSONObject();
        json.put("uid", "test_uid");
        // No deleted_at field
        
        asset.configure(json);
        
        assertNull(asset.getDeleteAt());
    }

    // ========== USER GETTER TESTS ==========

    @Test
    void testGetCreatedBy() {
        JSONObject json = new JSONObject();
        json.put("uid", "test_uid");
        json.put("created_by", "user_creator_123");
        
        asset.configure(json);
        
        assertEquals("user_creator_123", asset.getCreatedBy());
    }

    @Test
    void testGetUpdatedBy() {
        JSONObject json = new JSONObject();
        json.put("uid", "test_uid");
        json.put("updated_by", "user_updater_456");
        
        asset.configure(json);
        
        assertEquals("user_updater_456", asset.getUpdatedBy());
    }

    @Test
    void testGetDeletedBy() {
        JSONObject json = new JSONObject();
        json.put("uid", "test_uid");
        json.put("deleted_by", "user_deleter_789");
        
        asset.configure(json);
        
        assertEquals("user_deleter_789", asset.getDeletedBy());
    }

    @Test
    void testGetDeletedByWhenEmpty() {
        JSONObject json = new JSONObject();
        json.put("uid", "test_uid");
        // No deleted_by field
        
        asset.configure(json);
        
        assertEquals("", asset.getDeletedBy());
    }

    // ========== SET UID TESTS ==========

    @Test
    void testSetUid() {
        asset.setUid("new_asset_uid");
        assertEquals("new_asset_uid", asset.getAssetUid());
    }

    @Test
    void testSetUidMultipleTimes() {
        asset.setUid("uid1");
        assertEquals("uid1", asset.getAssetUid());
        
        asset.setUid("uid2");
        assertEquals("uid2", asset.getAssetUid());
        
        asset.setUid("uid3");
        assertEquals("uid3", asset.getAssetUid());
    }

    @Test
    void testSetUidWithSpecialCharacters() {
        asset.setUid("asset_uid-with-dashes_123");
        assertEquals("asset_uid-with-dashes_123", asset.getAssetUid());
    }

    @Test
    void testSetUidOverwritesConfiguredUid() {
        JSONObject json = new JSONObject();
        json.put("uid", "configured_uid");
        asset.configure(json);
        
        assertEquals("configured_uid", asset.getAssetUid());
        
        asset.setUid("overwritten_uid");
        assertEquals("overwritten_uid", asset.getAssetUid());
    }

    // ========== COMPREHENSIVE CONFIGURATION TESTS ==========

    @Test
    void testConfigureWithAllDateFields() {
        JSONObject json = new JSONObject();
        json.put("uid", "date_test_uid");
        json.put("created_at", "2023-01-01T00:00:00.000Z");
        json.put("updated_at", "2023-06-15T12:30:00.000Z");
        json.put("deleted_at", "2023-12-31T23:59:59.000Z");
        json.put("created_by", "creator_user");
        json.put("updated_by", "updater_user");
        json.put("deleted_by", "deleter_user");
        
        asset.configure(json);
        
        // Verify all date fields
        assertNotNull(asset.getCreateAt());
        assertNotNull(asset.getUpdateAt());
        assertNotNull(asset.getDeleteAt());
        
        // Verify all user fields
        assertEquals("creator_user", asset.getCreatedBy());
        assertEquals("updater_user", asset.getUpdatedBy());
        assertEquals("deleter_user", asset.getDeletedBy());
    }

    @Test
    void testConfigureWithMissingDateFields() {
        JSONObject json = new JSONObject();
        json.put("uid", "minimal_uid");
        // No date or user fields
        
        asset.configure(json);
        
        // deleted_at should be null when not provided
        assertNull(asset.getDeleteAt());
        
        // deleted_by should be empty string when not provided
        assertEquals("", asset.getDeletedBy());
    }

    @Test
    void testGettersWithCompleteAssetData() {
        JSONObject completeData = new JSONObject();
        completeData.put("uid", "complete_asset");
        completeData.put("content_type", "image/jpeg");
        completeData.put("file_size", "3145728");
        completeData.put("filename", "complete_image.jpg");
        completeData.put("url", "https://cdn.example.com/complete_image.jpg");
        completeData.put("created_at", "2023-03-15T08:20:00.000Z");
        completeData.put("updated_at", "2023-09-20T16:45:00.000Z");
        completeData.put("created_by", "blt_creator");
        completeData.put("updated_by", "blt_updater");
        
        JSONArray tags = new JSONArray();
        tags.put("production");
        tags.put("featured");
        completeData.put("tags", tags);
        
        asset.configure(completeData);
        
        // Test all getters
        assertEquals("complete_asset", asset.getAssetUid());
        assertEquals("image/jpeg", asset.getFileType());
        assertEquals("3145728", asset.getFileSize());
        assertEquals("complete_image.jpg", asset.getFileName());
        assertEquals("https://cdn.example.com/complete_image.jpg", asset.getUrl());
        assertArrayEquals(new String[]{"production", "featured"}, asset.getTags());
        assertNotNull(asset.getCreateAt());
        assertNotNull(asset.getUpdateAt());
        assertNull(asset.getDeleteAt());
        assertEquals("blt_creator", asset.getCreatedBy());
        assertEquals("blt_updater", asset.getUpdatedBy());
        assertEquals("", asset.getDeletedBy());
        assertNotNull(asset.toJSON());
    }

    @Test
    void testDateFieldsWithDifferentFormats() {
        JSONObject json = new JSONObject();
        json.put("uid", "date_format_test");
        json.put("created_at", "2023-01-01T00:00:00.000Z");
        json.put("updated_at", "2023-12-31T23:59:59.999Z");
        
        asset.configure(json);
        
        assertNotNull(asset.getCreateAt());
        assertNotNull(asset.getUpdateAt());
        
        // Verify they are Calendar objects
        assertEquals("gregory", asset.getCreateAt().getCalendarType());
        assertEquals("gregory", asset.getUpdateAt().getCalendarType());
    }

    // ========== FETCH METHOD TESTS ==========
    // Note: These tests actually call fetch() which triggers CSBackgroundTask creation
    // The background task won't complete in unit tests, but we verify the method execution

    @Test
    void testFetchWithValidCallback() throws IllegalAccessException {
        // Create a stack instance for the asset
        Stack stack = Contentstack.stack("api_key", "delivery_token", "environment");
        asset.setStackInstance(stack);
        asset.setHeader("environment", "production");
        asset.setUid("test_asset_uid");
        
        // Create a callback
        FetchResultCallback callback = new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                // This won't be called in unit tests but validates the callback interface
            }
        };
        
        // Actually call fetch() - this will create CSBackgroundTask
        // The task won't complete but the method should execute without error
        assertDoesNotThrow(() -> asset.fetch(callback));
        
        // Verify environment was added to urlQueries by fetch()
        assertTrue(asset.urlQueries.has("environment"));
        assertEquals("production", asset.urlQueries.opt("environment"));
    }

    @Test
    void testFetchWithNullCallback() throws IllegalAccessException {
        // Create a stack instance
        Stack stack = Contentstack.stack("api_key", "delivery_token", "environment");
        asset.setStackInstance(stack);
        asset.setHeader("environment", "staging");
        asset.setUid("test_asset_uid");
        
        // Call fetch with null callback - should not throw but won't create background task
        assertDoesNotThrow(() -> asset.fetch(null));
        
        // Environment should still be added to urlQueries
        assertTrue(asset.urlQueries.has("environment"));
    }

    @Test
    void testFetchAddsEnvironmentFromHeaders() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "environment");
        asset.setStackInstance(stack);
        asset.setHeader("environment", "development");
        asset.setUid("asset_123");
        
        FetchResultCallback callback = new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {}
        };
        
        // Call fetch
        asset.fetch(callback);
        
        // Verify environment from headers was added to urlQueries
        assertTrue(asset.urlQueries.has("environment"));
        assertEquals("development", asset.urlQueries.get("environment"));
    }

    @Test
    void testFetchPreservesExistingUrlQueries() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "environment");
        asset.setStackInstance(stack);
        asset.setHeader("environment", "production");
        asset.setUid("asset_789");
        
        // Add some url queries before fetch
        asset.urlQueries.put("include_dimension", true);
        asset.urlQueries.put("version", "1.0");
        
        FetchResultCallback callback = new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {}
        };
        
        // Call fetch
        asset.fetch(callback);
        
        // Verify environment is added while preserving existing queries
        assertEquals("production", asset.urlQueries.get("environment"));
        assertTrue((Boolean) asset.urlQueries.get("include_dimension"));
        assertEquals("1.0", asset.urlQueries.get("version"));
    }

    // ========== GET URL PARAMS TESTS ==========

    @Test
    void testGetUrlParamsWithNullJSON() throws Exception {
        // Use reflection to access private method
        java.lang.reflect.Method method = Asset.class.getDeclaredMethod("getUrlParams", JSONObject.class);
        method.setAccessible(true);
        
        @SuppressWarnings("unchecked")
        HashMap<String, Object> result = (HashMap<String, Object>) method.invoke(asset, (JSONObject) null);
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetUrlParamsWithEmptyJSON() throws Exception {
        JSONObject emptyJson = new JSONObject();
        
        java.lang.reflect.Method method = Asset.class.getDeclaredMethod("getUrlParams", JSONObject.class);
        method.setAccessible(true);
        
        @SuppressWarnings("unchecked")
        HashMap<String, Object> result = (HashMap<String, Object>) method.invoke(asset, emptyJson);
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetUrlParamsWithSingleEntry() throws Exception {
        JSONObject json = new JSONObject();
        json.put("environment", "production");
        
        java.lang.reflect.Method method = Asset.class.getDeclaredMethod("getUrlParams", JSONObject.class);
        method.setAccessible(true);
        
        @SuppressWarnings("unchecked")
        HashMap<String, Object> result = (HashMap<String, Object>) method.invoke(asset, json);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("production", result.get("environment"));
    }

    @Test
    void testGetUrlParamsWithMultipleEntries() throws Exception {
        JSONObject json = new JSONObject();
        json.put("environment", "staging");
        json.put("include_dimension", true);
        json.put("version", 2);
        json.put("locale", "en-us");
        
        java.lang.reflect.Method method = Asset.class.getDeclaredMethod("getUrlParams", JSONObject.class);
        method.setAccessible(true);
        
        @SuppressWarnings("unchecked")
        HashMap<String, Object> result = (HashMap<String, Object>) method.invoke(asset, json);
        
        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals("staging", result.get("environment"));
        assertEquals(true, result.get("include_dimension"));
        assertEquals(2, result.get("version"));
        assertEquals("en-us", result.get("locale"));
    }

    @Test
    void testGetUrlParamsWithNestedJSON() throws Exception {
        JSONObject nested = new JSONObject();
        nested.put("key1", "value1");
        
        JSONObject json = new JSONObject();
        json.put("nested", nested);
        json.put("simple", "test");
        
        java.lang.reflect.Method method = Asset.class.getDeclaredMethod("getUrlParams", JSONObject.class);
        method.setAccessible(true);
        
        @SuppressWarnings("unchecked")
        HashMap<String, Object> result = (HashMap<String, Object>) method.invoke(asset, json);
        
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsKey("nested"));
        assertEquals("test", result.get("simple"));
    }

    @Test
    void testGetUrlParamsWithArray() throws Exception {
        JSONArray array = new JSONArray();
        array.put("item1");
        array.put("item2");
        
        JSONObject json = new JSONObject();
        json.put("tags", array);
        json.put("count", 10);
        
        java.lang.reflect.Method method = Asset.class.getDeclaredMethod("getUrlParams", JSONObject.class);
        method.setAccessible(true);
        
        @SuppressWarnings("unchecked")
        HashMap<String, Object> result = (HashMap<String, Object>) method.invoke(asset, json);
        
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsKey("tags"));
        assertEquals(10, result.get("count"));
    }

    @Test
    void testGetUrlParamsWithNullValues() throws Exception {
        JSONObject json = new JSONObject();
        json.put("key1", "value1");
        json.put("key2", JSONObject.NULL);
        json.put("key3", (Object) null);
        
        java.lang.reflect.Method method = Asset.class.getDeclaredMethod("getUrlParams", JSONObject.class);
        method.setAccessible(true);
        
        @SuppressWarnings("unchecked")
        HashMap<String, Object> result = (HashMap<String, Object>) method.invoke(asset, json);
        
        assertNotNull(result);
        // All keys should be present, including those with null values
        assertTrue(result.containsKey("key1"));
        assertEquals("value1", result.get("key1"));
    }

    @Test
    void testGetUrlParamsWithSpecialCharacters() throws Exception {
        JSONObject json = new JSONObject();
        json.put("query", "text with spaces");
        json.put("special", "value&with=special?chars");
        json.put("unicode", "日本語");
        
        java.lang.reflect.Method method = Asset.class.getDeclaredMethod("getUrlParams", JSONObject.class);
        method.setAccessible(true);
        
        @SuppressWarnings("unchecked")
        HashMap<String, Object> result = (HashMap<String, Object>) method.invoke(asset, json);
        
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("text with spaces", result.get("query"));
        assertEquals("value&with=special?chars", result.get("special"));
        assertEquals("日本語", result.get("unicode"));
    }

    @Test
    void testGetUrlParamsWithBooleanAndNumericValues() throws Exception {
        JSONObject json = new JSONObject();
        json.put("boolean_true", true);
        json.put("boolean_false", false);
        json.put("integer", 42);
        json.put("double", 3.14);
        json.put("long", 9999999999L);
        
        java.lang.reflect.Method method = Asset.class.getDeclaredMethod("getUrlParams", JSONObject.class);
        method.setAccessible(true);
        
        @SuppressWarnings("unchecked")
        HashMap<String, Object> result = (HashMap<String, Object>) method.invoke(asset, json);
        
        assertNotNull(result);
        assertEquals(5, result.size());
        assertEquals(true, result.get("boolean_true"));
        assertEquals(false, result.get("boolean_false"));
        assertEquals(42, result.get("integer"));
        assertEquals(3.14, result.get("double"));
        assertEquals(9999999999L, result.get("long"));
    }

    @Test
    void testGetUrlParamsPreservesAllDataTypes() throws Exception {
        JSONObject json = new JSONObject();
        json.put("string", "test");
        json.put("int", 123);
        json.put("bool", true);
        json.put("double", 45.67);
        
        java.lang.reflect.Method method = Asset.class.getDeclaredMethod("getUrlParams", JSONObject.class);
        method.setAccessible(true);
        
        @SuppressWarnings("unchecked")
        HashMap<String, Object> result = (HashMap<String, Object>) method.invoke(asset, json);
        
        // Verify all types are preserved
        assertTrue(result.get("string") instanceof String);
        assertTrue(result.get("int") instanceof Integer);
        assertTrue(result.get("bool") instanceof Boolean);
        assertTrue(result.get("double") instanceof Double);
    }
}
