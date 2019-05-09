package com.contentstack.sdk;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * @Author Contentstack
 *
 * MIT License
 *
 * Copyright (c) 2012 - 2019 Contentstack
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */


public class QueryResult {


    private static final String TAG = "QueryResult";
    protected JSONObject receiveJson;
    protected JSONArray schemaArray;
    protected JSONObject contentObject;
    protected int count;
    protected List<Entry> resultObjects;


    /**
     * @return List of {@link Entry} objects list.
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * List&#60;Entry&#62; list = queryResultObject.getResultObjects();<br>
     * </pre>
     */
    public List<Entry> getResultObjects() {
        return resultObjects;
    }



    /**
     * Returns count of objects available.<br>
     * <b>Note : </b>
     * To retrieve this data, {@link Query#includeCount()} or {@link Query#count()}
     * should be added in {@link Query} while querying.
     * @return int count
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * int count = queryResultObject.getCount();<br>
     * </pre>
     */

    public int getCount() {
        return count;
    }


    /**
     * Returns class&#39;s schema if call to fetch schema executed successfully.
     * @return JSONArray schema Array
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * JSONArray schemaArray = queryResultObject.getSchema();<br>
     * </pre>
     */
    public JSONArray getSchema() {
        return schemaArray;
    }


    /**
     * Returns class&#39;s content type if call to fetch contentType executed successfully.
     * @return JSONObject contentObject
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * JSONObject contentObject = queryResultObject.getContentType();<br>
     * </pre>
     */
    public JSONObject getContentType() {
        return contentObject;
    }



    protected void setJSON(JSONObject jsonobject, List<Entry> objectList) {
        receiveJson = jsonobject;
        resultObjects = objectList;

        try{
            if(receiveJson != null){
                if(receiveJson.has("schema")){

                    JSONArray jsonarray = new JSONArray();
                    jsonarray  = receiveJson.getJSONArray("schema");
                    if(jsonarray != null){
                        schemaArray = jsonarray;
                    }
                }

                if(receiveJson.has("content_type")){

                    JSONObject jsonObject  = receiveJson.getJSONObject("content_type");
                    if(jsonObject != null){
                        contentObject = jsonObject;
                    }
                }

                if(receiveJson.has("count")){
                    count = receiveJson.optInt("count");
                }

                if(count <= 0){
                    if(receiveJson.has("entries")){
                        count = receiveJson.optInt("entries");
                    }
                }
            }

        }catch(Exception e){
            Stack.log(TAG, "-QueryResult--setJSON--"+e.getLocalizedMessage());
        }
    }


}
