package com.contentstack.sdk;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
            if (responseJSON.has(ctKey) && responseJSON.opt(ctKey) instanceof LinkedHashMap) {
                this.response = new JSONObject((LinkedHashMap<?, ?>) responseJSON.get(ctKey));
            }
            String ctListKey = "content_types";
            if (responseJSON.has(ctListKey) && responseJSON.opt(ctListKey) instanceof ArrayList) {
                ArrayList<LinkedHashMap<?, ?>> contentTypes = (ArrayList) responseJSON.get(ctListKey);
                List<Object> objectList = new ArrayList<>();
                if (!contentTypes.isEmpty()) {
                    contentTypes.forEach(model -> {
                        if (model instanceof LinkedHashMap) {
                            // Convert LinkedHashMap to JSONObject
                            JSONObject jsonModel = new JSONObject((LinkedHashMap<?, ?>) model);
                            objectList.add(jsonModel);
                        }
                    });
                }
                this.response = new JSONArray(objectList);
                this.responseJSONArray = new JSONArray(objectList);
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
