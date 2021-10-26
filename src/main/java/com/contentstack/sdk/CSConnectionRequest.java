package com.contentstack.sdk;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;


class CSConnectionRequest implements IRequestModelHTTP {

    protected String endpoint;
    private String urlToCall;
    private String controller;
    private String requestInfo;
    private JSONObject paramsJSON;
    private LinkedHashMap<String, Object> header;
    private HashMap<String, Object> urlQueries;
    private ResultCallBack callBackObject;
    private CSHttpConnection connection;
    private JSONObject responseJSON;
    private INotifyClass notifyClass;
    private AssetLibrary assetLibrary;

    private Entry entryInstance;
    private Query queryInstance;
    private Asset assetInstance;
    private Stack stackInstance;
    private ContentType contentType;
    private JSONObject errorJObject;
    private Error errorObject = new Error();


    public CSConnectionRequest() {
    }

    public CSConnectionRequest(Query queryInstance) {
        notifyClass = queryInstance;
        this.endpoint = queryInstance.contentTypeInstance.stackInstance.config.getEndpoint();
    }

    public CSConnectionRequest(Entry entryInstance) {
        this.entryInstance = entryInstance;
        this.endpoint = this.entryInstance.contentTypeInstance.stackInstance.config.getEndpoint();
    }

    public CSConnectionRequest(AssetLibrary assetLibrary) {
        this.assetLibrary = assetLibrary;
        this.endpoint = this.assetLibrary.stackInstance.config.getEndpoint();
    }

    public CSConnectionRequest(Asset asset) {
        this.assetInstance = asset;
        this.endpoint = this.assetInstance.stackInstance.config.getEndpoint();
    }

    public CSConnectionRequest(Stack stack) {
        this.stackInstance = stack;
    }

    public CSConnectionRequest(ContentType contentType) {
        this.contentType = contentType;
        this.endpoint = this.contentType.stackInstance.config.getEndpoint();
    }

    public void setQueryInstance(Query queryInstance) {
        this.queryInstance = queryInstance;
    }

    public void setURLQueries(HashMap<String, Object> urlQueries) {
        this.urlQueries = urlQueries;
    }

    public void setStackInstance(Stack stackInstance) {
        this.endpoint = this.stackInstance.config.getEndpoint();
        this.stackInstance = stackInstance;
    }


    public void setParams(Object... objects) {
        this.urlToCall = (String) objects[0];
        this.header = (LinkedHashMap<String, Object>) objects[1];
        this.controller = (String) objects[2];
        this.requestInfo = (String) objects[3];
        if (objects[4] != null) {
            callBackObject = (ResultCallBack) objects[4];
        }
        sendRequest();
    }


    @Override
    public void sendRequest() {
        connection = new CSHttpConnection(urlToCall, this);
        connection.setController(controller);
        connection.setHeaders(header);
        connection.setInfo(requestInfo);
        connection.setEndpoint(this.endpoint);
        connection.setFormParamsPOST(paramsJSON);
        connection.setCallBackObject(callBackObject);
        if (urlQueries != null && urlQueries.size() > 0) {
            connection.setFormParams(urlQueries);
        }
        connection.send();
    }


    @Override
    public void onRequestFailed(JSONObject error, int statusCode, ResultCallBack callBackObject) {
        String errMsg = "";
        int errCode = 0;
        if (error != null && !error.isEmpty()) {
            if (error.has("error_message")) {
                errMsg = error.optString("error_message");
            }
            if (error.has("error_code")) {
                errCode = error.optInt("error_code");
            }
            if (error.has("errors")) {
                if (error.opt("errors") instanceof JSONObject) {
                    JSONObject errorsJsonObj = error.optJSONObject("errors");
                    Iterator<String> iterator = errorsJsonObj.keys();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        errorObject.setErrorDetail(error.get("errors").toString());
                    }
                }
            }
        }
        errorObject.setErrorCode(errCode);
        errorObject.setErrorMessage(errMsg);
        errorObject.setErrorDetail(error.get("errors").toString());
        if (this.callBackObject != null) {
            this.callBackObject.onRequestFail(ResponseType.NETWORK, errorObject);
        }
    }


    @Override
    public void onRequestFinished(CSHttpConnection request) {

        responseJSON = request.getResponse();
        String controller = request.getController();
        if (controller.equalsIgnoreCase(Constants.QUERYOBJECT)) {
            EntriesModel model = new EntriesModel(responseJSON, null, false);
            notifyClass.getResult(model.formName, null);
            notifyClass.getResultObject(model.objectList, responseJSON, false);
        } else if (controller.equalsIgnoreCase(Constants.SINGLEQUERYOBJECT)) {
            EntriesModel model = new EntriesModel(responseJSON, null, false);
            notifyClass.getResult(model.formName, null);
            notifyClass.getResultObject(model.objectList, responseJSON, true);
        } else if (controller.equalsIgnoreCase(Constants.FETCHENTRY)) {
            EntryModel model = new EntryModel(responseJSON, null, false, false, false);
            entryInstance.resultJson = model.jsonObject;
            entryInstance.ownerEmailId = model.ownerEmailId;
            entryInstance.ownerUid = model.ownerUid;
            entryInstance.title = model.title;
            entryInstance.url = model.url;
            entryInstance.language = model.language;
            if (model.ownerMap != null) {
                entryInstance.owner = new HashMap<>(model.ownerMap);
            }
            if (model._metadata != null) {
                entryInstance._metadata = new HashMap<>(model._metadata);
            }
            entryInstance.uid = model.entryUid;
            entryInstance.setTags(model.tags);
            if (request.getCallBackObject() != null) {
                ((EntryResultCallBack) request.getCallBackObject()).onRequestFinish(ResponseType.NETWORK);
            }
        } else if (controller.equalsIgnoreCase(Constants.FETCHALLASSETS)) {
            AssetsModel assetsModel = new AssetsModel(responseJSON);
            List<Object> objectList = assetsModel.objects;
            assetLibrary.getResultObject(objectList, responseJSON, false);
        } else if (controller.equalsIgnoreCase(Constants.FETCHASSETS)) {
            AssetModel model = new AssetModel(responseJSON, false);
            assetInstance.contentType = model.contentType;
            assetInstance.fileSize = model.fileSize;
            assetInstance.uploadUrl = model.uploadUrl;
            assetInstance.fileName = model.fileName;
            assetInstance.json = model.json;
            assetInstance.assetUid = model.uploadedUid;
            assetInstance.setTags(model.tags);
            if (request.getCallBackObject() != null) {
                ((FetchResultCallback) request.getCallBackObject()).onRequestFinish(ResponseType.NETWORK);
            }
        } else if (controller.equalsIgnoreCase(Constants.FETCHSYNC)) {
            SyncStack model = new SyncStack();
            model.setJSON(responseJSON);
            if (request.getCallBackObject() != null) {
                ((SyncResultCallBack) request.getCallBackObject()).onRequestFinish(model);
            }
        } else if (controller.equalsIgnoreCase(Constants.FETCHCONTENTTYPES)) {
            ContentTypesModel model = new ContentTypesModel();
            model.setJSON(responseJSON);
            if (request.getCallBackObject() != null) {
                ((ContentTypesCallback) request.getCallBackObject()).onRequestFinish(model);
            }

        }
    }


}
