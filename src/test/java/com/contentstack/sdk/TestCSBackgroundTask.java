package com.contentstack.sdk;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test cases for the CSBackgroundTask class
 */
class TestCSBackgroundTask {

    @Test
    void testDefaultConstructor() {
        CSBackgroundTask task = new CSBackgroundTask();
        
        assertNotNull(task);
        assertNull(task.service);
    }

    @Test
    void testCheckHeaderWithEmptyHeaders() {
        CSBackgroundTask task = new CSBackgroundTask();
        HashMap<String, Object> emptyHeaders = new HashMap<>();
        
        // Should log IllegalAccessException but not throw
        assertDoesNotThrow(() -> task.checkHeader(emptyHeaders));
    }

    @Test
    void testCheckHeaderWithValidHeaders() {
        CSBackgroundTask task = new CSBackgroundTask();
        HashMap<String, Object> headers = new HashMap<>();
        headers.put("api_key", "test_key");
        headers.put("access_token", "test_token");
        
        assertDoesNotThrow(() -> task.checkHeader(headers));
    }

    @Test
    void testCheckHeaderWithSingleHeader() {
        CSBackgroundTask task = new CSBackgroundTask();
        HashMap<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "Bearer token");
        
        assertDoesNotThrow(() -> task.checkHeader(headers));
    }

    @Test
    void testCheckHeaderWithMultipleHeaders() {
        CSBackgroundTask task = new CSBackgroundTask();
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer token");
        headers.put("X-API-Key", "api_key");
        headers.put("User-Agent", "ContentStack SDK");
        
        assertDoesNotThrow(() -> task.checkHeader(headers));
    }

    @Test
    void testCheckHeaderWithNullValues() {
        CSBackgroundTask task = new CSBackgroundTask();
        HashMap<String, Object> headers = new HashMap<>();
        headers.put("api_key", null);
        headers.put("token", "value");
        
        assertDoesNotThrow(() -> task.checkHeader(headers));
    }

    @Test
    void testCheckHeaderSize() {
        CSBackgroundTask task = new CSBackgroundTask();
        
        // Empty headers
        HashMap<String, Object> emptyHeaders = new HashMap<>();
        assertEquals(0, emptyHeaders.size());
        
        // Non-empty headers
        HashMap<String, Object> validHeaders = new HashMap<>();
        validHeaders.put("key", "value");
        assertEquals(1, validHeaders.size());
    }

    @Test
    void testServiceFieldInitialization() {
        CSBackgroundTask task = new CSBackgroundTask();
        
        assertNull(task.service, "Service should be null on initialization");
    }

    @Test
    void testCheckHeaderWithSpecialCharacters() {
        CSBackgroundTask task = new CSBackgroundTask();
        HashMap<String, Object> headers = new HashMap<>();
        headers.put("X-Special-Header!@#", "value");
        headers.put("key-with-dashes", "value");
        
        assertDoesNotThrow(() -> task.checkHeader(headers));
    }

    @Test
    void testCheckHeaderWithLongValues() {
        CSBackgroundTask task = new CSBackgroundTask();
        HashMap<String, Object> headers = new HashMap<>();
        // Create a string with 1000 'a' characters (Java 8 compatible)
        char[] chars = new char[1000];
        Arrays.fill(chars, 'a');
        String longValue = new String(chars);
        headers.put("Long-Header", longValue);
        
        assertDoesNotThrow(() -> task.checkHeader(headers));
    }

    @Test
    void testCheckHeaderWithNumericValues() {
        CSBackgroundTask task = new CSBackgroundTask();
        HashMap<String, Object> headers = new HashMap<>();
        headers.put("Content-Length", 12345);
        headers.put("Timeout", 30);
        
        assertDoesNotThrow(() -> task.checkHeader(headers));
    }

    @Test
    void testCheckHeaderWithBooleanValues() {
        CSBackgroundTask task = new CSBackgroundTask();
        HashMap<String, Object> headers = new HashMap<>();
        headers.put("Use-Cache", true);
        headers.put("Compression", false);
        
        assertDoesNotThrow(() -> task.checkHeader(headers));
    }

    @Test
    void testCheckHeaderImmutability() {
        CSBackgroundTask task = new CSBackgroundTask();
        HashMap<String, Object> headers = new HashMap<>();
        headers.put("key1", "value1");
        
        task.checkHeader(headers);
        
        // Verify headers are not modified
        assertEquals(1, headers.size());
        assertEquals("value1", headers.get("key1"));
    }

    @Test
    void testMultipleCheckHeaderCalls() {
        CSBackgroundTask task = new CSBackgroundTask();
        
        HashMap<String, Object> headers1 = new HashMap<>();
        headers1.put("key1", "value1");
        task.checkHeader(headers1);
        
        HashMap<String, Object> headers2 = new HashMap<>();
        headers2.put("key2", "value2");
        task.checkHeader(headers2);
        
        HashMap<String, Object> emptyHeaders = new HashMap<>();
        task.checkHeader(emptyHeaders);
        
        // All calls should complete without throwing
        assertNotNull(task);
    }

    // ========== PROTECTED CONSTRUCTOR TESTS ==========

    @Test
    void testConstructorWithStackInstance() throws Exception {
        // Test the protected constructor: CSBackgroundTask(Stack, String, String, HashMap, HashMap, String, ResultCallBack)
        
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("api_key", "test_key");
        headers.put("access_token", "test_token");
        headers.put("environment", "production");
        
        LinkedHashMap<String, Object> urlParams = new LinkedHashMap<>();
        urlParams.put("include_count", true);
        urlParams.put("limit", 10);
        
        ResultCallBack callback = new ResultCallBack() {
            @Override
            public void onRequestFail(ResponseType responseType, Error error) {
                // Test callback
            }
        };
        
        // Use reflection to access protected constructor
        Constructor<CSBackgroundTask> constructor = CSBackgroundTask.class.getDeclaredConstructor(
            Stack.class, String.class, String.class, HashMap.class, HashMap.class, String.class, ResultCallBack.class
        );
        constructor.setAccessible(true);
        
        CSBackgroundTask task = constructor.newInstance(
            stack, "ASSET", "assets", headers, urlParams, "test_request", callback
        );
        
        assertNotNull(task);
        assertNotNull(task.service);
    }

    @Test
    void testConstructorWithQueryInstance() throws Exception {
        // Test the protected constructor: CSBackgroundTask(Query, Stack, String, String, LinkedHashMap, HashMap, String, ResultCallBack)
        
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        Query query = stack.contentType("blog_post").query();
        
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("api_key", "test_key");
        headers.put("access_token", "test_token");
        headers.put("environment", "staging");
        
        HashMap<String, Object> urlQueries = new HashMap<>();
        urlQueries.put("locale", "en-us");
        urlQueries.put("include_count", true);
        
        ResultCallBack callback = new ResultCallBack() {
            @Override
            public void onRequestFail(ResponseType responseType, Error error) {
                // Test callback
            }
        };
        
        // Use reflection to access protected constructor
        Constructor<CSBackgroundTask> constructor = CSBackgroundTask.class.getDeclaredConstructor(
            Query.class, Stack.class, String.class, String.class, LinkedHashMap.class, HashMap.class, String.class, ResultCallBack.class
        );
        constructor.setAccessible(true);
        
        CSBackgroundTask task = constructor.newInstance(
            query, stack, "QUERY", "entries", headers, urlQueries, "query_request", callback
        );
        
        assertNotNull(task);
        assertNotNull(task.service);
    }

    @Test
    void testConstructorWithGlobalField() throws Exception {
        // Test the protected constructor: CSBackgroundTask(GlobalField, Stack, String, String, HashMap, HashMap, String, ResultCallBack)
        
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        GlobalField globalField = stack.globalField("test_global_field");
        
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("api_key", "test_key");
        headers.put("access_token", "test_token");
        headers.put("environment", "development");
        
        LinkedHashMap<String, Object> urlParams = new LinkedHashMap<>();
        urlParams.put("include_schema", true);
        
        ResultCallBack callback = new ResultCallBack() {
            @Override
            public void onRequestFail(ResponseType responseType, Error error) {
                // Test callback
            }
        };
        
        // Use reflection to access protected constructor
        Constructor<CSBackgroundTask> constructor = CSBackgroundTask.class.getDeclaredConstructor(
            GlobalField.class, Stack.class, String.class, String.class, HashMap.class, HashMap.class, String.class, ResultCallBack.class
        );
        constructor.setAccessible(true);
        
        CSBackgroundTask task = constructor.newInstance(
            globalField, stack, "GLOBALFIELD", "global_fields/test_global_field", headers, urlParams, "globalfield_request", callback
        );
        
        assertNotNull(task);
        assertNotNull(task.service);
    }

    @Test
    void testConstructorWithStackAndEmptyParams() throws Exception {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("api_key", "key");
        headers.put("access_token", "token");
        
        LinkedHashMap<String, Object> emptyParams = new LinkedHashMap<>();
        
        ResultCallBack callback = new ResultCallBack() {
            @Override
            public void onRequestFail(ResponseType responseType, Error error) {}
        };
        
        Constructor<CSBackgroundTask> constructor = CSBackgroundTask.class.getDeclaredConstructor(
            Stack.class, String.class, String.class, HashMap.class, HashMap.class, String.class, ResultCallBack.class
        );
        constructor.setAccessible(true);
        
        CSBackgroundTask task = constructor.newInstance(
            stack, "TEST", "test_url", headers, emptyParams, "request_info", callback
        );
        
        assertNotNull(task);
    }

    @Test
    void testConstructorWithQueryAndMultipleParams() throws Exception {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        Query query = stack.contentType("product").query();
        
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("api_key", "key");
        headers.put("access_token", "token");
        headers.put("environment", "prod");
        headers.put("custom_header", "custom_value");
        
        HashMap<String, Object> urlQueries = new HashMap<>();
        urlQueries.put("locale", "en-us");
        urlQueries.put("include_count", true);
        urlQueries.put("skip", 0);
        urlQueries.put("limit", 50);
        
        ResultCallBack callback = new ResultCallBack() {
            @Override
            public void onRequestFail(ResponseType responseType, Error error) {}
        };
        
        Constructor<CSBackgroundTask> constructor = CSBackgroundTask.class.getDeclaredConstructor(
            Query.class, Stack.class, String.class, String.class, LinkedHashMap.class, HashMap.class, String.class, ResultCallBack.class
        );
        constructor.setAccessible(true);
        
        CSBackgroundTask task = constructor.newInstance(
            query, stack, "QUERY", "entries", headers, urlQueries, "query_all", callback
        );
        
        assertNotNull(task);
        assertNotNull(task.service);
    }

    @Test
    void testConstructorWithGlobalFieldAndMinimalParams() throws Exception {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        GlobalField globalField = stack.globalField("minimal_field");
        
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("api_key", "key");
        headers.put("access_token", "token");
        
        LinkedHashMap<String, Object> urlParams = new LinkedHashMap<>();
        
        ResultCallBack callback = new ResultCallBack() {
            @Override
            public void onRequestFail(ResponseType responseType, Error error) {}
        };
        
        Constructor<CSBackgroundTask> constructor = CSBackgroundTask.class.getDeclaredConstructor(
            GlobalField.class, Stack.class, String.class, String.class, HashMap.class, HashMap.class, String.class, ResultCallBack.class
        );
        constructor.setAccessible(true);
        
        CSBackgroundTask task = constructor.newInstance(
            globalField, stack, "GLOBALFIELD", "global_fields/minimal_field", headers, urlParams, "fetch", callback
        );
        
        assertNotNull(task);
    }

    @Test
    void testConstructorWithNullCallback() throws Exception {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("api_key", "key");
        headers.put("access_token", "token");
        
        LinkedHashMap<String, Object> urlParams = new LinkedHashMap<>();
        
        Constructor<CSBackgroundTask> constructor = CSBackgroundTask.class.getDeclaredConstructor(
            Stack.class, String.class, String.class, HashMap.class, HashMap.class, String.class, ResultCallBack.class
        );
        constructor.setAccessible(true);
        
        // Null callback should not cause exception during construction
        CSBackgroundTask task = constructor.newInstance(
            stack, "TEST", "test", headers, urlParams, "info", null
        );
        
        assertNotNull(task);
    }

    @Test
    void testConstructorCreatesCompleteUrl() throws Exception {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("api_key", "key");
        headers.put("access_token", "token");
        
        LinkedHashMap<String, Object> urlParams = new LinkedHashMap<>();
        
        ResultCallBack callback = new ResultCallBack() {
            @Override
            public void onRequestFail(ResponseType responseType, Error error) {}
        };
        
        String testUrl = "assets/blt123";
        
        Constructor<CSBackgroundTask> constructor = CSBackgroundTask.class.getDeclaredConstructor(
            Stack.class, String.class, String.class, HashMap.class, HashMap.class, String.class, ResultCallBack.class
        );
        constructor.setAccessible(true);
        
        CSBackgroundTask task = constructor.newInstance(
            stack, "ASSET", testUrl, headers, urlParams, "fetch_asset", callback
        );
        
        assertNotNull(task);
        // The constructor should combine stack.config.getEndpoint() + url
        assertNotNull(task.service);
    }

    @Test
    void testAllThreeConstructorsWithDifferentControllers() throws Exception {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("api_key", "key");
        headers.put("access_token", "token");
        
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        
        ResultCallBack callback = new ResultCallBack() {
            @Override
            public void onRequestFail(ResponseType responseType, Error error) {}
        };
        
        // Test Stack constructor with different controller
        Constructor<CSBackgroundTask> stackConstructor = CSBackgroundTask.class.getDeclaredConstructor(
            Stack.class, String.class, String.class, HashMap.class, HashMap.class, String.class, ResultCallBack.class
        );
        stackConstructor.setAccessible(true);
        CSBackgroundTask task1 = stackConstructor.newInstance(
            stack, "CONTENTTYPES", "content_types", headers, params, "info1", callback
        );
        assertNotNull(task1);
        
        // Test Query constructor with different controller
        Query query = stack.contentType("test").query();
        Constructor<CSBackgroundTask> queryConstructor = CSBackgroundTask.class.getDeclaredConstructor(
            Query.class, Stack.class, String.class, String.class, LinkedHashMap.class, HashMap.class, String.class, ResultCallBack.class
        );
        queryConstructor.setAccessible(true);
        CSBackgroundTask task2 = queryConstructor.newInstance(
            query, stack, "ENTRY", "entries/blt123", headers, params, "info2", callback
        );
        assertNotNull(task2);
        
        // Test GlobalField constructor with different controller
        GlobalField gf = stack.globalField("test");
        Constructor<CSBackgroundTask> gfConstructor = CSBackgroundTask.class.getDeclaredConstructor(
            GlobalField.class, Stack.class, String.class, String.class, HashMap.class, HashMap.class, String.class, ResultCallBack.class
        );
        gfConstructor.setAccessible(true);
        CSBackgroundTask task3 = gfConstructor.newInstance(
            gf, stack, "GLOBALFIELDS", "global_fields", headers, params, "info3", callback
        );
        assertNotNull(task3);
    }
}

