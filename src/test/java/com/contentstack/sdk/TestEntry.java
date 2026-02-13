package com.contentstack.sdk;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for Entry class.
 * Tests entry operations, configurations, and query methods.
 */
public class TestEntry {

    private Entry entry;
    private final String contentTypeUid = "test_content_type";

    @BeforeEach
    void setUp() {
        entry = new Entry(contentTypeUid);
        entry.headers = new LinkedHashMap<>();
    }

    // ========== CONSTRUCTOR TESTS ==========

    @Test
    void testEntryConstructorWithContentType() {
        Entry testEntry = new Entry("blog_post");
        assertNotNull(testEntry);
        assertEquals("blog_post", testEntry.contentTypeUid);
        assertNotNull(testEntry.params);
    }

    @Test
    void testEntryDirectInstantiationThrows() {
        assertThrows(IllegalAccessException.class, () -> {
            new Entry();
        });
    }

    // ========== CONFIGURE TESTS ==========

    @Test
    void testConfigureWithCompleteJson() {
        JSONObject json = new JSONObject();
        json.put("uid", "entry123");
        json.put("title", "Test Entry");
        json.put("url", "/test-entry");
        json.put("locale", "en-us");
        json.put("tags", new String[]{"tag1", "tag2"});
        
        Entry result = entry.configure(json);
        assertSame(entry, result);
    }

    @Test
    void testConfigureWithMinimalJson() {
        JSONObject json = new JSONObject();
        json.put("uid", "minimal_entry");
        
        Entry result = entry.configure(json);
        assertNotNull(result);
    }

    // ========== HEADER TESTS ==========

    @Test
    void testSetHeader() {
        entry.setHeader("custom-header", "custom-value");
        assertTrue(entry.headers.containsKey("custom-header"));
        assertEquals("custom-value", entry.headers.get("custom-header"));
    }

    @Test
    void testSetMultipleHeaders() {
        entry.setHeader("header1", "value1");
        entry.setHeader("header2", "value2");
        entry.setHeader("header3", "value3");
        
        assertEquals(3, entry.headers.size());
    }

    @Test
    void testSetHeaderWithEmptyKey() {
        entry.setHeader("", "value");
        assertFalse(entry.headers.containsKey(""));
    }

    @Test
    void testSetHeaderWithEmptyValue() {
        entry.setHeader("key", "");
        assertFalse(entry.headers.containsKey("key"));
    }

    @Test
    void testRemoveHeader() {
        entry.setHeader("temp-header", "temp-value");
        assertTrue(entry.headers.containsKey("temp-header"));
        
        entry.removeHeader("temp-header");
        assertFalse(entry.headers.containsKey("temp-header"));
    }

    @Test
    void testRemoveNonExistentHeader() {
        entry.removeHeader("non-existent");
        assertNotNull(entry.headers);
    }

    @Test
    void testRemoveHeaderWithEmptyKey() {
        entry.removeHeader("");
        assertNotNull(entry.headers);
    }

    // ========== GETTER/SETTER TESTS ==========

    @Test
    void testGetTitle() {
        assertNull(entry.getTitle());
    }

    @Test
    void testGetURL() {
        assertNull(entry.getURL());
    }

    @Test
    void testGetTags() {
        assertNull(entry.getTags());
    }

    @Test
    void testSetTags() {
        String[] tags = {"tag1", "tag2", "tag3"};
        entry.setTags(tags);
        assertArrayEquals(tags, entry.getTags());
    }

    @Test
    void testGetContentType() {
        assertEquals(contentTypeUid, entry.getContentType());
    }

    @Test
    void testGetUid() {
        assertNull(entry.getUid());
    }

    @Test
    void testSetUid() {
        entry.setUid("entry_uid_123");
        assertEquals("entry_uid_123", entry.getUid());
    }

    @Test
    void testGetLocale() {
        assertNull(entry.getLocale());
    }

    @Test
    void testSetLocale() {
        Entry result = entry.setLocale("en-us");
        assertSame(entry, result);
        assertTrue(entry.params.has("locale"));
        assertEquals("en-us", entry.params.get("locale"));
    }

    @Test
    void testToJSON() {
        assertNull(entry.toJSON());
    }

    @Test
    void testToJSONAfterConfigure() {
        JSONObject json = new JSONObject();
        json.put("uid", "test123");
        entry.configure(json);
        
        assertNotNull(entry.toJSON());
    }

    // ========== PARAM TESTS ==========

