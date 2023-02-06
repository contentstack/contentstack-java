package com.contentstack.sdk;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * The ContentTypesModel that contains content type response
 */
public class ContentTypesModel {

    private Object response;
    private JSONArray responseJSONArray = new JSONArray();

    public void setJSON(JSONObject responseJSON) {

        if (responseJSON != null) {
            String ctKey = "content_type";
            if (responseJSON.has(ctKey) && responseJSON.opt(ctKey) instanceof JSONObject) {
                this.response = responseJSON.optJSONObject(ctKey);
            }
            String ctListKey = "content_types";
            if (responseJSON.has(ctListKey) && responseJSON.opt(ctListKey) instanceof JSONArray) {
                this.response = responseJSON.optJSONArray(ctListKey);
                this.responseJSONArray = (JSONArray) this.response;
            }
        }
    }

    public Object getResponse() {
        return this.response;
    }

    public JSONArray getResultArray() {
        return responseJSONArray;
    }
}
