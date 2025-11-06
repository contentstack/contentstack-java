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
 * Comprehensive test cases for the GlobalFieldsModel class
 */
class TestGlobalFieldsModel {

    @Test
    void testDefaultState() {
        GlobalFieldsModel model = new GlobalFieldsModel();
        
        assertNull(model.getResponse());
        assertNotNull(model.getResultArray());
        assertEquals(0, model.getResultArray().length());
    }

    @Test
    void testSetJSONWithNull() {
        GlobalFieldsModel model = new GlobalFieldsModel();
        
        model.setJSON(null);
        
        assertNull(model.getResponse());
    }

    @Test
    void testSetJSONWithEmptyObject() {
        GlobalFieldsModel model = new GlobalFieldsModel();
        JSONObject emptyJSON = new JSONObject();
        
        model.setJSON(emptyJSON);
        
        assertNull(model.getResponse());
    }

    @Test
    void testSetJSONDoesNotThrow() {
        GlobalFieldsModel model = new GlobalFieldsModel();
        JSONObject json = new JSONObject();
        json.put("some_key", "some_value");
        
        assertDoesNotThrow(() -> model.setJSON(json));
    }

    @Test
    void testGetResponse() {
        GlobalFieldsModel model = new GlobalFieldsModel();
        assertNull(model.getResponse());
    }

    @Test
    void testGetResultArray() {
        GlobalFieldsModel model = new GlobalFieldsModel();
        
        JSONArray resultArray = model.getResultArray();
        assertNotNull(resultArray);
        assertEquals(0, resultArray.length());
    }

    @Test
    void testMultipleSetJSONCalls() {
        GlobalFieldsModel model = new GlobalFieldsModel();
        
        JSONObject json1 = new JSONObject();
        json1.put("key1", "value1");
        model.setJSON(json1);
        
        JSONObject json2 = new JSONObject();
        json2.put("key2", "value2");
        model.setJSON(json2);
        
        // Should not throw exception
        assertNotNull(model);
    }

    // ========== SINGLE GLOBAL FIELD TESTS (global_field key) ==========

    @Test
    void testSetJSONWithSingleGlobalField() throws Exception {
        LinkedHashMap<String, Object> globalFieldMap = new LinkedHashMap<>();
        globalFieldMap.put("uid", "seo_metadata");
        globalFieldMap.put("title", "SEO Metadata");
        globalFieldMap.put("description", "SEO related fields");

        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("global_field", globalFieldMap);

        GlobalFieldsModel model = new GlobalFieldsModel();
        model.setJSON(response);

        assertNotNull(model.getResponse());
        assertTrue(model.getResponse() instanceof JSONObject);

        JSONObject responseObj = (JSONObject) model.getResponse();
        assertEquals("seo_metadata", responseObj.opt("uid"));
        assertEquals("SEO Metadata", responseObj.opt("title"));
        assertEquals("SEO related fields", responseObj.opt("description"));
    }

    @Test
    void testSetJSONWithSingleGlobalFieldMinimal() throws Exception {
        LinkedHashMap<String, Object> globalFieldMap = new LinkedHashMap<>();
        globalFieldMap.put("uid", "minimal_gf");

        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("global_field", globalFieldMap);

        GlobalFieldsModel model = new GlobalFieldsModel();
        model.setJSON(response);

        assertNotNull(model.getResponse());
        assertTrue(model.getResponse() instanceof JSONObject);

        JSONObject responseObj = (JSONObject) model.getResponse();
        assertEquals("minimal_gf", responseObj.opt("uid"));
    }

    @Test
    void testSetJSONWithSingleGlobalFieldEmptyMap() throws Exception {
        LinkedHashMap<String, Object> globalFieldMap = new LinkedHashMap<>();

        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("global_field", globalFieldMap);

        GlobalFieldsModel model = new GlobalFieldsModel();
        model.setJSON(response);

        assertNotNull(model.getResponse());
        assertTrue(model.getResponse() instanceof JSONObject);
    }

    // ========== MULTIPLE GLOBAL FIELDS TESTS (global_fields key) ==========

