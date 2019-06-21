package com.contentstack.test;
import com.contentstack.sdk.*;
import com.contentstack.sdk.Error;

import java.util.List;


public class NewsMain {

    private static String TAG = NewsMain.class.getSimpleName();
    public static void main(String[] args) throws Exception {

        Stack stack = Contentstack.stack("bltdd99f24e8a94d536", "blt22ef89e3652f3a44", "dev");
        Query query_banner = stack.contentType("banner_codes").query();
        query_banner.addParam("code", "snipsnapfy14");
        query_banner.language(Language.ENGLISH_UNITED_STATES);
        query_banner.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error==null){
                    List<Entry> entryList = queryresult.getResultObjects();
                    entryList.forEach(entry -> {
                        Stack.log("query_banner", entry.toJSON().toString());
                    });
                }
            }
        });



        Query query_header = stack.contentType("header").query();
        query_header.addParam("code", "snipsnapfy14");
        query_header.language(Language.ENGLISH_UNITED_STATES);
        query_header.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error==null){
                    List<Entry> entryList = queryresult.getResultObjects();
                    entryList.forEach(entry -> {
                        Stack.log("query_banner", entry.toJSON().toString());
                    });
                }
            }
        });



    }

}

