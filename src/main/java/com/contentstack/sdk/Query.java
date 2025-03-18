package com.contentstack.sdk;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;


import static com.contentstack.sdk.Constants.*;

/**
 * Contentstack provides certain queries that you can use to fetch filtered results. You can use queries for Entries
 * and
 * <a href= "https://www.contentstack.com/docs/developers/apis/content-delivery-api/#queries">Queries</a> Contentstack
 * provides certain queries that you can use to fetch filtered results. You can use queries for Entries and Assets API
 * requests.
 * <p>
 * You can now pass the branch header in the API request to fetch or manage modules located within specific branches of
 * the stack.
 * <p>
 * <b>Note</b> Branches is a plan-based feature that is available only in the
 * new Contentstack interface.
 *
 * @author Shailesh Mishra
 * @version 1.0.0
 * @since 01-11-2017
 */
public class Query implements INotifyClass {

    protected static final Logger logger = Logger.getLogger(Query.class.getSimpleName());
    protected ContentType contentTypeInstance = null;
    protected LinkedHashMap<String, Object> headers = null;
    protected JSONObject urlQueries;
    protected JSONObject mainJSON;
    protected String contentTypeUid;
    protected QueryResultsCallBack queryResultCallback;
    protected SingleQueryResultCallback singleQueryResultCallback;
    protected JSONObject queryValueJSON;
    protected JSONObject queryValue;
    protected JSONArray objectUidForInclude = null;
    protected JSONArray objectUidForExcept = null;
    protected JSONArray objectUidForOnly = null;
    private boolean isJsonProper = true;
    private String errorString;
    private JSONObject onlyJsonObject;
    private JSONObject exceptJsonObject;

    protected Query(String formName) {
        this.contentTypeUid = formName;
        this.urlQueries = new JSONObject();
        this.queryValue = new JSONObject();
        this.queryValueJSON = new JSONObject();
        this.mainJSON = new JSONObject();
    }

    protected void setContentTypeInstance(ContentType contentTypeInstance) {
        this.contentTypeInstance = contentTypeInstance;
    }

    /**
     * To set headers for Built.io Contentstack rest calls. <br> Scope is limited to this object and followed classes.
     *
     * @param key   header name.
     * @param value header value against given header name. <br>
     *
     *              <br>
     *              <br>
     *              <b>Example :</b><br>
     *              <pre>
     *                                            Stack stack = Contentstack.stack( "apiKey", "deliveryToken", "environment");
     *                                            <br>
     *                                            Query query = stack.contentType("contentTypeUid").query(); query.setHeader("custom_key", "custom_value");
     *                                        <br>
     *                                            Stack stack = Contentstack.stack( "apiKey", "deliveryToken", "environment");
     *                                            <br>
     *                                            Query csQuery = stack.contentType("contentTypeUid").query(); <br>csQuery.setHeader("custom_key", "custom_value");
     *                                        </pre>
     */
    public Query setHeader(@NotNull String key, @NotNull String value) {
        if (!key.isEmpty() && !value.isEmpty()) {
            this.headers.put(key, value);
        }
        return this;
    }

    /**
     * Remove header key @param key custom_header_key
     *
     * @param key {@link String}
     *            <b>Example :</b><br>
     *            <p>
     *            Stack stack = Contentstack..stack( "apiKey", "deliveryToken", "environment"); Query csQuery =
     *            stack.contentType("contentTypeUid").query();<br> csQuery.removeHeader("custom_key");
     */
    public Query removeHeader(@NotNull String key) {
        if (!key.isEmpty()) {
            this.headers.remove(key);
        }
        return this;
    }

    public String getContentType() {
        return contentTypeInstance.contentTypeUid;
    }

