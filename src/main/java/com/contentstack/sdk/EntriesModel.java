package com.contentstack.sdk;


import com.contentstack.sdk.utility.CSAppUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
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

class EntriesModel {

    protected JSONObject jsonObject;
    protected String formName;
    protected List<Object> objectList;
    protected EntriesModel(JSONObject responseJSON, String formName, boolean isFromCache) {

        try {
            if (isFromCache){
                this.jsonObject = (responseJSON.opt("response") == null ? null : responseJSON.optJSONObject("response"));
            }else{
                this.jsonObject = responseJSON;
            }

            this.formName   = formName;
            objectList      = new ArrayList<Object>();
            JSONArray entriesArray =  jsonObject.opt("entries") == null ? null : jsonObject.optJSONArray("entries");

            if(entriesArray != null && entriesArray.length() > 0){
                int count = entriesArray.length();
                for(int i = 0; i < count; i++){
                    if(entriesArray.opt(i) != null && entriesArray.opt(i) instanceof JSONObject) {
                        EntryModel entry = new EntryModel(entriesArray.optJSONObject(i), null, true, isFromCache, false);
                        objectList.add(entry);
                    }
                }
            }
        }catch (Exception localException){
            Stack.log("EntriesModel", localException.getLocalizedMessage());
        }

    }
}
