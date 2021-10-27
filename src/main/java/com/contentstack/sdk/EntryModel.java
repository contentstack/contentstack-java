package com.contentstack.sdk;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

class EntryModel {

    private static final String publishDetailKey = "publish_details";
    private static final String entryKey = "entry";
    private static final String titleKey = "title";
    private static final String localeKey = "locale";
    private static final String uidKey = "uid";
    private static final String urlKey = "url";

    protected JSONObject publishDetails;
    protected JSONObject jsonObject;
    protected String title = null;
    protected String uid = null;
    protected String url = null;
    protected String updatedAt = null;
    protected String updatedBy = null;
    protected String createdAt = null;
    protected String createdBy = null;
    protected Boolean isDirectory = false;
    protected String[] tags = null;
    protected String description = null;
    protected String environment = null;
    protected JSONArray images = null;
    protected String locale = null;
    protected String time = null;
    protected String user = null;
    protected int version = 1;
    protected Boolean inProgress = null;
    protected String language = null;
    protected String rteContent = null;
    protected Map<String, Object> metadata = null;

    public EntryModel(JSONObject response) {
        this.jsonObject = response;
        if (this.jsonObject.has(entryKey)) {
            this.jsonObject = jsonObject.optJSONObject(entryKey);
        }

        if (this.jsonObject.has(uidKey)) {
            this.uid = (String) this.jsonObject.opt(uidKey);
        }
        if (this.jsonObject.has(titleKey)) {
            this.title = (String) this.jsonObject.opt(titleKey);
        }
        if (this.jsonObject.has(localeKey)) {
            this.language = (String) this.jsonObject.opt(localeKey);
        }
        if (this.jsonObject.has(urlKey)) {
            this.url = (String) this.jsonObject.opt(urlKey);
        }
        if (this.jsonObject.has("description")) {
            this.description = (String) this.jsonObject.opt("description");
        }

        this.images = (JSONArray) this.jsonObject.opt("images");
        this.isDirectory = (Boolean) this.jsonObject.opt("is_dir");
        this.updatedAt = (String) this.jsonObject.opt("updated_at");
        this.updatedBy = (String) this.jsonObject.opt("updated_by");
        this.createdAt = (String) this.jsonObject.opt("created_at");
        this.createdBy = (String) this.jsonObject.opt("created_by");
        this.locale = (String) this.jsonObject.opt(localeKey);
        this.inProgress = (Boolean) this.jsonObject.opt("_in_progress");
        this.version = (int) this.jsonObject.opt("_version");

        if (this.jsonObject.has(publishDetailKey)) {
            parsePublishDetail();
        }


    }

    private void parsePublishDetail() {
        if (this.jsonObject.opt(publishDetailKey) instanceof JSONObject) {
            this.publishDetails = (JSONObject) this.jsonObject.opt(publishDetailKey);
            this.environment = this.publishDetails.optString("environment");
            this.time = this.publishDetails.optString("time");
            this.user = this.publishDetails.optString("user");
        }
        this.metadata = new HashMap<>();
        this.metadata.put(publishDetailKey, this.publishDetails);
    }
}
