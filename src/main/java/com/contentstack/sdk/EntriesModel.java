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
            Object entryList = jsonObject.opt("entries");
            if (entryList instanceof JSONArray){
                JSONArray entries = (JSONArray) entryList;
                if (entries.length()>0){
                    entries.forEach(model -> {
                        if (model instanceof JSONObject) {
                            JSONObject newModel = (JSONObject) model;
                            EntryModel entry = new EntryModel(newModel);
                            objectList.add(entry);
                        }
                    });
                }
            }
        } catch (Exception e) {
            Logger logger = Logger.getLogger(EntriesModel.class.getSimpleName());
            logger.severe(e.getLocalizedMessage());
        }

    }
}
