package com.contentstack.sdk;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.*;
import org.junit.runner.JUnitCore;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ImageTransformTestcase extends JUnitCore {

    private static final Logger logger = Logger.getLogger(ImageTransformTestcase.class.getName());
    private static Stack stack;
    private final LinkedHashMap<String, Object> imageParams = new LinkedHashMap<>();
    private final String IMAGE_URL = "https://images.contentstack.io/v3/assets/blt903007d63561dea2/blt638399801b6bd23c/59afa6406c11eb860ddf04aa/download";


    @BeforeClass
    public static void oneTimeSetUp() throws Exception {
        logger.setLevel(Level.FINE);
        Dotenv dotenv = Dotenv.load();
        String DEFAULT_API_KEY = dotenv.get("API_KEY");
        String DEFAULT_DELIVERY_TOKEN = dotenv.get("DELIVERY_TOKEN");
        String DEFAULT_ENV = dotenv.get("ENVIRONMENT");
        String DEFAULT_HOST = dotenv.get("HOST");
        Config config = new Config();
        config.setHost(DEFAULT_HOST);
        assert DEFAULT_API_KEY != null;
        stack = Contentstack.stack(DEFAULT_API_KEY, DEFAULT_DELIVERY_TOKEN, DEFAULT_ENV, config);
        logger.info("test started...");
    }

    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
        logger.info("When all the test cases of class finishes...");
    }


    @After
    public void tearDown() {
        logger.info("Runs after every testcase completes.");
    }


    @Test
    public void test_00_fetchAllImageTransformation() {

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
        if (!image_url.equalsIgnoreCase("") && image_url.contains("?")) {
            String[] imgKeys = image_url.split("\\?");
            String rightUrl = imgKeys[1];
            String[] getAllPairs = rightUrl.split("&");
            counter = 0;
            if (imageParams.size() > 0) {
                for (int i = 0; i < imageParams.size(); i++) {
                    String keyValueParis = getAllPairs[i];
                    logger.info("pairs:--> " + keyValueParis);
                    ++counter;
                }
            }
        } else {
            logger.info("Testcases Failed");
        }
        if (counter == imageParams.size()) {
            logger.info("Testcases Passed");
        } else {
            logger.info("Testcases Failed");
        }
    }


    @Test
    public void test_01_fetchAllImageTransformation() {

        imageParams.put("auto", "webp");
        imageParams.put("quality", 200);
        imageParams.put("width", 100);
        imageParams.put("height", 50);
        imageParams.put("format", "png");
        imageParams.put("crop", "3:5");

        String image_url = stack.ImageTransform(IMAGE_URL, imageParams);
        int counter = 0;
        /* check url contains "?" */
        if (!image_url.equalsIgnoreCase("") && image_url.contains("?")) {
            String[] imgKeys = image_url.split("\\?");
            String rightUrl = imgKeys[1];
            String[] getAllPairs = rightUrl.split("&");
            counter = 0;
            if (imageParams.size() > 0) {
                for (int i = 0; i < imageParams.size(); i++) {
                    String keyValueParis = getAllPairs[i];
                    logger.info("pairs:--> " + keyValueParis);
                    ++counter;
                }
            }
        } else {
            logger.info("Testcases Failed");
        }

        if (counter == imageParams.size()) {
            logger.info("Testcases Passed");
        } else {
            logger.info("Testcases Failed");
        }
    }


    @Test
    public void test_02_fetchAllImageTransformation() {
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
        if (!image_url.equalsIgnoreCase("") && image_url.contains("?")) {
            String[] imgKeys = image_url.split("\\?");
            String rightUrl = imgKeys[1];
            String[] getAllPairs = rightUrl.split("&");
            counter = 0;
            if (imageParams.size() > 0) {
                for (int i = 0; i < imageParams.size(); i++) {
                    String keyValueParis = getAllPairs[i];
                    logger.info("pairs:--> " + keyValueParis);
                    ++counter;
                }
            }
        } else {
            logger.info("Testcases Failed");

        }

        if (counter == imageParams.size()) {

            logger.info("Testcases Passed");
        } else {
            logger.info("Testcases Failed");

        }
    }


    @Test
    public void test_03_fetchAllImageTransformation() {


        imageParams.put("trim", "20,20,20,20");
        imageParams.put("disable", "upscale");
        imageParams.put("canvas", "3:5");
        imageParams.put("orient", "l");

        String image_url = stack.ImageTransform(IMAGE_URL, imageParams);
        int counter = 0;
        /* check url contains "?" */
        if (!image_url.equalsIgnoreCase("") && image_url.contains("?")) {
            String[] imgKeys = image_url.split("\\?");
            String rightUrl = imgKeys[1];
            String[] getAllPairs = rightUrl.split("&");
            counter = 0;
            if (imageParams.size() > 0) {
                for (int i = 0; i < imageParams.size(); i++) {
                    String keyValueParis = getAllPairs[i];
                    logger.info("pairs:--> " + keyValueParis);
                    ++counter;
                }
            }
        } else {
            logger.info("Testcases Failed");

        }

        if (counter == imageParams.size()) {
            logger.info("Testcases Passed");
        } else {
            logger.info("Testcases Failed");
        }
    }
}
