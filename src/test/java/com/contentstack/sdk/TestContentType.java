package com.contentstack.sdk;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.LinkedHashMap;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for ContentType class.
 * Tests content type operations, entry/query creation, and configurations.
 */
public class TestContentType {

    private ContentType contentType;
    private final String contentTypeUid = "test_content_type";

    @BeforeEach
    void setUp() {
        contentType = new ContentType(contentTypeUid);
        contentType.headers = new LinkedHashMap<>();
    }

    // ========== CONSTRUCTOR TESTS ==========

    @Test
    void testContentTypeConstructor() {
        ContentType ct = new ContentType("blog_post");
        assertNotNull(ct);
        assertEquals("blog_post", ct.contentTypeUid);
    }

    @Test
    void testContentTypeDirectInstantiationThrows() {
        assertThrows(IllegalAccessException.class, () -> {
            new ContentType();
        });
    }

    @Test
    void testGetContentTypeUid() {
        assertEquals(contentTypeUid, contentType.contentTypeUid);
    }

    // ========== HEADER TESTS ==========

    @Test
    void testSetHeader() {
        contentType.setHeader("custom-header", "custom-value");
        assertTrue(contentType.headers.containsKey("custom-header"));
        assertEquals("custom-value", contentType.headers.get("custom-header"));
    }

    @Test
    void testSetMultipleHeaders() {
        contentType.setHeader("header1", "value1");
        contentType.setHeader("header2", "value2");
        contentType.setHeader("header3", "value3");
        
        assertEquals(3, contentType.headers.size());
        assertEquals("value1", contentType.headers.get("header1"));
        assertEquals("value2", contentType.headers.get("header2"));
        assertEquals("value3", contentType.headers.get("header3"));
    }

    @Test
    void testSetHeaderWithEmptyKey() {
        contentType.setHeader("", "value");
        assertFalse(contentType.headers.containsKey(""));
    }

    @Test
    void testSetHeaderWithEmptyValue() {
        contentType.setHeader("key", "");
        assertFalse(contentType.headers.containsKey("key"));
    }

    @Test
    void testRemoveHeader() {
        contentType.setHeader("temp-header", "temp-value");
        assertTrue(contentType.headers.containsKey("temp-header"));
        
        contentType.removeHeader("temp-header");
        assertFalse(contentType.headers.containsKey("temp-header"));
    }

    @Test
    void testRemoveNonExistentHeader() {
        contentType.removeHeader("non-existent");
        assertNotNull(contentType.headers);
    }

    @Test
    void testRemoveHeaderWithEmptyKey() {
        contentType.removeHeader("");
        assertNotNull(contentType.headers);
    }

    // ========== ENTRY CREATION TESTS ==========

    @Test
    void testEntryWithUid() {
        Entry entry = contentType.entry("entry_uid_123");
        assertNotNull(entry);
        assertEquals("entry_uid_123", entry.getUid());
        assertEquals(contentTypeUid, entry.getContentType());
    }

    @Test
    void testEntryWithEmptyUid() {
        Entry entry = contentType.entry("");
        assertNotNull(entry);
        assertEquals("", entry.getUid());
    }

    @Test
    void testMultipleEntriesCreation() {
        Entry entry1 = contentType.entry("entry1");
        Entry entry2 = contentType.entry("entry2");
        Entry entry3 = contentType.entry("entry3");
        
        assertNotNull(entry1);
        assertNotNull(entry2);
        assertNotNull(entry3);
        assertEquals("entry1", entry1.getUid());
        assertEquals("entry2", entry2.getUid());
        assertEquals("entry3", entry3.getUid());
    }

    // ========== QUERY CREATION TESTS ==========

    @Test
    void testQuery() {
        Query query = contentType.query();
        assertNotNull(query);
        assertEquals(contentTypeUid, query.getContentType());
    }

    @Test
    void testMultipleQueriesCreation() {
        Query query1 = contentType.query();
        Query query2 = contentType.query();
        Query query3 = contentType.query();
        
        assertNotNull(query1);
        assertNotNull(query2);
        assertNotNull(query3);
    }

