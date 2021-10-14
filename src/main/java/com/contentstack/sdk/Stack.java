package com.contentstack.sdk;

import com.contentstack.sdk.utility.CSAppConstants;
import com.contentstack.sdk.utility.CSController;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * A stack is a repository or a container that holds all the content/assets of your site. It allows multiple users to
 * create, edit, approve, and publish their content within a single space.
 */

public class Stack {

    private final Logger logger = Logger.getLogger(Stack.class.getSimpleName());
    protected LinkedHashMap<String, Object> localHeader = null;
    protected String URL_SCHEMA = "https://";
    protected String URL = "cdn.contentstack.io";
    protected String VERSION = "v3";
    protected Config config;
    protected String SYNC_KEY = "sync";
    protected String content_type;
    protected String sync_token = null;
    protected String pagination_token = null;
    protected String contentType;
    protected String localeCode;
    protected PublishType publishType;
    protected String start_from_date;
    protected HashMap<String, Object> headerGroup_app;
    //    protected String livePreviewHash;
//    protected String livePreviewContentType;
    private String stackApiKey = null;
    private String imageTransformationUrl;
    private LinkedHashMap<String, Object> imageParams = new LinkedHashMap<>();
    private JSONObject syncParams = null;
    private SyncResultCallBack syncCallBack;

    private Stack() {
    }

    protected Stack(String stackApiKey) {
        this.stackApiKey = stackApiKey;
        this.localHeader = new LinkedHashMap<>();
    }

    /**
     * @param query
     *         : HashMap contains query information
     * @return Stack
     */
    public Stack livePreviewQuery(HashMap<String, String> query) {
        // split requestStr to strings
        // str will contain content type and #hash
        // send management token and hash to the header to the request with access_token and env
        if (this.config.enableLivePreview) {
            config.livePreviewHash = query.get("live_preview");
            config.livePreviewContentType = query.get("content_type_uid");
        }
        return this;
    }

    protected void setConfig(Config config) {
        this.config = config;
        URL_SCHEMA = config.URL_SCHEMA;
        URL = config.URL;
        VERSION = config.VERSION;

        if (!config.environment.isEmpty()) {
            setHeader("environment", config.environment);
        }

        if (!config.region.name().isEmpty()) {
            String region = config.region.name().toLowerCase();
            if (!region.equalsIgnoreCase("us")) {
                if (URL.equalsIgnoreCase("cdn.contentstack.io")) {
                    URL = "cdn.contentstack.com";
                }
                URL = region + "-" + URL;
            }
        }

        try {
            if (config.enableLivePreview) { // enables the livePreview functionality
                // Validate all others required params are available
                if (config.managementToken == null || config.managementToken.isEmpty()) {
                    throw new IllegalAccessException("managementToken is required");
                }
                if (config.livePreviewHost == null || config.livePreviewHost.isEmpty()) {
                    throw new IllegalAccessException("host is required");
                }

                URL = config.livePreviewHost;
            }
        } catch (Exception e) {
            String info = "To enable live preview, managementToken and host are required";
            logger.warning(info);
            throw new IllegalArgumentException(e.getLocalizedMessage());
        }


    }

    public ContentType contentType(String contentTypeName) {
        this.content_type = contentType;
        ContentType contentType = new ContentType(contentTypeName);
        contentType.setStackInstance(this);
        return contentType;
    }

    /**
     * Takes asset uid as a parameter and returns @{@link Asset} instance
     *
     * @param uid
     *         uid of {@link Asset}
     * @return Asset instance
     */
    public Asset asset(String uid) {
        Asset asset = new Asset(uid);
        asset.setStackInstance(this);
        return asset;
    }

    protected Asset asset() {
        Asset asset = new Asset();
        asset.setStackInstance(this);
        return asset;
    }

    /**
     * assetLibrary returns AssetLibrary instance
     *
     * @return AssetLibrary
     */
    public AssetLibrary assetLibrary() {
        AssetLibrary library = new AssetLibrary();
        library.setStackInstance(this);
        return library;
    }

