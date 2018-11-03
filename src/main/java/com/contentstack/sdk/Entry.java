package com.contentstack.sdk;

import com.contentstack.sdk.Utility.CSAppConstants;
import com.contentstack.sdk.Utility.CSAppUtils;
import com.contentstack.sdk.Utility.CSController;
import com.contentstack.sdk.Utility.ContentstackUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/*** Entry is used to create, update and delete contentType&#39;s entries on the Built.io Content stack.
 *
 * @author built.io, Inc
 *
 */
public class Entry {

    private static final String TAG              = "Entry";
    private String contentTypeName               = null;
    private LinkedHashMap<String, Object> localHeader      = null;
    protected LinkedHashMap<String, Object> formHeader     = null;
    protected HashMap<String, Object> owner      = null;
    protected HashMap<String, Object> _metadata  = null;

    private ContentType contentTypeInstance      = null;
    private String[] tags                        = null;
    protected String uid                         = null;
    protected JSONObject resultJson              = null;
    protected String ownerEmailId                = null;
    protected String ownerUid                    = null;
    protected String title                       = null;
    protected String url                         = null;

    private JSONArray referenceArray;
    private JSONObject otherPostJSON;
    private JSONArray objectUidForOnly;
    private JSONArray objectUidForExcept;
    private JSONObject onlyJsonObject;
    private JSONObject exceptJsonObject;

    private Entry(){}

    protected Entry(String contentTypeName) {
        this.contentTypeName = contentTypeName;
        this.localHeader = new LinkedHashMap<>();
        this.otherPostJSON = new JSONObject();
    }

    protected void setContentTypeInstance(ContentType contentTypeInstance) {
        this.contentTypeInstance = contentTypeInstance;
    }

    public Entry configure(JSONObject jsonObject){
        EntryModel model       = new EntryModel(jsonObject, null,true,false,false);
        this.resultJson 	   = model.jsonObject;
        this.ownerEmailId 	   = model.ownerEmailId;
        this.ownerUid     	   = model.ownerUid;
        this.title             = model.title;
        this.url               = model.url;
        if(model.ownerMap != null) {
            this.owner = new HashMap<>(model.ownerMap);
        }
        if(model._metadata != null) {
            this._metadata = new HashMap<>(model._metadata);
        }

        this.uid		   		= model.entryUid;
        this.setTags(model.tags);
        model = null;

        return this;
    }

    /**
     * Set headers.
     *
     * @param key
     * custom_header_key
     *
     * @param value
     * custom_header_value
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * //'blt5d4sample2633b' is a dummy Stack API key
     * //'blt6d0240b5sample254090d' is dummy access token.
     * Stack stack = Contentstack.stack(context, "blt5d4sample2633b", "blt6d0240b5sample254090d", "stag", false);
     * Entry entry = stack.contentType("form_name").entry("entry_uid");
     * entry.setHeader("custom_header_key", "custom_header_value");
     * </pre>
     *
     */

    public void setHeader(String key, String value){
        if(!key.isEmpty() && !value.isEmpty()){
            localHeader.put(key, value);
        }
    }

    /**
     * Remove header key.
     *
     * @param key
     * custom_header_key
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * //'blt5d4sample2633b' is a dummy Stack API key
     * //'blt6d0240b5sample254090d' is dummy access token.
     * Stack stack = Contentstack.stack(context, "blt5d4sample2633b", "blt6d0240b5sample254090d", "stag", false);
     * Entry entry = stack.contentType("form_name").entry("entry_uid");
     * entry.removeHeader("custom_header_key");
     * </pre>
     */

    public void removeHeader(String key){
        if(!key.isEmpty()){
            localHeader.remove(key);
        }
    }

    /**
     * Get title string
     *
     * @return String @title
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * String title = entry.getTitle();
     * </pre>
     *
     */

    public String getTitle(){ return title; }

    /**
     * Get url string
     *
     *
     * @return String @url
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * String url = entry.getURL();
     * </pre>
     *
     */

    public String getURL(){ return url; }

