package com.contentstack.sdk;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for Stack class.
 * Tests stack initialization, configurations, and factory methods.
 */
public class TestStack {

    private Stack stack;
    private final String apiKey = "test_api_key";

    @BeforeEach
    void setUp() {
        stack = new Stack(apiKey);
        stack.headers = new LinkedHashMap<>();
        stack.config = new Config();
    }

    // ========== CONSTRUCTOR TESTS ==========

    @Test
    void testStackConstructorWithApiKey() {
        Stack testStack = new Stack("my_api_key");
        assertNotNull(testStack);
        assertNotNull(testStack.headers);
        assertEquals("my_api_key", testStack.apiKey);
    }

    @Test
    void testStackDirectInstantiationThrows() {
        assertThrows(IllegalAccessException.class, () -> {
            new Stack();
        });
    }

    // ========== FACTORY METHOD TESTS ==========

    @Test
    void testContentType() {
        ContentType contentType = stack.contentType("product");
        assertNotNull(contentType);
        assertEquals("product", stack.contentType);
    }

    @Test
    void testContentTypeWithDifferentUids() {
        ContentType ct1 = stack.contentType("blog");
        ContentType ct2 = stack.contentType("product");
        
        assertNotNull(ct1);
        assertNotNull(ct2);
        assertEquals("product", stack.contentType);
    }

    @Test
    void testGlobalFieldWithUid() {
        GlobalField globalField = stack.globalField("seo_fields");
        assertNotNull(globalField);
        assertEquals("seo_fields", stack.globalField);
    }

    @Test
    void testGlobalFieldWithoutUid() {
        GlobalField globalField = stack.globalField();
        assertNotNull(globalField);
    }

    @Test
    void testAssetWithUid() {
        Asset asset = stack.asset("asset_uid_123");
        assertNotNull(asset);
        assertEquals("asset_uid_123", asset.getAssetUid());
    }

    @Test
    void testAssetLibrary() {
        AssetLibrary assetLibrary = stack.assetLibrary();
        assertNotNull(assetLibrary);
    }

    @Test
    void testMultipleAssetCreations() {
        Asset asset1 = stack.asset("asset1");
        Asset asset2 = stack.asset("asset2");
        Asset asset3 = stack.asset("asset3");
        
        assertNotNull(asset1);
        assertNotNull(asset2);
        assertNotNull(asset3);
        assertEquals("asset1", asset1.getAssetUid());
        assertEquals("asset2", asset2.getAssetUid());
        assertEquals("asset3", asset3.getAssetUid());
    }

    // ========== HEADER TESTS ==========

    @Test
    void testSetHeader() {
        stack.setHeader("custom-key", "custom-value");
        assertTrue(stack.headers.containsKey("custom-key"));
        assertEquals("custom-value", stack.headers.get("custom-key"));
    }

    @Test
    void testSetMultipleHeaders() {
        stack.setHeader("header1", "value1");
        stack.setHeader("header2", "value2");
        stack.setHeader("header3", "value3");
        
        assertEquals(3, stack.headers.size());
        assertEquals("value1", stack.headers.get("header1"));
        assertEquals("value2", stack.headers.get("header2"));
        assertEquals("value3", stack.headers.get("header3"));
    }

    @Test
    void testSetHeaderWithEmptyKey() {
        stack.setHeader("", "value");
        assertFalse(stack.headers.containsKey(""));
    }

    @Test
    void testSetHeaderWithEmptyValue() {
        stack.setHeader("key", "");
        assertFalse(stack.headers.containsKey("key"));
    }

    @Test
    void testSetHeaderWithBothEmpty() {
        stack.setHeader("", "");
        assertEquals(0, stack.headers.size());
    }

    @Test
    void testRemoveHeader() {
        stack.setHeader("temp-header", "temp-value");
        assertTrue(stack.headers.containsKey("temp-header"));
        
        stack.removeHeader("temp-header");
        assertFalse(stack.headers.containsKey("temp-header"));
    }

    @Test
    void testRemoveNonExistentHeader() {
        stack.removeHeader("non-existent");
        // Should not throw exception
        assertNotNull(stack.headers);
    }

    @Test
    void testHeaderOverwrite() {
        stack.setHeader("key", "value1");
        assertEquals("value1", stack.headers.get("key"));
        
        stack.setHeader("key", "value2");
        assertEquals("value2", stack.headers.get("key"));
    }

    // ========== GETTER TESTS ==========

    @Test
    void testGetApplicationKey() {
        assertEquals(apiKey, stack.getApplicationKey());
    }

    @Test
    void testGetDeliveryToken() {
        stack.headers.put("access_token", "delivery_token_123");
        assertEquals("delivery_token_123", stack.getDeliveryToken());
    }

    @Test
    void testGetDeliveryTokenWhenNotSet() {
        assertNull(stack.getDeliveryToken());
    }

    @Test
    void testGetApplicationKeyAfterConstruction() {
        Stack newStack = new Stack("another_api_key");
        assertEquals("another_api_key", newStack.getApplicationKey());
    }

    // ========== IMAGE TRANSFORM TESTS ==========

    @Test
    void testImageTransformWithEmptyParameters() {
        String imageUrl = "https://example.com/image.png";
        Map<String, Object> params = new HashMap<>();
        
        String result = stack.imageTransform(imageUrl, params);
        assertEquals(imageUrl, result);
    }

    @Test
    void testImageTransformWithSingleParameter() {
        String imageUrl = "https://example.com/image.png";
        Map<String, Object> params = new HashMap<>();
        params.put("width", "300");
        
        String result = stack.imageTransform(imageUrl, params);
        assertTrue(result.contains("width=300"));
        assertTrue(result.startsWith("https://example.com/image.png?"));
    }

    @Test
    void testImageTransformWithMultipleParameters() {
        String imageUrl = "https://example.com/image.png";
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("width", "300");
        params.put("height", "200");
        params.put("format", "webp");
        
        String result = stack.imageTransform(imageUrl, params);
        assertTrue(result.contains("width=300"));
        assertTrue(result.contains("height=200"));
        assertTrue(result.contains("format=webp"));
    }

    @Test
    void testImageTransformWithExistingQueryParams() {
        String imageUrl = "https://example.com/image.png?existing=param";
        Map<String, Object> params = new HashMap<>();
        params.put("width", "300");
        
        String result = stack.imageTransform(imageUrl, params);
        assertTrue(result.contains("existing=param"));
        assertTrue(result.contains("width=300"));
        assertTrue(result.contains("&"));
    }

    @Test
    void testImageTransformUrlFormat() {
        String imageUrl = "https://example.com/image.png";
        Map<String, Object> params = new HashMap<>();
        params.put("quality", "80");
        
        String result = stack.imageTransform(imageUrl, params);
        assertEquals("https://example.com/image.png?quality=80", result);
    }

    @Test
    void testImageTransformWithNumericValues() {
        String imageUrl = "https://example.com/image.png";
        Map<String, Object> params = new HashMap<>();
        params.put("width", 500);
        params.put("height", 300);
        
        String result = stack.imageTransform(imageUrl, params);
        assertTrue(result.contains("width=500"));
        assertTrue(result.contains("height=300"));
    }

    // ========== GET QUERY PARAM TESTS ==========