    @Test
    void testSetJSONWithMultipleGlobalFields() throws Exception {
        LinkedHashMap<String, Object> gf1 = new LinkedHashMap<>();
        gf1.put("uid", "seo_metadata");
        gf1.put("title", "SEO Metadata");

        LinkedHashMap<String, Object> gf2 = new LinkedHashMap<>();
        gf2.put("uid", "author_info");
        gf2.put("title", "Author Information");

        ArrayList<LinkedHashMap<?, ?>> globalFieldsList = new ArrayList<>();
        globalFieldsList.add(gf1);
        globalFieldsList.add(gf2);

        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("global_fields", globalFieldsList);

        GlobalFieldsModel model = new GlobalFieldsModel();
        model.setJSON(response);

        assertNotNull(model.getResponse());
        assertTrue(model.getResponse() instanceof JSONArray);

        JSONArray responseArray = (JSONArray) model.getResponse();
        assertEquals(2, responseArray.length());

        JSONObject firstGF = (JSONObject) responseArray.get(0);
        assertEquals("seo_metadata", firstGF.opt("uid"));
        assertEquals("SEO Metadata", firstGF.opt("title"));

        JSONObject secondGF = (JSONObject) responseArray.get(1);
        assertEquals("author_info", secondGF.opt("uid"));
        assertEquals("Author Information", secondGF.opt("title"));
    }

    @Test
    void testSetJSONWithSingleGlobalFieldInList() throws Exception {
        LinkedHashMap<String, Object> gf1 = new LinkedHashMap<>();
        gf1.put("uid", "single_gf");
        gf1.put("title", "Single Global Field");

        ArrayList<LinkedHashMap<?, ?>> globalFieldsList = new ArrayList<>();
        globalFieldsList.add(gf1);

        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("global_fields", globalFieldsList);

        GlobalFieldsModel model = new GlobalFieldsModel();
        model.setJSON(response);

        assertNotNull(model.getResponse());
        assertTrue(model.getResponse() instanceof JSONArray);

        JSONArray responseArray = (JSONArray) model.getResponse();
        assertEquals(1, responseArray.length());

        JSONObject firstGF = (JSONObject) responseArray.get(0);
        assertEquals("single_gf", firstGF.opt("uid"));
        assertEquals("Single Global Field", firstGF.opt("title"));
    }

    @Test
    void testSetJSONWithEmptyGlobalFieldsList() throws Exception {
        ArrayList<LinkedHashMap<?, ?>> globalFieldsList = new ArrayList<>();

        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("global_fields", globalFieldsList);

        GlobalFieldsModel model = new GlobalFieldsModel();
        model.setJSON(response);

        assertNotNull(model.getResponse());
        assertTrue(model.getResponse() instanceof JSONArray);

        JSONArray responseArray = (JSONArray) model.getResponse();
        assertEquals(0, responseArray.length());
    }

    @Test
    void testSetJSONWithManyGlobalFields() throws Exception {
        ArrayList<LinkedHashMap<?, ?>> globalFieldsList = new ArrayList<>();
        
        for (int i = 0; i < 5; i++) {
            LinkedHashMap<String, Object> gf = new LinkedHashMap<>();
            gf.put("uid", "global_field_" + i);
            gf.put("title", "Global Field " + i);
            globalFieldsList.add(gf);
        }

        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("global_fields", globalFieldsList);

        GlobalFieldsModel model = new GlobalFieldsModel();
        model.setJSON(response);

        assertNotNull(model.getResponse());
        assertTrue(model.getResponse() instanceof JSONArray);

        JSONArray responseArray = (JSONArray) model.getResponse();
        assertEquals(5, responseArray.length());

        for (int i = 0; i < 5; i++) {
            JSONObject gf = (JSONObject) responseArray.get(i);
            assertEquals("global_field_" + i, gf.opt("uid"));
            assertEquals("Global Field " + i, gf.opt("title"));
        }
    }

    // ========== GET RESULT ARRAY TESTS ==========

