package com.contentstack.sdk;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

/**
 * <a href=
 * "https://www.contentstack.com/docs/developers/apis/content-delivery-api/#single-content-type">ContentType</a>
 * This call returns information of a specific content type. It returns the
 * content type schema, but does not include
 * its entries.
 *
 * @author Shailesh Mishra
 * @version 1.0.0
 * @since 01-11-2017
 */
public class ContentType {

    protected static final Logger logger = Logger.getLogger(ContentType.class.getSimpleName());
    protected String contentTypeUid = null;
    protected Stack stackInstance = null;
    protected LinkedHashMap<String, Object> headers = null;

    protected ContentType() throws IllegalAccessException {
        throw new IllegalAccessException("Can Not Access Private Modifier");
    }

    protected ContentType(String contentTypeUid) {
        this.contentTypeUid = contentTypeUid;
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
     * An entry is the actual piece of content created using one of the defined
     * content types.
     * <p>
     * You can now pass the branch header in the API request to fetch or manage
     * modules located within specific branches
     * of the stack.
     *
     * @param entryUid
     *                 the entry unique ID of the entry that you want to fetch.
     * @return the {@link Entry} entry.
     */
    public Entry entry(String entryUid) {
        Entry entry = new Entry(contentTypeUid);
        entry.setContentType(this, this.headers);
        entry.setUid(entryUid);
        return entry;
    }

    protected Entry entry() {
        Entry entry = new Entry(contentTypeUid);
        entry.headers = this.headers;
        entry.setContentType(this, this.headers);
        return entry;
    }

    /**
     * Query. The Get all entries request fetches the list of all the entries of a
     * particular content type. It returns
     * the content of each entry in JSON format. You need to specify the environment
     * and locale of which you want to get
     * the entries.
     *
     * <p>
     * If an entry is not published in a specific locale, make use of the
     * <b>include_fallback=true</b> query parameter to fetch the published content
     * from its fallback locale.
     * <p>
     * <b>Note:</b>If the fallback language of the specified locale is the master
     * language itself, this parameter would not be applicable.
     *
     * <p>
     * To include the publishing details in the response, make use of the
     * include_publish_details=true parameter. This
     * will return the publishing details of the entry in every environment along
     * with the version number that is
     * published in each of the environments. You can add other Queries to extend
     * the functionality of this API call.
     * Add a query parameter named query and provide your query (in JSON format) as
     * the value.
     *
     * @return the {@link Query}
     */
    public Query query() {
        Query query = new Query(contentTypeUid);
        query.headers = this.headers;
        query.setContentTypeInstance(this);
        return query;
    }

    /**
     * Fetch.
     *
     * @param params
     *                 the params
     * @param callback
     *                 the callback
     * @throws IllegalAccessException illegal access exception
     */
    public void fetch(@NotNull JSONObject params, final ContentTypesCallback callback) throws IllegalAccessException {
        String urlString = "content_types/" + contentTypeUid;
        Iterator<String> keys = params.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = params.opt(key);
            params.put(key, value);
        }
        params.put("environment", headers.get("environment"));
        if (contentTypeUid == null || contentTypeUid.isEmpty()) {
            throw new IllegalAccessException("contentTypeUid is required");
        }
        fetchContentTypes(urlString, params, headers, callback);
    }

    private void fetchContentTypes(String urlString, JSONObject params, HashMap<String, Object> headers,
            ContentTypesCallback callback) {
        if (callback != null) {
            HashMap<String, Object> urlParams = getUrlParams(params);
            new CSBackgroundTask(this, stackInstance, Constants.FETCHCONTENTTYPES, urlString, headers, urlParams,
                    Constants.REQUEST_CONTROLLER.CONTENTTYPES.toString(), callback);
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
