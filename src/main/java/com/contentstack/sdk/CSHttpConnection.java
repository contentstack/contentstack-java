package com.contentstack.sdk;

import okhttp3.Request;
import okhttp3.ResponseBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import com.fasterxml.jackson.databind.ObjectMapper; // Jackson for JSON parsing
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.MapType;

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
    private Stack stackInstance;
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

    private JSONObject createOrderedJSONObject(Map<String, Object> map) {
        JSONObject json = new JSONObject();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            json.put(entry.getKey(), entry.getValue());
        }
        return json;
    }

    private void getService(String requestUrl) throws IOException {

        this.headers.put(X_USER_AGENT_KEY, "contentstack-delivery-java/" + SDK_VERSION);
        this.headers.put(USER_AGENT_KEY, USER_AGENT);
        this.headers.put(CONTENT_TYPE, APPLICATION_JSON);

        Request request = null;
        if (this.config.plugins != null) {
            request = pluginRequestImp(requestUrl);
            this.headers.clear();
            Request finalRequest = request;
            request.headers().names().forEach(key -> {
                this.headers.put(key, finalRequest.headers().get(key));
            });
            requestUrl = request.url().toString();
        }

        try {
            Response<ResponseBody> response = this.service.getRequest(requestUrl, this.headers).execute();
            if (response.isSuccessful()) {
                assert response.body() != null;
                if (request != null) {
                    response = pluginResponseImp(request, response);
                }
                try {
                    // Use Jackson to parse the JSON while preserving order
                    ObjectMapper mapper = JsonMapper.builder().build();
                    MapType type = mapper.getTypeFactory().constructMapType(LinkedHashMap.class, String.class,
                            Object.class);
                    Map<String, Object> responseMap = mapper.readValue(response.body().string(), type);

                    // Use the custom method to create an ordered JSONObject
                    responseJSON = createOrderedJSONObject(responseMap);
                    if (this.config.livePreviewEntry != null && !this.config.livePreviewEntry.isEmpty()) {
                        handleJSONArray();
                    }
                    connectionRequest.onRequestFinished(CSHttpConnection.this);
                } catch (JSONException e) {
                    // Handle non-JSON response
                    setError("Invalid JSON response");
                }
            } else {
                assert response.errorBody() != null;
                setError(response.errorBody().string());
            }
        } catch (SocketTimeoutException e) {
            // Handle timeout
            setError("Request timed out: " + e.getMessage());
        } catch (IOException e) {
            // Handle other IO exceptions
            setError("IO error occurred: " + e.getMessage());
        }
    }

    private Request pluginRequestImp(String requestUrl) {
        Call<ResponseBody> call = this.service.getRequest(requestUrl, this.headers);
        Request request = call.request();
        this.config.plugins.forEach(plugin -> plugin.onRequest(this.stackInstance, request));
        return request;
    }

    private Response<ResponseBody> pluginResponseImp(Request request, Response<ResponseBody> response) {
        this.config.plugins.forEach(plugin -> plugin.onResponse(this.stackInstance, request, response));
        return response;
    }

    void handleJSONArray() {
        if (responseJSON.has("entries") && !responseJSON.optJSONArray("entries").isEmpty()) {
            JSONArray finalEntries = responseJSON.optJSONArray("entries");
            IntStream.range(0, finalEntries.length()).forEach(idx -> {
                JSONObject objJSON = (JSONObject) finalEntries.get(idx);
                handleJSONObject(finalEntries, objJSON, idx);
            });
        }
        if (responseJSON.has("entry") && !responseJSON.optJSONObject("entry").isEmpty()) {
            JSONObject entry = responseJSON.optJSONObject("entry");
            if (!entry.isEmpty()) {
                if (entry.has("uid") && entry.opt("uid").equals(this.config.livePreviewEntry.opt("uid"))) {
                    responseJSON = new JSONObject().put("entry", this.config.livePreviewEntry);
                }
            }
        }

    }

    void handleJSONObject(JSONArray arrayEntry, JSONObject jsonObj, int idx) {
        if (!jsonObj.isEmpty()) {
            if (jsonObj.has("uid") && jsonObj.opt("uid").equals(this.config.livePreviewEntry.opt("uid"))) {
                arrayEntry.put(idx, this.config.livePreviewEntry);
            }
        }
        responseJSON = new JSONObject().put("entries", arrayEntry);
    }

    void setError(String errResp) {
        try {
            responseJSON = new JSONObject(errResp);
        } catch (JSONException e) {
            // If errResp is not valid JSON, create a new JSONObject with the error message
            responseJSON = new JSONObject();
            responseJSON.put(ERROR_MESSAGE, errResp);
        }
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

    public void setStack(Stack stackInstance) {
        this.stackInstance = stackInstance;
    }
}