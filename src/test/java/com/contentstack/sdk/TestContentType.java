package com.contentstack.sdk;

import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestContentType {

    protected String API_KEY, DELIVERY_TOKEN, ENV;
    private final Logger logger = Logger.getLogger(TestContentType.class.getName());
    private Stack stack;

    @BeforeAll
    public void initBeforeTests() throws IllegalAccessException {
        Dotenv dotenv = Dotenv.load();
        API_KEY = dotenv.get("API_KEY");
        DELIVERY_TOKEN = dotenv.get("DELIVERY_TOKEN");
        ENV = dotenv.get("ENVIRONMENT");
        stack = Contentstack.stack(API_KEY, DELIVERY_TOKEN, ENV);
    }


    @Test
    @Order(1)
    void testPrivateAccess() {
        try {
            new ContentType();
        } catch (IllegalAccessException e) {
            Assertions.assertEquals("Can Not Access Private Modifier", e.getLocalizedMessage());
            logger.info("passed...");
        }
    }

    @Test
    @Order(2)
    void testContentType() {
        ContentType contentType = stack.contentType("product");
        Assertions.assertEquals("product", contentType.contentTypeUid);
        logger.info("passed...");
    }

    @Test
    @Order(3)
    void testContentTypeSetHeader() {
        ContentType contentType = stack.contentType("product");
        contentType.setHeader("headerKey", "headerValue");
        Assertions.assertTrue(contentType.headers.containsKey("headerKey"));
        logger.info("passed...");
    }


    @Test
    void testContentRemoveHeader() {
        ContentType contentType = stack.contentType("product");
        contentType.setHeader("headerKey", "headerValue");
        contentType.removeHeader("headerKey");
        Assertions.assertFalse(contentType.headers.containsKey("headerKey"));
        logger.info("passed...");
    }

    @Test
    void testEntryInstance() {
        ContentType contentType = stack.contentType("product");
        Entry entry = contentType.entry("just-fake-it");
        Assertions.assertEquals("product", entry.getContentType());
        Assertions.assertEquals("just-fake-it", entry.uid);
        Assertions.assertEquals(4, entry.headers.size());
        logger.info("passed...");
    }

    @Test
    void testQueryInstance() {
        ContentType contentType = stack.contentType("product");
        Query query = contentType.query();
        Assertions.assertEquals("product", query.getContentType());
        Assertions.assertEquals(4, query.headers.size());
        logger.info("passed...");
    }


    @Test
    void testContentTypeFetch() {
        ContentType contentType = stack.contentType("product");
        JSONObject paramObj = new JSONObject();
        paramObj.put("ctKeyOne", "ctKeyValue1");
        paramObj.put("ctKeyTwo", "ctKeyValue2");
        contentType.fetch(paramObj, new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel model, Error error) {
                JSONObject resp = (JSONObject) model.getResponse();
                Assertions.assertTrue(resp.has("schema"));
            }
        });
    }

    @Test
    void testContentTypesFetch() {
        ContentType contentType = stack.contentType("product");
        JSONObject paramObj = new JSONObject();
        paramObj.put("ctKeyOne", "ctKeyValue1");
        paramObj.put("ctKeyTwo", "ctKeyValue2");
        contentType.fetch(paramObj, new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel model, Error error) {
                JSONArray resp = model.getResultArray();
                Assertions.assertTrue(resp.isEmpty());
            }
        });
    }

    @Test
    void toCoverageCheckHeader() {
        Map<String, Object> mapHeader = new HashMap<>();
        CSBackgroundTask backgroundTask = new CSBackgroundTask();
        backgroundTask.checkHeader(mapHeader);
    }


}
