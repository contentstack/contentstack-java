package com.contentstack.sdk;

import org.json.JSONObject;

import java.util.List;

public interface INotifyClass {

    void getResult(Object object, String controller);

    void getResultObject(List<Object> object, JSONObject jsonObject, boolean isSingleEntry);
}
