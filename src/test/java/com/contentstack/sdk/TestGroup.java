package com.contentstack.sdk;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for Group class.
 * Tests all getter methods for different data types and nested structures.
 */
public class TestGroup {

    private Stack stack;
    private JSONObject testJson;
    private Group group;

    @BeforeEach
    void setUp() throws Exception {
        stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        
        // Create a test JSON with various data types
        testJson = new JSONObject();
        testJson.put("string_field", "test_string");
        testJson.put("boolean_field", true);
        testJson.put("number_field", 42);
        testJson.put("float_field", 3.14);
        testJson.put("double_field", 3.14159);
        testJson.put("long_field", 1234567890L);
        testJson.put("short_field", 100);
        testJson.put("date_field", "2023-11-06T10:30:00.000Z");
        
        // JSON Object
        JSONObject nestedObject = new JSONObject();
        nestedObject.put("nested_key", "nested_value");
        testJson.put("object_field", nestedObject);
        
        // JSON Array
        JSONArray jsonArray = new JSONArray();
        jsonArray.put("item1");
        jsonArray.put("item2");
        testJson.put("array_field", jsonArray);
        
        // Asset object
        JSONObject assetObject = new JSONObject();
        assetObject.put("uid", "asset_uid_1");
        assetObject.put("url", "https://example.com/asset.jpg");
        testJson.put("asset_field", assetObject);
        
        // Assets array
        JSONArray assetsArray = new JSONArray();
        JSONObject asset1 = new JSONObject();
        asset1.put("uid", "asset_1");
        assetsArray.put(asset1);
        JSONObject asset2 = new JSONObject();
        asset2.put("uid", "asset_2");
        assetsArray.put(asset2);
        testJson.put("assets_field", assetsArray);
        
        // Nested group
        JSONObject groupObject = new JSONObject();
        groupObject.put("group_key", "group_value");
        testJson.put("group_field", groupObject);
        
        // Groups array
        JSONArray groupsArray = new JSONArray();
        JSONObject group1 = new JSONObject();
        group1.put("name", "Group 1");
        groupsArray.put(group1);
        JSONObject group2 = new JSONObject();
        group2.put("name", "Group 2");
        groupsArray.put(group2);
        testJson.put("groups_field", groupsArray);
        
        // Entry references
        JSONArray entriesArray = new JSONArray();
        JSONObject entry1 = new JSONObject();
        entry1.put("uid", "entry_1");
        entry1.put("title", "Entry 1");
        entriesArray.put(entry1);
        testJson.put("entries_field", entriesArray);
        
        // Create Group instance using reflection
        Constructor<Group> constructor = Group.class.getDeclaredConstructor(Stack.class, JSONObject.class);
        constructor.setAccessible(true);
        group = constructor.newInstance(stack, testJson);
    }

    // ========== TO JSON TESTS ==========

    @Test
    void testToJSON() {
        JSONObject result = group.toJSON();
        assertNotNull(result);
        assertEquals(testJson, result);
        assertTrue(result.has("string_field"));
    }

    // ========== GET METHOD TESTS ==========

    @Test
    void testGetWithValidKey() {
        Object result = group.get("string_field");
        assertNotNull(result);
        assertEquals("test_string", result);
    }

    @Test
    void testGetWithNullKey() {
        Object result = group.get(null);
        assertNull(result);
    }

    @Test
    void testGetWithNonExistentKey() {
        // JSONObject.get() throws exception for non-existent keys
        assertThrows(org.json.JSONException.class, () -> group.get("non_existent_key"));
    }

    @Test
    void testGetWithNullResultJson() throws Exception {
        Constructor<Group> constructor = Group.class.getDeclaredConstructor(Stack.class, JSONObject.class);
        constructor.setAccessible(true);
        Group nullGroup = constructor.newInstance(stack, null);
        
        Object result = nullGroup.get("any_key");
        assertNull(result);
    }

    // ========== GET STRING TESTS ==========

    @Test
    void testGetStringWithValidKey() {
        String result = group.getString("string_field");
        assertNotNull(result);
        assertEquals("test_string", result);
    }

