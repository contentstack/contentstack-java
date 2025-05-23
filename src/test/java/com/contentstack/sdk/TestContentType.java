package com.contentstack.sdk;

import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestContentType {

    private final Logger logger = Logger.getLogger(TestContentType.class.getName());
    private final Stack stack = Credentials.getStack();

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
        logger.info("passed...");
    }

    @Test
    void testQueryInstance() {
        ContentType contentType = stack.contentType("product");
        Query query = contentType.query();
        Assertions.assertEquals("product", query.getContentType());
        logger.info("passed...");
    }

    @Test
    void testContentTypeFetch() throws IllegalAccessException {
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
    void testContentTypesFetch() throws IllegalAccessException {
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


}