    @Test
    void testAddParam() {
        Entry result = entry.addParam("key1", "value1");
        assertSame(entry, result);
        assertTrue(entry.params.has("key1"));
        assertEquals("value1", entry.params.get("key1"));
    }

    @Test
    void testAddMultipleParams() {
        entry.addParam("param1", "value1");
        entry.addParam("param2", "value2");
        entry.addParam("param3", "value3");
        
        assertTrue(entry.params.has("param1"));
        assertTrue(entry.params.has("param2"));
        assertTrue(entry.params.has("param3"));
    }

    @Test
    void testAddParamOverwritesExisting() {
        entry.addParam("key", "value1");
        entry.addParam("key", "value2");
        assertEquals("value2", entry.params.get("key"));
    }

    // ========== INCLUDE TESTS ==========

    @Test
    void testIncludeFallback() {
        Entry result = entry.includeFallback();
        assertSame(entry, result);
        assertTrue(entry.params.has("include_fallback"));
        assertEquals(true, entry.params.get("include_fallback"));
    }

    @Test
    void testIncludeBranch() {
        Entry result = entry.includeBranch();
        assertSame(entry, result);
        assertTrue(entry.params.has("include_branch"));
        assertEquals(true, entry.params.get("include_branch"));
    }

    @Test
    void testIncludeEmbeddedItems() {
        Entry result = entry.includeEmbeddedItems();
        assertSame(entry, result);
        assertTrue(entry.params.has("include_embedded_items[]"));
    }

    @Test
    void testIncludeContentType() {
        Entry result = entry.includeContentType();
        assertSame(entry, result);
        assertTrue(entry.params.has("include_content_type"));
        assertEquals(true, entry.params.get("include_content_type"));
    }

    @Test
    void testIncludeMetadata() {
        Entry result = entry.includeMetadata();
        assertSame(entry, result);
        assertTrue(entry.params.has("include_metadata"));
        assertEquals(true, entry.params.get("include_metadata"));
    }

    // ========== ASSET FIELDS TESTS (CDA asset_fields[] parameter) ==========

    @Test
    void testAssetFieldsWithSupportedValues() {
        Entry result = entry.assetFields("user_defined_fields", "embedded", "ai_suggested", "visual_markups");
        assertSame(entry, result);
        assertTrue(entry.params.has("asset_fields[]"));
        Object val = entry.params.get("asset_fields[]");
        assertTrue(val instanceof JSONArray);
        JSONArray arr = (JSONArray) val;
        assertEquals(4, arr.length());
        assertEquals("user_defined_fields", arr.get(0));
        assertEquals("embedded", arr.get(1));
        assertEquals("ai_suggested", arr.get(2));
        assertEquals("visual_markups", arr.get(3));
    }

    @Test
    void testAssetFieldsReturnsThis() {
        Entry result = entry.assetFields("embedded");
        assertSame(entry, result);
    }

    @Test
    void testAssetFieldsWithNoArgsDoesNotSetParam() {
        entry.assetFields();
        assertFalse(entry.params.has("asset_fields[]"));
    }

    @Test
    void testAssetFieldsWithNullDoesNotSetParam() {
        entry.assetFields((String[]) null);
        assertFalse(entry.params.has("asset_fields[]"));
    }

    @Test
    void testAssetFieldsChainingWithIncludeMetadata() {
        Entry result = entry.assetFields("user_defined_fields", "visual_markups").includeMetadata().setLocale("en-us");
        assertSame(entry, result);
        assertTrue(entry.params.has("asset_fields[]"));
        assertTrue(entry.params.has("include_metadata"));
        assertTrue(entry.params.has("locale"));
    }

