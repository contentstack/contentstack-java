package com.contentstack.sdk;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.logging.Logger;

import static com.contentstack.sdk.Constants.ENVIRONMENT;

public class Entry {

    private final Logger logger = Logger.getLogger(Entry.class.getSimpleName());
    protected JSONObject params;
    protected LinkedHashMap<String, Object> headers = null;
    protected HashMap<String, Object> owner = null;
    protected String uid = null;
    protected JSONObject publishDetails;
    protected JSONObject resultJson = null;
    protected String title = null;
    protected String url = null;
    protected String language = null;
    protected String contentTypeUid;
    protected ContentType contentType = null;
    protected String[] tags = null;
    protected JSONArray referenceArray;
    protected JSONArray objectUidForOnly;
    protected JSONArray exceptFieldArray;
    protected JSONObject onlyJsonObject;
    protected JSONObject exceptJsonObject;
    protected String rteContent = null;

    protected Entry() throws IllegalAccessException {
        throw new IllegalAccessException("Can Not Access Private Modifier");
    }

    protected Entry(String contentTypeName) {
        this.contentTypeUid = contentTypeName;
        this.params = new JSONObject();
    }

    protected void setContentType(@NotNull ContentType contentType, @NotNull LinkedHashMap<String, Object> header) {
        this.contentType = contentType;
        this.headers = header;
    }

    public Entry configure(JSONObject jsonObject) {
        EntryModel model = new EntryModel(jsonObject);
        this.resultJson = model.jsonObject;
        this.title = model.title;
        this.url = model.url;
        this.language = model.language;
        this.rteContent = model.rteContent;
        this.uid = model.uid;
        this.publishDetails = model.publishDetails;
        this.setTags(model.tags);
        return this;
    }

    /**
     * Set headers.
     *
     * @param key   custom_header_key
     * @param value custom_header_value <br>
     *              <br>
     *              <b>Example :</b><br>
     * 
     *              <pre class="prettyprint">
     *              Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *              Entry entry = stack.contentType("form_name").entry("entry_uid");
     *              entry.setHeader("custom_header_key", "custom_header_value");
     *              </pre>
     */

    public void setHeader(String key, String value) {
        if (!key.isEmpty() && !value.isEmpty()) {
            this.headers.put(key, value);
        }
    }

    /**
     * Remove header key.
     *
     * @param key custom_header_key <br>
     *            <br>
     *            <b>Example :</b><br>
     * 
     *            <pre class="prettyprint">
     *            Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *            Entry entry = stack.contentType("form_name").entry("entry_uid");
     *            entry.removeHeader("custom_header_key");
     *            </pre>
     */

    public void removeHeader(String key) {
        if (!key.isEmpty()) {
            this.headers.remove(key);
        }
    }

    /**
     * Get title string
     *
     * @return String @title <br>
     *         <br>
     *         <b>Example :</b><br>
     * 
     *         <pre class="prettyprint">
     *         String title = entry.getTitle();
     *         </pre>
     */

    public String getTitle() {
        return title;
    }

    /**
     * Get url string
     *
     * @return String @url <br>
     *         <br>
     *         <b>Example :</b><br>
     * 
     *         <pre class="prettyprint">
     *         String url = entry.getURL();
     *         </pre>
     */

    public String getURL() {
        return url;
    }

    /**
     * Get tags.
     *
     * @return String @tags <br>
     *         <br>
     *         <b>Example :</b> <br>
     * 
     *         <pre class="prettyprint">
     *         String[] tags = entry.getURL();
     *         </pre>
     */

    public String[] getTags() {
        return tags;
    }

    protected void setTags(String[] tags) {
        this.tags = tags;
    }

    /**
     * Get contentType name.
     *
     * @return String @contentTypeName <br>
     *         <br>
     *         <b>Example :</b><br>
     * 
     *         <pre class="prettyprint">
     *         String contentType = entry.getFileType();
     *         </pre>
     */

    public String getContentType() {
        return contentTypeUid;
    }

    /**
     * Get uid.
     *
     * @return String @uid <br>
     *         <br>
     *         <b>Example :</b><br>
     * 
     *         <pre class="prettyprint">
     *         String uid = entry.getUid();
     *         </pre>
     */

    public String getUid() {
        return uid;
    }

    protected void setUid(String uid) {
        this.uid = uid;
    }

    public String getLocale() {
        return this.language;
    }

