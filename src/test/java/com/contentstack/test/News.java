package com.contentstack.test;
import com.contentstack.sdk.*;
import com.contentstack.sdk.Error;


public class News {

    public static void main(String[] args) {

        Stack stack = null;
        try {
            stack = Contentstack.stack("blt920bb7e90248f607", "blt0c4300391e033d4a59eb2857", "production");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        ContentType contentType = stack.contentType("news");
        contentType.fetch(new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                System.out.println("fetch result: "+contentTypesModel.getResponse());
            }
        });


        /*query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    System.out.println("Title Size: "+queryresult.getResultObjects().size());
                    for (Entry entry: queryresult.getResultObjects()) {
                        System.out.println("Title: " + entry.getString("title"));
                    }
                } else {
                    System.out.println("Error Code: "+error.getErrorCode()+" Error Message: "+error.getErrorMessage());
                }
            }
        });*/

    }




}

