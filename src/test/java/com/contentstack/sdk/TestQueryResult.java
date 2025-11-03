package com.contentstack.sdk;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
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
}
