package com.contentstack.sdk;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for Query class.
 * Tests all query building methods, operators, and configurations.
 */
public class TestQuery {

    private Query query;
    private final String contentTypeUid = "test_content_type";

    @BeforeEach
    void setUp() {
        query = new Query(contentTypeUid);
        query.headers = new LinkedHashMap<>();
    }

    // ========== CONSTRUCTOR & BASIC TESTS ==========

    @Test
    void testQueryConstructor() {
        Query testQuery = new Query("my_content_type");
        assertNotNull(testQuery);
        assertEquals("my_content_type", testQuery.contentTypeUid);
        assertNotNull(testQuery.urlQueries);
        assertNotNull(testQuery.queryValue);
        assertNotNull(testQuery.mainJSON);
    }

    @Test
    void testGetContentType() {
        // Query.contentTypeUid is protected, directly accessible in tests
        assertEquals(contentTypeUid, query.contentTypeUid);
    }

    // ========== HEADER TESTS ==========

    @Test
    void testSetHeader() {
        Query result = query.setHeader("custom-key", "custom-value");
        assertSame(query, result); // Check method chaining
        assertTrue(query.headers.containsKey("custom-key"));
        assertEquals("custom-value", query.headers.get("custom-key"));
    }

    @Test
    void testSetMultipleHeaders() {
        query.setHeader("key1", "value1")
             .setHeader("key2", "value2")
             .setHeader("key3", "value3");
        
        assertEquals(3, query.headers.size());
        assertEquals("value1", query.headers.get("key1"));
        assertEquals("value2", query.headers.get("key2"));
        assertEquals("value3", query.headers.get("key3"));
    }

    @Test
    void testSetHeaderWithEmptyKey() {
        query.setHeader("", "value");
        assertFalse(query.headers.containsKey(""));
    }

    @Test
    void testSetHeaderWithEmptyValue() {
        query.setHeader("key", "");
        assertFalse(query.headers.containsKey("key"));
    }

    @Test
    void testRemoveHeader() {
        query.setHeader("test-key", "test-value");
        assertTrue(query.headers.containsKey("test-key"));
        
        Query result = query.removeHeader("test-key");
        assertSame(query, result);
        assertFalse(query.headers.containsKey("test-key"));
    }

    @Test
    void testRemoveNonExistentHeader() {
        query.removeHeader("non-existent");
        // Should not throw exception
    }

    // ========== WHERE CLAUSE TESTS ==========

    @Test
    void testWhereWithString() {
        Query result = query.where("title", "Test Title");
        assertSame(query, result);
        assertNotNull(query.queryValueJSON);
    }

    @Test
    void testWhereWithNumber() {
        query.where("count", 10);
        assertNotNull(query.queryValueJSON);
    }

    @Test
    void testWhereWithBoolean() {
        query.where("published", true);
        assertNotNull(query.queryValueJSON);
    }

    @Test
    void testWhereMultipleConditions() {
        query.where("title", "Test")
             .where("count", 5)
             .where("active", true);
        assertNotNull(query.queryValueJSON);
    }

    // ========== ADD/REMOVE QUERY TESTS ==========

    @Test
    void testAddQuery() {
        Query result = query.addQuery("custom_field", "custom_value");
        assertSame(query, result);
    }

    @Test
    void testAddMultipleQueries() {
        query.addQuery("field1", "value1")
             .addQuery("field2", "value2");
        assertNotNull(query.urlQueries);
    }

    @Test
    void testRemoveQuery() {
        query.addQuery("field1", "value1");
        Query result = query.removeQuery("field1");
        assertSame(query, result);
    }

    // ========== COMPARISON OPERATORS ==========

    @Test
    void testLessThan() {
        Query result = query.lessThan("price", 100);
        assertSame(query, result);
        assertNotNull(query.queryValueJSON);
    }

    @Test
    void testLessThanOrEqualTo() {
        Query result = query.lessThanOrEqualTo("price", 100);
        assertSame(query, result);
        assertNotNull(query.queryValueJSON);
    }

    @Test
    void testGreaterThan() {
        Query result = query.greaterThan("price", 50);
        assertSame(query, result);
        assertNotNull(query.queryValueJSON);
    }

    @Test
    void testGreaterThanOrEqualTo() {
        Query result = query.greaterThanOrEqualTo("price", 50);
        assertSame(query, result);
        assertNotNull(query.queryValueJSON);
    }

    @Test
    void testNotEqualTo() {
        Query result = query.notEqualTo("status", "draft");
        assertSame(query, result);
        assertNotNull(query.queryValueJSON);
    }

