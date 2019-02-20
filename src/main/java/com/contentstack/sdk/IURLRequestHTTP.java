package com.contentstack.sdk;

import com.contentstack.sdk.utility.CSAppConstants;
import org.json.JSONObject;
import java.util.LinkedHashMap;

/**
 *
 * @author Contentstack.com
 *
 */
public interface IURLRequestHTTP {

    public void send();

    public void setHeaders(LinkedHashMap headers);

    public LinkedHashMap getHeaders();

    public void setRequestMethod(CSAppConstants.RequestMethod requestMethod);

    public CSAppConstants.RequestMethod getRequestMethod();

    public JSONObject getResponse();

    public void setInfo(String info);

    public String getInfo();

    public void setController(String controller);

    public String getController();

    public void setCallBackObject(ResultCallBack builtResultCallBackObject);

    public ResultCallBack getCallBackObject();

    public void setTreatDuplicateKeysAsArrayItems(boolean treatDuplicateKeysAsArrayItems);

    public boolean getTreatDuplicateKeysAsArrayItems();


}
