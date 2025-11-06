package com.contentstack.sdk;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for Taxonomy class.
 * Tests all query building methods and method chaining.
 */
public class TestTaxonomy {

    private Taxonomy taxonomy;
    private APIService service;
    private Config config;
    private LinkedHashMap<String, Object> headers;

    @BeforeEach
    void setUp() throws IllegalAccessException {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_environment");
        service = stack.service;
        config = stack.config;
        headers = stack.headers;
        
        taxonomy = new Taxonomy(service, config, headers);
    }

    // ========== IN OPERATOR TESTS ==========

    @Test
    void testInWithSingleItem() {
        List<String> items = Arrays.asList("red");
        
        Taxonomy result = taxonomy.in("taxonomies.color", items);
        
        assertNotNull(result);
        assertSame(taxonomy, result); // Should return same instance for chaining
        assertTrue(taxonomy.query.has("taxonomies.color"));
        
        JSONObject colorQuery = taxonomy.query.getJSONObject("taxonomies.color");
        assertTrue(colorQuery.has("$in"));
    }

    @Test
    void testInWithMultipleItems() {
        List<String> items = Arrays.asList("red", "yellow", "blue");
        
        Taxonomy result = taxonomy.in("taxonomies.color", items);
        
        assertNotNull(result);
        assertTrue(taxonomy.query.has("taxonomies.color"));
        
        JSONObject colorQuery = taxonomy.query.getJSONObject("taxonomies.color");
        assertTrue(colorQuery.has("$in"));
    }

    @Test
    void testInWithEmptyList() {
        List<String> items = new ArrayList<>();
        
        Taxonomy result = taxonomy.in("taxonomies.category", items);
        
        assertNotNull(result);
        assertTrue(taxonomy.query.has("taxonomies.category"));
    }

    @Test
    void testInOverwritesPreviousValue() {
        List<String> items1 = Arrays.asList("red");
        List<String> items2 = Arrays.asList("blue", "green");
        
        taxonomy.in("taxonomies.color", items1);
        taxonomy.in("taxonomies.color", items2);
        
        JSONObject colorQuery = taxonomy.query.getJSONObject("taxonomies.color");
        assertTrue(colorQuery.has("$in"));
    }

    // ========== OR OPERATOR TESTS ==========

    @Test
    void testOrWithSingleCondition() {
        JSONObject condition1 = new JSONObject();
        condition1.put("taxonomies.color", "yellow");
        
        List<JSONObject> conditions = Arrays.asList(condition1);
        
        Taxonomy result = taxonomy.or(conditions);
        
        assertNotNull(result);
        assertSame(taxonomy, result);
        assertTrue(taxonomy.query.has("$or"));
    }

    @Test
    void testOrWithMultipleConditions() {
        JSONObject condition1 = new JSONObject();
        condition1.put("taxonomies.color", "yellow");
        
        JSONObject condition2 = new JSONObject();
        condition2.put("taxonomies.size", "small");
        
        List<JSONObject> conditions = Arrays.asList(condition1, condition2);
        
        Taxonomy result = taxonomy.or(conditions);
        
        assertNotNull(result);
        assertTrue(taxonomy.query.has("$or"));
    }

    @Test
    void testOrWithEmptyList() {
        List<JSONObject> conditions = new ArrayList<>();
        
        Taxonomy result = taxonomy.or(conditions);
        
        assertNotNull(result);
        assertTrue(taxonomy.query.has("$or"));
    }

    // ========== AND OPERATOR TESTS ==========

    @Test
    void testAndWithSingleCondition() {
        JSONObject condition1 = new JSONObject();
        condition1.put("taxonomies.color", "red");
        
        List<JSONObject> conditions = Arrays.asList(condition1);
        
        Taxonomy result = taxonomy.and(conditions);
        
        assertNotNull(result);
        assertSame(taxonomy, result);
        assertTrue(taxonomy.query.has("$and"));
    }

    @Test
    void testAndWithMultipleConditions() {
        JSONObject condition1 = new JSONObject();
        condition1.put("taxonomies.color", "red");
        
        JSONObject condition2 = new JSONObject();
        condition2.put("taxonomies.computers", "laptop");
        
        List<JSONObject> conditions = Arrays.asList(condition1, condition2);
        
        Taxonomy result = taxonomy.and(conditions);
        
        assertNotNull(result);
        assertTrue(taxonomy.query.has("$and"));
        // Note: and() uses toString(), so value is a String representation
    }

    @Test
    void testAndWithEmptyList() {
        List<JSONObject> conditions = new ArrayList<>();
        
        Taxonomy result = taxonomy.and(conditions);
        
        assertNotNull(result);
        assertTrue(taxonomy.query.has("$and"));
    }

    // ========== EXISTS OPERATOR TESTS ==========

