package com.contentstack.sdk;

import java.util.LinkedHashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;


/**
 * The type Asset model.
 */
@Getter
@Setter
@NoArgsConstructor
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

    /**
     * Instantiates a new Asset model.
     *
     * @param response the response
     * @param isArray  the is array
     */
    public AssetModel(JSONObject response, boolean isArray) {
        if (isArray) {
            json = response;
        } else {
            json = new JSONObject((LinkedHashMap<?, ?>) response.get("asset"));
        }

        if (json != null) {
            uploadedUid = (String) json.opt("uid");
            contentType = (String) json.opt("content_type");
            fileSize = (String) json.opt("file_size");
            fileName = (String) json.opt("filename");
            uploadUrl = (String) json.opt("url");
            if (json.opt("tags") instanceof JSONArray) {
                extractTags();
            }
            if (response.has("count")) {
                count = response.optInt("count");
            }

            if (response.has("objects")) {
                totalCount = response.optInt("objects");
            }
        }
    }

    private void extractTags() {
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

}