    /**
     * Get tags.
     *
     * @return String @tags
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * String[] tags = entry.getURL();
     * </pre>
     */

    public String[] getTags() {
        return tags;
    }

    /**
     * Get contentType name.
     *
     * @return String @contentTypeName
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * String contentType = entry.getFileType();
     * </pre>
     */

    public String getContentType() {
        return contentTypeName;
    }

    /**
     * Get uid.
     *
     * @return String @uid
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * String uid = entry.getUid();
     * </pre>
     */

    public String getUid() {
        return uid;
    }

    /**
     * Get metadata of entry.
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * HashMap<String, Object> metaData = entry.getMetadata();
     * </pre>
     */

    private HashMap<String, Object> getMetadata() {
        return _metadata;
    }

    /**
     * Get {@link Language} instance
     *
     * @return Language @getLanguage
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * Language local = entry.getLanguage();
     * </pre>
     *
     */

    public Language getLanguage(){
        String localeCode = null;

        if(_metadata != null && _metadata.size() > 0 && _metadata.containsKey("locale")){
            localeCode = (String) _metadata.get("locale");
        } else if(resultJson.has("locale")){
            localeCode = (String) resultJson.optString("locale");
        }

        if(localeCode != null) {
            localeCode = localeCode.replace("-", "_");
            LanguageCode codeValue = LanguageCode.valueOf(localeCode);
            int localeValue = codeValue.ordinal();
            Language[] language = Language.values();

            return language[localeValue];
        }
        return null;
    }

    public HashMap<String, Object> getOwner() {
        return owner;
    }

    /**
     * Get entry representation in json
     *
     * @return JSONObject @resultJson
     *
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * JSONObject json = entry.toJSON();
     * </pre>
     *
     */

    public JSONObject toJSON(){
        return resultJson;
    }

    /**
     * Get object value for key.
     *
     * @return Object @resultJson
     *
     * @param key
     * field_uid as key.
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * Object obj = entry.get("key");
     * </pre>
     */

    public Object get(String key){
        try{
            if(resultJson != null && key != null){
                return resultJson.get(key);
            }else{
                return null;
            }
        }catch (Exception e) {
            CSAppUtils.showLog(TAG, "-----------------get|" + e);
            return null;
        }
    }





    /**
     * Get string value for key.
     *
     * @return String @getString
     *
     * @param key field_uid as key.
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * String value = entry.getString("key");
     * </pre>
     */

    public String getString(String key){
        Object value = get(key);
        if(value != null){
            if(value instanceof String){
                return (String) value;
            }
        }
        return null;
    }

    /**
     * Get boolean value for key.
     *
     *
     * @return boolean @getBoolean
     * @param key
     *             field_uid as key.
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * Boolean value = entry.getBoolean("key");
     * </pre>
     */

    public Boolean getBoolean(String key){
        Object value = get(key);
        if(value != null){
            if(value instanceof Boolean){
                return (Boolean) value;
            }
        }
        return false;
    }

    /**
     * Get {@link JSONArray} value for key
     *
     * @return JSONArray @getJSONArray
     *
     * @param key
     *          field_uid as key.
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * JSONArray value = entry.getJSONArray("key");
     * </pre>
     */

    public JSONArray getJSONArray(String key){
        Object value = get(key);
        if(value != null){
            if(value instanceof JSONArray){
                return (JSONArray) value;
            }
        }
        return null;
    }

    /**
     * Get {@link JSONObject} value for key
     *
     * @return JSONObject @getJSONObject
     *
     * @param key field_uid as key.
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * JSONObject value = entry.getJSONObject("key");
     * </pre>
     */
    public JSONObject getJSONObject(String key){
        Object value = get(key);
        if(value != null){
            if(value instanceof JSONObject){
                return (JSONObject) value;
            }
        }
        return null;
    }

    /**
     * Get {@link JSONObject} value for key
     *
     * @return Number @getNumber
     *
     * @param key
     *               field_uid as key.
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * JSONObject value = entry.getJSONObject("key");
     * </pre>
     */