    /**
     * Returns apiKey of particular stack
     *
     * @return stack api key
     */
    public String getApplicationKey() {
        return stackApiKey;
    }

    /**
     * Returns accessToken of particular stack
     *
     * @return access token of particular stack
     */
    public String getAccessToken() {
        return localHeader != null ? (String) localHeader.get("access_token") : null;
    }

    /**
     * Removes Header by key
     *
     * @param key
     *         header key
     *         <br><br><b>Example :</b><br>
     *         stack.removeHeader("delivery_token");
     *         <br><br>
     *         </p>
     */
    public void removeHeader(String key) {
        if (!key.isEmpty()) {
            localHeader.remove(key);
        }
    }

    ;

    /**
     * Adds header to the stack by key and value
     *
     * @param key
     *         header key
     * @param value
     *         header value
     *         <p>
     *         Example stack.setHeader("delivery_token","blt843748744");
     *         </p>
     */
    public void setHeader(String key, String value) {
        if (!key.isEmpty() && !value.isEmpty()) {
            localHeader.put(key, value);
        }
    }

    /**
     * @param image_url
     *         on which we want to manipulate.
     * @param parameters
     *         It is an second parameter in which we want to place different manipulation key and value in array form
     * @return String
     * <p>
     * ImageTransform function is define for image manipulation with different parameters in second parameter in array
     * form
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     *  //'blt5d4sample2633b' is a dummy Stack API key
     *  //'blt6d0240b5sample254090d' is dummy access token.
     *  Stack stack = Contentstack.stack("blt5d4sample2633b", "blt6d0240b5sample254090d", "stag", false);<br>
     *  // resize the image by specifying width and height
     *  LinkedHashMap imageParams = new LinkedHashMap();
     *  imageParams.put("width", 100);
     *  imageParams.put("height",100);
     *  imageUrl = Stack.ImageTransform(image_url, parameters);
     *  stack.ImageTransform(image_url, parameters);
     *  </pre>
     */
    public String ImageTransform(String image_url, LinkedHashMap<String, Object> parameters) {
        imageTransformationUrl = image_url;
        imageParams = parameters;
        return getImageUrl();
    }

    private String getImageUrl() {

        if (imageParams == null || imageParams.size() == 0) {
            return imageTransformationUrl;
        }

        imageParams.forEach((key, value) -> {

            try {
                final String encodedKey = URLEncoder.encode(key.toString(), "UTF-8");
                final String encodedValue = URLEncoder.encode(value.toString(), "UTF-8");
                if (!imageTransformationUrl.contains("?")) {
                    imageTransformationUrl += "?" + encodedKey + "=" + encodedValue;
                } else {
                    imageTransformationUrl += "&" + encodedKey + "=" + encodedValue;
                }

            } catch (UnsupportedEncodingException e) {
                logger.severe(e.getLocalizedMessage());
            }
        });

        return imageTransformationUrl;
    }

    /**
     * @param params
     *         query parameters
     * @param callback
     *         ContentTypesCallback This call returns comprehensive information of all the content types available in a
     *         particular stack in your account.
     *
     *         <br><br><b>Example :</b><br>
     *         <pre class="prettyprint">
     *
     *         JSONObject params = new JSONObject();
     *         params.put("include_snippet_schema", true);
     *         params.put("limit", 3);
     *         stack.getContentTypes(new ContentTypesCallback() {
     *         public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
     *           if (error == null){
     *            // do your stuff.
     *         }
     *         }
     *         });
     *         </pre>
     */