    // ========== SET CONTENT TYPE DATA TESTS ==========

    @Test
    void testSetContentTypeDataWithCompleteJson() {
        JSONObject ctData = new JSONObject();
        ctData.put("title", "Blog Post");
        ctData.put("description", "A blog post content type");
        ctData.put("uid", "blog_post");
        
        JSONArray schema = new JSONArray();
        JSONObject field = new JSONObject();
        field.put("uid", "title");
        field.put("data_type", "text");
        schema.put(field);
        ctData.put("schema", schema);
        
        contentType.setContentTypeData(ctData);
        
        assertEquals("Blog Post", contentType.title);
        assertEquals("A blog post content type", contentType.description);
        assertEquals("blog_post", contentType.uid);
        assertNotNull(contentType.schema);
        assertEquals(1, contentType.schema.length());
        assertNotNull(contentType.contentTypeData);
    }

    @Test
    void testSetContentTypeDataWithMinimalJson() {
        JSONObject ctData = new JSONObject();
        ctData.put("uid", "minimal_ct");
        
        contentType.setContentTypeData(ctData);
        
        assertEquals("minimal_ct", contentType.uid);
    }

    @Test
    void testSetContentTypeDataWithNull() {
        contentType.setContentTypeData(null);
        assertNull(contentType.title);
    }

    @Test
    void testSetContentTypeDataWithEmptyJson() {
        JSONObject ctData = new JSONObject();
        contentType.setContentTypeData(ctData);
        
        assertNotNull(contentType.contentTypeData);
    }

    @Test
    void testSetContentTypeDataOverwrite() {
        JSONObject ctData1 = new JSONObject();
        ctData1.put("title", "First Title");
        contentType.setContentTypeData(ctData1);
        assertEquals("First Title", contentType.title);
        
        JSONObject ctData2 = new JSONObject();
        ctData2.put("title", "Second Title");
        contentType.setContentTypeData(ctData2);
        assertEquals("Second Title", contentType.title);
    }

    // ========== FIELD ACCESS TESTS ==========

    @Test
    void testGetTitle() {
        assertNull(contentType.title);
    }

    @Test
    void testGetDescription() {
        assertNull(contentType.description);
    }

    @Test
    void testGetUid() {
        assertNull(contentType.uid);
    }

    @Test
    void testGetSchema() {
        assertNull(contentType.schema);
    }

    @Test
    void testGetContentTypeData() {
        assertNull(contentType.contentTypeData);
    }

    @Test
    void testSetTitle() {
        contentType.title = "Test Title";
        assertEquals("Test Title", contentType.title);
    }

    @Test
    void testSetDescription() {
        contentType.description = "Test Description";
        assertEquals("Test Description", contentType.description);
    }

    @Test
    void testSetUid() {
        contentType.uid = "test_uid";
        assertEquals("test_uid", contentType.uid);
    }

    @Test
    void testSetSchema() {
        JSONArray schema = new JSONArray();
        schema.put(new JSONObject().put("field", "value"));
        contentType.schema = schema;
        assertEquals(1, contentType.schema.length());
    }

    // ========== EDGE CASE TESTS ==========

    @Test
    void testHeadersInitialization() {
        ContentType ct = new ContentType("test");
        ct.headers = new LinkedHashMap<>();
        assertNotNull(ct.headers);
        assertEquals(0, ct.headers.size());
    }

    @Test
    void testHeaderOverwrite() {
        contentType.setHeader("key", "value1");
        assertEquals("value1", contentType.headers.get("key"));
        
        contentType.setHeader("key", "value2");
        assertEquals("value2", contentType.headers.get("key"));
    }

    @Test
    void testRemoveAndAddSameHeader() {
        contentType.setHeader("key", "value1");
        contentType.removeHeader("key");
        assertFalse(contentType.headers.containsKey("key"));
        
        contentType.setHeader("key", "value2");
        assertEquals("value2", contentType.headers.get("key"));
    }

