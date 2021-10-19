package com.contentstack.sdk;

import org.json.JSONObject;

import java.util.List;

public interface INotifyClass {


    public void getResult(Object object, String controller);

    public void getResultObject(List<Object> object, JSONObject jsonObject, boolean isSingleEntry);
}
