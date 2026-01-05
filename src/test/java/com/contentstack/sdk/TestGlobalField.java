package com.contentstack.sdk;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.LinkedHashMap;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for GlobalField class.
 * Tests global field operations, configurations, and methods.
 */
public class TestGlobalField {

    private GlobalField globalField;
    private final String globalFieldUid = "test_global_field";

    @BeforeEach
    void setUp() {
        globalField = new GlobalField(globalFieldUid);
    }

    // ========== CONSTRUCTOR TESTS ==========

    @Test
    void testGlobalFieldConstructorWithUid() {
        GlobalField gf = new GlobalField("seo_fields");
        assertNotNull(gf);
        assertEquals("seo_fields", gf.globalFieldUid);
        assertNotNull(gf.headers);
        assertNotNull(gf.params);
    }

    @Test
    void testGlobalFieldDefaultConstructor() {
        GlobalField gf = new GlobalField();
        assertNotNull(gf);
        assertNull(gf.globalFieldUid);
        assertNotNull(gf.headers);
        assertNotNull(gf.params);
    }

    // ========== HEADER TESTS ==========

    @Test
    void testSetHeader() {
        globalField.setHeader("custom-header", "custom-value");
        assertTrue(globalField.headers.containsKey("custom-header"));
        assertEquals("custom-value", globalField.headers.get("custom-header"));
    }

    @Test
    void testSetMultipleHeaders() {
        globalField.setHeader("header1", "value1");
        globalField.setHeader("header2", "value2");
        globalField.setHeader("header3", "value3");
        
        assertEquals(3, globalField.headers.size());
        assertEquals("value1", globalField.headers.get("header1"));
        assertEquals("value2", globalField.headers.get("header2"));
        assertEquals("value3", globalField.headers.get("header3"));
    }

    @Test
    void testSetHeaderWithEmptyKey() {
        globalField.setHeader("", "value");
        assertFalse(globalField.headers.containsKey(""));
    }

    @Test
    void testSetHeaderWithEmptyValue() {
        globalField.setHeader("key", "");
        assertFalse(globalField.headers.containsKey("key"));
    }

    @Test
    void testSetHeaderWithBothEmpty() {
        globalField.setHeader("", "");
        assertEquals(0, globalField.headers.size());
    }

    @Test
    void testRemoveHeader() {
        globalField.setHeader("temp-header", "temp-value");
        assertTrue(globalField.headers.containsKey("temp-header"));
        
        globalField.removeHeader("temp-header");
        assertFalse(globalField.headers.containsKey("temp-header"));
    }

    @Test
    void testRemoveNonExistentHeader() {
        globalField.removeHeader("non-existent");
        assertNotNull(globalField.headers);
    }

    @Test
    void testRemoveHeaderWithEmptyKey() {
        globalField.removeHeader("");
        assertNotNull(globalField.headers);
    }

    // ========== INCLUDE TESTS ==========

    @Test
    void testIncludeBranch() {
        GlobalField result = globalField.includeBranch();
        assertSame(globalField, result);
        assertTrue(globalField.params.has("include_branch"));
        assertEquals(true, globalField.params.get("include_branch"));
    }

    @Test
    void testIncludeGlobalFieldSchema() {
        GlobalField result = globalField.includeGlobalFieldSchema();
        assertSame(globalField, result);
        assertTrue(globalField.params.has("include_global_field_schema"));
        assertEquals(true, globalField.params.get("include_global_field_schema"));
    }

    @Test
    void testMultipleIncludesCombined() {
        globalField.includeBranch().includeGlobalFieldSchema();
        
        assertTrue(globalField.params.has("include_branch"));
        assertTrue(globalField.params.has("include_global_field_schema"));
        assertEquals(2, globalField.params.length());
    }

    // ========== CHAINING TESTS ==========

    @Test
    void testMethodChaining() {
        GlobalField result = globalField
            .includeBranch()
            .includeGlobalFieldSchema();
        
        assertSame(globalField, result);
        assertTrue(globalField.params.has("include_branch"));
        assertTrue(globalField.params.has("include_global_field_schema"));
    }

    // ========== EDGE CASE TESTS ==========

