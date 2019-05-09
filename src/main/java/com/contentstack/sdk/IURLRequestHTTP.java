package com.contentstack.sdk;

import com.contentstack.sdk.utility.CSAppConstants;
import org.json.JSONObject;
import java.util.LinkedHashMap;


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
