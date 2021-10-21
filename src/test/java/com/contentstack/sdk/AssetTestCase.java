package com.contentstack.sdk;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;


public class AssetTestCase {

    private static final Logger logger = Logger.getLogger(AssetTestCase.class.getName());
    private static String ASSET_UID = null;
    private static Stack stack;
    private static String DEFAULT_HOST;

    @BeforeClass
    public static void oneTimeSetUp() throws Exception {
        logger.setLevel(Level.FINE);
        Dotenv dotenv = Dotenv.load();
        String DEFAULT_API_KEY = dotenv.get("API_KEY");
        String DEFAULT_DELIVERY_TOKEN = dotenv.get("DELIVERY_TOKEN");
        String DEFAULT_ENV = dotenv.get("ENVIRONMENT");
        DEFAULT_HOST = dotenv.get("HOST");
        Config config = new Config();
        config.setHost(DEFAULT_HOST);
        assert DEFAULT_API_KEY != null;
        stack = Contentstack.stack(DEFAULT_API_KEY, DEFAULT_DELIVERY_TOKEN, DEFAULT_ENV, config);
    }

    @BeforeClass
    public static void beforeClass() {
        // Get all the assets available
        logger.info("Started Running asset testcases");
        final AssetLibrary assetLibrary = stack.assetLibrary();
        assetLibrary.fetchAll(new FetchAssetsCallback() {
            public void onCompletion(ResponseType responseType, List<Asset> assets, Error error) {
                if (error == null) {
                    assets.stream().iterator().forEachRemaining(asset -> {
                        if (asset.getFileName().equalsIgnoreCase("phoenix2.jpg")) {
                            ASSET_UID = asset.getAssetUid();
                        }
                    });
                }
            }
        });
    }


    @Test()
    public void testGetAllAssetsToSetAssetUID() {
        final AssetLibrary assetLibrary = stack.assetLibrary();
        assetLibrary.fetchAll(new FetchAssetsCallback() {
            public void onCompletion(ResponseType responseType, List<Asset> assets, Error error) {
                if (error == null) {
                    logger.info("response: " + assets.get(0).getAssetUid());
                    ASSET_UID = assets.get(0).getAssetUid();
                }
            }
        });
    }


