package com.contentstack.sdk;

import com.contentstack.sdk.utility.CSAppUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Contentstack.com
 *
 */
class EntryModel {


    protected JSONObject jsonObject            = null;
    protected String entryUid                  = null;
    protected String ownerEmailId 			   = null;
    protected String ownerUid 				   = null;
    protected String title                     = null;
    protected String url                       = null;
    protected String[] tags					   = null;
    protected Map<String, Object> ownerMap = null;
    protected Map<String, Object> _metadata= null;
    private JSONArray tagsArray = null;

    public EntryModel(JSONObject jsonObj, String entryUid, boolean isFromObjectsModel ,boolean isFromCache, boolean isFromDeltaResponse) {

        try{
            this.entryUid = entryUid;
            if(isFromObjectsModel){
                jsonObject = jsonObj;
                this.entryUid = (String) (jsonObject.isNull("uid") ? " " : jsonObject.opt("uid"));
            }else{

                if(isFromCache){
                    jsonObject = jsonObj.opt("response") == null ? null : jsonObj.optJSONObject("response");
                }else{
                    jsonObject = jsonObj;
                }

                if(isFromDeltaResponse){
                    this.entryUid = (String) (jsonObject != null && jsonObject.isNull("uid") ? " " : jsonObject.opt("uid"));
                }else{
                    jsonObject = jsonObject != null && jsonObject.opt("entry") == null ? null : jsonObject.optJSONObject("entry");
                }
            }
            if(jsonObject != null && jsonObject.has("uid")){
                this.entryUid = (String) (jsonObject.isNull("uid") ? " " : jsonObject.opt("uid"));
            }

            if(jsonObject != null && jsonObject.has("title")){
                this.title = (String) (jsonObject.isNull("title") ? " " : jsonObject.opt("title"));
            }

            if(jsonObject != null && jsonObject.has("url")){
                this.url = (String) (jsonObject.isNull("url") ? " " : jsonObject.opt("url"));
            }

            if(jsonObject != null && jsonObject.has("_metadata")){
                JSONObject _metadataJSON  = jsonObject.optJSONObject("_metadata");
                Iterator<String> iterator = _metadataJSON.keys();
                _metadata = new HashMap<>();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    if(key.equalsIgnoreCase("uid")){
                        this.entryUid = _metadataJSON.optString(key);
                    }
                    _metadata.put(key, _metadataJSON.optString(key));
                }
            }else if(jsonObject != null && jsonObject.has("publish_details")){

                JSONArray publishArray = jsonObject.optJSONArray("publish_details");

                if (publishArray!=null && publishArray.length()>0){

                    for (int i = 0; i < publishArray.length(); i++) {
                        JSONObject jsonObject = publishArray.optJSONObject(i);
                        Iterator<String> iterator = jsonObject.keys();
                        HashMap<String, Object> hashMap = new HashMap<>();
                        while (iterator.hasNext()) {
                            String key = iterator.next();
                            hashMap.put(key, jsonObject.opt(key));
                        }
                    }
                }



                if(_metadata == null) {
                    _metadata = new HashMap<>();
                    _metadata.put("publish_details", publishArray);
                }

            }


            if(jsonObject != null && jsonObject.has("_owner") && (jsonObject.opt("_owner") != null) && (! jsonObject.opt("_owner").toString().equalsIgnoreCase("null")) ){
                JSONObject ownerObject = jsonObject.optJSONObject("_owner");
                if(ownerObject.has("email")  && ownerObject.opt("email") != null){
                    ownerEmailId = (String) ownerObject.opt("email");
                }

                if(ownerObject.has("uid")  && ownerObject.opt("uid") != null){
                    ownerUid = ownerObject.opt("uid").toString();
                }
                JSONObject owner = jsonObject.optJSONObject("_owner");
                Iterator<String> iterator = owner.keys();
                ownerMap = new HashMap<>();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    ownerMap.put(key, owner.optString(key));
                }
            }



        }catch (Exception e) {
            e.printStackTrace();
            CSAppUtils.showLog("EntryModel", "err|" + e);
        }

    }
}
