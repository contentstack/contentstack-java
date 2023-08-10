package com.contentstack.sdk;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestQueryCase {

    private final Logger logger = Logger.getLogger(TestQueryCase.class.getName());
    private final Stack stack = Credentials.getStack();
    private Query query;
    private String entryUid;

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
        Query query1 = stack.contentType("product").query();
        query1.includeReference("category");
        query1.find(new QueryResultsCallBack() {
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
        Query query1 = stack.contentType("product").query();
        String[] containArray = new String[]{"Roti Maker", "kids dress"};
        query1.notContainedIn("title", containArray);
        query1.find(new QueryResultsCallBack() {
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
        Query query1 = stack.contentType("product").query();
        String[] containArray = new String[]{"Roti Maker", "kids dress"};
        query1.containedIn("title", containArray);
        query1.find(new QueryResultsCallBack() {
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
        Query query1 = stack.contentType("product").query();
        query1.notEqualTo("title", "yellow t shirt");
        query1.find(new QueryResultsCallBack() {
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
        Query query1 = stack.contentType("product").query();
        query1.greaterThanOrEqualTo("price", 90);
        query1.find(new QueryResultsCallBack() {
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
        Query query1 = stack.contentType("product").query();
        query1.greaterThan("price", 90);
        query1.find(new QueryResultsCallBack() {
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
        Query query1 = stack.contentType("product").query();
        query1.lessThanOrEqualTo("price", 90);
        query1.find(new QueryResultsCallBack() {
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
        Query query1 = stack.contentType("product").query();
        query1.lessThan("price", "90");
        query1.find(new QueryResultsCallBack() {
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
        subQuery.containedIn("discount", new Integer[]{20, 45});

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
        subQuery.containedIn("discount", new Integer[]{20, 45});

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
        Query query1 = stack.contentType("product").query();
        query1.addQuery("limit", "8");
        query1.find(new QueryResultsCallBack() {
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
        Query query1 = stack.contentType("product").query();
        query1.addQuery("limit", "8");
        query1.removeQuery("limit");
        query1.find(new QueryResultsCallBack() {
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
        Query query1 = stack.contentType("product").query();
        query1.includeContentType();
        query1.find(new QueryResultsCallBack() {
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
        Query query1 = stack.contentType("product").query();
        query1.search("dress");
        query1.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    for (Entry entry : entries) {
                        JSONObject jsonObject = entry.toJSON();
                        Iterator<String> itr = jsonObject.keys();
                        while (itr.hasNext()) {
                            String key = itr.next();
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
        Query query1 = stack.contentType("product").query();
        query1.ascending("title").find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    for (int i = 0; i < entries.size() - 1; i++) {
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
        Query query1 = stack.contentType("product").query();
        query1.descending("title");
        query1.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<Entry> entries = queryresult.getResultObjects();
                    for (int i = 0; i < entries.size() - 1; i++) {
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
        Query query1 = stack.contentType("product").query();
        query1.limit(3);
        query1.find(new QueryResultsCallBack() {
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
        Query query1 = stack.contentType("product").query();
        query1.skip(3);
        query1.find(new QueryResultsCallBack() {
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
        Query query1 = stack.contentType("product").query();
        query1.only(new String[]{"price"});
        query1.find(new QueryResultsCallBack() {
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
    @Order(23)
    void testExcept() {
        Query query1 = stack.contentType("product").query();
        query1.except(new String[]{"price"});
        query1.find(new QueryResultsCallBack() {
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
        Query query1 = stack.contentType("product").query();
        query1.count();
        query1.find(new QueryResultsCallBack() {
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
        Query query1 = stack.contentType("product").query();
        query1.regex("title", "lap*", "i");
        query1.find(new QueryResultsCallBack() {
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
        Query query1 = stack.contentType("product").query();
        query1.exists("title");
        query1.find(new QueryResultsCallBack() {
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
        Query query1 = stack.contentType("product").query();
        query1.notExists("price1");
        query1.find(new QueryResultsCallBack() {
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
        Query query1 = stack.contentType("product").query();
        query1.tags(new String[]{"pink"});
        query1.find(new QueryResultsCallBack() {
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
        Query query1 = stack.contentType("product").query();
        query1.locale("en-us");
        query1.find(new QueryResultsCallBack() {
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
        Query query1 = stack.contentType("product").query();
        query1.includeCount();
        query1.find(new QueryResultsCallBack() {
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
        Query query1 = stack.contentType("product").query();
        query1.where("uid", "fake it");
        ArrayList<String> strings = new ArrayList<>();
        strings.add("title");
        query1.exceptWithReferenceUid(strings, "category");
        query1.find(new QueryResultsCallBack() {
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
        Query query1 = stack.contentType("product").query();
        query1.includeCount();
        query1.where("in_stock", true);
        query1.findOne(new SingleQueryResultCallback() {
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
        Query query1 = stack.contentType("product").query();
        query1.notEqualTo("title",
                "Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
        query1.includeCount();
        query1.find(new QueryResultsCallBack() {
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
        Query query1 = stack.contentType("product").query();
        query1.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    Assertions.assertEquals(27, queryresult.getResultObjects().size());
                } else {
                    Assertions.fail("Failing, Verify credentials");
                }
            }
        });
    }

    @Test
    @Order(36)
    void testIncludeContentType() {
        Query query1 = stack.contentType("product").query();
        query1.includeContentType();
        query1.find(new QueryResultsCallBack() {
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
        Query query1 = stack.contentType("product").query();
        query1.includeContentType();
        query1.find(new QueryResultsCallBack() {
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
        Query query1 = stack.contentType("product").query();
        query1.addParam("keyWithNull", "null");
        query1.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    Object nullObject = query1.urlQueries.opt("keyWithNull");
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
        ArrayList<Query> queryObj = new ArrayList<>();
        queryObj.add(query);
        queryObj.add(query);
        queryObj.add(query);
        ArrayList<String> queryEx = new ArrayList<>();
        queryEx.add("fakeQuery1");
        queryEx.add("fakeQuery2");
        queryEx.add("fakeQuery3");
        query.except(queryEx).or(queryObj);
        Assertions.assertEquals(3, query.objectUidForExcept.length());
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
        query.regex("regexKey", "regexValue").limit(5);
        Assertions.assertTrue(query.queryValue.has("$regex"));
    }

    @Test
    void testUnitQueryIncludeReferenceContentTypUid() {
        query.includeReferenceContentTypUid().limit(5);
        Assertions.assertTrue(query.urlQueries.has("include_reference_content_type_uid"));
    }

    @Test
    void testUnitQueryWhereIn() {
        query.whereIn("fakeIt", query).includeReferenceContentTypUid();
        Assertions.assertTrue(query.queryValueJSON.has("fakeIt"));
    }

    @Test
    void testUnitQueryWhereNotIn() {
        query.whereNotIn("fakeIt", query).limit(3);
        Assertions.assertTrue(query.queryValueJSON.has("fakeIt"));
    }


    @Test
    void testIncludeOwner() {
        query.includeMetadata();
        Assertions.assertTrue(query.urlQueries.has("include_metadata"));
    }

    @Test
    void testIncludeOwnerValue() {
        query.includeMetadata();
        Assertions.assertTrue(query.urlQueries.getBoolean("include_metadata"));
    }

}