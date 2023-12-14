package com.contentstack.sdk;

import okhttp3.ResponseBody;
import org.json.JSONObject;

public interface TaxonomyCallback {

    void onResponse(JSONObject response, Error error);

}