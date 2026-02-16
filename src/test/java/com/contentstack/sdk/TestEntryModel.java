package com.contentstack.sdk;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for EntryModel class
 */
class TestEntryModel {

    // ========== BASIC CONSTRUCTOR TESTS ==========

    @Test
    void testConstructorWithBasicFields() {
        JSONObject json = new JSONObject();
        json.put("uid", "entry123");
        json.put("title", "Test Entry");
        json.put("url", "/test-entry");
        json.put("locale", "en-us");
        json.put("description", "Test description");

        EntryModel model = new EntryModel(json);

        assertNotNull(model);
        assertEquals("entry123", model.uid);
        assertEquals("Test Entry", model.title);
        assertEquals("/test-entry", model.url);
        assertEquals("en-us", model.language);
        assertEquals("en-us", model.locale);
        assertEquals("Test description", model.description);
    }

    @Test
    void testConstructorWithEntryKeyWrapper() {
        // Create the actual entry data as JSONObject
        JSONObject entryData = new JSONObject();
        entryData.put("uid", "wrapped_entry");
        entryData.put("title", "Wrapped Entry");
        entryData.put("url", "/wrapped");

        // Create response with "entry" key
        JSONObject response = new JSONObject();
        response.put("entry", entryData);

        EntryModel model = new EntryModel(response);

        assertNotNull(model);
        assertEquals("wrapped_entry", model.uid);
        assertEquals("Wrapped Entry", model.title);
        assertEquals("/wrapped", model.url);
    }

    @Test
    void testConstructorWithoutEntryKeyWrapper() {
        JSONObject json = new JSONObject();
        json.put("uid", "direct_entry");
        json.put("title", "Direct Entry");

        EntryModel model = new EntryModel(json);

        assertNotNull(model);
        assertEquals("direct_entry", model.uid);
        assertEquals("Direct Entry", model.title);
    }

    // ========== IMAGES FIELD TESTS ==========

    @Test
    void testConstructorWithImagesArray() throws Exception {
        JSONArray imagesArray = new JSONArray();
        imagesArray.put("image1.jpg");
        imagesArray.put("image2.jpg");

        JSONObject json = new JSONObject();
        json.put("uid", "entry_with_images");
        
        // Use reflection to ensure images is stored as JSONArray
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(json);
        internalMap.put("images", imagesArray);

        EntryModel model = new EntryModel(json);

        assertNotNull(model);
        assertNotNull(model.images);
        assertEquals(2, model.images.length());
        assertEquals("image1.jpg", model.images.get(0));
        assertEquals("image2.jpg", model.images.get(1));
    }

    @Test
    void testConstructorWithoutImages() {
        JSONObject json = new JSONObject();
        json.put("uid", "entry_no_images");

        EntryModel model = new EntryModel(json);

        assertNotNull(model);
        assertNull(model.images);
    }

    @Test
    void testConstructorWithImagesAsNonArray() {
        JSONObject json = new JSONObject();
        json.put("uid", "entry_invalid_images");
        json.put("images", "not_an_array"); // Invalid type

        EntryModel model = new EntryModel(json);

        assertNotNull(model);
        assertNull(model.images); // Should not be set because it's not a JSONArray
    }

    // ========== IS_DIR FIELD TESTS ==========

    @Test
    void testConstructorWithIsDirTrue() throws Exception {
        JSONObject json = new JSONObject();
        json.put("uid", "directory_entry");
        
        // Use reflection to ensure is_dir is stored as Boolean
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(json);
        internalMap.put("is_dir", Boolean.TRUE);

        EntryModel model = new EntryModel(json);

        assertNotNull(model);
        assertNotNull(model.isDirectory);
        assertTrue(model.isDirectory);
    }

    @Test
    void testConstructorWithIsDirFalse() throws Exception {
        JSONObject json = new JSONObject();
        json.put("uid", "file_entry");
        
        // Use reflection to ensure is_dir is stored as Boolean
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(json);
        internalMap.put("is_dir", Boolean.FALSE);

        EntryModel model = new EntryModel(json);

        assertNotNull(model);
        assertNotNull(model.isDirectory);
        assertFalse(model.isDirectory);
    }

    @Test
    void testConstructorWithoutIsDir() {
        JSONObject json = new JSONObject();
        json.put("uid", "entry_no_isdir");

        EntryModel model = new EntryModel(json);

        assertNotNull(model);
        assertNull(model.isDirectory);
    }

    @Test
    void testConstructorWithIsDirAsNonBoolean() {
        JSONObject json = new JSONObject();
        json.put("uid", "entry_invalid_isdir");
        json.put("is_dir", "true"); // String instead of Boolean

        EntryModel model = new EntryModel(json);

        assertNotNull(model);
        assertNull(model.isDirectory); // Should not be set because it's not a Boolean
    }

    // ========== IN_PROGRESS FIELD TESTS ==========

