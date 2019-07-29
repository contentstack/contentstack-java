package com.contentstack.sdk;


import com.contentstack.sdk.utility.CSAppConstants;
import com.contentstack.sdk.utility.CSController;
import org.json.JSONObject;

import java.util.*;

/**
 * @Author Shailesh Mishra
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

public class AssetLibrary implements INotifyClass{

    private final static String TAG = "AssetLibrary";
    private com.contentstack.sdk.Stack stackInstance;
    private LinkedHashMap<String, Object> stackHeader;
    private LinkedHashMap<String, Object> localHeader;
    private JSONObject urlQueries;
    private FetchAssetsCallback assetsCallback;
    private int count;


    /**
     * Sorting order enum for {@link AssetLibrary}.
     * @author Contentstack.com, Inc
     */

    public enum ORDERBY
    {
        ASCENDING,
        DESCENDING
    }

    protected AssetLibrary(){
        this.localHeader = new LinkedHashMap<String, Object>();
        this.urlQueries  = new JSONObject();
    }

    protected void setStackInstance(Stack stack){
        this.stackInstance = stack;
        this.stackHeader = stack.localHeader;
    }

    /**
     * To set headers for Contentstack rest calls.
     * <br>
     * Scope is limited to this object only.
     * @param key
     * header name.
     * @param value
     * header value against given header name.
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * //'blt5d4sample2633b' is a dummy Application API key
     * AssetLibrary assetLibObject = Contentstack.stack(context, "blt5d4sample2633b", "bltdtsample_accessToken767vv",  config).assetLibrary();
     * assetLibObject.setHeader("custom_header_key", "custom_header_value");
     * </pre>
     */

    public void setHeader(String key, String value) {
        if (!key.isEmpty() && !value.isEmpty()) {
            localHeader.put(key, value);
        }
    }


    /**
     * Remove a header for a given key from headers.
     * <br>
     * Scope is limited to this object only.
     * @param key header key.
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * //'blt5d4sample2633b' is a dummy Application API key
     * AssetLibrary assetLibObject = Contentstack.stack(context, "blt5d4sample2633b", "bltdtsample_accessToken767vv",  config).assetLibrary();
     *
     * assetLibObject.removeHeader("custom_header_key");
     * </pre>
     */
    public void removeHeader(String key){
        if(!key.isEmpty()){
            localHeader.remove(key);
        }
    }


    /**
     * Sort assets by fieldUid.
     * @param key
     * field Uid.
     * @param orderby
     * {@link ORDERBY} value for ascending or descending.
     * @return
     * {@link AssetLibrary} object, so you can chain this call.
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * //'blt5d4sample2633b' is a dummy Application API key
     * AssetLibrary assetLibObject = Contentstack.stack(context, "blt5d4sample2633b", "bltdtsample_accessToken767vv",  config).assetLibrary();
     * assetLibObject.sort("fieldUid", AssetLibrary.ORDERBY.ASCENDING);
     * </pre>
     *
     */
    public AssetLibrary sort(String key, ORDERBY orderby){
        try {
            switch (orderby){
                case ASCENDING:
                    urlQueries.put("asc",key);
                    break;

                case DESCENDING:
                    urlQueries.put("desc",key);
                    break;
            }
        }catch(Exception e) {
            throwException("sort", CSAppConstants.ErrorMessage_QueryFilterException, e);
        }

        return this;
    }

    /**
     * Retrieve count and data of assets in result.
     * @return {@link AssetLibrary} object, so you can chain this call.
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * //'blt5d4sample2633b' is a dummy Stack API key
     * //'bltdtsample_accessToken767vv' is dummy access token.
     * AssetLibrary assetLibObject = Contentstack.stack(context, "blt5d4sample2633b", "bltdtsample_accessToken767vv",  config).assetLibrary();
     * assetLibObject.includeCount();
     * </pre>
     */
    public AssetLibrary includeCount(){
        try {
            urlQueries.put("include_count","true");
        } catch (Exception e) {
            throwException("includeCount", CSAppConstants.ErrorMessage_QueryFilterException, e);
        }
        return this;
    }


    /**
     * Retrieve relative urls objects in result.
     * @return
     * {@link AssetLibrary} object, so you can chain this call.
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * //'blt5d4sample2633b' is a dummy Stack API key
     * //'bltdtsample_accessToken767vv' is dummy access token.
     * AssetLibrary assetLibObject = Contentstack.stack(context, "blt5d4sample2633b", "bltdtsample_accessToken767vv",  config).assetLibrary();
     * assetLibObject.includeRelativeUrl();
     * </pre>
     */
    public AssetLibrary includeRelativeUrl(){
        try {
            urlQueries.put("relative_urls","true");
        } catch (Exception e) {
            throwException("relative_urls", CSAppConstants.ErrorMessage_QueryFilterException, e);
        }
        return this;
    }

    /**
     * Get a count of assets in success callback of {@link FetchAssetsCallback}.
     * @return int @count
     */
    public int getCount(){
        return count;
    }



    /**
     * Fetch a all asset.
     * @param assetsCallback
     * {@link FetchAssetsCallback} instance for success and failure result.
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     *    //'blt5d4sample2633b' is a dummy Stack API key
     *    //'bltdtsample_accessToken767vv' is dummy access token.
     *    AssetLibrary assetLibObject = Contentstack.stack(context, "blt5d4sample2633b", "bltdtsample_accessToken767vv",  config).assetLibrary();
     *   assetLibObject.fetchAll(new FetchAssetsCallback() {
     *   {@code public void onCompletion(ResponseType responseType, List<Asset> assets, Error error) }{
     *      if (error == null) {
     *         //Success Block.
     *      } else {
     *         //Error Block.
     *      }
     *   }
     *  });
     *
     * </pre>
     *
     */
    public void fetchAll(FetchAssetsCallback assetsCallback){
        try {
            this.assetsCallback = assetsCallback;
            String URL = "/" + stackInstance.VERSION + "/assets";
            LinkedHashMap<String, Object> headers = getHeader(localHeader);
            if (headers.containsKey("environment")) {
                urlQueries.put("environment", headers.get("environment"));
            }

            fetchFromNetwork(URL, urlQueries, headers,  assetsCallback);

        } catch (Exception e){
            e.printStackTrace();
        }

    }



    private void fetchFromNetwork(String URL, JSONObject urlQueries, LinkedHashMap<String, Object> headers, FetchAssetsCallback assetsCallback) {
        if(assetsCallback != null) {
            HashMap<String, Object> urlParams = getUrlParams(urlQueries);
            new CSBackgroundTask(this, stackInstance, CSController.FETCHALLASSETS, URL, headers, urlParams, new JSONObject(), CSAppConstants.callController.ASSETLIBRARY.toString(), false, CSAppConstants.RequestMethod.GET, assetsCallback);
        }
    }




    /**
     * @param urlQueriesJSON takes {@link JSONObject} object as argeument
     * @return
     */

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
                    e.printStackTrace();
                }
            }

            return hashMap;
        }

        return null;
    }


    /**
     * @param tag String to which  Class Context belong
     * @param messageString takes as Message
     * @param e Exception
     */
    private void throwException(String tag, String messageString, Exception e){
        Error error = new Error();
        error.setErrorMessage(messageString);
    }

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

    @Override
    public void getResult(Object object, String controller) {}

    @Override
    public void getResultObject(List<Object> objects, JSONObject jsonObject, boolean isSingleEntry) {

        if(jsonObject != null && jsonObject.has("count")){
            count = jsonObject.optInt("count");
        }

        List<Asset> assets = new ArrayList<Asset>();

        if(objects != null && objects.size() > 0){
            for (Object object : objects) {
                AssetModel model = (AssetModel) object;
                Asset asset      = stackInstance.asset();

                asset.contentType  = model.contentType;
                asset.fileSize     = model.fileSize;
                asset.uploadUrl    = model.uploadUrl;
                asset.fileName     = model.fileName;
                asset.json         = model.json;
                asset.assetUid = model.uploadedUid;
                asset.setTags(model.tags);
                model = null;

                assets.add(asset);
            }
        }

        if(assetsCallback != null) {
            assetsCallback.onRequestFinish(ResponseType.NETWORK, assets);
        }
    }

}
