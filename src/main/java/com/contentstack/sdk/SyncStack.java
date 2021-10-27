package com.contentstack.sdk;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Synchronization: The Sync API takes care of syncing your Contentstack data
 * with your app and ensures that the data is always up-to-date by providing
 * delta updates
 */

public class SyncStack {

    private JSONObject receiveJson;
    private int skip;
    private int limit;
    private int count;
    private String url;
    private String paginationToken;
    private String syncToken;
    private ArrayList<JSONObject> syncItems;

    public String getUrl() {
        return this.url;
    }

    public JSONObject getJSONResponse() {
        return this.receiveJson;
    }

    public int getCount() {
        return this.count;
    }

    public int getLimit() {
        return this.limit;
    }

    public int getSkip() {
        return this.skip;
    }

    public String getPaginationToken() {
        return this.paginationToken;
    }

    public String getSyncToken() {
        return this.syncToken;
    }

    public List<JSONObject> getItems() {
        return this.syncItems;
    }

    protected void setJSON(@NotNull JSONObject jsonobject) {
        this.receiveJson = jsonobject;

        try {

            if (receiveJson.has("items")) {
                JSONArray jsonarray = receiveJson.getJSONArray("items");
                if (jsonarray != null) {
                    syncItems = new ArrayList<>();
                    for (int position = 0; position < jsonarray.length(); position++) {
                        syncItems.add(jsonarray.optJSONObject(position));
                    }
                }
            }

            if (receiveJson.has("skip")) {
                this.skip = receiveJson.optInt("skip");
            }
            if (receiveJson.has("total_count")) {
                this.count = receiveJson.optInt("total_count");
            }
            if (receiveJson.has("limit")) {
                this.limit = receiveJson.optInt("limit");
            }
            if (receiveJson.has("pagination_token")) {
                this.paginationToken = receiveJson.optString("pagination_token");
            } else {
                this.paginationToken = null;
            }
            if (receiveJson.has("sync_token")) {
                this.syncToken = receiveJson.optString("sync_token");
            } else {
                this.syncToken = null;
            }

        } catch (Exception e) {
            Logger logger = Logger.getLogger(SyncStack.class.getSimpleName());
            logger.severe(e.getLocalizedMessage());
        }

    }

}
