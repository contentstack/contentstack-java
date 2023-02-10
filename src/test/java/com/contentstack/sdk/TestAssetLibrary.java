package com.contentstack.sdk;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestAssetLibrary {

    protected String API_KEY, DELIVERY_TOKEN, ENV;
    private final Logger logger = Logger.getLogger(TestAssetLibrary.class.getName());
    private Stack stack;

    @BeforeAll
    public void initBeforeTests() throws IllegalAccessException {
        Dotenv dotenv = Dotenv.load();
        API_KEY = dotenv.get("API_KEY");
        DELIVERY_TOKEN = dotenv.get("DELIVERY_TOKEN");
        ENV = dotenv.get("ENVIRONMENT");
        Config config = new Config();
        config.setHost(dotenv.get("HOST"));
        stack = Contentstack.stack(API_KEY, DELIVERY_TOKEN, ENV, config);
    }

    @Test
    @Order(1)
    void testNewAssetLibrary() {
        AssetLibrary assets = stack.assetLibrary();
        assets.fetchAll(new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, List<Asset> assets, Error error) {
                Asset model = assets.get(0);
                Assertions.assertTrue(model.getAssetUid().startsWith("blt"));
                assertEquals("image/jpeg", model.getFileType());
                assertEquals("482141", model.getFileSize());
                assertEquals("phoenix2.jpg", model.getFileName());
                Assertions.assertTrue(model.getUrl().endsWith("phoenix2.jpg"));
                Assertions.assertTrue(model.toJSON().has("created_at"));
                Assertions.assertTrue(model.getCreatedBy().startsWith("blt"));
                assertEquals("gregory", model.getUpdateAt().getCalendarType());
                Assertions.assertTrue(model.getUpdatedBy().startsWith("sys"));
                assertEquals("", model.getDeletedBy());
                logger.info("passed...");
            }
        });
    }

    @Test
    void testAssetSetHeader() {
        AssetLibrary assetLibrary = stack.assetLibrary();
        assetLibrary.setHeader("headerKey", "headerValue");
        Assertions.assertTrue(assetLibrary.headers.containsKey("headerKey"));
        logger.info("passed...");
    }

    @Test
    void testAssetRemoveHeader() {
        AssetLibrary assetLibrary = stack.assetLibrary();
        assetLibrary.setHeader("headerKey", "headerValue");
        assetLibrary.removeHeader("headerKey");
        Assertions.assertFalse(assetLibrary.headers.containsKey("headerKey"));
        logger.info("passed...");
    }

    @Test
    void testAssetSortAscending() {
        AssetLibrary assetLibrary = stack.assetLibrary().sort("ascending", AssetLibrary.ORDERBY.ASCENDING);
        Assertions.assertFalse(assetLibrary.headers.containsKey("asc"));
        logger.info("passed...");
    }

    @Test
    void testAssetSortDescending() {
        AssetLibrary assetLibrary = stack.assetLibrary();
        assetLibrary.sort("descending", AssetLibrary.ORDERBY.DESCENDING);
        Assertions.assertFalse(assetLibrary.headers.containsKey("desc"));
        logger.info("passed...");
    }

    @Test
    void testAssetIncludeCount() {
        AssetLibrary assetLibrary = stack.assetLibrary().includeCount();
        Assertions.assertFalse(assetLibrary.headers.containsKey("include_count"));
        logger.info("passed...");
    }

    @Test
    void testAssetIncludeRelativeUrl() {
        AssetLibrary assetLibrary = stack.assetLibrary();
        assetLibrary.includeRelativeUrl();
        Assertions.assertFalse(assetLibrary.headers.containsKey("relative_urls"));
        logger.info("passed...");
    }

    @Test
    void testAssetGetCount() {
        AssetLibrary assetLibrary = stack.assetLibrary().includeRelativeUrl();
        Assertions.assertEquals(0, assetLibrary.getCount());
        logger.info("passed...");
    }

    @Test
    void testIncludeFallback() {
        AssetLibrary assetLibrary = stack.assetLibrary().includeFallback();
        Assertions.assertFalse(assetLibrary.headers.containsKey("include_fallback"));
        logger.info("passed...");
    }
}
