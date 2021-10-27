package com.contentstack.sdk;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

class EntryModel {

    protected JSONObject jsonObject;
    protected String entryUid = null;
    protected String title = null;
    protected String url = null;
    protected String[] tags = null;
    protected String language = null;
    protected String rteContent = null;
    protected Map<String, Object> metadata = null;

    public EntryModel(JSONObject response) {
        this.jsonObject = response;
        final String PUBLISH_DETAILS = "publish_details";
        final String TITLE = "title";
        final String LOCALE = "locale";
        final String UID = "uid";
        final String URL = "url";

        try {

            if (this.jsonObject.has(UID)) {
                this.entryUid = (String) this.jsonObject.opt(UID);
            }
            if (this.jsonObject.has(TITLE)) {
                this.title = (String) this.jsonObject.opt(TITLE);
            }
            if (this.jsonObject.has(LOCALE)) {
                this.language = (String) this.jsonObject.opt(LOCALE);
            }
            if (this.jsonObject.has(URL)) {
                this.url = (String) this.jsonObject.opt(URL);
            } else if (this.jsonObject.has(PUBLISH_DETAILS)) {
                JSONArray publishArray = this.jsonObject.optJSONArray(PUBLISH_DETAILS);
                publishArray.forEach(model -> {
                    JSONObject jsonModel = (JSONObject) model;
                    Iterator<String> iterator = jsonModel.keys();
                    HashMap<String, Object> hashMap = new HashMap<>();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        hashMap.put(key, this.jsonObject.opt(key));
                    }
                });
                if (metadata == null) {
                    metadata = new HashMap<>();
                    metadata.put(PUBLISH_DETAILS, publishArray);
                }
            }
        } catch (Exception e) {
            Logger logger = Logger.getLogger(EntryModel.class.getSimpleName());
            logger.severe(e.getLocalizedMessage());
        }

    }
}
