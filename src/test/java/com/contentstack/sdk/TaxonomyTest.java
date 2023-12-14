package com.contentstack.sdk;

import okhttp3.HttpUrl;
import okhttp3.Request;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TaxonomyTest {

    private final Stack stack = Credentials.getStack();

    @Test
    void testInstance() {
        Assertions.assertNotNull(stack);
    }

    @Test
    void operationIn() {
        Taxonomy taxonomy = stack.taxonomy();
        String[] listOfItem = {"red", "yellow"};
        Request req = taxonomy.in("taxonomies.color", listOfItem).makeRequest().request();

        Assertions.assertEquals(3, req.headers().size());
        Assertions.assertEquals("GET", req.method());
        Assertions.assertEquals("cdn.contentstack.io", req.url().host());
        Assertions.assertEquals("/v3/taxonomies/entries", req.url().encodedPath());
        Assertions.assertEquals("[query]", req.url().queryParameterNames().toString());
    }


    @Test
    void operationOr() {
        Taxonomy taxonomy = stack.taxonomy();

        List<HashMap<String, String>> listOfItems = new ArrayList<>();
        HashMap<String, String> items = new HashMap<>();
        items.put("taxonomies.taxonomy_uid_1", "term_uid1");
        items.put("taxonomies.taxonomy_uid_2", "term_uid2");
        listOfItems.add(items);
        taxonomy.or(listOfItems);
        Request req = taxonomy.makeRequest().request();

        Assertions.assertEquals("[query]", req.url().queryParameterNames().toString());

    }

    public static String decodeUrl(HttpUrl encodedUrl) {
        String decodedUrl = null;
        try {
            decodedUrl = URLDecoder.decode(encodedUrl.toString(), StandardCharsets.UTF_8.toString());
            return decodedUrl;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace(); // Handle the exception according to your requirements
        }
        return decodedUrl;
    }

    @Test
    void operatorAnd() {
        Taxonomy taxonomy = stack.taxonomy();
        List<HashMap<String, String>> listOfItems = new ArrayList<>();
        HashMap<String, String> items = new HashMap<>();
        items.put("taxonomies.taxonomy_uid_1", "term_uid1");
        items.put("taxonomies.taxonomy_uid_2", "term_uid2");
        listOfItems.add(items);
        taxonomy.and(listOfItems);

        Request req = taxonomy.makeRequest().request();
    }


    @Test
    void operationExists() {
        // create instance of taxonomy
        Taxonomy taxonomy = stack.taxonomy().exists("listOfItems", true);
        Request req = taxonomy.makeRequest().request();
        Assertions.assertEquals("query={$or={taxonomies.taxonomy_uid_2=term_uid2, taxonomies.taxonomy_uid_1=term_uid1}}", req.url().query());
    }


    @Test
    void operationEqualAndBelow() {
        // create instance of taxonomy
        Taxonomy taxonomy = stack.taxonomy().equalAndBelow("listOfItems", "uid1");
        Request req = taxonomy.makeRequest().request();
        Assertions.assertEquals("query={$or={taxonomies.taxonomy_uid_2=term_uid2, taxonomies.taxonomy_uid_1=term_uid1}}", req.url().query());
    }


    @Test
    void operationBelow() {
        Taxonomy taxonomy = stack.taxonomy().equalAndBelowWithLevel("listOfItems", "uid1", 3);
        Request req = taxonomy.makeRequest().request();
        Assertions.assertEquals("query={$or={taxonomies.taxonomy_uid_2=term_uid2, taxonomies.taxonomy_uid_1=term_uid1}}", req.url().query());

    }


    @Test
    void operationEqualAbove() {
        Taxonomy taxonomy = stack.taxonomy().equalAbove("listOfItems", "uid1");
        Request req = taxonomy.makeRequest().request();
        Assertions.assertEquals("query={$or={taxonomies.taxonomy_uid_2=term_uid2, taxonomies.taxonomy_uid_1=term_uid1}}", req.url().query());

    }


    @Test
    void above() {
        Taxonomy taxonomy = stack.taxonomy().above("listOfItems", "uid1");
        Request req = taxonomy.makeRequest().request();
        Assertions.assertEquals("query={$or={taxonomies.taxonomy_uid_2=term_uid2, taxonomies.taxonomy_uid_1=term_uid1}}", req.url().query());

    }


}