    @Test
    void testGetResultArrayWithMultipleGlobalFields() throws Exception {
        LinkedHashMap<String, Object> gf1 = new LinkedHashMap<>();
        gf1.put("uid", "gf_1");

        LinkedHashMap<String, Object> gf2 = new LinkedHashMap<>();
        gf2.put("uid", "gf_2");

        ArrayList<LinkedHashMap<?, ?>> globalFieldsList = new ArrayList<>();
        globalFieldsList.add(gf1);
        globalFieldsList.add(gf2);

        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("global_fields", globalFieldsList);

        GlobalFieldsModel model = new GlobalFieldsModel();
        model.setJSON(response);

        JSONArray resultArray = model.getResultArray();
        assertNotNull(resultArray);
        assertEquals(2, resultArray.length());

        JSONObject firstGF = (JSONObject) resultArray.get(0);
        assertEquals("gf_1", firstGF.opt("uid"));
    }

    @Test
    void testGetResultArrayWithEmptyList() throws Exception {
        ArrayList<LinkedHashMap<?, ?>> globalFieldsList = new ArrayList<>();

        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("global_fields", globalFieldsList);

        GlobalFieldsModel model = new GlobalFieldsModel();
        model.setJSON(response);

        JSONArray resultArray = model.getResultArray();
        assertNotNull(resultArray);
        assertEquals(0, resultArray.length());
    }

    // ========== EDGE CASE TESTS ==========

    @Test
    void testSetJSONWithBothSingleAndMultiple() throws Exception {
        // Test when both keys are present - should process both
        LinkedHashMap<String, Object> singleGF = new LinkedHashMap<>();
        singleGF.put("uid", "single");

        LinkedHashMap<String, Object> multiGF = new LinkedHashMap<>();
        multiGF.put("uid", "multi");

        ArrayList<LinkedHashMap<?, ?>> globalFieldsList = new ArrayList<>();
        globalFieldsList.add(multiGF);

        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("global_field", singleGF);
        internalMap.put("global_fields", globalFieldsList);

        GlobalFieldsModel model = new GlobalFieldsModel();
        model.setJSON(response);

        // When both are present, global_fields overwrites the response
        assertNotNull(model.getResponse());
        assertTrue(model.getResponse() instanceof JSONArray);
    }

    @Test
    void testSetJSONWithComplexGlobalField() throws Exception {
        LinkedHashMap<String, Object> globalFieldMap = new LinkedHashMap<>();
        globalFieldMap.put("uid", "complex_gf");
        globalFieldMap.put("title", "Complex Global Field");
        globalFieldMap.put("schema", new LinkedHashMap<>());
        globalFieldMap.put("version", 2);

        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("global_field", globalFieldMap);

        GlobalFieldsModel model = new GlobalFieldsModel();
        model.setJSON(response);

        assertNotNull(model.getResponse());
        assertTrue(model.getResponse() instanceof JSONObject);

        JSONObject responseObj = (JSONObject) model.getResponse();
        assertEquals("complex_gf", responseObj.opt("uid"));
        assertEquals(2, responseObj.opt("version"));
    }

    @Test
    void testSetJSONWithNullGlobalFieldValue() throws Exception {
        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("global_field", null);

        GlobalFieldsModel model = new GlobalFieldsModel();
        model.setJSON(response);

        // Should not crash, response should remain null
        assertNull(model.getResponse());
    }

    @Test
    void testSetJSONMultipleTimes() throws Exception {
        GlobalFieldsModel model = new GlobalFieldsModel();

        // First call with single global field
        LinkedHashMap<String, Object> singleGF = new LinkedHashMap<>();
        singleGF.put("uid", "first");

        JSONObject response1 = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap1 = (Map<String, Object>) mapField.get(response1);
        internalMap1.put("global_field", singleGF);

        model.setJSON(response1);
        assertNotNull(model.getResponse());
        assertTrue(model.getResponse() instanceof JSONObject);

        // Second call with multiple global fields
        LinkedHashMap<String, Object> multiGF = new LinkedHashMap<>();
        multiGF.put("uid", "second");

        ArrayList<LinkedHashMap<?, ?>> globalFieldsList = new ArrayList<>();
        globalFieldsList.add(multiGF);

        JSONObject response2 = new JSONObject();
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap2 = (Map<String, Object>) mapField.get(response2);
        internalMap2.put("global_fields", globalFieldsList);

        model.setJSON(response2);
        assertNotNull(model.getResponse());
        assertTrue(model.getResponse() instanceof JSONArray);
    }

}
