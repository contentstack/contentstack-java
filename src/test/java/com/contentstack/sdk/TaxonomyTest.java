package com.contentstack.sdk;

import okhttp3.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;


public class TaxonomyTest {

    private final Taxonomy taxonomy = Credentials.getStack().taxonomy();

    @Test
    void testInstance() {
        Assertions.assertNotNull(taxonomy);
    }

    @Test
    void testInOperator() {
        HashMap<String, Object> query = new HashMap<>();
        query.put("taxonomies.taxonomy_uid", new HashMap<String, Object>() {{
            put("$in", new String[]{"term_uid1", "term_uid2"});
        }});

        taxonomy.query(query).find(new TaxonomyCallback() {
            @Override
            public void onFailure(Request request, ResponseBody errorMessage) {
                System.out.println("Failing API call : "+errorMessage.toString());

            }

            @Override
            public void onResponse(ResponseBody response) {
                System.out.println("Response : "+response.toString());

            }
        });
    }


    @Test
    void testUnitInOperator() {
        HashMap<String, Object> query = new HashMap<>();
        query.put("taxonomies.taxonomy_uid", new HashMap<String, Object>() {{
            put("$in", new String[]{"term_uid1", "term_uid2"});
        }});

        taxonomy.query(query);
        Request req = taxonomy.makeRequest().request();
        Assertions.assertEquals(3, req.headers().size());
        Assertions.assertEquals("GET", req.method().toString(), "test method are being passed though payload");
        Assertions.assertEquals("cdn.contentstack.io", req.url().host());
        Assertions.assertNull(req.url().encodedFragment(), "We do not expect any fragment");
        Assertions.assertEquals("/v3/taxonomies/entries", req.url().encodedPath());
        Assertions.assertEquals(3, Arrays.stream(req.url().encodedPathSegments().stream().toArray()).count());
        Assertions.assertNotNull( req.url().query());
        Assertions.assertNotNull(req.url().encodedQuery());
        Assertions.assertEquals("[query]", req.url().queryParameterNames().toString());
    }

    @Test
    void testUnitOrOperator() {
        HashMap<String, Object> query = new HashMap<>();
        query.put("taxonomies.taxonomy_uid", new HashMap<String, Object>() {{
            put("$in", new String[]{"term_uid1", "term_uid2"});
        }});

        taxonomy.query(query);
        Request req = taxonomy.makeRequest().request();
        Assertions.assertEquals(3, req.headers().size());
        Assertions.assertEquals("GET", req.method().toString(), "test method are being passed though payload");
        Assertions.assertEquals("cdn.contentstack.io", req.url().host());
        Assertions.assertNull(req.url().encodedFragment(), "We do not expect any fragment");
        Assertions.assertEquals("/v3/taxonomies/entries", req.url().encodedPath());
        Assertions.assertEquals(3, Arrays.stream(req.url().encodedPathSegments().stream().toArray()).count());
        Assertions.assertNotNull( req.url().query());
        Assertions.assertNotNull(req.url().encodedQuery());
        Assertions.assertEquals("[query]", req.url().queryParameterNames().toString());
    }







}
