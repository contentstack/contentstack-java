package com.contentstack.sdk;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for SyncStack class.
 * Tests all getters, setJSON method, and edge cases.
 */
public class TestSyncStack {

    private SyncStack syncStack;

    @BeforeEach
    void setUp() {
        syncStack = new SyncStack();
    }

    // ========== GETTER TESTS ==========

    @Test
    void testGetUrlInitiallyNull() {
        assertNull(syncStack.getUrl());
    }

    @Test
    void testGetJSONResponseInitiallyNull() {
        assertNull(syncStack.getJSONResponse());
    }

    @Test
    void testGetCountInitiallyZero() {
        assertEquals(0, syncStack.getCount());
    }

    @Test
    void testGetLimitInitiallyZero() {
        assertEquals(0, syncStack.getLimit());
    }

    @Test
    void testGetSkipInitiallyZero() {
        assertEquals(0, syncStack.getSkip());
    }

    @Test
    void testGetPaginationTokenInitiallyNull() {
        assertNull(syncStack.getPaginationToken());
    }

    @Test
    void testGetSyncTokenInitiallyNull() {
        assertNull(syncStack.getSyncToken());
    }

    @Test
    void testGetItemsInitiallyNull() {
        assertNull(syncStack.getItems());
    }

    // ========== SET JSON WITH NULL TESTS ==========

    @Test
    void testSetJSONWithNull() {
        assertThrows(IllegalArgumentException.class, () -> syncStack.setJSON(null));
    }

    // ========== SET JSON WITH ITEMS AS JSONARRAY ==========

    @Test
    void testSetJSONWithItemsAsJSONArray() {
        JSONObject json = new JSONObject();
        JSONArray itemsArray = new JSONArray();
        
        JSONObject item1 = new JSONObject();
        item1.put("uid", "item1");
        item1.put("title", "Test Item 1");
        itemsArray.put(item1);
        
        JSONObject item2 = new JSONObject();
        item2.put("uid", "item2");
        item2.put("title", "Test Item 2");
        itemsArray.put(item2);
        
        json.put("items", itemsArray);
        json.put("skip", 10);
        json.put("limit", 100);
        json.put("total_count", 250);
        json.put("pagination_token", "valid_token_123");
        json.put("sync_token", "sync_abc_xyz");
        
        syncStack.setJSON(json);
        
        assertEquals(json, syncStack.getJSONResponse());
        assertEquals(10, syncStack.getSkip());
        assertEquals(100, syncStack.getLimit());
        assertEquals(250, syncStack.getCount());
        assertEquals("valid_token_123", syncStack.getPaginationToken());
        assertEquals("sync_abc_xyz", syncStack.getSyncToken());
        assertNotNull(syncStack.getItems());
        assertEquals(2, syncStack.getItems().size());
    }

    @Test
    void testSetJSONWithItemsAsJSONArrayWithNullItems() {
        JSONObject json = new JSONObject();
        JSONArray itemsArray = new JSONArray();
        
        JSONObject item1 = new JSONObject();
        item1.put("uid", "item1");
        itemsArray.put(item1);
        
        itemsArray.put(JSONObject.NULL);  // Null item
        
        JSONObject item2 = new JSONObject();
        item2.put("uid", "item2");
        itemsArray.put(item2);
        
        json.put("items", itemsArray);
        
        syncStack.setJSON(json);
        
        assertNotNull(syncStack.getItems());
        // Should only have 2 items (null item is skipped)
        assertEquals(2, syncStack.getItems().size());
    }

    // ========== SET JSON WITH ITEMS AS JSONOBJECT ==========

    @Test
    void testSetJSONWithItemsAsJSONObject() {
        JSONObject json = new JSONObject();
        JSONObject singleItem = new JSONObject();
        singleItem.put("uid", "single_item");
        singleItem.put("title", "Single Test Item");
        
        json.put("items", singleItem);
        json.put("total_count", 1);
        
        syncStack.setJSON(json);
        
        assertNotNull(syncStack.getItems());
        assertEquals(1, syncStack.getItems().size());
        assertEquals(1, syncStack.getCount());
    }

