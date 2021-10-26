package com.contentstack.sdk;


import org.json.JSONObject;

import java.util.*;
import java.util.logging.Logger;

/**
 * Assets refer to all the media files (images, videos, PDFs, audio files, and so on) uploaded to Contentstack.
 * These files can be used in multiple entries.
 * Read more about [Assets](https://www.contentstack.com/docs/guide/content-management#working-with-assets)
 */
public class AssetLibrary implements INotifyClass {

    private static final Logger logger = Logger.getLogger(AssetLibrary.class.getSimpleName());
    public final JSONObject urlQueries;
    private Stack stackInstance;
    private LinkedHashMap<String, Object> stackHeader;
    private LinkedHashMap<String, Object> localHeader;
    private FetchAssetsCallback assetsCallback;
    private int count;


    protected AssetLibrary() {
        this.localHeader = new LinkedHashMap<>();
        this.urlQueries = new JSONObject();
    }

    protected void setStackInstance(Stack stack) {
        this.stackInstance = stack;
        this.stackHeader = stack.headers;
    }

    /**
     * To set headers for Contentstack rest calls.
     * <br>
     * Scope is limited to this object only.
     *
     * @param key   header name.
     * @param value header value against given header name.
     *
     *              <br><br><b>Example :</b><br>
     *              <pre class="prettyprint">
     *                                                                  //'blt5d4sample2633b' is a dummy Application API key
     *                                                                  AssetLibrary assetLibObject = Contentstack.stack("blt5d4sample2633b", "bltdtsample_accessToken767vv",  config).assetLibrary();
     *                                                                  assetLibObject.setHeader("custom_header_key", "custom_header_value");
     *                                                                  </pre>
     */
    public void setHeader(String key, String value) {
        if (!key.isEmpty() && !value.isEmpty()) {
            localHeader.put(key, value);
        }
    }

    /**
     * Remove a header for a given key from headers.
     * <br>
     * Scope is limited to this object only.
     *
     * @param key header key.
     *            <br><br><b>Example :</b><br>
     *            <pre class="prettyprint">
     *                                                        //'blt5d4sample2633b' is a dummy Application API key
     *                                                        AssetLibrary assetLibObject = Contentstack.stack("blt5d4sample2633b", "bltdtsample_accessToken767vv",  config).assetLibrary();
     *
     *                                                        assetLibObject.removeHeader("custom_header_key");
     *                                                        </pre>
     */
    public void removeHeader(String key) {
        if (!key.isEmpty()) {
            localHeader.remove(key);
        }
    }

    /**
     * Sort assets by fieldUid.
     *
     * @param key     field Uid.
     * @param orderby {@link ORDERBY} value for ascending or descending.
     * @return {@link AssetLibrary} object, so you can chain this call.
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * //'blt5d4sample2633b' is a dummy Application API key
     * AssetLibrary assetLibObject = Contentstack.stack("blt5d4sample2633b", "bltdtsample_accessToken767vv",  config).assetLibrary();
     * assetLibObject.sort("fieldUid", AssetLibrary.ORDERBY.ASCENDING);
     * </pre>
     */
    public AssetLibrary sort(String key, ORDERBY orderby) {
        if (orderby == ORDERBY.ASCENDING) {
            urlQueries.put("asc", key);
        } else {
            urlQueries.put("desc", key);
        }
        return this;
    }

    /**
     * Retrieve count and data of assets in result.
     *
     * @return {@link AssetLibrary} object, so you can chain this call.
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * //'blt5d4sample2633b' is a dummy Stack API key
     * //'bltdtsample_accessToken767vv' is dummy access token.
     * AssetLibrary assetLibObject = Contentstack.stack( "blt5d4sample2633b", "bltdtsample_accessToken767vv",  config).assetLibrary();
     * assetLibObject.includeCount();
     * </pre>
     */
    public AssetLibrary includeCount() {
        try {
            urlQueries.put("include_count", "true");
        } catch (Exception e) {
            throwException("includeCount");
        }
        return this;
    }

    /**
     * Retrieve relative urls objects in result.
     *
     * @return {@link AssetLibrary} object, so you can chain this call.
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * //'blt5d4sample2633b' is a dummy Stack API key
     * //'bltdtsample_accessToken767vv' is dummy access token.
     * AssetLibrary assetLibObject = Contentstack.stack( "blt5d4sample2633b", "bltdtsample_accessToken767vv",  config).assetLibrary();
     * assetLibObject.includeRelativeUrl();
     * </pre>
     */
    public AssetLibrary includeRelativeUrl() {
        try {
            urlQueries.put("relative_urls", "true");
        } catch (Exception e) {
            throwException("relative_urls");
        }
        return this;
    }

