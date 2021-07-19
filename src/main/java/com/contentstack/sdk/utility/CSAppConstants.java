package com.contentstack.sdk.utility;

/**
 * Package Constants
 */
public class CSAppConstants {

    /**
     * The constant SDK_VERSION.
     */
    public static final String SDK_VERSION = "1.7.0";

    /**
     * The enum Request method.
     */
    public static enum RequestMethod
    {
        /**
         * Get request method.
         */
        GET,
        /**
         * Post request method.
         */
        POST,
        /**
         * Put request method.
         */
        PUT,
        /**
         * Delete request method.
         */
        DELETE
    }

    /**
     * The enum Call controller.
     */
    public static enum callController {

        /**
         * Query call controller.
         */
        QUERY,
        /**
         * Entry call controller.
         */
        ENTRY,
        /**
         * Asset call controller.
         */
        ASSET,
        /**
         * Sync call controller.
         */
        SYNC,
        /**
         * Contenttypes call controller.
         */
        CONTENTTYPES,
        /**
         * Assetlibrary call controller.
         */
        ASSETLIBRARY
    }


    /**
     * The constant ErrorMessage_JsonNotProper.
     */
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public final static String ErrorMessage_JsonNotProper 			 = "Please provide valid JSON.";
    /**
     * The constant ErrorMessage_StackApiKeyIsNull.
     */
    public final static String ErrorMessage_StackApiKeyIsNull        = "Stack api key can not be null.";
    /**
     * The constant ErrorMessage_FormName.
     */
    public final static String ErrorMessage_FormName 				 = "Please set contentType name.";
    /**
     * The constant ErrorMessage_Stack_AccessToken_IsNull.
     */
    public final static String ErrorMessage_Stack_AccessToken_IsNull = "Access token can not be null.";
    /**
     * The constant ErrorMessage_Stack_Environment_IsNull.
     */
    public final static String ErrorMessage_Stack_Environment_IsNull = "Environment can not be null.";
    /**
     * The constant ErrorMessage_VolleyNoConnectionError.
     */
    public final static String ErrorMessage_VolleyNoConnectionError  = "Connection error";
    /**
     * The constant ErrorMessage_VolleyAuthFailureError.
     */
    public final static String ErrorMessage_VolleyAuthFailureError 	 = "Authentication Not present.";
    /**
     * The constant ErrorMessage_VolleyParseError.
     */
    public final static String ErrorMessage_VolleyParseError 		 = "Parsing Error.";
    /**
     * The constant ErrorMessage_VolleyServerError.
     */
    public final static String ErrorMessage_VolleyServerError 		 = "Server interaction went wrong, Please try again.";
    /**
     * The constant ErrorMessage_Default.
     */
    public final static String ErrorMessage_Default 				 = "Oops! Something went wrong. Please try again.";
    /**
     * The constant ErrorMessage_NoNetwork.
     */
    public final static String ErrorMessage_NoNetwork 				 = "Network not available.";
    /**
     * The constant ErrorMessage_CalledDefaultMethod.
     */
    public final static String ErrorMessage_CalledDefaultMethod      = "You must called Contentstack.stack() first";
    /**
     * The constant ErrorMessage_QueryFilterException.
     */
    public final static String ErrorMessage_QueryFilterException 	 = "Please provide valid params.";
}