    // ========== SET JSON WITH ITEMS AS LIST ==========

    @Test
    void testSetJSONWithItemsAsListOfJSONObjects() throws Exception {
        JSONObject json = new JSONObject();
        
        List<JSONObject> itemsList = new ArrayList<>();
        JSONObject item1 = new JSONObject();
        item1.put("uid", "item1");
        itemsList.add(item1);
        
        JSONObject item2 = new JSONObject();
        item2.put("uid", "item2");
        itemsList.add(item2);
        
        // Use reflection to inject ArrayList into JSONObject
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        HashMap<String, Object> map = (HashMap<String, Object>) mapField.get(json);
        map.put("items", itemsList);
        
        syncStack.setJSON(json);
        
        assertNotNull(syncStack.getItems());
        assertEquals(2, syncStack.getItems().size());
    }

    @Test
    void testSetJSONWithItemsAsListOfMaps() throws Exception {
        JSONObject json = new JSONObject();
        
        List<LinkedHashMap<String, Object>> itemsList = new ArrayList<>();
        
        LinkedHashMap<String, Object> map1 = new LinkedHashMap<>();
        map1.put("uid", "item1");
        map1.put("title", "Item 1");
        itemsList.add(map1);
        
        LinkedHashMap<String, Object> map2 = new LinkedHashMap<>();
        map2.put("uid", "item2");
        map2.put("title", "Item 2");
        itemsList.add(map2);
        
        // Use reflection to inject ArrayList into JSONObject
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        HashMap<String, Object> map = (HashMap<String, Object>) mapField.get(json);
        map.put("items", itemsList);
        
        syncStack.setJSON(json);
        
        assertNotNull(syncStack.getItems());
        assertEquals(2, syncStack.getItems().size());
    }

    @Test
    void testSetJSONWithItemsAsListOfInvalidTypes() throws Exception {
        JSONObject json = new JSONObject();
        
        List<Object> itemsList = new ArrayList<>();
        itemsList.add("invalid_string_item");
        itemsList.add(12345);
        
        JSONObject validItem = new JSONObject();
        validItem.put("uid", "valid_item");
        itemsList.add(validItem);
        
        // Use reflection to inject ArrayList into JSONObject
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        HashMap<String, Object> map = (HashMap<String, Object>) mapField.get(json);
        map.put("items", itemsList);
        
        syncStack.setJSON(json);
        
        assertNotNull(syncStack.getItems());
        // Should only have 1 item (invalid items are skipped with warning)
        assertEquals(1, syncStack.getItems().size());
    }

    // ========== SET JSON WITH ITEMS AS INVALID TYPE ==========

    @Test
    void testSetJSONWithItemsAsString() throws Exception {
        JSONObject json = new JSONObject();
        
        // Use reflection to inject String into JSONObject
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        HashMap<String, Object> map = (HashMap<String, Object>) mapField.get(json);
        map.put("items", "invalid_string");
        
        syncStack.setJSON(json);
        
        // Should create empty list and log warning
        assertNotNull(syncStack.getItems());
        assertEquals(0, syncStack.getItems().size());
    }

    @Test
    void testSetJSONWithItemsAsNull() throws Exception {
        JSONObject json = new JSONObject();
        
        // Use reflection to inject null into JSONObject
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        HashMap<String, Object> map = (HashMap<String, Object>) mapField.get(json);
        map.put("items", null);
        
        syncStack.setJSON(json);
        
        // Should create empty list and log warning
        assertNotNull(syncStack.getItems());
        assertEquals(0, syncStack.getItems().size());
    }

    // ========== SET JSON WITHOUT ITEMS ==========

