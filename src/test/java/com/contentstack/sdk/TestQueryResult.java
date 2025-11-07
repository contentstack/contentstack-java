package com.contentstack.sdk;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test cases for the QueryResult class
 */
class TestQueryResult {

    @Test
    void testQueryResultInitialization() {
        QueryResult queryResult = new QueryResult();
        
        assertNull(queryResult.getResultObjects());
        assertEquals(0, queryResult.getCount());
        assertNull(queryResult.getSchema());
        assertNull(queryResult.getContentType());
    }

    @Test
    void testSetJSONWithBasicData() {
        QueryResult queryResult = new QueryResult();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", 5);
        
        List<Entry> entryList = new ArrayList<>();
        
        queryResult.setJSON(jsonObject, entryList);
        
        assertEquals(5, queryResult.getCount());
        assertNotNull(queryResult.getResultObjects());
        assertEquals(0, queryResult.getResultObjects().size());
    }

    @Test
    void testSetJSONWithSchema() {
        QueryResult queryResult = new QueryResult();
        
        JSONArray schemaArray = new JSONArray();
        JSONObject field1 = new JSONObject();
        field1.put("uid", "title");
        field1.put("data_type", "text");
        schemaArray.put(field1);
        
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("schema", schemaArray);
        jsonObject.put("count", 1);
        
        List<Entry> entryList = new ArrayList<>();
        
        queryResult.setJSON(jsonObject, entryList);
        
        assertNotNull(queryResult.getSchema());
        assertEquals(1, queryResult.getSchema().length());
    }

    @Test
    void testSetJSONWithEntries() {
        QueryResult queryResult = new QueryResult();
        
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("entries", 10);
        
        List<Entry> entryList = new ArrayList<>();
        
        queryResult.setJSON(jsonObject, entryList);
        
        // When count is 0, it should check for entries field
        assertEquals(10, queryResult.getCount());
    }

    @Test
    void testSetJSONWithNullValues() {
        QueryResult queryResult = new QueryResult();
        
        JSONObject jsonObject = new JSONObject();
        List<Entry> entryList = null;
        
        queryResult.setJSON(jsonObject, entryList);
        
        assertNull(queryResult.getResultObjects());
        assertEquals(0, queryResult.getCount());
    }

    @Test
    void testGetResultObjectsReturnsCorrectList() {
        QueryResult queryResult = new QueryResult();
        
        List<Entry> expectedEntries = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        
        queryResult.setJSON(jsonObject, expectedEntries);
        
        List<Entry> actualEntries = queryResult.getResultObjects();
        assertSame(expectedEntries, actualEntries);
    }

    @Test
    void testCountPriorityOverEntries() {
        QueryResult queryResult = new QueryResult();
        
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", 5);
        jsonObject.put("entries", 10);
        
        List<Entry> entryList = new ArrayList<>();
        
        queryResult.setJSON(jsonObject, entryList);
        
        // Count should take priority
        assertEquals(5, queryResult.getCount());
    }

    @Test
    void testSetJSONWithEmptySchema() {
        QueryResult queryResult = new QueryResult();
        
        JSONArray emptySchema = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("schema", emptySchema);
        
        List<Entry> entryList = new ArrayList<>();
        
        queryResult.setJSON(jsonObject, entryList);
        
        assertNotNull(queryResult.getSchema());
        assertEquals(0, queryResult.getSchema().length());
    }

    @Test
    void testSetJSONWithMultipleSchemaFields() {
        QueryResult queryResult = new QueryResult();
        
        JSONArray schemaArray = new JSONArray();
        
        JSONObject field1 = new JSONObject();
        field1.put("uid", "title");
        field1.put("data_type", "text");
        schemaArray.put(field1);
        
        JSONObject field2 = new JSONObject();
        field2.put("uid", "description");
        field2.put("data_type", "text");
        schemaArray.put(field2);
        
        JSONObject field3 = new JSONObject();
        field3.put("uid", "date");
        field3.put("data_type", "date");
        schemaArray.put(field3);
        
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("schema", schemaArray);
        
        List<Entry> entryList = new ArrayList<>();
        
        queryResult.setJSON(jsonObject, entryList);
        
        assertNotNull(queryResult.getSchema());
        assertEquals(3, queryResult.getSchema().length());
    }

    @Test
    void testSetJSONMultipleTimes() {
        QueryResult queryResult = new QueryResult();
        
        // First call
        JSONObject json1 = new JSONObject();
        json1.put("count", 5);
        List<Entry> list1 = new ArrayList<>();
        queryResult.setJSON(json1, list1);
        assertEquals(5, queryResult.getCount());
        
        // Second call - should overwrite
        JSONObject json2 = new JSONObject();
        json2.put("count", 10);
        List<Entry> list2 = new ArrayList<>();
        queryResult.setJSON(json2, list2);
        assertEquals(10, queryResult.getCount());
    }

    // ========== EXCEPTION HANDLING TESTS ==========

