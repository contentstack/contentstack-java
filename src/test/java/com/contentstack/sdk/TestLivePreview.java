package com.contentstack.sdk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
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
        stack.livePreviewQuery(hashMap);
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
        stack.livePreviewQuery(hashMap);
        ContentType contentType = stack.contentType("contentType");
        Entry entryInstance = contentType.entry("entryUid478748374");
        entryInstance.fetch(null);
        Assertions.assertNotNull(entryInstance);
    }

    @Test()
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

    @Test()
    void testMissingHostToEnableLivePreview() {
        Config livePreviewEnablerConfig = new Config().enableLivePreview(true)
                .setManagementToken("management_token_123456");
        try {
            Contentstack.stack("liveAPIKey", "liveAccessToken", "liveEnv", livePreviewEnablerConfig);
        } catch (Exception e) {
            Assertions.assertEquals("host is required", e.getLocalizedMessage());
            logger.severe(e.getLocalizedMessage());
        }
    }

    @Test()
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


}
