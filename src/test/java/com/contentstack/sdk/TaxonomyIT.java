package com.contentstack.sdk;

import okhttp3.Request;
import okhttp3.ResponseBody;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


public class TaxonomyIT {

    private final Stack stack = Credentials.getStack();
    private final String host = Credentials.HOST;

    @Test
    void testInstance() {
        Assertions.assertNotNull(stack);
    }

    @Test
    void operationIn() {
        Taxonomy taxonomy = stack.taxonomy();
        List<String> listOfItems = new ArrayList<>();
        listOfItems.add("red");
        listOfItems.add("yellow");
        Request req = taxonomy.in("taxonomies.color", listOfItems).makeRequest().request();
        //Assertions.assertEquals(3, req.headers().size());
        Assertions.assertEquals("GET", req.method());
        Assertions.assertEquals(host, req.url().host());
        Assertions.assertEquals("/v3/taxonomies/entries", req.url().encodedPath());
        Assertions.assertEquals("query={\"taxonomies.color\":{\"$in\":[\"red\",\"yellow\"]}}", req.url().query());
    }


    @Test
    void operationOr() {

//        query={ $or: [
//        { "taxonomies.taxonomy_uid_1" : "term_uid1" },
//        { "taxonomies.taxonomy_uid_2" : "term_uid2" }
//        ]}

        Taxonomy taxonomy = stack.taxonomy();

        List<JSONObject> listOfItems = new ArrayList<>();
        JSONObject item1 = new JSONObject();
        item1.put("taxonomies.color", "yellow");
        JSONObject item2 = new JSONObject();
        item2.put("taxonomies.size", "small");
        listOfItems.add(item1);
        listOfItems.add(item2);
        taxonomy.or(listOfItems);
        Request req = taxonomy.makeRequest().request();

        Assertions.assertEquals("query={\"$or\":[{\"taxonomies.color\":\"yellow\"},{\"taxonomies.size\":\"small\"}]}", req.url().query());

    }

    @Test
    void operatorAnd() {
        Taxonomy taxonomy = stack.taxonomy();
        List<JSONObject> listOfItems = new ArrayList<>();
        JSONObject items1 = new JSONObject();
        items1.put("taxonomies.color", "green");
        JSONObject items2 = new JSONObject();
        items2.put("taxonomies.computers", "laptop");
        listOfItems.add(items1);
        listOfItems.add(items2);
        taxonomy.and(listOfItems);

        // {$and: [{"taxonomies.color" : "green" }, { "taxonomies.computers" : "laptop" }]}
        Request req = taxonomy.makeRequest().request();
        Assertions.assertEquals("query={\"$and\":\"[{\\\"taxonomies.color\\\":\\\"green\\\"}, {\\\"taxonomies.computers\\\":\\\"laptop\\\"}]\"}", req.url().query());
    }


    @Test
    void operationExists() {
        // create instance of taxonomy
        Taxonomy taxonomy = stack.taxonomy().exists("taxonomies.color", true);
        Request req = taxonomy.makeRequest().request();
        //{"taxonomies.color" : { "$exists": true }}
        //{"taxonomies.color":{"$exists":true}}
        Assertions.assertEquals("query={\"taxonomies.color\":{\"$exists\":true}}", req.url().query());
    }


    @Test
    void operationEqualAndBelow() {
        // create instance of taxonomy
        Taxonomy taxonomy = stack.taxonomy().equalAndBelow("taxonomies.color", "blue");
        Request req = taxonomy.makeRequest().request();
        // {"taxonomies.color" : { "$eq_below": "blue" }}
        Assertions.assertEquals("query={\"taxonomies.color\":{\"$eq_below\":\"blue\"}}", req.url().query());
    }


    @Test
    void operationBelowWithLevel() {
        Taxonomy taxonomy = stack.taxonomy().equalAndBelowWithLevel("taxonomies.color", "blue", 3);
        Request req = taxonomy.makeRequest().request();
        Assertions.assertEquals("query={\"taxonomies.color\":{\"$eq_below\":\"blue, level: 3\"}}", req.url().query());

    }


    @Test
    void operationEqualAbove() {
        Taxonomy taxonomy = stack.taxonomy().equalAbove("taxonomies.appliances", "led");
        Request req = taxonomy.makeRequest().request();
        Assertions.assertEquals("query={\"taxonomies.appliances\":{\"$eq_above\":\"led\"}}", req.url().query());

    }


    @Test
    void above() {
        Taxonomy taxonomy = stack.taxonomy().above("taxonomies.appliances", "led");
        Request req = taxonomy.makeRequest().request();
        Assertions.assertEquals("query={\"taxonomies.appliances\":{\"$above\":\"led\"}}", req.url().query());
    }

