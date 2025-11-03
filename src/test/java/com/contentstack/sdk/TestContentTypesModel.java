package com.contentstack.sdk;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

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
}
