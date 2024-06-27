package com.contentstack.sdk;

import org.json.JSONObject;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestEntry {

    private final Logger logger = Logger.getLogger(TestEntry.class.getName());
    private String entryUid = Credentials.ENTRY_UID;
    private final Stack stack = Credentials.getStack();
    private Entry entry;
    private final String CONTENT_TYPE = Credentials.CONTENT_TYPE;
    private final String VARIANT_UID = Credentials.VARIANT_UID;
    private static final String[] VARIANT_UIDS = Credentials.VARIANTS_UID ;
    
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
    void VariantsTestSingleUid(){
        entry = stack.contentType(CONTENT_TYPE).entry(entryUid).variants(VARIANT_UID);
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                assertEquals(VARIANT_UID.trim(), entry.getHeaders().get("x-cs-variant-uid"));
               System.out.println(entry.toJSON());
            }
        });
    }
    @Test
    void VariantsTestArray(){
        entry = stack.contentType(CONTENT_TYPE).entry(entryUid).variants(VARIANT_UIDS);
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
               System.out.println(entry.toJSON());
            }
        });
    }
    
    @Test
    void VariantsTestNullString() {
    entry = stack.contentType(CONTENT_TYPE).entry(entryUid).variants((String) null);
    entry.fetch(new EntryResultCallBack() {
        @Override
        public void onCompletion(ResponseType responseType, Error error) {
            assertNull(entry.getHeaders().get("x-cs-variant-uid"));
            System.out.println(entry.toJSON());
        }
    });
    }

    @Test
    @Order(4)
    void entryCalling() {
        Assertions.assertEquals(7, entry.headers.size());
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
    @Order(15)
    void entryToJSON() {
        boolean isJson = entry.toJSON() != null;
        Assertions.assertNotNull(entry.toJSON());
        Assertions.assertTrue(isJson);
        logger.info("passed...");
    }

    @Test
    @Order(16)
    void entryGetObject() {
        Object what = entry.get("short_description");
        Object invalidKey = entry.get("invalidKey");
        Assertions.assertNotNull(what);
        Assertions.assertNull(invalidKey);
        logger.info("passed...");
    }

    @Test
    @Order(17)
    void entryGetString() {
        Object what = entry.getString("short_description");
        Object version = entry.getString("_version");
        Assertions.assertNotNull(what);
        Assertions.assertNull(version);
        logger.info("passed...");
    }

    @Test
    @Order(18)
    void entryGetBoolean() {
        Boolean shortDescription = entry.getBoolean("short_description");
        Object inStock = entry.getBoolean("in_stock");
        Assertions.assertFalse(shortDescription);
        Assertions.assertNotNull(inStock);
        logger.info("passed...");
    }

    @Test
    @Order(19)
    void entryGetJSONArray() {
        Object image = entry.getJSONObject("image");
        Assertions.assertNotNull(image);
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
        Assertions.assertNotNull(price);
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
        Assertions.assertNotNull(price);
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
        Assertions.assertNotNull(price);
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
        Assertions.assertNotNull(price);
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
        Assertions.assertNotNull(price);
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
        Entry initEntry = stack.contentType("product").entry(entryUid).except(arrField);
        Assertions.assertEquals(3, initEntry.exceptFieldArray.length());
        logger.info("passed...");
    }

    @Test
    @Order(43)
    void entryIncludeReference() {
        Entry initEntry = stack.contentType("product").entry(entryUid).includeReference("fieldOne");
        Assertions.assertEquals(1, initEntry.referenceArray.length());
        Assertions.assertTrue(initEntry.params.has("include[]"));
        logger.info("passed...");
    }

    @Test
    @Order(44)
    void entryIncludeReferenceList() {
        String[] arrField = { "fieldOne", "fieldTwo", "fieldThree" };
        Entry initEntry = stack.contentType("product").entry(entryUid).includeReference(arrField);
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
        Entry initEntry = stack.contentType("product").entry(entryUid).onlyWithReferenceUid(strList,
                "reference@fakeit");
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
        Entry initEntry = stack.contentType("product")
                .entry(entryUid)
                .exceptWithReferenceUid(strList, "reference@fakeit");
        Assertions.assertTrue(initEntry.exceptJsonObject.has("reference@fakeit"));
        int size = initEntry.exceptJsonObject.optJSONArray("reference@fakeit").length();
        Assertions.assertEquals(strList.size(), size);
        logger.info("passed...");
    }

    @Test
    @Order(48)
    void entryAddParamMultiCheck() {
        Entry initEntry = stack.contentType("product")
                .entry(entryUid)
                .addParam("fake@key", "fake@value")
                .addParam("fake@keyinit", "fake@valueinit");
        Assertions.assertTrue(initEntry.params.has("fake@key"));
        Assertions.assertTrue(initEntry.params.has("fake@keyinit"));
        Assertions.assertEquals(2, initEntry.params.length());
        logger.info("passed...");
    }

    @Test
    @Order(49)
    void entryIncludeReferenceContentTypeUID() {
        Entry initEntry = stack.contentType("product").entry(entryUid).includeReferenceContentTypeUID();
        Assertions.assertTrue(initEntry.params.has("include_reference_content_type_uid"));
        logger.info("passed...");
    }

    @Test
    @Order(50)
    void entryIncludeContentType() {
        Entry initEntry = stack.contentType("product").entry(entryUid);
        initEntry.addParam("include_schema", "true").includeContentType();
        Assertions.assertTrue(initEntry.params.has("include_content_type"));
        Assertions.assertTrue(initEntry.params.has("include_global_field_schema"));
        logger.info("passed...");
    }

    @Test
    @Order(51)
    void entryIncludeContentTypeWithoutInclude_schema() {
        Entry initEntry = stack.contentType("product").entry(entryUid).includeContentType();
        Assertions.assertTrue(initEntry.params.has("include_content_type"));
        Assertions.assertTrue(initEntry.params.has("include_global_field_schema"));
        logger.info("passed...");
    }

    @Test
    @Order(52)
    void entryIncludeFallback() {
        Entry initEntry = stack.contentType("product").entry(entryUid).includeFallback();
        Assertions.assertTrue(initEntry.params.has("include_fallback"));
        logger.info("passed...");
    }

    @Test
    @Order(53)
    void entryIncludeEmbeddedItems() {
        Entry initEntry = stack.contentType("product").entry(entryUid).includeEmbeddedItems();
        Assertions.assertTrue(initEntry.params.has("include_embedded_items[]"));
        logger.info("passed...");
    }

    @Test
    @Order(54)
    void testEntryIncludeBranch() {
        Entry initEntry = stack.contentType("product").entry(entryUid);
        initEntry.includeBranch();
        Assertions.assertTrue(initEntry.params.has("include_branch"));
        Assertions.assertEquals(true, initEntry.params.opt("include_branch"));
        logger.info("passed...");
    }

    @Test
    @Order(54)
    void testEntryIncludeOwner() {
        Entry initEntry = stack.contentType("product").entry(entryUid);
        initEntry.includeMetadata();
        Assertions.assertTrue(initEntry.params.has("include_metadata"));
        Assertions.assertEquals(true, initEntry.params.opt("include_metadata"));
        logger.info("passed...");
    }

    @Test
    @Order(55)
    void testEntryPassConfigBranchIncludeBranch() throws IllegalAccessException {
        Config config = new Config();
        config.setBranch("feature_branch");
        Stack branchStack = Contentstack.stack(Credentials.API_KEY, Credentials.DELIVERY_TOKEN, Credentials.ENVIRONMENT,
                config);
        Entry entry = branchStack.contentType("product").entry(entryUid);
        entry.includeBranch().fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                logger.info(entry.headers + "");
            }
        });
        Assertions.assertTrue(entry.params.has("include_branch"));
        Assertions.assertEquals(true, entry.params.opt("include_branch"));
        Assertions.assertTrue(entry.headers.containsKey("branch"));
        logger.info("passed...");
    }

}
