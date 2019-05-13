package com.contentstack.sdk;

import com.contentstack.sdk.utility.CSAppConstants;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

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

public class SyncStack {

    private final String TAG = SyncStack.class.getSimpleName();
    private JSONObject receiveJson;
    private int skip;
    private int limit;
    private int count;
    private String URL;
    private String pagination_token;
    private String sync_token;
    private ArrayList<JSONObject> syncItems;


    public String getURL() { return this.URL; }

    public JSONObject getJSONResponse(){ return this.receiveJson; }

    public int getCount() {
        return this.count;
    }

    public int getLimit() {
        return this.limit;
    }

    public int getSkip() {
        return this.skip;
    }

    public String getPaginationToken(){ return this.pagination_token; }

    public String getSyncToken(){
        return  this.sync_token;
    }

    public ArrayList<JSONObject> getItems() { return this.syncItems; }

    protected void setJSON(JSONObject jsonobject) {

        if (jsonobject != null){
            receiveJson = jsonobject;
            try{
                if(receiveJson != null){

                    URL = CSAppConstants.REQUEST_URL;

                    if(receiveJson.has("items")) {
                        JSONArray jsonarray = receiveJson.getJSONArray("items");
                        if (jsonarray != null) {
                            syncItems = new ArrayList<>();
                            for (int position = 0; position < jsonarray.length(); position++){
                                syncItems.add(jsonarray.optJSONObject(position));
                            }
                        }
                    }

                    if(receiveJson.has("skip")){
                        this.skip  = receiveJson.optInt("skip");
                    }
                    if(receiveJson.has("total_count")){
                        this.count = receiveJson.optInt("total_count");
                    }
                    if(receiveJson.has("limit")){
                        this.limit = receiveJson.optInt("limit");
                    }
                    if (receiveJson.has("pagination_token")){
                        this.pagination_token = receiveJson.optString("pagination_token");
                    }else {
                        this.sync_token = null;
                    }
                    if (receiveJson.has("sync_token")){
                        this.sync_token = receiveJson.optString("sync_token");
                    }else {
                        this.sync_token = null;
                    }
                }
            }catch(Exception e){
                Stack.log(TAG,"QueryResult--setJSON--"+e.toString());
            }

        }
    }


}
