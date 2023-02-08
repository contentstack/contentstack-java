package com.contentstack.sdk;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

class EntryModel {

    private static final String PUBLISH_DETAIL_KEY = "publish_details";
    private static final String ENTRY_KEY = "entry";
    private static final String TITLE_KEY = "title";
    private static final String LOCALE_KEY = "locale";
    private static final String UID_KEY = "uid";
    private static final String URL_KEY = "url";

    protected JSONObject publishDetails;
    protected JSONObject jsonObject;
    protected String title = null;
    protected String uid = null;
    protected String url = null;
    protected String updatedAt;
    protected String updatedBy;
    protected String createdAt;
    protected String createdBy;
    protected Boolean isDirectory;
    protected String[] tags = null;
    protected Object description = null;
    protected String environment = null;
    protected JSONArray images;
    protected String locale;
    protected String time = null;
    protected String user = null;
    protected int version = 1;
    protected Boolean inProgress;
    protected String language = null;
    protected String rteContent = null;
    protected Map<String, Object> metadata = null;

    public EntryModel(JSONObject response) {
        this.jsonObject = response;
        if (this.jsonObject.has(ENTRY_KEY)) {
            this.jsonObject = jsonObject.optJSONObject(ENTRY_KEY);
        }

        if (this.jsonObject.has(UID_KEY)) {
            this.uid = (String) this.jsonObject.opt(UID_KEY);
        }
        if (this.jsonObject.has(TITLE_KEY)) {
            this.title = (String) this.jsonObject.opt(TITLE_KEY);
        }
        if (this.jsonObject.has(LOCALE_KEY)) {
            this.language = (String) this.jsonObject.opt(LOCALE_KEY);
        }
        if (this.jsonObject.has(URL_KEY)) {
            this.url = (String) this.jsonObject.opt(URL_KEY);
        }
        if (this.jsonObject.has("description")) {
            this.description = this.jsonObject.opt("description");
        }

        this.images = (JSONArray) this.jsonObject.opt("images");
        this.isDirectory = (Boolean) this.jsonObject.opt("is_dir");
        this.updatedAt = (String) this.jsonObject.opt("updated_at");
        this.updatedBy = (String) this.jsonObject.opt("updated_by");
        this.createdAt = (String) this.jsonObject.opt("created_at");
        this.createdBy = (String) this.jsonObject.opt("created_by");
        this.locale = (String) this.jsonObject.opt(LOCALE_KEY);
        this.inProgress = (Boolean) this.jsonObject.opt("_in_progress");
        this.version = this.jsonObject.opt("_version") != null ? (int) this.jsonObject.opt("_version") : 1;
        if (this.jsonObject.has(PUBLISH_DETAIL_KEY)) {
            parsePublishDetail();
        }

    }

    private void parsePublishDetail() {
        if (this.jsonObject.opt(PUBLISH_DETAIL_KEY) instanceof JSONObject) {
            this.publishDetails = (JSONObject) this.jsonObject.opt(PUBLISH_DETAIL_KEY);
            this.environment = this.publishDetails.optString("environment");
            this.time = this.publishDetails.optString("time");
            this.user = this.publishDetails.optString("user");
        }
        this.metadata = new HashMap<>();
        this.metadata.put(PUBLISH_DETAIL_KEY, this.publishDetails);
    }
}
