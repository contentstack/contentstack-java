package com.contentstack.sdk;

import org.junit.jupiter.api.*;


import java.util.List;
import java.util.logging.Logger;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestAssetLibrary {
    private final Logger logger = Logger.getLogger(TestAssetLibrary.class.getName());
    private final Stack stack = Credentials.getStack();


    @Test
    @Order(1)
    void testNewAssetLibrary() {
        AssetLibrary assets = stack.assetLibrary();
        assets.fetchAll(new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, List<Asset> assets, Error error) {
                Asset model = assets.get(0);
                Assertions.assertTrue(model.getAssetUid().startsWith("blt"));
                Assertions.assertNotNull( model.getFileType());
                Assertions.assertNotNull(model.getFileSize());
                Assertions.assertNotNull( model.getFileName());
                Assertions.assertTrue(model.toJSON().has("created_at"));
                Assertions.assertTrue(model.getCreatedBy().startsWith("blt"));
                Assertions.assertEquals("gregory", model.getUpdateAt().getCalendarType());
                Assertions.assertTrue(model.getUpdatedBy().startsWith("blt"));
                Assertions.assertEquals("", model.getDeletedBy());
                logger.info("passed...");
            }
        });
    }

    @Test
    void testAssetSetHeader() {
        AssetLibrary assetLibrary = stack.assetLibrary();
        assetLibrary.setHeader("headerKey", "headerValue");
        Assertions.assertTrue(assetLibrary.headers.containsKey("headerKey"));
    }

    @Test
    void testAssetRemoveHeader() {
        AssetLibrary assetLibrary = stack.assetLibrary();
        assetLibrary.setHeader("headerKey", "headerValue");
        assetLibrary.removeHeader("headerKey");
        Assertions.assertFalse(assetLibrary.headers.containsKey("headerKey"));
    }

    @Test
    void testAssetSortAscending() {
        AssetLibrary assetLibrary = stack.assetLibrary().sort("ascending", AssetLibrary.ORDERBY.ASCENDING);
        Assertions.assertFalse(assetLibrary.headers.containsKey("asc"));
    }

    @Test
    void testAssetSortDescending() {
        AssetLibrary assetLibrary = stack.assetLibrary();
        assetLibrary.sort("descending", AssetLibrary.ORDERBY.DESCENDING);
        Assertions.assertFalse(assetLibrary.headers.containsKey("desc"));
    }

    @Test
    void testAssetIncludeCount() {
        AssetLibrary assetLibrary = stack.assetLibrary().includeCount();
        Assertions.assertFalse(assetLibrary.headers.containsKey("include_count"));
    }

    @Test
    void testAssetIncludeRelativeUrl() {
        AssetLibrary assetLibrary = stack.assetLibrary();
        assetLibrary.includeRelativeUrl();
        Assertions.assertFalse(assetLibrary.headers.containsKey("relative_urls"));
    }

    @Test
    void testAssetGetCount() {
        AssetLibrary assetLibrary = stack.assetLibrary().includeRelativeUrl();
        Assertions.assertEquals(0, assetLibrary.getCount());
    }

    @Test
    void testIncludeFallback() {
        AssetLibrary assetLibrary = stack.assetLibrary().includeFallback();
        Assertions.assertFalse(assetLibrary.headers.containsKey("include_fallback"));
    }

    @Test
    void testIncludeOwner() {
        AssetLibrary assetLibrary = stack.assetLibrary().includeMetadata();
        Assertions.assertFalse(assetLibrary.headers.containsKey("include_owner"));
    }

    @Test
    void testAssetQueryOtherThanUID() {
    AssetLibrary query = stack.assetLibrary().where("tags","tag1");
    query.fetchAll(new FetchAssetsCallback() {
        @Override
        public void onCompletion(ResponseType responseType, List<Asset> assets, Error error) {
          System.out.println(assets);
        }
    });
    }

    @Test
    void testFetchFirst10Assets() throws IllegalAccessException {
        AssetLibrary assetLibrary = stack.assetLibrary();
        assetLibrary.skip(0).limit(10).fetchAll(new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, List<Asset> assets, Error error) {
                Assertions.assertNotNull(assets, "Assets list should not be null");
                Assertions.assertTrue(assets.size() <= 10, "Assets fetched should not exceed the limit");
            }
        });
    }

    @Test
    void testFetchAssetsWithSkip() throws IllegalAccessException {
        AssetLibrary assetLibrary = stack.assetLibrary();
        assetLibrary.skip(10).limit(10).fetchAll(new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, List<Asset> assets, Error error) {
                Assertions.assertNotNull(assets, "Assets list should not be null");
                Assertions.assertTrue(assets.size() <= 10, "Assets fetched should not exceed the limit");
            }
        });
    }

    @Test
    void testFetchBeyondAvailableAssets() throws IllegalAccessException {
        AssetLibrary assetLibrary = stack.assetLibrary();
        assetLibrary.skip(5000).limit(10).fetchAll(new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, List<Asset> assets, Error error) {
                Assertions.assertNotNull(assets, "Assets list should not be null");
                Assertions.assertEquals(0, assets.size(), "No assets should be fetched when skip exceeds available assets");
            }
        });
    }

    @Test
    void testFetchAllAssetsInBatches() throws IllegalAccessException {
        AssetLibrary assetLibrary = stack.assetLibrary();
        int limit = 50;
        int totalAssetsFetched[] = {0};

        for (int skip = 0; skip < 150; skip += limit) {
            assetLibrary.skip(skip).limit(limit).fetchAll(new FetchAssetsCallback() {
                @Override
                public void onCompletion(ResponseType responseType, List<Asset> assets, Error error) {
                    totalAssetsFetched[0] += assets.size();
                    Assertions.assertNotNull(assets, "Assets list should not be null");
                    Assertions.assertTrue(assets.size() <= limit, "Assets fetched should not exceed the limit");
                    Assertions.assertEquals(6, totalAssetsFetched[0]);
                }
            });
        }
    }

}
