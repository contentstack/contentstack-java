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
                try {
                    this.response = new JSONObject((LinkedHashMap<?, ?>) responseJSON.get(ctKey));
                } catch (Exception e) {
                    System.err.println("Error processing 'content_type': " + e.getMessage());
                }
            }
            String ctListKey = "content_types";
            if (responseJSON.has(ctListKey) && responseJSON.opt(ctListKey) instanceof ArrayList) {
               try {
                ArrayList<LinkedHashMap<?, ?>> contentTypes = (ArrayList) responseJSON.get(ctListKey);
                List<Object> objectList = new ArrayList<>();
                if (!contentTypes.isEmpty()) {
                    contentTypes.forEach(model -> {
                        if (model instanceof LinkedHashMap) {
                            // Convert LinkedHashMap to JSONObject
                            JSONObject jsonModel = new JSONObject((LinkedHashMap<?, ?>) model);
                            objectList.add(jsonModel);
                        } else {
                            System.err.println("Invalid type in 'content_types' list. Expected LinkedHashMap.");
                        }
                    });
                }
                this.response = new JSONArray(objectList);
                this.responseJSONArray = new JSONArray(objectList);
            } catch (Exception e) {
            System.err.println("Error processing 'content_types': " + e.getMessage());
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
