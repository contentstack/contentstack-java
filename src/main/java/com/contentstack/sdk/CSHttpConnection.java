package com.contentstack.sdk;
import com.contentstack.sdk.utility.CSAppConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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

public class CSHttpConnection implements IURLRequestHTTP{

    private final String urlPath;
    private String controller;
    private LinkedHashMap<String, Object> headers;
    private String info;
    private JSONObject requestJSON;
    private final IRequestModelHTTP connectionRequest;
    private ResultCallBack callBackObject;
    private CSAppConstants.RequestMethod requestMethod;
    private JSONObject responseJSON;

    public HashMap<String, Object> getFormParams() {
        return formParams;
    }
    public void setFormParams(HashMap<String, Object> formParams) {
        this.formParams = formParams;
    }

    private HashMap<String, Object> formParams;
    private boolean treatDuplicateKeysAsArrayItems;


    public CSHttpConnection(String urlToCall, IRequestModelHTTP csConnectionRequest) {
        this.urlPath = urlToCall;
        this.connectionRequest = csConnectionRequest;
    }

    @Override
    public void setController(String controller) {
        this.controller = controller;
    }

    @Override
    public String getController() {
        return controller;
    }

    @Override
    public void setHeaders(LinkedHashMap headers) {
        this.headers = headers;
    }

    @Override
    public LinkedHashMap getHeaders() {
        return headers;
    }

    @Override
    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String getInfo() {
        return info;
    }

    public void setFormParamsPOST(JSONObject requestJSON) {
        this.requestJSON = null;
        this.requestJSON = requestJSON;
    }

    @Override
    public void setCallBackObject(ResultCallBack callBackObject) {
        this.callBackObject = callBackObject;
    }

    @Override
    public ResultCallBack getCallBackObject() {
        return callBackObject;
    }

    @Override
    public void setTreatDuplicateKeysAsArrayItems(boolean treatDuplicateKeysAsArrayItems) {
        this.treatDuplicateKeysAsArrayItems = treatDuplicateKeysAsArrayItems;
    }

    @Override
    public boolean getTreatDuplicateKeysAsArrayItems() {
        return treatDuplicateKeysAsArrayItems;
    }

