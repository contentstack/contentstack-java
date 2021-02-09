package com.contentstack.test;

import com.contentstack.sdk.Error;
import com.contentstack.sdk.*;
import io.github.cdimascio.dotenv.Dotenv;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;


public class SyncTestCase {

    final Logger logger = LogManager.getLogger(SyncTestCase.class.getName());
    private String SYNC_TOKEN;
    private String PAGINATION_TOKEN;
    private final Stack stack;
    private int itemsSize = 0;
    private int counter = 0;
    private String dateISO = null;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public SyncTestCase() throws Exception {
        Dotenv dotenv = Dotenv.load();
        String API_KEY = dotenv.get("SYNC_API_KEY");
        String DELIVERY_TOKEN = dotenv.get("SYNC_DELIVERY_TOKEN");
        String ENVIRONMENT = dotenv.get("SYNC_ENVIRONMENT");
        SYNC_TOKEN = dotenv.get("SYNC_TOKEN");
        PAGINATION_TOKEN = dotenv.get("PAGINATION_TOKEN");
        assert API_KEY != null;
        stack = Contentstack.stack(API_KEY, DELIVERY_TOKEN, ENVIRONMENT);
    }


    @Test
    public void testSyncInit() {
        stack.sync(new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {
                if (error == null) {
                    itemsSize = syncStack.getItems().size();
                    counter = syncStack.getCount();
                    logger.info("sync stack size  :" + syncStack.getItems().size());
                    logger.info("sync stack count  :" + syncStack.getCount());
                    syncStack.getItems().forEach(item -> logger.info(item.toString()));
                    assertEquals(counter, syncStack.getCount());
                }
            }
        });
    }


    @Test
    public void testSyncToken() {
        stack.syncToken(SYNC_TOKEN, new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {
                if (error == null) {
                    itemsSize = syncStack.getItems().size();
                    counter = syncStack.getCount();
                    assertEquals(itemsSize, syncStack.getItems().size());
                }
            }
        });


    }

    @Test
    public void testPaginationToken() {
        stack.syncPaginationToken(PAGINATION_TOKEN, new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {
                if (error == null) {
                    itemsSize += syncStack.getItems().size();
                    counter = syncStack.getCount();
                    logger.info("sync pagination size  :" + syncStack.getItems().size());
                    logger.info("sync pagination count  :" + syncStack.getCount());
                    syncStack.getItems().forEach(item -> logger.info("Pagination" + item.toString()));
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
                            logger.info("date iso -->" + dateISO);
                            String serverDate = returnDateFromISOString(dateISO);
                            Date dateServer = null;
                            try {
                                dateServer = sdf.parse(serverDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            logger.info("dateServer -->" + dateServer);
                            assert dateServer != null;
                            int caparator = dateServer.compareTo(start_date);
                            assertEquals(1, caparator);
                        }
                    }
                    assertEquals(itemsSize, syncStack.getItems().size());
                }
            }
        });
    }


    @Test
    public void testSyncWithContentType() {
        stack.syncContentType("session", new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {
                if (error == null) {
                    itemsSize += syncStack.getItems().size();
                    counter = syncStack.getCount();
                    logger.info("sync content type size  :" + syncStack.getItems().size());
                    logger.info("sync content type count  :" + syncStack.getCount());
                    syncStack.getItems().forEach(item -> logger.info("content type: " + item.toString()));
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
                    for (JSONObject object : items) {
                        if (object.has("data"))
                            dataObject = object.optJSONObject("data").optString("locale");
                        assert dataObject != null;
                        logger.info("locale dataObject: --> " + dataObject);

                        if (!dataObject.isEmpty()) {
                            logger.info("locale dataObject: --> " + dataObject);
                            assertEquals("en-us", dataObject);
                        }
                    }
                    logger.info("sync stack size  :" + syncStack.getItems().size());
                    logger.info("sync stack count  :" + syncStack.getCount());
                    syncStack.getItems().forEach(item -> logger.info(item.toString()));
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
                    logger.info("publish type==>" + counter);
                    syncStack.getItems().forEach(items -> logger.info("publish type" + items.toString()));
                    assertEquals(itemsSize, syncStack.getItems().size());
                } else {
                    logger.info("publish type error !");
                }
            }
        });
    }


    @Test
    public void testSyncWithAll() throws ParseException {
        Date start_date = sdf.parse("2018-10-10");
        stack.sync("session", start_date, Language.ENGLISH_UNITED_STATES, Stack.PublishType.entry_published, new SyncResultCallBack() {
            @Override
            public void onCompletion(SyncStack syncStack, Error error) {
                if (error == null) {
                    itemsSize = syncStack.getItems().size();
                    counter = syncStack.getCount();
                    logger.info("stack with all type==>" + counter);
                    syncStack.getItems().forEach(items -> logger.info("sync with all type: " + items.toString()));
                    assertEquals(itemsSize, syncStack.getItems().size());
                }
            }

        });
    }

    private String returnDateFromISOString(String isoDateString) {
        String[] dateFormat = isoDateString.split("T");
        return dateFormat[0];
    }


}
