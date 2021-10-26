package com.contentstack.sdk;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class AssetsModel {

    List<Object> objects = new ArrayList<>();

    public AssetsModel(JSONObject response) {
        JSONArray listResponse = response != null && response.has("assets") ? response.optJSONArray("assets") : null;
        if (listResponse != null) {
            listResponse.forEach(model -> {
                JSONObject modelObj = (JSONObject) model;
                AssetModel newModel = new AssetModel(modelObj, true);
                objects.add(newModel);
            });
        }
    }
}
