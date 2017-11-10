package testcases.builtio.contentstack;

import com.builtio.contentstack.Config;
import com.builtio.contentstack.Contentstack;
import com.builtio.contentstack.Stack;
import org.junit.Test;
import org.junit.runner.JUnitCore;

import java.util.LinkedHashMap;
import java.util.concurrent.CountDownLatch;

public class ImageTransformTestcase extends JUnitCore {

    private static final String TAG = "AssetTestCase";
    public static final String DEFAULT_APPLICATION_KEY = "blt12c8ad610ff4ddc2";
    public static final String DEFAULT_ACCESS_TOKEN = "blt43359585f471685188b2e1ba";
    public static final String DEFAULT_ENV = "env1";

    private CountDownLatch latch;
    private Stack stack;
    private LinkedHashMap imageParams = new LinkedHashMap();
    private final String IMAGE_URL = "https://images.contentstack.io/v3/assets/blt903007d63561dea2/blt638399801b6bd23c/59afa6406c11eb860ddf04aa/download";


    public ImageTransformTestcase() throws Exception {
        Config config = new Config();
        config.setHost("api.contentstack.io");
        stack = Contentstack.stack( DEFAULT_APPLICATION_KEY, DEFAULT_ACCESS_TOKEN, DEFAULT_ENV, config);
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
                    System.out.println( "pairs:--> "+keyValueParis);
                    ++counter;
                }
            }
        }else {
            System.out.println( "Testcases Failed");
            try{
                latch.await();
            }catch(Exception e){
                System.out.println("---------------||"+e.toString());
            }
        }

        if (counter==imageParams.size()){
            latch.countDown();
            System.out.println( "Testcases Passed");
        }else {
            System.out.println( "Testcases Failed");
            try{
                latch.await();
            }catch(Exception e){
                System.out.println("---------------||"+e.toString());
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
                    System.out.println( "pairs:--> "+keyValueParis);
                    ++counter;
                }
            }
        }else {
            System.out.println( "Testcases Failed");
            try{
                latch.await();
            }catch(Exception e){
                System.out.println("---------------||"+e.toString());
            }
        }

        if (counter==imageParams.size()){
            latch.countDown();
            System.out.println( "Testcases Passed");
        }else {
            System.out.println( "Testcases Failed");
            try{
                latch.await();
            }catch(Exception e){
                System.out.println("---------------||"+e.toString());
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
                    System.out.println( "pairs:--> "+keyValueParis);
                    ++counter;
                }
            }
        }else {
            System.out.println( "Testcases Failed");
            try{
                latch.await();
            }catch(Exception e){
                System.out.println("---------------||"+e.toString());
            }
        }

        if (counter==imageParams.size()){
            latch.countDown();
            System.out.println( "Testcases Passed");
        }else {
            System.out.println( "Testcases Failed");
            try{
                latch.await();
            }catch(Exception e){
                System.out.println("---------------||"+e.toString());
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
                    System.out.println( "pairs:--> "+keyValueParis);
                    ++counter;
                }
            }
        }else {
            System.out.println( "Testcases Failed");
            try{
                latch.await();
            }catch(Exception e){
                System.out.println("---------------||"+e.toString());
            }
        }

        if (counter==imageParams.size()){
            latch.countDown();
            System.out.println( "Testcases Passed");
        }else {
            System.out.println( "Testcases Failed");
            try{
                latch.await();
            }catch(Exception e){
                System.out.println("---------------||"+e.toString());
            }
        }
    }
}