    public Number getNumber(String key){
        Object value = get(key);
        if(value != null){
            if(value instanceof Number){
                return (Number) value;
            }
        }
        return null;
    }

    /**
     * Get integer value for key
     *
     * @return int @getInt
     *
     * @param key
     * field_uid as key.
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * int value = entry.getInt("key");
     * </pre>
     */

    public int getInt(String key){
        Number value = getNumber(key);
        if(value != null){
            return value.intValue();
        }
        return 0;
    }

    /**
     * Get integer value for key
     *
     * @param key
     * field_uid as key.
     *
     * @return float @getFloat
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * float value = entry.getFloat("key");
     * </pre>
     */

    public float getFloat(String key){
        Number value = getNumber(key);
        if(value != null){
            return value.floatValue();
        }
        return (float) 0;
    }

    /**
     * Get double value for key
     *
     * @param key
     * field_uid as key.
     *
     * @return double @getDouble
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * double value = entry.getDouble("key");
     * </pre>
     */

    public double getDouble(String key){
        Number value = getNumber(key);
        if(value != null){
            return value.doubleValue();
        }
        return (double) 0;
    }

    /**
     * Get long value for key
     *
     * @return long @getLong
     *
     * @param key
     *               field_uid as key.
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * long value = entry.getLong("key");
     * </pre>
     */

    public long getLong(String key){
        Number value = getNumber(key);
        if(value != null){
            return value.longValue();
        }
        return (long) 0;
    }

    /**
     * Get short value for key
     *
     * @return short @getShort
     *
     * @param key
     *               field_uid as key.
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * short value = entry.getShort("key");
     * </pre>
     */
    public short getShort(String key){
        Number value = getNumber(key);
        if(value != null){
            return value.shortValue();
        }
        return (short) 0;
    }

    /**
     * Get {@link Calendar} value for key
     *
     * @return Calendar @getDate
     *
     * @param key
     *               field_uid as key.
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * Calendar value = entry.getDate("key");
     * </pre>
     */

    public Calendar getDate(String key){

        try {
            String value = getString(key);
            return ContentstackUtil.parseDate(value, null);
        } catch (Exception e) {
            CSAppUtils.showLog(TAG, "-----------------getDate|" + e);
        }
        return null;
    }

    /**
     * Get {@link Calendar} value of creation time of entry.
     *
     * @return Calendar @getCreateAt
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * Calendar createdAt = entry.getCreateAt("key");
     * </pre>
     */

    public Calendar getCreateAt(){

        try {
            String value = getString("created_at");
            return ContentstackUtil.parseDate(value, null);
        } catch (Exception e) {
            CSAppUtils.showLog(TAG, "-----------------getCreateAtDate|" + e);
        }
        return null;
    }

    /**
     * Get uid who created this entry.
     *
     * @return String @getCreatedBy
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * String createdBy_uid = entry.getCreatedBy();
     * </pre>
     */
    public String getCreatedBy(){

        return getString("created_by");
    }

    /**
     * Get {@link Calendar} value of updating time of entry.
     *
     * @return Calendar @getUpdateAt
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * Calendar updatedAt = entry.getUpdateAt("key");
     * </pre>
     */
    public Calendar getUpdateAt(){

        try {
            String value = getString("updated_at");
            return ContentstackUtil.parseDate(value, null);
        } catch (Exception e) {
            CSAppUtils.showLog(TAG, "-----------------getUpdateAtDate|" + e);
        }
        return null;
    }

    /**
     * Get uid who updated this entry.
     *
     * @return String @getString
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * String updatedBy_uid = entry.getUpdatedBy();
     * </pre>
     */
    public String getUpdatedBy(){

        return getString("updated_by");
    }

    /**
     * Get {@link Calendar} value of deletion time of entry.
     *
     * @return Calendar
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * Calendar updatedAt = entry.getUpdateAt("key");
     * </pre>
     */
    public Calendar getDeleteAt(){

        try {
            String value = getString("deleted_at");
            return ContentstackUtil.parseDate(value, null);
        } catch (Exception e) {
            CSAppUtils.showLog(TAG, "-----------------getDeleteAt|" + e);
        }
        return null;
    }

