package com.contentstack.sdk.utility;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
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

public class CSAppConstants {

    public static final String SDK_VERSION = "1.5.0";

    public static enum RequestMethod
    {
        GET,
        POST,
        PUT,
        DELETE
    }

    public static enum callController {

        QUERY,
        ENTRY,
        ASSET,
        SYNC,
        CONTENTTYPES,
        ASSETLIBRARY
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public final static String ErrorMessage_JsonNotProper 			 = "Please provide valid JSON.";
    public final static String ErrorMessage_StackApiKeyIsNull        = "Stack api key can not be null.";
    public final static String ErrorMessage_FormName 				 = "Please set contentType name.";
    public final static String ErrorMessage_Stack_AccessToken_IsNull = "Access token can not be null.";
    public final static String ErrorMessage_Stack_Environment_IsNull = "Environment can not be null.";
    public final static String ErrorMessage_VolleyNoConnectionError  = "Connection error";
    public final static String ErrorMessage_VolleyAuthFailureError 	 = "Authentication Not present.";
    public final static String ErrorMessage_VolleyParseError 		 = "Parsing Error.";
    public final static String ErrorMessage_VolleyServerError 		 = "Server interaction went wrong, Please try again.";
    public final static String ErrorMessage_Default 				 = "Oops! Something went wrong. Please try again.";
    public final static String ErrorMessage_NoNetwork 				 = "Network not available.";
    public final static String ErrorMessage_CalledDefaultMethod      = "You must called Contentstack.stack() first";
    public final static String ErrorMessage_QueryFilterException 	 = "Please provide valid params.";
}

