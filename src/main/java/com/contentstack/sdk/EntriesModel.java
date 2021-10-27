package com.contentstack.sdk;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

class EntriesModel {

    protected JSONObject jsonObject;
    protected List<Object> objectList;

    protected EntriesModel(JSONObject responseJSON) {
        try {
            this.jsonObject = responseJSON;
            objectList = new ArrayList<>();
            JSONArray entriesArray = jsonObject.opt("entries") == null ? null : jsonObject.optJSONArray("entries");
            assert entriesArray != null;
            entriesArray.forEach(model -> {
                if (model instanceof JSONObject) {
                    JSONObject newModel = (JSONObject) model;
                    EntryModel entry = new EntryModel(newModel);
                    objectList.add(entry);
                }
            });
        } catch (Exception e) {
            Logger logger = Logger.getLogger(EntriesModel.class.getSimpleName());
            logger.severe(e.getLocalizedMessage());
        }

    }
}
