package com.contentstack.sdk;


import org.json.JSONObject;

import java.util.*;
import java.util.logging.Logger;

import static com.contentstack.sdk.Constants.ENVIRONMENT;


public class Asset {

    private static final Logger logger = Logger.getLogger(Asset.class.getSimpleName());
    public JSONObject urlQueries = new JSONObject();
    protected String assetUid = null;
    protected String contentType = null;
    protected String fileSize = null;
    protected String fileName = null;
    protected String uploadUrl = null;
    protected JSONObject json = null;
    protected String[] tagsArray = null;
    protected LinkedHashMap<String, Object> headerGroupApp;
    protected LinkedHashMap<String, Object> headerGroupLocal;
    protected Stack stackInstance;

    protected Asset() {
        this.headerGroupLocal = new LinkedHashMap<>();
        this.headerGroupApp = new LinkedHashMap<>();

    }

    protected Asset(String assetUid) {
        this.assetUid = assetUid;
        this.headerGroupLocal = new LinkedHashMap<>();
        this.headerGroupApp = new LinkedHashMap<>();
    }

    protected void setStackInstance(Stack stack) {
        this.stackInstance = stack;
        this.headerGroupApp = stack.localHeader;
    }


    public Asset configure(JSONObject jsonObject) {
        AssetModel model;
        model = new AssetModel(jsonObject, true, false);
        this.contentType = model.contentType;
        this.fileSize = model.fileSize;
        this.uploadUrl = model.uploadUrl;
        this.fileName = model.fileName;
        this.json = model.json;
        this.assetUid = model.uploadedUid;
        this.setTags(model.tags);
        model = null;
        return this;
    }

    public void setHeader(String key, String value) {
        if (!key.isEmpty() && !value.isEmpty()) {
            removeHeader(key);
            headerGroupLocal.put(key, value);
        }
    }

    public void removeHeader(String key) {
        if (headerGroupLocal != null) {
            if (!key.isEmpty()) {
                headerGroupLocal.remove(key);
            }
        }
    }

    protected void setUid(String assetUid) {
        if (!assetUid.isEmpty()) {
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

        try {
            String value = json.optString("created_at");
            return Constants.parseDate(value, null);
        } catch (Exception e) {
            logger.severe(e.getLocalizedMessage());
        }
        return null;
    }

    public String getCreatedBy() {
        return json.optString("created_by");
    }

    public Calendar getUpdateAt() {

        try {
            String value = json.optString("updated_at");
            return Constants.parseDate(value, null);
        } catch (Exception e) {
            logger.severe(e.getLocalizedMessage());
        }
        return null;
    }

    public String getUpdatedBy() {
        return json.optString("updated_by");
    }

    public Calendar getDeleteAt() {

        try {
            String value = json.optString("deleted_at");
            return Constants.parseDate(value, null);
        } catch (Exception e) {
            logger.severe(e.getLocalizedMessage());
        }
        return null;
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


    public void fetch(FetchResultCallback callback) {
        try {
            String URL = "/" + stackInstance.VERSION + "/assets/" + assetUid;
            LinkedHashMap<String, Object> headers = getHeader(headerGroupLocal);
            if (headers.containsKey(ENVIRONMENT)) {
                urlQueries.put(ENVIRONMENT, headers.get(ENVIRONMENT));
            }
            fetchFromNetwork(URL, urlQueries, headers, callback);
        } catch (Exception e) {
            Error error = new Error();
            error.setErrorMessage(Constants.JSON_NOT_PROPER);
            callback.onRequestFail(ResponseType.UNKNOWN, error);
        }
    }


    private void fetchFromNetwork(String URL, JSONObject urlQueries, LinkedHashMap<String, Object> headers, FetchResultCallback callback) {
        if (callback != null) {
            HashMap<String, Object> urlParams = getUrlParams(urlQueries);
            new CSBackgroundTask(this, stackInstance, Constants.FETCHASSETS, URL, headers, urlParams, new JSONObject(), Constants.REQUEST_CONTROLLER.ASSET.toString(), false, Constants.REQUEST_METHOD.GET, callback);
        }
    }


    private HashMap<String, Object> getUrlParams(JSONObject urlQueriesJSON) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (urlQueriesJSON != null && urlQueriesJSON.length() > 0) {
            Iterator<String> iter = urlQueriesJSON.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    Object value = urlQueriesJSON.opt(key);
                    hashMap.put(key, value);
                } catch (Exception e) {
                    logger.severe(e.getLocalizedMessage());
                }
            }
            return hashMap;
        }
        return null;
    }


    private LinkedHashMap<String, Object> getHeader(LinkedHashMap<String, Object> localHeader) {
        LinkedHashMap<String, Object> mainHeader = headerGroupApp;
        LinkedHashMap<String, Object> classHeaders = new LinkedHashMap<>();

        if (localHeader != null && localHeader.size() > 0) {
            if (mainHeader != null && mainHeader.size() > 0) {
                for (Map.Entry<String, Object> entry : localHeader.entrySet()) {
                    String key = entry.getKey();
                    classHeaders.put(key, entry.getValue());
                }

                for (Map.Entry<String, Object> entry : mainHeader.entrySet()) {
                    String key = entry.getKey();
                    if (!classHeaders.containsKey(key)) {
                        classHeaders.put(key, entry.getValue());
                    }
                }
                return classHeaders;
            } else {
                return localHeader;
            }
        } else {
            return headerGroupApp;
        }
    }


    public Asset addParam(String key, String value) {
        if (key != null && value != null) {
            urlQueries.put(key, value);
        }
        return this;
    }


    public Asset includeFallback() {
        urlQueries.put("include_fallback", true);
        return this;
    }

}