    @Test
    void testSetJSONWithoutItems() {
        JSONObject json = new JSONObject();
        json.put("total_count", 100);
        json.put("skip", 0);
        json.put("limit", 50);
        
        syncStack.setJSON(json);
        
        assertNotNull(syncStack.getItems());
        assertEquals(0, syncStack.getItems().size());
        assertEquals(100, syncStack.getCount());
        assertEquals(0, syncStack.getSkip());
        assertEquals(50, syncStack.getLimit());
    }

    // ========== OPTIONAL FIELDS TESTS ==========

    @Test
    void testSetJSONWithoutOptionalFields() {
        JSONObject json = new JSONObject();
        json.put("items", new JSONArray());
        
        syncStack.setJSON(json);
        
        assertEquals(0, syncStack.getSkip());
        assertEquals(0, syncStack.getLimit());
        assertEquals(0, syncStack.getCount());
        assertNull(syncStack.getPaginationToken());
        assertNull(syncStack.getSyncToken());
    }

    @Test
    void testSetJSONWithAllOptionalFields() {
        JSONObject json = new JSONObject();
        json.put("items", new JSONArray());
        json.put("skip", 25);
        json.put("limit", 75);
        json.put("total_count", 500);
        json.put("pagination_token", "page_token_abc");
        json.put("sync_token", "sync_token_xyz");
        
        syncStack.setJSON(json);
        
        assertEquals(25, syncStack.getSkip());
        assertEquals(75, syncStack.getLimit());
        assertEquals(500, syncStack.getCount());
        assertEquals("page_token_abc", syncStack.getPaginationToken());
        assertEquals("sync_token_xyz", syncStack.getSyncToken());
    }

    // ========== TOKEN VALIDATION TESTS ==========

    @Test
    void testSetJSONWithValidPaginationToken() {
        JSONObject json = new JSONObject();
        json.put("items", new JSONArray());
        json.put("pagination_token", "valid_token-123_abc.xyz");
        
        syncStack.setJSON(json);
        
        assertEquals("valid_token-123_abc.xyz", syncStack.getPaginationToken());
    }

    @Test
    void testSetJSONWithInvalidPaginationToken() {
        JSONObject json = new JSONObject();
        json.put("items", new JSONArray());
        json.put("pagination_token", "invalid@token#with$special%chars");
        
        syncStack.setJSON(json);
        
        // Invalid token should be set to null
        assertNull(syncStack.getPaginationToken());
    }

    @Test
    void testSetJSONWithValidSyncToken() {
        JSONObject json = new JSONObject();
        json.put("items", new JSONArray());
        json.put("sync_token", "valid_sync_token-456_def.ghi");
        
        syncStack.setJSON(json);
        
        assertEquals("valid_sync_token-456_def.ghi", syncStack.getSyncToken());
    }

    @Test
    void testSetJSONWithInvalidSyncToken() {
        JSONObject json = new JSONObject();
        json.put("items", new JSONArray());
        json.put("sync_token", "invalid!sync@token#");
        
        syncStack.setJSON(json);
        
        // Invalid token should be set to null
        assertNull(syncStack.getSyncToken());
    }

    @Test
    void testSetJSONTokensSetToNullWhenNotPresent() {
        JSONObject json = new JSONObject();
        json.put("items", new JSONArray());
        // No pagination_token or sync_token
        
        syncStack.setJSON(json);
        
        assertNull(syncStack.getPaginationToken());
        assertNull(syncStack.getSyncToken());
    }

    // ========== SANITIZE JSON TESTS (INDIRECT) ==========