    @Test
    void testHeadersInitialization() {
        GlobalField gf = new GlobalField("test");
        assertNotNull(gf.headers);
        assertEquals(0, gf.headers.size());
    }

    @Test
    void testParamsInitialization() {
        GlobalField gf = new GlobalField("test");
        assertNotNull(gf.params);
        assertEquals(0, gf.params.length());
    }

    @Test
    void testHeaderOverwrite() {
        globalField.setHeader("key", "value1");
        assertEquals("value1", globalField.headers.get("key"));
        
        globalField.setHeader("key", "value2");
        assertEquals("value2", globalField.headers.get("key"));
    }

    @Test
    void testRemoveAndAddSameHeader() {
        globalField.setHeader("key", "value1");
        globalField.removeHeader("key");
        assertFalse(globalField.headers.containsKey("key"));
        
        globalField.setHeader("key", "value2");
        assertEquals("value2", globalField.headers.get("key"));
    }

    @Test
    void testMultipleIncludeBranchCalls() {
        globalField.includeBranch();
        globalField.includeBranch();
        
        assertTrue(globalField.params.has("include_branch"));
        assertEquals(true, globalField.params.get("include_branch"));
    }

    @Test
    void testMultipleIncludeGlobalFieldSchemaCalls() {
        globalField.includeGlobalFieldSchema();
        globalField.includeGlobalFieldSchema();
        
        assertTrue(globalField.params.has("include_global_field_schema"));
        assertEquals(true, globalField.params.get("include_global_field_schema"));
    }

    @Test
    void testGlobalFieldUidPreservation() {
        String originalUid = "original_global_field";
        GlobalField gf = new GlobalField(originalUid);
        
        gf.setHeader("key", "value");
        gf.includeBranch();
        gf.includeGlobalFieldSchema();
        
        assertEquals(originalUid, gf.globalFieldUid);
    }

    @Test
    void testComplexConfiguration() {
        globalField.setHeader("api_key", "test_key");
        globalField.setHeader("access_token", "token123");
        globalField.includeBranch();
        globalField.includeGlobalFieldSchema();
        
        assertEquals(2, globalField.headers.size());
        assertEquals(2, globalField.params.length());
        assertTrue(globalField.headers.containsKey("api_key"));
        assertTrue(globalField.headers.containsKey("access_token"));
        assertTrue(globalField.params.has("include_branch"));
        assertTrue(globalField.params.has("include_global_field_schema"));
    }

    @Test
    void testEmptyGlobalFieldUid() {
        GlobalField gf = new GlobalField("");
        assertEquals("", gf.globalFieldUid);
    }

    @Test
    void testGlobalFieldWithSpecialCharacters() {
        GlobalField gf = new GlobalField("global_field_123");
        assertEquals("global_field_123", gf.globalFieldUid);
    }

    @Test
    void testSetMultipleHeadersThenRemoveAll() {
        globalField.setHeader("h1", "v1");
        globalField.setHeader("h2", "v2");
        globalField.setHeader("h3", "v3");
        
        globalField.removeHeader("h1");
        globalField.removeHeader("h2");
        globalField.removeHeader("h3");
        
        assertEquals(0, globalField.headers.size());
    }

    // ========== FETCH METHOD TESTS ==========

    @Test
    void testFetchWithValidCallback() throws IllegalAccessException {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        globalField.setStackInstance(stack);
        
        GlobalFieldsCallback callback = new GlobalFieldsCallback() {
            @Override
            public void onCompletion(GlobalFieldsModel model, Error error) {
                // Callback implementation
            }
        };
        
        assertDoesNotThrow(() -> globalField.fetch(callback));
    }

    @Test
    void testFetchWithNullUid() throws IllegalAccessException {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        GlobalField gf = new GlobalField((String) null);
        gf.setStackInstance(stack);
        
        GlobalFieldsCallback callback = new GlobalFieldsCallback() {
            @Override
            public void onCompletion(GlobalFieldsModel model, Error error) {
                // Callback implementation
            }
        };
        
        assertThrows(IllegalAccessException.class, () -> gf.fetch(callback));
    }

