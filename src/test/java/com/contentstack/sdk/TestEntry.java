package com.contentstack.sdk;

import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestEntry {

    private final Logger logger = Logger.getLogger(TestEntry.class.getName());
    private final String CONTENT_TYPE = "product";
    private String entryUid = "justFakeIt";
    private Stack stack;
    private Entry entry;

    @BeforeAll
    public void intOnceBeforeAll() throws Exception {
        Dotenv dotenv = Dotenv.load();
        String DEFAULT_API_KEY = dotenv.get("API_KEY");
        String DEFAULT_DELIVERY_TOKEN = dotenv.get("DELIVERY_TOKEN");
        String DEFAULT_ENV = dotenv.get("ENVIRONMENT");
        String DEFAULT_HOST = dotenv.get("HOST");
        Config config = new Config();
        config.setHost(DEFAULT_HOST);
        assert DEFAULT_API_KEY != null;
        stack = Contentstack.stack(DEFAULT_API_KEY, DEFAULT_DELIVERY_TOKEN, DEFAULT_ENV, config);
    }

    @Test
    @Order(1)
    void entryCallingPrivateModifier() {
        try {
            new Entry();
        } catch (IllegalAccessException e) {
            Assertions.assertEquals("Can Not Access Private Modifier", e.getLocalizedMessage());
            logger.info("passed.");
        }
    }

    @Test
    @Order(2)
    void runQueryToGetEntryUid() {
        final Query query = stack.contentType(CONTENT_TYPE).query();
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    JSONObject array = (JSONObject) queryresult.receiveJson.optJSONArray("entries").get(0);
                    entryUid = array.optString("uid");
                    assertTrue(entryUid.startsWith("blt"));
                    logger.info("passed..");
                } else {
                    Assertions.fail("Could not fetch the query data");
                    logger.info("passed..");
                }
            }
        });
    }

    @Test
    @Order(3)
    void entryAPIFetch() {
        entry = stack.contentType(CONTENT_TYPE).entry(entryUid);
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                Assertions.assertEquals(entryUid, entry.getUid());
            }
        });
        logger.info("passed..");
    }

    @Test
    @Order(4)
    void entryCalling() {
        Assertions.assertEquals(4, entry.headers.size());
        logger.info("passed...");
    }

    @Test
    @Order(5)
    void entrySetHeader() {
        entry.setHeader("headerKey", "headerValue");
        Assertions.assertTrue(entry.headers.containsKey("headerKey"));
        logger.info("passed...");
    }

    @Test
    @Order(6)
    void entryRemoveHeader() {
        entry.removeHeader("headerKey");
        Assertions.assertFalse(entry.headers.containsKey("headerKey"));
        logger.info("passed...");
    }

    @Test
    @Order(7)
    void entryGetTitle() {
        Assertions.assertEquals("Blue Yellow", entry.getTitle());
        logger.info("passed...");
    }

    @Test
    @Order(8)
    void entryGetURL() {
        Assertions.assertNull(entry.getURL());
        logger.info("passed...");
    }

    @Test
    @Order(9)
    void entryGetTags() {
        Assertions.assertNull(entry.getTags());
        logger.info("passed...");
    }

    @Test
    @Order(10)
    void entryGetContentType() {
        Assertions.assertEquals("product", entry.getContentType());
        logger.info("passed...");
    }

    @Test
    @Order(11)
    void entryGetUID() {
        Assertions.assertEquals(entryUid, entry.getUid());
        logger.info("passed...");
    }

    @Test
    @Order(12)
    void entryGetLocale() {
        Assertions.assertEquals("en-us", entry.getLocale());
        logger.info("passed...");
    }

    @Test
    @Order(13)
    void entrySetLocale() {
        entry.setLocale("hi");
        Assertions.assertEquals("hi", entry.params.optString("locale"));
        logger.info("passed...");
    }

    @Test
    @Order(14)
    @Deprecated
    void entryGetOwner() {
        Assertions.assertNull(entry.getOwner());
        logger.info("passed...");
    }

    @Test
    @Order(15)
    void entryToJSON() {
        boolean isJson = entry.toJSON() instanceof JSONObject;
        Assertions.assertTrue(entry.toJSON() != null);
        Assertions.assertTrue(isJson);
        logger.info("passed...");
    }

    @Test
    @Order(16)
    void entryGetObject() {
        Object what = entry.get("short_description");
        Object invalidKey = entry.get("invalidKey");
        Assertions.assertTrue(what != null);
        Assertions.assertNull(invalidKey);
        logger.info("passed...");
    }

    @Test
    @Order(17)
    void entryGetString() {
        Object what = entry.getString("short_description");
        Object version = entry.getString("_version");
        Assertions.assertTrue(what != null);
        Assertions.assertNull(version);
        logger.info("passed...");
    }

    @Test
    @Order(18)
    void entryGetBoolean() {
        Boolean shortDescription = entry.getBoolean("short_description");
        Object inStock = entry.getBoolean("in_stock");
        Assertions.assertFalse(shortDescription);
        Assertions.assertTrue(inStock instanceof Boolean);
        logger.info("passed...");
    }

    @Test
    @Order(19)
    void entryGetJSONArray() {
        Object image = entry.getJSONArray("image");
        Assertions.assertTrue(image instanceof JSONArray);
        logger.info("passed...");
    }

    @Test
    @Order(20)
    void entryGetJSONArrayShouldResultNull() {
        Object shouldBeNull = entry.getJSONArray("uid");
        Assertions.assertNull(shouldBeNull);
        logger.info("passed...");
    }

    @Test
    @Order(21)
    void entryGetJSONObject() {
        Object shouldBeNull = entry.getJSONObject("uid");
        Assertions.assertNull(shouldBeNull);
        logger.info("passed...");
    }

    @Test
    @Order(22)
    void entryGetNumber() {
        Object price = entry.getNumber("price");
        Assertions.assertTrue(price instanceof Number);
        logger.info("passed...");
    }

    @Test
    @Order(23)
    void entryGetNumberNullExpected() {
        Object price = entry.getNumber("short_description");
        Assertions.assertNull(price);
        logger.info("passed...");
    }

    @Test
    @Order(24)
    void entryGetInt() {
        Object price = entry.getInt("price");
        Assertions.assertTrue(price instanceof Integer);
        logger.info("passed...");
    }

    @Test
    @Order(25)
    void entryGetIntNullExpected() {
        Object updatedBy = entry.getInt("updated_by");
        Assertions.assertEquals(0, updatedBy);
        logger.info("passed...");
    }

    @Test
    @Order(26)
    void entryGetFloat() {
        Object price = entry.getFloat("price");
        Assertions.assertTrue(price instanceof Float);
        logger.info("passed...");
    }

    @Test
    @Order(27)
    void entryGetFloatZeroExpected() {
        Object updatedBy = entry.getFloat("updated_by");
        Assertions.assertNotNull(updatedBy);
        logger.info("passed...");
    }

    @Test
    @Order(28)
    void entryGetDouble() {
        Object price = entry.getDouble("price");
        Assertions.assertTrue(price instanceof Double);
        logger.info("passed...");
    }

    @Test
    @Order(29)
    void entryGetDoubleZeroExpected() {
        Object updatedBy = entry.getDouble("updated_by");
        Assertions.assertNotNull(updatedBy);
        logger.info("passed...");
    }

    @Test
    @Order(30)
    void entryGetLong() {
        Object price = entry.getLong("price");
        Assertions.assertTrue(price instanceof Long);
        logger.info("passed...");
    }

    @Test
    @Order(31)
    void entryGetLongZeroExpected() {
        Object updatedBy = entry.getLong("updated_by");
        Assertions.assertNotNull(updatedBy);
        logger.info("passed...");
    }

    @Test
    @Order(32)
    void entryGetShort() {
        Object updatedBy = entry.getShort("updated_by");
        Assertions.assertNotNull(updatedBy);
        logger.info("passed...");
    }

    @Test
    @Order(33)
    void entryGetShortZeroExpected() {
        Object updatedBy = entry.getShort("updated_by");
        Assertions.assertNotNull(updatedBy);
        logger.info("passed...");
    }

    @Test
    @Order(34)
    void entryGetDate() {
        Object updatedBy = entry.getDate("updated_at");
        Assertions.assertTrue(updatedBy instanceof GregorianCalendar);
        logger.info("passed...");
    }

    @Test
    @Order(35)
    void entryGetCreateAt() {
        Object updatedBy = entry.getCreateAt();
        Assertions.assertTrue(updatedBy instanceof GregorianCalendar);
        logger.info("passed...");
    }

    @Test
    @Order(36)
    void entryGetCreatedBy() {
        String createdBy = entry.getCreatedBy();
        Assertions.assertTrue(createdBy.startsWith("blt"));
        logger.info("passed...");
    }

    @Test
    @Order(37)
    void entryGetUpdateAt() {
        Object updateAt = entry.getUpdateAt();
        Assertions.assertTrue(updateAt instanceof GregorianCalendar);
        logger.info("passed...");
    }

    @Test
    @Order(38)
    void entryGetUpdateBy() {
        String updateAt = entry.getUpdatedBy();
        Assertions.assertTrue(updateAt.startsWith("blt"));
        logger.info("passed...");
    }

    @Test
    @Order(39)
    void entryGetDeleteAt() {
        Object deleteAt = entry.getDeleteAt();
        Assertions.assertNull(deleteAt);
        logger.info("passed...");
    }

    @Test
    @Order(40)
    void entryGetDeletedBy() {
        Object deletedBy = entry.getDeletedBy();
        Assertions.assertNull(deletedBy);
        logger.info("passed...");
    }

    @Test
    @Order(41)
    void entryGetAsset() {
        Object asset = entry.getAsset("image");
        Assertions.assertNotNull(asset);
        logger.info("passed...");
    }

    /// Add few more tests

    @Test
    @Order(42)
    void entryExcept() {
        String[] arrField = { "fieldOne", "fieldTwo", "fieldThree" };
        Entry initEntry = stack.contentType("product").entry(entryUid);
        initEntry.except(arrField);
        Assertions.assertEquals(3, initEntry.exceptFieldArray.length());
        logger.info("passed...");
    }

    @Test
    @Order(43)
    void entryIncludeReference() {
        Entry initEntry = stack.contentType("product").entry(entryUid);
        initEntry.includeReference("fieldOne");
        Assertions.assertEquals(1, initEntry.referenceArray.length());
        Assertions.assertTrue(initEntry.params.has("include[]"));
        logger.info("passed...");
    }

    @Test
    @Order(44)
    void entryIncludeReferenceList() {
        String[] arrField = { "fieldOne", "fieldTwo", "fieldThree" };
        Entry initEntry = stack.contentType("product").entry(entryUid);
        initEntry.includeReference(arrField);
        Assertions.assertEquals(3, initEntry.referenceArray.length());
        Assertions.assertTrue(initEntry.params.has("include[]"));
        logger.info("passed...");
    }

    @Test
    @Order(45)
    void entryOnlyList() {
        String[] arrField = { "fieldOne", "fieldTwo", "fieldThree" };
        Entry initEntry = stack.contentType("product").entry(entryUid);
        initEntry.only(arrField);
        Assertions.assertEquals(3, initEntry.objectUidForOnly.length());
        logger.info("passed...");
    }

    @Test
    @Order(46)
    void entryOnlyWithReferenceUid() {
        ArrayList<String> strList = new ArrayList<>();
        strList.add("fieldOne");
        strList.add("fieldTwo");
        strList.add("fieldThree");
        Entry initEntry = stack.contentType("product").entry(entryUid);
        initEntry.onlyWithReferenceUid(strList, "reference@fakeit");
        Assertions.assertTrue(initEntry.onlyJsonObject.has("reference@fakeit"));
        int size = initEntry.onlyJsonObject.optJSONArray("reference@fakeit").length();
        Assertions.assertEquals(strList.size(), size);
        logger.info("passed...");
    }

    @Test
    @Order(47)
    void entryExceptWithReferenceUid() {
        ArrayList<String> strList = new ArrayList<>();
        strList.add("fieldOne");
        strList.add("fieldTwo");
        strList.add("fieldThree");
        Entry initEntry = stack.contentType("product").entry(entryUid);
        initEntry.exceptWithReferenceUid(strList, "reference@fakeit");
        Assertions.assertTrue(initEntry.exceptJsonObject.has("reference@fakeit"));
        int size = initEntry.exceptJsonObject.optJSONArray("reference@fakeit").length();
        Assertions.assertEquals(strList.size(), size);
        logger.info("passed...");
    }

    @Test
    @Order(48)
    void entryAddParamMultiCheck() {
        Entry initEntry = stack.contentType("product").entry(entryUid);
        initEntry.addParam("fake@key", "fake@value");
        initEntry.addParam("fake@keyinit", "fake@valueinit");
        Assertions.assertTrue(initEntry.params.has("fake@key"));
        Assertions.assertTrue(initEntry.params.has("fake@keyinit"));
        Assertions.assertEquals(2, initEntry.params.length());
        logger.info("passed...");
    }

    @Test
    @Order(49)
    void entryIncludeReferenceContentTypeUID() {
        Entry initEntry = stack.contentType("product").entry(entryUid);
        initEntry.includeReferenceContentTypeUID();
        Assertions.assertTrue(initEntry.params.has("include_reference_content_type_uid"));
        logger.info("passed...");
    }

    @Test
    @Order(50)
    void entryIncludeContentType() {
        Entry initEntry = stack.contentType("product").entry(entryUid);
        initEntry.addParam("include_schema", "true");
        initEntry.includeContentType();
        Assertions.assertTrue(initEntry.params.has("include_content_type"));
        Assertions.assertTrue(initEntry.params.has("include_global_field_schema"));
        logger.info("passed...");
    }

    @Test
    @Order(51)
    void entryIncludeContentTypeWithoutInclude_schema() {
        Entry initEntry = stack.contentType("product").entry(entryUid);
        initEntry.includeContentType();
        Assertions.assertTrue(initEntry.params.has("include_content_type"));
        Assertions.assertTrue(initEntry.params.has("include_global_field_schema"));
        logger.info("passed...");
    }

    @Test
    @Order(52)
    void entryIncludeFallback() {
        Entry initEntry = stack.contentType("product").entry(entryUid);
        initEntry.includeFallback();
        Assertions.assertTrue(initEntry.params.has("include_fallback"));
        logger.info("passed...");
    }

    @Test
    @Order(53)
    void entryIncludeEmbeddedItems() {
        Entry initEntry = stack.contentType("product").entry(entryUid);
        initEntry.includeEmbeddedItems();
        Assertions.assertTrue(initEntry.params.has("include_embedded_items[]"));
        logger.info("passed...");
    }

}
