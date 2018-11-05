package com.contentstack.sdk;

import com.contentstack.sdk.Utility.CSAppConstants;
import com.contentstack.sdk.Utility.CSAppUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author built.io, Inc
 *
 */
public class CSHttpConnection implements IURLRequestHTTP{

    private static final String TAG = "CSHttpConnection";
    private String urlPath;
    private String controller;
    private LinkedHashMap<String, Object> headers;
    private String info;
    private JSONObject requestJSON;
    private IRequestModelHTTP connectionRequest;
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

            for (Map.Entry<String, Object> e : params.entrySet()) {

                if (urlParams == null) {
                    urlParams = "?" + e.getKey() + "=" + e.getValue();
                } else {
                    urlParams += "&" + e.getKey() + "=" + e.getValue();
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
                        JSONArray array = (JSONArray) onlyJSON.optJSONArray(innerKey);
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
                        JSONArray array = (JSONArray) onlyJSON.optJSONArray(innerKey);
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
                CSAppUtils.showLog(TAG, "--------------------getQueryParam--||"+e1.toString());
            }
        }

        return urlParams;
    }




    @Override
    public void send() {

        String url                            = null;
        String httpsORhttp                    = CSAppConstants.URLSCHEMA_HTTPS;
        int requestId                         = getRequestId(requestMethod);
        final Map<String, String> headers     = new LinkedHashMap<>();
        int count                             = this.headers.size();

        if(requestMethod == CSAppConstants.RequestMethod.GET){
            String  params = setFormParamsGET(formParams);

            if( params != null)
            {
                url = urlPath + params;
            }
            else
            {
                url = urlPath;
            }
        }else{
            url = urlPath;
        }

        if(url.contains(CSAppConstants.URLSCHEMA_HTTPS)){
            httpsORhttp = CSAppConstants.URLSCHEMA_HTTPS;
        }else if(url.contains(CSAppConstants.URLSCHEMA_HTTP)){
            httpsORhttp = CSAppConstants.URLSCHEMA_HTTP;
        }

        for (Map.Entry<String, Object> entry : this.headers.entrySet())
        {
            String key = entry.getKey();
            String value = (String) entry.getValue();
            headers.put(key, value);
        }

        headers.put("Content-Type", "application/json");
        headers.put("User-Agent", defaultUserAgent()+"/"+ CSAppConstants.SDK_VERSION);
        requestJSON.put("_method","get");


        try {
            /* always network call*/
            String requestMethod = CSAppConstants.RequestMethod.GET.toString();
            callNetworkFunction(url, requestMethod, requestJSON, (LinkedHashMap<String, String>) headers);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    private void callNetworkFunction(String url, String requestMethod, JSONObject jsonParam, Map<String, String> reqHeaders) throws IOException {

        URL objURL = new URL(url);
        HttpURLConnection con = (HttpURLConnection) objURL.openConnection();
        con.setRequestMethod(requestMethod);

        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<String, String> entry : reqHeaders.entrySet())
        {
            String key = entry.getKey();
            String value = String.valueOf(entry.getValue());
            con.setRequestProperty(key, value);
            stringBuilder.append(key+":"+value+",");
        }

        con.setDoOutput(true);
        con.setDoInput(true);

        OutputStream outputStream = con.getOutputStream();
        String paramString = jsonParam.toString().trim();

        if (!paramString.isEmpty() && paramString!=null)
        {
            byte[] postParams = paramString.getBytes("UTF-8");
            outputStream.write(postParams);
        }

        outputStream.flush();
        outputStream.close();
        int responseCode = con.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK)
        {
            BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = inputStreamReader.readLine()) != null) {
                response.append(inputLine);
            }
            inputStreamReader.close();

            responseJSON = new JSONObject(response.toString());

            if (responseJSON != null) {
                connectionRequest.onRequestFinished(CSHttpConnection.this);
            }

        } else {

            generateBuiltError(con);

        }


    }





    private int getRequestId(CSAppConstants.RequestMethod requestMethod) {

        switch (requestMethod){
            case GET:
                return 0;
            case POST:
                return 1;
            case PUT:
                return 2;
            case DELETE:
                return 3;
            default:
                return 1;
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
                        //String responseBody = new String(error.networkResponse.data, "utf-8");
                        //responseJSON = responseBody != null ? new JSONObject(responseBody) : new JSONObject();
                        responseJSON.put("error_message", CSAppConstants.ErrorMessage_Default);

                    }else{

                        if(responseMessage.toString().equalsIgnoreCase("NoConnectionError")){

                            responseJSON.put("error_message", CSAppConstants.ErrorMessage_VolleyNoConnectionError);

                        }else if(responseMessage.toString().equalsIgnoreCase("AuthFailureError")){

                            responseJSON.put("error_message", CSAppConstants.ErrorMessage_VolleyAuthFailureError);

                        }else if(responseMessage.toString().equalsIgnoreCase("NetworkError")){

                            responseJSON.put("error_message", CSAppConstants.ErrorMessage_NoNetwork);

                        }else if(responseMessage.toString().equalsIgnoreCase("ParseError")){

                            responseJSON.put("error_message", CSAppConstants.ErrorMessage_VolleyParseError);

                        }else if(responseMessage.toString().equalsIgnoreCase("ServerError")){

                            responseJSON.put("error_message", CSAppConstants.ErrorMessage_VolleyServerError);

                        }else if(responseMessage.toString().equalsIgnoreCase("TimeoutError")){

                            responseJSON.put("error_message", CSAppConstants.ErrorMessage_VolleyServerError);

                        } else {
                            if (responseMessage != null) {
                                responseJSON.put("error_message", responseMessage);
                            }
                        }

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("errors", responseMessage.toString());
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
            CSAppUtils.showLog(TAG, exception.toString());
        }
    }


}