    @Test
    void testExtractSchemaArrayWithInvalidSchemaType() throws Exception {
        QueryResult queryResult = new QueryResult();
        
        // Create JSON with schema as a string instead of array (will cause exception)
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("schema", "invalid_schema_string");
        
        List<Entry> entryList = new ArrayList<>();
        
        // Should handle exception gracefully without throwing
        assertDoesNotThrow(() -> queryResult.setJSON(jsonObject, entryList));
        
        // Schema should remain null due to exception
        assertNull(queryResult.getSchema());
    }

    @Test
    void testExtractSchemaArrayWithNullJSON() {
        QueryResult queryResult = new QueryResult();
        
        // Set JSON with null
        assertDoesNotThrow(() -> queryResult.setJSON(null, new ArrayList<>()));
        
        // Schema should remain null
        assertNull(queryResult.getSchema());
        assertEquals(0, queryResult.getCount());
    }

    @Test
    void testExtractContentObjectWithValidContentType() throws Exception {
        QueryResult queryResult = new QueryResult();
        
        // Use reflection to inject LinkedHashMap for content_type (as network responses do)
        JSONObject jsonObject = new JSONObject();
        LinkedHashMap<String, Object> contentTypeMap = new LinkedHashMap<>();
        contentTypeMap.put("uid", "blog_post");
        contentTypeMap.put("title", "Blog Post");
        
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        HashMap<String, Object> map = (HashMap<String, Object>) mapField.get(jsonObject);
        map.put("content_type", contentTypeMap);
        
        List<Entry> entryList = new ArrayList<>();
        
        queryResult.setJSON(jsonObject, entryList);
        
        assertNotNull(queryResult.getContentType());
        assertEquals("blog_post", queryResult.getContentType().get("uid"));
    }

    @Test
    void testExtractContentObjectWithInvalidContentType() throws Exception {
        QueryResult queryResult = new QueryResult();
        
        // Create JSON with content_type as a string instead of Map (will cause ClassCastException)
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content_type", "invalid_content_type_string");
        
        List<Entry> entryList = new ArrayList<>();
        
        // Should handle exception gracefully without throwing
        assertDoesNotThrow(() -> queryResult.setJSON(jsonObject, entryList));
        
        // contentObject should remain null due to exception
        assertNull(queryResult.getContentType());
    }

    @Test
    void testExtractContentObjectWithNullValue() throws Exception {
        QueryResult queryResult = new QueryResult();
        
        // Create JSON with null content_type
        JSONObject jsonObject = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        HashMap<String, Object> map = (HashMap<String, Object>) mapField.get(jsonObject);
        map.put("content_type", null);
        
        List<Entry> entryList = new ArrayList<>();
        
        // Should handle null gracefully
        assertDoesNotThrow(() -> queryResult.setJSON(jsonObject, entryList));
        
        assertNull(queryResult.getContentType());
    }

    @Test
    void testExtractCountWithZeroAndEntriesField() {
        QueryResult queryResult = new QueryResult();
        
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", 0);
        jsonObject.put("entries", 15);
        
        List<Entry> entryList = new ArrayList<>();
        
        queryResult.setJSON(jsonObject, entryList);
        
        // When count is 0, should check entries field
        assertEquals(15, queryResult.getCount());
    }

    @Test
    void testExtractCountWithInvalidEntriesValue() {
        QueryResult queryResult = new QueryResult();
        
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", 0);
        jsonObject.put("entries", "invalid_entries_value");
        
        List<Entry> entryList = new ArrayList<>();
        
        // Should handle invalid value gracefully
        assertDoesNotThrow(() -> queryResult.setJSON(jsonObject, entryList));
        
        // Should default to 0 when optInt can't parse
        assertEquals(0, queryResult.getCount());
    }

    @Test
    void testExtractCountWithMissingCountField() {
        QueryResult queryResult = new QueryResult();
        
        JSONObject jsonObject = new JSONObject();
        // No count or entries field
        
        List<Entry> entryList = new ArrayList<>();
        
        queryResult.setJSON(jsonObject, entryList);
        
        // Should default to 0
        assertEquals(0, queryResult.getCount());
    }

    @Test
    void testExtractCountWithNegativeValue() {
        QueryResult queryResult = new QueryResult();
        
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", -5);
        
        List<Entry> entryList = new ArrayList<>();
        
        queryResult.setJSON(jsonObject, entryList);
        
        // Should accept negative value (business logic validation is elsewhere)
        assertEquals(-5, queryResult.getCount());
    }

    @Test
    void testSetJSONWithEmptyJSONAndNullList() {
        QueryResult queryResult = new QueryResult();
        
        JSONObject emptyJson = new JSONObject();
        
        assertDoesNotThrow(() -> queryResult.setJSON(emptyJson, null));
        
        assertNull(queryResult.getResultObjects());
        assertNull(queryResult.getSchema());
        assertNull(queryResult.getContentType());
        assertEquals(0, queryResult.getCount());
    }

