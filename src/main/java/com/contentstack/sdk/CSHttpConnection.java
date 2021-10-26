package com.contentstack.sdk;

import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

public class CSHttpConnection implements IURLRequestHTTP {

    private final Logger logger = Logger.getLogger(CSHttpConnection.class.getName());
    private final String urlPath;
    private final IRequestModelHTTP connectionRequest;
    private String controller;
    private LinkedHashMap<String, Object> headers;
    private String info;
    private String endpoint;
    private JSONObject requestJSON;
    private ResultCallBack callBackObject;
    private Constants.REQUEST_METHOD requestMethod;
    private JSONObject responseJSON;
    private HashMap<String, Object> formParams;
    //private boolean treatDuplicateKeysAsArrayItems;

    public CSHttpConnection(String urlToCall, IRequestModelHTTP csConnectionRequest) {
        this.urlPath = urlToCall;
        this.connectionRequest = csConnectionRequest;
    }

    public HashMap<String, Object> getFormParams() {
        return formParams;
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
    public LinkedHashMap getHeaders() {
        return headers;
    }

    @Override
    public void setHeaders(LinkedHashMap<String, Object> headers) {
        this.headers = headers;
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public void setInfo(String info) {
        this.info = info;
    }

    public void setFormParamsPOST(JSONObject requestJSON) {
        this.requestJSON = requestJSON;
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

    public String setFormParamsGET(HashMap<String, java.lang.Object> params) {
        if (params != null && params.size() > 0) {
            String urlParams = null;
            urlParams = info.equalsIgnoreCase(Constants.REQUEST_CONTROLLER.QUERY.name()) || info.equalsIgnoreCase(Constants.REQUEST_CONTROLLER.ENTRY.name()) ? getParams(params) : null;
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
                if (key.equalsIgnoreCase("include[]") ||
                        key.equalsIgnoreCase("only[BASE][]") ||
                        key.equalsIgnoreCase("except[BASE][]")) {
                    urlParams = convertUrlParam(urlParams, value, key);
                } else if (key.equalsIgnoreCase("only")) {
                    JSONObject onlyJSON = (JSONObject) value;
                    Iterator<String> itrString = onlyJSON.keys();
                    while (itrString.hasNext()) {
                        String innerKey = itrString.next();
                        JSONArray array = onlyJSON.optJSONArray(innerKey);
                        innerKey = URLEncoder.encode("only[" + innerKey + "][]", StandardCharsets.UTF_8);
                        for (int i = 0; i < array.length(); i++) {
                            urlParams += urlParams.equals("?") ? innerKey + "=" + array.opt(i) : "&" + innerKey + "=" + array.opt(i);
                        }
                    }
                } else if (key.equalsIgnoreCase("except")) {
                    JSONObject onlyJSON = (JSONObject) value;
                    Iterator<String> iter = onlyJSON.keys();
                    while (iter.hasNext()) {
                        String innerKey = iter.next();
                        JSONArray array = onlyJSON.optJSONArray(innerKey);
                        innerKey = URLEncoder.encode("except[" + innerKey + "][]", StandardCharsets.UTF_8);
                        for (int i = 0; i < array.length(); i++) {
                            urlParams += urlParams.equals("?") ? innerKey + "=" + array.opt(i) : "&" + innerKey + "=" + array.opt(i);
                        }
                    }
                } else if (key.equalsIgnoreCase("query")) {
                    JSONObject queryJSON = (JSONObject) value;
                    urlParams += urlParams.equals("?") ? key + "=" + URLEncoder.encode(queryJSON.toString(), StandardCharsets.UTF_8) : "&" + key + "=" + URLEncoder.encode(queryJSON.toString(), StandardCharsets.UTF_8);
                } else {
                    urlParams += urlParams.equals("?") ? key + "=" + value : "&" + key + "=" + value;
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return urlParams;
    }


    private String convertUrlParam(String urlParams, Object value, String key) {
        key = URLEncoder.encode(key, StandardCharsets.UTF_8);
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
            //sendGET(url);
            getService(url);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void getService(String requestUrl) throws IOException {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(this.endpoint).build();
        APIService service = retrofit.create(APIService.class);
        this.headers.remove(Constants.ENVIRONMENT);
        Response<ResponseBody> response = service.getRequest(requestUrl, this.headers).execute();
        if (response.isSuccessful()) {
            String resp = response.body().string();
            logger.info(resp);
            responseJSON = new JSONObject(resp);
            connectionRequest.onRequestFinished(CSHttpConnection.this);
        } else {
            setError(response.errorBody().string());
        }

    }


    void setError(String errResp) {
        logger.info(errResp);
        responseJSON = new JSONObject(errResp); // Parse error string to JSONObject
        responseJSON.put("error_message", responseJSON.optString("error_message"));
        responseJSON.put("error_code", responseJSON.optString("error_code"));
        responseJSON.put("errors", responseJSON.optString("errors"));
        int errCode = Integer.parseInt(responseJSON.optString("error_code"));
        connectionRequest.onRequestFailed(responseJSON, errCode, callBackObject);
    }


    private void sendGET(String requestUrl) throws IOException, JSONException {
        URL objUrl = new URL(requestUrl);
        HttpURLConnection conn = (HttpURLConnection) objUrl.openConnection();
        conn.setRequestMethod("GET");
        if (this.headers.containsKey("api_key")) {
            conn.setRequestProperty("api_key", headers.get("api_key").toString());
        }
        if (this.headers.containsKey("access_token")) {
            conn.setRequestProperty("access_token", headers.get("access_token").toString());
        }
        if (this.headers.containsKey("environment")) {
            conn.setRequestProperty("environment", headers.get("environment").toString());
        }
        conn.setRequestProperty("X-User-Agent", defaultUserAgent() + "/" + Constants.SDK_VERSION);
        conn.setRequestProperty("Content-Type", "application/json");
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                StringBuilder append = response.append(inputLine);
            }
            in.close();
            responseJSON = new JSONObject(response.toString());
            connectionRequest.onRequestFinished(CSHttpConnection.this);
        } else {
            settingErrorInfo(conn);
        }

    }

    private void settingErrorInfo(HttpURLConnection con) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream(), StandardCharsets.UTF_8));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        responseJSON = new JSONObject(response.toString());
        String errorMsg = responseJSON.optString("error_message");
        String errorCode = responseJSON.optString("error_code");
        String errorDetails = responseJSON.optString("errors");
        responseJSON.put("error_message", errorMsg);
        responseJSON.put("error_code", errorCode);
        responseJSON.put("errors", errorDetails);
        int errorCodeInt = Integer.parseInt(errorCode);
        connectionRequest.onRequestFailed(responseJSON, errorCodeInt, callBackObject);
    }

    private String defaultUserAgent() {
        String agent = System.getProperty("http.agent");
        return agent != null ? agent : ("Java" + System.getProperty("java.version"));
    }

    protected void setEndpoint(@NotNull String endpoint) {
        this.endpoint = endpoint;
    }
}