    /**
     * Get uid who deleted this entry.
     *
     * @return String
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * String deletedBy_uid = entry.getDeletedBy();
     * </pre>
     */
    public String getDeletedBy(){

        return getString("deleted_by");
    }

    /**
     * Get an asset from the entry
     *
     * @param key
     * field_uid as key.
     *
     * @return Asset
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * Asset asset = entry.getAsset("key");
     * </pre>
     */
    public Asset getAsset(String key){

        JSONObject assetObject = getJSONObject(key);
        Asset asset = contentTypeInstance.stackInstance.asset().configure(assetObject);

        return asset;
    }





    /**
     * Get an assets from the entry. This works with multiple true fields
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     *
     * {@code List<Asset> asset = entry.getAssets("key"); }
     *
     * </pre>
     *
     * @param key This is the String key
     * @return ArrayList This returns list of Assets.
     */



    public List<Asset> getAssets(String key){
        List<Asset> assets = new ArrayList<>();
        JSONArray assetArray = getJSONArray(key);

        for (int i = 0; i < assetArray.length(); i++) {

            if(assetArray.opt(i) instanceof JSONObject){
                Asset asset = contentTypeInstance.stackInstance.asset().configure(assetArray.optJSONObject(i));
                assets.add(asset);
            }
        }
        return assets;
    }

    /**
     * Get a group from entry.
     *
     * @param key
     *              field_uid as key.
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * Group innerGroup = entry.getGroup("key");
     * @return null
     * </pre>
     */
    public Group getGroup(String key){

        if(!key.isEmpty() && resultJson.has(key) && resultJson.opt(key) instanceof JSONObject ){
            return new Group(contentTypeInstance.stackInstance, resultJson.optJSONObject(key));
        }
        return null;
    }

    /**
     * Get a list of group from entry.
     *
     * <p>
     * <b>Note :-</b> This will work when group is multiple true.
     *
     * @param key field_uid as key.
     *
     * @return  list of group from entry
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * Group innerGroup = entry.getGroups("key");
     * </pre>
     */
    public List<Group> getGroups(String key){

        if(!key.isEmpty() && resultJson.has(key) && resultJson.opt(key) instanceof JSONArray ){
            JSONArray array = resultJson.optJSONArray(key);
            List<Group> groupList = new ArrayList<>();

            for (int i = 0; i < array.length(); i++) {
                if(array.opt(i) instanceof JSONObject){
                    Group group = new Group(contentTypeInstance.stackInstance, array.optJSONObject(i));
                    groupList.add(group);
                }
            }

            return groupList;
        }
        return null;
    }