    @Test
    void testGetStringWithNullValue() {
        // Throws exception for non-existent key via get() method
        assertThrows(org.json.JSONException.class, () -> group.getString("non_existent_key"));
    }

    @Test
    void testGetStringWithNullKey() {
        String result = group.getString(null);
        assertNull(result);
    }

    // ========== GET BOOLEAN TESTS ==========

    @Test
    void testGetBooleanWithValidKey() {
        Boolean result = group.getBoolean("boolean_field");
        assertNotNull(result);
        assertTrue(result);
    }

    @Test
    void testGetBooleanWithNullValue() {
        // Throws exception for non-existent key via get() method
        assertThrows(org.json.JSONException.class, () -> group.getBoolean("non_existent_key"));
    }

    @Test
    void testGetBooleanWithFalseValue() throws Exception {
        testJson.put("false_field", false);
        Constructor<Group> constructor = Group.class.getDeclaredConstructor(Stack.class, JSONObject.class);
        constructor.setAccessible(true);
        Group newGroup = constructor.newInstance(stack, testJson);
        
        Boolean result = newGroup.getBoolean("false_field");
        assertFalse(result);
    }

    // ========== GET JSON ARRAY TESTS ==========

    @Test
    void testGetJSONArrayWithValidKey() {
        JSONArray result = group.getJSONArray("array_field");
        assertNotNull(result);
        assertEquals(2, result.length());
        assertEquals("item1", result.get(0));
    }

    @Test
    void testGetJSONArrayWithNullValue() {
        // Throws exception for non-existent key via get() method
        assertThrows(org.json.JSONException.class, () -> group.getJSONArray("non_existent_key"));
    }

    // ========== GET JSON OBJECT TESTS ==========

    @Test
    void testGetJSONObjectWithValidKey() {
        JSONObject result = group.getJSONObject("object_field");
        assertNotNull(result);
        assertTrue(result.has("nested_key"));
        assertEquals("nested_value", result.get("nested_key"));
    }

    @Test
    void testGetJSONObjectWithNullValue() {
        // Throws exception for non-existent key via get() method
        assertThrows(org.json.JSONException.class, () -> group.getJSONObject("non_existent_key"));
    }

    // ========== GET NUMBER TESTS ==========

    @Test
    void testGetNumberWithValidKey() {
        Number result = group.getNumber("number_field");
        assertNotNull(result);
        assertEquals(42, result.intValue());
    }

    @Test
    void testGetNumberWithNullValue() {
        // Throws exception for non-existent key via get() method
        assertThrows(org.json.JSONException.class, () -> group.getNumber("non_existent_key"));
    }

    // ========== GET INT TESTS ==========

    @Test
    void testGetIntWithValidKey() {
        int result = group.getInt("number_field");
        assertEquals(42, result);
    }

    @Test
    void testGetIntWithNullValue() {
        // Throws exception for non-existent key via getNumber() -> get() method
        assertThrows(org.json.JSONException.class, () -> group.getInt("non_existent_key"));
    }

    // ========== GET FLOAT TESTS ==========

    @Test
    void testGetFloatWithValidKey() {
        float result = group.getFloat("float_field");
        assertEquals(3.14f, result, 0.01);
    }

    @Test
    void testGetFloatWithNullValue() {
        // Throws exception for non-existent key via getNumber() -> get() method
        assertThrows(org.json.JSONException.class, () -> group.getFloat("non_existent_key"));
    }

    // ========== GET DOUBLE TESTS ==========

    @Test
    void testGetDoubleWithValidKey() {
        double result = group.getDouble("double_field");
        assertEquals(3.14159, result, 0.00001);
    }

    @Test
    void testGetDoubleWithNullValue() {
        // Throws exception for non-existent key via getNumber() -> get() method
        assertThrows(org.json.JSONException.class, () -> group.getDouble("non_existent_key"));
    }

    // ========== GET LONG TESTS ==========

    @Test
    void testGetLongWithValidKey() {
        long result = group.getLong("long_field");
        assertEquals(1234567890L, result);
    }

    @Test
    void testGetLongWithNullValue() {
        // Throws exception for non-existent key via getNumber() -> get() method
        assertThrows(org.json.JSONException.class, () -> group.getLong("non_existent_key"));
    }

