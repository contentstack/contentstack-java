package com.contentstack.sdk;

import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestStack {

    Stack stack;
    protected String API_KEY, DELIVERY_TOKEN, ENV;
    private final Logger logger = Logger.getLogger(TestStack.class.getName());

    @BeforeEach
    public void initBeforeTests() throws IllegalAccessException {
        Dotenv dotenv = Dotenv.load();
        API_KEY = dotenv.get("API_KEY");
        DELIVERY_TOKEN = dotenv.get("DELIVERY_TOKEN");
        ENV = dotenv.get("ENVIRONMENT");
        stack = Contentstack.stack(API_KEY, DELIVERY_TOKEN, ENV);
    }

    @Test
    void stackExceptionTesting() {
        IllegalAccessException thrown = Assertions.assertThrows(IllegalAccessException.class, Stack::new,
                "Can Not Access Private Modifier");
        assertEquals("Can Not Access Private Modifier", thrown.getLocalizedMessage());
    }

    @Test
    void testStackInitThrowErr() {
        try {
            stack = new Stack();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            assertEquals("Can Not Access Private Modifier", e.getLocalizedMessage());
        }
    }

    @Test
    void testConstantConstructor() {
        Assertions.assertNotNull(new Constants());
    }

    @Test
    void testStackAddHeader() {
        stack.setHeader("abcd", "justForTesting");
        assertTrue(stack.headers.containsKey("abcd"));
    }

    @Test
    void testStackRemoveHeader() {
        stack.removeHeader("abcd");
        Assertions.assertFalse(stack.headers.containsKey("abcd"));
    }

    @Test
    void testContentTypeInstance() {
        stack.contentType("product");
        assertEquals("product", stack.contentType);
    }

    @Test
    void testAssetWithUidInstance() {
        Asset instance = stack.asset("fakeUid");
        Assertions.assertNotNull(instance);
    }

    @Test
    void testAssetInstance() {
        Asset instance = stack.asset();
        Assertions.assertNotNull(instance);
    }

    @Test
    void testAssetLibraryInstance() {
        AssetLibrary instance = stack.assetLibrary();
        Assertions.assertNotNull(instance);
    }

    @Test
    void testGetApplicationKeyKey() {
        assertTrue(stack.getApplicationKey().startsWith("blt"));
    }

    @Test
    void testGetApiKey() {
        assertTrue(stack.getApplicationKey().startsWith("blt"));
    }

    @Test
    void testGetDeliveryToken() {
        assertTrue(stack.getDeliveryToken().startsWith("blt"));
    }

    @Test
    @Deprecated
    void testGetAccessToken() {
        assertTrue(stack.getAccessToken().startsWith("blt"));
    }

    @Test
    void testRemoveHeader() {
        stack.removeHeader("environment");
        Assertions.assertFalse(stack.headers.containsKey("environment"));
        stack.setHeader("environment", ENV);
    }

    @Test
    void testSetHeader() {
        stack.setHeader("environment", ENV);
        assertTrue(stack.headers.containsKey("environment"));
    }

    @Test
    void testImageTransform() {
        LinkedHashMap<String, Object> linkedMap = new LinkedHashMap<>();
        linkedMap.put("fakeKey", "fakeValue");
        String newUrl = stack.ImageTransform("www.fakeurl.com/fakePath/fakeImage.png", linkedMap);
        assertEquals("www.fakeurl.com/fakePath/fakeImage.png?fakeKey=fakeValue", newUrl);
    }

    @Test
    void testImageTransformWithQuestionMark() {
        LinkedHashMap<String, Object> linkedMap = new LinkedHashMap<>();
        linkedMap.put("fakeKey", "fakeValue");
        String newUrl = stack.ImageTransform("www.fakeurl.com/fakePath/fakeImage.png?name=ishaileshmishra", linkedMap);
        assertEquals("www.fakeurl.com/fakePath/fakeImage.png?name=ishaileshmishra&fakeKey=fakeValue", newUrl);
    }

    @Test
    void testGetContentTypes() {
        JSONObject params = new JSONObject();
        params.put("fakeKey", "fakeValue");
        params.put("fakeKey1", "fakeValue2");
        stack.getContentTypes(params, null);
        assertEquals(4, params.length());
    }

    @Test
    void testSyncWithoutCallback() {
        stack.sync(null);
        assertEquals(2, stack.syncParams.length());
        assertTrue(stack.syncParams.has("init"));
    }

    @Test
    void testSyncPaginationTokenWithoutCallback() {
        stack.syncPaginationToken("justFakeToken", null);
        assertEquals(3, stack.syncParams.length());
        assertEquals("justFakeToken", stack.syncParams.get("pagination_token"));
        assertTrue(stack.syncParams.has("init"));
        assertTrue(stack.syncParams.has("environment"));
    }

    @Test
    void testSyncTokenWithoutCallback() {
        stack.syncToken("justFakeToken", null);
        assertEquals(3, stack.syncParams.length());
        assertEquals("justFakeToken", stack.syncParams.get("sync_token"));
        assertTrue(stack.syncParams.has("init"));
        assertTrue(stack.syncParams.has("environment"));
    }

    @Test
    void testSyncFromDateWithoutCallback() {
        Date date = new Date();
        stack.syncFromDate(date, null);
        assertEquals(3, stack.syncParams.length());
        assertTrue(stack.syncParams.get("start_from").toString().endsWith("Z"));
        assertTrue(stack.syncParams.has("init"));
        assertTrue(stack.syncParams.has("environment"));
    }

    @Test
    void testPrivateDateConverter() {
        Date date = new Date();
        String newDate = stack.convertUTCToISO(date);
        assertTrue(newDate.endsWith("Z"));
    }

    @Test
    void testSyncContentTypeWithoutCallback() {
        stack.syncContentType("fakeContentType", null);
        assertEquals(3, stack.syncParams.length());
        assertEquals("fakeContentType", stack.syncParams.get("content_type_uid"));
        assertTrue(stack.syncParams.has("init"));
        assertTrue(stack.syncParams.has("environment"));
    }

    @Test
    void testSyncLocaleWithoutCallback() {
        stack.syncLocale("en-us", null);
        assertEquals(3, stack.syncParams.length());
        assertEquals("en-us", stack.syncParams.get("locale"));
        assertTrue(stack.syncParams.has("init"));
        assertTrue(stack.syncParams.has("environment"));
    }

    @Test
    void testSyncPublishTypeEntryPublished() {
        stack.syncPublishType(Stack.PublishType.entry_published, null);
        assertEquals(3, stack.syncParams.length());
        assertEquals("entry_published", stack.syncParams.get("type"));
        assertTrue(stack.syncParams.has("init"));
        assertTrue(stack.syncParams.has("environment"));
    }

    @Test
    void testSyncPublishTypeAssetDeleted() {
        stack.syncPublishType(Stack.PublishType.asset_deleted, null);
        assertEquals(3, stack.syncParams.length());
        assertEquals("asset_deleted", stack.syncParams.get("type"));
        assertTrue(stack.syncParams.has("init"));
        assertTrue(stack.syncParams.has("environment"));
    }

    @Test
    void testSyncPublishTypeAssetPublished() {
        stack.syncPublishType(Stack.PublishType.asset_published, null);
        assertEquals(3, stack.syncParams.length());
        assertEquals("asset_published", stack.syncParams.get("type"));
        assertTrue(stack.syncParams.has("init"));
        assertTrue(stack.syncParams.has("environment"));
    }

    @Test
    void testSyncPublishTypeAssetUnPublished() {
        stack.syncPublishType(Stack.PublishType.asset_unpublished, null);
        assertEquals(3, stack.syncParams.length());
        assertEquals("asset_unpublished", stack.syncParams.get("type"));
        assertTrue(stack.syncParams.has("init"));
        assertTrue(stack.syncParams.has("environment"));
    }

    @Test
    void testSyncPublishTypeContentTypeDeleted() {
        stack.syncPublishType(Stack.PublishType.content_type_deleted, null);
        assertEquals(3, stack.syncParams.length());
        assertEquals("content_type_deleted", stack.syncParams.get("type"));
        assertTrue(stack.syncParams.has("init"));
        assertTrue(stack.syncParams.has("environment"));
    }

    @Test
    void testSyncPublishTypeEntryDeleted() {
        stack.syncPublishType(Stack.PublishType.entry_deleted, null);
        assertEquals(3, stack.syncParams.length());
        assertEquals("entry_deleted", stack.syncParams.get("type"));
        assertTrue(stack.syncParams.has("init"));
        assertTrue(stack.syncParams.has("environment"));
    }

    @Test
    void testSyncPublishTypeEntryUnpublished() {
        stack.syncPublishType(Stack.PublishType.entry_unpublished, null);
        assertEquals(3, stack.syncParams.length());
        assertEquals("entry_unpublished", stack.syncParams.get("type"));
        assertTrue(stack.syncParams.has("init"));
        assertTrue(stack.syncParams.has("environment"));
    }

    @Test
    void testSyncIncludingMultipleParams() {
        Date newDate = new Date();
        String startFrom = stack.convertUTCToISO(newDate);
        stack.sync("product", newDate, "en-us", Stack.PublishType.entry_published, null);
        assertEquals(6, stack.syncParams.length());
        assertEquals("entry_published", stack.syncParams.get("type"));
        assertEquals("en-us", stack.syncParams.get("locale"));
        assertEquals("product", stack.syncParams.get("content_type_uid"));
        assertEquals(startFrom, stack.syncParams.get("start_from"));
        assertTrue(stack.syncParams.has("init"));
        assertTrue(stack.syncParams.has("environment"));
    }

    @Test
    void testGetAllContentTypes() {
        JSONObject param = new JSONObject();
        stack.getContentTypes(param, new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                assertTrue(contentTypesModel.getResponse() instanceof JSONArray);
                assertEquals(5, ((JSONArray) contentTypesModel.getResponse()).length());
            }
        });
    }

    @Test
    void testSynchronization() {
        stack.sync(new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {
                if (error == null) {
                    logger.info(syncStack.getPaginationToken());
                } else {
                    logger.info(error.errorMessage);
                    assertEquals(105, error.errorCode);
                }
            }
        });
    }

    @Test
    void testConfigSetRegion() {
        Config config = new Config();
        config.setRegion(Config.ContentstackRegion.US);
        assertEquals("US", config.getRegion().toString());
    }

    @Test
    void testConfigGetRegion() {
        Config config = new Config();
        assertEquals("US", config.getRegion().toString());
    }

    @Test
    void testConfigGetHost() {
        Config config = new Config();
        assertEquals(config.host, config.getHost());
    }

}
