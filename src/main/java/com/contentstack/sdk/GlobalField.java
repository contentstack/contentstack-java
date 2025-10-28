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
    protected JSONObject params = new JSONObject();
    protected LinkedHashMap<String, Object> headers = null;

    protected GlobalField() {
        this.headers = new LinkedHashMap<>();
    }

    protected GlobalField(@NotNull String globalFieldUid) {
        this.globalFieldUid = globalFieldUid;
        this.headers = new LinkedHashMap<>();
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
        this.params.put("include_branch", true);
        return this;
    }

    public GlobalField includeGlobalFieldSchema() {
        this.params.put("include_global_field_schema", true);
        return this;
    }

    public void fetch(final GlobalFieldsCallback callback) throws IllegalAccessException {
        String urlString = "global_fields/" + globalFieldUid;
        if (globalFieldUid == null || globalFieldUid.isEmpty()) {
            throw new IllegalAccessException(ErrorMessages.GLOBAL_FIELD_UID_REQUIRED);
        }
        fetchGlobalFields(urlString, this.params, this.headers, callback);
    }

    public void findAll(final GlobalFieldsCallback callback) {
        String urlString = "global_fields";
        fetchGlobalFields(urlString, this.params, this.headers, callback);
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
