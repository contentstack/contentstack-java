package com.contentstack.sdk;

import okhttp3.Request;
import retrofit2.Response;

public interface ContentstackPlugin {

    void onRequest(Stack stack, Request request);

    Response onResponse(Stack stack, Request request, Response response);
}
