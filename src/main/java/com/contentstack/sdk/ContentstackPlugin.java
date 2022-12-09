package com.contentstack.sdk;

import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Response;

public interface ContentstackPlugin {

    void onRequest(Stack stack, Request request);

    Response<ResponseBody> onResponse(Stack stack, Request request, Response response);
}
