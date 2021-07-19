package com.contentstack.sdk;

import io.github.cdimascio.dotenv.Dotenv;
import junit.framework.TestCase;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static junit.framework.TestCase.assertEquals;


/**
 * The type Query test case.
 */
public class QueryTestCase {

    private static final Logger logger = Logger.getLogger(AssetTestCase.class.getName());
    private static Stack stack;
    private static Query query, queryFallback;

    /**
     * One time set up.
     *
     * @throws Exception the exception
     */
    @BeforeClass
    public static void oneTimeSetUp() throws Exception {
        logger.setLevel(Level.FINE);
        Dotenv dotenv = Dotenv.load();
        String DEFAULT_API_KEY = dotenv.get("API_KEY");
        String DEFAULT_DELIVERY_TOKEN = dotenv.get("DELIVERY_TOKEN");
        String DEFAULT_ENV = dotenv.get("ENVIRONMENT");
        String DEFAULT_HOST = dotenv.get("HOST");
        Config config = new Config();
        config.setHost(DEFAULT_HOST);
        assert DEFAULT_API_KEY != null;
        stack = Contentstack.stack(DEFAULT_API_KEY, DEFAULT_DELIVERY_TOKEN, DEFAULT_ENV, config);
        logger.info("Asset Test Case Running...");
        query = stack.contentType("product").query();
        queryFallback = stack.contentType("categories").query();
        logger.info("test started...");
    }

    /**
     * One time tear down.
     */
    @AfterClass()
    public static void oneTimeTearDown() {
        logger.info("When all the test cases of class finishes...");
    }

    /**
     * Sets up the test fixture.
     * (Called before every test case method.)
     */
    @BeforeClass()
    public static void setUp() {
        query = stack.contentType("product").query();
    }


    /**
     * Tears down the test fixture.
     * (Called after every test case method.)
     */
    @After()
    public void tearDown() {
        logger.info("Runs after every testcase completes.");
    }


