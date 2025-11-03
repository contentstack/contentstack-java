package com.contentstack.sdk;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

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
}
