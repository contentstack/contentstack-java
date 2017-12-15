package testcases.builtio.contentstack;


import com.builtio.contentstack.*;
import com.builtio.contentstack.Error;
import org.junit.Test;
import org.junit.runner.JUnitCore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Shailesh Mishra on 12/10/17.
 */
public class AssetTestCase extends JUnitCore {



    /* static api key and access token */
    private static final String TAG = "AssetTestCase";
    public static final String DEFAULT_APPLICATION_KEY = "blt12c8ad610ff4ddc2";
    public static final String DEFAULT_ACCESS_TOKEN = "blt43359585f471685188b2e1ba";
    public static final String DEFAULT_ENV = "env1";

    private CountDownLatch latch;
    private Stack stack;
    private String[] containArray;
    private ArrayList<Entry> entries = null;

    public  AssetTestCase() throws Exception {
        Config config = new Config();
        config.setHost("api.contentstack.io");
        stack = Contentstack.stack( DEFAULT_APPLICATION_KEY, DEFAULT_ACCESS_TOKEN, DEFAULT_ENV, config);
        latch = new CountDownLatch(1);
    }





    @Test
    public void test01_Asset_getAsset() throws InterruptedException {

        Entry entry = stack.contentType("multifield").entry("blt1b1cb4f26c4b682e");
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {

                if (error == null) {
                    printLog( "----------Test--Asset-01--Success---------" + entry.toJSON());
                    Asset asset = entry.getAsset("imagefile");
                    printLog( "----------Test--Asset-01--Success---------" + asset.toJSON());
                    printLog( "----------Test--Asset-01--Success---------" + asset.getFileType());
                    printLog( "----------Test--Asset-01--Success---------" + asset.getCreatedBy());
                    printLog( "----------Test--Asset-01--Success---------" + asset.getUpdatedBy());
                    printLog( "----------Test--Asset-01--Success---------" + asset.getFileName());
                    printLog( "----------Test--Asset-01--Success---------" + asset.getFileSize());
                    printLog( "----------Test--Asset-01--Success---------" + asset.getAssetUid());
                    printLog( "----------Test--Asset-01--Success---------" + asset.getUrl());
                    printLog( "----------Test--Asset-01--Success---------" + asset.getCreateAt().getTime());
                    printLog( "----------Test--Asset-01--Success---------" + asset.getUpdateAt().getTime());

                    latch.countDown();
                } else {
                    latch.countDown();
                    printLog( "----------Test--Asset--01--Error---------" + error.getErrorMessage());
                    printLog( "----------Test--Asset--01--Error---------" + error.getErrorCode());
                    printLog( "----------Test--Asset--01--Error---------" + error.getErrors());
                }

            }
        });

        try{
            latch.await();
        }catch(Exception e){
            System.out.println("---------------||"+e.toString());
        }

    }


    @Test
    public void test02_Asset_getAssets() throws InterruptedException{

        final Entry entry = stack.contentType("multifield").entry("blt1b1cb4f26c4b682e");

        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {

                if (error == null) {
                    printLog( "----------Test--ENTRY-02--Success---------" + entry.toJSON());

                    List<Asset> assets = entry.getAssets("file");

                    for (int i = 0; i < assets.size(); i++) {
                        Asset asset = assets.get(i);

                        printLog( "----------Test--Asset-02--Success---------" + i +" ---- " + asset.toJSON());
                        printLog( "----------Test--Asset-02--Success---------" + i +" ---- " + asset.getFileType());
                        printLog( "----------Test--Asset-02--Success---------" + i +" ---- " + asset.getCreatedBy());
                        printLog( "----------Test--Asset-02--Success---------" + i +" ---- " + asset.getUpdatedBy());
                        printLog( "----------Test--Asset-02--Success---------" + i +" ---- " + asset.getFileName());
                        printLog( "----------Test--Asset-02--Success---------" + i +" ---- " + asset.getFileSize());
                        printLog( "----------Test--Asset-02--Success---------" + i +" ---- " + asset.getAssetUid());
                        printLog( "----------Test--Asset-02--Success---------" + i +" ---- " + asset.getUrl());
                        printLog( "----------Test--Asset-02--Success---------" + i +" ---- " + asset.getCreateAt().getTime());
                        printLog( "----------Test--Asset-02--Success---------" + i +" ---- " + asset.getUpdateAt().getTime());

                    }

                    latch.countDown();
                } else {
                    latch.countDown();
                    printLog( "----------Test--Asset--02--Error---------" + error.getErrorMessage());
                    printLog( "----------Test--Asset--02--Error---------" + error.getErrorCode());
                    printLog( "----------Test--Asset--02--Error---------" + error.getErrors());
                }

            }
        });

        try{
            latch.await();

        }catch(Exception e){
            System.out.println("---------------||"+e.toString());
        }
    }


    @Test
    public void test03_Asset_fetch(){
        final Object result[] = new Object[2];
        final Asset asset = stack.asset("blt5312f71416d6e2c8");
        asset.fetch(new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {

                if(error == null){

                    System.out.println( "----------Test--Asset-03--Success---------" + asset.toJSON());
                    System.out.println( "----------Test--Asset-03--Success---------" + asset.getFileType());
                    System.out.println( "----------Test--Asset-03--Success---------" + asset.getCreatedBy());
                    System.out.println( "----------Test--Asset-03--Success---------" + asset.getUpdatedBy());
                    System.out.println( "----------Test--Asset-03--Success---------" + asset.getFileName());
                    System.out.println( "----------Test--Asset-03--Success---------" + asset.getFileSize());
                    System.out.println("----------Test--Asset-03--Success---------" + asset.getAssetUid());
                    System.out.println( "----------Test--Asset-03--Success---------" + asset.getUrl());
                    System.out.println( "----------Test--Asset-03--Success---------" + asset.getCreateAt().getTime());
                    System.out.println( "----------Test--Asset-03--Success---------" + asset.getUpdateAt().getTime());
                    result[0] = asset;
                    latch.countDown();
                }else {
                    latch.countDown();
                    result[0] = error;
                    System.out.println( "----------Test--Asset--03--Error---------" + error.getErrorMessage());
                    System.out.println("----------Test--Asset--03--Error---------" + error.getErrorCode());
                    System.out.println( "----------Test--Asset--03--Error---------" + error.getErrors());
                    latch.countDown();
                }

            }
        });

        try{
            latch.await();
            //assertEquals(true, result[0] instanceof Asset);
        }catch(Exception e){
            System.out.println("---------------||"+e.toString());
        }
    }



    @Test
    public void test04_AssetLibrary_fetch()  throws  InterruptedException {
        final AssetLibrary assetLibrary = stack.assetLibrary();
        assetLibrary.fetchAll(new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, List<Asset> assets, Error error) {

                if (error == null) {

                    for (int i = 0; i < assets.size(); i++) {
                        Asset asset = assets.get(i);

                        printLog( "----------Test--Asset-04--Success---------" + i +" ---- " + asset.toJSON());
                        printLog( "----------Test--Asset-04--Success---------" + i +" ---- " + asset.getFileType());
                        printLog( "----------Test--Asset-04--Success---------" + i +" ---- " + asset.getCreatedBy());
                        printLog( "----------Test--Asset-04--Success---------" + i +" ---- " + asset.getUpdatedBy());
                        printLog( "----------Test--Asset-04--Success---------" + i +" ---- " + asset.getFileName());
                        printLog( "----------Test--Asset-04--Success---------" + i +" ---- " + asset.getFileSize());
                        printLog( "----------Test--Asset-04--Success---------" + i +" ---- " + asset.getAssetUid());
                        printLog( "----------Test--Asset-04--Success---------" + i +" ---- " + asset.getUrl());
                        printLog( "----------Test--Asset-04--Success---------" + i +" ---- " + asset.getCreateAt().getTime());
                        printLog( "----------Test--Asset-04--Success---------" + i +" ---- " + asset.getUpdateAt().getTime());
                    }

                    latch.countDown();

                } else {
                    latch.countDown();
                    printLog( "----------Test--Asset--04--Error---------" + error.getErrorMessage());
                    printLog( "----------Test--Asset--04--Error---------" + error.getErrorCode());
                    printLog( "----------Test--Asset--04--Error---------" + error.getErrors());

                }
            }
        });

        try{
            latch.await();
        }catch(Exception e){
            System.out.println("---------------||"+e.toString());
        }
    }


    @Test
    public void test05_AssetLibrary_includeCount_fetch() throws InterruptedException{

        final AssetLibrary assetLibrary = stack.assetLibrary();
        assetLibrary.includeCount();
        assetLibrary.fetchAll(new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, List<Asset> assets, Error error) {

                if (error == null) {

                    System.out.println("count = [" + assetLibrary.getCount() + "]");

                    for (int i = 0; i < assets.size(); i++) {
                        Asset asset = assets.get(i);

                        printLog( "----------Test--Asset-05--Success---------" + i +" ---- " + asset.toJSON());
                        printLog( "----------Test--Asset-05--Success---------" + i +" ---- " + asset.getFileType());
                        printLog( "----------Test--Asset-05--Success---------" + i +" ---- " + asset.getCreatedBy());
                        printLog( "----------Test--Asset-05--Success---------" + i +" ---- " + asset.getUpdatedBy());
                        printLog( "----------Test--Asset-05--Success---------" + i +" ---- " + asset.getFileName());
                        printLog( "----------Test--Asset-05--Success---------" + i +" ---- " + asset.getFileSize());
                        printLog( "----------Test--Asset-05--Success---------" + i +" ---- " + asset.getAssetUid());
                        printLog( "----------Test--Asset-05--Success---------" + i +" ---- " + asset.getUrl());
                        printLog( "----------Test--Asset-05--Success---------" + i +" ---- " + asset.getCreateAt().getTime());
                        printLog( "----------Test--Asset-05--Success---------" + i +" ---- " + asset.getUpdateAt().getTime());

                    }

                    latch.countDown();
                } else {
                    latch.countDown();
                    printLog( "----------Test--Asset--05--Error---------" + error.getErrorMessage());
                    printLog( "----------Test--Asset--05--Error---------" + error.getErrorCode());
                    printLog( "----------Test--Asset--05--Error---------" + error.getErrors());
                }

            }
        });

        try{
            latch.await();
        }catch(Exception e){
            System.out.println("---------------||"+e.toString());
        }
    }


    @Test
    public void test06_AssetLibrary_includeRelativeUrl_fetch(){

        final AssetLibrary assetLibrary = stack.assetLibrary();
        assetLibrary.includeRelativeUrl();
        assetLibrary.fetchAll(new FetchAssetsCallback() {
            @Override
            public void onCompletion(ResponseType responseType, List<Asset> assets, Error error) {

                System.out.println( "----------Test--Asset-06--Success---------");
                if (error == null) {

                    for (int i = 0; i < assets.size(); i++) {
                        Asset asset = assets.get(i);

                        System.out.println( "----------Test--Asset-06--Success---------" + i +" ---- " + asset.toJSON());
                        System.out.println( "----------Test--Asset-06--Success---------" + i +" ---- " + asset.getFileType());
                        System.out.println( "----------Test--Asset-06--Success---------" + i +" ---- " + asset.getCreatedBy());
                        System.out.println( "----------Test--Asset-06--Success---------" + i +" ---- " + asset.getUpdatedBy());
                        System.out.println( "----------Test--Asset-06--Success---------" + i +" ---- " + asset.getFileName());
                        System.out.println( "----------Test--Asset-06--Success---------" + i +" ---- " + asset.getFileSize());
                        System.out.println( "----------Test--Asset-06--Success---------" + i +" ---- " + asset.getAssetUid());
                        System.out.println( "----------Test--Asset-06--Success---------" + i +" ---- " + asset.getUrl());
                        System.out.println( "----------Test--Asset-06--Success---------" + i +" ---- " + asset.getCreateAt().getTime());
                        System.out.println( "----------Test--Asset-06--Success---------" + i +" ---- " + asset.getUpdateAt().getTime());

                    }

                    latch.countDown();
                } else {
                    latch.countDown();
                    System.out.println( "----------Test--Asset--05--Error---------" + error.getErrorMessage());
                    System.out.println( "----------Test--Asset--05--Error---------" + error.getErrorCode());
                    System.out.println( "----------Test--Asset--05--Error---------" + error.getErrors());
                }

            }
        });

        try{
            latch.await();
        }catch(Exception e){
            System.out.println("---------------||"+e.toString());
        }
    }






    @Test
    public void test_14_StackGetParams() throws InterruptedException {
        final Object result[] = new Object[2];
        final Asset asset = stack.asset("blt5312f71416d6e2c8");
        asset.addParam("key", "some_value");

        asset.fetch(new FetchResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {

                if(error == null){

                    System.out.println( "----------Test--Asset-03--Success---------" + asset.toJSON());
                    System.out.println( "----------Test--Asset-03--Success---------" + asset.getFileType());
                    System.out.println( "----------Test--Asset-03--Success---------" + asset.getCreatedBy());
                    System.out.println( "----------Test--Asset-03--Success---------" + asset.getUpdatedBy());
                    System.out.println( "----------Test--Asset-03--Success---------" + asset.getFileName());
                    System.out.println( "----------Test--Asset-03--Success---------" + asset.getFileSize());
                    System.out.println("-----------Test--Asset-03--Success---------" + asset.getAssetUid());
                    System.out.println( "----------Test--Asset-03--Success---------" + asset.getUrl());
                    System.out.println( "----------Test--Asset-03--Success---------" + asset.getCreateAt().getTime());
                    System.out.println( "----------Test--Asset-03--Success---------" + asset.getUpdateAt().getTime());
                    result[0] = asset;
                    latch.countDown();
                }else {

                    result[0] = error;
                    System.out.println( "----------Test--Asset--03--Error---------" + error.getErrorMessage());
                    System.out.println("----------Test--Asset--03--Error---------" + error.getErrorCode());
                    System.out.println( "----------Test--Asset--03--Error---------" + error.getErrors());
                    latch.countDown();
                }

            }
        });

    }


    private void printLog( String logMessage){
        System.out.println(logMessage);
    }




}
