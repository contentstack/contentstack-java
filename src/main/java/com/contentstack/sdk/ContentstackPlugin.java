package com.contentstack.sdk;

import okhttp3.Request;

public interface ContentstackPlugin {

    default Request onRequest(
            Stack stack,
            Request request) {
        return request;
    }

    default retrofit2.Response<okhttp3.ResponseBody> onResponse(
            Stack stack,
            Request request,
            retrofit2.Response<okhttp3.ResponseBody> response) {
        return response;
    }
}
