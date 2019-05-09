package com.contentstack.sdk;

import com.contentstack.sdk.utility.CSAppConstants;
import com.contentstack.sdk.utility.CSAppUtils;
import com.contentstack.sdk.utility.CSController;
import org.json.JSONObject;

import java.util.*;

/**
 * @Author Contentstack
 *
 * MIT License
 *
 * Copyright (c) 2012 - 2019 Contentstack
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */


class CSConnectionRequest implements IRequestModelHTTP{

    private static final String TAG = "CSConnectionRequest";
    private String urlToCall;
    private CSAppConstants.RequestMethod method;
    private String controller;
    private JSONObject paramsJSON;
    private LinkedHashMap<String, Object> header;
    private HashMap<String, Object> urlQueries;
    private String requestInfo;
    private ResultCallBack callBackObject;
    private CSHttpConnection connection;
    private JSONObject responseJSON;
    private INotifyClass notifyClass;
    private INotifyClass assetLibrary;

    private Entry entryInstance;
    private Query queryInstance;
    private Asset assetInstance;
    private Stack stackInstance;
    private ContentType contentType;
    private JSONObject errorJObject;
    private Error errorObject = new Error() ;


    public CSConnectionRequest(){}

    public CSConnectionRequest(Query queryInstance) {
        notifyClass = queryInstance;
    }

    public CSConnectionRequest(Entry entryInstance) {
        this.entryInstance = entryInstance;
    }

    public CSConnectionRequest(INotifyClass assetLibrary) {
        this.assetLibrary = assetLibrary;
    }

    public CSConnectionRequest(Asset asset) {
        this.assetInstance = asset;
    }

    public CSConnectionRequest(Stack stack) {
        this.stackInstance = stack;
    }

    public CSConnectionRequest(ContentType contentType) {
        this.contentType = contentType;
    }

    public void setQueryInstance(Query queryInstance) {
        this.queryInstance = queryInstance;
    }

    public void setURLQueries(HashMap<String, Object> urlQueries) {
        this.urlQueries = urlQueries;
    }

    public void setStackInstance(Stack stackInstance) { this.stackInstance = stackInstance; }



    public void setParams(Object... objects){


        this.urlToCall    = (String) objects[0];
        this.method       = (CSAppConstants.RequestMethod) objects[1];
        this.controller   = (String) objects[2];
        paramsJSON        = (JSONObject) objects[3];
        this.header       = (LinkedHashMap<String, Object>) objects[4];


        if(objects[5]!= null){
            requestInfo   = (String) objects[5];
        }
        if(objects[6]!= null){
            callBackObject = (ResultCallBack) objects[6];
        }

        sendRequest();
    }



    @Override
    public void sendRequest() {

        connection = new CSHttpConnection(urlToCall, this);
        connection.setController(controller);
        connection.setHeaders(header);
        connection.setInfo(requestInfo);
        connection.setFormParamsPOST(paramsJSON);
        connection.setCallBackObject(callBackObject);


        if(urlQueries != null && urlQueries.size() > 0){
            connection.setFormParams(urlQueries);
        }

        connection.setRequestMethod(method);
        connection.send();

    }



    @Override
    public void onRequestFailed(JSONObject error, int statusCode, ResultCallBack callBackObject) {

        String errorMessage = null;
        int errorCode       = statusCode;
        HashMap<String, Object> resultHashMap = null;

        try {
            errorJObject = error;

            if(errorJObject != null){
                errorMessage = (errorJObject).isNull("error_message") ? "" : (errorJObject).optString("error_message");

                if((! errorJObject.isNull("error_code")) && (! errorJObject.optString("error_code").contains(" "))){
                    errorCode = (Integer) errorJObject.opt("error_code");
                }

                if(! errorJObject.isNull("errors")){
                    resultHashMap = new HashMap<String, Object>();
                    if(errorJObject.opt("errors") instanceof JSONObject){
                        JSONObject errorsJsonObj =  errorJObject.optJSONObject("errors");
                        Iterator<String> iterator = errorsJsonObj.keys();
                        while (iterator.hasNext()) {
                            String key = iterator.next();
                            Object value = errorsJsonObj.opt(key);
                            resultHashMap.put(key, value);
                        }
                    }else{
                        resultHashMap.put("errors", errorJObject.get("errors"));
                    }
                }
            }

        } catch (Exception e) {
            Stack.log(TAG, "------------------catch 210 urlReq---|" + e);
            errorMessage = e.getLocalizedMessage();
        }
        if(errorMessage == null || (! (errorMessage.length() > 0))){
            errorMessage = CSAppConstants.ErrorMessage_Default;
        }
        errorObject.setErrorCode(errorCode);
        errorObject.setErrorMessage(errorMessage);
        errorObject.setErrors(resultHashMap);

        if(this.callBackObject != null){
            this.callBackObject.onRequestFail(ResponseType.NETWORK, errorObject);
        }

    }



