package com.contentstack.sdk;

import com.contentstack.sdk.utility.CSAppConstants;
import com.contentstack.sdk.utility.CSController;
import com.contentstack.sdk.utility.ContentstackUtil;
import org.json.JSONObject;

import java.util.*;
import java.util.logging.Logger;


/**
 * Asset refer to  the media file (images, videos, PDFs, audio files, and so on)
 * uploaded to Contentstack. These files can be used in multiple entries.
 */

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
    protected LinkedHashMap<String, Object> headerGroup_app;
    protected LinkedHashMap<String, Object> headerGroup_local;
    protected Stack stackInstance;

    protected Asset() {
        this.headerGroup_local = new LinkedHashMap<>();
        this.headerGroup_app = new LinkedHashMap<>();

    }

    protected Asset(String assetUid) {
        this.assetUid = assetUid;
        this.headerGroup_local = new LinkedHashMap<>();
        this.headerGroup_app = new LinkedHashMap<>();
    }

    protected void setStackInstance(Stack stack) {
        this.stackInstance = stack;
        this.headerGroup_app = stack.localHeader;
    }


    /**
     * Creates new instance of {@link Asset} from valid {@link JSONObject}.
     * If JSON object is not appropriate then it will return null.
     *
     * @param jsonObject json object of particular file attached in the built object.<br>
     *                   {@link Asset} can be generate using of data filled {@link Entry}
     *                   and
     *                   {@link JSONObject}.<br>
     *
     *                   <br><br><b>Example :</b><br>
     *                   <br>1. Single Attachment :-<br>
     *                   <pre class="prettyprint linenums:1">
     *                    //'blt5d4sample2633b' is a dummy Application API key
     *                   Stack stack = Contentstack.stack("apiKey", "deliveryToken",  "environment");
     *                   Asset assetObject = stack.asset("assetUid");
     *                   assetObject.configure(entryObject.getJSONObject(attached_image_field_uid));</pre>
     *
     *                   <br>2. Multiple Attachment :-<br>
     *                   <pre class="prettyprint linenums:1">
     *                   JSONArray array = entryObject.getJSONArray(Attach_Image_Field_Uid);
     *                   {@code for (int i = 0; i < array.length(); i++)} {
     *                    	  Asset assetObject = stack.asset("assetUid");
     *                    	  assetObject.configure(entryObject.getJSONObject(attached_image_field_uid));
     *                      }
     *                   </pre>
     * @return {@link Asset} instance.
     */
    public Asset configure(JSONObject jsonObject) {
        AssetModel model = null;
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

    /**
     * To set headers for Contentstack rest calls.
     * <br>
     * Scope is limited to this object only.
     *
     * @param key   header name.
     * @param value header value against given header name.
     *              <br><br><b>Example :</b><br>
     *              <pre class="prettyprint">
     *              Asset assetObject = Contentstack.stack( "blt5d4sample2633b", "bltdtsample_accessToken767vv",  config).asset("assetUid");
     *              assetObject.setHeader("custom_header_key", "custom_header_value");
     *              </pre>
     */
    public void setHeader(String key, String value) {
        if (!key.isEmpty() && !value.isEmpty()) {
            removeHeader(key);
            headerGroup_local.put(key, value);
        }
    }

    /**
     * Remove a header for a given key from headers.
     * <br>
     * Scope is limited to this object only
     *
     * @param key header key.
     *            <br><br><b>Example :</b><br>
     *            <pre class="prettyprint">
     *            //'blt5d4sample2633b' is a dummy Application API key
     *            Asset assetObject = Contentstack.stack("blt5d4sample2633b", "bltdtsample_accessToken767vv",  config).asset("assetUid");
     *            assetObject.removeHeader("custom_header_key");
     *            </pre>
     */
    public void removeHeader(String key) {
        if (headerGroup_local != null) {
            if (!key.isEmpty()) {
                headerGroup_local.remove(key);
            }
        }
    }

    /**
     * To set uid of media file which is uploaded on Contentstack server.
     *
     * @param assetUid upload uid.
     *                 <br><br><b>Example :</b><br>
     *                 <pre class="prettyprint">
     *                 //'blt5d4sample2633b' is a dummy Application API key
     *                 Asset assetObject = Contentstack.stack("blt5d4sample2633b", "bltdtsample_accessToken767vv",  config).asset("assetUid");
     *                 assetObject.setUid("upload_uid");
     *                 </pre>
     */
    protected void setUid(String assetUid) {
        if (!assetUid.isEmpty()) {
            this.assetUid = assetUid;
        }
    }

    /**
     * @return String @assetUid
     * <br><br><b> Example :</b><br>
     * <pre class="prettyprint">
     * String uid = assetObject.getAssetUid();
     * return String of @uid
     * </pre>
     */
    public String getAssetUid() {
        return assetUid;
    }

    /**
     * @return String @contentType
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * String contentType = assetObject.getFileType();
     * </pre>
     */
    public String getFileType() {
        return contentType;
    }

    /**
     * @return String @fileSize
     * <br><b>Note :</b><br> file size will receive in bytes number.
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * String queueSize = assetObject.getFileSize();
     * </pre>
     */

    public String getFileSize() {
        return fileSize;
    }

    /**
     * @return String @fileName
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * String fileName = assetObject.getFileName();
     * </pre>
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @return String @uploadUrl by which you can download media file uploaded on Contentstack server.
     * You will get uploaded url after uploading media file on Contentstack server.
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * String url = assetObject.getUrl();
     * </pre>
     */
    public String getUrl() {
        return uploadUrl;
    }

    /**
     * @return JSON @json representation of this {@link Asset} instance data.
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     *  JSONObject json = assetObject.toJSON();
     * </pre>
     */
    public JSONObject toJSON() {
        return json;
    }

    /**
     * @return Calendar @{@link java.util.Date}
     * Get {@link Calendar} value of creation time of entry.
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * Calendar createdAt = assetObject.getCreateAt("key");
     * </pre>
     */

    public Calendar getCreateAt() {

        try {
            String value = json.optString("created_at");
            return ContentstackUtil.parseDate(value, null);
        } catch (Exception e) {
            logger.severe(e.getLocalizedMessage());
        }
        return null;
    }

    public String getCreatedBy() {
        return json.optString("created_by");
    }

    /**
     * Get {@link Calendar} value of updating time of entry.
     *
     * @return Calendar @{@link java.util.Date}
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * Calendar updatedAt = assetObject.getUpdateAt("key");
     * </pre>
     */

    public Calendar getUpdateAt() {

        try {
            String value = json.optString("updated_at");
            return ContentstackUtil.parseDate(value, null);
        } catch (Exception e) {
            logger.severe(e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * Get uid who updated this entry.
     *
     * @return String @getUpdatedBy
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * String updatedBy_uid = assetObject.getUpdatedBy();
     * </pre>
     */

    public String getUpdatedBy() {
        return json.optString("updated_by");
    }

    /**
     * Get {@link Calendar} value of deletion time of entry
     *
     * @return Calendar @{@link java.util.Date}
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * Calendar updatedAt = entry.getUpdateAt("key");
     * </pre>
     */
    public Calendar getDeleteAt() {

        try {
            String value = json.optString("deleted_at");
            return ContentstackUtil.parseDate(value, null);
        } catch (Exception e) {
            logger.severe(e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * Get uid who deleted this entry.
     *
     * @return String @getDeletedBy
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * String deletedBy_uid = assetObject.getDeletedBy();
     * </pre>
     */
    public String getDeletedBy() {
        return json.optString("deleted_by");
    }

    /**
     * Get tags.
     *
     * @return String @tagsArray
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * String[] tags = assetObject.getURL();
     * </pre>
     */
    public String[] getTags() {
        return tagsArray;
    }

    protected Asset setTags(String[] tags) {
        tagsArray = tags;
        return this;
    }

    /**
     * Include the dimensions (height and width) of the image in the response.
     * Supported image types: JPG, GIF, PNG, WebP, BMP, TIFF, SVG, and PSD
     *
     * @return Asset
     */
    public Asset includeDimension() {
        urlQueries.put("include_dimension", true);
        return this;
    }


    /**
     * Fetch a particular asset using uid.
     *
     * @param callback {@link FetchResultCallback} instance for success and failure result.
     *                 <br><br><b>Example :</b><br>
     *                 <pre class="prettyprint">
     *                  Asset asset = stack.asset("blt5312f71416d6e2c8");
     *                  asset.fetch(new FetchResultCallback() {
     *                    &#64;Override
     *                    public void onCompletion(ResponseType responseType, Error error) {
     *                          if(error == null){
     *                            //Success Block.
     *                          }else {
     *                            //Fail Block.
     *                          }
     *                    }
     *                  });
     *                 </pre>
     */

    public void fetch(FetchResultCallback callback) {
        try {
            String URL = "/" + stackInstance.VERSION + "/assets/" + assetUid;
            LinkedHashMap<String, Object> headers = getHeader(headerGroup_local);
            if (headers.containsKey("environment")) {
                urlQueries.put("environment", headers.get("environment"));
            }
            fetchFromNetwork(URL, urlQueries, headers, callback);
        } catch (Exception e) {
            Error error = new Error();
            error.setErrorMessage(CSAppConstants.ErrorMessage_JsonNotProper);
            callback.onRequestFail(ResponseType.UNKNOWN, error);
        }
    }


    private void fetchFromNetwork(String URL, JSONObject urlQueries, LinkedHashMap<String, Object> headers, FetchResultCallback callback) {
        if (callback != null) {
            HashMap<String, Object> urlParams = getUrlParams(urlQueries);
            new CSBackgroundTask(this, stackInstance, CSController.FETCHASSETS, URL, headers, urlParams, new JSONObject(), CSAppConstants.callController.ASSET.toString(), false, CSAppConstants.RequestMethod.GET, callback);
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
        LinkedHashMap<String, Object> mainHeader = headerGroup_app;
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
            return headerGroup_app;
        }
    }


    /**
     * This method adds key and value to an Entry.
     *
     * @param key   The key as string which needs to be added to an Asset
     * @param value The value as string which needs to be added to an Asset
     * @return {@link Asset}
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     *  final Asset asset = stack.asset("blt5312f71416d6e2c8");
     *  asset.addParam("key", "some_value");
     *  asset.fetch(new FetchResultCallback() {
     *    &#64;Override
     *    public void onCompletion(ResponseType responseType, Error error) {
     *          if(error == null){
     *            //Success Block.
     *          }else {
     *            //Fail Block.
     *          }
     *    }
     *  });
     * </pre>
     */
    public Asset addParam(String key, String value) {
        if (key != null && value != null) {
            urlQueries.put(key, value);
        }
        return this;
    }


    /**
     * Retrieve the published content of the fallback locale if an entry is not localized in specified locale
     *
     * @return {@link Asset} object, so you can chain this call.
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     *     Stack stack = Contentstack.stack("ApiKey", "deliveryToken", "environment");
     *     final Asset asset = stack.asset("asset_uid");
     *     asset.includeFallback();
     * </pre>
     */
    public Asset includeFallback() {
        urlQueries.put("include_fallback", true);
        return this;
    }

}
