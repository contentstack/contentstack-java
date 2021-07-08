package com.contentstack.sdk;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;


// Run testcase for the particular class
// run mvn -Dtest=TestAssetTestCase test
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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
        logger.info("Asset TestCase started...");
    }

    /**
     * Sets up the test fixture.
     * (Called before every test case method.)
     */
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

    @AfterClass
    public static void afterClass() {
        logger.info("Ran all asset testcases");
    }

    @Test()
    public void test_A_getAllAssetsToSetAssetUID() {
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
    public void test_B_VerifyAssetUID() {

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
    public void test_C_Asset_fetch() {
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
    public void test_D_AssetLibrary_fetch() {
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
    public void test_E_AssetLibrary_includeCount_fetch() {
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
    public void test_F_AssetLibrary_includeRelativeUrl_fetch() {
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
    public void test_G_StackGetParams() {
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
    public void test_H_Asset_Locale_Include_Fallback() {
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
    public void test_H_Asset_Include_Dimension() {
        final Asset asset = stack.asset(ASSET_UID);
        asset.includeDimension();
        asset.fetch(new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    logger.warning(asset.toJSON().optString("include_dimension"));
                } else {
                    logger.warning(error.getErrorDetail());
                    assertEquals(error.getErrorDetail(), "{\"uid\":[\"is not valid.\"]}");
                }
            }
        });
    }


    @Test
    public void test_I_Asset_Include_Dimension_using_addParams() {
        final Asset asset = stack.asset(ASSET_UID);
        asset.addParam("include_dimension", "true");
        asset.fetch(new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    logger.warning(asset.toJSON().optString("include_dimension"));
                    // assertEquals(assetUid, asset.getAssetUid());
                }
            }
        });
    }

    @Test()
    public void test_J_include_fallback() {
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
