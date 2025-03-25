package com.contentstack.sdk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The type Config testcase.
 */
public class TestLivePreview {

    private static final Logger logger = Logger.getLogger(TestLivePreview.class.getName());
    private static Config config;

    /**
     * One time set up.
     */
    @BeforeAll
    public static void setUp() {
        logger.setLevel(Level.FINE);
        config = new Config();
    }


    /**
     * Test config test.
     */
    @Test
    void testConfigTest() {
        Config livePreview = config.enableLivePreview(true).setLivePreviewHost("api.contentstack.com")
                .setManagementToken("managementToken");
        Assertions.assertEquals("api.contentstack.com", livePreview.livePreviewHost);
        Assertions.assertEquals("managementToken", livePreview.managementToken);
    }

    /**
     * Test config test.
     */
    @Test()
    void testEnableLivePreviewTrue() {
        Config livePreview = config.enableLivePreview(true);
        Assertions.assertTrue(livePreview.enableLivePreview);
    }

    /**
     * Test config test.
     */
    @Test()
    void testEnableLivePreviewFalse() {
        Config livePreview = config.enableLivePreview(false);
        Assertions.assertFalse(livePreview.enableLivePreview);
    }

    @Test()
    void testSetLivePreview() {
        Config livePreview = config.setLivePreviewHost("api.contentstack.com");
        Assertions.assertEquals("api.contentstack.com", livePreview.livePreviewHost);
    }

    @Test()
    void testSetAuthorization() {
        Config livePreview = config.setManagementToken("management_token");
        Assertions.assertEquals("management_token", livePreview.managementToken);
    }

    @Test()
    void testStackEnableLivePreviewQuery() throws Exception {
        config.enableLivePreview(true).setLivePreviewHost("api.contentstack.com").setManagementToken("managementToken");
        Stack stack = Contentstack.stack("liveAPIKey", "liveAccessToken", "liveEnv", config);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("live_preview", "hash167673");
        hashMap.put("content_type_uid", "contentType");
        ContentType contentType = stack.contentType("contentType");
        Query queryInstance = contentType.query();
        Assertions.assertNotNull(queryInstance);
    }

    @Test()
    void testStackEnableLivePreviewEntry() throws Exception {
        config.enableLivePreview(true).setLivePreviewHost("live-preview.contentstack.com")
                .setManagementToken("management_token_123456");
        Stack stack = Contentstack.stack("liveAPIKey", "liveAccessToken", "liveEnv", config);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("live_preview", "hash167673");
        hashMap.put("content_type_uid", "contentType");
        ContentType contentType = stack.contentType("contentType");
        Entry entryInstance = contentType.entry("entryUid478748374");
        Assertions.assertNotNull(entryInstance);
    }

    @Test()
    @Disabled("No validation required: improved test")
    void testEnableLivePreviewWithoutRequiredParameters() {
        Config livePreviewEnablerConfig = new Config().enableLivePreview(true);
        try {
            Contentstack.stack("liveAPIKey", "liveAccessToken", "liveEnv", livePreviewEnablerConfig);
        } catch (Exception e) {
            Assertions.assertEquals("managementToken is required", e.getLocalizedMessage());
            logger.severe(e.getLocalizedMessage());
        }
    }

    @Test()
    void testExceptionWhenAllRequiredParamsNotProvided() {
        Config livePreviewEnablerConfig = new Config().enableLivePreview(true)
                .setLivePreviewHost("live-preview.contentstack.io");
        try {
            Contentstack.stack("liveAPIKey", "liveAccessToken", "liveEnv", livePreviewEnablerConfig);
        } catch (Exception e) {
            Assertions.assertEquals("managementToken is required", e.getLocalizedMessage());
            logger.severe(e.getLocalizedMessage());
        }
    }

    /**
     *
     */
    @Test()
    void testMissingHostToEnableLivePreview() {
        Config livePreviewEnablerConfig = new Config().enableLivePreview(true)
                .setManagementToken("management_token_123456");
        try {
            Stack cs = Contentstack.stack("liveAPIKey", "liveAccessToken", "liveEnv", livePreviewEnablerConfig);
            Assertions.assertNotNull(cs);
        } catch (Exception e) {
            logger.severe(e.getLocalizedMessage());
        }
    }

    @Test()
    @Disabled("No validation required")
    void testCompleteLivePreview() throws Exception {
        Config livePreviewEnablerConfig = new Config().enableLivePreview(true)
                .setLivePreviewHost("live-preview.contentstack.io").setManagementToken("management_token_123456");
        Stack stack = Contentstack.stack("liveAPIKey", "liveAccessToken", "liveEnv", livePreviewEnablerConfig);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("content_type_uid", "content_type_uid");
        stack.livePreviewQuery(hashMap);
        Entry entry = stack.contentType("content_type_uid").entry("entry_uid");
        entry.fetch(null);
        Assertions.assertNotNull(entry);
    }