    public void getContentTypes(JSONObject params, final ContentTypesCallback callback) {

        try {
            String URL = "/" + this.VERSION + "/content_types";
            HashMap<String, Object> headers = getHeader(localHeader);
            if (params == null) {
                params = new JSONObject();
            }

            Iterator keys = params.keys();
            while (keys.hasNext()) {
                // loop to get the dynamic key
                String key = (String) keys.next();
                // get the value of the dynamic key
                Object value = params.opt(key);
                // do something here with the value...
                params.put(key, value);
            }

            if (headers.containsKey("environment")) {
                params.put("environment", headers.get("environment"));
                params.put("include_count", true);
            }

            fetchContentTypes(URL, params, headers, callback);

        } catch (Exception e) {

            Error error = new Error();
            error.setErrorMessage(CSAppConstants.ErrorMessage_JsonNotProper);
            callback.onRequestFail(ResponseType.UNKNOWN, error);
        }
    }

    /**
     * @param syncCallBack
     *         returns callback for sync result. The Sync request performs a complete sync of your app data. It returns
     *         all the published entries and assets of the specified stack in response. The response also contains a
     *         sync token, which you need to store, since this token is used to get subsequent delta updates later.
     *
     *         <br><br><b>Example :</b><br>
     *         <pre class="prettyprint">
     *
     *                                                                                 stack.sync(SyncResultCallBack syncCallBack){  }
     *                                                                                 </pre>
     */

    public void sync(SyncResultCallBack syncCallBack) {

        if (syncParams == null) {
            syncParams = new JSONObject();
        }
        try {
            syncParams.put("init", true);
        } catch (JSONException e) {
            logger.severe(e.getLocalizedMessage());
        }

        this.requestSync(syncCallBack);

    }

    /**
     * @param pagination_token
     *         If the response is paginated, use the pagination token under this parameter.
     * @param syncCallBack
     *         returns callback for sync result
     *         <p>
     *         If the result of the initial sync (or subsequent sync) contains more than 100 records, the response would
     *         be paginated. It provides pagination token in the response. However, you do not have to use the
     *         pagination token manually to get the next batch, the SDK does that automatically until the sync is
     *         complete. Pagination token can be used in case you want to fetch only selected batches. It is especially
     *         useful if the sync process is interrupted midway (due to network issues, etc.). In such cases, this token
     *         can be used to restart the sync process from where it was interrupted.
     *
     *         <br><br><b>Example :</b><br>
     *         <pre class="prettyprint">
     *                                                                                                                                 </pre>
     */
    public void syncPaginationToken(String pagination_token, SyncResultCallBack syncCallBack) {
        this.pagination_token = pagination_token;
        if (syncParams == null) {
            syncParams = new JSONObject();
        }

        try {
            syncParams.put("init", true);
            syncParams.put("pagination_token", pagination_token);
        } catch (JSONException e) {
            logger.severe(e.getLocalizedMessage());
        }

        this.requestSync(syncCallBack);
    }

    /**
     * @param sync_token
     *         Use the sync token that you received in the previous/initial sync under this parameter.
     * @param syncCallBack
     *         returns callback for sync result
     *         <p>
     *         You can use the sync token (that you receive after initial sync) to get the updated content next time.
     *         The sync token fetches only the content that was added after your last sync, and the details of the
     *         content that was deleted or updated.
     *
     *
     *         <br><br><b>Example :</b><br>
     *         <pre class="prettyprint">
     *
     *         //dummy sync_token = "blt28937206743728463";
     *         stack.syncToken(sync_token, new SyncResultCallBack() ){ }
     *         </pre>
     */
    public void syncToken(String sync_token, SyncResultCallBack syncCallBack) {

        this.sync_token = sync_token;
        if (syncParams == null) {
            syncParams = new JSONObject();
        }
        try {
            syncParams.put("init", true);
            syncParams.put("sync_token", sync_token);
        } catch (JSONException e) {
            logger.severe(e.getLocalizedMessage());
        }

        this.requestSync(syncCallBack);
    }

