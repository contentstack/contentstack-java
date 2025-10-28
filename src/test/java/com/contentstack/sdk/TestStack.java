package com.contentstack.sdk;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.*;


import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestStack {
    Stack stack = Credentials.getStack();
    protected String paginationToken;
    private final Logger logger = Logger.getLogger(TestStack.class.getName());
    private String entryUid = Credentials.ENTRY_UID;
    private String CONTENT_TYPE = Credentials.CONTENT_TYPE;


    @Test
    @Order(1)
    void stackExceptionTesting() {
        IllegalAccessException thrown = Assertions.assertThrows(IllegalAccessException.class, Stack::new,
                "Direct instantiation of Stack is not allowed. Use Contentstack.stack() to create an instance.");
        assertEquals("Direct instantiation of Stack is not allowed. Use Contentstack.stack() to create an instance.", thrown.getLocalizedMessage());
    }

    @Test
    @Order(2)
    void testStackInitThrowErr() {
        try {
            stack = new Stack();
        } catch (IllegalAccessException e) {
            assertEquals("Direct instantiation of Stack is not allowed. Use Contentstack.stack() to create an instance.", e.getLocalizedMessage());
        }
    }


    @Test
    @Order(4)
    void testStackAddHeader() {
        stack.setHeader("abcd", "justForTesting");
        assertTrue(stack.headers.containsKey("abcd"));
    }

    @Test
    @Order(5)
    void testStackRemoveHeader() {
        stack.removeHeader("abcd");
        Assertions.assertFalse(stack.headers.containsKey("abcd"));
    }

    @Test
    @Order(6)
    void testContentTypeInstance() {
        stack.contentType("product");
        assertEquals("product", stack.contentType);
    }

    @Test
    @Order(7)
    void testAssetWithUidInstance() {
        Asset instance = stack.asset("fakeUid");
        Assertions.assertNotNull(instance);
    }

    @Test
    @Order(8)
    void testAssetInstance() {
        Asset instance = stack.asset();
        Assertions.assertNotNull(instance);
    }

    @Test
    @Order(9)
    void testAssetLibraryInstance() {
        AssetLibrary instance = stack.assetLibrary();
        Assertions.assertNotNull(instance);
    }

    @Test
    @Order(11)
    void testGetApplicationKeyKey() {
        assertTrue(stack.getApplicationKey().startsWith("blt"));
    }

    @Test
    @Order(12)
    void testGetApiKey() {
        assertTrue(stack.getApplicationKey().startsWith("blt"));
    }

    @Test
    @Order(13)
    void testGetDeliveryToken() {
        assertNotNull(stack.getDeliveryToken());
    }

    @Test
    @Order(15)
    void testRemoveHeader() {
        stack.removeHeader("environment");
        Assertions.assertFalse(stack.headers.containsKey("environment"));
        stack.setHeader("environment", Credentials.ENVIRONMENT);
    }

    @Test
    @Order(16)
    void testSetHeader() {
        stack.setHeader("environment", Credentials.ENVIRONMENT);
        assertTrue(stack.headers.containsKey("environment"));
    }

    @Test
    @Order(17)
    void testImageTransform() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("fakeKey", "fakeValue");
        String newUrl = stack.imageTransform("www.fakeurl.com/fakePath/fakeImage.png", params);
        assertEquals("www.fakeurl.com/fakePath/fakeImage.png?fakeKey=fakeValue", newUrl);
    }

    @Test
    @Order(18)
    void testImageTransformWithQuestionMark() {
        LinkedHashMap<String, Object> linkedMap = new LinkedHashMap<>();
        linkedMap.put("fakeKey", "fakeValue");
        String newUrl = stack.imageTransform("www.fakeurl.com/fakePath/fakeImage.png?name=ishaileshmishra", linkedMap);
        assertEquals("www.fakeurl.com/fakePath/fakeImage.png?name=ishaileshmishra&fakeKey=fakeValue", newUrl);
    }

    @Test
    @Order(19)
    void testGetContentTypes() {
        JSONObject params = new JSONObject();
        params.put("fakeKey", "fakeValue");
        params.put("fakeKey1", "fakeValue2");
        stack.getContentTypes(params, null);
        assertEquals(4, params.length());
    }

    @Test
    @Order(20)
    void testSyncWithoutCallback() {
        stack.sync(null);
        assertEquals(2, stack.syncParams.length());
        assertTrue(stack.syncParams.has("init"));
    }

    @Test
    @Order(21)
    void testSyncPaginationTokenWithoutCallback() {
        stack.syncPaginationToken("justFakeToken", null);
        assertEquals(2, stack.syncParams.length());
        assertEquals("justFakeToken", stack.syncParams.get("pagination_token"));
        assertTrue(stack.syncParams.has("environment"));
    }

    @Test
    @Order(22)
    void testSyncTokenWithoutCallback() {
        stack.syncToken("justFakeToken", null);
        assertEquals(2, stack.syncParams.length());
        assertEquals("justFakeToken", stack.syncParams.get("sync_token"));
        assertTrue(stack.syncParams.has("environment"));
    }

    @Test
    @Order(23)
    void testSyncFromDateWithoutCallback() {
        Date date = new Date();
        stack.syncFromDate(date, null);
        assertEquals(3, stack.syncParams.length());
        assertTrue(stack.syncParams.get("start_from").toString().endsWith("Z"));
        assertTrue(stack.syncParams.has("init"));
        assertTrue(stack.syncParams.has("environment"));
    }

    @Test
    @Order(24)
    void testPrivateDateConverter() {
        Date date = new Date();
        String newDate = stack.convertUTCToISO(date);
        assertTrue(newDate.endsWith("Z"));
    }

    @Test
    @Order(25)
    void testSyncContentTypeWithoutCallback() {
        stack.syncContentType("fakeContentType", null);
        assertEquals(3, stack.syncParams.length());
        assertEquals("fakeContentType", stack.syncParams.get("content_type_uid"));
        assertTrue(stack.syncParams.has("init"));
        assertTrue(stack.syncParams.has("environment"));
    }

    @Test
    @Order(27)
    void testSyncLocaleWithoutCallback() {
        stack.syncLocale("en-us", null);
        assertEquals(3, stack.syncParams.length());
        assertEquals("en-us", stack.syncParams.get("locale"));
        assertTrue(stack.syncParams.has("init"));
        assertTrue(stack.syncParams.has("environment"));
    }

    @Test
    @Order(28)
    void testSyncPublishTypeEntryPublished() {
        // decode ignore NullPassTo/test: <please specify a reason of ignoring this>
        stack.syncPublishType(Stack.PublishType.ENTRY_PUBLISHED, null);
        assertEquals(3, stack.syncParams.length());
        assertEquals("entry_published", stack.syncParams.get("type"));
        assertTrue(stack.syncParams.has("init"));
        assertTrue(stack.syncParams.has("environment"));
    }

    @Test
    @Order(29)
    void testSyncPublishTypeAssetDeleted() {
        stack.syncPublishType(Stack.PublishType.ASSET_DELETED, null);
        assertEquals(3, stack.syncParams.length());
        assertEquals("asset_deleted", stack.syncParams.get("type"));
        assertTrue(stack.syncParams.has("init"));
        assertTrue(stack.syncParams.has("environment"));
    }

    @Test
    @Order(30)
    void testSyncPublishTypeAssetPublished() {
        stack.syncPublishType(Stack.PublishType.ASSET_PUBLISHED, null);
        assertEquals(3, stack.syncParams.length());
        assertEquals("asset_published", stack.syncParams.get("type"));
        assertTrue(stack.syncParams.has("init"));
        assertTrue(stack.syncParams.has("environment"));
    }

    @Test
    @Order(31)
    void testSyncPublishTypeAssetUnPublished() {
        stack.syncPublishType(Stack.PublishType.ASSET_UNPUBLISHED, null);
        assertEquals(3, stack.syncParams.length());
        assertEquals("asset_unpublished", stack.syncParams.get("type"));
        assertTrue(stack.syncParams.has("init"));
        assertTrue(stack.syncParams.has("environment"));
    }

    @Test
    @Order(32)
    void testSyncPublishTypeContentTypeDeleted() {
        stack.syncPublishType(Stack.PublishType.CONTENT_TYPE_DELETED, null);
        assertEquals(3, stack.syncParams.length());
        assertEquals("content_type_deleted", stack.syncParams.get("type"));
        assertTrue(stack.syncParams.has("init"));
        assertTrue(stack.syncParams.has("environment"));
    }

    @Test
    @Order(33)
    void testSyncPublishTypeEntryDeleted() {
        stack.syncPublishType(Stack.PublishType.ENTRY_DELETED, null);
        assertEquals(3, stack.syncParams.length());
        assertEquals("entry_deleted", stack.syncParams.get("type"));
        assertTrue(stack.syncParams.has("init"));
        assertTrue(stack.syncParams.has("environment"));
    }

    @Test
    @Order(34)
    void testSyncPublishTypeEntryUnpublished() {
        // decode ignore NullPassTo/test: <please specify a reason of ignoring this>
        stack.syncPublishType(Stack.PublishType.ENTRY_UNPUBLISHED, null);
        assertEquals(3, stack.syncParams.length());
        assertEquals("entry_unpublished", stack.syncParams.get("type"));
        assertTrue(stack.syncParams.has("init"));
        assertTrue(stack.syncParams.has("environment"));
    }

    @Test
    @Order(35)
    void testSyncIncludingMultipleParams() {
        Date newDate = new Date();
        String startFrom = stack.convertUTCToISO(newDate);
        stack.sync("product", newDate, "en-us", Stack.PublishType.ENTRY_PUBLISHED, null);
        assertEquals(6, stack.syncParams.length());
        assertEquals("entry_published", stack.syncParams.get("type").toString().toLowerCase());
        assertEquals("en-us", stack.syncParams.get("locale"));
        assertEquals("product", stack.syncParams.get("content_type_uid").toString().toLowerCase());
        assertEquals(startFrom, stack.syncParams.get("start_from"));
        assertTrue(stack.syncParams.has("init"));
        assertTrue(stack.syncParams.has("environment"));
    }

    @Test
    @Order(36)
    void testGetAllContentTypes() {
        JSONObject param = new JSONObject();
        stack.getContentTypes(param, new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                assertTrue(contentTypesModel.getResultArray() instanceof JSONArray);
                assertNotNull(((JSONArray) contentTypesModel.getResponse()).length());

            }
        });
    }

    @Test
    @Order(37)
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
    @Order(38)
    void testConfigSetRegion() {
        Config config = new Config();
        config.setRegion(Config.ContentstackRegion.US);
        assertEquals("US", config.getRegion().toString());
    }

    @Test
    @Order(39)
    void testConfigGetRegion() {
        Config config = new Config();
        assertEquals("US", config.getRegion().toString());
    }

    @Test
    @Order(40)
    void testConfigGetHost() {
        Config config = new Config();
        assertEquals(config.host, config.getHost());
    }

    // @Test
    // @Disabled("No relevant code")
    // @Order(41)
    // void testSynchronizationAPIRequest() throws IllegalAccessException {

    //     stack.sync(new SyncResultCallBack() {
    //         @Override
    //         public void onCompletion(SyncStack response, Error error) {
    //             paginationToken = response.getPaginationToken();
    //             Assertions.assertNull(response.getUrl());
    //             Assertions.assertNotNull(response.getJSONResponse());
    //             Assertions.assertEquals(129, response.getCount());
    //             Assertions.assertEquals(100, response.getLimit());
    //             Assertions.assertEquals(0, response.getSkip());
    //             Assertions.assertNotNull(response.getPaginationToken());
    //             Assertions.assertNull(response.getSyncToken());
    //             Assertions.assertEquals(100, response.getItems().size());
    //         }
    //     });
    // }

    // @Test
    // @Disabled("No relevant code")
    // @Order(42)
    // void testSyncPaginationToken() throws IllegalAccessException {
    //     stack.syncPaginationToken(paginationToken, new SyncResultCallBack() {
    //         @Override
    //         public void onCompletion(SyncStack response, Error error) {
    //             Assertions.assertNull(response.getUrl());
    //             Assertions.assertNotNull(response.getJSONResponse());
    //             Assertions.assertEquals(29, response.getCount());
    //             Assertions.assertEquals(100, response.getLimit());
    //             Assertions.assertEquals(100, response.getSkip());
    //             Assertions.assertNull(response.getPaginationToken());
    //             Assertions.assertNotNull(response.getSyncToken());
    //             Assertions.assertEquals(29, response.getItems().size());
    //         }
    //     });
    // }
    @Test
    @Order(43)
    void testAsseturlupdate() throws IllegalAccessException {
        Entry entry = stack.contentType(CONTENT_TYPE).entry(entryUid).includeEmbeddedItems();
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                stack.updateAssetUrl(entry);
                Assertions.assertEquals(entryUid, entry.getUid());
                Assertions.assertTrue(entry.params.has("include_embedded_items[]"));
            }
        });
    }

    @Test
    @Order(44)
    void testAURegionSupport() throws IllegalAccessException {
        Config config = new Config();
        Config.ContentstackRegion region = Config.ContentstackRegion.AU;
        config.setRegion(region);
        Assertions.assertFalse(config.region.name().isEmpty());
        Assertions.assertEquals("AU", config.region.name());
    }

    @Test
    @Order(45)
    void testAURegionBehaviourStackHost() throws IllegalAccessException {
        Config config = new Config();
        Config.ContentstackRegion region = Config.ContentstackRegion.AU;
        config.setRegion(region);
        Stack stack = Contentstack.stack("fakeApiKey", "fakeDeliveryToken", "fakeEnvironment", config);
        Assertions.assertFalse(config.region.name().isEmpty());
        Assertions.assertEquals("au-cdn.contentstack.com", stack.config.host);

    }

}