    @Test
    void testConstructorWithInProgressTrue() throws Exception {
        JSONObject json = new JSONObject();
        json.put("uid", "in_progress_entry");
        
        // Use reflection to ensure _in_progress is stored as Boolean
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(json);
        internalMap.put("_in_progress", Boolean.TRUE);

        EntryModel model = new EntryModel(json);

        assertNotNull(model);
        assertNotNull(model.inProgress);
        assertTrue(model.inProgress);
    }

    @Test
    void testConstructorWithInProgressFalse() throws Exception {
        JSONObject json = new JSONObject();
        json.put("uid", "completed_entry");
        
        // Use reflection to ensure _in_progress is stored as Boolean
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(json);
        internalMap.put("_in_progress", Boolean.FALSE);

        EntryModel model = new EntryModel(json);

        assertNotNull(model);
        assertNotNull(model.inProgress);
        assertFalse(model.inProgress);
    }

    @Test
    void testConstructorWithoutInProgress() {
        JSONObject json = new JSONObject();
        json.put("uid", "entry_no_progress");

        EntryModel model = new EntryModel(json);

        assertNotNull(model);
        assertNull(model.inProgress);
    }

    @Test
    void testConstructorWithInProgressAsNonBoolean() {
        JSONObject json = new JSONObject();
        json.put("uid", "entry_invalid_progress");
        json.put("_in_progress", "true"); // String instead of Boolean

        EntryModel model = new EntryModel(json);

        assertNotNull(model);
        assertNull(model.inProgress); // Should not be set because it's not a Boolean
    }

    // ========== PUBLISH DETAILS TESTS ==========

    @Test
    void testConstructorWithPublishDetails() {
        // Create publish_details as JSONObject
        JSONObject publishDetails = new JSONObject();
        publishDetails.put("environment", "production");
        publishDetails.put("time", "2024-01-01T00:00:00.000Z");
        // Test fixture: user is a non-secret publish-detail field (not a credential)
        publishDetails.put("user", "test_publisher_uid");

        JSONObject json = new JSONObject();
        json.put("uid", "published_entry");
        json.put("publish_details", publishDetails);

        EntryModel model = new EntryModel(json);

        assertNotNull(model);
        assertNotNull(model.publishDetails);
        assertEquals("production", model.environment);
        assertEquals("2024-01-01T00:00:00.000Z", model.time);
        assertEquals("test_publisher_uid", model.user);
        
        // Verify metadata is populated
        assertNotNull(model.metadata);
        assertTrue(model.metadata.containsKey("publish_details"));
        assertNotNull(model.metadata.get("publish_details"));
    }

    @Test
    void testConstructorWithEmptyPublishDetails() {
        // Create empty publish_details
        JSONObject publishDetails = new JSONObject();

        JSONObject json = new JSONObject();
        json.put("uid", "entry_empty_publish");
        json.put("publish_details", publishDetails);

        EntryModel model = new EntryModel(json);

        assertNotNull(model);
        assertNotNull(model.publishDetails);
        assertNotNull(model.metadata);
        assertTrue(model.metadata.containsKey("publish_details"));
    }

    @Test
    void testConstructorWithoutPublishDetails() {
        JSONObject json = new JSONObject();
        json.put("uid", "unpublished_entry");

        EntryModel model = new EntryModel(json);

        assertNotNull(model);
        assertNull(model.publishDetails);
        assertNull(model.metadata); // metadata is only created when parsePublishDetail is called
        assertNull(model.environment);
        assertNull(model.time);
        assertNull(model.user);
    }

    @Test
    void testConstructorWithPublishDetailsAsNonObject() {
        JSONObject json = new JSONObject();
        json.put("uid", "entry_invalid_publish");
        json.put("publish_details", "not_an_object"); // Invalid type

        EntryModel model = new EntryModel(json);

        assertNotNull(model);
        // parsePublishDetail is called but publish_details is not a JSONObject
        assertNull(model.publishDetails);
        assertNotNull(model.metadata);
        assertTrue(model.metadata.containsKey("publish_details"));
        assertNull(model.metadata.get("publish_details"));
    }

    // ========== COMPREHENSIVE TESTS ==========

