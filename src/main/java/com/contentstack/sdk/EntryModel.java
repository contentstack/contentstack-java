package com.contentstack.sdk;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;


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
            this.uid = this.jsonObject.optString(UID_KEY, null);
        }
        if (this.jsonObject.has(TITLE_KEY)) {
            this.title = this.jsonObject.optString(TITLE_KEY, null);
        }
        if (this.jsonObject.has(LOCALE_KEY)) {
            this.language = this.jsonObject.optString(LOCALE_KEY,null);
        }
        if (this.jsonObject.has(URL_KEY)) {
            this.url = this.jsonObject.optString(URL_KEY,null);
        }
        if (this.jsonObject.has("description")) {
            this.description = this.jsonObject.optString("description");
        }
        if(this.jsonObject.has("images") && this.jsonObject.opt("images") instanceof JSONArray) {
            this.images = this.jsonObject.optJSONArray("images");
        }
        if(this.jsonObject.has("is_dir") && this.jsonObject.opt("is_dir") instanceof Boolean) {
            this.isDirectory = this.jsonObject.optBoolean("is_dir");
        }
        if(this.jsonObject.has("updated_at")) {
            this.updatedAt = this.jsonObject.optString("updated_at");
        }
        if(this.jsonObject.has("updated_by")) {
            this.updatedBy = this.jsonObject.optString("updated_by");
        }
        if(this.jsonObject.has("created_at")) {
            this.createdAt = this.jsonObject.optString("created_at");
        }
        if(this.jsonObject.has("created_by")) {
            this.createdBy = this.jsonObject.optString("created_by");
        }
        if(this.jsonObject.has(LOCALE_KEY)) {
            this.locale = this.jsonObject.optString(LOCALE_KEY);
        }
        if(this.jsonObject.has("_in_progress") && this.jsonObject.opt("_in_progress") instanceof Boolean) {
            this.inProgress = this.jsonObject.optBoolean("_in_progress");
        }
        if(this.jsonObject.has("_version") && this.jsonObject.opt("_version") instanceof Integer) {
            this.version = this.jsonObject.optInt("_version",1);
        }   
        if (this.jsonObject.has(PUBLISH_DETAIL_KEY)) {
            parsePublishDetail();
        }

    }

    private void parsePublishDetail() {
        if (this.jsonObject.opt(PUBLISH_DETAIL_KEY) instanceof JSONObject) {
            this.publishDetails = this.jsonObject.optJSONObject(PUBLISH_DETAIL_KEY);
            if(this.publishDetails != null) {
                this.environment = this.publishDetails.optString("environment");
                this.time = this.publishDetails.optString("time");
                this.user = this.publishDetails.optString("user");
            }
        }
        this.metadata = new HashMap<>();
        this.metadata.put(PUBLISH_DETAIL_KEY, this.publishDetails);
    }
}

