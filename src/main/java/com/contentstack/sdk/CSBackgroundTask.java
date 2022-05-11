package com.contentstack.sdk;

import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

class CSBackgroundTask {

    protected APIService service;

    protected CSBackgroundTask() {
    }

    protected CSBackgroundTask(Stack stackInstance, String controller, String url, HashMap<String, Object> headers,
                               HashMap<String, Object> urlParams, String requestInfo, ResultCallBack callback) {
        checkHeader(headers);
        String completeUrl = stackInstance.config.getEndpoint() + url;
        CSConnectionRequest csConnectionRequest = new CSConnectionRequest(stackInstance);
        csConnectionRequest.setStackInstance(stackInstance);
        csConnectionRequest.setURLQueries(urlParams);
        this.service = stackInstance.service;
        csConnectionRequest.setParams(completeUrl, headers, controller, requestInfo, callback, this.service);

    }

    protected CSBackgroundTask(Query queryInstance, Stack stackInstance, String controller, String url,
                               LinkedHashMap<String, Object> headers, HashMap<String, Object> urlQueries, String requestInfo,
                               ResultCallBack callback) {
        checkHeader(headers);
        String completeUrl = stackInstance.config.getEndpoint() + url;
        CSConnectionRequest csConnectionRequest = new CSConnectionRequest(queryInstance);
        csConnectionRequest.setQueryInstance(queryInstance);
        csConnectionRequest.setURLQueries(urlQueries);
        this.service = stackInstance.service;
        csConnectionRequest.setParams(completeUrl, headers, controller, requestInfo, callback, this.service);

    }

    protected CSBackgroundTask(Entry entryInstance, Stack stackInstance, String controller, String url,
                               LinkedHashMap<String, Object> headers, HashMap<String, Object> urlQueries, String requestInfo,
                               ResultCallBack callBack) {
        checkHeader(headers);
        String completeUrl = stackInstance.config.getEndpoint() + url;
        CSConnectionRequest csConnectionRequest = new CSConnectionRequest(entryInstance);
        csConnectionRequest.setURLQueries(urlQueries);
        this.service = stackInstance.service;
        csConnectionRequest.setParams(completeUrl, headers, controller, requestInfo, callBack, this.service);
    }

    protected CSBackgroundTask(AssetLibrary assetLibrary, Stack stackInstance, String controller, String url,
                               LinkedHashMap<String, Object> headers, HashMap<String, Object> urlQueries, String requestInfo,
                               ResultCallBack callback) {
        checkHeader(headers);
        String completeUrl = stackInstance.config.getEndpoint() + url;
        CSConnectionRequest csConnectionRequest = new CSConnectionRequest(assetLibrary);
        csConnectionRequest.setURLQueries(urlQueries);
        this.service = stackInstance.service;
        csConnectionRequest.setParams(completeUrl, headers, controller, requestInfo, callback, this.service);

    }

    protected CSBackgroundTask(Asset asset, Stack stackInstance, String controller, String url,
                               LinkedHashMap<String, Object> headers, HashMap<String, Object> urlQueries, String requestInfo,
                               ResultCallBack callback) {
        checkHeader(headers);
        String completeUrl = stackInstance.config.getEndpoint() + url;
        CSConnectionRequest csConnectionRequest = new CSConnectionRequest(asset);
        csConnectionRequest.setURLQueries(urlQueries);
        this.service = stackInstance.service;
        csConnectionRequest.setParams(completeUrl, headers, controller, requestInfo, callback, this.service);
    }

    protected CSBackgroundTask(ContentType contentType, Stack stackInstance, String controller, String url,
                               HashMap<String, Object> headers, HashMap<String, Object> urlParams, String requestInfo,
                               ResultCallBack callback) {
        checkHeader(headers);
        String completeUrl = stackInstance.config.getEndpoint() + url;
        CSConnectionRequest csConnectionRequest = new CSConnectionRequest(contentType);
        csConnectionRequest.setURLQueries(urlParams);
        this.service = stackInstance.service;
        csConnectionRequest.setParams(completeUrl, headers, controller, requestInfo, callback, this.service);
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
