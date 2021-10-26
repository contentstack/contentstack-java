package com.contentstack.sdk;

import org.json.JSONArray;
import org.json.JSONObject;


class AssetModel {

    String uploadedUid;
    String contentType;
    String fileSize;
    String fileName;
    String uploadUrl;
    String[] tags;
    JSONObject json;
    int count = 0;
    int totalCount = 0;

    public AssetModel(JSONObject responseJSON, boolean isArray) {

        if (isArray) {
            json = responseJSON;
        } else {
            json = responseJSON.optJSONObject("asset");
        }

        uploadedUid = (String) json.opt("uid");
        contentType = (String) json.opt("content_type");
        fileSize = (String) json.opt("file_size");
        fileName = (String) json.opt("filename");
        uploadUrl = (String) json.opt("url");

        if (json.opt("tags") instanceof JSONArray) {
            JSONArray tagArray = json.optJSONArray("tags");
            if (tagArray != null && !tagArray.isEmpty()) {
                JSONArray tagsArray = (JSONArray) json.opt("tags");
                if (tagsArray.length() > 0) {
                    int counter = tagsArray.length();
                    tags = new String[counter];
                    for (int i = 0; i < counter; i++) {
                        tags[i] = (String) tagsArray.opt(i);
                    }
                }
            }
        }

        if (responseJSON.has("count")) {
            count = responseJSON.optInt("count");
        }

        if (responseJSON.has("objects")) {
            totalCount = responseJSON.optInt("objects");
        }
    }

}
