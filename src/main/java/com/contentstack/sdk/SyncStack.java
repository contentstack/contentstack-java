package com.contentstack.sdk;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


/**
 * Synchronization: The Sync API takes care of syncing your Contentstack data
 * with your app and ensures that the data is always up-to-date by providing
 * delta updates
 *
 */
public class SyncStack {

    private static final Logger logger = Logger.getLogger(SyncStack.class.getName());
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

    protected synchronized void setJSON(@NotNull JSONObject jsonobject) {
        if (jsonobject == null) {
            throw new IllegalArgumentException(ErrorMessages.MISSING_JSON_OBJECT_SYNC);
        }
    
        this.receiveJson = jsonobject;
    
        if (receiveJson.has("items")) {
            Object itemsObj = receiveJson.opt("items");
            
            if (itemsObj instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) itemsObj;
                syncItems = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonItem = jsonArray.optJSONObject(i);
                    if (jsonItem != null) {
                        syncItems.add(sanitizeJson(jsonItem));
                    }
                }
            } else if (itemsObj instanceof JSONObject) {
                syncItems = new ArrayList<>();
                syncItems.add(sanitizeJson((JSONObject) itemsObj));
            } else if (itemsObj instanceof List) {
                List<?> itemsList = (List<?>) itemsObj;
                syncItems = new ArrayList<>();
                for (Object item : itemsList) {
                    if (item instanceof JSONObject) {
                        syncItems.add(sanitizeJson((JSONObject) item));
                    } else if (item instanceof Map) {
                        JSONObject jsonItem = new JSONObject((Map<?, ?>) item);
                        syncItems.add(sanitizeJson(jsonItem));
                    } else {
                        logger.warning("Item in ArrayList is not a JSONObject or LinkedHashMap. Skipping. Type: " + item.getClass().getName());
                    }
                }
            } else {
                logger.warning("'items' is not a valid JSONArray, JSONObject, or ArrayList. Type: " + 
                    (itemsObj != null ? itemsObj.getClass().getName() : "null"));
                syncItems = new ArrayList<>();
            }
        } else {
            syncItems = new ArrayList<>();
        }
    
        this.paginationToken = null;
        this.syncToken = null;
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
            this.paginationToken = validateToken(receiveJson.optString("pagination_token"));
        } else {
            this.paginationToken = null;
        }

        if (receiveJson.has("sync_token")) {
            this.syncToken = validateToken(receiveJson.optString("sync_token"));
        } else {
            this.syncToken = null;
        }
    }

     /**
     * ✅ Sanitize JSON to prevent JSON injection
     */
    private JSONObject sanitizeJson(JSONObject json) {
        JSONObject sanitizedJson = new JSONObject();
        for (String key : json.keySet()) {
            Object value = json.opt(key);
            if (value instanceof String) {
                // ✅ Remove potentially dangerous script tags
                String cleanValue = ((String) value)
                    .replaceAll("(?i)<script>", "&lt;script&gt;")  // Prevent script injection
                    .replaceAll("(?i)</script>", "&lt;/script&gt;"); // Prevent closing script tags
    
                sanitizedJson.put(key, cleanValue); // ✅ Store sanitized value
            } else {
                sanitizedJson.put(key, value); // ✅ Keep non-string values unchanged
            }
        }
        return sanitizedJson;
    }
    
    
    /**
     * ✅ Validate tokens to prevent security risks
     */
    private String validateToken(String token) {
        if (token != null && !token.matches("^[a-zA-Z0-9-_.]+$")) {
            logger.warning("Invalid token detected: ");
            return null;
        }
        return token;
    }

}
