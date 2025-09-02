package com.contentstack.sdk;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class EntriesModel {

    protected JSONObject jsonObject;
    protected List<Object> objectList;

    protected EntriesModel(JSONObject responseJSON) {
        try {
            this.jsonObject = responseJSON;
            objectList = new ArrayList<>();
            Object entryList = jsonObject.opt("entries");
            if (entryList instanceof ArrayList) {
                ArrayList<LinkedHashMap> entries = (ArrayList) entryList;
                if (!entries.isEmpty()) {
                    entries.forEach(model -> {
                        if (model instanceof LinkedHashMap) {
                            // Convert LinkedHashMap to JSONObject
                            JSONObject jsonModel = new JSONObject((LinkedHashMap<?, ?>) model);
                            EntryModel entry = new EntryModel(jsonModel);
                            objectList.add(entry);
                        }
                    });
                }
            }
        } catch (Exception e) {
            Logger logger = Logger.getLogger(EntriesModel.class.getSimpleName());
            logger.log(Level.SEVERE, ErrorMessages.ENTRIES_PROCESSING_FAILED, e);
        }

    }
}
