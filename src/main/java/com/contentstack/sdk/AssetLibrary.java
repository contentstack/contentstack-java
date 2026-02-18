package com.contentstack.sdk;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.*;
import java.util.logging.Logger;

import static com.contentstack.sdk.Constants.ENVIRONMENT;

/**
 * The Asset library is used to get list of assets available in the stack, We can apply filters on the assets also. The
 * Get all assets request fetches the list of all the assets of a particular stack. It returns the content of each asset
 * in JSON format.
 */
public class AssetLibrary implements INotifyClass {

    protected static final Logger logger = Logger.getLogger(AssetLibrary.class.getSimpleName());
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

    //Sanitization of keys
    private boolean isValidKey(String key) {
        // Fixed regex: allow alphanumeric, underscore, dot, and square brackets at the end, escaped properly
        return key.matches("^[a-zA-Z0-9_.]+(\\[\\])?$");
    }

    //Sanitization of values
    private boolean isValidValue(Object value) {
        if(value instanceof String){
            return ((String) value).matches("^[a-zA-Z0-9_.\\-\\s]+$");
        }
        return true;
    }

    //Sanitization of values list
    private boolean isValidValueList(Object[] values) {
        for (Object value : values) {
            if (value instanceof String) {
                if (!((String) value).matches("^[a-zA-Z0-9_.\\-\\s]+$")) {
                    return false;
                }
            }
        }
        return true;
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
     * Retrieve the published content of the fallback locale if an entry is not localized in specified locale
     *
     * @return {@link AssetLibrary} object, so you can chain this call. <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
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
     * Retrieve Metadata in the response
     *
     * @return {@link AssetLibrary} object, so you can chain this call. <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         AssetLibrary assetLibObject = stack.includeOwner();
     *         AssetLibrary.includeMetadata();
     *         </pre>
     */
    public AssetLibrary includeMetadata() {
        urlQueries.put("include_metadata", true);
        return this;
    }

    /**
     * Sets the locale for asset queries.
     * <p>
     * This method allows you to specify a locale code, so asset results are returned 
     * for a particular language or region. If not explicitly set, the default locale 
     * configured in the stack will be used.
     *
     * @param locale The locale code to filter assets by (e.g., "en-us").
     * @return The {@link AssetLibrary} instance for method chaining.
     *
     * <b>Example:</b>
     * <pre class="prettyprint">
     * Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     * AssetLibrary assetLibrary = stack.assetLibrary();
     * assetLibrary.setLocale("en-us");
     * </pre>
     */
    public AssetLibrary setLocale(String locale) {
        urlQueries.put("locale",locale);
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
     * Add param assetlibrary.
     *
     * @param paramKey   the param key
     * @param paramValue the param value
     * @return the assetlibrary
     *
     *         <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         AssetLibrary assetLibObject = stack.assetlibrary();
     *         assetLibObject.addParam();
     *         </pre>
     */
    public AssetLibrary addParam(@NotNull String paramKey, @NotNull Object paramValue) {
        if (isValidKey(paramKey) && isValidValue(paramValue)) {
            urlQueries.put(paramKey, paramValue);
        } else {
            if (!isValidKey(paramKey)) {
                logger.warning(ErrorMessages.INVALID_PARAMETER_KEY);
            } else {
                logger.warning(ErrorMessages.INVALID_PARAMETER_VALUE);
            }
        }
        return this;
    }

    /**
     * Remove param key assetlibrary.
     *
     * @param paramKey   the param key
     * @return the assetlibrary
     *
     *         <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         AssetLibrary assetLibObject = stack.assetlibrary();
     *         assetLibObject.removeParam(paramKey);
     *         </pre>
     */
    public AssetLibrary removeParam(@NotNull String paramKey){
         if(isValidKey(paramKey)) {
            if(urlQueries.has(paramKey)){
                urlQueries.remove(paramKey);
            }
        } else {
            logger.warning("Invalid key");
        }
        return this;
    }

    

    /**
     * The number of objects to skip before returning any.
     *
     * @param number No of objects to skip from returned objects
     * @return {@link Query} object, so you can chain this call.
     * <p>
     * <b> Note: </b> The skip parameter can be used for pagination,
     * &#34;skip&#34; specifies the number of objects to skip in the response. <br>
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *          Stack stack = Contentstack.stack( "apiKey", "deliveryToken", "environment");
     *          AssetLibrary assetLibObject = stack.assetlibrary.skip(4);<br>
     *         </pre>
     */

    public AssetLibrary skip (@NotNull int number) {
        urlQueries.put("skip",number);
        return this;
    }

    /**
     * A limit on the number of objects to return.
     *
     * @param number No of objects to limit.
     * @return {@link Query} object, so you can chain this call.
     * <p>
     * <b> Note:</b> The limit parameter can be used for pagination, &#34;
     * limit&#34; specifies the number of objects to limit to in the response. <br>
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *          Stack stack = Contentstack.stack( "apiKey", "deliveryToken", "environment");
     *           AssetLibrary assetLibObject = stack.assetlibrary.limit(4);<br>
     *         </pre>
     */

    public AssetLibrary limit (@NotNull int number) {
        urlQueries.put("limit", number);
        return this;
    }

    public AssetLibrary assetFields(String... fields) {
        if (fields != null && fields.length > 0) {
            JSONArray array = new JSONArray();
            for (String field : fields) {
                array.put(field);
            }
            if (!array.isEmpty()) {
                urlQueries.put("asset_fields[]", array);
            }
        }
        return this;
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
                if(isValidKey(key) && isValidValue(value)) {
                    hashMap.put(key, value);
                }
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

        if (objects == null || objects.isEmpty()) {
            logger.warning(ErrorMessages.MISSING_ASSETS_LIST);
            return;
        }

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
        else {
            logger.warning(ErrorMessages.INVALID_OBJECT_TYPE_ASSET_MODEL);
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

    public AssetLibrary where(String key, String value) {
        if(isValidKey(key) && isValidValue(value)){  
            JSONObject queryParams = new JSONObject();
            queryParams.put(key,value);
            urlQueries.put("query", queryParams);
        } else {
            if (!isValidKey(key)) {
                throw new IllegalArgumentException(ErrorMessages.INVALID_PARAMETER_KEY);
            } else {
                throw new IllegalArgumentException(ErrorMessages.INVALID_PARAMETER_VALUE);
            }
        }
        return this;
    }
 
}
