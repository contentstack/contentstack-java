package com.contentstack.sdk;

import com.contentstack.sdk.utility.CSAppConstants;
import org.json.JSONObject;

import java.util.LinkedHashMap;

public interface IURLRequestHTTP {

    public void send();

    public LinkedHashMap<String, String> getHeaders();

    public void setHeaders(LinkedHashMap<String, Object> headers);

    public CSAppConstants.RequestMethod getRequestMethod();

    public void setRequestMethod(CSAppConstants.RequestMethod requestMethod);

    public JSONObject getResponse();

    public String getInfo();

    public void setInfo(String info);

    public String getController();

    public void setController(String controller);

    public ResultCallBack getCallBackObject();

    public void setCallBackObject(ResultCallBack builtResultCallBackObject);

    public boolean getTreatDuplicateKeysAsArrayItems();

    public void setTreatDuplicateKeysAsArrayItems(boolean treatDuplicateKeysAsArrayItems);


}