    /**
     * Get value for the given reference key.
     *
     * @param refKey
     * 			  key of a reference field.
     *
     * @param refContentType
     * 					 class uid.
     *
     * @return
     * 			{@link ArrayList} of {@link Entry} instances.
     * Also specified contentType value will be set as class uid for all {@link Entry} instance.
     *
     *
     *<br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     *  //'blt5d4sample2633b' is a dummy Stack API key
     *  //'blt6d0240b5sample254090d' is dummy access token.
     *
     *  {@code
     *  Stack stack = Contentstack.stack(context, "blt5d4sample2633b", "blt6d0240b5sample254090d", "stag", false);
     *  Query csQuery = stack.contentType("contentType_name").query();
     * csQuery.includeReference("for_bug");
     * csQuery.find(new QueryResultsCallBack() {<br>
     *          &#64;Override
     *          public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {<br>
     *
     *              if(error == null){
     *                  List&#60;Entry&#62; list = builtqueryresult.getResultObjects();
     *                   for (int i = 0; i < list.queueSize(); i++) {
     *                           Entry   entry   = list.get(i);
     *                           Entry taskEntry = entry.getAllEntries("for_task", "task");
     *                  }
     *              }
     *
     *          }
     *      });
     *      }<br>
     *
     * </pre>
     */
    public ArrayList<Entry> getAllEntries(String refKey, String refContentType) {
        try {
            if (resultJson != null) {

                if (resultJson.get(refKey) instanceof JSONArray) {

                    int count = ((JSONArray) resultJson.get(refKey)).length();
                    ArrayList<Entry> builtObjectList = new ArrayList<Entry>();
                    for (int i = 0; i < count; i++) {

                        EntryModel model = new EntryModel(((JSONArray) resultJson.get(refKey)).getJSONObject(i), null, false, false, true);
                        Entry entryInstance = null;
                        try {
                            entryInstance = contentTypeInstance.stackInstance.contentType(refContentType).entry();
                        } catch (Exception e) {
                            entryInstance = new Entry(refContentType);
                            CSAppUtils.showLog("BuiltObject", "----------------getAllEntries" + e.toString());
                        }
                        entryInstance.setUid(model.entryUid);
                        entryInstance.ownerEmailId = model.ownerEmailId;
                        entryInstance.ownerUid = model.ownerUid;
                        if(model.ownerMap != null) {
                            entryInstance.owner = new HashMap<>(model.ownerMap);
                        }
                        entryInstance.resultJson = model.jsonObject;
                        entryInstance.setTags(model.tags);

                        builtObjectList.add(entryInstance);
                        model = null;
                    }

                    return builtObjectList;

                }
            }
        } catch (Exception e) {
            CSAppUtils.showLog(TAG, "-----------------get|" + e);
            return null;
        }

        return null;
    }

    /**
     * Specifies list of field uids that would be &#39;excluded&#39; from the response.
     *
     * @param fieldUid
     * 					field uid  which get &#39;excluded&#39; from the response.
     *
     * @return
     *           {@link Entry} object, so you can chain this call.
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     *     //'blt5d4sample2633b' is a dummy Stack API key
     *     //'blt6d0240b5sample254090d' is dummy access token.
     *     Stack stack = Contentstack.stack(context, "blt5d4sample2633b", "blt6d0240b5sample254090d", "stag", false);
     *     Entry entry = stack.contentType("form_name").entry("entry_uid");<br>
     *     entry.except(new String[]{"name", "description"});
     * </pre>
     */
    public Entry except(String[] fieldUid){
        try{
            if(fieldUid != null && fieldUid.length > 0){

                if(objectUidForExcept == null){
                    objectUidForExcept = new JSONArray();
                }

                int count = fieldUid.length;
                for(int i = 0; i < count; i++){
                    objectUidForExcept.put(fieldUid[i]);
                }
            }
        }catch(Exception e) {
            CSAppUtils.showLog(TAG, "--except-catch|" + e);
        }
        return this;
    }

    /**
     * Add a constraint that requires a particular reference key details.
     *
     * @param referenceField
     * 				 key that to be constrained.
     *
     * @return
     * 			 {@link Entry} object, so you can chain this call.
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     *    //'blt5d4sample2633b' is a dummy Stack API key
     *    //'blt6d0240b5sample254090d' is dummy access token.
     *    Stack stack = Contentstack.stack(context, "blt5d4sample2633b", "blt6d0240b5sample254090d", "stag", false);
     *    Entry entry = stack.contentType("form_name").entry("entry_uid");<br>
     *    entry.includeReference("referenceUid");
     * </pre>
     */
    public Entry includeReference(String referenceField) {
        try {
            if (!referenceField.isEmpty()) {
                if (referenceArray == null) {
                    referenceArray = new JSONArray();
                }

                referenceArray.put(referenceField);

                otherPostJSON.put("include[]", referenceArray);
            }
        } catch (Exception e) {
            CSAppUtils.showLog(TAG, "--include Reference-catch|" + e);
        }

        return this;
    }