    @Test
    void aboveAPI() {
        Taxonomy taxonomy = stack.taxonomy().above("taxonomies.appliances", "led");
        taxonomy.find((response, error) -> {
            System.out.println("Successful: " + response);
            System.out.println("Error: " + error.errorMessage);
        });
        //Assertions.assertEquals("query={\"taxonomies.appliances\":{\"$above\":\"led\"}}", req.url().query());
    }

    @Test
    void testTaxonomyInWithSingleItem() {
        Taxonomy taxonomy = stack.taxonomy();
        List<String> listOfItems = new ArrayList<>();
        listOfItems.add("blue");
        Request req = taxonomy.in("taxonomies.color", listOfItems).makeRequest().request();
        
        Assertions.assertEquals("GET", req.method());
        Assertions.assertEquals(host, req.url().host());
        Assertions.assertEquals("/v3/taxonomies/entries", req.url().encodedPath());
        Assertions.assertTrue(req.url().query().contains("$in"));
        Assertions.assertTrue(req.url().query().contains("blue"));
    }

    @Test
    void testTaxonomyBelow() {
        Taxonomy taxonomy = stack.taxonomy().below("taxonomies.category", "electronics");
        Request req = taxonomy.makeRequest().request();
        Assertions.assertEquals("query={\"taxonomies.category\":{\"$below\":\"electronics\"}}", req.url().query());
    }

    @Test
    void testTaxonomyMultipleOperations() {
        Taxonomy taxonomy = stack.taxonomy();
        List<String> colors = new ArrayList<>();
        colors.add("red");
        colors.add("blue");
        taxonomy.in("taxonomies.color", colors);
        taxonomy.exists("taxonomies.size", true);
        
        Request req = taxonomy.makeRequest().request();
        String query = req.url().query();
        Assertions.assertTrue(query.contains("taxonomies.color"));
        Assertions.assertTrue(query.contains("$in"));
    }

    @Test
    void testTaxonomyWithEmptyList() {
        Taxonomy taxonomy = stack.taxonomy();
        List<String> emptyList = new ArrayList<>();
        Request req = taxonomy.in("taxonomies.tags", emptyList).makeRequest().request();
        
        Assertions.assertEquals("GET", req.method());
        Assertions.assertNotNull(req.url().query());
    }

    @Test
    void testTaxonomyEqualAndBelowMultipleLevels() {
        Taxonomy taxonomy = stack.taxonomy();
        taxonomy.equalAndBelowWithLevel("taxonomies.hierarchy", "root", 5);
        Request req = taxonomy.makeRequest().request();
        
        String query = req.url().query();
        Assertions.assertTrue(query.contains("taxonomies.hierarchy"));
        Assertions.assertTrue(query.contains("$eq_below"));
        Assertions.assertTrue(query.contains("5"));
    }

    @Test
    void testTaxonomyRequestHeaders() {
        Taxonomy taxonomy = stack.taxonomy().exists("taxonomies.featured", true);
        Request req = taxonomy.makeRequest().request();
        
        Assertions.assertNotNull(req.headers());
        Assertions.assertTrue(req.headers().size() > 0);
    }

    @Test
    void testTaxonomyUrlEncoding() {
        Taxonomy taxonomy = stack.taxonomy().equalAndBelow("taxonomies.name", "test value");
        Request req = taxonomy.makeRequest().request();
        
        Assertions.assertNotNull(req.url().encodedQuery());
        Assertions.assertTrue(req.url().toString().contains("taxonomies"));
    }

    @Test
    void testTaxonomyComplexQuery() {
        Taxonomy taxonomy = stack.taxonomy();
        
        List<String> colors = new ArrayList<>();
        colors.add("red");
        colors.add("blue");
        taxonomy.in("taxonomies.color", colors);
        
        taxonomy.exists("taxonomies.featured", true);
        taxonomy.equalAndBelow("taxonomies.category", "electronics");
        
        Request req = taxonomy.makeRequest().request();
        String query = req.url().query();
        
        Assertions.assertNotNull(query);
        Assertions.assertFalse(query.isEmpty());
    }

    @Test
    void testTaxonomyOrWithMultipleConditions() {
        Taxonomy taxonomy = stack.taxonomy();
        
        List<JSONObject> conditions = new ArrayList<>();
        
        JSONObject cond1 = new JSONObject();
        cond1.put("taxonomies.type", "article");
        
        JSONObject cond2 = new JSONObject();
        cond2.put("taxonomies.status", "published");
        
        JSONObject cond3 = new JSONObject();
        cond3.put("taxonomies.featured", true);
        
        conditions.add(cond1);
        conditions.add(cond2);
        conditions.add(cond3);
        
        taxonomy.or(conditions);
        Request req = taxonomy.makeRequest().request();
        
        String query = req.url().query();
        Assertions.assertTrue(query.contains("$or"));
        Assertions.assertTrue(query.contains("taxonomies.type"));
        Assertions.assertTrue(query.contains("taxonomies.status"));
    }


}