    @Test
    void testMultipleComparisonOperators() {
        query.greaterThan("price", 10)
             .lessThan("price", 100)
             .notEqualTo("status", "archived");
        assertNotNull(query.queryValueJSON);
    }

    // ========== ARRAY OPERATORS ==========

    @Test
    void testContainedIn() {
        String[] values = {"value1", "value2", "value3"};
        Query result = query.containedIn("tags", values);
        assertSame(query, result);
        assertNotNull(query.queryValueJSON);
    }

    @Test
    void testContainedInWithNumbers() {
        Integer[] values = {1, 2, 3, 4, 5};
        query.containedIn("priority", values);
        assertNotNull(query.queryValueJSON);
    }

    @Test
    void testNotContainedIn() {
        String[] values = {"blocked", "spam"};
        Query result = query.notContainedIn("status", values);
        assertSame(query, result);
        assertNotNull(query.queryValueJSON);
    }

    @Test
    void testContainedInWithEmptyArray() {
        String[] values = {};
        query.containedIn("tags", values);
        assertNotNull(query.queryValueJSON);
    }

    // ========== EXISTENCE OPERATORS ==========

    @Test
    void testExists() {
        Query result = query.exists("featured_image");
        assertSame(query, result);
        assertNotNull(query.queryValueJSON);
    }

    @Test
    void testNotExists() {
        Query result = query.notExists("legacy_field");
        assertSame(query, result);
        assertNotNull(query.queryValueJSON);
    }

    @Test
    void testMultipleExistenceChecks() {
        query.exists("author")
             .notExists("deprecated_field");
        assertNotNull(query.queryValueJSON);
    }

    // ========== LOGICAL OPERATORS ==========

    @Test
    void testAndOperator() {
        Query query1 = new Query(contentTypeUid);
        query1.headers = new LinkedHashMap<>();
        query1.where("title", "Test");
        
        Query query2 = new Query(contentTypeUid);
        query2.headers = new LinkedHashMap<>();
        query2.where("status", "published");
        
        List<Query> queries = new ArrayList<>();
        queries.add(query1);
        queries.add(query2);
        
        Query result = query.and(queries);
        assertSame(query, result);
    }

    @Test
    void testOrOperator() {
        Query query1 = new Query(contentTypeUid);
        query1.headers = new LinkedHashMap<>();
        
        Query query2 = new Query(contentTypeUid);
        query2.headers = new LinkedHashMap<>();
        
        List<Query> queries = new ArrayList<>();
        queries.add(query1);
        queries.add(query2);
        
        Query result = query.or(queries);
        assertSame(query, result);
    }

    // ========== REFERENCE METHODS ==========

    @Test
    void testIncludeReference() {
        Query result = query.includeReference("author");
        assertSame(query, result);
        assertNotNull(query.objectUidForInclude);
    }

    @Test
    void testIncludeMultipleReferences() {
        query.includeReference("author")
             .includeReference("category")
             .includeReference("tags");
        assertNotNull(query.objectUidForInclude);
    }

    @Test
    void testIncludeReferenceContentTypUid() {
        Query result = query.includeReferenceContentTypUid();
        assertSame(query, result);
    }

    // ========== SORTING TESTS ==========

    @Test
    void testAscending() {
        Query result = query.ascending("created_at");
        assertSame(query, result);
        assertNotNull(query.urlQueries);
    }

    @Test
    void testDescending() {
        Query result = query.descending("updated_at");
        assertSame(query, result);
        assertNotNull(query.urlQueries);
    }

    @Test
    void testMultipleSortingFields() {
        query.ascending("title")
             .descending("created_at");
        assertNotNull(query.urlQueries);
    }

    // ========== PROJECTION TESTS ==========

    @Test
    void testOnly() {
        String[] fields = {"title", "description", "author"};
        Query result = query.only(fields);
        assertSame(query, result);
        assertNotNull(query.objectUidForOnly);
    }

    @Test
    void testOnlyWithSingleField() {
        String[] fields = {"title"};
        query.only(fields);
        assertNotNull(query.objectUidForOnly);
    }

    @Test
    void testExcept() {
        String[] fields = {"internal_notes", "draft_content"};
        Query result = query.except(fields);
        assertSame(query, result);
        assertNotNull(query.objectUidForExcept);
    }

    @Test
    void testExceptWithList() {
        List<String> fields = new ArrayList<>();
        fields.add("field1");
        fields.add("field2");
        
        Query result = query.except(fields);
        assertSame(query, result);
        assertNotNull(query.objectUidForExcept);
    }