    // ========== GET SHORT TESTS ==========

    @Test
    void testGetShortWithValidKey() {
        short result = group.getShort("short_field");
        assertEquals((short) 100, result);
    }

    @Test
    void testGetShortWithNullValue() {
        // Throws exception for non-existent key via getNumber() -> get() method
        assertThrows(org.json.JSONException.class, () -> group.getShort("non_existent_key"));
    }

    // ========== GET DATE TESTS ==========

    @Test
    void testGetDateWithValidKey() {
        Calendar result = group.getDate("date_field");
        assertNotNull(result);
    }

    @Test
    void testGetDateWithNullValue() {
        Calendar result = group.getDate("non_existent_key");
        assertNull(result);
    }

    @Test
    void testGetDateWithInvalidFormat() {
        testJson.put("invalid_date", "not_a_date");
        Calendar result = group.getDate("invalid_date");
        // Should return null on exception
        assertNull(result);
    }

    // ========== GET ASSET TESTS ==========

    @Test
    void testGetAssetWithValidKey() {
        Asset result = group.getAsset("asset_field");
        assertNotNull(result);
    }

    @Test
    void testGetAssetWithNullValue() {
        // Throws exception for non-existent key via getJSONObject() -> get() method
        assertThrows(org.json.JSONException.class, () -> group.getAsset("non_existent_key"));
    }

    // ========== GET ASSETS TESTS ==========

