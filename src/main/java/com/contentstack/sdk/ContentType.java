package com.contentstack.sdk;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author  Contentstack.com
 */

public class ContentType {

    protected String contentTypeName = null;
    protected Stack stackInstance = null;
    private LinkedHashMap<String, Object> localHeader = null;
    private LinkedHashMap<String, Object> stackHeader = null;

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

}

