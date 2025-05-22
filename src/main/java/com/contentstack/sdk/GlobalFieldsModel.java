package com.contentstack.sdk;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;



/**
 * The GlobalFieldsModel that contains global fields response
 */
public class GlobalFieldsModel {

    private Object response;
    private JSONArray responseJSONArray = new JSONArray();

    public void setJSON(JSONObject responseJSON) {
        if (responseJSON != null) {
            String ctKey = "global_field";
            if (responseJSON.has(ctKey) && responseJSON.opt(ctKey) instanceof LinkedHashMap) {
                try {
                    this.response = new JSONObject((LinkedHashMap<?, ?>) responseJSON.get(ctKey));
                } catch (Exception e) {
                    System.err.println("Error processing 'global_field': " + e.getMessage());
                }
            }
            String gfListKey = "global_fields";
            if (responseJSON.has(gfListKey) && responseJSON.opt(gfListKey) instanceof ArrayList) {
               try {
                ArrayList<LinkedHashMap<?, ?>> globalFields = (ArrayList) responseJSON.get(gfListKey);
                List<Object> objectList = new ArrayList<>();
                if (!globalFields.isEmpty()) {
                    globalFields.forEach(model -> {
                        if (model instanceof LinkedHashMap) {
                            // Convert LinkedHashMap to JSONObject
                            JSONObject jsonModel = new JSONObject((LinkedHashMap<?, ?>) model);
                            objectList.add(jsonModel);
                        } else {
                            System.err.println("Invalid type in 'global_fields' list. Expected LinkedHashMap.");
                        }
                    });
                }
                this.response = new JSONArray(objectList);
                this.responseJSONArray = new JSONArray(objectList);
            } catch (Exception e) {
            System.err.println("Error processing 'global_fields': " + e.getMessage());
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
