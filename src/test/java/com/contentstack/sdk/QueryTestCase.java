package com.contentstack.sdk;

import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class QueryTestCase {

    private final Logger logger = Logger.getLogger(QueryTestCase.class.getName());
    private Stack stack;
    private Query query;
    private String entryUid;

    @BeforeAll
    public void beforeAll() throws IllegalAccessException {
        logger.setLevel(Level.FINE);
        Dotenv dotenv = Dotenv.load();
        String DEFAULT_API_KEY = dotenv.get("API_KEY");
        String DEFAULT_DELIVERY_TOKEN = dotenv.get("DELIVERY_TOKEN");
        String DEFAULT_ENV = dotenv.get("ENVIRONMENT");
        String DEFAULT_HOST = dotenv.get("HOST");
        Config config = new Config();
        config.setHost(DEFAULT_HOST);
        config.setRegion(Config.ContentstackRegion.US);
        assert DEFAULT_API_KEY != null;
        stack = Contentstack.stack(DEFAULT_API_KEY, DEFAULT_DELIVERY_TOKEN, DEFAULT_ENV, config);
    }

    @BeforeEach
    public void beforeEach() {
        query = stack.contentType("product").query();
    }

    @Test
    @Order(1)
    void testAllEntries() {
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    entryUid = queryresult.getResultObjects().get(0).uid;
                    Assertions.assertNotNull(queryresult);
                    Assertions.assertEquals(27, queryresult.getResultObjects().size());
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test()
    @Order(2)
    void testWhereEquals() {
        Query query = stack.contentType("categories").query();
        query.where("title", "Women");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> titles = queryresult.getResultObjects();
                    Assertions.assertEquals("Women", titles.get(0).title);
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test()
    @Order(4)
    void testWhereEqualsWithUid() {
        query.where("uid", this.entryUid);
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> titles = queryresult.getResultObjects();
                    Assertions.assertEquals("Blue Yellow", titles.get(0).title);
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test()
    @Order(3)
    void testWhere() {
        Query query = stack.contentType("product").query();
        query.where("title", "Blue Yellow");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> listOfEntries = queryresult.getResultObjects();
                    Assertions.assertEquals("Blue Yellow", listOfEntries.get(0).title);
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test
    @Order(4)
    void testIncludeReference() {
        query.includeReference("category");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> listOfEntries = queryresult.getResultObjects();
                    logger.fine(listOfEntries.toString());
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test
    @Order(5)
    void testNotContainedInField() {
        String[] containArray = new String[] { "Roti Maker", "kids dress" };
        query.notContainedIn("title", containArray);
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    Assertions.assertEquals(25, entries.size());
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test
    @Order(6)
    void testContainedInField() {
        String[] containArray = new String[] { "Roti Maker", "kids dress" };
        query.containedIn("title", containArray);
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    Assertions.assertEquals(2, entries.size());
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test
    @Order(7)
    void testNotEqualTo() {
        query.notEqualTo("title", "yellow t shirt");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    Assertions.assertEquals(26, entries.size());
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test
    @Order(8)
    void testGreaterThanOrEqualTo() {
        query.greaterThanOrEqualTo("price", 90);
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    Assertions.assertEquals(10, entries.size());
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test
    @Order(9)
    void testGreaterThanField() {
        query.greaterThan("price", 90);
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    Assertions.assertEquals(9, entries.size());
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test
    @Order(10)
    void testLessThanEqualField() {
        query.lessThanOrEqualTo("price", 90);
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    Assertions.assertEquals(17, entries.size());
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test
    @Order(11)
    void testLessThanField() {
        query.lessThan("price", "90");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    Assertions.assertEquals(0, entries.size());
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test
    @Order(12)
    void testEntriesWithOr() {

        ContentType ct = stack.contentType("product");
        Query orQuery = ct.query();

        Query query = ct.query();
        query.lessThan("price", 90);

        Query subQuery = ct.query();
        subQuery.containedIn("discount", new Integer[] { 20, 45 });

        ArrayList<Query> array = new ArrayList<>();
        array.add(query);
        array.add(subQuery);

        orQuery.or(array);

        orQuery.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    Assertions.assertEquals(18, entries.size());
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test
    @Order(13)
    void testEntriesWithAnd() {

        ContentType ct = stack.contentType("product");
        Query orQuery = ct.query();

        Query query = ct.query();
        query.lessThan("price", 90);

        Query subQuery = ct.query();
        subQuery.containedIn("discount", new Integer[] { 20, 45 });

        ArrayList<Query> array = new ArrayList<>();
        array.add(query);
        array.add(subQuery);

        orQuery.and(array);
        orQuery.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    Assertions.assertEquals(2, entries.size());
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test
    @Order(14)
    void testAddQuery() {
        query.addQuery("limit", "8");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    Assertions.assertEquals(8, entries.size());
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test
    @Order(15)
    void testRemoveQueryFromQuery() {
        query.addQuery("limit", "8");
        query.removeQuery("limit");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    Assertions.assertEquals(27, entries.size());
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test
    @Order(16)
    void testIncludeSchema() {
        query.includeContentType();
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    Assertions.assertEquals(27, entries.size());
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test
    @Order(17)
    void testSearch() {
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
                            Object value = jsonObject.opt(key);
                            Assertions.assertNotNull(value);
                        }
                    }
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test
    @Order(18)
    void testAscending() {
        query.ascending("title");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    for (int i = 0; i < entries.size(); i++) {
                        String previous = entries.get(i).getTitle(); // get first string
                        String next = entries.get(i + 1).getTitle(); // get second string
                        if (previous.compareTo(next) < 0) { // compare both if less than Zero then Ascending else
                                                            // descending
                            Assertions.assertTrue(true);
                        } else {
                            Assertions.fail("expected descending, found ascending");
                        }
                    }
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test
    @Order(19)
    void testDescending() {
        query.descending("title");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    for (int i = 0; i < entries.size(); i++) {
                        String previous = entries.get(i).getTitle(); // get first string
                        String next = entries.get(i + 1).getTitle(); // get second string
                        if (previous.compareTo(next) < 0) { // compare both if less than Zero then Ascending else
                                                            // descending
                            Assertions.fail("expected descending, found ascending");
                        } else {
                            Assertions.assertTrue(true);
                        }
                    }
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test
    @Order(20)
    void testLimit() {
        query.limit(3);
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    Assertions.assertEquals(3, entries.size());
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test
    @Order(21)
    void testSkip() {
        query.skip(3);
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    Assertions.assertEquals(24, entries.size());
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test
    @Order(22)
    void testOnly() {
        query.only(new String[] { "price" });
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    Assertions.assertEquals(0, entries.size());
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test
    @Order(23)
    void testExcept() {
        query.except(new String[] { "price" });
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    Assertions.assertEquals(27, entries.size());
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test
    @Order(24)
    @Deprecated
    void testCount() {
        query.count();
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    Assertions.assertEquals(0, entries.size());
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test
    @Order(25)
    void testRegex() {
        query.regex("title", "lap*", "i");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    Assertions.assertEquals(1, entries.size());
                    // to add in the coverage code execution
                    Group group = new Group(stack, entries.get(0).toJSON());
                    doSomeBackgroundTask(group);
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    protected void doSomeBackgroundTask(Group group) {
        JSONObject groupJsonObject = group.toJSON();
        Assertions.assertNotNull(groupJsonObject);
        Assertions.assertNotNull(groupJsonObject);
        Object titleObj = group.get("title");
        String titleStr = group.getString("title");
        Boolean titleBool = group.getBoolean("in_stock");
        JSONArray titleJSONArray = group.getJSONArray("image");
        JSONObject titleJSONObject = group.getJSONObject("publish_details");
        Object versionNum = group.getNumber("_version");
        Object versionInt = group.getInt("_version");
        Float versionFloat = group.getFloat("_version");
        Double versionDouble = group.getDouble("_version");
        long versionLong = group.getLong("_version");
        logger.fine("versionLong: " + versionLong);
        Assertions.assertNotNull(titleObj);
        Assertions.assertNotNull(titleStr);
        Assertions.assertNotNull(titleBool);
        Assertions.assertNotNull(titleJSONArray);
        Assertions.assertNotNull(titleJSONObject);
        Assertions.assertNotNull(versionNum);
        Assertions.assertNotNull(versionInt);
        Assertions.assertNotNull(versionFloat);
        Assertions.assertNotNull(versionDouble);
    }

    @Test
    @Order(26)
    void testExist() {
        query.exists("title");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    Assertions.assertEquals(27, entries.size());
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test
    @Order(27)
    void testNotExist() {
        query.notExists("price1");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    Assertions.assertEquals(27, entries.size());
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test
    @Order(28)
    void testTags() {
        query.tags(new String[] { "pink" });
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    Assertions.assertEquals(1, entries.size());
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });

    }

    @Test
    @Order(29)
    void testLanguage() {
        query.locale("en-us");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    Assertions.assertEquals(27, entries.size());
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });

    }

    @Test
    @Order(30)
    void testIncludeCount() {
        query.includeCount();
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    Assertions.assertTrue(queryresult.receiveJson.has("count"));
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test
    @Order(31)
    void testIncludeReferenceOnly() {

        final Query query = stack.contentType("multifield").query();
        query.where("uid", "fakeIt");

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
                    Assertions.assertEquals(0, entries.size());
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });

    }

    @Test
    @Order(32)
    void testIncludeReferenceExcept() {
        query = query.where("uid", "fakeit");
        ArrayList<String> strings = new ArrayList<>();
        strings.add("title");
        query.exceptWithReferenceUid(strings, "category");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    Assertions.assertEquals(0, entries.size());
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });

    }

    @Test
    @Order(33)
    void testFindOne() {
        query.includeCount();
        query.where("in_stock", true);
        query.findOne(new SingleQueryResultCallback() {
            @Override
            public void onCompletion(ResponseType responseType, Entry entry, Error error) {
                if (error == null) {
                    String entries = entry.getTitle();
                    Assertions.assertNotNull(entries);
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test
    @Order(34)
    void testComplexFind() {
        query.notEqualTo("title",
                "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.*************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.*************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.*************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.*************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.************************************Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.*******");
        query.includeCount();
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    Assertions.assertEquals(27, entries.size());
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test
    @Order(35)
    void testIncludeSchemaCheck() {
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    JSONArray schema = queryresult.getSchema();
                    Assertions.assertEquals(27, schema.length());
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test
    @Order(36)
    void testIncludeContentType() {
        query.includeContentType();
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    Assertions.assertEquals(27, entries.size());
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test
    @Order(37)
    void testIncludeContentTypeFetch() {
        query.includeContentType();
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    JSONObject contentType = queryresult.getContentType();
                    Assertions.assertEquals("", contentType.optString(""));
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test
    @Order(38)
    void testAddParams() {
        query.addParam("keyWithNull", "null");
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    Object nullObject = query.urlQueries.opt("keyWithNull");
                    assertEquals("null", nullObject.toString());
                }
            }
        });
    }

    @Test
    @Order(39)
    void testIncludeFallback() {
        Query queryFallback = stack.contentType("categories").query();
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

    @Test
    @Order(40)
    void testWithoutIncludeFallback() {
        Query queryFallback = stack.contentType("categories").query();
        queryFallback.locale("hi-in").find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    assertEquals(0, queryresult.getResultObjects().size());
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test
    @Order(41)
    void testEntryIncludeEmbeddedItems() {
        final Query query = stack.contentType("categories").query();
        query.includeEmbeddedItems().find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    assertTrue(query.urlQueries.has("include_embedded_items[]"));
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test
    @Order(42)
    void testError() {
        Error error = new Error("Faking error information", 400, "{errors: invalid credential}");
        Assertions.assertNotNull(error.getErrorDetail());
        Assertions.assertEquals(400, error.getErrorCode());
        Assertions.assertNotNull(error.getErrorMessage());
    }

    // Unit testcases
    // Running through the BeforeEach query instance

    @Test
    void testUnitQuerySetHeader() {
        query.setHeader("fakeHeaderKey", "fakeHeaderValue");
        Assertions.assertTrue(query.headers.containsKey("fakeHeaderKey"));
    }

    @Test
    void testUnitQueryRemoveHeader() {
        query.setHeader("fakeHeaderKey", "fakeHeaderValue");
        query.removeHeader("fakeHeaderKey");
        Assertions.assertFalse(query.headers.containsKey("fakeHeaderKey"));
    }

    @Test
    void testUnitQueryWhere() {
        query.where("title", "fakeTitle");
        Assertions.assertTrue(query.queryValueJSON.has("title"));
        Assertions.assertEquals("fakeTitle", query.queryValueJSON.opt("title"));
    }

    @Test
    void testUnitAndQuery() {
        ArrayList<Query> queryObj = new ArrayList<>();
        queryObj.add(query);
        queryObj.add(query);
        queryObj.add(query);
        try {
            query.and(queryObj);
            Assertions.assertTrue(query.queryValueJSON.has("$and"));
        } catch (Exception e) {
            Assertions.assertTrue(query.queryValueJSON.has("$and"));
        }
    }

    @Test
    void testUnitQueryOr() {
        ArrayList<Query> queryObj = new ArrayList<>();
        queryObj.add(query);
        queryObj.add(query);
        queryObj.add(query);
        try {
            query.or(queryObj);
            Assertions.assertTrue(query.queryValueJSON.has("$or"));
        } catch (Exception e) {
            Assertions.assertTrue(query.queryValueJSON.has("$or"));
        }
    }

    @Test
    void testUnitQueryExcept() {
        ArrayList<String> queryObj = new ArrayList<>();
        queryObj.add("fakeQuery1");
        queryObj.add("fakeQuery2");
        queryObj.add("fakeQuery3");
        query.except(queryObj);
        Assertions.assertEquals(3, query.objectUidForExcept.length());
    }

    @Test
    void testUnitQueryOwner() {
        query.includeOwner();
        Assertions.assertTrue(query.urlQueries.has("include_owner"));
    }

    @Test
    void testUnitQuerySkip() {
        query.skip(5);
        Assertions.assertTrue(query.urlQueries.has("skip"));
    }

    @Test
    void testUnitQueryLimit() {
        query.limit(5);
        Assertions.assertTrue(query.urlQueries.has("limit"));
    }

    @Test
    void testUnitQueryRegex() {
        query.regex("regexKey", "regexValue");
        Assertions.assertTrue(query.queryValue.has("$regex"));
    }

    @Test
    void testUnitQueryIncludeReferenceContentTypUid() {
        query.includeReferenceContentTypUid();
        Assertions.assertTrue(query.urlQueries.has("include_reference_content_type_uid"));
    }

    @Test
    void testUnitQueryWhereIn() {
        query.whereIn("fakeIt", query);
        Assertions.assertTrue(query.queryValueJSON.has("fakeIt"));
    }

    @Test
    void testUnitQueryWhereNotIn() {
        query.whereNotIn("fakeIt", query);
        Assertions.assertTrue(query.queryValueJSON.has("fakeIt"));
    }

}