    /**
     * @param locale {@link String}
     * @return Entry <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         Entry entry = entry.setLanguage();
     *         </pre>
     */

    public Entry setLocale(@NotNull String locale) {
        params.put("locale", locale);
        return this;
    }

    @Deprecated
    public Map<String, Object> getOwner() {
        return owner;
    }

    /**
     * Get entry representation in json
     *
     * @return JSONObject @resultJson <br>
     *         <br>
     *         <b>Example :</b><br>
     * 
     *         <pre class="prettyprint">
     *         JSONObject json = entry.toJSON();
     *         </pre>
     */

    public JSONObject toJSON() {
        return resultJson;
    }

    /**
     * Get object value for key.
     *
     * @param key field_uid as key. <br>
     *            <br>
     *            <b>Example :</b><br>
     *
     *            <pre class="prettyprint">
     *            Object obj = entry.get("key");
     *            </pre>
     *
     * @return Object @resultJson
     */
    public Object get(@NotNull String key) {
        return resultJson.opt(key);
    }

    /**
     * Get string value for key.
     *
     * @param key field_uid as key. <br>
     *            <br>
     *            <b>Example :</b><br>
     *
     *            <pre class="prettyprint">
     *            String value = entry.getString("key");
     *            </pre>
     *
     * @return String @getString
     */

    public String getString(@NotNull String key) {
        Object value = get(key);
        if (value instanceof String) {
            return (String) value;
        }
        return null;
    }

    /**
     * Get boolean value for key.
     *
     * @param key field_uid as key. <br>
     *            <br>
     *            <b>Example :</b><br>
     *
     *            <pre class="prettyprint">
     *            Boolean value = entry.getBoolean("key");
     *            </pre>
     *
     * @return boolean @getBoolean
     */

    public Boolean getBoolean(@NotNull String key) {
        Object value = get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return false;
    }

    /**
     * Get {@link JSONArray} value for key
     *
     * @param key field_uid as key. <br>
     *            <br>
     *            <b>Example :</b><br>
     *
     *            <pre class="prettyprint">
     *            JSONArray value = entry.getJSONArray("key");
     *            </pre>
     *
     * @return JSONArray @getJSONArray
     */

    public JSONArray getJSONArray(@NotNull String key) {
        Object value = get(key);
        if (value instanceof JSONArray) {
            return (JSONArray) value;
        }
        return null;
    }

    /**
     * Get {@link JSONObject} value for key
     *
     * @param key field_uid as key. <br>
     *            <br>
     *            <b>Example :</b><br>
     *
     *            <pre class="prettyprint">
     *            JSONObject value = entry.getJSONObject("key");
     *            </pre>
     *
     * @return JSONObject @getJSONObject
     */
    public JSONObject getJSONObject(@NotNull String key) {
        Object value = get(key);
        if (value instanceof JSONObject) {
            return (JSONObject) value;
        }
        return null;
    }

    /**
     * Get {@link JSONObject} value for key
     *
     * @param key field_uid as key. <br>
     *            <br>
     *            <b>Example :</b><br>
     *
     *            <pre class="prettyprint">
     *            JSONObject value = entry.getJSONObject("key");
     *            </pre>
     *
     * @return Number @getNumber
     */

    public Number getNumber(@NotNull String key) {
        Object value = get(key);
        if (value instanceof Number) {
            return (Number) value;
        }
        return null;
    }

    /**
     * Get integer value for key
     *
     * @param key field_uid as key. <br>
     *            <br>
     *            <b>Example :</b><br>
     *
     *            <pre class="prettyprint">
     *            int value = entry.getInt("key");
     *            </pre>
     *
     * @return int @getInt
     */

    public int getInt(@NotNull String key) {
        Object value = getNumber(key);
        if (value instanceof Integer) {
            return (Integer) value;
        }
        return 0;
    }

    /**
     * Get integer value for key
     *
     * @param key field_uid as key.
     * @return float @getFloat <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         float value = entry.getFloat("key");
     *         </pre>
     */

    public float getFloat(@NotNull String key) {
        Object value = getNumber(key);
        if (value instanceof Float) {
            return (Float) value;
        }
        return 0;
    }

    /**
     * Get double value for key
     *
     * @param key field_uid as key.
     * @return double @getDouble <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         double value = entry.getDouble("key");
     *         </pre>
     */

