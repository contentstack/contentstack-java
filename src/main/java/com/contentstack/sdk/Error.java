package com.contentstack.sdk;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
    public Map<String, Object> getErrors() {
        return errorHashMap;
    }

    protected void setErrors(HashMap<String, Object> errorHashMap) {
        this.errorHashMap = errorHashMap;
    }

}