    @Test
    public void testVerifyAssetUID() {

        final Asset asset = stack.asset(ASSET_UID);
        asset.fetch(new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    // Success Block.
                    logger.info("response: " + asset.getAssetUid());
                    assertEquals(ASSET_UID, asset.getAssetUid());
                }
            }
        });
    }

    @Test
    public void testAssetFetch() {
        final Asset asset = stack.asset(ASSET_UID);
        asset.fetch(new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    assertEquals(ASSET_UID, asset.getAssetUid());
                    assertEquals("image/jpeg", asset.getFileType());
                    assertEquals("blt69a06b75160147adc2c8b3a9", asset.getCreatedBy());
                    assertEquals("sys_blt309cacf8ab432f62", asset.getUpdatedBy());
                    assertEquals("phoenix2.jpg", asset.getFileName());
                    assertEquals("482141", asset.getFileSize());
                    if (DEFAULT_HOST.equalsIgnoreCase("cdn.contentstack.io")) {
                        assertEquals("https://images.contentstack.io/v3/assets/blt12c8ad610ff4ddc2/blt5312f71416d6e2c8/5704eda29ebb5cce3597b877/phoenix2.jpg", asset.getUrl());
                    } else {
                        assertEquals("https://stag-images.contentstack.io/v3/assets/blt12c8ad610ff4ddc2/blt5312f71416d6e2c8/5704eda29ebb5cce3597b877/phoenix2.jpg", asset.getUrl());
                    }
                }
            }
        });
    }

    @Test
    public void testAssetLibrary_fetch() {
        final AssetLibrary assetLibrary = stack.assetLibrary();
        assetLibrary.fetchAll(new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, List<Asset> assets, Error error) {
                if (error == null) {
                    assets.forEach(asset -> {
                        logger.info("----Test--Asset-D--Success----" + asset.toJSON());
                        logger.info("----Test--Asset-D--Success----" + asset.getFileType());
                        logger.info("----Test--Asset-D--Success----" + asset.getCreatedBy());
                        logger.info("----Test--Asset-D--Success----" + asset.getUpdatedBy());
                        logger.info("----Test--Asset-D--Success----" + asset.getFileName());
                        logger.info("----Test--Asset-D--Success----" + asset.getFileSize());
                        logger.info("----Test--Asset-D--Success----" + asset.getAssetUid());
                        logger.info("----Test--Asset-D--Success----" + asset.getUrl());
                    });
                }
            }
        });
    }

    @Test
    public void testAssetLibraryIncludeCount() {
        final AssetLibrary assetLibrary = stack.assetLibrary();
        assetLibrary.includeCount();

        assetLibrary.fetchAll(new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, List<Asset> assets, Error error) {
                if (error == null) {
                    if (DEFAULT_HOST.equalsIgnoreCase("cdn.contentstack.io")) {
                        assertEquals(16, assetLibrary.getCount());
                    } else {
                        assertEquals(20, assetLibrary.getCount());
                    }
                }
            }
        });
    }

    @Test
    public void testAssetLibraryIncludeRelativeUrl() {
        final AssetLibrary assetLibrary = stack.assetLibrary();
        assetLibrary.includeRelativeUrl();
        assetLibrary.fetchAll(new FetchAssetsCallback() {
            public void onCompletion(ResponseType responseType, List<Asset> assets, Error error) {
                if (error == null) {
                    assertEquals("/v3/assets/blt12c8ad610ff4ddc2/blt5312f71416d6e2c8/5704eda29ebb5cce3597b877/phoenix2.jpg", assets.get(0).getUrl());
                }
            }
        });
    }

    @Test
    public void testStackGetParams() {
        final Asset asset = stack.asset(ASSET_UID);
        asset.addParam("key", "some_value");
        asset.fetch(new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    logger.warning(asset.getAssetUid());
                    assertEquals(ASSET_UID, asset.getAssetUid());
                } else {
                    assertEquals(404, error.getErrorCode());
                }
            }
        });
    }

    @Test
    public void testAssetLocaleIncludeFallback() {
        final Asset asset = stack.asset(ASSET_UID);
        asset.includeFallback();
        asset.fetch(new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    logger.warning(asset.getAssetUid());
                } else {
                    assertEquals("Not Found", error.getErrorMessage());
                }
                assertTrue(asset.urlQueries.has("include_fallback"));
            }
        });
    }

    @Test
    public void testAssetIncludeDimension() {
        final Asset asset = stack.asset(ASSET_UID);
        asset.includeDimension();
        asset.fetch(new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    logger.warning(asset.toJSON().get("include_dimension").toString());
                } else {
                    logger.warning(error.getErrorDetail());
                    assertEquals(error.getErrorDetail(), "{\"uid\":[\"is not valid.\"]}");
                }
            }
        });
    }


    @Test
    public void testAssetIncludeDimensionUsingAddParams() {
        final Asset asset = stack.asset(ASSET_UID);
        asset.addParam("include_dimension", "true");
        asset.fetch(new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    logger.warning(asset.toJSON().get("include_dimension").toString());
                    // assertEquals(assetUid, asset.getAssetUid());
                }
            }
        });
    }

    @Test()
    public void testIncludeFallback() {
        final AssetLibrary assetLibrary = stack.assetLibrary();
        assetLibrary.includeFallback().fetchAll(new FetchAssetsCallback() {
            public void onCompletion(ResponseType responseType, List<Asset> assets, Error error) {
                if (error == null) {
                    logger.info("response: " + assets.get(0).getAssetUid());
                    assertTrue(assetLibrary.urlQueries.has("include_fallback"));
                }
            }
        });
    }

}
