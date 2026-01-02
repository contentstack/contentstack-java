package com.contentstack.sdk;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for EntriesModel class
 */
class TestEntriesModel {

    @Test
    void testConstructorWithArrayListEntries() throws Exception {
        // Create entry data as LinkedHashMap
        LinkedHashMap<String, Object> entry1 = new LinkedHashMap<>();
        entry1.put("uid", "entry1_uid");
        entry1.put("title", "Entry 1 Title");
        entry1.put("url", "/entry1");

        LinkedHashMap<String, Object> entry2 = new LinkedHashMap<>();
        entry2.put("uid", "entry2_uid");
        entry2.put("title", "Entry 2 Title");
        entry2.put("url", "/entry2");

        // Create ArrayList of LinkedHashMap entries
        ArrayList<LinkedHashMap<String, Object>> entriesList = new ArrayList<>();
        entriesList.add(entry1);
        entriesList.add(entry2);

        // Create JSONObject and inject ArrayList using reflection
        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("entries", entriesList);

        // Create EntriesModel
        EntriesModel model = new EntriesModel(response);

        // Verify
        assertNotNull(model);
        assertNotNull(model.objectList);
        assertEquals(2, model.objectList.size());

        // Verify first entry
        EntryModel firstEntry = (EntryModel) model.objectList.get(0);
        assertEquals("entry1_uid", firstEntry.uid);
        assertEquals("Entry 1 Title", firstEntry.title);
        assertEquals("/entry1", firstEntry.url);

        // Verify second entry
        EntryModel secondEntry = (EntryModel) model.objectList.get(1);
        assertEquals("entry2_uid", secondEntry.uid);
        assertEquals("Entry 2 Title", secondEntry.title);
        assertEquals("/entry2", secondEntry.url);
    }

    @Test
    void testConstructorWithSingleEntry() throws Exception {
        // Create single entry as LinkedHashMap
        LinkedHashMap<String, Object> entry = new LinkedHashMap<>();
        entry.put("uid", "single_entry_uid");
        entry.put("title", "Single Entry");
        entry.put("url", "/single-entry");
        entry.put("locale", "en-us");

        // Create ArrayList with single entry
        ArrayList<LinkedHashMap<String, Object>> entriesList = new ArrayList<>();
        entriesList.add(entry);

        // Create JSONObject and inject ArrayList using reflection
        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("entries", entriesList);

        // Create EntriesModel
        EntriesModel model = new EntriesModel(response);

        // Verify
        assertNotNull(model);
        assertNotNull(model.objectList);
        assertEquals(1, model.objectList.size());

        EntryModel entryModel = (EntryModel) model.objectList.get(0);
        assertEquals("single_entry_uid", entryModel.uid);
        assertEquals("Single Entry", entryModel.title);
        assertEquals("/single-entry", entryModel.url);
        assertEquals("en-us", entryModel.language);
    }

    @Test
    void testConstructorWithEmptyEntriesList() throws Exception {
        // Create empty ArrayList
        ArrayList<LinkedHashMap<String, Object>> entriesList = new ArrayList<>();

        // Create JSONObject and inject empty ArrayList using reflection
        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("entries", entriesList);

        // Create EntriesModel
        EntriesModel model = new EntriesModel(response);

        // Verify - should have empty objectList
        assertNotNull(model);
        assertNotNull(model.objectList);
        assertEquals(0, model.objectList.size());
    }

    @Test
    void testConstructorWithNullEntries() {
        // Create JSONObject without "entries" key
        JSONObject response = new JSONObject();

        // Create EntriesModel
        EntriesModel model = new EntriesModel(response);

        // Verify - should have empty objectList
        assertNotNull(model);
        assertNotNull(model.objectList);
        assertEquals(0, model.objectList.size());
    }

    @Test
    void testConstructorWithNonArrayListEntries() {
        // Create JSONObject with entries as a string (invalid type)
        JSONObject response = new JSONObject();
        response.put("entries", "not_an_arraylist");

        // Create EntriesModel - should handle gracefully
        EntriesModel model = new EntriesModel(response);

        // Verify - should have empty objectList
        assertNotNull(model);
        assertNotNull(model.objectList);
        assertEquals(0, model.objectList.size());
    }

    @Test
    void testConstructorWithComplexEntryData() throws Exception {
        // Create entry with nested data
        LinkedHashMap<String, Object> entry = new LinkedHashMap<>();
        entry.put("uid", "complex_entry_uid");
        entry.put("title", "Complex Entry");
        entry.put("url", "/complex-entry");
        entry.put("locale", "en-us");
        entry.put("description", "Complex entry description");
        entry.put("_version", 2);

        // Create ArrayList with entry
        ArrayList<LinkedHashMap<String, Object>> entriesList = new ArrayList<>();
        entriesList.add(entry);

        // Create JSONObject and inject ArrayList using reflection
        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("entries", entriesList);

        // Create EntriesModel
        EntriesModel model = new EntriesModel(response);

        // Verify
        assertNotNull(model);
        assertNotNull(model.objectList);
        assertEquals(1, model.objectList.size());

        EntryModel entryModel = (EntryModel) model.objectList.get(0);
        assertEquals("complex_entry_uid", entryModel.uid);
        assertEquals("Complex Entry", entryModel.title);
        assertEquals("/complex-entry", entryModel.url);
        assertEquals("en-us", entryModel.language);
        assertEquals("Complex entry description", entryModel.description);
        assertEquals(2, entryModel.version);
    }