    @Test
    void testExistsWithTrue() {
        Taxonomy result = taxonomy.exists("taxonomies.color", true);
        
        assertNotNull(result);
        assertSame(taxonomy, result);
        assertTrue(taxonomy.query.has("taxonomies.color"));
        
        JSONObject colorQuery = taxonomy.query.getJSONObject("taxonomies.color");
        assertTrue(colorQuery.has("$exists"));
        assertTrue(colorQuery.getBoolean("$exists"));
    }

    @Test
    void testExistsWithFalse() {
        Taxonomy result = taxonomy.exists("taxonomies.size", false);
        
        assertNotNull(result);
        assertTrue(taxonomy.query.has("taxonomies.size"));
        
        JSONObject sizeQuery = taxonomy.query.getJSONObject("taxonomies.size");
        assertTrue(sizeQuery.has("$exists"));
        assertFalse(sizeQuery.getBoolean("$exists"));
    }

    @Test
    void testExistsOverwritesPreviousValue() {
        taxonomy.exists("taxonomies.category", true);
        taxonomy.exists("taxonomies.category", false);
        
        JSONObject categoryQuery = taxonomy.query.getJSONObject("taxonomies.category");
        assertFalse(categoryQuery.getBoolean("$exists"));
    }

    // ========== EQUAL AND BELOW OPERATOR TESTS ==========

    @Test
    void testEqualAndBelow() {
        Taxonomy result = taxonomy.equalAndBelow("taxonomies.color", "blue");
        
        assertNotNull(result);
        assertSame(taxonomy, result);
        assertTrue(taxonomy.query.has("taxonomies.color"));
        
        JSONObject colorQuery = taxonomy.query.getJSONObject("taxonomies.color");
        assertTrue(colorQuery.has("$eq_below"));
        assertEquals("blue", colorQuery.getString("$eq_below"));
    }

    @Test
    void testEqualAndBelowWithDifferentTerms() {
        taxonomy.equalAndBelow("taxonomies.category", "electronics");
        
        assertTrue(taxonomy.query.has("taxonomies.category"));
        JSONObject categoryQuery = taxonomy.query.getJSONObject("taxonomies.category");
        assertEquals("electronics", categoryQuery.getString("$eq_below"));
    }

    // ========== EQUAL AND BELOW WITH LEVEL TESTS ==========

    @Test
    void testEqualAndBelowWithLevel() {
        Taxonomy result = taxonomy.equalAndBelowWithLevel("taxonomies.color", "blue", 2);
        
        assertNotNull(result);
        assertSame(taxonomy, result);
        assertTrue(taxonomy.query.has("taxonomies.color"));
        
        JSONObject colorQuery = taxonomy.query.getJSONObject("taxonomies.color");
        assertTrue(colorQuery.has("$eq_below"));
        String value = colorQuery.getString("$eq_below");
        assertTrue(value.contains("blue"));
        assertTrue(value.contains("level: 2"));
    }

    @Test
    void testEqualAndBelowWithLevelZero() {
        taxonomy.equalAndBelowWithLevel("taxonomies.size", "large", 0);
        
        JSONObject sizeQuery = taxonomy.query.getJSONObject("taxonomies.size");
        String value = sizeQuery.getString("$eq_below");
        assertTrue(value.contains("large"));
        assertTrue(value.contains("level: 0"));
    }

    @Test
    void testEqualAndBelowWithLevelNegative() {
        taxonomy.equalAndBelowWithLevel("taxonomies.category", "tech", -1);
        
        JSONObject categoryQuery = taxonomy.query.getJSONObject("taxonomies.category");
        String value = categoryQuery.getString("$eq_below");
        assertTrue(value.contains("tech"));
        assertTrue(value.contains("level: -1"));
    }

    // ========== BELOW OPERATOR TESTS ==========

    @Test
    void testBelow() {
        Taxonomy result = taxonomy.below("taxonomies.color", "blue");
        
        assertNotNull(result);
        assertSame(taxonomy, result);
        assertTrue(taxonomy.query.has("taxonomies.color"));
        
        JSONObject colorQuery = taxonomy.query.getJSONObject("taxonomies.color");
        assertTrue(colorQuery.has("$below"));
        assertEquals("blue", colorQuery.getString("$below"));
    }

    @Test
    void testBelowWithDifferentTerms() {
        taxonomy.below("taxonomies.category", "vehicles");
        
        assertTrue(taxonomy.query.has("taxonomies.category"));
        JSONObject categoryQuery = taxonomy.query.getJSONObject("taxonomies.category");
        assertEquals("vehicles", categoryQuery.getString("$below"));
    }

    // ========== EQUAL ABOVE OPERATOR TESTS ==========

