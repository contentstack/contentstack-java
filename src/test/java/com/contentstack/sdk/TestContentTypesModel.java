package com.contentstack.sdk;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test cases for the ContentTypesModel class
 */
class TestContentTypesModel {

    @Test
    void testDefaultConstructor() {
        ContentTypesModel model = new ContentTypesModel();
        
        assertNull(model.getResponse());
        assertNotNull(model.getResultArray());
        assertEquals(0, model.getResultArray().length());
    }

    @Test
    void testSetJSONWithNull() {
        ContentTypesModel model = new ContentTypesModel();
        
        model.setJSON(null);
        
        assertNull(model.getResponse());
    }

    @Test
    void testSetJSONWithEmptyObject() {
        ContentTypesModel model = new ContentTypesModel();
        JSONObject emptyJSON = new JSONObject();
        
        model.setJSON(emptyJSON);
        
        assertNull(model.getResponse());
    }

    @Test
    void testResponseJSONArrayInitialization() {
        ContentTypesModel model = new ContentTypesModel();
        
        JSONArray initialArray = model.getResultArray();
        assertNotNull(initialArray);
        assertEquals(0, initialArray.length());
    }

    @Test
    void testSetJSONDoesNotThrow() {
        ContentTypesModel model = new ContentTypesModel();
        JSONObject json = new JSONObject();
        json.put("some_key", "some_value");
        
        assertDoesNotThrow(() -> model.setJSON(json));
    }

    @Test
    void testGetResponseReturnsNull() {
        ContentTypesModel model = new ContentTypesModel();
        assertNull(model.getResponse());
    }

    @Test
    void testGetResultArrayNeverNull() {
        ContentTypesModel model = new ContentTypesModel();
        assertNotNull(model.getResultArray());
    }

    @Test
    void testMultipleSetJSONCalls() {
        ContentTypesModel model = new ContentTypesModel();
        
        JSONObject json1 = new JSONObject();
        json1.put("key1", "value1");
        model.setJSON(json1);
        
        JSONObject json2 = new JSONObject();
        json2.put("key2", "value2");
        model.setJSON(json2);
        
        // Should not throw exception
        assertNotNull(model);
    }

    // ========== SINGLE CONTENT TYPE (LinkedHashMap) TESTS ==========

    @Test
    void testSetJSONWithSingleContentType() throws Exception {
        // Test the instanceof LinkedHashMap path
        // We use reflection to inject LinkedHashMap directly
        
        LinkedHashMap<String, Object> contentTypeMap = new LinkedHashMap<>();
        contentTypeMap.put("uid", "blog_post");
        contentTypeMap.put("title", "Blog Post");
        contentTypeMap.put("description", "A blog post content type");
        
        JSONObject response = new JSONObject();
        
        // Use reflection to inject the LinkedHashMap directly
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("content_type", contentTypeMap);
        
        ContentTypesModel model = new ContentTypesModel();
        model.setJSON(response);
        
        // Verify the response was set
        assertNotNull(model.getResponse());
        assertTrue(model.getResponse() instanceof JSONObject);
        
        JSONObject responseObj = (JSONObject) model.getResponse();
        assertEquals("blog_post", responseObj.opt("uid"));
        assertEquals("Blog Post", responseObj.opt("title"));
        assertEquals("A blog post content type", responseObj.opt("description"));
    }

    @Test
    void testSetJSONWithSingleContentTypeMinimal() throws Exception {
        LinkedHashMap<String, Object> contentTypeMap = new LinkedHashMap<>();
        contentTypeMap.put("uid", "minimal_ct");
        
        JSONObject response = new JSONObject();
        
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("content_type", contentTypeMap);
        
        ContentTypesModel model = new ContentTypesModel();
        model.setJSON(response);
        
        assertNotNull(model.getResponse());
        assertTrue(model.getResponse() instanceof JSONObject);
    }

    @Test
    void testSetJSONWithSingleContentTypeWithSchema() throws Exception {
        LinkedHashMap<String, Object> contentTypeMap = new LinkedHashMap<>();
        contentTypeMap.put("uid", "complex_ct");
        contentTypeMap.put("title", "Complex Content Type");
        
        // Add schema
        ArrayList<Object> schemaList = new ArrayList<>();
        LinkedHashMap<String, Object> field1 = new LinkedHashMap<>();
        field1.put("uid", "title");
        field1.put("data_type", "text");
        schemaList.add(field1);
        contentTypeMap.put("schema", schemaList);
        
        JSONObject response = new JSONObject();
        
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("content_type", contentTypeMap);
        
        ContentTypesModel model = new ContentTypesModel();
        model.setJSON(response);
        
        assertNotNull(model.getResponse());
        JSONObject responseObj = (JSONObject) model.getResponse();
        assertEquals("complex_ct", responseObj.opt("uid"));
    }

