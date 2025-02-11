package com.contentstack.sdk;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * The type Assets model.
 */
class AssetsModel {

    List<Object> objects = new ArrayList<>();

    /**
     * Instantiates a new Assets model.
     *
     * @param response the response
     */
    public AssetsModel(JSONObject response) {
        Object listResponse = response != null && response.has("assets") ? response.opt("assets") : null;
        if (listResponse instanceof JSONArray) {
            // Handle traditional JSONArray
            populateObjectsFromJSONArray((JSONArray) listResponse);
        } else if (listResponse instanceof List) {
            // Convert ArrayList to JSONArray
            JSONArray jsonArray = new JSONArray((List<?>) listResponse);
            populateObjectsFromJSONArray(jsonArray);
        }
    }

    private void populateObjectsFromJSONArray(JSONArray jsonArray) {
        jsonArray.forEach(model -> {
            if (model instanceof JSONObject) {
                JSONObject modelObj = (JSONObject) model;
                AssetModel newModel = new AssetModel(modelObj, true);
                objects.add(newModel);
            }
        });
    }
}
