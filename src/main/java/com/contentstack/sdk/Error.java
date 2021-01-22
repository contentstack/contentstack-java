package com.contentstack.sdk;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * If there is something wrong with the API request, Contentstack returns an error.
 */

public class Error {

    String errorMessage = null;
    int errorCode       = 0;
    String errDetails = null;

    public Error() {
    }

    public Error(String errorMessage, int errorCode, String  errDetails) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.errDetails = errDetails;
    }

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
     * Returns error code
     * @return int value.
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
     Returns error in {@linkplain HashMap}
     format where error is key and its respective information as HashMap&#39;s value.
     @return Map Error Hashmap
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     *  HashMap&#60;String, Object&#62; errorHashMap = error.getErrors();
     * </pre>
     */
    public String getErrorDetail() {
        return this.errDetails;
    }

    protected void setErrorDetail(String errDetails) {
        this.errDetails = errDetails;
    }

}