    @Test
    void testConstructorWithAllFields() throws Exception {
        // Create publish_details
        JSONObject publishDetails = new JSONObject();
        publishDetails.put("environment", "staging");
        publishDetails.put("time", "2024-02-01T12:00:00.000Z");
        publishDetails.put("user", "admin");

        // Create images array
        JSONArray imagesArray = new JSONArray();
        imagesArray.put("banner.jpg");
        imagesArray.put("thumbnail.jpg");

        JSONObject json = new JSONObject();
        json.put("uid", "comprehensive_entry");
        json.put("title", "Comprehensive Entry");
        json.put("url", "/comprehensive");
        json.put("locale", "fr-fr");
        json.put("description", "Full entry with all fields");
        json.put("updated_at", "2024-01-15T10:30:00.000Z");
        json.put("updated_by", "editor");
        json.put("created_at", "2024-01-01T09:00:00.000Z");
        json.put("created_by", "creator");
        json.put("_version", 3);
        json.put("publish_details", publishDetails);
        
        // Use reflection for boolean types that need to remain as Boolean
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(json);
        internalMap.put("images", imagesArray);
        internalMap.put("is_dir", Boolean.FALSE);
        internalMap.put("_in_progress", Boolean.TRUE);

        EntryModel model = new EntryModel(json);

        // Verify all fields
        assertNotNull(model);
        assertEquals("comprehensive_entry", model.uid);
        assertEquals("Comprehensive Entry", model.title);
        assertEquals("/comprehensive", model.url);
        assertEquals("fr-fr", model.language);
        assertEquals("fr-fr", model.locale);
        assertEquals("Full entry with all fields", model.description);
        assertEquals("2024-01-15T10:30:00.000Z", model.updatedAt);
        assertEquals("editor", model.updatedBy);
        assertEquals("2024-01-01T09:00:00.000Z", model.createdAt);
        assertEquals("creator", model.createdBy);
        assertEquals(3, model.version);
        
        // Verify complex types
        assertNotNull(model.images);
        assertEquals(2, model.images.length());
        assertNotNull(model.isDirectory);
        assertFalse(model.isDirectory);
        assertNotNull(model.inProgress);
        assertTrue(model.inProgress);
        
        // Verify publish details
        assertNotNull(model.publishDetails);
        assertEquals("staging", model.environment);
        assertEquals("2024-02-01T12:00:00.000Z", model.time);
        assertEquals("admin", model.user);
        assertNotNull(model.metadata);
        assertTrue(model.metadata.containsKey("publish_details"));
    }

    @Test
    void testConstructorWithMinimalFields() {
        JSONObject json = new JSONObject();
        json.put("uid", "minimal_entry");

        EntryModel model = new EntryModel(json);

        assertNotNull(model);
        assertEquals("minimal_entry", model.uid);
        assertNull(model.title);
        assertNull(model.url);
        assertNull(model.language);
        assertNull(model.description);
        assertNull(model.images);
        assertNull(model.isDirectory);
        assertNull(model.inProgress);
        assertNull(model.publishDetails);
        assertNull(model.metadata);
    }

    @Test
    void testConstructorWithVersionField() throws Exception {
        JSONObject json = new JSONObject();
        json.put("uid", "versioned_entry");
        json.put("_version", 5);

        EntryModel model = new EntryModel(json);

        assertNotNull(model);
        assertEquals(5, model.version);
    }

    @Test
    void testConstructorWithDefaultVersion() {
        JSONObject json = new JSONObject();
        json.put("uid", "no_version_entry");

        EntryModel model = new EntryModel(json);

        assertNotNull(model);
        assertEquals(1, model.version); // Default version
    }

    @Test
    void testConstructorWithEntryKeyAndAllFields() throws Exception {
        // Create publish_details
        JSONObject publishDetails = new JSONObject();
        publishDetails.put("environment", "development");
        publishDetails.put("time", "2024-03-01T15:00:00.000Z");
        publishDetails.put("user", "dev_user");
        
        // Create images
        JSONArray imagesArray = new JSONArray();
        imagesArray.put("hero.jpg");
        
        // Create comprehensive entry data
        JSONObject entryData = new JSONObject();
        entryData.put("uid", "wrapped_comprehensive");
        entryData.put("title", "Wrapped Comprehensive");
        entryData.put("url", "/wrapped-comp");
        entryData.put("publish_details", publishDetails);
        
        // Use reflection for boolean and array types
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(entryData);
        internalMap.put("images", imagesArray);
        internalMap.put("is_dir", Boolean.TRUE);
        internalMap.put("_in_progress", Boolean.FALSE);

        // Wrap in "entry" key
        JSONObject response = new JSONObject();
        response.put("entry", entryData);

        EntryModel model = new EntryModel(response);

        // Verify all fields work with entry key wrapper
        assertNotNull(model);
        assertEquals("wrapped_comprehensive", model.uid);
        assertEquals("Wrapped Comprehensive", model.title);
        assertNotNull(model.images);
        assertEquals(1, model.images.length());
        assertNotNull(model.isDirectory);
        assertTrue(model.isDirectory);
        assertNotNull(model.inProgress);
        assertFalse(model.inProgress);
        assertNotNull(model.publishDetails);
        assertEquals("development", model.environment);
        assertNotNull(model.metadata);
    }

    @Test
    void testConstructorWithNullPublishDetailsAfterCheck() throws Exception {
        JSONObject json = new JSONObject();
        json.put("uid", "null_publish_entry");
        
        // Use reflection to set publish_details to null (after it exists)
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(json);
        internalMap.put("publish_details", null);

        EntryModel model = new EntryModel(json);

        assertNotNull(model);
        // parsePublishDetail is called, but publishDetails will be null
        assertNull(model.publishDetails);
        assertNotNull(model.metadata);
        assertNull(model.environment);
        assertNull(model.time);
        assertNull(model.user);
    }
}