    // ========== MULTIPLE CONTENT TYPES (ArrayList) TESTS ==========

    @Test
    void testSetJSONWithMultipleContentTypes() throws Exception {
        // Test the instanceof ArrayList path
        
        LinkedHashMap<String, Object> ct1 = new LinkedHashMap<>();
        ct1.put("uid", "blog_post");
        ct1.put("title", "Blog Post");
        
        LinkedHashMap<String, Object> ct2 = new LinkedHashMap<>();
        ct2.put("uid", "page");
        ct2.put("title", "Page");
        
        LinkedHashMap<String, Object> ct3 = new LinkedHashMap<>();
        ct3.put("uid", "product");
        ct3.put("title", "Product");
        
        ArrayList<LinkedHashMap<String, Object>> contentTypesList = new ArrayList<>();
        contentTypesList.add(ct1);
        contentTypesList.add(ct2);
        contentTypesList.add(ct3);
        
        JSONObject response = new JSONObject();
        
        // Use reflection to inject the ArrayList directly
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("content_types", contentTypesList);
        
        ContentTypesModel model = new ContentTypesModel();
        model.setJSON(response);
        
        // Verify the response was set as JSONArray
        assertNotNull(model.getResponse());
        assertTrue(model.getResponse() instanceof JSONArray);
        
        JSONArray responseArray = (JSONArray) model.getResponse();
        assertEquals(3, responseArray.length());
        
        // Verify the responseJSONArray was also set
        assertNotNull(model.getResultArray());
        assertEquals(3, model.getResultArray().length());
        
        // Verify content of first content type
        JSONObject firstCT = responseArray.getJSONObject(0);
        assertEquals("blog_post", firstCT.opt("uid"));
        assertEquals("Blog Post", firstCT.opt("title"));
    }

    @Test
    void testSetJSONWithEmptyContentTypesList() throws Exception {
        ArrayList<LinkedHashMap<String, Object>> emptyList = new ArrayList<>();
        
        JSONObject response = new JSONObject();
        
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("content_types", emptyList);
        
        ContentTypesModel model = new ContentTypesModel();
        model.setJSON(response);
        
        // Empty list should still create an empty JSONArray
        assertNotNull(model.getResponse());
        assertTrue(model.getResponse() instanceof JSONArray);
        
        JSONArray responseArray = (JSONArray) model.getResponse();
        assertEquals(0, responseArray.length());
    }

    @Test
    void testSetJSONWithSingleItemContentTypesList() throws Exception {
        LinkedHashMap<String, Object> ct = new LinkedHashMap<>();
        ct.put("uid", "single_ct");
        ct.put("title", "Single Content Type");
        
        ArrayList<LinkedHashMap<String, Object>> singleItemList = new ArrayList<>();
        singleItemList.add(ct);
        
        JSONObject response = new JSONObject();
        
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("content_types", singleItemList);
        
        ContentTypesModel model = new ContentTypesModel();
        model.setJSON(response);
        
        assertNotNull(model.getResponse());
        assertTrue(model.getResponse() instanceof JSONArray);
        
        JSONArray responseArray = (JSONArray) model.getResponse();
        assertEquals(1, responseArray.length());
        
        JSONObject firstCT = responseArray.getJSONObject(0);
        assertEquals("single_ct", firstCT.opt("uid"));
    }

    // ========== SET CONTENT TYPE DATA TESTS ==========

    @Test
    void testSetContentTypeDataWithJSONObject() throws Exception {
        // Create a ContentTypesModel with single content type
        LinkedHashMap<String, Object> contentTypeMap = new LinkedHashMap<>();
        contentTypeMap.put("uid", "test_ct");
        contentTypeMap.put("title", "Test Content Type");
        contentTypeMap.put("description", "Test description");
        
        JSONObject response = new JSONObject();
        
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("content_type", contentTypeMap);
        
        ContentTypesModel model = new ContentTypesModel();
        model.setJSON(response);
        
        // Create a ContentType to receive the data
        Stack stack = Contentstack.stack("test_key", "test_token", "test_env");
        ContentType contentType = new ContentType("test_ct");
        contentType.stackInstance = stack;
        contentType.headers = new LinkedHashMap<>();
        
        // Call setContentTypeData
        model.setContentTypeData(contentType);
        
        // Verify the data was set on the ContentType
        assertEquals("test_ct", contentType.uid);
        assertEquals("Test Content Type", contentType.title);
        assertEquals("Test description", contentType.description);
    }

