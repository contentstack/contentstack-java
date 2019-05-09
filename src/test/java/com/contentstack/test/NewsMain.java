package com.contentstack.test;
import com.contentstack.sdk.*;
import com.contentstack.sdk.Error;


public class NewsMain {

    private static String TAG = NewsMain.class.getSimpleName();
    public static void main(String[] args) throws Exception {

        Stack stack = Contentstack.stack("blt920bb7e90248f607", "blt0c4300391e033d4a59eb2857", "production");
        ContentType contentType = stack.contentType("news");
        contentType.fetch(new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                Stack.log(TAG, contentTypesModel.getResponse().toString());
            }
        });

    }

}

