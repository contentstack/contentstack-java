package com.contentstack.sdk;

import com.contentstack.sdk.utility.CSAppConstants;
import com.contentstack.sdk.utility.CSController;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

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

public class ContentType {

    protected String contentTypeName = null;
    protected Stack stackInstance = null;
    private LinkedHashMap<String, Object> localHeader = null;
    private LinkedHashMap<String, Object> stackHeader = null;
    private final String TAG = ContentType.class.getSimpleName();

    private ContentType(){}

    protected ContentType(String contentTypeName) {
        this.contentTypeName = contentTypeName;
        this.localHeader = new LinkedHashMap<>();
    }

    protected void setStackInstance(Stack stack){
        this.stackInstance = stack;
        this.stackHeader = stack.localHeader;
    }


    /**
     * To set headers for Built.io Contentstack rest calls.
     * <br>
     * Scope is limited to this object and followed classes.
     * @param key
     * header name.
     * @param value
     * header value against given header name.
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * //'blt5d4sample2633b' is a dummy Stack API key
     * //'blt6d0240b5sample254090d' is dummy access token.
     * Stack stack = Contentstack.stack(context, "blt5d4sample2633b", "blt6d0240b5sample254090d", "stag", false);
     * ContentType contentType = stack.contentType("form_name");<br>
     * contentType.setHeader("custom_key", "custom_value");
     * </pre>
     */

    public void setHeader(String key, String value){
        if(!key.isEmpty() && !value.isEmpty()){
            localHeader.put(key, value);
        }
    }


    /**
     * Remove header key.
     * @param key
     * custom_header_key
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * //'blt5d4sample2633b' is a dummy Stack API key
     * //'blt6d0240b5sample254090d' is dummy access token.
     *  Stack stack = Contentstack.stack(context, "blt5d4sample2633b", "blt6d0240b5sample254090d", "stag", false);
     *  ContentType contentType = stack.contentType("form_name");<br>
     *  contentType.removeHeader("custom_header_key");
     * </pre>
     */

    public void removeHeader(String key){
        if(!key.isEmpty()){
            localHeader.remove(key);
        }
    }



    /**
     * Represents a {@link Entry}.
     * Create {@link Entry} instance
     * @param entryUid
     * Set entry uid.
     * @return
     * {@link Entry} instance.
     *  <br><br><b>Example :</b><br>
     *  <pre class="prettyprint">
     *  //'blt5d4sample2633b' is a dummy Stack API key
     *  //'blt6d0240b5sample254090d' is dummy access token.
     *  Stack stack = Contentstack.stack(context, "blt5d4sample2633b", "blt6d0240b5sample254090d", "stag", false);
     *  ContentType contentType = stack.contentType("form_name");<br>
     *  // setUid will identify the object, and calling save will update it
     *  ENTRY entry = contentType.entry("bltf4fbbc94e8c851db");
     *  </pre>
     */
    public Entry entry(String entryUid) {
        Entry entry = new Entry(contentTypeName);
        entry.formHeader = getHeader(localHeader);
        entry.setContentTypeInstance(this);
        entry.setUid(entryUid);

        return entry;
    }



    protected Entry entry(){
        Entry entry = new Entry(contentTypeName);
        entry.formHeader = getHeader(localHeader);
        entry.setContentTypeInstance(this);

        return entry;
    }



    /**
     * Represents a {@link Query}.
     * Create {@link Query} instance.
     * @return
     * {@link Query} instance.
     *  <br><br><b>Example :</b><br>
     *  <pre class="prettyprint">
     *  //'blt5d4sample2633b' is a dummy Stack API key
     *  //'blt6d0240b5sample254090d' is dummy access token.
     *  Stack stack = Contentstack.stack(context, "blt5d4sample2633b", "blt6d0240b5sample254090d", "stag", false);
     *  ContentType contentType = stack.contentType("form_name");<br>
     *  Query csQuery = contentType.query();
     *  </pre>
     */

    public Query query(){
        Query query = new Query(contentTypeName);
        query.formHeader = getHeader(localHeader);
        query.setContentTypeInstance(this);
        return query;
    }




    /**
     *
     *
     * @return callback ContentTypesCallback
     * This call returns information of a specific content type. It returns the content type schema, but does not include its entries.
     *
     *  <br><br><b>Example :</b><br>
     *  <pre class="prettyprint">
     * ContentType  contentType = stack.contentType("content_type_uid");
     * contentType.fetch(new ContentTypesCallback() {
     * @Override
     * public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
     * if (error==null){
     *
     * }else {
     *
     * }
     * }
     * });
     *</pre>
     */


    public void fetch(final ContentTypesCallback callback) {

        try {

            String URL = "/" + stackInstance.VERSION + "/content_types/"+contentTypeName;
            HashMap<String, Object> headers = getHeader(localHeader);
            JSONObject param = new JSONObject();
            if (headers.containsKey("environment")) {
                param.put("environment", headers.get("environment"));
            }

            if (contentTypeName!=null) {
                fetchContentTypes(URL, param, headers, callback );
            }else {
                Error error = new Error();
                error.setErrorMessage(CSAppConstants.ErrorMessage_JsonNotProper);
                callback.onRequestFail(ResponseType.UNKNOWN, error);
            }


        }catch (Exception e){

            Error error = new Error();
            error.setErrorMessage(CSAppConstants.ErrorMessage_JsonNotProper);
            callback.onRequestFail(ResponseType.UNKNOWN, error);
        }

    }



    private void fetchContentTypes(String urlString, JSONObject content_type_param, HashMap<String, Object> headers, ContentTypesCallback callback) {

        if(callback != null) {

            HashMap<String, Object> urlParams = getUrlParams(content_type_param);
            new CSBackgroundTask(this,  stackInstance, CSController.FETCHCONTENTTYPES, urlString, headers, urlParams, new JSONObject(), CSAppConstants.callController.CONTENTTYPES.toString(), false, CSAppConstants.RequestMethod.GET, callback);
        }
    }



    /**
     *
     */
    private LinkedHashMap<String, Object> getHeader(LinkedHashMap<String, Object> localHeader) {
        LinkedHashMap<String, Object> mainHeader = stackHeader;
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
            return stackHeader;
        }
    }



    private HashMap<String, Object> getUrlParams(JSONObject urlQueriesJSON) {

        HashMap<String, Object> hashMap = new HashMap<>();

        if(urlQueriesJSON != null && urlQueriesJSON.length() > 0){
            Iterator<String> iter = urlQueriesJSON.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    Object value = urlQueriesJSON.opt(key);
                    hashMap.put(key, value);
                } catch (Exception e) {
                    Stack.log(TAG,"setQueryJson"+e.toString());
                }
            }

            return hashMap;
        }

        return null;
    }

}

