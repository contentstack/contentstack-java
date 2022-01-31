package com.contentstack.sdk;

import org.json.JSONObject;

public interface IRequestModelHTTP {

    void sendRequest();

    void onRequestFailed(JSONObject error, int statusCode, ResultCallBack callBackObject);

    void onRequestFinished(CSHttpConnection request);
}