    @Test
    void testSetJSONWithComplexSchemaStructure() {
        QueryResult queryResult = new QueryResult();
        
        JSONArray schemaArray = new JSONArray();
        
        // Add complex nested schema
        JSONObject field = new JSONObject();
        field.put("uid", "nested_field");
        field.put("data_type", "group");
        
        JSONArray nestedSchema = new JSONArray();
        JSONObject nestedField = new JSONObject();
        nestedField.put("uid", "inner_field");
        nestedField.put("data_type", "text");
        nestedSchema.put(nestedField);
        
        field.put("schema", nestedSchema);
        schemaArray.put(field);
        
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("schema", schemaArray);
        
        List<Entry> entryList = new ArrayList<>();
        
        queryResult.setJSON(jsonObject, entryList);
        
        assertNotNull(queryResult.getSchema());
        assertEquals(1, queryResult.getSchema().length());
        JSONObject retrievedField = queryResult.getSchema().getJSONObject(0);
        assertEquals("nested_field", retrievedField.get("uid"));
        assertTrue(retrievedField.has("schema"));
    }

    @Test
    void testSetJSONWithLargeCount() {
        QueryResult queryResult = new QueryResult();
        
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", Integer.MAX_VALUE);
        
        List<Entry> entryList = new ArrayList<>();
        
        queryResult.setJSON(jsonObject, entryList);
        
        assertEquals(Integer.MAX_VALUE, queryResult.getCount());
    }

    @Test
    void testSetJSONWithContentTypeContainingSchema() throws Exception {
        QueryResult queryResult = new QueryResult();
        
        // Create content_type with nested schema
        LinkedHashMap<String, Object> contentTypeMap = new LinkedHashMap<>();
        contentTypeMap.put("uid", "blog");
        contentTypeMap.put("title", "Blog");
        
        JSONArray schema = new JSONArray();
        JSONObject field = new JSONObject();
        field.put("uid", "title");
        schema.put(field);
        contentTypeMap.put("schema", schema);
        
        JSONObject jsonObject = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        HashMap<String, Object> map = (HashMap<String, Object>) mapField.get(jsonObject);
        map.put("content_type", contentTypeMap);
        
        List<Entry> entryList = new ArrayList<>();
        
        queryResult.setJSON(jsonObject, entryList);
        
        assertNotNull(queryResult.getContentType());
        assertTrue(queryResult.getContentType().has("schema"));
    }

    @Test
    void testMultipleSetJSONCallsWithDifferentData() {
        QueryResult queryResult = new QueryResult();
        
        // First call with schema
        JSONObject json1 = new JSONObject();
        JSONArray schema1 = new JSONArray();
        schema1.put(new JSONObject().put("uid", "field1"));
        json1.put("schema", schema1);
        json1.put("count", 5);
        
        queryResult.setJSON(json1, new ArrayList<>());
        
        assertNotNull(queryResult.getSchema());
        assertEquals(1, queryResult.getSchema().length());
        assertEquals(5, queryResult.getCount());
        
        // Second call with different schema and count
        JSONObject json2 = new JSONObject();
        JSONArray schema2 = new JSONArray();
        schema2.put(new JSONObject().put("uid", "field2"));
        schema2.put(new JSONObject().put("uid", "field3"));
        json2.put("schema", schema2);
        json2.put("count", 10);
        
        queryResult.setJSON(json2, new ArrayList<>());
        
        // Should overwrite previous values
        assertNotNull(queryResult.getSchema());
        assertEquals(2, queryResult.getSchema().length());
        assertEquals(10, queryResult.getCount());
    }

    @Test
    void testSetJSONWithAllFieldsPresent() throws Exception {
        QueryResult queryResult = new QueryResult();
        
        // Create comprehensive JSON with all possible fields
        JSONArray schemaArray = new JSONArray();
        schemaArray.put(new JSONObject().put("uid", "title"));
        
        LinkedHashMap<String, Object> contentTypeMap = new LinkedHashMap<>();
        contentTypeMap.put("uid", "test_ct");
        contentTypeMap.put("title", "Test CT");
        
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("schema", schemaArray);
        jsonObject.put("count", 25);
        
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        HashMap<String, Object> map = (HashMap<String, Object>) mapField.get(jsonObject);
        map.put("content_type", contentTypeMap);
        
        List<Entry> entryList = new ArrayList<>();
        
        queryResult.setJSON(jsonObject, entryList);
        
        // All fields should be populated
        assertNotNull(queryResult.getSchema());
        assertEquals(1, queryResult.getSchema().length());
        assertNotNull(queryResult.getContentType());
        assertEquals("test_ct", queryResult.getContentType().get("uid"));
        assertEquals(25, queryResult.getCount());
        assertNotNull(queryResult.getResultObjects());
    }

    @Test
    void testGettersReturnCorrectValuesAfterSetJSON() {
        QueryResult queryResult = new QueryResult();
        
        JSONObject jsonObject = new JSONObject();
        JSONArray schema = new JSONArray();
        schema.put(new JSONObject().put("field", "value"));
        jsonObject.put("schema", schema);
        jsonObject.put("count", 42);
        
        List<Entry> entries = new ArrayList<>();
        
        queryResult.setJSON(jsonObject, entries);
        
        // Test all getters
        assertNotNull(queryResult.getResultObjects());
        assertSame(entries, queryResult.getResultObjects());
        assertEquals(42, queryResult.getCount());
        assertNotNull(queryResult.getSchema());
        assertEquals(1, queryResult.getSchema().length());
    }
}
