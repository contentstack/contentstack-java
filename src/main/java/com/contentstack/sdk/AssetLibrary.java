package com.contentstack.sdk;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.*;
import java.util.logging.Logger;

import static com.contentstack.sdk.Constants.ENVIRONMENT;

/**
 * The type Asset library.
 */
public class AssetLibrary implements INotifyClass {

    protected final Logger logger = Logger.getLogger(AssetLibrary.class.getSimpleName());
    protected final JSONObject urlQueries;
    protected Stack stackInstance;
    protected LinkedHashMap<String, Object> headers;
    protected FetchAssetsCallback callback;
    protected int count;

    protected AssetLibrary() {
        this.urlQueries = new JSONObject();
    }

    protected void setStackInstance(@NotNull Stack stack) {
        this.stackInstance = stack;
        this.headers = stack.headers;
    }

    /**
     * Sets header.
     *
     * @param headerKey   the header key
     * @param headerValue the header value
     */
    public void setHeader(@NotNull String headerKey, @NotNull String headerValue) {
        this.headers.put(headerKey, headerValue);
    }

    /**
     * Remove header.
     *
     * @param headerKey the header key
     */
    public void removeHeader(@NotNull String headerKey) {
        if (!headerKey.isEmpty()) {
            this.headers.remove(headerKey);
        }
    }

    /**
     * Sort asset library.
     *
     * @param keyOrderBy the key order by
     * @param orderby    the orderby
     * @return the asset library
     */
    public AssetLibrary sort(String keyOrderBy, ORDERBY orderby) {
        if (orderby == ORDERBY.ASCENDING) {
            urlQueries.put("asc", keyOrderBy);
        } else {
            urlQueries.put("desc", keyOrderBy);
        }
        return this;
    }

    /**
     * Include count asset library.
     *
     * @return the asset library
     */
    public AssetLibrary includeCount() {
        urlQueries.put("include_count", "true");
        return this;
    }

    /**
     * Include relative url asset library.
     *
     * @return the asset library
     */
    public AssetLibrary includeRelativeUrl() {
        urlQueries.put("relative_urls", "true");
        return this;
    }

    /**
     * Retrieve the published content of the fallback locale if an entry is not
     * localized in specified locale
     *
     * @return {@link AssetLibrary} object, so you can chain this call. <br>
     *         <br>
     *         <b>Example :</b><br>
     * 
     *         <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         AssetLibrary assetLibObject = stack.assetLibrary();
     *         AssetLibrary.includeFallback();
     *         </pre>
     */
    public AssetLibrary includeFallback() {
        urlQueries.put("include_fallback", true);
        return this;
    }

    /**
     * Gets count.
     *
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * Fetch all.
     *
     * @param callback the callback
     */
    public void fetchAll(FetchAssetsCallback callback) {
        this.callback = callback;
        urlQueries.put(ENVIRONMENT, headers.get(ENVIRONMENT));
        fetchFromNetwork("assets", urlQueries, headers, callback);
    }

    private void fetchFromNetwork(String url, JSONObject urlQueries, LinkedHashMap<String, Object> headers,
            FetchAssetsCallback callback) {
        if (callback != null) {
            HashMap<String, Object> urlParams = getUrlParams(urlQueries);
            new CSBackgroundTask(this, stackInstance, Constants.FETCHALLASSETS, url, headers, urlParams,
                    Constants.REQUEST_CONTROLLER.ASSETLIBRARY.toString(), callback);
        }
    }

    private HashMap<String, Object> getUrlParams(JSONObject urlQueriesJSON) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (urlQueriesJSON != null && urlQueriesJSON.length() > 0) {
            Iterator<String> iter = urlQueriesJSON.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                Object value = urlQueriesJSON.opt(key);
                hashMap.put(key, value);
            }
        }
        return hashMap;
    }

    @Override
    public void getResult(Object object, String controller) {
        logger.warning("No implementation required");
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

        if (callback != null) {
            callback.onRequestFinish(ResponseType.NETWORK, assets);
        }
    }

    /**
     * The enum Orderby.
     */
    public enum ORDERBY {
        ASCENDING, DESCENDING
    }

}
