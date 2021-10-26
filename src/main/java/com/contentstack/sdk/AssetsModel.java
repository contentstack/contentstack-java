package com.contentstack.sdk;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class AssetsModel {

    List<Object> objects = new ArrayList<>();

    public AssetsModel(JSONObject jsonObject, boolean isFromCache) {
        jsonObject = !isFromCache && jsonObject.opt("response") == null ? jsonObject : jsonObject.optJSONObject("response");
        JSONArray jsonArray = jsonObject != null && jsonObject.has("assets") ? jsonObject.optJSONArray("assets") : null;
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                AssetModel model = new AssetModel(jsonArray.optJSONObject(i), true);
                objects.add(model);
                model = null;
            }
        }
    }
}
