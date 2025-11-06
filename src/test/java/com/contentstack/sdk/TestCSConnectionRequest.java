package com.contentstack.sdk;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CSConnectionRequest class
 */
class TestCSConnectionRequest {

    private Stack stack;
    private Query query;
    private Entry entry;
    private AssetLibrary assetLibrary;
    private Asset asset;
    private ContentType contentType;
    private GlobalField globalField;

    @BeforeEach
    void setUp() throws IllegalAccessException {
        stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_env");
        contentType = stack.contentType("blog_post");
        query = contentType.query();
        entry = stack.contentType("blog_post").entry("test_entry_uid");
        assetLibrary = stack.assetLibrary();
        asset = stack.asset("test_asset_uid");
        globalField = stack.globalField("test_global_field");
    }

    // ========== CONSTRUCTOR TESTS ==========

    @Test
    void testConstructorWithQuery() {
        CSConnectionRequest request = new CSConnectionRequest(query);
        assertNotNull(request);
        assertNotNull(request.endpoint);
    }

    @Test
    void testConstructorWithEntry() {
        CSConnectionRequest request = new CSConnectionRequest(entry);
        assertNotNull(request);
        assertNotNull(request.endpoint);
    }

    @Test
    void testConstructorWithAssetLibrary() {
        CSConnectionRequest request = new CSConnectionRequest(assetLibrary);
        assertNotNull(request);
        assertNotNull(request.endpoint);
    }

    @Test
    void testConstructorWithAsset() {
        CSConnectionRequest request = new CSConnectionRequest(asset);
        assertNotNull(request);
        assertNotNull(request.endpoint);
    }

    @Test
    void testConstructorWithStack() {
        CSConnectionRequest request = new CSConnectionRequest(stack);
        assertNotNull(request);
    }

    @Test
    void testConstructorWithContentType() {
        CSConnectionRequest request = new CSConnectionRequest(contentType);
        assertNotNull(request);
        assertNotNull(request.endpoint);
    }

    @Test
    void testConstructorWithGlobalField() {
        CSConnectionRequest request = new CSConnectionRequest(globalField);
        assertNotNull(request);
        assertNotNull(request.endpoint);
    }

    // ========== SETTER TESTS ==========

    @Test
    void testSetQueryInstance() {
        CSConnectionRequest request = new CSConnectionRequest(query);
        Query newQuery = stack.contentType("new_ct").query();
        request.setQueryInstance(newQuery);
        assertNotNull(request.endpoint);
    }

    @Test
    void testSetURLQueries() {
        CSConnectionRequest request = new CSConnectionRequest(stack);
        HashMap<String, Object> urlQueries = new HashMap<>();
        urlQueries.put("key", "value");
        request.setURLQueries(urlQueries);
        assertNotNull(request);
    }

    @Test
    void testSetStackInstance() throws IllegalAccessException {
        CSConnectionRequest request = new CSConnectionRequest(stack);
        Stack newStack = Contentstack.stack("new_key", "new_token", "new_env");
        request.setStackInstance(newStack);
        assertNotNull(request);
    }

    // ========== ON REQUEST FINISHED TESTS ==========

    @Test
    void testOnRequestFinishedWithQueryObject() throws Exception {
        // Create a mock CSHttpConnection
        CSHttpConnection mockConnection = createMockConnection();
        
        // Set controller
        Field controllerField = CSHttpConnection.class.getDeclaredField("controller");
        controllerField.setAccessible(true);
        controllerField.set(mockConnection, Constants.QUERYOBJECT);
        
        // Create response with entries
        JSONObject response = new JSONObject();
        List<JSONObject> entriesList = new ArrayList<>();
        JSONObject entry1 = new JSONObject();
        entry1.put("uid", "entry1");
        entry1.put("title", "Entry 1");
        entriesList.add(entry1);
        
        // Use reflection to inject List into JSONObject
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("entries", entriesList);
        
        Field responseField = CSHttpConnection.class.getDeclaredField("responseJSON");
        responseField.setAccessible(true);
        responseField.set(mockConnection, response);
        
        // Create CSConnectionRequest with Query
        CSConnectionRequest request = new CSConnectionRequest(query);
        
        // Call onRequestFinished
        assertDoesNotThrow(() -> request.onRequestFinished(mockConnection));
    }