    @Test()
    void testCompleteLivePreviewInQuery() throws Exception {
        Config livePreviewEnablerConfig = new Config().enableLivePreview(true).setLivePreviewHost("cdn.contentstack.io")
                .setManagementToken("fake@token");
        Stack stack = Contentstack.stack("liveAPIKey", "liveAccessToken", "liveEnv", livePreviewEnablerConfig);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("content_type_uid", "content_type_uid");
        stack.livePreviewQuery(hashMap);
        Query entry = stack.contentType("content_type_uid").query();
        entry.find(null);
        Assertions.assertNotNull(entry);
    }

    @Test
    void testCompleteLivePreviewWithPreviewToken() throws IOException, IllegalAccessException {
        Config livePreviewConfig = new Config()
                .enableLivePreview(true)
                .setLivePreviewHost("rest-preview.contentstack.com")
                .setPreviewToken("preview_token");

        Stack stack = Contentstack.stack("stackApiKey", "deliveryToken", "env1", livePreviewConfig);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("live_preview", "hash167673");
        hashMap.put("content_type_uid", "page");

        stack.livePreviewQuery(hashMap);
        Entry entry = stack.contentType("page").entry("entry_uid");
        entry.fetch(null);
        Assertions.assertNotNull(entry);

    }

    @Test()
    void testLivePreviewWithoutPreviewToken() throws Exception {
        Config livePreviewEnablerConfig = new Config().enableLivePreview(true).setLivePreviewHost("rest-preview.contentstack.com")
                .setManagementToken("fake@token");
        Stack stack = Contentstack.stack("stackApiKey", "deliveryToken", "env1", livePreviewEnablerConfig);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("live_preview", "hash167673");
        hashMap.put("content_type_uid", "page");
        
        IllegalAccessError thrown = Assertions.assertThrows(IllegalAccessError.class, () -> {
            stack.livePreviewQuery(hashMap);
        }, "Expected livePreviewQuery to throw IllegalAccessError");

        Assertions.assertTrue(thrown.getMessage().contains("Provide the Preview Token for the host rest-preview.contentstack.com"), 
            "Exception message should mention that Preview Token is required");

        logger.severe(thrown.getMessage());  
    }

    @Test
    void testLivePreviewDisabled() throws IllegalAccessException, IOException {
    Config config = new Config()
            .enableLivePreview(false)
            .setPreviewToken("preview_token");

    Stack stack = Contentstack.stack("stackApiKey", "deliveryToken", "env1", config);

    HashMap<String, String> hashMap = new HashMap<>();
    hashMap.put("live_preview", "hash167673");
    hashMap.put("content_type_uid", "page");

    Exception exception = assertThrows(IllegalStateException.class, () -> {
        stack.livePreviewQuery(hashMap);
    });

    // Optionally, you can check the message of the exception
    assertEquals("Live Preview is not enabled in Config", exception.getMessage(), 
                 "Expected exception message does not match");
    }

    @Test
    void testTimelinePreview() throws IllegalAccessException, IOException {
        Config config = new Config()
                .enableLivePreview(true)
                .setLivePreviewHost("rest-preview.contentstack.com")
                .setPreviewToken("preview_token");

        Stack stack = Contentstack.stack("stackApiKey", "deliveryToken", "env1", config);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("live_preview", "hash167673");
        hashMap.put("content_type_uid", "page");
        hashMap.put("entry_uid", "entryUid");
        hashMap.put("release_id", "12345");
        hashMap.put("preview_timestamp", "2025-09-25 17:45:30.005");


        stack.livePreviewQuery(hashMap);
        Entry entry = stack.contentType("page").entry("entry_uid");
        entry.fetch(null);
        Assertions.assertNotNull(entry);
    }

    @Test
    void testLivePreviewQueryWithoutReleaseId() throws Exception {
        Config config = new Config().enableLivePreview(true)
                .setLivePreviewHost("rest-preview.contentstack.com")
                .setPreviewToken("previewToken");
        Stack stack = Contentstack.stack("api_key", "access_token", "env", config);

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("content_type_uid", "blog");
        queryParams.put("entry_uid", "entry_123");
        queryParams.put("preview_timestamp", "1710800000");

        stack.livePreviewQuery(queryParams);

        Assertions.assertNull(config.releaseId);
        Assertions.assertEquals("1710800000", config.previewTimestamp);
    }

}