    @Override
    public void setRequestMethod(CSAppConstants.RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    @Override
    public CSAppConstants.RequestMethod getRequestMethod() {
        return requestMethod;
    }

    @Override
    public JSONObject getResponse() {
        return responseJSON;
    }



    public String setFormParamsGET(HashMap<String, java.lang.Object> params){
        if(params != null && params.size() > 0){
            String urlParams = null;
            urlParams = info.equalsIgnoreCase(CSAppConstants.callController.QUERY.name()) || info.equalsIgnoreCase(CSAppConstants.callController.ENTRY.name()) ? getParams(params) : null;
            if(urlParams==null) {
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

            String key   = e.getKey();
            Object value = e.getValue();

            try {

                if (key.equalsIgnoreCase("include[]")) {
                    key = URLEncoder.encode(key, "UTF-8");
                    JSONArray array = (JSONArray) value;

                    for (int i = 0; i < array.length(); i++) {
                        urlParams += urlParams.equals("?") ?  key + "=" + array.opt(i) : "&" + key + "=" + array.opt(i);
                    }

                } else if(key.equalsIgnoreCase("only[BASE][]")){
                    key = URLEncoder.encode(key, "UTF-8");
                    JSONArray array = (JSONArray) value;

                    for (int i = 0; i < array.length(); i++) {
                        urlParams += urlParams.equals("?") ? key + "=" + array.opt(i) : "&" + key + "=" + array.opt(i);
                    }
                } else if(key.equalsIgnoreCase("except[BASE][]")){
                    key = URLEncoder.encode(key, "UTF-8");
                    JSONArray array = (JSONArray) value;

                    for (int i = 0; i < array.length(); i++) {
                        urlParams += urlParams.equals("?") ? key + "=" + array.opt(i) : "&" + key + "=" + array.opt(i);
                    }
                } else if(key.equalsIgnoreCase("only")){
                    JSONObject onlyJSON = (JSONObject) value;

                    Iterator<String> iter = onlyJSON.keys();
                    while (iter.hasNext()) {
                        String innerKey = iter.next();
                        JSONArray array = onlyJSON.optJSONArray(innerKey);
                        innerKey = URLEncoder.encode("only["+innerKey+"][]", "UTF-8");
                        for (int i = 0; i < array.length(); i++) {
                            urlParams += urlParams.equals("?") ? innerKey + "=" + array.opt(i) : "&" + innerKey + "=" + array.opt(i);
                        }
                    }

                } else if(key.equalsIgnoreCase("except")){
                    JSONObject onlyJSON = (JSONObject) value;

                    Iterator<String> iter = onlyJSON.keys();
                    while (iter.hasNext()) {
                        String innerKey = iter.next();
                        JSONArray array = onlyJSON.optJSONArray(innerKey);
                        innerKey = URLEncoder.encode("except["+innerKey+"][]", "UTF-8");
                        for (int i = 0; i < array.length(); i++) {
                            urlParams += urlParams.equals("?") ? innerKey + "=" + array.opt(i) : "&" + innerKey + "=" + array.opt(i);
                        }
                    }

                } else if(key.equalsIgnoreCase("query")){
                    JSONObject queryJSON      = (JSONObject) value;

                    urlParams += urlParams.equals("?") ? key + "=" + URLEncoder.encode(queryJSON.toString(), "UTF-8") : "&" + key + "=" + URLEncoder.encode(queryJSON.toString(), "UTF-8");

                }else{
                    urlParams += urlParams.equals("?") ? key + "=" + value : "&" + key + "=" + value;
                }

            }catch (Exception e1){
                e1.printStackTrace();
            }
        }

        return urlParams;
    }




    @Override
    public void send() {

        String url = null;
        if(requestMethod == CSAppConstants.RequestMethod.GET){
            String  params = setFormParamsGET(formParams);
            if( params != null) {
                url = urlPath + params;
            }
            else {
                url = urlPath;
            }
        }else{
            url = urlPath;
        }

        try {
            sendGET(url);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }



    private void sendGET(String GET_URL) throws IOException, JSONException {
        URL obj = new URL(GET_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        if (this.headers.containsKey("api_key")){
            con.setRequestProperty("api_key", headers.get("api_key").toString());
        }
        if (this.headers.containsKey("access_token")){
            con.setRequestProperty("access_token", headers.get("access_token").toString());
        }
        if (this.headers.containsKey("environment")){
            con.setRequestProperty("environment", headers.get("environment").toString());
        }
        con.setRequestProperty("X-User-Agent", defaultUserAgent()+"/"+ CSAppConstants.SDK_VERSION);
        con.setRequestProperty("Content-Type", "application/json");
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                StringBuilder append = response.append(inputLine);
            }
            in.close();
            responseJSON = new JSONObject(response.toString());
            connectionRequest.onRequestFinished(CSHttpConnection.this);
        } else {
            generateBuiltError(con);
        }

    }

    private String defaultUserAgent() {
        String agent = System.getProperty("http.agent");
        return agent != null ? agent : ("Java" + System.getProperty("java.version"));
    }



    private void generateBuiltError(HttpURLConnection connection){
        try {
            int statusCode = 0;
            responseJSON = new JSONObject();
            responseJSON.put("error_message", CSAppConstants.ErrorMessage_Default);

            if(connection != null){

                String responseMessage = connection.getResponseMessage();
                statusCode = connection.getResponseCode();

                try {

                    if (connection.getResponseMessage() == null && connection.getResponseMessage().equalsIgnoreCase("")) {
                        statusCode  = connection.getResponseCode();
                        responseJSON.put("error_message", CSAppConstants.ErrorMessage_Default);

                    }else{

                        if(responseMessage.equalsIgnoreCase("NoConnectionError")){

                            responseJSON.put("error_message", CSAppConstants.ErrorMessage_VolleyNoConnectionError);

                        }else if(responseMessage.equalsIgnoreCase("AuthFailureError")){

                            responseJSON.put("error_message", CSAppConstants.ErrorMessage_VolleyAuthFailureError);

                        }else if(responseMessage.equalsIgnoreCase("NetworkError")){

                            responseJSON.put("error_message", CSAppConstants.ErrorMessage_NoNetwork);

                        }else if(responseMessage.equalsIgnoreCase("ParseError")){

                            responseJSON.put("error_message", CSAppConstants.ErrorMessage_VolleyParseError);

                        }else if(responseMessage.equalsIgnoreCase("ServerError")){

                            responseJSON.put("error_message", CSAppConstants.ErrorMessage_VolleyServerError);

                        }else if(responseMessage.equalsIgnoreCase("TimeoutError")){

                            responseJSON.put("error_message", CSAppConstants.ErrorMessage_VolleyServerError);

                        } else {
                            responseJSON.put("error_message", responseMessage);
                        }

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("errors", responseMessage);
                        responseJSON.put("errors", jsonObject);
                    }
                    connectionRequest.onRequestFailed(responseJSON, statusCode, callBackObject);

                }catch(Exception e){
                    connectionRequest.onRequestFailed(responseJSON, 0, callBackObject);
                }
            } else {
                connectionRequest.onRequestFailed(responseJSON, 0, callBackObject);
            }
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }


}