    public double getDouble(@NotNull String key) {
        Number value = getNumber(key);
        if (value != null) {
            return value.doubleValue();
        }
        return 0;
    }

    /**
     * Get long value for key
     *
     * @param key field_uid as key. <br>
     *            <br>
     *            <b>Example :</b><br>
     *
     *            <pre class="prettyprint">
     *            long value = entry.getLong("key");
     *            </pre>
     *
     * @return long @getLong
     */

    public long getLong(@NotNull String key) {
        Number value = getNumber(key);
        if (value != null) {
            return value.longValue();
        }
        return 0;
    }

    /**
     * Get short value for key
     *
     * @param key field_uid as key.
     *
     *            <br>
     *            <br>
     *            <b>Example :</b><br>
     *
     *            <pre class="prettyprint">
     *            short value = entry.getShort("key");
     *            </pre>
     *
     * @return short @getShort
     */
    public short getShort(@NotNull String key) {
        Number value = getNumber(key);
        if (value != null) {
            return value.shortValue();
        }
        return (short) 0;
    }

    /**
     * Get {@link Calendar} value for key
     *
     * @param key field_uid as key. <br>
     *            <br>
     *            <b>Example :</b><br>
     *
     *            <pre class="prettyprint">
     *            Calendar value = entry.getDate("key");
     *            </pre>
     *
     * @return Calendar @getDate
     */

