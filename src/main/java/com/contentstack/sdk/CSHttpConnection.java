package com.contentstack.sdk;

import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import static com.contentstack.sdk.Constants.*;

public class CSHttpConnection implements IURLRequestHTTP {

    protected static final Logger logger = Logger.getLogger(CSHttpConnection.class.getName());
    private final String urlPath;
    private final IRequestModelHTTP connectionRequest;
    private String controller;
    private LinkedHashMap<String, Object> headers;
    private String info;
    private APIService service;
    private Config config;
    private ResultCallBack callBackObject;
    private JSONObject responseJSON;
    private HashMap<String, Object> formParams;
    private final String utfType = String.valueOf(StandardCharsets.UTF_8);

    public CSHttpConnection(String urlToCall, IRequestModelHTTP csConnectionRequest) {
        this.urlPath = urlToCall;
        this.connectionRequest = csConnectionRequest;
    }

    public void setFormParams(HashMap<String, Object> formParams) {
        this.formParams = formParams;
    }

    @Override
    public String getController() {
        return controller;
    }

    @Override
    public void setController(String controller) {
        this.controller = controller;
    }

    @Override
    public void setHeaders(LinkedHashMap<String, Object> headers) {
        this.headers = headers;
    }

    @Override
    public LinkedHashMap<String, Object> getHeaders() {
        return this.headers;
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public ResultCallBack getCallBackObject() {
        return callBackObject;
    }

    @Override
    public void setCallBackObject(ResultCallBack callBackObject) {
        this.callBackObject = callBackObject;
    }

    @Override
    public JSONObject getResponse() {
        return responseJSON;
    }

    public String setFormParamsGET(HashMap<String, Object> params) {
        if (params != null && params.size() > 0) {
            String urlParams = null;
            urlParams = info.equalsIgnoreCase(Constants.REQUEST_CONTROLLER.QUERY.name())
                    || info.equalsIgnoreCase(Constants.REQUEST_CONTROLLER.ENTRY.name()) ? getParams(params) : null;
            if (urlParams == null) {
                for (Map.Entry<String, Object> e : params.entrySet()) {
                    if (urlParams == null) {
                        urlParams = "?" + e.getKey() + "=" + e.getValue();
                    } else {
                        urlParams += "&" + e.getKey() + "=" + e.getValue();
                    }
                }
            }
            return urlParams;
        }
        return null;
    }

    private String getParams(HashMap<String, Object> params) {
        String urlParams = "?";
        for (Map.Entry<String, Object> e : params.entrySet()) {
            String key = e.getKey();
            Object value = e.getValue();
            try {
                if (key.equalsIgnoreCase("include[]") || key.equalsIgnoreCase("only[BASE][]")
                        || key.equalsIgnoreCase("except[BASE][]")) {
                    urlParams = convertUrlParam(urlParams, value, key);
                } else if (key.equalsIgnoreCase("only")) {
                    JSONObject onlyJSON = (JSONObject) value;
                    Iterator<String> itrString = onlyJSON.keys();
                    while (itrString.hasNext()) {
                        String innerKey = itrString.next();
                        JSONArray array = onlyJSON.optJSONArray(innerKey);
                        innerKey = URLEncoder.encode("only[" + innerKey + "][]", utfType);
                        for (int i = 0; i < array.length(); i++) {
                            urlParams += urlParams.equals("?") ? innerKey + "=" + array.opt(i)
                                    : "&" + innerKey + "=" + array.opt(i);
                        }
                    }
                } else if (key.equalsIgnoreCase("except")) {
                    JSONObject onlyJSON = (JSONObject) value;
                    Iterator<String> iter = onlyJSON.keys();
                    while (iter.hasNext()) {
                        String innerKey = iter.next();
                        JSONArray array = onlyJSON.optJSONArray(innerKey);
                        innerKey = URLEncoder.encode("except[" + innerKey + "][]", utfType);
                        for (int i = 0; i < array.length(); i++) {
                            urlParams += urlParams.equals("?") ? innerKey + "=" + array.opt(i)
                                    : "&" + innerKey + "=" + array.opt(i);
                        }
                    }
                } else if (key.equalsIgnoreCase("query")) {
                    JSONObject queryJSON = (JSONObject) value;
                    urlParams += urlParams.equals("?") ? key + "=" + URLEncoder.encode(queryJSON.toString(), utfType)
                            : "&" + key + "=" + URLEncoder.encode(queryJSON.toString(), utfType);
                } else {
                    urlParams += urlParams.equals("?") ? key + "=" + value : "&" + key + "=" + value;
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return urlParams;
    }

    private String convertUrlParam(String urlParams, Object value, String key) throws UnsupportedEncodingException {
        key = URLEncoder.encode(key, utfType);
        JSONArray array = (JSONArray) value;
        for (int i = 0; i < array.length(); i++) {
            urlParams += urlParams.equals("?") ? key + "=" + array.opt(i) : "&" + key + "=" + array.opt(i);
        }
        return urlParams;
    }

    @Override
    public void send() {

        String url = "";
        String params = setFormParamsGET(formParams);
        if (params != null) {
            url = urlPath + params;
        } else {
            url = urlPath;
        }

        try {
            getService(url);
        } catch (IOException | JSONException e) {
            logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    private void getService(String requestUrl) throws IOException {
        this.headers.put(X_USER_AGENT_KEY, "contentstack-java/" + SDK_VERSION);
        this.headers.put(USER_AGENT_KEY, USER_AGENT);
        this.headers.put(CONTENT_TYPE, APPLICATION_JSON);
        Response<ResponseBody> response = this.service.getRequest(requestUrl, this.headers).execute();
        if (response.isSuccessful()) {
            assert response.body() != null;
            responseJSON = new JSONObject(response.body().string());
            if (this.config.livePreviewEntry != null && !this.config.livePreviewEntry.isEmpty()) {
                handleJSONArray();
            }
            connectionRequest.onRequestFinished(CSHttpConnection.this);
        } else {
            assert response.errorBody() != null;
            setError(response.errorBody().string());
        }

    }


    void handleJSONArray() {
        JSONArray entries = new JSONArray();
        if (responseJSON.has("entries") && !responseJSON.optJSONArray("entries").isEmpty()) {
            entries = responseJSON.optJSONArray("entries");
        }
        JSONArray finalEntries = entries;
        IntStream.range(0, entries.length()).forEach(idx -> {
            JSONObject objJSON = (JSONObject) finalEntries.get(idx);
            handleJSONObject(finalEntries, objJSON, idx);
        });
    }

    void handleJSONObject(JSONArray arrayEntry, JSONObject jsonObj, int idx) {
        if (!jsonObj.isEmpty()) {
            if (jsonObj.has("uid") && jsonObj.opt("uid").equals(this.config.livePreviewEntry.opt("uid"))) {
                arrayEntry.put(idx, this.config.livePreviewEntry);
            }
        }
        responseJSON = new JSONObject().put("entries", arrayEntry);
    }

    private void deepMergeElse(JSONArray arrayEntry, JSONObject jsonObj, int idx) {

        do {
            String k = jsonObj.keySet().iterator().next();
            if (jsonObj.opt(k) instanceof JSONArray) {
                JSONArray subArray = (JSONArray) jsonObj.opt(k);
                IntStream.range(0, subArray.length()).forEach(pos -> {
                    JSONObject objJSON = (JSONObject) subArray.get(pos);
                    handleJSONObject(subArray, objJSON, pos);
                });
            }
            if (jsonObj.opt(k) instanceof JSONObject) {
                handleJSONObject(arrayEntry, ((JSONObject) jsonObj.opt(k)), idx);
            }
        } while (jsonObj.keySet().iterator().hasNext());
    }


    void setError(String errResp) {
        logger.info(errResp);
        responseJSON = new JSONObject(errResp); // Parse error string to JSONObject
        responseJSON.put(ERROR_MESSAGE, responseJSON.optString(ERROR_MESSAGE));
        responseJSON.put(ERROR_CODE, responseJSON.optString(ERROR_CODE));
        responseJSON.put(ERRORS, responseJSON.optString(ERRORS));
        int errCode = Integer.parseInt(responseJSON.optString(ERROR_CODE));
        connectionRequest.onRequestFailed(responseJSON, errCode, callBackObject);
    }

    public void setAPIService(APIService service) {
        this.service = service;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
}