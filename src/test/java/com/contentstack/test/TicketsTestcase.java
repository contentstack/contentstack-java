package com.contentstack.test;

import com.contentstack.sdk.Error;
import com.contentstack.sdk.*;
import org.junit.Test;
import org.junit.runner.JUnitCore;

import static org.junit.Assert.assertEquals;

public class TicketsTestcase extends JUnitCore {


    private Stack stack;

    public TicketsTestcase() throws Exception {

        String STACK_API_KEY = "blt4c0468fe43dc5bdd";
        String ACCESS_TOKEN = "csbb1543164d7a0684b5a0f87f";
        String ENV = "staging";

        Config config = new Config();
        config.setHost("cdn.contentstack.io");
        stack = Contentstack.stack(STACK_API_KEY, ACCESS_TOKEN, ENV, config);
    }


    @Test
    public void TicketONE(){

        Query query = stack.contentType("collection").query().includeReference("card");
        //Query query = stack.contentType("help_center_topic").query();
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {

                int itemValidation = 0;
                if (error == null) {
                    for (Entry entry: queryResult.getResultObjects()) {
                        System.out.println("title: " + entry.getString("title"));
                        itemValidation = 200;
                    }
                } else {
                    itemValidation = 0;
                    System.out.println("failed: " + error.getErrorMessage() + " " + error.getErrorCode());
                }

                assertEquals(200, itemValidation);
            }
        });
    }




}
