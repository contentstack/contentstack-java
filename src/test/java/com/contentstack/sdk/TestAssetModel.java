package com.contentstack.sdk;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for AssetModel class.
 */
public class TestAssetModel {

    @Test
    void testConstructorWithIsArrayTrue() {
        JSONObject response = new JSONObject();
        response.put("uid", "asset_uid_123");
        response.put("content_type", "image/jpeg");
        response.put("file_size", "2048576");
        response.put("filename", "test_image.jpg");
        response.put("url", "https://cdn.example.com/test_image.jpg");
        
        AssetModel model = new AssetModel(response, true);
        
        assertNotNull(model);
        assertEquals("asset_uid_123", model.uploadedUid);
        assertEquals("image/jpeg", model.contentType);
        assertEquals("2048576", model.fileSize);
        assertEquals("test_image.jpg", model.fileName);
        assertEquals("https://cdn.example.com/test_image.jpg", model.uploadUrl);
    }

    /**
     * Note: Testing isArray=false is challenging because the constructor expects
     * response.get("asset") to return a LinkedHashMap, but when you put a LinkedHashMap
     * into a JSONObject, the org.json library converts it to a JSONObject internally.
     * This scenario is typically exercised in integration tests with actual network responses.
     */

    @Test
    void testConstructorWithTags() {
        JSONObject response = new JSONObject();
        response.put("uid", "asset_with_tags");
        response.put("filename", "tagged_asset.jpg");
        
        JSONArray tags = new JSONArray();
        tags.put("production");
        tags.put("featured");
        tags.put("banner");
        response.put("tags", tags);
        
        AssetModel model = new AssetModel(response, true);
        
        assertNotNull(model);
        assertNotNull(model.tags);
        assertEquals(3, model.tags.length);
        assertEquals("production", model.tags[0]);
        assertEquals("featured", model.tags[1]);
        assertEquals("banner", model.tags[2]);
    }

    @Test
    void testConstructorWithEmptyTags() {
        JSONObject response = new JSONObject();
        response.put("uid", "asset_empty_tags");
        response.put("filename", "test.jpg");
        response.put("tags", new JSONArray());
        
        AssetModel model = new AssetModel(response, true);
        
        assertNotNull(model);
        // Empty tags array shouldn't set the tags field
        assertNull(model.tags);
    }

    @Test
    void testConstructorWithCount() {
        JSONObject response = new JSONObject();
        response.put("uid", "asset_with_count");
        response.put("filename", "test.jpg");
        response.put("count", 42);
        
        AssetModel model = new AssetModel(response, true);
        
        assertNotNull(model);
        assertEquals(42, model.count);
    }

    @Test
    void testConstructorWithObjects() {
        JSONObject response = new JSONObject();
        response.put("uid", "asset_with_objects");
        response.put("filename", "test.jpg");
        response.put("objects", 100);
        
        AssetModel model = new AssetModel(response, true);
        
        assertNotNull(model);
        assertEquals(100, model.totalCount);
    }

    @Test
    void testConstructorWithCountAndObjects() {
        JSONObject response = new JSONObject();
        response.put("uid", "asset_full");
        response.put("filename", "complete.jpg");
        response.put("count", 25);
        response.put("objects", 150);
        
        AssetModel model = new AssetModel(response, true);
        
        assertNotNull(model);
        assertEquals(25, model.count);
        assertEquals(150, model.totalCount);
    }

    @Test
    void testConstructorWithAllFields() {
        JSONObject response = new JSONObject();
        response.put("uid", "complete_asset");
        response.put("content_type", "video/mp4");
        response.put("file_size", "10485760");
        response.put("filename", "video.mp4");
        response.put("url", "https://cdn.example.com/video.mp4");
        response.put("count", 1);
        response.put("objects", 1);
        
        JSONArray tags = new JSONArray();
        tags.put("video");
        tags.put("tutorial");
        response.put("tags", tags);
        
        AssetModel model = new AssetModel(response, true);
        
        assertNotNull(model);
        assertEquals("complete_asset", model.uploadedUid);
        assertEquals("video/mp4", model.contentType);
        assertEquals("10485760", model.fileSize);
        assertEquals("video.mp4", model.fileName);
        assertEquals("https://cdn.example.com/video.mp4", model.uploadUrl);
        assertEquals(1, model.count);
        assertEquals(1, model.totalCount);
        assertNotNull(model.tags);
        assertEquals(2, model.tags.length);
    }

