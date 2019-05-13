package com.contentstack.sdk;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Iterator;
import java.util.WeakHashMap;


/**
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

class AssetModel {

    String uploadedUid;
    String contentType;
    String fileSize;
    String fileName;
    String uploadUrl;
    String[] tags;
    JSONObject json;
    private int totalCount = 0;
    int count = 0;

    public AssetModel(JSONObject responseJSON, boolean isArray, boolean isFromCache) {

        if(isFromCache){
            json = responseJSON.opt("response") == null ? responseJSON : responseJSON.optJSONObject("response");
        }else{
            json = responseJSON;
        }

        if(isArray){
            json = responseJSON;
        }else{
            json = responseJSON.optJSONObject("asset");
        }

        uploadedUid = (String) json.opt("uid");
        contentType = (String) json.opt("content_type");
        fileSize    = (String) json.opt("file_size");
        fileName    = (String) json.opt("filename");
        uploadUrl   = (String) json.opt("url");

        if(json.opt("tags") instanceof JSONArray){
            if((json.has("tags")) && (json.opt("tags") != null) && (! (json.opt("tags").equals("")))){

                JSONArray tagsArray =  (JSONArray) json.opt("tags");
                if(tagsArray.length() > 0){
                    int count = tagsArray.length();
                    tags = new String[count];
                    for(int i = 0; i < count; i++){
                        tags[i] = (String) tagsArray.opt(i);
                    }
                }
            }
        }

        if(json != null && json.has("_metadata")){
            JSONObject _metadataJSON  = json.optJSONObject("_metadata");
            Iterator<String> iterator = _metadataJSON.keys();
            WeakHashMap<String, Object> _metadata = new WeakHashMap<String, Object>();
            while (iterator.hasNext()) {
                String key = iterator.next();
                _metadata.put(key, _metadataJSON.optString(key));
            }
        }

        if(responseJSON.has("count")){
            count = responseJSON.optInt("count");
        }

        if(responseJSON.has("objects")){
            totalCount = responseJSON.optInt("objects");
        }
    }

}