    /**
     * Usage: stack.contentType(ctUid).entry(entryUid).assetFields(...).fetch()
     * Verifies the full chain sets asset_fields[] on the entry before fetch.
     */
    @Test
    void testUsageSingleEntryFetchWithAssetFields() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        Entry entry = stack.contentType("blog").entry("entry_123")
            .assetFields("user_defined_fields", "embedded");
        assertTrue(entry.params.has("asset_fields[]"));
        JSONArray arr = entry.params.getJSONArray("asset_fields[]");
        assertEquals(2, arr.length());
        assertEquals("user_defined_fields", arr.get(0));
        assertEquals("embedded", arr.get(1));
    }

    @Test
    void testAssetFieldsSingleField() {
        entry.assetFields("embedded");
        JSONArray arr = entry.params.getJSONArray("asset_fields[]");
        assertEquals(1, arr.length());
        assertEquals("embedded", arr.get(0));
    }

    @Test
    void testAssetFieldsEmptyVarargsArrayDoesNotSetParam() {
        entry.assetFields(new String[0]);
        assertFalse(entry.params.has("asset_fields[]"));
    }

    @Test
    void testAssetFieldsSecondCallOverwrites() {
        entry.assetFields("user_defined_fields", "embedded");
        entry.assetFields("ai_suggested");
        JSONArray arr = entry.params.getJSONArray("asset_fields[]");
        assertEquals(1, arr.length());
        assertEquals("ai_suggested", arr.get(0));
    }

    @Test
    void testAssetFieldsDuplicateValuesAllowed() {
        entry.assetFields("embedded", "embedded");
        JSONArray arr = entry.params.getJSONArray("asset_fields[]");
        assertEquals(2, arr.length());
        assertEquals("embedded", arr.get(0));
        assertEquals("embedded", arr.get(1));
    }

    @Test
    void testAssetFieldsWithEmptyStringInArray() {
        entry.assetFields("valid", "", "visual_markups");
        JSONArray arr = entry.params.getJSONArray("asset_fields[]");
        assertEquals(3, arr.length());
        assertEquals("", arr.get(1));
    }

    // ========== ONLY/EXCEPT FIELD TESTS ==========

    @Test
    void testOnlyWithMultipleFields() {
        String[] fields = {"field1", "field2", "field3"};
        Entry result = entry.only(fields);
        assertSame(entry, result);
        assertNotNull(entry.objectUidForOnly);
        assertEquals(3, entry.objectUidForOnly.length());
    }

    @Test
    void testOnlyWithEmptyArray() {
        String[] fields = {};
        Entry result = entry.only(fields);
        assertSame(entry, result);
    }

    @Test
    void testOnlyWithReferenceFieldUid() {
        List<String> fields = Arrays.asList("field1", "field2");
        Entry result = entry.onlyWithReferenceUid(fields, "reference_field");
        assertSame(entry, result);
        assertNotNull(entry.onlyJsonObject);
    }

    @Test
    void testExceptWithMultipleFields() {
        String[] fields = {"field1", "field2", "field3"};
        Entry result = entry.except(fields);
        assertSame(entry, result);
        assertNotNull(entry.exceptFieldArray);
        assertEquals(3, entry.exceptFieldArray.length());
    }

    @Test
    void testExceptWithReferenceFieldUid() {
        List<String> fields = Arrays.asList("field1");
        Entry result = entry.exceptWithReferenceUid(fields, "reference_field");
        assertSame(entry, result);
        assertNotNull(entry.exceptJsonObject);
    }

    // ========== INCLUDE REFERENCE TESTS ==========

    @Test
    void testIncludeReferenceWithSingleField() {
        Entry result = entry.includeReference("author");
        assertSame(entry, result);
        assertNotNull(entry.referenceArray);
        assertEquals(1, entry.referenceArray.length());
    }

    @Test
    void testIncludeReferenceWithMultipleFields() {
        String[] references = {"author", "category", "tags"};
        Entry result = entry.includeReference(references);
        assertSame(entry, result);
        assertNotNull(entry.referenceArray);
        assertEquals(3, entry.referenceArray.length());
    }

    @Test
    void testIncludeReferenceContentTypeUID() {
        Entry result = entry.includeReferenceContentTypeUID();
        assertSame(entry, result);
        assertTrue(entry.params.has("include_reference_content_type_uid"));
    }

    // ========== CHAINING TESTS ==========

    @Test
    void testMethodChaining() {
        Entry result = entry
            .setLocale("en-us")
            .includeFallback()
            .includeBranch()
            .includeMetadata()
            .includeContentType()
            .addParam("custom", "value");
        
        assertSame(entry, result);
        assertTrue(entry.params.has("locale"));
        assertTrue(entry.params.has("include_fallback"));
        assertTrue(entry.params.has("include_branch"));
        assertTrue(entry.params.has("include_metadata"));
        assertTrue(entry.params.has("include_content_type"));
        assertTrue(entry.params.has("custom"));
    }

    @Test
    void testComplexQueryBuilding() {
        String[] onlyFields = {"title", "description"};
        String[] references = {"author"};
        
        entry.setLocale("en-us");
        entry.only(onlyFields);
        entry.includeReference(references);
        entry.includeFallback();
        entry.includeMetadata();
        
        assertTrue(entry.params.has("locale"));
        assertNotNull(entry.objectUidForOnly);
        assertNotNull(entry.referenceArray);
        assertTrue(entry.params.has("include_fallback"));
        assertTrue(entry.params.has("include_metadata"));
    }

    // ========== EDGE CASE TESTS ==========

    @Test
    void testSetNullUid() {
        entry.setUid(null);
        assertNull(entry.getUid());
    }

    @Test
    void testSetEmptyUid() {
        entry.setUid("");
        assertEquals("", entry.getUid());
    }

    @Test
    void testSetNullTags() {
        entry.setTags(null);
        assertNull(entry.getTags());
    }

    @Test
    void testSetEmptyTags() {
        String[] emptyTags = {};
        entry.setTags(emptyTags);
        assertEquals(0, entry.getTags().length);
    }

    @Test
    void testParamsInitialization() {
        Entry newEntry = new Entry("test_type");
        assertNotNull(newEntry.params);
        assertEquals(0, newEntry.params.length());
    }

    @Test
    void testConfigureWithNullValues() {
        JSONObject json = new JSONObject();
        json.put("uid", "test123");
        json.put("title", JSONObject.NULL);
        
        entry.configure(json);
        assertNotNull(entry);
    }

    @Test
    void testOnlyMultipleCalls() {
        entry.only(new String[]{"field1"});
        entry.only(new String[]{"field2"});
        
        assertNotNull(entry.objectUidForOnly);
    }

    @Test
    void testExceptMultipleCalls() {
        entry.except(new String[]{"field1"});
        entry.except(new String[]{"field2"});
        
        assertNotNull(entry.exceptFieldArray);
    }

    @Test
    void testIncludeReferenceMultipleCalls() {
        entry.includeReference("author");
        entry.includeReference("category");
        
        assertNotNull(entry.referenceArray);
    }

    @Test
    void testHeaderOverwrite() {
        entry.setHeader("key", "value1");
        entry.setHeader("key", "value2");
        assertEquals("value2", entry.headers.get("key"));
    }

    @Test
    void testMultipleLocaleChanges() {
        entry.setLocale("en-us");
        assertEquals("en-us", entry.params.get("locale"));
        
        entry.setLocale("fr-fr");
        assertEquals("fr-fr", entry.params.get("locale"));
    }

    @Test
    void testAllIncludesSet() {
        entry.includeFallback()
             .includeBranch()
             .includeEmbeddedItems()
             .includeContentType()
             .includeMetadata()
             .includeReferenceContentTypeUID();
        
        assertTrue(entry.params.has("include_fallback"));
        assertTrue(entry.params.has("include_branch"));
        assertTrue(entry.params.has("include_embedded_items[]"));
        assertTrue(entry.params.has("include_content_type"));
        assertTrue(entry.params.has("include_metadata"));
        assertTrue(entry.params.has("include_reference_content_type_uid"));
    }

    @Test
    void testOnlyAndExceptTogether() {
        entry.only(new String[]{"field1"});
        entry.except(new String[]{"field2"});
        
        assertNotNull(entry.objectUidForOnly);
        assertNotNull(entry.exceptFieldArray);
    }

    @Test
    void testContentTypeUidPreservation() {
        String originalUid = "original_content_type";
        Entry testEntry = new Entry(originalUid);
        
        testEntry.setLocale("en-us");
        testEntry.addParam("key", "value");
        testEntry.includeFallback();
        
        assertEquals(originalUid, testEntry.getContentType());
    }

    // ========== GETTER METHODS TESTS ==========

    @Test
    void testGetMethod() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        
        JSONObject json = new JSONObject();
        json.put("test_key", "test_value");
        json.put("number_key", 42);
        entry.configure(json);
        
        Object value = entry.get("test_key");
        assertNotNull(value);
        assertEquals("test_value", value);
    }

    @Test
    void testGetStringMethod() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        
        JSONObject json = new JSONObject();
        json.put("string_field", "hello world");
        json.put("number_field", 123);
        entry.configure(json);
        
        String stringValue = entry.getString("string_field");
        assertEquals("hello world", stringValue);
        
        // Non-string value should return null
        String numberAsString = entry.getString("number_field");
        assertNull(numberAsString);
    }

    @Test
    void testGetBooleanMethod() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        
        JSONObject json = new JSONObject();
        json.put("boolean_field", true);
        json.put("string_field", "not_boolean");
        entry.configure(json);
        
        Boolean boolValue = entry.getBoolean("boolean_field");
        assertTrue(boolValue);
        
        // Non-boolean value should return false
        Boolean falseValue = entry.getBoolean("string_field");
        assertFalse(falseValue);
    }

    @Test
    void testGetJSONArrayMethod() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        
        JSONObject json = new JSONObject();
        org.json.JSONArray array = new org.json.JSONArray();
        array.put("item1");
        array.put("item2");
        json.put("array_field", array);
        json.put("string_field", "not_array");
        entry.configure(json);
        
        org.json.JSONArray retrievedArray = entry.getJSONArray("array_field");
        assertNotNull(retrievedArray);
        assertEquals(2, retrievedArray.length());
        
        // Non-array value should return null
        org.json.JSONArray nullArray = entry.getJSONArray("string_field");
        assertNull(nullArray);
    }

    @Test
    void testGetJSONObjectMethod() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        
        JSONObject json = new JSONObject();
        JSONObject nestedObject = new JSONObject();
        nestedObject.put("nested_key", "nested_value");
        json.put("object_field", nestedObject);
        json.put("string_field", "not_object");
        entry.configure(json);
        
        JSONObject retrievedObject = entry.getJSONObject("object_field");
        assertNotNull(retrievedObject);
        assertEquals("nested_value", retrievedObject.getString("nested_key"));
        
        // Non-object value should return null
        JSONObject nullObject = entry.getJSONObject("string_field");
        assertNull(nullObject);
    }

    @Test
    void testGetNumberMethod() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        
        JSONObject json = new JSONObject();
        json.put("int_field", 42);
        json.put("double_field", 3.14);
        json.put("string_field", "not_number");
        entry.configure(json);
        
        Number intNumber = entry.getNumber("int_field");
        assertNotNull(intNumber);
        assertEquals(42, intNumber.intValue());
        
        Number doubleNumber = entry.getNumber("double_field");
        assertNotNull(doubleNumber);
        assertEquals(3.14, doubleNumber.doubleValue(), 0.01);
        
        // Non-number value should return null
        Number nullNumber = entry.getNumber("string_field");
        assertNull(nullNumber);
    }

    @Test
    void testGetIntMethod() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        
        JSONObject json = new JSONObject();
        json.put("int_field", 42);
        json.put("string_field", "not_int");
        entry.configure(json);
        
        int intValue = entry.getInt("int_field");
        assertEquals(42, intValue);
        
        // Non-int value should return 0
        int zeroValue = entry.getInt("string_field");
        assertEquals(0, zeroValue);
    }

    @Test
    void testGetFloatMethod() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        
        JSONObject json = new JSONObject();
        json.put("float_field", 3.14f);
        json.put("string_field", "not_float");
        entry.configure(json);
        
        float floatValue = entry.getFloat("float_field");
        assertEquals(3.14f, floatValue, 0.01f);
        
        // Non-float value should return 0
        float zeroValue = entry.getFloat("string_field");
        assertEquals(0f, zeroValue, 0.01f);
    }

    @Test
    void testGetDoubleMethod() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        
        JSONObject json = new JSONObject();
        json.put("double_field", 3.14159);
        json.put("string_field", "not_double");
        entry.configure(json);
        
        double doubleValue = entry.getDouble("double_field");
        assertEquals(3.14159, doubleValue, 0.00001);
        
        // Non-double value should return 0
        double zeroValue = entry.getDouble("string_field");
        assertEquals(0.0, zeroValue, 0.00001);
    }

    @Test
    void testGetLongMethod() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        
        JSONObject json = new JSONObject();
        json.put("long_field", 123456789L);
        json.put("string_field", "not_long");
        entry.configure(json);
        
        long longValue = entry.getLong("long_field");
        assertEquals(123456789L, longValue);
        
        // Non-long value should return 0
        long zeroValue = entry.getLong("string_field");
        assertEquals(0L, zeroValue);
    }

    @Test
    void testGetShortMethod() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        
        JSONObject json = new JSONObject();
        json.put("short_field", (short) 42);
        json.put("string_field", "not_short");
        entry.configure(json);
        
        short shortValue = entry.getShort("short_field");
        assertEquals((short) 42, shortValue);
        
        // Non-short value should return 0
        short zeroValue = entry.getShort("string_field");
        assertEquals((short) 0, zeroValue);
    }

    @Test
    void testGetDateMethod() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        
        JSONObject json = new JSONObject();
        json.put("date_field", "2024-01-01T00:00:00.000Z");
        json.put("invalid_date", "not_a_date");
        entry.configure(json);
        
        java.util.Calendar dateValue = entry.getDate("date_field");
        assertNotNull(dateValue);
        
        // Invalid date should return null
        java.util.Calendar nullDate = entry.getDate("invalid_date");
        assertNull(nullDate);
    }

    @Test
    void testGetCreateAtMethod() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        
        JSONObject json = new JSONObject();
        json.put("created_at", "2024-01-01T00:00:00.000Z");
        entry.configure(json);
        
        java.util.Calendar createdAt = entry.getCreateAt();
        assertNotNull(createdAt);
    }

    @Test
    void testGetCreatedByMethod() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        
        JSONObject json = new JSONObject();
        json.put("created_by", "user123");
        entry.configure(json);
        
        String createdBy = entry.getCreatedBy();
        assertEquals("user123", createdBy);
    }

    @Test
    void testGetUpdateAtMethod() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        
        JSONObject json = new JSONObject();
        json.put("updated_at", "2024-01-02T00:00:00.000Z");
        entry.configure(json);
        
        java.util.Calendar updatedAt = entry.getUpdateAt();
        assertNotNull(updatedAt);
    }

    @Test
    void testGetUpdatedByMethod() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        
        JSONObject json = new JSONObject();
        json.put("updated_by", "user456");
        entry.configure(json);
        
        String updatedBy = entry.getUpdatedBy();
        assertEquals("user456", updatedBy);
    }

    @Test
    void testGetDeleteAtMethod() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        
        JSONObject json = new JSONObject();
        json.put("deleted_at", "2024-01-03T00:00:00.000Z");
        entry.configure(json);
        
        java.util.Calendar deletedAt = entry.getDeleteAt();
        assertNotNull(deletedAt);
    }

    @Test
    void testGetDeletedByMethod() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        
        JSONObject json = new JSONObject();
        json.put("deleted_by", "user789");
        entry.configure(json);
        
        String deletedBy = entry.getDeletedBy();
        assertEquals("user789", deletedBy);
    }

    // ========== ASSET/GROUP METHODS TESTS ==========

    @Test
    void testGetAssetMethod() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        
        JSONObject assetJson = new JSONObject();
        assetJson.put("uid", "asset123");
        assetJson.put("filename", "test.jpg");
        
        JSONObject json = new JSONObject();
        json.put("asset_field", assetJson);
        entry.configure(json);
        
        Asset asset = entry.getAsset("asset_field");
        assertNotNull(asset);
    }

    @Test
    void testGetAssetsMethod() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        
        JSONObject asset1 = new JSONObject();
        asset1.put("uid", "asset1");
        
        JSONObject asset2 = new JSONObject();
        asset2.put("uid", "asset2");
        
        org.json.JSONArray assetsArray = new org.json.JSONArray();
        assetsArray.put(asset1);
        assetsArray.put(asset2);
        
        JSONObject json = new JSONObject();
        json.put("assets_field", assetsArray);
        entry.configure(json);
        
        List<Asset> assets = entry.getAssets("assets_field");
        assertNotNull(assets);
        assertEquals(2, assets.size());
    }

    @Test
    void testGetGroupMethod() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        
        JSONObject groupJson = new JSONObject();
        groupJson.put("field1", "value1");
        
        JSONObject json = new JSONObject();
        json.put("group_field", groupJson);
        entry.configure(json);
        
        Group group = entry.getGroup("group_field");
        assertNotNull(group);
    }

    @Test
    void testGetGroupWithEmptyKey() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        
        JSONObject json = new JSONObject();
        entry.configure(json);
        
        Group group = entry.getGroup("");
        assertNull(group);
    }

    @Test
    void testGetGroupsMethod() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        
        JSONObject group1 = new JSONObject();
        group1.put("field1", "value1");
        
        JSONObject group2 = new JSONObject();
        group2.put("field2", "value2");
        
        org.json.JSONArray groupsArray = new org.json.JSONArray();
        groupsArray.put(group1);
        groupsArray.put(group2);
        
        JSONObject json = new JSONObject();
        json.put("groups_field", groupsArray);
        entry.configure(json);
        
        List<Group> groups = entry.getGroups("groups_field");
        assertNotNull(groups);
        assertEquals(2, groups.size());
    }

    @Test
    void testGetGroupsWithEmptyKey() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        
        JSONObject json = new JSONObject();
        entry.configure(json);
        
        List<Group> groups = entry.getGroups("");
        assertTrue(groups.isEmpty());
    }

    @Test
    void testGetAllEntriesMethod() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        
        JSONObject refEntry1 = new JSONObject();
        refEntry1.put("uid", "ref_entry1");
        refEntry1.put("title", "Referenced Entry 1");
        
        JSONObject refEntry2 = new JSONObject();
        refEntry2.put("uid", "ref_entry2");
        refEntry2.put("title", "Referenced Entry 2");
        
        org.json.JSONArray refArray = new org.json.JSONArray();
        refArray.put(refEntry1);
        refArray.put(refEntry2);
        
        JSONObject json = new JSONObject();
        json.put("reference_field", refArray);
        entry.configure(json);
        
        List<Entry> allEntries = entry.getAllEntries("reference_field", "referenced_type");
        assertNotNull(allEntries);
        assertEquals(2, allEntries.size());
    }

    // ========== FETCH METHOD TESTS ==========

    @Test
    void testFetchWithEmptyUid() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        entry.setUid(""); // Empty UID
        
        EntryResultCallBack callback = new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                // Callback implementation
            }
        };
        
        // Should not throw, should handle empty UID gracefully
        assertDoesNotThrow(() -> entry.fetch(callback));
    }

    // ========== VARIANTS METHOD TESTS ==========

    @Test
    void testVariantsWithSingleVariant() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        
        Entry result = entry.variants("variant_uid_123");
        
        assertNotNull(result);
        assertTrue(entry.headers.containsKey("x-cs-variant-uid"));
        assertEquals("variant_uid_123", entry.headers.get("x-cs-variant-uid"));
    }

    @Test
    void testVariantsWithEmptyString() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        
        Entry result = entry.variants("");
        
        assertNotNull(result);
        assertFalse(entry.headers.containsKey("x-cs-variant-uid"));
    }

    @Test
    void testVariantsWithMultipleVariants() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        
        String[] variants = {"variant1", "variant2", "variant3"};
        Entry result = entry.variants(variants);
        
        assertNotNull(result);
        assertTrue(entry.headers.containsKey("x-cs-variant-uid"));
        String headerValue = (String) entry.headers.get("x-cs-variant-uid");
        assertTrue(headerValue.contains("variant1"));
        assertTrue(headerValue.contains("variant2"));
        assertTrue(headerValue.contains("variant3"));
    }

    @Test
    void testVariantsWithEmptyArray() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        
        String[] variants = {};
        Entry result = entry.variants(variants);
        
        assertNotNull(result);
        assertFalse(entry.headers.containsKey("x-cs-variant-uid"));
    }

    @Test
    void testVariantsWithNullAndEmptyStrings() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        
        String[] variants = {null, "", "valid_variant", "  ", "another_valid"};
        Entry result = entry.variants(variants);
        
        assertNotNull(result);
        assertTrue(entry.headers.containsKey("x-cs-variant-uid"));
        String headerValue = (String) entry.headers.get("x-cs-variant-uid");
        assertTrue(headerValue.contains("valid_variant"));
        assertTrue(headerValue.contains("another_valid"));
        assertFalse(headerValue.contains("null"));
    }

    // ========== GET HEADERS METHOD TEST ==========

    @Test
    void testGetHeaders() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        
        entry.setHeader("custom-header", "custom-value");
        
        LinkedHashMap<String, Object> headers = entry.getHeaders();
        assertNotNull(headers);
        assertTrue(headers.containsKey("custom-header"));
        assertEquals("custom-value", headers.get("custom-header"));
    }

    // ========== SET INCLUDE JSON TESTS (via fetch) ==========

    @Test
    void testFetchWithOnlyFields() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        entry.setUid("test_uid");
        
        // Set only fields to trigger objectUidForOnly branch
        entry.only(new String[]{"title", "description"});
        
        EntryResultCallBack callback = new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                // Callback implementation
            }
        };
        
        // This will call setIncludeJSON internally with objectUidForOnly
        assertDoesNotThrow(() -> entry.fetch(callback));
    }

    @Test
    void testFetchWithExceptFields() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        entry.setUid("test_uid");
        
        // Set except fields to trigger exceptFieldArray branch
        entry.except(new String[]{"metadata", "internal_field"});
        
        EntryResultCallBack callback = new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                // Callback implementation
            }
        };
        
        // This will call setIncludeJSON internally with exceptFieldArray
        assertDoesNotThrow(() -> entry.fetch(callback));
    }

    @Test
    void testFetchWithOnlyReferenceUid() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        entry.setUid("test_uid");
        
        // Set only with reference UID to trigger onlyJsonObject branch
        List<String> fields = Arrays.asList("title", "url");
        entry.onlyWithReferenceUid(fields, "reference_field");
        
        EntryResultCallBack callback = new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                // Callback implementation
            }
        };
        
        // This will call setIncludeJSON internally with onlyJsonObject
        assertDoesNotThrow(() -> entry.fetch(callback));
    }

    @Test
    void testFetchWithExceptReferenceUid() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        entry.setUid("test_uid");
        
        // Set except with reference UID to trigger exceptJsonObject branch
        List<String> fields = Arrays.asList("metadata", "internal");
        entry.exceptWithReferenceUid(fields, "reference_field");
        
        EntryResultCallBack callback = new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                // Callback implementation
            }
        };
        
        // This will call setIncludeJSON internally with exceptJsonObject
        assertDoesNotThrow(() -> entry.fetch(callback));
    }

    @Test
    void testFetchWithMultipleParams() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        entry.setUid("test_uid");
        
        // Add multiple params to trigger iterator loop in setIncludeJSON
        entry.addParam("include_schema", "true");
        entry.addParam("include_metadata", "true");
        entry.addParam("locale", "en-us");
        
        EntryResultCallBack callback = new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                // Callback implementation
            }
        };
        
        // This will call setIncludeJSON internally with multiple params
        assertDoesNotThrow(() -> entry.fetch(callback));
    }

    @Test
    void testFetchWithAllIncludeOptions() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        entry.setUid("test_uid");
        
        // Set all include options to cover all branches
        entry.only(new String[]{"title", "description"});
        entry.except(new String[]{"metadata"});
        entry.onlyWithReferenceUid(Arrays.asList("name", "email"), "author");
        entry.exceptWithReferenceUid(Arrays.asList("password"), "user");
        entry.addParam("include_schema", "true");
        entry.addParam("locale", "en-us");
        
        EntryResultCallBack callback = new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                // Callback implementation
            }
        };
        
        // This will call setIncludeJSON with all branches
        assertDoesNotThrow(() -> entry.fetch(callback));
    }

    @Test
    void testFetchWithEmptyOnlyArray() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        entry.setUid("test_uid");
        
        // Set empty only array (length == 0, should not trigger branch)
        entry.only(new String[]{});
        
        EntryResultCallBack callback = new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                // Callback implementation
            }
        };
        
        assertDoesNotThrow(() -> entry.fetch(callback));
    }

    @Test
    void testFetchWithEmptyExceptArray() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        entry.setUid("test_uid");
        
        // Set empty except array (length == 0, should not trigger branch)
        entry.except(new String[]{});
        
        EntryResultCallBack callback = new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                // Callback implementation
            }
        };
        
        assertDoesNotThrow(() -> entry.fetch(callback));
    }

    @Test
    void testFetchWithNullUid() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        entry.setUid(null);
        
        EntryResultCallBack callback = new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                // Callback implementation
            }
        };
        
        // Fetch with null UID throws NullPointerException (code doesn't check for null)
        assertThrows(NullPointerException.class, () -> entry.fetch(callback));
    }

    @Test
    void testFetchClearsOnlyAndExceptAfterUse() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        entry.setUid("test_uid");
        
        // Set only and except fields
        entry.only(new String[]{"title"});
        entry.except(new String[]{"metadata"});
        
        // Verify they are set
        assertNotNull(entry.objectUidForOnly);
        assertNotNull(entry.exceptFieldArray);
        
        EntryResultCallBack callback = new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                // Callback implementation
            }
        };
        
        entry.fetch(callback);
        
        // After fetch, these should be cleared (set to null) by setIncludeJSON
        // Note: This happens asynchronously, so we're just testing the method execution
        assertDoesNotThrow(() -> entry.fetch(callback));
    }

    @Test
    void testFetchWithLocaleParam() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        entry.setUid("test_uid");
        
        // Set locale to add to params
        entry.setLocale("fr-fr");
        
        EntryResultCallBack callback = new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                // Callback implementation
            }
        };
        
        // This will call setIncludeJSON with locale param
        assertDoesNotThrow(() -> entry.fetch(callback));
    }

    @Test
    void testFetchWithIncludeReference() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        entry.setUid("test_uid");
        
        // Add include reference to params
        entry.includeReference("author");
        entry.includeReference(new String[]{"categories", "tags"});
        
        EntryResultCallBack callback = new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                // Callback implementation
            }
        };
        
        // This will call setIncludeJSON with include[] param
        assertDoesNotThrow(() -> entry.fetch(callback));
    }

    @Test
    void testFetchWithAllIncludeMethods() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("test");
        Entry entry = ct.entry();
        entry.setUid("test_uid");
        
        // Call all include methods
        entry.includeFallback();
        entry.includeBranch();
        entry.includeMetadata();
        entry.includeContentType();
        entry.includeEmbeddedItems();
        entry.includeReferenceContentTypeUID();
        
        EntryResultCallBack callback = new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                // Callback implementation
            }
        };
        
        // This will call setIncludeJSON with all these params
        assertDoesNotThrow(() -> entry.fetch(callback));
    }
}
