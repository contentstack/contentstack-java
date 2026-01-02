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

    // ========== EXCEPTION HANDLING TESTS ==========

    @Test
    void testSetJSONWithInvalidGlobalFieldType() {
        GlobalFieldsModel model = new GlobalFieldsModel();
        
        // Create JSON with global_field as a String instead of LinkedHashMap
        JSONObject response = new JSONObject();
        response.put("global_field", "invalid_string_type");
        
        // Should handle exception gracefully without throwing
        assertDoesNotThrow(() -> model.setJSON(response));
        
        // Response should remain null due to instanceof check failing
        assertNull(model.getResponse());
    }

    @Test
    void testSetJSONWithInvalidGlobalFieldsType() {
        GlobalFieldsModel model = new GlobalFieldsModel();
        
        // Create JSON with global_fields as a String instead of ArrayList
        JSONObject response = new JSONObject();
        response.put("global_fields", "invalid_string_type");
        
        // Should handle exception gracefully without throwing
        assertDoesNotThrow(() -> model.setJSON(response));
        
        // Response should remain null due to instanceof check failing
        assertNull(model.getResponse());
    }

    @Test
    void testSetJSONWithNullGlobalFieldsValue() throws Exception {
        GlobalFieldsModel model = new GlobalFieldsModel();
        
        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("global_fields", null);
        
        // Should handle null gracefully
        assertDoesNotThrow(() -> model.setJSON(response));
        
        // Response should remain null
        assertNull(model.getResponse());
    }

    @Test
    void testSetJSONWithMalformedGlobalFieldMap() throws Exception {
        GlobalFieldsModel model = new GlobalFieldsModel();
        
        // Create a LinkedHashMap with circular reference or other malformed data
        // that might cause exception during JSONObject construction
        LinkedHashMap<String, Object> malformedMap = new LinkedHashMap<>();
        malformedMap.put("self", malformedMap); // Circular reference
        
        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("global_field", malformedMap);
        
        // Should handle exception gracefully without throwing
        assertDoesNotThrow(() -> model.setJSON(response));
    }

    @Test
    void testSetJSONWithGlobalFieldsCastException() throws Exception {
        GlobalFieldsModel model = new GlobalFieldsModel();
        
        // Create ArrayList with wrong generic type that will cause ClassCastException
        ArrayList<String> invalidList = new ArrayList<>();
        invalidList.add("not_a_linkedhashmap");
        invalidList.add("another_string");
        
        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("global_fields", invalidList);
        
        // Should handle ClassCastException gracefully without throwing
        assertDoesNotThrow(() -> model.setJSON(response));
        
        // Response might be set but should handle error gracefully
        // The method will catch the exception and print error message
    }

    @Test
    void testSetJSONWithMixedTypesInGlobalFieldsList() throws Exception {
        GlobalFieldsModel model = new GlobalFieldsModel();
        
        // Create ArrayList with ALL LinkedHashMaps (some empty, some with data)
        // The instanceof check in forEach handles filtering, but ClassCast happens if types are mixed
        LinkedHashMap<String, Object> validGF = new LinkedHashMap<>();
        validGF.put("uid", "valid_gf");
        validGF.put("title", "Valid Global Field");
        
        LinkedHashMap<String, Object> emptyGF = new LinkedHashMap<>();
        
        // Use ArrayList<LinkedHashMap<?, ?>> to ensure proper typing
        ArrayList<LinkedHashMap<?, ?>> validTypedList = new ArrayList<>();
        validTypedList.add(validGF);
        validTypedList.add(emptyGF);
        
        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("global_fields", validTypedList);
        
        // Should handle gracefully
        assertDoesNotThrow(() -> model.setJSON(response));
        
        // Should process valid items
        assertNotNull(model.getResponse());
        if (model.getResponse() instanceof JSONArray) {
            JSONArray result = (JSONArray) model.getResponse();
            // Should have 2 items (both LinkedHashMaps)
            assertEquals(2, result.length());
        }
    }

    @Test
    void testSetJSONWithEmptyStringInGlobalFieldsList() throws Exception {
        GlobalFieldsModel model = new GlobalFieldsModel();
        
        // ArrayList with mixed types will cause ClassCastException during cast
        ArrayList<Object> listWithInvalid = new ArrayList<>();
        listWithInvalid.add(new LinkedHashMap<>());
        listWithInvalid.add(""); // Empty string - will cause ClassCastException
        
        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("global_fields", listWithInvalid);
        
        // Should handle ClassCastException gracefully
        assertDoesNotThrow(() -> model.setJSON(response));
        
        // Response will be null due to exception being caught
        // The cast to ArrayList<LinkedHashMap<?, ?>> fails when list contains non-LinkedHashMap
    }

    @Test
    void testSetJSONWithNullInGlobalFieldsList() throws Exception {
        GlobalFieldsModel model = new GlobalFieldsModel();
        
        // ArrayList with mixed types (including null) will cause ClassCastException
        ArrayList<Object> listWithNull = new ArrayList<>();
        listWithNull.add(new LinkedHashMap<>());
        listWithNull.add(null); // Null element - will cause ClassCastException during cast
        
        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("global_fields", listWithNull);
        
        // Should handle ClassCastException gracefully
        assertDoesNotThrow(() -> model.setJSON(response));
        
        // Response will be null due to exception being caught
    }

    @Test
    void testSetJSONWithNumberInGlobalFieldsList() throws Exception {
        GlobalFieldsModel model = new GlobalFieldsModel();
        
        // ArrayList with mixed types (including numbers) will cause ClassCastException
        ArrayList<Object> listWithNumber = new ArrayList<>();
        listWithNumber.add(new LinkedHashMap<>());
        listWithNumber.add(123); // Integer - will cause ClassCastException during cast
        listWithNumber.add(45.67); // Double - will cause ClassCastException during cast
        
        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("global_fields", listWithNumber);
        
        // Should handle ClassCastException gracefully
        assertDoesNotThrow(() -> model.setJSON(response));
        
        // Response will be null due to exception being caught
    }

    @Test
    void testSetJSONWithJSONObjectInGlobalFieldsList() throws Exception {
        GlobalFieldsModel model = new GlobalFieldsModel();
        
        // ArrayList with mixed types (including JSONObject) will cause ClassCastException
        ArrayList<Object> listWithJSONObject = new ArrayList<>();
        listWithJSONObject.add(new LinkedHashMap<>());
        listWithJSONObject.add(new JSONObject().put("uid", "json_obj")); // JSONObject, not LinkedHashMap
        
        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("global_fields", listWithJSONObject);
        
        // Should handle ClassCastException gracefully
        assertDoesNotThrow(() -> model.setJSON(response));
        
        // Response will be null due to exception being caught
    }

    @Test
    void testSetJSONExceptionInSingleGlobalFieldDoesNotAffectMultiple() throws Exception {
        GlobalFieldsModel model = new GlobalFieldsModel();
        
        // Set up response with both keys
        // global_field is invalid (will be ignored due to instanceof check)
        // global_fields is valid
        LinkedHashMap<String, Object> validGF = new LinkedHashMap<>();
        validGF.put("uid", "valid_from_list");
        
        ArrayList<LinkedHashMap<?, ?>> validList = new ArrayList<>();
        validList.add(validGF);
        
        JSONObject response = new JSONObject();
        response.put("global_field", "invalid_type"); // Invalid type
        
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("global_fields", validList);
        
        assertDoesNotThrow(() -> model.setJSON(response));
        
        // Should successfully process global_fields despite global_field being invalid
        assertNotNull(model.getResponse());
        assertTrue(model.getResponse() instanceof JSONArray);
        
        JSONArray result = (JSONArray) model.getResponse();
        assertEquals(1, result.length());
    }

    @Test
    void testSetJSONWithAllInvalidItemsInGlobalFieldsList() throws Exception {
        GlobalFieldsModel model = new GlobalFieldsModel();
        
        // Create list with only invalid items - will cause ClassCastException
        ArrayList<Object> allInvalidList = new ArrayList<>();
        allInvalidList.add("string1");
        allInvalidList.add(123);
        allInvalidList.add(new JSONObject());
        allInvalidList.add(null);
        
        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("global_fields", allInvalidList);
        
        // Should handle ClassCastException gracefully
        assertDoesNotThrow(() -> model.setJSON(response));
        
        // Response will be null due to exception being caught during cast
    }

    @Test
    void testSetJSONMultipleCallsWithExceptions() throws Exception {
        GlobalFieldsModel model = new GlobalFieldsModel();
        
        // First call with invalid data
        JSONObject response1 = new JSONObject();
        response1.put("global_field", "invalid");
        response1.put("global_fields", "also_invalid");
        
        assertDoesNotThrow(() -> model.setJSON(response1));
        assertNull(model.getResponse());
        
        // Second call with valid data
        LinkedHashMap<String, Object> validGF = new LinkedHashMap<>();
        validGF.put("uid", "valid_after_error");
        
        ArrayList<LinkedHashMap<?, ?>> validList = new ArrayList<>();
        validList.add(validGF);
        
        JSONObject response2 = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response2);
        internalMap.put("global_fields", validList);
        
        assertDoesNotThrow(() -> model.setJSON(response2));
        
        // Should successfully process valid data after previous error
        assertNotNull(model.getResponse());
        assertTrue(model.getResponse() instanceof JSONArray);
    }

    @Test
    void testSetJSONWithEmptyGlobalFieldKey() {
        GlobalFieldsModel model = new GlobalFieldsModel();
        
        // Key exists but is empty string value
        JSONObject response = new JSONObject();
        response.put("global_field", "");
        
        assertDoesNotThrow(() -> model.setJSON(response));
        
        // Should not process since empty string is not LinkedHashMap
        assertNull(model.getResponse());
    }

    @Test
    void testSetJSONPreservesResultArrayOnException() throws Exception {
        GlobalFieldsModel model = new GlobalFieldsModel();
        
        // First, set valid data
        LinkedHashMap<String, Object> validGF = new LinkedHashMap<>();
        validGF.put("uid", "initial");
        
        ArrayList<LinkedHashMap<?, ?>> validList = new ArrayList<>();
        validList.add(validGF);
        
        JSONObject response1 = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap1 = (Map<String, Object>) mapField.get(response1);
        internalMap1.put("global_fields", validList);
        
        model.setJSON(response1);
        assertNotNull(model.getResponse());
        
        // Now try to set invalid data
        JSONObject response2 = new JSONObject();
        response2.put("global_field", 123); // Invalid type
        
        model.setJSON(response2);
        
        // ResultArray should remain from previous valid call
        // (Since new call doesn't update responseJSONArray when instanceof checks fail)
        assertNotNull(model.getResultArray());
    }

}