    public Calendar getDate(@NotNull String key) {
        try {
            String value = getString(key);
            return Constants.parseDate(value, null);
        } catch (Exception e) {
            logger.severe(e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * Get {@link Calendar} value of creation time of entry.
     *
     * @return Calendar @getCreateAt <br>
     *         <br>
     *         <b>Example :</b><br>
     * 
     *         <pre class="prettyprint">
     *         Calendar createdAt = entry.getCreateAt("key");
     *         </pre>
     */
    public Calendar getCreateAt() {

        try {
            String value = getString("created_at");
            return Constants.parseDate(value, null);
        } catch (Exception e) {
            logger.severe(e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * Get uid who created this entry.
     *
     * @return String @getCreatedBy <br>
     *         <br>
     *         <b>Example :</b><br>
     * 
     *         <pre class="prettyprint">
     *         String createdBy_uid = entry.getCreatedBy();
     *         </pre>
     */
    public String getCreatedBy() {
        return getString("created_by");
    }

    /**
     * Get {@link Calendar} value of updating time of entry.
     *
     * @return Calendar @getUpdateAt <br>
     *         <br>
     *         <b>Example :</b><br>
     * 
     *         <pre class="prettyprint">
     *         Calendar updatedAt = entry.getUpdateAt("key");
     *         </pre>
     */
    public Calendar getUpdateAt() {

        try {
            String value = getString("updated_at");
            return Constants.parseDate(value, null);
        } catch (Exception e) {
            logger.severe(e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * Get uid who updated this entry.
     *
     * @return String @getString <br>
     *         <br>
     *         <b>Example :</b><br>
     * 
     *         <pre class="prettyprint">
     *         String updatedBy_uid = entry.getUpdatedBy();
     *         </pre>
     */
    public String getUpdatedBy() {
        return getString("updated_by");
    }

    /**
     * Get {@link Calendar} value of deletion time of entry.
     *
     * @return Calendar
     *
     *         <br>
     *         <br>
     *         <b>Example :</b><br>
     * 
     *         <pre class="prettyprint">
     *         Calendar updatedAt = entry.getUpdateAt("key");
     *         </pre>
     */
    public Calendar getDeleteAt() {

        try {
            String value = getString("deleted_at");
            return Constants.parseDate(value, null);
        } catch (Exception e) {
            logger.severe(e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * Get uid who deleted this entry.
     *
     * @return String <br>
     *         <br>
     *         <b>Example :</b><br>
     * 
     *         <pre class="prettyprint">
     *         String deletedBy_uid = entry.getDeletedBy();
     *         </pre>
     */
    public String getDeletedBy() {
        return getString("deleted_by");
    }

    /**
     * Get an asset from the entry
     *
     * @param key field_uid as key.
     * @return Asset <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         Asset asset = entry.getAsset("key");
     *         </pre>
     */
    public Asset getAsset(String key) {
        JSONObject assetObject = getJSONObject(key);
        return contentType.stackInstance.asset().configure(assetObject);
    }

    /**
     * Get an assets from the entry. This works with multiple true fields <br>
     * <br>
     * <b>Example :</b><br>
     * 
     * <pre class="prettyprint">
     * {
     *     &#64;code
     *     List asset = entry.getAssets("key");
     * }
     * </pre>
     *
     * @param key This is the String key
     * @return ArrayList This returns list of Assets.
     */

    public List<Asset> getAssets(String key) {
        List<Asset> assets = new ArrayList<>();
        JSONArray assetArray = getJSONArray(key);
        assetArray.forEach(model -> {
            if (model instanceof JSONObject) {
                JSONObject newModel = (JSONObject) model;
                Asset asset = contentType.stackInstance.asset().configure(newModel);
                assets.add(asset);
            }
        });
        return assets;
    }

    /**
     * @param key
     *
     *
     *            Get a group from entry.
     *
     * @param key field_uid as key. <br>
     *            <br>
     *            <b>Example :</b><br>
     *
     *            <pre class="prettyprint">
     *             Group innerGroup = entry.getGroup("key");
     *             return null
     *            </pre>
     *
     *
     * @return {@link Group}
     */
    public Group getGroup(String key) {
        if (!key.isEmpty() && resultJson.has(key) && resultJson.opt(key) instanceof JSONObject) {
            return new Group(contentType.stackInstance, resultJson.optJSONObject(key));
        }
        return null;
    }

    /**
     * Get a list of group from entry.
     * <p>
     * <b>Note :-</b> This will work when group is multiple true.
     *
     * @param key field_uid as key.
     * @return list of group from entry <br>
     *         <br>
     *         <b>Example :</b><br>
     * 
     *         <pre class="prettyprint">
     *         Group innerGroup = entry.getGroups("key");
     *         </pre>
     */
    public List<Group> getGroups(String key) {
        List<Group> groupList = new ArrayList<>();
        if (!key.isEmpty() && resultJson.has(key) && resultJson.opt(key) instanceof JSONArray) {
            JSONArray array = resultJson.optJSONArray(key);
            array.forEach(model -> {
                JSONObject groupModel = (JSONObject) model;
                Group group = new Group(contentType.stackInstance, groupModel);
                groupList.add(group);
            });
        }
        return groupList;
    }

    /**
     * Get value for the given reference key.
     *
     * @param refKey         key of a reference field.
     * @param refContentType class uid.
     * @return {@link ArrayList} of {@link Entry} instances. Also specified
     *         contentType value will be set as class uid for all {@link Entry}
     *         instance. <br>
     *         <br>
     *         <b>Example :</b><br>
     * 
     *         <pre class="prettyprint">
     *          {@code
     *          Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *          Query csQuery = stack.contentType("contentType_name").query();
     *          csQuery.includeReference("for_bug");
     *          csQuery.find(new QueryResultsCallBack() {<br>
     *          &#64;Override
     *          public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {<br>
     *              if(error == null){
     *                  List&#60;Entry&#62; list = builtqueryresult.getResultObjects();
     *                   for (int i = 0; i < list.queueSize(); i++) {
     *                         Entry   entry   = list.get(i);
     *                         Entry taskEntry = entry.getAllEntries("for_task", "task");
     *                  }
     *              }
     *          }
     *      });
     *      }<br>
     *         </pre>
     */
    public ArrayList<Entry> getAllEntries(String refKey, String refContentType) {
        ArrayList<Entry> entryContainer = new ArrayList<>();
        if (resultJson != null) {
            Object resultArr = resultJson.opt(refKey);
            if (resultArr instanceof JSONArray) {
                JSONArray resultArrList = (JSONArray) resultArr;
                resultArrList.forEach(result -> {
                    JSONObject newResult = (JSONObject) result;
                    EntryModel model = new EntryModel(newResult);
                    Entry entryInstance = null;
                    try {
                        entryInstance = contentType.stackInstance.contentType(refContentType).entry();
                    } catch (Exception e) {
                        entryInstance = new Entry(refContentType);
                        logger.severe(e.getLocalizedMessage());
                    }
                    entryInstance.setUid(model.uid);
                    entryInstance.resultJson = model.jsonObject;
                    entryInstance.setTags(model.tags);
                    entryContainer.add(entryInstance);
                });
            }
        }
        return entryContainer;
    }

    /**
     * Specifies list of field uids that would be &#39;excluded&#39; from the
     * response.
     *
     * @param fieldUid field uid which get &#39;excluded&#39; from the response.
     * @return {@link Entry} object, so you can chain this call. <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *          Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *          Entry entry = stack.contentType("form_name").entry("entry_uid");<br>
     *          entry.except(new String[]{"name", "description"});
     *         </pre>
     */

    public Entry except(@NotNull String[] fieldUid) {
        if (fieldUid.length > 0) {
            if (exceptFieldArray == null) {
                exceptFieldArray = new JSONArray();
            }
            for (String s : fieldUid) {
                exceptFieldArray.put(s);
            }
        }
        return this;
    }

    /**
     * Add a constraint that requires a particular reference key details.
     *
     * @param referenceField key that to be constrained.
     * @return {@link Entry} object, so you can chain this call. <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *          Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *          Entry entry = stack.contentType("form_name").entry("entry_uid");<br>
     *          entry.includeReference("referenceUid");
     *         </pre>
     */
    public Entry includeReference(@NotNull String referenceField) {
        if (!referenceField.isEmpty()) {
            if (referenceArray == null) {
                referenceArray = new JSONArray();
            }
            referenceArray.put(referenceField);
            params.put("include[]", referenceArray);
        }
        return this;
    }

    /**
     * Add a constraint that requires a particular reference key details.
     *
     * @param referenceFields array key that to be constrained.
     * @return {@link Entry} object, so you can chain this call. <br>
     *         <br>
     *         <b>Example :</b><br>
     * 
     *         <pre class="prettyprint">
    *           Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *          Entry entry = stack.contentType("form_name").entry("entry_uid");<br>
     *          entry.includeReference(new String[]{"referenceUid_A", "referenceUid_B"});
     *         </pre>
     */
    public Entry includeReference(@NotNull String[] referenceFields) {
        if (referenceFields.length > 0) {
            if (referenceArray == null) {
                referenceArray = new JSONArray();
            }
            for (String field : referenceFields) {
                referenceArray.put(field);
            }
            params.put("include[]", referenceArray);
        }
        return this;
    }

    /**
     * Specifies an array of &#39;only&#39; keys in BASE object that would be
     * &#39;included&#39; in the response.
     *
     * @param fieldUid Array of the &#39;only&#39; reference keys to be included in
     *                 response.
     * @return {@link Entry} object, so you can chain this call. <br>
     *         <br>
     *         <b>Example :</b><br>
     * 
     *         <pre class="prettyprint">
     *          Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *          Entry entry = stack.contentType("form_name").entry("entry_uid");<br>
     *          entry.only(new String[]{"name", "description"});
     *         </pre>
     */
    public Entry only(String[] fieldUid) {
        if (fieldUid != null && fieldUid.length > 0) {
            if (objectUidForOnly == null) {
                objectUidForOnly = new JSONArray();
            }
            for (String field : fieldUid) {
                objectUidForOnly.put(field);
            }
        }
        return this;
    }

    /**
     * Specifies an array of &#39;only&#39; keys that would be &#39;included&#39; in
     * the response.
     *
     * @param fieldUid          Array of the &#39;only&#39; reference keys to be
     *                          included in response.
     * @param referenceFieldUid Key who has reference to some other class object..
     * @return {@link Entry} object, so you can chain this call. <br>
     *         <br>
     *         <b>Example :</b><br>
     * 
     *         <pre class="prettyprint">
                Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *          Entry entry = stack.contentType("form_name").entry("entry_uid");<br>
     *          ArrayList&#60;String&#62; array = new ArrayList&#60;String&#62;();
     *          array.add("description");
     *          array.add("name");
     *          entry.onlyWithReferenceUid(array, "referenceUid");
     *         </pre>
     */

    public Entry onlyWithReferenceUid(@NotNull ArrayList<String> fieldUid, @NotNull String referenceFieldUid) {
        if (onlyJsonObject == null) {
            onlyJsonObject = new JSONObject();
        }
        JSONArray fieldValueArray = new JSONArray();
        fieldUid.forEach(field -> {
            fieldValueArray.put(field);
        });
        onlyJsonObject.put(referenceFieldUid, fieldValueArray);
        includeReference(referenceFieldUid);
        return this;
    }

    /**
     * Specifies an array of &#39;except&#39; keys that would be &#39;excluded&#39;
     * in the response.
     *
     * @param fieldUid          Array of the &#39;except&#39; reference keys to be
     *                          excluded in response.
     * @param referenceFieldUid Key who has reference to some other class object.
     * @return {@link Entry} object, so you can chain this call.
     *
     *         <br>
     *         <br>
     *         <b>Example :</b><br>
     * 
     *         <pre class="prettyprint">
     *          Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *          Entry entry = stack.contentType("form_name").entry("entry_uid");<br>
     *          ArrayList&#60;String&#62; array = new ArrayList&#60;String&#62;();
     *          array.add("description");
     *          array.add("name");<br>
     *          entry.onlyWithReferenceUid(array, "referenceUid");
     *         </pre>
     */
    public Entry exceptWithReferenceUid(@NotNull ArrayList<String> fieldUid, @NotNull String referenceFieldUid) {
        if (exceptJsonObject == null) {
            exceptJsonObject = new JSONObject();
        }
        JSONArray fieldValueArray = new JSONArray();
        fieldUid.forEach(field -> {
            fieldValueArray.put(field);
        });
        exceptJsonObject.put(referenceFieldUid, fieldValueArray);
        includeReference(referenceFieldUid);
        return this;
    }

    /**
     * Fetches the latest version of the entries from Contentstack.com content stack
     *
     * @param callback {@link EntryResultCallBack} object to notify the application
     *                 when the request has completed. <br>
     *                 <br>
     *                 <b>Example :</b><br>
     * 
     *                 <pre class="prettyprint">
     *                 {@code
     *                 Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *                 Entry entry = stack.contentType("form_name").entry("entry_uid");<br>
     *                 entry.fetch(new EntryResultCallBack() {<br>
     *                 &#64;Override
     *                 public void onCompletion(ResponseType responseType, Error error) {
     *                 }<br>
     *                 });<br>
     *                 }
     *                 </pre>
     */

    public void fetch(EntryResultCallBack callback) {
        if (!uid.isEmpty()) {
            try {
                throw new IllegalAccessException("Entry Uid is required");
            } catch (IllegalAccessException e) {
                logger.severe(e.getLocalizedMessage());
            }
        }
        String urlString = "content_types/" + contentTypeUid + "/entries/" + uid;
        JSONObject urlQueries = new JSONObject();
        urlQueries.put(ENVIRONMENT, headers.get(ENVIRONMENT));
        checkLivePreview(headers, urlQueries);
        fetchFromNetwork(urlString, urlQueries, callback);
    }

    private void fetchFromNetwork(String urlString, JSONObject urlQueries, EntryResultCallBack callBack) {
        try {
            JSONObject mainJson = new JSONObject();
            setIncludeJSON(urlQueries, callBack);
            mainJson.put("query", urlQueries);
            HashMap<String, Object> urlParams = getUrlParams(mainJson);
            new CSBackgroundTask(this, contentType.stackInstance, Constants.FETCHENTRY, urlString, this.headers,
                    urlParams, Constants.REQUEST_CONTROLLER.ENTRY.toString(), callBack);
        } catch (Exception e) {
            throwException(null, e, callBack);
        }
    }

    private void checkLivePreview(LinkedHashMap<String, Object> headers, JSONObject urlQueries) {
        Config configInstance = contentType.stackInstance.config;
        if (configInstance.enableLivePreview
                && configInstance.livePreviewContentType.equalsIgnoreCase(contentTypeUid)) {
            configInstance.setHost(configInstance.livePreviewHost);
            headers.remove("access_token"); // Step 4: Remove access_token from header
            headers.remove(ENVIRONMENT); // Remove environment from urlQuery
            if (configInstance.livePreviewHash == null || configInstance.livePreviewHash.isEmpty()) {
                configInstance.livePreviewHash = "init";
            }
            headers.put("live_preview", configInstance.livePreviewHash);
            headers.put("authorization", configInstance.managementToken);
            urlQueries.remove(ENVIRONMENT);
        }
    }

    private LinkedHashMap<String, Object> getUrlParams(JSONObject jsonMain) {
        JSONObject queryJSON = jsonMain.optJSONObject("query");
        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<>();
        if (queryJSON != null && queryJSON.length() > 0) {
            Iterator<String> iter = queryJSON.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                Object value = queryJSON.opt(key);
                hashMap.put(key, value);
            }
        }
        return hashMap;
    }

    private void setIncludeJSON(JSONObject mainJson, ResultCallBack callBack) {
        try {
            Iterator<String> iterator = params.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                Object value = params.get(key);
                mainJson.put(key, value);
            }
            if (objectUidForOnly != null && objectUidForOnly.length() > 0) {
                mainJson.put("only[BASE][]", objectUidForOnly);
                objectUidForOnly = null;
            }
            if (exceptFieldArray != null && exceptFieldArray.length() > 0) {
                mainJson.put("except[BASE][]", exceptFieldArray);
                exceptFieldArray = null;
            }
            if (exceptJsonObject != null && exceptJsonObject.length() > 0) {
                mainJson.put("except", exceptJsonObject);
                exceptJsonObject = null;
            }
            if (onlyJsonObject != null && onlyJsonObject.length() > 0) {
                mainJson.put("only", onlyJsonObject);
                onlyJsonObject = null;
            }
        } catch (Exception e) {
            throwException(null, e, (EntryResultCallBack) callBack);
        }
    }

    private void throwException(String errorMsg, Exception e, EntryResultCallBack callBack) {
        Error error = new Error();
        if (errorMsg != null) {
            error.setErrorMessage(errorMsg);
        } else {
            error.setErrorMessage(e.toString());
        }
        callBack.onRequestFail(ResponseType.UNKNOWN, error);
    }

    /**
     * This method adds key and value to an Entry.
     *
     * @param key   The key as string which needs to be added to an Entry
     * @param value The value as string which needs to be added to an Entry
     * @return {@link Entry}
     *
     *         <br>
     *         <br>
     *         <b>Example :</b><br>
     * 
     *         <pre class="prettyprint">
     *         {@code
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         final Entry entry = stack.contentType("user").entry("entryUid"); <br>
     *         entry.addParam("include_dimensions", "true"); <br>
     *         entry.fetch(new ResultCallBack() {
     *         <br>&#64; Override
     *          public void onCompletion(ResponseType responseType, Error error) {
     *          }<br>
     *          });<br>
     *          }
     *         </pre>
     */

    public Entry addParam(@NotNull String key, @NotNull String value) {
        params.put(key, value);
        return this;
    }

    /**
     * This method also includes the content type UIDs of the referenced entries
     * returned in the response
     *
     * @return {@link Entry}
     *
     *         <br>
     *         <b>Example :</b><br>
     * 
     *         <pre class="prettyprint">
     *          {@code
     *          Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *          final Entry entry = stack.contentType("user").entry("entryUid"); <br>
     *          entry.includeReferenceContentTypeUID; <br>
     *          entry.fetch(new EntryResultCallBack() {
     *          <br>&#64;Override
     *          public void onCompletion(ResponseType responseType, Error error) {
     *          }<br>
     *          });<br>
     *          }
     *         </pre>
     */
    public Entry includeReferenceContentTypeUID() {
        params.put("include_reference_content_type_uid", "true");
        return this;
    }

    /**
     * Include Content Type of all returned objects along with objects themselves.
     *
     * @return {@link Entry} object, so you can chain this call. <br>
     *         <br>
     *         <b>Example :</b><br>
     * 
     *         <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         final Entry entry = stack.contentType("user").entry("entryUid");
     *         entry.includeContentType();
     *         </pre>
     */
    public Entry includeContentType() {
        params.remove("include_schema");
        params.put("include_content_type", true);
        params.put("include_global_field_schema", true);
        return this;
    }

    /**
     * Retrieve the published content of the fallback locale if an entry is not
     * localized in specified locale
     *
     * @return {@link Entry} object, so you can chain this call. <br>
     *         <br>
     *         <b>Example :</b><br>
     * 
     *         <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         final Entry entry = stack.contentType("user").entry("entryUid");
     *         entry.includeFallback();
     *         </pre>
     */
    public Entry includeFallback() {
        params.put("include_fallback", true);
        return this;
    }

    /**
     * includeEmbeddedItems instance of Entry Include Embedded Objects (Entries and
     * Assets) along with entry/entries details.<br>
     * Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     * final Entry entry = stack.contentType("user").entry("entryUid"); entry =
     * entry.includeEmbeddedObjects(); entry.fetch()
     *
     * @return {@link Entry}
     */
    public Entry includeEmbeddedItems() {
        params.put("include_embedded_items[]", "BASE");
        return this;
    }

}