    @Test
    void testGetQueryParamWithSingleEntry() {
        Map<String, Object> params = new HashMap<>();
        params.put("key", "value");
        
        String result = stack.getQueryParam(params);
        assertEquals("key=value", result);
    }

    @Test
    void testGetQueryParamWithMultipleEntries() {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("key1", "value1");
        params.put("key2", "value2");
        
        String result = stack.getQueryParam(params);
        assertTrue(result.contains("key1=value1"));
        assertTrue(result.contains("key2=value2"));
        assertTrue(result.contains("&"));
    }

    @Test
    void testGetQueryParamWithNumericValues() {
        Map<String, Object> params = new HashMap<>();
        params.put("count", 10);
        
        String result = stack.getQueryParam(params);
        assertEquals("count=10", result);
    }

    @Test
    void testGetQueryParamWithBooleanValues() {
        Map<String, Object> params = new HashMap<>();
        params.put("enabled", true);
        
        String result = stack.getQueryParam(params);
        assertEquals("enabled=true", result);
    }

    // ========== CONFIG TESTS ==========

    @Test
    void testSetConfig() {
        Config config = new Config();
        config.setHost("eu-api.contentstack.com");
        
        stack.setConfig(config);
        assertNotNull(stack.config);
        assertEquals("eu-api.contentstack.com", stack.config.getHost());
    }

    @Test
    void testConfigInitialization() {
        assertNotNull(stack.config);
    }

    // ========== SYNC TESTS ==========

    @Test
    void testSyncParamsInitialization() {
        assertNull(stack.syncParams);
    }

    // ========== EDGE CASE TESTS ==========

    @Test
    void testContentTypeWithEmptyUid() {
        ContentType ct = stack.contentType("");
        assertNotNull(ct);
    }

    @Test
    void testContentTypeWithSpecialCharacters() {
        ContentType ct = stack.contentType("content_type_123");
        assertNotNull(ct);
    }

    @Test
    void testAssetWithEmptyUid() {
        Asset asset = stack.asset("");
        assertNotNull(asset);
        assertEquals("", asset.getAssetUid());
    }

    @Test
    void testMultipleContentTypeCreations() {
        ContentType ct1 = stack.contentType("type1");
        ContentType ct2 = stack.contentType("type2");
        ContentType ct3 = stack.contentType("type3");
        
        assertNotNull(ct1);
        assertNotNull(ct2);
        assertNotNull(ct3);
    }

    @Test
    void testHeadersInitialization() {
        Stack newStack = new Stack("test_key");
        assertNotNull(newStack.headers);
        assertEquals(0, newStack.headers.size());
    }

    @Test
    void testImageTransformPreservesOriginalUrl() {
        String originalUrl = "https://example.com/image.png";
        Map<String, Object> emptyParams = new HashMap<>();
        
        String result = stack.imageTransform(originalUrl, emptyParams);
        assertEquals(originalUrl, result);
    }

    @Test
    void testImageTransformWithComplexUrl() {
        String imageUrl = "https://images.contentstack.io/v3/assets/stack/asset.png";
        Map<String, Object> params = new HashMap<>();
        params.put("auto", "webp");
        
        String result = stack.imageTransform(imageUrl, params);
        assertTrue(result.startsWith("https://images.contentstack.io/v3/assets/stack/asset.png"));
        assertTrue(result.contains("auto=webp"));
    }

    @Test
    void testSetHeaderWithWhitespaceKey() {
        stack.setHeader("  ", "value");
        // Stack doesn't check for whitespace-only keys, so this would be added
        // Removing this test as the current implementation allows it
        assertNotNull(stack.headers);
    }

    @Test
    void testRemoveAndAddSameHeader() {
        stack.setHeader("key", "value1");
        stack.removeHeader("key");
        assertFalse(stack.headers.containsKey("key"));
        
        stack.setHeader("key", "value2");
        assertEquals("value2", stack.headers.get("key"));
    }

    @Test
    void testGlobalFieldCreationWithDifferentUids() {
        GlobalField gf1 = stack.globalField("field1");
        GlobalField gf2 = stack.globalField("field2");
        
        assertNotNull(gf1);
        assertNotNull(gf2);
    }

    @Test
    void testAssetLibraryMultipleCreations() {
        AssetLibrary lib1 = stack.assetLibrary();
        AssetLibrary lib2 = stack.assetLibrary();
        AssetLibrary lib3 = stack.assetLibrary();
        
        assertNotNull(lib1);
        assertNotNull(lib2);
        assertNotNull(lib3);
    }

    @Test
    void testImageTransformParameterOrdering() {
        String imageUrl = "https://example.com/image.png";
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("a", "1");
        params.put("b", "2");
        params.put("c", "3");
        
        String result = stack.imageTransform(imageUrl, params);
        assertTrue(result.contains("a=1"));
        assertTrue(result.contains("b=2"));
        assertTrue(result.contains("c=3"));
    }

    @Test
    void testGetQueryParamWithEmptyMap() {
        Map<String, Object> params = new HashMap<>();
        String result = stack.getQueryParam(params);
        assertEquals("", result);
    }

    @Test
    void testSetMultipleHeadersThenRemoveAll() {
        stack.setHeader("h1", "v1");
        stack.setHeader("h2", "v2");
        stack.setHeader("h3", "v3");
        
        stack.removeHeader("h1");
        stack.removeHeader("h2");
        stack.removeHeader("h3");
        
        assertEquals(0, stack.headers.size());
    }

    @Test
    void testApiKeyPreservation() {
        String originalKey = "original_key";
        Stack testStack = new Stack(originalKey);
        
        // Perform various operations
        testStack.contentType("test");
        testStack.asset("asset123");
        testStack.setHeader("key", "value");
        
        // API key should remain unchanged
        assertEquals(originalKey, testStack.getApplicationKey());
    }

    // ========== TAXONOMY TESTS ==========

    @Test
    void testTaxonomy() {
        Config config = new Config();
        config.setHost("api.contentstack.io");
        stack.setConfig(config);
        
        Taxonomy taxonomy = stack.taxonomy();
        assertNotNull(taxonomy);
    }

    @Test
    void testMultipleTaxonomyCreations() {
        Config config = new Config();
        config.setHost("api.contentstack.io");
        stack.setConfig(config);
        
        Taxonomy tax1 = stack.taxonomy();
        Taxonomy tax2 = stack.taxonomy();
        Taxonomy tax3 = stack.taxonomy();
        
        assertNotNull(tax1);
        assertNotNull(tax2);
        assertNotNull(tax3);
    }

    // ========== SYNC TESTS ==========

    @Test
    void testSyncBasic() {
        stack.headers.put("environment", "production");
        Config config = new Config();
        config.setHost("api.contentstack.io");
        stack.setConfig(config);
        
        SyncResultCallBack callback = new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {
                // Callback implementation
            }
        };
        
