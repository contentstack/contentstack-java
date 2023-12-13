package com.contentstack.sdk;

import okhttp3.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
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
            public void onResponse(Request request, ResponseBody response) {
                System.out.println("Response : "+response.toString());

            }
        });
    }


//    @Test
//    void testTaxonomyIn() {
//        String[] value = {"term_uid1", "term_uid2"}; // ["term_uid1" , "term_uid2" ]
//
//        Request request = taxonomy.inOperator("taxonomies.taxonomy_uid", value).find().request();
//        taxonomy.find(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                call.request().url();
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });
//        Assertions.assertEquals(3, request.headers().size());
//        Assertions.assertEquals("GET", request.method().toString(), "test method are being passed though payload");
//        Assertions.assertEquals("cdn.contentstack.io", request.url().host());
//        Assertions.assertNull(request.url().encodedFragment(), "We do not expect any fragment");
//        Assertions.assertEquals("/taxonomies/entries", request.url().encodedPath());
//        Assertions.assertEquals(2, Arrays.stream(request.url().encodedPathSegments().stream().toArray()).count());
//        Assertions.assertEquals("", request.url().query());
//        Assertions.assertEquals("", request.url().encodedQuery());
//        Assertions.assertEquals("", request.url().queryParameterNames());
//        Assertions.assertEquals("", request.url(), "test url are being passed though payload");
//        Assertions.assertEquals("", request.body().contentType().toString(), "test content type are being passed though payload");
//    }


}
