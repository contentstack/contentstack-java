package com.contentstack.sdk;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * To retrieve information related to network call failure.
 *
 *  * @author  built.io. Inc
 */
public class Error {

    String errorMessage = null;
    int errorCode       = 0;
    Map<String, Object> errorHashMap = new LinkedHashMap<>();


    /**
     *
     * Returns error in string format.
     * @return String error message
     * <br><br><b>Example :</b><br>
     *  <pre class="prettyprint">
     *  String errorString = error.getErrorMessage();
     *  </pre>
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    protected void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


    /**
     * Returns error code.
     *
     * @return int value.
     *
     *  <br><br><b>Example :</b><br>
     *  <pre class="prettyprint">
     *  int errorCode = error.getErrorCode();
     *  </pre>
     */
    public int getErrorCode() {
        return errorCode;
    }

    protected void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }


    /**
     *
     * Returns error in {@linkplain HashMap} format where error is key and its respective information as HashMap&#39;s value.


     @return Map Error Hashmap
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     *  HashMap&#60;String, Object&#62; errorHashMap = error.getErrors();
     * </pre>
     */
    public Map<String, Object> getErrors() {
        return errorHashMap;
    }

    protected void setErrors(HashMap<String, Object> errorHashMap) {
        this.errorHashMap = errorHashMap;
    }

}