    @Test
    void testEqualAbove() {
        Taxonomy result = taxonomy.equalAbove("taxonomies.appliances", "led");
        
        assertNotNull(result);
        assertSame(taxonomy, result);
        assertTrue(taxonomy.query.has("taxonomies.appliances"));
        
        JSONObject appliancesQuery = taxonomy.query.getJSONObject("taxonomies.appliances");
        assertTrue(appliancesQuery.has("$eq_above"));
        assertEquals("led", appliancesQuery.getString("$eq_above"));
    }

    @Test
    void testEqualAboveWithDifferentTerms() {
        taxonomy.equalAbove("taxonomies.devices", "smartphone");
        
        assertTrue(taxonomy.query.has("taxonomies.devices"));
        JSONObject devicesQuery = taxonomy.query.getJSONObject("taxonomies.devices");
        assertEquals("smartphone", devicesQuery.getString("$eq_above"));
    }

    // ========== ABOVE OPERATOR TESTS ==========

    @Test
    void testAbove() {
        Taxonomy result = taxonomy.above("taxonomies.appliances", "led");
        
        assertNotNull(result);
        assertSame(taxonomy, result);
        assertTrue(taxonomy.query.has("taxonomies.appliances"));
        
        JSONObject appliancesQuery = taxonomy.query.getJSONObject("taxonomies.appliances");
        assertTrue(appliancesQuery.has("$above"));
        assertEquals("led", appliancesQuery.getString("$above"));
    }

    @Test
    void testAboveWithDifferentTerms() {
        taxonomy.above("taxonomies.categories", "subcategory");
        
        assertTrue(taxonomy.query.has("taxonomies.categories"));
        JSONObject categoriesQuery = taxonomy.query.getJSONObject("taxonomies.categories");
        assertEquals("subcategory", categoriesQuery.getString("$above"));
    }

    // ========== METHOD CHAINING TESTS ==========

    @Test
    void testMethodChaining() {
        Taxonomy result = taxonomy
                .in("taxonomies.color", Arrays.asList("red", "blue"))
                .exists("taxonomies.size", true)
                .equalAndBelow("taxonomies.category", "electronics");
        
        assertNotNull(result);
        assertSame(taxonomy, result);
        assertTrue(taxonomy.query.has("taxonomies.color"));
        assertTrue(taxonomy.query.has("taxonomies.size"));
        assertTrue(taxonomy.query.has("taxonomies.category"));
    }

    @Test
    void testComplexMethodChaining() {
        JSONObject orCondition1 = new JSONObject();
        orCondition1.put("taxonomies.color", "yellow");
        
        JSONObject orCondition2 = new JSONObject();
        orCondition2.put("taxonomies.size", "small");
        
        Taxonomy result = taxonomy
                .in("taxonomies.brand", Arrays.asList("nike", "adidas"))
                .or(Arrays.asList(orCondition1, orCondition2))
                .exists("taxonomies.inStock", true)
                .below("taxonomies.category", "sports");
        
        assertNotNull(result);
        assertTrue(taxonomy.query.has("taxonomies.brand"));
        assertTrue(taxonomy.query.has("$or"));
        assertTrue(taxonomy.query.has("taxonomies.inStock"));
        assertTrue(taxonomy.query.has("taxonomies.category"));
    }

    // ========== QUERY BUILDING TESTS ==========

    @Test
    void testQueryStructureAfterIn() {
        taxonomy.in("taxonomies.color", Arrays.asList("red", "blue", "green"));
        
        assertNotNull(taxonomy.query);
        JSONObject colorQuery = taxonomy.query.getJSONObject("taxonomies.color");
        assertNotNull(colorQuery);
        assertTrue(colorQuery.has("$in"));
    }

    @Test
    void testQueryStructureAfterExists() {
        taxonomy.exists("taxonomies.available", true);
        
        JSONObject availableQuery = taxonomy.query.getJSONObject("taxonomies.available");
        assertNotNull(availableQuery);
        assertTrue(availableQuery.getBoolean("$exists"));
    }

    @Test
    void testQueryStructureWithMultipleOperators() {
        taxonomy.in("taxonomies.color", Arrays.asList("red"))
                .exists("taxonomies.size", true)
                .below("taxonomies.category", "clothing");
        
        // All three should be in the query
        assertEquals(3, taxonomy.query.length());
    }

    // ========== MAKE REQUEST TESTS ==========

    @Test
    void testMakeRequestReturnsCall() {
        taxonomy.in("taxonomies.color", Arrays.asList("red"));
        
        assertDoesNotThrow(() -> {
            Object call = taxonomy.makeRequest();
            assertNotNull(call);
        });
    }

    // ========== FIND METHOD TESTS ==========