    /**
     * Test 01 fetch all entries.
     */
    @Test()
    public void test_01_fetchAllEntries() {
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    logger.fine(queryresult.toString());
                }
            }
        });
    }

    /**
     * Test 02 fetch single entry.
     */
    @Test()
    public void test_02_fetchSingleEntry() {
        Query query = stack.contentType("categories").query();
        query.where("title", "Women");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> titles = queryresult.getResultObjects();
                    titles.forEach(title -> logger.info("title: " + title.getString("title")));
                }
            }
        });
    }


    /**
     * Test 03 fetch single non existing entry.
     */
    @Test()
    public void test_03_fetchSingleNonExistingEntry() {
        Query query = stack.contentType("categories").query();
        query.where("uid", "blta3b58d6893d8935b");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> listOfEntries = queryresult.getResultObjects();
                    logger.finest(listOfEntries.toString());
                }
            }
        });
    }


    /**
     * Test 04 fetch entry with include reference.
     */
    @Test
    public void test_04_fetchEntryWithIncludeReference() {
        query.includeReference("category");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> listOfEntries = queryresult.getResultObjects();
                    logger.fine(listOfEntries.toString());
                }
            }
        });
    }


    /**
     * Test 05 fetch entry not contained in field.
     */
    @Test
    public void test_05_fetchEntryNotContainedInField() {
        String[] containArray = new String[]{"Roti Maker", "kids dress"};
        query.notContainedIn("title", containArray);
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    entries.forEach(entry -> logger.info(entry.getString("price")));
                }
            }
        });
    }


    /**
     * Test 06 fetch entry contained in field.
     */
    @Test
    public void test_06_fetchEntryContainedInField() {
        String[] containArray = new String[]{"Roti Maker", "kids dress"};
        query.containedIn("title", containArray);
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    entries.forEach(entry -> logger.info(entry.getString("price")));
                }
            }
        });
    }


    /**
     * Test 07 fetch entry not equal to field.
     */
    @Test
    public void test_07_fetchEntryNotEqualToField() {
        query.notEqualTo("title", "yellow t shirt");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    entries.forEach(entry -> logger.info(entry.getString("title")));
                }
            }
        });
    }


    /**
     * Test 08 fetch entry greater than equal to field.
     */
    @Test
    public void test_08_fetchEntryGreaterThanEqualToField() {
        query.greaterThanOrEqualTo("price", 90);
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    entries.forEach(entry -> logger.info(entry.getString("price")));
                }
            }
        });
    }


    /**
     * Test 09 fetch entry greater than field.
     */
    @Test
    public void test_09_fetchEntryGreaterThanField() {
        query.greaterThan("price", 90);
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    entries.forEach(entry -> logger.info(entry.getString("price")));
                }
            }
        });
    }


    /**
     * Test 10 fetch entry less than equal field.
     */
    @Test
    public void test_10_fetchEntryLessThanEqualField() {
        query.lessThanOrEqualTo("price", 90);
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    entries.forEach(entry -> logger.info(entry.getString("price")));
                }
            }
        });
    }


    /**
     * Test 11 fetch entry less than field.
     */
    @Test
    public void test_11_fetchEntryLessThanField() {
        query.lessThan("price", "90");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> resp = queryresult.getResultObjects();
                    resp.forEach(entry -> logger.info("Is price less than 90..? " + entry.get("price")));
                }
            }
        });
    }


    /**
     * Test 12 fetch entries with or.
     */
    @Test
    public void test_12_fetchEntriesWithOr() {

        ContentType ct = stack.contentType("product");
        Query orQuery = ct.query();

        Query query = ct.query();
        query.lessThan("price", 90);

        Query subQuery = ct.query();
        subQuery.containedIn("discount", new Integer[]{20, 45});

        ArrayList<Query> array = new ArrayList<>();
        array.add(query);
        array.add(subQuery);

        orQuery.or(array);

        orQuery.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> listOfEntries = queryresult.getResultObjects();
                    logger.fine(listOfEntries.toString());
                }
            }
        });
    }


    /**
     * Test 13 fetch entries with and.
     */
    @Test
    public void test_13_fetchEntriesWithAnd() {

        ContentType ct = stack.contentType("product");
        Query orQuery = ct.query();

        Query query = ct.query();
        query.lessThan("price", 90);

        Query subQuery = ct.query();
        subQuery.containedIn("discount", new Integer[]{20, 45});

        ArrayList<Query> array = new ArrayList<>();
        array.add(query);
        array.add(subQuery);

        orQuery.and(array);
        orQuery.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> listOfEntries = queryresult.getResultObjects();
                    logger.fine(listOfEntries.toString());
                }
            }
        });
    }


    /**
     * Test 14 add query.
     */
    @Test
    public void test_14_addQuery() {
        query.addQuery("limit", "8");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> listOfEntries = queryresult.getResultObjects();
                    logger.finest(listOfEntries.toString());
                }
            }
        });
    }


    /**
     * Test 15 remove query from query.
     */
    @Test
    public void test_15_removeQueryFromQuery() {
        query.addQuery("limit", "8");
        query.removeQuery("limit");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> listOfEntries = queryresult.getResultObjects();
                    logger.finest(listOfEntries.toString());
                }
            }
        });
    }


    /**
     * Test 16 include schema.
     */
    @Test
    public void test_16_includeSchema() {
        query.includeContentType();
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    JSONObject contentTypeObj = queryresult.getContentType();
                    logger.finest(contentTypeObj.toString());
                }
            }
        });
    }


    /**
     * Test 17 search.
     */
    @Test
    public void test_17_search() {
        query.search("dress");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    for (Entry entry : entries) {
                        JSONObject jsonObject = entry.toJSON();
                        Iterator<String> iter = jsonObject.keys();
                        while (iter.hasNext()) {
                            String key = iter.next();
                            try {
                                Object value = jsonObject.opt(key);
                                if (value instanceof String && ((String) value).contains("dress"))
                                    logger.info(value.toString());
                            } catch (Exception e) {
                                logger.info("----------------setQueryJson" + e.toString());
                            }
                        }
                    }
                }
            }
        });
    }


    /**
     * Test 18 ascending.
     */
    @Test
    public void test_18_ascending() {
        query.ascending("title");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    for (Entry entry : entries) {
                        logger.info(entry.getString("title"));
                    }
                }
            }
        });
    }


    /**
     * Test 19 descending.
     */
    @Test
    public void test_19_descending() {
        query.descending("title");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    for (Entry entry : entries) {
                        logger.info(entry.getString("title"));
                    }
                }
            }
        });
    }


    /**
     * Test 20 limit.
     */
    @Test
    public void test_20_limit() {
        query.limit(3);
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    for (Entry entry : entries) {
                        logger.info(" entry = [" + entry.getString("title") + "]");
                    }
                }
            }
        });
    }


    /**
     * Test 21 skip.
     */
    @Test
    public void test_21_skip() {
        query.skip(3);
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    logger.fine(entries.toString());
                }
            }
        });
    }


    /**
     * Test 22 only.
     */
    @Test
    public void test_22_only() {
        query.only(new String[]{"price"});
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    logger.fine(entries.toString());
                }
            }
        });
    }


    /**
     * Test 23 except.
     */
    @Test
    public void test_23_except() {
        query.locale("en-eu");
        query.except(new String[]{"price", "chutiya"});
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    logger.fine(entries.toString());
                }
            }
        });
    }


    /**
     * Test 24 count.
     */
    @Test
    public void test_24_count() {
        query.count();
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    int count = queryresult.getCount();
                    logger.fine("count: " + count);
                }
            }
        });
    }


    /**
     * Test 25 regex.
     */
    @Test
    public void test_25_regex() {
        query.regex("title", "lap*", "i");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    logger.fine(entries.toString());
                }
            }
        });
    }


    /**
     * Test 26 exist.
     */
    @Test
    public void test_26_exist() {
        query.exists("title");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    logger.fine(entries.toString());
                }
            }
        });
    }


    /**
     * Test 27 not exist.
     */
    @Test
    public void test_27_notExist() {
        query.notExists("price1");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    int entryCount = queryresult.getCount();
                    logger.fine("entry:" + entryCount);
                }
            }
        });
    }


    /**
     * Test 28 tags.
     */
    @Test
    public void test_28_tags() {
        query.tags(new String[]{"pink"});
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    logger.fine(entries.toString());
                }
            }
        });


    }


    /**
     * Test 29 language.
     */
    @Test
    public void test_29_language() {
        query.locale("en-us");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    logger.fine(entries.toString());
                }
            }
        });


    }


    /**
     * Test 30 include count.
     */
    @Test
    public void test_30_includeCount() {
        query.includeCount();
        query.where("uid", "blt3976eac6d3a0cb74");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    logger.fine(entries.toString());
                }
            }
        });
    }


    /**
     * Test 31 include reference only fetch.
     */
    @Test
    public void test_31_includeReferenceOnly_fetch() {

        final Query query = stack.contentType("multifield").query();
        query.where("uid", "blt1b1cb4f26c4b682e");

        ArrayList<String> strings = new ArrayList<>();
        strings.add("title");

        ArrayList<String> strings1 = new ArrayList<>();
        strings1.add("title");
        strings1.add("brief_description");
        strings1.add("discount");
        strings1.add("price");
        strings1.add("in_stock");

        query.onlyWithReferenceUid(strings, "package_info.info_category");
        query.exceptWithReferenceUid(strings1, "product_ref");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    logger.fine(entries.toString());
                }
            }
        });


    }


    /**
     * Test 32 include reference except fetch.
     */
    @Test
    public void test_32_includeReferenceExcept_fetch() {
        query = query.where("uid", "blt7801c5d40cbbe979");
        ArrayList<String> strings = new ArrayList<>();
        strings.add("title");
        query.exceptWithReferenceUid(strings, "category");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    logger.fine(entries.toString());
                }
            }
        });


    }


    /**
     * Test 33 find one.
     */
    @Test
    public void test_33_findOne() {
        query.includeCount();
        query.where("in_stock", true);
        query.findOne(new SingleQueryResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Entry entry, Error error) {
                if (error == null) {
                    logger.fine(entry.toJSON().getString("in_stock"));
                }
            }
        });
    }


    /**
     * Test 34 complex find.
     */
    @Test
    public void test_34_complexFind() {
        query.notEqualTo("title", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.*************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.*************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.*************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.*************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.*******");
        query.includeCount();
        query.find(new QueryResultsCallBack() {
            private void accept(Entry entry) {
                logger.info(entry.getTitle());
            }

            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    entries.forEach(this::accept);
                }
            }
        });
    }


    /**
     * Test 35 include schema.
     */
    @Test
    public void test_35_includeSchema() {
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                JSONArray result;
                if (error == null) {
                    result = queryresult.getSchema();
                    logger.fine(result.toString());
                }
            }
        });
    }


    /**
     * Test 36 include content type.
     */
    @Test
    public void test_36_includeContentType() {
        query.includeContentType();
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    JSONObject entries = queryresult.getContentType();
                    logger.fine(entries.toString());
                }
            }
        });
    }


    /**
     * Test 38 include content type.
     */
    @Test
    public void test_38_include_content_type() {
        query.includeContentType();
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                JSONObject result;
                if (error == null) {
                    result = queryresult.getContentType();
                    logger.fine(result.toString());
                }
            }
        });
    }


    /**
     * Test 39 include content type.
     */
    @Test
    public void test_39_include_content_type() {
        query.includeContentType();
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    JSONObject entries = queryresult.getContentType();
                    logger.fine(entries.toString());
                }
            }
        });
    }


    /**
     * Test 40 add params.
     */
    @Test
    public void test_40_addParams() {
        query.addParam("keyWithNull", null);
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    boolean result = query.urlQueries.has("keyWithNull");
                    logger.info("result Key With Null exists: " + result);
                    Object nullObject = query.urlQueries.opt("keyWithNull");
                    assertEquals("null", nullObject.toString());
                }
            }
        });
    }


    /**
     * Test 41 include fallback.
     */
    @Test
    public void test_41_include_fallback() {
        queryFallback.locale("hi-in");
        queryFallback.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    assertEquals(0, queryresult.getResultObjects().size());
                    queryFallback.includeFallback().locale("hi-in");
                    queryFallback.find(new QueryResultsCallBack() {
                        @Override
                        public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                            assertEquals(8, queryresult.getResultObjects().size());
                        }
                    });
                }
            }
        });
    }

    /**
     * Test 42 without include fallback.
     */
    @Test
    public void test_42_without_include_fallback() {
        queryFallback.locale("hi-in").find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    assertEquals(0, queryresult.getResultObjects().size());
                    logger.fine("total count: " + queryresult.getResultObjects().size());
                }
            }
        });
    }

    /**
     * Test 43 entry include embedded items.
     */
    @Test
    public void test_43_entry_include_embedded_items() {
        final Query query = stack.contentType("categories").query();
        query.includeEmbeddedItems().find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> arryResult = queryresult.getResultObjects();
                    for (Entry entry : arryResult) {
                        boolean _embedded_items = entry.toJSON().has("_embedded_items");
                        //TestCase.assertTrue(_embedded_items);
                    }
                }
                TestCase.assertTrue(query.urlQueries.has("include_embedded_items[]"));
            }
        });
    }

}