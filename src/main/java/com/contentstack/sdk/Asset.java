package com.contentstack.sdk;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import retrofit2.Retrofit;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

import static com.contentstack.sdk.Constants.ENVIRONMENT;
import static com.contentstack.sdk.Constants.parseDate;

/**
 * <a href=
 * "https://www.contentstack.com/docs/content-managers/working-with-assets/about-assets">Assets</a>
 * refer to all the media files (images, videos, PDFs, audio files, and so on)
 * uploaded in your Contentstack repository
 * for future use. These files can be attached and used in multiple entries.
 * <p>
 * You can now pass the branch header in the API request to fetch or manage
 * modules located within specific branches of
 * the stack.
 *
 * @author Shailesh Mishra
 * @version 1.0.0
 * @since 01-11-2017
 */
public class Asset {

    protected static final Logger logger = Logger.getLogger(Asset.class.getSimpleName());
    protected final JSONObject urlQueries = new JSONObject();
    protected String assetUid = null;
    protected String contentType = null;
    protected String fileSize = null;
    protected String fileName = null;
    protected String uploadUrl = null;
    protected JSONObject json = null;
    protected String[] tagsArray = null;
    protected LinkedHashMap<String, Object> headers;
    protected Stack stackInstance;
    protected Retrofit retrofit;

    protected Asset() {
        this.headers = new LinkedHashMap<>();
    }

    protected Asset(@NotNull String assetUid) {
        this.assetUid = assetUid;
        this.headers = new LinkedHashMap<>();
    }

    protected void setStackInstance(@NotNull Stack stack) {
        this.stackInstance = stack;
        this.headers = stack.headers;
    }