    @Override
    public void onRequestFinished(CSHttpConnection request) {

        responseJSON = request.getResponse();

        String controller = request.getController();

        if(controller.equalsIgnoreCase(CSController.QUERYOBJECT)){

            EntriesModel model = new EntriesModel(responseJSON, null,false);
            notifyClass.getResult(model.formName, null);
            notifyClass.getResultObject(model.objectList, responseJSON, false);
            model = null;

        }
        else if(controller.equalsIgnoreCase(CSController.SINGLEQUERYOBJECT)){

            EntriesModel model = new EntriesModel(responseJSON, null,false);
            notifyClass.getResult(model.formName, null);
            notifyClass.getResultObject(model.objectList, responseJSON, true);
            model = null;

        }else if(controller.equalsIgnoreCase(CSController.FETCHENTRY)){

            EntryModel model         		= new EntryModel(responseJSON, null,false,false,false);
            entryInstance.resultJson 		= model.jsonObject;
            entryInstance.ownerEmailId 		= model.ownerEmailId;
            entryInstance.ownerUid     		= model.ownerUid;
            entryInstance.title             = model.title;
            entryInstance.url               = model.url;
            if(model.ownerMap != null) {
                entryInstance.owner = new HashMap<>(model.ownerMap);
            }
            if(model._metadata != null) {
                entryInstance._metadata = new HashMap<>(model._metadata);
            }
            entryInstance.uid		   		= model.entryUid;
            entryInstance.setTags(model.tags);
            model = null;

            if(request.getCallBackObject() != null){
                ((EntryResultCallBack) request.getCallBackObject()).onRequestFinish(ResponseType.NETWORK);
            }

        }else if(controller.equalsIgnoreCase(CSController.FETCHALLASSETS)){
            AssetsModel assetsModel = new AssetsModel(responseJSON, false);
            List<Object> objectList = assetsModel.objects;
            assetsModel = null;

            assetLibrary.getResultObject(objectList, responseJSON, false);

        }else if(controller.equalsIgnoreCase(CSController.FETCHASSETS)){
            AssetModel model = new AssetModel(responseJSON, false, false);

            assetInstance.contentType  = model.contentType;
            assetInstance.fileSize     = model.fileSize;
            assetInstance.uploadUrl    = model.uploadUrl;
            assetInstance.fileName     = model.fileName;
            assetInstance.json         = model.json;
            assetInstance.assetUid = model.uploadedUid;
            assetInstance.setTags(model.tags);

            model = null;
            if(request.getCallBackObject() != null){
                ((FetchResultCallback) request.getCallBackObject()).onRequestFinish(ResponseType.NETWORK);
            }
        }else if(controller.equalsIgnoreCase(CSController.FETCHSYNC)){

            SyncStack model = new SyncStack();
            model.setJSON(responseJSON);
            if(request.getCallBackObject() != null){
                ((SyncResultCallBack) request.getCallBackObject()).onRequestFinish(model);
            }

        }else if(controller.equalsIgnoreCase(CSController.FETCHCONTENTTYPES)){

            ContentTypesModel model = new ContentTypesModel();
            model.setJSON(responseJSON);
            if(request.getCallBackObject() != null){
                ((ContentTypesCallback) request.getCallBackObject()).onRequestFinish(model);
            }

        }
    }


}
