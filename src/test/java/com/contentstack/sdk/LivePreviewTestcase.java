package com.contentstack.sdk;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The type Config testcase.
 */
public class LivePreviewTestcase {

    private static final Logger logger = Logger.getLogger(AssetTestCase.class.getName());
    private static Config config;

    /**
     * One time set up.
     */
    @BeforeClass
    public static void setUp() {
        logger.setLevel(Level.FINE);
        config = new Config();
    }


    /**
     * Test config test.
     */
    @Test()
    public void testConfigTest() {
        Config livePreview = config.enableLivePreview(true)
                .setLivePreviewHost("api.contentstack.com")
                .setAuthorization("managementToken");

        Assert.assertEquals("api.contentstack.com", livePreview.livePreviewHost);
        Assert.assertEquals("managementToken", livePreview.managementToken);
    }

    /**
     * Test config test.
     */
    @Test()
    public void testEnableLivePreviewTrue() {
        Config livePreview = config.enableLivePreview(true);
        Assert.assertTrue(livePreview.enableLivePreview);
    }

    /**
     * Test config test.
     */
    @Test()
    public void testEnableLivePreviewFalse() {
        Config livePreview = config.enableLivePreview(false);
        Assert.assertFalse(livePreview.enableLivePreview);
    }

    @Test()
    public void testSetLivePreview() {
        Config livePreview = config
                .setLivePreviewHost("api.contentstack.com");
        Assert.assertEquals("api.contentstack.com", livePreview.livePreviewHost);
    }


    @Test()
    public void testSetAuthorization() {
        Config livePreview = config
                .setAuthorization("management_token");
        Assert.assertEquals("management_token", livePreview.managementToken);
    }


    @Test()
    public void testStackEnableLivePreviewQuery() throws Exception {
        Config livePreview = config.enableLivePreview(true)
                .setLivePreviewHost("api.contentstack.com")
                .setAuthorization("managementToken");
        Stack stack = Contentstack.stack("liveAPIKey", "liveAccessToken", "liveEnv", config);
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("hash", "hash167673");
        hashMap.put("content_type_uid", "contentType");
        stack.livePreviewQuery(hashMap);
        ContentType contentType = stack.contentType("contentType");
        Query queryInstance = contentType.query();


    }


    @Test()
    public void testStackEnableLivePreviewEntry() throws Exception {
        config.enableLivePreview(true)
                .setLivePreviewHost("live-preview.contentstack.com")
                .setAuthorization("management_token_123456");
        Stack stack = Contentstack.stack("liveAPIKey", "liveAccessToken", "liveEnv", config);
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("hash", "hash167673");
        hashMap.put("content_type_uid", "contentType");
        stack.livePreviewQuery(hashMap);
        ContentType contentType = stack.contentType("contentType");
        Entry entryInstance = contentType.entry("entryUid478748374");

        entryInstance.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                System.out.println(error);
            }
        });
    }
}
