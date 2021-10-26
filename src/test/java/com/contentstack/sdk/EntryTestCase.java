package com.contentstack.sdk;

import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertTrue;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EntryTestCase {

    private final Logger logger = Logger.getLogger(EntryTestCase.class.getName());
    private final String CONTENT_TYPE = "product";
    private String entryUID = "justFakeIt";
    private Stack stack;


    @BeforeAll
    public void oneTimeSetUp() throws Exception {
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
    void veryFirstTest() {
        final Query query = stack.contentType(CONTENT_TYPE).query();
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    entryUID = queryresult.getResultObjects().get(15).getUid();
                    assertTrue(entryUID.startsWith("blt"));
                } else {
                    Assertions.fail("Could not fetch the query data");
                }
            }
        });
    }


    @Test
    void testOnly() {
        final Entry entry = stack.contentType(CONTENT_TYPE).entry(entryUID);
        entry.only(new String[]{"price"});
        entry.fetch(null);
        Assertions.assertEquals(786, entry.otherPostJSON);
    }

    @Test
    void testExceptFetch() {
        final Entry entry = stack.contentType(CONTENT_TYPE).entry(entryUID);
        entry.except(new String[]{"title"});
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    Assertions.assertFalse(entry.toJSON().has("title"));
                } else {
                    Assertions.assertEquals(422, error.getErrorCode());
                }
            }
        });
    }

    @Test
    void testIncludeReference() {
        final Entry entry = stack.contentType(CONTENT_TYPE).entry(entryUID);
        entry.includeReference(new String[]{"brand", "categories"});
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    JSONArray categoryArray = entry.getJSONArray("category");
                    categoryArray.forEach(object -> assertTrue(object.toString().contains("_content_type_uid")));
                }
            }
        });
    }

    @Test
    void testIncludeReferenceOnly() {
        final Entry entry = stack.contentType(CONTENT_TYPE).entry(entryUID);
        ArrayList<String> strings = new ArrayList<>();
        strings.add("title");
        strings.add("orange");
        strings.add("mango");
        entry.onlyWithReferenceUid(strings, "category");
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    Assertions.assertEquals("laptop", entry.toJSON().getString("title"));
                }
            }
        });

    }


    @Test
    void testIncludeReferenceExcept() {
        final Entry entry = stack.contentType(CONTENT_TYPE).entry(entryUID);
        ArrayList<String> strings = new ArrayList<>();
        strings.add("color");
        strings.add("price_in_usd");
        entry.exceptWithReferenceUid(strings, "category");
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                logger.info(entry.getTitle());
            }
        });
    }


    @Test
    void testGetMarkdown() {
        final Entry entry = stack.contentType("user").entry(entryUID);
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                logger.info(entry.getTitle());
            }
        });
    }


    @Test
    void testGet() {
        final Entry entry = stack.contentType("user").entry(entryUID);
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                logger.info(entry.getTitle());
            }
        });
    }


    @Test
    void testGetParam() {
        final Entry entry = stack.contentType("user").entry(entryUID);
        entry.addParam("include_dimensions", "true");
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                logger.info(entry.getTitle());
            }
        });
    }


    @Test
    void testIncludeReferenceContentTypeUID() {
        final Entry entry = stack.contentType("user").entry(entryUID);
        entry.includeReferenceContentTypeUID();
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    JSONObject jsonResult = entry.toJSON();
                    try {
                        JSONArray cartList = (JSONArray) jsonResult.get("cart");
                        Object whatTYPE = cartList.get(0);
                        if (whatTYPE instanceof JSONObject) {
                            assertTrue(true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    @Test
    void testLocale() {
        final Entry entry = stack.contentType("user").entry(entryUID);
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    String checkResp = entry.getLocale();
                    logger.warning(checkResp);
                }
            }
        });
    }

    @Test
    void testEntryExcept() {
        final Entry entry = stack.contentType("user").entry(entryUID);
        String[] allValues = {"color", "price_in_usd"};
        entry.except(allValues);
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    String checkResp = entry.getLocale();
                    logger.warning(checkResp);
                }
            }
        });
    }


    @Test
    void testEntryIncludeFallback() {
        final Entry entry = stack.contentType("categories").entry(entryUID).setLocale("hi-in");
        entry.setLocale("");
        entry.includeFallback().fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    String checkResp = entry.getLocale();
                    Assertions.assertEquals("en-us", checkResp);
                }
                assertTrue(entry.otherPostJSON.has("include_fallback"));
            }
        });
    }

    @Test
    void testEntryIncludeEmbeddedItems() {
        final Entry entry = stack.contentType("categories").entry(entryUID);
        entry.includeEmbeddedItems().fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    boolean _embedded_items = entry.toJSON().has("_embedded_items");
                    assertTrue(_embedded_items);
                }
                assertTrue(entry.otherPostJSON.has("include_embedded_items[]"));
            }
        });
    }

}
