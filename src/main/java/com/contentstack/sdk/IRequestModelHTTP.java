package com.contentstack.sdk;

import org.json.JSONObject;

/**
 * IRequestModelHTTP Interface
 */
public interface IRequestModelHTTP {

    void sendRequest();

    void onRequestFailed(JSONObject error, int statusCode, ResultCallBack callBackObject);

    void onRequestFinished(CSHttpConnection request);
}
