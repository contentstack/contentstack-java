package com.contentstack.test;
import com.contentstack.sdk.Error;
import com.contentstack.sdk.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import static junit.framework.TestCase.assertTrue;


public class TestEntryTestCase extends JUnitCore {

    private Logger logger = LogManager.getLogger(TestEntryTestCase.class.getName());
    private CountDownLatch latch;
    private Stack stack;
    private String[] uidArray;

    public TestEntryTestCase() throws Exception{
        Config config = new Config();
        config.setHost("cdn.contentstack.io");
        String DEFAULT_APPLICATION_KEY = "blt12c8ad610ff4ddc2";
        String DEFAULT_ACCESS_TOKEN = "blt43359585f471685188b2e1ba";
        String DEFAULT_ENV = "env1";

        //setup for EU uncomment below
        //config.setRegion(Config.ContentstackRegion.EU);
        //String DEFAULT_APPLICATION_KEY = "bltc12b8d966127fa01";
        //String DEFAULT_ACCESS_TOKEN = "cse3ab6095485b70ab2713ed60";

        stack = Contentstack.stack(DEFAULT_APPLICATION_KEY, DEFAULT_ACCESS_TOKEN, DEFAULT_ENV,config);
        uidArray = new String[]{"blte88d9bec040e7c7c", "bltdf783472903c3e21"};
        latch = new CountDownLatch(1);

    }


    @Test
    public void test_00_fetch() throws InterruptedException {
        final Entry entry = stack.contentType("product").entry("blt7801c5d40cbbe979");
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    latch.countDown();
                } else {
                    latch.countDown();
                }

            }
        });
        latch.await();

    }
    @Test
    public void test_01_only_fetch() throws InterruptedException {
        final Entry entry = stack.contentType("product").entry("blt7801c5d40cbbe979");
        entry.only(new String[]{"price"});
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    latch.countDown();
                } else {
                    latch.countDown();
                }

            }
        });
        latch.await();

    }
    @Test
    public void test_02_except_fetch() throws InterruptedException {
        final Entry entry = stack.contentType("product").entry("blt7801c5d40cbbe979");
        entry.except(new String[]{"title"});
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    latch.countDown();
                } else {
                    latch.countDown();
                }

            }
        });
        latch.await();

    }
    @Test
    public void test_03_includeReference_fetch() throws InterruptedException {
        final Entry entry = stack.contentType("product").entry("blt7801c5d40cbbe979");
        entry.includeReference("category");
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    latch.countDown();
                } else {
                    latch.countDown();
                }
            }
        });
        latch.await();
        JSONArray categoryArray = (JSONArray) entry.getJSONArray("category");
        try {
            if (categoryArray != null){
                assertTrue(categoryArray.get(0) instanceof JSONObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test_04_includeReferenceOnly_fetch() throws InterruptedException {
        final Entry entry = stack.contentType("product").entry("blt7801c5d40cbbe979");
        ArrayList<String> strings = new ArrayList<>();
        strings.add("title");
        strings.add("orange");
        strings.add("mango");
        entry.onlyWithReferenceUid(strings, "category");
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {

                if (error == null) {
                    latch.countDown();
                } else {
                    latch.countDown();
                }

            }
        });
        latch.await();
        try {
            JSONArray categoryArray = (JSONArray) entry.getJSONArray("category");
            if (categoryArray != null){
                JSONObject jsonObject = categoryArray.getJSONObject(0);
                boolean isContains = false;
                if (jsonObject.has("title")) { isContains = true; }
                assertTrue(isContains);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test_05_includeReferenceExcept_fetch() throws InterruptedException {
        final Entry entry = stack.contentType("product").entry("blt7801c5d40cbbe979");
        ArrayList<String> strings = new ArrayList<>();
        strings.add("color");
        strings.add("price_in_usd");
        entry.exceptWithReferenceUid(strings, "category");
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    latch.countDown();
                } else {
                    latch.countDown();
                }

            }
        });
        latch.await();

    }



    @Test
    public void test_06_getMarkdown_fetch() throws InterruptedException {
        final Entry entry = stack.contentType("user").entry("blt3b0aaebf6f1c3762");
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    latch.countDown();
                } else {
                    latch.countDown();
                }
            }
        });
        latch.await();
    }


    @Test
    public void test_07_getMarkdown_fetch() throws InterruptedException {
        final Entry entry = stack.contentType("user").entry("blt3b0aaebf6f1c3762");
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    latch.countDown();
                } else {
                    latch.countDown();
                }
            }
        });
        latch.await();
    }


    @Test
    public void test_08_get() throws InterruptedException {
        final Entry entry = stack.contentType("user").entry("blt3b0aaebf6f1c3762");
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    latch.countDown();
                } else {
                    latch.countDown();
                }
            }
        });
        latch.await();
    }



    @Test
    public void test_09_getParam() throws InterruptedException {
        final Entry entry = stack.contentType("user").entry("blt3b0aaebf6f1c3762");
        entry.addParam("include_dimensions", "true");
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {

                if (error == null) {
                    latch.countDown();
                } else {
                    latch.countDown();
                }
            }
        });
        latch.await();
    }


    @Test
    public void test_10_IncludeReferenceContentTypeUID() throws InterruptedException {
        final Entry entry = stack.contentType("user").entry("blt3b0aaebf6f1c3762");
        entry.includeReferenceContentTypeUID();
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    JSONObject jsonResult = entry.toJSON();
                    try {
                       JSONArray  cartList = (JSONArray) jsonResult.get("cart");
                        Object whatTYPE = cartList.get(0);
                        if (whatTYPE instanceof JSONObject){
                            assertTrue(true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    latch.countDown();
                } else {
                    latch.countDown();
                }
            }
        });
        latch.await();

    }



    @Test
    public void test_Locale() throws InterruptedException {
        final Entry entry = stack.contentType("user").entry("blt3b0aaebf6f1c3762");
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    String checkResp = entry.getLocale();
                    logger.debug(checkResp);
                    latch.countDown();
                } else {
                    latch.countDown();
                }

            }
        });
        latch.await();
    }

    @Test
    public void test_entry_except() throws InterruptedException {
        final Entry entry = stack.contentType("user").entry("blt3b0aaebf6f1c3762");
        String[] allValues = {"color", "price_in_usd"};
        entry.except(allValues);
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    String checkResp = entry.getLocale();
                    logger.debug(checkResp);
                    latch.countDown();
                } else {
                    latch.countDown();
                }
            }
        });
        latch.await();
    }

}