    @Test
    void testEntryInheritsHeaders() {
        contentType.setHeader("custom-header", "custom-value");
        Entry entry = contentType.entry("test_entry");
        
        assertNotNull(entry.headers);
        assertTrue(entry.headers.containsKey("custom-header"));
    }

    @Test
    void testQueryInheritsHeaders() {
        contentType.setHeader("custom-header", "custom-value");
        Query query = contentType.query();
        
        assertNotNull(query.headers);
        assertTrue(query.headers.containsKey("custom-header"));
    }

    @Test
    void testContentTypeUidPreservation() {
        String originalUid = "original_uid";
        ContentType ct = new ContentType(originalUid);
        
        ct.headers = new LinkedHashMap<>();
        ct.setHeader("key", "value");
        ct.entry("entry1");
        ct.query();
        
        assertEquals(originalUid, ct.contentTypeUid);
    }

    @Test
    void testSetContentTypeDataWithComplexSchema() {
        JSONObject ctData = new JSONObject();
        ctData.put("title", "Complex Content Type");
        ctData.put("uid", "complex_ct");
        
        JSONArray schema = new JSONArray();
        
        JSONObject field1 = new JSONObject();
        field1.put("uid", "title");
        field1.put("data_type", "text");
        field1.put("mandatory", true);
        schema.put(field1);
        
        JSONObject field2 = new JSONObject();
        field2.put("uid", "description");
        field2.put("data_type", "text");
        field2.put("mandatory", false);
        schema.put(field2);
        
        JSONObject field3 = new JSONObject();
        field3.put("uid", "image");
        field3.put("data_type", "file");
        schema.put(field3);
        
        ctData.put("schema", schema);
        
        contentType.setContentTypeData(ctData);
        
        assertEquals("Complex Content Type", contentType.title);
        assertEquals("complex_ct", contentType.uid);
        assertNotNull(contentType.schema);
        assertEquals(3, contentType.schema.length());
    }

    @Test
    void testSetNullValues() {
        contentType.title = null;
        contentType.description = null;
        contentType.uid = null;
        contentType.schema = null;
        
        assertNull(contentType.title);
        assertNull(contentType.description);
        assertNull(contentType.uid);
        assertNull(contentType.schema);
    }

    @Test
    void testSetEmptyValues() {
        contentType.title = "";
        contentType.description = "";
        contentType.uid = "";
        
        assertEquals("", contentType.title);
        assertEquals("", contentType.description);
        assertEquals("", contentType.uid);
    }

    @Test
    void testMultipleSetContentTypeDataCalls() {
        JSONObject ctData1 = new JSONObject();
        ctData1.put("title", "Title 1");
        ctData1.put("uid", "uid1");
        contentType.setContentTypeData(ctData1);
        
        assertEquals("Title 1", contentType.title);
        assertEquals("uid1", contentType.uid);
        
        JSONObject ctData2 = new JSONObject();
        ctData2.put("title", "Title 2");
        ctData2.put("uid", "uid2");
        contentType.setContentTypeData(ctData2);
        
        assertEquals("Title 2", contentType.title);
        assertEquals("uid2", contentType.uid);
    }

    // ========== PROTECTED ENTRY() METHOD TESTS ==========

    @Test
    void testEntryWithoutUid() throws Exception {
        // Set up a stack instance for the content type
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        contentType.stackInstance = stack;
        contentType.headers = new LinkedHashMap<>();
        contentType.headers.put("environment", "production");
        
        // Call protected entry() method using reflection
        java.lang.reflect.Method entryMethod = ContentType.class.getDeclaredMethod("entry");
        entryMethod.setAccessible(true);
        Entry entry = (Entry) entryMethod.invoke(contentType);
        
        assertNotNull(entry);
        assertEquals(contentTypeUid, entry.getContentType());
        assertNotNull(entry.headers);
        assertTrue(entry.headers.containsKey("environment"));
    }

