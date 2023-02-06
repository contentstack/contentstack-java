package com.contentstack.sdk;

import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Response;

public interface ContentstackPlugin {

    default void onRequest(Stack stack, Request request) {
    }

    default Response<ResponseBody> onResponse(Stack stack, Request request, Response<ResponseBody> response) {
        return response;
    }
}
