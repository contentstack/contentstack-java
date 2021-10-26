package com.contentstack.sdk;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;


class CSBackgroundTask {

    protected CSBackgroundTask() {
    }

    protected CSBackgroundTask(Stack stackInstance, String controller, String url, HashMap<String, Object> headers, HashMap<String, Object> urlParams, String requestInfo, ResultCallBack callback) {
        checkHeader(headers);
        String completeUrl = stackInstance.config.getEndpoint() + url;
        CSConnectionRequest csConnectionRequest = new CSConnectionRequest(stackInstance);
        csConnectionRequest.setStackInstance(stackInstance);
        csConnectionRequest.setURLQueries(urlParams);
        csConnectionRequest.setParams(completeUrl, headers, controller, requestInfo, callback);

    }

    protected CSBackgroundTask(Query queryInstance, Stack stackInstance, String controller, String url, LinkedHashMap<String, Object> headers, LinkedHashMap<String, Object> urlQueries, JSONObject jsonMain, String requestInfo, Constants.REQUEST_METHOD method, ResultCallBack callback) {
        checkHeader(headers);
        String completeUrl = stackInstance.URL_SCHEMA + stackInstance.URL + url;
        CSConnectionRequest csConnectionRequest = new CSConnectionRequest(queryInstance);
        csConnectionRequest.setQueryInstance(queryInstance);
        csConnectionRequest.setURLQueries(urlQueries);
        csConnectionRequest.setParams(completeUrl, method, controller, jsonMain, headers, requestInfo, callback);

    }


    protected CSBackgroundTask(Entry entryInstance, Stack stackInstance, String controller, String url, LinkedHashMap<String, Object> headers, HashMap<String, Object> urlQueries, JSONObject jsonMain, String requestInfo, boolean isOffline, Constants.REQUEST_METHOD method, ResultCallBack callBack) {
        checkHeader(headers);
        String completeUrl = stackInstance.URL_SCHEMA + stackInstance.URL + url;
        CSConnectionRequest csConnectionRequest = new CSConnectionRequest(entryInstance);
        csConnectionRequest.setURLQueries(urlQueries);
        csConnectionRequest.setParams(completeUrl, method, controller, jsonMain, headers, requestInfo, callBack);
    }


    protected CSBackgroundTask(AssetLibrary assetLibrary, Stack stackInstance, String controller, String url, LinkedHashMap<String, Object> headers, HashMap<String, Object> urlQueries, String requestInfo, ResultCallBack callback) {
        checkHeader(headers);
        String completeUrl = stackInstance.config.getEndpoint() + url;
        CSConnectionRequest csConnectionRequest = new CSConnectionRequest(assetLibrary);
        csConnectionRequest.setURLQueries(urlQueries);
        csConnectionRequest.setParams(completeUrl, headers, controller, requestInfo, callback);

    }


    protected CSBackgroundTask(Asset asset, Stack stackInstance, String controller, String url, LinkedHashMap<String, Object> headers, HashMap<String, Object> urlQueries, String requestInfo, ResultCallBack callback) {
        checkHeader(headers);
        String completeUrl = stackInstance.config.getEndpoint() + url;
        CSConnectionRequest csConnectionRequest = new CSConnectionRequest(asset);
        csConnectionRequest.setURLQueries(urlQueries);
        csConnectionRequest.setParams(completeUrl, headers, controller, requestInfo, callback);
    }


    protected CSBackgroundTask(ContentType contentType, Stack stackInstance, String controller, String url, HashMap<String, Object> headers, HashMap<String, Object> urlParams, String requestInfo, ResultCallBack callback) {
        checkHeader(headers);
        String completeUrl = stackInstance.config.getEndpoint() + url;
        CSConnectionRequest csConnectionRequest = new CSConnectionRequest(contentType);
        csConnectionRequest.setURLQueries(urlParams);
        csConnectionRequest.setParams(completeUrl, headers, controller, requestInfo, callback);
    }

    protected void checkHeader(@NotNull Map<String, Object> headers) {
        final Logger logger = Logger.getLogger("CSBackgroundTask");
        if (headers.size() == 0) {
            try {
                throw new IllegalAccessException("CSBackgroundTask Header Exception");
            } catch (IllegalAccessException e) {
                logger.severe(e.getLocalizedMessage());
            }
        }
    }

}
