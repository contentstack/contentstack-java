package com.contentstack.sdk;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.contentstack.sdk.Constants.*;

class CSConnectionRequest implements IRequestModelHTTP {

    protected String endpoint;
    private String urlToCall;
    private String controller;
    private String requestInfo;
    private LinkedHashMap<String, Object> header;
    private HashMap<String, Object> urlQueries;
    private ResultCallBack resultCallBack;
    private INotifyClass notifyClass;
    private AssetLibrary assetLibrary;
    private APIService service;
    private Entry entryInstance;
    private Asset assetInstance;
    private Stack stackInstance;

    public CSConnectionRequest(Query queryInstance) {
        notifyClass = queryInstance;
        this.endpoint = queryInstance.contentTypeInstance.stackInstance.config.getEndpoint();
    }

    public CSConnectionRequest(Entry entryInstance) {
        this.entryInstance = entryInstance;
        this.endpoint = this.entryInstance.contentType.stackInstance.config.getEndpoint();
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
        this.endpoint = contentType.stackInstance.config.getEndpoint();
    }

    public void setQueryInstance(Query queryInstance) {
        this.endpoint = queryInstance.contentTypeInstance.stackInstance.config.getEndpoint();
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
            resultCallBack = (ResultCallBack) objects[4];
        }
        this.service = (APIService) objects[5];
        sendRequest();
    }

    @Override
    public void sendRequest() {
        CSHttpConnection connection = new CSHttpConnection(urlToCall, this);
        connection.setController(controller);
        connection.setHeaders(header);
        connection.setInfo(requestInfo);
        connection.setAPIService(this.service);
        connection.setCallBackObject(resultCallBack);
        if (urlQueries != null && urlQueries.size() > 0) {
            connection.setFormParams(urlQueries);
        }
        connection.send();
    }

    @Override
    public void onRequestFailed(JSONObject error, int statusCode, ResultCallBack callBackObject) {
        Error errResp = new Error();
        if (error.has(ERROR_MESSAGE)) {
            String errMsg = error.optString(ERROR_MESSAGE);
            errResp.setErrorMessage(errMsg);
        }
        if (error.has(ERROR_CODE)) {
            int errCode = error.optInt(ERROR_CODE);
            errResp.setErrorCode(errCode);
        }
        if (error.has(ERRORS)) {
            String errorDetail = (String) error.opt(ERRORS);
            errResp.setErrorDetail(errorDetail);
        }
        if (this.resultCallBack != null) {
            this.resultCallBack.onRequestFail(ResponseType.NETWORK, errResp);
        }
    }

    @Override
    public void onRequestFinished(CSHttpConnection request) {
        JSONObject jsonResponse = request.getResponse();
        if (request.getController().equalsIgnoreCase(Constants.QUERYOBJECT)) {
            EntriesModel model = new EntriesModel(jsonResponse);
            notifyClass.getResultObject(model.objectList, jsonResponse, false);
        } else if (request.getController().equalsIgnoreCase(Constants.SINGLEQUERYOBJECT)) {
            EntriesModel model = new EntriesModel(jsonResponse);
            notifyClass.getResultObject(model.objectList, jsonResponse, true);
        } else if (request.getController().equalsIgnoreCase(Constants.FETCHENTRY)) {
            EntryModel model = new EntryModel(jsonResponse);
            entryInstance.resultJson = model.jsonObject;
            entryInstance.title = model.title;
            entryInstance.url = model.url;
            entryInstance.language = model.language;
            entryInstance.uid = model.uid;
            entryInstance.setTags(model.tags);
            if (request.getCallBackObject() != null) {
                ((EntryResultCallBack) request.getCallBackObject()).onRequestFinish(ResponseType.NETWORK);
            }
        } else if (request.getController().equalsIgnoreCase(Constants.FETCHALLASSETS)) {
            AssetsModel assetsModel = new AssetsModel(jsonResponse);
            List<Object> objectList = assetsModel.objects;
            assetLibrary.getResultObject(objectList, jsonResponse, false);
        } else if (request.getController().equalsIgnoreCase(Constants.FETCHASSETS)) {
            AssetModel model = new AssetModel(jsonResponse, false);
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
        } else if (request.getController().equalsIgnoreCase(Constants.FETCHSYNC)) {
            SyncStack model = new SyncStack();
            model.setJSON(jsonResponse);
            if (request.getCallBackObject() != null) {
                ((SyncResultCallBack) request.getCallBackObject()).onRequestFinish(model);
            }
        } else if (request.getController().equalsIgnoreCase(Constants.FETCHCONTENTTYPES)) {
            ContentTypesModel model = new ContentTypesModel();
            model.setJSON(jsonResponse);
            if (request.getCallBackObject() != null) {
                ((ContentTypesCallback) request.getCallBackObject()).onRequestFinish(model);
            }
        }
    }

}