    /**
     * @param from_date
     *         Enter the start date for initial sync.
     * @param syncCallBack
     *         Returns callback for sync result.
     *         <p>
     *         You can also initialize sync with entries published after a specific date. To do this, use syncWithDate
     *         and specify the start date as its value.
     *
     *         <br><br><b>Example :</b><br>
     *         <pre class="prettyprint">
     *
     *         // dummy date final Date start_date = sdf.parse("2018-10-07");
     *         stack.syncFromDate(start_date, new SyncResultCallBack()) { }
     *         </pre>
     */
    public void syncFromDate(Date from_date, SyncResultCallBack syncCallBack) {
        start_from_date = convertUTCToISO(from_date);
        if (syncParams == null) {
            syncParams = new JSONObject();
        }

        try {
            syncParams.put("init", true);
            syncParams.put("start_from", start_from_date);
        } catch (JSONException e) {
            logger.severe(e.getLocalizedMessage());
        }
        this.requestSync(syncCallBack);
    }

    private String convertUTCToISO(Date date) {

        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        dateFormat.setTimeZone(tz);
        return dateFormat.format(date);
    }

    /**
     * @param content_type
     *         Provide uid of your content_type
     * @param syncCallBack
     *         Returns callback for sync result.
     *
     *         <p>You can also initialize sync with entries of only specific content_type.
     *         To do this, use syncContentType and specify the content type uid as its value. However, if you do this,
     *         the subsequent syncs will only include the entries of the specified content_type.
     *
     *         <br><br><b>Example :</b><br>
     *         <pre class="prettyprint">
     *
     *         // dummy content_type like "session"
     *         <pre>
     *         stack.syncContentType(String content_type, new SyncResultCallBack()){  }
     *         </pre>
     */
    public void syncContentType(String content_type, SyncResultCallBack syncCallBack) {

        this.contentType = content_type;
        if (syncParams == null) {
            syncParams = new JSONObject();
        }
        try {
            syncParams.put("init", true);
            syncParams.put("content_type_uid", contentType);
        } catch (JSONException e) {
            logger.severe(e.getLocalizedMessage());
        }

        this.requestSync(syncCallBack);
    }

    /**
     * @param language
     *         Select the required locale from the Language class.
     * @param syncCallBack
     *         Returns callback for sync result.
     *         <p>You can also initialize sync with entries of only specific locales.
     *         To do this, use syncLocale and specify the locale code as its value. However, if you do this, the
     *         subsequent syncs will only include the entries of the specified locales.
     *
     *         <br><br><b>Example :</b><br>
     *         <pre class="prettyprint">
     *
     *         // dummy language- Language.ENGLISH_UNITED_STATES
     *         stackInstance.syncLocale(Language.ENGLISH_UNITED_STATES, new SyncResultCallBack() ) { }
     *         </pre>
     */
    public void syncLocale(Language language, SyncResultCallBack syncCallBack) {
        this.localeCode = getLanguageCode(language);

        if (syncParams == null) {
            syncParams = new JSONObject();
        }
        try {
            syncParams.put("init", true);
            syncParams.put("locale", localeCode);
        } catch (JSONException e) {
            logger.severe(e.getLocalizedMessage());
        }


        this.requestSync(syncCallBack);
    }

    private String getLanguageCode(Language language) {

        String localeCode = null;
        if (language != null) {

            Language languageName = Language.valueOf(language.name());
            int localeValue = languageName.ordinal();
            LanguageCode[] languageCodeValues = LanguageCode.values();
            localeCode = languageCodeValues[localeValue].name();
            localeCode = localeCode.replace("_", "-");
        }

        return localeCode;
    }

    /**
     * @param type
     *         Use the type parameter to get a specific type of content like
     *         <p>(asset_published, entry_published, asset_unpublished,
     *         asset_deleted, entry_unpublished, entry_deleted, content_type_deleted.)
     * @param syncCallBack
     *         returns callback for sync result.
     *         <p>
     *         Use the type parameter to get a specific type of content. You can pass one of the following values:
     *         asset_published, entry_published, asset_unpublished, asset_deleted, entry_unpublished, entry_deleted,
     *         content_type_deleted. If you do not specify any value, it will bring all published entries and published
     *         assets.
     *
     *         <br><br><b>Example :</b><br>
     *         <pre class="prettyprint">
     *           stackInstance.syncPublishType(Stack.PublishType.entry_published, new SyncResultCallBack()) { }
     *           </pre>
     */

