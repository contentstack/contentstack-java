package com.contentstack.sdk;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

/**
 * This call returns information of a specific global field. It returns the
 * global field schema.
 *
 */
public class GlobalField {

    protected static final Logger logger = Logger.getLogger(GlobalField.class.getSimpleName());
    protected String globalFieldUid;
    protected Stack stackInstance = null;
    protected JSONObject params;
    protected LinkedHashMap<String, Object> headers = null;

    protected GlobalField() throws IllegalAccessException {
        throw new IllegalAccessException("Can Not Access Private Modifier");
    }

    protected GlobalField(String globalFieldUid) {
        this.globalFieldUid = globalFieldUid;
    }

    protected void setStackInstance(Stack stack) {
        this.stackInstance = stack;
        this.headers = stack.headers;
    }

    /**
     * Sets header on {@link Stack}.
     *
     * @param headerKey
     *                    the header key
     * @param headerValue
     *                    the header value
     */
    public void setHeader(String headerKey, String headerValue) {
        if (!headerKey.isEmpty() && !headerValue.isEmpty()) {
            this.headers.put(headerKey, headerValue);
        }
    }

    /**
     * Remove header from {@link Stack}
     *
     * @param headerKey
     *                  the header key
     */
    public void removeHeader(String headerKey) {
        if (!headerKey.isEmpty()) {
            this.headers.remove(headerKey);
        }
    }

    /**
     * Fetch.
     *
     * @param params
     *                 the params
     * @param callback
     *                 the callback
     * @throws IllegalAccessException
     *                                illegal access exception
     */

    public GlobalField includeBranch() {
        params.put("include_branch", false);
        return this;
    }

    public void fetch(@NotNull JSONObject params, final GlobalFieldsCallback callback) throws IllegalAccessException {
        String urlString = "global_fields/" + globalFieldUid;
        Iterator<String> keys = params.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = params.opt(key);
            params.put(key, value);
        }
        params.put("environment", headers.get("environment"));
        if (globalFieldUid == null || globalFieldUid.isEmpty()) {
            throw new IllegalAccessException("globalFieldUid is required");
        }
        fetchGlobalFields(urlString, params, headers, callback);
    }

    private void fetchGlobalFields(String urlString, JSONObject params, HashMap<String, Object> headers,
            GlobalFieldsCallback callback) {
        if (callback != null) {
            HashMap<String, Object> urlParams = getUrlParams(params);
            new CSBackgroundTask(this, stackInstance, Constants.FETCHGLOBALFIELDS, urlString, headers, urlParams,
                    Constants.REQUEST_CONTROLLER.GLOBALFIELDS.toString(), callback);
        }
    }

    private HashMap<String, Object> getUrlParams(JSONObject urlQueriesJSON) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (urlQueriesJSON != null && urlQueriesJSON.length() > 0) {
            Iterator<String> itStr = urlQueriesJSON.keys();
            while (itStr.hasNext()) {
                String key = itStr.next();
                Object value = urlQueriesJSON.opt(key);
                hashMap.put(key, value);
            }
        }
        return hashMap;
    }

}