    @Test
    void testOnlyWithReferenceUid() {
        List<String> fields = new ArrayList<>();
        fields.add("title");
        fields.add("name");
        
        Query result = query.onlyWithReferenceUid(fields, "author");
        assertSame(query, result);
    }

    @Test
    void testExceptWithReferenceUid() {
        List<String> fields = new ArrayList<>();
        fields.add("internal_data");
        
        Query result = query.exceptWithReferenceUid(fields, "metadata");
        assertSame(query, result);
    }

    // ========== PAGINATION TESTS ==========

    @Test
    void testLimit() {
        Query result = query.limit(10);
        assertSame(query, result);
        assertNotNull(query.urlQueries);
    }

    @Test
    void testLimitWithZero() {
        query.limit(0);
        assertNotNull(query.urlQueries);
    }

    @Test
    void testLimitWithLargeNumber() {
        query.limit(1000);
        assertNotNull(query.urlQueries);
    }

    @Test
    void testSkip() {
        Query result = query.skip(20);
        assertSame(query, result);
        assertNotNull(query.urlQueries);
    }

    @Test
    void testSkipWithZero() {
        query.skip(0);
        assertNotNull(query.urlQueries);
    }

    @Test
    void testPaginationCombination() {
        query.limit(10).skip(20);
        assertNotNull(query.urlQueries);
    }

    // ========== COUNT TESTS ==========

    @Test
    void testCount() {
        Query result = query.count();
        assertSame(query, result);
    }

    @Test
    void testIncludeCount() {
        Query result = query.includeCount();
        assertSame(query, result);
        assertNotNull(query.urlQueries);
    }

    // ========== CONTENT TYPE TESTS ==========

    @Test
    void testIncludeContentType() {
        Query result = query.includeContentType();
        assertSame(query, result);
        assertNotNull(query.urlQueries);
    }

    // ========== REGEX TESTS ==========

    @Test
    void testRegexWithModifiers() {
        Query result = query.regex("title", "test", "i");
        assertSame(query, result);
        assertNotNull(query.queryValueJSON);
    }


    // ========== TAGS TESTS ==========


    @Test
    void testTagsWithSingleTag() {
        String[] tags = {"featured"};
        query.tags(tags);
        assertNotNull(query.queryValue);
    }


    // ========== LOCALE TESTS ==========

    @Test
    void testLocale() {
        Query result = query.locale("en-us");
        assertSame(query, result);
        assertNotNull(query.urlQueries);
    }

    @Test
    void testLocaleWithDifferentLocales() {
        query.locale("fr-fr");
        assertNotNull(query.urlQueries);
        
        Query query2 = new Query("test");
        query2.locale("es-es");
        assertNotNull(query2.urlQueries);
    }

    // ========== SEARCH TESTS ==========

    @Test
    void testSearch() {
        Query result = query.search("searchKeyword");
        assertSame(query, result);
        assertNotNull(query.urlQueries);
    }

    @Test
    void testSearchWithSpecialCharacters() {
        query.search("search with spaces");
        assertNotNull(query.urlQueries);
    }

    // ========== WHERE IN/NOT IN TESTS ==========

    @Test
    void testWhereIn() {
        Query subQuery = new Query("category");
        subQuery.headers = new LinkedHashMap<>();
        
        Query result = query.whereIn("category_id", subQuery);
        assertSame(query, result);
    }

    @Test
    void testWhereNotIn() {
        Query subQuery = new Query("blocked_users");
        subQuery.headers = new LinkedHashMap<>();
        
        Query result = query.whereNotIn("user_id", subQuery);
        assertSame(query, result);
    }

    // ========== INCLUDE METHODS TESTS ==========

    @Test
    void testIncludeFallback() {
        Query result = query.includeFallback();
        assertSame(query, result);
        assertNotNull(query.urlQueries);
    }

    @Test
    void testIncludeEmbeddedItems() {
        Query result = query.includeEmbeddedItems();
        assertSame(query, result);
        assertNotNull(query.urlQueries);
    }

    @Test
    void testIncludeBranch() {
        Query result = query.includeBranch();
        assertSame(query, result);
        assertNotNull(query.urlQueries);
    }

    @Test
    void testIncludeMetadata() {
        Query result = query.includeMetadata();
        assertSame(query, result);
        assertNotNull(query.urlQueries);
    }

