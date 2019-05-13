package com.contentstack.sdk;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class ContentTypesModel {

    private JSONObject responseJSON = new JSONObject();
    private JSONArray responseJSONArray = new JSONArray();

    public void setJSON(JSONObject responseJSON) {

        if (responseJSON!=null){

            if (responseJSON.has("content_type")){
                try {
                    this.responseJSON = responseJSON.getJSONObject("content_type");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (responseJSON.has("content_types")){
                try {
                    this.responseJSONArray = responseJSON.getJSONArray("content_types");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public JSONObject getResponse() {
        return responseJSON;
    }

    public JSONArray getResultArray() {
        return responseJSONArray;
    }
}
