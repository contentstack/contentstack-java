package com.contentstack.sdk;

import okhttp3.Request;
import okhttp3.ResponseBody;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class TaxonomyTest {

    private final Stack stack = Credentials.getStack();

    @Test
    void testInstance() {
        Assertions.assertNotNull(stack);
    }

    @Test
    void testInOperator() {
        HashMap<String, Object> query = new HashMap<>();
        query.put("taxonomies.taxonomy_uid", new HashMap<String, Object>() {{
            put("$in", new String[]{"term_uid1", "term_uid2"});
        }});

        stack.taxonomy().query(query).find(new TaxonomyCallback() {
            @Override
            public void onFailure(Request request, ResponseBody errorMessage) {
                System.out.println("Failing API call : " + errorMessage.toString());

            }

            @Override
            public void onResponse(ResponseBody response) {
                System.out.println("Response : " + response.toString());

            }
        });
    }


    @Test
    void testUnitInOperator() {
        HashMap<String, Object> query = new HashMap<>();
        query.put("taxonomies.taxonomy_uid", new HashMap<String, Object>() {{
            put("$in", new String[]{"term_uid1", "term_uid2"});
        }});
        Taxonomy taxonomy = stack.taxonomy();
        taxonomy.query(query);
        Request req = taxonomy.makeRequest().request();
        Assertions.assertEquals(3, req.headers().size());
        Assertions.assertEquals("GET", req.method().toString(), "test method are being passed though payload");
        Assertions.assertEquals("cdn.contentstack.io", req.url().host());
        Assertions.assertNull(req.url().encodedFragment(), "We do not expect any fragment");
        Assertions.assertEquals("/v3/taxonomies/entries", req.url().encodedPath());
        Assertions.assertEquals(3, Arrays.stream(req.url().encodedPathSegments().stream().toArray()).count());
        Assertions.assertNotNull(req.url().query());
        Assertions.assertNotNull(req.url().encodedQuery());
        Assertions.assertEquals("[query]", req.url().queryParameterNames().toString());
    }

    @Test
    void testUnitINOperator() {
        Taxonomy taxonomy = stack.taxonomy();
        String key = "taxonomies.taxonomy_uid";
        String[] listOfItem = {"term_uid1", "term_uid2"};
        taxonomy.in(key, listOfItem);
        Request req = taxonomy.makeRequest().request();
        Assertions.assertEquals(3, req.headers().size());
        Assertions.assertEquals("GET", req.method().toString(), "test method are being passed though payload");
        Assertions.assertEquals("cdn.contentstack.io", req.url().host());
        Assertions.assertNull(req.url().encodedFragment(), "We do not expect any fragment");
        Assertions.assertEquals("/v3/taxonomies/entries", req.url().encodedPath());
        Assertions.assertEquals(3, Arrays.stream(req.url().encodedPathSegments().stream().toArray()).count());
        Assertions.assertNotNull(req.url().query());
        Assertions.assertNotNull(req.url().encodedQuery());
        Assertions.assertEquals("[query]", req.url().queryParameterNames().toString());
    }


    @Test
    void testUnitOr() {
        Taxonomy taxonomy = stack.taxonomy();

        List<HashMap<String, String>> listOfItems = new ArrayList<>();
        HashMap<String, String> items = new HashMap<>();
        items.put("taxonomies.taxonomy_uid_1", "term_uid1");
        items.put("taxonomies.taxonomy_uid_2", "term_uid2");
        listOfItems.add(items);

        taxonomy.or(listOfItems);
        Request req = taxonomy.makeRequest().request();
        Assertions.assertEquals(3, req.headers().size());
        Assertions.assertEquals("GET", req.method().toString(), "test method are being passed though payload");
        Assertions.assertEquals("cdn.contentstack.io", req.url().host());
        Assertions.assertNull(req.url().encodedFragment(), "We do not expect any fragment");
        Assertions.assertEquals("/v3/taxonomies/entries", req.url().encodedPath());
        Assertions.assertEquals(3, Arrays.stream(req.url().encodedPathSegments().stream().toArray()).count());
        Assertions.assertNotNull(req.url().query());
        Assertions.assertNotNull(req.url().encodedQuery());
        Assertions.assertEquals("[query]", req.url().queryParameterNames().toString());
    }


}