    /**
     * Add a constraint that requires a particular reference key details.
     *
     * @param referenceFields
     * 				 array key that to be constrained.
     *
     * @return
     * 			 {@link Entry} object, so you can chain this call.
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     *    //'blt5d4sample2633b' is a dummy Stack API key
     *    //'blt6d0240b5sample254090d' is dummy access token.
     *    Stack stack = Contentstack.stack(context, "blt5d4sample2633b", "blt6d0240b5sample254090d", "stag", false);
     *    Entry entry = stack.contentType("form_name").entry("entry_uid");<br>
     *    entry.includeReference(new String[]{"referenceUid_A", "referenceUid_B"});
     * </pre>
     */
    public Entry includeReference(String[] referenceFields) {
        try {
            if (referenceFields != null && referenceFields.length > 0) {
                if (referenceArray == null) {
                    referenceArray = new JSONArray();
                }
                for (int i = 0; i < referenceFields.length; i++) {
                    referenceArray.put(referenceFields[i]);
                }

                otherPostJSON.put("include[]", referenceArray);
            }
        } catch (Exception e) {
            CSAppUtils.showLog(TAG, "--include Reference-catch|" + e);
        }

        return this;
    }

    /**
     * Specifies an array of &#39;only&#39; keys in BASE object that would be &#39;included&#39; in the response.
     *
     * @param fieldUid
     * Array of the &#39;only&#39; reference keys to be included in response.
     *
     * @return
     * {@link Entry} object, so you can chain this call.
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     *    //'blt5d4sample2633b' is a dummy Stack API key
     *    //'blt6d0240b5sample254090d' is dummy access token.
     *    Stack stack = Contentstack.stack(context, "blt5d4sample2633b", "blt6d0240b5sample254090d", "stag", false);
     *    Entry entry = stack.contentType("form_name").entry("entry_uid");<br>
     *    entry.only(new String[]{"name", "description"});
     * </pre>
     */
    public Entry only(String[] fieldUid) {
        try{
            if (fieldUid != null && fieldUid.length > 0) {
                if(objectUidForOnly == null){
                    objectUidForOnly = new JSONArray();
                }

                int count = fieldUid.length;
                for(int i = 0; i < count; i++){
                    objectUidForOnly.put(fieldUid[i]);
                }
            }
        }catch(Exception e) {
            CSAppUtils.showLog(TAG, "--include Reference-catch|" + e);
        }

        return this;
    }

    /**
     * Specifies an array of &#39;only&#39; keys that would be &#39;included&#39; in the response.
     *
     *  @param fieldUid
     *  Array of the &#39;only&#39; reference keys to be included in response.
     *
     *  @param referenceFieldUid
     *  Key who has reference to some other class object..
     *
     *  @return  {@link Entry} object, so you can chain this call.
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     *    //'blt5d4sample2633b' is a dummy Stack API key
     *    //'blt6d0240b5sample254090d' is dummy access token.
     *    Stack stack = Contentstack.stack(context, "blt5d4sample2633b", "blt6d0240b5sample254090d", "stag", false);
     *    Entry entry = stack.contentType("form_name").entry("entry_uid");<br>
     *    ArrayList&#60;String&#62; array = new ArrayList&#60;String&#62;();
     *    array.add("description");
     *    array.add("name");
     *    entry.onlyWithReferenceUid(array, "referenceUid");
     *</pre>
     */

    public Entry onlyWithReferenceUid(ArrayList<String> fieldUid, String referenceFieldUid){
        try{
            if(fieldUid != null && referenceFieldUid != null){
                if(onlyJsonObject == null){
                    onlyJsonObject = new JSONObject();
                }
                JSONArray fieldValueArray = new JSONArray();
                int count = fieldUid.size();
                for(int i = 0; i < count; i++){
                    fieldValueArray.put(fieldUid.get(i));
                }

                onlyJsonObject.put(referenceFieldUid, fieldValueArray);

                includeReference(referenceFieldUid);

            }
        }catch(Exception e) {
            CSAppUtils.showLog(TAG, "--onlyWithReferenceUid-catch|" + e);
        }
        return this;
    }