    @Test
    void testFetchWithEmptyUid() throws IllegalAccessException {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        GlobalField gf = new GlobalField("");
        gf.setStackInstance(stack);
        
        GlobalFieldsCallback callback = new GlobalFieldsCallback() {
            @Override
            public void onCompletion(GlobalFieldsModel model, Error error) {
                // Callback implementation
            }
        };
        
        assertThrows(IllegalAccessException.class, () -> gf.fetch(callback));
    }

    @Test
    void testFetchWithIncludeParameters() throws IllegalAccessException {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        globalField.setStackInstance(stack);
        globalField.includeBranch();
        globalField.includeGlobalFieldSchema();
        
        GlobalFieldsCallback callback = new GlobalFieldsCallback() {
            @Override
            public void onCompletion(GlobalFieldsModel model, Error error) {
                // Callback implementation
            }
        };
        
        assertDoesNotThrow(() -> globalField.fetch(callback));
        assertTrue(globalField.params.has("include_branch"));
        assertTrue(globalField.params.has("include_global_field_schema"));
    }

    @Test
    void testFetchWithHeaders() throws IllegalAccessException {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        globalField.setStackInstance(stack);
        globalField.setHeader("custom-header", "custom-value");
        
        GlobalFieldsCallback callback = new GlobalFieldsCallback() {
            @Override
            public void onCompletion(GlobalFieldsModel model, Error error) {
                // Callback implementation
            }
        };
        
        assertDoesNotThrow(() -> globalField.fetch(callback));
        assertTrue(globalField.headers.containsKey("custom-header"));
    }

    @Test
    void testFetchWithNullCallback() throws IllegalAccessException {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        globalField.setStackInstance(stack);
        
        // Should not throw exception even with null callback
        assertDoesNotThrow(() -> globalField.fetch(null));
    }

    // ========== FIND ALL METHOD TESTS ==========

    @Test
    void testFindAllWithValidCallback() throws IllegalAccessException {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        globalField.setStackInstance(stack);
        
        GlobalFieldsCallback callback = new GlobalFieldsCallback() {
            @Override
            public void onCompletion(GlobalFieldsModel model, Error error) {
                // Callback implementation
            }
        };
        
        assertDoesNotThrow(() -> globalField.findAll(callback));
    }

    @Test
    void testFindAllWithIncludeParameters() throws IllegalAccessException {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        globalField.setStackInstance(stack);
        globalField.includeBranch();
        globalField.includeGlobalFieldSchema();
        
        GlobalFieldsCallback callback = new GlobalFieldsCallback() {
            @Override
            public void onCompletion(GlobalFieldsModel model, Error error) {
                // Callback implementation
            }
        };
        
        assertDoesNotThrow(() -> globalField.findAll(callback));
        assertTrue(globalField.params.has("include_branch"));
        assertTrue(globalField.params.has("include_global_field_schema"));
    }

    @Test
    void testFindAllWithHeaders() throws IllegalAccessException {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        globalField.setStackInstance(stack);
        globalField.setHeader("custom-header", "custom-value");
        
        GlobalFieldsCallback callback = new GlobalFieldsCallback() {
            @Override
            public void onCompletion(GlobalFieldsModel model, Error error) {
                // Callback implementation
            }
        };
        
        assertDoesNotThrow(() -> globalField.findAll(callback));
        assertTrue(globalField.headers.containsKey("custom-header"));
    }

    @Test
    void testFindAllWithNullCallback() throws IllegalAccessException {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        globalField.setStackInstance(stack);
        
        // Should not throw exception even with null callback
        assertDoesNotThrow(() -> globalField.findAll(null));
    }

    // ========== GET URL PARAMS METHOD TESTS (via fetch/findAll) ==========

    @Test
    void testGetUrlParamsWithEmptyParams() throws IllegalAccessException {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        globalField.setStackInstance(stack);
        
        GlobalFieldsCallback callback = new GlobalFieldsCallback() {
            @Override
            public void onCompletion(GlobalFieldsModel model, Error error) {
                // Callback implementation
            }
        };
        
        // params should be empty initially
        assertEquals(0, globalField.params.length());
        assertDoesNotThrow(() -> globalField.fetch(callback));
    }

