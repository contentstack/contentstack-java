package com.contentstack.test;

import com.contentstack.sdk.Error;
import com.contentstack.sdk.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;


public class TestSyncTestCase {

    final Logger logger = LogManager.getLogger(TestSyncTestCase.class.getName());

    private Stack stack;
    private int itemsSize = 0;
    private int counter = 0;
    private String dateISO = null;
    private int include_count = 0;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public TestSyncTestCase() throws Exception {
        //Configurator.initialize(new DefaultConfiguration());

        Config config = new Config();
        config.setHost("cdn.contentstack.io");
        String prod_api_key = "blt477ba55f9a67bcdf";
        String prod_delivery_Token = "cs7731f03a2feef7713546fde5";
        String environment = "web";

        //setup for EU uncomment below
        //config.setRegion(Config.ContentstackRegion.EU);
        //String prod_api_key = "bltec63b57f491547fe";
        //String prod_delivery_Token = "cs5834dc67621234eb68fce5dd";

        stack = Contentstack.stack(prod_api_key, prod_delivery_Token, environment, config);
    }


    @Test
    public void testSyncInit() {

        stack.sync(new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {

                if (error == null) {
                    itemsSize = syncStack.getItems().size();
                    counter = syncStack.getCount();
                    String sync_token = syncStack.getSyncToken();
                    logger.info( "sync stack size  :"+syncStack.getItems().size());
                    logger.info("sync stack count  :"+syncStack.getCount());
                    syncStack.getItems().forEach(item-> logger.info(  item.toString()));

                    assertEquals(counter, syncStack.getCount());
                }
            }});
    }




    @Test
    public void testSyncToken() {
        stack.syncToken("bltbb61f31a70a572e6c9506a", new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {
                if (error == null) {
                    itemsSize = syncStack.getItems().size();
                    counter = syncStack.getCount();
                    assertEquals( itemsSize, syncStack.getItems().size());
                }
            }
        });


    }

    @Test
    public void testPaginationToken() {
        stack.syncPaginationToken("blt7f35951d259183fba680e1", new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {

                if (error == null) {
                    itemsSize += syncStack.getItems().size();
                    counter = syncStack.getCount();

                    logger.info("sync pagination size  :"+syncStack.getItems().size());
                    logger.info("sync pagination count  :"+syncStack.getCount());
                    syncStack.getItems().forEach(item-> logger.info( "Pagination"+item.toString()));
                    //assertEquals( itemsSize, itemsSize);
                }
            }
        });
    }


    @Test
    public void testSyncWithDate() throws ParseException {

        final Date start_date = sdf.parse("2018-10-07");
        stack.syncFromDate(start_date, new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {
                if (error == null) {

                    itemsSize = syncStack.getItems().size();
                    counter = syncStack.getCount();
                    for (JSONObject jsonObject1 : syncStack.getItems()) {
                        if (jsonObject1.has("event_at")) {

                            dateISO = jsonObject1.optString("event_at");
                            logger.info( "date iso -->"+dateISO);
                            String serverDate = returnDateFromISOString(dateISO);
                            Date dateServer = null;
                            try {
                                dateServer = sdf.parse(serverDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            logger.info( "dateServer -->"+dateServer);
                            assert dateServer != null;
                            int caparator = dateServer.compareTo(start_date);
                            assertEquals(1, caparator);
                        }
                    }

                    assertEquals(itemsSize, syncStack.getItems().size());
                }


            }});
    }





    @Test
    public void testSyncWithContentType() {

        stack.syncContentType("session", new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {
                if (error == null) {
                    itemsSize += syncStack.getItems().size();
                    counter = syncStack.getCount();

                    logger.info("sync content type size  :"+syncStack.getItems().size());
                    logger.info("sync content type count  :"+syncStack.getCount());
                    syncStack.getItems().forEach(item-> logger.info( "content type: "+item.toString()));

                    //assertEquals(100, itemsSize);
                }
            }
        });


    }



    @Test
    public void testSyncWithLocale() {

        stack.syncLocale(Language.ENGLISH_UNITED_STATES, new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {

                if (error == null) {
                    counter = syncStack.getCount();
                    ArrayList<JSONObject> items = syncStack.getItems();
                    String dataObject = null;
                    for (JSONObject object: items){
                        if (object.has("data"))
                            dataObject = object.optJSONObject("data").optString("locale");
                        assert dataObject != null;
                        logger.info("locale dataObject: --> "+ dataObject);

                        if (!dataObject.isEmpty()) {
                            logger.info("locale dataObject: --> "+ dataObject);
                            assertEquals("en-us", dataObject);
                        }
                    }

                    logger.info("sync stack size  :"+syncStack.getItems().size());
                    logger.info("sync stack count  :"+syncStack.getCount());
                    syncStack.getItems().forEach(item-> logger.info(item.toString()));
                }
            }
        });

    }




    @Test
    public void testPublishType() {

        stack.syncPublishType(Stack.PublishType.entry_published, new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {
                if (error == null) {
                    itemsSize = syncStack.getItems().size();
                    counter = syncStack.getCount();

                    logger.info( "publish type==>"+counter);
                    syncStack.getItems().forEach(items-> logger.info( "publish type"+items.toString()));

                    assertEquals(itemsSize, syncStack.getItems().size());
                }else {
                    // Error block
                    logger.info( "publish type error !");
                }

            }
        });

    }





    @Test
    public void testSyncWithAll() throws ParseException {

        Date start_date = sdf.parse("2018-10-10");
        stack.sync( "session", start_date, Language.ENGLISH_UNITED_STATES, Stack.PublishType.entry_published, new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {

                if (error == null) {
                    itemsSize = syncStack.getItems().size();
                    counter = syncStack.getCount();
                    logger.info( "stack with all type==>"+counter);
                    syncStack.getItems().forEach(items-> logger.info(  "sync with all type: "+items.toString()));
                    assertEquals(itemsSize, syncStack.getItems().size());
                }

            }

        });
    }



    @Test
    public void test_get_all_stack_content_types() throws JSONException {
        Stack where_stack = null;
        try {
            where_stack = Contentstack.stack("blt20962a819b57e233", "blt01638c90cc28fb6f", "production");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject params = new JSONObject();
        params.put("include_snippet_schema", true);
        params.put("limit", 3);

        assert where_stack != null;
        where_stack.getContentTypes(params, new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                if (error == null){
                    logger.debug(contentTypesModel.getResponse().toString());
                }
            }
        });

    }



    @Test
    public void getSingleContentType() throws JSONException {

        Stack where_stack = null;
        try {
            where_stack = Contentstack.stack("blt20962a819b57e233", "blt01638c90cc28fb6f", "production");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ContentType  contentType = where_stack.contentType("product");
        JSONObject params = new JSONObject();
        params.put("include_snippet_schema", true);
        params.put("limit", 3);
        contentType.fetch(params, new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                if (error==null){
                    logger.info( "single content:"+ contentTypesModel.getResponse());
                }else {
                    logger.info( "Error"+ error.getErrorMessage());
                }
            }
        });
    }


    private String returnDateFromISOString(String isoDateString) {
        String[] dateFormate = isoDateString.split("T");
        return dateFormate[0];
    }

}