    @Test
    void testConstructorWithMinimalData() {
        JSONObject response = new JSONObject();
        response.put("uid", "minimal_asset");
        
        AssetModel model = new AssetModel(response, true);
        
        assertNotNull(model);
        assertEquals("minimal_asset", model.uploadedUid);
        assertNull(model.contentType);
        assertNull(model.fileSize);
        assertNull(model.fileName);
        assertNull(model.uploadUrl);
        assertNull(model.tags);
        assertEquals(0, model.count);
        assertEquals(0, model.totalCount);
    }

    @Test
    void testConstructorWithNonJSONArrayTags() {
        JSONObject response = new JSONObject();
        response.put("uid", "asset_string_tags");
        response.put("filename", "test.jpg");
        response.put("tags", "not_an_array"); // String instead of JSONArray
        
        AssetModel model = new AssetModel(response, true);
        
        assertNotNull(model);
        // tags should not be extracted since it's not a JSONArray
        assertNull(model.tags);
    }

    @Test
    void testConstructorWithEmptyResponse() {
        JSONObject response = new JSONObject();
        AssetModel model = new AssetModel(response, true);
        
        assertNotNull(model);
        assertNull(model.uploadedUid);
        assertNull(model.contentType);
        assertNull(model.fileSize);
        assertNull(model.fileName);
        assertNull(model.uploadUrl);
        assertNull(model.tags);
        assertNotNull(model.json);
        assertEquals(0, model.count);
        assertEquals(0, model.totalCount);
    }

    @Test
    void testFieldAccess() {
        JSONObject response = new JSONObject();
        response.put("uid", "initial_uid");
        AssetModel model = new AssetModel(response, true);
        
        // Modify fields directly (package-private access)
        model.uploadedUid = "test_uid";
        model.contentType = "image/png";
        model.fileSize = "1024";
        model.fileName = "test.png";
        model.uploadUrl = "https://example.com/test.png";
        model.count = 5;
        model.totalCount = 10;
        
        String[] testTags = {"tag1", "tag2"};
        model.tags = testTags;
        
        JSONObject testJson = new JSONObject();
        testJson.put("key", "value");
        model.json = testJson;
        
        // Verify fields (package-private access)
        assertEquals("test_uid", model.uploadedUid);
        assertEquals("image/png", model.contentType);
        assertEquals("1024", model.fileSize);
        assertEquals("test.png", model.fileName);
        assertEquals("https://example.com/test.png", model.uploadUrl);
        assertEquals(5, model.count);
        assertEquals(10, model.totalCount);
        assertArrayEquals(testTags, model.tags);
        assertEquals(testJson, model.json);
    }

    @Test
    void testExtractTagsWithSingleTag() {
        JSONObject response = new JSONObject();
        response.put("uid", "single_tag_asset");
        
        JSONArray tags = new JSONArray();
        tags.put("single");
        response.put("tags", tags);
        
        AssetModel model = new AssetModel(response, true);
        
        assertNotNull(model.tags);
        assertEquals(1, model.tags.length);
        assertEquals("single", model.tags[0]);
    }

    @Test
    void testExtractTagsWithManyTags() {
        JSONObject response = new JSONObject();
        response.put("uid", "many_tags_asset");
        
        JSONArray tags = new JSONArray();
        for (int i = 0; i < 10; i++) {
            tags.put("tag" + i);
        }
        response.put("tags", tags);
        
        AssetModel model = new AssetModel(response, true);
        
        assertNotNull(model.tags);
        assertEquals(10, model.tags.length);
        for (int i = 0; i < 10; i++) {
            assertEquals("tag" + i, model.tags[i]);
        }
    }

}