    //Sanitization of keys
    private boolean isValidKey(String key) {
        return key.matches("^[a-zA-Z0-9_.]+$");
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
     * Add a constraint to fetch all entries that contains given value against specified key
     *
     * @param key   field uid.
     * @param value field value which get &#39;included&#39; from the response.
     * @return {@link Query} object, so you can chain this call.
     * <p>
     * <b>Note :</b> for group field provide key in a
     * &#34;key.groupFieldUid&#34; format. <br>
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Query csQuery = stack.contentType("contentTypeUid").query();
     *         csQuery.where("uid", "entry_uid");
     *         </pre>
     */

    public Query where(@NotNull String key, Object value) {
        if (isValidKey(key) && isValidValue(value) && value != null) {
            queryValueJSON.put(key, value);
        } else {
            throwException("where", "Invalid key or value", null);
        }
        return this;
    }

    /**
     * Add a custom query against specified key.
     *
     * @param key   key.
     * @param value value.
     * @return {@link Query} object, so you can chain this call.
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *          Stack stack = Contentstack..stack( "apiKey", "deliveryToken", "environment");
     *          Query csQuery = stack.contentType("contentTypeUid").query();
     *          csQuery.addQuery("query_param_key", "query_param_value");
     *         </pre>
     */
    public Query addQuery(@NotNull String key, String value) {
        if (isValidKey(key) && isValidValue(value) && value != null) {
            urlQueries.put(key, value);
        }
        return this;
    }

    /**
     * Remove provided query key from custom query if existed.
     *
     * @param key Query name to remove.
     * @return {@linkplain Query} object, so you can chain this call. <br>
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *         projectQuery.removeQuery("Query_Key");
     *         </pre>
     */
    public Query removeQuery(@NotNull String key) {
        if (isValidKey(key) && urlQueries.has(key)) {
            urlQueries.remove(key);
        }
        return this;
    }

    /**
     * Combines all the queries together using AND operator
     *
     * @param queryObjects list of {@link Query} instances on which AND query executes.
     * @return {@link Query} object, so you can chain this call.
     *
     * <br>
     * <br>
     * <b>Example ;</b><br>
     *
     * <pre class="prettyprint">
     *          Stack stack = Contentstack..stack( "apiKey", "deliveryToken", "environment");
     *          Query csQuery = stack.contentType("contentTypeUid").query();
     *          Query query = projectClass.query();
     *          query.where('username','something');
     *          Query subQuery = projectClass.query();
     *          subQuery.where('email_address','something@email.com');
     *          ArrayList&#60;Query&#62; array = new ArrayList&#60;Query&#62;();<br>
     *          array.add(query);
     *          array.add(subQuery);<br>
     *          projectQuery.and(array);
     *         </pre>
     */
    public Query and(@NotNull List<Query> queryObjects) {
        if (!queryObjects.isEmpty()) {
            JSONArray orValueJson = new JSONArray();
            queryObjects.forEach(obj -> orValueJson.put(obj.queryValueJSON));
            queryValueJSON.put("$and", orValueJson);
        } else {
            throwException("and", "Can not process with blank query objects", null);
        }
        return this;
    }

    /**
     * Add a constraint to fetch all entries which satisfy <b> any </b> queries.
     *
     * @param queryObjects list of {@link Query} instances on which OR query executes.
     * @return {@link Query} object, so you can chain this call.
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *          Stack stack = Contentstack.stack( "apiKey", "deliveryToken", "environment");
     *          Query csQuery = stack.contentType("contentTypeUid").query();
     *          Query query = projectClass.query();
     *          query.where('username','something');
     *          Query subQuery = projectClass.query();
     *          subQuery.where('email_address','something@email.com');
     *          ArrayList&#60;Query&#62; array = new ArrayList&#60;Query&#62;();
     *          array.add(query);
     *          array.add(subQuery);<br>
     *          csQuery.or(array);
     *         </pre>
     */
    public Query or(List<Query> queryObjects) {
        if (queryObjects != null && !queryObjects.isEmpty()) {
            try {
                JSONArray orValueJson = new JSONArray();
                for (Query queryObject : queryObjects) {
                    orValueJson.put(queryObject.queryValueJSON);
                }
                queryValueJSON.put("$or", orValueJson);

            } catch (Exception e) {
                throwException("or", Constants.QUERY_EXCEPTION, e);
            }
        }

        return this;
    }

    /**
     * Add a constraint to the query that requires a particular key entry to be less than the provided value.
     *
     * @param key   the key to be constrained.
     * @param value the value that provides an upper bound.
     * @return {@link Query} object, so you can chain this call. <br>
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Query csQuery = stack.contentType("contentTypeUid").query();
     *         csQuery.lessThan("due_date", "2013-06-25T00:00:00+05:30");
     *         </pre>
     */
    public Query lessThan(@NotNull String key, @NotNull Object value) {
        if(isValidKey(key) && isValidValue(value)) {
            if (queryValueJSON.isNull(key)) {
                if (!queryValue.isEmpty()) {
                    queryValue = new JSONObject();
                }
                queryValue.put("$lt", value);
                queryValueJSON.put(key, queryValue);
            } else if (queryValueJSON.has(key)) {
                queryValue.put("$lt", value);
                queryValueJSON.put(key, queryValue);
            }
        } else {
            throwException("lessThan", "Invalid key or value", null);
        }
        return this;
    }

    /**
     * Add a constraint to the query that requires a particular key entry to be less than or equal to the provided
     * value.
     *
     * @param key   The key to be constrained
     * @param value The value that must be equalled.
     * @return {@link Query} object, so you can chain this call.
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Query csQuery = stack.contentType("contentTypeUid").query();
     *         csQuery.lessThanOrEqualTo("due_date", "2013-06-25T00:00:00+05:30");
     *         </pre>
     */
    public Query lessThanOrEqualTo(@NotNull String key, Object value) {
        if(isValidKey(key) && isValidValue(value)) {
            if (queryValueJSON.isNull(key)) {
                if (!queryValue.isEmpty()) {
                    queryValue = new JSONObject();
                }
                queryValue.put("$lte", value);
                queryValueJSON.put(key, queryValue);
            } else if (queryValueJSON.has(key)) {
                queryValue.put("$lte", value);
                queryValueJSON.put(key, queryValue);
            }    
        } else {
            throwException("lessThanOrEqualTo", "Invalid key or value", null);
        }                           
        return this;
    }

    /**
     * Add a constraint to the query that requires a particular key entry to be greater than the provided value.
     *
     * @param key   The key to be constrained.
     * @param value The value that provides a lower bound.
     * @return {@link Query} object, so you can chain this call.
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Query csQuery = stack.contentType("contentTypeUid").query();
     *         csQuery.greaterThan("due_date", "2013-06-25T00:00:00+05:30");
     *         </pre>
     */
    public Query greaterThan(@NotNull String key, Object value) {
         if(isValidKey(key) && isValidValue(value)) {  
            if (queryValueJSON.isNull(key)) {
                if (!queryValue.isEmpty()) {
                    queryValue = new JSONObject();
                }
                queryValue.put("$gt", value);
                queryValueJSON.put(key, queryValue);
            } else if (queryValueJSON.has(key)) {
                queryValue.put("$gt", value);
                queryValueJSON.put(key, queryValue);
            }
        } else {
            throwException("greaterThan", "Invalid key or value", null);
        }
        return this;
    }

    /**
     * Add a constraint to the query that requires a particular key entry to be greater than or equal to the provided
     * value.
     *
     * @param key   The key to be constrained.
     * @param value The value that provides a lower bound.
     * @return {@link Query} object, so you can chain this call.
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Query csQuery = stack.contentType("contentTypeUid").query();
     *         csQuery.greaterThanOrEqualTo("due_date", "2013-06-25T00:00:00+05:30");
     *         </pre>
     */
    public Query greaterThanOrEqualTo(String key, Object value) {
        if(isValidKey(key) && isValidValue(value)) {
            if (queryValueJSON.isNull(key)) {
                if (queryValue.length() > 0) {
                    queryValue = new JSONObject();
                }
                queryValue.put("$gte", value);
                queryValueJSON.put(key, queryValue);
            } else if (queryValueJSON.has(key)) {
                queryValue.put("$gte", value);
                queryValueJSON.put(key, queryValue);
            }
        } else {
            throwException("greaterThanOrEqualTo", "Invalid key or value", null);
        }
        return this;
    }

    /**
     * Add a constraint to the query that requires a particular key&#39;s entry to be not equal to the provided value.
     *
     * @param key   The key to be constrained.
     * @param value The object that must not be equaled.
     * @return {@link Query} object, so you can chain this call.
     *
     * <br>
     * <br>
     * <b>Example ;</b><br>
     *
     * <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Query csQuery = stack.contentType("contentTypeUid").query();
     *         csQuery.notEqualTo("due_date", "2013-06-25T00:00:00+05:30");
     *         </pre>
     */
    public Query notEqualTo(@NotNull String key, Object value) {
        if (isValidKey(key) && isValidValue(value)) { 
            if (queryValueJSON.isNull(key)) {
                if (queryValue.length() > 0) {
                    queryValue = new JSONObject();
                }
                queryValue.put("$ne", value);
                queryValueJSON.put(key, queryValue);
            } else if (queryValueJSON.has(key)) {
                queryValue.put("$ne", value);
                queryValueJSON.put(key, queryValue);
            }
        } else {
            throwException("notEqualTo", "Invalid key or value", null);
        }
        return this;
    }

    /**
     * Add a constraint to the query that requires a particular key&#39;s entry to be contained in the provided array.
     *
     * @param key    The key to be constrained.
     * @param values The possible values for the key&#39;s object.
     * @return {@link Query} object, so you can chain this call. <br>
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Query csQuery = stack.contentType("contentTypeUid").query();
     *         csQuery.containedIn("severity", new Object[] { "Show Stopper", "Critical" });
     *         </pre>
     */
    public Query containedIn(@NotNull String key, Object[] values) {
        if (isValidKey(key) && isValidValueList(values)) {
            JSONArray valuesArray = new JSONArray();
            for (Object value : values) {
                valuesArray.put(value);
            }
            if (queryValueJSON.isNull(key)) {
                if (queryValue.length() > 0) {
                    queryValue = new JSONObject();
                }
                queryValue.put("$in", valuesArray);
                queryValueJSON.put(key, queryValue);
            } else if (queryValueJSON.has(key)) {
                queryValue.put("$in", valuesArray);
                queryValueJSON.put(key, queryValue);
            }
        } else {
            throwException("containedIn", "Invalid key or value", null);
        }
        return this;
    }

    /**
     * Add a constraint to the query that requires a particular key entry&#39;s value not be contained in the provided
     * array.
     *
     * @param key    The key to be constrained.
     * @param values The list of values the key object should not be.
     * @return {@link Query} object, so you can chain this call.
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Query csQuery = stack.contentType("contentTypeUid").query();
     *         csQuery.notContainedIn("severity", new Object[] { "Show Stopper", "Critical" });
     *         </pre>
     */
    public Query notContainedIn(@NotNull String key, Object[] values) {
        if(isValidKey(key) && isValidValueList(values)) {
            JSONArray valuesArray = new JSONArray();
            for (Object value : values) {
                valuesArray.put(value);
            }
            if (queryValueJSON.isNull(key)) {
                if (queryValue.length() > 0) {
                    queryValue = new JSONObject();
                }
                queryValue.put("$nin", valuesArray);
                queryValueJSON.put(key, queryValue);
            } else if (queryValueJSON.has(key)) {
                queryValue.put("$nin", valuesArray);
                queryValueJSON.put(key, queryValue);
            }
        } else {
            throwException("notContainedIn", "Invalid key or value", null);
        }
        return this;
    }

    /**
     * Add a constraint that requires, a specified key exists in response.
     *
     * @param key The key to be constrained.
     * @return {@link Query} object, so you can chain this call.
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Query csQuery = stack.contentType("contentTypeUid").query();
     *         csQuery.exists("status");
     *         </pre>
     */
    public Query exists(@NotNull String key) {
        if(isValidKey(key)) {
            if (queryValueJSON.isNull(key)) {
                if (queryValue.length() > 0) {
                    queryValue = new JSONObject();
                }
                queryValue.put(EXISTS, true);
                queryValueJSON.put(key, queryValue);
            } else if (queryValueJSON.has(key)) {
                queryValue.put(EXISTS, true);
                queryValueJSON.put(key, queryValue);
            }
        } else {        
            throwException("exists", "Invalid key", null);
        }
        return this;
    }

    /**
     * Add a constraint that requires, a specified key does not exist in response.
     *
     * @param key The key to be constrained.
     * @return {@link Query} object, so you can chain this call. <br>
     *
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Query csQuery = stack.contentType("contentTypeUid").query();
     *         csQuery.notExists("status");
     *         </pre>
     */
    public Query notExists(@NotNull String key) {
        if(isValidKey(key)) {
            if (queryValueJSON.isNull(key)) {
                if (queryValue.length() > 0) {
                    queryValue = new JSONObject();
                }
                queryValue.put(EXISTS, false);
                queryValueJSON.put(key, queryValue);
            } else if (queryValueJSON.has(key)) {

                queryValue.put(EXISTS, false);
                queryValueJSON.put(key, queryValue);
            }
        } else {    
            throwException("notExists", "Invalid key", null);
        }
        return this;
    }

    /**
     * Add a constraint that requires a particular reference key details.
     *
     * @param key key that to be constrained.
     * @return {@link Query} object, so you can chain this call.
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Query csQuery = stack.contentType("contentTypeUid").query();
     *         csQuery.includeReference("for_bug");
     *         </pre>
     */
    public Query includeReference(String key) {
        if (objectUidForInclude == null) {
            objectUidForInclude = new JSONArray();
        }
        if(isValidKey(key)) {
            objectUidForInclude.put(key);
        } else {
            throwException("includeReference", "Invalid key", null);
        }
        return this;
    }

    /**
     * Include tags with which to search entries.
     *
     * @param tags Comma separated array of tags with which to search entries.
     * @return {@link Query} object, so you can chain this call. <br>
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Query csQuery = stack.contentType("contentTypeUid").query();
     *         csQuery.tags(new String[] { "tag1", "tag2" });
     *         </pre>
     */
    public Query tags(@NotNull String[] tags) {
        String tagstr = String.join(",", tags);
        if(isValidValue(tagstr)) {
            urlQueries.put("tags", tagstr);
        } else {
            throwException("tags", "Invalid tag", null);
        }
        return this;
    }

    /**
     * Sort the results in ascending order with the given key. <br> Sort the returned entries in ascending order of the
     * provided key.
     *
     * @param key The key to order by.
     * @return {@link Query} object, so you can chain this call. <br>
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Query csQuery = stack.contentType("contentTypeUid").query();
     *         csQuery.ascending("name");
     *         </pre>
     */

    public Query ascending(@NotNull String key) {
        if(isValidKey(key)){
            urlQueries.put("asc", key);
        } else {
            throwException("ascending", "Invalid key", null);
        }
        return this;
    }

    /**
     * Sort the results in descending order with the given key. <br> Sort the returned entries in descending order of
     * the provided key.
     *
     * @param key The key to order by.
     * @return {@link Query} object, so you can chain this call. <br>
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Query csQuery = stack.contentType("contentTypeUid").query();
     *         csQuery.descending("name");
     *         </pre>
     */
    public Query descending(@NotNull String key) {  
        if(isValidKey(key)){
            urlQueries.put("desc", key);
        } else {
            throwException("descending", "Invalid key", null);
        }
        return this;
    }

    /**
     * Specifies list of field ids that would be &#39;excluded&#39; from the response.
     *
     * @param fieldUid field uid which get &#39;excluded&#39; from the response.
     * @return {@link Query} object, so you can chain this call.
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *          Stack stack = Contentstack.stack( "apiKey", "deliveryToken", "environment");
     *          Query csQuery = stack.contentType("contentTypeUid").query();<br>
     *          ArrayList&#60;String&#62; array = new ArrayList&#60;String&#62;();
     *          array.add("name");
     *          array.add("description");<br>
     *          csQuery.except(array);
     *         </pre>
     */
    public Query except(@NotNull List<String> fieldUid) {
        if(isValidValue(fieldUid)){ 
            if (!fieldUid.isEmpty()) {
                if (objectUidForExcept == null) {
                    objectUidForExcept = new JSONArray();
                }
                for (String s : fieldUid) {
                    objectUidForExcept.put(s);
                }
            }
        } else {    
            throwException("except", "Invalid key", null);
        }
        return this;
    }

    /**
     * Specifies list of field ids that would be &#39;excluded&#39; from the response.
     *
     * @param fieldIds field uid which get &#39;excluded&#39; from the response.
     * @return {@link Query} object, so you can chain this call. <br>
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *          Stack stack = Contentstack.stack( "apiKey", "deliveryToken", "environment");
     *          Query csQuery = stack.contentType("contentTypeUid").query();<br>
     *          csQuery.except(new String[]{"name", "description"});
     *         </pre>
     */
    public Query except(@NotNull String[] fieldIds) {
        if(isValidValue(fieldIds)) {
            if (fieldIds.length > 0) {
                if (objectUidForExcept == null) {
                    objectUidForExcept = new JSONArray();
                }
                for (String fieldId : fieldIds) {
                    objectUidForExcept.put(fieldId);
                }
            }
        } else {
            throwException("except", "Invalid key", null);
        }
        return this;
    }

    /**
     * Specifies an array of &#39;only&#39; keys in BASE object that would be &#39;included&#39; in the response.
     *
     * @param fieldUid Array of the &#39;only&#39; reference keys to be included in response.
     * @return {@link Query} object, so you can chain this call. <br>
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *          Stack stack = Contentstack.stack( "apiKey", "deliveryToken", "environment");
     *          Query csQuery = stack.contentType("contentTypeUid").query();<br>
     *          csQuery.only(new String[]{"name"});
     *         </pre>
     */
    public Query only(@NotNull String[] fieldUid) {
        if(isValidValue(fieldUid)){
            if (fieldUid.length > 0) {
                if (objectUidForOnly == null) {
                    objectUidForOnly = new JSONArray();
                }
                for (String s : fieldUid) {
                    objectUidForOnly.put(s);
                }
            }
        } else {
            throwException("only", "Invalid key", null);
        }
        return this;
    }

    /**
     * Specifies an array of &#39;only&#39; keys that would be &#39;included&#39; in the response.
     *
     * @param fieldUid          Array of the &#39;only&#39; reference keys to be included in response.
     * @param referenceFieldUid Key who has reference to some other class object.
     * @return {@link Query} object, so you can chain this call. <br>
     *
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *          Stack stack = Contentstack.stack( "apiKey", "deliveryToken", "environment");
     *          Query csQuery = stack.contentType("contentTypeUid").query();<br>
     *          ArrayList&#60;String&#62; array = new ArrayList&#60;String&#62;();
     *          array.add("description");
     *          array.add("name");<br>
     *          csQuery.onlyWithReferenceUid(array, "for_bug");
     *         </pre>
     */
    public Query onlyWithReferenceUid(@NotNull List<String> fieldUid, @NotNull String referenceFieldUid) {
        if(isValidValue(referenceFieldUid)){
            if (onlyJsonObject == null) {
                onlyJsonObject = new JSONObject();
            }
            JSONArray fieldValueArray = new JSONArray();
            for (String s : fieldUid) {
                fieldValueArray.put(s);
            }
            onlyJsonObject.put(referenceFieldUid, fieldValueArray);
            if (objectUidForInclude == null) {
                objectUidForInclude = new JSONArray();
            }
            objectUidForInclude.put(referenceFieldUid);
        } else {
            throwException("onlyWithReferenceUid", "Invalid key or value", null);
        }
        return this;
    }

    /**
     * Specifies an array of &#39;except&#39; keys that would be &#39;excluded&#39; in the response.
     *
     * @param fieldUid          Array of the &#39;except&#39; reference keys to be excluded in response.
     * @param referenceFieldUid Key who has reference to some other class object.
     * @return {@link Query} object, so you can chain this call. <br>
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *          Stack stack = Contentstack.stack( "apiKey", "deliveryToken", "environment");
     *          Query csQuery = stack.contentType("contentTypeUid").query();<br>
     *          ArrayList&#60;String&#62; array = new ArrayList&#60;String&#62;();
     *          array.add("description");
     *          array.add("name");<br>
     *          csQuery.exceptWithReferenceUid(array, "for_bug");
     *         </pre>
     */
    public Query exceptWithReferenceUid(@NotNull List<String> fieldUid, @NotNull String referenceFieldUid) {
        if(isValidValue(referenceFieldUid)){
            if (exceptJsonObject == null) {
                exceptJsonObject = new JSONObject();
            }
            JSONArray fieldValueArray = new JSONArray();
            for (String s : fieldUid) {
                fieldValueArray.put(s);
            }
            exceptJsonObject.put(referenceFieldUid, fieldValueArray);
            if (objectUidForInclude == null) {
                objectUidForInclude = new JSONArray();
            }
            objectUidForInclude.put(referenceFieldUid);
        } else {
            throwException("exceptWithReferenceUid", "Invalid key or value", null);
        }
        return this;
    }

    /**
     * Retrieve only count of entries in result.
     *
     * @return {@link Query} object, so you can chain this call. <b>Note :- </b> Call {@link QueryResult#getCount()}
     * method in the success to get count of objects. <br>
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *          Stack stack = Contentstack.stack( "apiKey", "deliveryToken", "environment");
     *          Query csQuery = stack.contentType("contentTypeUid").query();<br>
     *          csQuery.count();
     *         </pre>
     */
    public Query count() {
        urlQueries.put("count", "true");
        return this;
    }

    /**
     * Retrieve count and data of objects in result
     *
     * @return {@link Query} object, so you can chain this call. <b>Note :- </b> Call {@link QueryResult#getCount()}
     * method in the success to get count of objects. <br>
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *          Stack stack = Contentstack.stack( "apiKey", "deliveryToken", "environment");
     *          Query csQuery = stack.contentType("contentTypeUid").query();<br>
     *          csQuery.includeCount();
     *         </pre>
     */
    public Query includeCount() {
        urlQueries.put("include_count", "true");
        return this;
    }

    /**
     * Include Content Type of all returned objects along with objects themselves.
     *
     * @return {@link Query} object, so you can chain this call. <br>
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *          Stack stack = Contentstack.stack( "apiKey", "deliveryToken", "environment");
     *          Query csQuery = stack.contentType("contentTypeUid").query();<br>
     *          csQuery.includeContentType();
     *         </pre>
     */
    public Query includeContentType() {
        if (urlQueries.has("include_schema")) {
            urlQueries.remove("include_schema");
        }
        urlQueries.put("include_content_type", true);
        urlQueries.put("include_global_field_schema", true);
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
     *          Query csQuery = stack.contentType("contentTypeUid").query();<br>
     *          csQuery.skip(2);
     *         </pre>
     */
    public Query skip(int number) {
        urlQueries.put("skip", number);
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
     *          Query query = stack.contentType("contentTypeUid").query();<br>
     *          query.limit(2);
     *         </pre>
     */
    public Query limit(int number) {
        urlQueries.put(LIMIT, number);
        return this;
    }

    /**
     * Add a regular expression constraint for finding string values that match the provided regular expression. This
     * may be slow for large data sets.
     *
     * @param key   The key to be constrained.
     * @param regex The regular expression pattern to match.
     * @return {@link Query} object, so you can chain this call. <br>
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *          Stack stack = Contentstack.stack( "apiKey", "deliveryToken", "environment");
     *          Query query = stack.contentType("contentTypeUid").query();<br>
     *          query.regex("name", "^browser");
     *         </pre>
     */

    public Query regex(@NotNull String key, @NotNull String regex) {
        if(isValidKey(key) && isValidValue(regex)) {
            if (queryValueJSON.isNull(key)) {
                if (!queryValue.isEmpty()) {
                    queryValue = new JSONObject();
                }
                queryValue.put(REGEX, regex);
                queryValueJSON.put(key, queryValue);
            } else if (queryValueJSON.has(key)) {
                queryValue.put(REGEX, regex);
                queryValueJSON.put(key, queryValue);
            }
        } else {
            throwException(REGEX, Constants.QUERY_EXCEPTION, null);
        }
        return this;
    }

    /**
     * Add a regular expression constraint for finding string values that match the provided regular expression. This
     * may be slow for large data sets.
     *
     * @param key       The key to be constrained.
     * @param regex     The regular expression pattern to match
     * @param modifiers Any of the following supported Regular expression modifiers.
     *                  <p>
     *                  use <b> i </b> for case-insensitive matching.
     *                  </p>
     *                  <p>
     *                  use <b> m </b> for making dot match newlines.
     *                  </p>
     *                  <p>
     *                  use <b> x </b> for ignoring whitespace in regex
     *                  </p>
     * @return {@link Query} object, so you can chain this call. <br>
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *          Stack stack = Contentstack.stack( "apiKey", "deliveryToken", "environment");
     *          Query query = stack.contentType("contentTypeUid").query();<br>
     *          query.regex("name", "^browser", "i");
     *         </pre>
     */

    public Query regex(@NotNull String key, @NotNull String regex, String modifiers) {
        try {
            if (queryValueJSON.isNull(key)) {
                if (queryValue.length() > 0) {
                    queryValue = new JSONObject();
                }
                queryValue.put(REGEX, regex);
                if (modifiers != null) {
                    queryValue.put(OPTIONS, modifiers);
                }
                queryValueJSON.put(key, queryValue);
            } else if (queryValueJSON.has(key)) {
                queryValue.put(REGEX, regex);
                if (modifiers != null) {
                    queryValue.put(OPTIONS, modifiers);
                }
                queryValueJSON.put(key, queryValue);
            }
        } catch (Exception e) {
            throwException(OPTIONS, Constants.QUERY_EXCEPTION, e);
        }
        return this;
    }

    /**
     * set Language using locale code.
     *
     * @param locale {@link String} value
     * @return {@link Query} object, so you can chain this call <br>
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *          Stack stack = Contentstack.stack( "apiKey", "deliveryToken", "environment");
     *          Query query = stack.contentType("contentTypeUid").query();<br>
     *          query.locale("en-us");
     *         </pre>
     */
    public Query locale(@NotNull String locale) {
        urlQueries.put("locale", locale);
        return this;
    }

    /**
     * This method provides only the entries matching the specified value.
     *
     * @param value value used to match or compare
     * @return {@link Query} object, so you can chain this call. <br>
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *          Stack stack = Contentstack.stack( "apiKey", "deliveryToken", "environment");
     *          Query query = stack.contentType("contentTypeUid").query();<br>
     *          query.search("header");
     *         </pre>
     */

    public Query search(@NotNull String value) {
        if(isValidValue(value)) {
            if (urlQueries.isNull(value)) {
                urlQueries.put("typeahead", value);
            }
        } else {
            throwException("search", "Invalid value", null);
        }
        return this;
    }

    /**
     * Execute a Query and Caches its result (Optional)
     *
     * @param callback {@link QueryResultsCallBack} object to notify the application when the request has completed.
     * @return {@linkplain Query} object, so you can chain this call. <br>
     *
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *          Stack stack = Contentstack.stack( "apiKey", "deliveryToken", "environment");
     *          Query query = stack.contentType("contentTypeUid").query();<br>
     *          query.find(new QueryResultsCallBack() {<br>
     *              &#64;Override
     *          public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {<br>
     *              }
     *      });<br>
     *         </pre>
     */
    public Query find(QueryResultsCallBack callback) {
        Error error;
        if (isJsonProper) {
            if (!contentTypeUid.isEmpty()) {
                execQuery(null, callback);
            } else {
                throwException("find", Constants.CONTENT_TYPE_NAME, null);
                error = new Error();
                error.setErrorMessage(errorString);
            }
        } else {
            error = new Error();
            error.setErrorMessage(errorString);
        }
        return this;
    }

    /**
     * Execute a Query and Caches its result (Optional)
     *
     * @param callBack {@link QueryResultsCallBack} object to notify the application when the request has completed.
     * @return {@linkplain Query} object, so you can chain this call. <br>
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *          Stack stack = Contentstack.stack( "apiKey", "deliveryToken", "environment");
     *          Query csQuery = stack.contentType("contentTypeUid").query();<br>
     *          csQuery.findOne(new QueryResultsCallBack() {<br>
     *              &#64;Override
     *          public void onCompletion(ResponseType responseType, ENTRY entry, Error error) {<br>
     *
     *          }
     *      });<br>
     *         </pre>
     */
    public Query findOne(SingleQueryResultCallback callBack) {
        if (isJsonProper) {
            if (!contentTypeUid.isEmpty()) {
                int limit = -1;
                if (urlQueries != null && urlQueries.has(LIMIT)) {
                    limit = (int) urlQueries.get(LIMIT);
                }
                if (urlQueries != null) {
                    urlQueries.put(LIMIT, 1);
                    execQuery(callBack, null);
                    if (limit != -1) {
                        urlQueries.put(LIMIT, limit);
                    }
                }
            } else {
                throwException("find", Constants.CONTENT_TYPE_NAME, null);
            }
        }
        return this;
    }

    private void throwException(String queryName, String messageString, @Nullable Exception e) {
        HashMap<String, Object> errorHashMap = new HashMap<>();
        isJsonProper = false;
        errorString = messageString;
        if (e != null) {
            errorHashMap.put(queryName, e.getLocalizedMessage());
        }
        errorHashMap.put("detail", messageString);
        assert e != null;
        logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
    }

    protected void setQueryJson() {
        if (queryValueJSON != null && queryValueJSON.length() > 0) {
            urlQueries.put(QUERY, queryValueJSON);
        }
        if (objectUidForExcept != null && objectUidForExcept.length() > 0) {
            urlQueries.put("except[BASE][]", objectUidForExcept);
            objectUidForExcept = null;
        }
        if (objectUidForOnly != null && objectUidForOnly.length() > 0) {
            urlQueries.put("only[BASE][]", objectUidForOnly);
            objectUidForOnly = null;
        }
        if (onlyJsonObject != null && onlyJsonObject.length() > 0) {
            urlQueries.put("only", onlyJsonObject);
            onlyJsonObject = null;
        }
        if (exceptJsonObject != null && exceptJsonObject.length() > 0) {
            urlQueries.put(EXCEPT, exceptJsonObject);
            exceptJsonObject = null;
        }
        if (objectUidForInclude != null && objectUidForInclude.length() > 0) {
            urlQueries.put("include[]", objectUidForInclude);
            objectUidForInclude = null;
        }
    }

    protected void execQuery(SingleQueryResultCallback callBack, QueryResultsCallBack callback) {
        String urlString = "content_types/" + contentTypeUid + "/entries";
        queryResultCallback = callback;
        singleQueryResultCallback = callBack;
        setQueryJson();
        urlQueries.put(Constants.ENVIRONMENT, this.headers.get(Constants.ENVIRONMENT));
        includeLivePreview();
        mainJSON.put(QUERY, urlQueries);
        fetchFromNetwork(urlString, mainJSON, callback, callBack);
    }

    private void includeLivePreview() {
        Config ci = contentTypeInstance.stackInstance.config;
        if (ci.enableLivePreview
                && ci.livePreviewContentType.equalsIgnoreCase(contentTypeUid)
                && (ci.livePreviewHash == null
                || ci.livePreviewHash.isEmpty())) {
            ci.livePreviewHash = "init";
        }
    }

    // fetch from network.
    private void fetchFromNetwork(String urlString, JSONObject jsonMain, ResultCallBack callback, SingleQueryResultCallback resultCallback) {
        LinkedHashMap<String, Object> urlParams = getUrlParams(jsonMain);
        if (resultCallback != null) {
            new CSBackgroundTask(this, contentTypeInstance.stackInstance, Constants.SINGLEQUERYOBJECT, urlString,
                    this.headers, urlParams, Constants.REQUEST_CONTROLLER.QUERY.toString(), resultCallback);
        } else {
            new CSBackgroundTask(this, contentTypeInstance.stackInstance, Constants.QUERYOBJECT, urlString,
                    this.headers, urlParams, Constants.REQUEST_CONTROLLER.QUERY.toString(), callback);
        }
    }

    private LinkedHashMap<String, Object> getUrlParams(JSONObject jsonMain) {
        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<>();
        JSONObject queryJSON = jsonMain.optJSONObject(QUERY);
        if (queryJSON != null && queryJSON.length() > 0) {
            Iterator<String> itr = queryJSON.keys();
            while (itr.hasNext()) {
                String key = itr.next();
                Object value = queryJSON.opt(key);
                hashMap.put(key, value);
            }
        }
        return hashMap;
    }

    @Override
    public void getResult(Object object, String controller) {
        // It would not be called.
        //System.out.println(object);
    }

    @Override
    public void getResultObject(List<Object> objects, JSONObject jsonObject, boolean isSingleEntry) {
        List<Entry> objectList = new ArrayList<>();
        for (Object object : objects) {
            Entry entry;
            try {
                entry = contentTypeInstance.stackInstance.contentType(contentTypeUid)
                        .entry(((EntryModel) object).uid);
            } catch (Exception e) {
                entry = new Entry(contentTypeUid);
            }
            entry.setUid(((EntryModel) object).uid);
            entry.resultJson = ((EntryModel) object).jsonObject;
            entry.title = ((EntryModel) object).title;
            entry.url = ((EntryModel) object).url;
            entry.setTags(((EntryModel) object).tags);
            objectList.add(entry);
        }
        if (isSingleEntry) {
            Entry entry = contentTypeInstance.entry();
            if (!objectList.isEmpty()) {
                entry = objectList.get(0);
            }
            if (singleQueryResultCallback != null) {
                singleQueryResultCallback.onRequestFinish(ResponseType.NETWORK, entry);
            }
        } else {
            QueryResult queryResultObject = new QueryResult();
            queryResultObject.setJSON(jsonObject, objectList);
            if (queryResultCallback != null) {
                queryResultCallback.onRequestFinish(ResponseType.NETWORK, queryResultObject);
            }
        }

    }

    /**
     * This method adds key and value to an Entry. Parameters:
     *
     * @param key:   The key as string which needs to be added to the Query
     * @param value: The value as string which needs to be added to the Query
     * @return - Query
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Query csQuery = stack.contentType("contentTypeUid").query();
     *         csQuery.addParam("key", "some_value");
     *         csQuery.findOne(new QueryResultsCallBack() {
     *             &#64;Override
     *             public void onCompletion(ResponseType responseType, ENTRY entry, Error error) {
     *             }
     *         });
     *         </pre>
     */
    public Query addParam(@NotNull String key, @NotNull String value) {
        if(isValidKey(key) && isValidValue(value)) {
            urlQueries.put(key, value);
        } else {
            throwException("addParam", "Invalid key or value", null);
        }
        return this;
    }

    /**
     * This method also includes the content type UIDs of the referenced entries returned in the response
     *
     * @return {@link Query} <br>
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *          Stack stack = Contentstack.stack( "apiKey", "deliveryToken", "environment");
     *          Query csQuery = stack.contentType("contentTypeUid").query();<br>
     *          csQuery.includeReferenceContentTypUid();
     *          csQuery.findOne(new QueryResultsCallBack() {<br>
     *          &#64;Override
     *          public void onCompletion(ResponseType responseType, ENTRY entry, Error error) {<br>
     *          }
     *      });<br>
     *         </pre>
     */
    public Query includeReferenceContentTypUid() {
        urlQueries.put("include_reference_content_type_uid", "true");
        return this;
    }

    /**
     * Get entries having values based on referenced fields. This query retrieves all entries that satisfy the query
     * conditions made on referenced fields.
     *
     * @param key         The key to be constrained
     * @param queryObject {@link Query} object, so you can chain this call
     * @return {@link Query} object, so you can chain this call <br>
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Query csQuery = stack.contentType("contentTypeUid").query();
     *         csQuery.whereIn("due_date", csQuery);
     *         </pre>
     */
    public Query whereIn(@NotNull String key, Query queryObject) {
        if(isValidKey(key)){
            JSONObject inQueryObj = new JSONObject();
            inQueryObj.put("$in_query", queryObject.queryValueJSON.toString());
            queryValueJSON.put(key, inQueryObj);
        } else {
            throwException("whereIn", "Invalid key", null);
        }
        return this;
    }

    /**
     * Get entries having values based on referenced fields. This query works the opposite of $in_query and retrieves
     * all entries that does not satisfy query conditions made on referenced fields.
     *
     * @param key         The key to be constrained
     * @param queryObject {@link Query} object, so you can chain this call
     * @return {@link Query} object, so you can chain this call
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Query csQuery = stack.contentType("contentTypeUid").query();
     *         csQuery.whereNotIn("due_date", csQuery);
     *         </pre>
     */
    public Query whereNotIn(@NotNull String key, Query queryObject) {
        if(isValidKey(key)){    
            JSONObject inQueryObj = new JSONObject();
            inQueryObj.put("$nin_query", queryObject.queryValueJSON.toString());
            queryValueJSON.put(key, inQueryObj);
        } else {
            throwException("whereNotIn", "Invalid key", null);
        }
        return this;
    }

    /**
     * Retrieve the published content of the fallback locale if an entry is not localized in specified locale
     *
     * @return {@link Query} object, so you can chain this call. <br>
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", environment);
     *         Query csQuery = stack.contentType("contentTypeUid").query();
     *         csQuery.includeFallback();
     *         </pre>
     */
    public Query includeFallback() {
        urlQueries.put("include_fallback", true);
        return this;
    }

    /**
     * @return {@link Query} object, so you can chain this call. <br>
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", environment);
     *         Query query = stack.contentType("contentTypeUid").query();
     *         query.includeEmbeddedObjects()
     *         </pre>
     */
    public Query includeEmbeddedItems() {
        urlQueries.put("include_embedded_items[]", "BASE");
        return this;
    }

    /**
     * Includes Branch in the entry response
     *
     * @return {@link Query} object, so you can chain this call. <br>
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Query query = stack.contentType("contentTypeUid").query();
     *         entry.includeBranch();
     *         </pre>
     */
    public Query includeBranch() {
        urlQueries.put("include_branch", true);
        return this;
    }

    /**
     * Includes Metadata in the entry response
     *
     * @return {@link Query} object, so you can chain this call. <br>
     *
     * <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     *         Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment");
     *         Query query = stack.contentType("contentTypeUid").query();
     *         entry.includeMetadata();
     *         </pre>
     */
    public Query includeMetadata() {
        urlQueries.put("include_metadata", true);
        return this;
    }
}
