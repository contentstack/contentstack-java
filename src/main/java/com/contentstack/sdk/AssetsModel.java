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
        JSONArray listResponse = null;
        Object rawAssets = response.get("assets"); // Get assets
        if (rawAssets instanceof List) {  // Check if it's an ArrayList
            List<?> assetsList = (List<?>) rawAssets;
            listResponse = new JSONArray(assetsList); // Convert to JSONArray
        }
        if (listResponse != null) {
            listResponse.forEach(model -> {
                JSONObject modelObj = (JSONObject) model;
                AssetModel newModel = new AssetModel(modelObj, true);
                objects.add(newModel);
            }
        });
    }
}
