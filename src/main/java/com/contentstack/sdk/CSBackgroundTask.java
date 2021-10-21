package com.contentstack.sdk;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;


class CSBackgroundTask {

    public CSBackgroundTask(Query queryInstance, Stack stackInstance, String controller, String url, LinkedHashMap<String, Object> headers, LinkedHashMap<String, Object> urlQueries, JSONObject jsonMain, String requestInfo, Constants.REQUEST_METHOD method, ResultCallBack callback) {
        if (headers != null && headers.size() > 0) {
            String URL = stackInstance.URL_SCHEMA + stackInstance.URL + url;
            CSConnectionRequest csConnectionRequest = new CSConnectionRequest(queryInstance);
            csConnectionRequest.setQueryInstance(queryInstance);
            csConnectionRequest.setURLQueries(urlQueries);
            csConnectionRequest.setParams(URL, method, controller, jsonMain, headers, requestInfo, callback);
        } else {
            sendErrorForHeader(callback);
        }
    }


    public CSBackgroundTask(Entry entryInstance, Stack stackInstance, String controller, String url, LinkedHashMap<String, Object> headers, HashMap<String, Object> urlQueries, JSONObject jsonMain, String requestInfo, boolean isOffline, Constants.REQUEST_METHOD method, ResultCallBack callBack) {
        if (headers != null && headers.size() > 0) {
            String URL = stackInstance.URL_SCHEMA + stackInstance.URL + url;
            CSConnectionRequest csConnectionRequest = new CSConnectionRequest(entryInstance);
            csConnectionRequest.setURLQueries(urlQueries);
            csConnectionRequest.setParams(URL, method, controller, jsonMain, headers, requestInfo, callBack);
        } else {
            sendErrorForHeader(callBack);
        }
    }


    public CSBackgroundTask(AssetLibrary assetLibrary, Stack stackInstance, String controller, String url, LinkedHashMap<String, Object> headers, HashMap<String, Object> urlQueries, JSONObject jsonMain, String requestInfo, boolean isOffline, Constants.REQUEST_METHOD method, ResultCallBack callback) {
        if (headers != null && headers.size() > 0) {
            String URL = stackInstance.URL_SCHEMA + stackInstance.URL + url;
            CSConnectionRequest csConnectionRequest = new CSConnectionRequest(assetLibrary);
            csConnectionRequest.setURLQueries(urlQueries);
            csConnectionRequest.setParams(URL, method, controller, jsonMain, headers, requestInfo, callback);
        } else {
            sendErrorForHeader(callback);
        }
    }


    public CSBackgroundTask(Asset asset, Stack stackInstance, String controller, String url, LinkedHashMap<String, Object> headers, HashMap<String, Object> urlQueries, JSONObject jsonMain, String requestInfo, boolean isOffline, Constants.REQUEST_METHOD method, ResultCallBack callback) {
        if (headers != null && headers.size() > 0) {
            String URL = stackInstance.URL_SCHEMA + stackInstance.URL + url;
            CSConnectionRequest csConnectionRequest = new CSConnectionRequest(asset);
            csConnectionRequest.setURLQueries(urlQueries);
            csConnectionRequest.setParams(URL, method, controller, jsonMain, headers, requestInfo, callback);
        } else {
            sendErrorForHeader(callback);
        }

    }


    public CSBackgroundTask(Stack stackInstance, String controller, String url, HashMap<String, Object> headers, HashMap<String, Object> urlParams, JSONObject jsonMain, String requestInfo, boolean b, Constants.REQUEST_METHOD method, ResultCallBack callback) {
        if (headers != null && headers.size() > 0) {
            String URL = stackInstance.URL_SCHEMA + stackInstance.URL + url;
            CSConnectionRequest csConnectionRequest = new CSConnectionRequest(stackInstance);
            csConnectionRequest.setStackInstance(stackInstance);
            csConnectionRequest.setURLQueries(urlParams);
            csConnectionRequest.setParams(URL, method, controller, jsonMain, headers, requestInfo, callback);
        } else {
            sendErrorForHeader(callback);
        }
    }


    public CSBackgroundTask(ContentType contentTypeInstance, Stack stackInstance, String controller, String url, HashMap<String, Object> headers, HashMap<String, Object> urlParams, JSONObject jsonMain, String requestInfo, boolean b, Constants.REQUEST_METHOD method, ResultCallBack callback) {
        if (headers != null && headers.size() > 0) {
            String URL = stackInstance.URL_SCHEMA + stackInstance.URL + url;
            CSConnectionRequest csConnectionRequest = new CSConnectionRequest(contentTypeInstance);
            csConnectionRequest.setURLQueries(urlParams);
            csConnectionRequest.setParams(URL, method, controller, jsonMain, headers, requestInfo, callback);
        } else {
            sendErrorForHeader(callback);
        }
    }

    private void sendErrorForHeader(ResultCallBack callbackObject) {
        Error error = new Error();
        error.setErrorMessage(Constants.CALL_STACK);
        if (callbackObject != null) {
            callbackObject.onRequestFail(ResponseType.UNKNOWN, error);
        }
    }
}
