package com.contentstack.test;
import com.contentstack.sdk.Error;
import com.contentstack.sdk.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import java.util.List;
import java.util.concurrent.CountDownLatch;


public class TestAssetTestCase extends JUnitCore {

    private Logger logger =  LogManager.getLogger(TestAssetTestCase.class);
    public TestAssetTestCase() { }
    private CountDownLatch latch;
    private Stack stack;

    @Before
    public void setUp() throws Exception {
        Config config = new Config();
        config.setHost("cdn.contentstack.io");
        String DEFAULT_APPLICATION_KEY = "blt12c8ad610ff4ddc2";
        String DEFAULT_ACCESS_TOKEN = "blt43359585f471685188b2e1ba";
        String DEFAULT_ENV = "env1";

        //setup for EU uncomment below
        //config.setRegion(Config.ContentstackRegion.EU);
        //String DEFAULT_APPLICATION_KEY = "bltc12b8d966127fa01";
        //String DEFAULT_ACCESS_TOKEN = "cse3ab6095485b70ab2713ed60";

        stack = Contentstack.stack(DEFAULT_APPLICATION_KEY, DEFAULT_ACCESS_TOKEN, DEFAULT_ENV, config);
        latch = new CountDownLatch(1);

    }

    @Test
    public void test01_Asset_getAsset() {

        final Entry entry = stack.contentType("multifield").entry("blt1b1cb4f26c4b682e");
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {

                if (error == null) {
                    logger.info( "----------Test--Asset-01--Success---------" + entry.toJSON());
                    Asset asset = entry.getAsset("imagefile");

                    logger.info("----------Test--Asset-01--Success---------" + asset.toJSON());
                    logger.info("----------Test--Asset-01--Success---------" + asset.getFileType());
                    logger.info("----------Test--Asset-01--Success---------" + asset.getCreatedBy());
                    logger.info("----------Test--Asset-01--Success---------" + asset.getUpdatedBy());
                    logger.info("----------Test--Asset-01--Success---------" + asset.getFileName());
                    logger.info("----------Test--Asset-01--Success---------" + asset.getFileSize());
                    logger.info("----------Test--Asset-01--Success---------" + asset.getAssetUid());
                    logger.info("----------Test--Asset-01--Success---------" + asset.getUrl());
                    logger.info("----------Test--Asset-01--Success---------" + asset.getCreateAt().getTime());
                    logger.info("----------Test--Asset-01--Success---------" + asset.getUpdateAt().getTime());

                    latch.countDown();
                } else {
                    latch.countDown();
                    logger.info("----------Test--Asset--01--Error---------" + error.getErrorMessage());
                    logger.info("----------Test--Asset--01--Error---------" + error.getErrorCode());
                    logger.info("----------Test--Asset--01--Error---------" + error.getErrors());
                }

            }
        });

        try{
            latch.await();
        }catch(Exception e){
            logger.info("---------------||"+e.toString());
        }

    }


    @Test
    public void test02_Asset_getAssets() {

        final Entry entry = stack.contentType("multifield").entry("blt1b1cb4f26c4b682e");
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {

                if (error == null) {
                    logger.info("----------Test--ENTRY-02--Success---------" + entry.toJSON());
                    List<Asset> assets = entry.getAssets("file");
                    for (int i = 0; i < assets.size(); i++) {
                        Asset asset = assets.get(i);

                        logger.info("----------Test--Asset-02--Success---------" + i +" ---- " + asset.toJSON());
                        logger.info("----------Test--Asset-02--Success---------" + i +" ---- " + asset.getFileType());
                        logger.info("----------Test--Asset-02--Success---------" + i +" ---- " + asset.getCreatedBy());
                        logger.info("----------Test--Asset-02--Success---------" + i +" ---- " + asset.getUpdatedBy());
                        logger.info("----------Test--Asset-02--Success---------" + i +" ---- " + asset.getFileName());
                        logger.info("----------Test--Asset-02--Success---------" + i +" ---- " + asset.getFileSize());
                        logger.info("----------Test--Asset-02--Success---------" + i +" ---- " + asset.getAssetUid());
                        logger.info("----------Test--Asset-02--Success---------" + i +" ---- " + asset.getUrl());
                        logger.info("----------Test--Asset-02--Success---------" + i +" ---- " + asset.getCreateAt().getTime());
                        logger.info("----------Test--Asset-02--Success---------" + i +" ---- " + asset.getUpdateAt().getTime());

                    }

                    latch.countDown();
                } else {
                    latch.countDown();
                    logger.info("----------Test--Asset--02--Error---------" + error.getErrorMessage());
                    logger.info("----------Test--Asset--02--Error---------" + error.getErrorCode());
                    logger.info("----------Test--Asset--02--Error---------" + error.getErrors());
                }

            }
        });

        try{
            latch.await();

        }catch(Exception e){
            logger.info("---------------||"+e.toString());
        }
    }


    @Test
    public void test03_Asset_fetch(){
        final Object[] result = new Object[2];
        final Asset asset = stack.asset("blt5312f71416d6e2c8");
        asset.fetch(new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {

                if(error == null){
                    logger.info("----------Test--Asset-03--Success---------" + asset.toJSON());
                    logger.info("----------Test--Asset-03--Success---------" + asset.getFileType());
                    logger.info("----------Test--Asset-03--Success---------" + asset.getCreatedBy());
                    logger.info("----------Test--Asset-03--Success---------" + asset.getUpdatedBy());
                    logger.info("----------Test--Asset-03--Success---------" + asset.getFileName());
                    logger.info("----------Test--Asset-03--Success---------" + asset.getFileSize());
                    logger.info("----------Test--Asset-03--Success---------" + asset.getAssetUid());
                    logger.info("----------Test--Asset-03--Success---------" + asset.getUrl());
                    logger.info("----------Test--Asset-03--Success---------" + asset.getCreateAt().getTime());
                    logger.info("----------Test--Asset-03--Success---------" + asset.getUpdateAt().getTime());
                    result[0] = asset;
                    latch.countDown();
                }else {
                    latch.countDown();
                    result[0] = error;
                    logger.info("----------Test--Asset--03--Error---------" + error.getErrorMessage());
                    logger.info("----------Test--Asset--03--Error---------" + error.getErrorCode());
                    logger.info("----------Test--Asset--03--Error---------" + error.getErrors());
                    latch.countDown();
                }

            }
        });

        try{
            latch.await();
            //assertEquals(true, result[0] instanceof Asset);
        }catch(Exception e){
            logger.info("---------------||"+e.toString());
        }
    }



    @Test
    public void test04_AssetLibrary_fetch()   {
        final AssetLibrary assetLibrary = stack.assetLibrary();
        assetLibrary.fetchAll(new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, List<Asset> assets, Error error) {

                if (error == null) {

                    for (int i = 0; i < assets.size(); i++) {
                        Asset asset = assets.get(i);

                        logger.info("----------Test--Asset-04--Success---------" + i +" ---- " + asset.toJSON());
                        logger.info("----------Test--Asset-04--Success---------" + i +" ---- " + asset.getFileType());
                        logger.info("----------Test--Asset-04--Success---------" + i +" ---- " + asset.getCreatedBy());
                        logger.info("----------Test--Asset-04--Success---------" + i +" ---- " + asset.getUpdatedBy());
                        logger.info("----------Test--Asset-04--Success---------" + i +" ---- " + asset.getFileName());
                        logger.info("----------Test--Asset-04--Success---------" + i +" ---- " + asset.getFileSize());
                        logger.info("----------Test--Asset-04--Success---------" + i +" ---- " + asset.getAssetUid());
                        logger.info("----------Test--Asset-04--Success---------" + i +" ---- " + asset.getUrl());
                        logger.info("----------Test--Asset-04--Success---------" + i +" ---- " + asset.getCreateAt().getTime());
                        logger.info("----------Test--Asset-04--Success---------" + i +" ---- " + asset.getUpdateAt().getTime());
                    }

                    latch.countDown();

                } else {
                    latch.countDown();
                    logger.info("----------Test--Asset--04--Error---------" + error.getErrorMessage());
                    logger.info("----------Test--Asset--04--Error---------" + error.getErrorCode());
                    logger.info("----------Test--Asset--04--Error---------" + error.getErrors());

                }
            }
        });

        try{
            latch.await();
        }catch(Exception e){
            logger.info("---------------||"+e.toString());
        }
    }


    @Test
    public void test05_AssetLibrary_includeCount_fetch() {

        final AssetLibrary assetLibrary = stack.assetLibrary();
        assetLibrary.includeCount();
        assetLibrary.fetchAll(new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, List<Asset> assets, Error error) {

                if (error == null) {

                    logger.info("count = [" + assetLibrary.getCount() + "]");

                    for (int i = 0; i < assets.size(); i++) {
                        Asset asset = assets.get(i);

                        logger.info("----------Test--Asset-05--Success---------" + i +" ---- " + asset.toJSON());
                        logger.info("----------Test--Asset-05--Success---------" + i +" ---- " + asset.getFileType());
                        logger.info("----------Test--Asset-05--Success---------" + i +" ---- " + asset.getCreatedBy());
                        logger.info("----------Test--Asset-05--Success---------" + i +" ---- " + asset.getUpdatedBy());
                        logger.info("----------Test--Asset-05--Success---------" + i +" ---- " + asset.getFileName());
                        logger.info("----------Test--Asset-05--Success---------" + i +" ---- " + asset.getFileSize());
                        logger.info("----------Test--Asset-05--Success---------" + i +" ---- " + asset.getAssetUid());
                        logger.info("----------Test--Asset-05--Success---------" + i +" ---- " + asset.getUrl());
                        logger.info("----------Test--Asset-05--Success---------" + i +" ---- " + asset.getCreateAt().getTime());
                        logger.info("----------Test--Asset-05--Success---------" + i +" ---- " + asset.getUpdateAt().getTime());

                    }

                    latch.countDown();
                } else {
                    latch.countDown();
                    logger.info("----------Test--Asset--05--Error---------" + error.getErrorMessage());
                    logger.info("----------Test--Asset--05--Error---------" + error.getErrorCode());
                    logger.info("----------Test--Asset--05--Error---------" + error.getErrors());
                }

            }
        });

        try{
            latch.await();
        }catch(Exception e){
            logger.info("---------------||"+e.toString());
        }
    }


    @Test
    public void test06_AssetLibrary_includeRelativeUrl_fetch(){

        final AssetLibrary assetLibrary = stack.assetLibrary();
        assetLibrary.includeRelativeUrl();
        assetLibrary.fetchAll(new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, List<Asset> assets, Error error) {

                logger.info("----------Test--Asset-06--Success---------");
                if (error == null) {

                    for (int i = 0; i < assets.size(); i++) {
                        Asset asset = assets.get(i);

                        logger.info("----------Test--Asset-06--Success---------" + i +" ---- " + asset.toJSON());
                        logger.info("----------Test--Asset-06--Success---------" + i +" ---- " + asset.getFileType());
                        logger.info("----------Test--Asset-06--Success---------" + i +" ---- " + asset.getCreatedBy());
                        logger.info("----------Test--Asset-06--Success---------" + i +" ---- " + asset.getUpdatedBy());
                        logger.info("----------Test--Asset-06--Success---------" + i +" ---- " + asset.getFileName());
                        logger.info("----------Test--Asset-06--Success---------" + i +" ---- " + asset.getFileSize());
                        logger.info("----------Test--Asset-06--Success---------" + i +" ---- " + asset.getAssetUid());
                        logger.info("----------Test--Asset-06--Success---------" + i +" ---- " + asset.getUrl());
                        logger.info("----------Test--Asset-06--Success---------" + i +" ---- " + asset.getCreateAt().getTime());
                        logger.info("----------Test--Asset-06--Success---------" + i +" ---- " + asset.getUpdateAt().getTime());

                    }

                    latch.countDown();
                } else {
                    latch.countDown();
                    logger.info("----------Test--Asset--05--Error---------" + error.getErrorMessage());
                    logger.info("----------Test--Asset--05--Error---------" + error.getErrorCode());
                    logger.info("----------Test--Asset--05--Error---------" + error.getErrors());
                }

            }
        });

        try{
            latch.await();
        }catch(Exception e){
            logger.info("---------------||"+e.toString());
        }
    }






    @Test
    public void test_14_StackGetParams() {
        final Object[] result = new Object[2];
        final Asset asset = stack.asset("blt5312f71416d6e2c8");
        asset.addParam("key", "some_value");

        asset.fetch(new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {

                if(error == null){

                    logger.info("----------Test--Asset-03--Success---------" + asset.toJSON());
                    logger.info("----------Test--Asset-03--Success---------" + asset.getFileType());
                    logger.info("----------Test--Asset-03--Success---------" + asset.getCreatedBy());
                    logger.info("----------Test--Asset-03--Success---------" + asset.getUpdatedBy());
                    logger.info("----------Test--Asset-03--Success---------" + asset.getFileName());
                    logger.info("----------Test--Asset-03--Success---------" + asset.getFileSize());
                    logger.info("-----------Test--Asset-03--Success---------" + asset.getAssetUid());
                    logger.info("----------Test--Asset-03--Success---------" + asset.getUrl());
                    logger.info("----------Test--Asset-03--Success---------" + asset.getCreateAt().getTime());
                    logger.info("----------Test--Asset-03--Success---------" + asset.getUpdateAt().getTime());
                    result[0] = asset;
                    latch.countDown();
                }else {

                    result[0] = error;
                    logger.info("----------Test--Asset--03--Error---------" + error.getErrorMessage());
                    logger.info("----------Test--Asset--03--Error---------" + error.getErrorCode());
                    logger.info("----------Test--Asset--03--Error---------" + error.getErrors());
                    latch.countDown();
                }

            }
        });

    }


}