    @Test
    void testSetJSONSanitizesScriptTags() {
        JSONObject json = new JSONObject();
        JSONObject item = new JSONObject();
        item.put("malicious_field", "<script>alert('XSS')</script>");
        item.put("normal_field", "safe_value");
        
        JSONArray itemsArray = new JSONArray();
        itemsArray.put(item);
        json.put("items", itemsArray);
        
        syncStack.setJSON(json);
        
        assertNotNull(syncStack.getItems());
        assertEquals(1, syncStack.getItems().size());
        
        JSONObject sanitizedItem = syncStack.getItems().get(0);
        String maliciousValue = sanitizedItem.getString("malicious_field");
        
        // Script tags should be sanitized
        assertTrue(maliciousValue.contains("&lt;script&gt;"));
        assertTrue(maliciousValue.contains("&lt;/script&gt;"));
        assertFalse(maliciousValue.contains("<script>"));
    }

    @Test
    void testSetJSONSanitizesClosingScriptTags() {
        JSONObject json = new JSONObject();
        JSONObject item = new JSONObject();
        item.put("field", "</SCRIPT>test</Script>");
        
        JSONArray itemsArray = new JSONArray();
        itemsArray.put(item);
        json.put("items", itemsArray);
        
        syncStack.setJSON(json);
        
        JSONObject sanitizedItem = syncStack.getItems().get(0);
        String value = sanitizedItem.getString("field");
        
        // Both uppercase and lowercase should be sanitized (case-insensitive)
        assertTrue(value.contains("&lt;/script&gt;"));
        assertFalse(value.contains("</SCRIPT>"));
        assertFalse(value.contains("</Script>"));
    }

    @Test
    void testSetJSONPreservesNonStringValues() {
        JSONObject json = new JSONObject();
        JSONObject item = new JSONObject();
        item.put("string_field", "text");
        item.put("number_field", 42);
        item.put("boolean_field", true);
        item.put("null_field", JSONObject.NULL);
        
        JSONArray itemsArray = new JSONArray();
        itemsArray.put(item);
        json.put("items", itemsArray);
        
        syncStack.setJSON(json);
        
        JSONObject sanitizedItem = syncStack.getItems().get(0);
        
        // Non-string values should be preserved
        assertEquals("text", sanitizedItem.getString("string_field"));
        assertEquals(42, sanitizedItem.getInt("number_field"));
        assertEquals(true, sanitizedItem.getBoolean("boolean_field"));
        assertTrue(sanitizedItem.isNull("null_field"));
    }

    // ========== VALIDATE TOKEN TESTS (INDIRECT) ==========

    @Test
    void testValidateTokenWithAlphanumeric() {
        JSONObject json = new JSONObject();
        json.put("items", new JSONArray());
        json.put("pagination_token", "abc123XYZ789");
        
        syncStack.setJSON(json);
        
        assertEquals("abc123XYZ789", syncStack.getPaginationToken());
    }

    @Test
    void testValidateTokenWithHyphens() {
        JSONObject json = new JSONObject();
        json.put("items", new JSONArray());
        json.put("sync_token", "token-with-hyphens-123");
        
        syncStack.setJSON(json);
        
        assertEquals("token-with-hyphens-123", syncStack.getSyncToken());
    }

    @Test
    void testValidateTokenWithUnderscores() {
        JSONObject json = new JSONObject();
        json.put("items", new JSONArray());
        json.put("pagination_token", "token_with_underscores_456");
        
        syncStack.setJSON(json);
        
        assertEquals("token_with_underscores_456", syncStack.getPaginationToken());
    }

    @Test
    void testValidateTokenWithDots() {
        JSONObject json = new JSONObject();
        json.put("items", new JSONArray());
        json.put("sync_token", "token.with.dots.789");
        
        syncStack.setJSON(json);
        
        assertEquals("token.with.dots.789", syncStack.getSyncToken());
    }

    @Test
    void testValidateTokenWithSpecialCharsInvalid() {
        JSONObject json = new JSONObject();
        json.put("items", new JSONArray());
        json.put("pagination_token", "invalid!@#$%^&*()");
        
        syncStack.setJSON(json);
        
        // Invalid characters should cause token to be null
        assertNull(syncStack.getPaginationToken());
    }

