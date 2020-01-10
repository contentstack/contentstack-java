package com.contentstack.test;

import com.contentstack.sdk.Config;
import com.contentstack.sdk.Contentstack;
import com.contentstack.sdk.Stack;
import org.junit.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.runner.JUnitCore;
import java.util.LinkedHashMap;
import java.util.concurrent.CountDownLatch;


public class TestImageTransformTestcase extends JUnitCore {

    private CountDownLatch latch;
    private Stack stack;
    static final Logger logger = LogManager.getLogger(TestImageTransformTestcase.class.getName());
    private LinkedHashMap<String, Object> imageParams = new LinkedHashMap<String, Object>();
    private final String IMAGE_URL = "https://images.contentstack.io/v3/assets/blt903007d63561dea2/blt638399801b6bd23c/59afa6406c11eb860ddf04aa/download";


    public TestImageTransformTestcase() throws Exception {
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
    public void test_00_fetchAllImageTransformation() throws InterruptedException {

        imageParams.put("auto", "webp");
        imageParams.put("quality", 200);
        imageParams.put("width", 100);
        imageParams.put("height", 50);
        imageParams.put("format", "png");
        imageParams.put("crop", "3:5");
        imageParams.put("trim", "20,20,20,20");
        imageParams.put("disable", "upscale");
        imageParams.put("pad", "10,10,10,10");
        imageParams.put("bg-color", "#FFFFFF");
        imageParams.put("dpr", 20);
        imageParams.put("canvas", "3:5");
        imageParams.put("orient", "l");

        String image_url = stack.ImageTransform(IMAGE_URL, imageParams);
        int counter = 0;
        /* check url contains "?" */
        if (!image_url.equalsIgnoreCase("") && image_url.contains("?")){
            String [] imgKeys = image_url.split("\\?");
            String rightUrl = imgKeys[1];
            String [] getAllPairs = rightUrl.split("\\&");
            counter = 0;
            if (imageParams.size()>0){
                for (int i=0; i<imageParams.size(); i++){
                    String keyValueParis = getAllPairs[i];
                    logger.info( "pairs:--> "+keyValueParis);
                    ++counter;
                }
            }
        }else {
            logger.info( "Testcases Failed");
            try{
                latch.await();
            }catch(Exception e){
                logger.info("---------------||"+e.toString());
            }
        }

        if (counter==imageParams.size()){
            latch.countDown();
            logger.info( "Testcases Passed");
        }else {
            logger.info( "Testcases Failed");
            try{
                latch.await();
            }catch(Exception e){
                logger.info("---------------||"+e.toString());
            }
        }
    }






    @Test
    public void test_01_fetchAllImageTransformation() throws InterruptedException {

        imageParams.put("auto", "webp");
        imageParams.put("quality", 200);
        imageParams.put("width", 100);
        imageParams.put("height", 50);
        imageParams.put("format", "png");
        imageParams.put("crop", "3:5");

        String image_url = stack.ImageTransform(IMAGE_URL, imageParams);
        int counter = 0;
        /* check url contains "?" */
        if (!image_url.equalsIgnoreCase("") && image_url.contains("?")){
            String [] imgKeys = image_url.split("\\?");
            String rightUrl = imgKeys[1];
            String [] getAllPairs = rightUrl.split("\\&");
            counter = 0;
            if (imageParams.size()>0){
                for (int i=0; i<imageParams.size(); i++){
                    String keyValueParis = getAllPairs[i];
                    logger.info( "pairs:--> "+keyValueParis);
                    ++counter;
                }
            }
        }else {
            logger.info( "Testcases Failed");
            try{
                latch.await();
            }catch(Exception e){
                logger.info("---------------||"+e.toString());
            }
        }

        if (counter==imageParams.size()){
            latch.countDown();
            logger.info( "Testcases Passed");
        }else {
            logger.info( "Testcases Failed");
            try{
                latch.await();
            }catch(Exception e){
                logger.info("---------------||"+e.toString());
            }
        }
    }







    @Test
    public void test_02_fetchAllImageTransformation() throws InterruptedException {


        imageParams.put("trim", "20,20,20,20");
        imageParams.put("disable", "upscale");
        imageParams.put("pad", "10,10,10,10");
        imageParams.put("bg-color", "#FFFFFF");
        imageParams.put("dpr", 20);
        imageParams.put("canvas", "3:5");
        imageParams.put("orient", "l");

        String image_url = stack.ImageTransform(IMAGE_URL, imageParams);
        int counter = 0;
        /* check url contains "?" */
        if (!image_url.equalsIgnoreCase("") && image_url.contains("?")){
            String [] imgKeys = image_url.split("\\?");
            String rightUrl = imgKeys[1];
            String [] getAllPairs = rightUrl.split("\\&");
            counter = 0;
            if (imageParams.size()>0){
                for (int i=0; i<imageParams.size(); i++){
                    String keyValueParis = getAllPairs[i];
                    logger.info( "pairs:--> "+keyValueParis);
                    ++counter;
                }
            }
        }else {
            logger.info( "Testcases Failed");
            try{
                latch.await();
            }catch(Exception e){
                logger.info("---------------||"+e.toString());
            }
        }

        if (counter==imageParams.size()){
            latch.countDown();
            logger.info( "Testcases Passed");
        }else {
            logger.info( "Testcases Failed");
            try{
                latch.await();
            }catch(Exception e){
                logger.info("---------------||"+e.toString());
            }
        }
    }




    @Test
    public void test_03_fetchAllImageTransformation() throws InterruptedException {


        imageParams.put("trim", "20,20,20,20");
        imageParams.put("disable", "upscale");
        imageParams.put("canvas", "3:5");
        imageParams.put("orient", "l");

        String image_url = stack.ImageTransform(IMAGE_URL, imageParams);
        int counter = 0;
        /* check url contains "?" */
        if (!image_url.equalsIgnoreCase("") && image_url.contains("?")){
            String [] imgKeys = image_url.split("\\?");
            String rightUrl = imgKeys[1];
            String [] getAllPairs = rightUrl.split("\\&");
            counter = 0;
            if (imageParams.size()>0){
                for (int i=0; i<imageParams.size(); i++){
                    String keyValueParis = getAllPairs[i];
                    logger.info( "pairs:--> "+keyValueParis);
                    ++counter;
                }
            }
        }else {
            logger.info( "Testcases Failed");
            try{
                latch.await();
            }catch(Exception e){
                logger.info("---------------||"+e.toString());
            }
        }

        if (counter==imageParams.size()){
            latch.countDown();
            logger.info( "Testcases Passed");
        }else {
            logger.info( "Testcases Failed");
            try{
                latch.await();
            }catch(Exception e){
                logger.info("---------------||"+e.toString());
            }
        }
    }
}