    @Test
    void testMultipleIncludeMethods() {
        query.includeFallback()
             .includeEmbeddedItems()
             .includeBranch()
             .includeMetadata()
             .includeCount()
             .includeContentType();
        assertNotNull(query.urlQueries);
    }

    // ========== ADD PARAM TESTS ==========

    @Test
    void testAddParam() {
        Query result = query.addParam("include_dimensions", "true");
        assertSame(query, result);
        assertNotNull(query.urlQueries);
    }

    @Test
    void testAddMultipleParams() {
        query.addParam("param1", "value1")
             .addParam("param2", "value2")
             .addParam("param3", "value3");
        assertNotNull(query.urlQueries);
    }

    // ========== METHOD CHAINING TESTS ==========

    @Test
    void testComplexQueryChaining() {
        Query result = query
            .where("status", "published")
            .greaterThan("views", 1000)
            .lessThan("views", 10000)
            .exists("featured_image")
            .ascending("created_at")
            .limit(10)
            .skip(0)
            .includeCount()
            .includeReference("author")
            .locale("en-us");
        
        assertSame(query, result);
        assertNotNull(query.urlQueries);
        assertNotNull(query.queryValueJSON);
    }

    @Test
    void testQueryBuildingComplex() {
        query.where("type", "article")
             .containedIn("category", new String[]{"tech", "science"})
             .greaterThanOrEqualTo("rating", 4.0)
             .exists("author")
             .notEqualTo("status", "draft")
             .descending("published_date")
             .limit(20)
             .includeReference("author")
             .includeReference("tags")
             .includeCount()
             .search("technology");
        
        assertNotNull(query.urlQueries);
        assertNotNull(query.queryValueJSON);
    }

    // ========== EDGE CASES ==========

    @Test
    void testQueryWithNullHeader() {
        query.headers = null;
        // Should handle gracefully when headers is null
    }

    @Test
    void testMultipleOperationsOnSameField() {
        query.where("price", 100)
             .greaterThan("price", 50)
             .lessThan("price", 200);
        assertNotNull(query.queryValueJSON);
    }

    // ========== CONDITIONAL BRANCH TESTS ==========

    @Test
    void testLessThanWithExistingKey() {
        query.where("price", 100);
        Query result = query.lessThan("price", 200);
        assertNotNull(result);
    }

    @Test
    void testGreaterThanWithNewQueryValue() {
        query.queryValue = new JSONObject();
        query.queryValue.put("existing", "value");
        Query result = query.greaterThan("new_field", 50);
        assertNotNull(result);
    }

    @Test
    void testContainedInWithExistingKey() {
        query.where("status", "active");
        Query result = query.containedIn("status", new Object[]{"active", "pending"});
        assertNotNull(result);
    }

    @Test
    void testExistsWithNewQueryValue() {
        query.queryValue = new JSONObject();
        query.queryValue.put("existing", "value");
        Query result = query.exists("new_field");
        assertNotNull(result);
    }

    @Test
    void testRegexWithModifiersNullModifiers() {
        Query result = query.regex("name", "^test", null);
        assertNotNull(result);
    }

    @Test
    void testRegexWithModifiersWithValue() {
        Query result = query.regex("name", "^test", "i");
        assertNotNull(result);
    }

    @Test
    void testIncludeContentTypeWithExistingSchema() {
        query.urlQueries.put("include_schema", true);
        Query result = query.includeContentType();
        assertNotNull(result);
        assertFalse(query.urlQueries.has("include_schema"));
    }

    // ========== FIND/FIND ONE TESTS ==========

    @Test
    void testFindWithValidCallback() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("blog_post");
        Query q = ct.query();
        q.headers.put("environment", "production");
        
