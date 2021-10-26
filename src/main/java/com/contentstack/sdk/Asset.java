package com.contentstack.sdk;


import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

import static com.contentstack.sdk.Constants.ENVIRONMENT;
import static com.contentstack.sdk.Constants.parseDate;


public class Asset {

    protected final Logger logger = Logger.getLogger(Asset.class.getSimpleName());
    protected final JSONObject urlQueries = new JSONObject();
    protected String assetUid = null;
    protected String contentType = null;
    protected String fileSize = null;
    protected String fileName = null;
    protected String uploadUrl = null;
    protected JSONObject json = null;
    protected String[] tagsArray = null;
    //protected LinkedHashMap<String, Object> headerGroupApp;
    protected LinkedHashMap<String, Object> headers;
    protected Stack stackInstance;

    protected Asset() {
        this.headers = new LinkedHashMap<>();
        //this.headerGroupApp = new LinkedHashMap<>();
    }

    protected Asset(@NotNull String assetUid) {
        this.assetUid = assetUid;
        this.headers = new LinkedHashMap<>();
        //this.headerGroupApp = new LinkedHashMap<>();
    }

    protected void setStackInstance(@NotNull Stack stack) {
        this.stackInstance = stack;
        this.headers = stack.headers;
    }


    public Asset configure(@NotNull JSONObject jsonObject) {
        AssetModel model;
        model = new AssetModel(jsonObject, true);
        this.contentType = model.contentType;
        this.fileSize = model.fileSize;
        this.uploadUrl = model.uploadUrl;
        this.fileName = model.fileName;
        this.json = model.json;
        this.assetUid = model.uploadedUid;
        this.setTags(model.tags);
        return this;
    }

    public void setHeader(@NotNull String headerKey,
                          @NotNull String headerValue) {
        headers.put(headerKey, headerValue);
    }

    public void removeHeader(@NotNull String headerKey) {
        headers.remove(headerKey);
    }

    protected void setUid(@NotNull String assetUid) {
        if (!assetUid.isBlank()) {
            this.assetUid = assetUid;
        }
    }

    public String getAssetUid() {
        return assetUid;
    }

    public String getFileType() {
        return contentType;
    }

    public String getFileSize() {
        return fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public String getUrl() {
        return uploadUrl;
    }

    public JSONObject toJSON() {
        return json;
    }

    public Calendar getCreateAt() {
        return parseDate(json.optString("created_at"), null);
    }

    public String getCreatedBy() {
        return json.optString("created_by");
    }

    public Calendar getUpdateAt() {
        return parseDate(json.optString("updated_at"), null);
    }

    public String getUpdatedBy() {
        return json.optString("updated_by");
    }

    public Calendar getDeleteAt() {
        return parseDate(json.optString("deleted_at"), null);
    }

    public String getDeletedBy() {
        return json.optString("deleted_by");
    }

    public String[] getTags() {
        return tagsArray;
    }

    protected Asset setTags(String[] tags) {
        tagsArray = tags;
        return this;
    }

    public Asset includeDimension() {
        urlQueries.put("include_dimension", true);
        return this;
    }

    public Asset addParam(@NotNull String paramKey,
                          @NotNull String paramValue) {
        urlQueries.put(paramKey, paramValue);
        return this;
    }


    public Asset includeFallback() {
        urlQueries.put("include_fallback", true);
        return this;
    }

    public void fetch(FetchResultCallback callback) {
        //LinkedHashMap<String, Object> headerMap = getHeader(this.headers);
        if (this.headers.containsKey(ENVIRONMENT)) {
            urlQueries.put(ENVIRONMENT, this.headers.get(ENVIRONMENT));
        }
        fetchFromNetwork("assets/" + assetUid, urlQueries, this.headers, callback);
    }


    private void fetchFromNetwork(String url, JSONObject urlQueries, LinkedHashMap<String, Object> headers, FetchResultCallback callback) {
        if (callback != null) {
            HashMap<String, Object> urlParams = getUrlParams(urlQueries);
            new CSBackgroundTask(this, stackInstance, Constants.FETCHASSETS, url, headers, urlParams, new JSONObject(), Constants.REQUEST_CONTROLLER.ASSET.toString(), false, Constants.REQUEST_METHOD.GET, callback);
        }
    }


    private HashMap<String, Object> getUrlParams(JSONObject urlQueriesJSON) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (urlQueriesJSON != null && urlQueriesJSON.length() > 0) {
            Iterator<String> keyArrays = urlQueriesJSON.keys();
            while (keyArrays.hasNext()) {
                String key = keyArrays.next();
                Object value = urlQueriesJSON.opt(key);
                hashMap.put(key, value);
            }
            return hashMap;
        }
        return hashMap;
    }


//    private LinkedHashMap<String, Object> getHeader(@NotNull LinkedHashMap<String, Object> localHeader) {
//        LinkedHashMap<String, Object> mainHeader = headerGroupApp;
//        LinkedHashMap<String, Object> classHeaders = new LinkedHashMap<>();
//
//        if (localHeader.size() > 0) {
//            if (mainHeader.size() > 0) {
//                for (Map.Entry<String, Object> entry : localHeader.entrySet()) {
//                    String key = entry.getKey();
//                    classHeaders.put(key, entry.getValue());
//                }
//                for (Map.Entry<String, Object> entry : mainHeader.entrySet()) {
//                    String key = entry.getKey();
//                    classHeaders.putIfAbsent(key, entry.getValue());
//                }
//                return classHeaders;
//            } else {
//                return localHeader;
//            }
//        } else {
//            return headerGroupApp;
//        }
//    }


}
