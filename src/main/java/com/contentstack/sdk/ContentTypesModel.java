package com.contentstack.sdk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Logger;

public class ContentTypesModel {

    private final Logger logger = Logger.getLogger(ContentTypesModel.class.getSimpleName());
    private Object response;
    private JSONArray responseJSONArray = new JSONArray();

    public void setJSON(JSONObject responseJSON) {

        if (responseJSON != null) {
            if (responseJSON.has("content_type")) {
                try {
                    this.response = (JSONObject) responseJSON.getJSONObject("content_type");
                } catch (JSONException e) {
                    logger.severe(e.getLocalizedMessage());
                }
            }

            if (responseJSON.has("content_types")) {
                try {
                    this.responseJSONArray = responseJSON.getJSONArray("content_types");
                    this.response = (JSONArray) responseJSON.getJSONArray("content_types");
                } catch (JSONException e) {
                    logger.severe(e.getLocalizedMessage());
                }
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