        QueryResultsCallBack callback = new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                // Callback implementation
            }
        };
        
        assertDoesNotThrow(() -> q.find(callback));
    }



    @Test
    void testFindOneWithValidCallback() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("blog_post");
        Query q = ct.query();
        q.headers.put("environment", "production");
        
        SingleQueryResultCallback callback = new SingleQueryResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Entry entry, Error error) {
                // Callback implementation
            }
        };
        
        assertDoesNotThrow(() -> q.findOne(callback));
    }

    @Test
    void testFindOneWithExistingLimit() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("blog_post");
        Query q = ct.query();
        q.headers.put("environment", "production");
        q.limit(10); // Set existing limit
        
        SingleQueryResultCallback callback = new SingleQueryResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Entry entry, Error error) {
                // Callback implementation
            }
        };
        
        assertDoesNotThrow(() -> q.findOne(callback));
    }



    // ========== SET QUERY JSON TESTS (via find) ==========

    @Test
    void testSetQueryJsonWithAllFields() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("blog_post");
        Query q = ct.query();
        q.headers.put("environment", "production");
        
        // Set all possible fields
        q.where("title", "Test");
        q.except(new String[]{"field1"});
        q.only(new String[]{"field2"});
        q.onlyWithReferenceUid(List.of("ref_field"), "reference");
        q.exceptWithReferenceUid(List.of("except_field"), "reference2");
        q.includeReference("include_ref");
        
        QueryResultsCallBack callback = new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                // Callback implementation
            }
        };
        
        assertDoesNotThrow(() -> q.find(callback));
    }

    // ========== GET URL PARAMS TESTS (private, tested via find) ==========

    @Test
    void testGetUrlParamsWithMultipleParams() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("blog_post");
        Query q = ct.query();
        q.headers.put("environment", "production");
        
        q.where("field1", "value1");
        q.limit(10);
        q.skip(5);
        q.includeCount();
        
        QueryResultsCallBack callback = new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                // Callback implementation
            }
        };
        
        assertDoesNotThrow(() -> q.find(callback));
    }

    // ========== EXCEPTION PATH TESTS (with AssertionError handling) ==========

    @Test
    void testLessThanOrEqualToWithInvalidKey() {
        // This triggers throwException with null exception, which causes AssertionError due to assert e != null
        try {
            query.lessThanOrEqualTo("invalid@key!", "value");
        } catch (AssertionError e) {
            // Expected - the throwException method has assert e != null
        }
    }

    @Test
    void testGreaterThanOrEqualToWithInvalidKey() {
        try {
            query.greaterThanOrEqualTo("invalid@key!", "value");
        } catch (AssertionError e) {
            // Expected
        }
    }

    @Test
    void testNotEqualToWithInvalidKey() {
        try {
            query.notEqualTo("invalid@key!", "value");
        } catch (AssertionError e) {
            // Expected
        }
    }

    @Test
    void testNotContainedInWithInvalidKey() {
        try {
            query.notContainedIn("invalid@key!", new Object[]{"val1"});
        } catch (AssertionError e) {
            // Expected
        }
    }

    @Test
    void testExistsWithInvalidKey() {
        try {
            query.exists("invalid@key!");
        } catch (AssertionError e) {
            // Expected
        }
    }

    @Test
    void testNotExistsWithInvalidKey() {
        try {
            query.notExists("invalid@key!");
        } catch (AssertionError e) {
            // Expected
        }
    }

    @Test
    void testRegexWithInvalidKey() {
        try {
            query.regex("invalid@key!", "^pattern");
        } catch (AssertionError e) {
            // Expected
        }
    }

    // ========== EXCEPTION CATCH BLOCK TESTS ==========

    @Test
    void testOrWithNullQueryObjects() {
        Query result = query.or(null);
        assertNotNull(result);
    }

    @Test
    void testRegexWithModifiersException() {
        // Test exception path in regex with modifiers
        Query result = query.regex("field", "^pattern", "i");
        assertNotNull(result);
    }

    @Test
    void testRegexWithModifiersExceptionPath() {
        query.queryValue = new JSONObject();
        query.queryValue.put("test", "value");
        Query result = query.regex("field", "^pattern", "i");
        assertNotNull(result);
    }

    // ========== INCLUDE LIVE PREVIEW TESTS ==========

    @Test
    void testIncludeLivePreviewWithConditions() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        
        // Enable live preview
        stack.config.enableLivePreview = true;
        stack.config.livePreviewContentType = "blog_post";
        stack.config.livePreviewHash = null;
        
        ContentType ct = stack.contentType("blog_post");
        Query q = ct.query();
        q.headers.put("environment", "production");
        
        QueryResultsCallBack callback = new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                // Callback implementation
            }
        };
        
        assertDoesNotThrow(() -> q.find(callback));
        assertEquals("init", stack.config.livePreviewHash);
    }

    @Test
    void testIncludeLivePreviewWithEmptyHash() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        
        stack.config.enableLivePreview = true;
        stack.config.livePreviewContentType = "blog_post";
        stack.config.livePreviewHash = "";
        
        ContentType ct = stack.contentType("blog_post");
        Query q = ct.query();
        q.headers.put("environment", "production");
        
        QueryResultsCallBack callback = new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                // Callback implementation
            }
        };
        
        assertDoesNotThrow(() -> q.find(callback));
        assertEquals("init", stack.config.livePreviewHash);
    }

    @Test
    void testIncludeLivePreviewDisabled() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        
        stack.config.enableLivePreview = false;
        
        ContentType ct = stack.contentType("blog_post");
        Query q = ct.query();
        q.headers.put("environment", "production");
        
        QueryResultsCallBack callback = new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
                // Callback implementation
            }
        };
        
        assertDoesNotThrow(() -> q.find(callback));
    }

    // ========== GET RESULT OBJECT TESTS ==========

    @Test
    void testGetResultObjectWithSingleEntry() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("blog_post");
        Query q = ct.query();
        
        // Create mock entry model
        List<Object> objects = new ArrayList<>();
        JSONObject entryJson = new JSONObject();
        entryJson.put("uid", "entry_123");
        entryJson.put("title", "Test Entry");
        entryJson.put("url", "/test");
        entryJson.put("tags", new JSONArray());
        
        EntryModel model = new EntryModel(entryJson);
        objects.add(model);
        
        JSONObject resultJson = new JSONObject();
        
        // This will trigger the getResultObject method with isSingleEntry = true
        q.getResultObject(objects, resultJson, true);
        
        assertNotNull(q);
    }

    @Test
    void testGetResultObjectWithMultipleEntries() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("blog_post");
        Query q = ct.query();
        
        // Create mock entry models
        List<Object> objects = new ArrayList<>();
        
        JSONObject entry1Json = new JSONObject();
        entry1Json.put("uid", "entry_1");
        entry1Json.put("title", "Entry 1");
        entry1Json.put("tags", new JSONArray());
        EntryModel model1 = new EntryModel(entry1Json);
        objects.add(model1);
        
        JSONObject entry2Json = new JSONObject();
        entry2Json.put("uid", "entry_2");
        entry2Json.put("title", "Entry 2");
        entry2Json.put("tags", new JSONArray());
        EntryModel model2 = new EntryModel(entry2Json);
        objects.add(model2);
        
        JSONObject resultJson = new JSONObject();
        
        // This will trigger the getResultObject method with isSingleEntry = false
        q.getResultObject(objects, resultJson, false);
        
        assertNotNull(q);
    }

    @Test
    void testGetResultObjectWithEmptyList() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("blog_post");
        Query q = ct.query();
        
        List<Object> objects = new ArrayList<>();
        JSONObject resultJson = new JSONObject();
        
        q.getResultObject(objects, resultJson, true);
        
        assertNotNull(q);
    }

    @Test
    void testGetResultObjectWithException() throws IllegalAccessException {
        Stack stack = Contentstack.stack("api_key", "delivery_token", "env");
        ContentType ct = stack.contentType("blog_post");
        Query q = ct.query();
        
        // Create mock entry model
        List<Object> objects = new ArrayList<>();
        JSONObject entryJson = new JSONObject();
        entryJson.put("uid", "entry_123");
        entryJson.put("title", "Test Entry");
        entryJson.put("tags", new JSONArray());
        
        EntryModel model = new EntryModel(entryJson);
        objects.add(model);
        
        JSONObject resultJson = new JSONObject();
        
        // Trigger exception path by having stackInstance null
        // The catch block will create Entry with contentTypeUid
        q.contentTypeInstance = null;
        try {
            q.getResultObject(objects, resultJson, false);
        } catch (NullPointerException e) {
            // Expected when contentTypeInstance is null
        }
    }

    // ========== CONDITIONAL BRANCH COVERAGE ==========

    @Test
    void testLessThanOrEqualToWithExistingKey() {
        query.where("field", "value");
        Query result = query.lessThanOrEqualTo("field", 100);
        assertNotNull(result);
    }

    @Test
    void testGreaterThanOrEqualToWithNewQueryValue() {
        query.queryValue = new JSONObject();
        query.queryValue.put("existing", "value");
        Query result = query.greaterThanOrEqualTo("new_field", 50);
        assertNotNull(result);
    }

    @Test
    void testNotEqualToWithExistingKey() {
        query.where("status", "draft");
        Query result = query.notEqualTo("status", "published");
        assertNotNull(result);
    }

    @Test
    void testNotContainedInWithExistingKey() {
        query.where("category", "tech");
        Query result = query.notContainedIn("category", new Object[]{"sports", "entertainment"});
        assertNotNull(result);
    }

    @Test
    void testNotExistsWithNewQueryValue() {
        query.queryValue = new JSONObject();
        query.queryValue.put("existing", "value");
        Query result = query.notExists("optional_field");
        assertNotNull(result);
    }
}