    @Test
    void testEntryWithoutUidInheritsHeaders() throws Exception {
        // Set up stack and headers
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        contentType.stackInstance = stack;
        contentType.headers = new LinkedHashMap<>();
        contentType.headers.put("custom-header", "custom-value");
        contentType.headers.put("environment", "staging");
        
        // Call protected entry() method using reflection
        java.lang.reflect.Method entryMethod = ContentType.class.getDeclaredMethod("entry");
        entryMethod.setAccessible(true);
        Entry entry = (Entry) entryMethod.invoke(contentType);
        
        assertNotNull(entry);
        assertNotNull(entry.headers);
        assertTrue(entry.headers.containsKey("custom-header"));
        assertEquals("custom-value", entry.headers.get("custom-header"));
        assertEquals("staging", entry.headers.get("environment"));
    }

    // ========== FETCH METHOD TESTS ==========

    @Test
    void testFetchWithValidParameters() throws Exception {
        // Set up stack instance
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        contentType.stackInstance = stack;
        contentType.headers = new LinkedHashMap<>();
        contentType.headers.put("environment", "production");
        
        JSONObject params = new JSONObject();
        params.put("include_schema", true);
        params.put("include_count", true);
        
        ContentTypesCallback callback = new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel model, Error error) {
                // Callback for testing - won't be called in unit test
            }
        };
        
        // This will create a CSBackgroundTask but won't execute in unit tests
        assertDoesNotThrow(() -> contentType.fetch(params, callback));
        
        // Verify environment was added to params
        assertTrue(params.has("environment"));
        assertEquals("production", params.get("environment"));
    }

    @Test
    void testFetchWithEmptyParams() throws Exception {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        contentType.stackInstance = stack;
        contentType.headers = new LinkedHashMap<>();
        contentType.headers.put("environment", "development");
        
        JSONObject emptyParams = new JSONObject();
        
        ContentTypesCallback callback = new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel model, Error error) {
                // Callback for testing
            }
        };
        
        assertDoesNotThrow(() -> contentType.fetch(emptyParams, callback));
        
        // Environment should be added even to empty params
        assertTrue(emptyParams.has("environment"));
        assertEquals("development", emptyParams.get("environment"));
    }

    @Test
    void testFetchWithMultipleParams() throws Exception {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        contentType.stackInstance = stack;
        contentType.headers = new LinkedHashMap<>();
        contentType.headers.put("environment", "staging");
        
        JSONObject params = new JSONObject();
        params.put("include_schema", true);
        params.put("include_count", true);
        params.put("version", 1);
        params.put("locale", "en-us");
        
        ContentTypesCallback callback = new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel model, Error error) {}
        };
        
        assertDoesNotThrow(() -> contentType.fetch(params, callback));
        
        // Verify all params are preserved and environment is added
        assertTrue(params.has("include_schema"));
        assertTrue(params.has("include_count"));
        assertTrue(params.has("version"));
        assertTrue(params.has("locale"));
        assertTrue(params.has("environment"));
        assertEquals("staging", params.get("environment"));
    }

    @Test
    void testFetchWithNullContentTypeUid() throws Exception {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        
        // Create ContentType with null UID
        ContentType ctWithNullUid = new ContentType(null);
        ctWithNullUid.stackInstance = stack;
        ctWithNullUid.headers = new LinkedHashMap<>();
        ctWithNullUid.headers.put("environment", "production");
        
        JSONObject params = new JSONObject();
        ContentTypesCallback callback = new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel model, Error error) {}
        };
        
        // Should throw IllegalAccessException
        IllegalAccessException exception = assertThrows(IllegalAccessException.class, () -> {
            ctWithNullUid.fetch(params, callback);
        });
        
        assertTrue(exception.getMessage().contains("CONTENT_TYPE_UID_REQUIRED") || 
                   exception.getMessage().contains("Content type UID is required"));
    }

    @Test
    void testFetchWithEmptyContentTypeUid() throws Exception {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        
        // Create ContentType with empty UID
        ContentType ctWithEmptyUid = new ContentType("");
        ctWithEmptyUid.stackInstance = stack;
        ctWithEmptyUid.headers = new LinkedHashMap<>();
        ctWithEmptyUid.headers.put("environment", "production");
        
        JSONObject params = new JSONObject();
        ContentTypesCallback callback = new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel model, Error error) {}
        };
        
        // Should throw IllegalAccessException
        assertThrows(IllegalAccessException.class, () -> {
            ctWithEmptyUid.fetch(params, callback);
        });
    }

    @Test
    void testFetchPreservesExistingParams() throws Exception {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        contentType.stackInstance = stack;
        contentType.headers = new LinkedHashMap<>();
        contentType.headers.put("environment", "production");
        
        JSONObject params = new JSONObject();
        params.put("custom_param", "custom_value");
        params.put("count", 10);
        
        ContentTypesCallback callback = new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel model, Error error) {}
        };
        
        assertDoesNotThrow(() -> contentType.fetch(params, callback));
        
        // Verify custom params are preserved
        assertEquals("custom_value", params.get("custom_param"));
        assertEquals(10, params.get("count"));
        assertEquals("production", params.get("environment"));
    }

    @Test
    void testFetchWithNullCallback() throws Exception {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        contentType.stackInstance = stack;
        contentType.headers = new LinkedHashMap<>();
        contentType.headers.put("environment", "production");
        
        JSONObject params = new JSONObject();
        params.put("include_schema", true);
        
        // Fetch with null callback - should not throw
        assertDoesNotThrow(() -> contentType.fetch(params, null));
        
        // Environment should still be added
        assertTrue(params.has("environment"));
    }

    @Test
    void testFetchEnvironmentOverwrite() throws Exception {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        contentType.stackInstance = stack;
        contentType.headers = new LinkedHashMap<>();
        contentType.headers.put("environment", "production");
        
        JSONObject params = new JSONObject();
        params.put("environment", "staging"); // Pre-existing environment param
        params.put("other_param", "value");
        
        ContentTypesCallback callback = new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel model, Error error) {}
        };
        
        assertDoesNotThrow(() -> contentType.fetch(params, callback));
        
        // Environment from headers should overwrite the param
        assertEquals("production", params.get("environment"));
        assertEquals("value", params.get("other_param"));
    }

    // ========== GET URL PARAMS TESTS (via fetch) ==========

    @Test
    void testFetchProcessesUrlParams() throws Exception {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        contentType.stackInstance = stack;
        contentType.headers = new LinkedHashMap<>();
        contentType.headers.put("environment", "production");
        
        // Create params with various types
        JSONObject params = new JSONObject();
        params.put("string_param", "value");
        params.put("int_param", 123);
        params.put("boolean_param", true);
        params.put("double_param", 45.67);
        
        ContentTypesCallback callback = new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel model, Error error) {}
        };
        
        // This will internally call getUrlParams()
        assertDoesNotThrow(() -> contentType.fetch(params, callback));
        
        // All params should be processed
        assertEquals("value", params.get("string_param"));
        assertEquals(123, params.get("int_param"));
        assertTrue((Boolean) params.get("boolean_param"));
        assertEquals(45.67, params.get("double_param"));
    }

    @Test
    void testFetchWithNestedParams() throws Exception {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        contentType.stackInstance = stack;
        contentType.headers = new LinkedHashMap<>();
        contentType.headers.put("environment", "production");
        
        JSONObject params = new JSONObject();
        JSONObject nestedObject = new JSONObject();
        nestedObject.put("key1", "value1");
        nestedObject.put("key2", "value2");
        params.put("nested", nestedObject);
        
        ContentTypesCallback callback = new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel model, Error error) {}
        };
        
        assertDoesNotThrow(() -> contentType.fetch(params, callback));
        
        // Nested object should be preserved
        assertTrue(params.has("nested"));
        JSONObject retrievedNested = (JSONObject) params.get("nested");
        assertEquals("value1", retrievedNested.get("key1"));
        assertEquals("value2", retrievedNested.get("key2"));
    }
}
