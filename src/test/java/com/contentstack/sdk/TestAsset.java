package com.contentstack.sdk;

import java.util.List;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.junit.jupiter.api.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestAsset {

    private final Logger logger = Logger.getLogger(TestAsset.class.getName());
    private String assetUid;
    private final Stack stack = Credentials.getStack();

    private String envChecker() {
        String githubActions = System.getenv("GITHUB_ACTIONS");
        if (githubActions != null && githubActions.equals("true")) {
            System.out.println("Tests are running in GitHub Actions environment.");
            String mySecretKey = System.getenv("API_KEY");
            System.out.println("My Secret Key: " + mySecretKey);
            return "GitHub";
        } else {
            System.out.println("Tests are running in a local environment.");
            return "local";
        }
    }

    @Test
    @Order(1)
    void testNewAssetLibrary() {
        envChecker();
        AssetLibrary assets = stack.assetLibrary();
        assets.fetchAll(new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, List<Asset> assets, Error error) {
                Asset model = assets.get(0);
                assetUid = model.getAssetUid();
                Assertions.assertTrue(model.getAssetUid().startsWith("blt"));
                Assertions.assertNotNull( model.getFileType());
                Assertions.assertNotNull( model.getFileSize());
                Assertions.assertNotNull( model.getFileName());
                Assertions.assertTrue(model.toJSON().has("created_at"));
                Assertions.assertTrue(model.getCreatedBy().startsWith("blt"));
                Assertions.assertEquals("gregory", model.getUpdateAt().getCalendarType());
                Assertions.assertTrue(model.getUpdatedBy().startsWith("blt"));
                Assertions.assertEquals("", model.getDeletedBy());
            }
        });
    }

    @Test
    @Order(2)
    void testNewAssetZOnlyForOrderByUid() {
        String[] tags = {"black", "white", "red"};
        Asset asset = stack.asset(assetUid);
        asset.includeFallback().addParam("fake@header", "fake@header").setTags(tags).fetch(new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                Assertions.assertTrue(asset.getAssetUid().startsWith("blt"));
                Assertions.assertNotNull( asset.getFileType());
                Assertions.assertNotNull( asset.getFileSize());
                Assertions.assertNotNull( asset.getFileName());
                Assertions.assertTrue(asset.toJSON().has("created_at"));
                Assertions.assertTrue(asset.getCreatedBy().startsWith("blt"));
                Assertions.assertEquals("gregory", asset.getUpdateAt().getCalendarType());
                Assertions.assertTrue(asset.getUpdatedBy().startsWith("blt"));
                Assertions.assertNull(asset.getDeleteAt());
                Assertions.assertEquals("gregory", asset.getCreateAt().getCalendarType());
                Assertions.assertEquals("", asset.getDeletedBy());
            }
        });
    }

    @Test
    void testAssetDefaultConstructor() {
        logger.fine("We are working with fake apis");
        Asset asset = new Asset();
        Assertions.assertNotNull(asset);
    }

    @Test
    void testAddHeader() {
        String headerKey = "fakeKey";
        Asset assetInstance = stack.asset();
        assetInstance.setHeader(headerKey, "fakeValue");
        Assertions.assertTrue(assetInstance.headers.containsKey(headerKey));
    }

    @Test
    void testRemoveHeader() {
        String headerKey = "fakeKey";
        Asset assetInstance = stack.asset();
        assetInstance.removeHeader(headerKey);
        Assertions.assertFalse(assetInstance.headers.containsKey(headerKey));
    }

    @Test
    void testSetAssetUid() {
        String headerKey = "asset@fakeuid";
        Asset assetInstance = stack.asset();
        assetInstance.setUid(headerKey);
        Assertions.assertEquals(headerKey, assetInstance.assetUid);
    }

    @Test
    void testSetAssetTagsLength() {
        String[] tags = {"gif", "img", "landscape", "portrait"};
        Asset assetInstance = stack.asset();
        assetInstance.setTags(tags);
        Assertions.assertEquals(tags.length, assetInstance.tagsArray.length);
    }

    @Test
    void testGetAssetTags() {
        String[] tags = {"gif", "img", "landscape", "portrait"};
        Asset assetInstance = stack.asset();
        assetInstance.setTags(tags);
        Assertions.assertEquals(tags.length, assetInstance.getTags().length);
    }

    @Test
    void testAssetIncludeDimension() {
        Asset assetInstance = stack.asset();
        assetInstance.includeDimension();
        Assertions.assertTrue(assetInstance.urlQueries.has("include_dimension"));
    }

    @Test
    void testAssetIncludeFallback() {
        Asset assetInstance = stack.asset();
        assetInstance.includeFallback();
        Assertions.assertTrue(assetInstance.urlQueries.has("include_fallback"));
    }

    @Test
    void testAssetAddParam() {
        Asset assetInstance = stack.asset();
        assetInstance.addParam("fake@Param", "fake@Param");
        Assertions.assertTrue(assetInstance.urlQueries.has("fake@Param"));
    }

    @Test
    void testNewAssetInstance() {
        String fakeAssetUid = "fakeAssetUid";
        Asset assetInstance = stack.asset(fakeAssetUid);
        Assertions.assertEquals(fakeAssetUid, assetInstance.assetUid);
    }

    @Test
    void assetConfigure() {
        Asset assetInstance = stack.asset("assetuid@fake");
        Assertions.assertEquals("assetuid@fake", assetInstance.assetUid);
    }

    JSONObject rawJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uid", "something@fake");
        jsonObject.put("created_at", "2016-04-06T11:06:10.601Z");
        jsonObject.put("updated_at", "2016-12-16T12:36:33.961Z");
        jsonObject.put("created_by", "something@fake");
        jsonObject.put("content_type", "image/jpeg");
        jsonObject.put("file_size", "482141");
        JSONObject jsonAsset = new JSONObject();
        jsonAsset.put("asset", jsonObject);
        return jsonAsset;
    }

    @Test
    void testNewAssetConfigure() {
        JSONObject assetObject = rawJson();
        Asset asset = stack.asset("fake@uid");
        asset.configure(assetObject);
        Assertions.assertTrue(asset.json.has("asset"));
    }

    @Test
    void testAssetIncludeBranch() {
        Asset asset = stack.asset("fake@uid");
        asset.includeBranch();
        Assertions.assertTrue(asset.urlQueries.has("include_branch"));
    }

    @Test
    void testAssetIncludeOwner() {
        Asset asset = stack.asset("fake@uid");
        asset.includeMetadata();
        Assertions.assertTrue(asset.urlQueries.has("include_metadata"));
    }

}