    /**
     * Configure asset.
     *
     * @param jsonObject
     *                   the json object
     * @return the asset
     */
    public Asset configure(JSONObject jsonObject) {
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

    /**
     * Sets header.
     *
     * @param headerKey
     *                    the header key
     * @param headerValue
     *                    the header value
     *
     *                    <br>
     *                    <br>
     *                    <b>Example :</b><br>
     *
     *                    <pre class="prettyprint">
     *                    Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *                    Asset asset = stack.asset(asset_uid);
     *                    asset.setHeader();
     *                    </pre>
     */
    public void setHeader(@NotNull String headerKey, @NotNull String headerValue) {
        headers.put(headerKey, headerValue);
    }

    /**
     * Remove header.
     *
     * @param headerKey
     *                  the header key
     *
     *                  <br>
     *                  <br>
     *                  <b>Example :</b><br>
     *
     *                  <pre class="prettyprint">
     *                  Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *                  Asset asset = stack.asset(asset_uid);
     *                  asset.removeHeader();
     *                  </pre>
     */
    public void removeHeader(@NotNull String headerKey) {
        headers.remove(headerKey);
    }

    protected void setUid(@NotNull String assetUid) {
        if (!assetUid.isEmpty()) {
            this.assetUid = assetUid;
        }
    }

    /**
     * Gets asset uid.
     *
     * @return the asset uid
     *
     *         <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Asset asset = stack.asset(asset_uid);
     *         asset.fetch(new FetchResultCallback() {
     *             &#64;Override
     *             public void onCompletion(ResponseType responseType, Error error) {
     *                 asset.getAssetUid();
     *             }
     *         });
     *
     *         </pre>
     */
    public String getAssetUid() {
        return assetUid;
    }

    /**
     * Gets file type.
     *
     * @return the file type
     *
     *         <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Asset asset = stack.asset(asset_uid);
     *         asset.fetch(new FetchResultCallback() {
     *             &#64;Override
     *             public void onCompletion(ResponseType responseType, Error error) {
     *                 asset.getFileType();
     *             }
     *         });
     *
     *         </pre>
     */
    public String getFileType() {
        return contentType;
    }

    /**
     * Gets file size.
     *
     * @return the file size
     *
     *         <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Asset asset = stack.asset(asset_uid);
     *         asset.fetch(new FetchResultCallback() {
     *             &#64;Override
     *             public void onCompletion(ResponseType responseType, Error error) {
     *                 asset.getFileSize();
     *             }
     *         });
     *
     *         </pre>
     */
    public String getFileSize() {
        return fileSize;
    }

    /**
     * Gets file name.
     *
     * @return the file name
     *
     *         <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Asset asset = stack.asset(asset_uid);
     *         asset.fetch(new FetchResultCallback() {
     *             &#64;Override
     *             public void onCompletion(ResponseType responseType, Error error) {
     *                 asset.getFileName();
     *             }
     *         });
     *
     *         </pre>
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Gets url.
     *
     * @return the url
     *
     *         <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Asset asset = stack.asset(asset_uid);
     *         asset.fetch(new FetchResultCallback() {
     *             &#64;Override
     *             public void onCompletion(ResponseType responseType, Error error) {
     *                 asset.getUrl();
     *             }
     *         });
     *
     *         </pre>
     */
    public String getUrl() {
        return uploadUrl;
    }

    /**
     * To json json object.
     *
     * @return the json object
     *
     *         <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Asset asset = stack.asset(asset_uid);
     *         asset.fetch(new FetchResultCallback() {
     *             &#64;Override
     *             public void onCompletion(ResponseType responseType, Error error) {
     *                 asset.toJSON();
     *             }
     *         });
     *
     *         </pre>
     */
    public JSONObject toJSON() {
        return json;
    }

    /**
     * Gets create at.
     *
     * @return the create at
     *
     *
     *         <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Asset asset = stack.asset(asset_uid);
     *         asset.fetch(new FetchResultCallback() {
     *             &#64;Override
     *             public void onCompletion(ResponseType responseType, Error error) {
     *                 asset.getCreateAt();
     *             }
     *         });
     *
     *         </pre>
     */
    public Calendar getCreateAt() {
        return parseDate(json.optString("created_at"), null);
    }

    /**
     * Gets created by.
     *
     * @return the created by
     *
     *
     *         <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Asset asset = stack.asset(asset_uid);
     *         asset.fetch(new FetchResultCallback() {
     *             &#64;Override
     *             public void onCompletion(ResponseType responseType, Error error) {
     *                 asset.getCreatedBy();
     *             }
     *         });
     *
     *         </pre>
     */
    public String getCreatedBy() {
        return json.optString("created_by");
    }

    /**
     * Gets update at.
     *
     * @return the update at
     *
     *
     *         <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Asset asset = stack.asset(asset_uid);
     *         asset.fetch(new FetchResultCallback() {
     *             &#64;Override
     *             public void onCompletion(ResponseType responseType, Error error) {
     *                 asset.getUpdateAt();
     *             }
     *         });
     *
     *         </pre>
     */
    public Calendar getUpdateAt() {
        return parseDate(json.optString("updated_at"), null);
    }

    /**
     * Gets updated by.
     *
     * @return the updated by
     *
     *
     *         <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Asset asset = stack.asset(asset_uid);
     *         asset.fetch(new FetchResultCallback() {
     *             &#64;Override
     *             public void onCompletion(ResponseType responseType, Error error) {
     *                 asset.getUpdatedBy();
     *             }
     *         });
     *
     *         </pre>
     */
    public String getUpdatedBy() {
        return json.optString("updated_by");
    }

    /**
     * Gets delete at.
     *
     * @return the delete at
     *
     *         <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Asset asset = stack.asset(asset_uid);
     *         asset.fetch(new FetchResultCallback() {
     *             &#64;Override
     *             public void onCompletion(ResponseType responseType, Error error) {
     *                 asset.getDeleteAt();
     *             }
     *         });
     *
     *         </pre>
     */
    public Calendar getDeleteAt() {
        return parseDate(json.optString("deleted_at"), null);
    }

    /**
     * Gets deleted by.
     *
     * @return the deleted by
     *
     *         <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Asset asset = stack.asset(asset_uid);
     *         asset.fetch(new FetchResultCallback() {
     *             &#64;Override
     *             public void onCompletion(ResponseType responseType, Error error) {
     *                 asset.getDeletedBy();
     *             }
     *         });
     *         </pre>
     */
    public String getDeletedBy() {
        return json.optString("deleted_by");
    }

    /**
     * Get tags string [ ].
     *
     * @return the string [ ]
     *
     *
     *         <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Asset asset = stack.asset(asset_uid);
     *         asset.fetch(new FetchResultCallback() {
     *             &#64;Override
     *             public void onCompletion(ResponseType responseType, Error error) {
     *                 asset.getTags();
     *             }
     *         });
     *
     *         </pre>
     */
    public String[] getTags() {
        return tagsArray;
    }

    protected Asset setTags(String[] tags) {
        tagsArray = tags;
        return this;
    }

    /**
     * Include dimension asset.
     *
     * @return the asset
     *
     *         <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Asset asset = stack.asset(asset_uid);
     *         asset.includeDimension();
     *         </pre>
     */
    public Asset includeDimension() {
        urlQueries.put("include_dimension", true);
        return this;
    }

    /**
     * Add param asset.
     *
     * @param paramKey
     *                   the param key
     * @param paramValue
     *                   the param value
     * @return the asset
     *
     *         <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Asset asset = stack.asset(asset_uid);
     *         asset.addParam();
     *         </pre>
     */
    public Asset addParam(@NotNull String paramKey, @NotNull String paramValue) {
        urlQueries.put(paramKey, paramValue);
        return this;
    }

    /**
     * Include fallback asset.
     *
     * @return the asset
     *
     *
     *         <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Asset asset = stack.asset(asset_uid);
     *         asset.includeFallback();
     *         </pre>
     */
    public Asset includeFallback() {
        urlQueries.put("include_fallback", true);
        return this;
    }

    /**
     * Includes Branch in the asset response
     *
     * @return {@link Asset} object, so you can chain this call. <br>
     *
     *         <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Asset asset = stack.asset(asset_uid);
     *         asset.includeBranch();
     *         </pre>
     */
    public Asset includeBranch() {
        urlQueries.put("include_branch", true);
        return this;
    }

    /**
     * Fetch.
     *
     * @param callback
     *                 the callback
     */
    public void fetch(FetchResultCallback callback) {
        urlQueries.put(ENVIRONMENT, this.headers.get(ENVIRONMENT));
        fetchFromNetwork("assets/" + assetUid, urlQueries, this.headers, callback);
    }

    private void fetchFromNetwork(String url, JSONObject urlQueries, LinkedHashMap<String, Object> headers,
            FetchResultCallback callback) {
        if (callback != null) {
            HashMap<String, Object> urlParams = getUrlParams(urlQueries);
            new CSBackgroundTask(this, stackInstance, Constants.FETCHASSETS, url, headers, urlParams,
                    Constants.REQUEST_CONTROLLER.ASSET.toString(), callback);
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
        }
        return hashMap;
    }

}
