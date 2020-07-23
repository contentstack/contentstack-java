package com.contentstack.sdk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

class EntriesModel {

    protected JSONObject jsonObject;
    protected String formName;
    protected List<Object> objectList;
    private Logger logger = LogManager.getLogger(EntriesModel.class.getName());
    protected EntriesModel(JSONObject responseJSON, String formName, boolean isFromCache) {

        try {
            if (isFromCache){
                this.jsonObject = (responseJSON.opt("response") == null ? null : responseJSON.optJSONObject("response"));
            }else{
                this.jsonObject = responseJSON;
            }

            this.formName   = formName;
            objectList      = new ArrayList<Object>();
            JSONArray entriesArray =  jsonObject.opt("entries") == null ? null : jsonObject.optJSONArray("entries");

            if(entriesArray != null && entriesArray.length() > 0){
                int count = entriesArray.length();
                for(int i = 0; i < count; i++){
                    if(entriesArray.opt(i) != null && entriesArray.opt(i) instanceof JSONObject) {
                        EntryModel entry = new EntryModel(entriesArray.optJSONObject(i), null, true, isFromCache, false);
                        objectList.add(entry);
                    }
                }
            }
        }catch (Exception localException){
            logger.debug(localException.getLocalizedMessage());
        }

    }
}
