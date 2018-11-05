package com.contentstack.sdk.Utility;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class CSAppConstants {

    public static final boolean debug = false;
    public static final String SYNC_STORE_DEFAULT_TIMESTAMP  = "2010-01-01T00:00:00.000Z";;
    public static final String REQUEST_URL = "";
    public static String URLSCHEMA_HTTP = "http://";
    public static String URLSCHEMA_HTTPS = "https://";
    public static String SDK_VERSION = "1.2.0";
    public final static int NONETWORKCONNECTION = 408;
    public final static int TimeOutDuration = 15000;
    public final static int NumRetry = 0;
    public final static int BackOFMultiplier = 0;
    public static ArrayList<String> cancelledCallController = new ArrayList<String>();

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
        STACK,
        ASSET,
        SYNC,
        ASSETLIBRARY;
    }


    public static String printMessage(String message)
    {
        System.out.println(message);
        return message;
    }



    public static boolean isNetworkAvailable() {
        try {
            final URL url = new URL("http://www.google.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public final static String ErrorMessage_JsonNotProper 			 = "Please provide valid JSON.";
    public final static String ErrorMessage_StackContextIsNull       = "Context can not be null.";
    public final static String ErrorMessage_StackApiKeyIsNull        = "Stack api key can not be null.";
    public final static String ErrorMessage_FormName 				 = "Please set contentType name.";
    public final static String ErrorMessage_EntryUID 				 = "Please set entry uid.";
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
    public final static String ErrorMessage_EntryNotFoundInCache     = "ENTRY is not present in cache";
    public final static String ErrorMessage_SavingNetworkCallResponseForCache 	= "Error while saving network call response.";
}