    @Test
    void testConstructorWithMultipleEntries() throws Exception {
        // Create multiple entries
        ArrayList<LinkedHashMap<String, Object>> entriesList = new ArrayList<>();
        
        for (int i = 1; i <= 5; i++) {
            LinkedHashMap<String, Object> entry = new LinkedHashMap<>();
            entry.put("uid", "entry_" + i);
            entry.put("title", "Entry " + i);
            entry.put("url", "/entry-" + i);
            entriesList.add(entry);
        }

        // Create JSONObject and inject ArrayList using reflection
        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("entries", entriesList);

        // Create EntriesModel
        EntriesModel model = new EntriesModel(response);

        // Verify
        assertNotNull(model);
        assertNotNull(model.objectList);
        assertEquals(5, model.objectList.size());

        // Verify each entry
        for (int i = 0; i < 5; i++) {
            EntryModel entryModel = (EntryModel) model.objectList.get(i);
            assertEquals("entry_" + (i + 1), entryModel.uid);
            assertEquals("Entry " + (i + 1), entryModel.title);
        }
    }

    @Test
    void testConstructorWithMixedValidAndInvalidEntries() throws Exception {
        // Create ArrayList with mixed types
        ArrayList<Object> entriesList = new ArrayList<>();
        
        // Add valid LinkedHashMap entry
        LinkedHashMap<String, Object> validEntry = new LinkedHashMap<>();
        validEntry.put("uid", "valid_entry");
        validEntry.put("title", "Valid Entry");
        entriesList.add(validEntry);
        
        // Add invalid entry (String instead of LinkedHashMap)
        entriesList.add("invalid_entry");

        // Create JSONObject and inject ArrayList using reflection
        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("entries", entriesList);

        // Create EntriesModel - should only process valid entry
        EntriesModel model = new EntriesModel(response);

        // Verify - should have only 1 entry (the valid one)
        assertNotNull(model);
        assertNotNull(model.objectList);
        assertEquals(1, model.objectList.size());

        EntryModel entryModel = (EntryModel) model.objectList.get(0);
        assertEquals("valid_entry", entryModel.uid);
        assertEquals("Valid Entry", entryModel.title);
    }

    @Test
    void testConstructorWithExceptionHandling() {
        // Create a malformed JSONObject that might cause exceptions
        JSONObject response = new JSONObject();
        response.put("entries", new Object()); // Invalid type

        // Should not throw exception, should handle gracefully
        assertDoesNotThrow(() -> {
            EntriesModel model = new EntriesModel(response);
            assertNotNull(model);
            assertNotNull(model.objectList);
        });
    }

    @Test
    void testJsonObjectField() throws Exception {
        // Create entry data
        LinkedHashMap<String, Object> entry = new LinkedHashMap<>();
        entry.put("uid", "test_entry");
        entry.put("title", "Test Entry");

        ArrayList<LinkedHashMap<String, Object>> entriesList = new ArrayList<>();
        entriesList.add(entry);

        // Create JSONObject and inject ArrayList using reflection
        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("entries", entriesList);

        // Create EntriesModel
        EntriesModel model = new EntriesModel(response);

        // Verify jsonObject field is set
        assertNotNull(model.jsonObject);
        assertTrue(model.jsonObject.has("entries"));
    }

    @Test
    void testConstructorWithEntryContainingAllFields() throws Exception {
        // Create comprehensive entry with all possible fields
        LinkedHashMap<String, Object> entry = new LinkedHashMap<>();
        entry.put("uid", "comprehensive_entry");
        entry.put("title", "Comprehensive Entry");
        entry.put("url", "/comprehensive");
        entry.put("locale", "en-us");
        entry.put("_version", 1);
        entry.put("created_at", "2024-01-01T00:00:00.000Z");
        entry.put("updated_at", "2024-01-02T00:00:00.000Z");

        ArrayList<LinkedHashMap<String, Object>> entriesList = new ArrayList<>();
        entriesList.add(entry);

        // Create JSONObject and inject ArrayList using reflection
        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("entries", entriesList);

        // Create EntriesModel
        EntriesModel model = new EntriesModel(response);

        // Verify
        assertNotNull(model);
        assertNotNull(model.objectList);
        assertEquals(1, model.objectList.size());

        EntryModel entryModel = (EntryModel) model.objectList.get(0);
        assertEquals("comprehensive_entry", entryModel.uid);
        assertEquals("Comprehensive Entry", entryModel.title);
        assertEquals("/comprehensive", entryModel.url);
        assertEquals("en-us", entryModel.language);
    }
}