    @Test
    void testFindWithCallback() {
        taxonomy.in("taxonomies.color", Arrays.asList("red"));
        
        TaxonomyCallback callback = new TaxonomyCallback() {
            @Override
            public void onResponse(JSONObject response, Error error) {
                // Callback implementation
            }
        };
        
        // This will attempt network call - we expect RuntimeException due to network failure
        assertThrows(RuntimeException.class, () -> taxonomy.find(callback));
    }

    // ========== EDGE CASES ==========

    @Test
    void testMultipleInCallsOnSameTaxonomy() {
        taxonomy.in("taxonomies.color", Arrays.asList("red"));
        taxonomy.in("taxonomies.color", Arrays.asList("blue", "green"));
        
        // Second call should overwrite first
        JSONObject colorQuery = taxonomy.query.getJSONObject("taxonomies.color");
        assertNotNull(colorQuery);
    }

    @Test
    void testDifferentOperatorsOnSameTaxonomy() {
        taxonomy.in("taxonomies.category", Arrays.asList("electronics"));
        taxonomy.exists("taxonomies.category", true); // This overwrites the in() call
        
        JSONObject categoryQuery = taxonomy.query.getJSONObject("taxonomies.category");
        assertTrue(categoryQuery.has("$exists"));
        assertFalse(categoryQuery.has("$in"));
    }

    @Test
    void testQueryInitialization() throws Exception {
        // Access protected query field via reflection
        Field queryField = Taxonomy.class.getDeclaredField("query");
        queryField.setAccessible(true);
        JSONObject query = (JSONObject) queryField.get(taxonomy);
        
        assertNotNull(query);
        assertEquals(0, query.length()); // Should be empty initially
    }

    @Test
    void testInWithSpecialCharactersInTaxonomyName() {
        taxonomy.in("taxonomies.product-category_v2", Arrays.asList("item1"));
        
        assertTrue(taxonomy.query.has("taxonomies.product-category_v2"));
    }

    @Test
    void testInWithSpecialCharactersInTerms() {
        taxonomy.in("taxonomies.tags", Arrays.asList("tag-1", "tag_2", "tag.3"));
        
        assertTrue(taxonomy.query.has("taxonomies.tags"));
    }

    @Test
    void testExistsWithMultipleTaxonomies() {
        taxonomy.exists("taxonomies.color", true)
                .exists("taxonomies.size", false)
                .exists("taxonomies.brand", true);
        
        assertEquals(3, taxonomy.query.length());
    }

    @Test
    void testOrAndAndCanCoexist() {
        JSONObject orCondition = new JSONObject();
        orCondition.put("taxonomies.color", "red");
        
        JSONObject andCondition = new JSONObject();
        andCondition.put("taxonomies.size", "large");
        
        taxonomy.or(Arrays.asList(orCondition))
                .and(Arrays.asList(andCondition));
        
        assertTrue(taxonomy.query.has("$or"));
        assertTrue(taxonomy.query.has("$and"));
    }

    @Test
    void testBelowAndEqualAndBelowDifference() {
        // These should create different query structures
        Taxonomy tax1 = new Taxonomy(service, config, headers);
        tax1.below("taxonomies.category", "electronics");
        
        Taxonomy tax2 = new Taxonomy(service, config, headers);
        tax2.equalAndBelow("taxonomies.category", "electronics");
        
        JSONObject query1 = tax1.query.getJSONObject("taxonomies.category");
        JSONObject query2 = tax2.query.getJSONObject("taxonomies.category");
        
        assertTrue(query1.has("$below"));
        assertTrue(query2.has("$eq_below"));
        assertFalse(query1.has("$eq_below"));
        assertFalse(query2.has("$below"));
    }

    @Test
    void testAboveAndEqualAboveDifference() {
        Taxonomy tax1 = new Taxonomy(service, config, headers);
        tax1.above("taxonomies.devices", "smartphone");
        
        Taxonomy tax2 = new Taxonomy(service, config, headers);
        tax2.equalAbove("taxonomies.devices", "smartphone");
        
        JSONObject query1 = tax1.query.getJSONObject("taxonomies.devices");
        JSONObject query2 = tax2.query.getJSONObject("taxonomies.devices");
        
        assertTrue(query1.has("$above"));
        assertTrue(query2.has("$eq_above"));
        assertFalse(query1.has("$eq_above"));
        assertFalse(query2.has("$above"));
    }

    @Test
    void testQueryToStringContainsAllOperators() {
        taxonomy.in("taxonomies.color", Arrays.asList("red"))
                .exists("taxonomies.size", true)
                .below("taxonomies.category", "clothing");
        
        String queryString = taxonomy.query.toString();
        
        assertNotNull(queryString);
        assertTrue(queryString.length() > 0);
        // Verify it's valid JSON
        assertDoesNotThrow(() -> new JSONObject(queryString));
    }
}

