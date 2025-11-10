package com.contentstack.sdk;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for AssetsModel class.
 */
public class TestAssetsModel {

    @Test
    void testNoArgsConstructor() {
        // Note: AssetsModel doesn't have a public no-args constructor
        // Create via constructor with empty JSONObject instead
        JSONObject emptyResponse = new JSONObject();
        AssetsModel model = new AssetsModel(emptyResponse);
        
        assertNotNull(model);
        assertNotNull(model.objects);
        assertTrue(model.objects.isEmpty());
    }

    @Test
    void testConstructorWithNullAssets() {
        JSONObject response = new JSONObject();
        // No "assets" field - opt will return null
        
        AssetsModel model = new AssetsModel(response);
        
        assertNotNull(model);
        assertNotNull(model.objects);
        assertTrue(model.objects.isEmpty());
    }

    @Test
    void testConstructorWithInvalidAssetsTypeString() {
        JSONObject response = new JSONObject();
        response.put("assets", "not_a_list"); // String instead of List
        
        // Should throw IllegalArgumentException
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new AssetsModel(response);
        });
        
        assertEquals("Invalid type for 'assets' key. Provide assets as a List or ArrayList and try again.", 
                     exception.getMessage());
    }

    @Test
    void testConstructorWithInvalidAssetsTypeNumber() {
        JSONObject response = new JSONObject();
        response.put("assets", 12345); // Number instead of List
        
        // Should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            new AssetsModel(response);
        });
    }

    @Test
    void testConstructorWithInvalidAssetsTypeBoolean() {
        JSONObject response = new JSONObject();
        response.put("assets", true); // Boolean instead of List
        
        // Should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            new AssetsModel(response);
        });
    }

    @Test
    void testConstructorWithInvalidAssetsTypeObject() {
        JSONObject response = new JSONObject();
        JSONObject notAList = new JSONObject();
        notAList.put("key", "value");
        response.put("assets", notAList); // JSONObject instead of List
        
        // Should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            new AssetsModel(response);
        });
    }

    @Test
    void testFieldAccess() {
        JSONObject response = new JSONObject();
        AssetsModel model = new AssetsModel(response);
        
        List<Object> testList = new ArrayList<>();
        testList.add("item1");
        testList.add("item2");
        
        // Set field directly (package-private access)
        model.objects = testList;
        
        // Verify field (package-private access)
        assertEquals(testList, model.objects);
        assertEquals(2, model.objects.size());
    }

    @Test
    void testFieldAccessWithEmptyList() {
        JSONObject response = new JSONObject();
        AssetsModel model = new AssetsModel(response);
        
        List<Object> emptyList = new ArrayList<>();
        model.objects = emptyList;
        
        assertNotNull(model.objects);
        assertTrue(model.objects.isEmpty());
    }

    @Test
    void testFieldAccessWithNull() {
        JSONObject response = new JSONObject();
        AssetsModel model = new AssetsModel(response);
        
        model.objects = null;
        
        assertNull(model.objects);
    }


    @Test
    void testMultipleInvalidTypes() {
        // Test multiple invalid types to ensure error handling is thorough
        
        JSONObject response1 = new JSONObject();
        response1.put("assets", new Object());
        assertThrows(IllegalArgumentException.class, () -> new AssetsModel(response1));
        
        JSONObject response2 = new JSONObject();
        response2.put("assets", 3.14);
        assertThrows(IllegalArgumentException.class, () -> new AssetsModel(response2));
        
        JSONObject response3 = new JSONObject();
        response3.put("assets", 'c');
        assertThrows(IllegalArgumentException.class, () -> new AssetsModel(response3));
    }
    
    @Test
    void testExceptionMessageContent() {
        JSONObject response = new JSONObject();
        response.put("assets", "invalid");
        
        try {
            new AssetsModel(response);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Verify the exception is thrown with proper message
            assertNotNull(e.getMessage());
        }
    }

    @Test
    void testConstructorWithListAssets() throws Exception {
        List<JSONObject> assetsList = new ArrayList<>();
        
        JSONObject asset1 = new JSONObject();
        asset1.put("uid", "asset_1");
        asset1.put("filename", "file1.jpg");
        asset1.put("content_type", "image/jpeg");
        assetsList.add(asset1);
        
        JSONObject asset2 = new JSONObject();
        asset2.put("uid", "asset_2");
        asset2.put("filename", "file2.png");
        asset2.put("content_type", "image/png");
        assetsList.add(asset2);
        
        // Create a JSONObject and inject the List directly using reflection
        JSONObject response = new JSONObject();
        
        // Access the internal map of JSONObject using reflection
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        
        // Put the List directly into the internal map, bypassing put() method
        internalMap.put("assets", assetsList);
        
        // Now create AssetsModel - this should trigger the instanceof List path
        AssetsModel model = new AssetsModel(response);
        
        // Verify the model was created successfully
        assertNotNull(model);
        assertNotNull(model.objects);
        assertEquals(2, model.objects.size());
        
        // Verify the AssetModel objects were created
        AssetModel firstAsset = (AssetModel) model.objects.get(0);
        assertEquals("asset_1", firstAsset.uploadedUid);
        assertEquals("file1.jpg", firstAsset.fileName);
        
        AssetModel secondAsset = (AssetModel) model.objects.get(1);
        assertEquals("asset_2", secondAsset.uploadedUid);
        assertEquals("file2.png", secondAsset.fileName);
    }

    @Test
    void testConstructorWithEmptyListAssets() throws Exception {
        // Test the instanceof List path with an empty list
        
        List<JSONObject> emptyList = new ArrayList<>();
        
        JSONObject response = new JSONObject();
        
        // Use reflection to inject empty List directly
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("assets", emptyList);
        
        AssetsModel model = new AssetsModel(response);
        
        assertNotNull(model);
        assertNotNull(model.objects);
        assertTrue(model.objects.isEmpty());
    }
}