    /**
     * Get a count of assets in success callback of {@link FetchAssetsCallback}.
     *
     * @return int @count
     */
    public int getCount() {
        return count;
    }

    /**
     * Fetch a all asset.
     *
     * @param assetsCallback {@link FetchAssetsCallback} instance for success and failure result.
     *
     *                       <br><br><b>Example :</b><br>
     *                       <pre class="prettyprint">
     *                                                                                                                 AssetLibrary assetLibObject = Contentstack.stack("blt5d4sample2633b", "bltdtsample_accessToken767vv",  config).assetLibrary();     </pre>
     */
    public void fetchAll(FetchAssetsCallback assetsCallback) {
        try {
            this.assetsCallback = assetsCallback;
            String url = "v3/assets";
            LinkedHashMap<String, Object> headers = getHeader(localHeader);
            if (headers.containsKey(Constants.ENVIRONMENT)) {
                urlQueries.put(Constants.ENVIRONMENT, headers.get(Constants.ENVIRONMENT));
            }

            fetchFromNetwork(url, urlQueries, headers, assetsCallback);

        } catch (Exception e) {
            logger.severe(e.getLocalizedMessage());
        }

    }

    private void fetchFromNetwork(String url, JSONObject urlQueries, LinkedHashMap<String, Object> headers, FetchAssetsCallback assetsCallback) {
        if (assetsCallback != null) {
            HashMap<String, Object> urlParams = getUrlParams(urlQueries);
            new CSBackgroundTask(this, stackInstance, Constants.FETCHALLASSETS, url, headers, urlParams, new JSONObject(), Constants.REQUEST_CONTROLLER.ASSETLIBRARY.toString(), false, Constants.REQUEST_METHOD.GET, assetsCallback);
        }
    }

    /**
     * @param urlQueriesJSON takes {@link JSONObject} object as argeument
     * @return
     */

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

        return hashMap;
    }

    /**
     * @param messageString
     *         takes as Message
     */
    private void throwException(String messageString) {
        Error error = new Error();
        error.setErrorMessage(messageString);
    }

    private LinkedHashMap<String, Object> getHeader(LinkedHashMap<String, Object> localHeader) {
        LinkedHashMap<String, Object> mainHeader = stackHeader;
        LinkedHashMap<String, Object> classHeaders = new LinkedHashMap<>();

        if (localHeader != null && localHeader.size() > 0) {
            if (mainHeader != null && mainHeader.size() > 0) {
                for (Map.Entry<String, Object> entry : localHeader.entrySet()) {
                    String key = entry.getKey();
                    classHeaders.put(key, entry.getValue());
                }

                for (Map.Entry<String, Object> entry : mainHeader.entrySet()) {
                    String key = entry.getKey();
                    classHeaders.putIfAbsent(key, entry.getValue());
                }

                return classHeaders;

            } else {
                return localHeader;
            }

        } else {
            return stackHeader;
        }
    }

    @Override
    public void getResult(Object object, String controller) {
        // this method is empty
        logger.warning("this method is empty");
    }

    @Override
    public void getResultObject(List<Object> objects, JSONObject jsonObject, boolean isSingleEntry) {

        if (jsonObject != null && jsonObject.has("count")) {
            count = jsonObject.optInt("count");
        }

        List<Asset> assets = new ArrayList<>();

        if (objects != null && !objects.isEmpty()) {
            for (Object object : objects) {
                AssetModel model = (AssetModel) object;
                Asset asset = stackInstance.asset();

                asset.contentType = model.contentType;
                asset.fileSize = model.fileSize;
                asset.uploadUrl = model.uploadUrl;
                asset.fileName = model.fileName;
                asset.json = model.json;
                asset.assetUid = model.uploadedUid;
                asset.setTags(model.tags);
                assets.add(asset);
            }
        }

        if (assetsCallback != null) {
            assetsCallback.onRequestFinish(ResponseType.NETWORK, assets);
        }
    }

    /**
     * Retrieve the published content of the fallback locale if an entry is not localized in specified locale
     *
     * @return {@link AssetLibrary} object, so you can chain this call.
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     *     Stack stack = Contentstack.stack( "ApiKey", "deliveryToken",  environment_name);
     *     AssetLibrary assetLibObject = stack.assetLibrary();
     *     assetLibObject.includeFallback();
     * </pre>
     */
    public AssetLibrary includeFallback() {
        urlQueries.put("include_fallback", true);
        return this;
    }


    /**
     * Sorting order enum for {@link AssetLibrary}.
     *
     * @author Contentstack.com, Inc
     */
    public enum ORDERBY {
        ASCENDING,
        DESCENDING
    }

}