    /**
     * Specifies an array of &#39;except&#39; keys that would be &#39;excluded&#39; in the response.
     *
     * @param fieldUid
     * 					Array of the &#39;except&#39; reference keys to be excluded in response.
     *
     * @param referenceFieldUid
     * 					Key who has reference to some other class object.
     *
     * @return
     *           {@link Entry} object, so you can chain this call.
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     *    //'blt5d4sample2633b' is a dummy Stack API key
     *    //'blt6d0240b5sample254090d' is dummy access token.
     *    Stack stack = Contentstack.stack(context, "blt5d4sample2633b", "blt6d0240b5sample254090d", "stag", false);
     *    Entry entry = stack.contentType("form_name").entry("entry_uid");<br>
     *    ArrayList&#60;String&#62; array = new ArrayList&#60;String&#62;();
     *    array.add("description");
     *    array.add("name");<br>
     *    entry.onlyWithReferenceUid(array, "referenceUid");
     * </pre>
     */
    public Entry exceptWithReferenceUid(ArrayList<String> fieldUid, String referenceFieldUid){
        try{
            if(fieldUid != null && referenceFieldUid != null){
                if(exceptJsonObject == null){
                    exceptJsonObject = new JSONObject();
                }
                JSONArray fieldValueArray = new JSONArray();
                int count = fieldUid.size();
                for(int i = 0; i < count; i++){
                    fieldValueArray.put(fieldUid.get(i));
                }

                exceptJsonObject.put(referenceFieldUid, fieldValueArray);

                includeReference(referenceFieldUid);
            }
        }catch(Exception e) {
            CSAppUtils.showLog(TAG, "--exceptWithReferenceUid-catch|" + e);
        }
        return this;
    }



    protected void setTags(String[] tags) {
        this.tags = tags;
    }

    protected void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Fetches the latest version of the entries from Built.io content stack
     *
     * @param callBack
     * {@link EntryResultCallBack} object to notify the application when the request has completed.
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     *    //'blt5d4sample2633b' is a dummy Stack API key
     *    //'blt6d0240b5sample254090d' is dummy access token.
     *    {@code
     *
     *    Stack stack = Contentstack.stack(context, "blt5d4sample2633b", "blt6d0240b5sample254090d", "stag", false);
     *    Entry entry = stack.contentType("form_name").entry("entry_uid");<br>
     *    entry.fetch(new BuiltResultCallBack() {<br>
     *           &#64;Override
     *           public void onCompletion(ResponseType responseType, BuiltError builtError) {
     *
     *           }<br>
     *    });<br>
     *
     *      }
     *   </pre>
     */

    public void fetch(EntryResultCallBack callBack){
        try {
            if (!uid.isEmpty()) {

                String URL = "/" + contentTypeInstance.stackInstance.VERSION + "/content_types/" + contentTypeName + "/entries/" + uid;

                LinkedHashMap<String, Object> headers  = getHeader(localHeader);
                LinkedHashMap<String, String> headerAll = new LinkedHashMap<String, String>();
                JSONObject              urlQueries= new JSONObject();

                if (headers != null && headers.size() > 0) {
                    for (Map.Entry<String, Object> entry : headers.entrySet()) {
                        headerAll.put(entry.getKey(), (String)entry.getValue());
                    }

                    if(headers.containsKey("environment")){
                        urlQueries.put("environment", headers.get("environment"));
                    }
                }

                fetchFromNetwork(URL, urlQueries, callBack);

            }
        }catch(Exception e){
            throwException(null, e, callBack);
        }
    }





    private void fetchFromNetwork(String URL, JSONObject urlQueries, EntryResultCallBack callBack){
        try{

            JSONObject mainJson = new JSONObject();

            setIncludeJSON(urlQueries, callBack);
            mainJson.put("query", urlQueries);
            mainJson.put("_method", CSAppConstants.RequestMethod.GET.toString());
            HashMap<String, Object> urlParams = getUrlParams(mainJson);

            System.out.println("urlQueries: "+urlQueries);
            System.out.println("URL: "+URL);

            new CSBackgroundTask(this, contentTypeInstance.stackInstance, CSController.FETCHENTRY, URL, getHeader(localHeader), urlParams, new JSONObject(), CSAppConstants.callController.ENTRY.toString(), false, CSAppConstants.RequestMethod.GET, callBack);

        }catch(Exception e){
            throwException(null, e, callBack);
        }
    }






