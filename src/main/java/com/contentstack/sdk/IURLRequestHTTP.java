package com.contentstack.sdk;

import org.json.JSONObject;

import java.util.LinkedHashMap;

public interface IURLRequestHTTP {

    void send();

    LinkedHashMap<String, Object> getHeaders();

    void setHeaders(LinkedHashMap<String, Object> headers);

    JSONObject getResponse();

    String getInfo();

    void setInfo(String info);

    String getController();

    void setController(String controller);

    ResultCallBack getCallBackObject();

    void setCallBackObject(ResultCallBack builtResultCallBackObject);

}