    public void syncPublishType(PublishType type, SyncResultCallBack syncCallBack) {
        this.publishType = type;
        if (syncParams == null) {
            syncParams = new JSONObject();
        }

        try {
            syncParams.put("init", true);
            syncParams.put("type", publishType);
        } catch (JSONException e) {
            logger.severe(e.getLocalizedMessage());
        }

        this.requestSync(syncCallBack);
    }

    /**
     * @param contentType
     *         your content type id
     * @param from_date
     *         start date
     * @param language
     *         language as {@link Language}
     * @param type
     *         type as PublishType
     * @param syncCallBack
     *         Callback
     *         <p>
     *         You can also initialize sync with entries that satisfy multiple parameters. To do this, use syncWith and
     *         specify the parameters. However, if you do this, the subsequent syncs will only include the entries of
     *         the specified parameters
     *
     *         <br><br><b>Example :</b><br>
     *         <pre class="prettyprint">
     *
     *                                                                                                              stackInstance.sync(String contentType, Date from_date, Language language, PublishType type,  SyncResultCallBack syncCallBack) { }
     *
     *
     *                                                                                                             </pre>
     */

    public void sync(String contentType, Date from_date, Language language, PublishType type, SyncResultCallBack syncCallBack) {
        start_from_date = convertUTCToISO(from_date);
        this.contentType = contentType;
        this.publishType = type;
        this.localeCode = getLanguageCode(language);

        if (syncParams == null) {
            syncParams = new JSONObject();
        }
        try {
            syncParams.put("init", true);
            syncParams.put("start_from", this.start_from_date);
            syncParams.put("content_type_uid", this.contentType);
            syncParams.put("type", publishType);
            syncParams.put("locale", this.localeCode);
        } catch (JSONException e) {
            logger.severe(e.getLocalizedMessage());
        }

        this.requestSync(syncCallBack);
    }

    private void requestSync(final SyncResultCallBack callback) {

        try {
            String URL = "/" + this.VERSION + "/stacks/" + SYNC_KEY;
            HashMap<String, Object> headers = getHeader(localHeader);
            JSONObject urlQueries = new JSONObject();
            if (headers.containsKey("environment")) {
                syncParams.put("environment", headers.get("environment"));
            }
            urlQueries = syncParams;
            fetchFromNetwork(URL, syncParams, headers, callback);

        } catch (Exception e) {
            Error error = new Error();
            error.setErrorMessage(CSAppConstants.ErrorMessage_JsonNotProper);
            callback.onRequestFail(ResponseType.UNKNOWN, error);
        }
    }

    private void fetchContentTypes(String urlString, JSONObject content_type_param, HashMap<String, Object> headers, ContentTypesCallback callback) {

        if (callback != null) {
            HashMap<String, Object> urlParams = getUrlParams(content_type_param);
            new CSBackgroundTask(this, CSController.FETCHCONTENTTYPES, urlString, headers, urlParams, new JSONObject(), CSAppConstants.callController.CONTENTTYPES.toString(), false, CSAppConstants.RequestMethod.GET, callback);
        }
    }

    private void fetchFromNetwork(String urlString, JSONObject urlQueries, HashMap<String, Object> headers, SyncResultCallBack callback) {

        if (callback != null) {

            HashMap<String, Object> urlParams = getUrlParams(urlQueries);
            new CSBackgroundTask(this, CSController.FETCHSYNC, urlString, headers, urlParams, new JSONObject(), CSAppConstants.callController.SYNC.toString(), false, CSAppConstants.RequestMethod.GET, callback);
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

    private HashMap<String, Object> getHeader(HashMap<String, Object> localHeader) {

        HashMap<String, Object> mainHeader = headerGroup_app;
        HashMap<String, Object> classHeaders = new HashMap<>();

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


    public static enum PublishType {
        entry_published,
        entry_unpublished,
        entry_deleted,
        asset_published,
        asset_unpublished,
        asset_deleted,
        content_type_deleted
    }


}

