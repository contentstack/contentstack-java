package com.contentstack.sdk;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * QueryResult works as the Query Response that works as getter as per the Json Key
 */
public class QueryResult {

    protected static final Logger logger = Logger.getLogger(QueryResult.class.getSimpleName());
    protected JSONObject receiveJson;
    protected JSONArray schemaArray;
    protected JSONObject contentObject;
    protected int count;
    protected List<Entry> resultObjects;

    /**
     * @return List of {@link Entry} objects list. <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     * List&#60;Entry&#62; list = queryResultObject.getResultObjects();<br>
     *         </pre>
     */
    public List<Entry> getResultObjects() {
        return resultObjects;
    }

    /**
     * Returns count of objects available.<br>
     * <b>Note : </b> To retrieve this data, {@link Query#includeCount()} or
     * {@link Query#count()} should be added in {@link Query} while querying.
     *
     * @return int count <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     * int count = queryResultObject.getCount();<br>
     *         </pre>
     */

    public int getCount() {
        return count;
    }

    /**
     * Returns class&#39;s schema if call to fetch schema executed successfully.
     *
     * @return JSONArray schema Array <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     * JSONArray schemaArray = queryResultObject.getSchema();<br>
     *         </pre>
     */
    public JSONArray getSchema() {
        return schemaArray;
    }

    /**
     * Returns class&#39;s content type if call to fetch contentType executed successfully.
     *
     * @return JSONObject contentObject <br>
     * <br>
     * <b>Example :</b><br>
     *
     * <pre class="prettyprint">
     * JSONObject contentObject = queryResultObject.getContentType();<br>
     *         </pre>
     */
    public JSONObject getContentType() {
        return contentObject;
    }


    public void setJSON(JSONObject jsonObject, List<Entry> objectList) {
        receiveJson = jsonObject;
        resultObjects = objectList;
        extractSchemaArray();
        extractContentObject();
        extractCount();
    }

    private void extractSchemaArray() {
        try {
            if (receiveJson != null && receiveJson.has("schema")) {
                JSONArray jsonArray = receiveJson.getJSONArray("schema");
                if (jsonArray != null) {
                    schemaArray = jsonArray;
                }
            }
        } catch (Exception e) {
            logException(e);
        }
    }

    private void extractContentObject() {
        try {
            if (receiveJson != null && receiveJson.has("content_type")) {
                JSONObject jsonObject = receiveJson.getJSONObject("content_type");
                if (jsonObject != null) {
                    contentObject = jsonObject;
                }
            }
        } catch (Exception e) {
            logException(e);
        }
    }

    private void extractCount() {
        try {
            if (receiveJson != null) {
                count = receiveJson.optInt("count");
                if (count == 0 && receiveJson.has("entries")) {
                    count = receiveJson.optInt("entries");
                }
            }
        } catch (Exception e) {
            logException(e);
        }
    }

    private void logException(Exception e) {
        logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
    }


}