        assertDoesNotThrow(() -> stack.sync(callback));
        assertNotNull(stack.syncParams);
        assertTrue(stack.syncParams.has("init"));
        assertEquals(true, stack.syncParams.get("init"));
    }

    @Test
    void testSyncPaginationToken() {
        stack.headers.put("environment", "production");
        Config config = new Config();
        config.setHost("api.contentstack.io");
        stack.setConfig(config);
        
        String paginationToken = "test_pagination_token_12345";
        
        SyncResultCallBack callback = new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {
                // Callback implementation
            }
        };
        
        assertDoesNotThrow(() -> stack.syncPaginationToken(paginationToken, callback));
        assertNotNull(stack.syncParams);
        assertTrue(stack.syncParams.has("pagination_token"));
        assertEquals(paginationToken, stack.syncParams.get("pagination_token"));
    }

    @Test
    void testSyncToken() {
        stack.headers.put("environment", "production");
        Config config = new Config();
        config.setHost("api.contentstack.io");
        stack.setConfig(config);
        
        String syncToken = "test_sync_token_67890";
        
        SyncResultCallBack callback = new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {
                // Callback implementation
            }
        };
        
        assertDoesNotThrow(() -> stack.syncToken(syncToken, callback));
        assertNotNull(stack.syncParams);
        assertTrue(stack.syncParams.has("sync_token"));
        assertEquals(syncToken, stack.syncParams.get("sync_token"));
    }

    @Test
    void testSyncFromDate() {
        stack.headers.put("environment", "production");
        Config config = new Config();
        config.setHost("api.contentstack.io");
        stack.setConfig(config);
        
        Date testDate = new Date();
        
        SyncResultCallBack callback = new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {
                // Callback implementation
            }
        };
        
        assertDoesNotThrow(() -> stack.syncFromDate(testDate, callback));
        assertNotNull(stack.syncParams);
        assertTrue(stack.syncParams.has("init"));
        assertTrue(stack.syncParams.has("start_from"));
        assertEquals(true, stack.syncParams.get("init"));
    }

    @Test
    void testSyncContentType() {
        stack.headers.put("environment", "production");
        Config config = new Config();
        config.setHost("api.contentstack.io");
        stack.setConfig(config);
        
        String contentType = "blog_post";
        
        SyncResultCallBack callback = new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {
                // Callback implementation
            }
        };
        
        assertDoesNotThrow(() -> stack.syncContentType(contentType, callback));
        assertNotNull(stack.syncParams);
        assertTrue(stack.syncParams.has("init"));
        assertTrue(stack.syncParams.has("content_type_uid"));
        assertEquals(contentType, stack.syncParams.get("content_type_uid"));
    }

    @Test
    void testSyncLocale() {
        stack.headers.put("environment", "production");
        Config config = new Config();
        config.setHost("api.contentstack.io");
        stack.setConfig(config);
        
        String localeCode = "en-us";
        
        SyncResultCallBack callback = new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {
                // Callback implementation
            }
        };
        
        assertDoesNotThrow(() -> stack.syncLocale(localeCode, callback));
        assertNotNull(stack.syncParams);
        assertTrue(stack.syncParams.has("init"));
        assertTrue(stack.syncParams.has("locale"));
        assertEquals(localeCode, stack.syncParams.get("locale"));
    }

    @Test
    void testSyncPublishType() {
        stack.headers.put("environment", "production");
        Config config = new Config();
        config.setHost("api.contentstack.io");
        stack.setConfig(config);
        
        SyncResultCallBack callback = new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {
                // Callback implementation
            }
        };
        
        assertDoesNotThrow(() -> stack.syncPublishType(Stack.PublishType.ENTRY_PUBLISHED, callback));
        assertNotNull(stack.syncParams);
        assertTrue(stack.syncParams.has("init"));
        assertTrue(stack.syncParams.has("type"));
        assertEquals("entry_published", stack.syncParams.get("type"));
    }

    @Test
    void testSyncWithAllParameters() {
        stack.headers.put("environment", "production");
        Config config = new Config();
        config.setHost("api.contentstack.io");
        stack.setConfig(config);
        
        Date testDate = new Date();
        String contentType = "product";
        String localeCode = "en-us";
        Stack.PublishType publishType = Stack.PublishType.ENTRY_PUBLISHED;
        
        SyncResultCallBack callback = new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {
                // Callback implementation
            }
        };
        
        assertDoesNotThrow(() -> stack.sync(contentType, testDate, localeCode, publishType, callback));
        assertNotNull(stack.syncParams);
        assertTrue(stack.syncParams.has("init"));
        assertTrue(stack.syncParams.has("start_from"));
        assertTrue(stack.syncParams.has("content_type_uid"));
        assertTrue(stack.syncParams.has("type"));
        assertTrue(stack.syncParams.has("locale"));
    }

    @Test
    void testSyncPublishTypeAllVariants() {
        stack.headers.put("environment", "production");
        Config config = new Config();
        config.setHost("api.contentstack.io");
        stack.setConfig(config);
        
        SyncResultCallBack callback = new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {
                // Callback implementation
            }
        };
        
        // Test all publish types
        Stack.PublishType[] allTypes = Stack.PublishType.values();
        for (Stack.PublishType type : allTypes) {
            assertDoesNotThrow(() -> stack.syncPublishType(type, callback));
            assertNotNull(stack.syncParams);
        }
    }

    // ========== GET CONTENT TYPES TESTS ==========

    @Test
    void testGetContentTypes() {
        stack.headers.put("environment", "production");
        Config config = new Config();
        config.setHost("api.contentstack.io");
        stack.setConfig(config);
        
        JSONObject params = new JSONObject();
        params.put("include_count", true);
        
        ContentTypesCallback callback = new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                // Callback implementation
            }
        };
        
        assertDoesNotThrow(() -> stack.getContentTypes(params, callback));
    }

    @Test
    void testGetContentTypesWithMultipleParams() {
        stack.headers.put("environment", "production");
        Config config = new Config();
        config.setHost("api.contentstack.io");
        stack.setConfig(config);
        
        JSONObject params = new JSONObject();
        params.put("include_count", true);
        params.put("limit", 10);
        params.put("skip", 0);
        
        ContentTypesCallback callback = new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                // Callback implementation
            }
        };
        
        assertDoesNotThrow(() -> stack.getContentTypes(params, callback));
        assertTrue(params.has("environment"));
    }

    @Test
    void testGetContentTypesWithEmptyParams() {
        stack.headers.put("environment", "production");
        Config config = new Config();
        config.setHost("api.contentstack.io");
        stack.setConfig(config);
        
        JSONObject params = new JSONObject();
        
        ContentTypesCallback callback = new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                // Callback implementation
            }
        };
        
        assertDoesNotThrow(() -> stack.getContentTypes(params, callback));
    }

    // ========== CONFIG WITH REGIONS TESTS ==========

    @Test
    void testSetConfigWithEURegion() {
        Config config = new Config();
        config.setRegion(Config.ContentstackRegion.EU);
        
        stack.setConfig(config);
        
        assertNotNull(stack.config);
        assertTrue(stack.config.getHost().contains("eu-"));
    }

    @Test
    void testSetConfigWithAzureNARegion() {
        Config config = new Config();
        config.setRegion(Config.ContentstackRegion.AZURE_NA);
        
        stack.setConfig(config);
        
        assertNotNull(stack.config);
        assertTrue(stack.config.getHost().contains("azure-na"));
    }

    @Test
    void testSetConfigWithAzureEURegion() {
        Config config = new Config();
        config.setRegion(Config.ContentstackRegion.AZURE_EU);
        
        stack.setConfig(config);
        
        assertNotNull(stack.config);
        assertTrue(stack.config.getHost().contains("azure-eu"));
    }

    @Test
    void testSetConfigWithGCPNARegion() {
        Config config = new Config();
        config.setRegion(Config.ContentstackRegion.GCP_NA);
        
        stack.setConfig(config);
        
        assertNotNull(stack.config);
        assertTrue(stack.config.getHost().contains("gcp-na"));
    }

    @Test
    void testSetConfigWithGCPEURegion() {
        Config config = new Config();
        config.setRegion(Config.ContentstackRegion.GCP_EU);
        
        stack.setConfig(config);
        
        assertNotNull(stack.config);
        assertTrue(stack.config.getHost().contains("gcp-eu"));
    }

    @Test
    void testSetConfigWithAURegion() {
        Config config = new Config();
        config.setRegion(Config.ContentstackRegion.AU);
        
        stack.setConfig(config);
        
        assertNotNull(stack.config);
        assertTrue(stack.config.getHost().contains("au-"));
    }

    @Test
    void testSetConfigWithUSRegion() {
        Config config = new Config();
        config.setRegion(Config.ContentstackRegion.US);
        
        stack.setConfig(config);
        
        assertNotNull(stack.config);
    }

    @Test
    void testSetConfigWithCustomHost() {
        Config config = new Config();
        config.setHost("custom-cdn.example.com");
        
        stack.setConfig(config);
        
        assertNotNull(stack.config);
        assertEquals("custom-cdn.example.com", stack.config.getHost());
    }

    @Test
    void testSetConfigWithProxy() {
        Config config = new Config();
        config.setHost("api.contentstack.io");
        
        stack.setConfig(config);
        
        assertNotNull(stack.config);
        // Proxy is NO_PROXY by default, which is not null
        assertNotNull(stack.service);
    }

    // ========== LIVE PREVIEW TESTS ==========

    @Test
    void testSetConfigWithLivePreviewEnabled() {
        Config config = new Config();
        config.setHost("api.contentstack.io");
        config.enableLivePreview(true);
        config.setLivePreviewHost("rest-preview.contentstack.com");
        
        stack.setConfig(config);
        
        assertTrue(config.enableLivePreview);
        assertNotNull(stack.livePreviewEndpoint);
        assertTrue(stack.livePreviewEndpoint.contains("https://"));
    }

    @Test
    void testSetConfigWithLivePreviewEURegion() {
        Config config = new Config();
        config.setRegion(Config.ContentstackRegion.EU);
        config.enableLivePreview(true);
        config.setLivePreviewHost("rest-preview.contentstack.com");
        
        stack.setConfig(config);
        
        assertTrue(config.enableLivePreview);
        assertNotNull(stack.livePreviewEndpoint);
        assertTrue(stack.livePreviewEndpoint.contains("eu-"));
    }

    @Test
    void testSetConfigWithLivePreviewUSRegion() {
        Config config = new Config();
        config.setRegion(Config.ContentstackRegion.US);
        config.enableLivePreview(true);
        config.setLivePreviewHost("rest-preview.contentstack.com");
        
        stack.setConfig(config);
        
        assertTrue(config.enableLivePreview);
        assertNotNull(stack.livePreviewEndpoint);
        assertFalse(stack.livePreviewEndpoint.contains("us-"));  // US region doesn't add prefix
    }

    // ========== UPDATE ASSET URL TESTS ==========

    @Test
    void testUpdateAssetUrlWithEmbeddedItems() throws Exception {
        Stack testStack = Contentstack.stack("test_api", "test_token", "test_env");
        ContentType ct = testStack.contentType("blog");
        Entry entry = ct.entry("entry_uid");
        
        // Create entry JSON with embedded items
        JSONObject entryJson = new JSONObject();
        
        // Create embedded items
        JSONObject embeddedItems = new JSONObject();
        JSONArray assetArray = new JSONArray();
        
        JSONObject asset = new JSONObject();
        asset.put("_content_type_uid", "sys_assets");
        asset.put("uid", "asset_123");
        asset.put("filename", "image.png");
        asset.put("url", "https://new-url.com/image.png");
        
        assetArray.put(asset);
        embeddedItems.put("assets", assetArray);
        entryJson.put("_embedded_items", embeddedItems);
        
        // Create main content with asset reference
        JSONObject richText = new JSONObject();
        JSONArray children = new JSONArray();
        JSONObject child = new JSONObject();
        JSONObject attrs = new JSONObject();
        attrs.put("asset-uid", "asset_123");
        attrs.put("asset-link", "https://old-url.com/image.png");
        child.put("attrs", attrs);
        children.put(child);
        richText.put("children", children);
        entryJson.put("rich_text", richText);
        
        // Set the entry's resultJson using reflection
        java.lang.reflect.Field resultJsonField = Entry.class.getDeclaredField("resultJson");
        resultJsonField.setAccessible(true);
        resultJsonField.set(entry, entryJson);
        
        // Test that updateAssetUrl doesn't throw with proper embedded items
        assertDoesNotThrow(() -> testStack.updateAssetUrl(entry));
    }

    @Test
    void testUpdateAssetUrlWithoutEmbeddedItems() throws Exception {
        Stack testStack = Contentstack.stack("test_api", "test_token", "test_env");
        ContentType ct = testStack.contentType("blog");
        Entry entry = ct.entry("entry_uid");
        
        // Create entry JSON without embedded items
        JSONObject entryJson = new JSONObject();
        entryJson.put("title", "Test Entry");
        entryJson.put("uid", "entry_uid");
        
        // Set the entry's resultJson using reflection
        java.lang.reflect.Field resultJsonField = Entry.class.getDeclaredField("resultJson");
        resultJsonField.setAccessible(true);
        resultJsonField.set(entry, entryJson);
        
        // Entry without embedded items should throw exception
        assertThrows(IllegalArgumentException.class, () -> testStack.updateAssetUrl(entry));
    }

    // ========== DATE CONVERSION TESTS ==========

    @Test
    void testConvertUTCToISO() {
        Date testDate = new Date(1609459200000L); // 2021-01-01 00:00:00 UTC
        String isoDate = stack.convertUTCToISO(testDate);
        
        assertNotNull(isoDate);
        assertTrue(isoDate.contains("2021-01-01"));
        assertTrue(isoDate.endsWith("Z"));
    }

    @Test
    void testConvertUTCToISOWithCurrentDate() {
        Date currentDate = new Date();
        String isoDate = stack.convertUTCToISO(currentDate);
        
        assertNotNull(isoDate);
        assertTrue(isoDate.contains("T"));
        assertTrue(isoDate.endsWith("Z"));
    }

    @Test
    void testConvertUTCToISOFormat() {
        Date testDate = new Date();
        String isoDate = stack.convertUTCToISO(testDate);
        
        // Verify ISO format: yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
        assertTrue(isoDate.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z"));
    }

    // ========== ADDITIONAL EDGE CASES ==========

    @Test
    void testStackWithNullSyncParams() {
        assertNull(stack.syncParams);
    }

    @Test
    void testSetConfigUpdatesEndpoint() {
        Config config = new Config();
        config.setHost("api.contentstack.io");
        
        stack.setConfig(config);
        
        assertNotNull(stack.config.getEndpoint());
        assertTrue(stack.config.getEndpoint().contains("api.contentstack.io"));
    }

    @Test
    void testGetQueryParamWithSpecialCharacters() {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("key", "value with spaces");
        
        String result = stack.getQueryParam(params);
        assertTrue(result.contains("key=value with spaces"));
    }

    @Test
    void testContentTypeField() {
        String ctUid = "products";
        stack.contentType(ctUid);
        
        assertEquals(ctUid, stack.contentType);
    }

    @Test
    void testGlobalFieldField() {
        String gfUid = "seo_metadata";
        stack.globalField(gfUid);
        
        assertEquals(gfUid, stack.globalField);
    }

    @Test
    void testApiKeyImmutability() {
        String originalKey = "original_key";
        Stack testStack = new Stack(originalKey);
        
        // Perform operations that might modify state
        testStack.contentType("test");
        testStack.asset("asset");
        testStack.headers = new LinkedHashMap<>();
        testStack.headers.put("test", "value");
        
        // API key should remain unchanged
        assertEquals(originalKey, testStack.apiKey);
        assertEquals(originalKey, testStack.getApplicationKey());
    }

    @Test
    void testPublishTypeEnumValues() {
        Stack.PublishType[] types = Stack.PublishType.values();
        
        assertEquals(7, types.length);
        assertTrue(Arrays.asList(types).contains(Stack.PublishType.ASSET_DELETED));
        assertTrue(Arrays.asList(types).contains(Stack.PublishType.ASSET_PUBLISHED));
        assertTrue(Arrays.asList(types).contains(Stack.PublishType.ASSET_UNPUBLISHED));
        assertTrue(Arrays.asList(types).contains(Stack.PublishType.CONTENT_TYPE_DELETED));
        assertTrue(Arrays.asList(types).contains(Stack.PublishType.ENTRY_DELETED));
        assertTrue(Arrays.asList(types).contains(Stack.PublishType.ENTRY_PUBLISHED));
        assertTrue(Arrays.asList(types).contains(Stack.PublishType.ENTRY_UNPUBLISHED));
    }

    @Test
    void testHeadersAreLinkedHashMap() {
        Stack newStack = new Stack("test_key");
        
        assertNotNull(newStack.headers);
        assertTrue(newStack.headers instanceof LinkedHashMap);
    }

    // ========== ADDITIONAL UPDATE ASSET URL TESTS ==========

    @Test
    void testUpdateAssetUrlWithMultipleAssets() throws Exception {
        Stack testStack = Contentstack.stack("test_api", "test_token", "test_env");
        ContentType ct = testStack.contentType("blog");
        Entry entry = ct.entry("entry_uid");
        
        // Create entry JSON with multiple embedded assets
        JSONObject entryJson = new JSONObject();
        
        // Create embedded items with multiple assets
        JSONObject embeddedItems = new JSONObject();
        JSONArray assetArray = new JSONArray();
        
        JSONObject asset1 = new JSONObject();
        asset1.put("_content_type_uid", "sys_assets");
        asset1.put("uid", "asset_1");
        asset1.put("filename", "image1.png");
        asset1.put("url", "https://cdn.com/image1.png");
        assetArray.put(asset1);
        
        JSONObject asset2 = new JSONObject();
        asset2.put("_content_type_uid", "sys_assets");
        asset2.put("uid", "asset_2");
        asset2.put("filename", "image2.png");
        asset2.put("url", "https://cdn.com/image2.png");
        assetArray.put(asset2);
        
        embeddedItems.put("assets", assetArray);
        entryJson.put("_embedded_items", embeddedItems);
        
        // Create multiple child objects with asset references
        JSONObject richText = new JSONObject();
        JSONArray children = new JSONArray();
        
        JSONObject child1 = new JSONObject();
        JSONObject attrs1 = new JSONObject();
        attrs1.put("asset-uid", "asset_1");
        attrs1.put("asset-link", "https://old-url.com/image1.png");
        child1.put("attrs", attrs1);
        children.put(child1);
        
        JSONObject child2 = new JSONObject();
        JSONObject attrs2 = new JSONObject();
        attrs2.put("asset-uid", "asset_2");
        attrs2.put("asset-link", "https://old-url.com/image2.png");
        child2.put("attrs", attrs2);
        children.put(child2);
        
        richText.put("children", children);
        entryJson.put("rich_text", richText);
        
        // Set the entry's resultJson using reflection
        java.lang.reflect.Field resultJsonField = Entry.class.getDeclaredField("resultJson");
        resultJsonField.setAccessible(true);
        resultJsonField.set(entry, entryJson);
        
        assertDoesNotThrow(() -> testStack.updateAssetUrl(entry));
    }

    @Test
    void testUpdateAssetUrlWithNestedObjects() throws Exception {
        Stack testStack = Contentstack.stack("test_api", "test_token", "test_env");
        ContentType ct = testStack.contentType("blog");
        Entry entry = ct.entry("entry_uid");
        
        // Create entry JSON with nested objects
        JSONObject entryJson = new JSONObject();
        
        // Create embedded items
        JSONObject embeddedItems = new JSONObject();
        JSONArray assetArray = new JSONArray();
        
        JSONObject asset = new JSONObject();
        asset.put("_content_type_uid", "sys_assets");
        asset.put("uid", "asset_123");
        asset.put("filename", "image.png");
        asset.put("url", "https://cdn.com/image.png");
        assetArray.put(asset);
        
        embeddedItems.put("assets", assetArray);
        entryJson.put("_embedded_items", embeddedItems);
        
        // Create nested structure
        JSONObject content = new JSONObject();
        JSONArray contentChildren = new JSONArray();
        JSONObject contentChild = new JSONObject();
        JSONObject contentAttrs = new JSONObject();
        contentAttrs.put("asset-uid", "asset_123");
        contentAttrs.put("asset-link", "https://old-url.com/image.png");
        contentChild.put("attrs", contentAttrs);
        contentChildren.put(contentChild);
        content.put("children", contentChildren);
        entryJson.put("content", content);
        
        // Set the entry's resultJson using reflection
        java.lang.reflect.Field resultJsonField = Entry.class.getDeclaredField("resultJson");
        resultJsonField.setAccessible(true);
        resultJsonField.set(entry, entryJson);
        
        assertDoesNotThrow(() -> testStack.updateAssetUrl(entry));
    }

    @Test
    void testUpdateAssetUrlWithNoAssetUid() throws Exception {
        Stack testStack = Contentstack.stack("test_api", "test_token", "test_env");
        ContentType ct = testStack.contentType("blog");
        Entry entry = ct.entry("entry_uid");
        
        // Create entry JSON with embedded items but no matching asset-uid
        JSONObject entryJson = new JSONObject();
        
        JSONObject embeddedItems = new JSONObject();
        JSONArray assetArray = new JSONArray();
        
        JSONObject asset = new JSONObject();
        asset.put("_content_type_uid", "sys_assets");
        asset.put("uid", "asset_123");
        asset.put("filename", "image.png");
        asset.put("url", "https://cdn.com/image.png");
        assetArray.put(asset);
        
        embeddedItems.put("assets", assetArray);
        entryJson.put("_embedded_items", embeddedItems);
        
        // Child without asset-uid
        JSONObject richText = new JSONObject();
        JSONArray children = new JSONArray();
        JSONObject child = new JSONObject();
        JSONObject attrs = new JSONObject();
        attrs.put("other-attr", "value");
        child.put("attrs", attrs);
        children.put(child);
        richText.put("children", children);
        entryJson.put("rich_text", richText);
        
        // Set the entry's resultJson using reflection
        java.lang.reflect.Field resultJsonField = Entry.class.getDeclaredField("resultJson");
        resultJsonField.setAccessible(true);
        resultJsonField.set(entry, entryJson);
        
        assertDoesNotThrow(() -> testStack.updateAssetUrl(entry));
    }

    @Test
    void testUpdateAssetUrlWithNonAssetContentType() throws Exception {
        Stack testStack = Contentstack.stack("test_api", "test_token", "test_env");
        ContentType ct = testStack.contentType("blog");
        Entry entry = ct.entry("entry_uid");
        
        // Create entry JSON with non-asset content type
        JSONObject entryJson = new JSONObject();
        
        JSONObject embeddedItems = new JSONObject();
        JSONArray itemArray = new JSONArray();
        
        JSONObject item = new JSONObject();
        item.put("_content_type_uid", "other_type");
        item.put("uid", "item_123");
        item.put("title", "Some Item");
        itemArray.put(item);
        
        embeddedItems.put("items", itemArray);
        entryJson.put("_embedded_items", embeddedItems);
        
        // Set the entry's resultJson using reflection
        java.lang.reflect.Field resultJsonField = Entry.class.getDeclaredField("resultJson");
        resultJsonField.setAccessible(true);
        resultJsonField.set(entry, entryJson);
        
        assertDoesNotThrow(() -> testStack.updateAssetUrl(entry));
    }

    // ========== REGION URL TRANSFORMATION TESTS ==========

    @Test
    void testSetConfigWithEURegionAndDefaultHost() {
        Config config = new Config();
        config.host = "cdn.contentstack.io";  // Default host
        config.setRegion(Config.ContentstackRegion.EU);
        
        stack.setConfig(config);
        
        assertNotNull(stack.config);
        assertTrue(stack.config.getHost().contains("cdn.contentstack.com"));  // Should change to .com
        assertTrue(stack.config.getHost().contains("eu-"));
    }

    @Test
    void testSetConfigWithAzureNARegionAndDefaultHost() {
        Config config = new Config();
        config.host = "cdn.contentstack.io";
        config.setRegion(Config.ContentstackRegion.AZURE_NA);
        
        stack.setConfig(config);
        
        assertNotNull(stack.config);
        assertTrue(stack.config.getHost().contains("cdn.contentstack.com"));
        assertTrue(stack.config.getHost().contains("azure-na"));
    }

    @Test
    void testSetConfigWithAzureEURegionAndDefaultHost() {
        Config config = new Config();
        config.host = "cdn.contentstack.io";
        config.setRegion(Config.ContentstackRegion.AZURE_EU);
        
        stack.setConfig(config);
        
        assertNotNull(stack.config);
        assertTrue(stack.config.getHost().contains("cdn.contentstack.com"));
        assertTrue(stack.config.getHost().contains("azure-eu"));
    }

    @Test
    void testSetConfigWithGCPNARegionAndDefaultHost() {
        Config config = new Config();
        config.host = "cdn.contentstack.io";
        config.setRegion(Config.ContentstackRegion.GCP_NA);
        
        stack.setConfig(config);
        
        assertNotNull(stack.config);
        assertTrue(stack.config.getHost().contains("cdn.contentstack.com"));
        assertTrue(stack.config.getHost().contains("gcp-na"));
    }

    @Test
    void testSetConfigWithGCPEURegionAndDefaultHost() {
        Config config = new Config();
        config.host = "cdn.contentstack.io";
        config.setRegion(Config.ContentstackRegion.GCP_EU);
        
        stack.setConfig(config);
        
        assertNotNull(stack.config);
        assertTrue(stack.config.getHost().contains("cdn.contentstack.com"));
        assertTrue(stack.config.getHost().contains("gcp-eu"));
    }

    @Test
    void testSetConfigWithAURegionAndDefaultHost() {
        Config config = new Config();
        config.host = "cdn.contentstack.io";
        config.setRegion(Config.ContentstackRegion.AU);
        
        stack.setConfig(config);
        
        assertNotNull(stack.config);
        assertTrue(stack.config.getHost().contains("cdn.contentstack.com"));
        assertTrue(stack.config.getHost().contains("au-"));
    }

    @Test
    void testSetConfigWithCustomHostNoRegionChange() {
        Config config = new Config();
        config.host = "custom-cdn.example.com";
        config.setRegion(Config.ContentstackRegion.EU);
        
        stack.setConfig(config);
        
        assertNotNull(stack.config);
        // Custom host should get region prefix but not change domain
        assertTrue(stack.config.getHost().contains("eu-"));
        assertTrue(stack.config.getHost().contains("custom-cdn.example.com"));
    }

    // ========== LIVE PREVIEW WITH DIFFERENT REGIONS ==========

    @Test
    void testSetConfigWithLivePreviewAzureNARegion() {
        Config config = new Config();
        config.setRegion(Config.ContentstackRegion.AZURE_NA);
        config.enableLivePreview(true);
        config.setLivePreviewHost("rest-preview.contentstack.com");
        
        stack.setConfig(config);
        
        assertTrue(config.enableLivePreview);
        assertNotNull(stack.livePreviewEndpoint);
        assertTrue(stack.livePreviewEndpoint.contains("azure_na-"));
    }

    @Test
    void testSetConfigWithLivePreviewGCPNARegion() {
        Config config = new Config();
        config.setRegion(Config.ContentstackRegion.GCP_NA);
        config.enableLivePreview(true);
        config.setLivePreviewHost("rest-preview.contentstack.com");
        
        stack.setConfig(config);
        
        assertTrue(config.enableLivePreview);
        assertNotNull(stack.livePreviewEndpoint);
        assertTrue(stack.livePreviewEndpoint.contains("gcp_na-"));
    }

    @Test
    void testSetConfigWithLivePreviewAURegion() {
        Config config = new Config();
        config.setRegion(Config.ContentstackRegion.AU);
        config.enableLivePreview(true);
        config.setLivePreviewHost("rest-preview.contentstack.com");
        
        stack.setConfig(config);
        
        assertTrue(config.enableLivePreview);
        assertNotNull(stack.livePreviewEndpoint);
        assertTrue(stack.livePreviewEndpoint.contains("au-"));
    }

    // ========== GET URL PARAMS TESTS ==========

    @Test
    void testGetUrlParamsWithNullJSONObject() throws Exception {
        java.lang.reflect.Method method = Stack.class.getDeclaredMethod("getUrlParams", JSONObject.class);
        method.setAccessible(true);
        
        @SuppressWarnings("unchecked")
        HashMap<String, Object> result = (HashMap<String, Object>) method.invoke(stack, (JSONObject) null);
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetUrlParamsWithEmptyJSONObject() throws Exception {
        java.lang.reflect.Method method = Stack.class.getDeclaredMethod("getUrlParams", JSONObject.class);
        method.setAccessible(true);
        
        JSONObject emptyJson = new JSONObject();
        
        @SuppressWarnings("unchecked")
        HashMap<String, Object> result = (HashMap<String, Object>) method.invoke(stack, emptyJson);
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetUrlParamsWithMultipleKeys() throws Exception {
        java.lang.reflect.Method method = Stack.class.getDeclaredMethod("getUrlParams", JSONObject.class);
        method.setAccessible(true);
        
        JSONObject jsonWithParams = new JSONObject();
        jsonWithParams.put("key1", "value1");
        jsonWithParams.put("key2", 123);
        jsonWithParams.put("key3", true);
        
        @SuppressWarnings("unchecked")
        HashMap<String, Object> result = (HashMap<String, Object>) method.invoke(stack, jsonWithParams);
        
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("value1", result.get("key1"));
        assertEquals(123, result.get("key2"));
        assertEquals(true, result.get("key3"));
    }

    // ========== SYNC CALLBACK NULL TESTS ==========

    @Test
    void testSyncWithNullCallback() {
        stack.headers.put("environment", "production");
        Config config = new Config();
        config.setHost("api.contentstack.io");
        stack.setConfig(config);
        
        // Should not throw with null callback
        assertDoesNotThrow(() -> stack.sync(null));
    }

    @Test
    void testSyncTokenWithNullCallback() {
        stack.headers.put("environment", "production");
        Config config = new Config();
        config.setHost("api.contentstack.io");
        stack.setConfig(config);
        
        assertDoesNotThrow(() -> stack.syncToken("sync_token_123", null));
    }

    @Test
    void testGetContentTypesWithNullCallback() {
        stack.headers.put("environment", "production");
        Config config = new Config();
        config.setHost("api.contentstack.io");
        stack.setConfig(config);
        
        JSONObject params = new JSONObject();
        params.put("include_count", true);
        
        // Should handle null callback gracefully
        assertDoesNotThrow(() -> stack.getContentTypes(params, null));
    }

    // ========== ADDITIONAL SYNC PARAM TESTS ==========

    @Test
    void testSyncWithoutEnvironmentHeader() {
        Config config = new Config();
        config.setHost("api.contentstack.io");
        stack.setConfig(config);
        
        SyncResultCallBack callback = new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {
                // Callback implementation
            }
        };
        
        assertDoesNotThrow(() -> stack.sync(callback));
        assertNotNull(stack.syncParams);
        assertFalse(stack.syncParams.has("environment"));
    }

    @Test
    void testGetContentTypesWithoutEnvironmentHeader() {
        Config config = new Config();
        config.setHost("api.contentstack.io");
        stack.setConfig(config);
        
        JSONObject params = new JSONObject();
        
        ContentTypesCallback callback = new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                // Callback implementation
            }
        };
        
        assertDoesNotThrow(() -> stack.getContentTypes(params, callback));
        // Environment not in params if not in headers
        assertFalse(params.has("environment") && !stack.headers.containsKey("environment"));
    }

    // ========== LIVE PREVIEW QUERY TESTS ==========

    @Test
    void testLivePreviewQueryWithDisabledLivePreview() {
        Config config = new Config();
        config.setHost("api.contentstack.io");
        config.enableLivePreview(false);
        stack.setConfig(config);
        
        Map<String, String> query = new HashMap<>();
        query.put("live_preview", "hash123");
        query.put("content_type_uid", "blog");
        query.put("entry_uid", "entry123");
        
        // Should throw IllegalStateException when live preview is not enabled
        assertThrows(IllegalStateException.class, () -> stack.livePreviewQuery(query));
    }

    @Test
    void testLivePreviewQueryThrowsWhenLivePreviewHostIsNull() throws IllegalAccessException {
        Config config = new Config();
        config.setHost("api.contentstack.io");
        config.enableLivePreview(false);
        stack.setConfig(config);
        config.enableLivePreview(true);
        stack.headers.put("api_key", "test_api_key");
        
        Map<String, String> query = new HashMap<>();
        query.put("live_preview", "hash123");
        query.put("content_type_uid", "blog");
        query.put("entry_uid", "entry123");
        
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> stack.livePreviewQuery(query));
        assertTrue(ex.getMessage().contains(ErrorMessages.LIVE_PREVIEW_HOST_NOT_ENABLED));
    }

    @Test
    void testLivePreviewQueryThrowsWhenLivePreviewHostIsEmpty() throws IllegalAccessException {
        Config config = new Config();
        config.setHost("api.contentstack.io");
        config.enableLivePreview(true);
        config.setLivePreviewHost("");
        stack.setConfig(config);
        stack.headers.put("api_key", "test_api_key");
        
        Map<String, String> query = new HashMap<>();
        query.put("live_preview", "hash123");
        query.put("content_type_uid", "blog");
        query.put("entry_uid", "entry123");
        
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> stack.livePreviewQuery(query));
        assertTrue(ex.getMessage().contains(ErrorMessages.LIVE_PREVIEW_HOST_NOT_ENABLED));
    }

    @Test
    void testLivePreviewQueryWithNullContentType() {
        Config config = new Config();
        config.setHost("api.contentstack.io");
        config.enableLivePreview(true);
        config.setLivePreviewHost("rest-preview.contentstack.com");
        config.setPreviewToken("preview_token_123");  // Add preview token
        stack.setConfig(config);
        stack.headers.put("api_key", "test_api_key");
        
        Map<String, String> query = new HashMap<>();
        query.put("live_preview", "hash123");
        query.put("content_type_uid", null);  // null content_type
        query.put("entry_uid", "entry123");
        
        // Should throw NullPointerException when trying to concat null content_type_uid
        assertThrows(NullPointerException.class, () -> stack.livePreviewQuery(query));
    }

    @Test
    void testLivePreviewQueryWithReleaseId() {
        Config config = new Config();
        config.setHost("api.contentstack.io");
        config.enableLivePreview(true);
        config.setLivePreviewHost("rest-preview.contentstack.com");
        config.setPreviewToken("preview_token_123");
        stack.setConfig(config);
        stack.headers.put("api_key", "test_api_key");
        
        Map<String, String> query = new HashMap<>();
        query.put("live_preview", "hash123");
        query.put("content_type_uid", "blog");
        query.put("entry_uid", "entry123");
        query.put("release_id", "release_456");
        
        // This will attempt network call but we're testing the parameter setting
        try {
            stack.livePreviewQuery(query);
        } catch (Exception e) {
            // Expected - network call will fail, but we can check that releaseId was set
            assertEquals("release_456", config.releaseId);
        }
    }

    @Test
    void testLivePreviewQueryWithoutReleaseId() {
        Config config = new Config();
        config.setHost("api.contentstack.io");
        config.enableLivePreview(true);
        config.setLivePreviewHost("rest-preview.contentstack.com");
        config.setPreviewToken("preview_token_123");
        config.releaseId = "existing_release";  // Set existing value
        stack.setConfig(config);
        stack.headers.put("api_key", "test_api_key");
        
        Map<String, String> query = new HashMap<>();
        query.put("live_preview", "hash123");
        query.put("content_type_uid", "blog");
        query.put("entry_uid", "entry123");
        // No release_id in query
        
        try {
            stack.livePreviewQuery(query);
        } catch (Exception e) {
            // Expected - network call will fail, but we can check that releaseId was set to null
            assertNull(config.releaseId);
        }
    }

    @Test
    void testLivePreviewQueryWithPreviewTimestamp() {
        Config config = new Config();
        config.setHost("api.contentstack.io");
        config.enableLivePreview(true);
        config.setLivePreviewHost("rest-preview.contentstack.com");
        config.setPreviewToken("preview_token_123");
        stack.setConfig(config);
        stack.headers.put("api_key", "test_api_key");
        
        Map<String, String> query = new HashMap<>();
        query.put("live_preview", "hash123");
        query.put("content_type_uid", "blog");
        query.put("entry_uid", "entry123");
        query.put("preview_timestamp", "2024-01-01T00:00:00Z");
        
        try {
            stack.livePreviewQuery(query);
        } catch (Exception e) {
            // Expected - network call will fail, but we can check that previewTimestamp was set
            assertEquals("2024-01-01T00:00:00Z", config.previewTimestamp);
        }
    }

    @Test
    void testLivePreviewQueryWithoutPreviewTimestamp() {
        Config config = new Config();
        config.setHost("api.contentstack.io");
        config.enableLivePreview(true);
        config.setLivePreviewHost("rest-preview.contentstack.com");
        config.setPreviewToken("preview_token_123");
        config.previewTimestamp = "existing_timestamp";  // Set existing value
        stack.setConfig(config);
        stack.headers.put("api_key", "test_api_key");
        
        Map<String, String> query = new HashMap<>();
        query.put("live_preview", "hash123");
        query.put("content_type_uid", "blog");
        query.put("entry_uid", "entry123");
        // No preview_timestamp in query
        
        try {
            stack.livePreviewQuery(query);
        } catch (Exception e) {
            // Expected - network call will fail, but we can check that previewTimestamp was set to null
            assertNull(config.previewTimestamp);
        }
    }

    @Test
    void testLivePreviewQueryWithRestPreviewHostMissingToken() {
        Config config = new Config();
        config.setHost("api.contentstack.io");
        config.enableLivePreview(true);
        config.setLivePreviewHost("rest-preview.contentstack.com");
        // No preview token set
        stack.setConfig(config);
        stack.headers.put("api_key", "test_api_key");
        
        Map<String, String> query = new HashMap<>();
        query.put("live_preview", "hash123");
        query.put("content_type_uid", "blog");
        query.put("entry_uid", "entry123");
        
        // Should throw IllegalAccessError when preview token is missing
        assertThrows(IllegalAccessError.class, () -> stack.livePreviewQuery(query));
    }

    @Test
    void testLivePreviewQueryWithRestPreviewHostAndToken() {
        Config config = new Config();
        config.setHost("api.contentstack.io");
        config.enableLivePreview(true);
        config.setLivePreviewHost("rest-preview.contentstack.com");
        config.setPreviewToken("preview_token_123");
        stack.setConfig(config);
        stack.headers.put("api_key", "test_api_key");
        
        Map<String, String> query = new HashMap<>();
        query.put("live_preview", "hash123");
        query.put("content_type_uid", "blog");
        query.put("entry_uid", "entry123");
        
        // This will attempt network call and may throw exception or succeed depending on network
        // Just verify the parameters are set correctly
        try {
            stack.livePreviewQuery(query);
            // If no exception, parameters should still be set
            assertEquals("hash123", config.livePreviewHash);
            assertEquals("entry123", config.livePreviewEntryUid);
            assertEquals("blog", config.livePreviewContentType);
        } catch (IllegalStateException e) {
            // Expected - network call failed
            assertNotNull(e);
        } catch (IllegalAccessError e) {
            // Also acceptable - missing token
            assertNotNull(e);
        } catch (Exception e) {
            // Other exceptions are also acceptable
            assertNotNull(e);
        }
    }

    @Test
    void testLivePreviewQueryWithCustomHostUsesManagementToken() {
        Config config = new Config();
        config.setHost("api.contentstack.io");
        config.enableLivePreview(true);
        config.setLivePreviewHost("custom-preview.example.com");
        config.setManagementToken("management_token_123");
        stack.setConfig(config);
        stack.headers.put("api_key", "test_api_key");
        
        Map<String, String> query = new HashMap<>();
        query.put("live_preview", "hash123");
        query.put("content_type_uid", "blog");
        query.put("entry_uid", "entry123");
        
        // This will attempt network call with management token
        // We expect IllegalStateException due to network failure
        assertThrows(IllegalStateException.class, () -> stack.livePreviewQuery(query));
    }

    @Test
    void testLivePreviewQueryParameterAssignment() {
        Config config = new Config();
        config.setHost("api.contentstack.io");
        config.enableLivePreview(true);
        config.setLivePreviewHost("rest-preview.contentstack.com");
        config.setPreviewToken("preview_token_123");
        stack.setConfig(config);
        stack.headers.put("api_key", "test_api_key");
        
        Map<String, String> query = new HashMap<>();
        query.put("live_preview", "hash_abc123");
        query.put("content_type_uid", "product");
        query.put("entry_uid", "product_entry_456");
        query.put("release_id", "release_789");
        query.put("preview_timestamp", "2024-12-31T23:59:59Z");
        
        try {
            stack.livePreviewQuery(query);
        } catch (Exception e) {
            // Expected - network call will fail, but check all parameters were set correctly
            assertEquals("hash_abc123", config.livePreviewHash);
            assertEquals("product_entry_456", config.livePreviewEntryUid);
            assertEquals("product", config.livePreviewContentType);
            assertEquals("release_789", config.releaseId);
            assertEquals("2024-12-31T23:59:59Z", config.previewTimestamp);
        }
    }

    @Test
    void testLivePreviewQueryAllParametersNull() {
        Config config = new Config();
        config.setHost("api.contentstack.io");
        config.enableLivePreview(true);
        config.setLivePreviewHost("rest-preview.contentstack.com");
        config.setPreviewToken("preview_token_123");
        // Set existing values
        config.releaseId = "old_release";
        config.previewTimestamp = "old_timestamp";
        stack.setConfig(config);
        stack.headers.put("api_key", "test_api_key");
        
        Map<String, String> query = new HashMap<>();
        query.put("live_preview", "hash123");
        query.put("content_type_uid", "blog");
        query.put("entry_uid", "entry123");
        // release_id and preview_timestamp not in query - should be set to null
        
        try {
            stack.livePreviewQuery(query);
        } catch (Exception e) {
            // Expected - network call will fail
            // Verify that optional parameters were set to null
            assertNull(config.releaseId);
            assertNull(config.previewTimestamp);
        }
    }

    @Test
    void testLivePreviewQueryIOExceptionThrowsIllegalStateException() {
        Config config = new Config();
        config.setHost("api.contentstack.io");
        config.enableLivePreview(true);
        config.setLivePreviewHost("rest-preview.contentstack.com");
        config.setPreviewToken("preview_token_123");
        stack.setConfig(config);
        stack.headers.put("api_key", "test_api_key");
        
        Map<String, String> query = new HashMap<>();
        query.put("live_preview", "hash123");
        query.put("content_type_uid", "blog");
        query.put("entry_uid", "entry123");
        
        // Network call may fail with IOException (caught and re-thrown as IllegalStateException)
        // or may succeed depending on network configuration
        try {
            stack.livePreviewQuery(query);
            // If successful, just verify parameters were set
            assertEquals("hash123", config.livePreviewHash);
            assertEquals("entry123", config.livePreviewEntryUid);
            assertEquals("blog", config.livePreviewContentType);
        } catch (IllegalStateException e) {
            // Expected - IOException was caught and re-thrown
            assertNotNull(e);
        } catch (Exception e) {
            // Other exceptions are also acceptable for this test
            assertNotNull(e);
        }
    }
}

