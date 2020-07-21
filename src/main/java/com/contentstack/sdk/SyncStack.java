package com.contentstack.sdk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Synchronization:
 * The Sync API takes care of syncing your Contentstack data with your app and
 * ensures that the data is always up-to-date by providing delta updates
 */

public class SyncStack {

    private final Logger logger = LogManager.getLogger(SyncStack.class.getName());
    public static final String REQUEST_URL = "";
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

                    URL = REQUEST_URL;

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
                        this.pagination_token = null;
                    }
                    if (receiveJson.has("sync_token")){
                        this.sync_token = receiveJson.optString("sync_token");
                    }else {
                        this.sync_token = null;
                    }
                }
            }catch(Exception e){
                logger.debug("QueryResult--setJSON--"+e.toString());
            }

        }
    }


}