    @Test
    void testValidateTokenWithSpacesInvalid() {
        JSONObject json = new JSONObject();
        json.put("items", new JSONArray());
        json.put("sync_token", "token with spaces");
        
        syncStack.setJSON(json);
        
        // Spaces are not allowed
        assertNull(syncStack.getSyncToken());
    }

    // ========== MULTIPLE SET JSON CALLS ==========

    @Test
    void testMultipleSetJSONCallsOverwriteValues() {
        // First call
        JSONObject json1 = new JSONObject();
        json1.put("items", new JSONArray());
        json1.put("total_count", 100);
        json1.put("pagination_token", "token1");
        
        syncStack.setJSON(json1);
        assertEquals(100, syncStack.getCount());
        assertEquals("token1", syncStack.getPaginationToken());
        
        // Second call should overwrite
        JSONObject json2 = new JSONObject();
        json2.put("items", new JSONArray());
        json2.put("total_count", 200);
        json2.put("pagination_token", "token2");
        
        syncStack.setJSON(json2);
        assertEquals(200, syncStack.getCount());
        assertEquals("token2", syncStack.getPaginationToken());
    }

    @Test
    void testSetJSONResetsTokensToNull() {
        // First call with tokens
        JSONObject json1 = new JSONObject();
        json1.put("items", new JSONArray());
        json1.put("pagination_token", "page_token");
        json1.put("sync_token", "sync_token");
        
        syncStack.setJSON(json1);
        assertEquals("page_token", syncStack.getPaginationToken());
        assertEquals("sync_token", syncStack.getSyncToken());
        
        // Second call without tokens - should be null
        JSONObject json2 = new JSONObject();
        json2.put("items", new JSONArray());
        
        syncStack.setJSON(json2);
        assertNull(syncStack.getPaginationToken());
        assertNull(syncStack.getSyncToken());
    }

    // ========== EDGE CASES ==========

    @Test
    void testSetJSONWithEmptyJSONObject() {
        JSONObject json = new JSONObject();
        
        syncStack.setJSON(json);
        
        assertNotNull(syncStack.getJSONResponse());
        assertNotNull(syncStack.getItems());
        assertEquals(0, syncStack.getItems().size());
        assertEquals(0, syncStack.getCount());
        assertEquals(0, syncStack.getLimit());
        assertEquals(0, syncStack.getSkip());
        assertNull(syncStack.getPaginationToken());
        assertNull(syncStack.getSyncToken());
    }

    @Test
    void testSetJSONWithLargeItemsArray() {
        JSONObject json = new JSONObject();
        JSONArray itemsArray = new JSONArray();
        
        // Add 1000 items
        for (int i = 0; i < 1000; i++) {
            JSONObject item = new JSONObject();
            item.put("uid", "item_" + i);
            item.put("index", i);
            itemsArray.put(item);
        }
        
        json.put("items", itemsArray);
        json.put("total_count", 1000);
        
        syncStack.setJSON(json);
        
        assertNotNull(syncStack.getItems());
        assertEquals(1000, syncStack.getItems().size());
        assertEquals(1000, syncStack.getCount());
    }

    @Test
    void testSetJSONWithZeroValues() {
        JSONObject json = new JSONObject();
        json.put("items", new JSONArray());
        json.put("skip", 0);
        json.put("limit", 0);
        json.put("total_count", 0);
        
        syncStack.setJSON(json);
        
        assertEquals(0, syncStack.getSkip());
        assertEquals(0, syncStack.getLimit());
        assertEquals(0, syncStack.getCount());
    }

    @Test
    void testSetJSONWithNegativeValues() {
        JSONObject json = new JSONObject();
        json.put("items", new JSONArray());
        json.put("skip", -10);
        json.put("limit", -5);
        json.put("total_count", -100);
        
        syncStack.setJSON(json);
        
        // Should accept negative values (validation could be added)
        assertEquals(-10, syncStack.getSkip());
        assertEquals(-5, syncStack.getLimit());
        assertEquals(-100, syncStack.getCount());
    }
}

