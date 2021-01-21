package com.contentstack.sdk;
import com.contentstack.sdk.utility.CSAppConstants;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.LinkedHashMap;


class CSBackgroundTask {

    public CSBackgroundTask(Query queryInstance, Stack stackInstance, String controller, String url, LinkedHashMap<String, Object> headers, LinkedHashMap<String, Object> urlQueries, JSONObject jsonMain, String requestInfo, CSAppConstants.RequestMethod method, ResultCallBack callback) {
            if (headers != null && headers.size() > 0) {
                String URL = stackInstance.URLSCHEMA + stackInstance.URL + url;
                CSConnectionRequest csConnectionRequest = new CSConnectionRequest(queryInstance);
                csConnectionRequest.setQueryInstance(queryInstance);
                csConnectionRequest.setURLQueries(urlQueries);
                csConnectionRequest.setParams(URL, method, controller, jsonMain, headers, requestInfo, callback);
            } else {
                sendErrorForHeader(callback);
            }
    }


    public CSBackgroundTask(Entry entryInstance, Stack stackInstance, String controller, String url, LinkedHashMap<String, Object> headers, HashMap<String, Object> urlQueries, JSONObject jsonMain, String requestInfo, boolean isOffline, CSAppConstants.RequestMethod method, ResultCallBack callBack) {
            if (headers != null && headers.size() > 0) {
                String URL = stackInstance.URLSCHEMA + stackInstance.URL + url;
                CSConnectionRequest csConnectionRequest = new CSConnectionRequest(entryInstance);
                csConnectionRequest.setURLQueries(urlQueries);
                csConnectionRequest.setParams(URL, method, controller, jsonMain, headers,  requestInfo, callBack);
            } else {
                sendErrorForHeader(callBack);
            }
    }



    public CSBackgroundTask(AssetLibrary assetLibrary, Stack stackInstance, String controller, String url, LinkedHashMap<String, Object> headers, HashMap<String, Object> urlQueries, JSONObject jsonMain,  String requestInfo, boolean isOffline, CSAppConstants.RequestMethod method, ResultCallBack callback) {
            if (headers != null && headers.size() > 0) {
                String URL = stackInstance.URLSCHEMA + stackInstance.URL + url;
                CSConnectionRequest csConnectionRequest = new CSConnectionRequest(assetLibrary);
                csConnectionRequest.setURLQueries(urlQueries);
                csConnectionRequest.setParams(URL, method, controller, jsonMain, headers,  requestInfo, callback);
            } else {
                sendErrorForHeader(callback);
            }
    }


    public CSBackgroundTask(Asset asset, Stack stackInstance, String controller, String url, LinkedHashMap<String, Object> headers, HashMap<String, Object> urlQueries, JSONObject jsonMain,  String requestInfo, boolean isOffline, CSAppConstants.RequestMethod method, ResultCallBack callback) {
            if (headers != null && headers.size() > 0) {
                String URL = stackInstance.URLSCHEMA + stackInstance.URL + url;
                CSConnectionRequest csConnectionRequest = new CSConnectionRequest(asset);
                csConnectionRequest.setURLQueries(urlQueries);
                csConnectionRequest.setParams(URL, method, controller, jsonMain, headers, requestInfo, callback);
            } else {
                sendErrorForHeader(callback);
            }

    }


    public CSBackgroundTask(Stack stackInstance, String controller, String url, HashMap<String, Object> headers, HashMap<String, Object> urlParams, JSONObject jsonMain, String requestInfo, boolean b, CSAppConstants.RequestMethod method, ResultCallBack callback) {
            if (headers != null && headers.size() > 0) {
                String URL = stackInstance.URLSCHEMA + stackInstance.URL + url;
                CSConnectionRequest csConnectionRequest = new CSConnectionRequest(stackInstance);
                csConnectionRequest.setStackInstance(stackInstance);
                csConnectionRequest.setURLQueries(urlParams);
                csConnectionRequest.setParams(URL, method, controller, jsonMain, headers, requestInfo, callback);
            } else {
                sendErrorForHeader(callback);
            }
    }


    public CSBackgroundTask(ContentType contentTypeInstance, Stack stackInstance, String controller, String url, HashMap<String, Object> headers, HashMap<String, Object> urlParams, JSONObject jsonMain, String requestInfo, boolean b, CSAppConstants.RequestMethod method, ResultCallBack callback) {
            if (headers != null && headers.size() > 0) {
                String URL = stackInstance.URLSCHEMA + stackInstance.URL + url;
                CSConnectionRequest csConnectionRequest = new CSConnectionRequest(contentTypeInstance);
                csConnectionRequest.setURLQueries(urlParams);
                csConnectionRequest.setParams(URL, method, controller, jsonMain, headers, requestInfo, callback);
            } else {
                sendErrorForHeader(callback);
            }
    }

    private void sendErrorForHeader(ResultCallBack callbackObject) {
        Error error = new Error();
        error.setErrorMessage(CSAppConstants.ErrorMessage_CalledDefaultMethod);
        if (callbackObject != null) {
            callbackObject.onRequestFail(ResponseType.UNKNOWN, error);
        }
    }
}