    private LinkedHashMap<String, Object> getUrlParams(JSONObject jsonMain) {

        JSONObject queryJSON = jsonMain.optJSONObject("query");
        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<>();

        if(queryJSON != null && queryJSON.length() > 0){
            Iterator<String> iter = queryJSON.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    Object value = queryJSON.opt(key);
                    hashMap.put(key, value);
                } catch (Exception e) {
                    CSAppUtils.showLog(TAG, "----------------setQueryJson"+e.toString());
                }
            }
            return hashMap;
        }

        return null;
    }



    private void setIncludeJSON(JSONObject mainJson, ResultCallBack callBack){
        try {
            Iterator<String> iterator = otherPostJSON.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                Object value = otherPostJSON.get(key);
                mainJson.put(key, value);
            }

            if(objectUidForOnly!= null && objectUidForOnly.length() > 0){
                mainJson.put("only[BASE][]", objectUidForOnly);
                objectUidForOnly = null;

            }

            if(objectUidForExcept != null && objectUidForExcept.length() > 0){
                mainJson.put("except[BASE][]", objectUidForExcept);
                objectUidForExcept = null;
            }

            if(exceptJsonObject != null && exceptJsonObject.length() > 0){
                mainJson.put("except", exceptJsonObject);
                exceptJsonObject = null;
            }

            if(onlyJsonObject != null && onlyJsonObject.length() > 0){
                mainJson.put("only", onlyJsonObject);
                onlyJsonObject = null;
            }
        }catch(Exception e){
            throwException(null, e, (EntryResultCallBack)callBack);
        }
    }

    private void throwException(String errorMsg, Exception e, EntryResultCallBack callBack) {

        Error error = new Error();
        if(errorMsg != null){
            error.setErrorMessage(errorMsg);
        }else{
            error.setErrorMessage(e.toString());
        }

        callBack.onRequestFail(ResponseType.UNKNOWN, error);

    }

    private LinkedHashMap<String, Object> getHeader(LinkedHashMap<String, Object> localHeader) {

        LinkedHashMap<String, Object> mainHeader = formHeader;
        LinkedHashMap<String, Object> classHeaders = new LinkedHashMap<>();

        if(localHeader != null && localHeader.size() > 0){
            if(mainHeader != null && mainHeader.size() > 0) {
                for (Map.Entry<String, Object> entry : localHeader.entrySet()) {
                    String key = entry.getKey();
                    classHeaders.put(key, entry.getValue());
                }

                for (Map.Entry<String, Object> entry : mainHeader.entrySet()) {
                    String key = entry.getKey();
                    if(!classHeaders.containsKey(key)) {
                        classHeaders.put(key, entry.getValue());
                    }
                }

                return classHeaders;

            }else{
                return localHeader;
            }

        }else{
            return formHeader;
        }
    }


    /**
     * This method adds key and value to an Entry.
     * @param key The key as string which needs to be added to an Entry
     * @param value The value as string which needs to be added to an Entry
     * @return {@link Entry}
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     *    //'blt5d4sample2633b' is a dummy Stack API key
     *    //'blt6d0240b5sample254090d' is dummy access token.
     *    {@code
     *
     *    Stack stack = Contentstack.stack(context, "blt5d4sample2633b", "blt6d0240b5sample254090d", "stag", false);
     *    final Entry entry = stack.contentType("user").entry("blt3b0aaebf6f1c3762"); <br>
          entry.addParam("include_dimensions", "true"); <br>
     *    entry.fetch(new BuiltResultCallBack() {<br>
     *           &#64;Override
     *           public void onCompletion(ResponseType responseType, BuiltError builtError) {
     *
     *           }<br>
     *    });<br>
     *
     *      }
     *   </pre>
     *
     *
     */

    public Entry addParam(String key, String value){

        if(key != null && value != null){
                try {
                    otherPostJSON.put(key, value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }

        return this;
    }


}