    @Test
    void testGetUrlParamsWithMultipleParams() throws IllegalAccessException {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        globalField.setStackInstance(stack);
        
        // Add multiple parameters
        globalField.params.put("param1", "value1");
        globalField.params.put("param2", 123);
        globalField.params.put("param3", true);
        
        GlobalFieldsCallback callback = new GlobalFieldsCallback() {
            @Override
            public void onCompletion(GlobalFieldsModel model, Error error) {
                // Callback implementation
            }
        };
        
        assertEquals(3, globalField.params.length());
        assertDoesNotThrow(() -> globalField.fetch(callback));
    }

    @Test
    void testGetUrlParamsWithNullValue() throws IllegalAccessException {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        globalField.setStackInstance(stack);
        
        // Add parameter with null value
        globalField.params.put("null_param", JSONObject.NULL);
        
        GlobalFieldsCallback callback = new GlobalFieldsCallback() {
            @Override
            public void onCompletion(GlobalFieldsModel model, Error error) {
                // Callback implementation
            }
        };
        
        assertDoesNotThrow(() -> globalField.fetch(callback));
    }

    // ========== FETCH GLOBAL FIELDS METHOD TESTS (private, covered via fetch/findAll) ==========

    @Test
    void testFetchGlobalFieldsViaFetch() throws IllegalAccessException {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        globalField.setStackInstance(stack);
        globalField.setHeader("environment", "production");
        
        GlobalFieldsCallback callback = new GlobalFieldsCallback() {
            @Override
            public void onCompletion(GlobalFieldsModel model, Error error) {
                // Callback implementation
            }
        };
        
        assertDoesNotThrow(() -> globalField.fetch(callback));
    }

    @Test
    void testFetchGlobalFieldsViaFindAll() throws IllegalAccessException {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        globalField.setStackInstance(stack);
        globalField.setHeader("environment", "production");
        
        GlobalFieldsCallback callback = new GlobalFieldsCallback() {
            @Override
            public void onCompletion(GlobalFieldsModel model, Error error) {
                // Callback implementation
            }
        };
        
        assertDoesNotThrow(() -> globalField.findAll(callback));
    }

    @Test
    void testFetchGlobalFieldsWithComplexParams() throws IllegalAccessException {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        globalField.setStackInstance(stack);
        
        // Add complex parameters
        globalField.includeBranch();
        globalField.includeGlobalFieldSchema();
        globalField.params.put("locale", "en-us");
        globalField.params.put("version", 2);
        
        GlobalFieldsCallback callback = new GlobalFieldsCallback() {
            @Override
            public void onCompletion(GlobalFieldsModel model, Error error) {
                // Callback implementation
            }
        };
        
        assertDoesNotThrow(() -> globalField.fetch(callback));
        assertEquals(4, globalField.params.length());
    }

    // ========== SET STACK INSTANCE TESTS ==========

    @Test
    void testSetStackInstance() throws IllegalAccessException {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        stack.setHeader("stack-header", "stack-value");
        
        globalField.setStackInstance(stack);
        
        assertNotNull(globalField.stackInstance);
        assertEquals(stack, globalField.stackInstance);
        assertTrue(globalField.headers.containsKey("stack-header"));
        assertEquals("stack-value", globalField.headers.get("stack-header"));
    }

    @Test
    void testSetStackInstanceOverridesHeaders() throws IllegalAccessException {
        globalField.setHeader("old-header", "old-value");
        assertTrue(globalField.headers.containsKey("old-header"));
        
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        stack.setHeader("new-header", "new-value");
        
        globalField.setStackInstance(stack);
        
        // Headers should now reference stack's headers
        assertTrue(globalField.headers.containsKey("new-header"));
        assertFalse(globalField.headers.containsKey("old-header"));
    }

    @Test
    void testFetchAndFindAllWithSameCallback() throws IllegalAccessException {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        globalField.setStackInstance(stack);
        
        GlobalFieldsCallback callback = new GlobalFieldsCallback() {
            @Override
            public void onCompletion(GlobalFieldsModel model, Error error) {
                // Shared callback implementation
            }
        };
        
        assertDoesNotThrow(() -> globalField.fetch(callback));
        assertDoesNotThrow(() -> globalField.findAll(callback));
    }
}
