package com.contentstack.sdk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Logger;

public class ContentTypesModel {

    private static final Logger logger = Logger.getLogger(ContentTypesModel.class.getSimpleName());
    private JSONObject responseJSON = new JSONObject();
    private JSONArray responseJSONArray = new JSONArray();

    public void setJSON(JSONObject responseJSON) {

        if (responseJSON != null) {
            if (responseJSON.has("content_type")) {
                try {
                    this.responseJSON = responseJSON.getJSONObject("content_type");
                } catch (JSONException e) {
                    logger.severe(e.getLocalizedMessage());
                }
            }

            if (responseJSON.has("content_types")) {
                try {
                    this.responseJSONArray = responseJSON.getJSONArray("content_types");
                } catch (JSONException e) {
                    logger.severe(e.getLocalizedMessage());
                }
            }

        }
    }

    public JSONObject getResponse() {
        return responseJSON;
    }

    public JSONArray getResultArray() {
        return responseJSONArray;
    }
}
