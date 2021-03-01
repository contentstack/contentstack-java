package com.contentstack.sdk;

import io.github.cdimascio.dotenv.Dotenv;
import junit.framework.TestCase;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.logging.Logger;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EntryTestCase {

    private static final Logger logger = Logger.getLogger(EntryTestCase.class.getName());
    private static String entryUID = null;
    private static final String CONTENT_TYPE = "product";
    private static Stack stack;


    @BeforeClass
    public static void oneTimeSetUp() throws Exception {
        // Loading credentials
        Dotenv dotenv = Dotenv.load();
        String DEFAULT_API_KEY = dotenv.get("API_KEY");
        String DEFAULT_DELIVERY_TOKEN = dotenv.get("DELIVERY_TOKEN");
        String DEFAULT_ENV = dotenv.get("ENVIRONMENT");
        String DEFAULT_HOST = dotenv.get("HOST");
        Config config = new Config();
        config.setHost(DEFAULT_HOST);

        assert DEFAULT_API_KEY != null;
        stack = Contentstack.stack(DEFAULT_API_KEY, DEFAULT_DELIVERY_TOKEN, DEFAULT_ENV, config);
        final Query query = stack.contentType(CONTENT_TYPE).query();
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    entryUID = queryresult.getResultObjects().get(15).getUid();
                    System.out.println(entryUID);
                }
            }
        });
    }


    @Test
    public void test_01_findAllEntries() {
        final Query query = stack.contentType(CONTENT_TYPE).query();
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    entryUID = queryresult.getResultObjects().get(15).getUid();
                }
            }
        });
    }

    @Test
    public void test_02_only_fetch() {
        final Entry entry = stack.contentType(CONTENT_TYPE).entry(entryUID);
        entry.only(new String[]{"price"});
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    assertEquals(786, entry.toJSON().get("price"));
                }
            }
        });
    }

    @Test
    public void test_03_except_fetch() {
        final Entry entry = stack.contentType(CONTENT_TYPE).entry(entryUID);
        entry.except(new String[]{"title"});
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    assertFalse(entry.toJSON().has("title"));
                } else {
                    assertEquals(422, error.getErrorCode());
                }
            }
        });
    }

    @Test
    public void test_04_includeReference_fetch() {
        final Entry entry = stack.contentType(CONTENT_TYPE).entry(entryUID);
        entry.includeReference(new String[]{"brand","categories"});
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
    public void test_05_includeReferenceOnly_fetch() {
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
                    assertEquals("laptop", entry.toJSON().getString("title"));
                }
            }
        });

    }


    @Test
    public void test_06_includeReferenceExcept_fetch() {
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
    public void test_07_getMarkdown_fetch()  {
        final Entry entry = stack.contentType("user").entry(entryUID);
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                logger.info(entry.getTitle());
            }
        });
    }


    @Test
    public void test_08_get() {
        final Entry entry = stack.contentType("user").entry(entryUID);
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                logger.info(entry.getTitle());
            }
        });
    }


    @Test
    public void test_09_getParam() {
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
    public void test_10_IncludeReferenceContentTypeUID()  {
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
    public void test_11_Locale()  {
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
    public void test_12_entry_except()  {
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
    public void test_13_entry_include_fallback() {
        final Entry entry = stack.contentType("categories").entry(entryUID).setLocale("hi-in");
        entry.setLocale("");
        entry.includeFallback().fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    String checkResp = entry.getLocale();
                    assertEquals("en-us", checkResp);
                }
                TestCase.assertTrue(entry.otherPostJSON.has("include_fallback"));
            }
        });
    }

}