    @Test
    void testSetContentTypeDataWithNullResponse() {
        ContentTypesModel model = new ContentTypesModel();
        // response is null by default
        
        ContentType contentType = new ContentType("test_ct");
        
        // Should not throw exception
        assertDoesNotThrow(() -> model.setContentTypeData(contentType));
        
        // ContentType fields should remain null
        assertNull(contentType.title);
        assertNull(contentType.uid);
    }

    @Test
    void testSetContentTypeDataWithJSONArray() throws Exception {
        // Create a ContentTypesModel with multiple content types
        LinkedHashMap<String, Object> ct1 = new LinkedHashMap<>();
        ct1.put("uid", "ct1");
        ct1.put("title", "CT 1");
        
        ArrayList<LinkedHashMap<String, Object>> contentTypesList = new ArrayList<>();
        contentTypesList.add(ct1);
        
        JSONObject response = new JSONObject();
        
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("content_types", contentTypesList);
        
        ContentTypesModel model = new ContentTypesModel();
        model.setJSON(response);
        
        // response is JSONArray, not JSONObject
        assertTrue(model.getResponse() instanceof JSONArray);
        
        ContentType contentType = new ContentType("test_ct");
        
        // Should not throw exception (but won't set data since response is array)
        assertDoesNotThrow(() -> model.setContentTypeData(contentType));
    }

    @Test
    void testSetContentTypeDataWithCompleteData() throws Exception {
        LinkedHashMap<String, Object> contentTypeMap = new LinkedHashMap<>();
        contentTypeMap.put("uid", "complete_ct");
        contentTypeMap.put("title", "Complete Content Type");
        contentTypeMap.put("description", "Complete description");
        
        // Add schema
        ArrayList<Object> schemaList = new ArrayList<>();
        LinkedHashMap<String, Object> field = new LinkedHashMap<>();
        field.put("uid", "title_field");
        field.put("data_type", "text");
        schemaList.add(field);
        contentTypeMap.put("schema", schemaList);
        
        JSONObject response = new JSONObject();
        
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("content_type", contentTypeMap);
        
        ContentTypesModel model = new ContentTypesModel();
        model.setJSON(response);
        
        Stack stack = Contentstack.stack("test_key", "test_token", "test_env");
        ContentType contentType = new ContentType("complete_ct");
        contentType.stackInstance = stack;
        contentType.headers = new LinkedHashMap<>();
        
        model.setContentTypeData(contentType);
        
        assertEquals("complete_ct", contentType.uid);
        assertEquals("Complete Content Type", contentType.title);
        assertEquals("Complete description", contentType.description);
        assertNotNull(contentType.schema);
    }

    @Test
    void testSetContentTypeDataMultipleTimes() throws Exception {
        LinkedHashMap<String, Object> ct1Map = new LinkedHashMap<>();
        ct1Map.put("uid", "ct1");
        ct1Map.put("title", "Content Type 1");
        
        JSONObject response1 = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap1 = (Map<String, Object>) mapField.get(response1);
        internalMap1.put("content_type", ct1Map);
        
        ContentTypesModel model = new ContentTypesModel();
        model.setJSON(response1);
        
        Stack stack = Contentstack.stack("test_key", "test_token", "test_env");
        ContentType contentType = new ContentType("test");
        contentType.stackInstance = stack;
        contentType.headers = new LinkedHashMap<>();
        
        model.setContentTypeData(contentType);
        assertEquals("ct1", contentType.uid);
        
        // Set again with different data
        LinkedHashMap<String, Object> ct2Map = new LinkedHashMap<>();
        ct2Map.put("uid", "ct2");
        ct2Map.put("title", "Content Type 2");
        
        JSONObject response2 = new JSONObject();
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap2 = (Map<String, Object>) mapField.get(response2);
        internalMap2.put("content_type", ct2Map);
        
        model.setJSON(response2);
        model.setContentTypeData(contentType);
        
        // Should be updated to ct2
        assertEquals("ct2", contentType.uid);
        assertEquals("Content Type 2", contentType.title);
    }
}