    @Test
    void testGetAssetsWithValidKey() {
        List<Asset> result = group.getAssets("assets_field");
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetAssetsWithEmptyArray() throws Exception {
        testJson.put("empty_assets", new JSONArray());
        Constructor<Group> constructor = Group.class.getDeclaredConstructor(Stack.class, JSONObject.class);
        constructor.setAccessible(true);
        Group newGroup = constructor.newInstance(stack, testJson);
        
        List<Asset> result = newGroup.getAssets("empty_assets");
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testGetAssetsWithNonJSONObjectItems() throws Exception {
        JSONArray mixedArray = new JSONArray();
        mixedArray.put("not_an_object");
        JSONObject validAsset = new JSONObject();
        validAsset.put("uid", "valid_asset");
        mixedArray.put(validAsset);
        
        testJson.put("mixed_assets", mixedArray);
        Constructor<Group> constructor = Group.class.getDeclaredConstructor(Stack.class, JSONObject.class);
        constructor.setAccessible(true);
        Group newGroup = constructor.newInstance(stack, testJson);
        
        List<Asset> result = newGroup.getAssets("mixed_assets");
        assertNotNull(result);
        assertEquals(1, result.size());  // Only the valid JSONObject is processed
    }

    // ========== GET GROUP TESTS ==========

    @Test
    void testGetGroupWithValidKey() {
        Group result = group.getGroup("group_field");
        assertNotNull(result);
        assertEquals("group_value", result.get("group_key"));
    }

    @Test
    void testGetGroupWithEmptyKey() {
        Group result = group.getGroup("");
        assertNull(result);
    }

    @Test
    void testGetGroupWithNonExistentKey() {
        Group result = group.getGroup("non_existent_key");
        assertNull(result);
    }

    @Test
    void testGetGroupWithNonJSONObjectValue() {
        Group result = group.getGroup("string_field");
        assertNull(result);
    }

    // ========== GET GROUPS TESTS ==========

    @Test
    void testGetGroupsWithValidKey() {
        List<Group> result = group.getGroups("groups_field");
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Group 1", result.get(0).get("name"));
        assertEquals("Group 2", result.get(1).get("name"));
    }

    @Test
    void testGetGroupsWithEmptyKey() {
        List<Group> result = group.getGroups("");
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testGetGroupsWithNonExistentKey() {
        List<Group> result = group.getGroups("non_existent_key");
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testGetGroupsWithNonArrayValue() {
        List<Group> result = group.getGroups("string_field");
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testGetGroupsWithEmptyArray() throws Exception {
        testJson.put("empty_groups", new JSONArray());
        Constructor<Group> constructor = Group.class.getDeclaredConstructor(Stack.class, JSONObject.class);
        constructor.setAccessible(true);
        Group newGroup = constructor.newInstance(stack, testJson);
        
        List<Group> result = newGroup.getGroups("empty_groups");
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    // ========== GET ALL ENTRIES TESTS ==========

    @Test
    void testGetAllEntriesWithValidKey() {
        List<Entry> result = group.getAllEntries("entries_field", "test_content_type");
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllEntriesWithNonExistentKey() {
        List<Entry> result = group.getAllEntries("non_existent_key", "test_content_type");
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testGetAllEntriesWithNonArrayValue() {
        List<Entry> result = group.getAllEntries("string_field", "test_content_type");
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testGetAllEntriesWithEmptyArray() throws Exception {
        testJson.put("empty_entries", new JSONArray());
        Constructor<Group> constructor = Group.class.getDeclaredConstructor(Stack.class, JSONObject.class);
        constructor.setAccessible(true);
        Group newGroup = constructor.newInstance(stack, testJson);
        
        List<Entry> result = newGroup.getAllEntries("empty_entries", "test_content_type");
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testGetAllEntriesWithMultipleEntries() throws Exception {
        JSONArray entriesArray = new JSONArray();
        
        JSONObject entry1 = new JSONObject();
        entry1.put("uid", "entry_1");
        entry1.put("title", "Entry 1");
        entry1.put("tags", new JSONArray());
        entriesArray.put(entry1);
        
        JSONObject entry2 = new JSONObject();
        entry2.put("uid", "entry_2");
        entry2.put("title", "Entry 2");
        entry2.put("tags", new JSONArray());
        entriesArray.put(entry2);
        
        testJson.put("multiple_entries", entriesArray);
        Constructor<Group> constructor = Group.class.getDeclaredConstructor(Stack.class, JSONObject.class);
        constructor.setAccessible(true);
        Group newGroup = constructor.newInstance(stack, testJson);
        
        List<Entry> result = newGroup.getAllEntries("multiple_entries", "test_content_type");
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetAllEntriesWithException() throws Exception {
        // Test with null resultJson
        Constructor<Group> constructor = Group.class.getDeclaredConstructor(Stack.class, JSONObject.class);
        constructor.setAccessible(true);
        Group nullGroup = constructor.newInstance(stack, null);
        
        List<Entry> result = nullGroup.getAllEntries("any_key", "test_content_type");
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    // ========== ENTRY INSTANCE TESTS (private method, covered via getAllEntries) ==========

    @Test
    void testEntryInstanceViaGetAllEntries() {
        // This test covers the entryInstance private method
        List<Entry> result = group.getAllEntries("entries_field", "test_content_type");
        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertNotNull(result.get(0));
    }

    // ========== EDGE CASE TESTS ==========

    @Test
    void testMultipleDataTypes() {
        // Verify all data types can be accessed
        assertNotNull(group.getString("string_field"));
        assertNotNull(group.getBoolean("boolean_field"));
        assertNotNull(group.getNumber("number_field"));
        assertNotNull(group.getJSONObject("object_field"));
        assertNotNull(group.getJSONArray("array_field"));
    }

    @Test
    void testNullSafety() {
        // Verify null safety for all getter methods
        assertNull(group.get(null));
        assertNull(group.getString(null));
        assertFalse(group.getBoolean(null));
        assertNull(group.getJSONArray(null));
        assertNull(group.getJSONObject(null));
        assertNull(group.getNumber(null));
        // Note: Number getter methods return 0 for null keys (checked in if condition)
        assertEquals(0, group.getInt(null));
        assertEquals(0f, group.getFloat(null));
        assertEquals(0.0, group.getDouble(null));
        assertEquals(0L, group.getLong(null));
        assertEquals((short) 0, group.getShort(null));
        assertNull(group.getDate(null));
    }

    @Test
    void testConstructorWithStackAndJSON() throws Exception {
        Constructor<Group> constructor = Group.class.getDeclaredConstructor(Stack.class, JSONObject.class);
        constructor.setAccessible(true);
        
        JSONObject json = new JSONObject();
        json.put("test_key", "test_value");
        
        Group testGroup = constructor.newInstance(stack, json);
        assertNotNull(testGroup);
        assertEquals("test_value", testGroup.get("test_key"));
    }
}