    @Test
    void testOnRequestFinishedWithSingleQueryObject() throws Exception {
        // Create a mock CSHttpConnection
        CSHttpConnection mockConnection = createMockConnection();
        
        // Set controller
        Field controllerField = CSHttpConnection.class.getDeclaredField("controller");
        controllerField.setAccessible(true);
        controllerField.set(mockConnection, Constants.SINGLEQUERYOBJECT);
        
        // Create response with entries
        JSONObject response = new JSONObject();
        List<JSONObject> entriesList = new ArrayList<>();
        JSONObject entry1 = new JSONObject();
        entry1.put("uid", "entry1");
        entry1.put("title", "Entry 1");
        entriesList.add(entry1);
        
        // Use reflection to inject List into JSONObject
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("entries", entriesList);
        
        Field responseField = CSHttpConnection.class.getDeclaredField("responseJSON");
        responseField.setAccessible(true);
        responseField.set(mockConnection, response);
        
        // Create CSConnectionRequest with Query
        CSConnectionRequest request = new CSConnectionRequest(query);
        
        // Call onRequestFinished
        assertDoesNotThrow(() -> request.onRequestFinished(mockConnection));
    }

    @Test
    void testOnRequestFinishedWithFetchEntry() throws Exception {
        // Create a mock CSHttpConnection
        CSHttpConnection mockConnection = createMockConnection();
        
        // Set controller
        Field controllerField = CSHttpConnection.class.getDeclaredField("controller");
        controllerField.setAccessible(true);
        controllerField.set(mockConnection, Constants.FETCHENTRY);
        
        // Create response with entry
        LinkedHashMap<String, Object> entryMap = new LinkedHashMap<>();
        entryMap.put("uid", "test_entry_uid");
        entryMap.put("title", "Test Entry");
        entryMap.put("url", "/test-entry");
        entryMap.put("locale", "en-us");
        
        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("entry", entryMap);
        
        Field responseField = CSHttpConnection.class.getDeclaredField("responseJSON");
        responseField.setAccessible(true);
        responseField.set(mockConnection, response);
        
        // Create callback
        AtomicBoolean callbackCalled = new AtomicBoolean(false);
        EntryResultCallBack callback = new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                callbackCalled.set(true);
            }
        };
        
        Field callbackField = CSHttpConnection.class.getDeclaredField("callBackObject");
        callbackField.setAccessible(true);
        callbackField.set(mockConnection, callback);
        
        // Create CSConnectionRequest with Entry
        CSConnectionRequest request = new CSConnectionRequest(entry);
        
        // Call onRequestFinished
        request.onRequestFinished(mockConnection);
        
        // Verify callback was called
        assertTrue(callbackCalled.get());
        
        // Verify entry data was populated
        assertEquals("test_entry_uid", entry.uid);
        assertEquals("Test Entry", entry.title);
        assertEquals("/test-entry", entry.url);
        assertEquals("en-us", entry.language);
    }

    @Test
    void testOnRequestFinishedWithFetchAllAssets() throws Exception {
        // Create a mock CSHttpConnection
        CSHttpConnection mockConnection = createMockConnection();
        
        // Set controller
        Field controllerField = CSHttpConnection.class.getDeclaredField("controller");
        controllerField.setAccessible(true);
        controllerField.set(mockConnection, Constants.FETCHALLASSETS);
        
        // Create response with assets
        List<JSONObject> assetsList = new ArrayList<>();
        JSONObject asset1 = new JSONObject();
        asset1.put("uid", "asset1");
        asset1.put("filename", "test.jpg");
        assetsList.add(asset1);
        
        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("assets", assetsList);
        
        Field responseField = CSHttpConnection.class.getDeclaredField("responseJSON");
        responseField.setAccessible(true);
        responseField.set(mockConnection, response);
        
        // Create CSConnectionRequest with AssetLibrary
        CSConnectionRequest request = new CSConnectionRequest(assetLibrary);
        
        // Call onRequestFinished
        assertDoesNotThrow(() -> request.onRequestFinished(mockConnection));
    }

    @Test
    void testOnRequestFinishedWithFetchAssets() throws Exception {
        // Create a mock CSHttpConnection
        CSHttpConnection mockConnection = createMockConnection();
        
        // Set controller
        Field controllerField = CSHttpConnection.class.getDeclaredField("controller");
        controllerField.setAccessible(true);
        controllerField.set(mockConnection, Constants.FETCHASSETS);
        
        // Create response with single asset
        LinkedHashMap<String, Object> assetMap = new LinkedHashMap<>();
        assetMap.put("uid", "test_asset_uid");
        assetMap.put("filename", "test.jpg");
        assetMap.put("content_type", "image/jpeg");
        assetMap.put("file_size", "1024");
        assetMap.put("url", "https://example.com/test.jpg");
        
        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("asset", assetMap);
        
        Field responseField = CSHttpConnection.class.getDeclaredField("responseJSON");
        responseField.setAccessible(true);
        responseField.set(mockConnection, response);
        
        // Create callback
        AtomicBoolean callbackCalled = new AtomicBoolean(false);
        FetchResultCallback callback = new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                callbackCalled.set(true);
            }
        };
        
        Field callbackField = CSHttpConnection.class.getDeclaredField("callBackObject");
        callbackField.setAccessible(true);
        callbackField.set(mockConnection, callback);
        
        // Create CSConnectionRequest with Asset
        CSConnectionRequest request = new CSConnectionRequest(asset);
        
        // Call onRequestFinished
        request.onRequestFinished(mockConnection);
        
        // Verify callback was called
        assertTrue(callbackCalled.get());
        
        // Verify asset data was populated
        assertEquals("test_asset_uid", asset.assetUid);
        assertEquals("test.jpg", asset.fileName);
        assertEquals("image/jpeg", asset.contentType);
        assertEquals("1024", asset.fileSize);
        assertEquals("https://example.com/test.jpg", asset.uploadUrl);
    }

    @Test
    void testOnRequestFinishedWithFetchSync() throws Exception {
        // Create a mock CSHttpConnection
        CSHttpConnection mockConnection = createMockConnection();
        
        // Set controller
        Field controllerField = CSHttpConnection.class.getDeclaredField("controller");
        controllerField.setAccessible(true);
        controllerField.set(mockConnection, Constants.FETCHSYNC);
        
        // Create response
        JSONObject response = new JSONObject();
        response.put("sync_token", "test_sync_token");
        response.put("items", new JSONArray());
        
        Field responseField = CSHttpConnection.class.getDeclaredField("responseJSON");
        responseField.setAccessible(true);
        responseField.set(mockConnection, response);
        
        // Create callback
        AtomicBoolean callbackCalled = new AtomicBoolean(false);
        AtomicReference<SyncStack> receivedModel = new AtomicReference<>();
        SyncResultCallBack callback = new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {
                callbackCalled.set(true);
                receivedModel.set(syncStack);
            }
        };
        
        Field callbackField = CSHttpConnection.class.getDeclaredField("callBackObject");
        callbackField.setAccessible(true);
        callbackField.set(mockConnection, callback);
        
        // Create CSConnectionRequest with Stack
        CSConnectionRequest request = new CSConnectionRequest(stack);
        
        // Call onRequestFinished
        request.onRequestFinished(mockConnection);
        
        // Verify callback was called
        assertTrue(callbackCalled.get());
        assertNotNull(receivedModel.get());
    }

    @Test
    void testOnRequestFinishedWithFetchContentTypes() throws Exception {
        // Create a mock CSHttpConnection
        CSHttpConnection mockConnection = createMockConnection();
        
        // Set controller
        Field controllerField = CSHttpConnection.class.getDeclaredField("controller");
        controllerField.setAccessible(true);
        controllerField.set(mockConnection, Constants.FETCHCONTENTTYPES);
        
        // Create response with content_type
        LinkedHashMap<String, Object> contentTypeMap = new LinkedHashMap<>();
        contentTypeMap.put("uid", "blog_post");
        contentTypeMap.put("title", "Blog Post");
        
        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("content_type", contentTypeMap);
        
        Field responseField = CSHttpConnection.class.getDeclaredField("responseJSON");
        responseField.setAccessible(true);
        responseField.set(mockConnection, response);
        
        // Create callback
        AtomicBoolean callbackCalled = new AtomicBoolean(false);
        AtomicReference<ContentTypesModel> receivedModel = new AtomicReference<>();
        ContentTypesCallback callback = new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel model, Error error) {
                callbackCalled.set(true);
                receivedModel.set(model);
            }
        };
        
        Field callbackField = CSHttpConnection.class.getDeclaredField("callBackObject");
        callbackField.setAccessible(true);
        callbackField.set(mockConnection, callback);
        
        // Create CSConnectionRequest with ContentType
        CSConnectionRequest request = new CSConnectionRequest(contentType);
        
        // Call onRequestFinished
        request.onRequestFinished(mockConnection);
        
        // Verify callback was called
        assertTrue(callbackCalled.get());
        assertNotNull(receivedModel.get());
    }

    @Test
    void testOnRequestFinishedWithFetchGlobalFields() throws Exception {
        // Create a mock CSHttpConnection
        CSHttpConnection mockConnection = createMockConnection();
        
        // Set controller
        Field controllerField = CSHttpConnection.class.getDeclaredField("controller");
        controllerField.setAccessible(true);
        controllerField.set(mockConnection, Constants.FETCHGLOBALFIELDS);
        
        // Create response with global_field
        LinkedHashMap<String, Object> globalFieldMap = new LinkedHashMap<>();
        globalFieldMap.put("uid", "test_global_field");
        globalFieldMap.put("title", "Test Global Field");
        
        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("global_field", globalFieldMap);
        
        Field responseField = CSHttpConnection.class.getDeclaredField("responseJSON");
        responseField.setAccessible(true);
        responseField.set(mockConnection, response);
        
        // Create callback
        AtomicBoolean callbackCalled = new AtomicBoolean(false);
        AtomicReference<GlobalFieldsModel> receivedModel = new AtomicReference<>();
        GlobalFieldsCallback callback = new GlobalFieldsCallback() {
            @Override
            public void onCompletion(GlobalFieldsModel model, Error error) {
                callbackCalled.set(true);
                receivedModel.set(model);
            }
        };
        
        Field callbackField = CSHttpConnection.class.getDeclaredField("callBackObject");
        callbackField.setAccessible(true);
        callbackField.set(mockConnection, callback);
        
        // Create CSConnectionRequest with GlobalField
        CSConnectionRequest request = new CSConnectionRequest(globalField);
        
        // Call onRequestFinished
        request.onRequestFinished(mockConnection);
        
        // Verify callback was called
        assertTrue(callbackCalled.get());
        assertNotNull(receivedModel.get());
    }

    @Test
    void testOnRequestFinishedWithNullCallback() throws Exception {
        // Create a mock CSHttpConnection
        CSHttpConnection mockConnection = createMockConnection();
        
        // Set controller
        Field controllerField = CSHttpConnection.class.getDeclaredField("controller");
        controllerField.setAccessible(true);
        controllerField.set(mockConnection, Constants.FETCHASSETS);
        
        // Create response with single asset
        LinkedHashMap<String, Object> assetMap = new LinkedHashMap<>();
        assetMap.put("uid", "test_asset_uid");
        assetMap.put("filename", "test.jpg");
        
        JSONObject response = new JSONObject();
        Field mapField = JSONObject.class.getDeclaredField("map");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> internalMap = (Map<String, Object>) mapField.get(response);
        internalMap.put("asset", assetMap);
        
        Field responseField = CSHttpConnection.class.getDeclaredField("responseJSON");
        responseField.setAccessible(true);
        responseField.set(mockConnection, response);
        
        // Don't set callback (null)
        Field callbackField = CSHttpConnection.class.getDeclaredField("callBackObject");
        callbackField.setAccessible(true);
        callbackField.set(mockConnection, null);
        
        // Create CSConnectionRequest with Asset
        CSConnectionRequest request = new CSConnectionRequest(asset);
        
        // Call onRequestFinished - should not throw even with null callback
        assertDoesNotThrow(() -> request.onRequestFinished(mockConnection));
    }

    // ========== ON REQUEST FAILED TESTS ==========

    @Test
    void testOnRequestFailedWithErrorMessage() throws Exception {
        JSONObject errorResponse = new JSONObject();
        errorResponse.put("error_message", "Test error message");
        errorResponse.put("error_code", 404);
        errorResponse.put("errors", "Test error details");
        
        AtomicBoolean callbackCalled = new AtomicBoolean(false);
        AtomicReference<Error> receivedError = new AtomicReference<>();
        
        ResultCallBack callback = new ResultCallBack() {
            @Override
            public void onRequestFail(ResponseType responseType, Error error) {
                callbackCalled.set(true);
                receivedError.set(error);
            }
        };
        
        CSConnectionRequest request = new CSConnectionRequest(stack);
        Field callbackField = CSConnectionRequest.class.getDeclaredField("resultCallBack");
        callbackField.setAccessible(true);
        callbackField.set(request, callback);
        
        request.onRequestFailed(errorResponse, 404, callback);
        
        assertTrue(callbackCalled.get());
        assertNotNull(receivedError.get());
        assertEquals("Test error message", receivedError.get().getErrorMessage());
        assertEquals(404, receivedError.get().getErrorCode());
        assertEquals("Test error details", receivedError.get().getErrorDetail());
    }

    @Test
    void testOnRequestFailedWithoutErrorCode() throws Exception {
        JSONObject errorResponse = new JSONObject();
        errorResponse.put("error_message", "Test error message");
        
        AtomicBoolean callbackCalled = new AtomicBoolean(false);
        
        ResultCallBack callback = new ResultCallBack() {
            @Override
            public void onRequestFail(ResponseType responseType, Error error) {
                callbackCalled.set(true);
            }
        };
        
        CSConnectionRequest request = new CSConnectionRequest(stack);
        Field callbackField = CSConnectionRequest.class.getDeclaredField("resultCallBack");
        callbackField.setAccessible(true);
        callbackField.set(request, callback);
        
        request.onRequestFailed(errorResponse, 500, callback);
        
        assertTrue(callbackCalled.get());
    }

    @Test
    void testOnRequestFailedWithNullCallback() throws Exception {
        JSONObject errorResponse = new JSONObject();
        errorResponse.put("error_message", "Test error message");
        
        CSConnectionRequest request = new CSConnectionRequest(stack);
        Field callbackField = CSConnectionRequest.class.getDeclaredField("resultCallBack");
        callbackField.setAccessible(true);
        callbackField.set(request, null);
        
        // Should not throw even with null callback
        assertDoesNotThrow(() -> request.onRequestFailed(errorResponse, 500, null));
    }

    // ========== HELPER METHODS ==========

    private CSHttpConnection createMockConnection() throws Exception {
        // Create a simple IRequestModelHTTP implementation
        IRequestModelHTTP mockRequest = new IRequestModelHTTP() {
            @Override
            public void sendRequest() {}
            
            @Override
            public void onRequestFailed(JSONObject error, int statusCode, ResultCallBack callBackObject) {}
            
            @Override
            public void onRequestFinished(CSHttpConnection request) {}
        };
        
        // Create a CSHttpConnection with required parameters
        CSHttpConnection connection = new CSHttpConnection("https://api.contentstack.io/test", mockRequest);
        return connection;
    }
}

